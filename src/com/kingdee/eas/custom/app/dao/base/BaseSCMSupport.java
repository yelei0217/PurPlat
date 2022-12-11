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
 *	��Ӧ�� ͬ��������
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
					JsonElement msgIdJE = returnData.get("msgId"); // ������ϢId
					JsonElement busCodeJE = returnData.get("busCode"); // ҵ����������
					JsonElement reqTimeJE = returnData.get("reqTime"); // ������ϢId
				
					JsonElement modelJE = returnData.get("data"); // �������data
					if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
							busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
							reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
						msgId = msgIdJE.getAsString() ;
						busCode = busCodeJE.getAsString() ;
						reqTime = reqTimeJE.getAsString() ;
						baseType = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
						// ��¼��־
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
								// �ж�msgId �Ƿ����SaleOrderDTO
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
									// �ж�msgId �Ƿ����SaleOrderDTO
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
	 * У�� ʵ���Ƿ���ȷ
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,BaseSCMDTO m,String busCode ){
		 String result = "";
		 //��֯�Ƿ����
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"�����֯������,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		 }else{
			 result = result +"�����֯����Ϊ��,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B���Ŵ����Ƿ���Ҫ�ж�
			 result = result +"���۱�Ų���Ϊ��,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"ҵ�����ڲ���Ϊ��,";
		
		 //�����������
		 if("SK_MZ_OPI".equals(busCode)){
			 if(m.getFbiztype()== null || "".equals(m.getFbiztype()))
				 result = result +"��Ҫָ���������,";
			 else{
				 if("3".equals(m.getFbiztype())){
					 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
						 result = result +"��Ӧ�̲���Ϊ��,";
						 else{
							if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
								if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
									 result = result +"��Ӧ��δ���䵱ǰ��֯,";
								}else
									 result = result +"��Ӧ�̲�����,";
						  }	
				 }
			 }
		 }
		 
		 if(busCode.contains("_PO") || busCode.contains("_PI") )	{
			 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
				 result = result +"��Ӧ�̲���Ϊ��,";
				 else{
					if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
						if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
							 result = result +"��Ӧ��δ���䵱ǰ��֯,";
						}else
							 result = result +"��Ӧ�̲�����,";
				  }	
		 }
		 
		 if(busCode.contains("_SO") || busCode.contains("_SS") )	{
			 if(m.getFcustomerid() == null || "".equals(m.getFcustomerid()))
				 result = result +"�ͻ�����Ϊ��,";
			 else{
				if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFcustomerid())){
					if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFstorageorgunitid(), m.getFcustomerid()))
						 result = result +"�ͻ�δ���䵱ǰ��֯,";
					}else
						 result = result +"�ͻ�������,";
			  }
		 }
			
			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
				 result = result +"��˰�ϼơ���˰�� ��������Ϊ��,";
			 else{
				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
					 result = result +"��˰�ϼƵ��ڽ���˰��ĺϼ�,";
		  }
			 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(BaseSCMDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"��"+j+1+"������ID����Ϊ��,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
								 result = result +"��"+j+1+"����δ���䵱ǰ��֯,";
						 }else
							 result = result +"��"+j+1+"�� ����ID������,";
					 }
					 
					 if("CGZ_U_MZ_SO".equals(busCode) || busCode.contains("SS")|| busCode.contains("PI")){
						 if(dvo.getFwarehouseid() ==null || "".equals(dvo.getFwarehouseid()))
							 result = result +"��"+j+1+"�вֿ�ID����Ϊ��,";
						 else{
	 						if(!PurPlatUtil.judgeExists(ctx, "Warehouse",m.getFstorageorgunitid(), dvo.getFwarehouseid()))
								 result = result +"��"+j+1+"�вֿ�ID������,";
							else
								try {
									if(!PurPlatUtil.getWareIsStarted(ctx,m.getFstorageorgunitid(), dvo.getFwarehouseid()))
										 result = result +"��"+j+1+"�вֿ�δ������ʼ��,";
								} catch (EASBizException e) {
									e.printStackTrace();
								}
						 } 
					 }
					 if(PurPlatUtil.process_BusCode_List.contains(busCode)){
						 if(dvo.getFlot() ==null || "".equals(dvo.getFlot()))
							 result = result +"��"+j+1+"�����β���Ϊ��,";
						 else{
	 						if(PurPlatUtil.judgeLotExists(ctx, busCode, m.getFstorageorgunitid(), dvo.getFmaterialid(), dvo.getFlot()))
								 result = result +"��"+j+1+"������"+ dvo.getFlot()+"�Ѵ���,";
						 } 
						 
						 if(dvo.getFmfg()==null || "".equals(dvo.getFmfg()))
							 result = result +"��"+j+1+"���������ڲ���Ϊ��,";
						  else{
							  if(!PurPlatUtil.judgeDateFormat(dvo.getFmfg()))
								  result = result +"��"+j+1+"���������ڸ�ʽ����,";
						  } 
						 
						 if(dvo.getFexp()==null || "".equals(dvo.getFexp()))  
							 result = result +"��"+j+1+"�е������ڲ���Ϊ��,";
						  else{
							  if(!PurPlatUtil.judgeDateFormat(dvo.getFexp()))
								  result = result +"��"+j+1+"�е������ڸ�ʽ����,";
						  }
					 } 
					 
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"��"+j+1+"�м�����λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"��"+j+1+"�� ������λ"+dvo.getFunitid()+"������,";
					 }
					 
					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
						 result = result +"��"+j+1+"�л���������λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
							 result = result +"��"+j+1+"�� ����������λ"+dvo.getFbaseunitid()+"������,";
					 }
					 
					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
						 result = result +"��"+j+1+"�� ����������������������Ϊ��,";
					}
					 
					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
						 result = result +"��"+j+1+"�� ���ۡ�ʵ�ʵ��ۡ���˰���ۡ�ʵ�ʺ�˰���� ����Ϊ��,";
					}
					
					if(dvo.getFtaxrate() == null){ 
						 result = result +"��"+j+1+"�� ˰�ʲ���Ϊ��,";
					}
					if(dvo.getFtax() == null){ 
						 result = result +"��"+j+1+"�� ˰���Ϊ��,";
					}
					 
					if(dvo.getFamount()== null){ 
						 result = result +"��"+j+1+"�� ����Ϊ��,";
					}
					
					if(dvo.getFtaxamount() == null){ 
						 result = result +"��"+j+1+"�� ��˰�ϼƲ���Ϊ��,";
					}
					
//					  if(dvo.getFdeliverydate() == null){ 
//							 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
//						}
					  
//				   if(busCode.contains("_SO") || busCode.contains("_SS") )	{
//						if(dvo.getFsenddate() == null){ 
//							 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
//						} 
//				   }
 
				 }
			} else 
				result = result +"������һ����ϸ�е�����,";
			 
		 return result;
	}
	
	
}
