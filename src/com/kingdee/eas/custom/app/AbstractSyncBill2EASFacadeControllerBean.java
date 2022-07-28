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



public abstract class AbstractSyncBill2EASFacadeControllerBean extends AbstractBizControllerBean implements SyncBill2EASFacadeController
{
    protected AbstractSyncBill2EASFacadeControllerBean()
    {
    }

    protected BOSObjectType getBOSType()
    {
        return new BOSObjectType("D8347086");
    }

    public void savePurOrder(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("16179292-3ed3-4710-b047-9c172ab2094f"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _savePurOrder(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _savePurOrder(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveSaleOrder(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("f05a077f-c2e1-4fa0-b004-11682658f270"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveSaleOrder(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveSaleOrder(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void savePurInWare(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("1eb41427-0d37-4cc3-8b6a-45ce2364727f"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _savePurInWare(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _savePurInWare(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveSaleIss(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("98031fa4-b012-480b-9e44-3933f98a9187"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveSaleIss(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveSaleIss(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void purOrderCloseRow(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("c42bb6c6-02ac-4f0d-9747-e8d0d3cc4507"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _purOrderCloseRow(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _purOrderCloseRow(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saleOrderCloseRow(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("b049f778-1e47-4c4a-93b2-17382d1c7265"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saleOrderCloseRow(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saleOrderCloseRow(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveOtherPurIn(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("b453ff00-889c-496b-8a62-674dce4f1a8d"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveOtherPurIn(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveOtherPurIn(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveOtherSaleIss(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("4c51797a-2b52-4778-a567-a803fb322f18"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveOtherSaleIss(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveOtherSaleIss(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveApOtherBill(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("b6436cbe-a27c-40e4-911d-76d67607d071"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveApOtherBill(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveApOtherBill(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void savePaymentBill(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("a8d36f80-69a8-4378-bfad-3e166a566f8d"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _savePaymentBill(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _savePaymentBill(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveArOtherBill(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("39ffdf16-6552-41d1-9e91-cf9709232877"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveArOtherBill(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveArOtherBill(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveReceiveBill(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("8e5cefc3-4618-4abc-965f-822abae06c90"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveReceiveBill(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveReceiveBill(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void saveCostAdjus(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("b6de361a-f023-4ab0-8ee5-9c8b3853a537"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _saveCostAdjus(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _saveCostAdjus(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

    public void savePurInCGW(Context ctx, String jsonStr) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("3a2c69f6-8edf-4076-abb6-13ce6f45c3d8"), new Object[]{ctx, jsonStr});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _savePurInCGW(ctx, jsonStr);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _savePurInCGW(Context ctx, String jsonStr) throws BOSException
    {    	
        return;
    }

}