package com.kingdee.eas.basedata.master.cssp.app;

import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.framework.Result;

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
		   
		if(true){
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 1 , "" , 0  , "" ,id.toString()); 
		}
		return id;
	}

	@Override
	protected void _freezed(Context ctx, IObjectPK customerPK,
			boolean isForceFreezed) throws BOSException, EASBizException {
		// TODO Auto-generated method stub  xxxxxxxxx
		super._freezed(ctx, customerPK, isForceFreezed);
		if(isForceFreezed){
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			is.syncDateByType( 1 ,  "" , 2 ,  "" ,customerPK.toString());
		}
	}

	     

	@Override
	public void approve(Context arg0, IObjectPK arg1) throws BOSException,
			EASBizException {
		// TODO Auto-generated method stub ed1
		super.approve(arg0, arg1); 
		ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(arg0);
		is.syncDateByType( 1 , "" , 1 , "" ,arg1.toString());
	}

	
	
	@Override
	protected void _approve(Context ctx, IObjectPK customerPK)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._approve(ctx, customerPK);
	}
 

	@Override
	public Map approve(Context arg0, IObjectPK[] arg1) throws BOSException,
			EASBizException {
		// TODO Auto-generated method stub1
		Map map = super.approve(arg0, arg1); 
		for(int i=0;i<arg1.length ;i++){ 
			
			CustomerInfo cusinfo= CustomerFactory.getLocalInstance(arg0).getCustomerInfo(arg1[i]);
			if(cusinfo.getUsedStatus().getValue()==1){
				ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(arg0);
				is.syncDateByType( 1 , "" , 1 , "" ,arg1[i].toString());
			} 
		}
		return map;
	}

	@Override
	public void unApprove(Context arg0, IObjectPK arg1, boolean arg2)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub ed1
		super.unApprove(arg0, arg1, arg2); 
		CustomerInfo cusinfo= CustomerFactory.getLocalInstance(arg0).getCustomerInfo(arg1);
		if(cusinfo.getUsedStatus().getValue()==0){
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(arg0);
			is.syncDateByType( 1 , "" , 2 , "" ,arg1.toString());
		}
	}

	@Override
	public Map unApprove(Context arg0, IObjectPK[] arg1) throws BOSException,
			EASBizException {
		// TODO Auto-generated method stub1
		Map map =  super.unApprove(arg0, arg1);
		for(int i=0;i<arg1.length ;i++){ 
			CustomerInfo cusinfo= CustomerFactory.getLocalInstance(arg0).getCustomerInfo(arg1[i]);
			if(cusinfo.getUsedStatus().getValue()==0){
				ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(arg0);
				is.syncDateByType( 1 , "" , 2 , "" ,arg1[i].toString());
			}
		}
			
		return map;
	} 
	
	
}
