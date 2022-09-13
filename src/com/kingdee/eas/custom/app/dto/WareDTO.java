package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.util.List;

public class WareDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6137701219720203568L;

	private String wid ;
	private String wno ;
	private String wna ;
	private int wst;
	private List<SOrgDTO> clinics;
	
	public List<SOrgDTO> getClinics() {
		return clinics;
	}
	public void setClinics(List<SOrgDTO> clinics) {
		this.clinics = clinics;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getWno() {
		return wno;
	}
	public void setWno(String wno) {
		this.wno = wno;
	}
	public String getWna() {
		return wna;
	}
	public void setWna(String wna) {
		this.wna = wna;
	}
	public int getWst() {
		return wst;
	}
	public void setWst(int wst) {
		this.wst = wst;
	}

	
	
}
