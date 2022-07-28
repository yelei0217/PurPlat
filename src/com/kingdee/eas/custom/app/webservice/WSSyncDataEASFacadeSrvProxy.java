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

public class WSSyncDataEASFacadeSrvProxy { 

    public void syncDateByType( int type , String data , int newOrDele , String name , String number ) throws WSInvokeException {
        try {
            getController().syncDateByType(
            type,
            data,
            newOrDele,
            name,
            number
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void DoErrorJon( String data ) throws WSInvokeException {
        try {
            getController().DoErrorJon(
            data
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public String materialSyncFun( String data ) throws WSInvokeException {
        try {
            return getController().materialSyncFun(
            data);
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    public void DoMaterialJson( String data ) throws WSInvokeException {
        try {
            getController().DoMaterialJson(
            data
            );
        }
        catch( Throwable e ) {
            throw new WSInvokeException( e ) ;
        }
    }

    private com.kingdee.eas.custom.ISyncDataEASFacade getController() {
        try {
        if (WSConfig.getRomoteLocate()!=null&&WSConfig.getRomoteLocate().equals("false")){
            Message message =MessageContext.getCurrentContext().getRequestMessage();
            SOAPEnvelope soap =message.getSOAPEnvelope();
            SOAPHeaderElement headerElement=soap.getHeaderByName(WSConfig.loginQName,WSConfig.loginSessionId);
            String SessionId=headerElement.getValue();
            return ( com.kingdee.eas.custom.ISyncDataEASFacade )BOSObjectFactory.createBOSObject( SessionId , "com.kingdee.eas.custom.SyncDataEASFacade") ; 
        } else {
            return ( com.kingdee.eas.custom.ISyncDataEASFacade )BOSObjectFactory.createRemoteBOSObject( WSConfig.getSrvURL() , "com.kingdee.eas.custom.SyncDataEASFacade" , com.kingdee.eas.custom.ISyncDataEASFacade.class ) ; 
        }
        }
        catch( Throwable e ) {
            return ( com.kingdee.eas.custom.ISyncDataEASFacade )ORMEngine.createRemoteObject( WSConfig.getSrvURL() , "com.kingdee.eas.custom.SyncDataEASFacade" , com.kingdee.eas.custom.ISyncDataEASFacade.class ) ; 
        }
    }

    private BeanConvertHelper getBeanConvertor() {
        return new BeanConvertHelper(); 
    }

}