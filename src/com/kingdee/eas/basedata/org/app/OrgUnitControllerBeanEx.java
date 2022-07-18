package com.kingdee.eas.basedata.org.app;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.framework.Result;

public class OrgUnitControllerBeanEx extends OrgUnitControllerBean{

	@Override
	protected Result _submit(Context ctx, IObjectCollection colls)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		return super._submit(ctx, colls);
	}

	/*
  SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID ,
  admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit £¬admin.fisstart  fIsStart ,
  admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp , nvl(admin.FTaxNumber,'') fTaxNumber ,  nvl( admin.FADMINADDRESS_L2,'')  fAddress  , 
  nvl(person.fname_l2,'') fJuridicalPerson, nvl(admin.FRegisteredCapital,'') fRegisteredCapital  , nvl(to_char(admin.FSetupDate,'yyyy-hh-mm'),'') fSetupDate
  FROM T_ORG_admin admin
  inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID 
  left join  t_bd_person  person  on person.fid = admin.FJuridicalPersonID
  where admin.fid = 'jbYAAAJA7YnM567U'*/
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
		return super._submit(ctx, model);
	}

	@Override
	protected void _cancel(Context ctx, IObjectPK pk, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._cancel(ctx, pk, model);
	}

	@Override
	protected void _cancelCancel(Context ctx, IObjectPK pk, IObjectValue model)
			throws BOSException, EASBizException {
		// TODO Auto-generated method stub
		super._cancelCancel(ctx, pk, model);
	}
	
}
