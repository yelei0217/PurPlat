package com.kingdee.eas.custom.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.basedata.assistant.KAClassficationFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.PurPlatSyncdbLogCollection;
import com.kingdee.eas.custom.PurPlatSyncdbLogFactory;
import com.kingdee.eas.custom.PurPlatSyncdbLogInfo;
import com.kingdee.eas.custom.app.dto.SOrgDTO;
import com.kingdee.eas.custom.app.dto.WareDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.unit.MaterialUntil;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.jdbc.rowset.IRowSetMetaData;

public class SyncDataEASFacadeControllerBean extends AbstractSyncDataEASFacadeControllerBean{
	
    private static Logger logger =
        Logger.getLogger("com.kingdee.eas.custom.app.SyncDataEASFacadeControllerBean");


    String dataBase = "04";
    private static String warurl = "http://sr.wellekq.com:10090/his-war/notify/receiveClinicStore"; //测试地址

    private static String warJinYongurl = "http://sr.wellekq.com:10090/his-war/notify/updateClinicStoreStatus"; //测试地址
    
    /**
     * type   :  1:客户  2：供应商  3：组织  4 人员  5 仓库   
     * newOrDele : 0:新增 ; 1启用 ; 2:禁用
     */
    
    protected boolean syncDate(Context ctx, int type, String data,
			int newOrDele, String name, String fid ,PurPlatSyncdbLogInfo loginfo) throws BOSException {
    	boolean flag = false;
    	
    	
    	Map<String, Object> mapEAS = new  HashMap<String, Object>();
    	mapEAS.put("msgId",fid);
    	if(type==3){
    		mapEAS.put("baseType",1);//组织
    	}else if(type==2){
    		mapEAS.put("baseType", 2 );//供应商
    	}else if(type==1){
    		mapEAS.put("baseType", 3 );//客户
    	}else if(type==4){
    		mapEAS.put("baseType", 4 );//员工
    	}else if(type==5){
    		mapEAS.put("baseType", 5 );//仓库
    	} 
    	
    	Date newDate = new Date();
    	mapEAS.put("reqTime",newDate.getTime()+"");
    	mapEAS.put("operType","0");
    	Map<String, String> map = new  HashMap<String, String>();
    	DateBaseProcessType processType = null;
    	if(newOrDele ==0 ){
    		processType = DateBaseProcessType.AddNew;
    		mapEAS.put("operType",0);
    	}else if(newOrDele ==1 ){
    		processType = DateBaseProcessType.ENABLE;
    		mapEAS.put("operType",1);
    	}else if(newOrDele ==2 ){
    		processType = DateBaseProcessType.DisAble;
    		mapEAS.put("operType",2);
    	}
    	try {
    		
        	 
        	String retMsg = "";
        	map.put("RESJSON", "");
        	map.put("FID", fid);
        	map.put("NEWORDEL", newOrDele+"");
    		if(type ==1){ //客户  newOrDele : 0:新增 ; 1启用 ; 2:禁用
 
    			int fStatus = 0;
    			int fUpdateType = 0;
    			if(newOrDele == 1){
    				fStatus = 0;
    				fUpdateType = 0;
    			}else if(newOrDele == 2){
    				fStatus = 1;
    				fUpdateType = 2;
    			}
    			String selectSuppSql = " /*dialect*/ select  FNUMBER from  EAS_Customer_MIDTABLE where fid='"+fid+"' ";
				List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
				if(retsSup.size() == 0 ){//没有 
					
				}else{
					String selectDelete = " delete    EAS_Customer_MIDTABLE where fid='"+fid+"' ";
					EAISynTemplate.execute(ctx, dataBase, selectDelete);
				}
				String sql  = " /*dialect*/ select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , "+
				 "  to_char( cus.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd hh24:mi:ss' ) fUpdateTime , 0 fIsGroup, "+
				 "  com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , "+fStatus+"  fStatus , "+fUpdateType+" fUpdateType ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime "+
				 "  from T_BD_Customer  cus   "+
				 "  inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid "+
				 "  inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID  "+
				 "  inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fid = 'jbYAAAMU2SvM567U' "+
				 "  where cus.fid ='"+fid+"'   ";
				
				
				IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				IRowSet  rsCopy=  rs.createCopy();
				if(rs!=null && rs.size() > 0){
					String sqlInsert = insertMidTable(ctx,  "EAS_Customer_MIDTABLE", rs ,"fSign");
					map.put("ERROR",sqlInsert);
					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
					
					if(rsCopy.next()){	
						Map<String, String> mapTo = new  HashMap<String, String>();
						mapTo.put("fId",rsCopy.getString("FID") );
						mapTo.put("fNumber",rsCopy.getString("FNUMBER") );
						mapTo.put("fName",rsCopy.getString("FNAME") );
						mapTo.put("fOpenBank",rsCopy.getString("FOPENBANK") );
						mapTo.put("fBankAccount",rsCopy.getString("FBANKACCOUNT") );
						mapTo.put("fCreator",rsCopy.getString("FCREATOR") );
						mapTo.put("fCreateTime",rsCopy.getString("FCREATETIME") );
						mapTo.put("fUpdateTime",rsCopy.getString("FUPDATETIME") );
						mapTo.put("fIsGroup",rsCopy.getString("FISGROUP") );
						mapTo.put("fOrgtId",rsCopy.getString("FORGTID") );
						mapTo.put("fOrgNumber",rsCopy.getString("FORGNUMBER") );
						mapTo.put("fOrgName",rsCopy.getString("FORGNAME") );
						mapTo.put("fStatus",rsCopy.getString("FSTATUS") );
						mapTo.put("fUpdateType",rsCopy.getString("FUPDATETYPE") );  
						 
						mapEAS.put("data",mapTo);
						String datajsonStr = JSONObject.toJSONString(mapEAS);
						
						map.put("FNUMBER", mapTo.get("fNumber"));
			        	map.put("FNAME", mapTo.get("fName"));
			        	map.put("JSON", datajsonStr);
			        	 
			        	
			        	String  result = sendBaseDataToB2B(ctx ,datajsonStr);
			        	logger.info("发送客户通知给B2B系统，result：" + result);  
			        	Map<String, String> mapRet = (Map) JSONObject.parse(result);  
			        	if(mapRet.get("code") != null && "200".equals(String.valueOf(mapRet.get("code")))){
			        		flag=true;
			        	}  
			        	map.put("RESJSON", result);
					}
					
				}else{
					 map.put("ERROR", "根据ID"+fid+"在jbYAAAMU2SvM567U公司下找不到对应的客户信息,没有同步到中间库。");
					 System.out.println("########  ERROR ########根据ID："+fid+"在jbYAAAMU2SvM567U公司下找不到对应的客户信息,没有同步到中间库。");
				}   
				 
				getlogInfo(ctx , map ,  DateBasetype.Customer ,processType ,flag ,loginfo);  
    			
    			
    		}else if(type ==2){ //供应商  newOrDele : 0:新增 ; 1启用 ; 2:禁用
 
    			

				/*SELECT supp.fid  fid , supp.fnumber fnumber ,supp.fname_l2 Fname ,  gro.fname_l2 FCLASSNAME ,'' FOpenBank ,  '' FBankAccount,
   			 supp.FCREATORID  Fcreator , supp.FCREATETIME  FcreateTime , supp.FLASTUPDATETIME FupdateTime,  supp.FIsInternalCompany  FISGroup ,
   			 admin.FNUMBER  ForgNumber ,admin.FName_l2 ForgName , (case when supp.FUsedStatus = 1 then 0 else  1 end ) FStatus ,admin.Fid Forgtid, 0  FupdateType, sysdate FsynTime
   			 FROM  T_BD_Supplier  supp 
   			inner  join  T_BD_CSSPGroup gro on gro.fid =supp.FBrowseGroupID
   			inner join  T_ORG_admin    admin  on admin.fid = supp.FCONTROLUNITID  */
   			
				String sql  = " /*dialect*/ SELECT supp.fid  fid , supp.fnumber fnumber ,supp.fname_l2 Fname ,  gro.fname_l2 FCLASSNAME ,'' FOpenBank ,  '' FBankAccount, "+
				 "  supp.FCREATORID  Fcreator , to_char( supp.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' )   FcreateTime , to_char( supp.FLASTUPDATETIME ,'yyyy-mm-dd hh24:mi:ss' )  FupdateTime,  supp.FIsInternalCompany  FISGroup , "+
				 "  admin.FNUMBER  ForgNumber ,admin.FName_l2 ForgName , (case when supp.FUsedStatus = 1 then 0 else  1 end ) FStatus ,admin.Fid Forgtid, "+newOrDele+"  FupdateType ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime "+
				 "   FROM  T_BD_Supplier  supp    "+
				 "  inner  join  T_BD_CSSPGroup gro on gro.fid =supp.FBrowseGroupID "+
				 " inner join  T_ORG_admin    admin  on admin.fid = supp.FCONTROLUNITID "+
				 " inner join  T_BD_SupplierCompanyInfo supcom  on supcom.FSUPPLIERID  = supp.fid and supcom.FComOrgID  = 'jbYAAAMU2SvM567U'"+
				 "    where supp.fid ='"+fid+"'   "; 
				 
				CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='jbYAAAMU2SvM567U'");
			  		  
				IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				IRowSet  rsCopy=  rs.createCopy();
				if(rs!=null && rs.size() > 0){
					
					String selectSuppSql = " /*dialect*/ select  FNUMBER from  EAS_Supplier_MIDTABLE where fid='"+fid+"' ";
					List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
					if(retsSup.size() == 0 ){//没有 
						
					}else{
						String selectDelete = " delete    EAS_Supplier_MIDTABLE where fid='"+fid+"' ";
						EAISynTemplate.execute(ctx, dataBase, selectDelete);
					}
					
					
					String sqlInsert = insertMidTable(ctx,  "EAS_Supplier_MIDTABLE", rs ,"fSign");
					map.put("ERROR",sqlInsert);
					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
					
					
					String bankStr = "";
					String bankAccountStr = "";
					String bankSql = " select  supbank.FBank BANK , supbank.FBankAccount BANKACCOUNT  from  T_BD_SupplierCompanyBank   supbank "+
					" inner  join  T_BD_SupplierCompanyInfo supcom  on   supcom.FSUPPLIERID ='"+fid+"'   and  supcom.FComOrgID  = 'jbYAAAMU2SvM567U'  and  supcom.fid = supbank.FSupplierCompanyInfoID";
					IRowSet  rsBk = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,bankSql);
					while(rsBk.next()){	
						bankStr = bankStr+ rsBk.getString("BANK");
						bankAccountStr = bankAccountStr + rsBk.getString("BANKACCOUNT");
					}
					if(bankStr.length() >0){
						bankStr = bankStr.substring(0, bankStr.length()-1);
					}
					if(bankAccountStr.length() >0){
						bankAccountStr = bankAccountStr.substring(0, bankAccountStr.length()-1);
					}
					
					if(rsCopy.next()){	
						Map<String, String> mapTo = new  HashMap<String, String>();
						mapTo.put("fId",rsCopy.getString("FID") );
						mapTo.put("fNumber",rsCopy.getString("FNUMBER") );
						mapTo.put("fName",rsCopy.getString("FNAME") );
						 
						mapTo.put("fClassName",rsCopy.getString("FCLASSNAME") );
						
						mapTo.put("fOpenBank",bankStr );
						mapTo.put("fBankAccount",bankAccountStr );
						mapTo.put("fCreator",rsCopy.getString("FCREATOR") );
						mapTo.put("fCreateTime",rsCopy.getString("FCREATETIME") );
						mapTo.put("fUpdateTime",rsCopy.getString("FUPDATETIME") );
						mapTo.put("fIsGroup",rsCopy.getString("FISGROUP") );
						mapTo.put("fOrgtId",companyInfo.getId().toString() );
						mapTo.put("fOrgNumber",companyInfo.getNumber() );
						mapTo.put("fOrgName",companyInfo.getName() );
						mapTo.put("fStatus",rsCopy.getString("FSTATUS") );
						mapTo.put("fUpdateType",rsCopy.getString("FUPDATETYPE") ); 

						mapEAS.put("data",mapTo);
						String datajsonStr = JSONObject.toJSONString(mapEAS);
						 
						map.put("FNUMBER", mapTo.get("fNumber"));
			        	map.put("FNAME", mapTo.get("fName"));
			        	map.put("JSON", datajsonStr);
			        	
			        	String  result = sendBaseDataToB2B(ctx ,datajsonStr);
			        	 
			        	logger.info("发送供应商通知给B2B系统，result：" + result);  
			        	Map<String, String> mapRet = (Map) JSONObject.parse(result);  
			        	if(mapRet !=null && mapRet.get("code") != null && "200".equals(String.valueOf(mapRet.get("code")))){
			        		flag=true;
			        	} 
			        	map.put("RESJSON", result);
			        	 
					} 
				}else{
					 map.put("ERROR", "根据ID"+fid+"找不到对应的供应商信息,没有同步到中间库。");
					 System.out.println("########  ERROR ########根据ID："+fid+"找不到对应的供应商信息,没有同步到中间库");
				} 
					 
				getlogInfo(ctx , map ,  DateBasetype.Supplier ,processType,flag,loginfo);
    			
    			
				
    			  
    		}else if(type ==3){//组织  newOrDele : 0:新增 ; 1启用 ; 2:禁用
    		  
    			int fStatus = 0;
    			int fUpdateType = 0;
    			if(newOrDele == 1){
    				fStatus = 0;
    				fUpdateType = 1;
    			}else if(newOrDele == 2){
    				fStatus = 1;
    				fUpdateType = 2;
    			}
    			
    			
    			String selectSuppSql = " /*dialect*/ select  fid from  EAS_ORG_MIDTABLE where fid='"+fid+"' ";
				List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
				if(retsSup.size() == 0 ){//没有 
					
				}else{
					String selectDelete = " delete    EAS_ORG_MIDTABLE where fid='"+fid+"' ";
					EAISynTemplate.execute(ctx, dataBase, selectDelete);
				}
				
    			String sql  = " /*dialect*/ SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID , admin.fparentid  FPARENTID , "+
	  			  " admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit ,admin.fisstart  fIsStart ,  to_char( admin.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' ) FcreateTime ,"+
	  			  " admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp ,  "+newOrDele+"  FupdateType ,"+
	  			  "   to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime_0 "+
	  			  " FROM T_ORG_ADMIN admin "+
	  			  " inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID  "+ 
	  			  " where admin.fid = '"+fid+"' ";  
	  			
	  			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql); 
	  			IRowSet  rsCopy=  rs.createCopy();
				if(rs!=null && rs.size() > 0){
					String sqlInsert = insertMidTable(ctx,  "EAS_ORG_MIDTABLE", rs,"fSign_0");
					map.put("ERROR",sqlInsert);
					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
					
					
					if(rsCopy.next()){	
						Map<String, String> mapTo = new  HashMap<String, String>();
						mapTo.put("fid",rsCopy.getString("FID") );
						mapTo.put("fnumber",rsCopy.getString("FNUMBER") );
						mapTo.put("Fname",rsCopy.getString("FNAME") );
						 
						mapTo.put("Flongnumber",rsCopy.getString("FLONGNUMBER") );
						
						mapTo.put("FLayerTypeID",rsCopy.getString("FLAYERTYPEID") );
						mapTo.put("FPARENTID",rsCopy.getString("FPARENTID") );
						mapTo.put("FIsCompanyOrgUnit",rsCopy.getString("FISCOMPANYORGUNIT") );
						mapTo.put("FIsAdminOrgUnit",rsCopy.getString("FISADMINORGUNIT") );
						mapTo.put("fIsCostOrgUnit",rsCopy.getString("FISCOSTORGUNIT") );
						mapTo.put("fIsStart",rsCopy.getString("FISSTART") );
						mapTo.put("fLevel",rsCopy.getString("FLEVEL") );
						mapTo.put("fIsLeaf",rsCopy.getString("FISLEAF") );
						mapTo.put("fIsOUSealUp",rsCopy.getString("FISOUSEALUP") );
						mapTo.put("FcreateTime",rsCopy.getString("FCREATETIME") );
						mapTo.put("FupdateType",rsCopy.getString("FUPDATETYPE") ); 
						
						mapEAS.put("data",mapTo);
						String datajsonStr = JSONObject.toJSONString(mapEAS);
						
						map.put("FNUMBER", mapTo.get("fNumber"));
			        	map.put("FNAME", mapTo.get("fName")); 
			        	map.put("JSON", datajsonStr);
			        	
			        	
			        	String  result = sendBaseDataToB2B(ctx ,datajsonStr);
			        	
			        	logger.info("发送组织通知给B2B系统，result：" + result);  
			        	Map<String, String> mapRet = (Map) JSONObject.parse(result);  
			        	if(mapRet.get("code") != null && "200".equals(String.valueOf(mapRet.get("code")))){
			        		flag=true;
			        	} 
			        	
			        	map.put("RESJSON", result);
					} 
					
				}else{
					 map.put("ERROR", "根据ID"+fid+"找不到对应的组织信息,没有同步到中间库。");
					 System.out.println("########  ERROR ########根据ID："+fid+"找不到对应的组织信息,没有同步到中间库。");
				}  
				flag=true;
				getlogInfo(ctx , map ,  DateBasetype.orgUnit ,processType,flag,loginfo);
    			
    		}else if(type ==4){//人员  newOrDele : 0:新增 ; 1启用 ; 2:禁用
    			
    			int fStatus = 0;
    			int fUpdateType = 0;
    			if(newOrDele == 1){
    				fStatus = 0;
    				fUpdateType = 0;
    			}else if(newOrDele == 2){
    				fStatus = 1;
    				fUpdateType = 2;
    			}
    			
    			
    			String selectSuppSql = " /*dialect*/ select  fid from  EAS_Person_MIDTABLE where fid='"+fid+"' ";
				List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
				if(retsSup.size() == 0 ){//没有 
					
				}else{
					String selectDelete = " delete    EAS_Person_MIDTABLE where fid='"+fid+"' ";
					EAISynTemplate.execute(ctx, dataBase, selectDelete);
				}

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
    			
				String personsql = " /*dialect*/ SELECT    person.fid fId , person.fnumber fNumber, person.fname_l2 fName , empposition.fid fPositionID ,  "+
	          		" empposition.fnumber  fPositionNumber ,empposition.fname_l2 fPositionName  ,com.fid fOrgtid,dep.fid fDeptid,com.fnumber fOrgNumber, com.fname_l2 fOrgName , " +
	          		" '' POSTCODE , person.FEMAIL FEMAIL , '' BANK , '' BANKNUM , to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FSYNTIME_0   ,  "+
	          		"  emptype.fnumber fEmployeeTypeNumber, emptype.fname_l2 fEmployeeTypeName, to_char(person.FCREATETIME , 'yyyy-mm-dd hh24:mi:ss')   fCreateTime,  0 fUpdateType ,person.fnumber  fEmpNumber , "+ 
	          		" to_char( emprel.FEnterDate  ,'yyyy-mm-dd hh24:mi:ss' ) FENTERDATE , to_char( emprel.FActualFormalDate  ,'yyyy-mm-dd hh24:mi:ss' ) FPLANFORMALDATE ,  "+ 
	          		" joblevel.fid FJOBLEVELID , joblevel.fnumber FJOBLEVELNUMBER ,joblevel.fname_l2  FJOBLEVELNAME , hrjobcate.fid FJOBCATEGORYID  ,hrjobcate.fnumber FJOBCATEGORYNUMBER  , hrjobcate.fname_l2 FJOBCATEGORYNAME "+
	          		 
	          		" FROM   t_bd_person person "+ 
	          		" inner join t_hr_emporgrelation relation on person.fid = relation.fpersonid   and  relation.FAssignType= 1   and  to_char(relation.FLEFFDT , 'yyyy-mm-dd') ='2199-12-31'"+
	          		" left join T_ORG_Position empposition on empposition.fid = relation.FPositionID "+
	          		" left join  T_ORG_Admin  com on    com.fid =  relation.FCompanyID "+
	          		" left join  T_ORG_Admin dep on dep.fid =   relation.FAdminOrgID	 "+
	          		" left join  T_ORG_Job  job on  job.fid = empposition.FJOBID  "+
	          		" left join T_HR_EmpLaborRelation emprel  on emprel.fid = relation.FLABORRELATIONID  "+
	          		" left join  T_HR_BDEmployeeType emptype  on  emptype.fid = emprel.FLABORRELATIONSTATEID  "+
	          		" LEFT JOIN  T_HR_EmpPostRank postrank  on  postrank.fpersonid = person.fid and  to_char( postrank.FLEFFDT  ,'yyyy-mm-dd hh24:mi:ss' )  = '2199-12-31 00:00:00' "+
	          		" left  join T_HR_JobLevel joblevel on  joblevel.fid = postrank.FJOBLEVELID "+
	          		
	          		" left  join T_HR_PositionExtend postEx on  postEx.FPositionID = empposition.fid "+
	          		" left  join T_HR_HRJob hrjob on  hrjob.fid = postEx.FHRJobID "+
	          		" left  join T_HR_HRJobCategory hrjobcate on  hrjobcate.fid = hrjob.FHRJOBCATEGORYID "+
	          		" where  person.fid = '"+fid+"' ";
	    		IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,personsql);
				
