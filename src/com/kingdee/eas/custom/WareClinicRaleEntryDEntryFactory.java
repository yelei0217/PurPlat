package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class WareClinicRaleEntryDEntryFactory
{
    private WareClinicRaleEntryDEntryFactory()
    {
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntryDEntry getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntryDEntry)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("46DEC94E") ,com.kingdee.eas.custom.IWareClinicRaleEntryDEntry.class);
    }
    
    public static com.kingdee.eas.custom.IWareClinicRaleEntryDEntry getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntryDEntry)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("46DEC94E") ,com.kingdee.eas.custom.IWareClinicRaleEntryDEntry.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntryDEntry getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntryDEntry)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("46DEC94E"));
    }
    public static com.kingdee.eas.custom.IWareClinicRaleEntryDEntry getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRaleEntryDEntry)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("46DEC94E"));
    }
}