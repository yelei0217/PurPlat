package com.kingdee.eas.basedata.org.client;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.dao.query.ISQLExecutor;
import com.kingdee.bos.dao.query.SQLExecutorFactory;
import com.kingdee.eas.basedata.org.FullOrgUnitFactory;
import com.kingdee.eas.basedata.org.IFullOrgUnit;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.jdbc.rowset.IRowSet;

public class OUEditUICTEx extends OUEditUI{

	public OUEditUICTEx() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1270665879627285569L;

	@Override
	public void actionBizSealUp_actionPerformed(ActionEvent arg0)
			throws Exception {
		// TODO Auto-generated method stub
		super.actionBizSealUp_actionPerformed(arg0);
		if (this.editData.getId() != null) {
			String unitID = this.editData.getId().toString();
			IFullOrgUnit iaf = FullOrgUnitFactory.getRemoteInstance();
			if(iaf.exists(" where  id = '"+unitID+"'  and  IsOUSealUp  = 1 ")){
				HashMap<String, String> map = new HashMap<String, String>(); 
				String sql  = " /*dialect*/ SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID , "+
				  " admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit £¬admin.fisstart  fIsStart , "+
				  " admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp , nvl(admin.FTaxNumber,'') fTaxNumber ,  nvl( admin.FADMINADDRESS_L2,'')  fAddress  , "+
				  " nvl(person.fname_l2,'') fJuridicalPerson, nvl(admin.FRegisteredCapital,'') fRegisteredCapital  , nvl(to_char(admin.FSetupDate,'yyyy-hh-dd'),'') fSetupDate"+
				  " FROM T_ORG_admin admin "+
				  " inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID  "+
				  " left join  t_bd_person  person  on person.fid = admin.FJuridicalPersonID "+ 
				  " where admin.fid = '"+unitID.toString()+"' ";  
				 
				ISQLExecutor executorOrg =   SQLExecutorFactory.getRemoteInstance(sql);
		        IRowSet rs = executorOrg.executeSQL();
		        if(rs!=null && rs.size() > 0){
		              while(rs.next()){    
		            	    map.put("fId",rs.getString("FID") );
							map.put("fNumber",rs.getString("FNUMBER") );
							map.put("fName",rs.getString("FNAME") );
							map.put("fLongNumber",rs.getString("FLONGNUMBER") );
							map.put("fLayerTypeID",rs.getString("FLAYERTYPEID") );
							map.put("fIsCompanyOrgUnit",rs.getString("FISCOMPANYORGUNIT") );
							map.put("fIsAdminOrgUnit",rs.getString("FISADMINORGUNIT") );
							map.put("fIsCostOrgUnit",rs.getString("FISCOSTORGUNIT") );
							map.put("fIsStart",rs.getString("FISSTART") );
							map.put("fLevel",rs.getString("FLEVEL") );
							map.put("fIsLeaf",rs.getString("FISLEAF") );
							map.put("fIsOUSealUp",rs.getString("FISOUSEALUP") );
							map.put("fTaxNumber",rs.getString("FTAXNUMBER") );
							map.put("fAddress",rs.getString("FADDRESS") ); 
							map.put("fJuridicalPerson",rs.getString("FJURIDICALPERSON") ); 
							map.put("fUpdateType",rs.getString("FREGISTEREDCAPITAL") ); 
							map.put("fSetupDate",rs.getString("FSETUPDATE") ); 
		              }
		        } 
		        if(map.size() >0){
					String datajsonStr = JSONObject.toJSONString(map);
					ISyncDataEASFacade is = SyncDataEASFacadeFactory.getRemoteInstance();
					is.syncDateByType( 3 , datajsonStr , 0  , map.get("fName") ,map.get("fNumber"));
				}
			}
		}
		
	}
	
	

}
