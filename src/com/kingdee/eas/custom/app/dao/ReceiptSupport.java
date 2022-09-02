package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.EASPayTypeInfo;
import com.kingdee.eas.custom.app.dto.base.BaseFIDTO;
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.entity.Receipt;
import com.kingdee.eas.fi.ap.IOtherBill;
import com.kingdee.eas.fi.ap.OtherBillFactory;
import com.kingdee.eas.fi.ar.OtherBillentryInfo;
import com.kingdee.eas.fi.cas.BankCheckStatus;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.IReceivingBill;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fi.cas.ReceivingBillEntryInfo;
import com.kingdee.eas.fi.cas.ReceivingBillFactory;
import com.kingdee.eas.fi.cas.ReceivingBillInfo;
import com.kingdee.eas.fi.cas.ReceivingBillTypeInfo;
import com.kingdee.eas.fi.cas.SettlementStatusEnum;
import com.kingdee.eas.fi.cas.SourceTypeEnum;
import com.kingdee.eas.fi.cas.VcStatusEnum;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.util.app.ContextUtil;

public class ReceiptSupport {

	public static void doInsertBill(Context ctx,BaseFIDTO m,String busCode){
		try {
				ReceivingBillInfo info = createInfo(ctx,m,busCode);
				IReceivingBill ibiz = ReceivingBillFactory.getLocalInstance(ctx);
				ContextUtil.setCurrentFIUnit(ctx, info.getCompany());
				ContextUtil.setCurrentCtrlUnit(ctx, info.getCompany().getCU());
		    	IObjectPK pk=ibiz.save(info);
				pk=ibiz.submit(info);
				Set set=new HashSet();
				set.add(pk.toString());
	 			ibiz.audit(set);
			} catch (EASBizException e) {
	 		e.printStackTrace();
			} catch (BOSException e) {
				e.printStackTrace();
			
			}
	}
	
