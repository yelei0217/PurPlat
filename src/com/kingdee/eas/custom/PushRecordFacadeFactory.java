package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class PushRecordFacadeFactory
{
    private PushRecordFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.IPushRecordFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecordFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("1C9F64CA") ,com.kingdee.eas.custom.IPushRecordFacade.class);
    }
    
    public static com.kingdee.eas.custom.IPushRecordFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecordFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("1C9F64CA") ,com.kingdee.eas.custom.IPushRecordFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IPushRecordFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecordFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("1C9F64CA"));
    }
    public static com.kingdee.eas.custom.IPushRecordFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IPushRecordFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("1C9F64CA"));
    }
}