	    		IRowSet  rsCopy=  rs.createCopy();
				if(rs!=null && rs.size() > 0){
					String sqlInsert = insertMidTable(ctx,  "EAS_ORG_MIDTABLE", rs,"fSign_0");
					map.put("ERROR",sqlInsert);
					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
					
					
					if(rsCopy.next()){	
						Map<String, String> mapTo = new  HashMap<String, String>();
						mapTo.put("fId",rsCopy.getString("FID") );
						mapTo.put("fNumber",rsCopy.getString("FNUMBER") );
						mapTo.put("fName",rsCopy.getString("FNAME") );
						mapTo.put("fLoginNumber",rsCopy.getString("FLOGINNUMBER") );
						mapTo.put("fPositionID",rsCopy.getString("FPOSITIONID") );
						mapTo.put("fPositionNumber",rsCopy.getString("FPOSITIONNUMBER") );
						mapTo.put("fPositionName",rsCopy.getString("FPOSITIONNAME") );
						mapTo.put("fOrgtid",rsCopy.getString("FORGTID") );
						mapTo.put("fDeptid",rsCopy.getString("FDEPTID") );
						mapTo.put("fOrgNumber",rsCopy.getString("FORGNUMBER") );
						mapTo.put("fOrgName",rsCopy.getString("FORGNAME") ); 
						mapTo.put("fEmployeeTypeNumber",rsCopy.getString("FEMPLOYEETYPENUMBER") );
						mapTo.put("fEmployeeTypeName",rsCopy.getString("FEMPLOYEETYPENAME") ); 
						mapTo.put("fCreateTime",rsCopy.getString("FCREATETIME") ); 
						mapTo.put("fUpdateType",rsCopy.getString("FUPDATETYPE") ); 
						mapTo.put("fEmpNumber",rsCopy.getString("FEMPNUMBER") );  
						
						mapEAS.put("data",mapTo);
						String datajsonStr = JSONObject.toJSONString(mapEAS);
						
						map.put("FNUMBER", mapTo.get("fNumber"));
			        	map.put("FNAME", mapTo.get("fName")); 
			        	map.put("JSON", datajsonStr);
			        	
			        	String  result = sendBaseDataToB2B(ctx ,datajsonStr);
			        	map.put("RESJSON", result);
			        	
			        	logger.info("发送人员通知给B2B系统，result：" + result);  
			        	Map<String, String> mapRet = (Map) JSONObject.parse(result);  
			        	if(mapRet.get("code") != null && "200".equals(String.valueOf(mapRet.get("code")))){
			        		flag=true;
			        	}  
					} 
					
				}else{
					 map.put("ERROR", "根据ID"+fid+"找不到对应的人员信息,没有同步到中间库。");
					 System.out.println("########  ERROR ########根据ID："+fid+"找不到对应的人员信息,没有同步到中间库。");
				}   
				getlogInfo(ctx , map ,  DateBasetype.Person ,processType,flag,loginfo);
	    		 
    		}else if(type ==5){//仓库  newOrDele : 0:新增 ; 1启用 ; 2:禁用
 
    			Map<String, String> mapRet = new  HashMap<String, String>();
    			
    			
    			 
    			int fUpdateType = 0;
    			if(newOrDele == 1){ 
    				fUpdateType = 1;
    			}else if(newOrDele == 2){ 
    				fUpdateType = 2;
    			}
    			HashMap<String, String> mapB2B = new HashMap<String, String>(); 
    			HashMap<String, String> mapHIS = new HashMap<String, String>(); 
    			try{
    				
    			}catch (Exception e) {
					// TODO: handle exception
				}

				
				
				
    			String sql  = " /*dialect*/ select wah.fid fId , wah.fnumber fNumber, wah.fname_l2 fName ,admin.fid fOrgid,admin.fnumber fOrgNumber, admin.fname_l2 fOrgName, "+
				  "	wah.FWhState fStatus ,cuser.fname_l2  fCreator ,  to_char( wah.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' ) fCreateTime ,  "+fUpdateType+" fUpdateType ,to_char( wah.FLASTUPDATETIME  ,'yyyy-mm-dd hh24:mi:ss' ) fUpdateTime ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime  "+
				  " from  T_DB_WAREHOUSE wah inner  join T_ORG_Storage admin on admin.fid = wah.FstorageOrgID "+
				  "	inner join  T_PM_User  cuser on cuser.fid=wah.FCREATORID  "+ 
				  " where wah.fid = '"+fid+"' ";  
    			String orgid ="";
    			IRowSet  rsData = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			IRowSet  rsB2B = rsData.createCopy(); 
    			IRowSet  rsHIS = rsData.createCopy();  
    			
    			String selectSuppSqlB2B = " /*dialect*/ select  FNUMBER from  EAS_Warehouse_Cent where fid='"+fid+"' ";
				List<Map<String, Object>> retsSupB2B = EAISynTemplate.query(ctx,dataBase, selectSuppSqlB2B);
				if(retsSupB2B.size() == 0 ){//没有 
					
				}else{
					String selectDelete = " delete  EAS_Warehouse_Cent where fid='"+fid+"' ";
					EAISynTemplate.execute(ctx, dataBase, selectDelete);
				}  
				if(rsB2B!=null && rsB2B.size() > 0){
					String sqlInsert = insertMidTable(ctx,  "EAS_Warehouse_Cent", rsB2B,"fSign");
					map.put("ERROR",sqlInsert);
					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
				}else{
					 map.put("ERROR", "根据ID"+fid+"找不到对应的仓库信息,没有同步到中间库。");
				}   
				
				
				
    			if(rsData!=null && rsData.size() > 0){  
    				while(rsData.next()){    
    					orgid = rsData.getString("FORGID");
    					
    					mapB2B.put("fId",rsData.getString("FID") );
						mapB2B.put("fNumber",rsData.getString("FNUMBER") );
						mapB2B.put("fName",rsData.getString("FNAME") );
						mapB2B.put("fOrgtid",rsData.getString("FORGID") );
						mapB2B.put("fOrgNumber",rsData.getString("FORGNUMBER") );
						mapB2B.put("fOrgName",rsData.getString("FORGNAME") );
						mapB2B.put("fStatus",rsData.getString("FSTATUS") );
						mapB2B.put("fCreator",rsData.getString("FCREATOR") );
						mapB2B.put("fCreateTime",rsData.getString("FCREATETIME") );
						mapB2B.put("fUpdateType",rsData.getString("FUPDATETYPE") );
						mapB2B.put("fUpdateTime",rsData.getString("FUPDATETIME") ); 
						
    					if( !"jbYAAAMU2SvM567U".equals(orgid)){
    						mapHIS.put("fid",rsData.getString("FID") );
    						mapHIS.put("fstatus",rsData.getString("FSTATUS") );
    						mapHIS.put("fnumber",rsData.getString("FNUMBER") );
    						mapHIS.put("fname",rsData.getString("FNAME") );
							/*map.put("forgtid",rs.getString("FORGTID") );
							map.put("forgNumber",rs.getString("FORGNUMBER") );
							map.put("forgName",rs.getString("FORGNAME") );*/
    						mapHIS.put("forgId",rsData.getString("FORGID") );
						} 
    					String datajsonStrHis = JSONObject.toJSONString(mapHIS);
    					
    					
    					mapEAS.put("data",mapB2B);
						String datajsonStrB2B = JSONObject.toJSONString(mapEAS);
						
						String  result = sendBaseDataToB2B(ctx ,datajsonStrB2B);
			        	map.put("RESJSON", result);
			        	
			        	 
						logger.info("发送仓库通知给B2B系统，result：" + result);   
			        	mapRet = (Map) JSONObject.parse(result);  
			        	if(mapRet !=null && mapRet.get("code") != null && "200".equals(String.valueOf(mapRet.get("code")))){
			        		flag=true;
			        	} 
			        	
    					map.put("FNUMBER", mapB2B.get("fNumber"));
			        	map.put("FNAME", mapB2B.get("fName"));
			        	map.put("JSON", datajsonStrB2B);
			        	
    				}
		       }   
    		
    			 
				
    			if( !"jbYAAAMU2SvM567U".equals(orgid)){//his
    				String selectSuppSql = " /*dialect*/ select  FNUMBER from  EAS_Warehouse_Clinic where fid='"+fid+"' ";
    				List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
    				if(retsSup.size() == 0 ){//没有 
    					
    				}else{
    					String selectDelete = " delete    EAS_Warehouse_Clinic where fid='"+fid+"' ";
    					EAISynTemplate.execute(ctx, dataBase, selectDelete);
    				} 
    				if(rsHIS!=null && rsHIS.size() > 0){
    					String sqlInsert = insertMidTable(ctx,  "EAS_Warehouse_Clinic", rsHIS,"fSign");
    					map.put("ERROR",sqlInsert);
    					EAISynTemplate.execute(ctx, dataBase, sqlInsert);
    					
    					
    					List<Map<String,String>> eMps = new ArrayList<Map<String,String>>();
    					if(newOrDele == 0 ){
    						eMps.add(mapHIS);
    						Map<String,Object> mp = new HashMap<String,Object>();
    						mp.put("subList", eMps);
    						
    						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
    						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 , warurl);
    						logger.info("发送仓库,"+mapHIS+"通知给his系统，result：" + result);
    						System.out.println("########  result ########"+result);
    						
    						mapRet = (Map) JSONObject.parse(result);  
    						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
    							flag=true;
    						}else{
    							flag=false;
    						}
    						map.put("RESJSON", result);
    						map.put("JSON", JSONObject.toJSONString(eMps)); 
    					}else if(newOrDele == 1 ){ 
    						Map<String, String> mapNew = new  HashMap<String, String>();
    						mapNew.put("fid",mapHIS.get("fid").toString()); 
    						mapNew.put("fstatus","1" );
    						
    						eMps.add(mapNew);
    						Map<String,Object> mp = new HashMap<String,Object>();
    						mp.put("subList", eMps);
    						
    						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
    						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 ,warJinYongurl);
    						logger.info("发送仓库启用信息,"+mapHIS+"通知给his系统，result：" + result);
    						System.out.println("########  result ########"+result);
    						mapRet = (Map) JSONObject.parse(result);  
    						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
    							flag=true;
    						}else{
    							flag=false;
    						}
    						map.put("RESJSON", result);
    						map.put("JSON", JSONObject.toJSONString(eMps)); 
    					}else if(newOrDele == 2 ){ 
    						Map<String, String> mapNew = new  HashMap<String, String>();
    						mapNew.put("fid",mapHIS.get("fid").toString()); 
    						mapNew.put("fstatus","2" );
    						
    						eMps.add(mapNew);
    						Map<String,Object> mp = new HashMap<String,Object>();
    						mp.put("subList", eMps);
    						
    						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
    						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 ,warJinYongurl);
    						logger.info("发送仓库禁用信息,"+mapHIS+"通知给his系统，result：" + result);
    						System.out.println("########  result ########"+result);
    						mapRet = (Map) JSONObject.parse(result);  
    						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
    							flag=true;
    						}else{
    							flag=false;
    						}
    						map.put("RESJSON", "B2B:"+map.get("RESJSON")+";HIS:"+result);
    						map.put("JSON", JSONObject.toJSONString(eMps)); 
    					}
    					
    				}else{
    					 map.put("ERROR", "根据ID"+fid+"找不到对应的仓库信息,没有同步到中间库。");
    				}    
					getlogInfo(ctx , map,DateBasetype.WAREHOUSE ,processType,flag ,loginfo);
    			} 
    			
    		}
		} catch (EASBizException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!flag){
			try {
				if(type ==1){
					getlogInfo(ctx , map,DateBasetype.Customer ,processType,flag ,loginfo); 
				}else if(type ==2){
					getlogInfo(ctx , map,DateBasetype.Supplier ,processType,flag,loginfo );
				}else if(type ==3){
					getlogInfo(ctx , map,DateBasetype.orgUnit ,processType,flag ,loginfo);
				}else if(type ==4){
					getlogInfo(ctx , map,DateBasetype.Person ,processType,flag ,loginfo);
				}else if(type ==5){
					getlogInfo(ctx , map,DateBasetype.WAREHOUSE ,processType,flag ,loginfo);
				}
			}catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
    }
    
    @Override
	protected void _syncDateByType(Context ctx, int type, String data,
			int newOrDele, String name, String fid) throws BOSException {
		// TODO Auto-generated method stub
		//super._syncDateByType(ctx, type, data, newOrDele, name, number);
    	PurPlatSyncdbLogInfo loginfo = new PurPlatSyncdbLogInfo();
    	syncDate(ctx,type,data,newOrDele,name,fid,loginfo);
    	
	}

	public PurPlatSyncdbLogInfo getlogInfo(Context ctx , Map<String, String> map,DateBasetype dateBasetype,DateBaseProcessType processType, boolean flag ,PurPlatSyncdbLogInfo loginfo)
	throws BOSException, EASBizException {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(new Date());
		
		
		if(null != map.get("FNUMBER") && !"".equals(map.get("FNUMBER"))){
			loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("FNUMBER")); 
			loginfo.setSimpleName(map.get("FNUMBER").toString());
		}else{
			loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("FID")); 
			loginfo.setSimpleName(map.get("FID").toString());
		}
		
		if(null != map.get("FNAME") && !"".equals(map.get("FNAME"))){
			loginfo.setName(map.get("FNAME").toString());
		}
		
		
		if(null != map.get("JSON") && !"".equals(map.get("JSON"))){
			loginfo.setMessage(map.get("JSON").toString());
		}
		
		if(null != map.get("RESJSON") && !"".equals(map.get("RESJSON"))){
			loginfo.setRespond(map.get("RESJSON").toString());
		} 
		
		if(null != map.get("FID") && !"".equals(map.get("FID"))){
			loginfo.setDescription(map.get("FID").toString());
		}
		loginfo.setDateBaseType(dateBasetype);
		String version = String.valueOf(cal.getTimeInMillis());
		loginfo.setVersion(version);
		loginfo.setUpdateDate(new Date());
		
		loginfo.setStatus(flag);
		loginfo.setProcessType(processType);
		if(null != map.get("ERROR") && !"".equals(map.get("ERROR"))){
			loginfo.setErrorMessage(map.get("ERROR").toString());
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		loginfo.setIsSync(false); 
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(loginfo);
		return loginfo;
	}

	
	
	@Override
	protected void _DoMaterialJson(Context ctx, String data)
			throws BOSException {
		// TODO Auto-generated method stub
		super._DoMaterialJson(ctx, data);
		StringBuffer ids = new StringBuffer();
		ids= ids.append("'");
		try {
			String sql = " /*dialect*/select  fid ,cfdatebasetype TYPE , cfmessage  MESSAGE    from  CT_CUS_PurPlatSyncdbLog where  cfstatus = 0 and  cfdatebasetype = 6 and  nvl(CFSendCount,0) = 0  and  cfIsSync = 0 ";
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			  
			Map<String, String> typemap = new  HashMap<String, String>();
			Map<String, String> msgmap = new  HashMap<String, String>();
			
			
			if(rs!=null && rs.size() > 0){
				while(rs.next()){	 
					String fid = rs.getString("FID");
					String type = rs.getString("TYPE");
					String message = rs.getString("MESSAGE");
					typemap.put(fid, type);
					msgmap.put(fid, message);
					ids= ids.append(fid+"','"); 
				}  
			}
			
			if(ids.length() > 1 ){
				ids = new StringBuffer().append( ids.substring(0, ids.length()-2) );
				String upsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
				for(Map.Entry<String, String> entry : typemap.entrySet()){
				    String id = entry.getKey();
				    String type = entry.getValue(); 
				    if(type.equals("6") && null !=msgmap.get(id) && !"".equals(msgmap.get(id).toString())){//物料
				    	String msgjson = msgmap.get(id);
				    	
				    	MaterialUntil  ma = new MaterialUntil();
				    	ma.doCreateMaterial(ctx , msgjson);
				    }
				    
				} 
				String upEndsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 0 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upEndsql);
				
			}
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void _DoErrorJon(Context ctx, String data) throws BOSException {
		// TODO Auto-generated method stub
		//_DoMaterialJson(ctx, data);
		StringBuffer ids = new StringBuffer();
		ids= ids.append("'");
		try {
			
			
			String sql = " /*dialect*/select  fid ,cfdatebasetype TYPE , cfmessage  MESSAGE    from  CT_CUS_PurPlatSyncdbLog where  cfstatus = 0 and  cfdatebasetype != 6 and  nvl(CFSendCount,0) < 4  and  cfIsSync = 0 ";
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			  
			Map<String, String> typemap = new  HashMap<String, String>();
			Map<String, String> msgmap = new  HashMap<String, String>();
			
			
			if(rs!=null && rs.size() > 0){
				while(rs.next()){	 
					String fid = rs.getString("FID");
					String type = rs.getString("TYPE");
					String message = rs.getString("MESSAGE");
					typemap.put(fid, type);
					msgmap.put(fid, message);
					ids= ids.append(fid+"','"); 
				}  
			}
			
			if(ids.length() > 1 ){
				 
				Map<String, String> deleMap = new  HashMap<String, String>();
				
				ids = new StringBuffer().append( ids.substring(0, ids.length()-2) );
				String upsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
				
				PurPlatSyncdbLogCollection collection = PurPlatSyncdbLogFactory.getLocalInstance(ctx).getPurPlatSyncdbLogCollection(" where  id in ("+ids+" )");
				
				for(int i = 0 ; i < collection.size(); i ++){
					PurPlatSyncdbLogInfo info= collection.get(i);
					int newOrDele = 0 ;
					if("2".equals(info.getProcessType().getValue() )  ){
						newOrDele = 0 ;
			    	}else if("6".equals(info.getProcessType().getValue() ) ){
			    		newOrDele = 1 ;
			    	}else if("5".equals(info.getProcessType().getValue() )  ){
			    		newOrDele = 2 ;
			    	}
					boolean flag = false;
					if(info.getDateBaseType()!=null  && "6".equals(info.getDateBaseType().getValue())){
						MaterialUntil  ma = new MaterialUntil();
				    	ma.doCreateMaterial(ctx , info.getMessage());
					}else if(info.getDateBaseType()!=null  && "4".equals(info.getDateBaseType().getValue())){//1:客户  
						flag =  syncDate(ctx,1,data,newOrDele,"",info.getDescription(),info);
						
					}else if(info.getDateBaseType()!=null  && "3".equals(info.getDateBaseType().getValue())){//   2：供应商  
						flag = syncDate(ctx,2,data,newOrDele,"",info.getDescription(),info);
						
					}else if(info.getDateBaseType()!=null  && "1".equals(info.getDateBaseType().getValue())){//    3：组织   
						flag = syncDate(ctx,3,data,newOrDele,"",info.getDescription(),info);
					}else if(info.getDateBaseType()!=null  && "5".equals(info.getDateBaseType().getValue())){//   4 人员   
						flag = syncDate(ctx,4,data,newOrDele,"",info.getDescription(),info);
					}else if(info.getDateBaseType()!=null  && "8".equals(info.getDateBaseType().getValue())){//    5 仓库   
						flag = syncDate(ctx,5,data,newOrDele,"",info.getDescription(),info);
					} 
					//1:客户  2：供应商  3：组织  4 人员  5 仓库   
					
					
					if(flag){
						info.setStatus(true);
						PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(info);
					}else{
						//typemap.put(info.getId().toString(), info.getId().toString());
					}
				}  
				String upEndsql = "/*dialect*/ update CT_CUS_PurPlatSyncdbLog set cfIsSync = 0 , cfsendcount = nvl(cfsendcount,0)+1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upEndsql);
				
			} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	/*
	fNumber	物料编码
	fName	物料名称
	fModel	规格型号
	fMaterialGroup	物料类别
	fModel	型号
	fArtNo	货号
	fBrand	品牌
	fCreateTime	创建时间
	fUpdateTime	最后修改时间
	fKAClass	记账分类
	fBaseUnit	基本计量单位
	fInvUnit	库存计量单位
	fPurUnit	采购计量单位
	fSaleUnit	销售计量单位
	
	
		
	*/
	@Override
	protected String _materialSyncFun(Context ctx, String data)
			throws BOSException {
		// TODO Auto-generated method stub
		//return super._materialSyncFun(ctx, data); 
		//data  ="{\"msgId\":\"pkKBgt311111\",\"operType\":0,\"reqCount\":1,\"reqTime\":\"20220715121020\",\"data\":[{\"fNumber\":\"CSqq001\",\"fName\":\"测试物料001\",\"fModel\":\"型号\",\"fMaterialGroup\":\"W303\",\"fArtNo\":\"fArtNo\",\"fBrand\":\"fBrand\",\"fCreateTime\":\"2022-07-20\",\"fUpdateTime\":\"2022-07-20\",\"fKAClass\":\"erjg\",\"fBaseUnit\":\"G01\",\"fInvUnit\":\"G04\",\"fPurUnit\":\"G04\",\"fSaleUnit\":\"G04\"}]}";
		Map map = (Map) JSONObject.parse(data);
		 
		HashMap<String, String> returnMap =new  HashMap<String, String>();
		returnMap.put("code", "-1");
		
		if (map.get("msgId")== null || "".equals(map.get("msgId").toString()) ) { 
			returnMap.put("msg", "请求Id不能为空。");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		returnMap.put("msgId", map.get("msgId").toString() );
		if (map.get("reqCount")== null || "".equals(map.get("reqCount").toString()) ) { 
			returnMap.put("msg", "请求数量不能为空。");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		
		if (map.get("operType")== null || "".equals(map.get("operType").toString()) ) {  //0-新增，1-修改，2-禁用
			returnMap.put("msg", "操作类型不能为空。");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		 
		if (map.get("data")== null || "".equals(map.get("data").toString()) ) {  
			returnMap.put("msg", "对象数组不能为空。");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		String  error = new String();
		com.alibaba.fastjson.JSONArray  jsonArr = (com.alibaba.fastjson.JSONArray) map.get("data");   
		//JsonArray returnData = new JsonParser().parse(aa).getAsJsonArray();  
		boolean flag = true;
		int  size = jsonArr.size();
		for( int i = 0 ; i < size ; i++ ){
			Map dataMap = (Map) jsonArr.get(i);  
			
			if("0".equals(map.get("operType").toString())){
				if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
					error = error+ "物料编码不能为空;"; flag = false;
					continue;
				}
				
				 
				String  number  = dataMap.get("fNumber").toString() ;
				try {
					if ( !"0".equals(map.get("operType").toString()) && imbiz.exists("where number = '"+number+"'") ) { 
						error = error+ "物料编码已存在;"; flag = false;
						continue;
					}
				} catch (EASBizException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (dataMap.get("fName")== null || "".equals(dataMap.get("fName").toString()) ) { 
					error = error+ "编码为"+dataMap.get("fNumber").toString()+"的名称不能为空"; flag = false;
					continue;
				}  
				/*if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
					error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的型号不能为空";  flag = false;
					continue;
				}
				if (dataMap.get("fArtNo")== null || "".equals(dataMap.get("fArtNo").toString()) ) { 
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的货号不能为空"; flag = false;
					continue;
				}
				
				if (dataMap.get("fBrand")== null || "".equals(dataMap.get("fBrand").toString()) ) { 
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的品牌不能为空"; flag = false;
					continue;
				}*/
				if (dataMap.get("fMaterialGroup")== null || "".equals(dataMap.get("fMaterialGroup").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的物料类别不能为空"; flag = false;
					continue;
				}
				try {
					if(!MaterialGroupFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fMaterialGroup").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的物料类别不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				
				if (dataMap.get("fCreateTime")!= null &&  !"".equals(dataMap.get("fCreateTime").toString()) ) { 
					String createTime = dataMap.get("fCreateTime").toString();
					
					try {
						Date date = sdf1.parse(createTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						error = error+ "编码为"+dataMap.get("fNumber").toString()+"的创建时间格式不正确;"; flag = false;
						continue;
						
					}
				}
				
				if (dataMap.get("fUpdateTime")!= null && !"".equals(dataMap.get("fUpdateTime").toString()) ) { 
					String updateTime = dataMap.get("fUpdateTime").toString(); 
					try {
						Date date = sdf1.parse(updateTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						error = error+ "编码为"+dataMap.get("fNumber").toString()+"的最后修改时间格式不正确;"; flag = false;
						continue;
						
					}
					
				}
				
				if (dataMap.get("fKAClass")== null || "".equals(dataMap.get("fKAClass").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的记账分类不能为空"; flag = false;
					continue;
				}
				try {
					if(!KAClassficationFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fKAClass").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的记账分类不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dataMap.get("fBaseUnit")== null || "".equals(dataMap.get("fBaseUnit").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的基本计量单位不能为空"; flag = false;
					continue;
				} 
				try {
					if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fBaseUnit").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的基本计量单位不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (dataMap.get("fInvUnit")== null || "".equals(dataMap.get("fInvUnit").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的库存计量单位不能为空"; flag = false;
					continue;
				}
				
				try {
					if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fInvUnit").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的库存计量单位不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if (dataMap.get("fPurUnit")== null || "".equals(dataMap.get("fPurUnit").toString()) ) { 
					error = error+ "编码为"+dataMap.get("fNumber").toString()+"的采购计量单位不能为空"; flag = false;
					continue;
				}
				
				try {
					if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fPurUnit").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的采购计量单位不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (dataMap.get("fSaleUnit")== null || "".equals(dataMap.get("fSaleUnit").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的销售计量单位不能为空"; flag = false;
					continue;
				} 
				try {
					if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fSaleUnit").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的销售计量单位不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if("1".equals(map.get("operType").toString())){//修改
				
				if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
					error = error+ "物料编码不能为空;"; flag = false;
					continue;
				}
				
				 
				String  number  = dataMap.get("fNumber").toString() ;
				try {
					if (  !imbiz.exists("where number = '"+number+"'") ) { 
						error = error+ "物料编码不存在;"; flag = false;
						continue;
					}
				} catch (EASBizException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (dataMap.get("fName")== null || "".equals(dataMap.get("fName").toString()) ) { 
					error = error+ "编码为"+dataMap.get("fNumber").toString()+"的名称不能为空"; flag = false;
					continue;
				}  
				/*if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
					error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的型号不能为空";  flag = false;
					continue;
				}
				if (dataMap.get("fArtNo")== null || "".equals(dataMap.get("fArtNo").toString()) ) { 
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的货号不能为空"; flag = false;
					continue;
				}
				
				if (dataMap.get("fBrand")== null || "".equals(dataMap.get("fBrand").toString()) ) { 
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的品牌不能为空"; flag = false;
					continue;
				}*/
				if (dataMap.get("fMaterialGroup")== null || "".equals(dataMap.get("fMaterialGroup").toString()) ) { 
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的物料类别不能为空"; flag = false;
					continue;
				}
				try {
					if(!MaterialGroupFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fMaterialGroup").toString()+"'")){
						error = error+  "编码为"+dataMap.get("fNumber").toString()+"的物料类别不存在"; flag = false;
						continue;
					}
				} catch (EASBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}else if("2".equals(map.get("operType").toString())){
				if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
					error = error+ "物料编码不能为空;"; flag = false;
					continue;
				}
			}
			
			
		}
		String jsonStr =  new String();
		if(flag){
			returnMap.put("code", "200");
			jsonStr = JSONObject.toJSONString(returnMap);
		}else{
			returnMap.put("msg", error);
			jsonStr = JSONObject.toJSONString(returnMap);
		}
		 
		
		Calendar cal = Calendar.getInstance();
		PurPlatSyncdbLogInfo loginfo = new PurPlatSyncdbLogInfo();
		cal.setTime(new Date());
		loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("msgId"));
		loginfo.setName(map.get("msgId").toString());
		loginfo.setSimpleName(map.get("msgId").toString());
		loginfo.setDateBaseType(DateBasetype.Material);
		String version = String.valueOf(cal.getTimeInMillis());
		loginfo.setVersion(version);
		loginfo.setUpdateDate(new Date());
		loginfo.setMessage(data);
		loginfo.setRespond(jsonStr);
		loginfo.setStatus(false);
		loginfo.setIsSync(false);
		
		String type = map.get("operType").toString();
		//0-新增，1-修改，2-禁用
		if("0".equals(type)){
			loginfo.setProcessType(DateBaseProcessType.AddNew);
		}else if("2".equals(type)){
			loginfo.setProcessType(DateBaseProcessType.Update);
		}else if("3".equals(type)){
			loginfo.setProcessType(DateBaseProcessType.DisAble);
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		try {
			PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  
		return jsonStr;  
	}

	 


	 /**
	    * 向指定 URL 发送POST方法的请求
	    * 
	    * @param url
	    *            发送请求的 URL
	    * @param param
	    *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	    * @param isproxy
	    *               是否使用代理模式
	    * @return 所代表远程资源的响应结果
	    */
		public static String sendMessageToHISPost(String param,int oper,String url) {
			OutputStreamWriter out = null;
	        BufferedReader in = null;
	        String result = "";  
	        if(url != null && !"".equals(url)){
	        	try {
	        		URL realUrl = new URL(url);
	   	            HttpURLConnection conn =  (HttpURLConnection) realUrl.openConnection();
	   	             // 打开和URL之间的连接
	   	            // 发送POST请求必须设置如下两行
	   	            conn.setDoOutput(true);
	   	            conn.setDoInput(true);
	   	            conn.setRequestMethod("POST");    // POST方法
	   	            // 设置通用的请求属性
	   	            conn.setRequestProperty("accept", "*/*");
	   	            conn.setRequestProperty("connection", "Keep-Alive");
	   	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	   	          	conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	   	            conn.connect();
	   	            // 获取URLConnection对象对应的输出流
	   	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	   	            // 发送请求参数
	   	            out.write(param);
	   	            System.out.println("param------------------------"+param);
	   	            // flush输出流的缓冲
	   	            out.flush();
	   	            // 定义BufferedReader输入流来读取URL的响应
	   	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	   	            String line;
	   	            while ((line = in.readLine()) != null) {
	   	                result += line;
	   	            }
	   	        } catch (Exception e) {
	   	            System.out.println("发送 POST 请求出现异常！"+e);
	   	            e.printStackTrace();
	   	            return  e.getMessage();
	   	        }
	   	        //使用finally块来关闭输出流、输入流
	   	        finally{
	   	            try{
	   	                if(out!=null){
	   	                    out.close();
	   	                }
	   	                if(in!=null){
	   	                    in.close();
	   	                }
	   	            }
	   	            catch(IOException ex){
	   	                ex.printStackTrace();
	   	                return  ex.getMessage();
	   	            }
	   	        }
	        }
	       
	       return result;
	   }
	
	

		private String insertMidTable(Context ctx,  String tableName,IRowSet rows , String fSign) {
			String insertSql = "";
			try {
				if(rows.next()){
					IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
					int columnsSize = rowSetMataData.getColumnCount();
					String insertSqlStart = "INSERT INTO " + tableName + "(";
					String insertSqlValues = "";
					insertSql = "";
					for (int i = 1; i <= columnsSize; i++) {
						String columnName = rowSetMataData.getColumnName(i);
						String value = rows.getString(columnName);
						insertSqlStart += columnName + ",";
						if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date") ||  columnName.toLowerCase().endsWith("time_0") ) {
							// insertSqlValues += value+"','";
							if(null == value || "".equals(value) || "null".equals(value)){
								insertSqlValues +=   value + ",";
							}else{
								insertSqlValues += "to_date('" + value
								+ "','yyyy-mm-dd hh24:mi:ss')" + ",";
							}
						} else {
							if (null == value) {
								insertSqlValues += "'0',";
							} else {
								insertSqlValues += "'" + value + "',";
							}
						}
					}
					insertSqlStart += fSign+ ") VALUES(";
					insertSqlValues += "0)";
					insertSql = insertSqlStart + insertSqlValues;
					System.out.print("**************sql=" + insertSql);
				} 
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return insertSql;
		}
		
		private String insertMidTableAll(Context ctx,  String tableName,IRowSet rows , String fSign) {
			String insertSql = "";
			try {
				/*if(rows.next()){
					
					
				} */
				IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
				int columnsSize = rowSetMataData.getColumnCount();
				String insertSqlStart = "INSERT INTO " + tableName + "(";
				String insertSqlValues = "";
				insertSql = "";
				for (int i = 1; i <= columnsSize; i++) {
					String columnName = rowSetMataData.getColumnName(i);
					String value = rows.getString(columnName);
					insertSqlStart += columnName + ",";
					if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date") ||  columnName.toLowerCase().endsWith("time_0") ) {
						// insertSqlValues += value+"','";
						
						if(null == value || "".equals(value) || "null".equals(value)){
							insertSqlValues +=   value + ",";
						}else{
							insertSqlValues += "to_date('" + value
							+ "','yyyy-mm-dd hh24:mi:ss')" + ",";
						}
						
					} else {
						if (null == value) {
							insertSqlValues += "'0',";
						} else {
							insertSqlValues += "'" + value + "',";
						}
					}
				}
				insertSqlStart += fSign+ ") VALUES(";
				insertSqlValues += "0)";
				insertSql = insertSqlStart + insertSqlValues;
				//System.out.print("**************sql=" + insertSql);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return insertSql;
		}
		

		@Override
		protected void _doCangkuMid(Context ctx) throws BOSException {
			// TODO Auto-generated method stub
			super._doCangkuMid(ctx);
			
			
			Map<String, String> namemapB2B = new  HashMap<String, String>();
			Map<String, String> statusmapB2B = new  HashMap<String, String>();
			
			
			Map<String, String> namemapHIS = new  HashMap<String, String>();
			Map<String, String> statusmapHIS = new  HashMap<String, String>();
			
			List<String> updatesqls = new ArrayList<String>();
			List<String> sqls = new ArrayList<String>();
			
			String selectSuppSqlCent = " /*dialect*/ select   FID,FNAME , FSTATUS,FUPDATETYPE  from   EAS_Warehouse_Cent   ";
			List<Map<String, Object>> retsSupCent = EAISynTemplate.query(ctx,dataBase, selectSuppSqlCent);
			if(retsSupCent.size() > 0 ){
				for (Map<String, Object> map : retsSupCent) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString();
					String status = map.get("FSTATUS")==null? "0":map.get("FSTATUS").toString();
					namemapB2B.put( fid, name);
					statusmapB2B.put( fid, status);
				}
			} 
			
			String selectSuppSql = " /*dialect*/ select   FID,FNAME , FSTATUS,FUPDATETYPE  from   EAS_Warehouse_Clinic   ";
			List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
			if(retsSup.size() > 0 ){
				for (Map<String, Object> map : retsSup) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString();
					String status = map.get("FSTATUS")==null? "0":map.get("FSTATUS").toString();
					namemapHIS.put( fid, name);
					statusmapHIS.put( fid, status);
				}
			} 
			
			String sql  = " /*dialect*/ select wah.fid fId , wah.fnumber fNumber, wah.fname_l2 fName ,admin.fid fOrgid,admin.fnumber fOrgNumber, admin.fname_l2 fOrgName, "+
			  "  wah.FWhState fStatus ,cuser.fname_l2  fCreator ,  to_char( wah.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,(case when wah.FWhState= 2 then 2 else 1 end )   fUpdateType ,to_char( wah.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime  "+
			  " from  T_DB_WAREHOUSE wah inner  join T_ORG_Storage admin on admin.fid = wah.FstorageOrgID "+
			  "	inner join  T_PM_User  cuser on cuser.fid=wah.FCREATORID  ";
			IRowSet  rsData = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			 
			if(rsData!=null && rsData.size() > 0){
				
				try {
					while(rsData.next()){    
						
						String fid = rsData.getString("FID");
						String name = rsData.getObject("FNAME")==null?"":rsData.getString("FNAME"); 
						String status = rsData.getString("FSTATUS");
						 
						String orgid = rsData.getString("FORGID");
						
						String  table = "EAS_Warehouse_Cent";
						if( null==namemapB2B.get(fid) ||"".equals(namemapB2B.get(fid))  ){// 需要新增
							String sqlInsert = insertMidTableAll(ctx,  table, rsData ,"fSign");
							sqls.add(sqlInsert);
						}else{
							String midName = namemapB2B.get(fid);
							String midStatus= statusmapB2B.get(fid);
							boolean flag = false;
							String sqlUpdate = " update "+table+" set ";
							if(!name.equals(midName)){
								sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
								flag = true;
							}
							if(!status.equals(midStatus)){ 
								if("1".equals(status)){
									sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
									flag = true;
								}else if("0".equals(status)){
									sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
									flag = true;
								} else if("2".equals(status)){
									sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =2,";
									flag = true;
								} 
							}
							if(flag){
								sqlUpdate = sqlUpdate+" fSign = 0, FsynTime =sysdate where fid = '"+fid+"'";
								updatesqls.add(sqlUpdate);
							}
						} 
						
						if( !"jbYAAAMU2SvM567U".equals(orgid)){ 
							table = "EAS_Warehouse_Clinic";
							
							if( null==namemapHIS.get(fid) ||"".equals(namemapHIS.get(fid))  ){// 需要新增
								 String sqlInsert = insertMidTableAll(ctx,  table, rsData ,"fSign");
								 sqls.add(sqlInsert);
							}else{
								String midName = namemapHIS.get(fid);
								String midStatus= statusmapHIS.get(fid);
								boolean flag = false;
								String sqlUpdate = " update "+table+" set ";
								if(!name.equals(midName)){
									sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
									flag = true;
								}
								if(!status.equals(midStatus)){ 
									if("1".equals(status)){
										sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
										flag = true;
									}else if("0".equals(status)){
										sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
										flag = true;
									} else if("2".equals(status)){
										sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =2,";
										flag = true;
									} 
								}
								if(flag){
									sqlUpdate = sqlUpdate+" fSign = 0, FsynTime =sysdate where fid = '"+fid+"'";
									updatesqls.add(sqlUpdate);
								}
							}
						}    
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	       } 

			if(sqls.size() >0){
				doInsertSqls(ctx, dataBase, sqls);
			}
			if(updatesqls.size() >0){
				doInsertSqls(ctx, dataBase, updatesqls);
			}
			
		}

		@Override
		protected void _doCustomerMid(Context ctx) throws BOSException {
			// TODO Auto-generated method stub
			super._doCustomerMid(ctx);
			Map<String, String> namemap = new  HashMap<String, String>();
			Map<String, String> statusmap = new  HashMap<String, String>();
			
			List<String> updatesqls = new ArrayList<String>();
			List<String> sqls = new ArrayList<String>();
			
			String selectSuppSql = " /*dialect*/ select   FID,FNAME , FSTATUS,FUPDATETYPE  from   EAS_Customer_MIDTABLE   ";
			List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
			if(retsSup.size() > 0 ){
				for (Map<String, Object> map : retsSup) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString();
					String status = map.get("FSTATUS")==null? "0":map.get("FSTATUS").toString();
					namemap.put( fid, name);
					statusmap.put( fid, status);
				}
			} 
			
			String sql  = " /*dialect*/ select cus.fid fId  ,cus.fnumber fNumber ,cus.fname_l2 fName , '' fOpenBank , '' fBankAccount ,  cuser.fname_l2  fCreator , "+
			 "  to_char( cus.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' ) fCreateTime ,to_char( cus.FLASTUPDATETIME  ,'yyyy-mm-dd hh24:mi:ss' ) fUpdateTime , 0 fIsGroup, "+
			 "  com.fid fOrgtId , com.FNUMBER fOrgNumber ,com.fname_l2 fOrgName , (case when cus.FUsedStatus=1 then 0 else 1 end )   fStatus ,(case when cus.FUsedStatus=1 then 1 else 2 end )  fUpdateType ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime "+
			 "  from T_BD_Customer  cus   "+
			 "  inner join T_BD_CustomerCompanyInfo cuscom  on cuscom.FCUSTOMERID  = cus.fid "+
			 "  inner join  T_PM_User  cuser on cuser.fid=cus.FCREATORID  "+
			 "  inner join  T_ORG_Company com on com.fid  = cuscom.FComOrgID and   com.fid = 'jbYAAAMU2SvM567U' ";
			 
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			if(rs!=null && rs.size() > 0){
				
				try {
					while(rs.next()){	
						 String fid = rs.getString("FID");
						 String name = rs.getObject("FNAME")==null?"":rs.getString("FNAME");   
						 String status = rs.getString("FSTATUS");
						 
						 if( null==namemap.get(fid) ||"".equals(namemap.get(fid))  ){// 需要新增
							 String sqlInsert = insertMidTableAll(ctx,  "EAS_Customer_MIDTABLE", rs ,"fSign");
							 sqls.add(sqlInsert);
						 }else{
							 String midName = namemap.get(fid);
							 String midStatus= statusmap.get(fid);
							 boolean flag = false;
							 String sqlUpdate = " update EAS_Customer_MIDTABLE set ";
							 if(!name.equals(midName)){
								 sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
								 flag = true;
							 }
							 if(!status.equals(midStatus)){ 
								 if("1".equals(status)){
									 sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =2,";
									 flag = true;
								 }else if("0".equals(status)){
									 sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
									 flag = true;
								 } 
							 }
							 if(flag){
								 sqlUpdate = sqlUpdate+" fSign = 0, FsynTime =sysdate where fid = '"+fid+"'";
								 updatesqls.add(sqlUpdate);
							 }
						 }
						 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}	
			
			if(sqls.size() >0){
				doInsertSqls(ctx, dataBase, sqls);
			}
			if(updatesqls.size() >0){
				doInsertSqls(ctx, dataBase, updatesqls);
			}
				 
		}

		@Override
		protected void _doOrgMid(Context ctx) throws BOSException {
			// TODO Auto-generated method stub
			super._doOrgMid(ctx);
			Map<String, String> namemap = new  HashMap<String, String>();
			Map<String, String> statusmap = new  HashMap<String, String>();
			
			List<String> updatesqls = new ArrayList<String>();
			List<String> sqls = new ArrayList<String>();
			
			String selectSuppSql = " /*dialect*/ select   FID,FNAME , FISSTART,FUPDATETYPE  from   EAS_ORG_MIDTABLE   ";
			List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
			if(retsSup.size() > 0 ){
				for (Map<String, Object> map : retsSup) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString();
					String status = map.get("FISSTART")==null? "0":map.get("FISSTART").toString();
					namemap.put( fid, name);
					statusmap.put( fid, status);
				}
			} 
			
			String sql  = " /*dialect*/ SELECT admin.fid  fId , admin.fnumber fNumber  ,admin.fname_l2  fName , admin.FLONGNUMBER  fLongNumber ,laytype.fid  fLayerTypeID , admin.fparentid  FPARENTID , "+
			  " admin.FISCOMPANYORGUNIT   fIsCompanyOrgUnit  ,admin.FISADMINORGUNIT  fIsAdminOrgUnit ,admin.FISCOSTORGUNIT  fIsCostOrgUnit ,admin.fisstart  fIsStart ,  to_char( admin.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' ) FcreateTime ,"+
			  " admin.FLevel fLevel ,  admin.FIsLeaf  fIsLeaf , admin.FIsSealUp  fIsOUSealUp ,  (case when admin.FIsSealUp =1 then 2  else 1 end )  FupdateType ,"+
			  "  to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime_0 "+
			  " FROM T_ORG_ADMIN admin "+
			  " inner  join  T_Org_LayerType laytype  on laytype.fid = admin.FLAYERTYPEID  ";
			
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);  
			if(rs!=null && rs.size() > 0){
				try {
					while(rs.next()){	
						 String fid = rs.getString("FID");
						 String name = rs.getObject("FNAME")==null?"":rs.getString("FNAME");    
						 String status = rs.getString("FISOUSEALUP");
						 
						 if( null==namemap.get(fid) ||"".equals(namemap.get(fid))  ){// 需要新增
							 String sqlInsert = insertMidTableAll(ctx,  "EAS_ORG_MIDTABLE", rs ,"fSign_0");
							 sqls.add(sqlInsert);
						 }else{
							 String midName = namemap.get(fid);
							 String midStatus= statusmap.get(fid);
							 boolean flag = false;
							 String sqlUpdate = " update EAS_ORG_MIDTABLE set ";
							 if(!name.equals(midName)){
								 sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
								 flag = true;
							 }
							 if(!status.equals(midStatus)){ 
								 if("1".equals(status)){
									 sqlUpdate = sqlUpdate+" FISSTART = "+status+", FUPDATETYPE =2,";
									 flag = true;
								 }else if("0".equals(status)){
									 sqlUpdate = sqlUpdate+" FISSTART = "+status+", FUPDATETYPE =1,";
									 flag = true;
								 } 
							 }
							 if(flag){
								 sqlUpdate = sqlUpdate+" fSign_0 = 0, FsynTime_0 =sysdate where fid = '"+fid+"'";
								 updatesqls.add(sqlUpdate);
							 }
						 }
						 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			} 
			if(sqls.size() >0){
				doInsertSqls(ctx, dataBase, sqls);
			}
			if(updatesqls.size() >0){
				doInsertSqls(ctx, dataBase, updatesqls);
			}
				 
			
		}

		@Override
		protected void _doPersonMid(Context ctx) throws BOSException {
			// TODO Auto-generated method stub
			super._doPersonMid(ctx);
			
			Map<String, String> namemap = new  HashMap<String, String>(); 
			
			List<String> updatesqls = new ArrayList<String>();
			List<String> sqls = new ArrayList<String>();
			
			String selectSuppSql = " /*dialect*/ select   FID,FNAME  from   EAS_Person_MIDTABLE   ";
			List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
			if(retsSup.size() > 0 ){
				for (Map<String, Object> map : retsSup) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString(); 
					namemap.put( fid, name); 
				}
			} 
			 
			
    		String personsql = " /*dialect*/ SELECT    person.fid fId , person.fnumber fNumber, person.fname_l2 fName , empposition.fid fPositionID ,  "+
          		" empposition.fnumber  fPositionNumber ,empposition.fname_l2 fPositionName  ,com.fid fOrgtid,dep.fid fDeptid,com.fnumber fOrgNumber, com.fname_l2 fOrgName , " +
          		" '' POSTCODE , person.FEMAIL FEMAIL , '' BANK , '' BANKNUM , to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FSYNTIME_0   ,  "+
          		"  emptype.fnumber fEmployeeTypeNumber, emptype.fname_l2 fEmployeeTypeName, to_char(person.FCREATETIME , 'yyyy-mm-dd hh24:mi:ss')   fCreateTime,  0 fUpdateType ,person.fnumber  fEmpNumber , "+ 
          		" to_char( emprel.FEnterDate  ,'yyyy-mm-dd hh24:mi:ss' ) FENTERDATE , to_char( emprel.FActualFormalDate  ,'yyyy-mm-dd hh24:mi:ss' ) FPLANFORMALDATE ,  "+ 
          		" joblevel.fid FJOBLEVELID , joblevel.fnumber FJOBLEVELNUMBER ,joblevel.fname_l2  FJOBLEVELNAME , hrjobcate.fid FJOBCATEGORYID  ,hrjobcate.fnumber FJOBCATEGORYNUMBER  , hrjobcate.fname_l2 FJOBCATEGORYNAME "+
          		 
          		" FROM   t_bd_person person "+ 
          		" inner join t_hr_emporgrelation relation on person.fid = relation.fpersonid   and  relation.FAssignType= 1   and  to_char(relation.FLEFFDT , 'yyyy-mm-dd') ='2199-12-31'"+
          		" left join T_ORG_Position empposition on empposition.fid = relation.FPositionID "+
          		" left join  T_ORG_Admin  com on    com.fid =  relation.FCompanyID "+
          		" left join  T_ORG_Admin dep on dep.fid =   relation.FAdminOrgID	 "+
          		" left join  T_ORG_Job  job on  job.fid = empposition.FJOBID  "+
          		" left join T_HR_EmpLaborRelation emprel  on emprel.fid = relation.FLABORRELATIONID  "+
          		" left join  T_HR_BDEmployeeType emptype  on  emptype.fid = emprel.FLABORRELATIONSTATEID  "+
          		" LEFT JOIN  T_HR_EmpPostRank postrank  on  postrank.fpersonid = person.fid and  to_char( postrank.FLEFFDT  ,'yyyy-mm-dd hh24:mi:ss' )  = '2199-12-31 00:00:00' "+
          		" left  join T_HR_JobLevel joblevel on  joblevel.fid = postrank.FJOBLEVELID "+
          		
          		" left  join T_HR_PositionExtend postEx on  postEx.FPositionID = empposition.fid "+
          		" left  join T_HR_HRJob hrjob on  hrjob.fid = postEx.FHRJobID "+
          		" left  join T_HR_HRJobCategory hrjobcate on  hrjobcate.fid = hrjob.FHRJOBCATEGORYID ";
    		
    		IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,personsql);
			 
			if(rs!=null && rs.size() > 0){
				 
				try {
					while(rs.next()){	
						 String fid = rs.getString("FID");
						 String name = rs.getObject("FNAME")==null?"":rs.getString("FNAME");  
						 
						 if( null==namemap.get(fid) ||"".equals(namemap.get(fid))  ){// 需要新增
							 String sqlInsert = insertMidTableAll(ctx,  "EAS_Person_MIDTABLE", rs ,"fSign_0");
							 sqls.add(sqlInsert);
						 }else{
							 String midName = namemap.get(fid); 
							 boolean flag = false;
							 String sqlUpdate = " update EAS_Person_MIDTABLE set ";
							 if(!name.equals(midName)){
								 sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
								 flag = true;
							 } 
							 if(flag){
								 sqlUpdate = sqlUpdate+" fSign_0 = 0, FsynTime_0 =sysdate where fid = '"+fid+"'";
								 updatesqls.add(sqlUpdate);
							 }
						 }
						 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			} 
			
			if(sqls.size() >0){
				doInsertSqls(ctx, dataBase, sqls);
			}
			if(updatesqls.size() >0){
				doInsertSqls(ctx, dataBase, updatesqls);
			}
			
		}

		@Override
		protected void _doSuppMid(Context ctx) throws BOSException {
			// TODO Auto-generated method stub
			super._doSuppMid(ctx); 
			
			Map<String, String> namemap = new  HashMap<String, String>();
			Map<String, String> statusmap = new  HashMap<String, String>();
			
			List<String> updatesqls = new ArrayList<String>();
			List<String> sqls = new ArrayList<String>();
			
			String selectSuppSql = " /*dialect*/ select   FID,FNAME , FSTATUS,FUPDATETYPE  from   EAS_Supplier_MIDTABLE   ";
			List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
			if(retsSup.size() > 0 ){
				for (Map<String, Object> map : retsSup) {
					String fid = map.get("FID").toString(); 
					String name = map.get("FNAME")==null? "": map.get("FNAME").toString();
					String status = map.get("FSTATUS")==null? "0":map.get("FSTATUS").toString();
					namemap.put( fid, name);
					statusmap.put( fid, status);
				}
			} 
			
 
			String sql  = " /*dialect*/ SELECT supp.fid  fid , supp.fnumber fnumber ,nvl(supp.fname_l2,'') Fname ,  gro.fname_l2 FCLASSNAME ,'' FOpenBank ,  '' FBankAccount, "+
			 "  supp.FCREATORID  Fcreator , to_char( supp.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss' )   FcreateTime , to_char( supp.FLASTUPDATETIME ,'yyyy-mm-dd hh24:mi:ss' )  FupdateTime,  supp.FIsInternalCompany  FISGroup , "+
			 "  admin.FNUMBER  ForgNumber ,admin.FName_l2 ForgName , (case when supp.FUsedStatus = 1 then 0 else  1 end ) FStatus ,admin.Fid Forgtid, (case when supp.FUsedStatus = 1 then 1 else  2 end )  FupdateType ,to_char( sysdate  ,'yyyy-mm-dd hh24:mi:ss' ) FsynTime "+
			 "   FROM  T_BD_Supplier  supp    "+
			 "  inner  join  T_BD_CSSPGroup gro on gro.fid =supp.FBrowseGroupID "+
			 " inner join  T_ORG_admin    admin  on admin.fid = supp.FCONTROLUNITID "; 
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);  
			if(rs!=null && rs.size() > 0){
				
				try {
					while(rs.next()){	
						 String fid = rs.getString("FID");
						 String name = rs.getObject("FNAME")==null?"":rs.getString("FNAME");
						 String status = rs.getString("FSTATUS");
						 
						 if( null==namemap.get(fid) ||"".equals(namemap.get(fid))  ){// 需要新增
							 String sqlInsert = insertMidTableAll(ctx,  "EAS_Supplier_MIDTABLE", rs ,"fSign");
							 sqls.add(sqlInsert);
						 }else{
							 String midName = namemap.get(fid);
							 String midStatus= statusmap.get(fid);
							 boolean flag = false;
							 String sqlUpdate = " update EAS_Supplier_MIDTABLE set "; 
							 if(!name.equals(midName)){
								 sqlUpdate = sqlUpdate+" FNAME = '"+name+"',";
								 flag = true;
							 }
							 if(!status.equals(midStatus)){ 
								 if("1".equals(status)){
									 sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =2,";
									 flag = true;
								 }else if("0".equals(status)){
									 sqlUpdate = sqlUpdate+" FSTATUS = "+status+", FUPDATETYPE =1,";
									 flag = true;
								 } 
							 }
							 if(flag){
								 sqlUpdate = sqlUpdate+" fSign = 0, FsynTime =sysdate where fid = '"+fid+"'";
								 updatesqls.add(sqlUpdate);
							 }
						 }
						 
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}	
			if(sqls.size() >0){
				doInsertSqls(ctx, dataBase, sqls);
			}
			if(updatesqls.size() >0){
				doInsertSqls(ctx, dataBase, updatesqls);
			}
				 
		}
		
		private void doInsertSqls(Context ctx, String dataBase,List<String> sqls){
			try {
				int size = sqls.size();
				int qian = (int)Math.ceil(size/10000);
				if(size%10000 >0){
					qian ++;
				}
				for(int i = 0 ; i < qian ; i++){
					List<String> sumSqls =  new ArrayList<String>();
				
					if(size < (i+1)*10000 ){
						sumSqls = sqls.subList(i*10000, size);
					}else{
						sumSqls = sqls.subList(i*10000, (i+1)*10000);
					}
					EAISynTemplate.executeBatch(ctx, dataBase, sumSqls);
				
				}
			} catch (BOSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		/**
		 * 
		 * 获取仓库与门诊映射关系
		 * 
		 */
		@Override
		protected String _getWareclinicRales(Context ctx,String jsonStr ) throws BOSException {
			BaseResponseDTO respondDTO = new BaseResponseDTO();
			PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
			String msgId = "";
			String busCode ="";
			String reqTime ="";
			if(jsonStr !=null && !"".equals(jsonStr)){
				System.out.println("************************json begin****************************");
				System.out.println("#####################jsonStr################=" + jsonStr);
				DateBaseProcessType processType = DateBaseProcessType.AddNew;
				DateBasetype baseType = DateBasetype.WareClinicRale_S;
				
				JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
				JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
				JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
				JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
				
				PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");

				if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
						busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
						reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
					
					msgId = msgIdJE.getAsString();
					if(busCodeJE.getAsString().equals(baseType.getName())){
						String sql ="select c.FID WID, c.FNUMBER WNO ,c.FNAME_L2 WNA ,b.CFDATASTATE WST ,d.FID SID,d.FNUMBER SNO ,d.FNAME_L2 SNA,a.CFDATASTATE SST "+
						" from CT_CUS_WareCREDE a "+
						" inner join CT_CUS_WareClinicRaleEntry b on a.FPARENTID = b.FID "+
						" inner join T_DB_WAREHOUSE c on b.CFWAREHOUSEID =  c.FID "+
						" inner join T_ORG_STORAGE d on a.CFCLINICID = d.FID "+
						" where c.FSTORAGEORGID = 'jbYAAAMU2SvM567U'" ;
						IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);  
						if(rs!=null && rs.size() > 0){
							try {
			 					List<SOrgDTO> list= new ArrayList<SOrgDTO>();
			 					 Map<String,WareDTO> mpwd = new HashMap<String,WareDTO>();
								while(rs.next()){	
									
									WareDTO wd = new WareDTO();
									wd.setWid(rs.getString("WID"));
									wd.setWno(rs.getString("WNO"));
									wd.setWna(rs.getString("WNA"));
									wd.setWst(Integer.parseInt(rs.getString("WST")));
									mpwd.put(rs.getString("WID"), wd);
									
									SOrgDTO dto = new SOrgDTO();
									dto.setWid(rs.getString("WID"));
									dto.setSid(rs.getString("SID"));
									dto.setSna(rs.getString("SNA"));
									dto.setSno(rs.getString("SNO"));
									dto.setSst(Integer.parseInt(rs.getString("SST")));
									list.add(dto);
			 					}
								if(list !=null && list.size() >0 && mpwd !=null && mpwd.size() > 0){
									Map<String, List<SOrgDTO>>  mp = groupingArrayList(list);
									//result = gson.toJson(mp);
									List<WareDTO> returnLs = new ArrayList<WareDTO>();
									Set wids = mp.keySet();
									for(Object oid:wids){
										 WareDTO d = mpwd.get(oid);
										 List<SOrgDTO> ls = mp.get(oid);
										 d.setClinics(ls);
										 returnLs.add(d);
									}
									respondDTO.setMsg(returnLs);
									purPlatMenu = PurPlatSyncEnum.SUCCESS;
								}
							} catch (SQLException e) {
			 					e.printStackTrace();
			 					purPlatMenu=PurPlatSyncEnum.EXCEPTION_SERVER;
							}
						}
						
					}else
						purPlatMenu = PurPlatSyncEnum.BUSCODE_EXCEPTION;
				}else
					purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
				
			}else
				purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
			
			respondDTO.setCode(purPlatMenu.getValue());
			respondDTO.setMsgId(msgId);
			if(purPlatMenu!=PurPlatSyncEnum.SUCCESS)
				respondDTO.setMsg(purPlatMenu.getAlias());
			
			return new GsonBuilder().disableHtmlEscaping().create().toJson(respondDTO); 
		}
    
		 //第一种分组的方法
	    private static Map<String, List<SOrgDTO>> groupingArrayList(List<SOrgDTO> list){
	        Map<String, List<SOrgDTO>> map = new HashMap<String,List<SOrgDTO>>();
	        //分组
	        for (SOrgDTO l:list){
	            if (map.containsKey(l.getWid())) {  //判断是否存在该key
	                map.get(l.getWid()).add(l);   //存在就获取该key的value然后add
	            } else {
	                List<SOrgDTO> lt = new ArrayList<SOrgDTO>();
	                lt.add(l);
	                map.put(l.getWid(), lt);  //不存在就put
	            }
	        }
	        return map;
	    }

	    /**
	     * 查询集采客户
	     */
		@Override
		protected String _getCenterPurCustomer(Context ctx, String jsonStr)
				throws BOSException {
			String result = null;
			//Gson gson = new Gson();
			BaseResponseDTO respondDTO = new BaseResponseDTO();
			PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
			String msgId = "";
			String busCode ="";
			String reqTime ="";
			if(jsonStr !=null && !"".equals(jsonStr)){
				System.out.println("************************json begin****************************");
				System.out.println("#####################jsonStr################=" + jsonStr);
				DateBaseProcessType processType = DateBaseProcessType.Search;
				DateBasetype baseType = DateBasetype.Customer;
				
				JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
				JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
				JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
				JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
				
				PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");

				if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
						busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
						reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
					
					msgId = msgIdJE.getAsString();
					if(busCodeJE.getAsString().equals(baseType.getName())){
						String sql =" select a.FID cusid ,a.FNUMBER cusno ,a.FNAME_L2 cusna,b.FNUMBER cateno,b.FNAME_L2 catena, "+
									" a.FISINTERNALCOMPANY as isinner, FInternalCompanyID eascompanyid from t_bd_customer a "+
									" inner join T_BD_CSSPGroup b on a.FBROWSEGROUPID =b.FID "+
									" where a.cfissend =1 and a.FUSEDSTATUS =1 "+
									" and b.fid in ('4maBHK6PR3G0grhmW6XAbnolaaI=','vtDAX1FNQxW+LWlYA1QIWXolaaI=') ";
						IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);  
						if(rs!=null && rs.size() > 0){
							try {
			 					List list= new ArrayList();
								while(rs.next()){	
									HashMap<String,String> mp = new HashMap<String,String>();
									mp.put("cusid", rs.getString("cusid"));
									mp.put("cusno", rs.getString("cusno"));
									mp.put("cusna", rs.getString("cusna"));
									mp.put("cateno", rs.getString("cateno"));
									mp.put("isinner", rs.getString("isinner"));
									if(rs.getObject("eascompanyid") !=null && !"".equals(rs.getObject("eascompanyid").toString()))
										mp.put("eascompanyid", rs.getObject("eascompanyid").toString());
									else
										mp.put("eascompanyid", "");

									list.add(mp);
			 					}
								
								if(list !=null && list.size() > 0){
									respondDTO.setMsg(list);
								}
							 
							} catch (SQLException e) {
			 					e.printStackTrace();
			 					purPlatMenu=PurPlatSyncEnum.EXCEPTION_SERVER;
							}
						}
						
					}else
						purPlatMenu = PurPlatSyncEnum.BUSCODE_EXCEPTION;
				}else
					purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
				
			}else
				purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
			
			respondDTO.setCode(purPlatMenu.getValue());
			respondDTO.setMsgId(msgId);
			if(purPlatMenu!=PurPlatSyncEnum.SUCCESS)
				respondDTO.setMsg(purPlatMenu.getAlias());
			
			return new GsonBuilder().disableHtmlEscaping().create().toJson(respondDTO); 
		}
    
	    
		
		protected String  sendBaseDataToB2B(Context ctx, String jsonStr)
		throws BOSException {
			//return super._savePaymentBill(ctx, jsonStr);
			String result = "";
			try {
				HttpClient httpClient = new HTTPSTrustClient().init();
				result += HTTPSClientUtil.doPostJson(httpClient, InterfaceResource.sap_base_url, jsonStr);			
			} catch (Exception e) {
					e.printStackTrace();
			} 
			return result;
		}
		
		 
	    
}