package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public interface IPushRecordFacade extends IBizCtrl
{
    public void generPurOrder() throws BOSException;
    public void generPurInBIll() throws BOSException;
    public void matchAoutIssueTask() throws BOSException;
    public void generSaleIssueBill() throws BOSException;
    public void syncSaleOrder() throws BOSException;
    public void cancelPrice() throws BOSException;
    public void noCommonPush() throws BOSException;
    public void noCommonInitData() throws BOSException;
}