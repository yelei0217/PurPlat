package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class SyncBill2EASFacadeFactory
{
    private SyncBill2EASFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.ISyncBill2EASFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncBill2EASFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("D8347086") ,com.kingdee.eas.custom.ISyncBill2EASFacade.class);
    }
    
    public static com.kingdee.eas.custom.ISyncBill2EASFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncBill2EASFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("D8347086") ,com.kingdee.eas.custom.ISyncBill2EASFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.ISyncBill2EASFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncBill2EASFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("D8347086"));
    }
    public static com.kingdee.eas.custom.ISyncBill2EASFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.ISyncBill2EASFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("D8347086"));
    }
}