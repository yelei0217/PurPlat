/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;

import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.custom.PushRecordFacadeFactory;
import com.kingdee.eas.custom.SyncBill2EASFacadeFactory;
import com.kingdee.eas.framework.*;

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

    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }
    
    @Override
    public void actionHelp_actionPerformed(ActionEvent e) throws Exception {
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
    	
    	//
//     	String jsonStr = "{\"msgId\":\"202208102203126\",\"busCode\":\"GZ_LZ_PI\",\"reqTime\":\"202208102203126\",\"data\":{\"id\":\"202208102203126\",\"fnumber\":\"202208102203126\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052203123-111\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208052319122\",\"fsourcebillentryid\":\"202208052319122-2\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);

    	
//    	String jsonStr = "{\"msgId\":\"202208102203128\",\"busCode\":\"VMI_MZ_PI\",\"reqTime\":\"202208102203128\",\"data\":{\"id\":\"202208102203128\",\"fnumber\":\"202208102203128\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052203123-111\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208071644121\",\"busCode\":\"GZ_LZ_SS\",\"reqTime\":\"202208071644121\",\"data\":{\"id\":\"202208071644121\",\"fnumber\":\"202208071644121\",\"fbizdate\":\"2022-08-07\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208071644121-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208071532120\",\"fsourcebillentryid\":\"202208071532120-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-04\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208102203128\",\"busCode\":\"VMI_MZ_PI\",\"reqTime\":\"202208102203128\",\"data\":{\"id\":\"202208102203128\",\"fnumber\":\"202208102203128\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208052203123-111\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208102203129\",\"busCode\":\"VMIB_MZ_PI\",\"reqTime\":\"202208102203129\",\"data\":{\"id\":\"202208102203129\",\"fnumber\":\"202208102203129\",\"fbizdate\":\"2022-08-10\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208102203129-111\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-10\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
    	
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208111419123\",\"busCode\":\"VMI_LZ_SS\",\"reqTime\":\"202208111419123\",\"data\":{\"id\":\"202208111419123\",\"fnumber\":\"202208111419123\",\"fbizdate\":\"2022-08-11\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208111419123-1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-12\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	
//     	String jsonStr = "{\"msgId\":\"202208112015125\",\"busCode\":\"VMIB_LZ_SS\",\"reqTime\":\"202208112015125\",\"data\":{\"id\":\"202208112015125\",\"fnumber\":\"202208112015125\",\"fbizdate\":\"2022-08-11\",\"fstorageorgunitid\":\"jbYAAAMU2SvM567U\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fcustomerid\":\"+j25Byr3QE6/VcQkJCNqHb8MBA4=\",\"fadminorgunitid\":\"jbYAAAWC4L/M567U\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208112015125-1\",\"fwarehouseid\":\"jbYAAAac+li76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-12\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleIss(jsonStr);
    	
    	
    	
//    	String jsonStr ="{\"msgId\":\"202208151715120\",\"busCode\":\"CGZ_U_MZ_SO\",\"reqTime\":\"202208151715120\",\"data\":{\"id\":\"202208151715120\",\"fnumber\":\"202208151715120\",\"fbizdate\":\"2022-08-15\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fordercustomerid\":\"svL0ZnRPS86qelCx023QZ78MBA4=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208151715120-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fsenddate\":\"2022-08-15\",\"fdeliverydate\":\"2022-08-15\",\"fbaseqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr);
//    	
//    	PushRecordFacadeFactory.getRemoteInstance().generSaleIssueBill();

//    	String jsonStr ="{\"msgId\":\"202208221830112\",\"busCode\":\"DZ_MZ_PO\",\"reqTime\":\"202208221830112\",\"data\":{\"id\":\"202208221830112\",\"fnumber\":\"202208221830112\",\"fbizdate\":\"2022-08-22\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208221830112-1\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-22\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurOrder(jsonStr);
    	
    	
//    	String jsonStr = "{\"msgId\":\"202208221930123\",\"busCode\":\"DZ_MZ_PI\",\"reqTime\":\"202208221930123\",\"data\":{\"id\":\"202208221930123\",\"fnumber\":\"202208221930123\",\"fbizdate\":\"2022-08-22\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fpurchasepersonid\":\"Lt2bG2FWRzy0+1V/cUTJFIDvfe0=\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotalqty\":2,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"details\":[{\"id\":\"202208221930123-1\",\"fsourcebillid\":\"\",\"fsourcebillnumber\":\"202208221830112\",\"fsourcebillentryid\":\"202208221830112-1\",\"fsourcebillentryseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fstockerid\":\"\",\"fseq\":\"1\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fdeliverydate\":\"2022-08-22\",\"fbaseqty\":2}]}}";
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().savePurInWare(jsonStr);
  
//    	String jsonStr ="{\"msgId\":\"202208242333123\",\"busCode\":\"VMI_U_MZ_SO\",\"reqTime\":\"202208242333123\",\"data\":{\"id\":\"202208242333123\",\"fnumber\":\"202208242333123\",\"fbizdate\":\"2022-08-24\",\"fstorageorgunitid\":\"+dryvYY9QwWpQdJSTAHAesznrtQ=\",\"fordercustomerid\":\"XYUDVb+9ThW45EPmR5nBtL8MBA4=\",\"fadminorgunitid\":\"fo8U5I4cQBOrVQRR2ZMvHcznrtQ=\",\"ftotalamount\":172.41,\"ftotaltax\":27.59,\"ftotaltaxamount\":200,\"fcreatorid\":\"\",\"fsupplierid\":\"ZrYAxLT6RbCTmHETKr1WijfGffw=\",\"details\":[{\"id\":\"202208242333123-1\",\"fseq\":\"1\",\"fwarehouseid\":\"jbYAAAAF8SG76fiu\",\"fmaterialid\":\"YblhcM4pQOutUg5b4WEyC0QJ5/A=\",\"funitid\":\"G03\",\"fassociateqty\":\"2\",\"fbaseunitid\":\"G03\",\"fremark\":\"ccss\",\"fispresent\":false,\"fqty\":2,\"fprice\":86.206897,\"factualprice\":86.206897,\"ftaxrate\":16,\"ftaxprice\":100,\"factualtaxprice\":100,\"famount\":172.41,\"ftax\":27.59,\"ftaxamount\":200,\"fsenddate\":\"2022-08-24\",\"fdeliverydate\":\"2022-08-24\",\"fbaseqty\":2}]}}"; 
//    	String result = SyncBill2EASFacadeFactory.getRemoteInstance().saveSaleOrder(jsonStr);
    	
//    	PushRecordFacadeFactory.getRemoteInstance().generSaleIssueBill();
    	
//    	PushRecordFacadeFactory.getRemoteInstance().generPurInBIll();
    	
//        	System.out.println("fsf"+result);
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

}