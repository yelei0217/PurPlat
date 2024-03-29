package com.kingdee.eas.custom;

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

public interface ISyncDataEASFacade extends IBizCtrl
{
    public void syncDateByType(int type, String data, int newOrDele, String name, String number) throws BOSException;
    public void DoErrorJon(String data) throws BOSException;
    public String materialSyncFun(String data) throws BOSException;
    public void DoMaterialJson(String data) throws BOSException;
    public void doCustomerMid() throws BOSException;
    public void doOrgMid() throws BOSException;
    public void doSuppMid() throws BOSException;
    public void doPersonMid() throws BOSException;
    public void doCangkuMid() throws BOSException;
    public String getWareclinicRales(String jsonStr) throws BOSException;
    public String getCenterPurCustomer(String jsonStr) throws BOSException;
}