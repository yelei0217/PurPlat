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

public interface SyncDataEASFacadeController extends BizController
{
    public void syncDateByType(Context ctx, int type, String data, int newOrDele, String name, String number) throws BOSException, RemoteException;
    public void DoErrorJon(Context ctx, String data) throws BOSException, RemoteException;
    public String materialSyncFun(Context ctx, String data) throws BOSException, RemoteException;
    public void DoMaterialJson(Context ctx, String data) throws BOSException, RemoteException;
    public void doCustomerMid(Context ctx) throws BOSException, RemoteException;
    public void doOrgMid(Context ctx) throws BOSException, RemoteException;
    public void doSuppMid(Context ctx) throws BOSException, RemoteException;
    public void doPersonMid(Context ctx) throws BOSException, RemoteException;
    public void doCangkuMid(Context ctx) throws BOSException, RemoteException;
    public String getWareclinicRales(Context ctx, String jsonStr) throws BOSException, RemoteException;
    public String getCenterPurCustomer(Context ctx, String jsonStr) throws BOSException, RemoteException;
}