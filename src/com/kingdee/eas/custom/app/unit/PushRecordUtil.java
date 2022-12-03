package com.kingdee.eas.custom.app.unit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
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
import com.kingdee.eas.custom.app.dto.base.BaseFIDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.scm.sd.sale.SaleOrderEntryCollection;
import com.kingdee.eas.scm.sd.sale.SaleOrderEntryInfo;
import com.kingdee.eas.scm.sd.sale.SaleOrderInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class PushRecordUtil {
	
	
	
	private Set<String> get(Context ctx,String orderId){
		Set<String> set= null; 
		String sql = "select distinct CFFSUPPLIERID from where fparentid ='"+orderId+"'"; 
		try {
			set = new HashSet<String>();
			IRowSet rs = DbUtil.executeQuery(ctx, sql);
			while(rs.next()){
				if(rs.getObject("CFFSUPPLIERID")!=null && !"".equals(rs.getObject("CFFSUPPLIERID").toString()))
					set.add(rs.getObject("CFFSUPPLIERID").toString());
			} 
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		} 
  
		return set;
	}
	
	

	/**
	 * 	采购入库
	 * @param ctx
	 * @param m
	 * @return
	 * @throws EASBizException
	 * @throws BOSException
	 */
	private static PurInWarehsBillInfo createBillInfo(Context ctx,SaleOrderInfo m,String oper)
    throws EASBizException, BOSException
  {
    PurInWarehsBillInfo info = new PurInWarehsBillInfo();
    StorageOrgUnitInfo storageorginfo = null;
    CompanyOrgUnitInfo xmcompany = null;
    PurchaseOrgUnitInfo purchaseorginfo =  null;
    SupplierInfo supplierInfo = new SupplierInfo();
    if("lz".equals(oper)){
    	ObjectUuidPK orgPK = new ObjectUuidPK("jbYAAAMU2SvM567U");
    	storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
    	xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
    	purchaseorginfo = new PurchaseOrgUnitInfo();
        purchaseorginfo.setId(BOSUuid.read("jbYAAAMU2SvM567U"));
	      supplierInfo.setId(BOSUuid.read("jbYAAAVlObc3xn38"));
	      info.setSupplier(supplierInfo);
	      info.put("iscollpur", Integer.valueOf(1));
    }else{
    	storageorginfo = m.getStorageOrgUnit();
    	xmcompany = m.getCompanyOrgUnit();
    	purchaseorginfo= m.getPurchaseOrgUnit();
	      //supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
	      info.setSupplier(supplierInfo);
	      info.put("iscollpur", Integer.valueOf(0));
    }
 
     String billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//单据类型
    String sourceBilltypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//来源单据类型
    String biztypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C";//业务类型
    String transinfoId ="DawAAAAPoACwCNyn";//事务类型
  
    CtrlUnitInfo cuInfo = storageorginfo.getCU();
    info.setCU(cuInfo);
    info.setStorageOrgUnit(storageorginfo);
    info.setPurchaseType(PurchaseTypeEnum.PURCHASE); 
    
    BillTypeInfo billtype = new BillTypeInfo();
    billtype.setId(BOSUuid.read(billtypeId));
    info.setBillType(billtype);
 
    info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
    info.setCreateTime(new Timestamp(new Date().getTime()));
//    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
	info.setBizDate(m.getBizDate());

    CurrencyInfo currency = new CurrencyInfo();
    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
    info.setCurrency(currency);
    info.setExchangeRate(new BigDecimal("1.00"));
    
//    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
//    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
//      info.setStocker(person);
//    }

    BizTypeInfo bizTypeinfo = new BizTypeInfo();
    bizTypeinfo.setId(BOSUuid.read(biztypeId));
    info.setBizType(bizTypeinfo);
    
    TransactionTypeInfo transinfo = new TransactionTypeInfo();
    transinfo.setId(BOSUuid.read(transinfoId));
    info.setTransactionType(transinfo);
    BillTypeInfo sourceBillTypeInfo =null;
    if(sourceBilltypeId!=null && !"".equals(sourceBilltypeId)){
    	sourceBillTypeInfo = new BillTypeInfo();
        sourceBillTypeInfo.setId(BOSUuid.read(sourceBilltypeId));
    }
    
//    info.put("yisheng", person);
//    info.put("HISdanjubianma", m.getFnumber());
    BigDecimal totalAmount = new BigDecimal(0);
    
    int isCollpur = 0;
//    SupplierInfo supplierInfo = new SupplierInfo();
  
//    if (isCollpur == 1)
//    {
//      supplierInfo.setId(BOSUuid.read("jbYAAAVlObc3xn38"));
//      info.setSupplier(supplierInfo);
//      info.put("iscollpur", Integer.valueOf(1));
//    }
//    else
//    {
//      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
//      info.setSupplier(supplierInfo);
//      info.put("iscollpur", Integer.valueOf(0));
//    }
    //info.put("MsgId", m.getId());

   // info.put("factory", m.getFsupplierid());
  //  BigDecimal qty = new BigDecimal(1);
    String orderId = ""; //b2b订单ID
    SaleOrderEntryCollection saleEntryColl = m.getEntries();
    Iterator it = saleEntryColl.iterator();
    while(it.hasNext()){
    	 SaleOrderEntryInfo saleorderentryInfo = (SaleOrderEntryInfo) it.next();
        PurInWarehsEntryInfo entryInfo = createPurInEntryInfo(ctx,saleorderentryInfo);
        entryInfo.setStorageOrgUnit(storageorginfo);
        entryInfo.setCompanyOrgUnit(xmcompany);
        entryInfo.setBizDate(info.getBizDate());
        entryInfo.setReceiveStorageOrgUnit(storageorginfo);
        entryInfo.setBalanceSupplier(supplierInfo);
        entryInfo.setPurchaseOrgUnit(purchaseorginfo);	
        info.getEntries().addObject(entryInfo);
    }
 
//    info.put("bid", orderId);
   
    return info;
  }
  
  private static PurInWarehsEntryInfo createPurInEntryInfo(Context ctx, SaleOrderEntryInfo dvo)
    throws BOSException, EASBizException
  {
    PurInWarehsEntryInfo entryInfo = new PurInWarehsEntryInfo();
//    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
//    MaterialInfo material = null;
//    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
//    material = imaterial.getMaterialInfo(pk);
//    
//	EntityViewInfo view = new EntityViewInfo();
// 	FilterInfo filter = new FilterInfo();
// 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
//  	view.setFilter(filter);
// 	
//    MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
//    MaterialInfo material = materialColl.get(0);
//    
//    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
//    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
//    
//    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
//    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
//    
//    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
//    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
//    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
//    entryInfo.setWarehouse(warehouseinfo);
//    
    String invUpdateTypeId = "8r0AAAAEaOjC73rf";
    
    InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
    invUpdateType.setId(BOSUuid.read(invUpdateTypeId));
    entryInfo.setInvUpdateType(invUpdateType);
    
    entryInfo.setMaterial(dvo.getMaterial());
    entryInfo.setBaseUnit(dvo.getBaseUnit());
    entryInfo.setUnit(dvo.getUnit());
    entryInfo.setQty(dvo.getQty());
    entryInfo.setBaseQty(dvo.getBaseQty());
    entryInfo.setAssociateQty(BigDecimal.ZERO);
    entryInfo.setWrittenOffQty(dvo.getQty());
    entryInfo.setWrittenOffBaseQty(dvo.getBaseQty());
    entryInfo.setUnWriteOffQty(dvo.getQty());
    entryInfo.setUnWriteOffBaseQty(dvo.getBaseQty());
    entryInfo.setUnVmiSettleBaseQty(dvo.getBaseQty());
    entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqQty(BigDecimal.ZERO);
    entryInfo.setAssistQty(BigDecimal.ZERO);
//    entryInfo.put("MsgId", dvo.getId());
//    entryInfo.put("bid", dvo.getFsourcebillentryid());
    //entryInfo.setStandardCost(BigDecimal.ZERO);
 	    entryInfo.setTaxRate(dvo.getTaxRate());
	    entryInfo.setTax(dvo.getTax());
	    entryInfo.setLocalTax(dvo.getLocalTax());
	    entryInfo.setAmount(dvo.getAmount());
	    entryInfo.setLocalAmount(dvo.getAmount());
	    entryInfo.setWrittenOffAmount(dvo.getAmount());
	    entryInfo.setTaxPrice(dvo.getTaxPrice());
	    entryInfo.setPrice(dvo.getPrice());
	    entryInfo.setActualPrice(dvo.getActualPrice());
	    entryInfo.setActualTaxPrice(dvo.getActualTaxPrice());
	    entryInfo.setTaxAmount(dvo.getTaxAmount());
	    entryInfo.setLocalTaxAmount(dvo.getLocalTaxAmount());
	  //  entryInfo.setUnWriteOffAmount(dvo.getUnWriteOffAmount());
	    entryInfo.setUnitStandardCost(dvo.getPrice());
	    entryInfo.setStandardCost(dvo.getAmount());
	    entryInfo.setUnitActualCost(dvo.getPrice());
	    entryInfo.setActualCost(dvo.getAmount());
	    entryInfo.setUnitPurchaseCost(dvo.getPrice());
	    entryInfo.setPurchaseCost(dvo.getAmount());
	    
//    entryInfo.put("huohao", material.get("huohao"));
//    entryInfo.put("pinpai", material.get("pinpai"));
    
    return entryInfo;
  }
  
  private static SaleIssueBillInfo createSaleBillInfo(Context ctx,SaleOrderInfo m )
  throws BOSException, EASBizException
{
 
  SaleIssueBillInfo info = new SaleIssueBillInfo();
//  ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
  StorageOrgUnitInfo storageorginfo =m.getStorageOrgUnit();
//  StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
  CompanyOrgUnitInfo xmcompany = m.getCompanyOrgUnit();// ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
  CtrlUnitInfo cuInfo = storageorginfo.getCU();
  info.setCU(cuInfo);
  
 
	String billtypeId = "50957179-0105-1000-e000-015bc0a812fd463ED552";//单据类型
	String sourceBilltypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//来源单据类型
	String biztypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C";//业务类型
	String transinfoId ="DawAAAAPoAywCNyn";//事务类型
  info.setStorageOrgUnit(storageorginfo);
  info.setIsSysBill(false);
  info.setConvertMode(ConvertModeEnum.DIRECTEXCHANGERATE);
  
 
  BillTypeInfo billtype = new BillTypeInfo();
  billtype.setId(BOSUuid.read(billtypeId));
  info.setBillType(billtype);
  
  SaleOrgUnitInfo saleOrgInfo =m.getSaleOrgUnit(); //new SaleOrgUnitInfo();
//  saleOrgInfo.setId(BOSUuid.read(m.getFstorageorgunitid()));
  info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
  info.setCreateTime(new Timestamp(new Date().getTime())); 
  info.setBizDate(m.getBizDate());
  PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
  paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
  info.setPaymentType(paymenttypeinfo);
  
  CurrencyInfo currency = new CurrencyInfo();
  currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
  info.setCurrency(currency);
  info.setExchangeRate(new BigDecimal("1.00"));
  
  CustomerInfo customerInfo = new CustomerInfo();
  customerInfo.setId(BOSUuid.read(""));
  info.setCustomer(customerInfo);
  
  BizTypeInfo bizTypeinfo = new BizTypeInfo();
  bizTypeinfo.setId(BOSUuid.read(biztypeId)); //普通销售
  info.setBizType(bizTypeinfo);
  
  TransactionTypeInfo transinfo = new TransactionTypeInfo();
  transinfo.setId(BOSUuid.read(transinfoId));//普通销售出库
  info.setTransactionType(transinfo);
  
//  info.put("MsgId", m.getId());

  BillTypeInfo sourceBillTypeInfo = null ;
  if(sourceBilltypeId != null && !"".equals(sourceBilltypeId)){
	    sourceBillTypeInfo = new BillTypeInfo();
	    sourceBillTypeInfo.setId(BOSUuid.read(sourceBilltypeId));
  }

  SupplierInfo supplierInfo =null;
//  if(m.getFsupplierid() != null && !"".equals(m.getFsupplierid())){
//  	supplierInfo = new SupplierInfo();
//      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
//  }
  String orderId = ""; //b2b订单ID
 // BigDecimal totalAmount = new BigDecimal(0);

  SaleOrderEntryCollection saleEntryColl = m.getEntries();
  Iterator it = saleEntryColl.iterator();
  while(it.hasNext()){
  	SaleOrderEntryInfo saleorderentryInfo = (SaleOrderEntryInfo) it.next();
  	SaleIssueEntryInfo entryInfo = createSaleEntryInfo(ctx,saleorderentryInfo);
      
    entryInfo.setStorageOrgUnit(storageorginfo);
    entryInfo.setCompanyOrgUnit(xmcompany);
    entryInfo.setBizDate(info.getBizDate());
    entryInfo.setBalanceCustomer(customerInfo);
    entryInfo.setSaleOrgUnit(saleOrgInfo);
    info.getEntries().addObject(entryInfo);
  }
 
  
  info.put("bid", orderId);
 

  return info;
}

private static SaleIssueEntryInfo createSaleEntryInfo(Context ctx, SaleOrderEntryInfo dvo)
  throws EASBizException, BOSException
{
//  IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
   SaleIssueEntryInfo entryInfo = new SaleIssueEntryInfo();

//  MaterialInfo material = null;
//  IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
//  material = imaterial.getMaterialInfo(pk);
  
//	EntityViewInfo view = new EntityViewInfo();
//	FilterInfo filter = new FilterInfo();
//	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
// 	view.setFilter(filter);
//	
//   MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
//   MaterialInfo material = materialColl.get(0);
//   
//   IObjectPK pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
//  MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
//  
//  pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
//  MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
//  
//  pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
//  IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
//  WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
//  entryInfo.setWarehouse(warehouseinfo);

  String invUpdateTypeId = "8r0AAAAEaOnC73rf";
  BigDecimal factor = new BigDecimal(1);
 
  InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
  invUpdateType.setId(BOSUuid.read(invUpdateTypeId));
  entryInfo.setInvUpdateType(invUpdateType);
  entryInfo.setMaterial(dvo.getMaterial());
  entryInfo.setBaseUnit(dvo.getBaseUnit());
  entryInfo.setUnit(dvo.getUnit());
  entryInfo.setQty(dvo.getQty());
  entryInfo.setBaseQty(dvo.getBaseQty());
  entryInfo.setAssociateQty(BigDecimal.ZERO);
  entryInfo.setWrittenOffQty(dvo.getQty());
  entryInfo.setWrittenOffBaseQty(dvo.getBaseQty());
  entryInfo.setUnWriteOffQty(dvo.getQty());
  entryInfo.setUnWriteOffBaseQty(dvo.getBaseQty());
  entryInfo.setUnSettleQty(dvo.getQty());
  entryInfo.setUnSettleBaseQty(dvo.getBaseQty());
  entryInfo.setUnVmiSettleBaseQty(dvo.getQty());
  entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
  entryInfo.setAssistQty(BigDecimal.ZERO);
//  entryInfo.put("bid", dvo.getFsourcebillentryid());
//  if(!busCode.contains("VMI")){
  	entryInfo.setTaxRate(dvo.getTaxRate());
 	    entryInfo.setTax(dvo.getTax());
 	    entryInfo.setLocalTax(dvo.getLocalTax());
 	    entryInfo.setAmount(dvo.getAmount());
	    entryInfo.setLocalAmount(dvo.getLocalAmount());
	    entryInfo.setWrittenOffAmount(dvo.getAmount());
	    entryInfo.setTaxPrice(dvo.getTaxPrice());
	    entryInfo.setPrice(dvo.getPrice());
	    entryInfo.setActualPrice(dvo.getActualPrice());
	    entryInfo.setUnWriteOffAmount(dvo.getAmount());
	    entryInfo.setUnitStandardCost(dvo.getPrice());
	    entryInfo.setStandardCost(dvo.getAmount());    
	    entryInfo.setUnitActualCost(dvo.getPrice());
	    entryInfo.setActualCost(dvo.getAmount());
//  }
 
  return entryInfo;
}

}
