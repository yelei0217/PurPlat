package com.kingdee.eas.custom.app;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.PurPlatSyncdbLogFactory;
import com.kingdee.eas.custom.PurPlatSyncdbLogInfo;

public class SyncDataEASFacadeControllerBean extends AbstractSyncDataEASFacadeControllerBean
{
    private static Logger logger =
        Logger.getLogger("com.kingdee.eas.custom.app.SyncDataEASFacadeControllerBean");

	

    /**
     * type   :  1:客户  2：供应商  3：组织  4 人员  5 仓库
     * newOrDele ： 1 新增 0 禁用
     */
    @Override
	protected void _syncDateByType(Context ctx, int type, String data,
			int newOrDele, String name, String number) throws BOSException {
		// TODO Auto-generated method stub
		//super._syncDateByType(ctx, type, data, newOrDele, name, number);

    	try {
    		Map<String, String> map = new  HashMap<String, String>();
        	map.put("FNUMBER", number);
        	map.put("FNAME", name);
        	map.put("JSON", data);
        	map.put("RESJSON", "");
    		if(type ==1){ 
    			getlogInfo(ctx , map ,  DateBasetype.Customer );
    		}else if(type ==2){
    			getlogInfo(ctx , map ,  DateBasetype.Supplier );
    		}else if(type ==3){
    			getlogInfo(ctx , map ,  DateBasetype.orgUnit );
    		}else if(type ==4){
    			getlogInfo(ctx , map ,  DateBasetype.Person );
    		}else if(type ==5){
    			getlogInfo(ctx , map ,  DateBasetype.FreeItem );
    		}
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
	}

	public PurPlatSyncdbLogInfo getlogInfo(Context ctx , Map<String, String> map,DateBasetype dateBasetype)
	throws BOSException, EASBizException {
		Calendar cal = Calendar.getInstance();
		PurPlatSyncdbLogInfo loginfo = new PurPlatSyncdbLogInfo();
		cal.setTime(new Date());
		loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("FNUMBER"));
		loginfo.setName(map.get("FNAME").toString());
		loginfo.setSimpleName(map.get("FNUMBER").toString());
		loginfo.setDateBaseType(dateBasetype);
		String version = String.valueOf(cal.getTimeInMillis());
		loginfo.setVersion(version);
		loginfo.setUpdateDate(new Date());
		loginfo.setMessage(map.get("JSON").toString());
		loginfo.setRespond(map.get("RESJSON").toString());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(loginfo);
		return loginfo;
	}



	
	
    
    
}