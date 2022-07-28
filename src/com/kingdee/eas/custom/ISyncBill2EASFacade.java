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

public interface ISyncBill2EASFacade extends IBizCtrl
{
    public void savePurOrder(String jsonStr) throws BOSException;
    public void saveSaleOrder(String jsonStr) throws BOSException;
    public void savePurInWare(String jsonStr) throws BOSException;
    public void saveSaleIss(String jsonStr) throws BOSException;
    public void purOrderCloseRow(String jsonStr) throws BOSException;
    public void saleOrderCloseRow(String jsonStr) throws BOSException;
    public void saveOtherPurIn(String jsonStr) throws BOSException;
    public void saveOtherSaleIss(String jsonStr) throws BOSException;
    public void saveApOtherBill(String jsonStr) throws BOSException;
    public void savePaymentBill(String jsonStr) throws BOSException;
    public void saveArOtherBill(String jsonStr) throws BOSException;
    public void saveReceiveBill(String jsonStr) throws BOSException;
    public void saveCostAdjus(String jsonStr) throws BOSException;
    public void savePurInCGW(String jsonStr) throws BOSException;
}