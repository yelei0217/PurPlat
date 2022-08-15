package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import java.lang.String;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.framework.IDataBase;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.bos.util.*;

public interface IPushRecord extends IDataBase
{
    public PushRecordInfo getPushRecordInfo(IObjectPK pk) throws BOSException, EASBizException;
    public PushRecordInfo getPushRecordInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException;
    public PushRecordInfo getPushRecordInfo(String oql) throws BOSException, EASBizException;
    public PushRecordCollection getPushRecordCollection() throws BOSException;
    public PushRecordCollection getPushRecordCollection(EntityViewInfo view) throws BOSException;
    public PushRecordCollection getPushRecordCollection(String oql) throws BOSException;
    public void generPurOrder(PushRecordInfo model) throws BOSException;
    public void generPurInBIll(PushRecordInfo model) throws BOSException;
}