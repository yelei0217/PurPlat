package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class WareClinicRaleFactory
{
    private WareClinicRaleFactory()
    {
    }
    public static com.kingdee.eas.custom.IWareClinicRale getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRale)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("DCFD14D2") ,com.kingdee.eas.custom.IWareClinicRale.class);
    }
    
    public static com.kingdee.eas.custom.IWareClinicRale getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRale)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("DCFD14D2") ,com.kingdee.eas.custom.IWareClinicRale.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IWareClinicRale getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRale)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("DCFD14D2"));
    }
    public static com.kingdee.eas.custom.IWareClinicRale getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IWareClinicRale)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("DCFD14D2"));
    }
}