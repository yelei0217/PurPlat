package com.kingdee.eas.custom.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.basedata.assistant.CurrencyFactory;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.master.cssp.CSSPGroupFactory;
import com.kingdee.eas.basedata.master.cssp.CSSPGroupInfo;
import com.kingdee.eas.basedata.master.cssp.CSSPGroupStandardFactory;
import com.kingdee.eas.basedata.master.cssp.CSSPGroupStandardInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerCompanyBankFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerCompanyBankInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerCompanyInfoFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerCompanyInfoInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerGroupDetailInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
import com.kingdee.eas.basedata.master.cssp.CustomerSaleInfoFactory;
import com.kingdee.eas.basedata.master.cssp.CustomerSaleInfoInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierCompanyInfoInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
import com.kingdee.eas.basedata.master.cssp.SupplierGroupDetailInfo;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.cssp.UsedStatusEnum;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.basedata.org.OrgType;
import com.kingdee.eas.basedata.org.SaleOrgUnitFactory;
import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.DateBaseLogFactory;
import com.kingdee.eas.custom.DateBaseLogInfo;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.ISyncDataEASFacade;
import com.kingdee.eas.custom.SyncDataEASFacadeFactory;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.util.CoreUtil;
import com.kingdee.eas.framework.EffectedStatusEnum;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;


public class SupplySyncServiceControllerBean extends AbstractSupplySyncServiceControllerBean
{
    private static Logger logger =  Logger.getLogger("com.kingdee.eas.custom.app.SupplySyncServiceControllerBean");

    private static String url = "http://sr.wellekq.com:10091/his-war/notify/syncSupplier"; //���Ե�ַ
    //private static String url = "https://his5.meiweigroup.com/his-war/notify/syncSupplier";  //������ַ

