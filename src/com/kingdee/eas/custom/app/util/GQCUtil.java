package com.kingdee.eas.custom.app.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.CenterBillStatus;
import com.kingdee.eas.custom.entity.PricePolicyEntryVo;
import com.kingdee.eas.custom.entity.ProcessMaterialVo;
import com.kingdee.eas.custom.util.DBUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class GQCUtil {
    
    /**
     * 技加工物料查询
     * @param ctx
     * @param 
     * @return
     */
    private static List<Map<String,String>> getProcessMaterials(Context ctx){
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    		String sql ="select FID,FNumber,FName_l2,CFDesignID,CFMaterialID from CT_CUS_ProcessMaterial";
    	 	try {
    			IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			 if(rs!=null && rs.size() > 0){ 
    				 while(rs.next()){
    					if(rs.getObject("CFMaterialID")!=null && !"".equals(rs.getObject("CFMaterialID").toString())){
    						Map<String,String> mp = new HashMap<String,String>();
    						mp.put("design", rs.getObject("CFDesignID")== null ? "": rs.getObject("CFDesignID").toString());
    						mp.put("material", rs.getObject("CFMaterialID").toString());
    						mp.put("id", rs.getObject("FID").toString());
    						mp.put("number", rs.getObject("FNumber").toString());
    						mp.put("name", rs.getObject("FName_l2").toString());
    						list.add(mp);
    					}
    			     }
    			 } 
    		} catch (BOSException e) {
     			e.printStackTrace();
    		} catch (SQLException e) {
     			e.printStackTrace();
    		} 
    	return list ;
    }
    
    /**
     * 根据公司ID获取内部供应商ID
     * @param ctx
     * @param companyID 公司ID
     * @return  供应商ID
     */
    public static String getCustomerIDByCompanyID(Context ctx,String companyID){
    	String customerID ="";
    	if(companyID!=null && !"".equals(companyID)){
        	String sql ="select FID from t_bd_customer where FEffectedStatus = 2 and FUsedStatus = 1 and FIsInternalCompany = 1 and FInternalCompanyID  = '"+companyID+"'";
    	 	try {
    			IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			 if(rs!=null && rs.size() > 0){ 
    				 while(rs.next()){
    					if( rs.getObject("FID")!=null && !"".equals(rs.getObject("FID").toString())){
    						customerID = rs.getObject("FID").toString();
    					}
    			     }
    			 } 
    		} catch (BOSException e) {
     			e.printStackTrace();
    		} catch (SQLException e) {
     			e.printStackTrace();
    		} 
    	}
    	return customerID;
    }
   
    
    private static Map<String,PricePolicyEntryVo> getProcessSuplyModel(Context ctx,String comId){
		StringBuffer sbr = new StringBuffer();
		sbr.append("/*dialect*/select distinct s.fmaterialitemid,t.fsupplierid,FPrice,FEffectualDate,FUneffectualDate from T_sm_supplyinfo s").append("\r\n");
		sbr.append("inner join t_bd_material m on s.fmaterialitemid = m.fid").append("\r\n");
		sbr.append("inner join T_BD_MaterialGroup mg on mg.fid = m.fmaterialgroupid").append("\r\n"); 
		sbr.append("where s.FPurchaseOrgID ='"+comId+"' and mg.fgroupstandard ='dR8lnQEPEADgAAWKwKgSxZeb4R8='").append("\r\n");
		sbr.append("and s.FIsUseable = 1 and s.FUneffectualDate > sysdate and mg.fnumber = 'W302'");
		Map<String,PricePolicyEntryVo> mps = new HashMap<String,PricePolicyEntryVo>();
	 	try {
			IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sbr.toString());
			 if(rs!=null && rs.size() > 0){ 
				 while(rs.next()){
					if( rs.getObject("fmaterialitemid")!=null && !"".equals(rs.getObject("fmaterialitemid").toString())&&
						rs.getObject("fsupplierid")!=null && !"".equals(rs.getObject("fsupplierid").toString())&&
						rs.getObject("fprice")!=null && !"".equals(rs.getObject("fprice").toString())	
						){
						PricePolicyEntryVo v = new PricePolicyEntryVo();
						if(rs.getObject("fmaterialitemid")!=null && !"".equals(rs.getObject("fmaterialitemid").toString())){
							v.setMaterialID(rs.getObject("fmaterialitemid").toString());
						}
						
						if(rs.getObject("FPrice")!=null && !"".equals(rs.getObject("FPrice").toString())){
							v.setPrice(new BigDecimal(rs.getObject("FPrice").toString()));
						}
						
						if(rs.getObject("fsupplierid")!=null && !"".equals(rs.getObject("fsupplierid").toString())){
							v.setSupplierID(rs.getObject("fsupplierid").toString());
						}
						
						if(rs.getObject("FEffectualDate")!=null && !"".equals(rs.getObject("FEffectualDate").toString())){
							v.setEffectiveDate(rs.getDate("FEffectualDate"));
						}
						
						if(rs.getObject("FUneffectualDate")!=null && !"".equals(rs.getObject("FUneffectualDate").toString())){
							v.setExpireDate(rs.getDate("FUneffectualDate"));
						}
						
						mps.put(rs.getObject("fmaterialitemid").toString(),v);
					}
			     }
			 } 
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		} 
	return mps ;
}
    
    private static List<String> queryGqcHisItems(Context ctx,String db){
    	String sql = "select FID from gqc_price_his_item  where fstatus = 1 and fsign = 1";
    	 List<String> list = new ArrayList<String>();
    	try {
			List<Map<String, Object>> ls = EAISynTemplate.query(ctx, db,sql);
			if(ls !=null &&  ls.size() >0){
				for (Map<String, Object> map : ls) {
					if(map.get("FID")!=null && !"".equals(map.get("FID").toString()))
						list.add(map.get("FID").toString());
				}
			}
		} catch (BOSException e) {
			e.printStackTrace();
		}
		return list;
    }
    
 
    
    private static List<String> queryGqcHisItems(Context ctx,String db,String cityId){
    	String sql = "select FID from gqc_price_his_item  where FCITYID ='"+cityId+"' and fstatus = 1 ";
    	 List<String> list = new ArrayList<String>();
    	try {
			List<Map<String, Object>> ls = EAISynTemplate.query(ctx, db,sql);
			if(ls !=null &&  ls.size() >0){
				for (Map<String, Object> map : ls) {
					if(map.get("FID")!=null && !"".equals(map.get("FID").toString()))
						list.add(map.get("FID").toString());
				}
			}
		} catch (BOSException e) {
			e.printStackTrace();
		}
		return list;
    }
    
    
    
    private static List<Map<String,String>> queryCompanyList(Context ctx){
    	String sql = "select CITYID,COMID from EAS_CITY_COMPANY ";
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
 		try {
			IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
			 if(rs!=null && rs.size() > 0){ 
				 while(rs.next()){
					if( rs.getObject("CITYID")!=null && !"".equals(rs.getObject("CITYID").toString())&&
						rs.getObject("COMID")!=null && !"".equals(rs.getObject("COMID").toString())	){
						HashMap<String,String> mp = new HashMap<String,String>();
						mp.put("cityId", rs.getObject("CITYID").toString());
						mp.put("comId", rs.getObject("COMID").toString());
						list.add(mp);
					}
			     }
			 } 
			
		} catch (BOSException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
    }
    
    
    
    
    private static Map<String,Map<String,String>> queryGqcHisItemMaps(Context ctx,String db){
    	String sql = "select FID,FNUMBER,DESIGNAMOUNT,MATERIALAMOUNT,ENDDATE,FACTORYID from gqc_price_his_item where fstatus = 1 and fsign = 1";
    	Map<String,Map<String,String>> maps = new HashMap<String,Map<String,String>>();
    	 List<String> list = new ArrayList<String>();
    	try {
			List<Map<String, Object>> ls = EAISynTemplate.query(ctx, db,sql);
			if(ls !=null &&  ls.size() >0){
				for (Map<String, Object> map : ls) {
					Map<String,String> m = new HashMap<String,String>();
					if(map.get("FID")!=null && !"".equals(map.get("FID").toString())){
						m.put("fid", map.get("FID").toString());
						if(map.get("FNUMBER")!=null && !"".equals(map.get("FNUMBER").toString())){
							m.put("fnumber", map.get("FNUMBER").toString());
						}else{
							m.put("fnumber", "");
						}
						
						if(map.get("DESIGNAMOUNT")!=null && !"".equals(map.get("DESIGNAMOUNT").toString())){
							m.put("damt", map.get("DESIGNAMOUNT").toString());
						}else{
							m.put("damt", "");
						}
						
						if(map.get("MATERIALAMOUNT")!=null && !"".equals(map.get("MATERIALAMOUNT").toString())){
							m.put("mamt", map.get("MATERIALAMOUNT").toString());
						}else{
							m.put("mamt", "");
						}
						
						if(map.get("ENDDATE")!=null && !"".equals(map.get("ENDDATE").toString())){
							m.put("d", map.get("ENDDATE").toString());
						}else{
							m.put("d", "");
						}
						
						
						if(map.get("FACTORYID")!=null && !"".equals(map.get("FACTORYID").toString())){
							m.put("factory", map.get("FACTORYID").toString());
						}else{
							m.put("factory", "");
						}
						
						
						maps.put(map.get("FID").toString(), m);
					}
				}
			}
		} catch (BOSException e) {
			e.printStackTrace();
		}
		return maps;
    }
    
    
    private static Map<String,Map<String,String>> queryGqcHisItemMaps(Context ctx,String db,String cityId){
    	String sql = "select FID,FNUMBER,DESIGNAMOUNT,MATERIALAMOUNT,ENDDATE,FACTORYID from gqc_price_his_item where FCITYID = '"+cityId+"' and fstatus = 1 ";
    	Map<String,Map<String,String>> maps = new HashMap<String,Map<String,String>>();
    	 List<String> list = new ArrayList<String>();
    	try {
			List<Map<String, Object>> ls = EAISynTemplate.query(ctx, db,sql);
			if(ls !=null &&  ls.size() >0){
				for (Map<String, Object> map : ls) {
					Map<String,String> m = new HashMap<String,String>();
					if(map.get("FID")!=null && !"".equals(map.get("FID").toString())){
						m.put("fid", map.get("FID").toString());
						if(map.get("FNUMBER")!=null && !"".equals(map.get("FNUMBER").toString())){
							m.put("fnumber", map.get("FNUMBER").toString());
						}else{
							m.put("fnumber", "");
						}
						
						if(map.get("DESIGNAMOUNT")!=null && !"".equals(map.get("DESIGNAMOUNT").toString())){
							m.put("damt", map.get("DESIGNAMOUNT").toString());
						}else{
							m.put("damt", "");
						}
						
						if(map.get("MATERIALAMOUNT")!=null && !"".equals(map.get("MATERIALAMOUNT").toString())){
							m.put("mamt", map.get("MATERIALAMOUNT").toString());
						}else{
							m.put("mamt", "");
						}
						
						if(map.get("ENDDATE")!=null && !"".equals(map.get("ENDDATE").toString())){
							m.put("d", map.get("ENDDATE").toString());
						}else{
							m.put("d", "");
						}
						
						
						if(map.get("FACTORYID")!=null && !"".equals(map.get("FACTORYID").toString())){
							m.put("factory", map.get("FACTORYID").toString());
						}else{
							m.put("factory", "");
						}
						
						maps.put(map.get("FID").toString(), m);
					}
				}
			}
		} catch (BOSException e) {
			e.printStackTrace();
		}
		return maps;
    }
 
    
    
     public static String doSycnProcessMaterial(Context ctx,String db){
    	 String flag = "";
//    		List<Map<String,String>> compyList = queryCompanyList(ctx);
//    		if(compyList != null && compyList.size() > 0){
    			List<Map<String,String>> materials = getProcessMaterials(ctx);//自定义加工物料
    			if(materials != null && materials.size() >0 ){
//    				for(Map<String,String> companymp :compyList ){
    					String cityId ="mBPd7lIjS5GSjOgwxBzeFcznrtQ=";
    					String comId = "jbYAAAMU2SvM567U";
    				  	Map<String,PricePolicyEntryVo> supplyModel = getProcessSuplyModel(ctx,comId);
    			 		if(supplyModel !=null && supplyModel.size() > 0){
    					  	List<String> isSyncList = queryGqcHisItems(ctx,db,cityId); 
        				  	Map<String,Map<String,String>> gqcmaps =  queryGqcHisItemMaps(ctx,db,cityId); 
        					List<ProcessMaterialVo> ppvos = new ArrayList<ProcessMaterialVo>(); 
        			 		List<ProcessMaterialVo> ppvou = new ArrayList<ProcessMaterialVo>(); 
    			 			for(Map<String,String> materialmp : materials){ 
        		 				ProcessMaterialVo pv = new ProcessMaterialVo();
        		 				pv.setId(materialmp.get("id"));
        		 				pv.setName(materialmp.get("name"));
        		 				pv.setNumber(materialmp.get("number"));
        		 				String designID = materialmp.get("design");
        		 				String materialID = materialmp.get("material");
        		 				if(designID != null && !"".equals(designID)){
        		 					PricePolicyEntryVo v = supplyModel.get(designID);
        		 					if(v != null){
        		 						pv.setDesignAmount(v.getPrice());
        		 					}else{
        		     					pv.setDesignAmount(BigDecimal.ZERO);
        		     				}
        		 				}
        		 				if(materialID!=null && !"".equals(materialID)){
        		 					PricePolicyEntryVo v = supplyModel.get(materialID);
        		 					if(v != null){
        		 						pv.setMaterialAmount(v.getPrice());
        		 	 					pv.setStartDate(v.getEffectiveDate());
        		 	 					pv.setEndDate(v.getExpireDate());
        		 	 					pv.setFactoryid(v.getSupplierID());
        		 					}
        		 				}
        		 				pv.setCityId(cityId);
        		 				
        		 				if(isSyncList.size() > 0 && isSyncList.contains(materialmp.get("id").toString())){
        		 					
        		 					boolean flag1 = false;
        		 					Map<String,String> mp = gqcmaps.get(materialmp.get("id").toString());
        		 					if(!pv.getMaterialAmount().toString().equals(mp.get("mamt"))){  
        		 						flag1 = true ;
        		 					}
        		 					
        		 					if(pv.getDesignAmount()!=null && pv.getDesignAmount().compareTo(BigDecimal.ZERO)>0){  
        		 						if(!pv.getDesignAmount().toString().equals(mp.get("damt")))
        		 						flag1 = true ;
        		 					}
        		 					
        		 					if(!pv.getFactoryid().equals(mp.get("factory"))){  
        		 						flag1 = true ;
        		 					}
        		 					
        		 					if(!pv.getNumber().equals(mp.get("fnumber"))){  
        		 						flag1 = true ;
        		 					}
        		 					
        		 					if(flag1)
        		 						ppvou.add(pv);
        		 					
        		     			}else{
        		     				ppvos.add(pv);
        		     			}
        		    		}
        		    		 if((ppvos != null && ppvos.size() >0 )||(ppvou != null && ppvou.size() >0)){
        		    			 List<String> sqls = new ArrayList<String>();
        		    			 for(ProcessMaterialVo v : ppvos){
        		    				 if(v.getMaterialAmount()!=null && v.getMaterialAmount().compareTo(BigDecimal.ZERO)>0){ 
        		    						StringBuffer sbr = new StringBuffer("insert into GQC_PRICE_HIS_ITEM(fid,fnumber,fname,fcityid,factoryid,designamount,materialamount,startdate,enddate,fstatus,fsign,fsyntime) values (");
                		    				sbr.append("'").append(v.getId()).append("','").append(v.getNumber()).append("','").append(v.getName()).append("','").append(v.getCityId()).append("','").append(v.getFactoryid()).append("',");
                		    				sbr.append(v.getDesignAmount()).append(",").append(v.getMaterialAmount()).append(",to_date('").append(v.getStartDate()).append("','yyyy-mm-dd'),to_date('").append(v.getEndDate()).append("','yyyy-mm-dd'),1,0,sysdate) ");
                		    				sqls.add(sbr.toString());
        		    				 }
        		    			 }
        		    			 
        		    			 
        		    			 for(ProcessMaterialVo v : ppvou){
        		    				 if(v.getMaterialAmount()!=null && v.getMaterialAmount().compareTo(BigDecimal.ZERO)>0){ 
        		    						StringBuffer sbr = new StringBuffer("update GQC_PRICE_HIS_ITEM set fnumber ='").append(v.getNumber()).append("',");
                		     				sbr.append("fname ='").append(v.getName()).append("',fsign=0,");
                		     				if(v.getDesignAmount()!=null && v.getDesignAmount().compareTo(BigDecimal.ZERO)>0){
                		     					sbr.append("designamount ='").append(v.getDesignAmount()).append("',");
                		     				}
                		     				sbr.append("factoryid ='").append(v.getFactoryid()).append("',");
                		     				sbr.append("materialamount ='").append(v.getMaterialAmount()).append("' where fid = '").append(v.getId()).append("' and fcityid ='").append(cityId).append("'");
                		     				sqls.add(sbr.toString());
                		     	 
        		    				 }
        		     			 
        		     			 } 
        		    			 
        		    			 if (sqls.size() > 0) {
        		    					try {
        									 EAISynTemplate.executeBatch(ctx, db, sqls);
        									 flag = "同步完成";
        									 
        								} catch (BOSException e) {
        									flag = e.getMessage();
        									e.printStackTrace();
        								}
        		    			}
        		    		 
        		    	}else{
        		    		// 城市和门诊映射没有配置，或者 技加工物料没有配置
        		    		flag = "城市和门诊映射没有配置，或者 技加工物料没有配置";
        		    	}
    			 		}
    		    	
//    				}
    				
    			}else{
    				 flag = "共青城供货价格或者技加工物料配置错误";
    			}
    		//}
    		return flag;
     }
     
    public static void delPurInBotRelation(Context ctx,String billId,String oper){
 		if(billId!=null && !"".equals(billId) && oper!=null && !"".equals(oper)){
 			String sid ="";
 			String iid ="";
 			if("d8e80652-0106-1000-e000-04c5c0a812202407435C".equals(oper)){//普通采购
 				sid ="Ng5tuf2BQWGRb7Qvd4l7cwRRIsQ=";
 				iid ="+R8S17SMREaJJcEFAfhwZQRRIsQ=";
 			}else if("d8e80652-0107-1000-e000-04c5c0a812202407435C".equals(oper)){ //普通采购退货
 				sid ="AfimdUXxQGiX+ZrqCu3gIQRRIsQ=";
 				iid ="i5E7tMYgTvWutGpKexSVywRRIsQ=";
 			}else if("SaleIssueBillInfo".equals(oper)){
 				sid ="Ng5tuf2BQWGRb7Qvd4l7cwRRIsQ=";
 				iid ="y4EoQ15iR4idFLx99VHFcgRRIsQ=";
 			}
 			if(sid != null && !"".equals(sid)){
 			// 销售出库单 T_IM_SaleIssueBill
 				String sql ="select FBaseStatus from T_IM_SaleIssueBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 	 			try {
 	 				 boolean saleFlag = false ;
 					IRowSet rs = DBUtil.executeQuery(ctx,sql);
 					 if(rs!=null && rs.size() > 0){ 
 	 					 while(rs.next()){
 							 if(rs.getObject("FBaseStatus")!=null && !"".equals(rs.getObject("FBaseStatus").toString())){
 								 if(Integer.parseInt(rs.getObject("FBaseStatus").toString()) > 3){
 									 saleFlag = true ;
 									 break ;
 								 }
 							 }
 						 }  
 					 }
 					 sql ="select FBaseStatus from T_IM_PurInWarehsBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 					 rs = DBUtil.executeQuery(ctx,sql);
 	 				 boolean purInFlag = false ;
 					 if(rs!=null && rs.size() > 0){ 
 						 while(rs.next()){
 							 if(rs.getObject("FBaseStatus")!=null && !"".equals(rs.getObject("FBaseStatus").toString())){
 								 if(Integer.parseInt(rs.getObject("FBaseStatus").toString()) > 3){
 									 purInFlag = true ;
 									 break ;
 								 }
 							 }
 						 }  
 					 }
 					 if(!purInFlag && !saleFlag){
 	 						String delSQL = "delete from t_Im_Saleissueentry where fparentid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 		 					
 		 					DBUtil.execute(ctx, delSQL);
 		 					delSQL = "delete from T_IM_SaleIssueBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from T_IM_PurInWarehsentry where fparentid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from T_IM_PurInWarehsBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='783061E3' and fbotmappingid = '"+iid+"' and FSrcObjectID ='"+billId+"'";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from t_bot_relation where FSrcEntityID = '783061E3' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"'";
 		 					DBUtil.execute(ctx, delSQL);				 
 					 }
 					 
 					String delSQL = "delete from CT_CUS_CenterPurchase  where fname_l2='"+billId+"'";
	 				DBUtil.execute(ctx, delSQL); 
	 					
 				} catch (BOSException e) {
 					e.printStackTrace();
 				} catch (SQLException e) {
 	 				e.printStackTrace();
 				}
 			}
			
		}
 	} 
    
    
    public static void delPurReceivalBotRelation(Context ctx,String billId,String oper){
 		if(billId!=null && !"".equals(billId)){
 			String sid ="4h2yog1WRbSIsRN1+Lk/0gRRIsQ=";
 			String iid ="hvxtFRdbQlanRDjd6o9dEQRRIsQ=";
// 			if("d8e80652-0106-1000-e000-04c5c0a812202407435C".equals(oper)){
// 				sid ="4h2yog1WRbSIsRN1+Lk/0gRRIsQ=";
// 				iid ="hvxtFRdbQlanRDjd6o9dEQRRIsQ=";
// 			}else if("d8e80652-0107-1000-e000-04c5c0a812202407435C".equals(oper)){
// 				sid ="AfimdUXxQGiX+ZrqCu3gIQRRIsQ=";
// 				iid ="i5E7tMYgTvWutGpKexSVywRRIsQ=";
// 			}
 			if(sid!=null && !"".equals(sid)){
 			// 销售出库单 T_IM_SaleIssueBill
 				String sql ="select FBaseStatus from T_IM_SaleIssueBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 	 			try {
 	 				 boolean saleFlag = false ;
 					IRowSet rs = DBUtil.executeQuery(ctx,sql);
 					 if(rs!=null && rs.size() > 0){ 
 	 					 while(rs.next()){
 							 if(rs.getObject("FBaseStatus")!=null && !"".equals(rs.getObject("FBaseStatus").toString())){
 								 if(Integer.parseInt(rs.getObject("FBaseStatus").toString()) > 3){
 									 saleFlag = true ;
 									 break ;
 								 }
 							 }
 						 }  
 					 }
 					 
 					 sql ="select FBaseStatus from T_IM_PurInWarehsBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 					 rs = DBUtil.executeQuery(ctx,sql);
 	 				 boolean purInFlag = false ;
 					 if(rs!=null && rs.size() > 0){ 
 						 while(rs.next()){
 							 if(rs.getObject("FBaseStatus")!=null && !"".equals(rs.getObject("FBaseStatus").toString())){
 								 if(Integer.parseInt(rs.getObject("FBaseStatus").toString()) > 3){
 									 purInFlag = true ;
 									 break ;
 								 }
 							 }
 						 }  
 					 }
 					 if(!purInFlag && !saleFlag){
 	 						String delSQL = "delete from t_Im_Saleissueentry where fparentid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from T_IM_SaleIssueBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from T_IM_PurInWarehsentry where fparentid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from T_IM_PurInWarehsBill where fid in (select FDestObjectID from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='783061E3' and fbotmappingid ='"+iid+"' and FSrcObjectID ='"+billId+"')";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='783061E3' and fbotmappingid = '"+iid+"' and FSrcObjectID ='"+billId+"'";
 		 					DBUtil.execute(ctx, delSQL);
 		 					
 		 					delSQL = "delete from t_bot_relation where FSrcEntityID = '15F2BD83' and FDestEntityID ='CC3E933B' and fbotmappingid ='"+sid+"' and FSrcObjectID ='"+billId+"'";
 		 					DBUtil.execute(ctx, delSQL);	
 					 }
 					String delSQL = "delete from CT_CUS_CenterPurchase  where fname_l2='"+billId+"'";
	 				DBUtil.execute(ctx, delSQL); 
 				 
 				} catch (BOSException e) {
 					e.printStackTrace();
 				} catch (SQLException e) {
 	 				e.printStackTrace();
 				}
 			}
			
		}
 	}  
    
    public static void updateCenterPurchaseStatus(Context ctx,String result,String billid){
		 try {
			if(result != null && !"".equals(result)&&
					billid != null && !"".equals(billid)){
				 String status =   CenterBillStatus.SUCESSBOTP_VALUE; 
				 if("scuess".equals(result)){
					 status = CenterBillStatus.SUCESSBOTP_VALUE ;
				 }else {
					 status = CenterBillStatus.ERRORBOTP_VALUE ;
				 }
				 String updteSQL = "update　CT_CUS_CenterPurchase set CFCenterBillStatus ='"+status+"',FDESCRIPTION_L2 = '"+result+"' where fname_l2 = '"+billid+"'";
				 DBUtil.execute(ctx, updteSQL);
			}
		} catch (BOSException e) {
 			e.printStackTrace();
		}
    }
}
