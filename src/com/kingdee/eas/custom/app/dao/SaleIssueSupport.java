package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.ConvertModeEnum;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseCollection;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.FreeItemFactory;
import com.kingdee.eas.custom.FreeItemInfo;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.entity.ProcessIPOEntity;
import com.kingdee.eas.custom.entity.ProcessIPOModel;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;

public class SaleIssueSupport {

	
	 public static SaleIssueBillInfo createSaleBillInfo(Context ctx, ProcessIPOModel m)
	    throws BOSException, EASBizException
	  {
	    String biztypenumber = "210";
	    String transactiontypenumber = "010";
	    String invUpdateTypenumber = "002";
	    BigDecimal defQty = new BigDecimal(1);
	   
	    SaleIssueBillInfo info = new SaleIssueBillInfo();
	    ObjectUuidPK orgPK = new ObjectUuidPK(m.getClinic());
	    
	    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
	    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
	    CtrlUnitInfo cuInfo = storageorginfo.getCU();
	    info.setCU(cuInfo);
	    
	    info.setStorageOrgUnit(storageorginfo);
	    info.setIsSysBill(false);
	    info.setConvertMode(ConvertModeEnum.DIRECTEXCHANGERATE);
	    
	    BillTypeInfo billtype = new BillTypeInfo();
	    billtype.setId(BOSUuid.read("50957179-0105-1000-e000-015bc0a812fd463ED552"));
	    info.setBillType(billtype);
	    
	    SaleOrgUnitInfo saleOrgInfo = new SaleOrgUnitInfo();
	    saleOrgInfo.setId(BOSUuid.read(m.getClinic()));
	    
	    info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
	    info.setCreateTime(new Timestamp(new Date().getTime()));
	    
	    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	    Date bizDate = new Date();
//	    try
//	    {
//	      bizDate = formmat.parse(ProcessRollUnit.getPeriodLastDayByPeriodNumber(ctx, m.getPeriod()));
//	    }
//	    catch (ParseException e)
//	    {
//	      e.printStackTrace();
//	    }
	   // info.setBizDate(bizDate);
	    
	    PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
	    paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
	    info.setPaymentType(paymenttypeinfo);
	    
	    CurrencyInfo currency = new CurrencyInfo();
	    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
	    info.setCurrency(currency);
	    info.setExchangeRate(new BigDecimal("1.00"));
	    
	    EntityViewInfo viewInfoCustomer = new EntityViewInfo();
	    FilterInfo filterCustomer = new FilterInfo();
	    filterCustomer.getFilterItems().add(new FilterItemInfo("name", "", CompareType.EQUALS));
	    viewInfoCustomer.setFilter(filterCustomer);
	    CustomerInfo customerInfo = CustomerFactory.getLocalInstance(ctx).getCustomerCollection(viewInfoCustomer).get(0);
	    info.setCustomer(customerInfo);
	    
//	    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getDoctor());
//	    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
//	      info.setStocker(person);
//	    }
	    EntityViewInfo viewInfo1 = new EntityViewInfo();
	    FilterInfo filter1 = new FilterInfo();
	    filter1.getFilterItems().add(new FilterItemInfo("name", "%", CompareType.LIKE));
	    filter1.getFilterItems().add(new FilterItemInfo("storageorg.id", m.getClinic(), CompareType.EQUALS));
	    viewInfo1.setFilter(filter1);
	    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
	    WarehouseCollection collection1 = iwarehouse.getWarehouseCollection(viewInfo1);
	    
	    WarehouseInfo warehouseinfo = collection1.get(0);
	    
//	    BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx).getBizTypeCollection("where number = '" + biztypenumber + "'").get(0);
//	    info.setBizType(bizTypeinfo);
//	    
//	    TransactionTypeInfo transinfo = TransactionTypeFactory.getLocalInstance(ctx).getTransactionTypeInfo("where number = '" + transactiontypenumber + "'");
//	    info.setTransactionType(transinfo);
	    
	    BizTypeInfo bizTypeinfo = new BizTypeInfo();
	    bizTypeinfo.setId(BOSUuid.read("d8e80652-0106-1000-e000-04c5c0a812202407435C"));
	    info.setBizType(bizTypeinfo);
	    
	    TransactionTypeInfo transinfo = new TransactionTypeInfo();
	    transinfo.setId(BOSUuid.read("DawAAAAPoACwCNyn"));
	    info.setTransactionType(transinfo);
	    	    
	    BigDecimal totalAmount = new BigDecimal(0);
	    int isCollpur = m.getIsCollPur();
	    for (ProcessIPOEntity entry : m.getEntitys())
	    {
	      if (entry.getQty() != null) {
	        defQty = entry.getQty();
	      }
	      if (isCollpur == 1)
	      {
	        if (AppUnit.isExistsHisMaterial(ctx, entry.getMaterial()))
	        {
	          Map<String, String> materialMap = AppUnit.getMaterialIDsByHISName(ctx, entry.getMaterial());
	          String materialId = (String)materialMap.get("material");
	          String designId = (String)materialMap.get("design");
	          
	          SaleIssueEntryInfo entryInfo = createSaleEntryInfo(ctx, entry, defQty, entry.getMaterialAmount(), materialId, "iscoll");
	          entryInfo.setStorageOrgUnit(storageorginfo);
	          entryInfo.setCompanyOrgUnit(xmcompany);
	          entryInfo.setBizDate(info.getBizDate());
	          entryInfo.setWarehouse(warehouseinfo);
	        //  entryInfo.setInvUpdateType(invUpdateType);
	          entryInfo.setBalanceCustomer(customerInfo);
	          entryInfo.setSaleOrgUnit(saleOrgInfo);
	          if (VerifyUtil.notNull(m.getIscgjz())) {
	            entryInfo.put("iscgjz", m.getIscgjz());
	          }
	          if ((designId != null) && (!"".equals(designId)))
	          {
	            SaleIssueEntryInfo entryInfo1 = createSaleEntryInfo(ctx, entry, defQty, entry.getDesignAmount(), designId, "iscoll");
	            entryInfo1.setStorageOrgUnit(storageorginfo);
	            entryInfo1.setCompanyOrgUnit(xmcompany);
	            entryInfo1.setBizDate(info.getBizDate());
	            entryInfo1.setWarehouse(warehouseinfo);
	        //    entryInfo1.setInvUpdateType(invUpdateType);
	            entryInfo1.setBalanceCustomer(customerInfo);
	            entryInfo1.setSaleOrgUnit(saleOrgInfo);
	            if (VerifyUtil.notNull(m.getIscgjz())) {
	              entryInfo1.put("iscgjz", m.getIscgjz());
	            }
	            info.getEntries().addObject(entryInfo1);
	          }
	          totalAmount = totalAmount.add(entry.getAmount());
	          info.getEntries().addObject(entryInfo);
	        }
	      }
	      else
	      {
	        SaleIssueEntryInfo entryInfo = createSaleEntryInfo(ctx, entry, defQty, entry.getAmount(), entry.getMaterial(), "");
	        entryInfo.setStorageOrgUnit(storageorginfo);
	        entryInfo.setCompanyOrgUnit(xmcompany);
	        entryInfo.setBizDate(info.getBizDate());
	        entryInfo.setWarehouse(warehouseinfo);
	      //  entryInfo.setInvUpdateType(invUpdateType);
	        entryInfo.setBalanceCustomer(customerInfo);
	        entryInfo.setSaleOrgUnit(saleOrgInfo);
	        if (VerifyUtil.notNull(m.getIscgjz())) {
	          entryInfo.put("iscgjz", m.getIscgjz());
	        }
	        totalAmount = totalAmount.add(entry.getAmount());
	        info.getEntries().addObject(entryInfo);
	      }
	    }
	    info.setTotalAmount(totalAmount);
	    info.setTotalLocalAmount(totalAmount);
	    info.put("yisheng", "");
	    info.put("HisReqID", m.getId());
	    info.put("HISdanjubianma", m.getNumber());
 	    info.put("saleid", m.getSaleid());
	    if ((m.getIsyxjz() != null) && (!"".equals(m.getIsyxjz()))) {
	      info.put("isyxjz", m.getIsyxjz());
	    }
	    return info;
	  }
	  
