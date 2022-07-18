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
     *根据类型同步数据-User defined method
     *@param type 类型
     *@param data 数据
     *@param newOrDele 1新增  0禁用
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
}