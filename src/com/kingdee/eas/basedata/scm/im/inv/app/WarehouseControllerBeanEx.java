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
import com.sun.org.apache.bcel.internal.generic.F2D;

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
	protected IObjectPK _submit(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		Boolean isNew = false;
		if(model.get("ID") ==  null){
			isNew = true;
		}
		IObjectPK  id = super._submit(ctx, model);
		if(isNew){
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getRemoteInstance();
			is.syncDateByType( 5 , "" , 0  , "" ,id.toString());
//			HashMap<String, String> map = new HashMap<String, String>(); 
//			String sql  = " /*dialect*/ select wah.fid fId , wah.fnumber fNumber, wah.fname_l2 fName ,admin.fid fOrgtid,admin.fnumber fOrgNumber, admin.fname_l2 fOrgName, "+
//			  "	wah.FWhState fStatus ,cuser.fname_l2  fCreator ,  to_char( wah.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,  1 fUpdateType ,to_char( wah.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime   "+
//			  " from  T_DB_WAREHOUSE wah inner  join T_ORG_Storage admin on admin.fid = wah.FstorageOrgID "+
//			  "	inner join  T_PM_User  cuser on cuser.fid=wah.FCREATORID  "+ 
//			  " where wah.fid = '"+id.toString()+"' ";  
//			
//			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
// 
//			if(rs!=null && rs.size() > 0){
//				try {
//					while(rs.next()){	  
//						String orgid = rs.getString("FORGTID");
//						if( "jbYAAAMU2SvM567U".equals(orgid)){
//							map.put("fId",rs.getString("FID") );
//							map.put("fNumber",rs.getString("FNUMBER") );
//							map.put("fName",rs.getString("FNAME") );
//							map.put("fOrgtid",rs.getString("FORGTID") );
//							map.put("fOrgNumber",rs.getString("FORGNUMBER") );
//							map.put("fOrgName",rs.getString("FORGNAME") );
//							map.put("fStatus",rs.getString("FSTATUS") );
//							map.put("fCreator",rs.getString("FCREATOR") );
//							map.put("fCreateTime",rs.getString("FCREATETIME") );
//							map.put("fUpdateType",rs.getString("FUPDATETYPE") );
//							map.put("fUpdateTime",rs.getString("FUPDATETIME") ); 
//						}else{
//							map.put("fid",rs.getString("FID") );
//							map.put("fnumber",rs.getString("FNUMBER") );
//							map.put("fname",rs.getString("FNAME") );
//							map.put("forgId",rs.getString("FORGTID") );
//							map.put("forgNumber",rs.getString("FORGNUMBER") );
//							map.put("forgName",rs.getString("FORGNAME") ); 
//						}
//						  
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			 }  
//			if(map.size() >0){
//				String datajsonStr = JSONObject.toJSONString(map);
//				ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
//				is.syncDateByType( 5 , datajsonStr , 0 ,model.get("name").toString() ,model.get("number").toString() );
//			}
		}
		return id;
	}
 
	
	

}
