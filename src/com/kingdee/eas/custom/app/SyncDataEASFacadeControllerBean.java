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

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
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
import com.kingdee.eas.custom.PurPlatSyncdbLogFactory;
import com.kingdee.eas.custom.PurPlatSyncdbLogInfo;
import com.kingdee.eas.custom.app.unit.MaterialUntil;
import com.kingdee.jdbc.rowset.IRowSet;

public class SyncDataEASFacadeControllerBean extends AbstractSyncDataEASFacadeControllerBean{
	
	
    private static Logger logger =
        Logger.getLogger("com.kingdee.eas.custom.app.SyncDataEASFacadeControllerBean");


    private static String warurl = "http://sr.wellekq.com:10090/his-/his-war/notify/receiveClinicStore"; //���Ե�ַ

    private static String warJinYongurl = "http://sr.wellekq.com:10090//his-war/notify/updateClinicStoreStatus"; //���Ե�ַ
    
    /**
     * type   :  1:�ͻ�  2����Ӧ��  3����֯  4 ��Ա  5 �ֿ�   
     * newOrDele �� 1 ���� 0 ����
     */
    @Override
	protected void _syncDateByType(Context ctx, int type, String data,
			int newOrDele, String name, String number) throws BOSException {
		// TODO Auto-generated method stub
		//super._syncDateByType(ctx, type, data, newOrDele, name, number);

    	boolean flag = false;
    	try {
    		Map<String, String> map = new  HashMap<String, String>();
        	map.put("FNUMBER", number);
        	map.put("FNAME", name);
        	map.put("JSON", data);
        	
        	String retMsg = "";
        	map.put("RESJSON", "");
    		if(type ==1){ 
    			getlogInfo(ctx , map ,  DateBasetype.Customer  ,flag);
    		}else if(type ==2){
    			getlogInfo(ctx , map ,  DateBasetype.Supplier ,flag);
    			  
    			CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
    			
    			String supsql = " /*dialect*/ SELECT supplier.fid  fId, supplier.fnumber fNumber, supplier.fname_l2  fName ,'' fOpenBank , '' fBankAccount  ,  "+
    			  " cuser.fname_l2  fCreator ,  to_char( supplier.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( supplier.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime   "+
    			  " FROM   T_BD_Supplier supplier   "+
    			  " inner join  T_PM_User  cuser on cuser.fid= supplier.FCREATORID  "+
          		  " where  supplier.fnumber  = '"+number+"' ";
	    		IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,supsql);
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
							
							map.put("fIsGroup",map.get("FISGROUP") ); 
							map.put("fOrgNumber",companyInfo.getNumber() ); 
							map.put("fOrgName",companyInfo.getName() ); 
							map.put("fStatus","0" ); 
							map.put("fOrgtId",companyInfo.getId().toString() ); 
							map.put("fUpdateType",map.get("FSTATUS") );  
							
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
    		}else if(type ==3){
    			getlogInfo(ctx , map ,  DateBasetype.orgUnit ,flag);
    		}else if(type ==4){
    			getlogInfo(ctx , map ,  DateBasetype.Person ,flag);
    			

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
    			Map<String, String> mapWar = new  HashMap<String, String>();
    			mapWar = (Map) JSONObject.parse(data);
    			String orgid = map.get("FORGTID").toString();
    			if( "jbYAAAMU2SvM567U".equals(orgid)){//B2B
					 
				}else{//��his
					

					List<Map<String,String>> eMps = new ArrayList<Map<String,String>>();
					if(newOrDele ==1 ){
						eMps.add(mapWar);
						//Map<String,Object> mp = new HashMap<String,Object>();
						//mp.put("subList", eMps);
						
						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 , warurl);
						logger.info("���Ͳֿ�,"+mapWar+"֪ͨ��hisϵͳ��result��" + result);
						System.out.println("########  result ########"+result);
						map.put("RESJSON", result);
						map.put("JSON", JSONObject.toJSONString(eMps));
					}else if(newOrDele ==0 ){ 
						Map<String, String> mapNew = new  HashMap<String, String>();
						mapNew.put("fId",mapWar.get("fId").toString()); 
						mapNew.put("fStatus","2" );
						
						eMps.add(mapNew);
						//Map<String,Object> mp = new HashMap<String,Object>();
						//mp.put("subList", eMps);
						
						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 ,warJinYongurl);
						logger.info("���Ͳֿ������Ϣ,"+mapWar+"֪ͨ��hisϵͳ��result��" + result);
						System.out.println("########  result ########"+result);
						map.put("RESJSON", result);
						map.put("JSON", JSONObject.toJSONString(eMps));
					}
					
				}
    			
