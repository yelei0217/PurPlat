package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class WareClinicRaleEntryDEntryCollection extends AbstractObjectCollection 
{
    public WareClinicRaleEntryDEntryCollection()
    {
        super(WareClinicRaleEntryDEntryInfo.class);
    }
    public boolean add(WareClinicRaleEntryDEntryInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(WareClinicRaleEntryDEntryCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(WareClinicRaleEntryDEntryInfo item)
    {
        return removeObject(item);
    }
    public WareClinicRaleEntryDEntryInfo get(int index)
    {
        return(WareClinicRaleEntryDEntryInfo)getObject(index);
    }
    public WareClinicRaleEntryDEntryInfo get(Object key)
    {
        return(WareClinicRaleEntryDEntryInfo)getObject(key);
    }
    public void set(int index, WareClinicRaleEntryDEntryInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(WareClinicRaleEntryDEntryInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(WareClinicRaleEntryDEntryInfo item)
    {
        return super.indexOf(item);
    }
}