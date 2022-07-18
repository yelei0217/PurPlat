package com.kingdee.eas.basedata.scm.im.inv.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.CanNotDeleteException;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.framework.Result;
import com.kingdee.jdbc.rowset.IRowSet;

public class WarehouseControllerBeanEx extends WarehouseControllerBean{

	@Override
	protected Result _submit(Context ctx, IObjectCollection colls)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		return super._submit(ctx, colls);
	}

	@Override
	protected void _submit(Context ctx, IObjectPK pk, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._submit(ctx, pk, model);
	}

	@Override
	protected IObjectPK _submit(Context arg0, IObjectValue arg1)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		Boolean isNew = false;
		if(arg1.get("ID") ==  null){
			isNew = true;
		}
		IObjectPK  id = super._submit(arg0, arg1);
		if(isNew){
			HashMap<String, String> map = new HashMap<String, String>(); 
			String sql  = " /*dialect*/ select wah.fid fId , wah.fnumber fNumber, wah.fname_l2 fName ,admin.fid fOrgtid,admin.fnumber fOrgNumber, admin.fname_l2 fOrgName, "+
			  "	wah.FWhState fStatus ,cuser.fname_l2  fCreator ,  to_char( wah.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,  1 fUpdateType ,to_char( wah.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , "+
			  " from  T_DB_WAREHOUSE wah inner  join T_ORG_Storage admin on admin.fid = wah.FstorageOrgID "+
			  "	inner join  T_PM_User  cuser on cuser.fid=wah.FCREATORID  "+ 
			  " where wah.fid = '"+id.toString()+"' ";  
			
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(arg0,sql);
			if(rs!=null && rs.size() > 0){
				try {
					while(rs.next()){	  
						map.put("fId",rs.getString("FID") );
						map.put("fNumber",rs.getString("FNUMBER") );
						map.put("fName",rs.getString("FNAME") );
						map.put("fOrgtid",rs.getString("FORGTID") );
						map.put("fOrgNumber",rs.getString("FORGNUMBER") );
						map.put("fOrgName",rs.getString("FORGNAME") );
						map.put("fStatus",rs.getString("FSTATUS") );
						map.put("fCreator",rs.getString("FCREATOR") );
						map.put("fCreateTime",rs.getString("FCREATETIME") );
						map.put("fUpdateType",rs.getString("FUPDATETYPE") );
						map.put("fUpdateTime",rs.getString("FUPDATETIME") ); 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }  
			if(map.size() >0){
				String datajsonStr = JSONObject.toJSONString(map);
				ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(arg0);
				is.syncDateByType( 5 , datajsonStr , 1  , map.get("fName") ,map.get("fNumber") );
			}
		}
		return id;
	}
 
	
	

}
