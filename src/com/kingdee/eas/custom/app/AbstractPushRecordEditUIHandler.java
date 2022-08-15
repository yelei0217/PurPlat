/**
 * output package name
 */
package com.kingdee.eas.custom.app;

import com.kingdee.bos.Context;
import com.kingdee.eas.framework.batchHandler.RequestContext;
import com.kingdee.eas.framework.batchHandler.ResponseContext;


/**
 * output class name
 */
public abstract class AbstractPushRecordEditUIHandler extends com.kingdee.eas.framework.app.EditUIHandler

{
	public void handleActionGenerPurOrder(RequestContext request,ResponseContext response, Context context) throws Exception {
		_handleActionGenerPurOrder(request,response,context);
	}
	protected void _handleActionGenerPurOrder(RequestContext request,ResponseContext response, Context context) throws Exception {
	}
	public void handleActionGenerPurInBIll(RequestContext request,ResponseContext response, Context context) throws Exception {
		_handleActionGenerPurInBIll(request,response,context);
	}
	protected void _handleActionGenerPurInBIll(RequestContext request,ResponseContext response, Context context) throws Exception {
	}
}