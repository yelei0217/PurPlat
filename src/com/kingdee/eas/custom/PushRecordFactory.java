package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class PushRecordFactory
{
    private PushRecordFactory()
    {
    }
    public static com.kingdee.eas.custom.IPushRecord getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecord)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("F6221410") ,com.kingdee.eas.custom.IPushRecord.class);
    }
    
    public static com.kingdee.eas.custom.IPushRecord getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecord)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("F6221410") ,com.kingdee.eas.custom.IPushRecord.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IPushRecord getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecord)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("F6221410"));
    }
    public static com.kingdee.eas.custom.IPushRecord getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecord)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("F6221410"));
    }
}