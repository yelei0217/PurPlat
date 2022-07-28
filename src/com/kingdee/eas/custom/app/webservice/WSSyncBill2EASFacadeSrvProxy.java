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

    public void savePurOrder( String jsonStr ) throws WSInvokeException {
        try {
            getController().savePurOrder(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void savePaymentBill( String jsonStr ) throws WSInvokeException {
        try {
            getController().savePaymentBill(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveOtherSaleIss( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveOtherSaleIss(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void savePurInCGW( String jsonStr ) throws WSInvokeException {
        try {
            getController().savePurInCGW(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveCostAdjus( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveCostAdjus(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveSaleIss( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveSaleIss(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveOtherPurIn( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveOtherPurIn(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveReceiveBill( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveReceiveBill(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveApOtherBill( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveApOtherBill(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void savePurInWare( String jsonStr ) throws WSInvokeException {
        try {
            getController().savePurInWare(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void purOrderCloseRow( String jsonStr ) throws WSInvokeException {
        try {
            getController().purOrderCloseRow(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveArOtherBill( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveArOtherBill(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saveSaleOrder( String jsonStr ) throws WSInvokeException {
        try {
            getController().saveSaleOrder(
            jsonStr
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void saleOrderCloseRow( String jsonStr ) throws WSInvokeException {
        try {
            getController().saleOrderCloseRow(
            jsonStr
            );
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