package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;

public class SapRespondDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6189346083677275222L;
	
	private String Date;	//日期：20200101
	private String Time;	//时间：120101
	private String Flag;	//S成功E失败
	private String Message;	//说明
	
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	
}
