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
     * Object: ²Ö¿â·ÖÂ¼ 's µ¥¾ÝÍ· property 
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
     * Object: ²Ö¿â·ÖÂ¼ 's ÃÅÕï·ÖÂ¼ property 
     */
    public com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection getDEntrys()
    {
        return (com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection)get("DEntrys");
    }
    /**
     * Object: ²Ö¿â·ÖÂ¼ 's ²Ö¿â±àÂë property 
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
     * Object:²Ö¿â·ÖÂ¼'s ²Ö¿âÃû³Æproperty 
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
     * Object:²Ö¿â·ÖÂ¼'s ÆôÓÃ×´Ì¬property 
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