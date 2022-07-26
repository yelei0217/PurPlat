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