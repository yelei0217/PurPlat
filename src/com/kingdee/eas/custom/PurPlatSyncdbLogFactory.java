package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class PurPlatSyncdbLogFactory
{
    private PurPlatSyncdbLogFactory()
    {
    }
    public static com.kingdee.eas.custom.IPurPlatSyncdbLog getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncdbLog)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("6E21F7F4") ,com.kingdee.eas.custom.IPurPlatSyncdbLog.class);
    }
    
    public static com.kingdee.eas.custom.IPurPlatSyncdbLog getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncdbLog)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("6E21F7F4") ,com.kingdee.eas.custom.IPurPlatSyncdbLog.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IPurPlatSyncdbLog getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncdbLog)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("6E21F7F4"));
    }
    public static com.kingdee.eas.custom.IPurPlatSyncdbLog getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IPurPlatSyncdbLog)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("6E21F7F4"));
    }
}