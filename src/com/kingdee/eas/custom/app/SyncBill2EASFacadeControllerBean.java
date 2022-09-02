package com.kingdee.eas.custom.app;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dao.CostAdjusSupport;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
import com.kingdee.eas.custom.app.dao.SaleOrderSupport;
import com.kingdee.eas.custom.app.dao.base.BaseFISupport;
import com.kingdee.eas.custom.app.dao.base.BaseSCMSupport;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.util.LowTimer;

public class SyncBill2EASFacadeControllerBean extends AbstractSyncBill2EASFacadeControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2240878356734475478L;
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.SyncBill2EASFacadeControllerBean");
	private LowTimer timer = new LowTimer();
	
	
	@Override
	protected String _savePurOrder(Context ctx, String jsonStr)	throws BOSException {
		this.timer.reset(); 
//		 String res =  PurOrderSupport.syncBill(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 logger.info("do savePurOrder method cost :" + this.timer.msValue());
		 return res;
	}


	@Override
	protected String _saveSaleOrder(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		// String res =  SaleOrderSupport.syncBill(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 logger.info("do _saveSaleOrder method cost :" + this.timer.msValue());
		 return res;	}


	@Override
	protected String _purOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  PurOrderSupport.doCloseRow(ctx, jsonStr);
		 logger.info("do _purOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saleOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  SaleOrderSupport.doCloseRow(ctx, jsonStr);
		 logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _savePurInWare(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
//		 String res =  PurInWarehsSupport.doSync(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saveSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
//		 String res =  SaleIssueSupport.doSync(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 logger.info("do SyncBill2EASFacadeControllerBean saveSaleIss method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saveApOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseFISupport.syncBill(ctx, jsonStr);
		 logger.info("do BaseFISupport _saveApOtherBill method cost :" + this.timer.msValue());
		return res;	
	}


	@Override
	protected String _saveArOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		 logger.info("do BaseFISupport _saveArOtherBill method cost :" + this.timer.msValue());
		return res;		
	}


	@Override
	protected String _saveCostAdjus(Context ctx, String jsonStr)
			throws BOSException {
		try {
			 IObjectPK orgPK = new  ObjectUuidPK("jbYAAAad1Rl4MGHj");
			 PurInWarehsBillInfo in = PurInWarehsBillFactory.getLocalInstance(ctx).getPurInWarehsBillInfo(orgPK);
			 
			 PurInWarehsEntryInfo entry = (PurInWarehsEntryInfo) in.getEntries().getObject(0);
			 PurInWarehsEntryInfo entry1 = (PurInWarehsEntryInfo) entry.clone();
			 entry1.setQty(new BigDecimal(1));
			 entry1.setBaseQty(new BigDecimal(1));
			 in.getEntries().clear();
			 in.getEntries().addObject(entry1);
			 CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
			 
			 sourceColl.add(in);
 			 List<IObjectPK> pkIns = AppUnit.botpSave(ctx, "783061E3", sourceColl, "JV7MYpL+QEKaxoy2KYZKzwRRIsQ=");

		} catch (EASBizException e) {
			e.printStackTrace();
		}
		return "";	
    	
//		this.timer.reset(); 
//		 String res =  CostAdjusSupport.doSync(ctx, jsonStr);
//		 logger.info("do _saveCostAdjus _saveCostAdjus method cost :" + this.timer.msValue());
//		 return res;	
	}


	@Override
	protected String _savePaymentBill(Context ctx, String jsonStr)
			throws BOSException {
 		return super._savePaymentBill(ctx, jsonStr);
	}


	@Override
	protected String _saveReceiveBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		 logger.info("do BaseFISupport _saveReceiveBill method cost :" + this.timer.msValue());
		return res;	
	}
	
	
	
 
}