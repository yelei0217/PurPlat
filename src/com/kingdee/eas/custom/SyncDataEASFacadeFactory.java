package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class SyncDataEASFacadeFactory
{
    private SyncDataEASFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.ISyncDataEASFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncDataEASFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("5FE133C7") ,com.kingdee.eas.custom.ISyncDataEASFacade.class);
    }
    
    public static com.kingdee.eas.custom.ISyncDataEASFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncDataEASFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("5FE133C7") ,com.kingdee.eas.custom.ISyncDataEASFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.ISyncDataEASFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncDataEASFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("5FE133C7"));
    }
    public static com.kingdee.eas.custom.ISyncDataEASFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncDataEASFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("5FE133C7"));
    }
}