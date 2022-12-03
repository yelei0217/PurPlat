package com.kingdee.eas.basedata.scm.im.inv.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject; 
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.CanNotDeleteException;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.framework.Result;
import com.kingdee.jdbc.rowset.IRowSet;
import com.sun.org.apache.bcel.internal.generic.F2D;

public class WarehouseControllerBeanEx extends WarehouseControllerBean{

	@Override
	protected Result _submit(Context ctx, IObjectCollection colls)
			throws BOSException, EASBizException {
 		return super._submit(ctx, colls);
	}

	@Override
	protected void _submit(Context ctx, IObjectPK pk, IObjectValue model)
			throws BOSException, EASBizException {
		super._submit(ctx, pk, model);
	}

	@Override
	protected IObjectPK _submit(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
 		Boolean isNew = false;
 		if(model.get("ID") ==  null){
			isNew = true; 
		} 
		IObjectPK  id = super._submit(ctx, model);
		if(isNew){
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 5 , "" , 0  , "" ,id.toString()); 
		}
		return id;
	}
	

}
