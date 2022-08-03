package com.kingdee.eas.custom.app.dao;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.PurOrderDTO;
import com.kingdee.eas.custom.app.dto.PurOrderDetailDTO;
import com.kingdee.eas.custom.app.dto.SaleOrderDTO;
import com.kingdee.eas.custom.app.dto.SaleOrderDetailDTO;
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
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json ת�ɶ���
			JsonElement msgIdJE = returnData.get("msgId"); // ������ϢId
			JsonElement busCodeJE = returnData.get("busCode"); // ҵ����������
			JsonElement reqTimeJE = returnData.get("reqTime"); // ������ϢId
			Gson gson = new Gson();
			JsonElement modelJE = returnData.get("data"); // �������data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {

				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
				
			// ��¼��־
			IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
			SaleOrderDTO m = gson.fromJson(modelJE, SaleOrderDTO.class);
			// �ж�msgId �Ƿ����SaleOrderDTO
			if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
				
				
			}else
				result = PurPlatSyncEnum.EXISTS_BILL.getAlias();
				
			}else
				result = PurPlatSyncEnum.FIELD_NULL.getAlias();
		}else
			result = PurPlatSyncEnum.FIELD_NULL.getAlias();
	    
		return result;
	}
	
	

	/**
	 * У�� ʵ���Ƿ���ȷ
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,SaleOrderDTO m ){
		 String result = "";
		 //��֯�Ƿ����
		 if(m.getFpurchaseorgunitid() != null && !"".equals(m.getFpurchaseorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFpurchaseorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"�ɹ���֯������,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
			 
		 }else{
			 result = result +"�ɹ���֯����Ϊ��,";
		 }
		 
		 if(m.getFnumber() !=null && !"".equals(m.getFnumber())){
			 // B2B���Ŵ����Ƿ���Ҫ�ж�
			 
		 }else
			 result = result +"���۱�Ų���Ϊ��,";
		 
		 
		 if(m.getFnumber() == null || "".equals(m.getFnumber()))
			 result = result +"ҵ�����ڲ���Ϊ��,";
			
		 if(m.getFordercustomerid() == null || "".equals(m.getFordercustomerid()))
			 result = result +"�ͻ�����Ϊ��,";
		 else{
			if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFordercustomerid())){
				if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFpurchaseorgunitid(), m.getFordercustomerid()))
					 result = result +"�ͻ�δ���䵱ǰ��֯,";
				}else
					 result = result +"�ͻ�������,";
		  }
			
			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
				 result = result +"��˰�ϼơ���˰�� ��������Ϊ��,";
			 else{
				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
					 result = result +"��˰�ϼƵ��ڽ���˰��ĺϼ�,";
		  }
			 
			if(m.getDetails() !=null && m.getDetails().size() < 0 ){	 
				 for(SaleOrderDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() !=null && !"".equals(dvo.getFmaterialid())){
						 result = result +"��"+j+1+"������ID����Ϊ��,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFpurchaseorgunitid()  , dvo.getFmaterialid()))
								 result = result +"��"+j+1+"����δ���䵱ǰ��֯,";
						 }else
							 result = result +"��"+j+1+"�� ����ID������,";
					 }
					 
					 if(dvo.getFunitid() !=null && !"".equals(dvo.getFunitid()) ){
						 result = result +"��"+j+1+"�м�����λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"��"+j+1+"�� ������λ"+dvo.getFunitid()+"������,";
					 }
					 
					 if(dvo.getFbaseunitid() !=null && !"".equals(dvo.getFbaseunitid()) ){
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
			 
					if(dvo.getFdeliverydate() == null){ 
						 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
					}
				 }
			} else 
				result = result +"������һ����ϸ�е�����,";
			 
		 return result;
	}
	

	/**
	 *  ���� eas��
	 * @param ctx
	 */
	public static void doInsertBill(Context ctx,SaleOrderDTO m,String busCode){
		ExecutorService pool = Executors.newFixedThreadPool(6);
	    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
		
	    StringBuffer sbr = new StringBuffer("insert into T_SD_SALEORDER (FID,FCREATORID,FCREATETIME,FMODIFIERID,FMODIFICATIONTIME,FLASTUPDATEUSERID,FLASTUPDATETIME," +
	    		" FCONTROLUNITID,FNUMBER,FBIZDATE,FHASEFFECTED,FAUDITORID,FAUDITTIME,FBASESTATUS,FBIZTYPEID,FBILLTYPEID," +
	    		" FYEAR,FPERIOD,FISINNERSALE,FORDERCUSTOMERID,FPURCHASEORGUNITID,FDELIVERYTYPEID,FTRANSLEADTIME,FCURRENCYID," +
	    		" FEXCHANGERATE,FPAYMENTTYPEID,FSETTLEMENTTYPEID,FPREPAYMENT,FPREPAYMENTRATE,FSALEORGUNITID,FSALEPERSONID,FADMINORGUNITID," +
	    		" FTOTALAMOUNT,FTOTALTAX,FTAXAMOUNT,FPRERECEIVED,FUNPRERECEIVEDAMOUNT,FSENDADDRESS,FISSYSBILL,FCONVERTMODE,FLOCALTOTALAMOUNT," +
	    		" FLOCALTOTALTAXAMOUNT,FCOMPANYORGUNITID,FISINTAX,FVERSION,FOLDSTATUS,FISCENTRALBALANCE,FISREVERSE,FBEENPAIDPREPAYMENT,FSOURCEBILLNUMBER," +
	    		" FISSQUAREBALANCE,FISMATCHEDPROMOTION,FISENTIRESINGLEDISCOUNT,FORIGINALDISCOUNTAMOUNT ) values ( ");
		
	    String sId = BOSUuid.create("C48A423A").toString();
	    String userId = PurPlatUtil.getUserIdByPersonId(ctx, m.getFcreatorid());
		String bizTypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C"; // ��ͨ����
		String billTypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//���۶���
		String paymentTypeId = "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5";//����
		
		String settlementTypeId ="jbYAAAA0YHXpayuO" ;//�Թ�����
		String currencyId ="dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC" ;//�����
		
		String deliverTYpeId = "51eb893e-0105-1000-e000-0c00c0a8123362E9EE3F"; // �ͻ���SEND
		
		int isInTax = 0;//����Ϊ0������Ϊ1
		if(busCode.contains("LZ"))
			isInTax = 1;
		 else if(busCode.contains("MZ"))
			 isInTax = 0;
			 
		//String bizDate = m.getFbizdate().
		String bizDateStr =  new SimpleDateFormat("yyyy-MM-dd").format(m.getFbizdate());
		sbr.append("'").append(sId).append("','").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'").append(userId).append("',sysdate,");
		String ctrlOrgId = PurPlatUtil.getCtrlOrgId(ctx, "PUR", m.getFpurchaseorgunitid()); //���Ƶ�Ԫ
		sbr.append("'").append(ctrlOrgId).append("','").append(m.getFnumber()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),");
		sbr.append("'").append(userId).append("',0,'").append(userId).append("',sysdate,4,'").append(bizTypeId).append("','").append(billTypeId).append("','").append(bizDateStr.substring(0, 4)).append("','").append(bizDateStr.substring(5,7)).append("',");
		sbr.append("'").append(m.getFordercustomerid()).append("','").append(m.getFpurchaseorgunitid()).append("','").append(deliverTYpeId).append("',0,'").append(currencyId).append("',1,'").append(paymentTypeId).append("','").append(settlementTypeId).append("'");
		sbr.append(",0,'").append(m.getFpurchaseorgunitid()).append("','jbYAAAAB7DOA733t','").append(m.getFadminorgunitid()).append("',").append(m.getFtotalamount()).append(",").append(m.getFtotaltax()).append(",").append(m.getFtotaltaxamount()).append(",0,0,'").append(m.getFsendaddress()).append("',0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",'").append( m.getFpurchaseorgunitid()).append("',").append(isInTax).append(",0,0,0,0,0,0,0,0,0,0 ) ");
		pe.getSqlList().add(sbr);
		
		
		for(SaleOrderDetailDTO dvo : m.getDetails()){
			String eid  = BOSUuid.create("88882A58").toString();
			StringBuffer sbr1 = new StringBuffer("insert into T_SD_SALEORDERENTRY( FID,FSEQ,FMATERIALID,FUNITID,FBASESTATUS,FASSOCIATEQTY,FBASEUNITID,FREMARK,FISPRESENT," +
					" FBASEQTY,FQTY,FPRICE,FTAXPRICE,FDISCOUNTCONDITION,FDISCOUNTTYPE,FDISCOUNT,FDISCOUNTAMOUNT,FAMOUNT,FLOCALAMOUNT,FACTUALPRICE,FACTUALTAXPRICE," +
					" FTAXRATE,FTAX,FTAXAMOUNT,FSENDDATE,FDELIVERYDATE,FSTORAGEORGUNITID,FCOMPANYORGUNITID,FSENDOVERRATE,FSENDOWINGRATE,FSENDADVANCEDAY," +
					" FSENDDEFERRALDAY,FTOTALISSUEQTY,FTOTALRETURNEDQTY,FTOTALINVOICEDQTY,FTOTALSHIPPINGQTY,FTOTALRECEIVEDAMOUNT,FPARENTID,FTOTALISSUEBASEQTY," +
					" FTOTALREBASEQTY,FTOLINVOIDBASEQTY,FTOTALSHIPBASEQTY,FTOTALUNRETURNBASEQTY,FTOTALUNISSUEBASEQTY,FTOTALUNSHIPBASEQTY,FTOTALUNISSUEQTY," +
					" FISLOCKED,FLOCKQTY,FLOCKBASEQTY,FLOCALTAX,FLOCALTAXAMOUNT,FISBYSALEORDER,FORDEREDQTY,FUNORDEREDQTY,FPREPAYMENTRATE,FPREPAYMENT,FPRERECEIVED," +
					" FUNPRERECEIVEDAMOUNT,FQUANTITYUNCTRL,FTIMEUNCTRL,FDELIVERYDATEQTY,FTOTALINVOICEDAMT,FTOTALARAMOUNT,FVERSION,FISBETWEENCOMPANYSEND,FTOTALREVERSEDQTY," +
					" FTOTALREVERSEDBASEQTY,FPLANDELIVERYQTY,FTOTALCANCELLINGSTOCKQTY,FTOTALSUPPLYSTOCKQTY,FDELIVERYCUSTOMERID,FPAYMENTCUSTOMERID,FRECEIVECUSTOMERID," +
					" FARCLOSEDSTATUS,FTOTALPRODUCTQTY,FTOTALBASEPRODUCTQTY,FTOTALUNPRODUCTQTY,FTOTALBASEUNPRODUCTQTY,FMATCHEDAMOUNT,FLOCKASSISTQTY,FCHEAPRATE," +
					" FRETURNPLANDELIVERYQTY,FRETURNPLANDELIVERYBASEQTY,FSALEORGUNITID,FBIZDATE,FPRICESOURCETYPE,FISMRPCAL,FPROMOTIONSOURCEGROUP,FPROSTORAGEORGUNITID," +
					" FSUPPLYMODE,FTOTALTRANSFERQTY,FTOTALTRANSFERBASEQTY,FTOTALUNTRANSFERQTY,FTOTALUNTRANSFERBASEQTY,CFPINPAI,CFHUOHAO,CFXINGHAO," +
					" FINVOICEREQQTY,FINVOICEREQBASEQTY,FUNINVOICEREQQTY,FUNINVOICEREQBASEQTY,FINVOICEREQAMOUNT,FINVOICEREQAMOUNTLOCAL,FUNINVOICEREQAMOUNT,FUNINVOICEREQAMOUNTLOCAL ) values (");
		
			String deliveDateStr =  new SimpleDateFormat("yyyy-MM-dd").format(dvo.getFdeliverydate());
			String sendDateStr =  new SimpleDateFormat("yyyy-MM-dd").format(dvo.getFsenddate());

			Map<String,String> mmp = PurPlatUtil.getMaterialInfoByMId(ctx, dvo.getFmaterialid());
			int isPresent = 0;
			if(dvo.getFispresent())
				isPresent = 1;
			else
				isPresent = 0;
			sbr1.append("'").append(eid).append("',").append(dvo.getFseq()).append(",'").append(dvo.getFmaterialid()).append("','").append(dvo.getFunitid()).append("',4,").append(dvo.getFqty()).append(",'").append(dvo.getFbaseunitid()).append("','").append(dvo.getFremark()).append("',").append(isPresent).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty());
			sbr1.append(",").append(dvo.getFprice()).append(",0,").append(dvo.getFactualprice()).append(",").append(dvo.getFactualtaxprice()).append(",-1,0,0,0,").append(dvo.getFamount()).append(",").append(dvo.getFamount()).append(",");
			sbr1.append(dvo.getFactualprice()).append(",").append(dvo.getFactualtaxprice()).append(",").append(dvo.getFtaxrate()).append(",").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",").append(",to_date('").append(sendDateStr).append("','yyyy-MM-dd')").append(",to_date('").append(deliveDateStr).append("','yyyy-MM-dd'),0,0,0,0,0,0,0,0,0,'").append(sId).append("',");
			sbr1.append(",0,0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty()).append(",0,0,0,").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,0,'").append(dvo.getFqty()).append("',0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,'").append(m.getFordercustomerid()).append("','").append(m.getFordercustomerid()).append("','").append(m.getFordercustomerid()).append("',");
			sbr1.append("0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,0,0,0,'").append(m.getFpurchaseorgunitid()).append("',").append(",to_date('").append(sendDateStr).append("','yyyy-MM-dd'),'");
			sbr1.append(m.getFpurchaseorgunitid()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),-1,0,0,'").append(m.getFpurchaseorgunitid()).append("',0,0,0,0,0,'").append(mmp.get("pp")).append("','").append(mmp.get("hh")).append("','").append(mmp.get("xh")).append("',0,0,");
			sbr1.append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,").append(dvo.getFtaxamount()).append(",").append(dvo.getFtaxamount()).append(" )");
			pe.getSqlList().add(sbr1);
		}
	  
		if(pe.getSqlList().size()>0)
			try {
				pe.executeUpdate(ctx); 
				pool.shutdown(); 
			} catch (EASBizException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			} catch (BOSException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			}	
		     pool.shutdown(); 
		 
	}
	
}
