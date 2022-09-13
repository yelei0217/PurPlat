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

    public void DoErrorJon(Context ctx, String data) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("d359caef-7a73-4414-b451-a338eb8b0aad"), new Object[]{ctx, data});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _DoErrorJon(ctx, data);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _DoErrorJon(Context ctx, String data) throws BOSException
    {    	
        return;
    }

    public String materialSyncFun(Context ctx, String data) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("3137f617-0d1f-4d45-a551-1e5895818966"), new Object[]{ctx, data});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_materialSyncFun(ctx, data);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _materialSyncFun(Context ctx, String data) throws BOSException
    {    	
        return null;
    }

    public void DoMaterialJson(Context ctx, String data) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("bed676fe-acc0-41c6-8066-c0e02aad0e07"), new Object[]{ctx, data});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _DoMaterialJson(ctx, data);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _DoMaterialJson(Context ctx, String data) throws BOSException
    {    	
        return;
    }

    public void doCustomerMid(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("e7952baf-2485-41e2-b12d-0652fb49ee1b"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _doCustomerMid(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _doCustomerMid(Context ctx) throws BOSException
    {    	
        return;
    }

    public void doOrgMid(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("9f0b981c-d4ea-4258-b699-c782a0e1c677"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _doOrgMid(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _doOrgMid(Context ctx) throws BOSException
    {    	
        return;
    }

    public void doSuppMid(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("d9cb5cdb-ca3d-4380-9631-85f7c3f5fb40"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _doSuppMid(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _doSuppMid(Context ctx) throws BOSException
    {    	
        return;
    }

    public void doPersonMid(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("37226f02-d5d3-4fdc-8e26-6a34970d9eb8"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _doPersonMid(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _doPersonMid(Context ctx) throws BOSException
    {    	
        return;
    }

    public void doCangkuMid(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("9838503f-8349-4c50-b271-397c2c1cdc09"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _doCangkuMid(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _doCangkuMid(Context ctx) throws BOSException
    {    	
        return;
    }

    public String getWareclinicRales(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("579fc9c8-43bd-44e4-820a-087994c33156"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_getWareclinicRales(ctx, jsonStr);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _getWareclinicRales(Context ctx, String jsonStr) throws BOSException
    {    	
        return null;
    }

}