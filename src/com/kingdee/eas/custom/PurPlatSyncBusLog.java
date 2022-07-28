package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.dao.IObjectPK;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.Context;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.eas.custom.app.*;
import com.kingdee.eas.framework.DataBase;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.framework.IDataBase;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;

public class PurPlatSyncBusLog extends DataBase implements IPurPlatSyncBusLog
{
    public PurPlatSyncBusLog()
    {
        super();
        registerInterface(IPurPlatSyncBusLog.class, this);
    }
    public PurPlatSyncBusLog(Context ctx)
    {
        super(ctx);
        registerInterface(IPurPlatSyncBusLog.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("1D353876");
    }
    private PurPlatSyncBusLogController getController() throws BOSException
    {
        return (PurPlatSyncBusLogController)getBizController();
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@return
     */
    public PurPlatSyncBusLogInfo getPurPlatSyncBusLogInfo(IObjectPK pk) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncBusLogInfo(getContext(), pk);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@param selector 取值
     *@return
     */
    public PurPlatSyncBusLogInfo getPurPlatSyncBusLogInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncBusLogInfo(getContext(), pk, selector);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取值-System defined method
     *@param oql 取值
     *@return
     */
    public PurPlatSyncBusLogInfo getPurPlatSyncBusLogInfo(String oql) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncBusLogInfo(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@return
     */
    public PurPlatSyncBusLogCollection getPurPlatSyncBusLogCollection() throws BOSException
    {
        try {
            return getController().getPurPlatSyncBusLogCollection(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@param view 取集合
     *@return
     */
    public PurPlatSyncBusLogCollection getPurPlatSyncBusLogCollection(EntityViewInfo view) throws BOSException
    {
        try {
            return getController().getPurPlatSyncBusLogCollection(getContext(), view);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@param oql 取集合
     *@return
     */
    public PurPlatSyncBusLogCollection getPurPlatSyncBusLogCollection(String oql) throws BOSException
    {
        try {
            return getController().getPurPlatSyncBusLogCollection(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}