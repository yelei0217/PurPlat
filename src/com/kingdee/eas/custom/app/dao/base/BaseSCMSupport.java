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
import com.kingdee.eas.custom.app.dao.OtherPurInSupport;
import com.kingdee.eas.custom.app.dao.OtherSaleIssSupport;
import com.kingdee.eas.custom.app.dao.PurInWarehsSupport;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
import com.kingdee.eas.custom.app.dao.SaleIssueSupport;
import com.kingdee.eas.custom.app.dao.SaleOrderSupport;
import com.kingdee.eas.custom.app.dao.VMISaleOrderSupport;
import com.kingdee.eas.custom.app.dto.PurInDTO;
import com.kingdee.eas.custom.app.dto.PurOrderDTO;
import com.kingdee.eas.custom.app.dto.SaleIssDTO;
import com.kingdee.eas.custom.app.dto.SaleOrderDTO;
import com.kingdee.eas.custom.app.dto.SaleOrderDetailDTO;
import com.kingdee.eas.custom.app.dto.VMISaleOrderDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;

/***
 * 
 * @author LEI.YE
 *	供应链 同步基础类
 */
public class BaseSCMSupport {
	public static String syncBill(Context ctx,String jsonStr){
		String result = null;
		Gson gson = new Gson();
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		String msgId = "";
		String busCode ="";
		String reqTime =""; 
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZ_LZ_SS; 
			JsonObject returnData =null;
			try {
				returnData = new JsonParser().parse(jsonStr).getAsJsonObject();
				if(returnData !=null){ 
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
					//	IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");
						PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), "", "", "");
						if("VMI_U_MZ_SO".equals(busCode)){
							VMISaleOrderDTO m =null;
							try {
								m = gson.fromJson(modelJE, VMISaleOrderDTO.class);
							} catch (JsonSyntaxException e) {
								purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
			 					e.printStackTrace();
							}
							if(m!=null){
								// 判断msgId 是否存在SaleOrderDTO
								if(!PurPlatUtil.judgeMsgIdExists(ctx, baseType.getValue(),busCode, m.getId())){
									result = VMISaleOrderSupport.judgeModel(ctx,m,busCode);
									VMISaleOrderSupport.doInsertBill(ctx, m,baseType.getValue(), busCode,jsonStr);
								}else
									purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
							}else
								purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
						}else{
							  BaseSCMDTO m =null;
								try {
									m = gson.fromJson(modelJE, BaseSCMDTO.class);
								} catch (JsonSyntaxException e) {
									purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
				 					e.printStackTrace();
								}
								if(m!=null){
									// 判断msgId 是否存在SaleOrderDTO
									if(!PurPlatUtil.judgeMsgIdExists(ctx, baseType.getValue(),busCode, m.getId())){
										result = judgeModel(ctx,m,busCode);
										if("".equals(result))
										{
											if(busCode.contains("_SO"))
												SaleOrderSupport.doInsertBill(ctx,m,baseType.getValue(),busCode);
											else if(busCode.contains("_PO"))
												PurOrderSupport.doInsertBill(ctx, m,baseType.getValue(),busCode);
											else if(busCode.contains("_PI"))
												PurInWarehsSupport.doSaveBill(ctx,m,baseType.getValue(),busCode);
											else if(busCode.contains("_SS"))
												SaleIssueSupport.doSaveBill(ctx,m,baseType.getValue(),busCode);
											else if("SK_MZ_OPI".equals(busCode))
												OtherPurInSupport.doSaveBill(ctx,m,baseType.getValue(),busCode);
											else if("SK_MZ_OSS".equals(busCode))
												OtherSaleIssSupport.doSaveBill(ctx,m,baseType.getValue(),busCode);
				 							 purPlatMenu = PurPlatSyncEnum.SUCCESS;
										}else
											purPlatMenu = PurPlatSyncEnum.EXCEPTION_SERVER;
									}else
										purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
								}else
									purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
						}
						 
					}else
						purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
				}
				
			} catch (JsonSyntaxException e1) {
				purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
				e1.printStackTrace();
			}
			   
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
	private static String judgeModel(Context ctx,BaseSCMDTO m,String busCode ){
		 String result = "";
		 //组织是否存在
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"库存组织不存在,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		 }else{
			 result = result +"库存组织不能为空,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B单号存在是否需要判断
			 result = result +"单价编号不能为空,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"业务日期不能为空,";
		
		 //其他入库类型
		 if("SK_MZ_OPI".equals(busCode)){
			 if(m.getFbiztype()== null || "".equals(m.getFbiztype()))
				 result = result +"需要指定入库类型,";
			 else{
				 if("3".equals(m.getFbiztype())){
					 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
						 result = result +"供应商不能为空,";
						 else{
							if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
								if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
									 result = result +"供应商未分配当前组织,";
								}else
									 result = result +"供应商不存在,";
						  }	
				 }
			 }
		 }
		 
		 if(busCode.contains("_PO") || busCode.contains("_PI") )	{
			 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
				 result = result +"供应商不能为空,";
				 else{
					if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
						if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
							 result = result +"供应商未分配当前组织,";
						}else
							 result = result +"供应商不存在,";
				  }	
		 }
		 
		 if(busCode.contains("_SO") || busCode.contains("_SS") )	{
			 if(m.getFcustomerid() == null || "".equals(m.getFcustomerid()))
				 result = result +"客户不能为空,";
			 else{
				if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFcustomerid())){
					if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFstorageorgunitid(), m.getFcustomerid()))
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
				 for(BaseSCMDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"第"+j+1+"行物料ID不能为空,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
								 result = result +"第"+j+1+"物料未分配当前组织,";
						 }else
							 result = result +"第"+j+1+"行 物料ID不存在,";
					 }
					 
					 if("CGZ_U_MZ_SO".equals(busCode) || busCode.contains("SS")|| busCode.contains("PI")){
						 if(dvo.getFwarehouseid() ==null || "".equals(dvo.getFwarehouseid()))
							 result = result +"第"+j+1+"行仓库ID不能为空,";
						 else{
	 						if(!PurPlatUtil.judgeExists(ctx, "Warehouse",m.getFstorageorgunitid(), dvo.getFwarehouseid()))
								 result = result +"第"+j+1+"行仓库ID不存在,";
							else
								try {
									if(!PurPlatUtil.getWareIsStarted(ctx,m.getFstorageorgunitid(), dvo.getFwarehouseid()))
										 result = result +"第"+j+1+"行仓库未结束初始化,";
								} catch (EASBizException e) {
									e.printStackTrace();
								}
						 } 
					 }
					 if(PurPlatUtil.process_BusCode_List.contains(busCode)){
						 if(dvo.getFlot() ==null || "".equals(dvo.getFlot()))
							 result = result +"第"+j+1+"行批次不能为空,";
						 else{
	 						if(PurPlatUtil.judgeLotExists(ctx, busCode, m.getFstorageorgunitid(), dvo.getFmaterialid(), dvo.getFlot()))
								 result = result +"第"+j+1+"行批次"+ dvo.getFlot()+"已存在,";
						 } 
						 
						 if(dvo.getFmfg()==null || "".equals(dvo.getFmfg()))
							 result = result +"第"+j+1+"行生产日期不能为空,";
						  else{
							  if(!PurPlatUtil.judgeDateFormat(dvo.getFmfg()))
								  result = result +"第"+j+1+"行生产日期格式错误,";
						  } 
						 
						 if(dvo.getFexp()==null || "".equals(dvo.getFexp()))  
							 result = result +"第"+j+1+"行到期日期不能为空,";
						  else{
							  if(!PurPlatUtil.judgeDateFormat(dvo.getFexp()))
								  result = result +"第"+j+1+"行到期日期格式错误,";
						  }
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
