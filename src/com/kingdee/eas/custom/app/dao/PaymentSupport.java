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
import com.kingdee.eas.basedata.assistant.PaymentTypeFactory;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.assistant.SettlementTypeFactory;
import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
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
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
import com.kingdee.eas.fi.cas.PaymentBillFactory;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fi.cas.PaymentBillTypeFactory;
import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
import com.kingdee.eas.fi.cas.SourceTypeEnum;
import com.kingdee.eas.framework.CoreBaseInfo;

public class PaymentSupport {

	
	 public String syncPaymentBillFormOA(Context ctx, String database) throws BOSException {
	     String sql = null;
	     sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer,  bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime  from eas_lolkk_bx bx   where bx.PURCHTYPE = '08' and bx.eassign = 0";
	     Calendar cal = Calendar.getInstance();
	     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
	     String fid = null;
	     try {
	       System.out.println("--------------------------" + list.size());
	       for (Map<String, Object> map : list) {
	         fid = map.get("ID").toString();
	         PaymentBillInfo payInfo = new PaymentBillInfo();
	         if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
	           System.out.println("_--------------------------------------" + map.get("FNUMBER"));
 	           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据保存失败," + payInfo.getNumber() + "的公司编码为空");
	           continue;
	         } 
	         if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("") && 
	           PaymentBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "'")) {
 	           continue;
	         } 
	         payInfo.setSourceType(SourceTypeEnum.AP);
	         payInfo.setDescription("无");
	         payInfo.setIsExchanged(false);
	         payInfo.setExchangeRate(new BigDecimal("1.00"));
	         payInfo.setLastExhangeRate(new BigDecimal("0.00"));
	         payInfo.setIsInitializeBill(false);
	         CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
	         payInfo.setCurrency(currency);
	         payInfo.setFiVouchered(false);
	         payInfo.setIsLanding(false);
	         AsstActTypeInfo actType = new AsstActTypeInfo();
	         actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));
	         payInfo.setPayeeType(actType);
	         payInfo.setIsImport(false);
	         payInfo.setIsNeedPay(true);
	         payInfo.setIsReverseLockAmount(true);
	         payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);
	         PaymentBillTypeInfo billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo((IObjectPK)new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
	         payInfo.setPayBillType(billType);
	         PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
	         payInfo.setPaymentType(paymentTypeInfo);
	         SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
	           .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
	         payInfo.setSettlementType(settlementTypeInfo);
	         payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());
	         ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY").toString());
	         CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
	         payInfo.setCompany(company);
	         System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
	         PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='" + map.get("APPLYER").toString() + "'").get(0);
	         payInfo.setPayeeID(person.getId().toString());
	         payInfo.setPayeeName(person.getName());
	         payInfo.setPayeeNumber(person.getNumber());
	         payInfo.setPayeeBank(map.get("APPLYERBANK").toString());
	         payInfo.setPayeeAccountBank(map.get("APPLYERBANKNUM").toString());
	         payInfo.put("kaihuhang", map.get("APPLYERBANK").toString());
	         payInfo.put("yinhangzhanghao", map.get("APPLYERBANKNUM").toString());
	         payInfo.setBankAcctName(person.getName());
	         AdminOrgUnitInfo admin = null;
	         if (map.get("DEPT") != null && !"".equals(map.get("DEPT").toString())) {
	           admin = AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo((IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
	           payInfo.setAdminOrgUnit(admin);
	         } 
	         SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	         Date bizDate = null;
	         try {
	           if (map.get("BIZDATE") != null && !"".equals(map.get("BIZDATE").toString())) {
	             bizDate = formmat.parse(map.get("BIZDATE").toString());
	           } else {
	             bizDate = new Date();
	           } 
	         } catch (ParseException e) {
	           e.printStackTrace();
	         } 
	         payInfo.setBizDate(bizDate);
	         payInfo.setBillDate(new Date());
	         BigDecimal totalAmount = new BigDecimal(map.get("AMOUNT").toString());
	         String entrySql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ='" + map.get("ID").toString() + "' ";
	         List<Map<String, Object>> enrtyList = EAISynTemplate.query(ctx, database, entrySql.toString());
	         if (enrtyList != null && enrtyList.size() > 0) {
	           for (Map<String, Object> entryMap : enrtyList) {
	             BigDecimal amount = new BigDecimal(entryMap.get("AMOUNT").toString());
	             PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
	             if (entryMap.get("PAYTYPECODE") != null && !"".equals(entryMap.get("PAYTYPECODE").toString())) {
	               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
	                 .getExpenseTypeInfo("where number ='" + entryMap.get("PAYTYPECODE").toString() + "'");
	               entryInfo.setExpenseType(typeinfo);
	             } else {
	               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
	               entryInfo.setExpenseType(typeinfo);
	             } 
	             entryInfo.setCurrency(currency);
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
	             if (entryMap.get("REMARK") != null)
	               entryInfo.setRemark(entryMap.get("REMARK").toString()); 
	             payInfo.getEntries().addObject((IObjectValue)entryInfo);
	           } 
	         } else {
	           System.out.println("entrty is empty _---------------------------" + map.get("FNUMBER").toString());
 	           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据没有分录");
	           continue;
	         } 
	         payInfo.setActPayAmtVc(BigDecimal.ZERO);
	         payInfo.setActPayAmt(totalAmount);
	         payInfo.setActPayLocAmtVc(BigDecimal.ZERO);
	         payInfo.setAmount(totalAmount);
	         payInfo.setLocalAmt(totalAmount);
	         payInfo.setAccessoryAmt(0);
	         payInfo.setBgAmount(BigDecimal.ZERO);
	         payInfo.setVerifiedAmt(BigDecimal.ZERO);
	         payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);
	         payInfo.setUnVerifiedAmt(totalAmount);
	         payInfo.setUnVerifiedAmtLoc(totalAmount);
	         payInfo.setBgCtrlAmt(totalAmount);
	         payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);
	         PaymentBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)payInfo);
 	       } 
	     } catch (EASBizException e) {
	       e.printStackTrace();
	       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.PaymentBillToMid, String.valueOf(fid) + "单据保存失败", e.getMessage());
	       String msg = "运行失败，异常是：" + e.toString();
	       System.out.println("--------------------" + msg);
	       return msg;
	     }
		return fid; 
 	   }
}
