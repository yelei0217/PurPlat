package com.kingdee.eas.custom.app.dao.base;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dao.ApOtherSupport;
import com.kingdee.eas.custom.app.dao.ArOtherSupport;
import com.kingdee.eas.custom.app.dao.ReceiptSupport;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;

/**
 * 
 * @author LEI.YE
 *	财务 同步基础类
 */
public class BaseFISupport {

	public static String syncBill(Context ctx,String jsonStr){
		String result = null;
		String msgId = "";
		String busCode ="";
		String reqTime ="";
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		Gson gson = new Gson();
		
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZ_CK_LZ_AP;
		
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
			JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
			JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
			JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
			
			JsonElement modelJE = returnData.get("data"); // 请求参数data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
  				baseType = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
				
				// 记录日志
//				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");
  				PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(),"", "", "");

  				BaseFIDTO m = null;
				try {
					m = gson.fromJson(modelJE, BaseFIDTO.class);
				} catch (JsonSyntaxException e) {
					purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
 					e.printStackTrace();
				}
				if(m != null){
					// 判断msgId 是否存在SaleOrderDTO
					if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
						result = judgeModel(ctx,m,busCode);
						if("".equals(result))
						{
							if(busCode.contains("_AP"))
								ApOtherSupport.doSaveBill(ctx,m,busCode);
							else if(busCode.contains("_AR"))
								ArOtherSupport.doSaveBill(ctx, m,busCode);
							else if(busCode.contains("_R"))
								ReceiptSupport.doInsertBill(ctx,m,busCode);
	 						 purPlatMenu = PurPlatSyncEnum.SUCCESS;
						}else
							purPlatMenu = PurPlatSyncEnum.EXCEPTION_SERVER;
					}else
						purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
				}else 
					purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
			
			}else
 			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		}else
			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		
		respondDTO.setCode(purPlatMenu.getValue());
		respondDTO.setMsgId(msgId);
		if(purPlatMenu==PurPlatSyncEnum.EXCEPTION_SERVER)
		respondDTO.setMsg(result);
		else
			respondDTO.setMsg(purPlatMenu.getAlias());
		return gson.toJson(respondDTO);
	}

	/**
	 * 校验 实体是否正确
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,BaseFIDTO m,String busCode ){
		 String result = "";
		 //组织是否存在
		 if(m.getFcompanyorgunitid() != null && !"".equals(m.getFcompanyorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFcompanyorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"财务组织不存在,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		 }else{
			 result = result +"财务组织不能为空,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B单号存在是否需要判断
			 result = result +"单价编号不能为空,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"业务日期不能为空,";
		
		 if(busCode.contains("_AP") || busCode.contains("_P") )	{
			 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
				 result = result +"供应商不能为空,";
				 else{
					if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
						if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFcompanyorgunitid()  , m.getFsupplierid()))
							 result = result +"供应商未分配当前组织,";
						}else
							 result = result +"供应商不存在,";
				  }	
		 }
		 
		 if(busCode.contains("_AR") || busCode.contains("_R") )	{
			 if(m.getFcustomerid() == null || "".equals(m.getFcustomerid()))
				 result = result +"客户不能为空,";
			 else{
				if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFcustomerid())){
					if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFcompanyorgunitid(), m.getFcustomerid()))
						 result = result +"客户未分配当前组织,";
					}else
						 result = result +"客户不存在,";
			  }
		 }
		 
			
			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
				 result = result +"价税合计、金额、税额 都不允许为空,";
			 else{
				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
					 result = result +"价税合计等于金额加税额的合计,";
		  }
		 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(BaseFIDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"第"+j+1+"行物料ID不能为空,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFcompanyorgunitid()  , dvo.getFmaterialid()))
								 result = result +"第"+j+1+"物料未分配当前组织,";
						 }else
							 result = result +"第"+j+1+"行 物料ID不存在,";
					 }
				 
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"第"+j+1+"行计量单位不能为空,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"第"+j+1+"行 计量单位"+dvo.getFunitid()+"不存在,";
					 }
					 
					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
						 result = result +"第"+j+1+"行基本计量单位不能为空,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
							 result = result +"第"+j+1+"行 基本计量单位"+dvo.getFbaseunitid()+"不存在,";
					 }
					 
					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
						 result = result +"第"+j+1+"行 订货数量、基本数量不能为空,";
					}
					 
					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
						 result = result +"第"+j+1+"行 单价、实际单价、含税单价、实际含税单价 不能为空,";
					}
					
					if(dvo.getFtaxrate() == null){ 
						 result = result +"第"+j+1+"行 税率不能为空,";
					}
					if(dvo.getFtax() == null){ 
						 result = result +"第"+j+1+"行 税额不能为空,";
					}
					 
					if(dvo.getFamount()== null){ 
						 result = result +"第"+j+1+"行 金额不能为空,";
					}
					
					if(dvo.getFtaxamount() == null){ 
						 result = result +"第"+j+1+"行 价税合计不能为空,";
					}
					
//					  if(dvo.getFdeliverydate() == null){ 
//							 result = result +"第"+j+1+"行 交货日期不能为空,";
//						}
					  
//				   if(busCode.contains("_SO") || busCode.contains("_SS") )	{
//						if(dvo.getFsenddate() == null){ 
//							 result = result +"第"+j+1+"行 发货日期不能为空,";
//						} 
//				   }
 
				 }
			} else 
				result = result +"至少有一条明细行的数据,";
			 
		 return result;
	}
	
	
}
