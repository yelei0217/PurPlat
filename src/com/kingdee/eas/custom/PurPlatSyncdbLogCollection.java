package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class PurPlatSyncdbLogCollection extends AbstractObjectCollection 
{
    public PurPlatSyncdbLogCollection()
    {
        super(PurPlatSyncdbLogInfo.class);
    }
    public boolean add(PurPlatSyncdbLogInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(PurPlatSyncdbLogCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(PurPlatSyncdbLogInfo item)
    {
        return removeObject(item);
    }
    public PurPlatSyncdbLogInfo get(int index)
    {
        return(PurPlatSyncdbLogInfo)getObject(index);
    }
    public PurPlatSyncdbLogInfo get(Object key)
    {
        return(PurPlatSyncdbLogInfo)getObject(key);
    }
    public void set(int index, PurPlatSyncdbLogInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(PurPlatSyncdbLogInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(PurPlatSyncdbLogInfo item)
    {
        return super.indexOf(item);
    }
}