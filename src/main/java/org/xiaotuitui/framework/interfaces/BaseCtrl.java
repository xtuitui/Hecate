package org.xiaotuitui.framework.interfaces;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BaseCtrl {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String RESULT = "result";
    public static final String DATA = "data";
    public static final String CONTENT_TYPE = "application/json";
    
    protected void ajaxJson(String jsonString, HttpServletResponse response) {
		ajax(jsonString, CONTENT_TYPE, response);
	}
    
    protected void ajaxSuccess(HttpServletResponse response){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put(RESULT, SUCCESS);
    	ajax(jsonObject.toString(), CONTENT_TYPE, response);
    }
    
    protected void ajaxError(HttpServletResponse response){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put(RESULT, ERROR);
    	ajax(jsonObject.toString(), CONTENT_TYPE, response);
    }
    
    protected void ajaxSuccessData(HttpServletResponse response, Object object){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put(RESULT, SUCCESS);
    	jsonObject.put(DATA, object);
    	ajax(jsonObject.toString(), CONTENT_TYPE, response);
    }
    
    protected void ajaxErrorData(HttpServletResponse response, Object object){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put(RESULT, ERROR);
    	jsonObject.put(DATA, object);
    	ajax(jsonObject.toString(), CONTENT_TYPE, response);
    }
    
    protected void ajaxSuccessDataArray(HttpServletResponse response, Object... objectArray){
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put(RESULT, SUCCESS);
    	JSONArray jsonArray = new JSONArray();
    	for(Object object:objectArray){
    		jsonArray.add(object);
    	}
    	jsonObject.put(DATA, jsonArray);
    	ajax(jsonObject.toString(), CONTENT_TYPE, response);
    }
    
    private void ajax(String content, String type, HttpServletResponse response) {
    	PrintWriter out = null;
		try {
			response.setContentType(type + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			out = response.getWriter();
			out.write(content);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			out.close();
		}
	}
    
    @ExceptionHandler(value = {Exception.class})
	protected void handleException(HttpServletResponse response, Exception exception){
    	exception.printStackTrace();
    	ajaxError(response);
    }
    
}