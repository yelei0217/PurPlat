package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractWareClinicRaleInfo extends com.kingdee.eas.framework.CoreBillBaseInfo implements Serializable 
{
    public AbstractWareClinicRaleInfo()
    {
        this("id");
    }
    protected AbstractWareClinicRaleInfo(String pkField)
    {
        super(pkField);
        put("entrys", new com.kingdee.eas.custom.WareClinicRaleEntryCollection());
    }
    /**
     * Object: ≤÷ø‚√≈’Ô”≥…‰ 's ≤÷ø‚∑÷¬º property 
     */
    public com.kingdee.eas.custom.WareClinicRaleEntryCollection getEntrys()
    {
        return (com.kingdee.eas.custom.WareClinicRaleEntryCollection)get("entrys");
    }
    public BOSObjectType getBOSType()
    {
        return new BOSObjectType("DCFD14D2");
    }
}