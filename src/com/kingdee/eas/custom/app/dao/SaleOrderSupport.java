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
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.PushRecordFactory;
import com.kingdee.eas.custom.PushRecordInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.PushStatusEnum;
import com.kingdee.eas.custom.app.dto.SaleOrderDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.scm.sd.sale.ISaleOrderEntry;
import com.kingdee.eas.scm.sd.sale.SaleOrderEntryFactory;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class SaleOrderSupport {

	/**
	 *  插入 eas表
	 * @param ctx
	 */
	public static void doInsertBill(Context ctx,BaseSCMDTO m,String busCode){
		ExecutorService pool = Executors.newFixedThreadPool(6);
	    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
	    StringBuffer sbr = new StringBuffer("/*dialect*/insert into T_SD_SALEORDER (FID,FCREATORID,FCREATETIME,FMODIFIERID,FMODIFICATIONTIME,FLASTUPDATEUSERID,FLASTUPDATETIME," +
	    		" FCONTROLUNITID,FNUMBER,FBIZDATE,FDESCRIPTION,FHASEFFECTED,FAUDITORID,FAUDITTIME,FBASESTATUS,FBIZTYPEID,FBILLTYPEID," +
	    		" FYEAR,FPERIOD,FISINNERSALE,FORDERCUSTOMERID,FPURCHASEORGUNITID,FDELIVERYTYPEID,FTRANSLEADTIME,FCURRENCYID," +
	    		" FEXCHANGERATE,FPAYMENTTYPEID,FSETTLEMENTTYPEID,FPREPAYMENT,FPREPAYMENTRATE,FSALEORGUNITID,FSALEPERSONID," +
	    		" FTOTALAMOUNT,FTOTALTAX,FTOTALTAXAMOUNT,FPRERECEIVED,FUNPRERECEIVEDAMOUNT,FSENDADDRESS,FISSYSBILL,FCONVERTMODE,FLOCALTOTALAMOUNT," +
	    		" FLOCALTOTALTAXAMOUNT,FCOMPANYORGUNITID,FISINTAX,FVERSION,FOLDSTATUS,FISCENTRALBALANCE,FISREVERSE,FBEENPAIDPREPAYMENT," +
	    		" FISSQUAREBALANCE,FISMATCHEDPROMOTION,FISENTIRESINGLEDISCOUNT,FORIGINALDISCOUNTAMOUNT,CFMsgId ) values ( ");
		
	    String sId = BOSUuid.create("C48A423A").toString();
	    String userId = PurPlatUtil.getUserIdByPersonId(ctx, m.getFcreatorid());
		String bizTypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C"; // 普通销售
		String billTypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//销售订单
		String paymentTypeId = "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5";//赊销
		
		String settlementTypeId ="jbYAAAA0YHXpayuO" ;//对公付款
		String currencyId ="dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC" ;//人民币
		
		String deliverTYpeId = "51eb893e-0105-1000-e000-0c00c0a8123362E9EE3F"; // 送货：SEND
		
		int isInTax = 0;//门诊为0，栗喆为1
		if(busCode.contains("LZ"))
			isInTax = 1;
		 else if(busCode.contains("MZ"))
			 isInTax = 0;
			 
		//String bizDate = m.getFbizdate().
		String bizDateStr = m.getFbizdate();
		sbr.append("'").append(sId).append("','").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'");
		String ctrlOrgId = PurPlatUtil.getCtrlOrgId(ctx, "PUR", m.getFstorageorgunitid()); //控制单元
		sbr.append(ctrlOrgId).append("','").append(m.getFnumber()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),'").append(m.getFdescription()).append("',0,'");
		sbr.append(userId).append("',sysdate,4,'").append(bizTypeId).append("','").append(billTypeId).append("','").append(bizDateStr.substring(0, 4)).append("','").append(Integer.parseInt(bizDateStr.substring(5,7))).append("',0,'");
		sbr.append(m.getFcustomerid()).append("','").append(m.getFstorageorgunitid()).append("','").append(deliverTYpeId).append("',0,'").append(currencyId).append("',1,'").append(paymentTypeId).append("','").append(settlementTypeId).append("'");
		sbr.append(",0,0,'").append(m.getFstorageorgunitid()).append("','jbYAAAAB7DOA733t',").append(m.getFtotalamount()).append(",").append(m.getFtotaltax()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",0,0,'").append(m.getFsendaddress()).append("',0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",'").append( m.getFstorageorgunitid()).append("',").append(isInTax).append(",0,0,0,0,0,0,0,0,0,'").append(m.getId()).append("') ");
		pe.getSqlList().add(sbr);
		
		for(BaseSCMDetailDTO dvo :  m.getDetails()){
			String eid  = BOSUuid.create("88882A58").toString();
			StringBuffer sbr1 = new StringBuffer("/*dialect*/insert into T_SD_SALEORDERENTRY( FID,FSEQ,FMATERIALID,FUNITID,FBASESTATUS,FASSOCIATEQTY,FBASEUNITID,FREMARK,FISPRESENT," +
					" FBASEQTY,FQTY,FPRICE,FTAXPRICE,FDISCOUNTCONDITION,FDISCOUNTTYPE,FDISCOUNT,FDISCOUNTAMOUNT,FAMOUNT,FLOCALAMOUNT,FACTUALPRICE,FACTUALTAXPRICE," +
					" FTAXRATE,FTAX,FTAXAMOUNT,FSENDDATE,FDELIVERYDATE,FSTORAGEORGUNITID,FCOMPANYORGUNITID,FSENDOVERRATE,FSENDOWINGRATE,FSENDADVANCEDAY," +
					" FSENDDEFERRALDAY,FTOTALISSUEQTY,FTOTALRETURNEDQTY,FTOTALINVOICEDQTY,FTOTALSHIPPINGQTY,FTOTALRECEIVEDAMOUNT,FPARENTID,FTOTALISSUEBASEQTY," +
					" FTOTALREBASEQTY,FTOLINVOIDBASEQTY,FTOTALSHIPBASEQTY,FTOTALUNRETURNBASEQTY,FTOTALUNISSUEBASEQTY,FTOTALUNSHIPBASEQTY,FTOTALUNISSUEQTY," +
					" FISLOCKED,FLOCKQTY,FLOCKBASEQTY,FLOCALTAX,FLOCALTAXAMOUNT,FISBYSALEORDER,FORDEREDQTY,FUNORDEREDQTY,FPREPAYMENTRATE,FPREPAYMENT,FPRERECEIVED," +
					" FUNPRERECEIVEDAMOUNT,FQUANTITYUNCTRL,FTIMEUNCTRL,FDELIVERYDATEQTY,FTOTALINVOICEDAMT,FTOTALARAMOUNT,FVERSION,FISBETWEENCOMPANYSEND,FTOTALREVERSEDQTY," +
					" FTOTALREVERSEDBASEQTY,FPLANDELIVERYQTY,FTOTALCANCELLINGSTOCKQTY,FTOTALSUPPLYSTOCKQTY,FDELIVERYCUSTOMERID,FPAYMENTCUSTOMERID,FRECEIVECUSTOMERID," +
					" FARCLOSEDSTATUS,FTOTALPRODUCTQTY,FTOTALBASEPRODUCTQTY,FTOTALUNPRODUCTQTY,FTOTALBASEUNPRODUCTQTY,FMATCHEDAMOUNT,FLOCKASSISTQTY,FCHEAPRATE," +
					" FRETURNPLANDELIVERYQTY,FRETURNPLANDELIVERYBASEQTY,FSALEORGUNITID,FBIZDATE,FPRICESOURCETYPE,FISMRPCAL,FPROMOTIONSOURCEGROUP,FPROSTORAGEORGUNITID," +
					" FSUPPLYMODE,FTOTALTRANSFERQTY,FTOTALTRANSFERBASEQTY,FTOTALUNTRANSFERQTY,FTOTALUNTRANSFERBASEQTY,CFPINPAI,CFHUOHAO," +
					" FINVOICEREQQTY,FINVOICEREQBASEQTY,FUNINVOICEREQQTY,FUNINVOICEREQBASEQTY,FINVOICEREQAMOUNT,FINVOICEREQAMOUNTLOCAL,FUNINVOICEREQAMOUNT,FUNINVOICEREQAMOUNTLOCAL,CFMsgId");
		
			if(dvo.getFwarehouseid() !=null && !"".equals(dvo.getFwarehouseid())){
				sbr1.append(",FWarehouseID  ");
			}
			 sbr1.append(") values (");
			 
			 
			String deliveDateStr = "";
			String sendDateStr =   dvo.getFsenddate();
			
			if( dvo.getFdeliverydate()!=null && !"".equals( dvo.getFdeliverydate()))
				deliveDateStr = dvo.getFdeliverydate();
			else
				deliveDateStr = bizDateStr;
			
			if( dvo.getFsenddate()!=null && !"".equals( dvo.getFsenddate()))
				sendDateStr = dvo.getFsenddate();
			else
				sendDateStr = bizDateStr;

			Map<String,String> mmp = PurPlatUtil.getMaterialInfoByMId(ctx, dvo.getFmaterialid());
			int isPresent = 0;
//			if(dvo.getFispresent())
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
			sbr1.append(dvo.getFqty()).append(",'").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())).append("','").append(dvo.getFremark()).append("',").append(isPresent).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty());
			sbr1.append(",").append(dvo.getFprice()).append(",").append(dvo.getFtaxprice()).append(",-1,0,0,0,").append(dvo.getFamount()).append(",").append(dvo.getFamount()).append(",");
			sbr1.append(dvo.getFactualprice()).append(",").append(dvo.getFactualtaxprice()).append(",").append(dvo.getFtaxrate()).append(",").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",");
			sbr1.append("to_date('").append(sendDateStr).append("','yyyy-MM-dd')").append(",to_date('").append(deliveDateStr).append("','yyyy-MM-dd'),'").append(m.getFstorageorgunitid()).append("','").append(m.getFstorageorgunitid()).append("',0,0,0,0,0,0,0,0,0,'").append(sId).append("',0,0,0,0,");
			sbr1.append(dvo.getFqty()).append(",").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty()).append(",0,0,0,").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,0,").append(dvo.getFqty());
			sbr1.append(",0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,'").append(m.getFcustomerid()).append("','").append(m.getFcustomerid()).append("','").append(m.getFcustomerid()).append("',");
			sbr1.append("0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,0,0,0,'").append(m.getFstorageorgunitid()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),-1,0,0,'");
			sbr1.append(m.getFstorageorgunitid()).append("',0,0,0,0,0,'").append(mmp.get("pp")).append("','").append(mmp.get("hh")).append("',0,0,");
			sbr1.append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,").append(dvo.getFtaxamount()).append(",").append(dvo.getFtaxamount()).append(",'").append(dvo.getId());
			
			if(dvo.getFwarehouseid() !=null && !"".equals(dvo.getFwarehouseid())){
				sbr1.append("','").append(dvo.getFwarehouseid());
			}
			sbr1.append("' )");
			
			pe.getSqlList().add(sbr1);
		}
	  
		if(pe.getSqlList().size()>0)
			try {
				pe.executeUpdate(ctx); 
				pool.shutdown(); 
				DateBasetype dataseBaseType = DateBasetype.CGZ_U_MZ_SO;
				PushRecordInfo rInfo = new PushRecordInfo();
				rInfo.setNumber(m.getId());
				rInfo.setName(sId);
				rInfo.setDescription(m.getFnumber());
				rInfo.setDateBaseType(dataseBaseType);
				rInfo.setProcessType(DateBaseProcessType.GSaleIss);
				rInfo.setPushStatus(PushStatusEnum.unDo);
				CtrlUnitInfo control = new CtrlUnitInfo();
				control.setId(BOSUuid.read(ctrlOrgId));
				rInfo.setCU(control);
				PushRecordFactory.getLocalInstance(ctx).addnew(rInfo); 
				
			} catch (EASBizException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			} catch (BOSException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			}	
		     pool.shutdown(); 
		 
	}
	
	
	public static String doCloseRow(Context ctx,String jsonStr){
		String result ="";
		
		
		String msgId = "";
		String busCode ="";
		String reqTime ="";
		Gson gson = new Gson();
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json SaleOrderSupport  doCloseRow  begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
		    System.out.println("************************json SaleOrderSupport  doCloseRow  end****************************");
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZB_LZ_SO_CR;
	 
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
				
				if(modelJE.getAsJsonObject() !=null && modelJE.getAsJsonObject().get("id") !=null && !"".equals(modelJE.getAsJsonObject().get("id").getAsString())&&
					modelJE.getAsJsonObject().get("eids") !=null && !"".equals(modelJE.getAsJsonObject().get("eids").getAsString()))
				{
					try {
						// 记录日志
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

						      ISaleOrderEntry ibize = SaleOrderEntryFactory.getLocalInstance(ctx);
						      StringBuffer sbr = new StringBuffer("/*dialect*/ select distinct FID,FBaseStatus from T_SD_SALEORDERENTRY where FPARENTID in ( select FID from T_SD_SALEORDER where CFMsgId  ='").append(id).append("'");
						      sbr.append(") and CFMsgId in (").append(eids).append(")");
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
						                reasonLists.add("B2B行关闭操作");
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
						      }
						      else 
						    	  purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
					} catch (EASBizException e) {
 						e.printStackTrace();
					} catch (BOSException e) {
 						e.printStackTrace();
					} catch (SQLException e) {
 						e.printStackTrace();
					}
				}else 
					purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
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
