package org.xiaotuitui.testframework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.persistence.Column;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.ext.db2.Db2DataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.InputSource;

public class DBUnitUtil {
	
	private static String driver;
	
	private static String url;
	
	private static String username;
	
	private static String password;
	
	private static Connection connection;
	
	private static IDatabaseConnection databaseConnection;
	
	private static final String DATA_SET = "dataset";
	
	private static final String XML_ENCODING = "UTF-8";
	
	/**
	 * load the static resources
	 * */
	static{
		Properties properties = new Properties();
    	InputStream is = DBUnitUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
    	try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver = properties.getProperty("jdbc.driverClassName");
		url = properties.getProperty("jdbc.url");
    	username = properties.getProperty("jdbc.username");
    	password = properties.getProperty("jdbc.password");
	}
	
    /**
     * get the dbunit connection
     * 
     * @return databaseConnection
     * @throws DatabaseUnitException
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * */
	public static void registerDBUnitConnection() throws ClassNotFoundException, SQLException, DatabaseUnitException{
		if(databaseConnection==null){
			if(connection==null){
				Class.forName(driver);
				connection = DriverManager.getConnection(url, username, password);
			}
			databaseConnection = getDBUnitConnection(connection);
		}
	}
	
	public static IDatabaseConnection getDBUnitConnection() throws DatabaseUnitException, ClassNotFoundException, SQLException{
		if(databaseConnection==null){
			registerDBUnitConnection();
		}
		return databaseConnection;
	}

