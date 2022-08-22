package com.kingdee.eas.custom.app;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.workflow.metas.SAProcessDATASyncFacadeControllerBean;
import com.kingdee.eas.custom.app.dao.PurInWarehsSupport;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
import com.kingdee.eas.custom.app.dao.SaleIssueSupport;
import com.kingdee.eas.custom.app.dao.SaleOrderSupport;
import com.kingdee.eas.framework.bireport.bimanager.ws.engine.domain.Axis2Exception;
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
		 String res =  PurOrderSupport.syncBill(ctx, jsonStr);
		 logger.info("do savePurOrder method cost :" + this.timer.msValue());
		 return res;
	}


	@Override
	protected String _saveSaleOrder(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  SaleOrderSupport.syncBill(ctx, jsonStr);
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
		 String res =  PurInWarehsSupport.doSync(ctx, jsonStr);
		 logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saveSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  SaleIssueSupport.doSync(ctx, jsonStr);
		 logger.info("do _saleOrderCloseRow saveSaleIss method cost :" + this.timer.msValue());
		 return res;	
	}
	
	
	
 
}