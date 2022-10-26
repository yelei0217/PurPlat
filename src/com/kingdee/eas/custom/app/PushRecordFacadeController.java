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

public interface PushRecordFacadeController extends BizController
{
    public void generPurOrder(Context ctx) throws BOSException, RemoteException;
    public void generPurInBIll(Context ctx) throws BOSException, RemoteException;
    public void matchAoutIssueTask(Context ctx) throws BOSException, RemoteException;
    public void generSaleIssueBill(Context ctx) throws BOSException, RemoteException;
    public void syncSaleOrder(Context ctx) throws BOSException, RemoteException;
    public void cancelPrice(Context ctx) throws BOSException, RemoteException;
    public void noCommonPush(Context ctx) throws BOSException, RemoteException;
    public void noCommonInitData(Context ctx) throws BOSException, RemoteException;
    public void savePayment2PurLog(Context ctx, String id, String busCode) throws BOSException, RemoteException;
    public void syncPurLog2B2B(Context ctx) throws BOSException, RemoteException;
}