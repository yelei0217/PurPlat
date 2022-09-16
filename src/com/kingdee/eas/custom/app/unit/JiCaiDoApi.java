package com.kingdee.eas.custom.app.unit;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
 

public class JiCaiDoApi {

	static String url = "https://admin-dev.wellekq.com/api/meiwei-resource/test/suppliersInfo?access_token=noCiRLztStKqJNeUwMsJdoI76zkzEfan&origin=EAS";
	
	
	

	public static String sendMessageToBDPost(String param) {
		Map<String, String> paramHeader = new HashMap<String, String>();
		//paramHeader.put("app-key", InterfaceResource.db_center_app_key);
		//paramHeader.put("app-secret",InterfaceResource.db_center_app_secret); 
		paramHeader.put("Content-Type", "Application/json");
		paramHeader.put("Params", "access_token=noCiRLztStKqJNeUwMsJdoI76zkzEfan&origin=EAS"); 
		
		Map<String, String> paramBody= new HashMap<String, String>(); 
		paramBody.put("Params", "access_token=noCiRLztStKqJNeUwMsJdoI76zkzEfan&origin=EAS");
		 
		HttpClient httpClient;
		String result = "";
		try { 
			httpClient = new HTTPSTrustClient().init();
			result = HTTPSClientUtil.doPost(  httpClient, url,  paramHeader,  paramBody) ;
			
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 
			
		} 
		 return result;
	}
}
