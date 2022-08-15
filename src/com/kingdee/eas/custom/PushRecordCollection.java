package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class PushRecordCollection extends AbstractObjectCollection 
{
    public PushRecordCollection()
    {
        super(PushRecordInfo.class);
    }
    public boolean add(PushRecordInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(PushRecordCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(PushRecordInfo item)
    {
        return removeObject(item);
    }
    public PushRecordInfo get(int index)
    {
        return(PushRecordInfo)getObject(index);
    }
    public PushRecordInfo get(Object key)
    {
        return(PushRecordInfo)getObject(key);
    }
    public void set(int index, PushRecordInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(PushRecordInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(PushRecordInfo item)
    {
        return super.indexOf(item);
    }
}