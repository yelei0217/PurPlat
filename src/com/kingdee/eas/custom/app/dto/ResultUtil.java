package com.kingdee.eas.custom.app.dto;

public class ResultUtil {

	public ResultUtil(Integer code, String msg, String msgId) {
		super();
		this.code = code;
		this.msg = msg;
		this.msgId = msgId;
	}
	
	private String msgId;
	private Integer code;
	private String msg ;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static ResultUtil success(String msgId){
		ResultUtil resultSuccess = new ResultUtil(200,"²Ù×÷³É¹¦",msgId);
		return resultSuccess;
	}
	
	public static ResultUtil success(Integer code, String msg, String msgId){
		ResultUtil resultSuccess = new ResultUtil(code,msg,msgId);
		return resultSuccess;
	}
	
	public static ResultUtil error(Integer code, String msg, String msgId){
		ResultUtil resultError = new ResultUtil(code,msg,msgId);
		return resultError;
	}

}