	public static IDatabaseConnection getDBUnitConnection(Connection connection) throws DatabaseUnitException, ClassNotFoundException, SQLException{
        IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
        if(driver.toLowerCase().contains("sqlserver")){
        	databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
        }else if(driver.toLowerCase().contains("oracle")){
        	databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
        }else if(driver.toLowerCase().contains("db2")){
        	databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Db2DataTypeFactory());
        }else if(driver.toLowerCase().contains("mysql")){
        	databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
        }
        return databaseConnection;
    }
    
    /**
     * get the dbunit connection by schema
     * 
     * @param schema                 sqlserver default is "dbo", if not point this param, it will include sys.trace_xe_action_map and sys.trace_xe_event_map, 
     *                                                 this two table is system table, we do not want backup it.
     * @return databaseConnection
     * @throws DatabaseUnitException 
     * */
    public static IDatabaseConnection getDbunitConnectionByMsSqlAndSchema(String schema) throws DatabaseUnitException, ClassNotFoundException, SQLException{
    	if(connection==null){
    		Class.forName(driver);
    		connection = DriverManager.getConnection(url, username, password);
    	}
        IDatabaseConnection databaseConnection = new DatabaseConnection(connection, schema);
        databaseConnection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());
        return databaseConnection;
    }
    /**
     * get the dbunit dataSet by xml file name
     * 
     * @param name    xml file name
     * @return dataSet
     * @throws DataSetException 
     * */
    public static IDataSet getDbunitDataSetByXmlFileName(String filePath) throws DataSetException{
    	return new FlatXmlDataSet(new FlatXmlProducer(new InputSource(DBUnitUtil.class.getClassLoader().getResourceAsStream(filePath))));
    }
    
    /**
     * get the dbunit dataSet by entity
     * 
     * @param Object    object
     * @return dataSet
     * @throws Exception 
     * */
    public static IDataSet getDbunitDataSetByEntity(Object object, String tableName) throws Exception {
    	return getDbunitDataSetByEntityList(Arrays.asList(new Object[]{object}), tableName);
	}
    
    /**
     * get the dbunit dataSet by list
     * 
     * @param list    list
     * @return dataSet
     * @throws Exception 
     * */
    public static IDataSet getDbunitDataSetByEntityList(List<?> list, String tableName) throws Exception {
    	Document document = DocumentHelper.createDocument();
    	document.setXMLEncoding(XML_ENCODING);
    	Element root = document.addElement(DATA_SET);
    	for(Object object:list){
    		Element element = root.addElement(tableName);
    		Field[] fields = object.getClass().getDeclaredFields();
    		buildElementField(element, fields, object);
    	}
    	return new FlatXmlDataSet(new FlatXmlProducer(new InputSource(new StringReader(document.asXML()))));
	}
    
    private static void buildElementField(Element element, Field[] fields, Object object) throws Exception {
    	for(Field field:fields){
    		if(skipCurrentField(field)){
    			continue;
    		}
    		Column column = field.getAnnotation(Column.class);
			String columnName = column.name();
			Object value = getReflectValue(field, object);
			if(value==null){
				continue;
			}else{
				if(value instanceof java.util.Date || value instanceof java.sql.Date){
					element.addAttribute(columnName, EntityUtil.simpleDateFormat.format(value));
				}else{
					element.addAttribute(columnName, value.toString());
				}
			}
		}
	}

    private static boolean skipCurrentField(Field field){
    	if(Modifier.isStatic(field.getModifiers())){
			return true;
		}
		Column column = field.getAnnotation(Column.class);
		if(column==null){
			return true;
		}
		return false;
    }
    
    private static Object getReflectValue(Field field, Object object) throws Exception{
    	String fieldName = field.getName();
		String firstCharacter = fieldName.substring(0, 1);
		Method method = object.getClass().getMethod(EntityUtil.ENTITY_GET + fieldName.replaceFirst(firstCharacter, firstCharacter.toUpperCase()), new Class[]{});
		return method.invoke(object);
    }
    
	/**
     * backUp all the table by dbname to a xml file
     * 
     * @param schema                 sqlserver default is "dbo", if not point this param, it will include sys.trace_xe_action_map and sys.trace_xe_event_map, 
     *                                                 this two table is system table, we do not want backup it.
     * @param filePath    the outPut source xml path
     * @throws DatabaseUnitException 
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * */
    public static void backUpAllTableToXmlFileByMsSqlAndSchema(String filePath, String schema) throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException{
        IDatabaseConnection databaseConnection = getDbunitConnectionByMsSqlAndSchema(schema);
        IDataSet dataSet = databaseConnection.createDataSet();
        Writer writer = new FileWriter(filePath);
        FlatXmlDataSet.write(dataSet, writer);
    }
    /**
     * backUp all the table by dbname to a xml file
     * 
     * @param name    get dbname to connect the database in dbunit
     * @param filePath    the outPut source xml path
     * @throws DatabaseUnitException 
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * */
    public static void backUpAllTableToXmlFile(String filePath) throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException{
        IDatabaseConnection databaseConnection = getDBUnitConnection();
        IDataSet dataSet = databaseConnection.createDataSet();
        Writer writer = new FileWriter(filePath);
        FlatXmlDataSet.write(dataSet, writer);
    }
    
    /**
     * backUp custom table by dbname to a xml file
     * 
     * @param tableNames    table name which you want to backUp
     * @param filePath    the outPut source xml path
     * @throws DatabaseUnitException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * */
    public static void backUpCustomTableToXmlFile(String[] tableNames, String filePath) throws DatabaseUnitException, IOException, SQLException, ClassNotFoundException{
        IDatabaseConnection databaseConnection = getDBUnitConnection();
        QueryDataSet queryDataSet = new QueryDataSet(databaseConnection);
        for(String tableName:tableNames){
            queryDataSet.addTable(tableName);
        }
        Writer writer = new FileWriter(filePath);
        FlatXmlDataSet.write(queryDataSet, writer);
    }
    
    /**
     * resume the table data by xml file
     * 
     * @param filePath    the input source xml path
     * @throws DatabaseUnitException 
     * @throws FileNotFoundException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException 
     * */
    public static void resumeTableByXmlFile(String filePath) throws DatabaseUnitException, SQLException, IOException, ClassNotFoundException {
        IDatabaseConnection databaseConnection = getDBUnitConnection();
        IDataSet dataSet = new FlatXmlDataSet(new FlatXmlProducer(new InputSource(new FileInputStream(filePath))));
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
    }
    
    /**
     * Operation
     * Delete All
     */
    public static void deleteAll(IDataSet dataSet){
    	try {
			DatabaseOperation.DELETE_ALL.execute(databaseConnection, dataSet);
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Operation
     * Clean Insert
     */
    public static void cleanInsert(IDataSet dataSet){
    	try {
			DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Include Column
     * @return
     * @throws DataSetException 
     */
    public static ITable includeColumnTable(ITable table, org.dbunit.dataset.Column[] additionalColumnInfo) throws DataSetException{
		return DefaultColumnFilter.includedColumnsTable(table, additionalColumnInfo);
	}
    
    public static void closeConnection(){
    	closeDBunitDatabaseConnection(databaseConnection);
    	databaseConnection = null;
    	closeConnection(connection);
    	connection = null;
    }
    
    private static void closeDBunitDatabaseConnection(IDatabaseConnection databaseConnection){
    	if(databaseConnection!=null){
    		try {
				databaseConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private static void closeConnection(Connection connection){
    	if(connection!=null){
    		try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
}