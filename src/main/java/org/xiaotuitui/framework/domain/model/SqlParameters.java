package org.xiaotuitui.framework.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.xiaotuitui.framework.exception.SysException;

public class SqlParameters {

	private StringBuilder sqlStringBuilder;
	
	private Map<String, Object> parameters;
	
	public SqlParameters () {
		sqlStringBuilder = new StringBuilder("");
		parameters = new HashMap<String, Object>();
	}
	
	public SqlParameters (StringBuilder sqlStringBuilder, Map<String, Object> parameters) {
		this.sqlStringBuilder = sqlStringBuilder;
		this.parameters = parameters;
	}
	
	public SqlParameters (String sqlString, Map<String, Object> parameters) {
		this.sqlStringBuilder = new StringBuilder(sqlString);
		this.parameters = parameters;
	}
	
	public SqlParameters (StringBuilder sqlStringBuilder, String[] keyArray, Object[] valueArray) {
		this.sqlStringBuilder = sqlStringBuilder;
		createParameter(keyArray, valueArray);
	}
	
	public SqlParameters (String sqlString, String[] keyArray, Object[] valueArray) {
		this.sqlStringBuilder = new StringBuilder(sqlString);
		createParameter(keyArray, valueArray);
	}

	private void createParameter(String[] keyArray, Object[] valueArray) {
		if(keyArray==null||valueArray==null||keyArray.length!=valueArray.length){
			throw new SysException("Please give correct key and value array!");
		}
		this.parameters = new HashMap<String, Object>();
		int index = 0;
		for(String key:keyArray){
			parameters.put(key, valueArray[index++]);
		}
	}

	public StringBuilder getSqlStringBuilder() {
		return sqlStringBuilder;
	}

	public void setSqlStringBuilder(StringBuilder sqlStringBuilder) {
		this.sqlStringBuilder = sqlStringBuilder;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
}