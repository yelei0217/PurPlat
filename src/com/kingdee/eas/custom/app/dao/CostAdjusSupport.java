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
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
import com.kingdee.eas.basedata.master.material.IMaterial;
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
import com.kingdee.eas.custom.app.dto.CostAdjusDTO;
import com.kingdee.eas.custom.app.dto.CostAdjusDetailDTO;
import com.kingdee.eas.custom.app.dto.PurInDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.scm.cal.CostAdjustBillEntryInfo;
import com.kingdee.eas.scm.cal.CostAdjustBillInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;

public class CostAdjusSupport {

	
	
	public static String doSync(Context ctx,String jsonStr){
		String result = null;
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.GZ_CK_LZ_CJ;
			String msgId = "";
			String busCode ="";
			String reqTime ="";
			
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json ת�ɶ���
			JsonElement msgIdJE = returnData.get("msgId"); // ������ϢId
			JsonElement busCodeJE = returnData.get("busCode"); // ҵ����������
			JsonElement reqTimeJE = returnData.get("reqTime"); // ������ϢId
			Gson gson = new Gson();
			JsonElement modelJE = returnData.get("data"); // �������data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {
				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
				IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
				CostAdjusDTO m = gson.fromJson(modelJE, CostAdjusDTO.class);
				if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
					result = judgeModel(ctx,m,busCode);
					if("".equals(result))
					{
						
					}
				}
			}
			
		}
		return result;
	}
	private static String judgeModel(Context ctx,CostAdjusDTO m,String busCode ){
		String result = "";
		 //��֯�Ƿ����
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!StorageOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
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
			 result = result +"���ݱ�Ų���Ϊ��,";
		 
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"ҵ�����ڲ���Ϊ��,";
		 
		 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
			 result = result +"��˰�ϼơ���˰�� ��������Ϊ��,";
		 else{
			 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
				 result = result +"��˰�ϼƵ��ڽ���˰��ĺϼ�,";
		 }
		 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(CostAdjusDetailDTO dvo : m.getDetails()){
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
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"��"+j+1+"�м�����λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"��"+j+1+"�м�����λ"+dvo.getFunitid()+"������,";
					 }
					 
					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
						 result = result +"��"+j+1+"�л���������λ����Ϊ��,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
							 result = result +"��"+j+1+"�л���������λ"+dvo.getFbaseunitid()+"������,";
					 }
				 }
			}else					
				result = result +"������һ����ϸ�е�����,";
 
		 
		return result ;
	}
	
	private static CostAdjustBillInfo createCostAdjusInfo(Context ctx, CostAdjusDTO m,String busCode)
    throws EASBizException, BOSException
  {
		CostAdjustBillInfo info = new CostAdjustBillInfo();
		  ObjectUuidPK orgPK = new ObjectUuidPK(m.getFstorageorgunitid());
		  StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
		  CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
		  
			String billtypeId = "";//��������
			String sourceBilltypeId = "";//��Դ��������
			String biztypeId = "";//ҵ������
			String transinfoId ="";//��������
			
			String storeTypeId = "181875d5-0105-1000-e000-0111c0a812fd97D461A6"; // �������
			String storeStatusId = "181875d5-0105-1000-e000-012ec0a812fd62A73FA5"; // ���״̬

			
			StoreTypeInfo storeTypeInfo = new StoreTypeInfo();
			storeTypeInfo.setId(BOSUuid.read(storeTypeId));
			
			StoreStateInfo storeStatusInfo = new StoreStateInfo();
			storeStatusInfo.setId(BOSUuid.read(storeStatusId));
			
			 CtrlUnitInfo cuInfo = storageorginfo.getCU();
			    info.setCU(cuInfo);
			    info.setStorageOrgUnit(storageorginfo);
			    
			    
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
				
				
//			    CurrencyInfo currency = new CurrencyInfo();
//			    currency.setId(BOSUuid.read("dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC"));
//			    info.setCurrency(currency);
		 
			    
//			    PersonInfo person = AppUnit.getNullPersonInfoByHisID(ctx, m.getFstockerid());
//			    if ((person != null) && (VerifyUtil.notNull(person.getId().toString()))) {
//			      info.setStocker(person);
//			    }
			    
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
		
		return info;
  	}
	
	  private static CostAdjustBillEntryInfo createCostEntryInfo(Context ctx, CostAdjusDetailDTO dvo,String busCode )
	    throws BOSException, EASBizException
	  {
		  CostAdjustBillEntryInfo entryInfo = new CostAdjustBillEntryInfo();

		  IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);

		    MaterialInfo material = null;
		    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
		    material = imaterial.getMaterialInfo(pk);
		    entryInfo.setMaterial(material);
		    
		    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
		    MeasureUnitInfo unitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
		    entryInfo.setUnit(unitInfo);
		    
		    pk = new ObjectUuidPK(BOSUuid.read(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())));
		    MeasureUnitInfo baseUnitInfo = MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitInfo(pk);
		    entryInfo.setBaseUnit(baseUnitInfo);
		    
		    pk = new ObjectUuidPK(BOSUuid.read(dvo.getFwarehouseid()));
		    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
		    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
		    entryInfo.setWarehouse(warehouseinfo);
		    
		    entryInfo.setBaseQty(dvo.getFbaseqty());
		    entryInfo.setQty(dvo.getFqty());
		    entryInfo.setBaseQty(dvo.getFbaseqty());
		    entryInfo.setAssociateQty(BigDecimal.ZERO);
		    entryInfo.setAmount(dvo.getFamount());
		    entryInfo.setActualCost(dvo.getFamount());
 		    
		  return entryInfo;
	  }
  
}
