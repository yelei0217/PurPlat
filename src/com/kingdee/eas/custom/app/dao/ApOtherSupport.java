package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyFactory;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeFactory;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.assistant.SettlementTypeFactory;
import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
import com.kingdee.eas.basedata.master.account.AccountViewFactory;
import com.kingdee.eas.basedata.master.account.AccountViewInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeFactory;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.AdminOrgUnitFactory;
import com.kingdee.eas.basedata.org.AdminOrgUnitInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CostCenterOrgUnitFactory;
import com.kingdee.eas.basedata.org.CostCenterOrgUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.person.PersonFactory;
import com.kingdee.eas.basedata.person.PersonInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeFactory;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.cp.bc.ExpenseTypeFactory;
import com.kingdee.eas.cp.bc.ExpenseTypeInfo;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.app.unit.SupplyInfoLogUnit;
import com.kingdee.eas.fi.ap.IOtherBill;
import com.kingdee.eas.fi.ap.OtherBillFactory;
import com.kingdee.eas.fi.ap.OtherBillInfo;
import com.kingdee.eas.fi.ap.OtherBillPlanInfo;
import com.kingdee.eas.fi.ap.OtherBillType;
import com.kingdee.eas.fi.ap.OtherBillentryInfo;
import com.kingdee.eas.fi.ap.VerificateBillTypeEnum;
import com.kingdee.eas.fi.ar.ArApBillBaseInfo;
import com.kingdee.eas.fi.ar.BillStatusEnum;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.util.app.DbUtil;


/**
 * 
 * @author LEI.YE	
 *  应付单 同步
 *
 */
public class ApOtherSupport {

	public static void doInsertBill(Context ctx,BaseFIDTO m,String busCode){
		try {
				OtherBillInfo info = createInfo(ctx,m,busCode);
				IOtherBill ibiz = OtherBillFactory.getLocalInstance(ctx);
				IObjectPK pk = ibiz.save(info);
				ibiz.submit(pk);
				ibiz.audit(pk); 
			} catch (EASBizException e) {
	 		e.printStackTrace();
			} catch (BOSException e) {
				e.printStackTrace();
			}
	}
	
