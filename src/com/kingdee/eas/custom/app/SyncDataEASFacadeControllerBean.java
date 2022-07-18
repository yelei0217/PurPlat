package com.kingdee.eas.custom.app;

import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.PurPlatSyncdbLogFactory;
import com.kingdee.eas.custom.PurPlatSyncdbLogInfo;
import com.kingdee.jdbc.rowset.IRowSet;

public class SyncDataEASFacadeControllerBean extends AbstractSyncDataEASFacadeControllerBean{
	
	
    private static Logger logger =
        Logger.getLogger("com.kingdee.eas.custom.app.SyncDataEASFacadeControllerBean");

	

    /**
     * type   :  1:客户  2：供应商  3：组织  4 人员  5 仓库   
     * newOrDele ： 1 新增 0 禁用
     */
    @Override
	protected void _syncDateByType(Context ctx, int type, String data,
			int newOrDele, String name, String number) throws BOSException {
		// TODO Auto-generated method stub
		//super._syncDateByType(ctx, type, data, newOrDele, name, number);

    	try {
    		Map<String, String> map = new  HashMap<String, String>();
        	map.put("FNUMBER", number);
        	map.put("FNAME", name);
        	map.put("JSON", data);
        	map.put("RESJSON", "");
    		if(type ==1){ 
    			getlogInfo(ctx , map ,  DateBasetype.Customer );
    		}else if(type ==2){
    			getlogInfo(ctx , map ,  DateBasetype.Supplier );
    			/*SELECT supplier.fid  fId, supplier.fnumber fNumber, supplier.fname_l2  fName ,'' fOpenBank , '' fBankAccount  ,supplier.FCONTROLUNITID,
    			 cuser.fname_l2  fCreator ,  to_char( supplier.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( supplier.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime  
    			FROM   T_BD_Supplier supplier  
    			inner join  T_PM_User  cuser on cuser.fid= supplier.FCREATORID 
    			*/
    		}else if(type ==3){
    			getlogInfo(ctx , map ,  DateBasetype.orgUnit );
    		}else if(type ==4){
    			getlogInfo(ctx , map ,  DateBasetype.Person );
    			

    			/* SELECT    person.fid , person.fnumber, person.fname_l2 ,pmuser.FNUMBER  ,empposition.fid ,   
       		 empposition.fnumber  ,empposition.fname_l2   ,com.fid ,dep.fid,com.fnumber, com.fname_l2 ,job.fnumber ,
       		  emptype.fnumber , emptype.fname_l2, person.FCREATETIME  ,  0 fUpdateType ,person.fnumber  fEmpNumber
       		 ,dep.fnumber,dep.fname_l2
       		 
       		FROM  
       		t_bd_person person
       		left  join t_pm_user pmuser on pmuser.FPERSONID  = person.FID 
       		inner join t_hr_emporgrelation relation on person.fid = relation.fpersonid   and  relation.FAssignType= 1   and  to_char(relation.FLEFFDT , 'yyyy-hh-dd') ='2199-12-31'
       		left join T_ORG_Position empposition on empposition.fid = relation.FPositionID
       		left join  T_ORG_Admin  com on    com.fid =  relation.FCompanyID
       		left join  T_ORG_Admin dep on dep.fid =   relation.FAdminOrgID	
       		left join  T_ORG_Job  job on  job.fid = empposition.FJOBID 
       		left join T_HR_EmpLaborRelation emprel  on emprel.fid = relation.FLABORRELATIONID 
       		left join  T_HR_BDEmployeeType emptype  on  emptype.fid = emprel.FLABORRELATIONSTATEID 
       		where  person.fnumber = 'MS310100082'*/
    			
	    		String personsql = " /*dialect*/ SELECT    person.fid fId , person.fnumber fNumber, person.fname_l2 fName ,pmuser.FNUMBER  fLoginNumber ,empposition.fid fPositionID ,  "+
	          		" empposition.fnumber  fPositionNumber ,empposition.fname_l2 fPositionName  ,com.fid fOrgtid,dep.fid fDeptid,com.fnumber fOrgNumber, com.fname_l2 fOrgName ,job.fnumber fPostLeveNumber, "+
	          		"  emptype.fnumber fEmployeeTypeNumber, emptype.fname_l2 fEmployeeTypeName, to_char(person.FCREATETIME , 'yyyy-hh-dd')   fCreateTime,  0 fUpdateType ,person.fnumber  fEmpNumber "+
	          		" FROM   t_bd_person person "+
	          		" left  join t_pm_user pmuser on pmuser.FPERSONID  = person.FID "+
	          		" inner join t_hr_emporgrelation relation on person.fid = relation.fpersonid   and  relation.FAssignType= 1   and  to_char(relation.FLEFFDT , 'yyyy-hh-dd') ='2199-12-31'"+
	          		" left join T_ORG_Position empposition on empposition.fid = relation.FPositionID "+
	          		" left join  T_ORG_Admin  com on    com.fid =  relation.FCompanyID "+
	          		" left join  T_ORG_Admin dep on dep.fid =   relation.FAdminOrgID	 "+
	          		" left join  T_ORG_Job  job on  job.fid = empposition.FJOBID  "+
	          		" left join T_HR_EmpLaborRelation emprel  on emprel.fid = relation.FLABORRELATIONID  "+
	          		" left join  T_HR_BDEmployeeType emptype  on  emptype.fid = emprel.FLABORRELATIONSTATEID  "+
	          		" where  person.fnumber = 'MS310100082' ";
	    		IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,personsql);
				if(rs!=null && rs.size() > 0){
					  try {
						while(rs.next()){	 
							map.put("fId",rs.getString("FID") );
							map.put("fNumber",rs.getString("FNUMBER") );
							map.put("fName",rs.getString("FNAME") );
							map.put("fLoginNumber",rs.getString("FLOGINNUMBER") );
							map.put("fPositionID",rs.getString("FPOSITIONID") );
							map.put("fPositionNumber",rs.getString("FPOSITIONNUMBER") );
							map.put("fPositionName",rs.getString("FPOSITIONNAME") );
							map.put("fOrgtid",rs.getString("FORGTID") );
							map.put("fDeptid",rs.getString("FDEPTID") );
							map.put("fOrgNumber",rs.getString("FORGNUMBER") );
							map.put("fOrgName",rs.getString("FORGNAME") );
							map.put("fPostLeveNumber",rs.getString("FPOSTLEVENUMBER") );
							map.put("fEmployeeTypeNumber",rs.getString("FEMPLOYEETYPENUMBER") );
							map.put("fEmployeeTypeName",rs.getString("FEMPLOYEETYPENAME") ); 
							map.put("fCreateTime",rs.getString("FCREATETIME") ); 
							map.put("fUpdateType",rs.getString("FUPDATETYPE") ); 
							map.put("fEmpNumber",rs.getString("FEMPNUMBER") );  
						  }
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

    		}else if(type ==5){
    			getlogInfo(ctx , map ,  DateBasetype.FreeItem );
    		}
    		 
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
	}

	public PurPlatSyncdbLogInfo getlogInfo(Context ctx , Map<String, String> map,DateBasetype dateBasetype)
	throws BOSException, EASBizException {
		Calendar cal = Calendar.getInstance();
		PurPlatSyncdbLogInfo loginfo = new PurPlatSyncdbLogInfo();
		cal.setTime(new Date());
		loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("FNUMBER"));
		loginfo.setName(map.get("FNAME").toString());
		loginfo.setSimpleName(map.get("FNUMBER").toString());
		loginfo.setDateBaseType(dateBasetype);
		String version = String.valueOf(cal.getTimeInMillis());
		loginfo.setVersion(version);
		loginfo.setUpdateDate(new Date());
		loginfo.setMessage(map.get("JSON").toString());
		loginfo.setRespond(map.get("RESJSON").toString());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(loginfo);
		return loginfo;
	}



	
	
    
    
}