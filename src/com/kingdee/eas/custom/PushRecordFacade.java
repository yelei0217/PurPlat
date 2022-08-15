package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.eas.custom.app.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class PushRecordFacade extends AbstractBizCtrl implements IPushRecordFacade
{
    public PushRecordFacade()
    {
        super();
        registerInterface(IPushRecordFacade.class, this);
    }
    public PushRecordFacade(Context ctx)
    {
        super(ctx);
        registerInterface(IPushRecordFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("1C9F64CA");
    }
    private PushRecordFacadeController getController() throws BOSException
    {
        return (PushRecordFacadeController)getBizController();
    }
    /**
     *采购申请单下推生成采购订单-User defined method
     */
    public void generPurOrder() throws BOSException
    {
        try {
            getController().generPurOrder(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *采购订单下推生成采购入库单-User defined method
     */
    public void generPurInBIll() throws BOSException
    {
        try {
            getController().generPurInBIll(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *匹配需要自动出的定时任务-User defined method
     */
    public void matchAoutIssueTask() throws BOSException
    {
        try {
            getController().matchAoutIssueTask(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *销售订单自动下推销售出库单-User defined method
     */
    public void generSaleIssueBill() throws BOSException
    {
        try {
            getController().generSaleIssueBill(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *同步销售订单-技加工-User defined method
     */
    public void syncSaleOrder() throws BOSException
    {
        try {
            getController().syncSaleOrder(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取消划扣-User defined method
     */
    public void cancelPrice() throws BOSException
    {
        try {
            getController().cancelPrice(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *非普通加工单回滚-User defined method
     */
    public void noCommonPush() throws BOSException
    {
        try {
            getController().noCommonPush(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *非普通加工单初始化-User defined method
     */
    public void noCommonInitData() throws BOSException
    {
        try {
            getController().noCommonInitData(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}