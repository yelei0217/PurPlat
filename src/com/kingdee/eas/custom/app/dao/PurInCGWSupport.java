package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
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
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.im.inv.IPurInWarehsBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;

public class PurInCGWSupport {
	
	public static String syncBill(Context ctx,String jsonStr){
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
			DateBasetype baseType = DateBasetype.ZZ_YC_MZ_PI_C;

			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json ת�ɶ���
			JsonElement msgIdJE = returnData.get("msgId"); // ������ϢId
			JsonElement busCodeJE = returnData.get("busCode"); // ҵ����������
			JsonElement reqTimeJE = returnData.get("reqTime"); // ������ϢId
 			JsonElement modelJE = returnData.get("data"); // �������data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
  				//baseType = DateBasetype.getEnum(PurPlatUtil.dateTypeMenuMp.get(busCode));
				// ��¼��־
//				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), jsonStr, "", "");
				PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+PurPlatUtil.getCurrentTimeStrS(), "", "", "");
				PurInDTO m =null;
				try {
					m = gson.fromJson(modelJE, PurInDTO.class);
				} catch (JsonSyntaxException e) {
					purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
 					e.printStackTrace();
				}
				if(m !=null){
					// �ж�msgId �Ƿ����SaleOrderDTO
					if(!PurPlatUtil.judgeMsgIdExists(ctx,baseType.getValue(), busCode, msgId)){
						result = judgeModel(ctx,m,busCode);
						if("".equals(result))
						{
							doSaveBill(ctx,m,busCode);
 							purPlatMenu = PurPlatSyncEnum.SUCCESS;	
						}else
							purPlatMenu = PurPlatSyncEnum.EXCEPTION_SERVER;	
					}else
						purPlatMenu = PurPlatSyncEnum.EXISTS_BILL;
				}else
					purPlatMenu = PurPlatSyncEnum.JSON_ERROR;
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
	
	private static void doSaveBill(Context ctx,PurInDTO m,String busCode){
			try {
				PurInWarehsBillInfo info = createBillInfo(ctx, m,busCode);
				IPurInWarehsBill ibiz = PurInWarehsBillFactory.getLocalInstance(ctx);
				IObjectPK pk = ibiz.save(info);
				ibiz.submit(pk.toString());
			} catch (EASBizException e) {
	 		e.printStackTrace();
		} catch (BOSException e) {
			e.printStackTrace();
		}
	}
	
	
	private static PurInWarehsBillInfo createBillInfo(Context ctx, PurInDTO m,String busCode)
    throws EASBizException, BOSException
  {
    PurInWarehsBillInfo info = new PurInWarehsBillInfo();
    ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);

	String billtypeId = "";//��������
	String sourceBilltypeId = "";//��Դ��������
	String biztypeId = "";//ҵ������
	String transinfoId ="";//��������

    BigDecimal factor = new BigDecimal(1);
    billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//��������
	 sourceBilltypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//��Դ��������
	 biztypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C";//ҵ������
	 transinfoId ="DawAAAAPoACwCNyn";//��������
	 factor = new BigDecimal(1);
	
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
    
//    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
//    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
//      info.setStocker(person);
//    }
    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFdoctor());
	if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
		info.setStocker(person);
	}
    info.put("yisheng", person);
    
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
    for (PurInDetailDTO entry : m.getDetails())
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
  
  private static PurInWarehsEntryInfo createEntryInfo(Context ctx, PurInDetailDTO dvo,String busCode)
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
	invUpdateTypeId = "8r0AAAAEaOjC73rf";
	factor = new BigDecimal(1);
      
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

	    entryInfo.put("huohao", material.get("huohao"));
	    entryInfo.put("pinpai", material.get("pinpai"));
	    entryInfo.put("huanzheID", dvo.getFpatientid());
	    entryInfo.put("huanzhemingcheng", dvo.getFpatientname());
    
    return entryInfo;
  }

  	/**
	 * У�� ʵ���Ƿ���ȷ
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,PurInDTO m,String busCode ){
		 String result = "";
		 //��֯�Ƿ����
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"�����֯������,";
			} catch (EASBizException e) {
				e.printStackTrace();
			} catch (BOSException e) {
				e.printStackTrace();
			}
		 }else{
			 result = result +"�����֯����Ϊ��,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B���Ŵ����Ƿ���Ҫ�ж�
			 result = result +"���۱�Ų���Ϊ��,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"ҵ�����ڲ���Ϊ��,";
		
		  
		 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
				 result = result +"��Ӧ�̲���Ϊ��,";
				 else{
					if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
						if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
							 result = result +"��Ӧ��δ���䵱ǰ��֯,";
						}else
							 result = result +"��Ӧ�̲�����,";
		  }	
	 
		 
			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
				 result = result +"��˰�ϼơ���˰�� ��������Ϊ��,";
			 else{
				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
					 result = result +"��˰�ϼƵ��ڽ���˰��ĺϼ�,";
		  }
			 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(PurInDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"��"+j+1+"������ID����Ϊ��,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid()  , dvo.getFmaterialid()))
								 result = result +"��"+j+1+"����δ���䵱ǰ��֯,";
						 }else
							 result = result +"��"+j+1+"�� ����ID������,";
					 }
					 
					 if("CGZ_U_MZ_SO".equals(busCode) || busCode.contains("SS")){
						 if(dvo.getFwarehouseid() ==null || "".equals(dvo.getFwarehouseid()))
							 result = result +"��"+j+1+"�вֿ�ID����Ϊ��,";
						 else{
	 						if(!PurPlatUtil.judgeExists(ctx, "Warehouse",m.getFstorageorgunitid(), dvo.getFwarehouseid()))
								 result = result +"��"+j+1+"�вֿ�ID������,";
						 } 
					 }
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"��"+j+1+"�м�����λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"��"+j+1+"�� ������λ"+dvo.getFunitid()+"������,";
					 }
					 
					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
						 result = result +"��"+j+1+"�л���������λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
							 result = result +"��"+j+1+"�� ����������λ"+dvo.getFbaseunitid()+"������,";
					 }
					 
					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
						 result = result +"��"+j+1+"�� ����������������������Ϊ��,";
					}
					 
					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
						 result = result +"��"+j+1+"�� ���ۡ�ʵ�ʵ��ۡ���˰���ۡ�ʵ�ʺ�˰���� ����Ϊ��,";
					}
					
					if(dvo.getFtaxrate() == null){ 
						 result = result +"��"+j+1+"�� ˰�ʲ���Ϊ��,";
					}
					if(dvo.getFtax() == null){ 
						 result = result +"��"+j+1+"�� ˰���Ϊ��,";
					}
					 
					if(dvo.getFamount()== null){ 
						 result = result +"��"+j+1+"�� ����Ϊ��,";
					}
					
					if(dvo.getFtaxamount() == null){ 
						 result = result +"��"+j+1+"�� ��˰�ϼƲ���Ϊ��,";
					}
					
//					  if(dvo.getFdeliverydate() == null){ 
//							 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
//						}
					  
//				   if(busCode.contains("_SO") || busCode.contains("_SS") )	{
//						if(dvo.getFsenddate() == null){ 
//							 result = result +"��"+j+1+"�� �������ڲ���Ϊ��,";
//						} 
//				   }
				 }
			} else 
				result = result +"������һ����ϸ�е�����,";
		 return result;
	}
	
  
}
