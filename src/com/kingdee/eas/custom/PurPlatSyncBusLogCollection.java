package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class PurPlatSyncBusLogCollection extends AbstractObjectCollection 
{
    public PurPlatSyncBusLogCollection()
    {
        super(PurPlatSyncBusLogInfo.class);
    }
    public boolean add(PurPlatSyncBusLogInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(PurPlatSyncBusLogCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(PurPlatSyncBusLogInfo item)
    {
        return removeObject(item);
    }
    public PurPlatSyncBusLogInfo get(int index)
    {
        return(PurPlatSyncBusLogInfo)getObject(index);
    }
    public PurPlatSyncBusLogInfo get(Object key)
    {
        return(PurPlatSyncBusLogInfo)getObject(key);
    }
    public void set(int index, PurPlatSyncBusLogInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(PurPlatSyncBusLogInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(PurPlatSyncBusLogInfo item)
    {
        return super.indexOf(item);
    }
}