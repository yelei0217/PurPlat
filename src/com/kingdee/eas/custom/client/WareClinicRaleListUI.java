/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;
import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.framework.*;

/**
 * output class name
 */
public class WareClinicRaleListUI extends AbstractWareClinicRaleListUI
{
    private static final Logger logger = CoreUIObject.getLogger(WareClinicRaleListUI.class);
    
    /**
     * output class constructor
     */
    public WareClinicRaleListUI() throws Exception
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
        return com.kingdee.eas.custom.WareClinicRaleFactory.getRemoteInstance();
    }

    /**
     * output createNewData method
     */
    protected com.kingdee.bos.dao.IObjectValue createNewData()
    {
        com.kingdee.eas.custom.WareClinicRaleInfo objectValue = new com.kingdee.eas.custom.WareClinicRaleInfo();
		
        return objectValue;
    }

}