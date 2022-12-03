package com.kingdee.eas.custom.app;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.IPushRecord;
import com.kingdee.eas.custom.PushRecordCollection;
import com.kingdee.eas.custom.PushRecordFactory;
import com.kingdee.eas.custom.PushRecordInfo;
import com.kingdee.eas.custom.app.dao.PaymentSupport;
import com.kingdee.eas.custom.app.dao.VMISaleOrderSupport;
import com.kingdee.eas.custom.app.dto.VMISaleOrderDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.sd.sale.ISaleOrder;
import com.kingdee.eas.scm.sd.sale.SaleOrderFactory;
import com.kingdee.eas.scm.sd.sale.SaleOrderInfo;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.util.UuidException;

public class PushRecordFacadeControllerBean extends AbstractPushRecordFacadeControllerBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2038896682094063859L;
	
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.PushRecordFacadeControllerBean");
	
	@Override
	protected void _generPurInBIll(Context ctx) throws BOSException {
		IPushRecord ibiz = PushRecordFactory.getLocalInstance(ctx);
	 	EntityViewInfo view = new EntityViewInfo();
	 	FilterInfo filter = new FilterInfo();
	 	filter.getFilterItems().add(new FilterItemInfo("processType", DateBaseProcessType.GSaleIss_MZ,CompareType.EQUALS)); //
	 	filter.getFilterItems().add(new FilterItemInfo("PushStatus", PushStatusEnum.unDo,CompareType.EQUALS)); // 
	 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType", DateBasetype.VMI_U_MZ_SS_VMI,CompareType.EQUALS)); // 
 	    filter.setMaskString("#0 and #1 and #2 ");
	 	view.setFilter(filter);
	 	
	 	try {
			PushRecordCollection coll= ibiz.getPushRecordCollection(view);
			if(coll !=null && coll.size() >0 ){
				Iterator it = coll.iterator();
				CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
				ISaleIssueBill iSaleIssue = SaleIssueBillFactory.getLocalInstance(ctx);
//				IPurInWarehsBill iPurInWarehs = PurInWarehsBillFactory.getLocalInstance(ctx);
				while(it.hasNext()){
					PushRecordInfo pushInfo = (PushRecordInfo) it.next();
					if(pushInfo.getName()!=null && !"".equals(pushInfo.getName())){
						IObjectPK paramIObjectPK = new ObjectUuidPK(BOSUuid.read(pushInfo.getName())); 
						SaleIssueBillInfo  issInfo = iSaleIssue.getSaleIssueBillInfo(paramIObjectPK);
						sourceColl.add(issInfo);
					}	
					
					List<IObjectPK> pkIns = AppUnit.botpSave(ctx, "783061E3", sourceColl, "SJeUcqjdT5mI93sc1GeqPgRRIsQ=");
				 
					
					List<IObjectPK> pkOuts = AppUnit.botpSave(ctx, "CC3E933B", sourceColl, "7OYnlhozTdejkMQ2rtLJvQRRIsQ=");
					
				}
			}
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (UuidException e) {
 			e.printStackTrace();
		}
	}
 
	//门诊领用 - 门诊高值领用 CGZ_U_MZ_SO 下推 生成 销售出库单
	private void doGengerSalaIssueBillCGZ(Context ctx){
	 	try {
			IPushRecord ibiz = PushRecordFactory.getLocalInstance(ctx);
		 	EntityViewInfo view = new EntityViewInfo();
		 	FilterInfo filter = new FilterInfo();
		 	filter.getFilterItems().add(new FilterItemInfo("processType", DateBaseProcessType.GSaleIss,CompareType.EQUALS)); //
		 	filter.getFilterItems().add(new FilterItemInfo("PushStatus", PushStatusEnum.unDo,CompareType.EQUALS)); // 
		 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType", DateBasetype.CGZ_U_MZ_SO,CompareType.EQUALS)); // 
		    filter.setMaskString("#0 and #1 and #2");
		 	view.setFilter(filter);
			PushRecordCollection coll= ibiz.getPushRecordCollection(view);
			if(coll !=null && coll.size() >0 ){
				Iterator it = coll.iterator();
				CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
				ISaleOrder iSaleOrder = SaleOrderFactory.getLocalInstance(ctx);
				ISaleIssueBill iSaleIssue = SaleIssueBillFactory.getLocalInstance(ctx);
				//IPurInWarehsBill iPurInWarehs = PurInWarehsBillFactory.getLocalInstance(ctx);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				CoreBaseCollection updateColl = new CoreBaseCollection();
// 				ExecutorService pool = Executors.newFixedThreadPool(6);
// 			    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
// 			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				while(it.hasNext()){
					PushRecordInfo pushInfo = (PushRecordInfo) it.next();
					if(pushInfo.getName()!=null && !"".equals(pushInfo.getName())){
						IObjectPK paramIObjectPK = new ObjectUuidPK(BOSUuid.read(pushInfo.getName())); 
						if(iSaleOrder.exists(paramIObjectPK)){
							SaleOrderInfo saleOrderInfo = iSaleOrder.getSaleOrderInfo(paramIObjectPK);
							String botpId = PurPlatUtil.getMappIdByFName(ctx,"INM-004"); // 销售订单下推销售出库
						 	if(botpId!=null && !"".equals(botpId) && saleOrderInfo.getBaseStatus() == BillBaseStatusEnum.AUDITED){
						 		sourceColl.add(saleOrderInfo);
  						 		List<IObjectPK> pks = AppUnit.botpSave(ctx, "CC3E933B", sourceColl, botpId);
						 		sourceColl.clear();
						 		if(pks !=null && pks.size() >0){
						 			IObjectPK issPK = pks.get(0);
									SaleIssueBillInfo saleIssInfo = iSaleIssue.getSaleIssueBillInfo(issPK); 
									if(issPK != null && !"".equals(issPK.toString())){
										//iSaleIssue.submit(saleIssInfo);  //销售出库单 提交
										PushRecordInfo rInfo = new PushRecordInfo();
										rInfo.setNumber(saleIssInfo.getNumber());
										rInfo.setName(issPK.toString());
										//rInfo.setDescription(m.getFnumber());
 										rInfo.setDateBaseType(pushInfo.getDateBaseType());
										rInfo.setProcessType(DateBaseProcessType.GSaleIss_MZ);
										rInfo.setPushStatus(PushStatusEnum.unDo);
										rInfo.setCU(saleIssInfo.getCU());
										PushRecordFactory.getLocalInstance(ctx).addnew(rInfo); 
									}
						 			pushInfo.setPushStatus(PushStatusEnum.doSuccess);
						 			pushInfo.setDescription(saleOrderInfo.getNumber()+"--"+saleIssInfo.getNumber());
								}else
									 pushInfo.setPushStatus(PushStatusEnum.doFail);
							}else
								 pushInfo.setPushStatus(PushStatusEnum.doFail);
						}else
							 pushInfo.setPushStatus(PushStatusEnum.doFail);
					}else
						 pushInfo.setPushStatus(PushStatusEnum.doFail);
					updateColl.add(pushInfo);
				}
				
				if(updateColl !=null && updateColl.size() >0)
					ibiz.update(updateColl);
			}
		} catch (EASBizException e) {
				e.printStackTrace();
		} catch (UuidException e) {
				e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		} 
	
	}
	
	//VMI物料领用 - VMI门诊领用 VMI_U_MZ_SO 下推 生成 销售出库单
	private void doGengerSalaIssueBillVMI(Context ctx){
	 	try {
			IPushRecord ibiz = PushRecordFactory.getLocalInstance(ctx);
		 	EntityViewInfo view = new EntityViewInfo();
		 	FilterInfo filter = new FilterInfo();
		 	filter.getFilterItems().add(new FilterItemInfo("processType", DateBaseProcessType.GSaleIss,CompareType.EQUALS)); //
		 	filter.getFilterItems().add(new FilterItemInfo("PushStatus", PushStatusEnum.unDo,CompareType.EQUALS)); // 
		 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType", DateBasetype.VMI_U_MZ_SO,CompareType.EQUALS)); // 
		    filter.setMaskString("#0 and #1 and #2");
		 	view.setFilter(filter);
			PushRecordCollection coll= ibiz.getPushRecordCollection(view);
			if(coll !=null && coll.size() >0 ){
				Iterator it = coll.iterator();
				CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
				ISaleOrder iSaleOrder = SaleOrderFactory.getLocalInstance(ctx);
				ISaleIssueBill iSaleIssue = SaleIssueBillFactory.getLocalInstance(ctx);
				//IPurInWarehsBill iPurInWarehs = PurInWarehsBillFactory.getLocalInstance(ctx);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				CoreBaseCollection updateColl = new CoreBaseCollection();
				while(it.hasNext()){
					PushRecordInfo pushInfo = (PushRecordInfo) it.next();
					if(pushInfo.getName()!=null && !"".equals(pushInfo.getName())){
						IObjectPK paramIObjectPK = new ObjectUuidPK(BOSUuid.read(pushInfo.getName())); 
						if(iSaleOrder.exists(paramIObjectPK)){
							SaleOrderInfo saleOrderInfo = iSaleOrder.getSaleOrderInfo(paramIObjectPK);
							String botpId = "";//
							List<IObjectPK> pks = null;//
							boolean mzFlag = false ;
							boolean lzFlag = false ;
							sourceColl.add(saleOrderInfo); 
							
							//VMI SO-B2B-LZ-SS 
							botpId = PurPlatUtil.getMappIdByFName(ctx,"VMI-SO-HIS-MZ-SS");   
							if(botpId !=null && !"".equals(botpId)){
								pks = AppUnit.botp(ctx, "CC3E933B", sourceColl, botpId);
								if(pks!=null && pks.size() > 0){
									// 门诊 
									//  // 销售订单-门诊采购入库单（VMI） 
									botpId = PurPlatUtil.getMappIdByFName(ctx,"SO-HIS-MZ-PI");  
									if(botpId !=null && !"".equals(botpId)){
 										pks = AppUnit.botp(ctx, "783061E3", sourceColl, botpId);
										if(pks!=null && pks.size() > 0){
											botpId = PurPlatUtil.getMappIdByFName(ctx,"SO-HIS-MZ-SS"); // 销售订单-门诊销售出库单（VMI）
											if(botpId !=null && !"".equals(botpId)){
											pks = AppUnit.botp(ctx, "CC3E933B", sourceColl, botpId);
											if(pks!=null && pks.size() > 0)
												mzFlag = true;
											}
										}
									} 
								
									// 栗喆
									botpId = PurPlatUtil.getMappIdByFName(ctx,"SO-HIS-LZ-PI"); // 销售订单-栗喆采购入库单（VMI）
									if(botpId !=null && !"".equals(botpId)){
										 pks = AppUnit.botp(ctx, "783061E3", sourceColl, botpId);
										 if(pks!=null && pks.size() > 0){
											 botpId = PurPlatUtil.getMappIdByFName(ctx,"SO-HIS-LZ-SS"); // 销售订单-栗喆销售出库单（VMI）
											 if(botpId !=null && !"".equals(botpId)){
												 pks = AppUnit.botp(ctx, "CC3E933B", sourceColl, botpId);
											 if(pks!=null && pks.size() > 0)
												 lzFlag = true;
											 }
										 }
									}
								}
							}
							sourceColl.clear();
							if(mzFlag && lzFlag){  
					 			pushInfo.setPushStatus(PushStatusEnum.doSuccess);
					 			pushInfo.setDescription(saleOrderInfo.getNumber());
							}else
								 pushInfo.setPushStatus(PushStatusEnum.doFail);
							botpId ="";
							pks = null;
						}else
							 pushInfo.setPushStatus(PushStatusEnum.doFail);
					}else
						 pushInfo.setPushStatus(PushStatusEnum.doFail);
					updateColl.add(pushInfo);
				}
				
				if(updateColl !=null && updateColl.size() >0)
					ibiz.update(updateColl);
			}
		} catch (EASBizException e) {
				e.printStackTrace();
		} catch (UuidException e) {
				e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		} 
	
	}
	
	@Override
	protected void _generSaleIssueBill(Context ctx) throws BOSException { 
		
		doGengerSalaIssueBillCGZ(ctx);
		
		doGengerSalaIssueBillVMI(ctx);
		
	}
    
	private void updateSaleIssInvType(Context ctx,String fid){
		if(fid != null && !"".equals(fid)){
			   String sql = "update T_IM_SaleIssueEntry set FInvUpdateTypeID ='CeUAAAAIdBvC73rf',FSupplierID='jbYAAAVlObc3xn38' where FParentID ='"+fid+"'";
			   System.out.println(sql);
			   try {
				DbUtil.execute(ctx,sql);
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		}
	
	}

	/**
	 * 根据付款单ID 保存传递JSON至日志记录表
	 * @param id 付款单ID
	 * @param busCode 业务类型
	 */
	@Override
	protected void _savePayment2PurLog(Context ctx, String id, String busCode)throws BOSException {
		 if(id !=null && !"".equals(id) && busCode !=null && !"".equals(busCode) ){
			 DateBasetype type = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
			if( PaymentSupport.baseTypeSets.contains(type)){
				try {
					PaymentSupport.insertBillToLog(ctx, id, busCode);
				} catch (EASBizException e) {
 					e.printStackTrace();
				}
			}  
		 }
	}

	/**
	 * 
	 * 同步付款单JSON 至B2B 系统
	 */
	@Override
	protected void _syncPurLog2B2B(Context ctx) throws BOSException {
		PaymentSupport.doSendPayMentToB2B(ctx);
	}
	
    
}