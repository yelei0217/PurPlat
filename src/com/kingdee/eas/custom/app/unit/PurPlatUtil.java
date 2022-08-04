package com.kingdee.eas.custom.app.unit;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class PurPlatUtil {
	

	/**
	 *  获取当前时间 格式化
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrentTimeStr(){
  		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
	}
	

	/**
	 *  获取当前时间 格式化
	 * @return yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimeStrS(){
 		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(System.currentTimeMillis()));
	}

	
	/**
	 *  查询消息ID是否在单据中存在
	 * @param ctx
	 * @param busCode
	 * @param msgId
	 * @return
	 */
	public static boolean judgeMsgIdExists(Context ctx, String busCode,String msgId) {
	     boolean flag = false;
	     if (VerifyUtil.notNull(msgId) && VerifyUtil.notNull(busCode) ) {
	    	 String tableName ="";
	    	 if("GZ_LZ_PO".equals(busCode)||"DZ_MZ_PO".equals(busCode)){
	    		 tableName =" T_SM_PurOrder ";
	    	 }
	        try {
	    		String sql = " select count(1) C from "+tableName+" where CFMsgId = '"+msgId+"' ";
	         IRowSet rs = DbUtil.executeQuery(ctx, sql);
	         if (rs.next() && 
	           rs.getObject("C") != null && !"".equals(rs.getObject("C").toString()) && 
	           Integer.valueOf(rs.getObject("C").toString()).compareTo(Integer.valueOf(1)) >= 0) {
	           flag = true;
	         }
	       }
	       catch (BOSException e) {
	         e.printStackTrace();
	       } catch (SQLException e){
	         e.printStackTrace();
	       } 
	     }
	     return flag;
	  }
	
	public static boolean judgeExists(Context ctx,String oper,String orgId,String fid){
		  boolean flag = false;
		    if (VerifyUtil.notNull(oper) && VerifyUtil.notNull( fid) ) {
		    	String sql = "";
		    	 if("SC".equals(oper)){
 		    		   sql = " select count(1) C from T_BD_SupplierCompanyInfo where FSupplierID ='"+fid+"' and FComOrgID ='"+orgId+"' ";
		    	 } else if("SP".equals(oper)){
		    		 sql = " select count(1) C from T_BD_SupplierPurchaseInfo where FSupplierID ='"+fid+"' and FPurchaseOrgID ='"+orgId+"' ";
		    	 } else if("S".equals(oper)){
		    		 sql = " select count(1) C from T_BD_Supplier where FId ='"+fid+"' and FUsedStatus =1 ";
 		    	 } else if("M".equals(oper)){
		    		 sql = " select count(1) C from T_BD_Material where FId ='"+fid+"' and FStatus =1 ";
 		    	 } else if("MP".equals(oper)){
		    		 sql = " select count(1) C from T_BD_MaterialPurchasing where FMaterialID ='"+fid+"' and FOrgUnit = '"+orgId+"' ";
 		    	 }else if("UNIT".equals(oper)){
		    		 sql = " select count(1) C from T_BD_MeasureUnit where FNumber ='"+fid+"' and FGroupID ='CUYZGEtVTzqROJAmOrUCEBwqyGg=' ";
 		    	 }else if("USER".equals(oper)){
		    		 sql = " select count(1) C T_PM_User where FPERSONID = '"+fid+"' ";
 		    	 }else if("CUS".equals(oper)){
		    		 sql = " select count(1) C from T_BD_Customer where FId ='"+fid+"' and FUsedStatus =1 ";
 		    	 }else if("CUSC".equals(oper)){
		    		   sql = " select count(1) C from T_BD_CustomerCompanyInfo where FCustomerID ='"+fid+"' and FComOrgID ='"+orgId+"' ";
 		    	 } else if("CUSS".equals(oper)){
		    		 sql = " select count(1) C from T_BD_CustomerSaleInfo where FCustomerID ='"+fid+"' and FComOrgID ='"+orgId+"' ";
 		    	 }  
		    	 
		    	 if(VerifyUtil.notNull(oper) ){
		    	      try {
					         IRowSet rs = DbUtil.executeQuery(ctx, sql);
					         if (rs.next() && 
					           rs.getObject("C") != null && !"".equals(rs.getObject("C").toString()) && 
					           Integer.valueOf(rs.getObject("C").toString()).compareTo(Integer.valueOf(1)) >= 0) {
					           flag = true;
					         }
					       }
					       catch (BOSException e) {
					         e.printStackTrace();
					       } catch (SQLException e) {
					         e.printStackTrace();
					       } 
		    	 }
		    }
		  return flag ;
	}
	
	
	/**
	 *  通过 采购组织ID 获取 采购组织对应的控制单元ID
	 * @param ctx
	 * @param purOrgId
	 * @return
	 */
	public static String getCtrlOrgId(Context ctx,String oper,String fid){
		String ctrlOrgId ="";
		String sql ="";
		if(VerifyUtil.notNull(oper) && VerifyUtil.notNull(fid) ){
			if("PUR".equals(oper)){
				sql  ="select FCONTROLUNITID from T_ORG_PURCHASE where FID='"+fid+"'";
			}
	
			if(VerifyUtil.notNull(sql)){
			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next() &&  rs.getObject("FCONTROLUNITID") != null && !"".equals(rs.getObject("FCONTROLUNITID").toString())) 
			        	 ctrlOrgId = rs.getObject("FCONTROLUNITID").toString();
			       }
			       catch (BOSException e) {
			         e.printStackTrace();
			       } catch (SQLException e) {
			         e.printStackTrace();
			       } 
			}
		}
		return ctrlOrgId;
	}
	
	
	/**
	 *  通过 采购组织ID 获取 采购组织对应的控制单元ID
	 * @param ctx
	 * @param purOrgId
	 * @return
	 */
	public static String getUserIdByPersonId(Context ctx,String personId){
		String userId = "ZypTdtSLS8S90LPPdhP1MxO33n8=";
 		if(VerifyUtil.notNull(personId) ){
			String sql  =" select FID T_PM_User where FPERSONID = '"+personId+"' ";
			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next() &&  rs.getObject("FID") != null && !"".equals(rs.getObject("FID").toString())) 
			        	 userId = rs.getObject("FID").toString();
			       }
			       catch (BOSException e) {
			         e.printStackTrace();
			       } catch (SQLException e) {
			         e.printStackTrace();
			       } 
			 
		}
		return userId;
	}
	
	
	
	public static String getMeasureUnitFIdByFNumber(Context ctx,String number){
		String fid ="";
		if(VerifyUtil.notNull(number) ){
			String	 sql = " select FID from T_BD_MeasureUnit where FNumber ='"+number+"' and FGroupID ='CUYZGEtVTzqROJAmOrUCEBwqyGg=' ";
			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next() &&  rs.getObject("FID") != null && !"".equals(rs.getObject("FID").toString())) 
			        	 fid = rs.getObject("FID").toString();
			       }
			       catch (BOSException e) {
			         e.printStackTrace();
			       } catch (SQLException e) {
			         e.printStackTrace();
			       } 
		}
		
		return fid ;
	}
	
	/**
	 *  通过 采购组织ID 获取 采购组织对应的控制单元ID
	 * @param ctx
	 * @param purOrgId
	 * @return
	 */
	public static Map<String,String> getMaterialInfoByMId(Context ctx,String mId){
		 Map<String,String> mp = null ;
 		if(VerifyUtil.notNull(mId) ){
 			mp = new HashMap<String,String>();
 			String sql ="select m.FID,m.FNUMBER,m.FNAME_L2,m.CFPINGPAI,m.CFHUOHAO,m.FMODEL,m.CFXINGHAO,mg.FNAME_L2 goupName " +
 					" from T_BD_MATERIAL m " +
 					" inner join t_bd_materialGroup mg on m.FMATERIALGROUPID = mg.FID " +
 					" where m.FID ='"+mId+"'";
			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next()){
			        	 if(rs.getObject("FID")!=null &&!"".equals(rs.getObject("FID").toString()))
			        		 mp.put("mId", rs.getObject("FID").toString());
			        	  
			        	 if(rs.getObject("FNUMBER")!=null &&!"".equals(rs.getObject("FNUMBER").toString()))
			        		 mp.put("number", rs.getObject("FNUMBER").toString()); 
			        	 
			        	 if(rs.getObject("FNAME_L2")!=null &&!"".equals(rs.getObject("FNAME_L2").toString()))
			        		 mp.put("name", rs.getObject("FNAME_L2").toString()); 
			        	 
			        	 
			        	 if(rs.getObject("CFPINGPAI")!=null &&!"".equals(rs.getObject("CFPINGPAI").toString()))
			        		 mp.put("pp", rs.getObject("CFPINGPAI").toString()); 
			        	 else
			        		 mp.put("pp","");
			        	 
			        	 
			        	 if(rs.getObject("CFHUOHAO")!=null &&!"".equals(rs.getObject("CFHUOHAO").toString()))
			        		 mp.put("hh", rs.getObject("CFHUOHAO").toString()); 
			        	 else
			        		 mp.put("hh",""); 
			  
			        	 
			        	 if(rs.getObject("FMODEL")!=null &&!"".equals(rs.getObject("FMODEL").toString()))
			        		 mp.put("gg", rs.getObject("FMODEL").toString()); 
			        	 else
			        		 mp.put("gg","");  
			        	 
			        	 
			        	 if(rs.getObject("CFXINGHAO")!=null &&!"".equals(rs.getObject("CFXINGHAO").toString()))
			        		 mp.put("xx", rs.getObject("CFXINGHAO").toString()); 
			        	 else
			        		 mp.put("xx","");  
			        	 
			        	 
			        	 if(rs.getObject("goupName")!=null &&!"".equals(rs.getObject("goupName").toString()))
			        		 mp.put("gn", rs.getObject("goupName").toString()); 
			        	 else
			        		 mp.put("gn","");   
			        	 
			         }
 			       }
			       catch (BOSException e) {
			         e.printStackTrace();
			       } catch (SQLException e) {
			         e.printStackTrace();
			       } 
			 
		}
		return mp;
	}
	
	
	
}
