package com.kingdee.eas.basedata.scm.im.inv.app;

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

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.HisRelatoinEASFactory;
import com.kingdee.eas.custom.IHisRelatoinEAS;
import com.kingdee.eas.custom.app.util.GQCUtil;
import com.kingdee.eas.custom.app.util.TempMateHelper;
import com.kingdee.eas.mw.srqr.ISaleIssueHisLog;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogEntryCollection;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogEntryInfo;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogFactory;
import com.kingdee.eas.mw.srqr.SaleIssueHisLogInfo;
import com.kingdee.eas.mw.srqr.app.status;
import com.kingdee.eas.mw.srqr.app.util.SaleIssueSyncTool;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.scm.im.inv.app.SaleIssueBillControllerBean;
import com.kingdee.jdbc.rowset.IRowSet;

public class SaleIssueBillControllerBeanEx extends SaleIssueBillControllerBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8982150855269493619L;
	
	
	private void snedsendMessageToHIS(Context ctx,IObjectPK pk,String oper){
		try {
			SaleIssueBillInfo info = SaleIssueBillFactory.getLocalInstance(ctx).getSaleIssueBillInfo(pk);
			if("unaudit".equals(oper)){
				System.out.println("######## snedsendMessageToHIS unaudit 1 ########");
				Map<String,String> mp = new HashMap<String,String>();
				mp.put("fid",pk.toString());
				TempMateHelper.sendMessageToHISPost(JSONObject.toJSONString(mp),2);
			}else if("audit".equals(oper)){
				IObjectPK logPk = SaleIssueSyncTool.saveHisLogWithReturn(ctx,info,status.audit);
				ISaleIssueHisLog ish = SaleIssueHisLogFactory.getLocalInstance(ctx);
				if(logPk !=null && ish.exists(logPk)){
					System.out.println("######## snedsendMessageToHIS audit 1 ########");
					SaleIssueHisLogInfo logInfo = ish.getSaleIssueHisLogInfo(logPk);
					Map<String,Object> mp = new HashMap<String,Object>();
					mp.put("fid", pk.toString());
					mp.put("number", info.getNumber());
					mp.put("orgId", logInfo.getCompanyID());
					mp.put("year", logInfo.getYear());
					mp.put("hisId", logInfo.getHISID());
					mp.put("easSign", 0);
					mp.put("hisSign", 0);
					mp.put("period", logInfo.getPeriod());
					Calendar cal=Calendar.getInstance();
				    cal.setTime(new Date());
					Timestamp tt =  new Timestamp(cal.getTimeInMillis());
				    mp.put("easUpdateTime", tt);
				    SaleIssueHisLogEntryCollection items = logInfo.getEntrys();
				    Iterator it = items.iterator();
				    List<Map<String,Object>> eMps = new ArrayList<Map<String,Object>>();
				    
				    while(it.hasNext()){
				    	Map<String,Object> emp = new HashMap<String,Object>();
				    	SaleIssueHisLogEntryInfo logEInfo = (SaleIssueHisLogEntryInfo) it.next();
				    	
//				    	String eId = logEInfo.getId().toString();
				    	Map materialMp = SaleIssueSyncTool.getMaterialInfoByID(ctx,logEInfo.getIssEntryID());
				    	String pp ="";
						String hh ="";
						String gg ="";
						String xh ="";
						
				    	emp.put("fid",logEInfo.getIssEntryID());
				    	emp.put("parentId",pk.toString());
				    	emp.put("qty",logEInfo.getQty());
				    	emp.put("actualCost",logEInfo.getAmount());
				    	emp.put("hisPayItem",logEInfo.getHISPayItem());
				    	emp.put("hisId",logEInfo.getHISID());
				    	emp.put("hisId",logEInfo.getHISID());
				    	emp.put("easUpdateTime",tt);
				 
				       	if(materialMp.get("PP")!=null && !"".equals(materialMp.get("PP").toString())){
							pp = materialMp.get("PP").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
						} 
						
						if(materialMp.get("HH")!=null && !"".equals(materialMp.get("HH").toString())){
							hh = materialMp.get("HH").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
						}
						
						if(materialMp.get("GG")!=null && !"".equals(materialMp.get("GG").toString())){
							gg = materialMp.get("GG").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
						}
						
//						if(materialMp.get("XH")!=null && !"".equals(materialMp.get("XH").toString())){
//							xh = materialMp.get("XH").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
//						}
						if(materialMp.get("FNAME")!=null && !"".equals(materialMp.get("FNAME").toString())){
							xh = materialMp.get("FNAME").toString().replaceAll(";", "").replaceAll("/", "").replaceAll("'", "");
						}
				    	emp.put("fPinPai",pp);
				    	emp.put("fHuoHao",hh);
				    	emp.put("fMaterialNumber",materialMp.get("FNUMBER"));
				    	emp.put("fModel",gg);
				    	emp.put("fXingHao",xh);
				    	eMps.add(emp);
				    }
				    mp.put("subList", eMps);
				  System.out.println("########  body ########"+JSONObject.toJSONString(mp));
				  String result =  TempMateHelper.sendMessageToHISPost(JSONObject.toJSONString(mp),1);
				  System.out.println("########  result ########"+result);
				}
			}
			
		} catch (EASBizException e) {
			e.printStackTrace();
		} catch (BOSException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	//提交
	@Override
	protected IObjectPK _submit(Context arg0, IObjectValue arg1)
			throws BOSException, EASBizException {
 		System.out.println("########SaleIssueBillControllerBeanEx _submit 111 ########");
 		checkBillFields(arg0,(SaleIssueBillInfo)arg1);
 		IObjectPK pk = super._submit(arg0, arg1);
 		snedsendMessageToHIS(arg0,pk,"audit");
 		return pk;
	}
	
	//反审核
	@Override
	protected void _unAudit(Context ctx, IObjectPK pk) throws BOSException,
			EASBizException {
 		System.out.println("########_unAudit########");
 		String billId = pk.toString();
 		Map<String,String>  mp = getSaleIssueBillExInfo(ctx,billId);
 		 if(mp != null && mp.size() > 0){ 
 			 if(mp.get("cfiscollpur") !=null && !"".equals(mp.get("cfiscollpur").toString()) && "1".equals(mp.get("cfiscollpur").toString())){
 				 String purInID = getPurInWarehsBillId(ctx,mp.get("hisnumber").toString());
 				 GQCUtil.delPurInBotRelation(ctx,purInID,"SaleIssueBillInfo");
 			 }
 		 }
 		super._unAudit(ctx, pk);
 		SaleIssueBillInfo info = SaleIssueBillFactory.getLocalInstance(ctx).getSaleIssueBillInfo(pk);
 		SaleIssueSyncTool.saveHisLog(ctx,info,status.unaudit);
 		
 		snedsendMessageToHIS(ctx,pk,"unaudit");
 		
	}
	
	
	private Map<String,String> getSaleIssueBillExInfo(Context ctx,String id){
		Map<String,String> map = new HashMap<String,String>();
 		String str =  "select cfiscollpur,cfhisdanjubianma from T_IM_SaleIssueBill where fid = '"+id+"'";
    	try {
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,str);
			 if(rs!=null && rs.size() > 0){ 
				 while(rs.next()){
					if( rs.getObject("cfiscollpur")!=null && !"".equals(rs.getObject("cfiscollpur").toString())){
						map.put("cfiscollpur",rs.getObject("cfiscollpur").toString()) ;
					}else{
						map.put("cfiscollpur","") ;
					}
					
					if( rs.getObject("cfhisdanjubianma")!=null && !"".equals(rs.getObject("cfhisdanjubianma").toString())){
						map.put("hisnumber",rs.getObject("cfhisdanjubianma").toString()) ;
					}else{
						map.put("hisnumber","") ;
					}
					
			     }
			 } 
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		}  
		return map;
	}
	
	private String getPurInWarehsBillId(Context ctx,String hisnumber){
		String billId ="";
		String str =  "/*dialect*/ select Max(FID) FID from T_IM_SaleIssueBill where cfhisdanjubianma = '"+hisnumber+"'";
    	try {
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,str);
			 if(rs!=null && rs.size() > 0){ 
				 while(rs.next()){
					if( rs.getObject("FID")!=null && !"".equals(rs.getObject("FID").toString())){
						billId = rs.getObject("FID").toString() ;
					}
			     }
			 } 
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		} 
		
		return billId;
			
	}
	
	
	@Override
	protected IObjectPK _save(Context arg0, IObjectValue arg1)
			throws BOSException, EASBizException {
 		return super._save(arg0, arg1);
	}
	
	 
	protected void checkBillFields(Context ctx, SaleIssueBillInfo info) throws BOSException, EASBizException {
		if(info.getBaseStatus() == null)
			info.setBaseStatus(BillBaseStatusEnum.ADD);
//		if(info.getNumber()==null || StringUtils.isEmpty(info.getNumber().trim()))
//			throw new EASBizException(EASBizException.CHECKNUMBLANK);
		String sid = info.getStorageOrgUnit().getId().toString();
		IHisRelatoinEAS ihef = HisRelatoinEASFactory.getLocalInstance(ctx);
		boolean nullerror = false ;//是否手动新增
		boolean gzerror = false ;
		boolean qtyerror = false ;
		if(ihef.exists("where companyNo.id ='"+sid+"'")){//判断HIS关联EAS门诊基础资料
			IObjectCollection ics = info.getEntries();
			Iterator<SaleIssueEntryInfo> it = ics.iterator();
			BigDecimal qty = BigDecimal.ZERO;
			String mId = "";
			while(it.hasNext()){
				SaleIssueEntryInfo entryInfo = it.next();
				if(entryInfo.getMaterial()!=null && entryInfo.getMaterial().getId()!=null){
					String groupId = getGroupIDByMaterialID(ctx, entryInfo.getMaterial().getId().toString());
					//如果物料的分类是高值耗材
					if("Dtmk86FlSCmVG2q3RQFBp8efwEI=".equals(groupId)){
						if(entryInfo.getSourceBillEntryId()==null || entryInfo.getSourceBillType()==null ||
							!("510b6503-0105-1000-e000-0113c0a812fd463ED552".equals(entryInfo.getSourceBillType().getId().toString())||
							 "50957179-0105-1000-e000-015bc0a812fd463ED552".equals(entryInfo.getSourceBillType().getId().toString()))
						){
							gzerror = true;
							break ;
						}
//						nullerror = !hisExists(ctx,entryInfo.getId().toString());
//						if(nullerror){
//							break;
//						}
					
						if(entryInfo.getQty() !=null)
							qty = entryInfo.getQty();
					 //除收费项一级分类为正畸外(主业务分类)，其他收费订单出库数量只允许出整数
						mId = getYeWuZhuShuJuByeId(ctx,entryInfo.getId().toString());
						if(mId != null && !"TyLE2JxGSCeVHnZwhM0qjIV8ZBE=".equals(mId)){
							if(qty.stripTrailingZeros().scale() > 0)
								qtyerror = true ;
							break;
						}
					}
				}
			}
		}
		if(gzerror)
			throw new BOSException("HIS与EAS门诊已经关联，不允许手工新增高值物料的销售出库单，请与对应销售订单数据关联。");
		
		if(qtyerror)
			throw new BOSException("销售出库单数量只允许整数。");
//		if(nullerror)
//			throw new BOSException("his收费项目明细不能为空");
	}
	 
	 private static String getGroupIDByMaterialID(Context ctx,String materialId){
		  String groupId ="";
		  if(materialId!=null && !"".equals(materialId)){
			  StringBuffer sbr = new StringBuffer("select fmaterialgroupid from T_BD_Material where fid = '").append(materialId).append("'");
			  IRowSet rs = null;
				try {
					rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sbr.toString());
					if(rs!=null && rs.size() > 0){
						  if(rs.next()){			  
							  if(rs.getObject("fmaterialgroupid")!=null && !"".equals(rs.getObject("fmaterialgroupid"))){
								  groupId = rs.getObject("fmaterialgroupid").toString();
							  }
						  }
					 } 
				} catch (BOSException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}   
		  } 
		return groupId; 
	  }
	 
	 private static String getYeWuZhuShuJuByeId(Context ctx,String eId){
		  String mId ="";
		  if(eId!=null && !"".equals(eId)){
			  StringBuffer sbr = new StringBuffer("select CFYEWUZHUSHUJUID from T_IM_saleissueentry where fid = '").append(eId).append("'");
			  IRowSet rs = null;
				try {
					rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sbr.toString());
					if(rs!=null && rs.size() > 0){
						  if(rs.next()){			  
							  if(rs.getObject("CFYEWUZHUSHUJUID")!=null && !"".equals(rs.getObject("CFYEWUZHUSHUJUID"))){
								  mId = rs.getObject("CFYEWUZHUSHUJUID").toString();
							  }
						  }
					 } 
				} catch (BOSException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}   
		  } 
		return mId; 
	  }
	 
	 
	 private static boolean hisExists(Context ctx,String entryId){
		  boolean flag = false ;
		  if(entryId!=null && !"".equals(entryId)){
			  StringBuffer sbr = new StringBuffer("  select cfhissfxmid,cfhismingxiid from t_Im_Saleissueentry where fid = '").append(entryId).append("'");
			  IRowSet rs = null;
				try {
					rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sbr.toString());
					if(rs!=null && rs.size() > 0){
						  if(rs.next()){			  
							  if(rs.getObject("cfhissfxmid")!=null && !"".equals(rs.getObject("cfhissfxmid"))){
							//	rs.getObject("cfhismingxiid")!=null && !"".equals(rs.getObject("cfhismingxiid"))){
								  flag = true;
							  }
						  }
					 } 
				} catch (BOSException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}   
		  } 
		return flag; 
	  }

	 
	 /**
	  *  eas 数据库删除以后判断 如果是his传递过来的需要将删除的数据插入中间表中
	  */
	@Override
	protected void _delete(Context ctx, IObjectPK pk) throws BOSException,EASBizException {
		boolean flag = false;
		SaleIssueBillInfo info = SaleIssueBillFactory.getLocalInstance(ctx).getSaleIssueBillInfo(pk);
		String id = pk.toString(); 
		if(info.get("hisdanjubianma")!=null&&!"".equals(info.get("hisdanjubianma").toString())&&info.getBizType()!=null && info.getBizType().getId()!=null&&
	 			("d8e80652-010e-1000-e000-04c5c0a812202407435C".equals(info.getBizType().getId().toString())||
	 			 "d8e80652-0110-1000-e000-04c5c0a812202407435C".equals(info.getBizType().getId().toString()))
	 	){
			   SaleIssueEntryCollection entryCollection = info.getEntry();
		       Iterator<SaleIssueEntryInfo> it = entryCollection.iterator(); 
			 while (it.hasNext()) { 
				 SaleIssueEntryInfo entry = it.next();
				 if(entry.get("hissfxmid")!=null && !"".equals(entry.get("hissfxmid").toString())
				    		&&entry.get("hismingxiID")!=null && !"".equals(entry.get("hismingxiID").toString())&&
				    		("8r0AAAAEaOnC73rf".equals(entry.getInvUpdateType().getId().toString())||
				    		 "8r0AAAAEaPbC73rf".equals(entry.getInvUpdateType().getId().toString())||
				    		 "CeUAAAAIdBvC73rf".equals(entry.getInvUpdateType().getId().toString()))	
				    		){
					 flag = true;
					 break;
				 }
			 }
			
		}
		
		super._delete(ctx, pk);
		if(!_exists(ctx, pk)&& flag){
			//调用HIS即时通知结果
			Map<String,String> mp = new HashMap<String,String>();
			mp.put("fid",id);
			TempMateHelper.sendMessageToHISPost(JSONObject.toJSONString(mp),2);
			
//			// 删除的销售出库单 记录到   中间表
//			ISaleIssueHisLog ilogBiz = SaleIssueHisLogFactory.getLocalInstance(ctx);
//			EntityViewInfo viewInfo = new EntityViewInfo();
//			FilterInfo filter = new FilterInfo();
//			filter.getFilterItems().add(new FilterItemInfo("IssID",id,CompareType.EQUALS));
//			viewInfo.setFilter(filter);
//			SaleIssueHisLogCollection coll = ilogBiz.getSaleIssueHisLogCollection(viewInfo);
//			if(coll !=null && coll.size() >0){
//				List<String> insertSQL = crateInsertSQL(ctx,coll.get(0));
//				if(insertSQL !=null && insertSQL.size() > 0){
//					EAISynTemplate.executeBatch(ctx,"04", insertSQL);
//				}
//			}
			
		}
		
	}
	 
	
	private static List<String> crateInsertSQL(Context ctx,SaleIssueHisLogInfo info){
		 List<String> listSQL = new ArrayList<String>();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 StringBuffer sql = new StringBuffer();
				    sql.setLength(0);
				    sql.append("/*dialect*/insert into EAS_SALEISSUE_HIS_DEL(FID,FNumber,FOrgID,FYear,FPeriod,FHISID,EASSIGN,HISSIGN,update_time) values ");
				    sql.append("('").append(info.getIssID()).append("','").append(info.getIssNumber()).append("'");
				    sql.append(",'"+info.getCompanyID()+"'").append(","+info.getYear()).append(","+info.getPeriod()).append(",'"+info.getHISID()+"',");
				    sql.append("0,0,to_timestamp('" + sdf.format(info.getLastUpdateTime()) + "','yyyy-mm-dd hh24:mi:ss'))").append("\r\n");
				    listSQL.add(sql.toString());
				    SaleIssueHisLogEntryCollection coll = info.getEntrys();
				    Iterator<SaleIssueHisLogEntryInfo> it = coll.iterator();
				    while(it.hasNext()){
						SaleIssueHisLogEntryInfo entry = it.next();
						StringBuffer sql1 = new StringBuffer();
						sql1.append("/*dialect*/insert into EAS_SALEISSUE_SUB_HIS_DEL(FID,FParentID,FQty,FActualCost,FHISPayItem,FHISID,FMATERIALNUMBER,FPINPAI,FHUOHAO,FMODEL,FXINGHAO) values (");
						sql1.append("'").append(entry.getIssEntryID()).append("','").append(info.getIssID()).append("',");
						sql1.append(entry.getQty()).append(",").append(entry.getAmount()).append(",'");
						sql1.append(entry.getHISPayItem()).append("','").append(entry.getHISID()).append("','','','','','')");					 
						 listSQL.add(sql1.toString()); 
				    }
		 return listSQL;
	}
	
	 
	
	  
}