	private static OtherBillInfo createInfo(Context ctx, BaseFIDTO m,String busCode )
    throws BOSException, EASBizException
  {
		OtherBillInfo info = new OtherBillInfo();
		   info.setIsReversed(false);
	       info.setIsReverseBill(false);
	       info.setIsTransBill(false);
	       info.setIsAllowanceBill(false);
	       info.setIsImportBill(false);
	       info.setIsExchanged(false);
	       info.setIsInitializeBill(false);
	       
	       PaymentTypeInfo paymentTypeInfo = new PaymentTypeInfo();
	       paymentTypeInfo.setId(BOSUuid.read("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
		   info.setPaymentType(paymentTypeInfo);
		   info.setBillType(OtherBillType.OtherPay);
		   CurrencyInfo currency = new CurrencyInfo();
		   currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		   info.setCurrency(currency); 
		   ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
 	       CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
		   info.setCompany(xmcompany); 
	       SettlementTypeInfo settlementTypeInfo = new SettlementTypeInfo();
	       settlementTypeInfo.setId(BOSUuid.read("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
		   info.setSettleType(settlementTypeInfo);
		    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
		    Date bizDate = new Date();
		    try {
				bizDate = formmat.parse(m.getFbizdate());
			} catch (ParseException e) {
	 			e.printStackTrace();
			}
		    info.setBizDate(bizDate);
 		    info.setBillDate(new Date());
 		    info.setExchangeRate(new BigDecimal("1.00"));		   
		   // 往来类型： 供应商
 		    AsstActTypeInfo actType  = new AsstActTypeInfo();
 		    actType.setId(BOSUuid.read("YW3xsAEJEADgAAVEwKgTB0c4VZA="));
 		    info.setAsstActType(actType);
 		    
 		    ObjectUuidPK suppPK = new ObjectUuidPK(m.getFsupplierid());
		   	SupplierInfo supplierInfo =  SupplierFactory.getLocalInstance(ctx).getSupplierInfo((IObjectPK)suppPK);
	        info.setAsstActID(supplierInfo.getId().toString());
	        info.setAsstActName(supplierInfo.getName());
	        info.setAsstActNumber(supplierInfo.getNumber());
	        //VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.PurInvoice;
	        
	        info.setBillType(OtherBillType.InvoiceBill);
	        
  		    BizTypeInfo bizTypeInfo = new BizTypeInfo();
		    bizTypeInfo.setId(BOSUuid.read("d8e80652-0106-1000-e000-04c5c0a812202407435C"));
		    info.setBizType(bizTypeInfo);

		    AccountViewInfo accountInfo = new AccountViewInfo();
		    accountInfo.setId(BOSUuid.read("Qutv0uolQN6fq9AZuxyUu52pmCY="));

		    for (BaseFIDetailDTO dvo : m.getDetails())
		    {
		    	OtherBillentryInfo entryInfo = createEntryInfo(ctx,dvo,busCode);
		    	entryInfo.setCompany(m.getFstorageorgunitid());
		        entryInfo.setAccount(accountInfo);
		        entryInfo.setParent((ArApBillBaseInfo)info);
		        entryInfo.setBillDate(bizDate);
		        entryInfo.setPayableDate(bizDate);
		        
		        info.getEntries().addObject((IObjectValue)entryInfo);
 		    }
		    
		    info.setAmount(m.getFtotalamount());
		    info.setTotalTax(m.getFtotaltax());
		    info.setTotalTaxAmount(m.getFtotaltaxamount());
		    info.setTotalAmount(m.getFtotalamount());
		    info.setAmountLocal(m.getFtotalamount());
		    info.setTotalAmountLocal(m.getFtotaltaxamount());
		    info.setTotalTaxLocal(m.getFtotaltax());
		    info.setThisApAmount(m.getFtotalamount());
		    info.setUnVerifyAmount(m.getFtotalamount());
		    info.setUnVerifyAmountLocal(m.getFtotalamount());
 
		    OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
		    otherBillPlanInfo.setLockAmount(m.getFtotalamount());
		    otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
		    otherBillPlanInfo.setRecievePayAmount(m.getFtotalamount());
		    otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
		    info.getPayPlan().add(otherBillPlanInfo);
		    
		return info;
  }
	
	private static OtherBillentryInfo createEntryInfo(Context ctx, BaseFIDetailDTO dvo ,String busCode)
    throws EASBizException, BOSException
  {
		 OtherBillentryInfo entryInfo = new OtherBillentryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal taxPirce = dvo.getFtaxprice();
 		 BigDecimal taxAmount = dvo.getFtaxamount();
		 BigDecimal qty = dvo.getFqty();
		 BigDecimal amount = dvo.getFamount();
		 
		  entryInfo.setPrice(price);
          entryInfo.setTaxPrice(taxPirce);
          entryInfo.setActualPrice(dvo.getFactualprice());
          entryInfo.setRealPrice(price);
          entryInfo.setQuantity(qty);
          entryInfo.setBaseQty(qty);
          entryInfo.setDiscountRate(BigDecimal.ZERO);
          entryInfo.setDiscountAmount(BigDecimal.ZERO);
          entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
          entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
          entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
          entryInfo.setAssistQty(BigDecimal.ZERO);
          entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
          entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
          entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
          entryInfo.setVerifyQty(BigDecimal.ZERO);
          entryInfo.setLockVerifyQty(BigDecimal.ZERO);
          entryInfo.setLocalUnwriteOffAmount(amount);
          entryInfo.setAmount(amount);
          entryInfo.setAmountLocal(amount);
          entryInfo.setTaxAmount(dvo.getFtax());
          entryInfo.setTaxAmountLocal(dvo.getFtax());
          entryInfo.setTaxRate(dvo.getFtaxrate());
          entryInfo.setUnVerifyAmount(taxAmount);
          entryInfo.setUnVerifyAmountLocal(taxAmount);
          entryInfo.setLockUnVerifyAmt(taxAmount);
          entryInfo.setLockUnVerifyAmtLocal(taxAmount);
          entryInfo.setApportionAmount(BigDecimal.ZERO);
          entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
          entryInfo.setUnApportionAmount(taxAmount);
          entryInfo.setRecievePayAmount(taxAmount);
          entryInfo.setRecievePayAmountLocal(taxAmount);
          entryInfo.setUnInvoiceReqAmount(taxAmount);
          entryInfo.setUnInvoiceReqAmountLocal(taxAmount);
 
          IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
 
	  	   MaterialInfo material = null;
	  	   IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
	  	   material = imaterial.getMaterialInfo(pk);
	       entryInfo.setMaterial(material);

	       entryInfo.setMaterialName(material.getName());
	       entryInfo.setMaterialModel(material.getModel());
	       
	  	   pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
	  	   MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	  	   entryInfo.setMeasureUnit(unitInfo);
	  	    
	  	    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())));
	  	    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	  	   entryInfo.setBaseUnit(baseUnitInfo);
	  
          entryInfo.setUnwriteOffBaseQty(qty);
		 return entryInfo;
  }
//	 private String apOtherNotShichang(Context ctx, String database, Map<String, Object> map) throws BOSException {
//	     String fid = null;
//	     try {
//	       String faccount = "2241.96";
//	       String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	       Boolean addOrUpdate = Boolean.valueOf(false);
//	       fid = map.get("ID").toString();
//	       OtherBillInfo info = info = new OtherBillInfo();
//	       System.out.println("_--------------------------------------" + map.get("FNUMBER") + "====" + map.get("COMPANY") + "-----" + map.get("SUPPLIER"));
//	       info.setIsReversed(false);
//	       info.setIsReverseBill(false);
//	       info.setIsTransBill(false);
//	       info.setIsAllowanceBill(false);
//	       info.setIsImportBill(false);
//	       info.setIsExchanged(false);
//	       info.setIsInitializeBill(false);
//	       PaymentTypeInfo paymentTypeInfo = 
//	         PaymentTypeFactory.getLocalInstance(ctx)
//	         .getPaymentTypeInfo(
//	           (IObjectPK)new ObjectUuidPK(
//	             "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
//	       info.setPaymentType(paymentTypeInfo);
//	       info.setBillType(OtherBillType.OtherPay);
//	       CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
//	         .getCurrencyCollection("where number='BB01'").get(0);
//	       ObjectUuidPK orgPK = new ObjectUuidPK(
//	           map.get("COMPANY").toString());
//	       CompanyOrgUnitInfo xmcompany = 
//	         CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
//	       info.setCompany(xmcompany);
//	       System.out.println("------------------所属公司：" + 
//	           xmcompany.getId() + "----" + xmcompany.getName());
//	       AdminOrgUnitInfo admin = null;
//	       if (map.get("DEPT") != null && !"".equals(map.get("DEPT"))) {
//	         admin = 
//	           AdminOrgUnitFactory.getLocalInstance(ctx)
//	           .getAdminOrgUnitInfo(
//	             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
//	         info.setAdminOrgUnit(admin);
//	         CostCenterOrgUnitInfo CostCenter = 
//	           CostCenterOrgUnitFactory.getLocalInstance(ctx)
//	           .getCostCenterOrgUnitInfo(
//	             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
//	         info.setCostCenter(CostCenter);
//	       } 
//	       if (map.get("YHZH") != null && !"".equals(map.get("YHZH").toString()))
//	         info.put("yinhangzhanghao", map.get("YHZH")); 
//	       if (map.get("APPLYERBANKNUM") != null && !"".equals(map.get("APPLYERBANKNUM").toString()))
//	         info.setRecAccountBank(map.get("APPLYERBANKNUM").toString()); 
//	       if (map.get("KHH") != null && !"".equals(map.get("KHH").toString()))
//	         info.put("kaihuhang", map.get("KHH")); 
//	       if (map.get("APPLYERBANK") != null && !"".equals(map.get("APPLYERBANK").toString()))
//	         info.setRecBank(map.get("APPLYERBANK").toString()); 
//	       PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
//	       info.setPurOrg(purchaseorginfo);
//	       String personId = "";
//	       ObjectUuidPK objectUuidPK1 = new ObjectUuidPK(BOSUuid.read(personId));
//	       PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)objectUuidPK1);
//	       info.setPerson(person);
//	       SettlementTypeInfo settlementTypeInfo = 
//	         SettlementTypeFactory.getLocalInstance(ctx)
//	         .getSettlementTypeInfo(
//	           (IObjectPK)new ObjectUuidPK(
//	             "e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
//	       info.setSettleType(settlementTypeInfo);
//	       SimpleDateFormat formmat = new SimpleDateFormat(
//	           "yyyy-MM-dd hh:mm:ss");
//	       Date bizDate = null;
//	       try {
//	         if (map.get("BIZDATE") != null && 
//	           !"".equals(map.get("BIZDATE").toString())) {
//	           bizDate = formmat.parse(map.get("BIZDATE").toString());
//	         } else {
//	           bizDate = new Date();
//	         } 
//	       } catch (ParseException e) {
//	         e.printStackTrace();
//	       } 
//	       info.setBizDate(bizDate);
//	       info.setBillDate(new Date());
//	       info.setCurrency(currency);
//	       info.setExchangeRate(new BigDecimal("1.00"));
//	       OtherBillType otherBillType = null;
//	       otherBillType = OtherBillType.OtherPay;
//	       info.setBillType(otherBillType);
//	       info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
//	       info.put("OAcaigoushenqingdanjine", map.get("AMOUNT"));
//	       String jk = null;
//	       if (map.get("ISLOAN").toString().equals("0")) {
//	         jk = "否";
//	       } else if (map.get("ISLOAN").toString().equals("1")) {
//	         jk = "是";
//	       } 
//	       info.put("shifoujiekuan", jk);
//	       String zlf = null;
//	       if (map.get("ISRENTALFEE").toString().equals("0")) {
//	         zlf = "否";
//	       } else if (map.get("ISRENTALFEE").toString().equals("1")) {
//	         zlf = "是";
//	       } 
//	       info.put("shifouzulinfei", zlf);
//	       String djlx = null;
//	       String isAdminDept = "0";
//	       String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部", "咨询部", "营销中心" };
//	       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
//	         djlx = "费用报销";
//	         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
//	         faccount = "2241.97";
//	         if (admin != null && admin.getName() != null)
//	           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
//	             isAdminDept = "2";
//	           } else {
//	             isAdminDept = "1";
//	           }  
//	       } else if (map.get("PURCHTYPE").toString().equals("02")) {
//	         djlx = "采购付款";
//	         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	         faccount = "2241.96";
//	       } else if (map.get("PURCHTYPE").toString().equals("03")) {
//	         djlx = "市场投放";
//	         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	         faccount = "2241.96";
//	       } else if (map.get("PURCHTYPE").toString().equals("04")) {
//	         djlx = "差旅费报销";
//	         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
//	         faccount = "2241.97";
//	         if (admin != null && admin.getName() != null)
//	           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
//	             isAdminDept = "2";
//	           } else {
//	             isAdminDept = "1";
//	           }  
//	       } else if (map.get("PURCHTYPE").toString().equals("05") || map.get("PURCHTYPE").toString().equals("10")) {
//	         djlx = "对外付款";
//	         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	         faccount = "2241.96";
//	       } else if (map.get("PURCHTYPE").toString().equals("06")) {
//	         djlx = "合同专用付款";
//	         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	         faccount = "2241.96";
//	       } else if (map.get("PURCHTYPE").toString().equals("07")) {
//	         djlx = "技加工";
//	         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
//	         faccount = "2241.96";
//	       } 
//	       info.put("yingfudanjuleixing", djlx);
//	       info.put("shifouguanlibumen", isAdminDept);
//	       info.put("fapiaohao", "OA0000");
//	       String companytype = SupplyInfoLogUnit.getComapnyTypeByNumber(ctx, xmcompany.getNumber());
//	       if (companytype != null && !"".equals(companytype))
//	         info.put("CompanyType", companytype); 
//	       AccountViewInfo accountInfo = new AccountViewInfo();
//	       accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
//	           "where number = '" + faccount + "' and companyID ='" + map.get("COMPANY").toString() + "' ");
//	       AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo((IObjectPK)new ObjectUuidPK(actTypePk));
//	       info.setAsstActType(actType);
//	       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
//	         info.setAsstActID(person.getId().toString());
//	         info.setAsstActName(person.getName());
//	         info.setAsstActNumber(person.getNumber());
//	       } else {
//	         try {
//	           SupplierInfo supplierInfo = 
//	             SupplierFactory.getLocalInstance(ctx).getSupplierInfo(
//	               " where number='" + map.get("SUPPLIERID") + "'");
//	           info.setAsstActID(supplierInfo.getId().toString());
//	           info.setAsstActName(supplierInfo.getName());
//	           info.setAsstActNumber(supplierInfo.getNumber());
//	         } catch (Exception e) {
// 	           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
//	               DateBasetype.OA_OtherBill, info.getNumber(), 
//	               info.getString("OA_OtherBill"), "没有该供应商编码");
//	           return "";
//	         } 
//	       } 
//	       VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
//	       info.setSourceBillType(billTypeEnum);
//	       BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx)
//	         .getBizTypeInfo("where number = 110");
//	       info.setBizType(bizTypeInfo);
//	       String sql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid =" + 
//	         map.get("ID");
//	       List<Map<String, Object>> list1 = EAISynTemplate.query(ctx, 
//	           database, sql);
//	       BigDecimal totalAmount = new BigDecimal(0);
//	       BigDecimal totalyjk = new BigDecimal(0);
//	       BigDecimal totalytbk = new BigDecimal(0);
//	       if (list1 != null && list1.size() > 0) {
//	         for (Map<String, Object> map1 : list1) {
//	           OtherBillentryInfo entryInfo = new OtherBillentryInfo();
//	           if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
//	             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='" + map1.get("PAYTYPECODE").toString() + "'");
//	             entryInfo.setExpenseItem(typeinfo);
//	           } else {
//	             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
//	             entryInfo.setExpenseItem(typeinfo);
//	           } 
//	           BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
//	           BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
//	           BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
//	           if (price.compareTo(BigDecimal.ZERO) < 0) {
//	             price = price.negate();
//	             qty = qty.negate();
//	           } 
//	           entryInfo.setPrice(price);
//	           entryInfo.setTaxPrice(price);
//	           entryInfo.setActualPrice(price);
//	           entryInfo.setRealPrice(price);
//	           entryInfo.setQuantity(qty);
//	           entryInfo.setBaseQty(BigDecimal.ZERO);
//	           entryInfo.setDiscountRate(BigDecimal.ZERO);
//	           entryInfo.setDiscountAmount(BigDecimal.ZERO);
//	           entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
//	           entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
//	           entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
//	           entryInfo.setAssistQty(BigDecimal.ZERO);
//	           entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
//	           entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
//	           entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
//	           entryInfo.setVerifyQty(BigDecimal.ZERO);
//	           entryInfo.setLockVerifyQty(BigDecimal.ZERO);
//	           entryInfo.setLocalUnwriteOffAmount(amount);
//	           entryInfo.setAmount(amount);
//	           entryInfo.setAmountLocal(amount);
//	           entryInfo.setTaxAmount(BigDecimal.ZERO);
//	           entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
//	           entryInfo.setTaxRate(BigDecimal.ZERO);
//	           entryInfo.setUnVerifyAmount(amount);
//	           entryInfo.setUnVerifyAmountLocal(amount);
//	           entryInfo.setLockUnVerifyAmt(amount);
//	           entryInfo.setLockUnVerifyAmtLocal(amount);
//	           entryInfo.setApportionAmount(BigDecimal.ZERO);
//	           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
//	           entryInfo.setUnApportionAmount(amount);
//	           entryInfo.setRecievePayAmount(amount);
//	           entryInfo.setRecievePayAmountLocal(amount);
//	           entryInfo.setCompany(map.get("COMPANY").toString());
//	           entryInfo.setAccount(accountInfo);
//	           if (map1.get("REMARK") != null)
//	             entryInfo.setRemark(map1.get("REMARK").toString()); 
//	           if (map1.get("YJK") != null && !"".equals(map1.get("YJK").toString()))
//	             totalyjk = totalyjk.add((BigDecimal)map1.get("YJK")); 
//	           if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK").toString()))
//	             totalytbk = totalytbk.add((BigDecimal)map1.get("YTBK")); 
//	           entryInfo.setParent((ArApBillBaseInfo)info);
//	           entryInfo.setUnwriteOffBaseQty(qty);
//	           entryInfo.put("pinpai", map.get("BRAND"));
//	           entryInfo.put("huohao", map.get("ATRNO"));
//	           totalAmount = totalAmount.add(amount);
//	           info.getEntries().addObject((IObjectValue)entryInfo);
//	         } 
//	       } else {
//	         System.out.println("entrty is empty _--------------------------------------" + map.get("FNUMBER"));
// 	         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
//	             DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据没有分录");
//	         return "";
//	       } 
//	       info.setAmount(totalAmount);
//	       info.setTotalTax(BigDecimal.ZERO);
//	       info.setTotalTaxAmount(totalAmount);
//	       info.setTotalAmount(totalAmount);
//	       info.setAmountLocal(totalAmount);
//	       info.setTotalAmountLocal(totalAmount);
//	       info.setTotalTaxLocal(BigDecimal.ZERO);
//	       info.setThisApAmount(totalAmount);
//	       info.setUnVerifyAmount(totalAmount);
//	       info.setUnVerifyAmountLocal(totalAmount);
//	       info.put("yingtuibukuan", totalytbk);
//	       info.put("yuanjiekuan", totalyjk);
//	       OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
//	       otherBillPlanInfo.setLockAmount(totalAmount);
//	       otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
//	       otherBillPlanInfo.setRecievePayAmount(totalAmount);
//	       otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
//	       info.getPayPlan().add(otherBillPlanInfo);
//	       OtherBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
// 	       try {
//	         System.out.println("------------------info所属公司1111：" + 
//	             info.getCompany().getId() + "----" + info.getCompany().getName());
//	         info.setBillStatus(BillStatusEnum.SAVE);
//	         OtherBillFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
//	         System.out.println("------------------info所属公司2222：" + 
//	             info.getCompany().getId() + "----" + info.getCompany().getName());
//	         OtherBillFactory.getLocalInstance(ctx).audit((IObjectPK)new ObjectUuidPK(info.getId().toString()));
//	         if (addOrUpdate.booleanValue()) {
//	           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
//	               DateBasetype.OA_OtherBill, info.getNumber(), 
//	               info.getString("OA_OtherBill"), "单据修改成功");
//	         } else {
//	           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
//	               DateBasetype.OA_OtherBill, info.getNumber(), 
//	               info.getString("OA_OtherBill"), "单据审核成功");
//	         } 
//	       } catch (Exception e2) {
// 	         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
//	             DateBasetype.OA_OtherBill, info.getNumber(), 
//	             info.getString("OA_OtherBill"), "单据保存成功，提交审核失败。");
//	       } 
//	     } catch (EASBizException e) {
//	       e.printStackTrace();
//	       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
//	           DateBasetype.OA_OtherBill, String.valueOf(fid) + "单据保存失败", e.getMessage());
//	     } 
//	     return null;
//	   }
}
