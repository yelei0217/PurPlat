package com.kingdee.eas.basedata.master.cssp.app;

import java.sql.SQLException;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.basedata.master.cssp.CustomerCompanyInfoInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.framework.Result;
import com.kingdee.jdbc.rowset.IRowSet;

public class CustomerCompanyInfoControllerBeanEx extends CustomerCompanyInfoControllerBean{

	 

	@Override
	protected void _cancel(Context ctx, IObjectPK[] pks) throws BOSException,
			EASBizException {
		// TODO Auto-generated method stub1
		super._cancel(ctx, pks);
		for(int i=0;i<pks.length ;i++){ 
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 1 , "" , 2 , "" ,pks[i].toString());
		}
	}
  
	@Override
	protected void _cancelCancel(Context ctx, IObjectPK[] pks)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub1  ÆôÓÃ
		super._cancelCancel(ctx, pks);
		for(int i=0;i<pks.length ;i++){ 
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 1 , "" , 1 , "" ,pks[i].toString());
		} 
	}
 

	@Override
	protected IObjectPK _submit(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stubqqqqqqqq
		Boolean isNew = false;
		if(model.get("ID") ==  null){
			isNew = true;
		}
		IObjectPK id = super._submit(ctx, model);    
		
		if(true){ 
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 1 , "" , 1  , "",( (com.kingdee.eas.basedata.master.cssp.CustomerInfo)model.get("customer") ).getId().toString());
//			HashMap<String, String> map = new HashMap<String, String>(); 
//			String sql  = " /*dialect*/ select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , "+
//			 "  to_char( cus.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime , 0 fIsGroup, "+
//			 "  com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , 0  fStatus , 0 fUpdateType "+
//			 "  from T_BD_Customer  cus   "+
//			 "  inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid "+
//			 "  inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID  "+
//			 "  inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fid = 'jbYAAAMU2SvM567U' "+
//			 "  where cuscom.fid ='"+id.toString()+"'  ";
//			
//			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
//			if(rs!=null && rs.size() > 0){
//				  try {
//					while(rs.next()){	 
//						map.put("fId",rs.getString("FID") );
//						map.put("fNumber",rs.getString("FNUMBER") );
//						map.put("fName",rs.getString("FNAME") );
//						map.put("fOpenBank",rs.getString("FOPENBANK") );
//						map.put("fBankAccount",rs.getString("FBANKACCOUNT") );
//						map.put("fCreator",rs.getString("FCREATOR") );
//						map.put("fCreateTime",rs.getString("FCREATETIME") );
//						map.put("fUpdateTime",rs.getString("FUPDATETIME") );
//						map.put("fIsGroup",rs.getString("FISGROUP") );
//						map.put("fOrgtId",rs.getString("FORGTID") );
//						map.put("fOrgNumber",rs.getString("FORGNUMBER") );
//						map.put("fOrgName",rs.getString("FORGNAME") );
//						map.put("fStatus",rs.getString("FSTATUS") );
//						map.put("fUpdateType",rs.getString("FUPDATETYPE") ); 
//					  }
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			 }  
//			if(map.size() >0){
//				
//			}
			
		}
		return id;
	}
 
	
}
