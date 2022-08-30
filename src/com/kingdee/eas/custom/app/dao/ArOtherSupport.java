package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeFactory;
import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.basedata.person.PersonInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeFactory;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.DiscountModeEnum;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BusMasterDataClassCollection;
import com.kingdee.eas.custom.BusMasterDataClassFactory;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.entity.Receivables;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.fi.ar.OtherBillInfo;
import com.kingdee.eas.fi.ar.OtherBillTypeEnum;
import com.kingdee.eas.fi.ar.OtherBillentryInfo;
import com.kingdee.eas.framework.DataBaseInfo;
import com.kingdee.eas.util.app.ContextUtil;


public class ArOtherSupport {
	
	public static OtherBillInfo CreateCollection(Context ctx, Receivables m)
	throws EASBizException, BOSException {
		String recType = m.getBillType(); // ҵ������ 100:���� ;101:Ƿ��
		String defaulCsutomerName = "���ۿͻ�"; // Ĭ�Ͽͻ�����
		String defaulPaymentType = "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"; // ����
		
		BigDecimal recAmount = m.getRecAmount();
		BigDecimal recAmountnegate = m.getRecAmount().negate();
		BigDecimal qty = BigDecimal.valueOf(1);
		BigDecimal qtynegate = BigDecimal.valueOf(-1);
		
		if ("ys-xf-yjk".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
		} else if ("ys-xf-other".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�"; // ���ݸ��ʽ ��ѯ�����ʽ�Ϲ����Ŀͻ� 
		}else if ("ys-xf-xj".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
			defaulPaymentType = "cd54aa9f-03a4-459c-9c5a-5489dce5f0676BCA0AB5"; // ����
		}else if ("ys-xf-qfd".equals(recType)) {
			defaulCsutomerName = "�ͻ�Ƿ��"; 
		}else if ("ys-back-yjk".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
		}else if ("ys-back-cw".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
		}else if ("ys-back-kh".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
		}else if ("ys-back-xj".equals(recType)) {
			defaulCsutomerName = "���ۿͻ�";
			defaulPaymentType = "cd54aa9f-03a4-459c-9c5a-5489dce5f0676BCA0AB5"; // ����
		}else if ("ys-mcqk".equals(recType)||"ys-yjk-zj".equals(recType)) {
			defaulCsutomerName = "�ͻ�Ƿ��";
		}
		 
		// ���� eas��׼�շ���Ŀ�ж� ������ֽ� ���ʽ��������������ʽ����
		Date currentDate = new java.util.Date();
		
		OtherBillInfo billinfo = new OtherBillInfo();
		// ������
		billinfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// ����ʱ��
		billinfo.setCreateTime(new java.sql.Timestamp(currentDate.getTime()));
		// ��˾ID
		ObjectUuidPK orgpk = new ObjectUuidPK(m.getClinic());
		
		// ��֯��Ԫ
		CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(
				ctx).getCompanyOrgUnitInfo(orgpk);
		billinfo.setCompany(xmcompany);
		
		CtrlUnitInfo cuInfo = xmcompany.getCU();
		billinfo.setCU(cuInfo);// ����Ԫ
		
		SaleOrgUnitInfo saleorg = new SaleOrgUnitInfo();
		saleorg.setId(BOSUuid.read(m.getClinic()));
		billinfo.setSaleOrg(saleorg);// ������֯
		
		// ҵ������
		SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date bizDate = currentDate;
		try {
			bizDate = formmat.parse(m.getBizDate());
			//bizDate = formmat.parse(AppUnit.getPeriodLastDayByCompanyNum(ctx, xmcompany.getNumber(), m.getBizDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		billinfo.setBizDate(bizDate);
		billinfo.setBillType(OtherBillTypeEnum.InvoiceBill);
		billinfo.setIsInTax(false);
		
		// �������� --��ǰʱ��
		billinfo.setBillDate(bizDate);
		// �������� �ͻ�
		DataBaseInfo database = AsstActTypeFactory.getLocalInstance(ctx)
				.getDataBaseInfo("where number='00001'");
		
		AsstActTypeInfo asstype = new AsstActTypeInfo();
		asstype.setId(BOSUuid.read(database.getId().toString()));
		billinfo.setAsstActType(asstype);
		// ��ȡ���㷽ʽʵ��
		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx)
				.getCustomerInfo("where name = '" + defaulCsutomerName + "'");
	 
		// ���ʽ
		PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
		paymenttypeinfo.setId(BOSUuid.read(defaulPaymentType));
		billinfo.setPaymentType(paymenttypeinfo);
		
		CurrencyInfo currency = new CurrencyInfo();
		currency.setId(BOSUuid
				.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		billinfo.setCurrency(currency);// �����
		billinfo.setExchangeRate(BigDecimal.valueOf(1));
		
		billinfo.setAmount(recAmount);
		billinfo.setAmountLocal(recAmount);
		billinfo.setUnVerifyAmount(recAmount);
		billinfo.setUnVerifyAmountLocal(recAmount);
		billinfo.setTotalAmount(recAmount);
		billinfo.setLastExhangeRate(qty);
		
		billinfo.setTotalTax(BigDecimal.ZERO);
		billinfo.setTotalTaxAmount(BigDecimal.ZERO);
		billinfo.setVerifyAmount(BigDecimal.ZERO);
		billinfo.setVerifyAmountLocal(BigDecimal.ZERO);
		billinfo.setIsImportBill(false);
		// ���\ҽ��
		PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m
				.getReceiveDoctor());
		if (person != null && VerifyUtil.notNull(person.getId().toString())) {
			billinfo.setPerson(person);
		}
		
		// ҵ�������� Ĭ�� ����
		BusMasterDataClassCollection masterDataInfo = BusMasterDataClassFactory
				.getLocalInstance(ctx).getBusMasterDataClassCollection(
						"where number = '" + m.getMainClass() + "'");
		
		// �Զ����ֶ�
//		billinfo.put("jiezhenyisheng", person);
//		billinfo.put("yewuzhushuju", masterDataInfo);
//		billinfo.put("hisdanjubianma", m.getNumber());
//		billinfo.put("HisReqID", m.getId());
		
		// ҵ������
		BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
				.getBizTypeCollection("where number = '210'").get(0);
		billinfo.setBizType(bizTypeinfo);
		
		OtherBillentryInfo entry = new OtherBillentryInfo();
		entry.setAmount(recAmount); // ���
		entry.setAmountLocal(recAmount);// ��λ��
		entry.setUnVerifyAmount(recAmount); // δ������
		entry.setUnVerifyAmountLocal(recAmount);// δ�����λ��
		entry.setRecievePayAmount(recAmount); // Ӧ�գ��������
		entry.setRecievePayAmountLocal(recAmount);// Ӧ�գ�������λ��
		entry.setPrice(recAmount); // ����
		entry.setRealPrice(recAmount);// ʵ�ʵ���
		entry.setTaxPrice(recAmount);// ��˰����
		entry.setActualPrice(recAmount);// ʵ�ʺ�˰����
		
		entry.setQuantity(qty);// ����
		entry.setLockVerifyAmt(BigDecimal.ZERO); // ���������
		entry.setLockVerifyAmtLocal(BigDecimal.ZERO);// ��������λ��
		entry.setLockUnVerifyAmt(BigDecimal.ZERO); // δ�������
		entry.setLockUnVerifyAmtLocal(BigDecimal.ZERO);// δ������λ��
		entry.setVerifyAmount(BigDecimal.ZERO); // �ѽ�����
		entry.setVerifyAmountLocal(BigDecimal.ZERO); // �ѽ����λ��
		
		entry.setDiscountType(DiscountModeEnum.NULL);// �ۿ۷�ʽ
		entry.setDiscountRate(BigDecimal.ZERO);// ��λ�ۿ�
		entry.setDiscountAmount(BigDecimal.ZERO);
		entry.setDiscountAmountLocal(BigDecimal.ZERO);
		
		entry.setTaxRate(BigDecimal.ZERO); // ˰��
		entry.setTaxAmount(BigDecimal.ZERO);// ˰��
		entry.setTaxAmountLocal(BigDecimal.ZERO);// ˰�λ��
		// FOrderCustomerID �����ͻ�
		// FServiceCustomerID �ͻ��ͻ�
		// entry.setAsstActID(customer.getId().toString());
		// entry.setAsstActName(customer.getName());
		// entry.setAsstActNumber(customer.getNumber());
		
		billinfo.setAsstActID(customer.getId().toString());
		billinfo.setAsstActName(customer.getName());
		billinfo.setAsstActNumber(customer.getNumber());
		
		entry.setOrderCustomer(customer);// �����ͻ�
		entry.setOrdCustName(customer.getName());// �����ͻ�����
		entry.setOrdCustNumber(customer.getNumber());// �����ͻ�����
		
		entry.setServiceCustomer(customer); // �ͻ��ͻ�
		entry.setSerCustName(customer.getName());// �ͻ��ͻ�����
		entry.setSerCustNumber(customer.getNumber());// �ͻ��ͻ�����
		
		entry.setRecAsstActID(customer.getId().toString()); // �ͻ��ͻ�
		entry.setRecAsstActNumber(customer.getName());// �ͻ��ͻ�����
		entry.setRecAsstActNumber(customer.getNumber());// �ͻ��ͻ�����
		
		// ����
		EntityViewInfo viewInfo = new EntityViewInfo();
		FilterInfo filter = new FilterInfo();
		filter.getFilterItems().add(
				new FilterItemInfo("name", "�շ���Ŀ", CompareType.EQUALS));
		viewInfo.setFilter(filter);
		
		IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
		MaterialInfo material = imaterial.getMaterialCollection(viewInfo).get(0);
		entry.setMaterial(material);
		entry.setMaterialModel(material.getModel());
		entry.setMaterialName(material.getName());
		entry.setMeasureUnit(material.getBaseUnit());
		entry.setCompany(xmcompany.getName());
		entry.setBillDate(currentDate);
		
		entry.setWittenOffBaseQty(BigDecimal.ZERO);// �Ѻ�����������
		entry.setLocalWrittenOffAmount(BigDecimal.ZERO);// �Ѻ�����λ�ҽ��
		entry.setUnwriteOffBaseQty(BigDecimal.valueOf(1));// δ������������
		entry.setLocalUnwriteOffAmount(recAmount);// δ������λ�ҽ��
		entry.setSeq(1);
		entry.setBaseQty(qty);
		entry.setBaseUnit(material.getBaseUnit());
//		entry.put("huohao", material.get("huohao"));
//		entry.put("pinpai", material.get("pinpai"));
//		entry.put("zhekoujine", m.getDiscountAmount());
//		entry.put("yingshoujine", recAmount);
//		entry.put("shishoujine", recAmount);
		billinfo.getEntries().addObject(entry);
//		billinfo.put("menzhen", xmcompany); 
//		billinfo.put("shifouzhuanrumenzhen", 0); 
		
		return billinfo;
}
}
