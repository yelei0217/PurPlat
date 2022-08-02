package com.kingdee.eas.custom.app.dao;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.PurOrderDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;

public class SaleOrderSupport {

	
	public static String verifyJsonStr(Context ctx,String jsonStr){
		String result = null;
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.B2B_GZ_LZ_SO;
			String msgId = "";
			String busCode ="";
			String reqTime ="";
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
			JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
			JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
			JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
			Gson gson = new Gson();
			JsonElement modelJE = returnData.get("data"); // 请求参数data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {

				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
				
			// 记录日志
			IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
			PurOrderDTO m = gson.fromJson(modelJE, PurOrderDTO.class);
			// 判断msgId 是否存在
			if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
				
				
			}else
				result = PurPlatSyncEnum.EXISTS_BILL.getAlias();
				
			}else
				result = PurPlatSyncEnum.FIELD_NULL.getAlias();
		}else
			result = PurPlatSyncEnum.FIELD_NULL.getAlias();
	    
		return result;
	}
	
 
	
	
}
