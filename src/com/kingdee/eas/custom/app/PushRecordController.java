package com.kingdee.eas.custom.app;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.dao.IObjectPK;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.custom.PushRecordInfo;
import com.kingdee.bos.Context;
import com.kingdee.eas.framework.app.DataBaseController;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.custom.PushRecordCollection;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;

import java.rmi.RemoteException;
import com.kingdee.bos.framework.ejb.BizController;

public interface PushRecordController extends DataBaseController
{
    public PushRecordInfo getPushRecordInfo(Context ctx, IObjectPK pk) throws BOSException, EASBizException, RemoteException;
    public PushRecordInfo getPushRecordInfo(Context ctx, IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException, RemoteException;
    public PushRecordInfo getPushRecordInfo(Context ctx, String oql) throws BOSException, EASBizException, RemoteException;
    public PushRecordCollection getPushRecordCollection(Context ctx) throws BOSException, RemoteException;
    public PushRecordCollection getPushRecordCollection(Context ctx, EntityViewInfo view) throws BOSException, RemoteException;
    public PushRecordCollection getPushRecordCollection(Context ctx, String oql) throws BOSException, RemoteException;
    public void generPurOrder(Context ctx, PushRecordInfo model) throws BOSException, RemoteException;
    public void generPurInBIll(Context ctx, PushRecordInfo model) throws BOSException, RemoteException;
}