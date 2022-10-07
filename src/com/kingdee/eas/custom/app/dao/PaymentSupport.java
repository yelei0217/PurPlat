package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;

import com.google.gson.Gson;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.assistant.SettlementTypeFactory;
import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.IPurPlatSyncBusLog;
import com.kingdee.eas.custom.PurPlatSyncBusLogCollection;
import com.kingdee.eas.custom.PurPlatSyncBusLogFactory;
import com.kingdee.eas.custom.PurPlatSyncBusLogInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
import com.kingdee.eas.fi.cas.PaymentBillFactory;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
import com.kingdee.eas.fi.cas.SourceTypeEnum;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class PaymentSupport {
	 
	public static String doSendPayMentToB2B(Context ctx){
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		String result = "";
		String msgId =PurPlatUtil.getCurrentTimeStrS();
		Gson gson = new Gson(); 
   		EntityViewInfo view = new EntityViewInfo();
 	 	FilterInfo filter = new FilterInfo();
 	 	filter.getFilterItems().add(new FilterItemInfo("isSync",false,CompareType.EQUALS)); //
 	 	filter.getFilterItems().add(new FilterItemInfo("processType",DateBaseProcessType.AddNew,CompareType.EQUALS)); 
 	 	Set<DateBasetype> baseTypeSets = new HashSet<DateBasetype>();
 	 	baseTypeSets.add(DateBasetype.GZ_CK_LZ_P);
 	 	baseTypeSets.add(DateBasetype.GZ_CK_MZ_P);
 	 	baseTypeSets.add(DateBasetype.VMI_CK_LZ_P);
 	 	baseTypeSets.add(DateBasetype.VMI_CK_MZ_P);
 	 	baseTypeSets.add(DateBasetype.DZ_CK_MZ_P);
 	 	baseTypeSets.add(DateBasetype.YC_CK_MZ_P);
 	 	baseTypeSets.add(DateBasetype.YX_CK_LZ_P);
 	 	baseTypeSets.add(DateBasetype.YX_CK_MZ_P);
 	 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType",baseTypeSets,CompareType.INCLUDE)); //
 	  	view.setFilter(filter);
 	  	try {
 	 	  	IPurPlatSyncBusLog ibiz = PurPlatSyncBusLogFactory.getLocalInstance(ctx);
 	  		PurPlatSyncBusLogCollection coll = ibiz.getPurPlatSyncBusLogCollection(view);
 	  		if(coll !=null && coll.size() >0 ){
 	  			Iterator it = coll.iterator();
 	  			CoreBaseCollection updateColl = new CoreBaseCollection();
  	  			while(it.hasNext()){
 	  				PurPlatSyncBusLogInfo info = (PurPlatSyncBusLogInfo) it.next();
 	  				String sendStr = info.getMessage();
 	  				if(sendStr !=null && !"".equals(sendStr)){
 	  					sendPaymentToB2B(ctx,sendStr);
 	  					info.setIsSync(true);
 	  					updateColl.add(info); 
 	  				} 
 	  			}
  	  			if(updateColl !=null && updateColl.size() >0 ){
  	  				ibiz.updateBatchData(updateColl);
  	  				purPlatMenu = PurPlatSyncEnum.SUCCESS;
  	  			} 
  	  			else purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
 	  		}else
 	  			purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
 	  	} catch (BOSException e) {
 			e.printStackTrace();
		} catch (EASBizException e) {
 			e.printStackTrace();
		}  
		respondDTO.setCode(purPlatMenu.getValue());
		respondDTO.setMsgId(msgId);
		if(purPlatMenu==PurPlatSyncEnum.EXCEPTION_SERVER)
			respondDTO.setMsg(result);
		else
			respondDTO.setMsg(purPlatMenu.getAlias()); 
		return gson.toJson(respondDTO); 
	} 
	
	private static String sendPaymentToB2B(Context ctx, String jsonStr)
	throws BOSException {
 		String result = "";
		try {
			HttpClient httpClient = new HTTPSTrustClient().init();
			result += HTTPSClientUtil.doPostJson(httpClient, InterfaceResource.sap_base_url, jsonStr);			
		} catch (Exception e) {
				e.printStackTrace();
		} 
		return result;
	}
	
	public static String insertBillToLog(Context ctx,String id,String busCode )
    throws BOSException, EASBizException{
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		String result = "";
		String msgId =PurPlatUtil.getCurrentTimeStrS();
		Gson gson = new Gson();
		
		if(id !=null && !"".equals(id)){
			 IObjectPK pk = new  ObjectUuidPK(id);
			if( PaymentBillFactory.getLocalInstance(ctx).exists(pk)){
 				Map<String,Object> billmp = getPaymentBillMap(ctx,id);
				if(billmp !=null && billmp.size() >0){
					Map<String,Object> insertMap = new HashMap<String,Object>();
					List<Map<String,String>> entryList = getPaymentEntryList(ctx,id);
					billmp.put("details", entryList);
					
					insertMap.put("msgId", msgId);
					insertMap.put("busCode", busCode);
					insertMap.put("reqTime", msgId);
					insertMap.put("data", billmp);
					
					DateBaseProcessType processType = DateBaseProcessType.AddNew;
					DateBasetype baseType = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
					result = gson.toJson(insertMap);
					
				    PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(),result);
				 
				    purPlatMenu = PurPlatSyncEnum.SUCCESS;
				    
				}else
		 			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
				
			}else
	 			purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
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
	
	
	public static List<Map<String,String>> getPaymentEntryList(Context ctx,String pId){
		List<Map<String,String>> list = null;
		if(VerifyUtil.notNull(pId) ){
			String sql = "select b.fid,b.FSEQ,d.FNUMBER facctviewnumber,d.FNAME_L2 facctviewname, b.FAmount,b.FLocalAmount "+
			" from T_CAS_PAYMENTBILL a "+
			" inner join T_CAS_PAYMENTBILLENTRY b on b.FPAYMENTBILLID = a.FID "+
			//" inner join T_BD_Currency c on a.FCurrencyID = c.FID "+
			" inner join T_BD_AccountView d on b.FOppAccountID = d.FID --and d.FCOMPANYID = a.FCOMPANYID "+
			" where a.FID='"+pId+"' ";
			try {
				IRowSet rs = DbUtil.executeQuery(ctx, sql);
				if(rs !=null && rs.size() >0 ){
					while(rs.next()){
						Map<String,String> mp = new HashMap<String,String>();
						 if(rs.getObject("fid")!=null &&!"".equals(rs.getObject("fid").toString()))
			        		 mp.put("fid", rs.getObject("fid").toString()); 
						 if(rs.getObject("FSEQ")!=null &&!"".equals(rs.getObject("FSEQ").toString()))
			        		 mp.put("fseq", rs.getObject("FSEQ").toString()); 
						 if(rs.getObject("facctviewnumber")!=null &&!"".equals(rs.getObject("facctviewnumber").toString()))
			        		 mp.put("facctviewnumber", rs.getObject("facctviewnumber").toString()); 
						 if(rs.getObject("facctviewname")!=null &&!"".equals(rs.getObject("facctviewname").toString()))
			        		 mp.put("facctviewname", rs.getObject("facctviewname").toString());					
						 if(rs.getObject("FAmount")!=null &&!"".equals(rs.getObject("FAmount").toString()))
			        		 mp.put("famount", rs.getObject("FAmount").toString());
						 if(rs.getObject("FLocalAmount")!=null &&!"".equals(rs.getObject("FLocalAmount").toString()))
			        		 mp.put("flocalamount", rs.getObject("FLocalAmount").toString());
						 list.add(mp);
					}
				} 
			} catch (BOSException e) {
 				e.printStackTrace();
			} catch (SQLException e) {
 				e.printStackTrace();
			}
			
		}
		return list; 
	}
	public static Map<String,Object> getPaymentBillMap(Context ctx,String pId){
		 Map<String,Object> mp = null ;
		if(VerifyUtil.notNull(pId) ){
			String sql ="select a.FID,a.FNUMBER,to_char(a.fbizdate,'yyyy-MM-dd') FBIZDATE "+
			" ,a.FCOMPANYID fcompanyorgunitid,c.FNUMBER fcompanyorgnumber,c.FNAME_L2 fcompanyorgname, "+
			" d.FNAME_L2 fbankname,e.FBANKACCOUNTNUMBER fbankaccountnumber,f.FNAME_L2 fasstacttypename, "+
			" a.FPayeeID ,a.FPayeeNumber,a.FPayeeName,a.FPayeeBank,a.FPayeeAccountBank,g.FNAME_L2 fpersonname, "+
			" decode(FBillStatus,10,'保存',11,'已提交',12,'已审批',14,'已收款',15,'已付款',6,'审批中',8,'已审核','未知') FBillStatus, "+
			" c.FNAME_L2 fcurrencyname,a.FAmount,a.FLocalAmount,a.FUsage,a.FDESCRIPTION "+
			" from T_CAS_PAYMENTBILL a "+
			" inner join t_org_company c on a.FCOMPANYID = c.FID "+
			" inner join T_BD_Bank d on a.FPayerBankID = d.FID "+
			" inner join T_BD_AccountBanks e on a.FPayerAccountBankID = e.FID "+
			" inner join T_BD_AsstActType f on a.FPayeeTypeID = f.FID "+
			" left join T_BD_Person g on a.FPERSONID = g.FID "+
			" inner join T_BD_Currency h on a.FCurrencyID = h.FID "+
			" where a.FID='"+pId+"' "; 
			try {
				IRowSet rs = DbUtil.executeQuery(ctx, sql);
				  if (rs.next()){
					  mp = new HashMap<String,Object>();
					  if(rs.getObject("FID")!=null &&!"".equals(rs.getObject("FID").toString()))
			        		 mp.put("fid", rs.getObject("FID").toString());
					  if(rs.getObject("FNUMBER")!=null &&!"".equals(rs.getObject("FNUMBER").toString()))
			        		 mp.put("fnumber", rs.getObject("FNUMBER").toString());
					  if(rs.getObject("FBIZDATE")!=null &&!"".equals(rs.getObject("FBIZDATE").toString()))
			        		 mp.put("fbizdate", rs.getObject("FBIZDATE").toString());
			         if(rs.getObject("fcompanyorgunitid")!=null &&!"".equals(rs.getObject("fcompanyorgunitid").toString()))
			        	 	mp.put("fcompanyorgunitid", rs.getObject("fcompanyorgunitid").toString()); 		
			         if(rs.getObject("fcompanyorgnumber")!=null &&!"".equals(rs.getObject("fcompanyorgnumber").toString()))
			        	 	mp.put("fcompanyorgnumber", rs.getObject("fcompanyorgnumber").toString()); 
		    	     if(rs.getObject("fcompanyorgname")!=null &&!"".equals(rs.getObject("fcompanyorgname").toString()))
				         	mp.put("fcompanyorgname", rs.getObject("fcompanyorgname").toString());  	
		    	     
		    	     if(rs.getObject("fbankname")!=null &&!"".equals(rs.getObject("fbankname").toString()))
				         mp.put("fbankname", rs.getObject("fbankname").toString());  	
		    	     if(rs.getObject("fbankaccountnumber")!=null &&!"".equals(rs.getObject("fbankaccountnumber").toString()))
				         mp.put("fbankaccountnumber", rs.getObject("fbankaccountnumber").toString());  	
		    	     if(rs.getObject("fasstacttypename")!=null &&!"".equals(rs.getObject("fasstacttypename").toString()))
				         mp.put("fasstacttypename", rs.getObject("fasstacttypename").toString());  	
		    	    
		    	     if(rs.getObject("FPayeeID")!=null &&!"".equals(rs.getObject("FPayeeID").toString()))
				         mp.put("fpayeeid", rs.getObject("FPayeeID").toString());  	
		    	     if(rs.getObject("FPayeeNumber")!=null &&!"".equals(rs.getObject("FPayeeNumber").toString()))
				         mp.put("fpayeenumber", rs.getObject("FPayeeNumber").toString());  	
		    	     if(rs.getObject("FPayeeName")!=null &&!"".equals(rs.getObject("FPayeeName").toString()))
				         mp.put("fpayeename", rs.getObject("FPayeeName").toString());  	
		    	     if(rs.getObject("FPayeeBank")!=null &&!"".equals(rs.getObject("FPayeeBank").toString()))
				         mp.put("fpayeebank", rs.getObject("FPayeeBank").toString());  		    	     
  			    	 if(rs.getObject("FPayeeAccountBank")!=null &&!"".equals(rs.getObject("FPayeeAccountBank").toString()))
					    mp.put("fpayeeaccountbank", rs.getObject("FPayeeAccountBank").toString());   
  			    	 if(rs.getObject("fpersonname")!=null &&!"".equals(rs.getObject("fpersonname").toString()))
 					    mp.put("fpersonname", rs.getObject("fpersonname").toString());    
  			    	 
  			    	 if(rs.getObject("FBillStatus")!=null &&!"".equals(rs.getObject("FBillStatus").toString()))
  					    mp.put("fbillstatus", rs.getObject("FBillStatus").toString());  
  			    	 
  			    	 if(rs.getObject("fcurrencyname")!=null &&!"".equals(rs.getObject("fcurrencyname").toString()))
   					    mp.put("fcurrencyname", rs.getObject("fcurrencyname").toString());  
  			    	 
  			    	 if(rs.getObject("FAmount")!=null &&!"".equals(rs.getObject("FAmount").toString()))
  					    mp.put("ftotalamount", rs.getObject("FAmount").toString());   
  			    	 if(rs.getObject("FLocalAmount")!=null &&!"".equals(rs.getObject("FLocalAmount").toString()))
  					    mp.put("ftotallocalamount", rs.getObject("FLocalAmount").toString());
  			    	 
  			    	 if(rs.getObject("FUsage")!=null &&!"".equals(rs.getObject("FUsage").toString()))
   					    mp.put("fusage", rs.getObject("FUsage").toString());  
  			    	 if(rs.getObject("FDESCRIPTION")!=null &&!"".equals(rs.getObject("FDESCRIPTION").toString()))
   					    mp.put("fdescription", rs.getObject("FDESCRIPTION").toString());  
				  } 
				
			} catch (BOSException e) {
 				e.printStackTrace();
			} catch (SQLException e) {
 				e.printStackTrace();
			}  
		}
		return mp;
	}
	private static PaymentBillInfo createInfo(Context ctx, BaseFIDTO m,String busCode )
    throws BOSException, EASBizException
  {
		   PaymentBillInfo payInfo = new PaymentBillInfo();
		   payInfo.setSourceType(SourceTypeEnum.AP);
	         payInfo.setDescription("无");
	         payInfo.setIsExchanged(false);
	         payInfo.setExchangeRate(new BigDecimal("1.00"));
	         payInfo.setLastExhangeRate(new BigDecimal("0.00"));
	         payInfo.setIsInitializeBill(false);
	         CurrencyInfo currency = new CurrencyInfo();
			 currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
	         payInfo.setCurrency(currency);
	         payInfo.setFiVouchered(false);
	         payInfo.setIsLanding(false);
//	         AsstActTypeInfo actType = new AsstActTypeInfo();
//	         actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));
//	         payInfo.setPayeeType(actType);
	         
			 AsstActTypeInfo actType  = new AsstActTypeInfo();
	 		 actType.setId(BOSUuid.read("YW3xsAEJEADgAAVEwKgTB0c4VZA="));
	 		 payInfo.setPayeeType(actType);
	 		 ObjectUuidPK suppPK = new ObjectUuidPK(m.getFsupplierid());
			 SupplierInfo supplierInfo =  SupplierFactory.getLocalInstance(ctx).getSupplierInfo((IObjectPK)suppPK);
	 
	 		    
	         payInfo.setPayeeID(supplierInfo.getId().toString());
	         payInfo.setPayeeName(supplierInfo.getName());
	         payInfo.setPayeeNumber(supplierInfo.getNumber());
	         payInfo.setBankAcctName(supplierInfo.getName());
	         payInfo.setPayeeBank(m.getFbank());
	         payInfo.setPayeeAccountBank(m.getFbankaccount());
	         payInfo.put("kaihuhang", m.getFbank());
	         payInfo.put("yinhangzhanghao",m.getFbankaccount());
		     
		     
	         payInfo.setIsImport(false);
	         payInfo.setIsNeedPay(true);
	         payInfo.setIsReverseLockAmount(true);
	         payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);
	         PaymentBillTypeInfo billType = new PaymentBillTypeInfo();
	         billType.setId(BOSUuid.read("NLGLdwEREADgAAHcwKgSRj6TKVs="));
	     //    PaymentBillTypeInfo billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo((IObjectPK)new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
	         payInfo.setPayBillType(billType);
	         
	         PaymentTypeInfo paymentTypeInfo = new PaymentTypeInfo();
	         billType.setId(BOSUuid.read("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));

	        // PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
	         payInfo.setPaymentType(paymentTypeInfo);
	         SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
	           .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
	         payInfo.setSettlementType(settlementTypeInfo);
	      //   payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());
	         ObjectUuidPK orgPK = new ObjectUuidPK(m.getFcompanyorgunitid());
	         CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
	         payInfo.setCompany(company);
	         System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
	         SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
			    Date bizDate = new Date();
			    try {
					bizDate = formmat.parse(m.getFbizdate());
				} catch (ParseException e) {
		 			e.printStackTrace();
				}
			payInfo.setBizDate(bizDate);
			payInfo.setBillDate(new Date());
			payInfo.setExchangeRate(new BigDecimal("1.00"));	
		    payInfo.setActPayAmtVc(BigDecimal.ZERO);
		    payInfo.setActPayAmt(m.getFtotaltaxamount());
		    payInfo.setActPayLocAmtVc(BigDecimal.ZERO);
		    payInfo.setAmount(m.getFtotalamount());
		    payInfo.setLocalAmt(m.getFtotalamount());
		    payInfo.setAccessoryAmt(0);
		    payInfo.setBgAmount(BigDecimal.ZERO);
		    payInfo.setVerifiedAmt(BigDecimal.ZERO);
		    payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);
		    payInfo.setUnVerifiedAmt(m.getFtotaltaxamount());
		    payInfo.setUnVerifiedAmtLoc(m.getFtotaltaxamount());
		    payInfo.setBgCtrlAmt(m.getFtotaltaxamount());
		    payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);  
 	         
		   return payInfo;
  }
	
	private static PaymentBillEntryInfo createEntryInfo(Context ctx, BaseFIDetailDTO dvo ,String busCode)
    throws EASBizException, BOSException
  {
		PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal taxPirce = dvo.getFtaxprice();
 		 BigDecimal taxAmount = dvo.getFtaxamount();
		 BigDecimal qty = dvo.getFqty();
		 BigDecimal amount = dvo.getFamount();
		 entryInfo.setAmount(amount);
         entryInfo.setAmountVc(BigDecimal.ZERO);
         entryInfo.setLocalAmt(amount);
         entryInfo.setLocalAmtVc(BigDecimal.ZERO);
         entryInfo.setUnVcAmount(amount);
         entryInfo.setUnVcLocAmount(amount);
         entryInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);
         entryInfo.setRebate(BigDecimal.ZERO);
         entryInfo.setRebateAmtVc(BigDecimal.ZERO);
         entryInfo.setRebateLocAmt(BigDecimal.ZERO);
         entryInfo.setRebateLocAmtVc(BigDecimal.ZERO);
         entryInfo.setActualAmt(amount);
         entryInfo.setActualAmtVc(BigDecimal.ZERO);
         entryInfo.setActualLocAmt(amount);
         entryInfo.setActualLocAmtVc(BigDecimal.ZERO);
         entryInfo.setUnLockAmt(amount);
         entryInfo.setUnLockLocAmt(amount);
         entryInfo.setLockAmt(BigDecimal.ZERO);
         entryInfo.setPayableDate(new Date());
		return entryInfo;
  }
 
}
