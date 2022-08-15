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
     *�ɹ����뵥�������ɲɹ�����-User defined method
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
     *�ɹ������������ɲɹ���ⵥ-User defined method
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
     *ƥ����Ҫ�Զ����Ķ�ʱ����-User defined method
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
     *���۶����Զ��������۳��ⵥ-User defined method
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
     *ͬ�����۶���-���ӹ�-User defined method
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
     *ȡ������-User defined method
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
     *����ͨ�ӹ����ع�-User defined method
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
     *����ͨ�ӹ�����ʼ��-User defined method
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