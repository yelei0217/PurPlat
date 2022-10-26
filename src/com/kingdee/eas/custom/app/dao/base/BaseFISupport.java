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
 *	���� ͬ��������
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
		
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json ת�ɶ���
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
					// �ж�msgId �Ƿ����SaleOrderDTO
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
	 * У�� ʵ���Ƿ���ȷ
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,BaseFIDTO m,String busCode ){
		 String result = "";
		 //��֯�Ƿ����
		 if(m.getFcompanyorgunitid() != null && !"".equals(m.getFcompanyorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFcompanyorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"������֯������,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		 }else{
			 result = result +"������֯����Ϊ��,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B���Ŵ����Ƿ���Ҫ�ж�
			 result = result +"���۱�Ų���Ϊ��,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"ҵ�����ڲ���Ϊ��,";
		
		 if(busCode.contains("_AP") || busCode.contains("_P") )	{
			 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
				 result = result +"��Ӧ�̲���Ϊ��,";
				 else{
					if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
						if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFcompanyorgunitid()  , m.getFsupplierid()))
							 result = result +"��Ӧ��δ���䵱ǰ��֯,";
						}else
							 result = result +"��Ӧ�̲�����,";
				  }	
		 }
		 
		 if(busCode.contains("_AR") || busCode.contains("_R") )	{
			 if(m.getFcustomerid() == null || "".equals(m.getFcustomerid()))
				 result = result +"�ͻ�����Ϊ��,";
			 else{
				if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFcustomerid())){
					if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFcompanyorgunitid(), m.getFcustomerid()))
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
				 for(BaseFIDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"��"+j+1+"������ID����Ϊ��,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFcompanyorgunitid()  , dvo.getFmaterialid()))
								 result = result +"��"+j+1+"����δ���䵱ǰ��֯,";
						 }else
							 result = result +"��"+j+1+"�� ����ID������,";
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
