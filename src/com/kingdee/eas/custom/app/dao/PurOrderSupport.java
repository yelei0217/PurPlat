package com.kingdee.eas.custom.app.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectStringPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.PurOrderDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.scm.sm.pur.IPurOrderEntry;
import com.kingdee.eas.scm.sm.pur.PurOrderEntryFactory;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

/**
 *  
 * @author lei.ye
 *
 */
public class PurOrderSupport {
	
	//private LowTimer timer = new LowTimer();
	//private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.IncomeCostMatchFacadeControllerBean");
	
//	public static String syncBill(Context ctx,String jsonStr){
//		String result = null;
//		if(jsonStr != null && !"".equals(jsonStr)){
//		    System.out.println("************************json begin****************************");
//		    System.out.println("#####################jsonStr################=" + jsonStr);
//			DateBaseProcessType processType = DateBaseProcessType.AddNew;
//			DateBasetype baseType = DateBasetype.B2B_GZ_LZ_PO;
//			String msgId = "";
//			String busCode ="";
//			String reqTime ="";
//			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json ת�ɶ���
//			JsonElement msgIdJE = returnData.get("msgId"); // ������ϢId
//			JsonElement busCodeJE = returnData.get("busCode"); // ҵ����������
//			JsonElement reqTimeJE = returnData.get("reqTime"); // ������ϢId
//			Gson gson = new Gson();
//			JsonElement modelJE = returnData.get("data"); // �������data
//			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
//					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
//					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
//
//				msgId = msgIdJE.getAsString() ;
//				busCode = busCodeJE.getAsString() ;
//				reqTime = reqTimeJE.getAsString() ;
//				
//			// ��¼��־
//		//	IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
//			PurOrderDTO m = gson.fromJson(modelJE, PurOrderDTO.class);
//			// �ж�msgId �Ƿ����
//			if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
//				result = judgeModel(ctx,m);
//				if("".equals(result))
//				{
//					doInsertBill(ctx,m,busCode);
//					result = "success";
//				}
//			
//			}else
//				result = PurPlatSyncEnum.EXISTS_BILL.getAlias();
//			}else
//				result = PurPlatSyncEnum.FIELD_NULL.getAlias();
//		}else
//			result = PurPlatSyncEnum.FIELD_NULL.getAlias();
//	    
//		return result;
//	}
 
