/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;

import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.custom.SyncBill2EASFacadeFactory;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.framework.*;

/**
 * output class name
 */
public class PurPlatSyncdbLogListUI extends AbstractPurPlatSyncdbLogListUI
{
    private static final Logger logger = CoreUIObject.getLogger(PurPlatSyncdbLogListUI.class);
    
    /**
     * output class constructor
     */
    public PurPlatSyncdbLogListUI() throws Exception
    {
        super();
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
    	// TODO Auto-generated method stub
    	super.actionHelp_actionPerformed(e);
//    	String jsonStr ="{\"msgId\":\"202208052319122\",\"busCode\":\"GZ_LZ_PO\",\"reqTime\":\"202208052319122\",\"data\":{\"id\":\"202208052319122\",\"fnumber\":\"202208052319122\",\"fbizdate\":\"2022-08-05\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052319122-2\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-04\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
    	
//    	String jsonStr ="{\"msgId\":\"202208071532120\",\"busCode\":\"GZ_LZ_SO\",\"reqTime\":\"202208071532120\",\"data\":{\"id\":\"202208071532120\",\"fnumber\":\"202208071532120\",\"fbizdate\":\"2022-08-07\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fordercustomerid\":\"XYUDVb+9ThW45EPmR5nBtL8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208071532120-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fsenddate\":\"2022-08-07\",\"fdeliverydate\":\"2022-08-07\",\"fbaseqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr);
    	
//    	String jsonStr ="{\"msgId\":\"111111\",\"busCode\":\"GZB_LZ_SO_CR\",\"reqTime\":\"202208041736120\",\"data\":{\"id\":\"1111\",\"eids\":\"111-111\"}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saleOrderCloseRow(jsonStr);
    	
//    	String jsonStr ="{\"msgId\":\"111111\",\"busCode\":\"GZB_LZ_PO_CR\",\"reqTime\":\"202208041736120\",\"data\":{\"id\":\"1111\",\"eids\":\"111-111\"}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().purOrderCloseRow(jsonStr);
    	
//    	String jsonStr = "{\"msgId\":\"202208051644123\",\"busCode\":\"GZ_MZ_PO\",\"reqTime\":\"202208051644123\",\"data\":{\"id\":\"202208051644123\",\"fnumber\":\"202208051644123\",\"fbizdate\":\"2022-08-05\",\"fstorageorgunitid\":\"kO6+E1zaSbKzTzLMtI+csMznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208051644123-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G01\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G01\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-05\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208052203123\",\"busCode\":\"GZ_LZ_PI\",\"reqTime\":\"202208052203123\",\"data\":{\"id\":\"202208052203123\",\"fnumber\":\"202208052203123\",\"fbizdate\":\"2022-08-07\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052203123-111\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"1111\",\"fsourcebillentryid\":\"111-111\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-04\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);

    	String jsonStr = "{\"msgId\":\"202208071644121\",\"busCode\":\"GZ_LZ_SS\",\"reqTime\":\"202208071644121\",\"data\":{\"id\":\"202208071644121\",\"fnumber\":\"202208071644121\",\"fbizdate\":\"2022-08-07\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208071644121-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208071532120\",\"fsourcebillentryid\":\"202208071532120-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-04\",\"fbaseqty\":2}]}}";
    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	System.out.println("fsf"+result);
    }
    
    /**
     * output getBizInterface method
     */
    protected com.kingdee.eas.framework.ICoreBase getBizInterface() throws Exception
    {
        return com.kingdee.eas.custom.PurPlatSyncdbLogFactory.getRemoteInstance();
    }

    /**
     * output createNewData method
     */
    protected com.kingdee.bos.dao.IObjectValue createNewData()
    {
        com.kingdee.eas.custom.PurPlatSyncdbLogInfo objectValue = new com.kingdee.eas.custom.PurPlatSyncdbLogInfo();
		
        return objectValue;
    }

}