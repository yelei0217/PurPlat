package com.kingdee.eas.mw.srqr.app.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.RowSet;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.base.core.fm.ContextHelperFactory;
import com.kingdee.eas.base.permission.UserInfo;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitFactory;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.OrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.person.PersonFactory;
import com.kingdee.eas.basedata.person.PersonInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.util.DBUtil;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.mw.pay.CostDetailFactory;
import com.kingdee.eas.mw.pay.CostDetailInfo;
import com.kingdee.eas.mw.pay.CostSumCollection;
import com.kingdee.eas.mw.pay.CostSumFactory;
import com.kingdee.eas.mw.pay.CostSumInfo;
import com.kingdee.eas.mw.pay.ICostDetail;
import com.kingdee.eas.mw.srqr.CostComputeHeaderLogFactory;
import com.kingdee.eas.mw.srqr.CostComputeHeaderLogInfo;
import com.kingdee.eas.mw.srqr.ICostComputeHeaderLog;
import com.kingdee.eas.mw.srqr.ISaleIssueHisLog;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogCollection;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogEntryCollection;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogEntryInfo;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogFactory;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogInfo;
import com.kingdee.eas.mw.srqr.app.status;
import com.kingdee.eas.mw.srqr.app.syncStatus;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.SaleIssueBillCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class SaleIssueSyncTool {
	/**
	 * 获取实体的财务组织和财务组织当前的会计期间
	 * @param ctx
	 * @return 
	 */
	private static List getCompanyAndPeriod(Context ctx){
		OrgUnitInfo unit =  ContextUtil.getCurrentOrgUnit(ctx) ; 
		String sql = "/*dialect*/SELECT distinct a.fcompanyid as COMPANYID,a.FCurrentPeriodID as PERIODID , c.fnumber as PERIODNUM "+
	    " from T_BD_SystemStatusCtrol a inner join T_ORG_COMPANY b on  a.fcompanyid =b.fid  "+
	    " inner join T_BD_Period c on  a.FCurrentPeriodID =c.fid  "+
	    " where a.FSystemStatusID ='e45c1988-00fd-1000-e000-33d8c0a8100d02A5514C' and a.FIsStart =1 "+
	    " and b.FIsSealUp = 0 and b.FIsBizUnit = 1 and to_char(FInvalidDate,'yyyyMMDD')='21991231' " +
	    " and b.FIsAssistantOrg = 0 and c.fnumber > '202101' and FIsStorageOrgUnit =1 and FIsCompanyOrgUnit =1" ;
		//  " and c.fperiodyear =  to_char(sysdate,'yyyy') ";
//		sql =sql+" and a.FCOMPANYID = 'kO6+E1zaSbKzTzLMtI+csMznrtQ=' ";
		 List list = new ArrayList();
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){		 
						Map mp = new HashMap();	  
						mp.put("companyid", rs.getObject("COMPANYID"));
						mp.put("periodid", rs.getObject("PERIODID"));
						mp.put("periodnum", rs.getObject("PERIODNUM"));
						
// 						mp.put("periodid", "jbYAAAEXnCOCOIxM");
//						mp.put("periodnum", "202011");
						
						list.add(mp);
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		return list;
	}
	
	/**
	 * 获取实体的财务组织组织ID获取会计期间编码
	 * @param ctx
	 * @return 
	 */
	private static String getPeriodNumberByCompanyId(Context ctx,String companyId){
	//	OrgUnitInfo unit =  ContextUtil.getCurrentOrgUnit(ctx) ;
		String sql = "/*dialect*/SELECT distinct a.fcompanyid as COMPANYID,a.FCurrentPeriodID as PERIODID , c.fnumber as PERIODNUM "+
	    " from T_BD_SystemStatusCtrol a inner join T_ORG_COMPANY b on  a.fcompanyid =b.fid  "+
	    " inner join T_BD_Period c on  a.FCurrentPeriodID =c.fid  "+
	    " where a.FSystemStatusID ='e45c1988-00fd-1000-e000-36b8c0a8500d02A5514C' and  a.FIsStart =1 "+
	    " and b.FIsSealUp = 0 and b.FIsBizUnit = 1 and to_char(FInvalidDate,'yyyyMMDD')='21991231' and a.FCOMPANYID = '"+companyId+"' ";
		String periodNumber ="";
 		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){		 
						  if(rs.getObject("PERIODNUM")!=null && !"".equals(rs.getObject("PERIODNUM"))){
							  periodNumber = rs.getObject("PERIODNUM").toString();
						  }
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		return periodNumber;
	}
	
	/**
	 *   获取 存货结转明细报告报告头 根据财务组织id 和 会计期间id
	 * @param ctx
	 * @param companyID 财务组织id
	 * @param periodID  会计期间id
	 * @return
	 */
	private static String getRportHeaderID(Context ctx,String companyID,String periodID){
		String handerId ="";
		if(companyID!=null && !"".equals(companyID)&& periodID!=null && !"".equals(periodID)){
		String sql = "/*dialect*/select FID from (select FID from T_CL_CostComputeRportHeader" +
				" where FCOMPANYID = '"+companyID+"' and FPERIODID ='"+periodID+"'"+
				" and not exists(SELECT * from CT_SRQ_CostComputeHeaderLog where CT_SRQ_CostComputeHeaderLog.FNumber = T_CL_CostComputeRportHeader.FID ) "+
				" order by FCREATETIME desc ) where rownum<=1 " ;
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  if(rs.next()){	
						  if(rs.getObject("FID")!=null && !"".equals(rs.getObject("FID").toString()))
							  handerId = rs.getObject("FID").toString();
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return handerId;
	}
	
	/**
	 * 获取 根据报告头 存货结转明细报告报明细
	 * @param ctx
	 * @param handerID
	 * @return
	 */
	private static List<String> getBillNumberByHanderID(Context ctx,String handerID){
		List<String>  list = new ArrayList<String> ();
		if(handerID!=null && !"".equals(handerID)){
		String sql ="/*dialect*/select distinct FBILLNUMBER  from T_CL_CostComputeReport" +
	  //  " where FPARENTID ='"+handerID+"' and FCalculateKind =1 and FBILLNAME ='销售出库单 - 普通销售出库' and FBILLNUMBER  not LIKE '*VMI%' ";
	    " where FPARENTID ='"+handerID+"' and FCalculateKind =1 and FBILLNAME ='销售出库单 - 普通销售出库'   ";

		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FBILLNUMBER")!=null && !"".equals(rs.getObject("FBILLNUMBER").toString()))
							  list.add(rs.getObject("FBILLNUMBER").toString());
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	/**
	 *  判断log是否存在
	 * @param ctx
	 * @param companyID
	 * @param number
	 * @param year
	 * @param period
	 * @return
	 */
	private static boolean judgeLogExists(Context ctx,String companyID,String number,int year,int period){
		boolean flag = false;
		String sql = "select count(1) C from CT_SRQ_SaleIssueHisLog " +
				" where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' " ;
			  //  " and CFYear ="+year+" and CFPeriod = "+period;
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("C")!=null && !"".equals(rs.getObject("C").toString()))
							 if( Integer.parseInt(rs.getObject("C").toString()) > 0 )
								 flag = true; 
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		
		return flag ;
	}
	
	  
	private static boolean updateHisLog(Context ctx,String companyID,String number){
		boolean flag = false;
		String sql = "update CT_SRQ_SaleIssueHisLog set cfsyncstatus = 0 where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' " ;
		  IRowSet rs = null;
			try {
				 com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql);
			} catch (BOSException e) {
				e.printStackTrace();
			} 
		return flag ;
	}
	
	
	
	/**
	 * 获取 Log FID
	 * @param ctx
	 * @param companyID
	 * @param number
	 * @param year
	 * @param period
	 * @return
	 */
	private static String getLogFID(Context ctx,String companyID,String number,int year,int period){
		String fid = "";
		String sql = "select FID from CT_SRQ_SaleIssueHisLog " +
				" where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' " +
			    " and CFYear ="+year+" and CFPeriod = "+period;
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FID")!=null && !"".equals(rs.getObject("FID").toString()))
							fid = rs.getObject("FID").toString();
 					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		return fid ;
	}
	
	/**
	 * 修改log为反审核
	 * @param ctx
	 * @param companyID
	 * @param number
	 * @param year
	 * @param period
	 */
	private static void updateSaleIssueHisLogStatus(Context ctx,String companyID,String number,int year,int period){
		String sql = "updaate CT_SRQ_SaleIssueHisLog set CFStatus = 2" +
				" where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' " +
				" and CFYear ="+year+" and CFPeriod = "+period;
 			try {
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql);
			} catch (BOSException e) {
				e.printStackTrace();
			}  
	}
	
	private static void delSaleIssueHisLogStatus(Context ctx,String number){
		String sql = "delete from CT_SRQ_SaleIssueHisLog where CFIssNumber = '"+number+"' " ; 
		String sql1 = "delete from CT_SRQ_SaleIssueHisLogEntry where FParentID in (select fid from CT_SRQ_SaleIssueHisLog where CFIssNumber = '"+number+"' )" ; 
 			try {
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql1);

				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql);
			} catch (BOSException e) {
				e.printStackTrace();
			}  
	}
	
	
	private static void delSaleIssueHisLogStatus(Context ctx,String companyID,String number){
		String sql = "delete from CT_SRQ_SaleIssueHisLog where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' " ; 
		String sql1 = "delete from CT_SRQ_SaleIssueHisLogEntry where FParentID in (select fid from CT_SRQ_SaleIssueHisLog where CFCompanyID = '"+companyID+"' and CFIssNumber = '"+number+"' )" ; 
 			try {
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql1);
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql);
			} catch (BOSException e) {
				e.printStackTrace();
			}  
	}
	
	
	
	public static void saveHisLog(Context ctx,SaleIssueBillInfo info,status st){
		 try {
			ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
			CoreBaseInfo logInfo = createHisLog(ctx,info,st);
			if(st != status.unaudit){
				if(logInfo != null){
					ish.addnew(logInfo);
				}
			} 
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (EASBizException e) {
			e.printStackTrace();
		}
	}
	
	
	public static IObjectPK saveHisLogWithReturn(Context ctx,SaleIssueBillInfo info,status st){
		IObjectPK pk = null ;
		 try {
			ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
			CoreBaseInfo logInfo = createHisLog(ctx,info,st);
			if(st != status.unaudit){
				if(logInfo != null){
					pk = ish.addnew(logInfo);
				}
			} 
		} catch (BOSException e) {
			e.printStackTrace();
		} catch (EASBizException e) {
			e.printStackTrace();
		}
		return pk;
	}
	
	
	
	private static CoreBaseInfo createHisLog(Context ctx,SaleIssueBillInfo info,status st){
		 SaleIssueHisLogInfo logInfo =null;
		try{
	 		 if(info.get("hisdanjubianma")!=null&&!"".equals(info.get("hisdanjubianma").toString())
	 			//&&info.get("HisReqID")!=null&&!"".equals(info.get("HisReqID").toString())
	 			&&info.getBizType()!=null && info.getBizType().getId()!=null&&
	 			("d8e80652-010e-1000-e000-04c5c0a812202407435C".equals(info.getBizType().getId().toString())||
	 			 "d8e80652-0110-1000-e000-04c5c0a812202407435C".equals(info.getBizType().getId().toString()))
	 			){
	 			//&&!info.getNumber().startsWith("*VMI")){
	 		       String issNumber = info.getNumber();
	 		       String companyId = info.getStorageOrgUnit().getId().toString();
			 		   SaleIssueHisLogEntryCollection items = new SaleIssueHisLogEntryCollection();
			 		   SaleIssueEntryCollection entryCollection = info.getEntry();
				       Iterator<SaleIssueEntryInfo> it = entryCollection.iterator(); 
				      // ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
					 while (it.hasNext()) { 
				    	 SaleIssueEntryInfo entry = it.next();
				    	 if(entry.get("hissfxmid")!=null && !"".equals(entry.get("hissfxmid").toString())
				    		&&entry.get("hismingxiID")!=null && !"".equals(entry.get("hismingxiID").toString())&&
				    		("8r0AAAAEaOnC73rf".equals(entry.getInvUpdateType().getId().toString())||
				    		 "8r0AAAAEaPbC73rf".equals(entry.getInvUpdateType().getId().toString())||
				    		 "CeUAAAAIdBvC73rf".equals(entry.getInvUpdateType().getId().toString()))	
				    		){
				    		 //普通出库 8r0AAAAEaOnC73rf
				    		 //赠品出库 8r0AAAAEaPbC73rf
				    		 //Vmi出库 CeUAAAAIdBvC73rf
				    		SaleIssueHisLogEntryInfo item = new SaleIssueHisLogEntryInfo();
				    		if("8r0AAAAEaPbC73rf".equals(entry.getInvUpdateType().getId().toString())){
				    			item.setAmount(entry.getStandardCost());
				    			item.setQty(entry.getQty());
				    		}else if("CeUAAAAIdBvC73rf".equals(entry.getInvUpdateType().getId().toString())){
				    			item.setQty(entry.getQty());
				    			item.setAmount(BigDecimal.ZERO);
				    		}else{
				    			item.setAmount(entry.getActualCost());
				    			if(info.getNumber().startsWith("*VMISIssue_SIssue")){
				    				item.setQty(BigDecimal.ZERO);
				    			}else{
				    				item.setQty(entry.getQty());
				    			}
				    		}
				    		//item.setQty(entry.getQty());
				    		if(entry.get("hismingxiID").toString().startsWith("SALE"))
				    			item.setHISID(entry.get("hismingxiID").toString().substring(4));
					    	else
					    		item.setHISID(entry.get("hismingxiID").toString());	
				    		
				    		item.setHISPayItem(entry.get("hissfxmid").toString());
				    		item.setIssEntryID(entry.getId().toString());
				    		items.add(item);
				    	 }
				     }
	  		       if(items!=null && items.size() > 0){
	  		    	   SaleIssueHisLogInfo tempInfo = createLogInfo(ctx,items,info,st,companyId,issNumber);  		    	 
	  		    	 if(st == status.audit){
	  		    		if(!judgeLogExists(ctx,companyId,issNumber,info.getYear(),info.getPeriod())){
	  		    			logInfo = tempInfo;
		  		    	 }
//	  		    		else{
//		  		    		 //如果存在修改状态
//			  		    	List<String> strList =  crateSQL(ctx,tempInfo,"u"); ;
//			   				if(strList!=null && strList.size() > 0)
//				  			EAISynTemplate.executeBatch(ctx, "04",strList);
//		  		    	 }
	  		    	 }else if(st == status.unaudit){
//	  		    		 if(judgeLogExists(ctx,companyId,issNumber,info.getYear(),info.getPeriod())){
//	  		    			List<String> modifyStrList = crateSQL(ctx,tempInfo,"unaudit"); 
//		  					if(modifyStrList!=null && modifyStrList.size() > 0)
//		  					EAISynTemplate.executeBatch(ctx, "04",modifyStrList);
//	  		    		 }
	  		    		 delSalaIssueByNumber(ctx,issNumber,"04");// 删除中间表
	  		    		 if(judgeLogExists(ctx,companyId,issNumber,info.getYear(),info.getPeriod())){
	  		    			 delSaleIssueHisLogStatus(ctx,companyId,issNumber);
	  		    		 }
	  		    	 }else if(st == status.Cost){
	  		    		 if(judgeLogExists(ctx,companyId,issNumber,info.getYear(),info.getPeriod())){
	  		    			 delSaleIssueHisLogStatus(ctx,companyId,issNumber);
	  		    		 }
  		    			logInfo = tempInfo;
	  		    	 }
	  		       }
	 		   }
	 		}catch(Exception e){
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.HIS_SaleOut,info.getNumber(),info.getNumber(),"单据保存失败");//记录日志
	 		}
	 	 return logInfo;
	}
	
	private static SaleIssueHisLogInfo createLogInfo
	   (Context ctx,SaleIssueHisLogEntryCollection items ,SaleIssueBillInfo info,status st,String companyid,String issnumber){
		   SaleIssueHisLogInfo logInfo = new SaleIssueHisLogInfo();
	       UserInfo userInfo = ContextUtil.getCurrentUserInfo(ctx);
			    logInfo.getEntrys().addCollection(items);
		    	logInfo.setYear(info.getYear());
		    	logInfo.setPeriod(info.getPeriod());
		    	logInfo.setIssNumber(issnumber);
			    logInfo.setCompanyID(companyid);
		    	logInfo.setIssID(info.getId().toString());
		    	logInfo.setSyncStatus(syncStatus.NO_SYNC);
			  	Calendar cal=Calendar.getInstance();
			    cal.setTime(new Date());
			    Timestamp tt =  new Timestamp(cal.getTimeInMillis());
			    String version = String.valueOf(cal.getTimeInMillis());
		    	logInfo.setNumber(version);
		    	if(info.get("HisReqID")!=null && !"".equals(info.get("HisReqID"))){
			    	if(info.get("HisReqID").toString().startsWith("SALE"))
			    		logInfo.setHISID(info.get("HisReqID").toString().substring(4));
			    	else
			    		logInfo.setHISID(info.get("HisReqID").toString());	
		    	}else{
		    		logInfo.setHISID("");	
		    	}
		    	logInfo.setDescription(info.getNumber()+"--"+version);
		    	logInfo.setBizDate(new Date());
		    	logInfo.setHandler(userInfo);
		    	logInfo.setCreator(userInfo);
		    	logInfo.setCreateTime(tt);
		    	logInfo.setLastUpdateTime(tt);
		    	logInfo.setLastUpdateUser(userInfo);
		    	logInfo.setStatus(st);
		    	CtrlUnitInfo cu = new CtrlUnitInfo();
		    	cu.setId(BOSUuid.read("00000000-0000-0000-0000-000000000000CCE7AED4"));
		    	logInfo.setCU(cu);
		return logInfo;
	}
	

	private static void delSaleIssueHisLog(Context ctx,String companyID,String year,String period){
		String sql = "delete from CT_SRQ_SaleIssueHisLog where CFCompanyID = '"+companyID+"' and CFYear = '"+year+"' and CFPeriod = '"+period+"'";
		String sql1 = "delete from CT_SRQ_SaleIssueHisLogEntry where FParentID in (select fid from CT_SRQ_SaleIssueHisLog where CFCompanyID = '"+companyID+"' and CFYear = '"+year+"' and CFPeriod = '"+period+"')" ; 
 			try {
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql1);
				  com.kingdee.eas.custom.util.DBUtil.execute(ctx,sql);
			} catch (BOSException e) {
				e.printStackTrace();
			}  
	}
	
	
	private static void delSalaIssueMid(Context ctx,String dbid,String companyID,String year,String period){
		  List<String> delList = new ArrayList<String>();
		  String str1 = "delete EAS_SALEISSUE_SUB_HIS where FPARENTID in (select fid from EAS_SALEISSUE_HIS where FOrgID = '"+companyID+"' and FYear='"+year+"' and  FPeriod='"+period+"' ) ";
		  delList.add(str1);
		  String str2 = "delete EAS_SALEISSUE_HIS where FOrgID='"+companyID+"' and FYear = '"+year+"' and  FPeriod='"+period+"' ";
		  delList.add(str2);
		  try {
			  EAISynTemplate.executeBatch(ctx,dbid,delList);
		  } catch (BOSException e) {
			  System.out.println(str1);
			  System.out.println(str2);
		    e.printStackTrace();
		  }
	} 
	
	public static void doCostSync(Context ctx,String companyId){
		if(companyId !=null && !"".equals(companyId)){
			String periodNum = getPeriodNumberByCompanyId(ctx,companyId);
			if(periodNum !=null && !"".equals(periodNum)){
				String year=  periodNum.substring(0, 4);
 				String month = periodNum.substring(4);
 				  delSaleIssueHisLog(ctx,companyId,year,month);  //批量删除销售出库记录单数据
				  delSalaIssueMid(ctx,"04",companyId,year,month); //批量删除中间表记录
				  List<String> billNumberList = getBillNumberByComIDAndPeriod(ctx,companyId,periodNum);
					try {
						ISaleIssueBill isf = SaleIssueBillFactory.getLocalInstance(ctx);
						ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
						CoreBaseCollection logCollectoin = new CoreBaseCollection();
						  if(billNumberList != null && billNumberList.size()>0){
								for(String issnumber : billNumberList){ 
									if(isf.exists("where number='"+issnumber+"'")){
										SaleIssueBillInfo info = isf.getSaleIssueBillInfo("where number='"+issnumber+"'");
										CoreBaseInfo logInfo = createHisLog(ctx,info,status.Cost);
										if(logInfo != null){
	 										logCollectoin.add(logInfo);
										}
									}
								}
						  }	
						  if(logCollectoin.size()>0){
								ish.addnewBatchData(logCollectoin);
							}
					} catch (BOSException e) {
 						e.printStackTrace();
					} catch (EASBizException e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	public static void doCostSync(Context ctx){
		List  ls  = getCompanyAndPeriod(ctx);
		if(ls!=null &&  ls.size() > 0){
			try {
				ISaleIssueBill isf = SaleIssueBillFactory.getLocalInstance(ctx);
				String companyid="";
				String periodNum = "";
				String year="";
				String month = "";
				CoreBaseCollection logCollectoin = new CoreBaseCollection();
				ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
				for(int j =0 ; j <ls.size() ; j++){
					  Map mp = (Map) ls.get(j);
					  companyid = (String) mp.get("companyid");
					  periodNum = (String) mp.get("periodnum").toString(); 
					  year = periodNum.substring(0, 4);
					  month = periodNum.substring(4);
						List<String> billNumberList = getBillNumberByComIDAndPeriod(ctx,companyid,periodNum);
						if(billNumberList != null && billNumberList.size()>0){
							  delSaleIssueHisLog(ctx,companyid,year,month);  //批量删除销售出库记录单数据
							  delSalaIssueMid(ctx,"04",companyid,year,month); //批量删除中间表记录
							for(String issnumber : billNumberList){ 
								//delSalaIssueByNumber(ctx,issnumber,"04");// 删除中间表
								if(isf.exists("where number='"+issnumber+"'")){
									SaleIssueBillInfo info = isf.getSaleIssueBillInfo("where number='"+issnumber+"'");
									CoreBaseInfo logInfo = createHisLog(ctx,info,status.Cost);
									if(logInfo != null){
										logCollectoin.add(logInfo);
									}
								} 
						}
					}
				}
				if(logCollectoin.size()>0){
					ish.addnewBatchData(logCollectoin);
				}
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	 
	private static List<Map> getCompanyAndPeriodFromLog(Context ctx,String oper){
		String sql = "/*dialect*/SELECT distinct CFCompanyID ,CFYear,CFPeriod from CT_SRQ_SaleIssueHisLog"+
	    " where CFSyncStatus = 0 ";
		if("1".equals(oper)){
			sql = sql+" and CFStatus !=2 ";	
		}else{
			sql = sql+" and CFStatus =2 ";	
		}
		//sql = sql+ " and cfissid = 'CsOj8TrtRlOsA+JMtKEWn8w+kzs='";
 		 List<Map> list = new ArrayList<Map>();
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){		 
						Map mp = new HashMap();	  
						mp.put("companyid", rs.getObject("CFCOMPANYID"));
						mp.put("year", rs.getObject("CFYEAR"));
						mp.put("period", rs.getObject("CFPERIOD"));
						list.add(mp);
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		return list;
	}
	
	
	private static void doErrorLog(Context ctx,String database){
		String sql = "/*dialect*/select cfissnumber FNUMBER ,COUNT(cfissnumber) count FROM CT_SRQ_SaleIssueHisLog GROUP BY cfissnumber HAVING COUNT(cfissnumber) > 1 " ;
		 List<String> list = new ArrayList<String>();
		  IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){		 
						  if(rs.getObject("FNUMBER") !=null && !"".equals(rs.getObject("FNUMBER").toString())){
							  list.add((rs.getObject("FNUMBER").toString()));
						  }
					  }
				 } 
				
				if(list.size() > 0){
					ISaleIssueBill isf = SaleIssueBillFactory.getLocalInstance(ctx);
					CoreBaseCollection logCollectoin = new CoreBaseCollection();
					ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
					for(String issnumber : list){ 
						delSalaIssueByNumber(ctx,issnumber,database);// 删除中间表
						delSaleIssueHisLogStatus(ctx,issnumber);
						SaleIssueBillInfo info = isf.getSaleIssueBillInfo("where number='"+issnumber+"'");
						if(info!=null && info.getNumber()!=null && !"".equals(info.getNumber())){
							CoreBaseInfo logInfo = createHisLog(ctx,info,status.Cost);
							if(logInfo != null){
								 logCollectoin.add(logInfo);
							}
						} 
					}
					if(logCollectoin.size()>0){
						ish.addnewBatchData(logCollectoin);
					}
				}
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} catch (EASBizException e) {
 				e.printStackTrace();
			} 
	}
	
	/**
	 * 日志同步到中间表
	 * @param ctx
	 * @param database
	 */
	public static void doSyncIssueLogToMid(Context ctx,String database){
		List<Map> addList = getCompanyAndPeriodFromLog(ctx,"1");
		List<String> execuSql = new ArrayList<String>();
		if(addList!=null && addList.size() >0 ){
 			try{
 				//查询 有多条log单据数据---全部删除，然后新增一条
 				doErrorLog(ctx,database);
 				
  				StringBuffer sql = new StringBuffer();
				ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx) ; 
				CoreBaseCollection updateCollection = new CoreBaseCollection();
				for(Map mp :addList){
					String companyId = mp.get("companyid").toString();
					int year = Integer.parseInt( mp.get("year").toString());
					int period = Integer.parseInt( mp.get("period").toString());
					EntityViewInfo view = new EntityViewInfo();
					FilterInfo filterInfo = new FilterInfo();
					filterInfo.getFilterItems().add(new FilterItemInfo("SyncStatus",syncStatus.NO_SYNC,CompareType.EQUALS));
					filterInfo.getFilterItems().add(new FilterItemInfo("Status",status.unaudit,CompareType.NOTEQUALS)); 
					filterInfo.getFilterItems().add(new FilterItemInfo("CompanyID",companyId,CompareType.EQUALS)); 
					filterInfo.getFilterItems().add(new FilterItemInfo("Year",year,CompareType.EQUALS)); 
					filterInfo.getFilterItems().add(new FilterItemInfo("Period",period,CompareType.EQUALS)); 
					view.setFilter(filterInfo);
					SaleIssueHisLogCollection saleColl = ish.getSaleIssueHisLogCollection(view);
					sql.append("select FNUMBER,to_char(UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss') UPDATE_TIME from EAS_SALEISSUE_HIS where ").append("\r\n");
					sql.append("FOrgID='").append(companyId).append("' and FYEAR = ").append(year).append(" and FPERIOD =").append(period);
 					List<Map<String, Object>> list = EAISynTemplate.query(ctx, database,sql.toString());
 					//Map<String,String> logMp = new HashMap<String,String>();
 					List<String> saleLists = new ArrayList<String>();
					if(list!=null && list.size() > 0){
						for(Map<String, Object> mp1 :list){
							saleLists.add(mp1.get("FNUMBER").toString());
 						}
					}
 					Iterator<SaleIssueHisLogInfo> it = saleColl.iterator();
					while(it.hasNext()){
						SaleIssueHisLogInfo logInfo = it.next();
						 if(saleLists.contains(logInfo.getIssNumber())){
 							execuSql.addAll(crateSQL(ctx,logInfo,"u"));
						}else{
							execuSql.addAll(crateSQL(ctx,logInfo,"a"));
						}
						logInfo.setSyncStatus(syncStatus.SYNC);
						updateCollection.add(logInfo);
					}
					sql.setLength(0);
				}
				if(updateCollection!=null && updateCollection.size() > 0)
				 ish.updateBatchData(updateCollection);
				System.out.println(execuSql.toString());
				if(execuSql!=null && execuSql.size() > 0)
				doInsertSqls(ctx, database,execuSql);
			}catch (BOSException e) {
				System.out.println(execuSql);
				e.printStackTrace();
 			}catch (EASBizException e) {
 				System.out.println(execuSql);
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> crateSQL(Context ctx,SaleIssueHisLogInfo info ,String oper){
		 List<String> listSQL = new ArrayList<String>();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if(oper!=null && !"".equals(oper)){
			if("a".equals(oper)){
					StringBuffer sql = new StringBuffer();
				    sql.setLength(0);
				    sql.append("/*dialect*/insert into EAS_SALEISSUE_HIS(FID,FNumber,FOrgID,FYear,FPeriod,FHISID,EASSIGN,HISSIGN,update_time) values ");
				    sql.append("('").append(info.getIssID()).append("','").append(info.getIssNumber()).append("'");
				    sql.append(",'"+info.getCompanyID()+"'").append(","+info.getYear()).append(","+info.getPeriod()).append(",'"+info.getHISID()+"',");
				    sql.append("0,0,to_timestamp('" + sdf.format(info.getLastUpdateTime()) + "','yyyy-mm-dd hh24:mi:ss'))").append("\r\n");
				    listSQL.add(sql.toString());
				    SaleIssueHisLogEntryCollection coll = info.getEntrys();
				    Iterator<SaleIssueHisLogEntryInfo> it = coll.iterator();
				    while(it.hasNext()){
 						SaleIssueHisLogEntryInfo entry = it.next();
						Map mp = getMaterialInfoByID(ctx,entry.getIssEntryID());
						StringBuffer sql1 = new StringBuffer();
						sql1.append("/*dialect*/insert into EAS_SALEISSUE_SUB_HIS(FID,FParentID,FQty,FActualCost,FHISPayItem,FHISID,FMATERIALNUMBER,FPINPAI,FHUOHAO,FMODEL,FXINGHAO) values (");
						sql1.append("'").append(entry.getIssEntryID()).append("','").append(info.getIssID()).append("',");
						sql1.append(entry.getQty()).append(",").append(entry.getAmount()).append(",'");
						sql1.append(entry.getHISPayItem()).append("','").append(entry.getHISID()).append("',").append("\r\n");
						String pp ="";
						String hh ="";
						String gg ="";
						String xh ="";
						if(mp!=null && mp.size() > 0) {
							if(mp.get("PP")!=null && !"".equals(mp.get("PP").toString())){
								pp = mp.get("PP").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
							} 
							
							if(mp.get("HH")!=null && !"".equals(mp.get("HH").toString())){
								hh = mp.get("HH").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
							}
							
							if(mp.get("GG")!=null && !"".equals(mp.get("GG").toString())){
								gg = mp.get("GG").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
							}
							
//							if(mp.get("XH")!=null && !"".equals(mp.get("XH").toString())){
//								xh = mp.get("XH").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
//							}
							if(mp.get("FNAME")!=null && !"".equals(mp.get("FNAME").toString())){
								xh = mp.get("FNAME").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
							}
							sql1.append("'").append(mp.get("FNUMBER")).append("','").append(pp).append("','").append(hh).append("','");
							sql1.append(gg).append("','").append(xh).append("')");
						}else {
							sql1.append("'','','','','')");
						}
						listSQL.add(sql1.toString());
				    }
			}else if("u".equals(oper)){
				StringBuffer sql = new StringBuffer();
				sql.append("/*dialect*/update EAS_SALEISSUE_HIS set EASSIGN = 0 , update_time = to_timestamp('" + sdf.format(info.getLastUpdateTime()) + "','yyyy-mm-dd hh24:mi:ss') where FID='").append(info.getIssID()+"'");
				listSQL.add(sql.toString());
				SaleIssueHisLogEntryCollection coll = info.getEntrys();
				Iterator<SaleIssueHisLogEntryInfo> it = coll.iterator();
				while(it.hasNext()){
					StringBuffer sql1 = new StringBuffer();
					SaleIssueHisLogEntryInfo entry = it.next();
					sql1.append("update EAS_SALEISSUE_SUB_HIS set FActualCost=").append(entry.getAmount()).append(" , FQty =").append(entry.getQty()).append(" where FID='").append(entry.getIssEntryID()+"'");
					listSQL.add(sql1.toString());
				}
			}else if("d".equals(oper)){
				StringBuffer sql = new StringBuffer();
				sql.append(" delete from EAS_SALEISSUE_HIS where FID='").append(info.getIssID()+"'");
				listSQL.add(sql.toString());
				SaleIssueHisLogEntryCollection coll = info.getEntrys();
				Iterator<SaleIssueHisLogEntryInfo> it = coll.iterator();
				while(it.hasNext()){
					StringBuffer sql1 = new StringBuffer();
					SaleIssueHisLogEntryInfo entry = it.next();
					sql1.append(" delete from EAS_SALEISSUE_SUB_HIS where FID='").append(entry.getIssEntryID()+"'");
					listSQL.add(sql1.toString());
				}
			}else if("unaudit".equals(oper)){
				StringBuffer sql = new StringBuffer();
				sql.append(" update EAS_SALEISSUE_HIS set EASSIGN = 0 , update_time = to_timestamp('" + sdf.format(info.getLastUpdateTime()) + "','yyyy-mm-dd hh24:mi:ss') where FID='").append(info.getIssID()+"'");
				listSQL.add(sql.toString());
				SaleIssueHisLogEntryCollection coll = info.getEntrys();
				Iterator<SaleIssueHisLogEntryInfo> it = coll.iterator();
				while(it.hasNext()){
					StringBuffer sql1 = new StringBuffer();
					SaleIssueHisLogEntryInfo entry = it.next();
					sql1.append(" update EAS_SALEISSUE_SUB_HIS set FActualCost=0,FQty=0 where FID='").append(entry.getIssEntryID()+"'");
					listSQL.add(sql1.toString());
				}
			}
		}
		return listSQL;
	}
	
	
	private static List<String> getBillNumberByComIDAndPeriod(Context ctx,String companyid , String period){
		List<String>  list = new ArrayList<String> ();
		if(companyid!=null && !"".equals(companyid)){
		String sql ="/*dialect*/select DISTINCT a.FNUMBER  as FBILLNUMBER  from T_IM_SaleIssueBill a "+
		" INNER JOIN T_IM_SALEISSUEENTRY b on a.FID =b.FPARENTID "+
		" where a.FMONTH ='"+period+"' and a.FSTORAGEORGUNITID ='"+companyid+"' and "+
		" a.fbasestatus >=4 and b.CFHISMINGXIID is not null and b.CFHISSFXMID is not null and "+
		" a.cfhisdanjubianma is not null ";

		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FBILLNUMBER")!=null && !"".equals(rs.getObject("FBILLNUMBER").toString()))
							  list.add(rs.getObject("FBILLNUMBER").toString());
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	
	private static List<String> getNoExistsNumberByComIDAndPeriod(Context ctx,String companyid , String period){
		List<String>  list = new ArrayList<String> ();
		if(companyid!=null && !"".equals(companyid)){
		String year = period.substring(0, 4);
		String month = period.substring(4);
		String sql ="/*dialect*/select DISTINCT cfissnumber as FBILLNUMBER from CT_SRQ_SaleIssueHisLog  "+
		" where CFYEAR ='"+year+"' and CFPERIOD ='"+month+"' and cfcompanyid ='"+companyid+"' and cfissnumber not in ("+
		" select DISTINCT a.FNUMBER as FBILLNUMBER  from T_IM_SaleIssueBill a INNER JOIN T_IM_SALEISSUEENTRY b on a.FID =b.FPARENTID "+
		" where a.FMONTH ='"+period+"' and a.fstorageorgunitid = '"+companyid+"' and a.fbasestatus >=4 and "+
		" b.CFHISMINGXIID is not null and b.CFHISSFXMID is not null and a.cfhisdanjubianma is not null )";
		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FBILLNUMBER")!=null && !"".equals(rs.getObject("FBILLNUMBER").toString()))
							  list.add(rs.getObject("FBILLNUMBER").toString());
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	


	/**
	 * 生成成本明细记录
	 * @param ctx
	 *//*
	public static void doSyncCostDetail(Context ctx){
		List  ls  = getCompanyAndPeriod(ctx);
		if(ls!=null &&  ls.size() > 0){
			//SaleIssueBillInfo info = null;
			SaleIssueBillCollection coll = new SaleIssueBillCollection();
			try {
				ISaleIssueBill isf = SaleIssueBillFactory.getLocalInstance(ctx);
				ICostComputeHeaderLog icchl = CostComputeHeaderLogFactory.getLocalInstance(ctx);
				CoreBaseCollection  collection = new CoreBaseCollection();
				String companyid="";
				String periodid="";
				String handerID="";
				CoreBaseCollection logCollectoin = new CoreBaseCollection();
				ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
				for(int j =0 ; j <ls.size() ; j++){
					  Map mp = (Map) ls.get(j);
					  companyid = (String) mp.get("companyid");
					  periodid = (String) mp.get("periodid");
					  handerID = getRportHeaderID(ctx,companyid,periodid);
					if(handerID!=null && handerID.length()>0){
						List<String> billNumberList = getBillNumberByHanderID(ctx,handerID);
						if(billNumberList!=null && billNumberList.size()>0){
							for(String issnumber : billNumberList){
								if(isf.exists("where number='"+issnumber+"'")){
									SaleIssueBillInfo info = isf.getSaleIssueBillInfo("where number='"+issnumber+"'");
									if(info!=null && info.getNumber()!=null && !"".equals(info.getNumber())){
										//if(createHisLog(ctx,info,status.Cost)!=null)
										CoreBaseCollection costColl = createCostDetail(ctx,info,status.Cost);
										if(costColl!=null && costColl.size() >0){ 
											logCollectoin.addCollection(costColl);
										}
									}
								}
								
							}
						} 
					}
				}
				if(logCollectoin.size()>0){
					ish.addnewBatchData(logCollectoin);
				}
			} catch (BOSException e) {
 				e.printStackTrace();
			} catch (EASBizException e) {
				e.printStackTrace();
			}
		}
	}*/
		
	
	
	
	public static Map<String,String> getMaterialInfoByID(Context ctx,String billId){
		  Map<String,String> mp =null ;
		  String sql="select b.FNUMBER,b.FName_l2 as FNAME,b.CFPINPAI as PP,b.CFHUOHAO as HH,b.FMODEL as GG,b.CFXINGHAO as XH " +
		    "from T_IM_SaleIssueEntry a inner join T_BD_MATERIAL b on a.FMATERIALID = b.FID " +
		    "where a.FID = ?"; 
		  try {
		   IRowSet rs=DbUtil.executeQuery(ctx, sql,new Object[]{billId});
		   if(rs!=null && rs.size() > 0){
		    mp = new HashMap<String,String>();
		      if(rs.next()){       
		       if(rs.getObject("FNUMBER")!=null &&!"".equals(rs.getObject("FNUMBER").toString())){
		        mp.put("FNUMBER", rs.getObject("FNUMBER").toString());
		       }else{
		        mp.put("FNUMBER", "");
		       }
		       
		       if(rs.getObject("PP")!=null &&!"".equals(rs.getObject("PP").toString())){
		        mp.put("PP", rs.getObject("PP").toString());
		       }else{
		        mp.put("PP", "");
		       }
		       
		       if(rs.getObject("HH")!=null &&!"".equals(rs.getObject("HH").toString())){
		        mp.put("HH", rs.getObject("HH").toString());
		       }else{
		        mp.put("HH", "");
		       }
		       
		       if(rs.getObject("GG")!=null &&!"".equals(rs.getObject("GG").toString())){
		        mp.put("GG", rs.getObject("GG").toString());
		       }else{
		        mp.put("GG", "");
		       }
		       
		       if(rs.getObject("XH")!=null &&!"".equals(rs.getObject("XH").toString())){
		        mp.put("XH", rs.getObject("XH").toString());
		       }else{
		        mp.put("XH", "");
		       }
		       if(rs.getObject("FNAME")!=null &&!"".equals(rs.getObject("FNAME").toString())){
			        mp.put("FNAME", rs.getObject("FNAME").toString());
			       }else{
			        mp.put("FNAME", "");
			       }
		      }
		   }
		  } catch (BOSException e) {
		    e.printStackTrace();
		  } catch (SQLException e) {
		   e.printStackTrace();
		  }
		  return mp;
		 }
	
	private static void delSalaIssueByNumber(Context ctx,String number,String dbid){
		
		  List<String> delList = new ArrayList<String>();
		  String str1 = "delete EAS_SALEISSUE_SUB_HIS where FPARENTID in (select fid from EAS_SALEISSUE_HIS where FNUMBER='"+number+"') ";
		  delList.add(str1);
		  String str2 = "delete EAS_SALEISSUE_HIS where fnumber ='"+number+"' ";
		  delList.add(str2);
		  try {
		   EAISynTemplate.executeBatch(ctx,dbid,delList);
		  } catch (BOSException e) {
		    e.printStackTrace();
		  }
	} 


	 
	/**
	 * 获取 根据报告头 存货结转明细报告报明细
	 * @param ctx
	 * @param handerID
	 * @return
	 */
	private static List<String> getLISHIBillNumberByHanderID(Context ctx,String handerID){
		List<String>  list = new ArrayList<String> ();
		if(handerID!=null && !"".equals(handerID)){
		String sql ="/*dialect*/select distinct FBILLNUMBER  from T_CL_COSTCOMPUTEPRICEREPORT" +
	  //  " where FPARENTID ='"+handerID+"' and FCalculateKind =1 and FBILLNAME ='销售出库单 - 普通销售出库' and FBILLNUMBER  not LIKE '*VMI%' ";
	    " where FPARENTID ='"+handerID+"' and FCalculateKind =1 and FBILLNAME ='销售出库单 - 普通销售出库'   ";

		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FBILLNUMBER")!=null && !"".equals(rs.getObject("FBILLNUMBER").toString()))
							  list.add(rs.getObject("FBILLNUMBER").toString());
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	 
	private static List<String> getLISHIBillNumberByComIDAndPeriod(Context ctx,String companyid , String period){
		List<String>  list = new ArrayList<String> ();
		if(companyid!=null && !"".equals(companyid)){
		String sql ="/*dialect*/select DISTINCT a.FNUMBER  as FBILLNUMBER  from T_IM_SaleIssueBill a "+
		" INNER JOIN T_IM_SALEISSUEENTRY b on a.FID =b.FPARENTID "+
		" where a.FMONTH ='"+period+"' and a.FSTORAGEORGUNITID ='"+companyid+"'  and "+
		" b.FACTUALCOST > 0 and b.CFHISMINGXIID is not null and b.CFHISSFXMID is not null and "+
		" a.cfhisdanjubianma is not null  and a.CFHISREQID is not null    ";

		IRowSet rs = null;
			try {
				rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
				if(rs!=null && rs.size() > 0){
					  while(rs.next()){	
						  if(rs.getObject("FBILLNUMBER")!=null && !"".equals(rs.getObject("FBILLNUMBER").toString()))
							  list.add(rs.getObject("FBILLNUMBER").toString());
					  }
				 } 
			} catch (BOSException e) {
				e.printStackTrace();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	
	

	private static void doInsertSqls(Context ctx, String dataBase,List<String> sqls){
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
}