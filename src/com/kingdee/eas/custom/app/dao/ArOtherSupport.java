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
		String recType = m.getBillType(); // 业务类型 100:正常 ;101:欠款
		String defaulCsutomerName = "零售客户"; // 默认客户名称
		String defaulPaymentType = "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"; // 赊销
		
		BigDecimal recAmount = m.getRecAmount();
		BigDecimal recAmountnegate = m.getRecAmount().negate();
		BigDecimal qty = BigDecimal.valueOf(1);
		BigDecimal qtynegate = BigDecimal.valueOf(-1);
		
		if ("ys-xf-yjk".equals(recType)) {
			defaulCsutomerName = "零售客户";
		} else if ("ys-xf-other".equals(recType)) {
			defaulCsutomerName = "零售客户"; // 根据付款方式 查询到付款方式上关联的客户 
		}else if ("ys-xf-xj".equals(recType)) {
			defaulCsutomerName = "零售客户";
			defaulPaymentType = "cd54aa9f-03a4-459c-9c5a-5489dce5f0676BCA0AB5"; // 现销
		}else if ("ys-xf-qfd".equals(recType)) {
			defaulCsutomerName = "客户欠款"; 
		}else if ("ys-back-yjk".equals(recType)) {
			defaulCsutomerName = "零售客户";
		}else if ("ys-back-cw".equals(recType)) {
			defaulCsutomerName = "零售客户";
		}else if ("ys-back-kh".equals(recType)) {
			defaulCsutomerName = "零售客户";
		}else if ("ys-back-xj".equals(recType)) {
			defaulCsutomerName = "零售客户";
			defaulPaymentType = "cd54aa9f-03a4-459c-9c5a-5489dce5f0676BCA0AB5"; // 现销
		}else if ("ys-mcqk".equals(recType)||"ys-yjk-zj".equals(recType)) {
			defaulCsutomerName = "客户欠款";
		}
		 
		// 根据 eas标准收费项目判断 如果是现金 付款方式：现销；其他方式赊销
		Date currentDate = new java.util.Date();
		
		OtherBillInfo billinfo = new OtherBillInfo();
		// 创建人
		billinfo.setCreator(ContextUtil.getCurrentUserInfo(ctx));
		// 创建时间
		billinfo.setCreateTime(new java.sql.Timestamp(currentDate.getTime()));
		// 公司ID
		ObjectUuidPK orgpk = new ObjectUuidPK(m.getClinic());
		
		// 组织单元
		CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory.getLocalInstance(
				ctx).getCompanyOrgUnitInfo(orgpk);
		billinfo.setCompany(xmcompany);
		
		CtrlUnitInfo cuInfo = xmcompany.getCU();
		billinfo.setCU(cuInfo);// 管理单元
		
		SaleOrgUnitInfo saleorg = new SaleOrgUnitInfo();
		saleorg.setId(BOSUuid.read(m.getClinic()));
		billinfo.setSaleOrg(saleorg);// 销售组织
		
		// 业务日期
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
		
		// 单据日期 --当前时间
		billinfo.setBillDate(bizDate);
		// 往来类型 客户
		DataBaseInfo database = AsstActTypeFactory.getLocalInstance(ctx)
				.getDataBaseInfo("where number='00001'");
		
		AsstActTypeInfo asstype = new AsstActTypeInfo();
		asstype.setId(BOSUuid.read(database.getId().toString()));
		billinfo.setAsstActType(asstype);
		// 获取结算方式实体
		CustomerInfo customer = CustomerFactory.getLocalInstance(ctx)
				.getCustomerInfo("where name = '" + defaulCsutomerName + "'");
	 
		// 付款方式
		PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
		paymenttypeinfo.setId(BOSUuid.read(defaulPaymentType));
		billinfo.setPaymentType(paymenttypeinfo);
		
		CurrencyInfo currency = new CurrencyInfo();
		currency.setId(BOSUuid
				.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
		billinfo.setCurrency(currency);// 人民币
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
		// 就診医生
		PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m
				.getReceiveDoctor());
		if (person != null && VerifyUtil.notNull(person.getId().toString())) {
			billinfo.setPerson(person);
		}
		
		// 业务主数据 默认 其他
		BusMasterDataClassCollection masterDataInfo = BusMasterDataClassFactory
				.getLocalInstance(ctx).getBusMasterDataClassCollection(
						"where number = '" + m.getMainClass() + "'");
		
		// 自定义字段
