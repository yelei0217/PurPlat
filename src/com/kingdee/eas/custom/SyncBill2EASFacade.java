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
     *@return
     */
    public String savePurOrder(String jsonStr) throws BOSException
    {
        try {
            return getController().savePurOrder(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *销售订单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveSaleOrder(String jsonStr) throws BOSException
    {
        try {
            return getController().saveSaleOrder(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *采购入库单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String savePurInWare(String jsonStr) throws BOSException
    {
        try {
            return getController().savePurInWare(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *销售出库单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveSaleIss(String jsonStr) throws BOSException
    {
        try {
            return getController().saveSaleIss(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *采购订单行关闭-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String purOrderCloseRow(String jsonStr) throws BOSException
    {
        try {
            return getController().purOrderCloseRow(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *销售订单行关闭-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saleOrderCloseRow(String jsonStr) throws BOSException
    {
        try {
            return getController().saleOrderCloseRow(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *其他入库单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveOtherPurIn(String jsonStr) throws BOSException
    {
        try {
            return getController().saveOtherPurIn(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *其他出库单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveOtherSaleIss(String jsonStr) throws BOSException
    {
        try {
            return getController().saveOtherSaleIss(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *应付单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveApOtherBill(String jsonStr) throws BOSException
    {
        try {
            return getController().saveApOtherBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *付款单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String savePaymentBill(String jsonStr) throws BOSException
    {
        try {
            return getController().savePaymentBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *应收单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveArOtherBill(String jsonStr) throws BOSException
    {
        try {
            return getController().saveArOtherBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *收款单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveReceiveBill(String jsonStr) throws BOSException
    {
        try {
            return getController().saveReceiveBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *成本调整单同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String saveCostAdjus(String jsonStr) throws BOSException
    {
        try {
            return getController().saveCostAdjus(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *采购入库单成果物新增同步接口-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String savePurInCGW(String jsonStr) throws BOSException
    {
        try {
            return getController().savePurInCGW(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *采购入库单--红单-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String rollBackPurchInBill(String jsonStr) throws BOSException
    {
        try {
            return getController().rollBackPurchInBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *销售出库单--红单-User defined method
     *@param jsonStr 传入json参数
     *@return
     */
    public String rollBackSaleIssBill(String jsonStr) throws BOSException
    {
        try {
            return getController().rollBackSaleIssBill(getContext(), jsonStr);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}