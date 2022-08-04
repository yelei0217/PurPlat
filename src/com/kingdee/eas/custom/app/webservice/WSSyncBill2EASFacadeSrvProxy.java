package com.kingdee.eas.custom.app.webservice;

import org.apache.axis.Message;

import org.apache.axis.MessageContext;

import org.apache.axis.message.SOAPEnvelope;

import org.apache.axis.message.SOAPHeaderElement;

import com.kingdee.bos.webservice.WSConfig;

import com.kingdee.bos.webservice.WSInvokeException;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ObjectMultiPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.orm.core.ORMEngine;
import com.kingdee.bos.webservice.BeanConvertHelper;
import com.kingdee.bos.webservice.BOSTypeConvertor;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.bos.webservice.WSConfig;
import com.kingdee.bos.webservice.MetaDataHelper;
import com.kingdee.bos.BOSObjectFactory;

public class WSSyncBill2EASFacadeSrvProxy { 

    public String savePaymentBill( String jsonStr ) throws WSInvokeException {
        try {
            return getController().savePaymentBill(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String savePurOrder( String jsonStr ) throws WSInvokeException {
        try {
            return getController().savePurOrder(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveSaleIss( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveSaleIss(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveOtherPurIn( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveOtherPurIn(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String savePurInCGW( String jsonStr ) throws WSInvokeException {
        try {
            return getController().savePurInCGW(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String purOrderCloseRow( String jsonStr ) throws WSInvokeException {
        try {
            return getController().purOrderCloseRow(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveSaleOrder( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveSaleOrder(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveReceiveBill( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveReceiveBill(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String savePurInWare( String jsonStr ) throws WSInvokeException {
        try {
            return getController().savePurInWare(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saleOrderCloseRow( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saleOrderCloseRow(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveApOtherBill( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveApOtherBill(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveArOtherBill( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveArOtherBill(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveOtherSaleIss( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveOtherSaleIss(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String saveCostAdjus( String jsonStr ) throws WSInvokeException {
        try {
            return getController().saveCostAdjus(
            jsonStr);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    private com.kingdee.eas.custom.ISyncBill2EASFacade getController() {
        try {
        if (WSConfig.getRomoteLocate()!=null&&WSConfig.getRomoteLocate().equals("false")){
            Message message =MessageContext.getCurrentContext().getRequestMessage();
            SOAPEnvelope soap =message.getSOAPEnvelope();
            SOAPHeaderElement headerElement=soap.getHeaderByName(WSConfig.loginQName,WSConfig.loginSessionId);
            String SessionId=headerElement.getValue();
            return ( com.kingdee.eas.custom.ISyncBill2EASFacade )BOSObjectFactory.createBOSObject( SessionId , "com.kingdee.eas.custom.SyncBill2EASFacade") ; 
        } else {
            return ( com.kingdee.eas.custom.ISyncBill2EASFacade )BOSObjectFactory.createRemoteBOSObject( WSConfig.getSrvURL() , "com.kingdee.eas.custom.SyncBill2EASFacade" , com.kingdee.eas.custom.ISyncBill2EASFacade.class ) ; 
        }
        }
        catch( Throwable e ) {
            return ( com.kingdee.eas.custom.ISyncBill2EASFacade )ORMEngine.createRemoteObject( WSConfig.getSrvURL() , "com.kingdee.eas.custom.SyncBill2EASFacade" , com.kingdee.eas.custom.ISyncBill2EASFacade.class ) ; 
        }
    }

    private BeanConvertHelper getBeanConvertor() {
        return new BeanConvertHelper(); 
    }

}