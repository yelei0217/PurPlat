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

public class SyncBill2EASFacade extends AbstractBizCtrl implements ISyncBill2EASFacade
{
    public SyncBill2EASFacade()
    {
        super();
        registerInterface(ISyncBill2EASFacade.class, this);
    }
    public SyncBill2EASFacade(Context ctx)
    {
        super(ctx);
        registerInterface(ISyncBill2EASFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("D8347086");
    }
    private SyncBill2EASFacadeController getController() throws BOSException
    {
        return (SyncBill2EASFacadeController)getBizController();
    }
    /**
     *�ɹ���������ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void savePurOrder(String jsonStr) throws BOSException
    {
        try {
            getController().savePurOrder(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���۶���ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveSaleOrder(String jsonStr) throws BOSException
    {
        try {
            getController().saveSaleOrder(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�ɹ���ⵥͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void savePurInWare(String jsonStr) throws BOSException
    {
        try {
            getController().savePurInWare(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���۳��ⵥͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveSaleIss(String jsonStr) throws BOSException
    {
        try {
            getController().saveSaleIss(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�ɹ������йر�-User defined method
     *@param jsonStr ����json����
     */
    public void purOrderCloseRow(String jsonStr) throws BOSException
    {
        try {
            getController().purOrderCloseRow(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���۶����йر�-User defined method
     *@param jsonStr ����json����
     */
    public void saleOrderCloseRow(String jsonStr) throws BOSException
    {
        try {
            getController().saleOrderCloseRow(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *������ⵥͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveOtherPurIn(String jsonStr) throws BOSException
    {
        try {
            getController().saveOtherPurIn(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�������ⵥͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveOtherSaleIss(String jsonStr) throws BOSException
    {
        try {
            getController().saveOtherSaleIss(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *Ӧ����ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveApOtherBill(String jsonStr) throws BOSException
    {
        try {
            getController().saveApOtherBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void savePaymentBill(String jsonStr) throws BOSException
    {
        try {
            getController().savePaymentBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *Ӧ�յ�ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveArOtherBill(String jsonStr) throws BOSException
    {
        try {
            getController().saveArOtherBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�տͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveReceiveBill(String jsonStr) throws BOSException
    {
        try {
            getController().saveReceiveBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�ɱ�������ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void saveCostAdjus(String jsonStr) throws BOSException
    {
        try {
            getController().saveCostAdjus(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�ɹ���ⵥ�ɹ�������ͬ���ӿ�-User defined method
     *@param jsonStr ����json����
     */
    public void savePurInCGW(String jsonStr) throws BOSException
    {
        try {
            getController().savePurInCGW(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}