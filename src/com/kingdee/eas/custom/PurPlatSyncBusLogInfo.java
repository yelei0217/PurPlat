package com.kingdee.eas.custom;

import java.io.Serializable;

public class PurPlatSyncBusLogInfo extends AbstractPurPlatSyncBusLogInfo implements Serializable 
{
    public PurPlatSyncBusLogInfo()
    {
        super();
    }
    protected PurPlatSyncBusLogInfo(String pkField)
    {
        super(pkField);
    }
}