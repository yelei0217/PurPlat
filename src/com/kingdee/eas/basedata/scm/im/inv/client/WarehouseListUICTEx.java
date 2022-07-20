package com.kingdee.eas.basedata.scm.im.inv.client;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.dao.query.ISQLExecutor;
import com.kingdee.bos.dao.query.SQLExecutorFactory;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.jdbc.rowset.IRowSet;

public class WarehouseListUICTEx extends WarehouseListUI {

	public WarehouseListUICTEx() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5369278252855052237L;

	@Override
	public void actionCancel_actionPerformed(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.actionCancel_actionPerformed(e);
		ArrayList<String> ids = super.getSelectedIdValues();
		IWarehouse  iw = WarehouseFactory.getRemoteInstance();
		for(String id : ids){
			if(iw.exists("where id = '"+id+"' and  WhState=2 ")){
				HashMap<String, String> map = new HashMap<String, String>(); 
				String sql  = " /*dialect*/ select wah.fid fId , wah.fnumber fNumber, wah.fname_l2 fName ,admin.fid fOrgtid,admin.fnumber fOrgNumber, admin.fname_l2 fOrgName, "+
				  "	wah.FWhState fStatus ,cuser.fname_l2  fCreator ,  to_char( wah.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,  2 fUpdateType ,to_char( wah.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , "+
				  " from  T_DB_WAREHOUSE wah inner  join T_ORG_Storage admin on admin.fid = wah.FstorageOrgID "+
				  "	inner join  T_PM_User  cuser on cuser.fid=wah.FCREATORID  "+ 
				  " where wah.fid = '"+id.toString()+"' ";  
				
				ISQLExecutor executorOrg =   SQLExecutorFactory.getRemoteInstance(sql);
		        IRowSet rs = executorOrg.executeSQL();
		        if(rs!=null && rs.size() > 0){
		              while(rs.next()){    
		            	  String orgid = rs.getString("FORGTID");
							if( "jbYAAAMU2SvM567U".equals(orgid)){
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
							}else{
								map.put("fId",rs.getString("FID") );
								map.put("fNumber",rs.getString("FNUMBER") );
								map.put("fName",rs.getString("FNAME") );
								map.put("fOrgtid",rs.getString("FORGTID") );
								map.put("fOrgNumber",rs.getString("FORGNUMBER") );
								map.put("fOrgName",rs.getString("FORGNAME") );
							} 
		              }
		        }   
		        if(map.size() >0){
					String datajsonStr = JSONObject.toJSONString(map);
					ISyncDataEASFacade is = SyncDataEASFacadeFactory.getRemoteInstance();
					is.syncDateByType( 5 , datajsonStr , 0  , map.get("fName") ,map.get("fNumber"));
				}
			}
			
		}
		
	}

	
	
}
