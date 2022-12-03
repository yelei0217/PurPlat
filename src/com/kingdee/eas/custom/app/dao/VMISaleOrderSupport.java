package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.MaterialCollection;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
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
import com.kingdee.eas.custom.PushRecordFactory;
import com.kingdee.eas.custom.PushRecordInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.PushStatusEnum;
import com.kingdee.eas.custom.app.dto.VMISaleOrderDTO;
import com.kingdee.eas.custom.app.dto.VMISaleOrderDetailDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDTO;
import com.kingdee.eas.custom.app.dto.base.BaseSCMDetailDTO;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.im.inv.IPurInWarehsBill;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueEntryInfo;
import com.kingdee.eas.scm.ws.app.importbill.ScmbillImportUtils;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class VMISaleOrderSupport {

	/**
	 *  插入 eas表
	 * @param ctx
	 */
	public static void doInsertBill(Context ctx,VMISaleOrderDTO m,String busCode,String reqStr){
		ExecutorService pool = Executors.newFixedThreadPool(6);
	    ParallelSqlExecutor pe = new ParallelSqlExecutor(pool); 
	    StringBuffer sbr = new StringBuffer("/*dialect*/insert into T_SD_SALEORDER (FID,FCREATORID,FCREATETIME,FMODIFIERID,FMODIFICATIONTIME,FLASTUPDATEUSERID,FLASTUPDATETIME," +
	    		" FCONTROLUNITID,FNUMBER,FBIZDATE,FDESCRIPTION,FHASEFFECTED,FAUDITORID,FAUDITTIME,FBASESTATUS,FBIZTYPEID,FBILLTYPEID," +
	    		" FYEAR,FPERIOD,FISINNERSALE,FORDERCUSTOMERID,FPURCHASEORGUNITID,FDELIVERYTYPEID,FTRANSLEADTIME,FCURRENCYID," +
	    		" FEXCHANGERATE,FPAYMENTTYPEID,FSETTLEMENTTYPEID,FPREPAYMENT,FPREPAYMENTRATE,FSALEORGUNITID,FSALEPERSONID," +
	    		" FTOTALAMOUNT,FTOTALTAX,FTOTALTAXAMOUNT,FPRERECEIVED,FUNPRERECEIVEDAMOUNT,FSENDADDRESS,FISSYSBILL,FCONVERTMODE,FLOCALTOTALAMOUNT," +
	    		" FLOCALTOTALTAXAMOUNT,FCOMPANYORGUNITID,FISINTAX,FVERSION,FOLDSTATUS,FISCENTRALBALANCE,FISREVERSE,FBEENPAIDPREPAYMENT," +
	    		" FISSQUAREBALANCE,FISMATCHEDPROMOTION,FISENTIRESINGLEDISCOUNT,FORIGINALDISCOUNTAMOUNT,CFMsgId ) values ( ");
		
	    String sId = BOSUuid.create("C48A423A").toString();
	    String userId = PurPlatUtil.getUserIdByPersonId(ctx, m.getFcreatorid());
		String bizTypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C"; // 普通销售
		String billTypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//销售订单
		String paymentTypeId = "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5";//赊销
		String customerId= "svL0ZnRPS86qelCx023QZ78MBA4="; //零售客户
		String settlementTypeId ="jbYAAAA0YHXpayuO" ;//对公付款
		String currencyId ="dfd38d11-00fd-1000-e000-1ebdc0a8100dDEB58FDC" ;//人民币
		
		String deliverTYpeId = "51eb893e-0105-1000-e000-0c00c0a8123362E9EE3F"; // 送货：SEND
		
		int isInTax = 0;//门诊为0，栗喆为1
//		if(busCode.contains("LZ"))
//			isInTax = 1;
//		 else if(busCode.contains("MZ"))
//			 isInTax = 0;
			 
		//String bizDate = m.getFbizdate().
		String bizDateStr = m.getFbizdate();
		sbr.append("'").append(sId).append("','").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'").append(userId).append("',sysdate,'");
		String ctrlOrgId = PurPlatUtil.getCtrlOrgId(ctx, "PUR", m.getFstorageorgunitid()); //控制单元
		sbr.append(ctrlOrgId).append("','").append(m.getFnumber()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),'").append(m.getFdescription()).append("',0,'");
		sbr.append(userId).append("',sysdate,4,'").append(bizTypeId).append("','").append(billTypeId).append("','").append(bizDateStr.substring(0, 4)).append("','").append(Integer.parseInt(bizDateStr.substring(5,7))).append("',0,'");
		sbr.append(customerId).append("','").append(m.getFstorageorgunitid()).append("','").append(deliverTYpeId).append("',0,'").append(currencyId).append("',1,'").append(paymentTypeId).append("','").append(settlementTypeId).append("'");
		sbr.append(",0,0,'").append(m.getFstorageorgunitid()).append("','jbYAAAAB7DOA733t',").append(m.getFtotalamount()).append(",").append(m.getFtotaltax()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",0,0,'").append(m.getFsendaddress()).append("',0,0,").append(m.getFtotalamount()).append(",").append(m.getFtotaltaxamount());
		sbr.append(",'").append( m.getFstorageorgunitid()).append("',").append(isInTax).append(",0,0,0,0,0,0,0,0,0,'").append(m.getId()).append("') ");
		pe.getSqlList().add(sbr);
		
		for(VMISaleOrderDetailDTO dvo :  m.getDetails()){
			String eid  = BOSUuid.create("88882A58").toString();
			StringBuffer sbr1 = new StringBuffer("/*dialect*/insert into T_SD_SALEORDERENTRY( FID,FSEQ,FMATERIALID,FUNITID,FBASESTATUS,FASSOCIATEQTY,FBASEUNITID,FREMARK,FISPRESENT," +
					" FBASEQTY,FQTY,FPRICE,FTAXPRICE,FDISCOUNTCONDITION,FDISCOUNTTYPE,FDISCOUNT,FDISCOUNTAMOUNT,FAMOUNT,FLOCALAMOUNT,FACTUALPRICE,FACTUALTAXPRICE," +
					" FTAXRATE,FTAX,FTAXAMOUNT,FSENDDATE,FDELIVERYDATE,FSTORAGEORGUNITID,FCOMPANYORGUNITID,FSENDOVERRATE,FSENDOWINGRATE,FSENDADVANCEDAY," +
					" FSENDDEFERRALDAY,FTOTALISSUEQTY,FTOTALRETURNEDQTY,FTOTALINVOICEDQTY,FTOTALSHIPPINGQTY,FTOTALRECEIVEDAMOUNT,FPARENTID,FTOTALISSUEBASEQTY," +
					" FTOTALREBASEQTY,FTOLINVOIDBASEQTY,FTOTALSHIPBASEQTY,FTOTALUNRETURNBASEQTY,FTOTALUNISSUEBASEQTY,FTOTALUNSHIPBASEQTY,FTOTALUNISSUEQTY," +
					" FISLOCKED,FLOCKQTY,FLOCKBASEQTY,FLOCALTAX,FLOCALTAXAMOUNT,FISBYSALEORDER,FORDEREDQTY,FUNORDEREDQTY,FPREPAYMENTRATE,FPREPAYMENT,FPRERECEIVED," +
					" FUNPRERECEIVEDAMOUNT,FQUANTITYUNCTRL,FTIMEUNCTRL,FDELIVERYDATEQTY,FTOTALINVOICEDAMT,FTOTALARAMOUNT,FVERSION,FISBETWEENCOMPANYSEND,FTOTALREVERSEDQTY," +
					" FTOTALREVERSEDBASEQTY,FPLANDELIVERYQTY,FTOTALCANCELLINGSTOCKQTY,FTOTALSUPPLYSTOCKQTY,FDELIVERYCUSTOMERID,FPAYMENTCUSTOMERID,FRECEIVECUSTOMERID," +
					" FARCLOSEDSTATUS,FTOTALPRODUCTQTY,FTOTALBASEPRODUCTQTY,FTOTALUNPRODUCTQTY,FTOTALBASEUNPRODUCTQTY,FMATCHEDAMOUNT,FLOCKASSISTQTY,FCHEAPRATE," +
					" FRETURNPLANDELIVERYQTY,FRETURNPLANDELIVERYBASEQTY,FSALEORGUNITID,FBIZDATE,FPRICESOURCETYPE,FISMRPCAL,FPROMOTIONSOURCEGROUP,FPROSTORAGEORGUNITID," +
					" FSUPPLYMODE,FTOTALTRANSFERQTY,FTOTALTRANSFERBASEQTY,FTOTALUNTRANSFERQTY,FTOTALUNTRANSFERBASEQTY,CFPINPAI,CFHUOHAO," +
					" FINVOICEREQQTY,FINVOICEREQBASEQTY,FUNINVOICEREQQTY,FUNINVOICEREQBASEQTY,FINVOICEREQAMOUNT,FINVOICEREQAMOUNTLOCAL,FUNINVOICEREQAMOUNT,FUNINVOICEREQAMOUNTLOCAL,CFMsgId");
		
 				   sbr1.append(",FWarehouseID,cflzsprice,cflzstaxprice,cflzsamount,cflzstaxrate,cflzstax,cflzstaxamount,cflzpprice,cflzptaxprice,cflzpamount,cflzptaxrate,cflzptax,cflzptaxamount,cffsupplierid");
			 
			 sbr1.append(") values (");
			String deliveDateStr =  dvo.getFdeliverydate();
			String sendDateStr =   dvo.getFsenddate();

			Map<String,String> mmp = PurPlatUtil.getMaterialInfoByMId(ctx, dvo.getFmaterialid());
			int isPresent = 0;
 			if(dvo.getFispresent() !=null && !"".equals(dvo.getFispresent())&& "1".equals(dvo.getFispresent()))
				isPresent = 1;
			else
				isPresent = 0;
 			
 			EntityViewInfo view = new EntityViewInfo();
		 	FilterInfo filter = new FilterInfo();
		 	filter.getFilterItems().add(new FilterItemInfo("number",dvo.getFmaterialid(),CompareType.EQUALS)); //
		  	view.setFilter(filter);
		  	
		    MaterialInfo material = null;
		    MaterialCollection materialColl =null ;
			try {
				materialColl = MaterialFactory.getLocalInstance(ctx).getMaterialCollection(view);
				material = materialColl.get(0);
			} catch (BOSException e) {
 				e.printStackTrace();
			}
			
			sbr1.append("'").append(eid).append("',").append(dvo.getFseq()).append(",'").append(material.getId().toString()).append("','").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFunitid())).append("',4,");
			sbr1.append(dvo.getFqty()).append(",'").append(PurPlatUtil.getMeasureUnitFIdByFNumber(ctx, dvo.getFbaseunitid())).append("','").append(dvo.getFsnno()).append("',").append(isPresent).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty());
			sbr1.append(",").append(dvo.getFprice()).append(",").append(dvo.getFtaxprice()).append(",-1,0,0,0,").append(dvo.getFamount()).append(",").append(dvo.getFamount()).append(",");
			sbr1.append(dvo.getFactualprice()).append(",").append(dvo.getFactualtaxprice()).append(",").append(dvo.getFtaxrate()).append(",").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",");
			sbr1.append("to_date('").append(sendDateStr).append("','yyyy-MM-dd')").append(",to_date('").append(deliveDateStr).append("','yyyy-MM-dd'),'").append(m.getFstorageorgunitid()).append("','").append(m.getFstorageorgunitid()).append("',0,0,0,0,0,0,0,0,0,'").append(sId).append("',0,0,0,0,");
			sbr1.append(dvo.getFqty()).append(",").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",").append(dvo.getFqty()).append(",0,0,0,").append(dvo.getFtax()).append(",").append(dvo.getFtaxamount()).append(",0,0,").append(dvo.getFqty());
			sbr1.append(",0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,'").append(customerId).append("','").append(customerId).append("','").append(customerId).append("',");
			sbr1.append("0,0,0,").append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,0,0,0,'").append(m.getFstorageorgunitid()).append("',to_date('").append(bizDateStr).append("','yyyy-MM-dd'),-1,0,0,'");
			sbr1.append(m.getFstorageorgunitid()).append("',0,0,0,0,0,'").append(mmp.get("pp")).append("','").append(mmp.get("hh")).append("',0,0,");
			sbr1.append(dvo.getFqty()).append(",").append(dvo.getFbaseqty()).append(",0,0,").append(dvo.getFtaxamount()).append(",").append(dvo.getFtaxamount()).append(",'").append(dvo.getId());
			sbr1.append("','").append(dvo.getFwarehouseid());
			sbr1.append("','").append(dvo.getFlzsprice());
			sbr1.append("','").append(dvo.getFlzstaxprice());
			sbr1.append("','").append(dvo.getFlzsamount());
			sbr1.append("','").append(dvo.getFlzstaxrate());
			sbr1.append("','").append(dvo.getFlzstax());
			sbr1.append("','").append(dvo.getFlzstaxamount());
			sbr1.append("','").append(dvo.getFlzpprice());
			sbr1.append("','").append(dvo.getFlzptaxprice());
			sbr1.append("','").append(dvo.getFlzpamount());
			sbr1.append("','").append(dvo.getFlzptaxrate());
			sbr1.append("','").append(dvo.getFlzptax());
			sbr1.append("','").append(dvo.getFlzptaxamount());
			sbr1.append("','").append(dvo.getFsupplierid());
			sbr1.append("' )");
			
			pe.getSqlList().add(sbr1);
		}
	  
		if(pe.getSqlList().size()>0)
			try {
				pe.executeUpdate(ctx); 
				pool.shutdown(); 
				DateBasetype dataseBaseType = DateBasetype.VMI_U_MZ_SO;
				PushRecordInfo rInfo = new PushRecordInfo();
				rInfo.setNumber(m.getId());
				rInfo.setName(sId);
				rInfo.setDescription(m.getFnumber());
				rInfo.setDateBaseType(dataseBaseType);
				rInfo.setProcessType(DateBaseProcessType.GSaleIss);
				rInfo.setPushStatus(PushStatusEnum.unDo);
				CtrlUnitInfo control = new CtrlUnitInfo();
				control.setId(BOSUuid.read(ctrlOrgId));
				rInfo.setCU(control);
				rInfo.setReq(reqStr);
				PushRecordFactory.getLocalInstance(ctx).addnew(rInfo); 
				
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
	public static String judgeModel(Context ctx,VMISaleOrderDTO m,String busCode ){
		 String result = "";
		 //组织是否存在
		 if(m.getFstorageorgunitid() != null && !"".equals(m.getFstorageorgunitid()) ){
			 IObjectPK orgPK = new  ObjectUuidPK(m.getFstorageorgunitid());
			try {
				if(!PurchaseOrgUnitFactory.getLocalInstance(ctx).exists(orgPK))
					result = result +"库存组织不存在,";
			} catch (EASBizException e) {
 				e.printStackTrace();
			} catch (BOSException e) {
 				e.printStackTrace();
			}
		 }else{
			 result = result +"库存组织不能为空,";
		 }
		 
		 if(m.getFnumber() ==null || "".equals(m.getFnumber()))
			 // B2B单号存在是否需要判断
			 result = result +"单价编号不能为空,";
		 
		 if(m.getFbizdate() == null || "".equals(m.getFbizdate()))
			 result = result +"业务日期不能为空,";
		
		 if(m.getFsupplierid() == null || "".equals(m.getFsupplierid()))
			 result = result +"供应商不能为空,";
			 else{
				if(PurPlatUtil.judgeExists(ctx, "S", "", m.getFsupplierid())){
					if(!PurPlatUtil.judgeExists(ctx, "SP",m.getFstorageorgunitid()  , m.getFsupplierid()))
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
				 for(VMISaleOrderDetailDTO dvo : m.getDetails()){
					 int j = 0 ; 
					 if(dvo.getFmaterialid() ==null || "".equals(dvo.getFmaterialid())){
						 result = result +"第"+j+1+"行物料ID不能为空,";
					 }else{
						 if(PurPlatUtil.judgeExists(ctx, "M", "",dvo.getFmaterialid())){
							 if(!PurPlatUtil.judgeExists(ctx, "MP",m.getFstorageorgunitid() , dvo.getFmaterialid()))
								 result = result +"第"+j+1+"物料未分配当前组织,";
						 }else
							 result = result +"第"+j+1+"行 物料ID不存在,";
					 }
					 
					 if(dvo.getFwarehouseid() ==null || "".equals(dvo.getFwarehouseid()))
						 result = result +"第"+j+1+"行仓库ID不能为空,";
					 else{
 						if(!PurPlatUtil.judgeExists(ctx, "Warehouse",m.getFstorageorgunitid(), dvo.getFwarehouseid()))
							 result = result +"第"+j+1+"行仓库ID不存在,";
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
					if(dvo.getFlzsprice() == null){  result = result +"第"+j+1+"行	栗喆销售单价,";}
					if(dvo.getFlzstaxprice() == null){  result = result +"第"+j+1+"行	栗喆销售含税单价,";}
					if(dvo.getFlzsamount() == null){  result = result +"第"+j+1+"行	栗喆销售金额,";}
					if(dvo.getFlzstaxrate() == null){  result = result +"第"+j+1+"行	栗喆销售税率,";}
					if(dvo.getFlzstax() == null){  result = result +"第"+j+1+"行	栗喆销售税额,";}
					if(dvo.getFlzstaxamount() == null){  result = result +"第"+j+1+"行	栗喆销售价税合计,";}
					if(dvo.getFlzpprice() == null){  result = result +"第"+j+1+"行	栗喆采购单价,";}
					if(dvo.getFlzptaxprice() == null){  result = result +"第"+j+1+"行	栗喆采购含税单价,";}
					if(dvo.getFlzpamount() == null){  result = result +"第"+j+1+"行	栗喆采购金额,";}
					if(dvo.getFlzptaxrate() == null){  result = result +"第"+j+1+"行	栗喆采购税率,";}
					if(dvo.getFlzptax() == null){  result = result +"第"+j+1+"行	栗喆采购税额,";}
					if(dvo.getFlzptaxamount() == null){  result = result +"第"+j+1+"行	栗喆采购价税合计,";}
				 }
			} else 
				result = result +"至少有一条明细行的数据,";
		 return result;
	}
	
	public static void doGengerBill(Context ctx, VMISaleOrderDTO m){
		try {
			IPurInWarehsBill ipbiz = PurInWarehsBillFactory.getLocalInstance(ctx);
			ISaleIssueBill isbiz =SaleIssueBillFactory.getLocalInstance(ctx);
			PurInWarehsBillInfo pinfo =null ;
			IObjectPK pk;
			SaleIssueBillInfo sinfo =null ;
			//门诊-- 入库
			 pinfo = createBillInfo(ctx, m,0);
			 pk = ipbiz.save(pinfo);
			 ipbiz.submit(pk.toString());
			//门诊-- 出库
			 sinfo = createSaleBillInfo(ctx,m,0);
			 pk = isbiz.save(sinfo);
			 isbiz.submit(pk.toString());
			
		  	 //栗喆-- 入库
			 pinfo = createBillInfo(ctx, m,1);
			 pk = ipbiz.save(pinfo);
			 ipbiz.submit(pk.toString());
			 //栗喆-- 出库
			 sinfo = createSaleBillInfo(ctx,m,1);
			 pk = isbiz.save(sinfo);
			 isbiz.submit(pk.toString());
			
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		}
	}
	
	
	
	private static PurInWarehsBillInfo createBillInfo(Context ctx, VMISaleOrderDTO m,int isCollpur)
    throws EASBizException, BOSException
  {
    PurInWarehsBillInfo info = new PurInWarehsBillInfo();
    String storageOrgId = m.getFstorageorgunitid();
    SupplierInfo supplierInfo = new SupplierInfo();
    String supplierId ="";
    if(isCollpur==1){
    	supplierId = m.getFsupplierid();
     	storageOrgId ="jbYAAAMU2SvM567U";
        info.put("iscollpur", Integer.valueOf(1));
    } else{
    	supplierId ="jbYAAAVlObc3xn38";
     	storageOrgId = m.getFstorageorgunitid();
        info.put("iscollpur", Integer.valueOf(0));
    }
	  supplierInfo.setId(BOSUuid.read(supplierId));
	  info.setSupplier(supplierInfo);
//    
//    if (isCollpur == 1)
//    {
//      supplierInfo.setId(BOSUuid.read("jbYAAAVlObc3xn38"));
//      info.setSupplier(supplierInfo);
//      info.put("iscollpur", Integer.valueOf(1));
//    }
//    else
//    {
//      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
//      info.setSupplier(supplierInfo);
//      info.put("iscollpur", Integer.valueOf(0));
//    }
    
    
    ObjectUuidPK orgPK = new ObjectUuidPK(storageOrgId);
    StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
    CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);

	String billtypeId = "50957179-0105-1000-e000-015fc0a812fd463ED552";//单据类型
	String sourceBilltypeId = "510b6503-0105-1000-e000-010bc0a812fd463ED552";//来源单据类型
	String biztypeId = "d8e80652-0106-1000-e000-04c5c0a812202407435C";//业务类型
	String transinfoId ="DawAAAAPoACwCNyn";//事务类型
	
    CtrlUnitInfo cuInfo = storageorginfo.getCU();
    info.setCU(cuInfo);
    info.setStorageOrgUnit(storageorginfo);
    info.setPurchaseType(PurchaseTypeEnum.PURCHASE); 
    
    BillTypeInfo billtype = new BillTypeInfo();
    billtype.setId(BOSUuid.read(billtypeId));
    info.setBillType(billtype);
    
    PurchaseOrgUnitInfo purchaseorginfo = new PurchaseOrgUnitInfo();
    purchaseorginfo.setId(BOSUuid.read(storageOrgId));
    
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
    
   //int isCollpur = 0;
 
    info.put("MsgId", m.getId());

    //info.put("factory", m.getFsupplierid());
     for (VMISaleOrderDetailDTO entry : m.getDetails())
    {
        PurInWarehsEntryInfo entryInfo = createEntryInfo(ctx,entry,isCollpur);
        entryInfo.setStorageOrgUnit(storageorginfo);
        entryInfo.setCompanyOrgUnit(xmcompany);
        entryInfo.setBizDate(info.getBizDate());
        entryInfo.setReceiveStorageOrgUnit(storageorginfo);
        entryInfo.setBalanceSupplier(supplierInfo);
        entryInfo.setPurchaseOrgUnit(purchaseorginfo);
        
//        if(sourceBilltypeId!=null && !"".equals(sourceBilltypeId)){
//            Map<String,String> orderEmp = PurPlatUtil.getOrderEntryMapByMsgId(ctx,m.getFstorageorgunitid(),entry.getFsourcebillentryid(),"P");
//            if(orderEmp !=null && orderEmp.size() > 0){
//            	entryInfo.setSourceBillEntryId(orderEmp.get("id"));
//            	entryInfo.setSourceBillEntrySeq(Integer.parseInt(orderEmp.get("seq")));
//            }
//            
//            Map<String,String> ordermp = PurPlatUtil.getOrderMapByNumber(ctx,m.getFstorageorgunitid(),entry.getFsourcebillnumber(),"P");
//            if(ordermp !=null && orderEmp.size() > 0){
//                entryInfo.setSourceBillId(ordermp.get("id"));
//                entryInfo.setSourceBillNumber(ordermp.get("number"));
//            }
//        	  entryInfo.setSourceBillType(sourceBillTypeInfo);
//        }
        
        totalAmount = totalAmount.add(entry.getFamount());
        info.getEntries().addObject(entryInfo);
       
    }
   // info.setTotalQty(m.getFtotalqty());
 	info.setTotalAmount(totalAmount);
	info.setTotalLocalAmount(totalAmount);
    
    return info;
  }
  
  private static PurInWarehsEntryInfo createEntryInfo(Context ctx, VMISaleOrderDetailDTO dvo,int isCollpur)
    throws BOSException, EASBizException
  {
    PurInWarehsEntryInfo entryInfo = new PurInWarehsEntryInfo();
//    MaterialInfo material =  null;
//    IMaterial imaterial = MaterialFactory.getLocalInstance(ctx);
//    IObjectPK pk = new ObjectUuidPK(BOSUuid.read(dvo.getFmaterialid()));
//    material = imaterial.getMaterialInfo(pk);
    
    String warehouseId = dvo.getFwarehouseid();
    BigDecimal qty = dvo.getFqty();
    BigDecimal baseQty = dvo.getFbaseqty();
    BigDecimal price = BigDecimal.ZERO; 
    BigDecimal taxprice = BigDecimal.ZERO; 
    BigDecimal taxrate = BigDecimal.ZERO; 
    BigDecimal amount = BigDecimal.ZERO; 
    BigDecimal tax = BigDecimal.ZERO; 
    BigDecimal taxamount = BigDecimal.ZERO; 
    
    if(isCollpur==1){ 
    	warehouseId = "jbYAAAac+li76fiu";
    	price = dvo.getFlzpprice(); 
    	taxprice = dvo.getFlzptaxprice(); 
    	taxrate = dvo.getFlzptaxrate(); 
    	amount = dvo.getFlzpamount(); 
    	tax =  dvo.getFlzptax(); 
    	taxamount = dvo.getFlzptaxamount(); 
    }else{ 	
    	warehouseId = dvo.getFwarehouseid();
    	price = dvo.getFprice(); 
    	taxprice = dvo.getFtaxprice(); 
    	taxrate = dvo.getFtaxrate(); 
    	amount = dvo.getFamount(); 
    	tax =  dvo.getFtax(); 
    	taxamount = dvo.getFtaxamount(); 
    }
  
    
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
    
    pk = new ObjectUuidPK(BOSUuid.read(warehouseId));
    IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
    WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
    entryInfo.setWarehouse(warehouseinfo);
    
    String invUpdateTypeId = "8r0AAAAEaOjC73rf";
    InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
    invUpdateType.setId(BOSUuid.read(invUpdateTypeId));
    entryInfo.setInvUpdateType(invUpdateType);
    
    entryInfo.setMaterial(material);
    entryInfo.setBaseUnit(baseUnitInfo);
    entryInfo.setUnit(unitInfo);
    entryInfo.setQty(qty);
    entryInfo.setBaseQty(baseQty);
    entryInfo.setAssociateQty(BigDecimal.ZERO);
    entryInfo.setWrittenOffQty(price);
    entryInfo.setWrittenOffBaseQty(baseQty);
    entryInfo.setUnWriteOffQty(qty);
    entryInfo.setUnWriteOffBaseQty(baseQty);
    entryInfo.setUnVmiSettleBaseQty(baseQty);
    entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqBaseQty(BigDecimal.ZERO);
    entryInfo.setCanDirectReqQty(BigDecimal.ZERO);
    entryInfo.setAssistQty(BigDecimal.ZERO);
    entryInfo.put("MsgId", dvo.getId());
    //entryInfo.setStandardCost(BigDecimal.ZERO);
   
	entryInfo.setTaxRate(taxrate);
	entryInfo.setTax(tax);
	entryInfo.setLocalTax(tax);
    entryInfo.setAmount(amount);
	entryInfo.setLocalAmount(amount);
	    entryInfo.setWrittenOffAmount(amount);
	    entryInfo.setTaxPrice(taxprice);
	    entryInfo.setPrice(price);
	    entryInfo.setActualPrice(taxprice);
	    entryInfo.setActualTaxPrice(taxprice);
	    entryInfo.setTaxAmount(taxamount);
	    entryInfo.setLocalTaxAmount(taxamount);
	    entryInfo.setUnWriteOffAmount(amount);
	    entryInfo.setUnitStandardCost(price);
	    entryInfo.setStandardCost(amount);
	    entryInfo.setUnitActualCost(price);
	    entryInfo.setActualCost(amount);
	    entryInfo.setUnitPurchaseCost(price);
	    entryInfo.setPurchaseCost(amount);

    entryInfo.put("huohao", material.get("huohao"));
    entryInfo.put("pinpai", material.get("pinpai"));
 
    return entryInfo;
  }
  
  private static SaleIssueBillInfo createSaleBillInfo(Context ctx, VMISaleOrderDTO m,int isCollpur )
  throws BOSException, EASBizException
{
	    String storageOrgId = m.getFstorageorgunitid();
	    String customerId ="";
	    if(isCollpur==1){
	    	storageOrgId ="jbYAAAMU2SvM567U";
	    	customerId = getInnerCustIdByComId(ctx,m.getFstorageorgunitid());
	    }
	    else{
	    	 customerId= "svL0ZnRPS86qelCx023QZ78MBA4="; //零售客户
	    	storageOrgId = m.getFstorageorgunitid();
	    }
	    	 
	    
	    CustomerInfo customerInfo = new CustomerInfo();
	    customerInfo.setId(BOSUuid.read(customerId));
 
  SaleIssueBillInfo info = new SaleIssueBillInfo();
  ObjectUuidPK orgPK = new ObjectUuidPK(storageOrgId);
  
  StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
  CompanyOrgUnitInfo xmcompany = ScmbillImportUtils.getCompanyInfo(ctx, storageorginfo, 4);
  CtrlUnitInfo cuInfo = storageorginfo.getCU();
  info.setCU(cuInfo);
  
  info.setStorageOrgUnit(storageorginfo);
  info.setIsSysBill(false);
  info.setConvertMode(ConvertModeEnum.DIRECTEXCHANGERATE);
  
  String billtypeId = "50957179-0105-1000-e000-015bc0a812fd463ED552";//单据类型
  String sourceBilltypeId = "510b6503-0105-1000-e000-0113c0a812fd463ED552";//来源单据类型
  String biztypeId = "d8e80652-010e-1000-e000-04c5c0a812202407435C";//业务类型
  String transinfoId ="DawAAAAPoAywCNyn";//事务类型
	 
  BillTypeInfo billtype = new BillTypeInfo();
  billtype.setId(BOSUuid.read(billtypeId));
  info.setBillType(billtype);
  
  SaleOrgUnitInfo saleOrgInfo = new SaleOrgUnitInfo();
  saleOrgInfo.setId(BOSUuid.read(storageOrgId));
  
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
  

  
  BizTypeInfo bizTypeinfo = new BizTypeInfo();
  bizTypeinfo.setId(BOSUuid.read(biztypeId)); //普通销售
  info.setBizType(bizTypeinfo);
  
  TransactionTypeInfo transinfo = new TransactionTypeInfo();
  transinfo.setId(BOSUuid.read(transinfoId));//普通销售出库
  info.setTransactionType(transinfo);
  
  info.put("MsgId", m.getId());
  info.setCustomer(customerInfo);
  
  
  BillTypeInfo sourceBillTypeInfo = null ;
  if(sourceBilltypeId != null && !"".equals(sourceBilltypeId)){
	    sourceBillTypeInfo = new BillTypeInfo();
	    sourceBillTypeInfo.setId(BOSUuid.read(sourceBilltypeId));
  }

//  SupplierInfo supplierInfo =null;
//  if(m.getFsupplierid() != null && !"".equals(m.getFsupplierid())){
//  	supplierInfo = new SupplierInfo();
//      supplierInfo.setId(BOSUuid.read(m.getFsupplierid()));
//  }
  
 // BigDecimal totalAmount = new BigDecimal(0);
  for (VMISaleOrderDetailDTO entry : m.getDetails())
  {
      SaleIssueEntryInfo entryInfo = createSaleEntryInfo(ctx,entry,isCollpur);
      entryInfo.setStorageOrgUnit(storageorginfo);
      entryInfo.setCompanyOrgUnit(xmcompany);
      entryInfo.setBizDate(info.getBizDate());
      entryInfo.setBalanceCustomer(customerInfo);
      entryInfo.setSaleOrgUnit(saleOrgInfo);
//  	  if(supplierInfo !=null)
//	    	entryInfo.setSupplier(supplierInfo);
	    
      info.getEntries().addObject(entryInfo);
    }

  return info;
}

private static SaleIssueEntryInfo createSaleEntryInfo(Context ctx, VMISaleOrderDetailDTO dvo ,int isCollpur)
  throws EASBizException, BOSException
{
    SaleIssueEntryInfo entryInfo = new SaleIssueEntryInfo();
    String warehouseId = dvo.getFwarehouseid();
    BigDecimal qty = dvo.getFqty();
    BigDecimal baseQty = dvo.getFbaseqty();
    BigDecimal price = BigDecimal.ZERO; 
    BigDecimal taxprice = BigDecimal.ZERO; 
    BigDecimal taxrate = BigDecimal.ZERO; 
    BigDecimal amount = BigDecimal.ZERO; 
    BigDecimal tax = BigDecimal.ZERO; 
    BigDecimal taxamount = BigDecimal.ZERO; 
     if(isCollpur==1) {
    	warehouseId = "jbYAAAac+li76fiu";
    	price = dvo.getFlzsprice();
     	taxprice = dvo.getFlzstaxprice(); 
    	taxrate = dvo.getFlzstaxrate(); 
    	amount = dvo.getFlzsamount(); 
    	tax =  dvo.getFlzstax(); 
    	taxamount = dvo.getFlzstaxamount(); 
    }else {	
     	warehouseId = dvo.getFwarehouseid();
    	price = dvo.getFprice(); 
    	taxprice = dvo.getFtaxprice(); 
    	taxrate = dvo.getFtaxrate(); 
    	amount = dvo.getFamount(); 
    	tax =  dvo.getFtax(); 
    	taxamount = dvo.getFtaxamount(); 
    }
   
  
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
  
  pk = new ObjectUuidPK(BOSUuid.read(warehouseId));
  IWarehouse iwarehouse = WarehouseFactory.getLocalInstance(ctx);
  WarehouseInfo warehouseinfo = iwarehouse.getWarehouseInfo(pk);
  entryInfo.setWarehouse(warehouseinfo);
  
  String invUpdateTypeId = "8r0AAAAEaOnC73rf";
 
  InvUpdateTypeInfo invUpdateType = new InvUpdateTypeInfo();
  invUpdateType.setId(BOSUuid.read(invUpdateTypeId));
  entryInfo.setInvUpdateType(invUpdateType);
  entryInfo.setMaterial(material);
  entryInfo.setBaseUnit(baseUnitInfo);
  entryInfo.setUnit(unitInfo);
  entryInfo.setQty(qty);
  entryInfo.setBaseQty(baseQty);
  entryInfo.setAssociateQty(BigDecimal.ZERO);
  entryInfo.setWrittenOffQty(qty);
  entryInfo.setWrittenOffBaseQty(baseQty);
  entryInfo.setUnWriteOffQty(qty);
  entryInfo.setUnWriteOffBaseQty(baseQty);
  entryInfo.setUnSettleQty(qty);
  entryInfo.setUnSettleBaseQty(baseQty);
  entryInfo.setUnVmiSettleBaseQty(qty);
  entryInfo.setUnReturnedBaseQty(BigDecimal.ZERO);
  entryInfo.setAssistQty(BigDecimal.ZERO);
 
  entryInfo.setTaxRate(taxrate);
  entryInfo.setTax(tax);
 	    entryInfo.setLocalTax(tax);
 	    entryInfo.setAmount(amount);
	    entryInfo.setLocalAmount(amount);
	    entryInfo.setWrittenOffAmount(amount);
	    entryInfo.setTaxPrice(taxprice);
	    entryInfo.setPrice(price);
	    entryInfo.setActualPrice(taxprice);
	    entryInfo.setUnWriteOffAmount(amount);
	    entryInfo.setUnitStandardCost(price);
	    entryInfo.setStandardCost(amount);    
	    entryInfo.setUnitActualCost(price);
	    entryInfo.setActualCost(amount);
	    
	    entryInfo.put("huohao", material.get("huohao"));
  entryInfo.put("pinpai", material.get("pinpai"));
  entryInfo.put("MsgId", dvo.getId());
  return entryInfo;
} 


private static String getInnerCustIdByComId(Context ctx,String companyId){
	String fid ="";
	if(VerifyUtil.notNull(companyId) ){
		String	 sql = " select FID from t_bd_customer where FINTERNALCOMPANYID = '"+companyId+"' and FIsInternalCompany=1 and  FUsedStatus =1";
		     try {
		         IRowSet rs = DbUtil.executeQuery(ctx, sql);
		         if (rs.next() &&  rs.getObject("FID") != null && !"".equals(rs.getObject("FID").toString())) 
		        	 fid = rs.getObject("FID").toString();
		       }
		       catch (BOSException e) {
		         e.printStackTrace();
		       } catch (SQLException e) {
		         e.printStackTrace();
		       } 
	}
	
	return fid ;
}
}
