package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractPushRecordInfo extends com.kingdee.eas.framework.DataBaseInfo implements Serializable 
{
    public AbstractPushRecordInfo()
    {
        this("id");
    }
    protected AbstractPushRecordInfo(String pkField)
    {
        super(pkField);
    }
    /**
     * Object:下推记录's 处理类型property 
     */
    public com.kingdee.eas.custom.app.DateBaseProcessType getProcessType()
    {
        return com.kingdee.eas.custom.app.DateBaseProcessType.getEnum(getString("processType"));
    }
    public void setProcessType(com.kingdee.eas.custom.app.DateBaseProcessType item)
    {
		if (item != null) {
        setString("processType", item.getValue());
		}
    }
    /**
     * Object:下推记录's 数据类型property 
     */
    public com.kingdee.eas.custom.app.DateBasetype getDateBaseType()
    {
        return com.kingdee.eas.custom.app.DateBasetype.getEnum(getString("dateBaseType"));
    }
    public void setDateBaseType(com.kingdee.eas.custom.app.DateBasetype item)
    {
		if (item != null) {
        setString("dateBaseType", item.getValue());
		}
    }
    /**
     * Object:下推记录's 下推状态property 
     */
    public com.kingdee.eas.custom.app.PushStatusEnum getPushStatus()
    {
        return com.kingdee.eas.custom.app.PushStatusEnum.getEnum(getString("PushStatus"));
    }
    public void setPushStatus(com.kingdee.eas.custom.app.PushStatusEnum item)
    {
		if (item != null) {
        setString("PushStatus", item.getValue());
		}
    }
    public BOSObjectType getBOSType()
    {
        return new BOSObjectType("F6221410");
    }
}