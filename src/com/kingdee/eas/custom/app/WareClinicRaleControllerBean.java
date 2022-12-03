package com.kingdee.eas.custom.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.IWareClinicRaleEntryDEntry;
import com.kingdee.eas.custom.WareClinicRaleEntryCollection;
import com.kingdee.eas.custom.WareClinicRaleEntryDEntryCollection;
import com.kingdee.eas.custom.WareClinicRaleEntryDEntryFactory;
import com.kingdee.eas.custom.WareClinicRaleEntryDEntryInfo;
import com.kingdee.eas.custom.WareClinicRaleEntryInfo;
import com.kingdee.eas.custom.WareClinicRaleInfo;
import com.kingdee.eas.custom.app.unit.PurPlatUtil;
import com.kingdee.eas.custom.rest.HTTPSClientUtil;
import com.kingdee.eas.custom.rest.HTTPSTrustClient;
import com.kingdee.eas.custom.rest.InterfaceResource;

public class WareClinicRaleControllerBean extends AbstractWareClinicRaleControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8636879096822899169L;
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.WareClinicRaleControllerBean");

	@Override
	protected IObjectPK _save(Context ctx, IObjectValue model)
			throws BOSException, EASBizException {
		WareClinicRaleInfo info = (WareClinicRaleInfo) model;
		WareClinicRaleEntryCollection ecoll = info.getEntrys();
		Iterator eit  = ecoll.iterator();
		List<Map<String,Object>> sendLsit =new ArrayList<Map<String,Object>>();
		boolean orgCountError = false;
		Set orgIdSet =new HashSet();
		int totalCount =0;
		IWareClinicRaleEntryDEntry dbiz = WareClinicRaleEntryDEntryFactory.getLocalInstance(ctx);
		while(eit.hasNext()){
			WareClinicRaleEntryInfo eInfo = (WareClinicRaleEntryInfo) eit.next();
			if(eInfo.getDEntrys() != null && eInfo.getDEntrys().size() > 0){
				WareClinicRaleEntryDEntryCollection dcoll = eInfo.getDEntrys();
				Iterator dit =	dcoll.iterator();
				while(dit.hasNext()){
					WareClinicRaleEntryDEntryInfo dInfo = (WareClinicRaleEntryDEntryInfo) dit.next();
					Map<String,Object> mnp = new HashMap<String,Object>();
					Map<String,Object> mnpd = new HashMap<String,Object>();
					mnpd.put("easPurId", dInfo.getClinic().getId().toString());
					mnpd.put("easCenId", eInfo.getWarehouse().getId().toString());
					mnpd.put("status",dInfo.getDataState().getValue());
					mnp.put("data", mnpd);
					
					if(dInfo.getId()!=null && !"".equals(dInfo.getId().toString())){
						WareClinicRaleEntryDEntryInfo oInfo =dbiz.getWareClinicRaleEntryDEntryInfo(new ObjectUuidPK(BOSUuid.read(dInfo.getId().toString())));
						if(oInfo.getDataState()!=dInfo.getDataState() || oInfo.getClinic().getId()!= oInfo.getClinic().getId()) {
							mnp.put("operType", 2); 
							sendLsit.add(mnp);
						}
					}else{
						mnp.put("operType", 0);
						sendLsit.add(mnp);
					}
					
//					totalCount = totalCount + 1;
//					orgIdSet.add(dInfo.getClinic().getId().toString()); 
				} 
			}else{
	 			throw new com.kingdee.eas.common.EASBizException(com.kingdee.eas.common.EASBizException.CHECKBLANK,new Object[] {"对应门诊"});
			}
		}
//		if(orgIdSet.size() != totalCount)
// 			throw new com.kingdee.eas.common.EASBizException(com.kingdee.eas.common.EASBizException.CHECKNUMDUP,new Object[] {"门诊编码"});
		if(sendLsit !=null && sendLsit.size() > 0){
			for(int j=0 ;j<sendLsit.size();j++){
				Map<String,Object> mnp = sendLsit.get(j);
				if(mnp !=null && mnp.size() >0){
					String sentStr = new GsonBuilder().disableHtmlEscaping().create().toJson(mnp); 
					sendBaseDataToB2B(ctx,sentStr);
					System.out.println(sentStr);
				}
			}
		}
		return super._save(ctx, model);
	}

	private String sendBaseDataToB2B(Context ctx,String jsonStr)
	throws BOSException {
 		String result = "";
		try {
			HttpClient httpClient = new HTTPSTrustClient().init();
			result = HTTPSClientUtil.doPostJson(httpClient, InterfaceResource.sap_warerale_url, jsonStr);			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

}