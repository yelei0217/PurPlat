package com.kingdee.eas.basedata.master.cssp.app;

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

public class CustomerControllerBeanEx extends CustomerControllerBean{
	
	@Override
	protected Result _submit(Context ctx, IObjectCollection colls)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		return super._submit(ctx, colls);
	}

	@Override
	protected void _submit(Context ctx, IObjectPK dataBaseDPK,
			IObjectValue model) throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._submit(ctx, dataBaseDPK, model);
	}

	/*select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , 
	to_char( cus.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , 0 fIsGroup,
	com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , 0  fStatus , 0 fUpdateType
	from T_BD_Customer  cus  
	inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid
	inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID 
	inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fnumber = 'jbYAAAMU2SvM567U'
	where cus.fid ='jbYAAAMvky2/DAQO' and cus.fisInternalCompany = 1 */
	@Override
	protected IObjectPK _submit(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stubqqqqqqqq
		Boolean isNew = false;
		if(model.get("ID") ==  null){
			isNew = true;
		}
		IObjectPK id = super._submit(ctx, model);    
		   
		if(isNew){
			HashMap<String, String> map = new HashMap<String, String>(); 
			String sql  = " /*dialect*/ select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , "+
			 "  to_char( cus.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , 0 fIsGroup, "+
			 "  com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , 0  fStatus , 0 fUpdateType "+
			 "  from T_BD_Customer  cus   "+
			 "  inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid "+
			 "  inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID  "+
			 "  inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fnumber = 'jbYAAAMU2SvM567U' "+
			 "  where cus.fid ='"+id.toString()+"' and cus.fisInternalCompany = 1 ";
			
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			if(rs!=null && rs.size() > 0){
				  try {
					while(rs.next()){	 
						map.put("fId",rs.getString("FID") );
						map.put("fNumber",rs.getString("FNUMBER") );
						map.put("fName",rs.getString("FNAME") );
						map.put("fOpenBank",rs.getString("FOPENBANK") );
						map.put("fBankAccount",rs.getString("FBANKACCOUNT") );
						map.put("fCreator",rs.getString("FCREATOR") );
						map.put("fCreateTime",rs.getString("FCREATETIME") );
						map.put("fUpdateTime",rs.getString("FUPDATETIME") );
						map.put("fIsGroup",rs.getString("FISGROUP") );
						map.put("fOrgtId",rs.getString("FORGTID") );
						map.put("fOrgNumber",rs.getString("FORGNUMBER") );
						map.put("fOrgName",rs.getString("FORGNAME") );
						map.put("fStatus",rs.getString("FSTATUS") );
						map.put("fUpdateType",rs.getString("FUPDATETYPE") ); 
					  }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }  
		}
		return id;
	}

	@Override
	protected void _freezed(Context ctx, IObjectPK customerPK,
			boolean isForceFreezed) throws BOSException, EASBizException {
		// TODO Auto-generated method stub  xxxxxxxxx
		super._freezed(ctx, customerPK, isForceFreezed);
		if(isForceFreezed){
			HashMap<String, String> map = new HashMap<String, String>(); 
			String sql  = " /*dialect*/ select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , "+
			 "  to_char( cus.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , 0 fIsGroup, "+
			 "  com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , 1  fStatus , 2 fUpdateType "+
			 "  from T_BD_Customer  cus   "+
			 "  inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid "+
			 "  inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID  "+
			 "  inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fnumber = 'jbYAAAMU2SvM567U' "+
			 "  where cus.fid ='"+customerPK.toString()+"' and cus.fisInternalCompany = 1 ";
			
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			if(rs!=null && rs.size() > 0){
				  try {
					while(rs.next()){	
						map.put("fId",rs.getString("FID") );
						map.put("fNumber",rs.getString("FNUMBER") );
						map.put("fName",rs.getString("FNAME") );
						map.put("fOpenBank",rs.getString("FOPENBANK") );
						map.put("fBankAccount",rs.getString("FBANKACCOUNT") );
						map.put("fCreator",rs.getString("FCREATOR") );
						map.put("fCreateTime",rs.getString("FCREATETIME") );
						map.put("fUpdateTime",rs.getString("FUPDATETIME") );
						map.put("fIsGroup",rs.getString("FISGROUP") );
						map.put("fOrgtId",rs.getString("FORGTID") );
						map.put("fOrgNumber",rs.getString("FORGNUMBER") );
						map.put("fOrgName",rs.getString("FORGNAME") );
						map.put("fStatus",rs.getString("FSTATUS") );
						map.put("fUpdateType",rs.getString("FUPDATETYPE") ); 
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }  
		}
	}

	@Override
	protected void _unFreezed(Context ctx, IObjectPK customerPK,
			boolean isForceUnFreezed) throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._unFreezed(ctx, customerPK, isForceUnFreezed);
	}
 
	
	
}
