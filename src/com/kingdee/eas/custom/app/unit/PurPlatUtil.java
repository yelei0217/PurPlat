package com.kingdee.eas.custom.app.unit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.util.DBUtil;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class PurPlatUtil {

	
	 public final static Map<String, String> parentTypeValue = new HashMap<String, String>() {
	 {
	         put("80","1");
	            
	      }
	 };      
	            
    public final static Map<String, String> dateTypeMenuMp = new HashMap<String, String>() {
        {
            put("ORGUNIT","1");
            put("POSITION","2");
            put("SUPPLIER","3");
            put("CUSTOMER","4");
            put("PERSON","5");
            put("MATERIAL","6");
            put("PAYTYPE","7");
            put("FREEITEM","8");
            put("MAINCLASS","9");
            put("HIS_SALEORDER","10");
            put("HIS_RECEIVABLES","11");
            put("HIS_RECEIPT","12");
            put("HIS_PURCHIN","13");
            put("HIS_SALEOUT","14");
            put("SUPPLIER2HIS","15");
            put("PAYTYPETREE","16");
            put("OA_OTHERBILL","17");
            put("OA_PURREQUEST","18");
            put("PAYMENTBILLTOMID","19");
            put("EXPENSETYPETOMID","20");
            put("SUPPLYINFOTOMID","21");
            put("OA_PAYMENTBILL","22");
            put("HIS_PORCESS","23");
            put("CROSSCLINICSALL","24");
            put("BORROWLANDBILL","25");
            put("HR_RUZHI","26");
            put("HR_LIZHI","27");
            put("HR_YIDONG","28");
            put("VSPJD","29");
            put("OA_SHR_HIRE","30");
            put("OA_SHR_RESIGN","31");
            put("OA_SHR_FLUCT","32");
            put("OA_SHR_FLUCTOUT","33");
            put("HIS_BACK","34");
            put("HIS_REP","35");
            put("HIS_REPORT","36");
            put("HIS_BACK_EXT","37");
            put("HIS_REP_EXT","38");
            put("GZ_LZ_PO","60");
            put("GZ_LZ_SO","61");
            put("GZ_MZ_PO","62");
            put("GZ_LZ_PI","63");
            put("GZ_LZ_SS","64");
            put("GZB_LZ_PI","65");
            put("GZB_LZ_PO_CR","66");
            put("GZB_LZ_SS","67");
            put("GZB_LZ_SO_CR","68");
            put("GZB_MZ_PI","69");
            put("GZB_MZ_PO_CR","70");
            put("DZ_MZ_PO","71");
            put("DZ_MZ_PI","72");
            put("DZB_MZ_PI","73");
            put("DZB_MZ_PO_CR","74");
            put("VMI2C_LZ_PI","75");
            put("VMI2CB_LZ_PI","76");
            put("VMI_LZ_SS","77");
            put("VMI_MZ_PI","78");
            put("VMIB_MZ_PI","79");
            put("VMIB_LZ_SS","80");
            put("CGZ_U_MZ_SO","81");
            put("CGZ_U_MZ_SS","82");
            put("CDZ_U_MZ_SS","83");
            put("VMI_U_MZ_SO","84");
            put("VMI_U_MZ_SS_VMI","85");
            put("VMI_U_MZ_PI","86");
            put("VMI_U_MZ_SS","87");
            put("VMI_U_LZ_PI","88");
            put("VMI_U_LZ_SS","89");
            put("SK_MZ_OPI","90");
            put("SK_MZ_OSS","91");
            put("GZ_CK_LZ_AP","92");
            put("GZ_CK_LZ_CJ","93");
            put("GZ_CK_LZ_P","94");
            put("GZ_CK_LZ_AR","95");
            put("GZ_CK_LZ_R","96");
            put("GZ_CK_MZ_AP","97");
            put("GZ_CK_MZ_P","98");
            put("VMI_CK_LZ_AP","99");
            put("VMI_CK_LZ_CJ","100");
            put("VMI_CK_LZ_P","101");
            put("VMI_CK_LZ_AR","102");
            put("VMI_CK_LZ_R","103");
            put("VMI_CK_MZ_AP","104");
            put("VMI_CK_MZ_CJ","105");
            put("VMI_CK_MZ_P","106");
            put("YC_PI","107");
            put("YC_SS","108");
            put("YX_MZ_PI","109");
            put("YX_LZ_PI","110");
            put("YX_LZ_SS","111");
            put("YX_MZ_SS","112");
            put("ZZ_YC_LZ_PO","114");
            put("ZZ_YC_LZ_SO","115");
            put("ZZ_YC_LZ_PI","116");
            put("ZZ_YC_LZ_SS","117");
            put("ZZ_YC_MZ_PO","118");
            put("ZZ_YC_MZ_PI","120");
            put("ZZ_YC_MZ_SO","121");
            put("ZZ_YC_MZ_SS","122");
            put("ZZ_YC_MZ_PI_C","123");
            put("ZZ_GX_LZ_PI","124");
            put("SO_LZ_PO","125");
            put("SO_LZ_SO","126");
            put("SO_LZ_PI","127");
            put("SO_LZ_SS","128");
            put("SOB_LZ_SO","129");
            put("SOB_LZ_SS","131");
            put("SOB_LZ_PI","134");
            put("DZ_CK_MZ_AP","135");
            put("DZ_CK_MZ_CJ","136");
            put("DZ_CK_MZ_P","137");
            put("YC_CK_MZ_AP","138");
            put("YC_CK_MZ_CJ","139");
            put("YC_CK_MZ_P","140");
            put("YX_CK_LZ_AP","141");
            put("YX_CK_LZ_CJ","142");
            put("YX_CK_LZ_P","143");
            put("YX_CK_LZ_R","144");
            put("YX_CK_MZ_AP","145");
            put("YX_CK_MZ_CJ","146");
            put("YX_CK_MZ_P","147");
            put("GZ_MZ_PI","148");
            put("WAREHOUSE","150");
            put("WareClinicRale_S","39");
            
        }
    };
	 
    /**
	 *  获取当前时间 格式化
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrentDateStr(){
  		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
	}
	
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
	public static boolean judgeMsgIdExists(Context ctx,String typeVal, String busCode,String msgId) {
	     boolean flag = false;
	     if (VerifyUtil.notNull(msgId) && VerifyUtil.notNull(busCode) ) {
	    	 String tableName ="";
	    	 String filed = "";
	    	 if("GZ_LZ_PO".equals(busCode)||"GZ_MZ_PO".equals(busCode)||"ZZ_YC_LZ_PO".equals(busCode)||"DZ_MZ_PO".equals(busCode)||"ZZ_YC_MZ_PO".equals(busCode)||"SO_LZ_PO".equals(busCode)){
	    		 tableName =" T_SM_PurOrder ";
	    		 filed ="FCOMPANYORGUNITID";
	    	 }else if("GZ_LZ_SO".equals(busCode)||"CGZ_U_MZ_SO".equals(busCode)||"DZ_MZ_SO".equals(busCode)||"CGZ_U_MZ_SO".equals(busCode)||"VMI_U_MZ_SO".equals(busCode)
	    			 ||"ZZ_YC_LZ_SO".equals(busCode)||"ZZ_YC_MZ_SO".equals(busCode)||"SO_LZ_SO".equals(busCode)||"SOB_LZ_SO".equals(busCode)){
	    		 tableName =" T_SD_SALEORDER ";
	    		 filed ="FCOMPANYORGUNITID";
	    	 }else if("GZ_LZ_PI".equals(busCode)||"GZ_MZ_PI".equals(busCode)||"VMI_MZ_PI".equals(busCode)||"VMIB_MZ_PI".equals(busCode)||"DZ_MZ_PI".equals(busCode)
	    			 ||"VMI2C_LZ_PI".equals(busCode)||"VMI2CB_LZ_PI".equals(busCode)||"VMI_MZ_PI".equals(busCode)||"VMI_U_LZ_PI".equals(busCode)||"VMI_MZ_PI".equals(busCode)
	    			 ||"YC_PI".equals(busCode)||"YX_MZ_PI".equals(busCode)||"ZZ_YC_MZ_PI".equals(busCode)
	    			 ||"GZB_MZ_PI".equals(busCode)||"GZB_LZ_PI".equals(busCode)||"DZB_MZ_PI".equals(busCode)||"VMI_U_LZ_PI".equals(busCode)  
	    			 ||"VMI2CB_LZ_PI".equals(busCode)||"VMIB_MZ_PI".equals(busCode)||"VMI_U_MZ_PI".equals(busCode)||"ZZ_YC_MZ_PI_C".equals(busCode) ){
	    		 tableName =" T_IM_PurInWarehsBill ";
	    		 filed ="FSTORAGEORGUNITID";
	    	 }else if("GZ_LZ_SS".equals(busCode)||"VMI_LZ_SS".equals(busCode)||"VMI_LZ_SS".equals(busCode)||"VMIB_LZ_SS".equals(busCode)||"SO_LZ_SS".equals(busCode)
	    			 ||"CGZ_U_MZ_SS".equals(busCode)||"CDZ_U_MZ_SS".equals(busCode)||"VMI_U_MZ_SS_VMI".equals(busCode)||"VMI_U_MZ_SS".equals(busCode)
	    			 ||"VMI_U_LZ_SS".equals(busCode)||"YX_LZ_SS".equals(busCode)||"YX_MZ_SS".equals(busCode)||"ZZ_YC_LZ_SS".equals(busCode)||
	    			 "ZZ_YC_MZ_SS".equals(busCode)||"GZB_LZ_SS".equals(busCode)){
	    		 tableName =" T_IM_SaleIssueBill ";
	    		 filed ="FSTORAGEORGUNITID";
	    	 }else if("GZ_CK_LZ_CJ".equals(busCode)||"VMI_CK_LZ_CJ".equals(busCode)||"VMI_CK_MZ_CJ".equals(busCode)||"DZ_CK_MZ_CJ".equals(busCode)||
	    			 "YC_CK_MZ_CJ".equals(busCode)||"YX_CK_LZ_CJ".equals(busCode)||"YX_CK_MZ_CJ".equals(busCode)){
	    		 tableName =" T_CL_CostAdjustBill ";
	    		 filed ="FSTORAGEORGUNITID";
	    	 }else if("GZ_CK_LZ_AP".equals(busCode)||"GZ_CK_MZ_AP".equals(busCode)||"VMI_CK_LZ_AP".equals(busCode)||"VMI_CK_MZ_AP".equals(busCode)||
	    		  "DZ_CK_MZ_AP".equals(busCode)||"YC_CK_MZ_AP".equals(busCode)||"YX_CK_LZ_AP".equals(busCode)||"YX_CK_MZ_AP".equals(busCode)){
	    		 tableName =" T_AP_OtherBill ";
	    		 filed ="FCOMPANYID";
	    	 }else if("GZ_CK_LZ_P".equals(busCode)||"VMI_CK_LZ_P".equals(busCode)||"DZ_CK_MZ_P".equals(busCode)||"YC_CK_MZ_P".equals(busCode)||
	    		  "YX_CK_LZ_P".equals(busCode)||"YX_CK_MZ_P".equals(busCode)){
	    		 tableName =" T_CAS_PaymentBill ";
	    		 filed ="FCOMPANYID";
	    	 }else if("GZ_CK_LZ_R".equals(busCode)||"VMI_CK_LZ_R".equals(busCode)||"YX_CK_LZ_R".equals(busCode)){
	    		 tableName =" T_CAS_ReceivingBill ";
	    		 filed ="FCOMPANYID";
	    	 }else if("GZ_CK_LZ_AR".equals(busCode)||"GZ_CK_MZ_AP".equals(busCode)||"VMI_CK_LZ_AR".equals(busCode)){
	    		 tableName =" T_AR_OtherBill ";
	    		 filed ="FCOMPANYID";
		     }else if("SK_MZ_OPI".equals(busCode)){
		    	 tableName =" T_IM_OtherInWarehsBill ";
		    	 filed ="FSTORAGEORGUNITID";
		     }else if("SK_MZ_OSS".equals(busCode)){
		    	 tableName =" T_IM_OtherIssueBill ";
		    	 filed ="FSTORAGEORGUNITID";
		     }
	    	 
	    	 String whereStr =" ";
	    	 	if(busCode.contains("_MZ_"))
	    	 		whereStr = filed +" !='jbYAAAMU2SvM567U' ";
	    	 else
	    		 if(busCode.contains("_LZ_"))
	    			 whereStr = filed +" ='jbYAAAMU2SvM567U' ";
	    	 	
	    	 	if(!"".equals(whereStr)){
	    	 		 try {
	    		    	 String sql = " select count(1) C from "+tableName+" where "+whereStr+" and CFBusCode = '"+typeVal+"' and CFMsgId = '"+msgId+"' ";
	    		         IRowSet rs = DbUtil.executeQuery(ctx, sql);
	    		         if (rs.next() && rs.getObject("C") != null && !"".equals(rs.getObject("C").toString()) ) {
 	    		        	if(Integer.parseInt(rs.getObject("C").toString()) > 0) 
	    		        		flag = true;
	    		         }
	    		       }
	    		       catch (BOSException e) {
	    		         e.printStackTrace();
	    		       } catch (SQLException e){
	    		         e.printStackTrace();
	    		       } 
	    	 	}
	       
	     }
	     return flag;
	  }
	
	/**
	 *  判断 批次是否已存在
	 * @param ctx
	 * @param oper 操作类型
	 * @param orgId 组织ID
	 * @param mno 物料编码
	 * @param lot 批次
	 * @return
	 */
	public static boolean judgeLotExists(Context ctx,String busCode,String orgId,String mno,String lot){
		boolean flag = false;
		 if (VerifyUtil.notNull(busCode) && VerifyUtil.notNull( mno)&& VerifyUtil.notNull( orgId)&& VerifyUtil.notNull( lot) ) {
			 String tableName = "";
			 if(busCode.contains("_PI"))
				 tableName = "T_IM_PurInWarehsEntry";
			 else if(busCode.contains("_SS"))
				 tableName = "T_IM_SaleIssueEntry";
			 
			 if(tableName !=null && !"".equals(tableName)){
				 String sql = "select count(1) C from "+tableName+" a inner join t_bd_material b on a.FMATERIALID =b.FID where a.FSTORAGEORGUNITID ='"+orgId+"' and b.FNUMBER ='"+mno+"' and a.FLOT='"+lot+"' ";
				 try {
					IRowSet rs = DbUtil.executeQuery(ctx, sql);
					 if (rs.next() && rs.getObject("C") != null && !"".equals(rs.getObject("C").toString())) {
 				        if(Integer.parseInt(rs.getObject("C").toString()) > 0) 
				        	flag = true;
 					 }
				} catch (NumberFormatException e) {
 					e.printStackTrace();
				} catch (BOSException e) {
 					e.printStackTrace();
				} catch (SQLException e) {
 					e.printStackTrace();
				}
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
//		    		 sql = " select count(1) C from T_BD_Material where FId ='"+fid+"' and FStatus =1 ";
 		    		 sql = " select count(1) C from T_BD_Material where FNumber ='"+fid+"' and FStatus =1 ";
 		    	 } else if("MP".equals(oper)){
//		    		 sql = " select count(1) C from T_BD_MaterialPurchasing where FMaterialID ='"+fid+"' and FOrgUnit = '"+orgId+"' ";
		    		 sql = "  select count(1) C from T_BD_Material a inner join T_BD_MaterialPurchasing b on a.fid=b.FMATERIALID where a.FNumber ='"+fid+"' and b.FOrgUnit = '"+orgId+"' ";
 		    	 }else if("UNIT".equals(oper)){
		    		 sql = " select count(1) C from T_BD_MeasureUnit where FNumber ='"+fid+"' and FGroupID ='CUYZGEtVTzqROJAmOrUCEBwqyGg=' ";
 		    	 }else if("USER".equals(oper)){
		    		 sql = " select count(1) C T_PM_User where FPERSONID = '"+fid+"' ";
 		    	 }else if("CUS".equals(oper)){
		    		 sql = " select count(1) C from T_BD_Customer where FId ='"+fid+"' and FUsedStatus =1 ";
 		    	 }else if("CUSC".equals(oper)){
		    		   sql = " select count(1) C from T_BD_CustomerCompanyInfo where FCustomerID ='"+fid+"' and FComOrgID ='"+orgId+"' ";
 		    	 } else if("CUSS".equals(oper)){
		    		 sql = " select count(1) C from T_BD_CustomerSaleInfo where FCustomerID ='"+fid+"' and FSaleOrgID ='"+orgId+"' ";
 		    	 } else if("PurOrder".equals(oper)){
		    		 sql = " select count(1) C from T_SM_PurOrder where CFMsgId ='"+fid+"' and FPurchaseOrgUnitID ='"+orgId+"' ";
 		    	 }   else if("PurOrderEntry".equals(oper)){
		    		 sql = " select count(1) C from T_SM_PurOrderEntry where CFMsgId ='"+fid+"' and FStorageOrgUnitID ='"+orgId+"' ";
 		    	 } else if("Warehouse".equals(oper)){
		    		 sql = " select count(1) C from T_DB_WAREHOUSE where FID ='"+fid+"' and FstorageOrgID ='"+orgId+"' ";
 		    	 }      
		    	 
		    	 if(VerifyUtil.notNull(oper) ){
		    	      try {
					         IRowSet rs = DbUtil.executeQuery(ctx, sql);
					         if (rs.next() && rs.getObject("C") != null && !"".equals(rs.getObject("C").toString()) ) {
 					           if(Integer.parseInt(rs.getObject("C").toString()) > 0) 
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
 					" where m.FNumber ='"+mId+"'";
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
			        		 mp.put("xh", rs.getObject("CFXINGHAO").toString()); 
			        	 else
			        		 mp.put("xh","");  
			        	 
			        	 
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
	
	
	/**
	 *  通过 采购组织ID 获取 采购组织对应的控制单元ID
	 * @param ctx
	 * @param purOrgId
	 * @return
	 */
	public static Map<String,String> getOrderRaleMapByMsgId(Context ctx,String orgId,String mId,String entryId,String oper,String typeVal){
		Map<String,String> mp = null ;
		if(VerifyUtil.notNull(mId) && VerifyUtil.notNull(orgId) && VerifyUtil.notNull(oper)  && VerifyUtil.notNull(typeVal)  && VerifyUtil.notNull(entryId) ){
			String  sql = " select a.FID ,a.FNumber,b.FID EntryID,b.FSEQ  from  ";
			 if("P".endsWith(oper)){
				 sql =sql+" T_SM_PurOrder a inner join T_SM_PurOrderEntry b ";
	    	 }else if("S".equals(oper)){
	    		 sql =sql+" T_SD_SaleOrder a inner join T_SD_SaleOrderEntry b ";
	    	 } 
			sql =sql+" on a.FID = b.FParentID where a.CFMSGID = '"+mId+"' and b.CFMSGID ='"+entryId+"'and a.CFBusCode = '"+typeVal+"' and a.FCompanyOrgUnitID = '"+orgId+"' ";
		     try {
		    	 mp = new HashMap<String,String>();
		         IRowSet rs = DbUtil.executeQuery(ctx, sql);
		         if (rs.next()){
		        	 if(rs.getObject("FID")!=null &&!"".equals(rs.getObject("FID").toString()))
		        		 mp.put("id", rs.getObject("FID").toString());
		        	  
		        	 if(rs.getObject("FNumber")!=null &&!"".equals(rs.getObject("FNumber").toString()))
		        		 mp.put("number", rs.getObject("FNumber").toString()); 
		        	 
		         	 if(rs.getObject("EntryID")!=null &&!"".equals(rs.getObject("EntryID").toString()))
		        		 mp.put("eid", rs.getObject("EntryID").toString()); 
		         	 
		         	 if(rs.getObject("FSEQ")!=null &&!"".equals(rs.getObject("FSEQ").toString()))
		        		 mp.put("seq", rs.getObject("FSEQ").toString()); 
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
	/**
	 *  通过 采购组织ID 获取 采购组织对应的控制单元ID
	 * @param ctx
	 * @param purOrgId
	 * @return
	 */
	public static Map<String,String> getOrderEntryMapByMsgId(Context ctx,String orgId,String mId,String oper){
		 Map<String,String> mp = null ;
 		if(VerifyUtil.notNull(mId) && VerifyUtil.notNull(orgId) && VerifyUtil.notNull(oper) ){
 			 String tableName ="";
	    	 if("P".endsWith(oper)){
	    		 tableName =" T_SM_PurOrderEntry ";
	    	 }else if("S".equals(oper)){
	    		 tableName =" T_SD_SaleOrderEntry ";
	    	 }
 			mp = new HashMap<String,String>();
 			String  sql = " select FID,FSEQ from "+tableName+" where CFMsgId ='"+mId+"' and FCompanyOrgUnitID ='"+orgId+"' ";

			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next()){
			        	 if(rs.getObject("FID")!=null &&!"".equals(rs.getObject("FID").toString()))
			        		 mp.put("id", rs.getObject("FID").toString());
			        	  
			        	 if(rs.getObject("FSEQ")!=null &&!"".equals(rs.getObject("FSEQ").toString()))
			        		 mp.put("seq", rs.getObject("FSEQ").toString()); 
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
	
	
	public static Map<String,String> getOrderMapByNumber(Context ctx,String orgId,String mId,String oper){
		 Map<String,String> mp = null ;
	 		if(VerifyUtil.notNull(mId) && VerifyUtil.notNull(orgId) && VerifyUtil.notNull(oper) ){
			mp = new HashMap<String,String>();
			 String tableName ="";
	    	 if("P".endsWith(oper)){
	    		 tableName =" T_SM_PurOrder ";
	    	 }else if("S".equals(oper)){
	    		 tableName =" T_SD_SaleOrder ";
	    	 }
	    	 
			String  sql = " select FID,FNUMBER from "+tableName+" where CFMSGID ='"+mId+"' and FCompanyOrgUnitID ='"+orgId+"' ";
			     try {
			         IRowSet rs = DbUtil.executeQuery(ctx, sql);
			         if (rs.next()){
			        	 if(rs.getObject("FID")!=null &&!"".equals(rs.getObject("FID").toString()))
			        		 mp.put("id", rs.getObject("FID").toString());
			        	  
			        	 if(rs.getObject("FNUMBER")!=null &&!"".equals(rs.getObject("FNUMBER").toString()))
			        		 mp.put("number", rs.getObject("FNUMBER").toString()); 
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
	
	/**
	 * 根据下推规则名称获取 下推规则ID
	 * @param ctx
	 * @param fname
	 * @return
	 */
	public static String getMappIdByFName(Context ctx,String fname){
		String mappId ="";
		if(fname !=null && !"".equals(fname)){
 	 		  String sql = "select FID from T_BOT_Mapping where FName='"+fname+"'"; 
			  try {
				  IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx, sql);
				  if(rs!=null){
					  while(rs.next()){
						  if( rs.getObject("FID")!=null && !"".equals( rs.getObject("FID").toString()))
							  mappId = rs.getObject("FID").toString();
					  }
				  }
			} catch (BOSException e) {
	 			e.printStackTrace();
			} catch (SQLException e) {
	 			e.printStackTrace();
			} 
		}
		return mappId;
	}
	
	  /**
	  *  技加工业务类型 集合
	  */
	 public final static HashSet<String> process_BusCode_List = new HashSet<String>(){
	 	{
	 		add("YC_PI");//	义齿加工-采购入库单 
	 		add("YC_SS");//	义齿加工-销售出库单 
	 		add("YX_MZ_PI");//	隐形矫正加-门诊采购入库单 
	 		add("YX_LZ_PI");//	隐形矫正加-栗喆采购入库单 
	 		add("YX_LZ_SS");//	隐形矫正加-栗喆销售出库单 
	 		add("YX_MZ_SS");//	隐形矫正加-门诊销售出库 
	 		//add("ZZ_GX_LZ_PI");//	种植个性化基台栗喆-采购入库单 
	 	}
	 }; 
	 
	  /**
	  *  检查库存数量 集合
	  */
	 public final static HashSet<String> checkwarecount_BusCode_List = new HashSet<String>(){
	 	{
	 		add("GZ_LZ_SS");//	栗喆销售出库单 
	 		add("GZB_LZ_PI");//	栗喆-采购入库单(红单) 
	 		add("GZB_MZ_PI");//	门诊采购入库(红单) 
	 		add("DZB_MZ_PI");//	门诊采购入库单(红单)
	 		add("VMI2CB_LZ_PI");//	隐形矫正加-栗喆销售出库单  
	 		add("VMIB_MZ_PI");//	门诊-采购入库单VMI(红单)
	 		add("CGZ_U_MZ_SO");//	门诊高值领用-销售订单
	 		add("VMI_U_MZ_SO");//	VMI物料领用 门诊-销售订单 
	 		add("YC_SS");//	义齿加工-销售出库单
	 		add("YX_MZ_SS");//	隐形矫正加-门诊销售出库
	 		add("YX_LZ_SS");//	隐形矫正加-栗喆销售出库单 
	 		add("ZZ_YC_LZ_SS");//种植原厂基台-粟喆-销售出库单 
	 		add("ZZ_YC_MZ_SS");//种植原厂基台-门诊-销售出库单 
	 		add("SO_LZ_SS");//对外销售采购-栗喆-销售出库单
	 		add("SOB_LZ_PI");//对外销售退货-栗喆-采购入库单
	 	}
	 }; 
	 
	 
	 /**
	  * 判断时间格式 格式必须为“YYYY-MM-dd”
	  * 2004-2-30 是无效的
	  * 2003-2-29 是无效的
	  * @param sDate
	  * @return
	  */
	 public static boolean judgeDateFormat(String sDate) {
	     int legalLen = 10;
	     if ((sDate == null) || (sDate.length() != legalLen)) {
	         return false;
	     } 
	     DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	     try {
	         Date date = format.parse(sDate);
	         return sDate.equals(format.format(date));
	     } catch (Exception e) {
	         return false;
	    }
	 }
	 
	 /**
	  * 判断时间格式 格式必须为“YYYY-MM-dd”
	  * 2004-2-30 是无效的
	  * 2003-2-29 是无效的
	  * @param sDate
	  * @return
	  */
	 public static Date getDateFormat(String sDate) {
	     int legalLen = 10;
	     if ((sDate == null) || (sDate.length() != legalLen)) {
	         return null;
	     } 
	     DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	     try {
	         return  format.parse(sDate);
	        // return sDate.equals(format.format(date));
	     } catch (Exception e) {
	         return null;
	    }
	 }
	 
	 /**
	  * 判断仓库是否结束初始化
	  * @param ctx
	  * @param storageOrgId
	  * @param warehouseId
	  * @return
	  * @throws EASBizException
	  */
		public static boolean getWareIsStarted(Context ctx,String storageOrgId,String warehouseId) throws EASBizException{
			boolean flag = false;
			try {
				if(storageOrgId !=null && !"".equals(storageOrgId) && warehouseId !=null && !"".equals(warehouseId)){
					String sql ="select sw.FIsStarted from T_DB_SOAccreditWH sw "+
					" inner join T_DB_WAREHOUSE w on sw.FWAREHOUSEID = w.FID "+
					" where w.FID ='"+warehouseId+"' and w.FSTORAGEORGID = '"+storageOrgId+"' and w.FWhState = 1 and w.FTransState = 1 ";
					 IRowSet rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
					 if(rs!=null && rs.size() > 0){
						  if(rs.next()){		
							 if(rs.getObject("FIsStarted")!=null && !"".equals(rs.getObject("FIsStarted").toString()))
								 if("1".equals(rs.getObject("FIsStarted").toString()))
									 flag = true;
						  }
					 }		
				}
			} catch (BOSException e) {
	 			e.printStackTrace();
			} catch (SQLException e) {
	 			e.printStackTrace();
			}
			return flag;
		}
		
		public static BigDecimal getInventoryQtyVIM(Context ctx, String orgId,String houseId, String unitId, String materNumber) {
			BigDecimal qty = new BigDecimal(0.0D);
			StringBuffer sql = new StringBuffer();
			sql.append("/*dialect*/select a.FStorageOrgUnitID,a.fwarehouseid,").append("\n");
			sql.append("a.FUnitID,a.FMaterialID,sum(a.FBaseQty) FQuantity").append("\n");
			sql.append("from T_IM_Inventory a inner join T_BD_MATERIAL b on a.FMaterialID =b.FID ").append("\n");
			sql.append(" inner join T_BD_MeasureUnit c on a.FUnitID = c.FID ").append("\n");
			sql.append(" where FSTORESTATUSID='181875d5-0105-1000-e000-012ec0a812fd62A73FA5'").append("\n");
			sql.append(" and a.FStoreTypeID='181875d5-0105-1000-e000-0114c0a812fd97D461A6'").append("\n");
			sql.append(" and a.FStorageOrgUnitID = '").append(orgId).append("'");
			sql.append(" and a.FCOMPANYORGUNITID = '").append(orgId).append("'");
			sql.append(" and a.fwarehouseid = '").append(houseId).append("'");
			sql.append(" and c.FNumber = '").append(unitId).append("'");
			sql.append(" and b.FNumber = '").append(materNumber).append("'");
			sql.append(" group by a.FStorageOrgUnitID,a.fwarehouseid,a.FUnitID,a.FMaterialID");
			IRowSet rs = null;
			try {
				rs = DBUtil.executeQuery(ctx, sql);
				if ((rs != null) && (rs.size() > 0) && (rs.next())&& (rs.getObject("FQuantity") != null))
					qty = (BigDecimal) rs.getObject("FQuantity");
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return qty;
		}
		
		public static BigDecimal getInventoryQty(Context ctx, String orgId,String houseId, String unitId, String materNumber, boolean isZengpin) {
			BigDecimal qty = new BigDecimal(0.0D);
			StringBuffer sql = new StringBuffer();
			String isZengpinSql = "";
			if (isZengpin)
				isZengpinSql = " and a.FStoreTypeID = 'N5YMUAEgEADgAABPwKg/GpfUYaY=' ";
			else {
				isZengpinSql = " and a.FStoreTypeID = '181875d5-0105-1000-e000-0111c0a812fd97D461A6' ";
			}
			Map mp = new HashMap();
			sql.append("/*dialect*/select a.FStorageOrgUnitID,a.fwarehouseid,").append("\n");
			sql.append("a.FUnitID,a.FMaterialID,sum(a.FBaseQty) FQuantity").append("\n");
			sql.append("from T_IM_Inventory a inner join T_BD_MATERIAL b on a.FMaterialID =b.FID ").append("\n");
			sql.append(" inner join T_BD_MeasureUnit c on a.FUnitID = c.FID ").append("\n");
			sql.append(" where a.FSTORESTATUSID='181875d5-0105-1000-e000-012ec0a812fd62A73FA5'").append("\n");
			sql.append(" and a.FStorageOrgUnitID = '").append(orgId).append("'").append(isZengpinSql);
			sql.append(" and a.FCOMPANYORGUNITID = '").append(orgId).append("'");
			sql.append(" and a.fwarehouseid = '").append(houseId).append("'");
			sql.append(" and c.FNumber = '").append(unitId).append("'");
			sql.append(" and b.FNumber = '").append(materNumber).append("'");
			sql.append(" group by a.FStorageOrgUnitID,a.fwarehouseid,a.FUnitID,a.FMaterialID");
			IRowSet rs = null;
			try {
				rs = DBUtil.executeQuery(ctx, sql);
				if ((rs != null) && (rs.size() > 0) && (rs.next())&& (rs.getObject("FQuantity") != null))
					qty = (BigDecimal) rs.getObject("FQuantity");
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return qty;
		}
		
		
}
