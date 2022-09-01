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
import com.kingdee.eas.framework.ICoreBillEntryBase;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.eas.custom.app.*;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.framework.CoreBillEntryBase;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;

public class WareClinicRaleEntry extends CoreBillEntryBase implements IWareClinicRaleEntry
{
    public WareClinicRaleEntry()
    {
        super();
        registerInterface(IWareClinicRaleEntry.class, this);
    }
    public WareClinicRaleEntry(Context ctx)
    {
        super(ctx);
        registerInterface(IWareClinicRaleEntry.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("446936C0");
    }
    private WareClinicRaleEntryController getController() throws BOSException
    {
        return (WareClinicRaleEntryController)getBizController();
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@return
     */
    public WareClinicRaleEntryInfo getWareClinicRaleEntryInfo(IObjectPK pk) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleEntryInfo(getContext(), pk);
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
    public WareClinicRaleEntryInfo getWareClinicRaleEntryInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleEntryInfo(getContext(), pk, selector);
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
    public WareClinicRaleEntryInfo getWareClinicRaleEntryInfo(String oql) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleEntryInfo(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取集合-System defined method
     *@return
     */
    public WareClinicRaleEntryCollection getWareClinicRaleEntryCollection() throws BOSException
    {
        try {
            return getController().getWareClinicRaleEntryCollection(getContext());
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
    public WareClinicRaleEntryCollection getWareClinicRaleEntryCollection(EntityViewInfo view) throws BOSException
    {
        try {
            return getController().getWareClinicRaleEntryCollection(getContext(), view);
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
    public WareClinicRaleEntryCollection getWareClinicRaleEntryCollection(String oql) throws BOSException
    {
        try {
            return getController().getWareClinicRaleEntryCollection(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}