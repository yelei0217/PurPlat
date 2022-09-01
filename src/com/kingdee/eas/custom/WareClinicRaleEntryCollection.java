package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class WareClinicRaleEntryCollection extends AbstractObjectCollection 
{
    public WareClinicRaleEntryCollection()
    {
        super(WareClinicRaleEntryInfo.class);
    }
    public boolean add(WareClinicRaleEntryInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(WareClinicRaleEntryCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(WareClinicRaleEntryInfo item)
    {
        return removeObject(item);
    }
    public WareClinicRaleEntryInfo get(int index)
    {
        return(WareClinicRaleEntryInfo)getObject(index);
    }
    public WareClinicRaleEntryInfo get(Object key)
    {
        return(WareClinicRaleEntryInfo)getObject(key);
    }
    public void set(int index, WareClinicRaleEntryInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(WareClinicRaleEntryInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(WareClinicRaleEntryInfo item)
    {
        return super.indexOf(item);
    }
}