package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.kingdee.eas.basedata.person.PersonFactory;
import com.kingdee.eas.basedata.person.PersonInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.cp.bc.ExpenseTypeFactory;
import com.kingdee.eas.cp.bc.ExpenseTypeInfo;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.fi.ap.IOtherBill;
import com.kingdee.eas.fi.ap.OtherBillFactory;
import com.kingdee.eas.fi.ap.OtherBillentryInfo;
import com.kingdee.eas.fi.ar.OtherBillInfo;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
import com.kingdee.eas.fi.cas.PaymentBillFactory;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fi.cas.PaymentBillTypeFactory;
import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
import com.kingdee.eas.fi.cas.SourceTypeEnum;
import com.kingdee.eas.framework.CoreBaseInfo;

public class PaymentSupport {

	
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
	         ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
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
