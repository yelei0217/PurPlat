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
    public String savePurOrder(String jsonStr) throws BOSException;
    public String saveSaleOrder(String jsonStr) throws BOSException;
    public String savePurInWare(String jsonStr) throws BOSException;
    public String saveSaleIss(String jsonStr) throws BOSException;
    public String purOrderCloseRow(String jsonStr) throws BOSException;
    public String saleOrderCloseRow(String jsonStr) throws BOSException;
    public String saveOtherPurIn(String jsonStr) throws BOSException;
    public String saveOtherSaleIss(String jsonStr) throws BOSException;
    public String saveApOtherBill(String jsonStr) throws BOSException;
    public String savePaymentBill(String jsonStr) throws BOSException;
    public String saveArOtherBill(String jsonStr) throws BOSException;
    public String saveReceiveBill(String jsonStr) throws BOSException;
    public String saveCostAdjus(String jsonStr) throws BOSException;
    public String savePurInCGW(String jsonStr) throws BOSException;
}