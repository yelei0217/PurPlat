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
import com.kingdee.bos.workflow.define.ProcessType;
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


    private static String warurl = "http://sr.wellekq.com:10090/his-war/notify/receiveClinicStore"; //���Ե�ַ

    private static String warJinYongurl = "http://sr.wellekq.com:10090/his-war/notify/updateClinicStoreStatus"; //���Ե�ַ
    
    /**
     * type   :  1:�ͻ�  2����Ӧ��  3����֯  4 ��Ա  5 �ֿ�   
     * newOrDele : 0:���� ; 1���� ; 2:����
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
    			getlogInfo(ctx , map ,  DateBasetype.Customer ,DateBaseProcessType.AddNew ,flag);
    		}else if(type ==2){
    			getlogInfo(ctx , map ,  DateBasetype.Supplier ,DateBaseProcessType.AddNew,flag);
    			  
    		}else if(type ==3){
    			getlogInfo(ctx , map ,  DateBasetype.orgUnit ,DateBaseProcessType.AddNew,flag);
    		}else if(type ==4){
    			getlogInfo(ctx , map ,  DateBasetype.Person ,DateBaseProcessType.AddNew,flag);
    			

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
						e.printStackTrace();
					}
				}

    		}else if(type ==5){
    			Map<String, String> mapWar = new  HashMap<String, String>();
    			mapWar = (Map) JSONObject.parse(data);
    			Map<String, String> mapRet = new  HashMap<String, String>();
    			DateBaseProcessType processType = null;
    			String orgid = mapWar.get("forgId").toString();
    			if( "jbYAAAMU2SvM567U".equals(orgid)){//B2B
					 
				}else{//��his 
					List<Map<String,String>> eMps = new ArrayList<Map<String,String>>();
					if(newOrDele == 0 ){
						eMps.add(mapWar);
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("subList", eMps);
						
						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 , warurl);
						logger.info("���Ͳֿ�,"+mapWar+"֪ͨ��hisϵͳ��result��" + result);
						System.out.println("########  result ########"+result);
						
						mapRet = (Map) JSONObject.parse(result);  
						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
							flag=true;
						}
						map.put("RESJSON", result);
						map.put("JSON", JSONObject.toJSONString(eMps));
						processType = DateBaseProcessType.AddNew;
					}else if(newOrDele == 1 ){ 
						Map<String, String> mapNew = new  HashMap<String, String>();
						mapNew.put("fid",mapWar.get("fid").toString()); 
						mapNew.put("fstatus","1" );
						
						eMps.add(mapNew);
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("subList", eMps);
						
						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 ,warJinYongurl);
						logger.info("���Ͳֿ�������Ϣ,"+mapWar+"֪ͨ��hisϵͳ��result��" + result);
						System.out.println("########  result ########"+result);
						mapRet = (Map) JSONObject.parse(result);  
						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
							flag=true;
						}
						map.put("RESJSON", result);
						map.put("JSON", JSONObject.toJSONString(eMps));
						processType = DateBaseProcessType.ENABLE; 
					}else if(newOrDele == 2 ){ 
						Map<String, String> mapNew = new  HashMap<String, String>();
						mapNew.put("fid",mapWar.get("fid").toString()); 
						mapNew.put("fstatus","2" );
						
						eMps.add(mapNew);
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("subList", eMps);
						
						System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
						String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1 ,warJinYongurl);
						logger.info("���Ͳֿ������Ϣ,"+mapWar+"֪ͨ��hisϵͳ��result��" + result);
						System.out.println("########  result ########"+result);
						mapRet = (Map) JSONObject.parse(result);  
						if(mapRet.get("flag") != null && "1".equals(String.valueOf(mapRet.get("flag")))){
							flag=true;
						}
						map.put("RESJSON", result);
						map.put("JSON", JSONObject.toJSONString(eMps));
						processType = DateBaseProcessType.DisAble;
					}
				}
    			getlogInfo(ctx , map,DateBasetype.FreeItem ,processType,flag );
    		}
		} catch (EASBizException e) {
			e.printStackTrace();
		}
		
    	
	}

	public PurPlatSyncdbLogInfo getlogInfo(Context ctx , Map<String, String> map,DateBasetype dateBasetype,DateBaseProcessType processType, boolean flag)
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
		loginfo.setProcessType(processType);
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
					ids= ids.append(fid+"',"); 
				}  
			}
			
			if(ids.length() > 1 ){
				ids = new StringBuffer().append( ids.substring(0, ids.length()-1) );
				String upsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
				for(Map.Entry<String, String> entry : typemap.entrySet()){
				    String id = entry.getKey();
				    String type = entry.getValue(); 
				    if(type.equals("6") && null !=msgmap.get(id) && !"".equals(msgmap.get(id).toString())){//����
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
		super._DoErrorJon(ctx, data);
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
					ids= ids.append(fid+"',"); 
				}  
			}
			
			if(ids.length() > 1 ){
				ids = new StringBuffer().append( ids.substring(0, ids.length()-1) );
				String upsql = " update CT_CUS_PurPlatSyncdbLog set cfIsSync = 1 where  fid in ("+ids+") ";
				com.kingdee.eas.custom.util.DBUtil.execute(ctx,upsql);
				
				for(Map.Entry<String, String> entry : typemap.entrySet()){
				    String id = entry.getKey();
				    String type = entry.getValue(); 
				    if(type.equals("6") && null !=msgmap.get(id) && !"".equals(msgmap.get(id).toString())){//����
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
		data  ="{\"msgId\":\"pkKBgt311111\",\"operType\":0,\"reqCount\":1,\"reqTime\":\"20220715121020\",\"data\":[{\"fNumber\":\"CSqq001\",\"fName\":\"��������001\",\"fModel\":\"�ͺ�\",\"fMaterialGroup\":\"W303\",\"fArtNo\":\"fArtNo\",\"fBrand\":\"fBrand\",\"fCreateTime\":\"2022-07-20\",\"fUpdateTime\":\"2022-07-20\",\"fKAClass\":\"erjg\",\"fBaseUnit\":\"G01\",\"fInvUnit\":\"G04\",\"fPurUnit\":\"G04\",\"fSaleUnit\":\"G04\"}]}";
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
			
			if("0".equals(map.get("operType").toString())){
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
				/*if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
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
				}*/
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
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				
				if (dataMap.get("fCreateTime")!= null &&  !"".equals(dataMap.get("fCreateTime").toString()) ) { 
					String createTime = dataMap.get("fCreateTime").toString();
					
					try {
						Date date = sdf1.parse(createTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĵ���ʱ���ʽ����ȷ;"; flag = false;
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
						error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"������޸�ʱ���ʽ����ȷ;"; flag = false;
						continue;
						
					}
					
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
			}if("1".equals(map.get("operType").toString())){//�޸�
				
				if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
					error = error+ "���ϱ��벻��Ϊ��;"; flag = false;
					continue;
				}
				
				 
				String  number  = dataMap.get("fNumber").toString() ;
				try {
					if (  !imbiz.exists("where number = '"+number+"'") ) { 
						error = error+ "���ϱ��벻����;"; flag = false;
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
				/*if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
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
				}*/
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
				
				
			}else if("2".equals(map.get("operType").toString())){
				if (dataMap.get("FId")== null || "".equals(dataMap.get("FId").toString()) ) { 
					error = error+ "����ID����Ϊ��;"; flag = false;
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
		//0-������1-�޸ģ�2-����
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
	   	            return  e.getMessage();
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
	   	                return  ex.getMessage();
	   	            }
	   	        }
	        }
	       
	       return result;
	   }
	
	
    
    
}