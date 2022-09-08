package com.kingdee.eas.custom.app.dto.base;

import java.io.Serializable;

public class BaseResponseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3187582589250876968L;

	private String msgId;
	private String code;
	private String msg ;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
