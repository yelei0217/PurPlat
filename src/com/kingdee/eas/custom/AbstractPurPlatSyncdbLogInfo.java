package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractPurPlatSyncdbLogInfo extends com.kingdee.eas.framework.DataBaseInfo implements Serializable 
{
    public AbstractPurPlatSyncdbLogInfo()
    {
        this("id");
    }
    protected AbstractPurPlatSyncdbLogInfo(String pkField)
    {
        super(pkField);
    }
    /**
     * Object:��������ͬ����¼'s ��������property 
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
     * Object:��������ͬ����¼'s ͬ��״̬property 
     */
    public boolean isStatus()
    {
        return getBoolean("status");
    }
    public void setStatus(boolean item)
    {
        setBoolean("status", item);
    }
    /**
     * Object:��������ͬ����¼'s �汾property 
     */
    public String getVersion()
    {
        return getString("version");
    }
    public void setVersion(String item)
    {
        setString("version", item);
    }
    /**
     * Object:��������ͬ����¼'s ͬ��ʱ��property 
     */
    public java.sql.Time getUpdatetime()
    {
        return getTime("updatetime");
    }
    public void setUpdatetime(java.sql.Time item)
    {
        setTime("updatetime", item);
    }
    /**
     * Object:��������ͬ����¼'s ��������property 
     */
    public java.util.Date getUpdateDate()
    {
        return getDate("updateDate");
    }
    public void setUpdateDate(java.util.Date item)
    {
        setDate("updateDate", item);
    }
    /**
     * Object:��������ͬ����¼'s ��������property 
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
     * Object:��������ͬ����¼'s ��Ϣproperty 
     */
    public String getMessage()
    {
        return getString("Message");
    }
    public void setMessage(String item)
    {
        setString("Message", item);
    }
    /**
     * Object:��������ͬ����¼'s ��Ӧ��Ϣproperty 
     */
    public String getRespond()
    {
        return getString("Respond");
    }
    public void setRespond(String item)
    {
        setString("Respond", item);
    }
    /**
     * Object:��������ͬ����¼'s �������property 
     */
    public int getSendCount()
    {
        return getInt("SendCount");
    }
    public void setSendCount(int item)
    {
        setInt("SendCount", item);
    }
    /**
     * Object:��������ͬ����¼'s ʧ����Ϣproperty 
     */
    public String getErrorMessage()
    {
        return getString("errorMessage");
    }
    public void setErrorMessage(String item)
    {
        setString("errorMessage", item);
    }
    /**
     * Object:��������ͬ����¼'s �Ƿ�����ͬ��property 
     */
    public boolean isIsSync()
    {
        return getBoolean("isSync");
    }
    public void setIsSync(boolean item)
    {
        setBoolean("isSync", item);
    }
    public BOSObjectType getBOSType()
    {
        return new BOSObjectType("6E21F7F4");
    }
}