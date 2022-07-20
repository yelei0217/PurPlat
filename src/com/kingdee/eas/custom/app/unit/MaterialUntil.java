package com.kingdee.eas.custom.app.unit;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectStringPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.eas.basedata.assistant.KAClassficationFactory;
import com.kingdee.eas.basedata.assistant.MeasureUnitCollection;
import com.kingdee.eas.basedata.assistant.MeasureUnitFactory;
import com.kingdee.eas.basedata.master.material.EquipmentPropertyEnum;
import com.kingdee.eas.basedata.master.material.IMaterial;
import com.kingdee.eas.basedata.master.material.MaterialFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupCollection;
import com.kingdee.eas.basedata.master.material.MaterialGroupFactory;
import com.kingdee.eas.basedata.master.material.MaterialGroupInfo;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.basedata.master.material.UsedStatusEnum;
import com.kingdee.eas.basedata.org.CtrlUnitFactory;
import com.kingdee.eas.basedata.org.CtrlUnitInfo;
import com.kingdee.eas.common.EASBizException;

public class MaterialUntil {

	public String doCreateMaterial(Context ctx, String data) throws BOSException{
		
		Map map = (Map) JSONObject.parse(data);
		String operType = map.get("operType").toString();
		 
		IMaterial imbiz = MaterialFactory.getLocalInstance(ctx);
		String  error = new String();
		com.alibaba.fastjson.JSONArray  jsonArr = (com.alibaba.fastjson.JSONArray) map.get("data");     
		boolean flag = true;
		int  size = jsonArr.size();
		for( int i = 0 ; i < size ; i++ ){
			Map dataMap = (Map) jsonArr.get(i);  
			
			Date credate = new Date();
			Date update = new Date();
			if (dataMap.get("fNumber")== null || "".equals(dataMap.get("fNumber").toString()) ) { 
				error = error+ "���ϱ��벻��Ϊ��;"; flag = false;
				continue;
			} 
			String  number  = dataMap.get("fNumber").toString() ;
			try {
				if ( !"0".equals(operType) && imbiz.exists("where number = '"+number+"'") ) { 
					error = error+ "���ϱ����Ѵ���;"; flag = false;
					continue;
				}
			} catch (EASBizException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (dataMap.get("fName")== null || "".equals(dataMap.get("fName").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����Ʋ���Ϊ��"; flag = false;
				continue;
			}  
			if (dataMap.get("fModel")== null || "".equals(dataMap.get("fModel").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"���ͺŲ���Ϊ��";  flag = false;
				continue;
			}
			if (dataMap.get("fArtNo")== null || "".equals(dataMap.get("fArtNo").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ��Ų���Ϊ��"; flag = false;
				continue;
			}
			
			if (dataMap.get("fBrand")== null || "".equals(dataMap.get("fBrand").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"��Ʒ�Ʋ���Ϊ��"; flag = false;
				continue;
			}
			
			if (dataMap.get("fCreateTime")== null || "".equals(dataMap.get("fCreateTime").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĵ���ʱ�䲻��Ϊ��"; flag = false;
				continue;
			}
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				credate = sdf1.parse(createTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĵ���ʱ���ʽ����ȷ;"; flag = false;
				continue;
				
			}
			if (dataMap.get("")== null || "".equals(dataMap.get("fUpdateTime").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"������޸�ʱ�䲻��Ϊ��"; flag = false;
				continue;
			}
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			try {
				update = sdf1.parse(updateTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"������޸�ʱ���ʽ����ȷ;"; flag = false;
				continue;
				
			}
			
			if (dataMap.get("fKAClass")== null || "".equals(dataMap.get("fKAClass").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�ļ��˷��಻��Ϊ��"; flag = false;
				continue;
			}
			try {
				if(!KAClassficationFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fKAClass").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�ļ��˷��಻����"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dataMap.get("fBaseUnit")== null || "".equals(dataMap.get("fBaseUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ���������λ����Ϊ��"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fBaseUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ļ���������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fInvUnit")== null || "".equals(dataMap.get("fInvUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ŀ�������λ����Ϊ��"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fInvUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ŀ�������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (dataMap.get("fPurUnit")== null || "".equals(dataMap.get("fPurUnit").toString()) ) { 
				error = error+ "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĳɹ�������λ����Ϊ��"; flag = false;
				continue;
			}
			
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fPurUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�Ĳɹ�������λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dataMap.get("fSaleUnit")== null || "".equals(dataMap.get("fSaleUnit").toString()) ) { 
				error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����ۼ�����λ����Ϊ��"; flag = false;
				continue;
			} 
			try {
				if(!MeasureUnitFactory.getLocalInstance(ctx).exists(" where number = '"+dataMap.get("fSaleUnit").toString()+"'")){
					error = error+  "����Ϊ"+dataMap.get("FNUMBER").toString()+"�����ۼ�����λ������"; flag = false;
					continue;
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				MaterialInfo ma = createInfo(  ctx,   dataMap );
				IObjectPK pk = imbiz.addnew(ma);
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		
		return data;
		
	}
	
	
	public static MaterialInfo createInfo(Context ctx, Map dataMap ) throws BOSException, EASBizException, ParseException {
		MaterialInfo material = null;

		String materialGroup = dataMap.get("fMaterialGroup").toString();
		MaterialGroupInfo mgInfo = null;
		EntityViewInfo viewInfo = new EntityViewInfo();
		FilterInfo filter = new FilterInfo();
		filter.getFilterItems().add(new FilterItemInfo("number", materialGroup,CompareType.EQUALS));
		viewInfo.setFilter(filter);
		MaterialGroupCollection coll = MaterialGroupFactory.getLocalInstance(ctx).getMaterialGroupCollection(viewInfo);
		if ((coll != null) && (coll.size() > 0)) {
			mgInfo = coll.get(0);
			material = new MaterialInfo();
			material.setName(dataMap.get("fName").toString());
			material.setNumber(dataMap.get("fNumber").toString());
			material.put("isImport", Boolean.valueOf(true));
			material.put("notSupportRule", Boolean.valueOf(true));
			
			MeasureUnitCollection measunit= MeasureUnitFactory.getLocalInstance(ctx).getMeasureUnitCollection(" where number = '"+dataMap.get("fBaseUnit").toString()+"'");
			
			material.setBaseUnit(measunit.get(0));
			material.setMaterialGroup(mgInfo);
			CtrlUnitInfo cuInfo = CtrlUnitFactory.getLocalInstance(ctx).getCtrlUnitInfo(new ObjectStringPK("00000000-0000-0000-0000-000000000000CCE7AED4"));
			
			material.setAdminCU(cuInfo);
			material.setCU(cuInfo);
			material.setEquipProperty(EquipmentPropertyEnum.DEFAULT);
			material.setEffectedStatus(2);
			material.setStatus(UsedStatusEnum.APPROVED);
			material.setVersion(1);
			material.setPricePrecision(6);
			
			String createTime = dataMap.get("fCreateTime").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf1.parse(createTime); 
			Timestamp ts = new Timestamp(date.getTime());
			material.setCreateTime(ts);
			
			String updateTime = dataMap.get("fUpdateTime").toString(); 
			Date updatedate = sdf1.parse(updateTime); 
			Timestamp updatets = new Timestamp(updatedate.getTime());
			material.setCreateTime(updatets);
			
			material.setModel(dataMap.get("fModel").toString());
			
			//material.setModel(dataMap.get("fModel").toString());
			
			material.put("huohao", dataMap.get("fArtNo").toString());
			material.put("pinpai", dataMap.get("fBrand").toString());
			 
			
		}
		return material;
	}
	
}
