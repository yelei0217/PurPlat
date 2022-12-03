package com.kingdee.bos.service.formula.api;

import java.util.Locale;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.base.permission.UserInfo;
import com.kingdee.eas.basedata.org.FullOrgUnitInfo;
import com.kingdee.eas.basedata.org.OrgType;
import com.kingdee.eas.common.EASBizException;

public class LZContectUtil {
	protected static final int DB_TYPE = 0;

	public static synchronized Context context(String number)
			throws EASBizException, BOSException {
		Context ctx = new Context();
		UserInfo user = new UserInfo();
		IObjectPK userPk = new ObjectUuidPK("ZypTdtSLS8S90LPPdhP1MxO33n8=");
		user.setName("03");
		user.setNumber("his");
		user.setId(BOSUuid.read("ZypTdtSLS8S90LPPdhP1MxO33n8="));
		ctx.setCaller(userPk);
		ctx.setUserName(user.getName());
		ctx.setAIS("A01");

		FullOrgUnitInfo orgUnit = new FullOrgUnitInfo();
		String orgUnitId = "jbYAAAMU2SvM567U";
		orgUnit.setId(BOSUuid.read(orgUnitId));
		orgUnit.setName("上海栗喆医疗器械有限公司");
		orgUnit.setNumber("MS3101MWZH001");

		ctx.setLocale(new Locale("l2"));
		ctx.put("originLocale", new Locale("zh_CN"));
		ctx.setSolution("eas");

		ctx.put("UserInfo", user);
		ctx.put("SwitchToNewLoginFlow", "true");
		ctx.put("ClientName", "127.0.0.1");
		ctx.put("Password", "MCo8/7ZfqN1R1nrLzMPP9A==");
		ctx.put("ClientIP", "127.0.0.1");
		ctx.put("dbTypeCode", Integer.valueOf(0));
		ctx.put("CurCompanyId", orgUnitId);
		ctx.put("CurOU", orgUnit);
		ctx.put("CompanyInfo", null);
		ctx.put(OrgType.NONE, null);
		ctx.put("hint", "no");
		ctx.put("USBKEY_LOGIN", null);
		return ctx;
	}
}
