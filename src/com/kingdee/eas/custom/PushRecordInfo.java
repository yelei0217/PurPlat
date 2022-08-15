package com.kingdee.eas.custom;

import java.io.Serializable;

public class PushRecordInfo extends AbstractPushRecordInfo implements Serializable 
{
    public PushRecordInfo()
    {
        super();
    }
    protected PushRecordInfo(String pkField)
    {
        super(pkField);
    }
}