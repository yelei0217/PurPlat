package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostAdjusDetailDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7018849252145668497L;

	private String id ;
	private String fseq ;
	private String fmaterialid ;
 	private String	funitid ;
//	private String	fbaseunitid ;
	private String fwarehouseid ;
//	private String fstockerid ;
//	private Boolean	fispresent ;
	private String fparentid ;
	private BigDecimal fprice;
//	private BigDecimal factualprice;
//	private BigDecimal ftaxprice;
//	private BigDecimal factualtaxprice;
	private BigDecimal famount;
//	private BigDecimal ftaxamount;
//	private BigDecimal ftaxrate;
//	private BigDecimal ftax;
	private BigDecimal fqty;
//	private BigDecimal fbaseqty;
//	private String fmfg ;
//	private String fexp ;
//	private String fremark ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFseq() {
		return fseq;
	}
	public void setFseq(String fseq) {
		this.fseq = fseq;
	}
	public String getFmaterialid() {
		return fmaterialid;
	}
	public void setFmaterialid(String fmaterialid) {
		this.fmaterialid = fmaterialid;
	}
	public String getFunitid() {
		return funitid;
	}
	public void setFunitid(String funitid) {
		this.funitid = funitid;
	}
	public String getFwarehouseid() {
		return fwarehouseid;
	}
	public void setFwarehouseid(String fwarehouseid) {
		this.fwarehouseid = fwarehouseid;
	}
	public String getFparentid() {
		return fparentid;
	}
	public void setFparentid(String fparentid) {
		this.fparentid = fparentid;
	}
	public BigDecimal getFprice() {
		return fprice;
	}
	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}
	public BigDecimal getFamount() {
		return famount;
	}
	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}
	public BigDecimal getFqty() {
		return fqty;
	}
	public void setFqty(BigDecimal fqty) {
		this.fqty = fqty;
	}
	  
}