	@Override
	public String SyncSupplyJT(Context ctx, String data) throws BOSException {
		// TODO Auto-generated method stub
		//return super.SyncSupplyJT(ctx, data); 
		 
		
		Map<String, String> map = new  HashMap<String, String>();
		//map = (Map) JSONObject.parse(datajsonStr); 
		
		//data = "{\"FYINXING\":\"0\",\"FOPERTYPE\":\"2\",\"FSHEBEI\":\"0\",\"FHAOCAI\":\"0\",\"FBANKS\":\"����111551,1112552222\",\"FISGROUP\":\"0\",\"FYICHI\":\"0\",\"FCLASSNAME\":\"G4\",\"FSTATUS\":\"0\",\"FORGNUMBER\":\"Z+ABU8twSxmCkpJXqg52zMznrtQ=\",\"FNAME\":\"���Թ�Ӧ��115\",\"FNUMBER\":\"CESHI115\"}";
		map = (Map) JSONObject.parse(data);
		
		HashMap<String, String> returnMap =new  HashMap<String, String>();
		returnMap.put("code", "error");
		
		
	
		Context oldCTX = CoreUtil.copyContext(ctx) ;
		Context tempCTX = new Context() ;
		try {
			tempCTX =CoreUtil.context(oldCTX, "user");
		} catch (EASBizException e1) {
 			e1.printStackTrace();
		} 
		// ȫ����
		List<Map<String, String>> quanJT = new ArrayList<Map<String, String>>();
		// ��ȫ����
		List<Map<String, String>> feiJT = new ArrayList<Map<String, String>>();
		// ��Ӧ��
		String sql = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 
		// �����־��Ϣ
		
		SupplierInfo info = null; 
		
		boolean isAdd = false; 
		System.out.println("*********************SyncSupplyJT ���� data:"+data);
		if (map.get("FNUMBER")== null || "".equals(map.get("FNUMBER").toString()) ) { 
			returnMap.put("msg", "��Ӧ�̱��벻��Ϊ�ա�");
			String jsonStr = JSONObject.toJSONString(returnMap);
			return jsonStr;  
		}
		 
		DateBaseLogInfo loginfo = getlogInfo(map,DateBasetype.Supplier);
		if(data !=null && !"".equals(data)){ 
			try{
				   
				if (SupplierFactory.getLocalInstance(ctx).exists("where number='" + map.get("FNUMBER") + "'")) {
					
					if (map.get("FOPERTYPE")== null || "".equals(map.get("FOPERTYPE").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵Ĳ������Ͳ���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					
					info = SupplierFactory.getLocalInstance(ctx).getSupplierInfo("where number='" + map.get("FNUMBER") + "'");
					map.put("supplier", info.getId().toString());
					
					String errMsg = "";
					String successMsg = "";
					boolean flag = false;
					
					
					if (map.get("FOPERTYPE").toString().indexOf("2")>=0) {// 2����ͣ�ù�Ӧ��

						
						if (map.get("FSTATUS")== null || "".equals(map.get("FSTATUS").toString()) ) {   
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�״̬����Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) {   
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�Ϊ���Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻����;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						CompanyOrgUnitInfo companyInfo = new CompanyOrgUnitInfo();
						if (map.get("FSTATUS").toString().equals("1")) {// 0:����;1:����;
							companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
							
							loginfo.setProcessType(DateBaseProcessType.DisAble);
							if (info.getUsedStatus().getValue() == 1) { //  1��׼  ����
								
								if(map.get("FISGROUP").toString().equals("1")){
									if(companyInfo.getId().toString().equals("00000000-0000-0000-0000-000000000000CCE7AED4")){ 
										try{
											SupplierFactory.getLocalInstance(ctx).freezed(new ObjectUuidPK(info.getId()), true);
										}catch (Exception e) { 
											System.out.println("===================="+e.getMessage()); 
											if(e.getMessage().indexOf("ҵ����Ϊ0�����������")>0){ 
												String insertDisSql = "insert into CT_CUS_DisSupplier(fid,fcreatorid,fcreatetime,flastupdateuserid,flastupdatetime,fcontrolunitid,CFSupid) " +
														" values (newbosid('29732D15'),'256c221a-0106-1000-e000-10d7c0a813f413B7DE7F',sysdate,'256c221a-0106-1000-e000-10d7c0a813f413B7DE7F',sysdate,'00000000-0000-0000-0000-000000000000CCE7AED4','"+info.getId()+"')"; 
												DbUtil.execute(ctx, insertDisSql);
											}
										} 
										String  updateMidSql = " /*dialect*/update  EAS_ORG_SupplyinfoMid    set  FSTATUS = 0,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where  supplierkey = '"+info.getId()+"'  and FSTATUS = 1 ";
										EAISynTemplate.execute(ctx, "04", updateMidSql);
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����ɾ����");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
										
										flag = true;
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����ɾ����";
										
									}else{
										String  updatePurSql = " /*dialect*/update T_BD_SupplierPurchaseInfo set FUsingStatus = 1  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
										DbUtil.execute(ctx, updatePurSql);
										String  updateComSql = " /*dialect*/update T_BD_SupplierCompanyInfo set FUsingStatus = 1  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
										DbUtil.execute(ctx, updateComSql);
										
										String  updateMidSql = " /*dialect*/update  EAS_ORG_SupplyinfoMid    set  FSTATUS = 0,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where  supplierkey = '"+info.getId()+"'  and FSTATUS = 1 and  csfid = '"+companyInfo.getId()+"' ";
										EAISynTemplate.execute(ctx, "04", updateMidSql);
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯����ɾ����");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
										
										flag = true;
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯����ɾ����";
										 
									}
									
								}else{
									
									if(companyInfo.getId().toString().equals("00000000-0000-0000-0000-000000000000CCE7AED4")){  
										System.out.println("===================="+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;"); 
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;��");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
										
										flag = true;
										successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;";
									}else{
										String  updatePurSql = " /*dialect*/update T_BD_SupplierPurchaseInfo set FUsingStatus = 1  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
										DbUtil.execute(ctx, updatePurSql);
										String  updateComSql = " /*dialect*/update T_BD_SupplierCompanyInfo set FUsingStatus = 1  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
										DbUtil.execute(ctx, updateComSql);
										
										String  updateMidSql = " /*dialect*/update  EAS_ORG_SupplyinfoMid    set  FSTATUS = 0,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where  supplierkey = '"+info.getId()+"'  and FSTATUS = 1 and  csfid = '"+companyInfo.getId()+"' ";
										EAISynTemplate.execute(ctx, "04", updateMidSql);
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯����ɾ����");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
										
										flag = true;
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯����ɾ����";
										 
									}
									//SupplierFactory.getLocalInstance(tempCTX).freezed(new ObjectUuidPK(info.getId()), true);
								}  
							} 
							
						}else if (map.get("FSTATUS").toString().equals("0")) {// 0:����; 
							
							companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
							loginfo.setProcessType(DateBaseProcessType.ENABLE);
							if(map.get("FISGROUP").toString().equals("1")){
								if(companyInfo.getId().toString().equals("00000000-0000-0000-0000-000000000000CCE7AED4")){  
									if(info.getUsedStatus().getValue() == 2){
										String  updatePurSql = " /*dialect*/update T_BD_SupplierPurchaseInfo set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' ";
										DbUtil.execute(ctx, updatePurSql);
										String  updateComSql = " /*dialect*/update T_BD_SupplierCompanyInfo  set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' ";
										DbUtil.execute(ctx, updateComSql);
										
										SupplierFactory.getLocalInstance(ctx).unFreezed(new ObjectUuidPK(info.getId()), true);
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������á�");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
										
										flag = true;
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������á�";
									} 
								}else{
									String  updatePurSql = " /*dialect*/update T_BD_SupplierPurchaseInfo set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
									DbUtil.execute(ctx, updatePurSql);
									String  updateComSql = " /*dialect*/update T_BD_SupplierCompanyInfo  set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
									DbUtil.execute(ctx, updateComSql);
									 
									
									loginfo.setStatus(true);
									loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯�������á�");
									DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
									
									flag = true;
									successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯�������á�";
									 
								}
								
							}else{
								
								if(companyInfo.getId().toString().equals("00000000-0000-0000-0000-000000000000CCE7AED4")){  
									System.out.println("===================="+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;"); 
									loginfo.setStatus(true);
									loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;��");
									DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
									
									flag = true;
									successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�Ϊ���ţ����ܰ��ռ��ŷ�ʽ����;";
								}else{
									String  updatePurSql = " /*dialect*/update T_BD_SupplierPurchaseInfo set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
									DbUtil.execute(ctx, updatePurSql);
									String  updateComSql = " /*dialect*/update T_BD_SupplierCompanyInfo set FUsingStatus = 0  WHERE FSupplierID ='"+info.getId()+"' and  FControlUnitID='"+companyInfo.getId()+"' ";
									DbUtil.execute(ctx, updateComSql);
									 
									loginfo.setStatus(true);
									loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯�������á�");
									DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
									
									flag = true;
									successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ����"+companyInfo.getName()+"��֯�����á�";
									 
								}
								//SupplierFactory.getLocalInstance(tempCTX).freezed(new ObjectUuidPK(info.getId()), true);
							} 
						}    
					}
					if (map.get("FOPERTYPE").toString().indexOf("0")>=0) {//1����Ӧ������
						successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ���Ѿ�����;";
						returnMap.put("msg",  successMsg); 
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					if (map.get("FOPERTYPE").toString().indexOf("1")>=0) {//1����Ӧ�̸��� 
						
						if (map.get("FNAME")== null || "".equals(map.get("FNAME").toString()) ) {   
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�����Ʋ���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) {   
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�Ϊ���Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if(!map.get("FISGROUP").toString().equals("1")){
							CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ info.getCU().getId().toString()+ "'");
							CtrlUnitInfo ctrlorg = new CtrlUnitInfo(); 
							String cuid = companyInfo.getCU().getId().toString(); 
							ctrlorg.setId(BOSUuid.read(cuid)); 
							tempCTX.put(OrgType.Company, companyInfo); //������֯
							tempCTX.put("CompanyInfo", companyInfo);
							tempCTX.put("CurCompanyId", info.getCU().getId().toString());
							tempCTX.put(OrgType.ControlUnit, ctrlorg);

						}
						String name = map.get("FNAME").toString();
						String supName = info.getName();
						int i=0;
						String msgStr = "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��";
						if(!name.equals(supName)){
							if (info.getUsedStatus() == UsedStatusEnum.APPROVED) { 
								DbUtil.execute(ctx,"update T_BD_Supplier set FUsedStatus = 0 where  fnumber ='"+ info.getNumber() + "' ");

								loginfo.setProcessType(DateBaseProcessType.Update);
								info.setName(name);
								if(map.get("FISGROUP").toString().equals("1")){
									SupplierFactory.getLocalInstance(ctx).update(new ObjectUuidPK(info.getId()), info);
								}else{
									SupplierFactory.getLocalInstance(tempCTX).update(new ObjectUuidPK(info.getId()), info);
								}
								
								DbUtil.execute(ctx,"update T_BD_Supplier set FUsedStatus = 1 where  fnumber ='"+ info.getNumber() + "' ");
								i=1;
							} else if (info.getUsedStatus() == UsedStatusEnum.UNAPPROVE) { 
								loginfo.setProcessType(DateBaseProcessType.Update);
								info.setName(name);
								
								if(map.get("FISGROUP").toString().equals("1")){
									SupplierFactory.getLocalInstance(ctx).update(new ObjectUuidPK(info.getId()), info);
								}else{
									SupplierFactory.getLocalInstance(tempCTX).update(new ObjectUuidPK(info.getId()), info);
								}
								i=1;
							}else{
								successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ���Ѿ�����,�����޸�;";
								returnMap.put("msg",  successMsg); 
								String jsonStr = JSONObject.toJSONString(returnMap);
								return jsonStr;  
							}
							 
							loginfo.setStatus(true);
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��������ͬ����");
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
							flag = true;
							successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��������ͬ��;";
						} 
					}
					
					if(map.get("FOPERTYPE").toString().indexOf("3")>=0) { //3��������֯��Χ
						 
						if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻����;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
						
						String comNumber = companyInfo.getNumber(); 
						String orgSql = " select fid  from t_org_admin where  (fnumber = '"+comNumber+"' and FLAYERTYPEID  = 'mkPGwYLIQ4y/tsiVKLQX8WKCSYg=') or FID= '00000000-0000-0000-0000-000000000000CCE7AED4'  ";
						IRowSet rs =  DbUtil.executeQuery(ctx,orgSql);
						if(rs == null || rs.size() == 0){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"����֯���ͱ���Ϊ����;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
					    }
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) { 
					
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ȫ����״̬����Ϊ��;";
							returnMap.put("msg",  successMsg);
							 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						  
						/*if (map.get("FOLDOPENBANK")== null || "".equals(map.get("FOLDOPENBANK").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵Ŀ����в���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FOLDBANKACCOUNT")== null || "".equals(map.get("FOLDBANKACCOUNT").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵������˺Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} */
						
						String sqlBank = "select distinct  supbank.FBank BANK  , supbank.FBankAccount BANKACCOUNT from   T_BD_SupplierCompanyBank  supbank"+
						" inner  join T_BD_SupplierCompanyInfo  supcom on supcom.fid = supbank.FSUPPLIERCOMPANYINFOID"+
						" inner  join T_BD_Supplier  sup on sup.fid = supcom.FSupplierID where sup.fnumber= '"+map.get("FNUMBER").toString()+"'";
						
						IRowSet rsBank =  DbUtil.executeQuery(ctx,sqlBank);
						String bankStr="";
						if(rsBank.size() > 0){
							try {
								while (rsBank.next()) {
									String bank = rsBank.getString("BANK"); 
									String bankCount = rsBank.getString("BANKACCOUNT");  
									String str = bank+","+bankCount +"|";
									
									bankStr = bankStr + str;
								} 
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						} 
						 
						
						if (map.get("FSHEBEI")== null || "".equals(map.get("FSHEBEI").toString()) ) { 
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����Ϊ��;";
							returnMap.put("msg",  successMsg);
							 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						if( !"0".equals(map.get("FSHEBEI").toString()) && !"1".equals(map.get("FSHEBEI").toString())){
							  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FHAOCAI")== null || "".equals(map.get("FHAOCAI").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ĲĲ���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FHAOCAI").toString()) && !"1".equals(map.get("FHAOCAI").toString())){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�Ĳ�����ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FYICHI")== null || "".equals(map.get("FYICHI").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���ݲ���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��������ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FYINXING")== null || "".equals(map.get("FYINXING").toString()) ) { 
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ����β���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���������ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}  
						
						String shebei = map.get("FSHEBEI").toString();
						String haocai = map.get("FHAOCAI").toString(); 
						String yichi = map.get("FYICHI").toString();
						String yinxing = map.get("FYINXING").toString(); 
						 
						
						if (map.get("FISGROUP").toString().equals("1")) {
							 
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��Ϊ������֯������Ҫ�ٷ���ҵ����֯��");
							loginfo.setStatus(true);
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
							
							flag = true;
							successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��Ϊ������֯������Ҫ�ٷ���ҵ����֯;";  
							
						} else if (map.get("FISGROUP").toString().equals("0")) {
							Map<String, String> jt = new HashMap<String, String>();
							jt.put("sid", info.getId().toString());
							jt.put("cid", companyInfo.getCU().getId().toString());
							//jt.put("bank", map.get("FOLDOPENBANK").toString());
							//jt.put("bankAccount", map.get("FOLDBANKACCOUNT").toString())
							
							
							jt.put("bankmsg", bankStr);
							
							jt.put("FSHEBEI", map.get("FSHEBEI").toString());
							jt.put("FHAOCAI", map.get("FHAOCAI").toString());
							jt.put("FYICHI", map.get("FYICHI").toString());
							jt.put("FYINXING", map.get("FYINXING").toString());
							feiJT.add(jt);
							
							
							
							boolean flagThis = AppUnit.AssginSupplierByService(ctx, quanJT, feiJT); 
							if(flagThis){ 
								loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̳ɹ�����"+companyInfo.getName()+"ҵ����֯��");
								//returnMap.put("msg", "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̳ɹ�����"+companyInfo.getName()+"ҵ����֯��");
								successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̳ɹ�����"+companyInfo.getName()+"ҵ����֯��"; 
							}else{
								loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ���ѷ���"+companyInfo.getName()+"ҵ����֯��");
								//returnMap.put("msg", "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ���ѷ���"+companyInfo.getName()+"ҵ����֯��");
								successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ���ѷ���"+companyInfo.getName()+"ҵ����֯��"; 
							}
							loginfo.setStatus(true);
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
							
							flag = true; 
						} 
						 
					}
					if(map.get("FOPERTYPE").toString().indexOf("4")>=0) { //4����Ӧ�����ͱ��
						
						 
						if (map.get("FCLASSNAME")== null || "".equals(map.get("FCLASSNAME").toString()) ) { 
						 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵ķ�����벻��Ϊ��;";
							returnMap.put("msg",  successMsg);
							 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
							
						}
						if( !CSSPGroupFactory.getLocalInstance(ctx).exists("where number ='"+ map.get("FCLASSNAME").toString()+"'") ){
							  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵ķ�����벻����;";
							returnMap.put("msg",  successMsg);
							 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						String classSup = map.get("FCLASSNAME").toString();
						String supClassId = info.getBrowseGroup().getId().toString();
						 
						CSSPGroupInfo classinfo = CSSPGroupFactory.getLocalInstance(ctx).getCSSPGroupInfo("where number ='"+ map.get("FCLASSNAME").toString()+"'");
						 
						if(!supClassId.equals(classinfo.getId().toString())){
							if (info.getUsedStatus() == UsedStatusEnum.APPROVED) { 
								DbUtil.execute(ctx,"update T_BD_Supplier set FBrowseGroupID = '"+classinfo.getId().toString()+"' where  fnumber ='"+ info.getNumber() + "' ");
								DbUtil.execute(ctx,"update T_BD_SupplierGroupDetail set FSupplierGroupID = '"+classinfo.getId().toString()+"' ,FSupplierGroupFullName='"+classinfo.getName()+"' "
										+" ,FSupplierGroupStandardID='"+classinfo.getGroupStandard().getId()+"'  where  FSupplierID ='"+ info.getId() + "' ");
								loginfo.setProcessType(DateBaseProcessType.Update);
								 
							} else if (info.getUsedStatus() == UsedStatusEnum.UNAPPROVE) { 
								DbUtil.execute(ctx,"update T_BD_Supplier set FBrowseGroupID = '"+classinfo.getId().toString()+"' where  fnumber ='"+ info.getNumber() + "' ");
								
								DbUtil.execute(ctx,"update T_BD_SupplierGroupDetail set FSupplierGroupID = '"+classinfo.getId().toString()+"' ,FSupplierGroupFullName='"+classinfo.getName()+"' "
										+" ,FSupplierGroupStandardID='"+classinfo.getGroupStandard().getId()+"'  where  FSupplierID ='"+ info.getId() + "' ");
								
								loginfo.setProcessType(DateBaseProcessType.Update);
								 
							}
							 
							loginfo.setStatus(true);
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̹�Ӧ�������ѱ��Ϊ"+classSup+"��");
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
						  
							flag = true; 
							successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̹�Ӧ�������ѱ��Ϊ"+classSup+";";
							 
						}else{
							loginfo.setStatus(true);
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̹�Ӧ�������Ѿ���"+classSup+",����Ҫ�����");
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
						 
							flag = true; 
							successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̹�Ӧ�������Ѿ���"+classSup+",����Ҫ���;";
							 
						} 
						
					} 
					if(map.get("FOPERTYPE").toString().indexOf("5")>=0) { 
					  
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ȫ����״̬����Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻����;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
						
						String comNumber = companyInfo.getNumber(); 
						String orgSql = " select fid  from t_org_admin where (fnumber = '"+comNumber+"' and FLAYERTYPEID  = 'mkPGwYLIQ4y/tsiVKLQX8WKCSYg=') or FID= '00000000-0000-0000-0000-000000000000CCE7AED4'  ";
						IRowSet rsCom =  DbUtil.executeQuery(ctx,orgSql);
						if(rsCom == null || rsCom.size() == 0){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"����֯���ͱ���Ϊ����;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
					   }
						
						/*if (map.get("FOPENBANK")== null || "".equals(map.get("FOPENBANK").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵Ŀ����в���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FBANKACCOUNT")== null || "".equals(map.get("FBANKACCOUNT").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵������˺Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}*/  
						if (map.get("FBANKS")== null || "".equals(map.get("FBANKS").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������Ϣ����Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						
						
						/*String openbank = map.get("FOPENBANK");
						String bankCount = map.get("FBANKACCOUNT"); */ 
						
						
						String suppiyid = info.getId().toString(); 
						 
						String orgid = map.get("FORGNUMBER").toString(); 
						String isgroup = map.get("FISGROUP").toString();
						
						String bankList = map.get("FBANKS");
					    String[] arr = bankList.split("\\|");
					    
					    if(arr.length >0 ){
					    	for(int a = 0 ; a < arr.length ; a++ ){
						    	String bankMsg = arr[a];
						    	String[] brr = bankMsg.split(",");
						    	
						    	if(brr.length == 2){
						    		String openbank = brr[0];
							    	String bankCount = brr[1];
							    	

									String selectSql = "";
									if("0".equals(isgroup)){
										//selectSql = "select fid from  T_BD_SupplierCompanyBank where  FBankAccount = '"+bankCount+"'    and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'  and FCONTROLUNITID = '"+orgid+"'     )  ";
										selectSql = "select fid from  T_BD_SupplierCompanyBank where  FBankAccount = '"+bankCount+"'  and  FBank = '"+openbank+"'  and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'  and FCONTROLUNITID = '"+orgid+"'     )  ";
									}else{
										//selectSql = "select fid from  T_BD_SupplierCompanyBank where  FBankAccount = '"+bankCount+"'    and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'    )  ";
										selectSql = "select fid from  T_BD_SupplierCompanyBank where  FBankAccount = '"+bankCount+"'  and  FBank = '"+openbank+"'  and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'    )  ";
									}
									
									
									IRowSet rs =  DbUtil.executeQuery(ctx,selectSql);
									if(rs.size() > 0){
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"�Ѵ���ϵͳ�С�");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
									 
										
										flag = true; 
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"�Ѵ���ϵͳ��;";
										   
									}else{ 
										StringBuffer sbr1  = new StringBuffer("insert into T_BD_SupplierCompanyBank (FID,FBank,FBankAccount,FSupplierCompanyInfoID,FSeq)");
										
										if("0".equals(isgroup)){
											/*sbr1.append(" select    newbosid('E62C04BD') , '"+openbank+"','"+bankCount+"', supcombank.FSupplierCompanyInfoID , 1  from  t_bd_Supplier  supp "); 
											sbr1.append(" inner  join   t_bd_Suppliercompanyinfo supcom  on  supcom.FCONTROLUNITID ='"+orgid+"'  and  supp.fid = supcom.FSupplierID "); 
											sbr1.append(" inner join  T_BD_SupplierCompanyBank supcombank on  supcombank.FSupplierCompanyInfoID = supcom.fid");  
											sbr1.append(" where  supp.fnumber = '"+info.getNumber()+"'   and   supcombank.FBank = '"+oldopenbank+"' and  supcombank.FBankAccount ='"+oldbankCount+"'"); */
											
					
											sbr1.append(" select    newbosid('E62C04BD') , '"+openbank+"','"+bankCount+"', a.SUPCOMID , 1  from " +
													" ( select  distinct supcom.fid  SUPCOMID from  t_bd_Supplier  supp "); 
											sbr1.append(" inner  join   t_bd_Suppliercompanyinfo supcom  on  supcom.FCONTROLUNITID ='"+orgid+"'  and  supp.fid = supcom.FSupplierID "); 
											//sbr1.append(" left join  T_BD_SupplierCompanyBank supcombank on  supcombank.FSupplierCompanyInfoID = supcom.fid");  
											sbr1.append(" where  supp.fnumber = '"+info.getNumber()+"' ) a ");
											
										}else{ 
											sbr1.append(" select    newbosid('E62C04BD') , '"+openbank+"','"+bankCount+"', a.SUPCOMID , 1 from " +
													" ( select  distinct supcom.fid  SUPCOMID from   t_bd_Supplier  supp "); 
											sbr1.append(" inner  join   t_bd_Suppliercompanyinfo supcom  on supp.fid = supcom.FSupplierID "); 
											//sbr1.append(" left join  T_BD_SupplierCompanyBank supcombank on  supcombank.FSupplierCompanyInfoID = supcom.fid");  
											sbr1.append(" where  supp.fnumber = '"+info.getNumber()+"' ) a  ");  
										}  
										DbUtil.execute(ctx,sbr1.toString());   
										
										loginfo.setStatus(true);
										loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"�����ɹ���");
										DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
					
										flag = true; 
										successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"�����ɹ�;";
										 
									} 
						    	}else{
						    		successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��������Ϣ��"+(a+1)+"��"+bankList+"�޷�����;";
						    	} 
						    	 
						    }
					    }else{
					    	successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��������Ϣ"+bankList+"�޷�����;";
					    }
					     
					} 
					if(map.get("FOPERTYPE").toString().indexOf("6")>=0) { //6���޸Ŀ����к������˻�
						
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ȫ����״̬����Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻����;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}

						CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
						
						String comNumber = companyInfo.getNumber(); 
						String orgSql = " select fid  from t_org_admin where  (fnumber = '"+comNumber+"' and FLAYERTYPEID  = 'mkPGwYLIQ4y/tsiVKLQX8WKCSYg=') or FID= '00000000-0000-0000-0000-000000000000CCE7AED4'  ";
						IRowSet rsCom =  DbUtil.executeQuery(ctx,orgSql);
						if(rsCom == null || rsCom.size() == 0){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"����֯���ͱ���Ϊ����;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
					   }
						
						if (map.get("FOPENBANK")== null || "".equals(map.get("FOPENBANK").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵Ŀ����в���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FBANKACCOUNT")== null || "".equals(map.get("FBANKACCOUNT").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵������˺Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}  
						
						if (map.get("FOLDOPENBANK")== null || "".equals(map.get("FOLDOPENBANK").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�ľɹ�Ӧ�̵Ŀ����в���Ϊ�ա�;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FOLDBANKACCOUNT")== null || "".equals(map.get("FOLDBANKACCOUNT").toString()) ) { 
							 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵ľ������˺Ų���Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						String openbank = map.get("FOPENBANK");
						String bankCount = map.get("FBANKACCOUNT"); 
						String oldopenbank = map.get("FOLDOPENBANK");
						String oldbankCount = map.get("FOLDBANKACCOUNT"); 
						
						String suppiyid = info.getId().toString(); 
						 
						String orgid = map.get("FORGNUMBER").toString();
						
						String isgroup = map.get("FISGROUP").toString(); 
						
						String selectSql = "";
						if("0".equals(isgroup)){
							//selectSql = "select fid from  T_BD_SupplierCompanyBank where  FBank = '"+oldopenbank+"'  and   FBankAccount = '"+oldbankCount+"' and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'  and FCONTROLUNITID = '"+orgid+"'     )  ";
							selectSql = "select fid from  T_BD_SupplierCompanyBank where     FBankAccount = '"+oldbankCount+"' and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'  and FCONTROLUNITID = '"+orgid+"'     )  ";
						}else{
							//selectSql = "select fid from  T_BD_SupplierCompanyBank where   FBank = '"+oldopenbank+"'  and   FBankAccount = '"+oldbankCount+"' and  FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"' )  ";
							selectSql = "select fid from  T_BD_SupplierCompanyBank where     FBankAccount = '"+oldbankCount+"' and  FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"' )  ";
						}
						
						IRowSet rs =  DbUtil.executeQuery(ctx,selectSql);
						if(rs.size() > 0){ 
							String updateBank = " update T_BD_SupplierCompanyBank  set  FBank = '"+openbank+"' , FBankAccount = '"+bankCount+"'  where     FBank = '"+oldopenbank+"'  and   FBankAccount = '"+oldbankCount+"'  ";
							if("0".equals(isgroup)){
								updateBank = updateBank+" and   FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"'  and FCONTROLUNITID = '"+orgid+"'     )  ";
							}else{
								updateBank =  updateBank+" and  FSupplierCompanyInfoID in   (select  fid  from  t_bd_Suppliercompanyinfo  where  FSupplierID ='"+suppiyid+"' )  ";
							}  
							DbUtil.execute(ctx,updateBank); 
							
							loginfo.setStatus(true);
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+oldopenbank+"�������˻�"+oldopenbank+"�ֱ��޸�Ϊ"+openbank+"�������˻�"+bankCount+"��");
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
						  
							flag = true;
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̴˿�����"+oldopenbank+"�������˻�"+oldopenbank+"�ֱ��޸�Ϊ"+openbank+"�������˻�"+bankCount+";";
							  
						}else{
							loginfo.setStatus(true);
							loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"��ϵͳ�в����ڡ�");
							DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
						  
							successMsg = successMsg+"����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˿�����"+openbank+"�������˻�"+bankCount+"��ϵͳ�в�����;";
							  
							returnMap.put("code", "error");
							returnMap.put("msg",successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}  
					}
					
					if(map.get("FOPERTYPE").toString().indexOf("7")>=0) {//7�����Ĺ�Ӧ�̷�Χ
						if (map.get("FSHEBEI")== null || "".equals(map.get("FSHEBEI").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����Ϊ��;";
							returnMap.put("msg",  successMsg);
							
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						if( !"0".equals(map.get("FSHEBEI").toString()) && !"1".equals(map.get("FSHEBEI").toString())){
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ȫ����״̬����Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}
						
						if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ��;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
							
						}
						if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻����;";
							returnMap.put("msg",  successMsg); 
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}

						CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
						
						String comNumber = companyInfo.getNumber(); 
						String orgSql = " select fid  from t_org_admin where  (fnumber = '"+comNumber+"' and FLAYERTYPEID  = 'mkPGwYLIQ4y/tsiVKLQX8WKCSYg=') or FID= '00000000-0000-0000-0000-000000000000CCE7AED4'  ";
						IRowSet rsCom =  DbUtil.executeQuery(ctx,orgSql);
						if(rsCom == null || rsCom.size() == 0){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"����֯���ͱ���Ϊ����;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
					   }
						
						if (map.get("FHAOCAI")== null || "".equals(map.get("FHAOCAI").toString()) ) { 
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ĲĲ���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FHAOCAI").toString()) && !"1".equals(map.get("FHAOCAI").toString())){
							
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�Ĳ�����ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FYICHI")== null || "".equals(map.get("FYICHI").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���ݲ���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��������ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						
						if (map.get("FYINXING")== null || "".equals(map.get("FYINXING").toString()) ) {  
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ����β���Ϊ��;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						} 
						if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){ 
							successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���������ֻ����0����1;";
							returnMap.put("msg",  successMsg);
							String jsonStr = JSONObject.toJSONString(returnMap);
							return jsonStr;  
						}  
						
						String shebei = map.get("FSHEBEI").toString();
						String haocai = map.get("FHAOCAI").toString(); 
						String yichi = map.get("FYICHI").toString();
						String yinxing = map.get("FYINXING").toString(); 
						
						String suppiyid = info.getId().toString(); 
						 
						String orgid = map.get("FORGNUMBER").toString();
						
						String isgroup = map.get("FISGROUP").toString(); 
						
						String updateSql = "";
						if("0".equals(isgroup)){
							StringBuffer  sqlSup  = new StringBuffer();
							sqlSup.append("/*dialect*/ select distinct  nvl(CFKDCheckBox2,'0')  OLDYICHI, nvl(CFKDCheckBox3,'0')   OLDYINXING ")
							.append("   from T_BD_SupplierPurchaseInfo where   FSupplierID = '"+suppiyid+"' and     FCONTROLUNITID = '"+orgid+"' ");
							IRowSet rowsSup = DbUtil.executeQuery(ctx, sqlSup.toString()); 
							try {
								while (rowsSup.next()) {
									String oldyichi = rowsSup.getString("OLDYICHI"); 
									String oldyinxing = rowsSup.getString("OLDYINXING");  
									map.put("OLDYICHI", oldyichi);
									map.put("OLDYINXING", oldyinxing); 
								} 
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							updateSql = " update�� T_BD_SupplierPurchaseInfo SET  CFKDCheckBox ="+shebei+"  , CFKDCheckBox1="+haocai+" ,CFKDCheckBox2="+yichi+" ,CFKDCheckBox3="+yinxing+"   where  FSupplierID = '"+suppiyid+"' and     FCONTROLUNITID = '"+orgid+"' ";
						}else{
							/*if("00000000-0000-0000-0000-000000000000CCE7AED4".equals(orgid)){
								updateSql = " update�� T_BD_SupplierPurchaseInfo SET  CFKDCheckBox ="+shebei+"  , CFKDCheckBox1="+haocai+" ,CFKDCheckBox2="+yichi+" ,CFKDCheckBox3="+yinxing+"   where  FSupplierID = '"+suppiyid+"' ";
							}else{
								updateSql = " update�� T_BD_SupplierPurchaseInfo SET  CFKDCheckBox ="+shebei+"  , CFKDCheckBox1="+haocai+" ,CFKDCheckBox2="+yichi+" ,CFKDCheckBox3="+yinxing+"   where  FSupplierID = '"+suppiyid+"' and  and  FCONTROLUNITID = '"+orgid+"' ";
							}*/
							StringBuffer  sqlSup  = new StringBuffer();
							sqlSup.append("/*dialect*/ select distinct  nvl(CFKDCheckBox2,'0')  OLDYICHI, nvl(CFKDCheckBox3,'0')   OLDYINXING ")
							.append("   from T_BD_SupplierPurchaseInfo where  FSupplierID = '"+suppiyid+"' ");
							IRowSet rowsSup = DbUtil.executeQuery(ctx, sqlSup.toString());
							
							try {
								while (rowsSup.next()) {
									String oldyichi = rowsSup.getString("OLDYICHI"); 
									String oldyinxing = rowsSup.getString("OLDYINXING");  
									map.put("OLDYICHI", oldyichi);
									map.put("OLDYINXING", oldyinxing); 
								} 
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							updateSql = " update�� T_BD_SupplierPurchaseInfo SET  CFKDCheckBox ="+shebei+"  , CFKDCheckBox1="+haocai+" ,CFKDCheckBox2="+yichi+" ,CFKDCheckBox3="+yinxing+"   where  FSupplierID = '"+suppiyid+"' ";
						}  
						DbUtil.execute(ctx,updateSql); 
						
						loginfo.setStatus(true);
						loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�̴˷�Χͬ���ɹ���");
						DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);


						flag = true;
						successMsg = successMsg+"����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̴˷�Χͬ���ɹ�;";
						 
					}
					
					if(flag){ 
						Map<String, String> retMap= getHisMap(ctx,map,1);
						returnMap.put("code", "success");
						if(retMap.get("result")!=null && !retMap.get("result").equals("")){
							returnMap.put("msg",successMsg+"HIS������Ϣ:"+retMap.get("result"));
						}else{
							returnMap.put("msg",successMsg);
						}
						
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;   
					}else {  
						returnMap.put("code", "success");
						returnMap.put("msg","����Ϊ"+map.get("FNUMBER").toString()+"û���޸Ĳ�����");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;   
					}
				}else {
					
					if (map.get("FOPERTYPE").toString().indexOf("0") < 0) {
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̲����ڣ��������ͱ���Ϊ����������");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr; 
					}
					
					if (map.get("FNAME")== null || "".equals(map.get("FNAME").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�����Ʋ���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					
					if (map.get("FCLASSNAME")== null || "".equals(map.get("FCLASSNAME").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵ķ�����벻��Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					if( !CSSPGroupFactory.getLocalInstance(ctx).exists("where number ='"+ map.get("FCLASSNAME").toString()+"'") ){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵ķ�����벻���ڡ�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					
					
					if (map.get("FISGROUP")== null || "".equals(map.get("FISGROUP").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ȫ����״̬����Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					
					if (map.get("FORGNUMBER")== null || "".equals(map.get("FORGNUMBER").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻��Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					if( !CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where id='"+ map.get("FORGNUMBER").toString()+ "'") ){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������֯���벻���ڡ�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}

					CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
					
					String comNumber = companyInfo.getNumber(); 
					String orgSql = " select fid  from t_org_admin where (fnumber = '"+comNumber+"' and FLAYERTYPEID  = 'mkPGwYLIQ4y/tsiVKLQX8WKCSYg=') or FID= '00000000-0000-0000-0000-000000000000CCE7AED4'  ";
					IRowSet rsCom =  DbUtil.executeQuery(ctx,orgSql);
					if(rsCom == null || rsCom.size() == 0 ){  
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"����֯���ͱ���Ϊ���С�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;   
				    }
					/*if (map.get("FOPENBANK")== null || "".equals(map.get("FOPENBANK").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵Ŀ����в���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					
					if (map.get("FBANKACCOUNT")== null || "".equals(map.get("FBANKACCOUNT").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵������˺Ų���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}  */ 
					
					if (map.get("FBANKS")== null || "".equals(map.get("FBANKS").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�������Ϣ����Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;
					} 
					
					if (map.get("FSTATUS")== null || "".equals(map.get("FSTATUS").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵�״̬����Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					
					String bankList = map.get("FBANKS");
				    String[] arr = bankList.split("\\|");
				    
				    if(arr.length >0 ){
				    	for(int a = 0 ; a < arr.length ; a++ ){
					    	String bankMsg = arr[a];
					    	String[] brr = bankMsg.split(","); 
					    	if(brr.length == 2){
					    		String openbank = brr[0];
						    	String bankCount = brr[1]; 
					    	}else{
					    		returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ��������Ϣ��"+(a+1)+"��"+bankList+"�޷�������");
								String jsonStr = JSONObject.toJSONString(returnMap);
								return jsonStr; 
					    	}  
					    }
				    }else{ 
				    	returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ��������Ϣ"+bankList+"�޷�������");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr; 
				    }
					
					if (map.get("FSHEBEI")== null || "".equals(map.get("FSHEBEI").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					}
					if( !"0".equals(map.get("FSHEBEI").toString()) && !"1".equals(map.get("FSHEBEI").toString())){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��豸����ֻ����0����1��");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					
					if (map.get("FHAOCAI")== null || "".equals(map.get("FHAOCAI").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�ĲĲ���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					if( !"0".equals(map.get("FHAOCAI").toString()) && !"1".equals(map.get("FHAOCAI").toString())){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ�Ĳ�����ֻ����0����1��");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					
					if (map.get("FYICHI")== null || "".equals(map.get("FYICHI").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���ݲ���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ��������ֻ����0����1��");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					
					if (map.get("FYINXING")== null || "".equals(map.get("FYINXING").toString()) ) { 
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ����β���Ϊ�ա�");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					if( !"0".equals(map.get("FYICHI").toString()) && !"1".equals(map.get("FYICHI").toString())){
						returnMap.put("msg", "����Ϊ"+map.get("FNUMBER").toString()+"�Ĺ�Ӧ�̵��Ƿ���������ֻ����0����1��");
						String jsonStr = JSONObject.toJSONString(returnMap);
						return jsonStr;  
					} 
					
					loginfo.setProcessType(DateBaseProcessType.AddNew);
					isAdd = true;
					info = new SupplierInfo();
					if (map.get("FCLASSNAME").toString().equals("G5")) {
						//����Ӧ�̵�FCLASSNAME ��G5��ʱ�� Ҳ�ڿͻ��в���һ������ 
						doCustomerBySupp(ctx, map );
					}
				}
				
				info.setNumber(map.get("FNUMBER").toString());
				info.setName(map.get("FNAME").toString());

				UsedStatusEnum item = null;
				if (map.get("FSTATUS").toString().equals("1")) {
					item = UsedStatusEnum.FREEZED;
				} else {
					item = UsedStatusEnum.APPROVED;
				}
				info.setUsedStatus(item);
				SupplierCompanyInfoInfo company = new SupplierCompanyInfoInfo();
				CompanyOrgUnitInfo companyInfo = new CompanyOrgUnitInfo();
				if (isAdd) {
					SupplierGroupDetailInfo detailinfo = new SupplierGroupDetailInfo();
					CSSPGroupStandardInfo csinfo = CSSPGroupStandardFactory.getLocalInstance(ctx).getCSSPGroupStandardInfo("where number='supplierGroupStandard'");
					detailinfo.setSupplierGroupStandard(csinfo);
					CSSPGroupInfo cssinfo = CSSPGroupFactory.getLocalInstance(ctx).getCSSPGroupInfo("where number ='"+ map.get("FCLASSNAME").toString()+ "' and groupStandard.id='"+ csinfo.getId().toString() + "'");
					detailinfo.setSupplierGroup(cssinfo);
					info.getSupplierGroupDetails().add(detailinfo);
					companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
					company.setCompanyOrgUnit(companyInfo);
					company.setIsFreezePayment(false);
					CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
					company.setSettlementCurrency(currency);
					info.setIsInternalCompany(false); // �Ƿ����ڹ�˾
					info.setInternalCompany(companyInfo);
					info.setBrowseGroup(cssinfo);// ��Ӧ�̷���
					System.out.println("_______________________________________________________��Ӧ������"+ info.getNumber());
					CtrlUnitInfo ctrlorg = new CtrlUnitInfo(); 
					info.put("isImport", true); 
					String cuid = "";
					if((map.get("FISGROUP").toString().equals("1"))){
						cuid = "00000000-0000-0000-0000-000000000000CCE7AED4";
					}else{
						cuid = companyInfo.getCU().getId().toString();
					}
					ctrlorg.setId(BOSUuid.read(cuid));
					
					tempCTX.put(OrgType.Company, companyInfo); //������֯
					tempCTX.put("CompanyInfo", companyInfo);
					tempCTX.put("CurCompanyId", map.get("FORGNUMBER").toString());
					tempCTX.put(OrgType.ControlUnit, ctrlorg);
					
					info.setCU(ctrlorg);
					info.setEffectedStatus(EffectedStatusEnum.EFFECTED);
				}
				if((map.get("FISGROUP").toString().equals("1"))){
					String suppid = SupplierFactory.getLocalInstance(ctx).save(info).toString();
					map.put("supplier", suppid);
				}else{
					String suppid = SupplierFactory.getLocalInstance(tempCTX).save(info).toString();
					map.put("supplier", suppid);
				}

				if(isAdd){
					if (map.get("FISGROUP").toString().equals("1")) {
						Map<String, String> jt = new HashMap<String, String>();
						jt.put("sid", info.getId().toString());
						jt.put("cid","00000000-0000-0000-0000-000000000000CCE7AED4");
						//jt.put("bank", map.get("FOPENBANK").toString());
						//jt.put("bankAccount", map.get("FBANKACCOUNT").toString());
						
						jt.put("bankmsg", map.get("FBANKS").toString());
						
						
						jt.put("FSHEBEI", map.get("FSHEBEI").toString());
						jt.put("FHAOCAI", map.get("FHAOCAI").toString());
						jt.put("FYICHI", map.get("FYICHI").toString());
						jt.put("FYINXING", map.get("FYINXING").toString()); 
						quanJT.add(jt);
					} else if (map.get("FISGROUP").toString().equals("0")) {
						Map<String, String> jt = new HashMap<String, String>();
						jt.put("sid", info.getId().toString());
						jt.put("cid", companyInfo.getCU().getId().toString());
						//jt.put("bank", map.get("FOPENBANK").toString());
						//jt.put("bankAccount", map.get("FBANKACCOUNT").toString());
						
						jt.put("bankmsg", map.get("FBANKS").toString());
						
						jt.put("FSHEBEI", map.get("FSHEBEI").toString());
						jt.put("FHAOCAI", map.get("FHAOCAI").toString());
						jt.put("FYICHI", map.get("FYICHI").toString());
						jt.put("FYINXING", map.get("FYINXING").toString());
						feiJT.add(jt);
					}
				}  
				loginfo.setStatus(true);
			} catch (EASBizException e) { 
				loginfo.setStatus(true);
				loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��ͬ��ʧ�ܡ�");
				try {
					DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
				} catch (EASBizException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				returnMap.put("msg", "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ��ͬ��ʧ�ܡ�");
				String jsonStr = JSONObject.toJSONString(returnMap);
				return jsonStr;
			}
			
		}else{
			returnMap.put("code", "error");
			returnMap.put("msg", "��������Ϊ��");

		}
		//------------
		boolean flag = AppUnit.AssginSupplierByService(ctx, quanJT, feiJT); 
		
		if(flag){
			loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������������ɹ�����"+map.get("FORGNUMBER").toString()+"ҵ����֯��");
			
			returnMap.put("msg", "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������������ɹ�����"+map.get("FORGNUMBER").toString()+"ҵ����֯��"); 
		}else{
			loginfo.setDescription("����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������������β���δ����"+map.get("FORGNUMBER").toString()+"ҵ����֯��");
			returnMap.put("msg", "����Ϊ"+info.getNumber()+"�Ĺ�Ӧ�������������β���δ����"+map.get("FORGNUMBER").toString()+"ҵ����֯��");
		}

		try {
			DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		;
		Map<String, String> retMap= getHisMap(ctx,map,0);
		String retStr = JSONObject.toJSONString(retMap);
		returnMap.put("code", "success");  
		
		if(retMap.get("result")!=null && !retMap.get("result").equals("")){
			returnMap.put("msg","�����ɹ���"+"HIS������Ϣ:"+retMap.get("result"));
		}else{
			returnMap.put("msg","�����ɹ���"+retStr);
		}
		returnMap.put("code", "success"); 
		String jsonStr = JSONObject.toJSONString(returnMap);
		return jsonStr;
	}




	public DateBaseLogInfo getlogInfo(Map<String, String> map,DateBasetype dateBasetype)
	throws BOSException {
		Calendar cal = Calendar.getInstance();
		DateBaseLogInfo loginfo = new DateBaseLogInfo();
		cal.setTime(new Date());
		loginfo.setNumber(cal.getTimeInMillis() + "." + map.get("FNUMBER"));
		loginfo.setName(map.get("FNUMBER").toString());
		loginfo.setSimpleName(map.get("FNUMBER").toString());
		loginfo.setDateBaseType(dateBasetype);
		String version = String.valueOf(cal.getTimeInMillis());
		loginfo.setVersion(version);
		loginfo.setUpdateDate(new Date());
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updatetime = sdf1.format(new Date()).substring(11);
		loginfo.setUpdatetime(Time.valueOf(updatetime));
		return loginfo;
	}
	
	public Map<String, String> getHisMap(Context ctx,Map<String, String> map,int  type) {
		HashMap<String, String> hismap = new  HashMap<String, String>();
		//String yichi = map.get("FYICHI").toString();
		//String yinxing = map.get("FYINXING").toString();  
		String isgroupOa = map.get("FISGROUP").toString();  
		
		
		String oatype = map.get("FOPERTYPE").toString();  
		String osstatus = map.get("FSTATUS").toString();  
		 
		boolean flag = false;
		hismap.put("fstatus", map.get("FSTATUS").toString().equals("1")?"1" :"0");

		if(type == 0){//����
			hismap.put("fupdatetype", "0");
		}else{//�޸�
			hismap.put("fupdatetype", "1");
		}  
		
		System.out.println("*********************getHisMap ���� flag:"+flag);
		try {
//			if(oatype.indexOf("7") >=0  ){
//				String oldyichi = map.get("OLDYICHI").toString();
//				String oldyinxing = map.get("OLDYINXING").toString();  
//				
//				if("0".equals(oldyichi) && "0".equals(oldyinxing) && "0".equals(yichi) && "0".equals(yinxing)  ){
//					 
//					System.out.println(map.get("FNUMBER").toString()+"==============================��Ӧ�̲���Ҫ���ݸ�HIS===========");
//				}else{
//					String suppid =  map.get("supplier").toString();
//					hismap = getSupById(  ctx,   suppid , map,  hismap ,isgroupOa );
//					flag = true;
//					if("0".equals(yichi) && "0".equals(yinxing)){
//						hismap.put("fupdatetype", "2");
//					}
//				}
//			}else{
//				String suppid =  map.get("supplier").toString();
//				String cityid = map.get("FORGNUMBER").toString();
//				StringBuffer  sqlSup  = new StringBuffer();
//				
//				String oldyichi ="0"; 
//				String oldyinxing = "0";    
//				if("0".equals(isgroupOa)){ 
//					sqlSup.append("/*dialect*/ select distinct  nvl(CFKDCheckBox2,'0')  OLDYICHI, nvl(CFKDCheckBox3,'0')   OLDYINXING ")
//					.append("   from T_BD_SupplierPurchaseInfo  where FSupplierID = '"+suppid+"' and     FCONTROLUNITID = '"+cityid+"' "); 
//					 
//				}else{ 
//					sqlSup.append("/*dialect*/ select distinct nvl(CFKDCheckBox2,'0')  OLDYICHI, nvl(CFKDCheckBox3,'0')   OLDYINXING ")
//					.append("   from T_BD_SupplierPurchaseInfo  where FSupplierID = '"+suppid+"' ");
//				}  
//				IRowSet rowsSup = DbUtil.executeQuery(ctx, sqlSup.toString()); 
//				while (rowsSup.next()) {
//					oldyichi = rowsSup.getString("OLDYICHI"); 
//					oldyinxing = rowsSup.getString("OLDYINXING");   
//				} 
//				if("1".equals(oldyichi) || "1".equals(oldyinxing) ){
//					hismap = getSupById(  ctx,   suppid , map,  hismap ,isgroupOa );
//					flag = true;
//				}
//				
//			}
			
			String suppid =  map.get("supplier").toString();
			hismap = getSupById(  ctx,   suppid , map,  hismap ,isgroupOa );
			flag = true;
			System.out.println("*********************getHisMap ���� hismap:"+hismap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if(flag){
			List<Map<String,String>> eMps = new ArrayList<Map<String,String>>();
			eMps.add(hismap);
			//Map<String,Object> mp = new HashMap<String,Object>();
			//mp.put("subList", eMps);
			
			System.out.println("########  body ########"+JSONObject.toJSONString(eMps));
			String result =  sendMessageToHISPost(JSONObject.toJSONString(eMps),1);
			logger.info("���͹�Ӧ��,"+hismap+"֪ͨ��hisϵͳ��result��" + result);
			System.out.println("########  result ########"+result);
			hismap.put("result", result);
		}
		try {
			//CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='jbYAAAMU2SvM567U''");
			 
			//Map<String, String> mapNew = new HashMap<String, String>();
			//String supsql = " /*dialect*/ SELECT supplier.fid  fId, supplier.fnumber fNumber, supplier.fname_l2  fName ,'' fOpenBank , '' fBankAccount  ,  "+
			/*  " cuser.fname_l2  fCreator ,  to_char( supplier.FCREATETIME ,'yyyy-mm-dd' ) fCreateTime ,to_char( supplier.FLASTUPDATETIME  ,'yyyy-mm-dd' ) fUpdateTime   "+
			  " FROM   T_BD_Supplier supplier   "+
			  " inner join  T_PM_User  cuser on cuser.fid= supplier.FCREATORID  " +
			  " inner join  T_BD_SupplierCompanyInfo supcom  on supcom.FSUPPLIERID  = supplier.fid and supcom.FComOrgID  = 'jbYAAAMU2SvM567U'"+
	  		  " where  supplier.fid  = '"+map.get("supplier").toString()+"' ";
			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,supsql);
			if(rs!=null && rs.size() > 0){
				  
				while(rs.next()){	 
					mapNew.put("fId",rs.getString("FID") );
					mapNew.put("fNumber",rs.getString("FNUMBER") );
					mapNew.put("fName",rs.getString("FNAME") );
					mapNew.put("fOpenBank",rs.getString("FOPENBANK") );
					mapNew.put("fBankAccount",rs.getString("FBANKACCOUNT") );
					mapNew.put("fCreator",rs.getString("FCREATOR") );
					mapNew.put("fCreateTime",rs.getString("FCREATETIME") );
					mapNew.put("fUpdateTime",rs.getString("FUPDATETIME") ); 
					
					mapNew.put("fIsGroup",map.get("FISGROUP") ); 
					mapNew.put("fOrgNumber",companyInfo.getNumber() ); 
					mapNew.put("fOrgName",companyInfo.getName() ); 
					mapNew.put("fStatus",hismap.get("fstatus") ); 
					mapNew.put("fOrgtId",companyInfo.getId().toString() ); 
					mapNew.put("fUpdateType",hismap.get("fupdatetype") );  
					
				}
				
				String datajsonStr = JSONObject.toJSONString(mapNew);
				ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
				is.syncDateByType( 2 , datajsonStr , Integer.parseInt(hismap.get("fupdatetype"))  , map.get("fName") ,map.get("supplier") ); 
			}*/
			System.out.println("########  supplier ########"+map.get("supplier"));
			ISyncDataEASFacade is = SyncDataEASFacadeFactory.getLocalInstance(ctx);
			if("0".equals(oatype) ){ //����
				is.syncDateByType( 2 , "" , 0  , map.get("fName") ,map.get("supplier") ); 
			}else if(oatype.indexOf("2") >=0  ){ //�޸�
				if("1".equals(osstatus)){
					is.syncDateByType( 2 , "" , 2 , map.get("fName") ,map.get("supplier") ); //����
				}else{
					is.syncDateByType( 2 , "" , 1 , map.get("fName") ,map.get("supplier") ); //����
				}
			}else if(oatype.indexOf("1") >=0  || oatype.indexOf("5") >=0 || oatype.indexOf("6") >=0 ){ //�޸�
				is.syncDateByType( 2 , "" , 1  , map.get("fName") ,map.get("supplier") ); 
			}
			
			
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hismap; 
		
	}
	
	public HashMap<String, String> getSupById(Context ctx, String suppid , Map<String, String> map , HashMap<String, String> hismap, String isgroupOa 
			) throws BOSException, SQLException {
		
		
		if("0".equals(isgroupOa)){
			StringBuffer sql = new StringBuffer();
			sql.append("/*dialect*/ select a.FID as FID, c.fname_l2 as FCLASSNAME,a.FNumber as FNUMBER,a.FName_L2 as FNAME,'' as  FOPENBANK,'' as FBANKACCOUNT,")
			.append("a.FCreatorID as FCREATOR,(CASE WHEN b.FNumber='M' THEN 1 ELSE 0 END) as FISGROUP, ")
			.append("b.FNumber as FORGNUMBER,b.FName_L2 as FORGNAME,(case a.FUsedStatus when 1  then 0 when 0 then 0  else 2 end  ) as FupdateType,b.FID as FORGTID,0 as FSIGN,''as FSYNLOG,0 as FMAILSIGN,")
			.append("to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime ,")
			.append("(case a.FUsedStatus when 3  then 1 else 0 end  ) as FStatus  from T_BD_Supplier a  INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID=b.FID")
			.append(" inner join T_BD_CSSPGroup c on a.FBrowseGroupID = c.fid  where  a.fid = '"+suppid+"' ");
			IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
			
			while (rows.next()) {
				String fid = rows.getString("FID");
				String classname = rows.getString("FCLASSNAME");
				String name = rows.getString("FNAME");
				String number = rows.getString("FNUMBER"); 
				String isgroup = rows.getString("FISGROUP"); 
				
				
				String updateType = rows.getString("FupdateType");
				if("2".equals(updateType)){
					hismap.put("fupdatetype", "2");
				}

				String createtime = rows.getString("FCreateTime");
				String updatetime = rows.getString("FUpdateTime");
				String creator = rows.getString("FCREATOR");  
				
				hismap.put("fid",fid );
				hismap.put("fclassname",classname );
				hismap.put("fname",name );
				hismap.put("fnumber",number );
				 
				hismap.put("fisgroup",isgroup );	 
				
				hismap.put("fcreatetime",createtime );
				hismap.put("fupdatetime",updatetime );
				hismap.put("fcreator",creator );  
			}
			
			CompanyOrgUnitInfo companyInfo;
			try {
				companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
			
				hismap.put("forgnumber",companyInfo.getNumber() );
				hismap.put("forgname",companyInfo.getName() );
				hismap.put("forgtid",companyInfo.getId().toString() );
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
 
		}else if("1".equals(isgroupOa)){
			StringBuffer sql = new StringBuffer();
			sql.append("/*dialect*/ select a.FID as FID, c.fname_l2 as FCLASSNAME,a.FNumber as FNUMBER,a.FName_L2 as FNAME,'' as  FOPENBANK,'' as FBANKACCOUNT,")
			.append("a.FCreatorID as FCREATOR,(CASE WHEN b.FNumber='M' THEN 1 ELSE 0 END) as FISGROUP,b.fid FORGTID ,b.fnumber FORGNUMBER,b.fname_l2 FORGNAME,")
			.append("to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime ,")
			.append("(case a.FUsedStatus when 3  then 1 else 0 end  ) as FStatus  from T_BD_Supplier a  INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID=b.FID")
			.append(" inner join T_BD_CSSPGroup c on a.FBrowseGroupID = c.fid  where  a.fid = '"+suppid+"' ");
			IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
			
			while (rows.next()) {
				String fid = rows.getString("FID");
				String classname = rows.getString("FCLASSNAME");
				String name = rows.getString("FNAME");
				String number = rows.getString("FNUMBER"); 
				String isgroup = rows.getString("FISGROUP"); 
				
				
				

				String createtime = rows.getString("FCreateTime");
				String updatetime = rows.getString("FUpdateTime");
				String creator = rows.getString("FCREATOR");  
				
				
				hismap.put("fid",fid );
				hismap.put("fclassname",classname );
				hismap.put("fname",name );
				hismap.put("fnumber",number );
				 
				hismap.put("fisgroup",isgroup );	 
				
				hismap.put("fcreatetime",createtime );
				hismap.put("fupdatetime",updatetime );
				hismap.put("fcreator",creator );  
				 
				String orgnumber = rows.getString("FORGNUMBER");
				String orgname = rows.getString("FORGNAME");
				String orgid = rows.getString("FORGTID");  
				hismap.put("forgnumber",orgnumber );
				hismap.put("forgname",orgname );
				hismap.put("forgtid",orgid ); 
				 
			}
		}
		
		return  hismap;
		
	}
	
	public void doCustomerBySupp(Context ctx, Map<String, String> map) throws BOSException {
		// �����־��Ϣ
		DateBaseLogInfo loginfo = getlogInfo(map,DateBasetype.Customer);
		CustomerInfo info = null;
		try {
			boolean isAdd = false;
			if (CustomerFactory.getLocalInstance(ctx).exists("where number='" + map.get("FNUMBER") + "'")) {
				info = CustomerFactory.getLocalInstance(ctx).getCustomerInfo("where number='" + map.get("FNUMBER") + "'");
				if (map.get("FSTATUS").toString().equals("1")) {// ɾ��
					loginfo.setProcessType(DateBaseProcessType.DisAble);
					if (info.getUsedStatus().getValue() == 1) {// ����
						CustomerFactory.getLocalInstance(ctx).freezed(new ObjectUuidPK(info.getId()), true);
					}
					loginfo.setStatus(true);
					DateBaseLogFactory.getLocalInstance(ctx).save(loginfo); 
				} else {// �޸�UsedStatusEnum
					if (info.getUsedStatus() == UsedStatusEnum.APPROVED) {
						loginfo.setProcessType(DateBaseProcessType.Update);
						DbUtil.execute(ctx,"update t_bd_customer set FUsedStatus = 0 where  fnumber ='"+ info.getNumber() + "' ");

						info.setName(map.get("FNAME").toString());
						CustomerFactory.getLocalInstance(ctx).update(new ObjectUuidPK(info.getId()), info);

						DbUtil.execute(ctx,"update t_bd_customer set FUsedStatus = 1 where  fnumber ='"+ info.getNumber() + "' ");

						loginfo.setStatus(true);
						DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);

					} else if (info.getUsedStatus() == UsedStatusEnum.UNAPPROVE) {

						info.setName(map.get("FNAME").toString());
						CustomerFactory.getLocalInstance(ctx).update(new ObjectUuidPK(info.getId()), info);

						loginfo.setStatus(true);
						DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
					}

					/*if (map.get("FBANKACCOUNT")!= null &&  !"".equals(map.get("FBANKACCOUNT").toString()) ) { 
						 
						String bank = map.get("FBANKACCOUNT");
						String suppiyid = info.getId().toString(); 
						
						String updateBank = " update T_BD_CustomerCompanyBank  set  FBankAccount = '"+bank+"' where  FCustomerCompanyInfoID in   (select  fid  from  T_BD_CustomerCompanyInfo  where  FCustomerID ='"+suppiyid+"' ) and FBankAccount != '"+bank+"' ";
						DbUtil.execute(ctx,updateBank);
					 
					}*/
				}
			} else {
				isAdd = true;
				info = new CustomerInfo();
				loginfo.setProcessType(DateBaseProcessType.AddNew);
				
				info.setNumber(map.get("FNUMBER").toString());// ���ñ���
				info.setName(map.get("FNAME").toString());// ��������

				UsedStatusEnum item = null;
				if (map.get("FSTATUS").toString().equals("1")) {
					item = UsedStatusEnum.FREEZED;
				} else {
					item = UsedStatusEnum.APPROVED;
				}
				info.setUsedStatus(item);

				System.out.println("-------supp-------------�ͻ�����"+ map.get("FNUMBER") + "------------------"+ map.get("FNAME"));
				CustomerCompanyInfoInfo company = new CustomerCompanyInfoInfo();
				if (isAdd) {
					CustomerGroupDetailInfo detailinfo = new CustomerGroupDetailInfo();
					CSSPGroupStandardInfo csinfo = CSSPGroupStandardFactory.getLocalInstance(ctx).getCSSPGroupStandardInfo("where number='customerGroupStandard'");
					detailinfo.setCustomerGroupStandard(csinfo);
					CSSPGroupInfo cssinfo = CSSPGroupFactory.getLocalInstance(ctx).getCSSPGroupInfo("where number='K2' and groupStandard.id='"+ csinfo.getId().toString() + "'");
					detailinfo.setCustomerGroup(cssinfo);
					detailinfo.setCustomer(info);
					info.getCustomerGroupDetails().add(detailinfo);
					
					CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where id='"+ map.get("FORGNUMBER").toString()+ "'");
					company.setCompanyOrgUnit(companyInfo);
					company.setIsFreezeIssueInvoice(false);
					CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
					company.setSettlementCurrency(currency);
					info.setIsInternalCompany(false);// �Ƿ����ڹ�˾
					info.setBrowseGroup(cssinfo);// �ͻ���������

					CtrlUnitInfo ctrlorg = null;
					if (map.get("FISGROUP").toString().equals("1")) {
						ctrlorg = new CtrlUnitInfo();
						ctrlorg.setId(BOSUuid.read("00000000-0000-0000-0000-000000000000CCE7AED4"));
						info.setAdminCU(ctrlorg);
					} else if (map.get("FISGROUP").toString().equals("0")) {
						info.setAdminCU(companyInfo.getCU());
					}

				}
				CustomerFactory.getLocalInstance(ctx).save(info); 

				if (isAdd) {
					System.out.println("--------------------�ͻ�������֯"+ company.getCompanyOrgUnit().getName());
					CustomerSaleInfoInfo customerSaleInfoInfo = new CustomerSaleInfoInfo();
					SaleOrgUnitInfo saleOrgUnitInfo = SaleOrgUnitFactory.getLocalInstance(ctx).getSaleOrgUnitInfo(new ObjectUuidPK(map.get("FORGNUMBER").toString()));
					customerSaleInfoInfo.setSaleOrgUnit(saleOrgUnitInfo);
					customerSaleInfoInfo.setBillingOrgUnit(info);// Ӧ�տͻ�
					customerSaleInfoInfo.setDeliverOrgUnit(info);// �ͻ��ͻ�
					customerSaleInfoInfo.setSettlementOrgUnit(info);// �տ�ͻ�
					customerSaleInfoInfo.setName(map.get("FNAME").toString());
					customerSaleInfoInfo.setCustomer(info);
					CustomerSaleInfoFactory.getLocalInstance(ctx).save(customerSaleInfoInfo);

					System.out.println("--------------------�ͻ�������֯"+ company.getCompanyOrgUnit().getName());
					company.setCustomer(info);
					CustomerCompanyInfoFactory.getLocalInstance(ctx).save(company);

					System.out.println("--------------------�ͻ�����--------------------------------------");
					
					String bankList = map.get("FBANKS");
				    String[] arr = bankList.split("\\|");
				    
				    if(arr.length >0 ){
				    	for(int a = 0 ; a < arr.length ; a++ ){
					    	String bankMsg = arr[a];
					    	String[] brr = bankMsg.split(","); 
					    	if(brr.length == 2){
					    		String openbank = brr[0];
						    	String bankCount = brr[1]; 
						    	CustomerCompanyBankInfo bank = new CustomerCompanyBankInfo();
								bank.setBank(openbank);
								bank.setBankAccount(bankCount);
								bank.setCustomerCompanyInfo(company);
								CustomerCompanyBankFactory.getLocalInstance(ctx).save(bank);
					    	}  
					    }
				    } 
				}
				
				loginfo.setStatus(true);
				DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
				System.out.println("---------------supp-----------------------ִ�����");
				logger.error("--------------------supp------------------ִ�����");
			}
			
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			// ʧ��֮���޸�ͬ�����
			System.out.println("-----------------supp---------------------����ΪG5�Ĺ�Ӧ�����ɶ�Ӧ�ͻ�ʧ��");
			logger.error("----------------------supp----------------����ΪG5�Ĺ�Ӧ�����ɶ�Ӧ�ͻ�ʧ��");
			e.printStackTrace();
		}

	}





	@Override
	protected void _newOrgAddSupp(Context ctx, String data) throws BOSException {
		// TODO Auto-generated method stub
		
		
		List<Map<String,String>> eMps = new ArrayList<Map<String,String>>();
		
		//�����
		String citySql = ""; 
		 
		//super._newOrgAddSupp(ctx, data);
		
		String cityid = "jbYAAAIgaLfM567U";
		AppUnit.newCityAddSupp( ctx, cityid);
		StringBuffer sql = new StringBuffer();
		sql.append("/*dialect*/ select a.FID as FID, c.fname_l2 as FCLASSNAME,a.FNumber as FNUMBER,a.FName_L2 as FNAME,'' as  FOPENBANK,'' as FBANKACCOUNT, ")
		.append(" a.FCreatorID as FCREATOR,(CASE WHEN b.FNumber='M' THEN 1 ELSE 0 END) as FISGROUP,")
		.append(" b.FNumber as FORGNUMBER,b.FName_L2 as FORGNAME,(case a.FUsedStatus when 1  then 0 when 0 then 0  else 2 end  ) as FupdateType,b.FID as FORGTID,0 as FSIGN,''as FSYNLOG,0 as FMAILSIGN, ")
		.append(" to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime , ")
		.append(" (case a.FUsedStatus when 3  then 1 else 0 end  ) as FStatus  from T_BD_Supplier a  INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID=b.FID ")
		.append(" inner  join  T_BD_Supplierpurchaseinfo suppur2  on   suppur2.FSupplierID = a.fid and   suppur2.FCONTROLUNITID  = '"+cityid+"'  and suppur2.FPURCHASEORGID  =suppur2.FCONTROLUNITID INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID=b.FID  ")
		.append(" inner join T_BD_CSSPGroup c on a.FBrowseGroupID = c.fid  where  a.FUsedStatus = 1  and  (suppur2.CFKDCheckBox2 =1 or suppur2.CFKDCheckBox3=1 )  ");
		IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
		
		try {
			while (rows.next()) {
				String fid = rows.getString("FID");
				String classname = rows.getString("FCLASSNAME");
				String name = rows.getString("FNAME");
				String number = rows.getString("FNUMBER"); 
				String isgroup = rows.getString("FISGROUP"); 
				
				String orgnumber = rows.getString("FORGNUMBER");
				String orgname = rows.getString("FORGNAME");
				String orgid = rows.getString("FORGTID");  
				String updateType = rows.getString("FupdateType");
				

				String createtime = rows.getString("FCreateTime");
				String updatetime = rows.getString("FUpdateTime");
				String creator = rows.getString("FCREATOR");  
				
				HashMap<String, String> hismap = new  HashMap<String, String>();  
				 
				hismap.put("FSTATUS", "0");
				hismap.put("FID",fid );
				hismap.put("FCLASSNAME",classname );
				hismap.put("FNAME",name );
				hismap.put("FNUMBER",number );
				
				hismap.put("FISGROUP",isgroup );	
				
				hismap.put("FORGNUMBER",orgnumber );
				hismap.put("FORGNAME",orgname );
				hismap.put("FORGTID",orgid );
				hismap.put("FUPDATETYPE",updateType );
				
				hismap.put("FCREATETIME",createtime );
				hismap.put("FUPDATETIME",updatetime );
				hismap.put("FCREATOR",creator );  
				
				eMps.add(hismap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		Map<String,Object> mp = new HashMap<String,Object>();
		mp.put("subList", eMps);
		
		System.out.println("########  body ########"+JSONObject.toJSONString(mp));
		String result =  sendMessageToHISPost(JSONObject.toJSONString(mp),1);
		logger.info("��������֮���͹�Ӧ��,"+mp+"֪ͨ��hisϵͳ��result��" + result);
		System.out.println("########  result ########"+result);
		
		//�鹫˾

		//AppUnit.newCompanyAddSupp( ctx, "jbYAAAJY+5DM567U");
	}
	
	

	 /**
    * ��ָ�� URL ����POST����������
    * 
    * @param url
    *            ��������� URL
    * @param param
    *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
    * @param isproxy
    *               �Ƿ�ʹ�ô���ģʽ
    * @return ������Զ����Դ����Ӧ���
    */
	public static String sendMessageToHISPost(String param,int oper) {
		OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";  
        if(url != null && !"".equals(url)){
        	try {
        		URL realUrl = new URL(url);
   	            HttpURLConnection conn =  (HttpURLConnection) realUrl.openConnection();
   	             // �򿪺�URL֮�������
   	            // ����POST�������������������
   	            conn.setDoOutput(true);
   	            conn.setDoInput(true);
   	            conn.setRequestMethod("POST");    // POST����
   	            // ����ͨ�õ���������
   	            conn.setRequestProperty("accept", "*/*");
   	            conn.setRequestProperty("connection", "Keep-Alive");
   	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
   	          	conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
   	            conn.connect();
   	            // ��ȡURLConnection�����Ӧ�������
   	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
   	            // �����������
   	            out.write(param);
   	            System.out.println("param------------------------"+param);
   	            // flush������Ļ���
   	            out.flush();
   	            // ����BufferedReader����������ȡURL����Ӧ
   	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
   	            String line;
   	            while ((line = in.readLine()) != null) {
   	                result += line;
   	            }
   	        } catch (Exception e) {
   	            System.out.println("���� POST ��������쳣��"+e);
   	            e.printStackTrace();
   	        }
   	        //ʹ��finally�����ر��������������
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





	@Override
	protected void _DisAbleSupp(Context ctx) throws BOSException {
		// TODO Auto-generated method stub 
		String sqlSup = "/*dialect*/ select distinct   CFSupid SUPID  from  CT_CUS_DisSupplier  ";  
		 
		IRowSet rowsSup = DbUtil.executeQuery(ctx, sqlSup); 
		try {
			while (rowsSup.next()) {
				String  supid = rowsSup.getString("SUPID");  
				boolean  flag = true;
				try{
					SupplierFactory.getLocalInstance(ctx).freezed(new ObjectUuidPK(supid), true); 
				}catch (Exception e) {
					// TODO: handle exception
					System.out.println("===================="+e.getMessage());  
					flag =false;
				}
				
				if(flag){
					String deleteSql = " delete from  CT_CUS_DisSupplier where CFSupid = '"+supid+"'";
					DbUtil.execute(ctx, deleteSql); 
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}    
	

	
	
}