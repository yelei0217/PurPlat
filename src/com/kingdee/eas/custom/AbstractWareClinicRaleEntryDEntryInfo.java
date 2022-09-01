package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractWareClinicRaleEntryDEntryInfo extends com.kingdee.eas.framework.CoreBillEntryBaseInfo implements Serializable 
{
    public AbstractWareClinicRaleEntryDEntryInfo()
    {
        this("id");
    }
    protected AbstractWareClinicRaleEntryDEntryInfo(String pkField)
    {
        super(pkField);
    }
    /**
     * Object: 门诊分录 's null property 
     */
    public com.kingdee.eas.custom.WareClinicRaleEntryInfo getParent1()
    {
        return (com.kingdee.eas.custom.WareClinicRaleEntryInfo)get("parent1");
    }
    public void setParent1(com.kingdee.eas.custom.WareClinicRaleEntryInfo item)
    {
        put("parent1", item);
    }
    /**
     * Object: 门诊分录 's 门诊编码 property 
     */
    public com.kingdee.eas.basedata.org.StorageOrgUnitInfo getClinic()
    {
        return (com.kingdee.eas.basedata.org.StorageOrgUnitInfo)get("Clinic");
    }
    public void setClinic(com.kingdee.eas.basedata.org.StorageOrgUnitInfo item)
    {
        put("Clinic", item);
    }
    /**
     * Object:门诊分录's 门诊名称property 
     */
    public String getClinicName()
    {
        return getString("ClinicName");
    }
    public void setClinicName(String item)
    {
        setString("ClinicName", item);
    }
    /**
     * Object:门诊分录's 启用状态property 
     */
    public com.kingdee.eas.basedata.framework.DataStateEnum getDataState()
    {
        return com.kingdee.eas.basedata.framework.DataStateEnum.getEnum(getInt("DataState"));
    }
    public void setDataState(com.kingdee.eas.basedata.framework.DataStateEnum item)
    {
		if (item != null) {
        setInt("DataState", item.getValue());
		}
    }
    public BOSObjectType getBOSType()
    {
        return new BOSObjectType("46DEC94E");
    }
}