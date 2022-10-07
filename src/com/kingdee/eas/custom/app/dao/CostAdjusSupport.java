package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cloudera.impala.jdbc4.internal.apache.zookeeper.version.Info;
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
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
import com.kingdee.eas.basedata.scm.common.TransactionTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.IWarehouse;
import com.kingdee.eas.basedata.scm.im.inv.StoreStateInfo;
import com.kingdee.eas.basedata.scm.im.inv.StoreTypeInfo;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseFactory;
import com.kingdee.eas.basedata.scm.im.inv.WarehouseInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.CostAdjusDTO;
import com.kingdee.eas.custom.app.dto.CostAdjusDetailDTO;
import com.kingdee.eas.custom.app.dto.PurInDTO;
import com.kingdee.eas.custom.app.dto.base.BaseResponseDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.scm.cal.CalculateKindEnum;
import com.kingdee.eas.scm.cal.CostAdjustBillEntryInfo;
import com.kingdee.eas.scm.cal.CostAdjustBillFactory;
import com.kingdee.eas.scm.cal.CostAdjustBillInfo;
import com.kingdee.eas.scm.cal.ICostAdjustBill;
import com.kingdee.eas.scm.cal.IssueTypeEnum;
import com.kingdee.eas.scm.common.BillBaseStatusEnum;
import com.kingdee.eas.scm.common.EntryBaseStatusEnum;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;

public class CostAdjusSupport {

	public static String doSync(Context ctx,String jsonStr){
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
			DateBasetype baseType = DateBasetype.GZ_CK_LZ_CJ;
			
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
//				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
				try {
					CostAdjusDTO m = null;
					try {
						m = gson.fromJson(modelJE, CostAdjusDTO.class);
					} catch (JsonSyntaxException e) {
						purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
 						e.printStackTrace();
					}
					
					if(m !=null){
						if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
							result = judgeModel(ctx,m,busCode);
							if("".equals(result))
							{
								ICostAdjustBill ibiz = CostAdjustBillFactory.getLocalInstance(ctx);
								CostAdjustBillInfo info  = createCostAdjusInfo(ctx, m, busCode);
								IObjectPK pk = ibiz.addnew(info);
								ibiz.submit(pk.toString());
							}else 
						    	  purPlatMenu = PurPlatSyncEnum.EXCEPTION_SERVER;
						}else 
					    	  purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
					}else
						purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
				} catch (BOSException e) {
 					e.printStackTrace();
				} catch (EASBizException e) {
 					e.printStackTrace();
				}
			}else
				purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
			
		}else
			purPlatMenu = PurPlatSyncEnum.FIELD_NULL;
		
		respondDTO.setCode(purPlatMenu.getValue());
		respondDTO.setMsgId(msgId);
		if(purPlatMenu==PurPlatSyncEnum.EXCEPTION_SERVER)
		respondDTO.setMsg(result);
		else
			respondDTO.setMsg(purPlatMenu.getAlias());
		return gson.toJson(respondDTO);
 	}
	
	private static String judgeModel(Context ctx,CostAdjusDTO m,String busCode ){
		String result = "";
		 //组织是否存在
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!StorageOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"库存组织不存在,";
			} catch (EASBizException e) {
				e.printStackTrace();
			} catch (BOSException e) {
				e.printStackTrace();
			}
			 
		 }else{
			 result = result +"库存组织不能为空,";
		 }
		 
//		 if(m.getFnumber() ==null || "".equals(m.getFnumber())) 
//			 result = result +"单据编号不能为空,";
		 
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"业务日期不能为空,";
		 
		 if( m.getFtotalamount() == null)
			 result = result +"金额不允许为空,";
		 else{
			 if(m.getFtotalamount().compareTo(BigDecimal.ZERO) < 0)
				 result = result +"金额不能等于零,";
		 }
		 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(CostAdjusDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"第"+j+1+"行物料ID不能为空,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
								 result = result +"第"+j+1+"物料未分配当前组织,";
						 }else
							 result = result +"第"+j+1+"行 物料ID不存在,";
					 }
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"第"+j+1+"行计量单位不能为空,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"第"+j+1+"行计量单位"+dvo.getFunitid()+"不存在,";
					 }
					 