	/**
	 *  ���� eas��
	 * @param ctx
	 */
	public static void doInsertBill(Context ctx,BaseSCMDTO m,String typeVal,String busCode){
		ExecutorService pool = Executors.newFixedThreadPool(6);
	    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
	    StringBuffer sbr = new StringBuffer(" /*dialect*/ insert into T_SM_PurOrder (FID,FCREATORID,FCREATETIME,FMODIFIERID,FMODIFICATIONTIME,FLASTUPDATEUSERID,FLASTUPDATETIME,FCONTROLUNITID," );
		sbr.append("FNUMBER,FBIZDATE,FHASEFFECTED,FAUDITORID,FAUDITTIME,FBASESTATUS,FBIZTYPEID,FBILLTYPEID,FYEAR,FPERIOD,"); 
		sbr.append("FPURCHASEORGUNITID,FPURCHASEPERSONID,FSUPPLIERID,FISDIRECTSEND,FPAYMENTTYPEID,FSETTLEMENTTYPEID,FCURRENCYID," );
		sbr.append("FEXCHANGERATE,FPREPAYMENTRATE,FPREPAYMENT,FPREPAID,FSUPPLIERCONFIRM,FINVOICEDAMOUNT,FPAIDAMOUNT,FISINNERSALE," );
		sbr.append("FTOTALAMOUNT,FTOTALTAX,FTOTALTAXAMOUNT,FUNPREPAIDAMOUNT,FISSYSBILL,FCONVERTMODE,FLOCALTOTALAMOUNT,FLOCALTOTALTAXAMOUNT," );
		sbr.append("FSTORAGEORGUNITID,FISCENTRALBALANCE,FCOMPANYORGUNITID,FISINTAX,FISQUICKEN,FVERSION,FISPRICEINTAX,FISMATCHED,FISAPPROVEDMATERIAL,");
		sbr.append(	"FISNETORDER,FISRECALINNERPRICE,FPRICESOURCE,FMATCHINVAMOUNT,FINVOICEMATCH,FCHANGETYPE,CFMsgId,CFBusCode ) values ( ");
		String sId = BOSUuid.create("3171BFAD").toString();
		String userId = PurPlatUtil.getUserIdByPersonId(ctx, m.getFcreatorid());

		String bizTypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C"; // ��ͨ�ɹ�
		String billTypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//�ɹ�����
		String paymentTypeId = "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5";//�޹�
		String settlementTypeId ="jbYAAAA0YHXpayuO" ;//�Թ�����
		String currencyId ="dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC" ;//�����
		String rowType = "00000000-0000-0000-0000-0000000000017C7DC4A3";
		int isInTax = 0;//����Ϊ0������Ϊ1
		if(busCode.contains("LZ"))
			isInTax = 1;
		 else if(busCode.contains("MZ"))
			 isInTax = 0;
		//String bizDate = m.getFbizdate().
		String bizDateStr = m.getFbizdate();
		sbr.append("'").append(sId).append("','").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'");
		String ctrlOrgId = PurPlatUtil.getCtrlOrgId(ctx, "PUR", m.getFstorageorgunitid()); //���Ƶ�Ԫ
		sbr.append(ctrlOrgId).append("','").append(m.getFnumber()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),0,'");
		sbr.append(userId).append("',sysdate,4,'").append(bizTypeId).append("','").append(billTypeId).append("',").append(bizDateStr.substring(0, 4)).append(",").append(Integer.parseInt(bizDateStr.substring(5,7))).append(",'");
		sbr.append(m.getFstorageorgunitid()).append("','").append(m.getFpurchasepersonid()).append("','").append(m.getFsupplierid()).append("',0,'").append(paymentTypeId).append("','").append(settlementTypeId).append("','").append(currencyId).append("',");
		sbr.append("1,0,0,0,0,0,0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltax()).append(",").append(m.getFtotaltaxamount()).append(",0,0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",'").append( m.getFstorageorgunitid()).append("',0,'").append(m.getFstorageorgunitid()).append("',").append(isInTax).append(",0,0,1,0,0,0,1,1,0,0,0 ,'").append(m.getId()).append("','").append(typeVal).append("')");
		pe.getSqlList().add(sbr);
		
		for(BaseSCMDetailDTO dvo : m.getDetails()){
			String eid  = BOSUuid.create("26041CC5").toString();
			StringBuffer sbr1 = new StringBuffer("/*dialect*/ insert into T_SM_PURORDERENTRY(FID,FSEQ,FMATERIALID,FUNITID,FBASESTATUS,FASSOCIATEQTY,FBASEUNITID,FREMARK,FISPRESENT,FDEMANDQTY,FQTY," +
					"FSTORAGEORGUNITID,FCOMPANYORGUNITID,FASSISTQTY,FPRICE,FDISCOUNTRATE,FACTUALPRICE,FTAXRATE,FTAXPRICE,FACTUALTAXPRICE," +
					"FAMOUNT,FLOCALAMOUNT,FTAX,FTAXAMOUNT,FDISCOUNTAMOUNT,FDELIVERYDATE,FRECEIVEOVERRATE,FRECEIVEOWINGRATE," +
					"FDELIVERADVANCEDAY,FDELIVERDEFERRALDAY,FBASEQTY,FTOTALRECEIVEQTY,FTOTALRECEIPTQTY,FTOTALRETURNEDQTY,FTOTALINVOICEDQTY," +
					"FTOTALINVOICEDAMOUNT,FTOTALPAIDAMOUNT,FTOTALEXPENSE,FPARENTID,FISQUANTITYUNCTRL,FISTIMEUNCTRL,FTOTALRECEIVEBASEQTY," +
					"FTOTALRECEIPTBASEQTY,FTOTALRETURNEDBASEQTY,FTOTALINVOICEDBASEQTY,FTOTALUNRETURNBASEQTY,FTOTALUNRECEIVEBASEQTY,FTOTALUNRECEIVEQTY,FISSUPINFO,FCURSEORDERQTY," +
					"FLOCALTAX,FLOCALTAXAMOUNT,FSALEORDERNUMBER,FPREPAIDAMOUNT,FREQUESTORGUNITID,FREQUESTCOMPANYORGUNITID,FISREQUESTTORECEIVED,FTOTALMOVEQTY,FTOTALINVOICEDAMT," +
					"FPREPAYMENT,FPRERECEIVED,FUNPRERECEIVEDAM,FVERSION,FCANINVMOVEQTY,FUNORDEREDQTY,FISBETWEENCOMPANYREC,FROWTYPEID,FDESTINATIONTYPE,FMATERIALNAME,FISREQCOMEQLRECCOM," +
					"FPLANRECEIVEQTY,FTOTALSUPPLYSTOCKQTY,FTOTALCANCELLEDSTOCKQTY,FTOTALSUPPLYSTOCKBASEQTY,FTOTALPREPAYAMT,FTOTALREQPAYAMT,FISREQPREPAYGTPREPAY,FNONUMMATERIALMODEL," +
					"FMATCHEDAMOUNT,FPURCHASEORGUNITID,FBIZDATE,CFPINPAI,CFHUOHAO,CFXINGHAO,CFWULIAOLEIBIE,CFMsgId ) values (");
			
			String deliveDateStr = "";
			
			if( dvo.getFdeliverydate()!=null && !"".equals( dvo.getFdeliverydate()))
				deliveDateStr = dvo.getFdeliverydate();
			else
				deliveDateStr = bizDateStr;
			
			Map<String,String> mmp = PurPlatUtil.getMaterialInfoByMId(ctx, dvo.getFmaterialid());
			
			int isPresent = 0;
			//if(dvo.getFispresent())
			if(dvo.getFispresent() !=null && !"".equals(dvo.getFispresent())&& "1".equals(dvo.getFispresent()))
				isPresent = 1;
			else
				isPresent = 0;
 			EntityViewInfo view = new EntityViewInfo();
		 	FilterInfo filter = new FilterInfo();
		 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
		  	view.setFilter(filter);
		  	
		    MaterialInfo material = null;
		    MaterialCollection materialColl =null ;
			try {
				materialColl = MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
				material = materialColl.get(0);
			} catch (BOSException e) {
 				e.printStackTrace();
			}
			
			sbr1.append("'").append(eid).append("',").append(dvo.getFseq()).append(",'").append(material.getId().toString()).append("','").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())).append("',4,");
			sbr1.append(dvo.getFqty()).append(",'").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())).append("','").append(dvo.getFremark()).append("',").append(isPresent).append(",0,").append(dvo.getFqty());
			sbr1.append(",'").append( m.getFstorageorgunitid()).append("','").append( m.getFstorageorgunitid()).append("',0,").append(dvo.getFprice()).append(",0,").append(dvo.getFactualprice()).append(",").append(dvo.getFtaxrate()).append(",");
			sbr1.append(dvo.getFtaxprice()).append(",").append(dvo.getFactualtaxprice()).append(",").append(dvo.getFamount()).append(",").append(dvo.getFamount()).append(",").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,");
			sbr1.append("to_date('").append(deliveDateStr).append("','yyyy-MM-dd'),0,0,0,0,").append(dvo.getFbaseqty()).append(",0,0,0,0,0,0,0,'").append(sId).append("',0,1,0,0,0,0,0,");
			sbr1.append(dvo.getFbaseqty()).append(",").append(dvo.getFqty()).append(",0,0,").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,0,'");
			sbr1.append(m.getFstorageorgunitid()).append("','").append(m.getFstorageorgunitid()).append("',1,0,0,0,0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFqty()).append(",0,'").append(rowType).append("',10,'");
			sbr1.append(mmp.get("name")).append("',1,0,0,0,0,0,0,0,'").append(mmp.get("gg")).append("',0,'").append(m.getFstorageorgunitid()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),'");
			sbr1.append(mmp.get("pp")).append("','").append(mmp.get("hh")).append("','").append(mmp.get("xh")).append("','").append(mmp.get("gn")).append("','").append(dvo.getId()).append("' ) ");;
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
	
	

//	
//	/**
//	 * У�� ʵ���Ƿ���ȷ
//	 * @param ctx
//	 * @param m
//	 * @return
//	 */
//	private static String judgeModel(Context ctx,PurOrderDTO m ){
//		 String result = "";
//		 //��֯�Ƿ����
//		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
//			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
//			try {
//				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
//					result = result +"�ɹ���֯������,";
//			} catch (EASBizException e) {
// 				e.printStackTrace();
//			} catch (BOSException e) {
// 				e.printStackTrace();
//			}
//			 
//		 }else{
//			 result = result +"�ɹ���֯����Ϊ��,";
//		 }
//		 
//		 if(m.getFnumber() ==null || "".equals(m.getFnumber())) 
//			 result = result +"���ݱ�Ų���Ϊ��,";
//		 
//		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
//			 result = result +"ҵ�����ڲ���Ϊ��,";
//			
//		 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
//			 result = result +"��Ӧ�̲���Ϊ��,";
//		 else{
//			if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
//				if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
//					 result = result +"��Ӧ��δ���䵱ǰ��֯,";
//				}else
//					 result = result +"��Ӧ�̲�����,";
//		  }
//			
//			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
//				 result = result +"��˰�ϼơ���˰�� ��������Ϊ��,";
//			 else{
//				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
//					 result = result +"��˰�ϼƵ��ڽ���˰��ĺϼ�,";
//		  }
//			 
//			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
//				 for(PurOrderDetailDTO dvo : m.getDetails()){
//					 int j = 0 ; 
//					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
//						 result = result +"��"+j+1+"������ID����Ϊ��,";
//					 }else{
//						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
//							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
//								 result = result +"��"+j+1+"����δ���䵱ǰ��֯,";
//						 }else
//							 result = result +"��"+j+1+"�� ����ID������,";
//					 }
//					 
//					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
//						 result = result +"��"+j+1+"�м�����λ����Ϊ��,";
//					 }else{
//						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
//							 result = result +"��"+j+1+"�� ������λ"+dvo.getFunitid()+"������,";
//					 }
//					 
//					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
//						 result = result +"��"+j+1+"�л���������λ����Ϊ��,";
//					 }else{
//						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
//							 result = result +"��"+j+1+"�� ����������λ"+dvo.getFbaseunitid()+"������,";
//					 }
//					 
//					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
//						 result = result +"��"+j+1+"�� ����������������������Ϊ��,";
//					}
//					 
//					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
//						 result = result +"��"+j+1+"�� ���ۡ�ʵ�ʵ��ۡ���˰���ۡ�ʵ�ʺ�˰���� ����Ϊ��,";
//					}
//					
//					if(dvo.getFtaxrate() == null){ 
//						 result = result +"��"+j+1+"�� ˰�ʲ���Ϊ��,";
//					}
//					if(dvo.getFtax() == null){ 
//						 result = result +"��"+j+1+"�� ˰���Ϊ��,";
//					}
//					 
//					if(dvo.getFamount()== null){ 
//						 result = result +"��"+j+1+"�� ����Ϊ��,";
//					}
//					
//					if(dvo.getFtaxamount() == null){ 
//						 result = result +"��"+j+1+"�� ��˰�ϼƲ���Ϊ��,";
//					}
//			 
//					if(dvo.getFdeliverydate() == null){ 
//						 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
//					}
//				 }
//			} else 
//				result = result +"������һ����ϸ�е�����,";
//			 
//		 return result;
//	}
	
	
	/**
	 *  �йر�
	 * @param ctx
	 * @param jsonStr
	 * @return
	 */
	public static String doCloseRow(Context ctx,String jsonStr){
		String result ="";
		String msgId = "";
		String busCode ="";
		String reqTime ="";
		Gson gson = new Gson();
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json PurOrderSupport  doCloseRow  begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
		    System.out.println("************************json PurOrderSupport  doCloseRow  end****************************");

			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZB_LZ_PO_CR;
			
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
				
				if(modelJE.getAsJsonObject() !=null && modelJE.getAsJsonObject().get("id") !=null && !"".equals(modelJE.getAsJsonObject().get("id").getAsString())&&
					modelJE.getAsJsonObject().get("eids") !=null && !"".equals(modelJE.getAsJsonObject().get("eids").getAsString()))
				{
					try {
						// ��¼��־
						IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
						String id = modelJE.getAsJsonObject().get("id").getAsString();
						String eids = modelJE.getAsJsonObject().get("eids").getAsString();

						 String[] esStr = eids.split(",");
						 Set<String> entryIds = new HashSet();
						    int j = esStr.length;
						    for (int i = 0; i < j ; i++)
						    {
						      String s = esStr[i];
						      if ((s != null) && (!"".equals(s))) {
						        entryIds.add(s);
						      }
						    }
						    StringBuffer sbr1 = new StringBuffer("");
						      for (String s : entryIds)
						      {
						        sbr1.append("'").append(s).append("',");
						      }
						      if (sbr1.length() > 1) {
						        eids = sbr1.substring(0, sbr1.length() - 1);
						      }  

						      IPurOrderEntry ibize = PurOrderEntryFactory.getLocalInstance(ctx);
						      StringBuffer sbr = new StringBuffer("/*dialect*/ select distinct FID,FBaseStatus from T_SM_PURORDERENTRY where FPARENTID in ( select FID from T_SM_PURORDER where CFMsgId  ='").append(id).append("'");
						      if(busCode.contains("LZ"))
							  		sbr.append(" and FCOMPANYORGUNITID ='jbYAAAMU2SvM567U' ");
							     else if(busCode.contains("MZ"))
								   sbr.append(" and FCOMPANYORGUNITID !='jbYAAAMU2SvM567U' ");
						      sbr.append(") and CFMsgId in (").append(eids).append(")");
						      if(busCode.contains("LZ"))
							  		sbr.append(" and FPURCHASEORGUNITID ='jbYAAAMU2SvM567U' ");
							     else if(busCode.contains("MZ"))
								   sbr.append(" and FPURCHASEORGUNITID !='jbYAAAMU2SvM567U' ");
							    
						      IRowSet rs = DbUtil.executeQuery(ctx, sbr.toString());
						      List<String> reasonLists = new ArrayList();
						      List<IObjectPK> pkLists = new ArrayList();
						      if ((rs != null) && (rs.size() > 0))
						      {
						        while (rs.next()) {
						          if ((rs.getObject("FID") != null) && (!"".equals(rs.getObject("FID").toString())) && 
						            (rs.getObject("FBaseStatus") != null) && (!"".equals(rs.getObject("FBaseStatus").toString()))) {
						              if ("4".equals(rs.getObject("FBaseStatus").toString()))
						              {
						                pkLists.add(new ObjectStringPK(rs.getObject("FID").toString()));
						                reasonLists.add("B2B�йرղ���");
						              }
						          }
						        }
						        if ((pkLists != null) && (pkLists.size() > 0))
						        {
						          IObjectPK[] pks = (IObjectPK[])pkLists.toArray(new IObjectPK[pkLists.size()]);
						          String[] reasons = (String[])reasonLists.toArray(new String[reasonLists.size()]);
						          ibize.handClose(pks, reasons);
						        }
 						        purPlatMenu = PurPlatSyncEnum.SUCCESS;
						      } else
						    	  purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
					} catch (EASBizException e) {
 						e.printStackTrace();
					} catch (BOSException e) {
 						e.printStackTrace();
					} catch (SQLException e) {
 						e.printStackTrace();
					}
				}
			}else
				purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		}else
			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		
		respondDTO.setCode(purPlatMenu.getValue());
		respondDTO.setMsgId(msgId);
		respondDTO.setMsg(purPlatMenu.getAlias());
		
		return gson.toJson(respondDTO) ;
	}

}
