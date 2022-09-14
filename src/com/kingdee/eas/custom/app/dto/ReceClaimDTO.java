package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ReceClaimDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722762170063874851L;
	
	private String EASID;	//EAS流水码
	private String ZTYPE;	//认领类型
	private String BUKRS;	//公司代码
	private String BANKN;	//收款银行账号
	private String WAERS;	//币种 
	private String BUDAT;	//收款日期
	private String BUDAT2;//	再次认领日期
	private String KUNNR;	//客户编码
	private BigDecimal DMBTR;	//收款金额
	private BigDecimal DMBTR_HK;//	其中货款金额
	private BigDecimal DMBTR_BZJ;//	其中保证金金额
	private BigDecimal DMBTR_YJ;//	其中押金金额
	private String SGTXT;	//摘要
	
	 
	public String getEASID() {
		return EASID;
	}
	public void setEASID(String easid) {
		EASID = easid;
	}
	public String getBUKRS() {
		return BUKRS;
	}
	public void setBUKRS(String bukrs) {
		BUKRS = bukrs;
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
	public String getBUDAT2() {
		return BUDAT2;
	}
	public void setBUDAT2(String budat2) {
		BUDAT2 = budat2;
	}
	public String getKUNNR() {
		return KUNNR;
	}
	public void setKUNNR(String kunnr) {
		KUNNR = kunnr;
	}
	public BigDecimal getDMBTR() {
		return DMBTR;
	}
	public void setDMBTR(BigDecimal dmbtr) {
		DMBTR = dmbtr;
	}
	public String getZTYPE() {
		return ZTYPE;
	}
	public void setZTYPE(String ztype) {
		ZTYPE = ztype;
	}
	public String getWAERS() {
		return WAERS;
	}
	public void setWAERS(String waers) {
		WAERS = waers;
	}
	public BigDecimal getDMBTR_HK() {
		return DMBTR_HK;
	}
	public void setDMBTR_HK(BigDecimal dmbtr_hk) {
		DMBTR_HK = dmbtr_hk;
	}
	public BigDecimal getDMBTR_BZJ() {
		return DMBTR_BZJ;
	}
	public void setDMBTR_BZJ(BigDecimal dmbtr_bzj) {
		DMBTR_BZJ = dmbtr_bzj;
	}
	public BigDecimal getDMBTR_YJ() {
		return DMBTR_YJ;
	}
	public void setDMBTR_YJ(BigDecimal dmbtr_yj) {
		DMBTR_YJ = dmbtr_yj;
	}
	public String getSGTXT() {
		return SGTXT;
	}
	public void setSGTXT(String sgtxt) {
		SGTXT = sgtxt;
	}
	
}
