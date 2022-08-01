package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;

public class ResponseDTO<T>  implements Serializable{

	public ResponseDTO() {
		super();
	}
	
	
	public ResponseDTO(String busCode, String msgId, String reqTime, T t) {
		super();
		this.busCode = busCode;
		this.msgId = msgId;
		this.reqTime = reqTime;
		this.t = t;
	}


	private String msgId;
	private String busCode;
	private String reqTime;
	private T t;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}	
	
	
	
}
