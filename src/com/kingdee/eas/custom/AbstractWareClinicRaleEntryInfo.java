package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractWareClinicRaleEntryInfo extends com.kingdee.eas.framework.CoreBillEntryBaseInfo implements Serializable 
{
    public AbstractWareClinicRaleEntryInfo()
    {
        this("id");
    }
    protected AbstractWareClinicRaleEntryInfo(String pkField)
    {
        super(pkField);
        put("DEntrys", new com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection());
    }
    /**
     * Object: 分录 's 单据头 property 
     */
    public com.kingdee.eas.custom.WareClinicRaleInfo getParent()
    {
        return (com.kingdee.eas.custom.WareClinicRaleInfo)get("parent");
    }
    public void setParent(com.kingdee.eas.custom.WareClinicRaleInfo item)
    {
        put("parent", item);
    }
    /**
     * Object: 分录 's 明细分录 property 
     */
    public com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection getDEntrys()
    {
        return (com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection)get("DEntrys");
    }
    /**
     * Object: 分录 's 仓库编码 property 
     */
    public com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo getWarehouse()
    {
        return (com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo)get("warehouse");
    }
    public void setWarehouse(com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo item)
    {
        put("warehouse", item);
    }
    /**
     * Object:分录's 仓库名称property 
     */
    public String getWarehouseName()
    {
        return getString("warehouseName");
    }
    public void setWarehouseName(String item)
    {
        setString("warehouseName", item);
    }
    /**
     * Object:分录's 启用状态property 
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
        return new BOSObjectType("446936C0");
    }
}