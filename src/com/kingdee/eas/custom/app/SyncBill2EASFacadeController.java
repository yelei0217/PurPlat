package com.kingdee.eas.custom.app;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

import java.rmi.RemoteException;
import com.kingdee.bos.framework.ejb.BizController;

public interface SyncBill2EASFacadeController extends BizController
{
    public void savePurOrder(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveSaleOrder(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void savePurInWare(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveSaleIss(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void purOrderCloseRow(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saleOrderCloseRow(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveOtherPurIn(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveOtherSaleIss(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveApOtherBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void savePaymentBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveArOtherBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveReceiveBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void saveCostAdjus(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void savePurInCGW(Context ctx, String jsonStr) throws BOSException, RemoteException;
}