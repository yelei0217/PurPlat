package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class PurPlatSyncBusLogFactory
{
    private PurPlatSyncBusLogFactory()
    {
    }
    public static com.kingdee.eas.custom.IPurPlatSyncBusLog getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncBusLog)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("1D353876") ,com.kingdee.eas.custom.IPurPlatSyncBusLog.class);
    }
    
    public static com.kingdee.eas.custom.IPurPlatSyncBusLog getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncBusLog)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("1D353876") ,com.kingdee.eas.custom.IPurPlatSyncBusLog.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IPurPlatSyncBusLog getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncBusLog)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("1D353876"));
    }
    public static com.kingdee.eas.custom.IPurPlatSyncBusLog getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncBusLog)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("1D353876"));
    }
}