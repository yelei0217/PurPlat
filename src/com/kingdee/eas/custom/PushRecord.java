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

public class PushRecord extends DataBase implements IPushRecord
{
    public PushRecord()
    {
        super();
        registerInterface(IPushRecord.class, this);
    }
    public PushRecord(Context ctx)
    {
        super(ctx);
        registerInterface(IPushRecord.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("F6221410");
    }
    private PushRecordController getController() throws BOSException
    {
        return (PushRecordController)getBizController();
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@return
     */
    public PushRecordInfo getPushRecordInfo(IObjectPK pk) throws BOSException, EASBizException
    {
        try {
            return getController().getPushRecordInfo(getContext(), pk);
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
    public PushRecordInfo getPushRecordInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException
    {
        try {
            return getController().getPushRecordInfo(getContext(), pk, selector);
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
    public PushRecordInfo getPushRecordInfo(String oql) throws BOSException, EASBizException
    {
        try {
            return getController().getPushRecordInfo(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@return
     */
    public PushRecordCollection getPushRecordCollection() throws BOSException
    {
        try {
            return getController().getPushRecordCollection(getContext());
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
    public PushRecordCollection getPushRecordCollection(EntityViewInfo view) throws BOSException
    {
        try {
            return getController().getPushRecordCollection(getContext(), view);
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
    public PushRecordCollection getPushRecordCollection(String oql) throws BOSException
    {
        try {
            return getController().getPushRecordCollection(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *生成采购订单-User defined method
     *@param model model
     */
    public void generPurOrder(PushRecordInfo model) throws BOSException
    {
        try {
            getController().generPurOrder(getContext(), model);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *生成采购入库单-User defined method
     *@param model model
     */
    public void generPurInBIll(PushRecordInfo model) throws BOSException
    {
        try {
            getController().generPurInBIll(getContext(), model);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}