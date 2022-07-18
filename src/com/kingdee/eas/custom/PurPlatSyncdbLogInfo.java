package com.kingdee.eas.custom;

import java.io.Serializable;

public class PurPlatSyncdbLogInfo extends AbstractPurPlatSyncdbLogInfo implements Serializable 
{
    public PurPlatSyncdbLogInfo()
    {
        super();
    }
    protected PurPlatSyncdbLogInfo(String pkField)
    {
        super(pkField);
    }
}