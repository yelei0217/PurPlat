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

	
	// �տ
	public static ReceivingBillInfo getReceivingCollection(Context ctx, Receipt m)
			throws BOSException, EASBizException {
		// �տbycb
		String bizDate = "";// ����
		String billtype = "";// ���ͣ�3��һ�����ݣ�Ԥ�տ������4��2�����ݣ�Ԥ�տ����������ۻؿ��
		String clinic = "";// ����
		BigDecimal realAmount = BigDecimal.ZERO;// ʵ�ս��
		String payType = "";// ���㷽ʽ�������ͻ�
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String recType = m.getRecType();  //�տ�����: 100-���ۻؿ� ; 101-Ԥ�տ�         
		String defaulCsutomerName ="";  //Ĭ�Ͽͻ�����
		String defaulReceivingBillType ="";//���㷽ʽ �����ۻؿ� - DAWSqQEREADgAAGVwKgSfCqo2zU= ; Ԥ�տ� - DAWSqQEREADgAAILwKgSfCqo2zU=	
		String defaulPaymentType="91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5";;   //eas�Դ��ĸ��ʽ
		String defaulSettlementType =""; //ϵͳ�Դ� ���㷽ʽ 
		EASPayTypeInfo easPayTypeInfo =null;  // ���ʽ �Զ����������
 		//��Ŷ��󼯺�
		CoreBaseCollection requestCollection = new CoreBaseCollection();  
         if(recType.equals("sk-hqk")){									//��Ƿ��ҵ��
        	 defaulCsutomerName ="�ͻ�Ƿ��";
        	 //defaulReceivingBillType="DAWSqQEREADgAAGVwKgSfCqo2zU=";   //���ۻؿ�
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU="; 	//Ԥ�տ�
        	 defaulSettlementType="99"; 
         }else if(recType.equals("sk-ycz")){ 							 //Ԥ��ҵ��
         	 defaulCsutomerName ="���ۿͻ�";
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU="; 	//Ԥ�տ�
        	 defaulSettlementType="99";
         }else if(recType.equals("sk-back-yjk")){  				       //�˻�Ԥ����
        	 defaulCsutomerName ="���ۿͻ�";
        	 defaulSettlementType="02"; 
        	 defaulReceivingBillType="DAWSqQEREADgAAILwKgSfCqo2zU=";   //�˻ص��ͻ�
          }else if(recType.equals("sk-back-kh")){
        	 defaulSettlementType="02"; 
        	 defaulCsutomerName ="���ۿͻ�";
        	 defaulReceivingBillType="DAWSqQEREADgAAInwKgSfCqo2zU=";   //�����ۿ�˿ 
          } else if(recType.equals("sk-tyjk-yl")){
        	 defaulCsutomerName ="���ۿͻ�"; 
        	 defaulReceivingBillType="DAWSqQEREADgAAIswKgSfCqo2zU=";   //��Ԥ�տ�˿
         	 defaulSettlementType="02"; 
         } else if(recType.equals("sk-tyjk-xj")){
        	 defaulCsutomerName ="���ۿͻ�";
        	 defaulReceivingBillType="DAWSqQEREADgAAIswKgSfCqo2zU=";   //��Ԥ�տ�˿
        	 defaulSettlementType="01"; 
          }     
         
		// ͨ������ �õ�Receipt.class
        bizDate = m.getBizDate();
		billtype = m.getBillType();
		clinic = m.getClinic();
		realAmount = m.getRealAmount();
		payType = m.getPayType();
		// ���������Զ����ɶ�Ӧ���͵ĵ��� 
		ReceivingBillInfo rbInfo = new ReceivingBillInfo();
		CompanyOrgUnitInfo couInfo = CompanyOrgUnitFactory
				.getLocalInstance(ctx).getCompanyOrgUnitInfo(
						new ObjectUuidPK(clinic));
		rbInfo.setCompany(couInfo);// ��˾
		CtrlUnitInfo cuInfo = couInfo.getCU(); 
		rbInfo.setCU(cuInfo);// ����Ԫ 
		rbInfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// ����ʱ��
		rbInfo.setCreateTime(new java.sql.Timestamp(new java.util.Date().getTime()));

		try {
			rbInfo.setBizDate(sdf.parse(bizDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ҵ������
		rbInfo.setHasEffected(false);// �Ƿ�������Ч
		rbInfo.setSourceType(SourceTypeEnum.AR);// Դ����
		rbInfo.setSourceSysType(SourceTypeEnum.CASH);// Դϵͳ����
		//rbInfo.setFundType();
		//�ұ�
		CurrencyInfo curInfo = new CurrencyInfo();
		curInfo.setId(BOSUuid
				.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		rbInfo.setCurrency(curInfo);

		rbInfo.setExchangeRate(BigDecimal.ONE);// ����
		rbInfo.setIsExchanged(false);// �Ƿ��Ѿ�����
		rbInfo.setLastExhangeRate(BigDecimal.ONE);// ���������
		rbInfo.setIsCommitSettle(false);// �Ƿ��ύ��������
		rbInfo.setIsInitializeBill(false);// �Ƿ��ʼ������
		rbInfo.setFiVouchered(false);// �Ƿ�������ƾ֤
		rbInfo.setSettlementStatus(SettlementStatusEnum.UNSUBMIT);// ���н���״̬
		rbInfo.setActRecAmt(realAmount);// ʵ�ս��ϼ�
		rbInfo.setActRecAmtVc(BigDecimal.ZERO);// ʵ�ս���ۼƺ���
		rbInfo.setActRecLocAmt(realAmount);// ʵ�ձ�λ�ҽ��ϼ�
		rbInfo.setActRecLocAmtVc(BigDecimal.ZERO);// ʵ�ձ�λ�ҽ���ۼƺ���
	 
		// ������
		AsstActTypeInfo actType = new AsstActTypeInfo();
		actType.setId(BOSUuid.read("YW3xsAEJEADgAAUWwKgTB0c4VZA="));
		rbInfo.setPayerType(actType);

		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx).getCustomerInfo("where name = '"+defaulCsutomerName+"'");
		if (null != customer) {
			rbInfo.setPayerID(customer.getId().toString());
			rbInfo.setPayerName(customer.getName());
			rbInfo.setPayerNumber(customer.getNumber());
		}
		rbInfo.setIsImport(false);// �Ƿ���
		rbInfo.setAmount(realAmount);// ���
		rbInfo.setLocalAmt(realAmount);// ��λ��
		rbInfo.setAccessoryAmt(0);// ������
		rbInfo.setIsRelateReceipt(false);// �Ƿ��ѹ������˵�
		rbInfo.setIsBookRL(false);// �Ƿ�Ǽ������ռ���
		rbInfo.setBgAmount(BigDecimal.ZERO);// Ԥ���׼���
		rbInfo.setIsAppointVoucher(false);// �Ƿ�������ָ��ƾ֤
		rbInfo.setReceivingBillType(CasRecPayBillTypeEnum.commonType);// �տ����
	 
		ReceivingBillTypeInfo rbtInfo = new ReceivingBillTypeInfo();
		rbtInfo.setId(BOSUuid.read(defaulReceivingBillType));
		rbInfo.setRecBillType(rbtInfo);// �տ����� Ԥ��
		
		rbInfo.setIsRelateRecBook(false);// �Ƿ��ѹ�������Ӧ��Ʊ��
		rbInfo.setIsCtrlOppAcct(false);// ���ƶԷ���Ŀ
		rbInfo.setIsRedBill(false);// �Ƿ��Ǻ��ֵ���
		rbInfo.setIsTransBill(false);// �Ƿ�ת������
		rbInfo.setIsTransOtherBill(false);// �Ƿ�תԤ��תԤ��
		rbInfo.setVerifiedAmt(BigDecimal.ZERO);// �ѽ�����ϼ�
		rbInfo.setVerifiedAmtLoc(BigDecimal.ZERO);// �ѽ����λ�Һϼ�
		rbInfo.setUnVerifiedAmt(realAmount);// δ������ϼ�
		rbInfo.setUnVerifiedAmtLoc(realAmount);// δ�����λ�Һϼ�
		rbInfo.setIsNeedVoucher(true);// ���˷�ʽ
		rbInfo.setMixEntryVerify(2);// ����ո������ͷ�¼�Ƿ������Ӧ��������
		rbInfo.setIsImpFromGL(false);// �Ƿ���������������
		rbInfo.setIsSaleReturn(false);// ��¼�տ������Ƿ������ۻؿ���������ۻؿ�
		rbInfo.setIsProxyReturn(false);// ���ݷ�¼�տ������Ƿ��д��տ�����˴��տ�
		rbInfo.setIsPreReturn(true);// ���ݷ�¼�տ������Ƿ���Ԥ�տ������Ԥ�տ�
		rbInfo.setIsCoopBuild(false);// �Ƿ�Эͬ����
		rbInfo.setIsReverseLockAmount(true);// �Ƿ�д�������

		// ���ʽ
		PaymentTypeInfo ptInfo = new PaymentTypeInfo();
		ptInfo.setId(BOSUuid.read(defaulPaymentType));
		rbInfo.setPaymentType(ptInfo);
		
		//Ԥ�տ�  �ֽ� 
//		if(recType.equals("sk-tyjk-xj")||(recType.equals("sk-ycz")&&"�ֽ�".equals(easPayTypeInfo.getName()))){
//			 AccountViewInfo account = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo("where number ='1001.01' and CompanyID ='"+clinic+"'");
//			 rbInfo.setPayeeAccount(account);   
//			 defaulSettlementType="01"; 
//		}
		
		// ���㷽ʽ  Ĭ��Ϊ99
//		EntityViewInfo viewInfoSTI = new EntityViewInfo();
//		FilterInfo filterSTI = new FilterInfo();
//		filterSTI.getFilterItems().add(new FilterItemInfo("number", defaulSettlementType, CompareType.EQUALS));
//		viewInfoSTI.setFilter(filterSTI);
//		ISettlementType iSettlementType = SettlementTypeFactory.getLocalInstance(ctx);
//		SettlementTypeInfo settlementTypeInfo = iSettlementType.getSettlementTypeCollection(viewInfoSTI).get(0);
//		rbInfo.setSettlementType(settlementTypeInfo); 
		SettlementTypeInfo settlementTypeInfo = new SettlementTypeInfo();
		settlementTypeInfo.setId(BOSUuid.read("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
		
		rbInfo.setPrintCount(1);// ��ӡ����
		rbInfo.setPcaVouchered(false);// �Ƿ�������������ƾ֤
		rbInfo.setBankCheckStatus(BankCheckStatus.match);
		rbInfo.setBgCtrlAmt(realAmount);
		rbInfo.setIsRefundmentPay(false);
		rbInfo.setIsHasRefundPay(false);

		ReceivingBillEntryInfo rbeInfo = new ReceivingBillEntryInfo();
		rbeInfo.setSeq(1);// ���
		rbeInfo.setAmount(realAmount);// ���
		rbeInfo.setAmountVc(BigDecimal.ZERO);// ������
		rbeInfo.setLocalAmt(realAmount);// ��λ�ҽ��
		rbeInfo.setLocalAmtVc(BigDecimal.ZERO);// ��λ�ҽ�����
		rbeInfo.setUnVcAmount(realAmount);// Ӧ�գ�����δ�������
		rbeInfo.setUnVcLocAmount(realAmount);// Ӧ�գ�����δ������λ�ҽ��
		rbeInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);// δ������㱾λ�ҽ��
		rbeInfo.setRebate(BigDecimal.ZERO);// �ֽ��ۿ�
		rbeInfo.setRebateAmtVc(BigDecimal.ZERO);// �ۿ۽���ۼƺ���
		rbeInfo.setRebateLocAmt(BigDecimal.ZERO);// �ۿ۱�λ�ҽ��
		rbeInfo.setRebateLocAmtVc(BigDecimal.ZERO);// �ۿ۱�λ�ҽ���ۼƺ���
		rbeInfo.setActualAmt(realAmount);// ʵ�գ��������
		rbeInfo.setActualAmtVc(BigDecimal.ZERO);// ʵ�գ���������ۼƺ���
		rbeInfo.setActualLocAmt(realAmount);// ʵ�գ�������λ�ҽ��
		rbeInfo.setLockAmt(BigDecimal.ZERO);// �������
		rbeInfo.setLockLocAmt(BigDecimal.ZERO);// ������λ�ҽ��
		rbeInfo.setUnLockAmt(realAmount);// δ�������
		rbeInfo.setUnLockLocAmt(realAmount);// δ������λ�ҽ��
		rbeInfo.setVcStatus(VcStatusEnum.NOT_VERIFICATED);// ����״̬
		rbeInfo.setReceivingBill(rbInfo);// �տͷ
		rbeInfo.setHisUnVcAmount(BigDecimal.ZERO);// ��ʷδ�������
		rbeInfo.setHisUnVcLocAmount(BigDecimal.ZERO);// ��ʷδ������λ��
		rbeInfo.setCoreBillEntrySeq(0);// ���ĵ��ݷ�¼�к�
		rbeInfo.setCurrency(curInfo);// �ұ�
		// rbeInfo.setContractEntrySeq("0");//��ͬ�к�
		rbeInfo.setMatchedAmount(BigDecimal.ZERO);// ��ƥ����
		rbeInfo.setMatchedAmountLoc(BigDecimal.ZERO);// ��ƥ���λ��
		rbeInfo.setBgCtrlAmt(realAmount);//
		rbeInfo.setRecBillType(rbtInfo);// �տ����� Ԥ��
 		rbInfo.getEntries().add(rbeInfo);

//		rbInfo.put("HisReqID", m.getId());  //HisReqId
//		rbInfo.put("HISdanjubianma",m.getNumber());// his���ݱ���		 
		
 		return rbInfo;
	}
	
}
