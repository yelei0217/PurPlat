package com.kingdee.eas.custom.app;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dao.CostAdjusSupport;
import com.kingdee.eas.custom.app.dao.PurInCGWSupport;
import com.kingdee.eas.custom.app.dao.PurInWarehsSupport;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
import com.kingdee.eas.custom.app.dao.SaleIssueSupport;
import com.kingdee.eas.custom.app.dao.SaleOrderSupport;
import com.kingdee.eas.custom.app.dao.base.BaseFISupport;
import com.kingdee.eas.custom.app.dao.base.BaseSCMSupport;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
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
		 System.out.println(res);
		 logger.info("do savePurOrder method cost :" + this.timer.msValue());
		 return res;
	}


	@Override
	protected String _saveSaleOrder(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		// String res =  SaleOrderSupport.syncBill(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saveSaleOrder method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _purOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  PurOrderSupport.doCloseRow(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _purOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saleOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  SaleOrderSupport.doCloseRow(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _savePurInWare(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		return res;	
	}


	@Override
	protected String _saveSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
//		 String res =  SaleIssueSupport.doSync(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean saveSaleIss method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saveApOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseFISupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do BaseFISupport _saveApOtherBill method cost :" + this.timer.msValue());
		return res;	
	}


	@Override
	protected String _saveArOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		 logger.info("do BaseFISupport _saveArOtherBill method cost :" + this.timer.msValue());
		return res;		
	}


	@Override
	protected String _saveCostAdjus(Context ctx, String jsonStr)
			throws BOSException {
		 this.timer.reset(); 
		 String res =  CostAdjusSupport.doSync(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saveCostAdjus _saveCostAdjus method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _savePaymentBill(Context ctx, String jsonStr)
			throws BOSException {
 		//return super._savePaymentBill(ctx, jsonStr);
		 String  result ="_savePaymentBill";
        try {
			HttpClient httpClient = new HTTPSTrustClient().init();
			result += HTTPSClientUtil.doPostJson(httpClient, InterfaceResource.sap_base_url, jsonStr);			
		} catch (Exception e) {
 			e.printStackTrace();
		}
 		 
 		return result;
	}


	@Override
	protected String _saveReceiveBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		 logger.info("do BaseFISupport _saveReceiveBill method cost :" + this.timer.msValue());
		return res;	
	}

	
	/**
	 * 
	 * 采购入库单--退库
	 */
	@Override
	protected String _rollBackPurchInBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  PurInWarehsSupport.doRollBackBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do PurInWarehsSupport _rollBackPurchInBill method cost :" + this.timer.msValue());
		return res;	
	}


	/**
	 * 
	 * 销售出库单--退库
	 * 
	 */
	@Override
	protected String _rollBackSaleIssBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  SaleIssueSupport.doRollBackBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do _rollBackSaleIssBill SaleIssueSupport method cost :" + this.timer.msValue());
		return res;	
	}

	/**
	 * 
	 * 其他入库单--同步
	 * 
	 */
	@Override
	protected String _saveOtherPurIn(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean _saveOtherPurIn method cost :" + this.timer.msValue());
		 return res;
	}
	
	/**
	 * 
	 * 其他出库单--同步
	 * 
	 */
	@Override
	protected String _saveOtherSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean _saveOtherSaleIss method cost :" + this.timer.msValue());
		 return res;
	}


	/**
	 * 
	 * 成果物 -- 同步
	 * 
	 */
	@Override
	protected String _savePurInCGW(Context ctx, String jsonStr)throws BOSException {
		 String res =  PurInCGWSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
 		 logger.info("do SyncBill2EASFacadeControllerBean _savePurInCGW method cost :" + this.timer.msValue());
		 return res;
 		
	}
	
	
	
 
}