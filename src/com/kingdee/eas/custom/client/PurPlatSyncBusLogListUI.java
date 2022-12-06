/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ui.face.IUIWindow;
import com.kingdee.bos.ui.face.UIException;
import com.kingdee.bos.ui.face.UIFactory;
import com.kingdee.bos.ctrl.swing.KDWorkButton;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.client.OprtState;
import com.kingdee.eas.common.client.UIContext;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.PushRecordFacadeFactory;
import com.kingdee.eas.custom.SyncBill2EASFacadeFactory;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;
import com.kingdee.eas.framework.*;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.ws.PurInWarehsBillFacade;
import com.kingdee.eas.util.client.MsgBox;

/**
 * output class name
 */
public class PurPlatSyncBusLogListUI extends AbstractPurPlatSyncBusLogListUI
{
    private static final Logger logger = CoreUIObject.getLogger(PurPlatSyncBusLogListUI.class);
    
    /**
     * output class constructor
     */
    public PurPlatSyncBusLogListUI() throws Exception
    {
        super();
    }

    @Override
    public void onLoad() throws Exception {
     	super.onLoad();
    }
    
    
    
    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }
    
    @Override
    public void actionHelp_actionPerformed(ActionEvent e) throws Exception {
    	
//    	String jsonStr ="{\"msgId\":\"202209221439002\",\"busCode\":\"DZ_MZ_PO\",\"reqTime\":\"202209221439002\",\"data\":{\"id\":\"202209221439002\",\"fnumber\":\"202209221439001\",\"fbizdate\":\"2022-09-22\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"\",\"ftotalqty\":2,\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202209221439002-1\",\"fseq\":1,\"fmaterialid\":\"301329\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"funitid\":\"G03\",\"fassociateqty\":2,\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-22\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
//        System.out.println("savePurOrder"+result);
    	
//    	String jsonStr ="{\"msgId\":\"202209222302001\",\"busCode\":\"CGZ_U_MZ_SO\",\"reqTime\":\"202209222302001\",\"data\":{\"id\":\"202209222302001\",\"fnumber\":\"202209222302001\",\"fbizdate\":\"2022-09-23\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"svL0ZnRPS86qelCx023QZ78MBA4=\",\"fadminorgunitid\":\"\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"202209222302001-1\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fbaseunitid\":\"G03\",\"fassociateqty\":2,\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fqty\":2,\"fremark\":\"ccss\",\"fispresent\":0,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-23\",\"fbaseqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr);
//    	System.out.println("saveSaleOrder"+result);  
    	
//    	String jsonStr = "{\"msgId\":\"20220926112805\",\"busCode\":\"GZ_MZ_PI\",\"reqTime\":\"20220926112805\",\"data\":{\"details\":[{\"factualprice\":6666.00,\"factualtaxprice\":7399.2600,\"famount\":6666.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-30\",\"fexp\":\"2022-09-11\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-22\",\"fprice\":6666.00,\"fqty\":1,\"fseq\":1,\"fsourcebillentryid\":\"1\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"504\",\"fsourcebillnumber\":\"504\",\"ftax\":733.2600,\"ftaxamount\":7399.2600,\"ftaxprice\":7399.2600,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehourseid\":\"jbYAAACU9X276fiu\",\"id\":\"1658\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-09-26\",\"fcreatorid\":\"\",\"fnumber\":\"2022092611280523069\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"ftotalamount\":6666.00,\"ftotalqty\":1,\"ftotaltax\":733.2600,\"ftotaltaxamount\":7399.2600,\"id\":\"102\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//      System.out.println("savePurInWare"+result);
    	
//    	String jsonStr = "{\"msgId\":\"20220928121418\",\"busCode\":\"GZ_LZ_SS\",\"reqTime\":\"20220928121418\",\"data\":{\"details\":[{\"factualprice\":8888.0,\"factualtaxprice\":9865.68,\"famount\":17776.0,\"fassociateqty\":2,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":8888.0,\"fqty\":2,\"fseq\":1,\"fsourcebillentryid\":\"1755\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"527\",\"fsourcebillnumber\":\"527\",\"ftax\":1955.36,\"ftaxamount\":19731.36,\"ftaxprice\":9865.68,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"17551\"},{\"factualprice\":8888.0,\"factualtaxprice\":9865.68,\"famount\":17776.0,\"fassociateqty\":2,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":8888.0,\"fqty\":2,\"fseq\":1,\"fsourcebillentryid\":\"1756\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"527\",\"fsourcebillnumber\":\"527\",\"ftax\":1955.36,\"ftaxamount\":19731.36,\"ftaxprice\":9865.68,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"17561\"},{\"factualprice\":6666.0,\"factualtaxprice\":7399.26,\"famount\":13332.0,\"fassociateqty\":2,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":6666.0,\"fqty\":2,\"fseq\":1,\"fsourcebillentryid\":\"1757\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"527\",\"fsourcebillnumber\":\"527\",\"ftax\":1466.52,\"ftaxamount\":14798.52,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"17571\"},{\"factualprice\":6666.0,\"factualtaxprice\":7399.26,\"famount\":13332.0,\"fassociateqty\":2,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":6666.0,\"fqty\":2,\"fseq\":1,\"fsourcebillentryid\":\"1758\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"527\",\"fsourcebillnumber\":\"527\",\"ftax\":1466.52,\"ftaxamount\":14798.52,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"17581\"}],\"fbizdate\":\"2022-09-28\",\"fcreatorid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"cw+GQHEuSay63HHYBbYkFL8MBA4=\",\"fnumber\":\"2022092812034609370\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"ftotalamount\":31108.0,\"ftotalqty\":4,\"ftotaltax\":3421.88,\"ftotaltaxamount\":34529.88,\"id\":\"181\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
//    	System.out.println("saveSaleIss"+result);

//    	String jsonStr = "{\"msgId\":\"202209221741012\",\"busCode\":\"VMIB_MZ_PI\",\"reqTime\":\"202209221741012\",\"data\":{\"id\":\"202209221741012\",\"fnumber\":\"202209221741012\",\"fbizdate\":\"2022-09-22\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"202209221741012-1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":2,\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-22\",\"fmfg\":\"2022-09-22\",\"fexp\":\"2022-09-22\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//        System.out.println("savePurInWare"+result);
    	
    
//    	String jsonStr ="{\"msgId\":\"202209221601001\",\"busCode\":\"DZB_LZ_PO_CR\",\"reqTime\":\"202209221601001\",\"data\":{\"id\":\"202209221439002\",\"eids\":\"202209221439002-1\"}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().purOrderCloseRow(jsonStr);
//    	System.out.println("purOrderCloseRow"+result);
    	
//    	String jsonStr ="{\"msgId\":\"202209101044002\",\"busCode\":\"GZB_LZ_SO_CR\",\"reqTime\":\"202209101044002\",\"data\":{\"id\":\"202208312032001\",\"eids\":\"202208312032001-1\"}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saleOrderCloseRow(jsonStr);
//    	System.out.println("saleOrderCloseRow"+result);
    	
//    	String jsonStr = "{\"msgId\":\"202208051644123\",\"busCode\":\"GZ_MZ_PO\",\"reqTime\":\"202208051644123\",\"data\":{\"id\":\"202208051644123\",\"fnumber\":\"202208051644123\",\"fbizdate\":\"2022-08-05\",\"fstorageorgunitid\":\"kO6+E1zaSbKzTzLMtI+csMznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208051644123-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G01\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G01\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-05\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
    	
//    	String jsonStr = "{\"msgId\":\"202209022257001\",\"busCode\":\"GZ_MZ_PI\",\"reqTime\":\"202209022257001\",\"data\":{\"id\":\"202209022257001\",\"fnumber\":\"202209022257001\",\"fbizdate\":\"2022-09-02\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202209022257001-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208312257001\",\"fsourcebillentryid\":\"202208221830112-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-02\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
//     	String jsonStr = "{\"msgId\":\"202208312019005\",\"busCode\":\"GZ_LZ_PI\",\"reqTime\":\"202208312019005\",\"data\":{\"id\":\"202208312019005\",\"fnumber\":\"202208312019005\",\"fbizdate\":\"2022-08-31\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208312019005-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208312019003\",\"fsourcebillentryid\":\"202208312019003-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-31\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//    	System.out.println("savePurInWare"+result);
    	 
    	
//    	String jsonStr ="{\"msgId\":\"202208312257001\",\"busCode\":\"DZ_MZ_PO\",\"reqTime\":\"202208312257001\",\"data\":{\"id\":\"202208312257001\",\"fnumber\":\"202208312257001\",\"fbizdate\":\"2022-08-31\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208312257001-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-31\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
//    	String jsonStr = "{\"msgId\":\"202208312257003\",\"busCode\":\"DZ_MZ_PI\",\"reqTime\":\"202208312257003\",\"data\":{\"id\":\"202208312257003\",\"fnumber\":\"202208312257003\",\"fbizdate\":\"2022-08-31\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208312257001-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208312257001\",\"fsourcebillentryid\":\"202208221830112-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-31\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208102203128\",\"busCode\":\"VMI_MZ_PI\",\"reqTime\":\"202208102203128\",\"data\":{\"id\":\"202208102203128\",\"fnumber\":\"202208102203128\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052203123-111\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208102203129\",\"busCode\":\"VMIB_MZ_PI\",\"reqTime\":\"202208102203129\",\"data\":{\"id\":\"202208102203129\",\"fnumber\":\"202208102203129\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208102203129-111\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
    	
//    	String jsonStr = "{\"msgId\":\"202209292019001\",\"busCode\":\"CDZ_U_MZ_SS\",\"reqTime\":\"202209292019001\",\"data\":{\"id\":\"202209292019001\",\"fnumber\":\"202209292019001\",\"fbizdate\":\"2022-09-29\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"202209292019001-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208312019003\",\"fsourcebillentryid\":\"202208312019003-1\",\"fsourcebillentryseq\":\"1\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fbaseunitid\":\"G03\",\"fassociateqty\":2,\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fqty\":2,\"fremark\":\"ccss\",\"fispresent\":0,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-29\",\"fmfg\":\"2022-09-29\",\"fexp\":\"2022-09-29\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);	
//    	System.out.println("savePurInWare"+result);
    	
//    	String jsonStr = "{\"msgId\":\"202208111419123\",\"busCode\":\"VMI_LZ_SS\",\"reqTime\":\"202208111419123\",\"data\":{\"id\":\"202208111419123\",\"fnumber\":\"202208111419123\",\"fbizdate\":\"2022-08-11\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208111419123-1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-12\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	
//     	String jsonStr = "{\"msgId\":\"202208112015125\",\"busCode\":\"VMIB_LZ_SS\",\"reqTime\":\"202208112015125\",\"data\":{\"id\":\"202208112015125\",\"fnumber\":\"202208112015125\",\"fbizdate\":\"2022-08-11\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208112015125-1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-12\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	
//    	String jsonStr ="{\"msgId\":\"202209222302001\",\"busCode\":\"CGZ_U_MZ_SO\",\"reqTime\":\"202209222302001\",\"data\":{\"id\":\"202209222302001\",\"fnumber\":\"202209222302001\",\"fbizdate\":\"2022-09-29\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"svL0ZnRPS86qelCx023QZ78MBA4=\",\"fadminorgunitid\":\"\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"202209222302001-1\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fbaseunitid\":\"G03\",\"fassociateqty\":2,\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fqty\":2,\"fremark\":\"ccss\",\"fispresent\":0,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-29\",\"fbaseqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr); 	
    	
//    	PushRecordFacadeFactory.getRemoteInstance().generSaleIssueBill();

  
//    	String jsonStr ="{\"msgId\":\"202209191507002\",\"busCode\":\"VMI_U_MZ_SO\",\"reqTime\":\"202209191507002\",\"data\":{\"id\":\"202209191507002\",\"fnumber\":\"202209191507002\",\"fbizdate\":\"2022-09-19\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fordercustomerid\":\"XYUDVb+9ThW45EPmR5nBtL8MBA4=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"details\":[{\"id\":\"202209191507002-1\",\"fseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fsenddate\":\"2022-09-18\",\"fdeliverydate\":\"2022-09-18\",\"fbaseqty\":2,\"flzpprice\":86.206897,\"flzptaxprice\":100,\"flzptaxrate\":16,\"flzpamount\":172.41,\"flzptax\":27.59,\"flzptaxamount\":200,\"flzsprice\":100,\"flzstaxprice\":100,\"flzstaxrate\":0,\"flzsamount\":200,\"flzstax\":0,\"flzstaxamount\":200}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr);
//    	System.out.println("saveSaleOrder"+result);
    	  
//    	String jsonStr ="{\"msgId\":\"202210071845123\",\"busCode\":\"GZ_CK_LZ_CJ\",\"reqTime\":\"202210071845123\",\"data\":{\"id\":\"202210071845123\",\"fnumber\":\"202210071845123\",\"fbizdate\":\"2022-10-07\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"ftotalamount\":200,\"ftotalqty\":2,\"fcreatorid\":\"\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"details\":[{\"id\":\"202208301845123-1\",\"fseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fprice\":100,\"famount\":200,\"fqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveCostAdjus(jsonStr);
//        System.out.println("saveCostAdjus"+result);
   
//        String jsonStr ="{\"msgId\":\"202210072218001\",\"busCode\":\"GZ_CK_LZ_AP\",\"reqTime\":\"202210072218001\",\"data\":{\"id\":\"202210072218001\",\"fnumber\":\"202210072218001\",\"fbizdate\":\"2022-10-07\",\"fcompanyorgunitid\":\"jbYAAAMU2SvM567U\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"finvoicenumber\":\"0001\",\"fcreatorid\":\"\",\"details\":[{\"id\":\"202210072218001-1\",\"fseq\":\"1\",\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fbaseqty\":2}]}}"; 
//        String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveApOtherBill(jsonStr);
//        System.out.println("saveApOtherBill"+result);
 
//	      String jsonStr ="{\"msgId\":\"202210072218001\",\"busCode\":\"GZ_CK_LZ_AR\",\"reqTime\":\"202210072218001\",\"data\":{\"id\":\"202210072218001\",\"fnumber\":\"202210072218001\",\"fbizdate\":\"2022-10-07\",\"fcompanyorgunitid\":\"jbYAAAMU2SvM567U\",\"fcustomerid\":\"XYUDVb+9ThW45EPmR5nBtL8MBA4=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202210072218001-1\",\"fseq\":\"1\",\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fbaseqty\":2}]}}"; 
//	      String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveArOtherBill(jsonStr);
//	      System.out.println("saveApOtherBill"+result);
    
//	      String jsonStr ="{\"msgId\":\"202210072218002\",\"busCode\":\"GZ_CK_LZ_R\",\"reqTime\":\"202210072218002\",\"data\":{\"id\":\"202210072218002\",\"fnumber\":\"202210072218002\",\"fbizdate\":\"2022-10-07\",\"fcompanyorgunitid\":\"jbYAAAMU2SvM567U\",\"fcustomerid\":\"XYUDVb+9ThW45EPmR5nBtL8MBA4=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202210072218002-1\",\"fsourcebillid\":\"202210072218001\",\"fsourcebillnumber\":\"202210072218001\",\"fsourcebillentryid\":\"202210072218001-1\",\"fsourcebillentryseq\":\"1\",\"fseq\":\"1\",\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fbaseqty\":2}]}}"; 
//	      String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveReceiveBill(jsonStr);
//	      System.out.println("saveReceiveBill"+result);
    	
//    	String jsonStr = "{\"msgId\":\"202209041527001\",\"busCode\":\"GZB_MZ_PI\",\"reqTime\":\"202209041527001\",\"data\":{\"id\":\"202209041527001\",\"fnumber\":\"202209041527001\",\"fbizdate\":\"2022-09-04\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202209022257001-1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-04\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
    	
//    	String jsonStr = "{\"msgId\":\"20220928144239\",\"busCode\":\"GZB_LZ_PI\",\"reqTime\":\"20220928144239\",\"data\":{\"bid\":\"533\",\"details\":[{\"bid\":\"1779\",\"factualprice\":8888.00,\"factualtaxprice\":9865.6800,\"famount\":8888.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":8888.00,\"fqty\":1,\"fseq\":1,\"fsourcebillentryid\":\"1779\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"533\",\"fsourcebillnumber\":\"533\",\"ftax\":977.6800,\"ftaxamount\":9865.6800,\"ftaxprice\":9865.6800,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"1779\",\"iswholerow\":\"0\"}],\"fbizdate\":\"2022-09-28\",\"fcreatorid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fnumber\":\"R20220928144238992232\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"ftotalamount\":8888.00,\"ftotalqty\":1,\"ftotaltax\":977.6800,\"ftotaltaxamount\":9865.6800,\"id\":\"37\",\"iswholebill\":\"0\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().rollBackPurchInBill(jsonStr);
//     	System.out.println("rollBackPurchInBill"+result);
    	
    	
//    	String jsonStr = "{\"msgId\":\"20220928144259\",\"busCode\":\"GZB_LZ_SS\",\"reqTime\":\"20220928144259\",\"data\":{\"bid\":\"533\",\"details\":[{\"bid\":\"1779\",\"factualprice\":8888.00,\"factualtaxprice\":9865.6800,\"famount\":8888.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-09-28\",\"fexp\":\"2022-09-28\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"CSqq003\",\"fmfg\":\"2022-09-28\",\"fprice\":8888.00,\"fqty\":1,\"fseq\":1,\"fsourcebillentryid\":\"1779\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"533\",\"fsourcebillnumber\":\"533\",\"ftax\":977.6800,\"ftaxamount\":9865.6800,\"ftaxprice\":9865.6800,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"id\":\"1779\",\"iswholerow\":\"0\"}],\"fbizdate\":\"2022-09-28\",\"fcreatorid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fnumber\":\"R20220928144238992232\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"ftotalamount\":8888.00,\"ftotalqty\":1,\"ftotaltax\":977.6800,\"ftotaltaxamount\":9865.6800,\"id\":\"37\",\"iswholebill\":\"0\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().rollBackSaleIssBill(jsonStr);
//     	System.out.println("rollBackPurchInBill"+result);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202209121610002\",\"busCode\":\"ZZ_YC_MZ_PI_C\",\"reqTime\":\"202209121610002\",\"data\":{\"id\":\"202209121610002\",\"fnumber\":\"202209121610002\",\"fbizdate\":\"2022-09-12\",\"fstorageorgunitid\":\"kO6+E1zaSbKzTzLMtI+csMznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"fdoctor\":\"YVqJZ6h0SfqyzXpy4GNGHoDvfe0=\",\"details\":[{\"id\":\"202209121610002-1\",\"fwarehouseid\":\"jbYAAAAGX9G76fiu\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":\"0\",\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"flot\":\"54541-YblhcM4pQOutUg5b4WEyC0QJ5-202209072028001\",\"fmfg\":\"2022-09-12\",\"fexp\":\"2022-09-12\",\"fpatientid\":\"54541\",\"fpatientname\":\"张三\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInCGW(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202209121610002\",\"busCode\":\"11\",\"reqTime\":\"202209121610002\"}";
//    	String result = SyncDataEASFacadeFactory.getRemoteInstance().getWareclinicRales(jsonStr);
//     	System.out.println("getWareclinicRales"+result);
     	
    	
//    	String jsonStr = "{\"msgId\":\"2022092911112001\",\"busCode\":\"SK_MZ_OPI\",\"reqTime\":\"2022092911112001\",\"data\":{\"id\":\"2022092911112001\",\"fnumber\":\"2022091411112001\",\"fbizdate\":\"2022-09-29\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"2022092911112001-1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":2,\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-09-29\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveOtherPurIn(jsonStr);
//     	System.out.println("saveOtherPurIn"+result);
     	
//    	String jsonStr = "{\"msgId\":\"2022091411112002\",\"busCode\":\"SK_MZ_OSS\",\"reqTime\":\"2022091411112002\",\"data\":{\"id\":\"2022091411112002\",\"fnumber\":\"2022091411112002\",\"fbizdate\":\"2022-09-14\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"fcustomerid\":\"\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fstockerid\":\"\",\"details\":[{\"id\":\"2022091411112002-1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":1,\"fmaterialid\":\"102036\",\"funitid\":\"G03\",\"fassociateqty\":2,\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":0,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-14\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveOtherSaleIss(jsonStr);
//     	System.out.println("saveOtherSaleIss"+result);
    	
//    	String jsonStr = "{\"msgId\":\"202209121610002\",\"busCode\":\"Customer\",\"reqTime\":\"202209121610002\"}";
//    	String result = SyncDataEASFacadeFactory.getRemoteInstance().getCenterPurCustomer(jsonStr);
//     	System.out.println("getCenterPurCustomer"+result);
     	
//    	String jsonStr = "{\"msgId\":\"20220715121022\",\"operType\":0,\"baseType\":5,\"reqTime\":\"20220715121022\",\"data\":{\"fId\":\"jbYAAAac+li76fiu11\",\"fNumber\":\"MS3101MWZH00101\",\"fName\":\"上海中心仓1\",\"fOrgId\":\"jbYAAAMU2SvM567U\",\"fOrgNumber\":\"MS3101MWZH001\",\"fOrgName\":\"上海栗喆医疗器械有限公司\",\"fStatus\":\"1\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePaymentBill(jsonStr);
//     	System.out.println("savePaymentBill"+result);
//        MsgBox.showInfo("savePaymentBill"+result);
    	
//    	PushRecordFacadeFactory.getRemoteInstance().generSaleIssueBill();
    	
    	
    	// PushRecordFacadeFactory.getRemoteInstance().savePayment2PurLog("W8RsrujrQQaOMdgxk7arV0AoToE=","GZ_CK_LZ_P");
    	 
//    	 PushRecordFacadeFactory.getRemoteInstance().syncPurLog2B2B();
    	//义齿加工-采购入库单 
//    	String jsonStr = "{\"msgId\":\"202211071116002\",\"busCode\":\"YC_PI\",\"reqTime\":\"202211071116002\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"W210061\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"flot\":\"202211071116002\",\"id\":\"1658\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071116002\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"aSV7TOELRfGo3sTm+wGaszfGffw=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071116002\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//        System.out.println("savePurInWare"+result);
    	
        
    	//义齿加工-销售出库单 
//    	String jsonStr = "{\"msgId\":\"202211071116003\",\"busCode\":\"YC_SS\",\"reqTime\":\"202211071116003\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"202211071116002\",\"fmaterialid\":\"W210061\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"id\":\"202211071116003-1\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071116003\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fcustomerid\":\"svL0ZnRPS86qelCx023QZ78MBA4=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071116003\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
//        System.out.println("saveSaleIss"+result);
        
    	
    	//隐形矫正加-门诊采购入库单 
//    	String jsonStr = "{\"msgId\":\"202211071630001\",\"busCode\":\"YX_MZ_PI\",\"reqTime\":\"202211071630001\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"W206978\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"flot\":\"202211071630001\",\"id\":\"1658\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071630001\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"aSV7TOELRfGo3sTm+wGaszfGffw=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071630001\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//        System.out.println("savePurInWare"+result);
    	
        
    	//隐形矫正加-门诊销售出库 
//    	String jsonStr = "{\"msgId\":\"202211071630002\",\"busCode\":\"YX_MZ_SS\",\"reqTime\":\"202211071630002\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"202211071630001\",\"fmaterialid\":\"W206978\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"id\":\"202211071630002-1\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071630002\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fcustomerid\":\"svL0ZnRPS86qelCx023QZ78MBA4=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071630002\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
//        System.out.println("saveSaleIss"+result);
    	
    	
        //种植原厂基台-门诊-采购入库单成果物  ZZ_YC_MZ_PI_C
//    	String jsonStr = "{\"msgId\":\"202211071701001\",\"busCode\":\"ZZ_YC_MZ_PI_C\",\"reqTime\":\"202211071701001\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"101195\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"flot\":\"202211071701001\",\"id\":\"202211071701001-1\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071701001\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"aSV7TOELRfGo3sTm+wGaszfGffw=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071701001\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInCGW(jsonStr);
//     	System.out.println("savePurInWare"+result);
    	
    	// 种植个性化基台栗喆-采购入库单 ZZ_GX_LZ_PI
//    	String jsonStr = "{\"msgId\":\"202211071707001\",\"busCode\":\"ZZ_GX_LZ_PI\",\"reqTime\":\"202211071707001\",\"data\":{\"details\":[{\"factualprice\":6666,\"factualtaxprice\":7399.26,\"famount\":6666,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G01\",\"fdeliverydate\":\"2022-11-07\",\"fexp\":\"2022-11-07\",\"fispresent\":\"0\",\"flot\":\"\",\"fmaterialid\":\"101195\",\"fmfg\":\"2022-11-07\",\"fprice\":6666,\"fqty\":1,\"fseq\":1,\"ftax\":733.26,\"ftaxamount\":7399.26,\"ftaxprice\":7399.26,\"ftaxrate\":11,\"funitid\":\"G01\",\"fwarehouseid\":\"jbYAAAAF8SO76fiu\",\"flot\":\"202211071707001\",\"id\":\"202211071707001-1\"}],\"fadminorgunitid\":\"\",\"fbizdate\":\"2022-11-07\",\"fcreatorid\":\"\",\"fnumber\":\"202211071707001\",\"fpurchasepersonid\":\"\",\"fstockerid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"aSV7TOELRfGo3sTm+wGaszfGffw=\",\"ftotalamount\":6666,\"ftotalqty\":1,\"ftotaltax\":733.26,\"ftotaltaxamount\":7399.26,\"id\":\"202211071707001\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//     	System.out.println("savePurInWare"+result);
      
    	
//    	String jsonStr ="{\"msgId\":\"20221201170653\",\"busCode\":\"GZ_MZ_PO\",\"reqTime\":\"20221201170653\",\"data\":{\"details\":[{\"factualprice\":1945,\"factualtaxprice\":1945,\"famount\":1945,\"fassociateqty\":1,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-01\",\"fispresent\":\"0\",\"fmaterialid\":\"685\",\"fprice\":1945,\"fqty\":1,\"fseq\":1377,\"ftax\":0,\"ftaxamount\":1945,\"ftaxprice\":1945,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6668\",\"noTaxPriceBySkuPlatform\":1721.24,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":223.76,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":1945,\"taxPriceBySkuSup\":1900},{\"factualprice\":1945,\"factualtaxprice\":1945,\"famount\":1945,\"fassociateqty\":1,\"fbaseqty\":2,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-01\",\"fispresent\":\"0\",\"fmaterialid\":\"685\",\"fprice\":1945,\"fqty\":1,\"fseq\":1377,\"ftax\":0,\"ftaxamount\":1945,\"ftaxprice\":1945,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6669\",\"noTaxPriceBySkuPlatform\":1721.24,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":223.76,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":1945,\"taxPriceBySkuSup\":1900},{\"factualprice\":2100,\"factualtaxprice\":2100,\"famount\":2100,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-01\",\"fispresent\":\"0\",\"fmaterialid\":\"683\",\"fprice\":2100,\"fqty\":1,\"fseq\":1378,\"ftax\":0,\"ftaxamount\":2100,\"ftaxprice\":2100,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6670\",\"noTaxPriceBySkuPlatform\":1858.41,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":241.59,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":2100,\"taxPriceBySkuSup\":1900}],\"fbizdate\":\"2022-12-01\",\"fcustomerid\":\"cw+GQHEuSay63HHYBbYkFL8MBA4=\",\"fnumber\":\"20221201170513275884\",\"fpurchasepersonid\":\"JEdLVexbSo+Y70v5EGqKeYDvfe0=\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"jbYAAAVlObc3xn38\",\"ftotalamount\":5990,\"ftotalqty\":3,\"ftotaltax\":5990,\"ftotaltaxamount\":5990,\"id\":\"962\"}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
//        System.out.println("savePurOrder"+result);
    	 
    	
    	
//    	String jsonStr = "{\"busCode\":\"DZ_MZ_PI\",\"data\":{\"details\":[{\"factualprice\":\"15.00\",\"factualtaxprice\":\"13.00\",\"famount\":\"13\",\"fassociateqty\":\"1\",\"fbaseqty\":\"1\",\"fbaseunitid\":\"G04\",\"fdeliverydate\":\"2022-12-03 21:02:18\",\"fispresent\":\"0\",\"fmaterialid\":\"698\",\"fprice\":\"15.00\",\"fqty\":\"1\",\"fremark\":\"\",\"fseq\":\"1\",\"fsourcebillentryid\":\"6759\",\"fsourcebillentryseq\":\"1\",\"fsourcebillid\":\"979\",\"fsourcebillnumber\":\"979\",\"ftax\":\"0\",\"ftaxamount\":\"13\",\"ftaxprice\":\"13.00\",\"ftaxrate\":\"0\",\"funitid\":\"G04\",\"fwarehouseid\":\"jbYAAAaiC0u76fiu\",\"id\":\"4\",\"parentId\":\"14\"}],\"fadminorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fbizdate\":\"2022-12-03 21:02:18\",\"fcreatorid\":\"\",\"fnumber\":\"979_698\",\"fpurchasepersonid\":\"\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"MzlsK4jDTp6OlqLYCREczjfGffw=\",\"ftotalamount\":\"13\",\"ftotalqty\":\"1\",\"ftotaltax\":\"0\",\"ftotaltaxamount\":\"13\",\"id\":\"14\",\"submitFlag\":\"EAS_SYNC_ING\",\"submitTime\":1670074783596,\"submitTryNumber\":1},\"msgId\":\"DZ_MZ_PI20221203213943_979_698_14\",\"reqTime\":\"20221203213943\"}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
//     	System.out.println("savePurInWare"+result);
//    	PushRecordFacadeFactory.getRemoteInstance().generSaleIssueBill();
    	
    	
    	String jsonStr = "{\"msgId\":\"20221206160801\",\"busCode\":\"GZ_MZ_PO\",\"reqTime\":\"20221206160801\",\"data\":{\"details\":[{\"factualprice\":1945.00,\"factualtaxprice\":1945.00,\"famount\":1945.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-06\",\"fispresent\":\"0\",\"fmaterialid\":\"685\",\"fprice\":1945.00,\"fqty\":1,\"fseq\":1463,\"ftax\":0,\"ftaxamount\":1945.00,\"ftaxprice\":1945.00,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6899\",\"noTaxPriceBySkuPlatform\":1721.24,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":223.76,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":1945.00,\"taxPriceBySkuSup\":1900.00},{\"factualprice\":1945.00,\"factualtaxprice\":1945.00,\"famount\":1945.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-06\",\"fispresent\":\"0\",\"fmaterialid\":\"685\",\"fprice\":1945.00,\"fqty\":1,\"fseq\":1463,\"ftax\":0,\"ftaxamount\":1945.00,\"ftaxprice\":1945.00,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6900\",\"noTaxPriceBySkuPlatform\":1721.24,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":223.76,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":1945.00,\"taxPriceBySkuSup\":1900.00},{\"factualprice\":2100.00,\"factualtaxprice\":2100.00,\"famount\":2100.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-06\",\"fispresent\":\"0\",\"fmaterialid\":\"683\",\"fprice\":2100.00,\"fqty\":1,\"fseq\":1464,\"ftax\":0,\"ftaxamount\":2100.00,\"ftaxprice\":2100.00,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6901\",\"noTaxPriceBySkuPlatform\":1858.41,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":241.59,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":2100.00,\"taxPriceBySkuSup\":1900.00},{\"factualprice\":2100.00,\"factualtaxprice\":2100.00,\"famount\":2100.00,\"fassociateqty\":1,\"fbaseqty\":1,\"fbaseunitid\":\"G02\",\"fdeliverydate\":\"2022-12-06\",\"fispresent\":\"0\",\"fmaterialid\":\"683\",\"fprice\":2100.00,\"fqty\":1,\"fseq\":1464,\"ftax\":0,\"ftaxamount\":2100.00,\"ftaxprice\":2100.00,\"ftaxrate\":0,\"funitid\":\"G02\",\"id\":\"6902\",\"noTaxPriceBySkuPlatform\":1858.41,\"noTaxPriceBySkuSup\":1681.42,\"taxAmountByPlatform\":241.59,\"taxAmountBySup\":218.58,\"taxBySkuByPlatform\":13,\"taxBySkuBySup\":13,\"taxPriceBySkuPlatform\":2100.00,\"taxPriceBySkuSup\":1900.00}],\"fbizdate\":\"2022-12-06\",\"fcustomerid\":\"cw+GQHEuSay63HHYBbYkFL8MBA4=\",\"fnumber\":\"20221206160704261938\",\"fpurchasepersonid\":\"JEdLVexbSo+Y70v5EGqKeYDvfe0=\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fsupplierid\":\"jbYAAAVlObc3xn38\",\"ftotalamount\":8090.00,\"ftotalqty\":4,\"ftotaltax\":0,\"ftotaltaxamount\":8090.00,\"id\":\"91009\"}}";
    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
    	System.out.println("savePurOrder"+result);
    	
    }

    /**
    
     * output getBizInterface method
     */
    protected com.kingdee.eas.framework.ICoreBase getBizInterface() throws Exception
    {
        return com.kingdee.eas.custom.PurPlatSyncBusLogFactory.getRemoteInstance();
    }

    /**
     * output createNewData method
     */
    protected com.kingdee.bos.dao.IObjectValue createNewData()
    {
        com.kingdee.eas.custom.PurPlatSyncBusLogInfo objectValue = new com.kingdee.eas.custom.PurPlatSyncBusLogInfo();
		
        return objectValue;
    }

	@Override
	public void actionAbout_actionPerformed(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		//super.actionAbout_actionPerformed(e);
		
	}

    
}