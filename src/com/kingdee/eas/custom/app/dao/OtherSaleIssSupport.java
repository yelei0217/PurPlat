package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.ICustomer;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BorrowLandBillEntryInfo;
import com.kingdee.eas.custom.BorrowLandBillInfo;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.common.EntryBaseStatusEnum;
import com.kingdee.eas.scm.im.inv.IOtherInWarehsBill;
import com.kingdee.eas.scm.im.inv.IOtherIssueBill;
import com.kingdee.eas.scm.im.inv.OtherInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.OtherInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.OtherIssueBillEntryInfo;
import com.kingdee.eas.scm.im.inv.OtherIssueBillFactory;
import com.kingdee.eas.scm.im.inv.OtherIssueBillInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.util.UuidException;

public class OtherSaleIssSupport {

	protected BigDecimal zero = new BigDecimal("0.00");

	public static void doSaveBill(Context ctx,BaseSCMDTO m,String busCode){
		try {
				OtherIssueBillInfo info = createBillInfo(ctx, m,busCode);
				IOtherIssueBill ibiz = OtherIssueBillFactory.getLocalInstance(ctx);
				IObjectPK pk = ibiz.save(info);
				ibiz.submit(pk.toString());
			} catch (EASBizException e) {
	 		e.printStackTrace();
		} catch (BOSException e) {
			e.printStackTrace();
		}
	}
	
