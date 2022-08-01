package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.person.PersonInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dto.PurInDTO;
import com.kingdee.eas.custom.app.dto.PurInDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;

public class PurInWarehsSupport {

	private static PurInWarehsBillInfo createPurBillInfo(Context ctx, PurInDTO m,String busCode,String msgId)
    throws EASBizException, BOSException
  {
    PurInWarehsBillInfo info = new PurInWarehsBillInfo();
    String biztypenumber = "110";
    String invUpdateTypenumber = "001";
    String transactiontypenumber = "001";
    BigDecimal defQty = new BigDecimal(1);
//    if ("standard".equals(m.getBillType()))
//    {
//      biztypenumber = "110";
//      invUpdateTypenumber = "001";
//      transactiontypenumber = "001";
//      defQty = new BigDecimal(1);
//    }
//    else if ("rollback".equals(m.getBillType()))
//    {
//      biztypenumber = "111";
//      invUpdateTypenumber = "001";
//      transactiontypenumber = "002";
//      defQty = new BigDecimal(-1);
//    }
    ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);


    CtrlUnitInfo cuInfo = storageorginfo.getCU();
    info.setCU(cuInfo);
    info.setStorageOrgUnit(storageorginfo);
    info.setPurchaseType(PurchaseTypeEnum.PURCHASE);
    
  //  String bizDate = ProcessRollUnit.getPeriodLastDayByPeriodNumber(ctx, m.getPeriod());
    
    BillTypeInfo billtype = new BillTypeInfo();
    billtype.setId(BOSUuid.read("50957179-0105-1000-e000-015fc0a812fd463ED552"));
    info.setBillType(billtype);
    
    PurchaseOrgUnitInfo purchaseorginfo = new PurchaseOrgUnitInfo();
    purchaseorginfo.setId(BOSUuid.read(m.getFstorageorgunitid()));
    
    info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
    info.setCreateTime(new Timestamp(new Date().getTime()));
    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//    Date dateItem = new Date();
//    try {
//		dateItem = formmat.parse(bizDate);
//	} catch (ParseException e) {
// 		e.printStackTrace();
//	}
    info.setBizDate(m.getFbizdate());
    CurrencyInfo currency = new CurrencyInfo();
    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
    info.setCurrency(currency);
    info.setExchangeRate(new BigDecimal("1.00"));
    
    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
      info.setStocker(person);
    }
