package com.kingdee.eas.custom.app.unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PurPlatUtil {
	
	

	
	
	
	
	/**
	 *  ��ȡ��ǰʱ�� ��ʽ��
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrentTimeStr(){
  		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
	}

	/**
	 *  ��ȡ��ǰʱ�� ��ʽ��
	 * @return yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimeStrS(){
 		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(System.currentTimeMillis()));
	}

	
}
