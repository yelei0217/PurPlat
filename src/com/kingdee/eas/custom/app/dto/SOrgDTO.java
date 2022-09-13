package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;

public class SOrgDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1044948879580910774L;
	
	private String wid ;
	private String sid ;
	private String sno ;
	private String sna ;
	private int sst;
	
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getSna() {
		return sna;
	}
	public void setSna(String sna) {
		this.sna = sna;
	}
	public int getSst() {
		return sst;
	}
	public void setSst(int sst) {
		this.sst = sst;
	}
	
	

}
