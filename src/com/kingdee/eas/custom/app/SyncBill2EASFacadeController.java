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
    public String savePurOrder(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveSaleOrder(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String savePurInWare(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveSaleIss(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String purOrderCloseRow(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saleOrderCloseRow(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveOtherPurIn(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveOtherSaleIss(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveApOtherBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String savePaymentBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveArOtherBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveReceiveBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String saveCostAdjus(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String savePurInCGW(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String rollBackPurchInBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String rollBackSaleIssBill(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public void syncMaterial2TempTab(Context ctx) throws BOSException, RemoteException;
    public void syncIMCounting(Context ctx, String number) throws BOSException, RemoteException;
}