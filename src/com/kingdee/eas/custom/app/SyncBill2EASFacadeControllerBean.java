package com.kingdee.eas.custom.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.dao.CostAdjusSupport;
import com.kingdee.eas.custom.app.dao.PurInCGWSupport;
import com.kingdee.eas.custom.app.dao.PurInWarehsSupport;
import com.kingdee.eas.custom.app.dao.PurOrderSupport;
import com.kingdee.eas.custom.app.dao.SaleIssueSupport;
import com.kingdee.eas.custom.app.dao.SaleOrderSupport;
import com.kingdee.eas.custom.app.dao.base.BaseFISupport;
import com.kingdee.eas.custom.app.dao.base.BaseSCMSupport;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;
import com.kingdee.eas.framework.CoreBillBaseCollection;
import com.kingdee.eas.scm.im.inv.ISaleIssueBill;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillFactory;
import com.kingdee.eas.scm.im.inv.PurInWarehsBillInfo;
import com.kingdee.eas.scm.im.inv.PurInWarehsEntryInfo;
import com.kingdee.eas.scm.im.inv.SaleIssueBillFactory;
import com.kingdee.eas.scm.im.inv.SaleIssueBillInfo;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.LowTimer;

public class SyncBill2EASFacadeControllerBean extends AbstractSyncBill2EASFacadeControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2240878356734475478L;
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.SyncBill2EASFacadeControllerBean");
	private LowTimer timer = new LowTimer();
	
	
	@Override
	protected String _savePurOrder(Context ctx, String jsonStr)	throws BOSException {
		this.timer.reset(); 
//		 String res =  PurOrderSupport.syncBill(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do savePurOrder method cost :" + this.timer.msValue());
		 return res;
	}


	@Override
	protected String _saveSaleOrder(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		// String res =  SaleOrderSupport.syncBill(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saveSaleOrder method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _purOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  PurOrderSupport.doCloseRow(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _purOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saleOrderCloseRow(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		 String res =  SaleOrderSupport.doCloseRow(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _savePurInWare(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
		String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do _saleOrderCloseRow doCloseRow method cost :" + this.timer.msValue());
		return res;	
	}


	@Override
	protected String _saveSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		this.timer.reset(); 
