package com.kingdee.eas.basedata.org.app;

import java.sql.SQLException;
import java.util.HashMap;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.framework.Result;
import com.kingdee.jdbc.rowset.IRowSet;

public class FullOrgUnitControllerBeanEx extends FullOrgUnitControllerBean{

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


	  /*SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID ,
	  admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit £¬admin.fisstart  fIsStart ,
	  admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp , nvl(admin.FTaxNumber,'') fTaxNumber ,  nvl( admin.FADMINADDRESS_L2,'')  fAddress  , 
	  nvl(person.fname_l2,'') fJuridicalPerson, nvl(admin.FRegisteredCapital,'') fRegisteredCapital  , nvl(to_char(admin.FSetupDate,'yyyy-hh-mm'),'') fSetupDate
	  FROM T_ORG_admin admin
	  inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID 
	  left join  t_bd_person  person  on person.fid = admin.FJuridicalPersonID
	  where admin.fid = 'jbYAAAJA7YnM567U' */
	@Override
	protected IObjectPK _submit(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stubsssss
		Boolean isNew = false;
		if(model.get("ID") ==  null){
			isNew = true;
		}
		IObjectPK id  = super._submit(ctx, model);
		if(isNew){
			HashMap<String, String> map = new HashMap<String, String>(); 
			String sql  = " /*dialect*/ SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID , "+
			  " admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit £¬admin.fisstart  fIsStart , "+
			  " admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp , nvl(admin.FTaxNumber,'') fTaxNumber ,  nvl( admin.FADMINADDRESS_L2,'')  fAddress  , "+
			  " nvl(person.fname_l2,'') fJuridicalPerson, nvl(admin.FRegisteredCapital,'') fRegisteredCapital  , nvl(to_char(admin.FSetupDate,'yyyy-hh-mm'),'') fSetupDate"+
			  " FROM T_ORG_admin admin "+
			  " inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID  "+
			  " left join  t_bd_person  person  on person.fid = admin.FJuridicalPersonID "+ 
			  " where admin.fid = 'jbYAAAJA7YnM567U' ";  
			
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			if(rs!=null && rs.size() > 0){
				  try {
					while(rs.next()){	 
						map.put("fId",rs.getString("FID") );
						map.put("fNumber",rs.getString("FNUMBER") );
						map.put("fName",rs.getString("FNAME") );
						map.put("fLongNumber",rs.getString("FLONGNUMBER") );
						map.put("fLayerTypeID",rs.getString("FBANKACCOUNT") );
						map.put("fIsCompanyOrgUnit",rs.getString("FISCOMPANYORGUNIT") );
						map.put("fIsAdminOrgUnit",rs.getString("FISADMINORGUNIT") );
						map.put("fIsCostOrgUnit",rs.getString("FUPDATETIME") );
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
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }  
		}
		return id;
	}

}