	// 收款单
	private static ReceivingBillInfo createInfo(Context ctx, BaseFIDTO m,String busCode )
			throws BOSException, EASBizException {
		ReceivingBillInfo rbInfo = new ReceivingBillInfo();

		Date currentDate = new java.util.Date();
		// 创建人
		rbInfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// 创建时间
		rbInfo.setCreateTime(new java.sql.Timestamp(currentDate.getTime()));
		// 公司ID
		ObjectUuidPK orgpk = new ObjectUuidPK(m.getFstorageorgunitid());
		
		// 组织单元
		CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(
				ctx).getCompanyOrgUnitInfo(orgpk);
		rbInfo.setCompany(xmcompany);
		
		CtrlUnitInfo cuInfo = xmcompany.getCU();
		rbInfo.setCU(cuInfo);// 管理单元
		
		// 业务日期
		SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
		Date bizDate = currentDate;
		try {
			bizDate = formmat.parse(m.getFbizdate());
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		rbInfo.setBizDate(bizDate);
		rbInfo.setHasEffected(false);// 是否曾经生效
		rbInfo.setSourceType(SourceTypeEnum.AR);// 源类型
		rbInfo.setSourceSysType(SourceTypeEnum.CASH);// 源系统类型
		//rbInfo.setFundType();
		//币别
		CurrencyInfo currency = new CurrencyInfo();
		currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		rbInfo.setCurrency(currency);
		
		rbInfo.setExchangeRate(BigDecimal.ONE);// 汇率
		rbInfo.setIsExchanged(false);// 是否已经调汇
		rbInfo.setLastExhangeRate(BigDecimal.ONE);// 最后调汇汇率
		rbInfo.setIsCommitSettle(false);// 是否提交结算中心
		rbInfo.setIsInitializeBill(false);// 是否初始化单据
		rbInfo.setFiVouchered(false);// 是否已生成凭证
		rbInfo.setSettlementStatus(SettlementStatusEnum.UNSUBMIT);// 集中结算状态
		rbInfo.setActRecAmt(m.getFtotalamount());// 实收金额合计
		rbInfo.setActRecAmtVc(BigDecimal.ZERO);// 实收金额累计核销
		rbInfo.setActRecLocAmt(m.getFtotaltaxamount());// 实收本位币金额合计
		rbInfo.setActRecLocAmtVc(BigDecimal.ZERO);// 实收本位币金额累计核销
		
		// 往来户
		AsstActTypeInfo actType = new AsstActTypeInfo();
		actType.setId(BOSUuid.read("YW3xsAEJEADgAAUWwKgTB0c4VZA="));
		rbInfo.setPayerType(actType);
		
		ObjectUuidPK cuspk = new ObjectUuidPK(m.getFcustomerid());
		// 获取结算方式实体
		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx).getCustomerInfo(cuspk);
		if (null != customer) {
			rbInfo.setPayerID(customer.getId().toString());
			rbInfo.setPayerName(customer.getName());
			rbInfo.setPayerNumber(customer.getNumber());
		}
		
		rbInfo.setIsImport(false);// 是否导入
		rbInfo.setAmount(m.getFtotalamount());// 金额
		rbInfo.setLocalAmt(m.getFtotalamount());// 金额本位币
		rbInfo.setAccessoryAmt(0);// 附件数
		rbInfo.setIsRelateReceipt(false);// 是否已关联进账单
		rbInfo.setIsBookRL(false);// 是否登记银行日记账
		rbInfo.setBgAmount(BigDecimal.ZERO);// 预算核准金额
		rbInfo.setIsAppointVoucher(false);// 是否已生成指定凭证
		rbInfo.setReceivingBillType(CasRecPayBillTypeEnum.commonType);// 收款单类型
		
		ReceivingBillTypeInfo rbtInfo = new ReceivingBillTypeInfo();
		rbtInfo.setId(BOSUuid.read("DAWSqQEREADgAAGVwKgSfCqo2zU="));
		rbInfo.setRecBillType(rbtInfo);// 收款类型 销售回款
		
		rbInfo.setIsRelateRecBook(false);// 是否已关联生成应收票据
		rbInfo.setIsCtrlOppAcct(false);// 控制对方科目
		rbInfo.setIsRedBill(false);// 是否是红字单据
		rbInfo.setIsTransBill(false);// 是否转销单据
		rbInfo.setIsTransOtherBill(false);// 是否转预收转预付
		rbInfo.setVerifiedAmt(BigDecimal.ZERO);// 已结算金额合计
		rbInfo.setVerifiedAmtLoc(BigDecimal.ZERO);// 已结算金额本位币合计
		rbInfo.setUnVerifiedAmt(m.getFtotalamount());// 未结算金额合计
		rbInfo.setUnVerifiedAmtLoc(m.getFtotalamount());// 未结算金额本位币合计
		rbInfo.setIsNeedVoucher(true);// 对账方式
		rbInfo.setMixEntryVerify(2);// 混合收付款类型分录是否符合相应结算条件
		rbInfo.setIsImpFromGL(false);// 是否从总账引入的数据
		rbInfo.setIsSaleReturn(false);// 分录收款类型是否含有销售回款或者退销售回款
		rbInfo.setIsProxyReturn(false);// 单据分录收款类型是否含有代收款或者退代收款
		rbInfo.setIsPreReturn(true);// 单据分录收款类型是否含有预收款或者退预收款
		rbInfo.setIsCoopBuild(false);// 是否协同生成
		rbInfo.setIsReverseLockAmount(true);// 是否反写锁定金额
		
 		// 付款方式 赊销
		PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
		paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
		rbInfo.setPaymentType(paymenttypeinfo);
 
		SettlementTypeInfo settlementTypeInfo = new SettlementTypeInfo();
		settlementTypeInfo.setId(BOSUuid.read("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
		rbInfo.setSettlementType(settlementTypeInfo); 
		
		rbInfo.setPrintCount(1);// 打印次数
		rbInfo.setPcaVouchered(false);// 是否生成利润中心凭证
		rbInfo.setBankCheckStatus(BankCheckStatus.match);
		rbInfo.setBgCtrlAmt(m.getFtotalamount());
		rbInfo.setIsRefundmentPay(false);
		rbInfo.setIsHasRefundPay(false);

		
		for (BaseFIDetailDTO dvo : m.getDetails())
	    {
			ReceivingBillEntryInfo entryInfo = createEntryInfo(ctx,dvo,busCode);
			entryInfo.setCurrency(currency);// 币别
			rbInfo.getEntries().addObject((IObjectValue)entryInfo);
		}
		
		return rbInfo;
	}
	
	// 收款单-明细对象
	private static ReceivingBillEntryInfo createEntryInfo(Context ctx,  BaseFIDetailDTO dvo ,String busCode)
			throws BOSException, EASBizException {
		ReceivingBillEntryInfo rbeInfo = new ReceivingBillEntryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal taxPirce = dvo.getFtaxprice();
 		 BigDecimal taxAmount = dvo.getFtaxamount();
		 BigDecimal qty = dvo.getFqty();
		 BigDecimal amount = dvo.getFamount();
 		 BigDecimal tax = dvo.getFtax();

  		rbeInfo.setSeq(dvo.getFseq());// 序号
		rbeInfo.setAmount(amount);// 金额
		rbeInfo.setAmountVc(BigDecimal.ZERO);// 金额核销
		rbeInfo.setLocalAmt(taxAmount);// 本位币金额
		rbeInfo.setLocalAmtVc(BigDecimal.ZERO);// 本位币金额核销
		rbeInfo.setUnVcAmount(taxAmount);// 应收（付）未核销金额
		rbeInfo.setUnVcLocAmount(taxAmount);// 应收（付）未核销本位币金额
		rbeInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);// 未结算调汇本位币金额
		rbeInfo.setRebate(BigDecimal.ZERO);// 现金折扣
		rbeInfo.setRebateAmtVc(BigDecimal.ZERO);// 折扣金额累计核销
		rbeInfo.setRebateLocAmt(BigDecimal.ZERO);// 折扣本位币金额
		rbeInfo.setRebateLocAmtVc(BigDecimal.ZERO);// 折扣本位币金额累计核销
		rbeInfo.setActualAmt(taxAmount);// 实收（付）金额
		rbeInfo.setActualAmtVc(BigDecimal.ZERO);// 实收（付）金额累计核销
		rbeInfo.setActualLocAmt(taxAmount);// 实收（付）本位币金额
		rbeInfo.setLockAmt(BigDecimal.ZERO);// 锁定金额
		rbeInfo.setLockLocAmt(BigDecimal.ZERO);// 锁定本位币金额
		rbeInfo.setUnLockAmt(amount);// 未锁定金额
		rbeInfo.setUnLockLocAmt(amount);// 未锁定本位币金额
		rbeInfo.setVcStatus(VcStatusEnum.NOT_VERIFICATED);// 核销状态
		rbeInfo.setHisUnVcAmount(BigDecimal.ZERO);// 历史未核销金额
		rbeInfo.setHisUnVcLocAmount(BigDecimal.ZERO);// 历史未核销金额本位币
		rbeInfo.setCoreBillEntrySeq(0);// 核心单据分录行号
 		rbeInfo.setMatchedAmount(BigDecimal.ZERO);// 已匹配金额
		rbeInfo.setMatchedAmountLoc(BigDecimal.ZERO);// 已匹配金额本位币
		rbeInfo.setBgCtrlAmt(amount);//
  		return rbeInfo;
	}
	
}
