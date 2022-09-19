package com.kingdee.eas.custom.app;

import org.apache.log4j.Logger;
import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kingdee.bos.*;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.metadata.rule.RuleExecutor;
import com.kingdee.bos.metadata.MetaDataPK;
//import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.framework.ejb.AbstractEntityControllerBean;
import com.kingdee.bos.framework.ejb.AbstractBizControllerBean;
//import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.service.ServiceContext;
import com.kingdee.bos.service.IServiceContext;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.IPushRecord;
import com.kingdee.eas.custom.PushRecordCollection;
import com.kingdee.eas.custom.PushRecordFactory;
import com.kingdee.eas.custom.PushRecordInfo;
import com.kingdee.eas.custom.app.dao.VMISaleOrderSupport;
import com.kingdee.eas.custom.app.dto.VMISaleOrderDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.im.inv.IPurInWarehsBill;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
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

	@Override
	protected void _generSaleIssueBill(Context ctx) throws BOSException {
		IPushRecord ibiz = PushRecordFactory.getLocalInstance(ctx);
	 	EntityViewInfo view = new EntityViewInfo();
	 	FilterInfo filter = new FilterInfo();
	 	filter.getFilterItems().add(new FilterItemInfo("processType", DateBaseProcessType.GSaleIss,CompareType.EQUALS)); //
	 	filter.getFilterItems().add(new FilterItemInfo("PushStatus", PushStatusEnum.unDo,CompareType.EQUALS)); // 
	 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType", DateBasetype.CGZ_U_MZ_SO,CompareType.EQUALS)); // 
	 	filter.getFilterItems().add(new FilterItemInfo("dateBaseType", DateBasetype.VMI_U_MZ_SO,CompareType.EQUALS)); // 
	    filter.setMaskString("#0 and #1 and (#2 or #3)");
	 	view.setFilter(filter);
	 	try {
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
 						 		List<IObjectPK> pks = AppUnit.botp(ctx, "CC3E933B", sourceColl, botpId);
// 						 		List<IObjectPK> pks = AppUnit.botpSave(ctx, "CC3E933B", sourceColl, botpId);
						 		sourceColl.clear();
						 		if(pks !=null && pks.size() >0){
						 			// 如果类型未  VMI_U_MZ_SO 需要修改明细行的更改类型
						 			IObjectPK issPK = pks.get(0);
									SaleIssueBillInfo saleIssInfo = iSaleIssue.getSaleIssueBillInfo(issPK);
									if(pushInfo.getDateBaseType() == DateBasetype.VMI_U_MZ_SO){
										updateSaleIssInvType(ctx,pks.get(0).toString());
										
										//门诊-采购入库 （EAS自动）	VMI_U_MZ_PI 
										
										//门诊-销售出库 （EAS自动）	VMI_U_MZ_SS	 
										
										//栗喆-采购入库 （EAS自动）	VMI_U_LZ_PI 
										
										//栗喆-销售出库 （EAS自动）	VMI_U_LZ_SS	
										String reqStr = pushInfo.getReq();
										Gson gson = new Gson();
										VMISaleOrderDTO m =null;
 										try {
											m = gson.fromJson(reqStr, VMISaleOrderDTO.class);
											VMISaleOrderSupport.doGengerBill(ctx, m);

										} catch (JsonSyntaxException e) {
											//purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
						 					e.printStackTrace();
						 					pushInfo.setPushStatus(PushStatusEnum.doFail);
										}
									}
								//	IObjectPK issPK = iSaleIssue.submit(saleIssInfo);
									if(issPK != null && !"".equals(issPK.toString())){
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
		} 
	}
    
	private void updateSaleIssInvType(Context ctx,String fid){
		if(fid != null && !"".equals(fid)){
			   String sql = "update T_IM_SaleIssueEntry set FInvUpdateTypeID ='CeUAAAAIdBvC73rf' where FParentID ='"+fid+"'";
			   System.out.println(sql);
			   try {
				DbUtil.execute(ctx,sql);
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		}
	
	}
	
    
}