//					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
//						 result = result +"第"+j+1+"行基本计量单位不能为空,";
//					 }else{
//						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
//							 result = result +"第"+j+1+"行基本计量单位"+dvo.getFbaseunitid()+"不存在,";
//					 }
				 }
			}else					
				result = result +"至少有一条明细行的数据,";
 
		 
		return result ;
	}
	
	private static CostAdjustBillInfo createCostAdjusInfo(Context ctx, CostAdjusDTO m,String busCode)
    throws EASBizException, BOSException
  {
		  CostAdjustBillInfo info = new CostAdjustBillInfo();
		  ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
		  StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
		  CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
		  
			String billtypeId = "3a3b5446-0106-1000-e000-01bcc0a812e6463ED552";//单据类型
			String storeTypeId = "181875d5-0105-1000-e000-0111c0a812fd97D461A6"; // 库存类型
			String storeStatusId = "181875d5-0105-1000-e000-012ec0a812fd62A73FA5"; // 库存状态
//
			 CalculateKindEnum kind = null;
			if("GZ_CK_LZ_CJ".equals(busCode)){
				kind = CalculateKindEnum.OUTPUT_WAREHOUSE; 
				  billtypeId = "3a3b5446-0106-1000-e000-01bcc0a812e6463ED552";//单据类型
				  storeTypeId = "181875d5-0105-1000-e000-0111c0a812fd97D461A6"; // 库存类型 普通
				  storeStatusId = "181875d5-0105-1000-e000-012ec0a812fd62A73FA5"; // 库存状态 可用
			}else{
				kind = CalculateKindEnum.INPUT_WAREHOUSE; 
				
			}
			StoreTypeInfo storeTypeInfo = new StoreTypeInfo();
			storeTypeInfo.setId(BOSUuid.read(storeTypeId));
			
			StoreStateInfo storeStatusInfo = new StoreStateInfo();
			storeStatusInfo.setId(BOSUuid.read(storeStatusId));
			
			 	CtrlUnitInfo cuInfo = storageorginfo.getCU();
			    info.setCU(cuInfo);
			    info.setStorageOrgUnit(storageorginfo);
			    String number = AppUnit.getCodeRule(ctx, info, m.getFstorageorgunitid());
			    info.setNumber(number);
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
				 
				info.setCreateType(com.kingdee.eas.scm.cal.CostAdjuestCreateTypeEnum.USERINPUT);
				info.setCalculateKind(kind);
				info.setIssueType(IssueTypeEnum.SALEISSUEBILL); 
				
//			    CurrencyInfo currency = new CurrencyInfo();
//			    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
//			    info.setCurrency(currency);
		 
			    
//			    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
//			    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
//			      info.setStocker(person);
//			    }
				String dateStr = m.getFbizdate();
				
				info.setYear(Integer.parseInt(dateStr.substring(0, 4)));
				info.setPeriod(Integer.parseInt(dateStr.substring(5, 7)));
				info.setMonth(Integer.parseInt(dateStr.substring(0, 7).replace("-", "")));
				info.setDay(Integer.parseInt(dateStr.replace("-", "")));
			    
			    for(CostAdjusDetailDTO dvo:m.getDetails()){
			    	CostAdjustBillEntryInfo  entryInfo =  createCostEntryInfo(ctx,dvo,busCode);
			    	 entryInfo.setStorageOrgUnit(storageorginfo);
			         entryInfo.setCompanyOrgUnit(xmcompany);
			         entryInfo.setBizDate(info.getBizDate());
			         entryInfo.setStoreType(storeTypeInfo);
			         entryInfo.setStoreStatus(storeStatusInfo);
			         info.getEntries().addObject(entryInfo);
			    }
			    
			    info.setTotalActualCost(m.getFtotalamount());
			    info.setTotalAmount(m.getFtotalamount());
			    info.setTotalQty(m.getFtotalqty());
			    info.setTotalStandardCost(m.getFtotalamount());
			    info.put("MsgId", m.getId());
			    info.setBaseStatus(BillBaseStatusEnum.ADD);
		return info;
  	}
	
	  private static CostAdjustBillEntryInfo createCostEntryInfo(Context ctx, CostAdjusDetailDTO dvo,String busCode )
	    throws BOSException, EASBizException
	  {
		    CostAdjustBillEntryInfo entryInfo = new CostAdjustBillEntryInfo();
	   		EntityViewInfo view = new EntityViewInfo();
	 	 	FilterInfo filter = new FilterInfo();
	 	 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
	 	  	view.setFilter(filter);
	 	 	
	 	    MaterialCollection materialColl =  MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
	 	    MaterialInfo material = materialColl.get(0); 
	 	    entryInfo.setMaterial(material);
	 	   
	 	    IObjectPK  pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
		    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
		    entryInfo.setUnit(unitInfo);
		    
//		    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
//		    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
		    entryInfo.setBaseUnit(unitInfo);
		    
		    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
		    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
		    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
		    entryInfo.setWarehouse(warehouseinfo);
		    
//		    BigDecimal baseQty = BigDecimal.ZERO;
		    BigDecimal qty = BigDecimal.ZERO;
		    BigDecimal amount = BigDecimal.ZERO;
	 
		    if(dvo.getFqty() !=null )
		    	qty = dvo.getFqty();	
		    if(dvo.getFamount() !=null )
		    	amount = dvo.getFamount(); 	
		    
		    entryInfo.setBaseQty(qty);
		    entryInfo.setQty(qty);
 		    entryInfo.setAssociateQty(qty); 
 		    
		    entryInfo.setAmount(amount);
		    entryInfo.setActualCost(amount);
		    entryInfo.setStandardCost(amount);
		    
		    entryInfo.put("MsgId", dvo.getId());
		    entryInfo.setBaseStatus(EntryBaseStatusEnum.ADD); 
		  return entryInfo;
	  }
  
}
