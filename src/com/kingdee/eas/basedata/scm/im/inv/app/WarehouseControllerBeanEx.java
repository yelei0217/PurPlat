package com.kingdee.eas.basedata.scm.im.inv.app;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.framework.Result;

public class WarehouseControllerBeanEx extends WarehouseControllerBean{

	@Override
	protected Result _submit(Context ctx, IObjectCollection colls)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		System.out.println("github test111");
		return super._submit(ctx, colls);
	}

	@Override
	protected void _submit(Context ctx, IObjectPK pk, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._submit(ctx, pk, model);
	}

	@Override
	protected IObjectPK _submit(Context arg0, IObjectValue arg1)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		return super._submit(arg0, arg1);
	}

}
