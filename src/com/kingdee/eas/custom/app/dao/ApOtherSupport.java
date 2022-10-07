package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
import com.kingdee.eas.basedata.master.account.AccountViewInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.fi.ap.IOtherBill;
import com.kingdee.eas.fi.ap.OtherBillFactory;
import com.kingdee.eas.fi.ap.OtherBillInfo;
import com.kingdee.eas.fi.ap.OtherBillPlanInfo;
import com.kingdee.eas.fi.ap.OtherBillType;
import com.kingdee.eas.fi.ap.OtherBillentryInfo;
import com.kingdee.eas.fi.ar.ArApBillBaseInfo;


/**
 * 
 * @author LEI.YE	
 *  应付单 同步
 *
 */
public class ApOtherSupport {

	public static void doSaveBill(Context ctx,BaseFIDTO m,String busCode){
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
		   ObjectUuidPK orgPK = new ObjectUuidPK(m.getFcompanyorgunitid());
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
		    	entryInfo.setCompany(m.getFcompanyorgunitid());
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
		    //发票号
		    info.setInvoiceNumber(m.getFinvoicenumber());
		    
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
  
	   		EntityViewInfo view = new EntityViewInfo();
	 	 	FilterInfo filter = new FilterInfo();
	 	 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
	 	  	view.setFilter(filter);
	 	 	
	 	    MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
	 	    MaterialInfo material = materialColl.get(0); 
	 	   entryInfo.setMaterial(material);
	       entryInfo.setMaterialName(material.getName());
	       entryInfo.setMaterialModel(material.getModel());
	       
	       IObjectPK pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
	  	   MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	  	   entryInfo.setMeasureUnit(unitInfo);
	  	    
	  	    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())));
	  	    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	  	   entryInfo.setBaseUnit(baseUnitInfo); 
	  
          entryInfo.setUnwriteOffBaseQty(qty);
		 return entryInfo;
  }
 
}
