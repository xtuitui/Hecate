package org.xiaotuitui.testframework;

import org.xiaotuitui.framework.interfaces.BaseCtrl;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	private static final String SUCCESS = BaseCtrl.SUCCESS;
	
	private static final String ERROR = BaseCtrl.ERROR;

	private static final String RESULT = BaseCtrl.RESULT;
    
	private static final String DATA = BaseCtrl.DATA;
    
	public static String ajaxSuccess(){
		return ajaxSuccessJsonObject().toJSONString();
	}
	
	private static JSONObject ajaxSuccessJsonObject(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(RESULT, SUCCESS);
		return jsonObject;
	}
	
	public static String ajaxErrorData(Object object){
		return ajaxErrorDataJsonObject(object).toJSONString();
	}
	
	private static JSONObject ajaxErrorDataJsonObject(Object object){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(RESULT, ERROR);
		jsonObject.put(DATA, object);
		return jsonObject;
	}

}