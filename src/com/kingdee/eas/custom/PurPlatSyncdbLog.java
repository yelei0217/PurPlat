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

public class PurPlatSyncdbLog extends DataBase implements IPurPlatSyncdbLog
{
    public PurPlatSyncdbLog()
    {
        super();
        registerInterface(IPurPlatSyncdbLog.class, this);
    }
    public PurPlatSyncdbLog(Context ctx)
    {
        super(ctx);
        registerInterface(IPurPlatSyncdbLog.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("6E21F7F4");
    }
    private PurPlatSyncdbLogController getController() throws BOSException
    {
        return (PurPlatSyncdbLogController)getBizController();
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@return
     */
    public PurPlatSyncdbLogInfo getPurPlatSyncdbLogInfo(IObjectPK pk) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncdbLogInfo(getContext(), pk);
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
    public PurPlatSyncdbLogInfo getPurPlatSyncdbLogInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncdbLogInfo(getContext(), pk, selector);
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
    public PurPlatSyncdbLogInfo getPurPlatSyncdbLogInfo(String oql) throws BOSException, EASBizException
    {
        try {
            return getController().getPurPlatSyncdbLogInfo(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@return
     */
    public PurPlatSyncdbLogCollection getPurPlatSyncdbLogCollection() throws BOSException
    {
        try {
            return getController().getPurPlatSyncdbLogCollection(getContext());
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
    public PurPlatSyncdbLogCollection getPurPlatSyncdbLogCollection(EntityViewInfo view) throws BOSException
    {
        try {
            return getController().getPurPlatSyncdbLogCollection(getContext(), view);
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
    public PurPlatSyncdbLogCollection getPurPlatSyncdbLogCollection(String oql) throws BOSException
    {
        try {
            return getController().getPurPlatSyncdbLogCollection(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}