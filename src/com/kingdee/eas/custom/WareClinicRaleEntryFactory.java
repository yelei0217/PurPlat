package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class WareClinicRaleEntryFactory
{
    private WareClinicRaleEntryFactory()
    {
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntry getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntry)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("446936C0") ,com.kingdee.eas.custom.IWareClinicRaleEntry.class);
    }
    
    public static com.kingdee.eas.custom.IWareClinicRaleEntry getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntry)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("446936C0") ,com.kingdee.eas.custom.IWareClinicRaleEntry.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntry getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntry)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("446936C0"));
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntry getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntry)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("446936C0"));
    }
}