package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.ICustomer;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BorrowLandBillEntryInfo;
import com.kingdee.eas.custom.BorrowLandBillInfo;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.common.EntryBaseStatusEnum;
import com.kingdee.eas.scm.im.inv.OtherInWarehsBillEntryInfo;
import com.kingdee.eas.scm.im.inv.OtherInWarehsBillInfo;

public class OtherPurInSupport {

	protected BigDecimal zero = new BigDecimal("0.00");
	/**
	 * 
	 * @param brInfo 借入借出实体对象
	 * @return  其他入库单对象实体
	 */
	public static OtherInWarehsBillInfo createInfo(Context ctx,BorrowLandBillInfo brInfo){
		OtherInWarehsBillInfo  info = null;
		boolean flag = brInfo.getBoolean("Iszengpin");
		if(brInfo!=null && brInfo.getBaseStatus().equals(BillBaseStatusEnum.AUDITED)){
			info = new OtherInWarehsBillInfo();
			info.setBizDate(brInfo.getBizDate()); 
			//单据类型
		    BillTypeInfo billtype = new BillTypeInfo();
		    billtype.setId(BOSUuid.read("50957179-0105-1000-e000-017bc0a812fd463ED552"));
		    info.setBillType(billtype);
		    info.setIsInitBill(false);
		    info.setIsReversed(false); 
		    info.setStorageOrgUnit(brInfo.getStorageOrgUnitIn()); 
		    info.setHasEffected(false);
		    
		    CompanyOrgUnitInfo cou=null;
		  
			InvUpdateTypeInfo updateTypeInfo = new InvUpdateTypeInfo();
			if(flag){//是赠品
				updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaPXC73rf"));
			}else{//不是赠品
				//8r0AAAAEaOjC73rf  8r0AAAAEaOjC73rf
				updateTypeInfo.setId(BOSUuid.read("8r0AAAAEaOjC73rf"));
			}
			
			ICustomer icustomer;
			CustomerInfo customerInfo = null;
			try {
				icustomer = CustomerFactory.getLocalInstance(ctx); 
				String cusSql =" where IsInternalCompany =1 and InternalCompany = '"+brInfo.getStorageOrgUnit().getId().toString()+"'";
				if(icustomer.exists(cusSql)){
					customerInfo = icustomer.getCustomerInfo(cusSql);
				}
			} catch (BOSException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		    int entrySize = brInfo.getEntrys().size(); 
		    BigDecimal totalAmount = BigDecimal.ZERO;
		    BigDecimal totalQty = BigDecimal.ZERO;
		    for(int j = 0 ; j < entrySize ; j++){
		    	BorrowLandBillEntryInfo e1 = brInfo.getEntrys().get(j);
		    	OtherInWarehsBillEntryInfo entryInfo = new OtherInWarehsBillEntryInfo(); 
			 BigDecimal price = e1.getPrice();
			 BigDecimal amount = e1.getAmount();
			 BigDecimal qty = e1.getQty(); 
			 entryInfo.setInvUpdateType(updateTypeInfo); 
			 entryInfo.setPrice(price);
			 entryInfo.setAmount(amount);
			 entryInfo.setActualCost(amount);
			 entryInfo.setStandardCost(BigDecimal.ZERO);
			 entryInfo.setAssCoefficient(BigDecimal.ZERO);
 			 entryInfo.setAssistQty(BigDecimal.ZERO);   
 		 
		     MaterialInfo material = entryInfo.getMaterial();
			try {
				material = MaterialFactory.getLocalInstance(ctx).getMaterialInfo(new ObjectUuidPK(e1.getMaterielnum().getId()));
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BOSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		     MeasureUnitInfo baeunit = material.getBaseUnit();
		     entryInfo.setMaterial(material);
//		     entryInfo.setId(e1.getMaterielnum().getBaseUnit().getId());
			// entryInfo.setAssistUnit(unit);
			 entryInfo.setAssociateQty(BigDecimal.ZERO); 
			 entryInfo.setBaseQty(qty);
			 entryInfo.setBaseStatus(EntryBaseStatusEnum.ADD);
			 entryInfo.setBaseUnit(baeunit);
			 entryInfo.setUnit(e1.getMaterielUnit());
			// entryInfo.setBaseUnitActualcost(BigDecimal.ZERO);
			 entryInfo.setBizDate(info.getBizDate());
 			 entryInfo.setStorageOrgUnit(info.getStorageOrgUnit());
			 entryInfo.setCompanyOrgUnit(cou); 
			 entryInfo.setSourceBillId(brInfo.getId().toString());
			 entryInfo.setSourceBillNumber(brInfo.getNumber());
			 entryInfo.setSourceBillEntryId(e1.getId().toString());
			 entryInfo.setSourceBillEntrySeq(e1.getSeq());
			 entryInfo.setWarehouse(e1.getWarehousein());
			 entryInfo.setUnitStandardCost(BigDecimal.ZERO); 
			 entryInfo.setQty(qty);
			 entryInfo.setUnVmiSettleBaseQty(BigDecimal.ZERO);
			 entryInfo.setTotalVmiSettleBaseQty(BigDecimal.ZERO);
			 
			 entryInfo.setIsPresent(flag);
			 
			 if(customerInfo!=null){
				 entryInfo.setCustomer(customerInfo);
			 }
			 totalAmount =totalAmount.add(amount);
			 totalQty = totalQty.add(qty);
			 info.getEntries().addObject(entryInfo);
		    } 
		    info.setTotalStandardCost(BigDecimal.ZERO);
		    info.setTotalActualCost(totalAmount);
		    info.setBaseStatus(BillBaseStatusEnum.ADD);
		    info.setTotalAmount(totalAmount);
		    info.setTotalQty(BigDecimal.ZERO);
		    //业务类型
		    BizTypeInfo bizTypeInfo = new BizTypeInfo();
		    bizTypeInfo.setId(BOSUuid.read("N5d2igEgEADgAABzwKg/GiQHQ1w="));
		    info.setBizType(bizTypeInfo);
		    
		    //事务类型
		    TransactionTypeInfo transactiontypeinfo = new TransactionTypeInfo();
		    bizTypeInfo.setId(BOSUuid.read("DawAAAAPoCWwCNyn"));
		    info.setTransactionType(transactiontypeinfo);  
  
//		    BizTypeInfo bizTypeInfo=null;
//		    TransactionTypeInfo transactiontypeinfo = null;
//			try {
//				bizTypeInfo = BizTypeFactory.getLocalInstance(ctx).getBizTypeInfo(new ObjectUuidPK("N5d2igEgEADgAABzwKg/GiQHQ1w="));
//				info.setBizType(bizTypeInfo);
//				transactiontypeinfo = TransactionTypeFactory.getLocalInstance(ctx).getTransactionTypeInfo(new ObjectUuidPK("DawAAAAPoCWwCNyn"));
//			    info.setTransactionType(transactiontypeinfo);  
//			} catch (EASBizException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (BOSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
 		}
		return info;
	}
}