	private static OtherIssueBillInfo createBillInfo(Context ctx,BaseSCMDTO m,String busCode){
		OtherIssueBillInfo info = new OtherIssueBillInfo ();
		 try {
			SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
			 try {
				info.setBizDate(formmat.parse(m.getFbizdate()));
			} catch (ParseException e) {
			 	e.printStackTrace();
			}
			ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
			StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
			CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
			
			//单据类型
			BillTypeInfo billtype = new BillTypeInfo();
			billtype.setId(BOSUuid.read("50957179-0105-1000-e000-0177c0a812fd463ED552"));
			info.setBillType(billtype);
			info.setIsInitBill(false);
			info.setIsReversed(false); 
			info.setStorageOrgUnit(storageorginfo); 
			info.setHasEffected(false);
			
			info.setTotalStandardCost(BigDecimal.ZERO);
			info.setTotalActualCost(m.getFtotaltaxamount());
			info.setBaseStatus(BillBaseStatusEnum.ADD);
			info.setTotalAmount(m.getFtotalamount());
			info.setTotalQty(BigDecimal.ZERO);
		   
			info.put("MsgId", m.getId());

		    //业务类型
		    BizTypeInfo bizTypeInfo = new BizTypeInfo();
		    bizTypeInfo.setId(BOSUuid.read("Nz878AEgEADgAABMwKg/GiQHQ1w="));
		    info.setBizType(bizTypeInfo);
		    //事务类型
		    TransactionTypeInfo transactiontypeinfo = new TransactionTypeInfo();
		    transactiontypeinfo.setId(BOSUuid.read("DawAAAAPoCGwCNyn"));
		    info.setTransactionType(transactiontypeinfo);  
		    
		    CustomerInfo customerInfo = null ;
		    if(m.getFcustomerid()!=null && !"".equals(m.getFcustomerid())){
		    	ObjectUuidPK cusPK = new ObjectUuidPK(m.getFcustomerid());
				customerInfo =CustomerFactory.getLocalInstance(ctx).getCustomerInfo(cusPK); 
		    }
		
			
			 for (BaseSCMDetailDTO entry : m.getDetails())
			 {
				OtherIssueBillEntryInfo entryInfo = createEntryInfo(ctx,entry,busCode);
			 	entryInfo.setBizDate(info.getBizDate());
				entryInfo.setStorageOrgUnit(storageorginfo);
				entryInfo.setCompanyOrgUnit(xmcompany);
				if(customerInfo!=null)
				entryInfo.setCustomer(customerInfo);

			    info.getEntries().addObject(entryInfo);
			 }
			
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (UuidException e) {
 			e.printStackTrace();
		}
		return info;
	}
	
	
	private static OtherIssueBillEntryInfo createEntryInfo(Context ctx,BaseSCMDetailDTO dvo,String busCode){
		OtherIssueBillEntryInfo  entryInfo = new OtherIssueBillEntryInfo();
		 BigDecimal price = dvo.getFprice();
		 BigDecimal amount = dvo.getFamount();
		 BigDecimal qty = dvo.getFqty(); 
			try {
					InvUpdateTypeInfo updateTypeInfo = new InvUpdateTypeInfo();
					boolean ispresent = false;
					if(dvo.getFispresent() !=null && !"".equals(dvo.getFispresent())&& "1".equals(dvo.getFispresent()))
						ispresent = true;
					else
						ispresent = false;
						
					
					if(ispresent){//是赠品
						updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaPXC73rf"));
					}else{//不是赠品
						//8r0AAAAEaOjC73rf  8r0AAAAEaOjC73rf
						updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaOjC73rf"));
					}
					 entryInfo.setIsPresent(ispresent);
					 
					 entryInfo.setInvUpdateType(updateTypeInfo); 
					 entryInfo.setPrice(price);
					 entryInfo.setAmount(amount);
					 entryInfo.setActualCost(amount);
					 entryInfo.setStandardCost(BigDecimal.ZERO);
					 entryInfo.setAssCoefficient(BigDecimal.ZERO);
					 entryInfo.setAssistQty(BigDecimal.ZERO);   
					 
					 
						EntityViewInfo view = new EntityViewInfo();
					 	FilterInfo filter = new FilterInfo();
					 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
					  	view.setFilter(filter);
					 	
					    MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
					    MaterialInfo material = materialColl.get(0);
					    
					    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
					    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
					    
					    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
					    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
					    
					    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
					    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
					    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
					    entryInfo.setWarehouse(warehouseinfo);
					    
					    
					    
					 entryInfo.setMaterial(material);
					 entryInfo.setAssociateQty(BigDecimal.ZERO); 
					 entryInfo.setBaseQty(qty);
					 entryInfo.setBaseStatus(EntryBaseStatusEnum.ADD);
					 entryInfo.setBaseUnit(baseUnitInfo);
					 entryInfo.setUnit(unitInfo);
					 entryInfo.setWarehouse(warehouseinfo);
					 entryInfo.setUnitStandardCost(BigDecimal.ZERO); 
					 entryInfo.setQty(qty);
					 entryInfo.setUnVmiSettleBaseQty(BigDecimal.ZERO);
					 entryInfo.setTotalVmiSettleBaseQty(BigDecimal.ZERO);
					 entryInfo.put("MsgId", dvo.getId());
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (UuidException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		return entryInfo;
	}
	
//	/**
//	 * 
//	 * @param brInfo 借入借出实体对象
//	 * @return  其他出库单对象实体
//	 */
//	public static OtherIssueBillInfo crateInfo(Context ctx,BorrowLandBillInfo brInfo){
//		OtherIssueBillInfo  info = null;
//		boolean flag = brInfo.getBoolean("Iszengpin");
//		if(brInfo!=null && brInfo.getBaseStatus().equals(BillBaseStatusEnum.AUDITED)){
//			info = new OtherIssueBillInfo ();
//			info.setBizDate(brInfo.getBizDate()); 
//			//单据类型
//		    BillTypeInfo billtype = new BillTypeInfo();
//		    billtype.setId(BOSUuid.read("50957179-0105-1000-e000-0177c0a812fd463ED552"));
//		    info.setBillType(billtype);
//		    info.setIsInitBill(false);
//		    info.setIsReversed(false); 
//		    info.setStorageOrgUnit(brInfo.getStorageOrgUnit()); 
//		    info.setHasEffected(false);
//		    
//		    CompanyOrgUnitInfo cou=null;
// 		 
//			InvUpdateTypeInfo updateTypeInfo = new InvUpdateTypeInfo();
//			if(flag){//是赠品
//				updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaPbC73rf"));
//			}else{//不是赠品
//				//8r0AAAAEaOjC73rf  8r0AAAAEaOjC73rf
//				updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaOnC73rf"));
//			}
//			
//
//			ICustomer icustomer;
//			CustomerInfo customerInfo = null;
//			try {
//				icustomer = CustomerFactory.getLocalInstance(ctx); 
//				String cusSql =" where IsInternalCompany =1 and InternalCompany = '"+brInfo.getStorageOrgUnitIn().getId().toString()+"'";
//				if(icustomer.exists(cusSql)){
//					customerInfo = icustomer.getCustomerInfo(cusSql);
//				}
//			} catch (BOSException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			} catch (EASBizException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		    int entrySize = brInfo.getEntrys().size(); 
//		    BigDecimal totalAmount = BigDecimal.ZERO;
//		    BigDecimal totalQty = BigDecimal.ZERO;
//		    for(int j = 0 ; j < entrySize ; j++){
//		    	BorrowLandBillEntryInfo e1 = brInfo.getEntrys().get(j);
//			 OtherIssueBillEntryInfo entryInfo = new OtherIssueBillEntryInfo(); 
//			 BigDecimal price = e1.getPrice();
//			 BigDecimal amount = e1.getAmount();
//			 BigDecimal qty = e1.getQty(); 
//			 entryInfo.setInvUpdateType(updateTypeInfo); 
//			 entryInfo.setPrice(price);
//			 entryInfo.setAmount(amount);
//			 entryInfo.setActualCost(amount);
//			 entryInfo.setStandardCost(BigDecimal.ZERO);
//			 entryInfo.setAssCoefficient(BigDecimal.ZERO);
// 			 entryInfo.setAssistQty(BigDecimal.ZERO);  
//			 entryInfo.setMaterial(e1.getMaterielnum());
//		     MaterialInfo material = entryInfo.getMaterial();
//				try {
//					material = MaterialFactory.getLocalInstance(ctx).getMaterialInfo(new ObjectUuidPK(e1.getMaterielnum().getId()));
//				} catch (EASBizException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (BOSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			     
//		     MeasureUnitInfo baseunit = material.getBaseUnit();
//		     entryInfo.setMaterial(material);
////			 MeasureUnitInfo unitInfo = new MeasureUnitInfo();
////			 unitInfo.setId(e1.getMaterielnum().getBaseUnit().getId());
////			 entryInfo.setAssistUnit(unit);
//			 entryInfo.setUnit(e1.getMaterielUnit());
//			 entryInfo.setAssociateQty(BigDecimal.ZERO); 
//			 entryInfo.setBaseQty(qty);
//			 entryInfo.setBaseStatus(EntryBaseStatusEnum.ADD);
//			 entryInfo.setBaseUnit(baseunit);
//			 entryInfo.setBaseUnitActualcost(BigDecimal.ZERO);
//			 entryInfo.setBizDate(info.getBizDate());
// 			 entryInfo.setStorageOrgUnit(info.getStorageOrgUnit());
//			 entryInfo.setCompanyOrgUnit(cou); 
//			 entryInfo.setSourceBillId(brInfo.getId().toString());
//			 entryInfo.setSourceBillNumber(brInfo.getNumber());
//			 entryInfo.setSourceBillEntryId(e1.getId().toString());
//			 entryInfo.setSourceBillEntrySeq(e1.getSeq());
//			 entryInfo.setWarehouse(e1.getWarehouseout());
//			 entryInfo.setUnitStandardCost(BigDecimal.ZERO); 
//			 entryInfo.setUnVmiSettleBaseQty(BigDecimal.ZERO);
//			 entryInfo.setTotalVmiSettleBaseQty(BigDecimal.ZERO);
//			 
//			 entryInfo.setIsPresent(flag);
//			 if(customerInfo!=null){
//				 entryInfo.setCustomer(customerInfo);
//			 }
//			 
//			 entryInfo.setQty(qty);
//			 totalAmount =totalAmount.add(amount);
//			 totalQty = totalQty.add(qty);
//			 info.getEntries().addObject(entryInfo);
//		    }
//		    info.setTotalStandardCost(BigDecimal.ZERO);
//		    info.setTotalActualCost(totalAmount);
//		    info.setBaseStatus(BillBaseStatusEnum.ADD); 
//		    info.setTotalAmount(totalAmount);
//		    info.setTotalQty(BigDecimal.ZERO);
//		    
//		    //业务类型
//		    BizTypeInfo bizTypeInfo = new BizTypeInfo();
//		    bizTypeInfo.setId(BOSUuid.read("Nz878AEgEADgAABIwKg/GiQHQ1w="));
//		    info.setBizType(bizTypeInfo);
//		    //事务类型
//		    TransactionTypeInfo transactiontypeinfo = new TransactionTypeInfo();
//		    bizTypeInfo.setId(BOSUuid.read("DawAAAAPoCGwCNyn"));
//		    info.setTransactionType(transactiontypeinfo);  
//		    
////		    BizTypeInfo bizTypeInfo=null;
////		    TransactionTypeInfo transactiontypeinfo = null;
////			try {
////				bizTypeInfo = BizTypeFactory.getLocalInstance(ctx).getBizTypeInfo(new ObjectUuidPK("Nz878AEgEADgAABIwKg/GiQHQ1w="));
////				info.setBizType(bizTypeInfo);
////				transactiontypeinfo = TransactionTypeFactory.getLocalInstance(ctx).getTransactionTypeInfo(new ObjectUuidPK("DawAAAAPoCGwCNyn"));
////			    info.setTransactionType(transactiontypeinfo);  
////			} catch (EASBizException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (BOSException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}  
// 		}
//		return info;
//	}
 
}
