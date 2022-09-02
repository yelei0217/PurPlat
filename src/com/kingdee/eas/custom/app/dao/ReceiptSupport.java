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
	
	// �տ
	private static ReceivingBillInfo createInfo(Context ctx, BaseFIDTO m,String busCode )
			throws BOSException, EASBizException {
		ReceivingBillInfo rbInfo = new ReceivingBillInfo();

		Date currentDate = new java.util.Date();
		// ������
		rbInfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// ����ʱ��
		rbInfo.setCreateTime(new java.sql.Timestamp(currentDate.getTime()));
		// ��˾ID
		ObjectUuidPK orgpk = new ObjectUuidPK(m.getFstorageorgunitid());
		
		// ��֯��Ԫ
		CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(
				ctx).getCompanyOrgUnitInfo(orgpk);
		rbInfo.setCompany(xmcompany);
		
		CtrlUnitInfo cuInfo = xmcompany.getCU();
		rbInfo.setCU(cuInfo);// ����Ԫ
		
		// ҵ������
		SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
		Date bizDate = currentDate;
		try {
			bizDate = formmat.parse(m.getFbizdate());
 		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		rbInfo.setBizDate(bizDate);
		rbInfo.setHasEffected(false);// �Ƿ�������Ч
		rbInfo.setSourceType(SourceTypeEnum.AR);// Դ����
		rbInfo.setSourceSysType(SourceTypeEnum.CASH);// Դϵͳ����
		//rbInfo.setFundType();
		//�ұ�
		CurrencyInfo currency = new CurrencyInfo();
		currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		rbInfo.setCurrency(currency);
		
		rbInfo.setExchangeRate(BigDecimal.ONE);// ����
		rbInfo.setIsExchanged(false);// �Ƿ��Ѿ�����
		rbInfo.setLastExhangeRate(BigDecimal.ONE);// ���������
		rbInfo.setIsCommitSettle(false);// �Ƿ��ύ��������
		rbInfo.setIsInitializeBill(false);// �Ƿ��ʼ������
		rbInfo.setFiVouchered(false);// �Ƿ�������ƾ֤
		rbInfo.setSettlementStatus(SettlementStatusEnum.UNSUBMIT);// ���н���״̬
		rbInfo.setActRecAmt(m.getFtotalamount());// ʵ�ս��ϼ�
		rbInfo.setActRecAmtVc(BigDecimal.ZERO);// ʵ�ս���ۼƺ���
		rbInfo.setActRecLocAmt(m.getFtotaltaxamount());// ʵ�ձ�λ�ҽ��ϼ�
		rbInfo.setActRecLocAmtVc(BigDecimal.ZERO);// ʵ�ձ�λ�ҽ���ۼƺ���
		
		// ������
		AsstActTypeInfo actType = new AsstActTypeInfo();
		actType.setId(BOSUuid.read("YW3xsAEJEADgAAUWwKgTB0c4VZA="));
		rbInfo.setPayerType(actType);
		
		ObjectUuidPK cuspk = new ObjectUuidPK(m.getFcustomerid());
		// ��ȡ���㷽ʽʵ��
		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx).getCustomerInfo(cuspk);
		if (null != customer) {
			rbInfo.setPayerID(customer.getId().toString());
			rbInfo.setPayerName(customer.getName());
			rbInfo.setPayerNumber(customer.getNumber());
		}
		
		rbInfo.setIsImport(false);// �Ƿ���
		rbInfo.setAmount(m.getFtotalamount());// ���
		rbInfo.setLocalAmt(m.getFtotalamount());// ��λ��
		rbInfo.setAccessoryAmt(0);// ������
		rbInfo.setIsRelateReceipt(false);// �Ƿ��ѹ������˵�
		rbInfo.setIsBookRL(false);// �Ƿ�Ǽ������ռ���
		rbInfo.setBgAmount(BigDecimal.ZERO);// Ԥ���׼���
		rbInfo.setIsAppointVoucher(false);// �Ƿ�������ָ��ƾ֤
		rbInfo.setReceivingBillType(CasRecPayBillTypeEnum.commonType);// �տ����
		
		ReceivingBillTypeInfo rbtInfo = new ReceivingBillTypeInfo();
		rbtInfo.setId(BOSUuid.read("DAWSqQEREADgAAGVwKgSfCqo2zU="));
		rbInfo.setRecBillType(rbtInfo);// �տ����� ���ۻؿ�
		
		rbInfo.setIsRelateRecBook(false);// �Ƿ��ѹ�������Ӧ��Ʊ��
		rbInfo.setIsCtrlOppAcct(false);// ���ƶԷ���Ŀ
		rbInfo.setIsRedBill(false);// �Ƿ��Ǻ��ֵ���
		rbInfo.setIsTransBill(false);// �Ƿ�ת������
		rbInfo.setIsTransOtherBill(false);// �Ƿ�תԤ��תԤ��
		rbInfo.setVerifiedAmt(BigDecimal.ZERO);// �ѽ�����ϼ�
		rbInfo.setVerifiedAmtLoc(BigDecimal.ZERO);// �ѽ����λ�Һϼ�
		rbInfo.setUnVerifiedAmt(m.getFtotalamount());// δ������ϼ�
		rbInfo.setUnVerifiedAmtLoc(m.getFtotalamount());// δ�����λ�Һϼ�
		rbInfo.setIsNeedVoucher(true);// ���˷�ʽ
		rbInfo.setMixEntryVerify(2);// ����ո������ͷ�¼�Ƿ������Ӧ��������
		rbInfo.setIsImpFromGL(false);// �Ƿ���������������
		rbInfo.setIsSaleReturn(false);// ��¼�տ������Ƿ������ۻؿ���������ۻؿ�
		rbInfo.setIsProxyReturn(false);// ���ݷ�¼�տ������Ƿ��д��տ�����˴��տ�
		rbInfo.setIsPreReturn(true);// ���ݷ�¼�տ������Ƿ���Ԥ�տ������Ԥ�տ�
		rbInfo.setIsCoopBuild(false);// �Ƿ�Эͬ����
		rbInfo.setIsReverseLockAmount(true);// �Ƿ�д�������
		
 		// ���ʽ ����
		PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
		paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
		rbInfo.setPaymentType(paymenttypeinfo);
 
		SettlementTypeInfo settlementTypeInfo = new SettlementTypeInfo();
		settlementTypeInfo.setId(BOSUuid.read("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
		rbInfo.setSettlementType(settlementTypeInfo); 
		
		rbInfo.setPrintCount(1);// ��ӡ����
		rbInfo.setPcaVouchered(false);// �Ƿ�������������ƾ֤
		rbInfo.setBankCheckStatus(BankCheckStatus.match);
		rbInfo.setBgCtrlAmt(m.getFtotalamount());
		rbInfo.setIsRefundmentPay(false);
		rbInfo.setIsHasRefundPay(false);

		
		for (BaseFIDetailDTO dvo : m.getDetails())
	    {
			ReceivingBillEntryInfo entryInfo = createEntryInfo(ctx,dvo,busCode);
			entryInfo.setCurrency(currency);// �ұ�
			rbInfo.getEntries().addObject((IObjectValue)entryInfo);
		}
		
		return rbInfo;
	}
	
	// �տ-��ϸ����
	private static ReceivingBillEntryInfo createEntryInfo(Context ctx,  BaseFIDetailDTO dvo ,String busCode)
			throws BOSException, EASBizException {
		ReceivingBillEntryInfo rbeInfo = new ReceivingBillEntryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal taxPirce = dvo.getFtaxprice();
 		 BigDecimal taxAmount = dvo.getFtaxamount();
		 BigDecimal qty = dvo.getFqty();
		 BigDecimal amount = dvo.getFamount();
 		 BigDecimal tax = dvo.getFtax();

  		rbeInfo.setSeq(dvo.getFseq());// ���
		rbeInfo.setAmount(amount);// ���
		rbeInfo.setAmountVc(BigDecimal.ZERO);// ������
		rbeInfo.setLocalAmt(taxAmount);// ��λ�ҽ��
		rbeInfo.setLocalAmtVc(BigDecimal.ZERO);// ��λ�ҽ�����
		rbeInfo.setUnVcAmount(taxAmount);// Ӧ�գ�����δ�������
		rbeInfo.setUnVcLocAmount(taxAmount);// Ӧ�գ�����δ������λ�ҽ��
		rbeInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);// δ������㱾λ�ҽ��
		rbeInfo.setRebate(BigDecimal.ZERO);// �ֽ��ۿ�
		rbeInfo.setRebateAmtVc(BigDecimal.ZERO);// �ۿ۽���ۼƺ���
		rbeInfo.setRebateLocAmt(BigDecimal.ZERO);// �ۿ۱�λ�ҽ��
		rbeInfo.setRebateLocAmtVc(BigDecimal.ZERO);// �ۿ۱�λ�ҽ���ۼƺ���
		rbeInfo.setActualAmt(taxAmount);// ʵ�գ��������
		rbeInfo.setActualAmtVc(BigDecimal.ZERO);// ʵ�գ���������ۼƺ���
		rbeInfo.setActualLocAmt(taxAmount);// ʵ�գ�������λ�ҽ��
		rbeInfo.setLockAmt(BigDecimal.ZERO);// �������
		rbeInfo.setLockLocAmt(BigDecimal.ZERO);// ������λ�ҽ��
		rbeInfo.setUnLockAmt(amount);// δ�������
		rbeInfo.setUnLockLocAmt(amount);// δ������λ�ҽ��
		rbeInfo.setVcStatus(VcStatusEnum.NOT_VERIFICATED);// ����״̬
		rbeInfo.setHisUnVcAmount(BigDecimal.ZERO);// ��ʷδ�������
		rbeInfo.setHisUnVcLocAmount(BigDecimal.ZERO);// ��ʷδ������λ��
		rbeInfo.setCoreBillEntrySeq(0);// ���ĵ��ݷ�¼�к�
 		rbeInfo.setMatchedAmount(BigDecimal.ZERO);// ��ƥ����
		rbeInfo.setMatchedAmountLoc(BigDecimal.ZERO);// ��ƥ���λ��
		rbeInfo.setBgCtrlAmt(amount);//
  		return rbeInfo;
	}
	
}