    			getlogInfo(ctx , map ,  DateBasetype.FreeItem ,flag );
    		}
    		 
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
	}

	public PurPlatSyncdbLogInfo getlogInfo(Context ctx , Map<String, String> map,DateBasetype dateBasetype, boolean flag)
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
		loginfo.setStatus(flag);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		loginfo.setIsSync(false); 
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(loginfo);
		return loginfo;
	}

	@Override
	protected void _DoErrorJon(Context ctx, String data) throws BOSException {
		// TODO Auto-generated method stub
		super._DoErrorJon(ctx, data);
		StringBuffer ids = new StringBuffer();
		ids= ids.append("'");
		try {
			String sql = " select  fid ,cfdatebasetype TYPE , cfmessage  MESSAGE    from  CT_CUS_PurPlatSyncdbLog where  cfstatus = 0 and  CFSendCount < 4  and  cfIsSync = 0 ";
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
					ids= ids.append(fid+"',"); 
				}  
			}
			
			if(ids.length() > 1 ){
				ids = new StringBuffer().append( ids.substring(0, ids.length()-1) );
				String upsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
				for(Map.Entry<String, String> entry : msgmap.entrySet()){
				    String id = entry.getKey();
				    String type = entry.getValue(); 
				    if(type.equals("6") && null !=msgmap.get(id) && !"".equals(msgmap.get(id).toString())){//����
				    	String msgjson = msgmap.get(id);
				    	
				    	MaterialUntil  ma = new MaterialUntil();
				    	ma.doCreateMaterial(ctx , msgjson);
				    }
				    
				}
				
				
				String upEndsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 0 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
			}
			
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	/*
	fNumber	���ϱ���
	fName	��������
	fModel	����ͺ�
	fMaterialGroup	�������
	fModel	�ͺ�
	fArtNo	����
	fBrand	Ʒ��
	fCreateTime	����ʱ��
	fUpdateTime	����޸�ʱ��
	fKAClass	���˷���
	fBaseUnit	����������λ
	fInvUnit	��������λ
	fPurUnit	�ɹ�������λ
	fSaleUnit	���ۼ�����λ
	
	
		
	*/
	@Override
	protected String _materialSyncFun(Context ctx, String data)
			throws BOSException {
		// TODO Auto-generated method stub
		//return super._materialSyncFun(ctx, data); 
		Map map = (Map) JSONObject.parse(data);
		 
		HashMap<String, String> returnMap =new  HashMap<String, String>();
		returnMap.put("code", "-1");
		
		if (map.get("msgId")== null || "".equals(map.get("msgId").toString()) ) { 
			returnMap.put("msg", "����Id����Ϊ�ա�");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		returnMap.put("msgId", map.get("msgId").toString() );
		if (map.get("reqCount")== null || "".equals(map.get("reqCount").toString()) ) { 
			returnMap.put("msg", "������������Ϊ�ա�");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		
		if (map.get("operType")== null || "".equals(map.get("operType").toString()) ) {  //0-������1-�޸ģ�2-����
			returnMap.put("msg", "�������Ͳ���Ϊ�ա�");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		 
		if (map.get("data")== null || "".equals(map.get("data").toString()) ) {  
			returnMap.put("msg", "�������鲻��Ϊ�ա�");
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
			if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
				error = error+ "���ϱ��벻��Ϊ��;"; flag = false;
				continue;
			}
			
			 
			String  number  = dataMap.get("fNumber").toString() ;
			try {
				if ( !"0".equals(map.get("operType").toString()) && imbiz.exists("where number = '"+number+"'") ) { 
					error = error+ "���ϱ����Ѵ���;"; flag = false;
					continue;
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (dataMap.get("fName")== null || "".equals(dataMap.get("fName").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����Ʋ���Ϊ��"; flag = false;
				continue;
			}  
			if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"���ͺŲ���Ϊ��";  flag = false;
				continue;
			}
			if (dataMap.get("fArtNo")== null || "".equals(dataMap.get("fArtNo").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ��Ų���Ϊ��"; flag = false;
				continue;
			}
			
			if (dataMap.get("fBrand")== null || "".equals(dataMap.get("fBrand").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"��Ʒ�Ʋ���Ϊ��"; flag = false;
				continue;
			}
			if (dataMap.get("fMaterialGroup")== null || "".equals(dataMap.get("fMaterialGroup").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����������Ϊ��"; flag = false;
				continue;
			}
			try {
				if(!MaterialGroupFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fMaterialGroup").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"��������𲻴���"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (dataMap.get("fCreateTime")== null || "".equals(dataMap.get("fCreateTime").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĵ���ʱ�䲻��Ϊ��"; flag = false;
				continue;
			}
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf1.parse(createTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĵ���ʱ���ʽ����ȷ;"; flag = false;
				continue;
				
			}
			if (dataMap.get("")== null || "".equals(dataMap.get("fUpdateTime").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"������޸�ʱ�䲻��Ϊ��"; flag = false;
				continue;
			}
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			try {
				Date date = sdf1.parse(updateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"������޸�ʱ���ʽ����ȷ;"; flag = false;
				continue;
				
			}
			
			if (dataMap.get("fKAClass")== null || "".equals(dataMap.get("fKAClass").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�ļ��˷��಻��Ϊ��"; flag = false;
				continue;
			}
			try {
				if(!KAClassficationFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fKAClass").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�ļ��˷��಻����"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataMap.get("fBaseUnit")== null || "".equals(dataMap.get("fBaseUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ���������λ����Ϊ��"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fBaseUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ���������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fInvUnit")== null || "".equals(dataMap.get("fInvUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ŀ�������λ����Ϊ��"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fInvUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ŀ�������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (dataMap.get("fPurUnit")== null || "".equals(dataMap.get("fPurUnit").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĳɹ�������λ����Ϊ��"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fPurUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĳɹ�������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fSaleUnit")== null || "".equals(dataMap.get("fSaleUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����ۼ�����λ����Ϊ��"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fSaleUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����ۼ�����λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	    * ��ָ�� URL ����POST����������
	    * 
	    * @param url
	    *            ��������� URL
	    * @param param
	    *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	    * @param isproxy
	    *               �Ƿ�ʹ�ô���ģʽ
	    * @return ������Զ����Դ����Ӧ���
	    */
		public static String sendMessageToHISPost(String param,int oper,String url) {
			OutputStreamWriter out = null;
	        BufferedReader in = null;
	        String result = "";  
	        if(url != null && !"".equals(url)){
	        	try {
	        		URL realUrl = new URL(url);
	   	            HttpURLConnection conn =  (HttpURLConnection) realUrl.openConnection();
	   	             // �򿪺�URL֮�������
	   	            // ����POST�������������������
	   	            conn.setDoOutput(true);
	   	            conn.setDoInput(true);
	   	            conn.setRequestMethod("POST");    // POST����
	   	            // ����ͨ�õ���������
	   	            conn.setRequestProperty("accept", "*/*");
	   	            conn.setRequestProperty("connection", "Keep-Alive");
	   	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	   	          	conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	   	            conn.connect();
	   	            // ��ȡURLConnection�����Ӧ�������
	   	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	   	            // �����������
	   	            out.write(param);
	   	            System.out.println("param------------------------"+param);
	   	            // flush������Ļ���
	   	            out.flush();
	   	            // ����BufferedReader����������ȡURL����Ӧ
	   	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	   	            String line;
	   	            while ((line = in.readLine()) != null) {
	   	                result += line;
	   	            }
	   	        } catch (Exception e) {
	   	            System.out.println("���� POST ��������쳣��"+e);
	   	            e.printStackTrace();
	   	        }
	   	        //ʹ��finally�����ر��������������
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
	   	            }
	   	        }
	        }
	       
	       return result;
	   }
	
	
    
    
}