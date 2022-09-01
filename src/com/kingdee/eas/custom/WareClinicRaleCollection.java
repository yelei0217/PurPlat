package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class WareClinicRaleCollection extends AbstractObjectCollection 
{
    public WareClinicRaleCollection()
    {
        super(WareClinicRaleInfo.class);
    }
    public boolean add(WareClinicRaleInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(WareClinicRaleCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(WareClinicRaleInfo item)
    {
        return removeObject(item);
    }
    public WareClinicRaleInfo get(int index)
    {
        return(WareClinicRaleInfo)getObject(index);
    }
    public WareClinicRaleInfo get(Object key)
    {
        return(WareClinicRaleInfo)getObject(key);
    }
    public void set(int index, WareClinicRaleInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(WareClinicRaleInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(WareClinicRaleInfo item)
    {
        return super.indexOf(item);
    }
}