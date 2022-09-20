package com.kingdee.eas.custom.app.unit;


import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectStringPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.entity.SelectorItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.sql.ParserException;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.base.core.fm.ContextHelperFactory;
import com.kingdee.eas.basedata.assistant.KAClassficationFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitCollection;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.material.EquipmentPropertyEnum;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupCollection;
import com.kingdee.eas.basedata.master.material.MaterialGroupFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupInfo;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.master.material.UsedStatusEnum;
import com.kingdee.eas.basedata.org.CtrlUnitFactory;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.PurPlatSyncdbLogCollection;
import com.kingdee.eas.custom.PurPlatSyncdbLogFactory;
import com.kingdee.eas.custom.PurPlatSyncdbLogInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.jdbc.rowset.IRowSet;

public class MaterialUntil {

	public String doCreateMaterial(Context ctx, String data) throws BOSException{
		
		//data = "{\"msgId\":\"pkKBgt311111\",\"operType\":0,\"reqCount\":1,\"reqTime\":\"20220715121020\",\"data\":[{\"fNumber\":\"CSqq012\",\"fName\":\"测试物料012\",\"fModel\":\"型号\",\"fMaterialGroup\":\"W1\",\"fArtNo\":\"fArtNo\",\"fBrand\":\"fBrand\",\"fCreateTime\":\"2022-07-20\",\"fUpdateTime\":\"2022-07-20\",\"fKAClass\":\"erjg\",\"fBaseUnit\":\"G01\",\"fInvUnit\":\"G04\",\"fPurUnit\":\"G04\",\"fSaleUnit\":\"G04\"}]}";
		Map map = (Map) JSONObject.parse(data);
		String operType = map.get("operType").toString(); 
		String msgid = map.get("msgId").toString(); 
		com.alibaba.fastjson.JSONArray  jsonArr = (com.alibaba.fastjson.JSONArray) map.get("data");     
		String error = "";
		if("0".equals(operType)){//新增
			error = createMaterial(  ctx, jsonArr );
		}else if("1".equals(operType)){//修改
			try {
				error = updateMaterial(  ctx, jsonArr );
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = e.getMessage();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = e.getMessage();
			}
		}else if("2".equals(operType)){//删除
			try {
				error = endMaterial(  ctx, jsonArr );
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = e.getMessage();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = e.getMessage();
			}
		}
		try {
			PurPlatSyncdbLogCollection  logColl = PurPlatSyncdbLogFactory.getLocalInstance(ctx).getPurPlatSyncdbLogCollection( " where name = '"+msgid+"' ");
			
			
			 for(int i= 0 ; i < logColl.size() ; i++ ){
				 PurPlatSyncdbLogInfo  log  = logColl.get(i);
				 if(!"".equals(error)){
						log.setStatus(false);
						log.setErrorMessage(error);
						log.setSendCount(1);
					}else{
						log.setStatus(true);
						log.setSendCount(1);
					}
					PurPlatSyncdbLogFactory.getLocalInstance(ctx).save(log);
			 } 
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
		
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
	
	
	private ParallelSqlExecutor setAllCtrlComOrgs(Context ctx , String kaclass , String materialid , ParallelSqlExecutor pe) throws BOSException, SQLException { 
		
		String userId = ContextHelperFactory.getLocalInstance(ctx).getCurrentUser().getId().toString(); 
		StringBuffer sbr = new  StringBuffer(); 
	 	sbr.append(" /*dialect*/SELECT cunit.FID CID ,nvl(kacl.fid,'') LID FROM T_ORG_CtrlUnit cunit  left join T_BD_KAClassfication kacl on kacl.fnumber = '"+kaclass+"' and  kacl.FCURRENCYCOMPANY  = cunit.fid  \r\n"); 
	 	sbr.append(" WHERE (cunit.FID <> '11111111-1111-1111-1111-111111111111CCE7AED4') AND cunit.FIsOUSealUp = 0 AND (cunit.FLongNumber LIKE 'M%')    \r\n"); 
	 	sbr.append(" ORDER BY cunit.FLongNumber ASC     \r\n");   
	 	IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, sbr.toString());
		if(rs!=null){
			while(rs.next()){
				String cityid = rs.getObject("CID").toString();
				String kaclid = "";
				if(rs.getObject("LID") != null && !"".equals(rs.getObject("LID").toString()) ){
					kaclid = rs.getObject("LID").toString();
				} 
				
				String compinfoid  = BOSUuid.create("D431F8BB").toString();
				
				StringBuffer comsbr  = new StringBuffer(" INSERT INTO T_BD_MaterialCompanyInfo ( fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID ,  FMATERIALID , FCOMPANYID ,FKACLASSID , ");
			    comsbr.append(" FACCOUNTTYPE  , FSTANDARDCOST  ,FEFFECTEDSTATUS ,FCALCULATETYPE ,FSTATUS ,FCREATECOBYORDER ,FISLOT   ,FISASSISTPROPERTY ,FISPROJECT ,FISTRACKNUMBER  ) ");
			    comsbr.append(" values( '"+compinfoid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cityid+"',  '"+materialid+"' , '"+cityid+"', '"+kaclid+"' , ");
			    comsbr.append(" 4, 0 ,2,0,1,0,0,0,0,0	 )"); 
				pe.getSqlList().add(comsbr);
				if(!"00000000-0000-0000-0000-000000000000CCE7AED4".equals(cityid)){
					 
					StringBuffer comsbrsql = new  StringBuffer(); 
					comsbrsql.append(" /*dialect*/select c1.fid  FID , kacl.FID KID from t_org_company c1   \r\n"); 
					comsbrsql.append(" left join T_BD_KAClassfication kacl on kacl.fnumber = '"+kaclass+"' and  kacl.FCURRENCYCOMPANY  = c1.fid  \r\n"); 
					comsbrsql.append(" where c1.fcontrolunitid = '"+cityid+"'   and   c1.fid !='"+cityid+"'  and    \r\n"); 
					comsbrsql.append(" not exists (   \r\n"); 
					comsbrsql.append(" select 1 from T_BD_MaterialCompanyInfo mc2     \r\n"); 
					comsbrsql.append(" inner join t_org_company org2 on org2.fid = mc2.FCompanyID    \r\n"); 
					comsbrsql.append(" where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FCompanyID = c1.fid    \r\n"); 
					comsbrsql.append(" and mc2.FMaterialID='"+materialid+"' )    \r\n"); 
					
					IRowSet rsCom = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, comsbrsql.toString());
					if(rsCom!=null && rsCom.size() > 0){
						while(rsCom.next()){
							String comid = rsCom.getObject("FID").toString();
							
							String kacComlid = "";
							if(rsCom.getObject("KID") != null && !"".equals(rsCom.getObject("KID").toString()) ){
								kacComlid = rsCom.getObject("KID").toString();
							} 
							 
							String compinComid  = BOSUuid.create("D431F8BB").toString();
							StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialCompanyInfo ( fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID ,  FMATERIALID , FCOMPANYID ,FKACLASSID , ");
						    sbr2.append(" FACCOUNTTYPE  , FSTANDARDCOST  ,FEFFECTEDSTATUS ,FCALCULATETYPE , FSTATUS ,FCREATECOBYORDER ,FISLOT   ,FISASSISTPROPERTY ,FISPROJECT ,FISTRACKNUMBER  ) ");
						    sbr2.append(" values('"+compinComid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cityid+"',  '"+materialid+"' , '"+comid+"', '"+kacComlid+"' , ");
						    sbr2.append("  4, 0 ,2,0,1,0,0,0,0,0	 )"); 
						    pe.getSqlList().add(sbr2); 
						}
					}   
				}
				
			}
		} 
		return pe;
	}

	
	public   String createMaterial(Context ctx, com.alibaba.fastjson.JSONArray  jsonArr ) throws BOSException {
		boolean flag = true;
		String  error = new String();
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		int  size = jsonArr.size();
		for( int i = 0 ; i < size ; i++ ){
			Map dataMap = (Map) jsonArr.get(i);  
			
			Date credate = new Date();
			Date update = new Date();
			if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
				error = error+ "物料编码不能为空;"; flag = false;
				continue;
			} 
			String  number  = dataMap.get("fNumber").toString() ;
			try {
				if (  imbiz.exists("where number = '"+number+"'") ) { 
					error = error+ "物料编码"+number+"已存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (dataMap.get("fName")== null  || "".equals(dataMap.get("fName").toString()) ) { 
				error = error+ "编码为"+dataMap.get("fNumber").toString()+"的名称不能为空;"; flag = false;
				continue;
			}  
			String fModel = "";
			if (dataMap.get("fModel")!= null && !"".equals(dataMap.get("fModel").toString()) ) { 
				fModel = dataMap.get("fModel").toString();
			}
			String fArtNo = "";
			if (dataMap.get("fArtNo")!= null && !"".equals(dataMap.get("fArtNo").toString()) ) { 
				fArtNo = dataMap.get("fArtNo").toString();
			}
			String fBrand = "";
			if (dataMap.get("fBrand")!= null && !"".equals(dataMap.get("fBrand").toString()) ) { 
				fBrand = dataMap.get("fBrand").toString();
			}
			
			if (dataMap.get("fCreateTime")== null || "".equals(dataMap.get("fCreateTime").toString()) ) { 
				error = error+ "编码为"+dataMap.get("fNumber").toString()+"的创建时间不能为空;"; flag = false;
				continue;
			}
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				credate = sdf1.parse(createTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "编码为"+dataMap.get("fNumber").toString()+"的创建时间格式不正确;"; flag = false;
				continue;
				
			}
			if (dataMap.get("fUpdateTime")== null || "".equals(dataMap.get("fUpdateTime").toString()) ) { 
				error = error+  "编码为"+dataMap.get("fNumber").toString()+"的最后修改时间不能为空;"; flag = false;
				continue;
			}
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			try {
				update = sdf1.parse(updateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "编码为"+dataMap.get("fNumber").toString()+"的最后修改时间格式不正确;"; flag = false;
				continue;
				
			}
			
			if (dataMap.get("fKAClass")== null || "".equals(dataMap.get("fKAClass").toString()) ) { 
				error = error+  "编码为"+dataMap.get("fNumber").toString()+"的记账分类不能为空;"; flag = false;
				continue;
			}
			try {
				if(!KAClassficationFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fKAClass").toString()+"'")){
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的记账分类不存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataMap.get("fBaseUnit")== null || "".equals(dataMap.get("fBaseUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("fNumber").toString()+"的基本计量单位不能为空;"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fBaseUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的基本计量单位不存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fInvUnit")== null || "".equals(dataMap.get("fInvUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("fNumber").toString()+"的库存计量单位不能为空;"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fInvUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的库存计量单位不存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = error+  e.getMessage()+";"; flag = false;
			}
			
			
			if (dataMap.get("fPurUnit")== null || "".equals(dataMap.get("fPurUnit").toString()) ) { 
				error = error+ "编码为"+dataMap.get("fNumber").toString()+"的采购计量单位不能为空;"; flag = false;
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
				error = error+  e.getMessage()+";"; flag = false;
			}
			
			if (dataMap.get("fSaleUnit")== null || "".equals(dataMap.get("fSaleUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("fNumber").toString()+"的销售计量单位不能为空;"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fSaleUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("fNumber").toString()+"的销售计量单位不存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = error+  e.getMessage()+";"; flag = false;
			}
			try {
				
				String kaclass = dataMap.get("fKAClass").toString();
				
				MeasureUnitCollection measure = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitCollection(" where number = '"+dataMap.get("fPurUnit").toString()+"'");
				MeasureUnitCollection salemeasure = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitCollection(" where number = '"+dataMap.get("fSaleUnit").toString()+"'");
				
				MeasureUnitCollection kcmeasure = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitCollection(" where number = '"+dataMap.get("fInvUnit").toString()+"'");
			 
				
				
				MaterialInfo ma = createInfo(  ctx,   dataMap );
				IObjectPK pk = imbiz.addnew(ma);
				String userId = ContextHelperFactory.getLocalInstance(ctx).getCurrentUser().getId().toString(); 
				ExecutorService pool = Executors.newFixedThreadPool(6);
			    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
				ArrayList<String> ctrlOrgIds = getAllCtrlOrgIDs(ctx);
				 
				for(String cid:ctrlOrgIds){
					
					StringBuffer sbrdA  = new StringBuffer("insert into T_BD_DataBaseDAssign(fid,fcreatorid,fcreatetime,flastupdateuserid,flastupdatetime,fcontrolunitid,fdatabasedid,fassigncuid,fbosobjecttype,fstatus)");
					sbrdA.append("values (newbosid('6C8AF54D'),'"+userId+"',sysdate,'"+userId+"',sysdate,'00000000-0000-0000-0000-000000000000CCE7AED4','"+pk.toString()+"','"+cid+"','4409E7F0',0)"); 
		    		pe.getSqlList().add(sbrdA);
		    		
		    		
					String costid  = BOSUuid.create("C45E21AA").toString();
					String caigouid  = BOSUuid.create("0193BD9B").toString();
					String xiaoshouid  = BOSUuid.create("C84112CF").toString();
					String kucunid  = BOSUuid.create("557E499F").toString();
					
					//成本资料
					StringBuffer sbr  = new StringBuffer(" INSERT INTO T_BD_MaterialCost (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		sbr.append("FEFFECTEDSTATUS ,FSTORETYPE ,fiscreatecostobject ,fcostobejctgroupid,FDEFAULTCOSTITEMID ,FISPARTICIPATEREDUCT ,FEXPENSEID  ) ");
		    		sbr.append(" values('"+costid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		sbr.append(" 2	,1	,0,'','',0,''	 )"); 
		    		pe.getSqlList().add(sbr);
		    		
		    		 //采购资料  
				    StringBuffer pusbr  = new StringBuffer(" INSERT INTO T_BD_MaterialPurchasing (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
				    pusbr.append(" FUNITID , FRECEIVETOPRATIO ,FRECEIVEBOTTOMRATIO, FDAYDAHEAD ,FDAYSDELAY ,   FISRETURN ,FEFFECTEDSTATUS ,FPURCHASECHECK ,FISNOTCONTROLTIME ,FISNOTCONTROLQTY ,FISPURCHASECHECK , " +
				    		" FFINEQUALITYFAIRPRICE,FUSESUPPLYLIST ,FUSESUPPLYPRICE ,FQUOTAPOLICYID ,FMINDIVISIONQTY ,FPRICE  ,FPRICETOP , FQuotaPeriod) ");
				    pusbr.append(" values('"+caigouid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
				    pusbr.append(" '"+measure.get(0).getId().toString()+"' , 0,0,0,0    , 0 ,2,0,1,0 ,0   ,0,0,1,'SdQ8j/QxRueXfLDTP9izyRRbtvQ=',0 ,0,0,1	 )"); 
		    		pe.getSqlList().add(pusbr);
		    		 
		    		
		    		//销售资料 
		    		StringBuffer salesbr  = new StringBuffer(" INSERT INTO T_BD_MaterialSales (    fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		salesbr.append(" FISRETURN ,FISNOTCHECKONRETURN ,FISRECEIVBYCHECK ,FUNITID ,FISSUETOPRATIO ,FISSUEBOTTOMRATIO ,FDAYDAHEAD ,FDAYSDELAY ,FABCTYPE ,FEFFECTEDSTATUS ,FISPURBYSALE ,FISCONSIGNCHECK ,FINNERPRICERATE ,FCHEAPRATE ) ");
		    		salesbr.append(" values('"+xiaoshouid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		salesbr.append("  0,0,0,'"+salemeasure.get(0).getId().toString()+"',0,0,0,0,-1,2,0,0,0,0	 )"); 
		    		pe.getSqlList().add(salesbr);
		    		 
		    		//库存资料 
		    		StringBuffer kcsbr  = new StringBuffer(" INSERT INTO T_BD_MaterialInventory (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		
		    		kcsbr.append(" FISCONTROL ,FQTYSAFETY ,FQTYMIN ,FQTYMAX ,FDAYSBOTTOM  ,FDAYSTOP ,FDAYSTURNOVER ,FISNEGATIVE ,FISBATCHNO ,FISSEQUENCENO ,FISLOTNUMBER ,FISBARCODE ,FQTYMINPACKAGE ,FABCTYPE ,FISCOMPAGES ,FISSUEPRIORITYMODE ,  ");
		    		kcsbr.append(" FUNITID , FEFFECTEDSTATUS , FISPERIODVALID ,FPERIODVALID ,FPERIODVALIDUNIT ,FINWAREHSAHEAD , FOUTWAREHSAHEAD , FPREPWARNAHEAD ,FAHEADUNIT , FPLANNINGMODE ,FREBOOKQTY , FCONSUMESPEED ,FPURCHASINGAHEADDATE ,  ");
		    		kcsbr.append(" FBATCHPOLICY ,FFIXATIONBATCHQTY ,FDAYSPLANTURNOVER ,FCHEAPRATE ,FISCHECK ,FTOOLRATE ,FISPROJECTNUMBER ,FISTRACKINGNUMBER ,FISRESERVATION ,FRESERVATIONDATE ,FCLOSEDATECALMODE )  ");
		    		kcsbr.append("   ");
		    		kcsbr.append(" values( '"+kucunid+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		kcsbr.append("  0, 0,0,0, 0 , 0 , 0,0, 0 ,0, 0 , 0 ,0,-1  ,0 ,    -1 , '"+kcmeasure.get(0).getId().toString()+"' ,2       ,  0 ,0, 3,      0  , 0,    0 ,  3,      0 ,   0 ,0,0,1,0,0,0,  0 ,   0,   0,0,0,0,0 )"); 
		    		pe.getSqlList().add(kcsbr);
		    		  
		    		 
					if(!"00000000-0000-0000-0000-000000000000CCE7AED4".equals(cid)){ 
						
						//基础资料分配记录
						ArrayList<String> cityIds  =getNoAssignMaterialIDS(ctx,cid,pk.toString(),"data");
					    if(cityIds!=null &&cityIds.size()>0){
					    	for(String comid : cityIds){ 
					    		StringBuffer sbr2  = new StringBuffer("insert into T_BD_DataBaseDAssign(fid,fcreatorid,fcreatetime,flastupdateuserid,flastupdatetime,fcontrolunitid,fdatabasedid,fassigncuid,fbosobjecttype,fstatus)");
					    		sbr2.append("values (newbosid('6C8AF54D'),'"+userId+"',sysdate,'"+userId+"',sysdate,'00000000-0000-0000-0000-000000000000CCE7AED4','"+pk.toString()+"','"+comid+"','4409E7F0',0)"); 
					    		pe.getSqlList().add(sbr2);
					    		 
					    	}
					    }  
					    
					    
						//成本资料
						ArrayList<String> companyIds  =getNoAssignMaterialIDS(ctx,cid,pk.toString(),"cost");
					    if(companyIds!=null &&companyIds.size()>0){
					    	for(String comid : companyIds){
					    		String costidCom = BOSUuid.create("C45E21AA").toString();
					    		StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialCost (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
					    		sbr2.append("FEFFECTEDSTATUS ,FSTORETYPE ,fiscreatecostobject ,fcostobejctgroupid,FDEFAULTCOSTITEMID ,FISPARTICIPATEREDUCT ,FEXPENSEID  ) ");
					    		sbr2.append(" values('"+costidCom+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
					    		sbr2.append(" 2	,1	,0,'','',0,''	 )"); 
					    		pe.getSqlList().add(sbr2);
					    	}
					    }  
					    //采购资料 
					    ArrayList<String> pucompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"purchase");
					    if(pucompanyIds!=null &&pucompanyIds.size()>0){
					    	for(String comid : pucompanyIds){
					    		String caigouidCom  = BOSUuid.create("0193BD9B").toString();
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialPurchasing (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
							    sbr2.append(" FUNITID , FRECEIVETOPRATIO ,FRECEIVEBOTTOMRATIO, FDAYDAHEAD ,FDAYSDELAY ,FISRETURN ,FEFFECTEDSTATUS ,FPURCHASECHECK ,FISNOTCONTROLTIME ,FISNOTCONTROLQTY ,FISPURCHASECHECK , " +
							    		" FFINEQUALITYFAIRPRICE,FUSESUPPLYLIST ,FUSESUPPLYPRICE ,FQUOTAPOLICYID ,FMINDIVISIONQTY ,FPRICE  ,FPRICETOP , FQuotaPeriod  ) ");
							    sbr2.append(" values('"+caigouidCom+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append(" '"+measure.get(0).getId().toString()+"' , 0,0,0,0 , 0 ,2,0,1,0 ,0,0,0,0,'SdQ8j/QxRueXfLDTP9izyRRbtvQ=',0 ,0,0,1	 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }  					   
					    //销售资料 
					    ArrayList<String>  salecompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"sales");
					    if(salecompanyIds!=null &&salecompanyIds.size()>0){
					    	for(String comid : salecompanyIds){
					    		String xiaoshouidCom  = BOSUuid.create("C84112CF").toString();
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialSales (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID , ");
							    sbr2.append(" FISRETURN ,FISNOTCHECKONRETURN ,FISRECEIVBYCHECK ,FUNITID ,FISSUETOPRATIO ,FISSUEBOTTOMRATIO ,FDAYDAHEAD ,FDAYSDELAY ,FABCTYPE ,FEFFECTEDSTATUS ,FISPURBYSALE ,FISCONSIGNCHECK ,FINNERPRICERATE ,FCHEAPRATE )");
							    sbr2.append(" values('"+xiaoshouidCom+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append(" 0,0,0,'"+salemeasure.get(0).getId().toString()+"',0,0,0,0,-1,2,0,0,0,0		 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }  
					    //库存资料
					    ArrayList<String>  kccompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"inven");
					    if(kccompanyIds!=null &&kccompanyIds.size()>0){
					    	for(String comid : kccompanyIds){
					    		String kucunidCom  = BOSUuid.create("557E499F").toString();
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialInventory (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
			    		
							    sbr2.append(" FISCONTROL ,FQTYSAFETY ,FQTYMIN ,FQTYMAX ,FDAYSBOTTOM  ,FDAYSTOP ,FDAYSTURNOVER ,FISNEGATIVE ,FISBATCHNO , FISSEQUENCENO ,FISLOTNUMBER ,FISBARCODE ,FQTYMINPACKAGE ,FABCTYPE ,FISCOMPAGES ,FISSUEPRIORITYMODE ,  ");
							    sbr2.append(" FUNITID , FEFFECTEDSTATUS , FISPERIODVALID ,FPERIODVALID ,FPERIODVALIDUNIT ,FINWAREHSAHEAD , FOUTWAREHSAHEAD , FPREPWARNAHEAD ,FAHEADUNIT , FPLANNINGMODE ,FREBOOKQTY , FCONSUMESPEED ,FPURCHASINGAHEADDATE ,  ");
							    sbr2.append(" FBATCHPOLICY ,FFIXATIONBATCHQTY ,FDAYSPLANTURNOVER ,FCHEAPRATE ,FISCHECK ,FTOOLRATE ,FISPROJECTNUMBER ,FISTRACKINGNUMBER ,FISRESERVATION ,FRESERVATIONDATE ,FCLOSEDATECALMODE )  ");
							    sbr2.append("   ");
							    sbr2.append(" values('"+kucunidCom+"','"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append("  0, 0,0,0, 0 , 0 , 0,0, 0 ,0, 0 , 0 ,0,-1  ,0 ,    -1 , '"+kcmeasure.get(0).getId().toString()+"' ,2       ,  0 ,0, 3, 0  , 0,    0 ,  3,      0 ,   0 ,0,0,1,0,0,0,  0 ,   0,   0,0,0,0,0 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }   
			    		
					} 
				}
				//财务资料   单独处理
				try {
					pe = setAllCtrlComOrgs(ctx,kaclass,pk.toString() ,pe);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					error = error+e1.getMessage()+";";
					pool.shutdown(); 
				}
				   
			
				if(pe.getSqlList().size()>0){
					try {
						pe.executeUpdate(ctx); 
						pool.shutdown(); 
					} catch (EASBizException e) { 
						e.printStackTrace();
						pool.shutdown(); 
						error = error+e.getMessage()+";";
					} catch (BOSException e) { 
						e.printStackTrace();
						pool.shutdown(); 
						error = error+e.getMessage()+";";
					}	
				     pool.shutdown(); 
				}
					
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = error+e.getMessage()+";";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = error+e.getMessage()+";";
			}
			 
		}
		
		return error;
	}
	
	public   String endMaterial (Context ctx, com.alibaba.fastjson.JSONArray  jsonArr ) throws BOSException, EASBizException, ParseException {
		String  error = new String();
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		int  size = jsonArr.size();
		for( int i = 0 ; i < size ; i++ ){
			Map dataMap = (Map) jsonArr.get(i);  
			if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
				error = error+ "物料编码不能为空;";  
				continue;
			} 
			/*if (dataMap.get("fid")== null || "".equals(dataMap.get("fid").toString()) ) { 
				error = error+ "物料编码不能为空;";  
				continue;
			} */
			String  number  = dataMap.get("fNumber").toString() ;
			try {
				if (  imbiz.exists("where number = '"+number+"'") ) { 
					//imbiz.freeze(""); 
					EntityViewInfo view = new EntityViewInfo();
					view.getSelector().add(new SelectorItemInfo("number"));
					try {
						view.setFilter(new FilterInfo(number));
					} catch (ParserException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						error = error+e1.getMessage();
					}
					MaterialInfo material = imbiz.getMaterialCollection(view).get(0); 
					imbiz.unapprove(new ObjectUuidPK(material.getId()));
				}else{
					error = error+"此物料编码"+dataMap.get("fNumber")+"不存在;";
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				error = error+e1.getMessage();
			}
		}
		return error;
	}
	
	public   String updateMaterial (Context ctx, com.alibaba.fastjson.JSONArray  jsonArr ) throws BOSException, EASBizException, ParseException {
		String  error = new String();
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		int  size = jsonArr.size();
		for( int i = 0 ; i < size ; i++ ){
			boolean flag  = false;
			Map dataMap = (Map) jsonArr.get(i);  
			if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
				error = error+ "物料编码不能为空;";  
				continue;
			} 
			String  number  = dataMap.get("fNumber").toString() ;
			try {
				if (  !imbiz.exists("where number = '"+number+"'") ) { 
					error = error+ "根据物料编码"+number+"查询找不到对应物料信息;"; 
					continue;
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				error = error+e1.getMessage()+";";
			}
			 
		  
		  	
			MaterialInfo material = new MaterialInfo();
			FilterInfo filter = new FilterInfo();
			EntityViewInfo view = new EntityViewInfo();
			
			filter.getFilterItems().add(new FilterItemInfo("number",number,CompareType.EQUALS)); // 
			view.setFilter(filter);
			MaterialCollection materialColl = imbiz.getMaterialCollection(view);
			if(materialColl.size()>0){
				material = materialColl.get(0);
			}
			
			String updateSql = "";
			if (dataMap.get("fName")!= null && !"".equals(dataMap.get("fName").toString())
					&& !"".equals(material.getName()) && !material.getName().equals(dataMap.get("fName").toString()) ) { 
				material.setName(dataMap.get("fName").toString());flag  = true;
			}  
			if (dataMap.get("fModel")!= null && !"".equals(dataMap.get("fModel").toString())
					&& !"".equals( material.getModel()) && !material.getModel().equals(dataMap.get("fModel").toString())  ) { 
				material.setModel(dataMap.get("fModel").toString());flag  = true;
				 
			}
			if (dataMap.get("fArtNo")!= null && !"".equals(dataMap.get("fArtNo").toString())
					&& !"".equals( material.get("huohao")) && !material.get("huohao").toString().equals(dataMap.get("fArtNo").toString())  ) { 
				material.put("huohao",dataMap.get("fArtNo").toString());flag  = true;
			}
			
			if (dataMap.get("fBrand")!= null && !"".equals(dataMap.get("fBrand").toString())
					&& !"".equals( material.get("pinpai")) && !material.get("pinpai").toString().equals(dataMap.get("fBrand").toString())  ) { 
				material.put("pinpai",dataMap.get("fBrand").toString());flag  = true;
				 
			}
			if (dataMap.get("fMaterialGroup")== null || "".equals(dataMap.get("fMaterialGroup").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的物料类别不能为空;";  
				continue;
			}
			String magroup = dataMap.get("fMaterialGroup").toString();
			try {
				if(!MaterialGroupFactory.getLocalInstance(ctx).exists(" where number = '"+magroup+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的物料类别不存在;";  
					continue;
				}
				 
				FilterInfo filterNew = new FilterInfo();
				EntityViewInfo viewGroup = new EntityViewInfo();
				viewGroup.getSelector().add(new SelectorItemInfo("number"));
				
				filterNew.getFilterItems().add(new FilterItemInfo("number",dataMap.get("fMaterialGroup").toString(),CompareType.EQUALS)); 
				viewGroup.setFilter(filterNew);
				
				MaterialGroupInfo materialGroup = new MaterialGroupInfo();
				MaterialGroupCollection maco =  MaterialGroupFactory.getLocalInstance(ctx).getMaterialGroupCollection(viewGroup);
				if(maco.size()>0){
					materialGroup = maco.get(0);
				}
				String groupid = material.getMaterialGroup().getId().toString();
				if(!materialGroup.getId().toString().equals(groupid)){
					
					material.setMaterialGroup(materialGroup);flag  = true;
				}
				
				if(flag){
					imbiz.unapprove(new ObjectUuidPK(material.getId()));
					imbiz.save(material);
					imbiz.approve(new ObjectUuidPK(material.getId()));
				}
				
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return error;
	}
	
	public static MaterialInfo createInfo(Context ctx, Map dataMap ) throws BOSException, EASBizException, ParseException {
		MaterialInfo material = null;

		String materialGroup = dataMap.get("fMaterialGroup").toString();
		MaterialGroupInfo mgInfo = null;
		EntityViewInfo viewInfo = new EntityViewInfo();
		FilterInfo filter = new FilterInfo();
		filter.getFilterItems().add(new FilterItemInfo("number", materialGroup,CompareType.EQUALS));
		viewInfo.setFilter(filter);
		MaterialGroupCollection coll = MaterialGroupFactory.getLocalInstance(ctx).getMaterialGroupCollection(viewInfo);
		if ((coll != null) && (coll.size() > 0)) {
			mgInfo = coll.get(0);
			material = new MaterialInfo();
			material.setName(dataMap.get("fName").toString());
			material.setNumber(dataMap.get("fNumber").toString());
			material.put("isImport", Boolean.valueOf(true));
			material.put("notSupportRule", Boolean.valueOf(true));
			
			MeasureUnitCollection measunit= MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitCollection(" where number = '"+dataMap.get("fBaseUnit").toString()+"'");
			
			material.setBaseUnit(measunit.get(0));
			material.setMaterialGroup(mgInfo);
			CtrlUnitInfo cuInfo = CtrlUnitFactory.getLocalInstance(ctx).getCtrlUnitInfo(new ObjectStringPK("00000000-0000-0000-0000-000000000000CCE7AED4"));
			
			material.setAdminCU(cuInfo);
			material.setCU(cuInfo);
			material.setEquipProperty(EquipmentPropertyEnum.DEFAULT);
			material.setEffectedStatus(2);
			material.setStatus(UsedStatusEnum.APPROVED);
			material.setVersion(1);
			material.setPricePrecision(6);
			
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf1.parse(createTime); 
			Timestamp ts = new Timestamp(date.getTime());
			material.setCreateTime(ts);
			
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			Date updatedate = sdf1.parse(updateTime); 
			Timestamp updatets = new Timestamp(updatedate.getTime());
			material.setCreateTime(updatets);
			
			material.setModel(dataMap.get("fModel").toString());
			
			//material.setModel(dataMap.get("fModel").toString());
			
			material.put("huohao", dataMap.get("fArtNo").toString());
			material.put("pinpai", dataMap.get("fBrand").toString());
			 
			
		}
		return material;
	}
	
	

	  private static ArrayList<String> getAllCtrlOrgIDs(Context ctx){
		  ArrayList<String>  ids = new ArrayList<String>();
		  StringBuffer sbr = new  StringBuffer(); 
	 	  sbr.append(" SELECT FID FROM T_ORG_CtrlUnit  \r\n"); 
	 	  sbr.append(" WHERE (FID <> '11111111-1111-1111-1111-111111111111CCE7AED4') AND FIsOUSealUp = 0 AND (FLongNumber LIKE 'M%')  \r\n"); 
	 	  sbr.append(" ORDER BY FLongNumber ASC  \r\n");   
		  try {
			  IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, sbr.toString());
			  if(rs!=null){
				  while(rs.next()){
					  ids.add( rs.getObject("FID").toString());
				  }
			  }
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  return ids ;
	  }
	
	  // 获取没有分配的公司id集合
	  private static ArrayList<String> getNoAssignMaterialIDS(Context ctx,String ctrlId,String materialid,String ope){
		  ArrayList<String>  ids = new ArrayList<String>();
		  StringBuffer sbr = new  StringBuffer();  
		  if(ope.equals("purchase")){
			  //采购资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"'   and   c1.fid !='"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialPurchasing mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("company")){
			  //财务资料   这个单独处理 不走此查询
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and   c1.fid !='"+ctrlId+"'  and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialCompanyInfo mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FCompanyID  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FCompanyID = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("sales")){
			  //销售资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and   c1.fid !='"+ctrlId+"'  and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialSales mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("inven")){
			  //库存资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and   c1.fid !='"+ctrlId+"'  and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialInventory mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("cost")){
			  //成本资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"'  and   c1.fid !='"+ctrlId+"'  and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialCost mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("data")){  
	    		 sbr.append("select fid from t_org_company c1 ");
				  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and   c1.fid !='"+ctrlId+"'  and  ");
				  sbr.append("not exists ( ");
				  sbr.append("select 1 from T_BD_DataBaseDAssign mc2   ");
				  sbr.append("inner join t_org_company org2 on org2.fid = mc2.fcontrolunitid   ");
				  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.fcontrolunitid = c1.fid   ");
				  sbr.append("and mc2.FDataBaseDID='"+materialid+"' )  "); 
		  }

		  try {
			  IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, sbr.toString());
			  if(rs!=null){
				  while(rs.next()){
					  ids.add( rs.getObject("fid").toString());
				  }
			  }
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  return ids ;
	  }
	  
}
