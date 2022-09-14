package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MonthEndNoClaimDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927825339686936346L;

	private String EASID ; 	//EAS流水码
	private String BUKRS ; 	//公司代码
	private String GJAHR ; 	//年度
	private String MONAT ; 	//月份
	private String WAERS;	//币种 
	private String BANKN ; 	//收款银行账号
	private String BUDAT ; 	//收款日期
	private BigDecimal DMBTR ; 	//收款金额
	private String SGTXT ; 	//摘要
	 
	public String getBUKRS() {
		return BUKRS;
	}
	public void setBUKRS(String bukrs) {
		BUKRS = bukrs;
	}
	public String getGJAHR() {
		return GJAHR;
	}
	public void setGJAHR(String gjahr) {
		GJAHR = gjahr;
	}
	public String getMONAT() {
		return MONAT;
	}
	public void setMONAT(String monat) {
		MONAT = monat;
	}
	public String getBANKN() {
		return BANKN;
	}
	public void setBANKN(String bankn) {
		BANKN = bankn;
	}
	public String getBUDAT() {
		return BUDAT;
	}
	public void setBUDAT(String budat) {
		BUDAT = budat;
	}
	public BigDecimal getDMBTR() {
		return DMBTR;
	}
	public void setDMBTR(BigDecimal dmbtr) {
		DMBTR = dmbtr;
	}
	public String getEASID() {
		return EASID;
	}
	public void setEASID(String easid) {
		EASID = easid;
	}
	public String getWAERS() {
		return WAERS;
	}
	public void setWAERS(String waers) {
		WAERS = waers;
	}
	public String getSGTXT() {
		return SGTXT;
	}
	public void setSGTXT(String sgtxt) {
		SGTXT = sgtxt;
	}
}