//    EntityViewInfo viewInfo1 = new EntityViewInfo();
//    FilterInfo filter1 = new FilterInfo();
//    filter1.getFilterItems().add(new FilterItemInfo("name", "%", CompareType.LIKE));
//    filter1.getFilterItems().add(new FilterItemInfo("storageorg.id", m.getFstorageorgunitid(), CompareType.EQUALS));
//    viewInfo1.setFilter(filter1);
 
    
    BizTypeInfo bizTypeinfo = new BizTypeInfo();
    bizTypeinfo.setId(BOSUuid.read("d8e80652-0106-1000-e000-04c5c0a812202407435C"));
    info.setBizType(bizTypeinfo);

    	//BizTypeFactory.getLocalInstance(ctx).getBizTypeCollection("where number = '" + biztypenumber + "'").get(0);
    
    TransactionTypeInfo transinfo = new TransactionTypeInfo();
    transinfo.setId(BOSUuid.read("DawAAAAPoACwCNyn"));
    info.setTransactionType(transinfo);

    	//TransactionTypeFactory.getLocalInstance(ctx).getTransactionTypeInfo("where number = '" + transactiontypenumber + "'");
    
    
    // InvUpdateTypeInfo invUpdateType = InvUpdateTypeFactory.getLocalInstance(ctx).getInvUpdateTypeInfo("where number ='" + invUpdateTypenumber + "' ");
    BillTypeInfo sourceBillTypeInfo = new BillTypeInfo();
    sourceBillTypeInfo.setId(BOSUuid.read("510b6503-0105-1000-e000-010bc0a812fd463ED552"));
    info.setSourceBillType(sourceBillTypeInfo);
    
    info.put("yisheng", person);
    info.put("HisReqID", msgId);
    info.put("HISdanjubianma", m.getFnumber());
    BigDecimal totalAmount = new BigDecimal(0);
    
    int isCollpur = 0;
    SupplierInfo supplierInfo = new SupplierInfo();
  
    if (isCollpur == 1)
    {
      supplierInfo.setId(BOSUuid.read("jbYAAAVlObc3xn38"));
      info.setSupplier(supplierInfo);
      info.put("iscollpur", Integer.valueOf(1));
    }
    else
    {
      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
      info.setSupplier(supplierInfo);
      info.put("iscollpur", Integer.valueOf(0));
    }
    
    info.put("factory", m.getFsupplierid());
  //  BigDecimal qty = new BigDecimal(1);
    for (PurInDetailDTO entry : m.getDetails())
    {
        PurInWarehsEntryInfo entryInfo = createPurEntryInfo(ctx, entry);
        entryInfo.setStorageOrgUnit(storageorginfo);
        entryInfo.setCompanyOrgUnit(xmcompany);
        entryInfo.setBizDate(info.getBizDate());
      //  entryInfo.setWarehouse(warehouseinfo);
        entryInfo.setReceiveStorageOrgUnit(storageorginfo);
      //  entryInfo.setInvUpdateType(invUpdateType);
        entryInfo.setBalanceSupplier(supplierInfo);
        entryInfo.setPurchaseOrgUnit(purchaseorginfo);
        totalAmount = totalAmount.add(entry.getFamount());
        info.getEntries().addObject(entryInfo);
       
    }
    info.setTotalAmount(totalAmount);
    info.setTotalLocalAmount(totalAmount);
    return info;
  }
  
  private static PurInWarehsEntryInfo createPurEntryInfo(Context ctx, PurInDetailDTO dvo)
    throws BOSException, EASBizException
  {
    PurInWarehsEntryInfo entryInfo = new PurInWarehsEntryInfo();
    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);

    MaterialInfo material = null;
    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
    material = imaterial.getMaterialInfo(pk);
    
    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFunitid()));
    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
    
    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFbaseunitid()));
    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
    
    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
    entryInfo.setWarehouse(warehouseinfo);
    
    InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
    invUpdateType.setId(BOSUuid.read("8r0AAAAEaOjC73rf"));
    entryInfo.setInvUpdateType(invUpdateType);
    
    entryInfo.setMaterial(material);
    entryInfo.setBaseUnit(baseUnitInfo);
    entryInfo.setUnit(unitInfo);
    entryInfo.setQty(dvo.getFqty());
    entryInfo.setBaseQty(dvo.getFbaseqty());
    entryInfo.setAssociateQty(BigDecimal.ZERO);
    entryInfo.setWrittenOffQty(dvo.getFqty());
    entryInfo.setWrittenOffBaseQty(dvo.getFbaseqty());
    entryInfo.setUnWriteOffQty(BigDecimal.ZERO);
    entryInfo.setUnWriteOffBaseQty(BigDecimal.ZERO);
    entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqQty(BigDecimal.ZERO);
    entryInfo.setAssistQty(BigDecimal.ZERO);
    //entryInfo.setStandardCost(BigDecimal.ZERO);
    entryInfo.setTax(BigDecimal.ZERO);
    entryInfo.setAmount(dvo.getFamount());
    entryInfo.setLocalAmount(dvo.getFlocalamount());
    entryInfo.setWrittenOffAmount(dvo.getFamount());
    
    entryInfo.setTaxPrice(dvo.getFtaxprice());
    entryInfo.setPrice(dvo.getFprice());
    entryInfo.setActualPrice(dvo.getFtaxprice());
    entryInfo.setActualTaxPrice(dvo.getFtaxprice());
    
    entryInfo.setTaxAmount(dvo.getFtaxamount());
    entryInfo.setLocalTaxAmount(dvo.getFtaxamount());
    entryInfo.setUnWriteOffAmount(dvo.getFamount());
    entryInfo.setUnitStandardCost(dvo.getFprice());
    entryInfo.setStandardCost(dvo.getFamount());
    
    entryInfo.setUnitActualCost(dvo.getFprice());
    entryInfo.setActualCost(dvo.getFamount());
    entryInfo.setUnitPurchaseCost(dvo.getFprice());
    entryInfo.setPurchaseCost(dvo.getFamount());
 
    entryInfo.put("huohao", material.get("huohao"));
    entryInfo.put("pinpai", material.get("pinpai"));
//    entryInfo.put("huanzheID", entry.getPatientId());
//    entryInfo.put("huanzhemingcheng", entry.getPatientName());
    
    return entryInfo;
  }
  
  
}
