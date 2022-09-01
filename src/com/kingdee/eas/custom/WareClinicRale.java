package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.eas.framework.CoreBillBase;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.framework.ICoreBillBase;
import com.kingdee.bos.Context;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.eas.custom.app.*;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;

public class WareClinicRale extends CoreBillBase implements IWareClinicRale
{
    public WareClinicRale()
    {
        super();
        registerInterface(IWareClinicRale.class, this);
    }
    public WareClinicRale(Context ctx)
    {
        super(ctx);
        registerInterface(IWareClinicRale.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("DCFD14D2");
    }
    private WareClinicRaleController getController() throws BOSException
    {
        return (WareClinicRaleController)getBizController();
    }
    /**
     *取集合-System defined method
     *@return
     */
    public WareClinicRaleCollection getWareClinicRaleCollection() throws BOSException
    {
        try {
            return getController().getWareClinicRaleCollection(getContext());
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
    public WareClinicRaleCollection getWareClinicRaleCollection(EntityViewInfo view) throws BOSException
    {
        try {
            return getController().getWareClinicRaleCollection(getContext(), view);
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
    public WareClinicRaleCollection getWareClinicRaleCollection(String oql) throws BOSException
    {
        try {
            return getController().getWareClinicRaleCollection(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *取值-System defined method
     *@param pk 取值
     *@return
     */
    public WareClinicRaleInfo getWareClinicRaleInfo(IObjectPK pk) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleInfo(getContext(), pk);
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
    public WareClinicRaleInfo getWareClinicRaleInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleInfo(getContext(), pk, selector);
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
    public WareClinicRaleInfo getWareClinicRaleInfo(String oql) throws BOSException, EASBizException
    {
        try {
            return getController().getWareClinicRaleInfo(getContext(), oql);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}