//		 String res =  SaleIssueSupport.doSync(ctx, jsonStr);
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean saveSaleIss method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _saveApOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseFISupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do BaseFISupport _saveApOtherBill method cost :" + this.timer.msValue());
		return res;	
	}


	@Override
	protected String _saveArOtherBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		 logger.info("do BaseFISupport _saveArOtherBill method cost :" + this.timer.msValue());
		return res;		
	}


	@Override
	protected String _saveCostAdjus(Context ctx, String jsonStr)
			throws BOSException {
		 this.timer.reset(); 
		 String res =  CostAdjusSupport.doSync(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do _saveCostAdjus _saveCostAdjus method cost :" + this.timer.msValue());
		 return res;	
	}


	@Override
	protected String _savePaymentBill(Context ctx, String jsonStr)
			throws BOSException {
 		//return super._savePaymentBill(ctx, jsonStr);
		 String  result ="_savePaymentBill";
        try {
			HttpClient httpClient = new HTTPSTrustClient().init();
			result += HTTPSClientUtil.doPostJson(httpClient, InterfaceResource.sap_base_url, jsonStr);			
		} catch (Exception e) {
 			e.printStackTrace();
		}
 		 
 		return result;
	}


	@Override
	protected String _saveReceiveBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  BaseFISupport.syncBill(ctx, jsonStr);
		System.out.println(res);
		 logger.info("do BaseFISupport _saveReceiveBill method cost :" + this.timer.msValue());
		return res;	
	}

	
	/**
	 * 
	 * 采购入库单--退库
	 */
	@Override
	protected String _rollBackPurchInBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  PurInWarehsSupport.doRollBackBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do PurInWarehsSupport _rollBackPurchInBill method cost :" + this.timer.msValue());
		return res;	
	}


	/**
	 * 
	 * 销售出库单--退库
	 * 
	 */
	@Override
	protected String _rollBackSaleIssBill(Context ctx, String jsonStr)
			throws BOSException {
		String res =  SaleIssueSupport.doRollBackBill(ctx, jsonStr);
		System.out.println(res);
		logger.info("do _rollBackSaleIssBill SaleIssueSupport method cost :" + this.timer.msValue());
		return res;	
	}

	/**
	 * 
	 * 其他入库单--同步
	 * 
	 */
	@Override
	protected String _saveOtherPurIn(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean _saveOtherPurIn method cost :" + this.timer.msValue());
		 return res;
	}
	
	/**
	 * 
	 * 其他出库单--同步
	 * 
	 */
	@Override
	protected String _saveOtherSaleIss(Context ctx, String jsonStr)
			throws BOSException {
		 String res =  BaseSCMSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
		 logger.info("do SyncBill2EASFacadeControllerBean _saveOtherSaleIss method cost :" + this.timer.msValue());
		 return res;
	}


	/**
	 * 
	 * 成果物 -- 同步
	 * 
	 */
	@Override
	protected String _savePurInCGW(Context ctx, String jsonStr)throws BOSException {
		 String res =  PurInCGWSupport.syncBill(ctx, jsonStr);
		 System.out.println(res);
 		 logger.info("do SyncBill2EASFacadeControllerBean _savePurInCGW method cost :" + this.timer.msValue());
		 return res;
 		
	}


	/**
	 * 
	 * 同步 库存盘点结果至HIS系统
	 * 
	 */
	@Override
	protected void _syncIMCounting(Context ctx, String number) throws BOSException {
		if(number !=null && !"".equals(number)){
			StringBuffer sbr = new StringBuffer();
			sbr.append(" /*dialect*/select b.FStorageOrgUnitID as easOrgId, b.FWarehouseID as warehouseFid, m.FNUMBER skuId,c.FCountingQty qty,mu.FNUMBER uno,  ").append("\r\n");
			sbr.append("  m1.supname,m1.pinpai,m1.huohao,m1.bigUnitCode,m1.bigUnitName,m1.smallUnitCode,m1.smallUnitName,m1.model ,m1.rate,m1.categoryCode,m1.categoryName,  ").append("\r\n");
			sbr.append("  decode(c.FStoreTypeID,'3', decode(m.FMATERIALGROUPID,'Dtmk86FlSCmVG2q3RQFBp8efwEI=',1,'m0jaPS40Sx2p7U36OsuGa8efwEI=',2,2) ,2) type  ").append("\r\n");
			sbr.append("   from T_IM_CountingTask a  ").append("\r\n");
			sbr.append("  inner join T_IM_COUNTINGTABLE  b on a.FID = b.FCOUNTINGTASKID  ").append("\r\n");
			sbr.append("  inner join T_IM_COUNTINGTABLEENTRY c on c.FPARENTID = b.FID  ").append("\r\n");
			sbr.append("  inner join T_BD_Material m  on c.FMATERIALID = m.FID  ").append("\r\n");
			sbr.append("  inner join T_BD_MeasureUnit mu on  mu.FID = c.FBASEUNIT  ").append("\r\n");
			sbr.append(" inner join  (  select m.fid,m.FNAME_L2 supname,m.CFPINPAI as pinpai ,m.CFHUOHAO as huohao,mg.FNumber categoryCode,mg.FName_l2 as categoryName  ").append("\r\n");
			sbr.append("  ,mu.FNUMBER as bigUnitCode,mu.FNAME_L2 as bigUnitName,mu.FNUMBER as smallUnitCode,mu.FNAME_L2 as smallUnitName,m.FMODEL model, 1 rate  ").append("\r\n");
			sbr.append(" from T_BD_MATERIAL m  ").append("\r\n");
			sbr.append(" inner join T_BD_MeasureUnit mu on  m.FBaseUnit = mu.FID  ").append("\r\n");
			sbr.append(" inner join T_BD_MaterialGroup mg on mg.fid = m.FMATERIALGROUPID  ").append("\r\n");
			sbr.append(" where m.FMATERIALGROUPID in ('Dtmk86FlSCmVG2q3RQFBp8efwEI=','m0jaPS40Sx2p7U36OsuGa8efwEI=') and m.FStatus =1  ").append("\r\n");
			sbr.append(" and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ='  ").append("\r\n");
			sbr.append(" and m.fid not in (  ").append("\r\n");
			sbr.append(" select distinct a.FID from T_BD_Material a  ").append("\r\n");
			sbr.append(" INNER JOIN  T_BD_MultiMeasureUnit  b  on a.FID =b.FMATERIALID  ").append("\r\n");
			sbr.append(" where b.FBASECONVSRATE <> 1 and a.FMATERIALGROUPID in('m0jaPS40Sx2p7U36OsuGa8efwEI=','Dtmk86FlSCmVG2q3RQFBp8efwEI=')  ").append("\r\n");
			sbr.append(" and a.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and a.FStatus =1  ").append("\r\n");
			sbr.append(" )  ").append("\r\n");
			sbr.append(" union all  ").append("\r\n");
			sbr.append(" select m.fid,m.FNAME_L2 supname,m.CFPINPAI as pinpai ,m.CFHUOHAO as huohao,mg.FNumber categoryCode,mg.FName_l2 as categoryName  ").append("\r\n");
			sbr.append(" ,mu1.FNUMBER as bigUnitCode,mu1.FNAME_L2 as bigUnitName,mu2.FNUMBER as smallUnitCode,mu2.FNAME_L2 as smallUnitName,  ").append("\r\n");
			sbr.append("  m.FMODEL model,mmu.FBASECONVSRATE rate  ").append("\r\n");
			sbr.append(" from T_BD_Material m  ").append("\r\n");
			sbr.append(" INNER JOIN  T_BD_MultiMeasureUnit mmu on mmu.FMATERIALID = m.FID  ").append("\r\n");
			sbr.append(" inner join T_BD_MeasureUnit mu1 on mu1.FID = mmu.FMEASUREUNITID  ").append("\r\n");
			sbr.append(" inner join T_BD_MeasureUnit mu2 on mu2.FID = m.FBASEUNIT  ").append("\r\n");
			sbr.append(" inner join T_BD_MaterialGroup mg on mg.fid = m.FMATERIALGROUPID  ").append("\r\n");
			sbr.append(" where mmu.FBASECONVSRATE <> 1 and m.FMATERIALGROUPID in('m0jaPS40Sx2p7U36OsuGa8efwEI=','Dtmk86FlSCmVG2q3RQFBp8efwEI=')  ").append("\r\n");
			sbr.append(" and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and m.FStatus =1  ").append("\r\n");
			sbr.append("  ) m1 on m1.FID= m.FID  ").append("\r\n");
			sbr.append("  where a.FNUMBER = '").append(number).append("' and m.FMATERIALGROUPID in ('Dtmk86FlSCmVG2q3RQFBp8efwEI=','m0jaPS40Sx2p7U36OsuGa8efwEI=') and m.FStatus =1  ").append("\r\n");
			sbr.append("  and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and c.FCOUNTINGQTY<>0  ").append("\r\n");
			System.out.println("query SQL ####################################################"+sbr.toString());
			try {
				IRowSet rs =  DbUtil.executeQuery(ctx, sbr.toString());
				if(rs !=null && rs.size() > 0){
					List datas = new ArrayList(); 
					while(rs.next()){
						Map mp =new HashMap(); 
						mp.put("goodName", rs.getObject("supname")); //	商品名
						mp.put("brandName", rs.getObject("pinpai")); //品牌名
						mp.put("specName", rs.getObject("model")); //规格名
						mp.put("type", rs.getObject("type"));//类型 1：高值,2：低值,3:VMI
						mp.put("warehouseFid", rs.getObject("warehouseFid")); //EAS仓库id 
					    mp.put("bigUnitCode", rs.getObject("bigUnitCode"));//大单位code
					    mp.put("bigUnitName", rs.getObject("bigUnitCode"));//大单位名
						mp.put("smallUnitCode", rs.getObject("smallUnitCode"));//小单位code
						mp.put("smallUnitName", rs.getObject("smallUnitCode"));//小单位名
						mp.put("num", rs.getObject("qty"));//数量
						mp.put("spuId", "OLD_"+rs.getObject("skuId")); //spuId
						mp.put("skuId", rs.getObject("skuId")); //skuId
						mp.put("validityTime", "2039-12-30"); //easOrgId
						mp.put("easOrgId", rs.getObject("easOrgId")); //easOrgId
						mp.put("count", rs.getObject("rate"));//换算后比列数量 
						mp.put("categoryCode", rs.getObject("categoryCode")); //分类
						mp.put("categoryName", rs.getObject("categoryName")); //分类名
						mp.put("produce", ""); //	生产地  
						datas.add(mp);
					} 
					
					if(datas !=null && datas.size() >0){
						//调用his接口
					    String url = "http://sr.wellekq.com:10091/his-war/mall-api/material/initStock"; //测试地址
					    //String url = "https://his5.meiweigroup.com/his-war/notify/syncSupplier";  //生产地址
						String result =  sendMessageToHis(url,JSONObject.toJSONString(datas));
						System.out.println("_syncIMCounting result is :"+result);
					}
				}
			} catch (SQLException e) {
 				e.printStackTrace();
			}
		}
	
	}

	
	 /**
	    * 向指定 URL 发送POST方法的请求
	    * 
	    * @param url
	    *            发送请求的 URL
	    * @param param
	    *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	    * @param isproxy
	    *               是否使用代理模式
	    * @return 所代表远程资源的响应结果
	    */
		private static String sendMessageToHis(String url,String param) {
			OutputStreamWriter out = null;
	        BufferedReader in = null;
	        String result = "";  
	        if(url != null && !"".equals(url)){
	        	try {
	        		URL realUrl = new URL(url);
	   	            HttpURLConnection conn =  (HttpURLConnection) realUrl.openConnection();
	   	             // 打开和URL之间的连接
	   	            // 发送POST请求必须设置如下两行
	   	            conn.setDoOutput(true);
	   	            conn.setDoInput(true);
	   	            conn.setRequestMethod("POST");    // POST方法
	   	            // 设置通用的请求属性
	   	            conn.setRequestProperty("accept", "*/*");
	   	            conn.setRequestProperty("connection", "Keep-Alive");
	   	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	   	          	conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	   	            conn.connect();
	   	            // 获取URLConnection对象对应的输出流
	   	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	   	            // 发送请求参数
	   	            out.write(param);
	   	            System.out.println("param------------------------"+param);
	   	            // flush输出流的缓冲
	   	            out.flush();
	   	            // 定义BufferedReader输入流来读取URL的响应
	   	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	   	            String line;
	   	            while ((line = in.readLine()) != null) {
	   	                result += line;
	   	            }
	   	        } catch (Exception e) {
	   	            System.out.println("发送 POST 请求出现异常！"+e);
	   	            e.printStackTrace();
	   	        }
	   	        //使用finally块来关闭输出流、输入流
	   	        finally{
	   	            try{
	   	                if(out!=null){
	   	                    out.close();
	   	                }
	   	                if(in!=null){
	   	                    in.close();
	   	                }
	   	            }
	   	            catch(IOException ex){
	   	                ex.printStackTrace();
	   	            }
	   	        }
	        } 
	       return result;
	   }


	/**
	 * 
	 * 同步 物料至中间库
	 * 
	 */
	@Override
	protected void _syncMaterial2TempTab(Context ctx) throws BOSException {
		StringBuffer sbr = new StringBuffer();
		// 同步 eas_material_spu
		sbr.append(" /*dialect*/MERGE INTO eas_material_spu t1 ").append("\r\n");
		sbr.append(" using ( ").append("\r\n");
		sbr.append("    select 'OLD-'||m.FNUMBER as spuId,m.FNAME_L2 supname,m.CFPINPAI as pinpai ,m.CFHUOHAO as huohao,m.FLASTUPDATETIME ").append("\r\n");
		sbr.append(" from T_BD_MATERIAL m ").append("\r\n");
		sbr.append(" inner join T_BD_MeasureUnit mu on  m.FBaseUnit = mu.FID ").append("\r\n");
		sbr.append(" where m.FMATERIALGROUPID in ('Dtmk86FlSCmVG2q3RQFBp8efwEI=','m0jaPS40Sx2p7U36OsuGa8efwEI=') and m.FStatus =1 ").append("\r\n");
		sbr.append(" and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and m.FCONTROLUNITID='RNf9877kRE2huN/UdYtCvMznrtQ=' ").append("\r\n");
		sbr.append(" ) t2 ").append("\r\n");
		sbr.append("     ON ( t1.spuId=t2.spuId) ").append("\r\n");
		sbr.append(" WHEN MATCHED THEN ").append("\r\n");
		sbr.append(" UPDATE SET  t1.supname = t2.supname,t1.huohao=t2.huohao,t1.pinpai=t2.pinpai ").append("\r\n");
		sbr.append(" WHEN NOT MATCHED THEN ").append("\r\n");
		sbr.append(" insert (spuId,supname,pinpai,huohao,createtime,udpatetime) values (t2.spuId,t2.supname,t2.pinpai,t2.huohao,sysdate,sysdate) ").append("\r\n");
		//EAISynTemplate.execute(ctx, database, sbr.toString());   
		DbUtil.execute(ctx,sbr.toString());
		sbr.setLength(0);
		
		//同步  eas_material_sku 
		sbr.append(" /*dialect*/MERGE INTO eas_material_sku t1  ").append("\r\n");
		sbr.append(" using (  ").append("\r\n");
		sbr.append("  select distinct 'OLD-'||m.FNUMBER as spuId,m.FNUMBER as skuId,mu.FNUMBER as bigUnitCode,mu.FNUMBER as smallUnitCode,m.FMODEL model, 1 rate  ").append("\r\n");
		sbr.append(" from T_BD_MATERIAL m  ").append("\r\n");
		sbr.append(" inner join T_BD_MeasureUnit mu on  m.FBaseUnit = mu.FID  ").append("\r\n");
		sbr.append(" where m.FMATERIALGROUPID in ('Dtmk86FlSCmVG2q3RQFBp8efwEI=','m0jaPS40Sx2p7U36OsuGa8efwEI=') and m.FStatus =1  ").append("\r\n");
		sbr.append(" and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ='  ").append("\r\n");
		sbr.append(" and m.fid not in (  ").append("\r\n");
		sbr.append(" select distinct a.FID from T_BD_Material a  ").append("\r\n");
		sbr.append(" INNER JOIN  T_BD_MultiMeasureUnit  b  on a.FID =b.FMATERIALID  ").append("\r\n");
		sbr.append(" where b.FBASECONVSRATE <> 1 and a.FMATERIALGROUPID in('m0jaPS40Sx2p7U36OsuGa8efwEI=','Dtmk86FlSCmVG2q3RQFBp8efwEI=')  ").append("\r\n");
		sbr.append(" and a.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and a.FStatus =1  ").append("\r\n");
		sbr.append(" )  ").append("\r\n");
		sbr.append(" union all  ").append("\r\n");
		sbr.append(" select distinct 'OLD-'||m.FNUMBER as spuId,m.FNUMBER as skuId,mu1.FNUMBER as bigUnitCode,mu2.FNUMBER as smallUnitCode,m.FMODEL model, mmu.FBASECONVSRATE rate  ").append("\r\n");
		sbr.append(" from T_BD_Material m  ").append("\r\n");
		sbr.append(" INNER JOIN  T_BD_MultiMeasureUnit mmu on mmu.FMATERIALID = m.FID  ").append("\r\n");
		sbr.append(" inner join T_BD_MeasureUnit mu1 on mu1.FID = mmu.FMEASUREUNITID  ").append("\r\n");
		sbr.append(" inner join T_BD_MeasureUnit mu2 on mu2.FID = m.FBASEUNIT  ").append("\r\n");
		sbr.append(" where mmu.FBASECONVSRATE <> 1 and m.FMATERIALGROUPID in('m0jaPS40Sx2p7U36OsuGa8efwEI=','Dtmk86FlSCmVG2q3RQFBp8efwEI=')  ").append("\r\n");
		sbr.append(" and m.FCONTROLUNITID<>'iQMu+uuiQda3ZeJ4FCNppMznrtQ=' and m.FStatus =1  ").append("\r\n");
		sbr.append(" ) t2  ").append("\r\n");
		sbr.append("     ON ( t1.spuId=t2.spuId)  ").append("\r\n");
		sbr.append(" WHEN MATCHED THEN  ").append("\r\n");
		sbr.append(" UPDATE SET  t1.bigUnitCode = t2.bigUnitCode,t1.smallUnitCode=t2.smallUnitCode,t1.model=t2.model,t1.rate=t2.rate  ").append("\r\n");
		sbr.append(" WHEN NOT MATCHED THEN  ").append("\r\n");
		sbr.append(" insert (spuId,skuId,bigUnitCode,smallUnitCode,model,rate) values (t2.spuId,t2.skuId,t2.bigUnitCode,t2.smallUnitCode,t2.model,t2.rate)  ").append("\r\n");
		//EAISynTemplate.execute(ctx, database, sbr.toString());   
		DbUtil.execute(ctx,sbr.toString());
		sbr.setLength(0);
	} 
	
	
 
}