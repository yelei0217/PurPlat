package com.kingdee.eas.custom.app;

import javax.ejb.*;
import java.rmi.RemoteException;
import com.kingdee.bos.*;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.rule.RuleExecutor;
import com.kingdee.bos.metadata.MetaDataPK;
//import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.framework.ejb.AbstractEntityControllerBean;
import com.kingdee.bos.framework.ejb.AbstractBizControllerBean;
//import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.service.ServiceContext;
import com.kingdee.bos.service.IServiceContext;
import com.kingdee.eas.framework.Result;
import com.kingdee.eas.framework.LineResult;
import com.kingdee.eas.framework.exception.EASMultiException;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;

import java.lang.String;



public abstract class AbstractSyncDataEASFacadeControllerBean extends AbstractBizControllerBean implements SyncDataEASFacadeController
{
    protected AbstractSyncDataEASFacadeControllerBean()
    {
    }

    protected BOSObjectType getBOSType()
    {
        return new BOSObjectType("5FE133C7");
    }

    public void syncDateByType(Context ctx, int type, String data, int newOrDele, String name, String number) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("68728a16-208a-4c3f-b266-fb2457f3ccf6"), new Object[]{ctx, new Integer(type), data, new Integer(newOrDele), name, number});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _syncDateByType(ctx, type, data, newOrDele, name, number);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _syncDateByType(Context ctx, int type, String data, int newOrDele, String name, String number) throws BOSException
    {    	
        return;
    }

}