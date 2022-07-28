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
     *采购订单保存接口-User defined method
     *@param jsonStr 传入json参数
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
     *销售订单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *采购入库单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *销售出库单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *采购订单行关闭-User defined method
     *@param jsonStr 传入json参数
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
     *销售订单行关闭-User defined method
     *@param jsonStr 传入json参数
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
     *其他入库单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *其他出库单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *应付单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *付款单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *应收单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *收款单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *成本调整单同步接口-User defined method
     *@param jsonStr 传入json参数
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
     *采购入库单成果物新增同步接口-User defined method
     *@param jsonStr 传入json参数
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