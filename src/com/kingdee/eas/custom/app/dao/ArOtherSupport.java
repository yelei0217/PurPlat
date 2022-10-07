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
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.fi.ar.IOtherBill;
import com.kingdee.eas.fi.ar.OtherBillFactory;
import com.kingdee.eas.fi.ar.OtherBillInfo;
import com.kingdee.eas.fi.ar.OtherBillTypeEnum;
import com.kingdee.eas.fi.ar.OtherBillentryInfo;
import com.kingdee.eas.util.app.ContextUtil;

/**
 * 
 * @author LEI.YE
 * 应收单 同步
 */
public class ArOtherSupport {
	
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
		OtherBillInfo billinfo = new OtherBillInfo();
		BigDecimal amount = m.getFtotalamount();
		BigDecimal taxAmount = m.getFtotaltaxamount();
		BigDecimal tax = m.getFtotaltax();
		Date currentDate = new java.util.Date();
		// 创建人
		billinfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// 创建时间
		billinfo.setCreateTime(new java.sql.Timestamp(currentDate.getTime()));
		// 公司ID
		ObjectUuidPK orgpk = new ObjectUuidPK(m.getFcompanyorgunitid());
		
		// 组织单元
		CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(
				ctx).getCompanyOrgUnitInfo(orgpk);
		billinfo.setCompany(xmcompany);
		
		CtrlUnitInfo cuInfo = xmcompany.getCU();
		billinfo.setCU(cuInfo);// 管理单元
		
		SaleOrgUnitInfo saleorg = new SaleOrgUnitInfo();
		saleorg.setId(BOSUuid.read(m.getFcompanyorgunitid().toString()));
		billinfo.setSaleOrg(saleorg);// 销售组织
		
		// 业务日期
		SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
		Date bizDate = currentDate;
		try {
			bizDate = formmat.parse(m.getFbizdate());
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		billinfo.setBizDate(bizDate);
		billinfo.setBillType(OtherBillTypeEnum.InvoiceBill);
		billinfo.setIsInTax(false);
		
		// 单据日期 --当前时间
		billinfo.setBillDate(bizDate);
		// 往来类型 客户
		AsstActTypeInfo asstype = new AsstActTypeInfo();
		asstype.setId(BOSUuid.read("YW3xsAEJEADgAAUWwKgTB0c4VZA="));
		billinfo.setAsstActType(asstype);
		
		ObjectUuidPK cuspk = new ObjectUuidPK(m.getFcustomerid());
		// 获取结算方式实体
		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx).getCustomerInfo(cuspk);
		billinfo.setAsstActID(customer.getId().toString());
		billinfo.setAsstActName(customer.getName());
		billinfo.setAsstActNumber(customer.getNumber());
		
		// 付款方式 赊销
		PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
		paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
		billinfo.setPaymentType(paymenttypeinfo);
		
		CurrencyInfo currency = new CurrencyInfo();
		currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		billinfo.setCurrency(currency);// 人民币
		billinfo.setExchangeRate(BigDecimal.valueOf(1));
		
		billinfo.setAmount(amount);
		billinfo.setAmountLocal(amount);
		billinfo.setUnVerifyAmount(taxAmount);
		billinfo.setUnVerifyAmountLocal(taxAmount);
		billinfo.setTotalAmount(amount);
		billinfo.setLastExhangeRate(m.getFtotalqty());
		
		billinfo.setTotalTax(tax);
		billinfo.setTotalTaxAmount(taxAmount);
		billinfo.setVerifyAmount(BigDecimal.ZERO);
		billinfo.setVerifyAmountLocal(BigDecimal.ZERO);
		billinfo.setIsImportBill(false);
		
		for (BaseFIDetailDTO dvo : m.getDetails())
	    {
			OtherBillentryInfo entryInfo = createEntryInfo(ctx,dvo,busCode);
			entryInfo.setOrderCustomer(customer);// 订货客户
			entryInfo.setOrdCustName(customer.getName());// 订货客户名称
			entryInfo.setOrdCustNumber(customer.getNumber());// 订货客户编码
			
			entryInfo.setServiceCustomer(customer); // 送货客户
			entryInfo.setSerCustName(customer.getName());// 送货客户名称
			entryInfo.setSerCustNumber(customer.getNumber());// 送货客户编码
			
			entryInfo.setRecAsstActID(customer.getId().toString()); // 送货客户
			entryInfo.setRecAsstActNumber(customer.getName());// 送货客户名称
			entryInfo.setRecAsstActNumber(customer.getNumber());// 送货客户编码
			
			entryInfo.setCompany(m.getFcompanyorgunitid());
			entryInfo.setBillDate(currentDate);
			
			billinfo.getEntries().addObject((IObjectValue)entryInfo);
		}
		return billinfo;
  }
	
	private static OtherBillentryInfo createEntryInfo(Context ctx, BaseFIDetailDTO dvo ,String busCode)
    throws EASBizException, BOSException
  {
		OtherBillentryInfo entryInfo = new OtherBillentryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal taxPirce = dvo.getFtaxprice();
		// BigDecimal taxRate = dvo.getFtax();
		 BigDecimal taxAmount = dvo.getFtaxamount();
		 BigDecimal qty = dvo.getFqty();
		 BigDecimal baseqty = dvo.getFbaseqty();
		 BigDecimal amount = dvo.getFamount();
		  entryInfo.setPrice(price);
          entryInfo.setTaxPrice(taxPirce);
          entryInfo.setActualPrice(dvo.getFactualprice());
          entryInfo.setRealPrice(price);
          entryInfo.setQuantity(qty);
          entryInfo.setBaseQty(baseqty);
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
           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
           entryInfo.setRecievePayAmount(taxAmount);
          entryInfo.setRecievePayAmountLocal(taxAmount);
          entryInfo.setUnInvoiceReqAmount(taxAmount);
          entryInfo.setUnInvoiceReqAmountLocal(taxAmount);
          entryInfo.setUnwriteOffBaseQty(baseqty);

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
		return entryInfo;
  }
	
}
