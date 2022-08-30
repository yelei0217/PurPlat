package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
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
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.EASPayTypeInfo;
import com.kingdee.eas.custom.entity.Receipt;
import com.kingdee.eas.fi.cas.BankCheckStatus;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.ReceivingBillEntryInfo;
import com.kingdee.eas.fi.cas.ReceivingBillInfo;
import com.kingdee.eas.fi.cas.ReceivingBillTypeInfo;
import com.kingdee.eas.fi.cas.SettlementStatusEnum;
import com.kingdee.eas.fi.cas.SourceTypeEnum;
import com.kingdee.eas.fi.cas.VcStatusEnum;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.util.app.ContextUtil;

public class ReceiptSupport {

	
	// 收款单
	public static ReceivingBillInfo getReceivingCollection(Context ctx, Receipt m)
			throws BOSException, EASBizException {
		// 收款单bycb
		String bizDate = "";// 日期
		String billtype = "";// 类型，3是一条数据，预收款，正数；4是2条数据，预收款正数，销售回款负数
		String clinic = "";// 部门
		BigDecimal realAmount = BigDecimal.ZERO;// 实收金额
		String payType = "";// 结算方式，关联客户
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String recType = m.getRecType();  //收款类型: 100-销售回款 ; 101-预收款         
		String defaulCsutomerName ="";  //默认客户名称
		String defaulReceivingBillType ="";//结算方式 ：销售回款 - DAWSqQEREADgAAGVwKgSfCqo2zU= ; 预收款 - DAWSqQEREADgAAILwKgSfCqo2zU=	
		String defaulPaymentType="91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5";;   //eas自带的付款方式
		String defaulSettlementType =""; //系统自带 结算方式 
		EASPayTypeInfo easPayTypeInfo =null;  // 付款方式 自定义基础资料
 		//存放对象集合
		CoreBaseCollection requestCollection = new CoreBaseCollection();  
         if(recType.equals("sk-hqk")){									//还欠款业务
        	 defaulCsutomerName ="客户欠款";
        	 //defaulReceivingBillType="DAWSqQEREADgAAGVwKgSfCqo2zU=";   //销售回款
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU="; 	//预收款
        	 defaulSettlementType="99"; 
         }else if(recType.equals("sk-ycz")){ 							 //预收业务
         	 defaulCsutomerName ="零售客户";
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU="; 	//预收款
        	 defaulSettlementType="99";
         }else if(recType.equals("sk-back-yjk")){  				       //退回预交款
        	 defaulCsutomerName ="零售客户";
        	 defaulSettlementType="02"; 
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU=";   //退回到客户
          }else if(recType.equals("sk-back-kh")){
        	 defaulSettlementType="02"; 
        	 defaulCsutomerName ="零售客户";
        	 defaulReceivingBillType="DAWSqQEREADgAAInwKgSfCqo2zU=";   //退销售款（退款） 
          } else if(recType.equals("sk-tyjk-yl")){
        	 defaulCsutomerName ="零售客户"; 
        	 defaulReceivingBillType="DAWSqQEREADgAAIswKgSfCqo2zU=";   //退预收款（退款）
         	 defaulSettlementType="02"; 
         } else if(recType.equals("sk-tyjk-xj")){
        	 defaulCsutomerName ="零售客户";
        	 defaulReceivingBillType="DAWSqQEREADgAAIswKgSfCqo2zU=";   //退预收款（退款）
        	 defaulSettlementType="01"; 
          }     
         
		// 通过反射 得到Receipt.class
        bizDate = m.getBizDate();
		billtype = m.getBillType();
		clinic = m.getClinic();
		realAmount = m.getRealAmount();
		payType = m.getPayType();
		// 根据数据自动生成对应类型的单据 
		ReceivingBillInfo rbInfo = new ReceivingBillInfo();
		CompanyOrgUnitInfo couInfo = CompanyOrgUnitFactory
				.getLocalInstance(ctx).getCompanyOrgUnitInfo(
						new ObjectUuidPK(clinic));
		rbInfo.setCompany(couInfo);// 公司
		CtrlUnitInfo cuInfo = couInfo.getCU(); 
		rbInfo.setCU(cuInfo);// 管理单元 
		rbInfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// 创建时间
		rbInfo.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));

		try {
			rbInfo.setBizDate(sdf.parse(bizDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 业务日期
		rbInfo.setHasEffected(false);// 是否曾经生效
		rbInfo.setSourceType(SourceTypeEnum.AR);// 源类型
		rbInfo.setSourceSysType(SourceTypeEnum.CASH);// 源系统类型
		//rbInfo.setFundType();
		//币别
		CurrencyInfo curInfo = new CurrencyInfo();
		curInfo.setId(BOSUuid
				.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		rbInfo.setCurrency(curInfo);

		rbInfo.setExchangeRate(BigDecimal.ONE);// 汇率
		rbInfo.setIsExchanged(false);// 是否已经调汇
		rbInfo.setLastExhangeRate(BigDecimal.ONE);// 最后调汇汇率
		rbInfo.setIsCommitSettle(false);// 是否提交结算中心
		rbInfo.setIsInitializeBill(false);// 是否初始化单据
		rbInfo.setFiVouchered(false);// 是否已生成凭证
		rbInfo.setSettlementStatus(SettlementStatusEnum.UNSUBMIT);// 集中结算状态
		rbInfo.setActRecAmt(realAmount);// 实收金额合计
		rbInfo.setActRecAmtVc(BigDecimal.ZERO);// 实收金额累计核销
		rbInfo.setActRecLocAmt(realAmount);// 实收本位币金额合计
		rbInfo.setActRecLocAmtVc(BigDecimal.ZERO);// 实收本位币金额累计核销
	 
		// 往来户
		AsstActTypeInfo actType = new AsstActTypeInfo();
		actType.setId(BOSUuid.read("YW3xsAEJEADgAAUWwKgTB0c4VZA="));
		rbInfo.setPayerType(actType);

		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx).getCustomerInfo("where name = '"+defaulCsutomerName+"'");
		if (null != customer) {
			rbInfo.setPayerID(customer.getId().toString());
			rbInfo.setPayerName(customer.getName());
			rbInfo.setPayerNumber(customer.getNumber());
		}
		rbInfo.setIsImport(false);// 是否导入
		rbInfo.setAmount(realAmount);// 金额
		rbInfo.setLocalAmt(realAmount);// 金额本位币
		rbInfo.setAccessoryAmt(0);// 附件数
		rbInfo.setIsRelateReceipt(false);// 是否已关联进账单
		rbInfo.setIsBookRL(false);// 是否登记银行日记账
		rbInfo.setBgAmount(BigDecimal.ZERO);// 预算核准金额
		rbInfo.setIsAppointVoucher(false);// 是否已生成指定凭证
		rbInfo.setReceivingBillType(CasRecPayBillTypeEnum.commonType);// 收款单类型
	 
		ReceivingBillTypeInfo rbtInfo = new ReceivingBillTypeInfo();
		rbtInfo.setId(BOSUuid.read(defaulReceivingBillType));
		rbInfo.setRecBillType(rbtInfo);// 收款类型 预收
		
		rbInfo.setIsRelateRecBook(false);// 是否已关联生成应收票据
		rbInfo.setIsCtrlOppAcct(false);// 控制对方科目
		rbInfo.setIsRedBill(false);// 是否是红字单据
		rbInfo.setIsTransBill(false);// 是否转销单据
		rbInfo.setIsTransOtherBill(false);// 是否转预收转预付
		rbInfo.setVerifiedAmt(BigDecimal.ZERO);// 已结算金额合计
		rbInfo.setVerifiedAmtLoc(BigDecimal.ZERO);// 已结算金额本位币合计
		rbInfo.setUnVerifiedAmt(realAmount);// 未结算金额合计
		rbInfo.setUnVerifiedAmtLoc(realAmount);// 未结算金额本位币合计
		rbInfo.setIsNeedVoucher(true);// 对账方式
		rbInfo.setMixEntryVerify(2);// 混合收付款类型分录是否符合相应结算条件
		rbInfo.setIsImpFromGL(false);// 是否从总账引入的数据
		rbInfo.setIsSaleReturn(false);// 分录收款类型是否含有销售回款或者退销售回款
		rbInfo.setIsProxyReturn(false);// 单据分录收款类型是否含有代收款或者退代收款
		rbInfo.setIsPreReturn(true);// 单据分录收款类型是否含有预收款或者退预收款
		rbInfo.setIsCoopBuild(false);// 是否协同生成
		rbInfo.setIsReverseLockAmount(true);// 是否反写锁定金额

		// 付款方式
		PaymentTypeInfo ptInfo = new PaymentTypeInfo();
		ptInfo.setId(BOSUuid.read(defaulPaymentType));
		rbInfo.setPaymentType(ptInfo);
		
		//预收款  现金 
//		if(recType.equals("sk-tyjk-xj")||(recType.equals("sk-ycz")&&"现金".equals(easPayTypeInfo.getName()))){
//			 AccountViewInfo account = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo("where number ='1001.01' and CompanyID ='"+clinic+"'");
//			 rbInfo.setPayeeAccount(account);   
//			 defaulSettlementType="01"; 
//		}
		
		// 结算方式  默认为99
//		EntityViewInfo viewInfoSTI = new EntityViewInfo();
//		FilterInfo filterSTI = new FilterInfo();
//		filterSTI.getFilterItems().add(new FilterItemInfo("number", defaulSettlementType, CompareType.EQUALS));
//		viewInfoSTI.setFilter(filterSTI);
//		ISettlementType iSettlementType = SettlementTypeFactory.getLocalInstance(ctx);
//		SettlementTypeInfo settlementTypeInfo = iSettlementType.getSettlementTypeCollection(viewInfoSTI).get(0);
//		rbInfo.setSettlementType(settlementTypeInfo); 
		SettlementTypeInfo settlementTypeInfo = new SettlementTypeInfo();
		settlementTypeInfo.setId(BOSUuid.read("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
		
		rbInfo.setPrintCount(1);// 打印次数
		rbInfo.setPcaVouchered(false);// 是否生成利润中心凭证
		rbInfo.setBankCheckStatus(BankCheckStatus.match);
		rbInfo.setBgCtrlAmt(realAmount);
		rbInfo.setIsRefundmentPay(false);
		rbInfo.setIsHasRefundPay(false);

		ReceivingBillEntryInfo rbeInfo = new ReceivingBillEntryInfo();
		rbeInfo.setSeq(1);// 序号
		rbeInfo.setAmount(realAmount);// 金额
		rbeInfo.setAmountVc(BigDecimal.ZERO);// 金额核销
		rbeInfo.setLocalAmt(realAmount);// 本位币金额
		rbeInfo.setLocalAmtVc(BigDecimal.ZERO);// 本位币金额核销
		rbeInfo.setUnVcAmount(realAmount);// 应收（付）未核销金额
		rbeInfo.setUnVcLocAmount(realAmount);// 应收（付）未核销本位币金额
		rbeInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);// 未结算调汇本位币金额
		rbeInfo.setRebate(BigDecimal.ZERO);// 现金折扣
		rbeInfo.setRebateAmtVc(BigDecimal.ZERO);// 折扣金额累计核销
		rbeInfo.setRebateLocAmt(BigDecimal.ZERO);// 折扣本位币金额
		rbeInfo.setRebateLocAmtVc(BigDecimal.ZERO);// 折扣本位币金额累计核销
		rbeInfo.setActualAmt(realAmount);// 实收（付）金额
		rbeInfo.setActualAmtVc(BigDecimal.ZERO);// 实收（付）金额累计核销
		rbeInfo.setActualLocAmt(realAmount);// 实收（付）本位币金额
		rbeInfo.setLockAmt(BigDecimal.ZERO);// 锁定金额
		rbeInfo.setLockLocAmt(BigDecimal.ZERO);// 锁定本位币金额
		rbeInfo.setUnLockAmt(realAmount);// 未锁定金额
		rbeInfo.setUnLockLocAmt(realAmount);// 未锁定本位币金额
		rbeInfo.setVcStatus(VcStatusEnum.NOT_VERIFICATED);// 核销状态
		rbeInfo.setReceivingBill(rbInfo);// 收款单头
		rbeInfo.setHisUnVcAmount(BigDecimal.ZERO);// 历史未核销金额
		rbeInfo.setHisUnVcLocAmount(BigDecimal.ZERO);// 历史未核销金额本位币
		rbeInfo.setCoreBillEntrySeq(0);// 核心单据分录行号
		rbeInfo.setCurrency(curInfo);// 币别
		// rbeInfo.setContractEntrySeq("0");//合同行号
		rbeInfo.setMatchedAmount(BigDecimal.ZERO);// 已匹配金额
		rbeInfo.setMatchedAmountLoc(BigDecimal.ZERO);// 已匹配金额本位币
		rbeInfo.setBgCtrlAmt(realAmount);//
		rbeInfo.setRecBillType(rbtInfo);// 收款类型 预收
 		rbInfo.getEntries().add(rbeInfo);

//		rbInfo.put("HisReqID", m.getId());  //HisReqId
//		rbInfo.put("HISdanjubianma",m.getNumber());// his单据编码		 
		
 		return rbInfo;
	}
	
}
