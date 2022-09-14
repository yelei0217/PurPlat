package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;

public class SapReceRspDTO  implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7350532490421738318L;
	
	
	private String TRANSACTION_ID ;
	private String SAP_DOC_ID ;
	private String DATE ;
	private String TIME ;
	private String FLAG ;
	private String MESSAGE ;
	
	
	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}
	public void setTRANSACTION_ID(String transaction_id) {
		TRANSACTION_ID = transaction_id;
	}
	public String getSAP_DOC_ID() {
		return SAP_DOC_ID;
	}
	public void setSAP_DOC_ID(String sap_doc_id) {
		SAP_DOC_ID = sap_doc_id;
	}
	public String getDATE() {
		return DATE;
	}
	public void setDATE(String date) {
		DATE = date;
	}
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String time) {
		TIME = time;
	}
	public String getFLAG() {
		return FLAG;
	}
	public void setFLAG(String flag) {
		FLAG = flag;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String message) {
		MESSAGE = message;
	}
	
	
	
}
