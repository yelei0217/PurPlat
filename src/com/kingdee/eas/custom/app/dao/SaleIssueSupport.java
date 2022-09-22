package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.ConvertModeEnum;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.InvUpdateTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.PurInDetailDTO;
import com.kingdee.eas.custom.app.dto.SaleIssDTO;
import com.kingdee.eas.custom.app.dto.SaleIssDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillCollection;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryCollection;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryCollection;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;

public class SaleIssueSupport {

//	public static String doSync(Context ctx,String jsonStr){
//		String result = null;
//		if(jsonStr != null && !"".equals(jsonStr)){
//		    System.out.println("************************json begin****************************");
//		    System.out.println("#####################jsonStr################=" + jsonStr);
//			DateBaseProcessType processType = DateBaseProcessType.AddNew;
//			DateBasetype baseType = DateBasetype.B2B_GZ_LZ_SS;
//			String msgId = "";
//			String busCode ="";
//			String reqTime ="";
//			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
//			JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
//			JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
//			JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
//			Gson gson = new Gson();
//			JsonElement modelJE = returnData.get("data"); // 请求参数data
//			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
//					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
//					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
//				msgId = msgIdJE.getAsString() ;
//				busCode = busCodeJE.getAsString() ;
//				reqTime = reqTimeJE.getAsString() ;
//				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
//				try {
//					SaleIssDTO m = gson.fromJson(modelJE, SaleIssDTO.class);
//					if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
//						result = judgeModel(ctx,m,busCode);
//						if("".equals(result))
//						{
//							SaleIssueBillInfo info = createSaleBillInfo(ctx,m,busCode);
//							ISaleIssueBill ibiz =SaleIssueBillFactory.getLocalInstance(ctx);
//							IObjectPK pk = ibiz.save(info);
//							ibiz.submit(pk.toString());
//							if(!busCode.contains("VMI")){
//								String fromID = info.getEntry().get(0).getSourceBillId();
//								if(fromID !=null && !"".equals(fromID)){
//								   String sql = "/*dialect*/insert into t_bot_relation (FID,FSrcEntityID,FDestEntityID,FSrcObjectID,FDestObjectID,FDate,FOperatorID,FisEffected,FBOTMappingID,FType) " +
//						    		" values(newbosid('59302EC6'),'C48A423A','CC3E933B','" + fromID + "','" + pk.toString() + "',sysdate,'02','0','6a7669e6-0108-1000-e000-2136c0a812fd045122C4','0')";
//								     DbUtil.execute(ctx,sql);
//								}
//							}
//							result = "success";
//						}
//					}else
//						result = PurPlatSyncEnum.EXISTS_BILL.getAlias();
//				} catch (BOSException e) {
//		 			e.printStackTrace();
//					result = PurPlatSyncEnum.EXCEPTION_SERVER.getAlias();
//				} catch (EASBizException e) {
//					result = PurPlatSyncEnum.EXCEPTION_SERVER.getAlias();
//					e.printStackTrace();
//				}
//			}else
//				result = PurPlatSyncEnum.FIELD_NULL.getAlias();
//		}else
//			result = PurPlatSyncEnum.FIELD_NULL.getAlias();
//
//		return result ;		
//	}
//	
//	/**
//	 * 校验 实体是否正确
//	 * @param ctx
//	 * @param m
//	 * @return
//	 */
//	private static String judgeModel(Context ctx,SaleIssDTO m,String busCode ){
//		 String result = "";
//		 //组织是否存在
//		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
//			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
//			try {
//				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
//					result = result +"库存组织不存在,";
//			} catch (EASBizException e) {
// 				e.printStackTrace();
//			} catch (BOSException e) {
// 				e.printStackTrace();
//			}
//			 
//		 }else{
//			 result = result +"库存组织不能为空,";
//		 }
//		 
//		 if(m.getFnumber() ==null || "".equals(m.getFnumber())) 
//			 result = result +"单据编号不能为空,";
//		 
//		 
//		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
//			 result = result +"业务日期不能为空,";
//			
//		 if(m.getFcustomerid() == null || "".equals(m.getFcustomerid()))
//			 result = result +"客户不能为空,";
//		 else{
//			if(PurPlatUtil.judgeExists(ctx, "CUS", "", m.getFcustomerid())){
//				if(!PurPlatUtil.judgeExists(ctx, "CUSS",m.getFstorageorgunitid(), m.getFcustomerid()))
//					 result = result +"客户未分配当前组织,";
//				}else
//					 result = result +"客户不存在,";
//		  }
//		 
//		 if(busCode.contains("VMI")){
//			 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
//				 result = result +"供应商不能为空,";
//			 else{
//				if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
//					if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
//						 result = result +"供应商未分配当前组织,";
//					}else
//						 result = result +"供应商不存在,";
//			  }
//					  
//		 }
//			
//			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
//				 result = result +"价税合计、金额、税额 都不允许为空,";
//			 else{
//				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
//					 result = result +"价税合计等于金额加税额的合计,";
//		  }
//			 
//			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
//				 for(SaleIssDetailDTO dvo : m.getDetails()){
//					 int j = 0 ; 
//					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
//						 result = result +"第"+j+1+"行物料ID不能为空,";
//					 }else{
//						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
//							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
//								 result = result +"第"+j+1+"物料未分配当前组织,";
//						 }else
//							 result = result +"第"+j+1+"行 物料ID不存在,";
//					 }
//					 
//					 if(dvo.getFwarehouseid() ==null || "".equals(dvo.getFwarehouseid())){
//						 result = result +"第"+j+1+"行仓库ID不能为空,";
//					 }else{
// 						if(!PurPlatUtil.judgeExists(ctx, "Warehouse",m.getFstorageorgunitid(), dvo.getFwarehouseid()))
//							 result = result +"第"+j+1+"行仓库ID不存在,";
//					 } 
//					 
//					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
//						 result = result +"第"+j+1+"行计量单位不能为空,";
//					 }else{
//						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
//							 result = result +"第"+j+1+"行 计量单位"+dvo.getFunitid()+"不存在,";
//					 }
//					 
//					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
//						 result = result +"第"+j+1+"行基本计量单位不能为空,";
//					 }else{
//						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
//							 result = result +"第"+j+1+"行 基本计量单位"+dvo.getFbaseunitid()+"不存在,";
//					 }
//					 
//					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
//						 result = result +"第"+j+1+"行 订货数量、基本数量不能为空,";
//					}
//					 
//					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
//						 result = result +"第"+j+1+"行 单价、实际单价、含税单价、实际含税单价 不能为空,";
//					}
//					
//					if(dvo.getFtaxrate() == null){ 
//						 result = result +"第"+j+1+"行 税率不能为空,";
//					}
//					if(dvo.getFtax() == null){ 
//						 result = result +"第"+j+1+"行 税额不能为空,";
//					}
//					 
//					if(dvo.getFamount()== null){ 
//						 result = result +"第"+j+1+"行 金额不能为空,";
//					}
//					
//					if(dvo.getFtaxamount() == null){ 
//						 result = result +"第"+j+1+"行 价税合计不能为空,";
//					}
//			 
//				 }
//			} else 
//				result = result +"至少有一条明细行的数据,";
//			 
//		 return result;
//	}
 	 
