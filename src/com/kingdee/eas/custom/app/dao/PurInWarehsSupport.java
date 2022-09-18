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

import mondrian.rolap.BitKey.Big;

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
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.person.PersonInfo;
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
import com.kingdee.eas.custom.app.dto.PurInDTO;
import com.kingdee.eas.custom.app.dto.PurInDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.im.inv.IPurInWarehsBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillCollection;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryCollection;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;

public class PurInWarehsSupport {

//	public static String doSync(Context ctx,String jsonStr){
//		String result = null;
//		if(jsonStr != null && !"".equals(jsonStr)){
//		    System.out.println("************************json begin****************************");
//		    System.out.println("#####################jsonStr################=" + jsonStr);
//			DateBaseProcessType processType = DateBaseProcessType.AddNew;
//			DateBasetype baseType = DateBasetype.B2B_GZ_LZ_PI;
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
//					PurInDTO m = gson.fromJson(modelJE, PurInDTO.class);
//					if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
//						result = judgeModel(ctx,m,busCode);
//						if("".equals(result))
//						{
// 							PurInWarehsBillInfo info = createPurBillInfo(ctx, m,busCode);
//							IPurInWarehsBill ibiz = PurInWarehsBillFactory.getLocalInstance(ctx);
//							IObjectPK pk = ibiz.save(info);
//							ibiz.submit(pk.toString());
//							if(!busCode.contains("VMI")){
//								String fromID = info.getEntry().get(0).getSourceBillId();
//								if(fromID !=null && !"".equals(fromID)){
//								   String sql = "/*dialect*/insert into t_bot_relation (FID,FSrcEntityID,FDestEntityID,FSrcObjectID,FDestObjectID,FDate,FOperatorID,FisEffected,FBOTMappingID,FType) " +
//						    		" values(newbosid('59302EC6'),'3171BFAD','783061E3','" + fromID + "','" + pk.toString() + "',sysdate,'02','0','5iUfG0tUSoalSLeGmOHURwRRIsQ=','0')";
//								     DbUtil.execute(ctx,sql);
//								}
//							}
//						    
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
//	/**
//	 * 校验 实体是否正确
//	 * @param ctx
//	 * @param m
//	 * @return
//	 */
//	private static String judgeModel(Context ctx,PurInDTO m,String busCode ){
//		 String result = "";
//		 //组织是否存在
//		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
//			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
//			try {
//				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
//					result = result +"采购组织不存在,";
//			} catch (EASBizException e) {
// 				e.printStackTrace();
//			} catch (BOSException e) {
// 				e.printStackTrace();
//			}
//			 
//		 }else{
//			 result = result +"采购组织不能为空,";
//		 }
//		 
//		 if(m.getFnumber() ==null || "".equals(m.getFnumber())) 
//			 result = result +"单据编号不能为空,";
//		 
//		 
//		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
//			 result = result +"业务日期不能为空,";
//			
//		 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
//			 result = result +"供应商不能为空,";
//		 else{
//			if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
//				if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
//					 result = result +"供应商未分配当前组织,";
//				}else
//					 result = result +"供应商不存在,";
//		  }
//			
//			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
//				 result = result +"价税合计、金额、税额 都不允许为空,";
//			 else{
//				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
//					 result = result +"价税合计等于金额加税额的合计,";
//			 }
//			 
//			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
//				 for(PurInDetailDTO dvo : m.getDetails()){
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
//					if(!busCode.contains("VMI")){
//						 if(dvo.getFsourcebillnumber() ==null || "".equals(dvo.getFsourcebillnumber())){
//							 result = result +"第"+j+1+"订单编码不能为空,";
//						 }else{
//							 if(!PurPlatUtil.judgeExists(ctx, "PurOrder", m.getFstorageorgunitid(),dvo.getFsourcebillnumber())) 
//								 result = result +"第"+j+1+"行 订单编码"+dvo.getFsourcebillnumber()+"不存在,";
//						 }
//						 
//						 if(dvo.getFsourcebillentryid() ==null || "".equals(dvo.getFsourcebillentryid())){
//							 result = result +"第"+j+1+"订单明细行ID不能为空,";
//						 }else{
//							 if(!PurPlatUtil.judgeExists(ctx, "PurOrderEntry", m.getFstorageorgunitid(),dvo.getFsourcebillentryid())) 
//								 result = result +"第"+j+1+"行 订单明细行ID"+dvo.getFsourcebillentryid()+"不存在,";
//						 }
//					}
//				 
//				 }
//			} else 
//				result = result +"至少有一条明细行的数据,";
//			 
//		 return result;
//	}
//	
	
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
			DateBasetype baseType = DateBasetype.GZB_MZ_PI;
	 
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
				PurInDTO m = gson.fromJson(modelJE, PurInDTO.class);
				//采购入库单-退货业务流程
				if("GZB_MZ_PI".equals(busCode)||"GZB_LZ_PI".equals(busCode)||"DZB_MZ_PI".equals(busCode)
						||"VMI2CB_LZ_PI".equals(busCode)||"VMIB_MZ_PI".equals(busCode)){
					//根据 id 和明细id 查询 采购入库单是否存在
					
					// 判断msgId 是否存在SaleOrderDTO
					if(PurPlatUtil.judgeMsgIdExists(ctx, busCode, m.getBid())){
						 try {
							 PurInWarehsBillCollection coll = PurInWarehsBillFactory.getLocalInstance(ctx).getPurInWarehsBillCollection("where MsgId='"+m.getBid()+"'");
							 List<PurInDetailDTO> list = m.getDetails();
							 Map<String,BigDecimal> entryMp =null;
							 if(list !=null && list.size() > 0){
								 entryMp = new HashMap<String,BigDecimal>();
								 for(PurInDetailDTO dvo:list){
									 if(dvo.getBid() !=null && !"".equals(dvo.getBid())&& dvo.getFqty()!=null&&
											 dvo.getFqty().compareTo(BigDecimal.ZERO)>0	)
									 entryMp.put(dvo.getBid(), dvo.getFqty());
								 }
							 }
						
							 if(coll !=null && coll.size() >0){
								 CoreBillBaseCollection sourceColl = new CoreBillBaseCollection();  
								 PurInWarehsBillInfo info = coll.get(0);
								 //整单退货
								if(m.getIswholebill()!= null && "1".equals(m.getIswholebill())){
									sourceColl.add(info);
								}else{
									//部分退货
									 PurInWarehsEntryCollection entryColl = info.getEntry();
									 PurInWarehsEntryCollection tempEntryColl = new PurInWarehsEntryCollection();
									 Iterator it = entryColl.iterator();
									 while(it.hasNext()){
										 PurInWarehsEntryInfo entryInfo = (PurInWarehsEntryInfo) it.next();
										 if(entryInfo.get("MsgId") !=null && !"".equals( entryInfo.get("MsgId").toString() )){
											 if(entryMp.get(entryInfo.get("MsgId").toString())!=null){
												 BigDecimal curRetrunQty =  entryMp.get(entryInfo.get("MsgId").toString());
												 if(entryInfo.getQty().subtract(entryInfo.getReturnsQty()).subtract(curRetrunQty).compareTo(BigDecimal.ZERO) >=0)
												 {
													 PurInWarehsEntryInfo ec = (PurInWarehsEntryInfo) entryInfo.clone();
													 ec.setQty(curRetrunQty);
													 tempEntryColl.add(ec);
												 } 
											 }
										 }else
												result = PurPlatSyncEnum.NOTEXISTS_BILL.getAlias();
									 }
									 if(tempEntryColl.size() > 0){
										 info.getEntries().clear();
										 info.getEntries().addObjectCollection(tempEntryColl);
										 sourceColl.add(info);
									 }
								}
								if(sourceColl !=null && sourceColl.size() > 0){
									 List<IObjectPK> pks = AppUnit.botpSave(ctx, "783061E3", sourceColl, "JV7MYpL+QEKaxoy2KYZKzwRRIsQ=");
									 sourceColl.clear();
									 purPlatMenu = PurPlatSyncEnum.SUCCESS;
								}else
									 purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
							 }else
								 purPlatMenu = PurPlatSyncEnum.NOTEXISTS_BILL;
						} catch (BOSException e) {
 							e.printStackTrace();
						}
						
					}else
						purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
				}else
					purPlatMenu = PurPlatSyncEnum.BUSCODE_EXCEPTION;
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
					PurInWarehsBillInfo info = createBillInfo(ctx, m,busCode);
					IPurInWarehsBill ibiz = PurInWarehsBillFactory.getLocalInstance(ctx);
					IObjectPK pk = ibiz.save(info);
					ibiz.submit(pk.toString());
					if(!busCode.contains("VMI")){
						String fromID = info.getEntry().get(0).getSourceBillId();
						if(fromID !=null && !"".equals(fromID)){
							String sql = "/*dialect*/insert into t_bot_relation (FID,FSrcEntityID,FDestEntityID,FSrcObjectID,FDestObjectID,FDate,FOperatorID,FisEffected,FBOTMappingID,FType) " +
							" values(newbosid('59302EC6'),'3171BFAD','783061E3','" + fromID + "','" + pk.toString() + "',sysdate,'02','0','5iUfG0tUSoalSLeGmOHURwRRIsQ=','0')";
							DbUtil.execute(ctx,sql);
						}
					}	
			} catch (EASBizException e) {
	 		e.printStackTrace();
		} catch (BOSException e) {
			e.printStackTrace();
		}
	}
	
	
	private static PurInWarehsBillInfo createBillInfo(Context ctx, BaseSCMDTO m,String busCode)
    throws EASBizException, BOSException
  {
    PurInWarehsBillInfo info = new PurInWarehsBillInfo();

    ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);

	String billtypeId = "";//单据类型
	String sourceBilltypeId = "";//来源单据类型
	String biztypeId = "";//业务类型
	String transinfoId ="";//事务类型

    BigDecimal factor = new BigDecimal(1);
	if("GZ_LZ_PI".equals(busCode)||"GZ_MZ_PI".equals(busCode)||"SO_LZ_PI".equals(busCode)
			||"ZZ_GX_LZ_PI".equals(busCode)||"ZZ_YC_LZ_PI".equals(busCode)||"ZZ_YC_MZ_PI".equals(busCode)||
			"YX_MZ_PI".equals(busCode)||"YX_LZ_PI".equals(busCode)||"YC_PI".equals(busCode)||"DZ_MZ_PI".equals(busCode)){
		 billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//单据类型
		 sourceBilltypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//来源单据类型
		 biztypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C";//业务类型
		 transinfoId ="DawAAAAPoACwCNyn";//事务类型
		 factor = new BigDecimal(1);
	}else if("VMI_MZ_PI".equals(busCode)||"VMI_U_MZ_PI".equals(busCode)||"VMI_U_LZ_PI".equals(busCode)){
		 billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//单据类型
		 sourceBilltypeId = "";//来源单据类型
		 biztypeId = "LAdiD6Y5Sim6q6bmixITqSQHQ1w=";//业务类型
		 transinfoId ="CeUAAAAIdB+wCNyn";//事务类型
		 factor = new BigDecimal(1);
	}else if("VMIB_MZ_PI".equals(busCode)){
		 billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//单据类型
		 sourceBilltypeId = "";//来源单据类型
		 biztypeId = "3IS1Qaa0R32+3JPxlVZg5iQHQ1w=";//业务类型
		 transinfoId ="CeUAAAAMEoywCNyn";//事务类型
		 factor = new BigDecimal(-1);
	}
	
	
    CtrlUnitInfo cuInfo = storageorginfo.getCU();
    info.setCU(cuInfo);
    info.setStorageOrgUnit(storageorginfo);
    info.setPurchaseType(PurchaseTypeEnum.PURCHASE); 
    
    BillTypeInfo billtype = new BillTypeInfo();
    billtype.setId(BOSUuid.read(billtypeId));
    info.setBillType(billtype);
    
    PurchaseOrgUnitInfo purchaseorginfo = new PurchaseOrgUnitInfo();
    purchaseorginfo.setId(BOSUuid.read(m.getFstorageorgunitid()));
    
    info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
    info.setCreateTime(new Timestamp(new Date().getTime()));
    SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd");

    try {
		info.setBizDate(formmat.parse(m.getFbizdate()));
	} catch (ParseException e) {
 		e.printStackTrace();
	}
    CurrencyInfo currency = new CurrencyInfo();
    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
    info.setCurrency(currency);
    info.setExchangeRate(new BigDecimal("1.00"));
    
    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
      info.setStocker(person);
    }

    BizTypeInfo bizTypeinfo = new BizTypeInfo();
    bizTypeinfo.setId(BOSUuid.read(biztypeId));
    info.setBizType(bizTypeinfo);
    
    TransactionTypeInfo transinfo = new TransactionTypeInfo();
    transinfo.setId(BOSUuid.read(transinfoId));
    info.setTransactionType(transinfo);
    BillTypeInfo sourceBillTypeInfo =null;
    if(sourceBilltypeId!=null && !"".equals(sourceBilltypeId)){
    	sourceBillTypeInfo = new BillTypeInfo();
        sourceBillTypeInfo.setId(BOSUuid.read(sourceBilltypeId));
    }
    
    info.put("yisheng", person);
    info.put("HISdanjubianma", m.getFnumber());
    BigDecimal totalAmount = new BigDecimal(0);
    
    int isCollpur = 0;
    SupplierInfo supplierInfo = new SupplierInfo();
  
    if (isCollpur == 1)
    {
      supplierInfo.setId(BOSUuid.read("jbYAAAVlObc3xn38"));
      info.setSupplier(supplierInfo);
      info.put("iscollpur", Integer.valueOf(1));
    }
    else
    {
      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
      info.setSupplier(supplierInfo);
      info.put("iscollpur", Integer.valueOf(0));
    }
    info.put("MsgId", m.getId());

    info.put("factory", m.getFsupplierid());
  //  BigDecimal qty = new BigDecimal(1);
    for (BaseSCMDetailDTO entry : m.getDetails())
    {
        PurInWarehsEntryInfo entryInfo = createEntryInfo(ctx,  entry,busCode);
        entryInfo.setStorageOrgUnit(storageorginfo);
        entryInfo.setCompanyOrgUnit(xmcompany);
        entryInfo.setBizDate(info.getBizDate());
        entryInfo.setReceiveStorageOrgUnit(storageorginfo);
        entryInfo.setBalanceSupplier(supplierInfo);
        entryInfo.setPurchaseOrgUnit(purchaseorginfo);
        
        if(sourceBilltypeId!=null && !"".equals(sourceBilltypeId)){
            Map<String,String> orderEmp = PurPlatUtil.getOrderEntryMapByMsgId(ctx,m.getFstorageorgunitid(),entry.getFsourcebillentryid(),"P");
            if(orderEmp !=null && orderEmp.size() > 0){
            	entryInfo.setSourceBillEntryId(orderEmp.get("id"));
            	entryInfo.setSourceBillEntrySeq(Integer.parseInt(orderEmp.get("seq")));
            }
            
            Map<String,String> ordermp = PurPlatUtil.getOrderMapByNumber(ctx,m.getFstorageorgunitid(),entry.getFsourcebillnumber(),"P");
            if(ordermp !=null && orderEmp.size() > 0){
                entryInfo.setSourceBillId(ordermp.get("id"));
                entryInfo.setSourceBillNumber(ordermp.get("number"));
            }
        	  entryInfo.setSourceBillType(sourceBillTypeInfo);
        }
        
        totalAmount = totalAmount.add(entry.getFamount());
        info.getEntries().addObject(entryInfo);
       
    }
    info.setTotalQty(m.getFtotalqty().multiply(factor));
    if(!busCode.contains("VMI")){
	    info.setTotalAmount(totalAmount);
	    info.setTotalLocalAmount(totalAmount);
    }
    return info;
  }
  
  private static PurInWarehsEntryInfo createEntryInfo(Context ctx, BaseSCMDetailDTO dvo,String busCode)
    throws BOSException, EASBizException
  {
    PurInWarehsEntryInfo entryInfo = new PurInWarehsEntryInfo();
//    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
//    MaterialInfo material = null;
//    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
//    material = imaterial.getMaterialInfo(pk);
    
    
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
    
    String invUpdateTypeId = "8r0AAAAEaOjC73rf";
    BigDecimal factor = new BigDecimal(1);
	if("GZ_LZ_PI".equals(busCode)||"GZ_MZ_PI".equals(busCode)||"SO_LZ_PI".equals(busCode)
			||"ZZ_GX_LZ_PI".equals(busCode)||"ZZ_YC_LZ_PI".equals(busCode)||"ZZ_YC_MZ_PI".equals(busCode)||
			"YX_MZ_PI".equals(busCode)||"YX_LZ_PI".equals(busCode)||"YC_PI".equals(busCode)||"DZ_MZ_PI".equals(busCode)){
			invUpdateTypeId = "8r0AAAAEaOjC73rf";
			factor = new BigDecimal(1);
	}else if("VMI_MZ_PI".equals(busCode)||"VMI_U_MZ_PI".equals(busCode)||"VMI_U_LZ_PI".equals(busCode)){
			invUpdateTypeId = "CeUAAAAIdBrC73rf";
			factor = new BigDecimal(1);
	}else if("VMIB_MZ_PI".equals(busCode)){
		invUpdateTypeId = "CeUAAAAIdBrC73rf";
		factor = new BigDecimal(-1);
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
    entryInfo.setWrittenOffQty(dvo.getFqty());
    entryInfo.setWrittenOffBaseQty(dvo.getFbaseqty());
    entryInfo.setUnWriteOffQty(dvo.getFqty().multiply(factor));
    entryInfo.setUnWriteOffBaseQty(dvo.getFbaseqty().multiply(factor));
    entryInfo.setUnVmiSettleBaseQty(dvo.getFqty().multiply(factor));
    entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqQty(BigDecimal.ZERO);
    entryInfo.setAssistQty(BigDecimal.ZERO);
    entryInfo.put("MsgId", dvo.getId());
    //entryInfo.setStandardCost(BigDecimal.ZERO);
   if(!busCode.contains("VMI")){
	    entryInfo.setTaxRate(dvo.getFtaxrate());
	    entryInfo.setTax(dvo.getFtax());
	    entryInfo.setLocalTax(dvo.getFtax());
	    entryInfo.setAmount(dvo.getFamount());
	    entryInfo.setLocalAmount(dvo.getFamount());
	    entryInfo.setWrittenOffAmount(dvo.getFamount());
	    entryInfo.setTaxPrice(dvo.getFtaxprice());
	    entryInfo.setPrice(dvo.getFprice());
	    entryInfo.setActualPrice(dvo.getFtaxprice());
	    entryInfo.setActualTaxPrice(dvo.getFtaxprice());
	    entryInfo.setTaxAmount(dvo.getFtaxamount());
	    entryInfo.setLocalTaxAmount(dvo.getFtaxamount());
	    entryInfo.setUnWriteOffAmount(dvo.getFamount());
	    entryInfo.setUnitStandardCost(dvo.getFprice());
	    entryInfo.setStandardCost(dvo.getFamount());
	    entryInfo.setUnitActualCost(dvo.getFprice());
	    entryInfo.setActualCost(dvo.getFamount());
	    entryInfo.setUnitPurchaseCost(dvo.getFprice());
	    entryInfo.setPurchaseCost(dvo.getFamount());
   }

    entryInfo.put("huohao", material.get("huohao"));
    entryInfo.put("pinpai", material.get("pinpai"));
//    entryInfo.put("huanzheID", entry.getPatientId());
//    entryInfo.put("huanzhemingcheng", entry.getPatientName());
    
    return entryInfo;
  }
  
  
}
