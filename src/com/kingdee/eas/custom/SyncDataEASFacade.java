package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.eas.custom.app.*;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class SyncDataEASFacade extends AbstractBizCtrl implements ISyncDataEASFacade
{
    public SyncDataEASFacade()
    {
        super();
        registerInterface(ISyncDataEASFacade.class, this);
    }
    public SyncDataEASFacade(Context ctx)
    {
        super(ctx);
        registerInterface(ISyncDataEASFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("5FE133C7");
    }
    private SyncDataEASFacadeController getController() throws BOSException
    {
        return (SyncDataEASFacadeController)getBizController();
    }
    /**
     *��������ͬ������-User defined method
     *@param type ����
     *@param data ����
     *@param newOrDele 1����  0����
     *@param name name
     *@param number number
     */
    public void syncDateByType(int type, String data, int newOrDele, String name, String number) throws BOSException
    {
        try {
            getController().syncDateByType(getContext(), type, data, newOrDele, name, number);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *DoErrorJon-User defined method
     *@param data data
     */
    public void DoErrorJon(String data) throws BOSException
    {
        try {
            getController().DoErrorJon(getContext(), data);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *����ͬ������-User defined method
     *@param data data
     *@return
     */
    public String materialSyncFun(String data) throws BOSException
    {
        try {
            return getController().materialSyncFun(getContext(), data);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}