	public static String doRollBackBill(Context ctx,String jsonStr){
 		String result = null;
		String msgId = "";
		String busCode ="";
		String reqTime ="";
		BaseResponseDTO respondDTO = new BaseResponseDTO();
		PurPlatSyncEnum purPlatMenu = PurPlatSyncEnum.SUCCESS;
		Gson gson = new Gson();
 		
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZB_LZ_SS;
	 
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
			JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
			JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
			JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
 			JsonElement modelJE = returnData.get("data"); // 请求参数data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
  				baseType = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
				
				// 记录日志
				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");
				SaleIssDTO m = gson.fromJson(modelJE, SaleIssDTO.class);
				//采购入库单-退货业务流程
		 		if("GZB_LZ_SS".equals(busCode)||"VMIB_LZ_SS".equals(busCode)){
					//根据 id 和明细id 查询 采购入库单是否存在
		 		// 判断msgId 是否存在SaleOrderDTO
					if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
						 try {
							 SaleIssueBillCollection coll = SaleIssueBillFactory.getLocalInstance(ctx).getSaleIssueBillCollection("where bid='"+m.getBid()+"'");
							 List<SaleIssDetailDTO> list = m.getDetails();
							 Map<String,BigDecimal> entryMp =null;
							 if(list !=null && list.size() > 0){
								 entryMp = new HashMap<String,BigDecimal>();
								 for(SaleIssDetailDTO dvo:list){
									 if(dvo.getBid() !=null && !"".equals(dvo.getBid())&& dvo.getFqty()!=null&&
											 dvo.getFqty().compareTo(BigDecimal.ZERO)>0	)
									 entryMp.put(dvo.getBid(), dvo.getFqty());
								 }
							 }
						
							 if(coll !=null && coll.size() >0){
								 CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
								 SaleIssueBillInfo info = coll.get(0);
								 //整单退货
								if(m.getIswholebill()!= null && "1".equals(m.getIswholebill())){
									sourceColl.add(info);
								}else{
									//部分退货
									 SaleIssueEntryCollection entryColl = info.getEntry();
									 SaleIssueEntryCollection tempEntryColl = new SaleIssueEntryCollection();
									 Iterator it = entryColl.iterator();
									 while(it.hasNext()){
										 SaleIssueEntryInfo entryInfo = (SaleIssueEntryInfo) it.next();
										 if(entryInfo.get("bid") !=null && !"".equals( entryInfo.get("bid").toString() )){
											 if(entryMp.get(entryInfo.get("bid").toString())!=null){
												 BigDecimal curRetrunQty =  entryMp.get(entryInfo.get("bid").toString());
												 if(entryInfo.getQty().subtract(entryInfo.getReturnsQty()).subtract(curRetrunQty).compareTo(BigDecimal.ZERO) >=0)
												 {
													 SaleIssueEntryInfo ec = (SaleIssueEntryInfo) entryInfo.clone();
													 ec.setQty(curRetrunQty);
													 tempEntryColl.add(ec);
												 } else
													 result = PurPlatSyncEnum.BACK_NUM_MAX.getAlias();	 
											 }else
												 result = PurPlatSyncEnum.NOTEXISTS_BILL.getAlias();
										 }else
											 result = PurPlatSyncEnum.NOTEXISTS_BILL.getAlias();
									 }
									 if(tempEntryColl.size() > 0){
										 info.getEntries().clear();
										 info.getEntries().addObjectCollection(tempEntryColl);
										 sourceColl.add(info);
 									 }else
 									 purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
								}
								if(sourceColl !=null && sourceColl.size() > 0){
						 			 List<IObjectPK> pks = AppUnit.botpSave(ctx, "CC3E933B", sourceColl, "ufgs6nQJRo29KGbQb3EbdgRRIsQ=");
									 sourceColl.clear();
									 purPlatMenu = PurPlatSyncEnum.SUCCESS;
								}else
									 purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
								
							 }else
								 purPlatMenu = PurPlatSyncEnum.EXCEPTION_SERVER;
						} catch (BOSException e) {
 							e.printStackTrace();
  							purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
						}
					}else
						purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
				}else
					purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
			}else
				purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		}else
			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		
		respondDTO.setCode(purPlatMenu.getValue());
		respondDTO.setMsgId(msgId);
		respondDTO.setMsg(purPlatMenu.getAlias());
		return gson.toJson(respondDTO);
		
	}
	
	
	public static void doSaveBill(Context ctx,BaseSCMDTO m,String busCode){
			try { 
					SaleIssueBillInfo info = createSaleBillInfo(ctx,m,busCode);
					ISaleIssueBill ibiz =SaleIssueBillFactory.getLocalInstance(ctx);
					IObjectPK pk = ibiz.save(info);
					ibiz.submit(pk.toString());
					if(!busCode.contains("VMI")){
						String fromID = info.getEntry().get(0).getSourceBillId();
						if(fromID !=null && !"".equals(fromID)){
						   String sql = "/*dialect*/insert into t_bot_relation (FID,FSrcEntityID,FDestEntityID,FSrcObjectID,FDestObjectID,FDate,FOperatorID,FisEffected,FBOTMappingID,FType) " +
				    		" values(newbosid('59302EC6'),'C48A423A','CC3E933B','" + fromID + "','" + pk.toString() + "',sysdate,'02','0','6a7669e6-0108-1000-e000-2136c0a812fd045122C4','0')";
						     DbUtil.execute(ctx,sql);
						}
					}
			} catch (EASBizException e) {
	 		e.printStackTrace();
		} catch (BOSException e) {
			e.printStackTrace();
		}
	}
		
	
	private static SaleIssueBillInfo createSaleBillInfo(Context ctx, BaseSCMDTO m,String busCode )
	    throws BOSException, EASBizException
	  {
	   
	    SaleIssueBillInfo info = new SaleIssueBillInfo();
	    ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
	    
	    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
	    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
	    CtrlUnitInfo cuInfo = storageorginfo.getCU();
	    info.setCU(cuInfo);
	    
		String billtypeId = "";//单据类型
		String sourceBilltypeId = "";//来源单据类型
		String biztypeId = "";//业务类型
		String transinfoId ="";//事务类型
	    
	    info.setStorageOrgUnit(storageorginfo);
	    info.setIsSysBill(false);
	    info.setConvertMode(ConvertModeEnum.DIRECTEXCHANGERATE);
	    
		if("GZ_LZ_SS".equals(busCode)){
			 billtypeId = "50957179-0105-1000-e000-015bc0a812fd463ED552";//单据类型
			 sourceBilltypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//来源单据类型
			 biztypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C";//业务类型
			 transinfoId ="DawAAAAPoAywCNyn";//事务类型
			
		}else if("VMI_LZ_SS".equals(busCode)){
			 billtypeId = "50957179-0105-1000-e000-015bc0a812fd463ED552";//单据类型
			 sourceBilltypeId = "";//来源单据类型
			 biztypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C";//业务类型
			 transinfoId ="DawAAAAPoAywCNyn";//事务类型 
		}else if("VMIB_LZ_SS".equals(busCode)){
			 billtypeId = "50957179-0105-1000-e000-015bc0a812fd463ED552";//单据类型
			 sourceBilltypeId = "";//来源单据类型
			 biztypeId = "d8e80652-0110-1000-e000-04c5c0a812202407435C";//业务类型
			 transinfoId ="DawAAAAPoA2wCNyn";//事务类型  
		}
	    
	    BillTypeInfo billtype = new BillTypeInfo();
	    billtype.setId(BOSUuid.read(billtypeId));
	    info.setBillType(billtype);
	    
	    SaleOrgUnitInfo saleOrgInfo = new SaleOrgUnitInfo();
	    saleOrgInfo.setId(BOSUuid.read(m.getFstorageorgunitid()));
	    
	    info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
	    info.setCreateTime(new Timestamp(new Date().getTime()));
	    
	    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");
	    Date bizDate = new Date();
	    try {
			bizDate = formmat.parse(m.getFbizdate());
		} catch (ParseException e) {
 			e.printStackTrace();
		}
	    info.setBizDate(bizDate);
	    
	    PaymentTypeInfo paymenttypeinfo = new PaymentTypeInfo();
	    paymenttypeinfo.setId(BOSUuid.read("91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5"));
	    info.setPaymentType(paymenttypeinfo);
	    
	    CurrencyInfo currency = new CurrencyInfo();
	    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
	    info.setCurrency(currency);
	    info.setExchangeRate(new BigDecimal("1.00"));
	    
	    CustomerInfo customerInfo = new CustomerInfo();
	    customerInfo.setId(BOSUuid.read(m.getFcustomerid()));
	    info.setCustomer(customerInfo);
	    
	    BizTypeInfo bizTypeinfo = new BizTypeInfo();
	    bizTypeinfo.setId(BOSUuid.read(biztypeId)); //普通销售
	    info.setBizType(bizTypeinfo);
	    
	    TransactionTypeInfo transinfo = new TransactionTypeInfo();
	    transinfo.setId(BOSUuid.read(transinfoId));//普通销售出库
	    info.setTransactionType(transinfo);
	    
	    info.put("MsgId", m.getId());

	    BillTypeInfo sourceBillTypeInfo = null ;
	    if(sourceBilltypeId != null && !"".equals(sourceBilltypeId)){
		    sourceBillTypeInfo = new BillTypeInfo();
		    sourceBillTypeInfo.setId(BOSUuid.read(sourceBilltypeId));
	    }

	    SupplierInfo supplierInfo =null;
	    if(m.getFsupplierid() != null && !"".equals(m.getFsupplierid())){
	    	supplierInfo = new SupplierInfo();
	        supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
	    }
	    String orderId = ""; //b2b订单ID
	   // BigDecimal totalAmount = new BigDecimal(0);
	    for (BaseSCMDetailDTO entry : m.getDetails())
	    {
	        SaleIssueEntryInfo entryInfo = createSaleEntryInfo(ctx,entry,busCode);
	        entryInfo.setStorageOrgUnit(storageorginfo);
	        entryInfo.setCompanyOrgUnit(xmcompany);
	        entryInfo.setBizDate(info.getBizDate());
	      //  entryInfo.setWarehouse(warehouseinfo);
	      //  entryInfo.setInvUpdateType(invUpdateType);
	        entryInfo.setBalanceCustomer(customerInfo);
	        entryInfo.setSaleOrgUnit(saleOrgInfo);
	        orderId = entry.getFsourcebillid();
	        //totalAmount = totalAmount.add(entry.getAmount());
		    if(sourceBilltypeId != null && !"".equals(sourceBilltypeId)){
		        Map<String,String> orderEmp = PurPlatUtil.getOrderEntryMapByMsgId(ctx,m.getFstorageorgunitid(),entry.getFsourcebillentryid(),"S");
		        if(orderEmp !=null && orderEmp.size() > 0){
		        	entryInfo.setSourceBillEntryId(orderEmp.get("id"));
		        	entryInfo.setSourceBillEntrySeq(Integer.parseInt(orderEmp.get("seq")));
		        }
		        
		        Map<String,String> ordermp = PurPlatUtil.getOrderMapByNumber(ctx,m.getFstorageorgunitid(),entry.getFsourcebillnumber(),"S");
		        if(ordermp !=null && orderEmp.size() > 0){
		            entryInfo.setSourceBillId(ordermp.get("id"));
		            entryInfo.setSourceBillNumber(ordermp.get("number"));
		        }
		        entryInfo.setSourceBillType(sourceBillTypeInfo);
		    }
		    
		    if(supplierInfo !=null)
		    	entryInfo.setSupplier(supplierInfo);
		  
	        info.getEntries().addObject(entryInfo);
	      }
	    info.put("bid", orderId);
	    if(!busCode.contains("VMI")){
		    info.setTotalAmount(m.getFtotalamount());
		    info.setTotalLocalAmount(m.getFtotalamount());
	    }
	 
	    return info;
	  }
	  
	private static SaleIssueEntryInfo createSaleEntryInfo(Context ctx, BaseSCMDetailDTO dvo ,String busCode)
	    throws EASBizException, BOSException
	  {
//	    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
 	    SaleIssueEntryInfo entryInfo = new SaleIssueEntryInfo();

//	    MaterialInfo material = null;
//	    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
//	    material = imaterial.getMaterialInfo(pk);
	    
 		EntityViewInfo view = new EntityViewInfo();
 	 	FilterInfo filter = new FilterInfo();
 	 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
 	  	view.setFilter(filter);
 	 	
 	    MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
 	    MaterialInfo material = materialColl.get(0);
 	    
 	    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
	    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	    
	    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
	    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
	    
	    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
	    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
	    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
	    entryInfo.setWarehouse(warehouseinfo);
	    
	    String invUpdateTypeId = "'";
	    BigDecimal factor = new BigDecimal(1);
		if("GZ_LZ_SS".equals(busCode)){
			invUpdateTypeId ="8r0AAAAEaOnC73rf";
			factor =  new BigDecimal(1);
		}else if("VMI_LZ_SS".equals(busCode)){
			invUpdateTypeId ="CeUAAAAIdBvC73rf";
			factor =  new BigDecimal(1);
		}else if("VMIB_LZ_SS".equals(busCode)){
			invUpdateTypeId ="CeUAAAAIdBvC73rf";
			factor =  new BigDecimal(-1);
		}
	    InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
	    invUpdateType.setId(BOSUuid.read(invUpdateTypeId));
	    entryInfo.setInvUpdateType(invUpdateType);
	    entryInfo.setMaterial(material);
	    entryInfo.setBaseUnit(baseUnitInfo);
	    entryInfo.setUnit(unitInfo);
	    entryInfo.setQty(dvo.getFqty().multiply(factor));
	    entryInfo.setBaseQty(dvo.getFbaseqty().multiply(factor));
	    entryInfo.setAssociateQty(BigDecimal.ZERO);
	    entryInfo.setWrittenOffQty(dvo.getFqty().multiply(factor));
	    entryInfo.setWrittenOffBaseQty(dvo.getFbaseqty());
	    entryInfo.setUnWriteOffQty(dvo.getFqty().multiply(factor));
	    entryInfo.setUnWriteOffBaseQty(dvo.getFbaseqty().multiply(factor));
	    entryInfo.setUnSettleQty(dvo.getFqty().multiply(factor));
	    entryInfo.setUnSettleBaseQty(dvo.getFbaseqty().multiply(factor));
	    entryInfo.setUnVmiSettleBaseQty(dvo.getFqty().multiply(factor));
	    entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
	    entryInfo.setAssistQty(BigDecimal.ZERO);
	    entryInfo.put("bid", dvo.getFsourcebillentryid());
	    if(!busCode.contains("VMI")){
	    	entryInfo.setTaxRate(dvo.getFtaxrate());
	   	    entryInfo.setTax(dvo.getFtax());
	   	    entryInfo.setLocalTax(dvo.getFtax());
	 	    entryInfo.setTax(BigDecimal.ZERO);
		    entryInfo.setAmount(dvo.getFamount());
		    entryInfo.setLocalAmount(dvo.getFamount());
		    entryInfo.setWrittenOffAmount(dvo.getFamount());
		    entryInfo.setTaxPrice(dvo.getFtaxprice());
		    entryInfo.setPrice(dvo.getFprice());
		    entryInfo.setActualPrice(dvo.getFtaxprice());
		    entryInfo.setUnWriteOffAmount(dvo.getFamount());
		    entryInfo.setUnitStandardCost(dvo.getFprice());
		    entryInfo.setStandardCost(dvo.getFamount());    
		    entryInfo.setUnitActualCost(dvo.getFprice());
		    entryInfo.setActualCost(dvo.getFamount());
	    }
	    entryInfo.put("huohao", material.get("huohao"));
	    entryInfo.put("pinpai", material.get("pinpai"));
	    entryInfo.put("MsgId", dvo.getId());
	    return entryInfo;
	  }
	  
}
