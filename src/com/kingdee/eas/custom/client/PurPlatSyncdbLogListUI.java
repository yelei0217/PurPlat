/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;

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
     	//super.actionHelp_actionPerformed(e);
     	ISyncDataEASFacade  is = SyncDataEASFacadeFactory.getRemoteInstance();
     	//is.materialSyncFun("");
    	//is.doCustomerMid();
     	//is.doOrgMid();
     	//is.doSuppMid();
     	//is.doCangkuMid();
     	//is.doPersonMid();
     	
     	//is.syncDateByType( 2 , "" , 0  , "" ,"+WTOjqOTQEuPXFlQEYMe1DfGffw=" ); 
     	is.DoMaterialJson( ""); 
     	
     	//is.DoErrorJon("");
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