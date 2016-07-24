package org.xiaotuitui.testframework;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityUtil {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	public static final String ENTITY_GET = "get";
	
	public static final String ENTITY_SET = "set";
	
    /**
     * create a simple entity
     * 
     * @param clz    the Class of entity
     * @param seq    the sequence of entity
     * @return object
     * */
    public static Object createEntity(Class<?> clz , int seq){
        Object object = null;
        try {
            object = clz.newInstance();
            Field[] fields = clz.getDeclaredFields();
            setField(object, fields, seq);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
    /**
     * create a simple entity List
     * 
     * @param clz    the Class of entity
     * @param number    the size of entityList
     * @return list
     * */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List createEntityList(Class<?> clz,int number){
        List list = new ArrayList();
        for(int i=1;i<=number;i++){
            list.add(createEntity(clz,i));
        }
        return list;
    }
    /**
     * set the private field for entity
     * 
     * @param object    target entity
     * @param fields    fields form target entity
     * @param seq      the sequence of entity
     * */
    private static void setField(Object object, Field[] fields, int seq) throws IllegalArgumentException, IllegalAccessException{
        for(Field field:fields){
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            field.setAccessible(true);
            if(field.getType().equals(String.class)){
                field.set(object, field.getName()+seq);
            }else if(field.getType().equals(Date.class)){
                field.set(object, new Date());
            }else if(field.getType().equals(Integer.class)){
                field.set(object, seq);
            }else if(field.getType().equals(Long.class)){
            	field.set(object, Long.valueOf(seq));
            }else if(field.getType().equals(Double.class)){
                field.set(object, seq + 0.1 * seq);
            }else if(field.getType().equals(Boolean.class)){
                field.set(object, seq % 2==0);
            }
        }
    }
    /**
     * to return is the two object is equals
     * 
     * @param obj1    
     * @param obj2    
     * @return boolean
     * */
    public static boolean objEqualsObj(Object obj1,Object obj2) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        if(obj1==obj2){
            return true;
        }
        if(obj1.getClass()!=obj2.getClass()){
            return false;
        }
        Field[] fields = obj1.getClass().getDeclaredFields();
        for(Field f:fields){
            f.setAccessible(true);
            String name = f.getName();
            Field f2 = obj2.getClass().getDeclaredField(name);
            f2.setAccessible(true);
            Object val1 = f.get(obj1);
            Object val2 = f2.get(obj2);
            if(val1==null||val2==null){
            	if(val1==null&&val2==null){
            		continue;
            	}else{
            		return false;
            	}
            }
            if(val1 instanceof java.lang.String && val2 instanceof java.lang.String){
                val1 = ((String)val1).trim();
                val2 = ((String)val2).trim();
            }
            if(val1 instanceof java.util.Date && val2 instanceof java.util.Date){
            	if(!simpleDateFormat.format(val1).equals(simpleDateFormat.format(val2))){
            		return false;
            	}
            }else if(!val1.equals(val2)){
                return false;
            }
        }
        return true;
    }
    
}