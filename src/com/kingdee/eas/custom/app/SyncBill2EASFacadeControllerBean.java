package com.kingdee.eas.custom.app;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
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
		 String res =  PurOrderSupport.syncBill(ctx, jsonStr);
		 logger.info("do savePurOrder method cost :" + this.timer.msValue());
		 return res;
	}
	
}