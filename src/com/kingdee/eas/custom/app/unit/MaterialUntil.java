package com.kingdee.eas.custom.app.unit;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectStringPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.eas.base.core.fm.ContextHelperFactory;
import com.kingdee.eas.basedata.assistant.KAClassficationCollection;
import com.kingdee.eas.basedata.assistant.KAClassficationFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitCollection;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.material.EquipmentPropertyEnum;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupCollection;
import com.kingdee.eas.basedata.master.material.MaterialGroupFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupInfo;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.master.material.UsedStatusEnum;
import com.kingdee.eas.basedata.org.CtrlUnitFactory;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.jdbc.rowset.IRowSet;

public class MaterialUntil {

	public String doCreateMaterial(Context ctx, String data) throws BOSException{
		
		Map map = (Map) JSONObject.parse(data);
		String operType = map.get("operType").toString();
		 
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		String  error = new String();
		com.alibaba.fastjson.JSONArray  jsonArr = (com.alibaba.fastjson.JSONArray) map.get("data");     
		boolean flag = true;
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
				if ( !"0".equals(operType) && imbiz.exists("where number = '"+number+"'") ) { 
					error = error+ "物料编码已存在;"; flag = false;
					continue;
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (dataMap.get("fName")== null || "".equals(dataMap.get("fName").toString()) ) { 
				error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的名称不能为空"; flag = false;
				continue;
			}  
			if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
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
			}
			
			if (dataMap.get("fCreateTime")== null || "".equals(dataMap.get("fCreateTime").toString()) ) { 
				error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的创建时间不能为空"; flag = false;
				continue;
			}
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				credate = sdf1.parse(createTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的创建时间格式不正确;"; flag = false;
				continue;
				
			}
			if (dataMap.get("")== null || "".equals(dataMap.get("fUpdateTime").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的最后修改时间不能为空"; flag = false;
				continue;
			}
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			try {
				update = sdf1.parse(updateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的最后修改时间格式不正确;"; flag = false;
				continue;
				
			}
			
			if (dataMap.get("fKAClass")== null || "".equals(dataMap.get("fKAClass").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的记账分类不能为空"; flag = false;
				continue;
			}
			try {
				if(!KAClassficationFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fKAClass").toString()+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的记账分类不存在"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataMap.get("fBaseUnit")== null || "".equals(dataMap.get("fBaseUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的基本计量单位不能为空"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fBaseUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的基本计量单位不存在"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fInvUnit")== null || "".equals(dataMap.get("fInvUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的库存计量单位不能为空"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fInvUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的库存计量单位不存在"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (dataMap.get("fPurUnit")== null || "".equals(dataMap.get("fPurUnit").toString()) ) { 
				error = error+ "编码为"+dataMap.get("FNUMBER").toString()+"的采购计量单位不能为空"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fPurUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的采购计量单位不存在"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fSaleUnit")== null || "".equals(dataMap.get("fSaleUnit").toString()) ) { 
				error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的销售计量单位不能为空"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fSaleUnit").toString()+"'")){
					error = error+  "编码为"+dataMap.get("FNUMBER").toString()+"的销售计量单位不存在"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					
					//成本资料
					StringBuffer sbr  = new StringBuffer(" INSERT INTO T_BD_MaterialCost (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		sbr.append("FEFFECTEDSTATUS ,FSTORETYPE ,FIscreWatecostobject ,fcostobejctgroupid,FDEFAULTCOSTITEMID ,FISPARTICIPATEREDUCT ,FEXPENSEID  ) ");
		    		sbr.append(" values(newbosid('C45E21AA'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		sbr.append(" 2	,1	,0,'','',0,''	 )"); 
		    		pe.getSqlList().add(sbr);
		    		
		    		 //采购资料  
				    StringBuffer pusbr  = new StringBuffer(" INSERT INTO T_BD_MaterialPurchasing (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
				    pusbr.append(" FUNITID , FRECEIVETOPRATIO ,FRECEIVEBOTTOMRATIO, FDAYDAHEAD ,FDAYSDELAY ,   FISRETURN ,FEFFECTEDSTATUS ,FPURCHASECHECK ,FISNOTCONTROLTIME ,FISNOTCONTROLQTY ,FISPURCHASECHECK , " +
				    		" FFINEQUALITYFAIRPRICE,FUSESUPPLYLIST ,FUSESUPPLYPRICE ,FQUOTAPOLICYID ,FMINDIVISIONQTY ,FPRICE  ,FPRICETOP , FQuotaPeriod) ");
				    pusbr.append(" values(newbosid('0193BD9B'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
				    pusbr.append(" '"+measure.get(0).getId().toString()+"' , 0,0,0,0    , 0 ,2,0,1,0 ,0   ,0,0,1,'SdQ8j/QxRueXfLDTP9izyRRbtvQ=',0 ,0,0,1	 )"); 
		    		pe.getSqlList().add(pusbr);
		    		 
		    		
		    		//销售资料 
		    		StringBuffer salesbr  = new StringBuffer(" INSERT INTO T_BD_MaterialSales (    fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		salesbr.append(" FISRETURN ,FISNOTCHECKONRETURN ,FISRECEIVBYCHECK ,FUNITID ,FISSUETOPRATIO ,FISSUEBOTTOMRATIO ,FDAYDAHEAD ,FDAYSDELAY ,FABCTYPE ,FEFFECTEDSTATUS ,FISPURBYSALE ,FISCONSIGNCHECK ,FINNERPRICERATE ,FCHEAPRATE ) ");
		    		salesbr.append(" values(newbosid('C84112CF'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		salesbr.append("  0,0,0,'"+salemeasure.get(0).getId().toString()+"',0,0,0,0,-1,2,0,0,0,0	 )"); 
		    		pe.getSqlList().add(salesbr);
		    		 
		    		//库存资料 
		    		StringBuffer kcsbr  = new StringBuffer(" INSERT INTO T_BD_MaterialInventory (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
		    		
		    		kcsbr.append(" FISCONTROL ,FQTYSAFETY ,FQTYMIN ,FQTYMAX ,FDAYSBOTTOM  ,FDAYSTOP ,FDAYSTURNOVER ,FISNEGATIVE ,FISBATCHNO FISSEQUENCENO ,FISLOTNUMBER ,FISBARCODE ,FQTYMINPACKAGE ,FABCTYPE ,FISCOMPAGES ,FISSUEPRIORITYMODE ,  ");
		    		kcsbr.append(" FUNITID , FEFFECTEDSTATUS , FISPERIODVALID ,FPERIODVALID ,FPERIODVALIDUNIT ,FINWAREHSAHEAD , FOUTWAREHSAHEAD , FPREPWARNAHEAD ,FAHEADUNIT , FPLANNINGMODE ,FREBOOKQTY , FCONSUMESPEED ,FPURCHASINGAHEADDATE ,  ");
		    		kcsbr.append(" FBATCHPOLICY ,FFIXATIONBATCHQTY ,FDAYSPLANTURNOVER ,FCHEAPRATE ,FISCHECK ,FTOOLRATE ,FISPROJECTNUMBER ,FISTRACKINGNUMBER ,FISRESERVATION ,FRESERVATIONDATE ,FCLOSEDATECALMODE )  ");
		    		kcsbr.append("   ");
		    		kcsbr.append(" values(newbosid('557E499F'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+cid+"', '' , '"+pk.toString()+"', ");
		    		kcsbr.append("  0, 0,0,0, 0 , 0 , 0,0, 0 ,0,, 0 , 0 ,0,-1  ,0 ,    -1 , '"+kcmeasure.get(0).getId().toString()+"' ,2       ,  0 ,0, 3,      0  ， 0,    0 ,  3,      0 ,   0 ,0,0,1,0,0,0,  0 ,   0,   0,0,0,0,0 )"); 
		    		pe.getSqlList().add(kcsbr);
		    		  
		    		 
					if(!"00000000-0000-0000-0000-000000000000CCE7AED4".equals(cid)){
						//成本资料
						ArrayList<String> companyIds  =getNoAssignMaterialIDS(ctx,cid,pk.toString(),"cost");
					    if(companyIds!=null &&companyIds.size()>0){
					    	for(String comid : companyIds){
					    		StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialCost (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
					    		sbr2.append("FEFFECTEDSTATUS ,FSTORETYPE ,FIscreWatecostobject ,fcostobejctgroupid,FDEFAULTCOSTITEMID ,FISPARTICIPATEREDUCT ,FEXPENSEID  ) ");
					    		sbr2.append(" values(newbosid('C45E21AA'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
					    		sbr2.append(" 2	,1	,0,'','',0,''	 )"); 
					    		pe.getSqlList().add(sbr2);
					    	}
					    }  
					    //采购资料 
					    ArrayList<String> pucompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"purchase");
					    if(pucompanyIds!=null &&pucompanyIds.size()>0){
					    	for(String comid : pucompanyIds){
					    		
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialPurchasing (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
							    sbr2.append(" FUNITID , FRECEIVETOPRATIO ,FRECEIVEBOTTOMRATIO, FDAYDAHEAD ,FDAYSDELAY ,FISRETURN ,FEFFECTEDSTATUS ,FPURCHASECHECK ,FISNOTCONTROLTIME ,FISNOTCONTROLQTY ,FISPURCHASECHECK , " +
							    		" FFINEQUALITYFAIRPRICE,FUSESUPPLYLIST ,FUSESUPPLYPRICE ,FQUOTAPOLICYID ,FMINDIVISIONQTY ,FPRICE  ,FPRICETOP , FQuotaPeriod  ) ");
							    sbr2.append(" values(newbosid('0193BD9B'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append(" '"+measure.get(0).getId().toString()+"' , 0,0,0,0 , 0 ,2,0,1,0 ,0,0,0,0,'SdQ8j/QxRueXfLDTP9izyRRbtvQ=',0 ,0,0,1	 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }  					   
					    //销售资料 
					    ArrayList<String>  salecompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"sales");
					    if(salecompanyIds!=null &&salecompanyIds.size()>0){
					    	for(String comid : salecompanyIds){
					    		
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialSales (   fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID , ");
							    sbr2.append(" FISRETURN ,FISNOTCHECKONRETURN ,FISRECEIVBYCHECK ,FUNITID ,FISSUETOPRATIO ,FISSUEBOTTOMRATIO ,FDAYDAHEAD ,FDAYSDELAY ,FABCTYPE ,FEFFECTEDSTATUS ,FISPURBYSALE ,FISCONSIGNCHECK ,FINNERPRICERATE ,FCHEAPRATE )");
							    sbr2.append(" values(newbosid('C84112CF'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append(" 0,0,0,'"+salemeasure.get(0).getId().toString()+"',0,0,0,0,-1,2,0,0,0,0		 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }  
					    //库存资料
					    ArrayList<String>  kccompanyIds  = getNoAssignMaterialIDS(ctx,cid,pk.toString(),"inven");
					    if(kccompanyIds!=null &&kccompanyIds.size()>0){
					    	for(String comid : kccompanyIds){
					    		
							    StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialInventory (  fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID , FSTATUS ,FORGUNIT ,FFREEZEORGUNIT ,FMATERIALID ,");
			    		
							    sbr2.append(" FISCONTROL ,FQTYSAFETY ,FQTYMIN ,FQTYMAX ,FDAYSBOTTOM  ,FDAYSTOP ,FDAYSTURNOVER ,FISNEGATIVE ,FISBATCHNO FISSEQUENCENO ,FISLOTNUMBER ,FISBARCODE ,FQTYMINPACKAGE ,FABCTYPE ,FISCOMPAGES ,FISSUEPRIORITYMODE ,  ");
							    sbr2.append(" FUNITID , FEFFECTEDSTATUS , FISPERIODVALID ,FPERIODVALID ,FPERIODVALIDUNIT ,FINWAREHSAHEAD , FOUTWAREHSAHEAD , FPREPWARNAHEAD ,FAHEADUNIT , FPLANNINGMODE ,FREBOOKQTY , FCONSUMESPEED ,FPURCHASINGAHEADDATE ,  ");
							    sbr2.append(" FBATCHPOLICY ,FFIXATIONBATCHQTY ,FDAYSPLANTURNOVER ,FCHEAPRATE ,FISCHECK ,FTOOLRATE ,FISPROJECTNUMBER ,FISTRACKINGNUMBER ,FISRESERVATION ,FRESERVATIONDATE ,FCLOSEDATECALMODE )  ");
							    sbr2.append("   ");
							    sbr2.append(" values(newbosid('557E499F'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cid+"',  1 , '"+comid+"', '' , '"+pk.toString()+"', ");
							    sbr2.append("  0, 0,0,0, 0 , 0 , 0,0, 0 ,0,, 0 , 0 ,0,-1  ,0 ,    -1 , '"+kcmeasure.get(0).getId().toString()+"' ,2       ,  0 ,0, 3,      0  ， 0,    0 ,  3,      0 ,   0 ,0,0,1,0,0,0,  0 ,   0,   0,0,0,0,0 )"); 
					    		pe.getSqlList().add(sbr2); 
					    	}
					    }  
					   
			    		
					} 
				}
				//财务资料   单独处理
				pe = setAllCtrlComOrgs(ctx,kaclass,pk.toString() ,pe);
				  
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		return data;
		
	}
	
	
	private ParallelSqlExecutor setAllCtrlComOrgs(Context ctx , String kaclass , String materialid , ParallelSqlExecutor pe) { 
		
		String userId = ContextHelperFactory.getLocalInstance(ctx).getCurrentUser().getId().toString(); 
		StringBuffer sbr = new  StringBuffer(); 
	 	sbr.append(" /*dialect*/SELECT cunit.FID CID ,nvl(kacl.fid,'') LID FROM T_ORG_CtrlUnit cunit  left join T_BD_KAClassfication kacl on kacl.fnumber = '"+kaclass+"' and  kacl.FCURRENCYCOMPANY  = cunit.fid  \r\n"); 
	 	sbr.append(" WHERE (cunit.FID <> '11111111-1111-1111-1111-111111111111CCE7AED4') AND cunit.FIsOUSealUp = 0 AND (cunit.FLongNumber LIKE 'M%')    \r\n"); 
	 	sbr.append(" ORDER BY cunit.FLongNumber ASC     \r\n");   
		try {
			IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, sbr.toString());
			if(rs!=null){
				while(rs.next()){
					String cityid = rs.getObject("CID").toString();
					String kaclid = rs.getObject("LID").toString();
					
					StringBuffer comsbr  = new StringBuffer(" INSERT INTO T_BD_MaterialCompanyInfo ( fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID ,  FMATERIALID , FCOMPANYID ,FKACLASSID , ");
				    comsbr.append(" FACCOUNTTYPE  , FSTANDARDCOST  ,FEFFECTEDSTATUS ,FCALCULATETYPE FSTATUS ,FCREATECOBYORDER ,FISLOT   ,FISASSISTPROPERTY ,FISPROJECT ,FISTRACKNUMBER  ) ");
				    comsbr.append(" values(newbosid('D431F8BB'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cityid+"',  '"+materialid+"' , '"+cityid+"', '"+kaclid+"' , ");
				    comsbr.append(" ' 4, 0 ,2,0,1,0,0,0,0,0	 )"); 
					pe.getSqlList().add(comsbr);
					if(!"00000000-0000-0000-0000-000000000000CCE7AED4".equals(cityid)){
						 
						StringBuffer comsbrsql = new  StringBuffer(); 
						comsbrsql.append(" /*dialect*/select c1.fid  FID , kacl.FID KID from t_org_company c1   \r\n"); 
						comsbrsql.append(" left join T_BD_KAClassfication kacl on kacl.fnumber = '"+kaclass+"' and  kacl.FCURRENCYCOMPANY  = cunit.fid  \r\n"); 
						comsbrsql.append(" where c1.fcontrolunitid = '"+cityid+"' and    \r\n"); 
						comsbrsql.append(" not exists (   \r\n"); 
						comsbrsql.append(" select 1 from T_BD_MaterialCompanyInfo mc2     \r\n"); 
						comsbrsql.append(" inner join t_org_company org2 on org2.fid = mc2.FCompanyID    \r\n"); 
						comsbrsql.append(" where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FCompanyID = c1.fid    \r\n"); 
						comsbrsql.append(" and mc2.FMaterialID='"+materialid+"' )    \r\n"); 
						
						IRowSet rsCom = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, comsbrsql.toString());
						if(rsCom!=null && rsCom.size() > 0){
							while(rsCom.next()){
								String comid = rs.getObject("FID").toString();
								String kacComlid = rs.getObject("KID").toString();
								
								StringBuffer sbr2  = new StringBuffer(" INSERT INTO T_BD_MaterialCompanyInfo ( fid,FCREATORID ,FCREATETIME ,FLASTUPDATEUSERID ,FLASTUPDATETIME ,FCONTROLUNITID ,  FMATERIALID , FCOMPANYID ,FKACLASSID , ");
							    sbr2.append(" FACCOUNTTYPE  , FSTANDARDCOST  ,FEFFECTEDSTATUS ,FCALCULATETYPE FSTATUS ,FCREATECOBYORDER ,FISLOT   ,FISASSISTPROPERTY ,FISPROJECT ,FISTRACKNUMBER  ) ");
							    sbr2.append(" values(newbosid('D431F8BB'),'"+userId+"',sysdate,'"+userId+"',sysdate,'"+cityid+"',  '"+materialid+"' , '"+comid+"', '"+kacComlid+"' , ");
							    sbr2.append(" ' 4, 0 ,2,0,1,0,0,0,0,0	 )"); 
							    pe.getSqlList().add(sbr2); 
							}
						}   
					}
					
				}
			}
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		  
		 
	    
		
		
		return pe;
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
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialPurchasing mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("company")){
			  //财务资料   这个单独处理 不走此查询
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialCompanyInfo mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FCompanyID  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FCompanyID = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("sales")){
			  //销售资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialSales mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("inven")){
			  //库存资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialInventory mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
		  }else if(ope.equals("cost")){
			  //成本资料
			  sbr.append("select fid from t_org_company c1 \r\n");
			  sbr.append("where c1.fcontrolunitid = '"+ctrlId+"' and \r\n");
			  sbr.append("not exists ( \r\n");
			  sbr.append("select 1 from T_BD_MaterialCost mc2  \r\n");
			  sbr.append("inner join t_org_company org2 on org2.fid = mc2.FOrgUnit  \r\n");
			  sbr.append("where org2.fcontrolunitid = c1.fcontrolunitid and mc2.FOrgUnit = c1.fid  \r\n");
			  sbr.append("and mc2.FMaterialID='"+materialid+"' )  \r\n"); 
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
