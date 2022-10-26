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



public abstract class AbstractPushRecordFacadeControllerBean extends AbstractBizControllerBean implements PushRecordFacadeController
{
    protected AbstractPushRecordFacadeControllerBean()
    {
    }

    protected BOSObjectType getBOSType()
    {
        return new BOSObjectType("1C9F64CA");
    }

    public void generPurOrder(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("2abca07b-1f42-4d5b-9b27-6a8007036245"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _generPurOrder(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _generPurOrder(Context ctx) throws BOSException
    {    	
        return;
    }

    public void generPurInBIll(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("841232da-68c9-4a15-a87e-643dbc32e2f4"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _generPurInBIll(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _generPurInBIll(Context ctx) throws BOSException
    {    	
        return;
    }

    public void matchAoutIssueTask(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("622db534-7556-4120-a39b-7d95fefa9bdf"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _matchAoutIssueTask(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _matchAoutIssueTask(Context ctx) throws BOSException
    {    	
        return;
    }

    public void generSaleIssueBill(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("8e264aa1-d91d-43b5-bde6-39b8eece3503"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _generSaleIssueBill(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _generSaleIssueBill(Context ctx) throws BOSException
    {    	
        return;
    }

    public void syncSaleOrder(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("f96389cd-df10-4bf3-a329-c96553ec1b3e"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _syncSaleOrder(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _syncSaleOrder(Context ctx) throws BOSException
    {    	
        return;
    }

    public void cancelPrice(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("ff41f9b8-03e0-43a4-b73a-3afa3f5d4ed0"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _cancelPrice(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _cancelPrice(Context ctx) throws BOSException
    {    	
        return;
    }

    public void noCommonPush(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("9b7cff6e-29be-4ede-8b12-1b360f835035"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _noCommonPush(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _noCommonPush(Context ctx) throws BOSException
    {    	
        return;
    }

    public void noCommonInitData(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("ad704e78-9a1a-4a5b-b5c2-7a1143b7422d"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _noCommonInitData(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _noCommonInitData(Context ctx) throws BOSException
    {    	
        return;
    }

    public void savePayment2PurLog(Context ctx, String id, String busCode) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("8b4e3edf-738e-4bc4-977d-6fb8a216577c"), new Object[]{ctx, id, busCode});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _savePayment2PurLog(ctx, id, busCode);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _savePayment2PurLog(Context ctx, String id, String busCode) throws BOSException
    {    	
        return;
    }

    public void syncPurLog2B2B(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("e3ccceae-9bee-4a2f-bf28-c4606ff33be5"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _syncPurLog2B2B(ctx);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _syncPurLog2B2B(Context ctx) throws BOSException
    {    	
        return;
    }

}