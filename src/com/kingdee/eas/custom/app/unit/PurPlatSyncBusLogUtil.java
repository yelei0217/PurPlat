package com.kingdee.eas.custom.app.unit;

import java.util.Calendar;
import java.util.Date;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.PurPlatSyncBusLogFactory;
import com.kingdee.eas.custom.PurPlatSyncBusLogInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.util.app.DbUtil;

public class PurPlatSyncBusLogUtil {
	
	
	
	/**
	 * 
	 * @param ctx
	 * @param processType
	 * @param baseType
	 * @param name
	 * @param number
	 * @param request
	 * @param respond
	 * @param errMsg
	 */
	public static IObjectPK insertLog(Context ctx, DateBaseProcessType processType,DateBasetype baseType,String name,String number,String request,String respond,String errMsg){
		IObjectPK logPk = null;
		try {
			PurPlatSyncBusLogInfo loginfo=new PurPlatSyncBusLogInfo();
			//Calendar cal=Calendar.getInstance();
			//cal.setTime(new Date());
			//String version= String.valueOf(cal.getTimeInMillis());
			loginfo.setProcessType(processType);
			loginfo.setNumber(number);
			loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(System.currentTimeMillis()+"");
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
 			loginfo.setMessage(request); // 请求信息
			loginfo.setRespond(respond);  //相应信息
			loginfo.setErrorMessage(errMsg); //异常信息
			logPk = PurPlatSyncBusLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		}
		return logPk;
	} 
	
	/**
	 * 
	 * @param ctx
	 * @param processType
	 * @param baseType
	 * @param name
	 * @param number
	 * @param request
	 * @param respond
	 * @param errMsg
	 */
	public static IObjectPK insertLog(Context ctx, DateBaseProcessType processType,DateBasetype baseType,String name,String number,String message){
		IObjectPK logPk = null;
		try {
			PurPlatSyncBusLogInfo loginfo=new PurPlatSyncBusLogInfo();
			loginfo.setProcessType(processType);
			loginfo.setNumber(number);
			loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(System.currentTimeMillis()+"");
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
 			loginfo.setMessage(message); // 请求信息 
 			loginfo.setIsSync(false);
			logPk = PurPlatSyncBusLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		}
		return logPk;
	} 
	
	
	/**
	 * 修改 生成单据
	 * @param fid
	 * @param respond
	 * @param errMsg
	 */
	public static void updateGenBillStatus(Context ctx,String fid,String status,String respond,String errMsg){
		if(fid !=null && !"".equals(fid) && status !=null && !"".equals(status) && respond !=null && !"".equals(respond) && errMsg !=null && !"".equals(errMsg)){
			try {
				String sql = " update CT_CUS_PurPlatSyncBusLog set CFStatus ="+status+", CFRespond = '"+respond+"' ,CFErrorMessage ='"+errMsg+"' where fid = '"+ fid + "' ";
				DbUtil.execute(ctx, sql);
			} catch (BOSException e) {
	 			e.printStackTrace();
			}
		}
		
	}
	
}