	  private static SaleIssueEntryInfo createSaleEntryInfo(Context ctx, ProcessIPOEntity entry, BigDecimal qty, BigDecimal amount, String materialName, String oper)
	    throws EASBizException, BOSException
	  {
	    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
	    MaterialInfo material = null;
	    BigDecimal price = amount;
	    if ((oper != null) && ("iscoll".equals(oper)))
	    {
	      IObjectPK pk = new ObjectUuidPK(BOSUuid.read(materialName));
	      material = imaterial.getMaterialInfo(pk);
	      price = amount.divide(qty);
	    }
	    else
	    {
	      EntityViewInfo viewInfo = new EntityViewInfo();
	      FilterInfo filter = new FilterInfo();
	      filter.getFilterItems().add(new FilterItemInfo("number", materialName, CompareType.EQUALS));
	      viewInfo.setFilter(filter);
	      material = imaterial.getMaterialCollection(viewInfo).get(0);
	    }
	    SaleIssueEntryInfo entryInfo = new SaleIssueEntryInfo();
	    entryInfo.setMaterial(material);
	    entryInfo.setBaseUnit(material.getBaseUnit());
	    entryInfo.setUnit(material.getBaseUnit());
	    
	    entryInfo.setQty(qty);
	    entryInfo.setBaseQty(qty);
	    entryInfo.setAssociateQty(qty);
	    entryInfo.setUnWriteOffQty(qty);
	    entryInfo.setUnWriteOffBaseQty(qty);
	    entryInfo.setUnReturnedBaseQty(qty);
	    entryInfo.setUndeliverQty(qty);
	    entryInfo.setUndeliverBaseQty(BigDecimal.ZERO);
	    entryInfo.setUnInQty(qty);
	    entryInfo.setUnInBaseQty(qty);
	    entryInfo.setUnitStandardCost(BigDecimal.ZERO);
	    entryInfo.setStandardCost(BigDecimal.ZERO);
	    entryInfo.setTax(BigDecimal.ZERO);
	    
	    entryInfo.setAmount(amount);
	    entryInfo.setLocalAmount(amount);
	    entryInfo.setWrittenOffAmount(amount);
	    entryInfo.setNonTaxAmount(amount);
	    
	    entryInfo.setSalePrice(price);
	    entryInfo.setPrice(price);
	    entryInfo.setTaxPrice(price);
	    entryInfo.setActualPrice(price);
	    
	    entryInfo.put("yawei", entry.getToothPosition());
	    entryInfo.put("huohao", material.get("huohao"));
	    entryInfo.put("pinpai", material.get("pinpai"));
	    entryInfo.put("huanzheID", entry.getPatientId());
	    entryInfo.put("huanzhemingcheng", entry.getPatientName());
	    
	    entryInfo.put("saleentryid", entry.getSaleentryid());
	    entryInfo.put("hismingxiID", entry.getId());
	    FreeItemInfo freeItemInfo = null;
	    if (VerifyUtil.notNull(entry.getEasChargeItemDetail()))
	    {
	      freeItemInfo = FreeItemFactory.getLocalInstance(ctx).getFreeItemInfo(new ObjectUuidPK(entry.getEasChargeItemDetail()));
	      entryInfo.put("eassfxm", freeItemInfo.getId().toString());
	    }
	    entryInfo.put("hisFirstItem", entry.getFirstCategoryId());
	    entryInfo.put("hisFirstItemName", entry.getFirstCategoryName());
	    entryInfo.put("hisChargeItem", entry.getSecondCategoryId());
	    entryInfo.put("hisChargeItemName", entry.getSecondCategoryName());
	    entryInfo.put("hissfxmid", entry.getHisChargeItemDetail());
	    entryInfo.put("hissfxm", entry.getHisChargeItem());
	    
	    
	    InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
	    invUpdateType.setId(BOSUuid.read("8r0AAAAEaOjC73rf"));
	    entryInfo.setInvUpdateType(invUpdateType);
	    
	    
	    
	    
	    return entryInfo;
	  }
	  
	  
}
