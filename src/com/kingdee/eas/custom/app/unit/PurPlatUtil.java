package com.kingdee.eas.custom.app.unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PurPlatUtil {
	
	

	
	
	
	
	/**
	 *  获取当前时间 格式化
	 * @return yyyyMMddHHmmss
	 */
	public static String getCurrentTimeStr(){
  		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
	}

	/**
	 *  获取当前时间 格式化
	 * @return yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimeStrS(){
 		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(System.currentTimeMillis()));
	}

	
}