//		billinfo.put("jiezhenyisheng", person);
//		billinfo.put("yewuzhushuju", masterDataInfo);
//		billinfo.put("hisdanjubianma", m.getNumber());
//		billinfo.put("HisReqID", m.getId());
		
		// 业务类型
		BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
				.getBizTypeCollection("where number = '210'").get(0);
		billinfo.setBizType(bizTypeinfo);
		
		OtherBillentryInfo entry = new OtherBillentryInfo();
		entry.setAmount(recAmount); // 金额
		entry.setAmountLocal(recAmount);// 金额本位币
		entry.setUnVerifyAmount(recAmount); // 未结算金额
		entry.setUnVerifyAmountLocal(recAmount);// 未结算金额本位币
		entry.setRecievePayAmount(recAmount); // 应收（付）金额
		entry.setRecievePayAmountLocal(recAmount);// 应收（付）金额本位币
		entry.setPrice(recAmount); // 单价
		entry.setRealPrice(recAmount);// 实际单价
		entry.setTaxPrice(recAmount);// 含税单价
		entry.setActualPrice(recAmount);// 实际含税单价
		
		entry.setQuantity(qty);// 数量
		entry.setLockVerifyAmt(BigDecimal.ZERO); // 已锁定金额
		entry.setLockVerifyAmtLocal(BigDecimal.ZERO);// 已锁定金额本位币
		entry.setLockUnVerifyAmt(BigDecimal.ZERO); // 未锁定金额
		entry.setLockUnVerifyAmtLocal(BigDecimal.ZERO);// 未锁定金额本位币
		entry.setVerifyAmount(BigDecimal.ZERO); // 已结算金额
		entry.setVerifyAmountLocal(BigDecimal.ZERO); // 已结算金额本位币
		
		entry.setDiscountType(DiscountModeEnum.NULL);// 折扣方式
		entry.setDiscountRate(BigDecimal.ZERO);// 单位折扣
		entry.setDiscountAmount(BigDecimal.ZERO);
		entry.setDiscountAmountLocal(BigDecimal.ZERO);
		
		entry.setTaxRate(BigDecimal.ZERO); // 税率
		entry.setTaxAmount(BigDecimal.ZERO);// 税额
		entry.setTaxAmountLocal(BigDecimal.ZERO);// 税额本位币
		// FOrderCustomerID 订货客户
		// FServiceCustomerID 送货客户
		// entry.setAsstActID(customer.getId().toString());
		// entry.setAsstActName(customer.getName());
		// entry.setAsstActNumber(customer.getNumber());
		
		billinfo.setAsstActID(customer.getId().toString());
		billinfo.setAsstActName(customer.getName());
		billinfo.setAsstActNumber(customer.getNumber());
		
		entry.setOrderCustomer(customer);// 订货客户
		entry.setOrdCustName(customer.getName());// 订货客户名称
		entry.setOrdCustNumber(customer.getNumber());// 订货客户编码
		
		entry.setServiceCustomer(customer); // 送货客户
		entry.setSerCustName(customer.getName());// 送货客户名称
		entry.setSerCustNumber(customer.getNumber());// 送货客户编码
		
		entry.setRecAsstActID(customer.getId().toString()); // 送货客户
		entry.setRecAsstActNumber(customer.getName());// 送货客户名称
		entry.setRecAsstActNumber(customer.getNumber());// 送货客户编码
		
		// 物料
		EntityViewInfo viewInfo = new EntityViewInfo();
		FilterInfo filter = new FilterInfo();
		filter.getFilterItems().add(
				new FilterItemInfo("name", "收费项目", CompareType.EQUALS));
		viewInfo.setFilter(filter);
		
		IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
		MaterialInfo material = imaterial.getMaterialCollection(viewInfo).get(0);
		entry.setMaterial(material);
		entry.setMaterialModel(material.getModel());
		entry.setMaterialName(material.getName());
		entry.setMeasureUnit(material.getBaseUnit());
		entry.setCompany(xmcompany.getName());
		entry.setBillDate(currentDate);
		
		entry.setWittenOffBaseQty(BigDecimal.ZERO);// 已核销基本数量
		entry.setLocalWrittenOffAmount(BigDecimal.ZERO);// 已核销本位币金额
		entry.setUnwriteOffBaseQty(BigDecimal.valueOf(1));// 未核销基本数量
		entry.setLocalUnwriteOffAmount(recAmount);// 未核销本位币金额
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
