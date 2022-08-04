package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PurPlatSyncEnum;
import com.kingdee.eas.custom.app.dto.PurOrderDTO;
import com.kingdee.eas.custom.app.dto.PurOrderDetailDTO;
import com.kingdee.eas.custom.app.unit.PurPlatSyncBusLogUtil;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.scm.sd.sale.SaleOrder;
import com.kingdee.eas.scm.sd.sale.SaleOrderInfo;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.LowTimer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  
 * @author lei.ye
 *
 */
public class PurOrderSupport {
	
	
	//private LowTimer timer = new LowTimer();
	//private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.IncomeCostMatchFacadeControllerBean");
	
	public static String syncBill(Context ctx,String jsonStr){
		String result = null;
		if(jsonStr != null && !"".equals(jsonStr)){
		    System.out.println("************************json begin****************************");
		    System.out.println("#####################jsonStr################=" + jsonStr);
			DateBaseProcessType processType = DateBaseProcessType.AddNew;
			DateBasetype baseType = DateBasetype.B2B_GZ_LZ_PO;
			String msgId = "";
			String busCode ="";
			String reqTime ="";
			JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();  // json 转成对象
			JsonElement msgIdJE = returnData.get("msgId"); // 请求消息Id
			JsonElement busCodeJE = returnData.get("busCode"); // 业务类型类型
			JsonElement reqTimeJE = returnData.get("reqTime"); // 请求消息Id
			Gson gson = new Gson();
			JsonElement modelJE = returnData.get("data"); // 请求参数data
			if(msgIdJE !=null && msgIdJE.getAsString() !=null && !"".equals( msgIdJE.getAsString())&&
					busCodeJE !=null && busCodeJE.getAsString() !=null && !"".equals( busCodeJE.getAsString())&&
					reqTimeJE !=null && reqTimeJE.getAsString() !=null && !"".equals( reqTimeJE.getAsString())) {

				msgId = msgIdJE.getAsString() ;
				busCode = busCodeJE.getAsString() ;
				reqTime = reqTimeJE.getAsString() ;
				
			// 记录日志
		//	IObjectPK logPK = PurPlatSyncBusLogUtil.insertLog(ctx, processType, baseType, msgId, msgId+reqTime, jsonStr, "", "");
			PurOrderDTO m = gson.fromJson(modelJE, PurOrderDTO.class);
			// 判断msgId 是否存在
			if(!PurPlatUtil.judgeMsgIdExists(ctx, busCode, msgId)){
				result = judgeModel(ctx,m);
				if("".equals(result))
				{
					doInsertBill(ctx,m,busCode);
					result = "success";
				}
			
			}else
				result = PurPlatSyncEnum.EXISTS_BILL.getAlias();
			}else
				result = PurPlatSyncEnum.FIELD_NULL.getAlias();
		}else
			result = PurPlatSyncEnum.FIELD_NULL.getAlias();
	    
		return result;
	}
	
 
	/**
	 *  插入 eas表
	 * @param ctx
	 */
	private static void doInsertBill(Context ctx,PurOrderDTO m,String busCode){
		ExecutorService pool = Executors.newFixedThreadPool(6);
	    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
	    StringBuffer sbr = new StringBuffer(" /*dialect*/ insert into T_SM_PurOrder (FID,FCREATORID,FCREATETIME,FMODIFIERID,FMODIFICATIONTIME,FLASTUPDATEUSERID,FLASTUPDATETIME,FCONTROLUNITID," );
		sbr.append("FNUMBER,FBIZDATE,FHASEFFECTED,FAUDITORID,FAUDITTIME,FBASESTATUS,FBIZTYPEID,FBILLTYPEID,FYEAR,FPERIOD,"); 
		sbr.append("FPURCHASEORGUNITID,FPURCHASEPERSONID,FSUPPLIERID,FISDIRECTSEND,FPAYMENTTYPEID,FSETTLEMENTTYPEID,FCURRENCYID," );
		sbr.append("FEXCHANGERATE,FPREPAYMENTRATE,FPREPAYMENT,FPREPAID,FSUPPLIERCONFIRM,FINVOICEDAMOUNT,FPAIDAMOUNT,FISINNERSALE,FADMINORGUNITID," );
		sbr.append("FTOTALAMOUNT,FTOTALTAX,FTOTALTAXAMOUNT,FUNPREPAIDAMOUNT,FISSYSBILL,FCONVERTMODE,FLOCALTOTALAMOUNT,FLOCALTOTALTAXAMOUNT," );
		sbr.append("FSTORAGEORGUNITID,FISCENTRALBALANCE,FCOMPANYORGUNITID,FISINTAX,FISQUICKEN,FVERSION,FISPRICEINTAX,FISMATCHED,FISAPPROVEDMATERIAL,");
		sbr.append(	"FISNETORDER,FISRECALINNERPRICE,FPRICESOURCE,FMATCHINVAMOUNT,FINVOICEMATCH,FCHANGETYPE ) values ( ");
		String sId = BOSUuid.create("3171BFAD").toString();
		String userId = PurPlatUtil.getUserIdByPersonId(ctx, m.getFcreatorid());

		String bizTypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C"; // 普通采购
		String billTypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//采购订单
		String paymentTypeId = "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5";//赊购
		String settlementTypeId ="jbYAAAA0YHXpayuO" ;//对公付款
		String currencyId ="dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC" ;//人民币
		String rowType = "00000000-0000-0000-0000-0000000000017C7DC4A3";
		int isInTax = 0;//门诊为0，栗喆为1
		if(busCode.contains("LZ"))
			isInTax = 1;
		 else if(busCode.contains("MZ"))
			 isInTax = 0;
		//String bizDate = m.getFbizdate().
		String bizDateStr = m.getFbizdate();
		sbr.append("'").append(sId).append("','").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'");
		String ctrlOrgId = PurPlatUtil.getCtrlOrgId(ctx, "PUR", m.getFpurchaseorgunitid()); //控制单元
		sbr.append(ctrlOrgId).append("','").append(m.getFnumber()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),0,'");
		sbr.append(userId).append("',sysdate,4,'").append(bizTypeId).append("','").append(billTypeId).append("',").append(bizDateStr.substring(0, 4)).append(",").append(Integer.parseInt(bizDateStr.substring(5,7))).append(",'");
		sbr.append(m.getFpurchaseorgunitid()).append("','").append(m.getFpurchasepersonid()).append("','").append(m.getFsupplierid()).append("',0,'").append(paymentTypeId).append("','").append(settlementTypeId).append("','").append(currencyId).append("',");
		sbr.append("1,0,0,0,0,0,0,0,'").append(m.getFadminorgunitid()).append("',").append(m.getFtotalamount()).append(",").append(m.getFtotaltax()).append(",").append(m.getFtotaltaxamount()).append(",0,0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",'").append( m.getFpurchaseorgunitid()).append("',0,'").append(m.getFpurchaseorgunitid()).append("',").append(isInTax).append(",0,0,1,0,0,0,1,1,0,0,0 ) ");
		pe.getSqlList().add(sbr);
		
		for(PurOrderDetailDTO dvo : m.getDetails()){
			String eid  = BOSUuid.create("26041CC5").toString();
			StringBuffer sbr1 = new StringBuffer("/*dialect*/ insert into T_SM_PURORDERENTRY(FID,FSEQ,FMATERIALID,FUNITID,FBASESTATUS,FASSOCIATEQTY,FBASEUNITID,FREMARK,FISPRESENT,FDEMANDQTY,FQTY," +
					"FSTORAGEORGUNITID,FCOMPANYORGUNITID,FASSISTQTY,FPRICE,FDISCOUNTRATE,FACTUALPRICE,FTAXRATE,FTAXPRICE,FACTUALTAXPRICE," +
					"FAMOUNT,FLOCALAMOUNT,FTAX,FTAXAMOUNT,FDISCOUNTAMOUNT,FDELIVERYDATE,FRECEIVEOVERRATE,FRECEIVEOWINGRATE," +
					"FDELIVERADVANCEDAY,FDELIVERDEFERRALDAY,FBASEQTY,FTOTALRECEIVEQTY,FTOTALRECEIPTQTY,FTOTALRETURNEDQTY,FTOTALINVOICEDQTY," +
					"FTOTALINVOICEDAMOUNT,FTOTALPAIDAMOUNT,FTOTALEXPENSE,FPARENTID,FISQUANTITYUNCTRL,FISTIMEUNCTRL,FTOTALRECEIVEBASEQTY," +
					"FTOTALRECEIPTBASEQTY,FTOTALRETURNEDBASEQTY,FTOTALINVOICEDBASEQTY,FTOTALUNRETURNBASEQTY,FTOTALUNRECEIVEBASEQTY,FTOTALUNRECEIVEQTY,FISSUPINFO,FCURSEORDERQTY," +
					"FLOCALTAX,FLOCALTAXAMOUNT,FSALEORDERNUMBER,FPREPAIDAMOUNT,FREQUESTORGUNITID,FREQUESTCOMPANYORGUNITID,FISREQUESTTORECEIVED,FTOTALMOVEQTY,FTOTALINVOICEDAMT," +
					"FPREPAYMENT,FPRERECEIVED,FUNPRERECEIVEDAM,FVERSION,FCANINVMOVEQTY,FUNORDEREDQTY,FISBETWEENCOMPANYREC,FROWTYPEID,FDESTINATIONTYPE,FMATERIALNAME,FISREQCOMEQLRECCOM," +
					"FPLANRECEIVEQTY,FTOTALSUPPLYSTOCKQTY,FTOTALCANCELLEDSTOCKQTY,FTOTALSUPPLYSTOCKBASEQTY,FTOTALPREPAYAMT,FTOTALREQPAYAMT,FISREQPREPAYGTPREPAY,FNONUMMATERIALMODEL," +
					"FMATCHEDAMOUNT,FPURCHASEORGUNITID,FBIZDATE,FUSEADMINORGUNITID,CFPINPAI,CFHUOHAO,CFXINGHAO,CFWULIAOLEIBIE ) values (");
			
			String deliveDateStr = dvo.getFdeliverydate();
			Map<String,String> mmp = PurPlatUtil.getMaterialInfoByMId(ctx, dvo.getFmaterialid());
			int isPresent = 0;
			if(dvo.getFispresent())
				isPresent = 1;
			else
				isPresent = 0;
			sbr1.append("'").append(eid).append("',").append(dvo.getFseq()).append(",'").append(dvo.getFmaterialid()).append("','").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())).append("',4,");
			sbr1.append(dvo.getFqty()).append(",'").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())).append("','").append(dvo.getFremark()).append("',").append(isPresent).append(",0,").append(dvo.getFqty());
			sbr1.append(",'").append( m.getFpurchaseorgunitid()).append("','").append( m.getFpurchaseorgunitid()).append("',0,").append(dvo.getFprice()).append(",0,").append(dvo.getFactualprice()).append(",").append(dvo.getFtaxrate()).append(",");
			sbr1.append(dvo.getFtaxprice()).append(",").append(dvo.getFactualtaxprice()).append(",").append(dvo.getFamount()).append(",").append(dvo.getFamount()).append(",").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,");
			sbr1.append("to_date('").append(deliveDateStr).append("','yyyy-MM-dd'),0,0,0,0,").append(dvo.getFbaseqty()).append(",0,0,0,0,0,0,0,'").append(sId).append("',0,1,0,0,0,0,0,0,0,0,0,").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,0,'");
			sbr1.append(m.getFpurchaseorgunitid()).append("','").append(m.getFpurchaseorgunitid()).append("',1,0,0,0,0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFqty()).append(",0,'").append(rowType).append("',10,'");
			sbr1.append(mmp.get("name")).append("',1,0,0,0,0,0,0,0,'").append(mmp.get("gg")).append("',0,'").append(m.getFpurchaseorgunitid()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),'");
			sbr1.append(m.getFadminorgunitid()).append("','").append(mmp.get("pp")).append("','").append(mmp.get("hh")).append("','").append(mmp.get("xh")).append("','").append(mmp.get("gn")).append("') ");
			pe.getSqlList().add(sbr1);
		}
	  
		if(pe.getSqlList().size()>0)
			try {
				pe.executeUpdate(ctx); 
				pool.shutdown(); 
			} catch (EASBizException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			} catch (BOSException e) { 
				e.printStackTrace();
				pool.shutdown(); 
			}	
		     pool.shutdown(); 
		 
	}
	
	

	
	/**
	 * 校验 实体是否正确
	 * @param ctx
	 * @param m
	 * @return
	 */
	private static String judgeModel(Context ctx,PurOrderDTO m ){
		 String result = "";
		 //组织是否存在
		 if(m.getFpurchaseorgunitid() != null && !"".equals(m.getFpurchaseorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFpurchaseorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"采购组织不存在,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
			 
		 }else{
			 result = result +"采购组织不能为空,";
		 }
		 
		 if(m.getFnumber() !=null && !"".equals(m.getFnumber())){
			 // B2B单号存在是否需要判断
			 
		 }else
			 result = result +"单价编号不能为空,";
		 
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"业务日期不能为空,";
			
		 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
			 result = result +"供应商不能为空,";
		 else{
			if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
				if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFpurchaseorgunitid()  , m.getFsupplierid()))
					 result = result +"供应商未分配当前组织,";
				}else
					 result = result +"供应商不存在,";
		  }
			
			 if(m.getFtotaltaxamount() == null || m.getFtotaltax() == null || m.getFtotalamount() == null)
				 result = result +"价税合计、金额、税额 都不允许为空,";
			 else{
				 if(m.getFtotaltaxamount().compareTo( m.getFtotaltax().add(m.getFtotalamount() )) != 0)
					 result = result +"价税合计等于金额加税额的合计,";
		  }
			 
			if(m.getDetails() !=null && m.getDetails().size() > 0 ){	 
				 for(PurOrderDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"第"+j+1+"行物料ID不能为空,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFpurchaseorgunitid()  , dvo.getFmaterialid()))
								 result = result +"第"+j+1+"物料未分配当前组织,";
						 }else
							 result = result +"第"+j+1+"行 物料ID不存在,";
					 }
					 
					 if(dvo.getFunitid() ==null || "".equals(dvo.getFunitid()) ){
						 result = result +"第"+j+1+"行计量单位不能为空,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFunitid())) 
							 result = result +"第"+j+1+"行 计量单位"+dvo.getFunitid()+"不存在,";
					 }
					 
					 if(dvo.getFbaseunitid() ==null || "".equals(dvo.getFbaseunitid()) ){
						 result = result +"第"+j+1+"行基本计量单位不能为空,";
					 }else{
						 if(!PurPlatUtil.judgeExists(ctx, "UNIT", "",dvo.getFbaseunitid())) 
							 result = result +"第"+j+1+"行 基本计量单位"+dvo.getFbaseunitid()+"不存在,";
					 }
					 
					if(dvo.getFqty() ==null || dvo.getFbaseqty() == null){ 
						 result = result +"第"+j+1+"行 订货数量、基本数量不能为空,";
					}
					 
					if(dvo.getFprice()==null || dvo.getFactualprice() == null || dvo.getFtaxprice() == null || dvo.getFactualtaxprice() == null){ 
						 result = result +"第"+j+1+"行 单价、实际单价、含税单价、实际含税单价 不能为空,";
					}
					
					if(dvo.getFtaxrate() == null){ 
						 result = result +"第"+j+1+"行 税率不能为空,";
					}
					if(dvo.getFtax() == null){ 
						 result = result +"第"+j+1+"行 税额不能为空,";
					}
					 
					if(dvo.getFamount()== null){ 
						 result = result +"第"+j+1+"行 金额不能为空,";
					}
					
					if(dvo.getFtaxamount() == null){ 
						 result = result +"第"+j+1+"行 价税合计不能为空,";
					}
			 
					if(dvo.getFdeliverydate() == null){ 
						 result = result +"第"+j+1+"行 交货日期不能为空,";
					}
				 }
			} else 
				result = result +"至少有一条明细行的数据,";
			 
		 return result;
	}
	
}
