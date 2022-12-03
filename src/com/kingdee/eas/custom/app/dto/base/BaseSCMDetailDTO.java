package com.kingdee.eas.custom.app.dto.base;

import java.io.Serializable;
import java.math.BigDecimal;

public class BaseSCMDetailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4487576613402867447L;

	private String  id;
	private Integer fseq;
	private String fsourcebillid ;
	private String fsourcebillnumber ;
	private String fsourcebillentryid ;
	private int fsourcebillentryseq ;
	private String  fmaterialid;
	private String  funitid;
	private BigDecimal      fassociateqty;
	private String  fbaseunitid;
	private String fispresent;
	private BigDecimal      fqty;
	private BigDecimal      fprice;
	private BigDecimal      factualprice;
	private BigDecimal      ftaxrate;
	private BigDecimal      ftaxprice;
	private BigDecimal      factualtaxprice;
	private BigDecimal      famount;
	private BigDecimal      ftax;
	private BigDecimal      ftaxamount;
	private String    fdeliverydate;
	private String 	fsenddate;
	private BigDecimal      fbaseqty;
	private String  fremark;
	private String fwarehouseid ;
	private String fmfg ;
	private String fexp ;
	
	private String flot ;
	
	private String fsupplierid ;
	
	
	public String getFsupplierid() {
		return fsupplierid;
	}
	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
	}
	public String getFlot() {
		return flot;
	}
	public void setFlot(String flot) {
		this.flot = flot;
	}
	public String getFmfg() {
		return fmfg;
	}
	public void setFmfg(String fmfg) {
		this.fmfg = fmfg;
	}
	public String getFexp() {
		return fexp;
	}
	public void setFexp(String fexp) {
		this.fexp = fexp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getFseq() {
		return fseq;
	}
	public void setFseq(Integer fseq) {
		this.fseq = fseq;
	}
	public String getFsourcebillid() {
		return fsourcebillid;
	}
	public void setFsourcebillid(String fsourcebillid) {
		this.fsourcebillid = fsourcebillid;
	}
	public String getFsourcebillnumber() {
		return fsourcebillnumber;
	}
	public void setFsourcebillnumber(String fsourcebillnumber) {
		this.fsourcebillnumber = fsourcebillnumber;
	}
	public String getFsourcebillentryid() {
		return fsourcebillentryid;
	}
	public void setFsourcebillentryid(String fsourcebillentryid) {
		this.fsourcebillentryid = fsourcebillentryid;
	}
	public int getFsourcebillentryseq() {
		return fsourcebillentryseq;
	}
	public void setFsourcebillentryseq(int fsourcebillentryseq) {
		this.fsourcebillentryseq = fsourcebillentryseq;
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
	public BigDecimal getFassociateqty() {
		return fassociateqty;
	}
	public void setFassociateqty(BigDecimal fassociateqty) {
		this.fassociateqty = fassociateqty;
	}
	public String getFbaseunitid() {
		return fbaseunitid;
	}
	public void setFbaseunitid(String fbaseunitid) {
		this.fbaseunitid = fbaseunitid;
	}
	public String getFispresent() {
		return fispresent;
	}
	public void setFispresent(String fispresent) {
		this.fispresent = fispresent;
	}
	public BigDecimal getFqty() {
		return fqty;
	}
	public void setFqty(BigDecimal fqty) {
		this.fqty = fqty;
	}
	public BigDecimal getFprice() {
		return fprice;
	}
	public void setFprice(BigDecimal fprice) {
		this.fprice = fprice;
	}
	public BigDecimal getFactualprice() {
		return factualprice;
	}
	public void setFactualprice(BigDecimal factualprice) {
		this.factualprice = factualprice;
	}
	public BigDecimal getFtaxrate() {
		return ftaxrate;
	}
	public void setFtaxrate(BigDecimal ftaxrate) {
		this.ftaxrate = ftaxrate;
	}
	public BigDecimal getFtaxprice() {
		return ftaxprice;
	}
	public void setFtaxprice(BigDecimal ftaxprice) {
		this.ftaxprice = ftaxprice;
	}
	public BigDecimal getFactualtaxprice() {
		return factualtaxprice;
	}
	public void setFactualtaxprice(BigDecimal factualtaxprice) {
		this.factualtaxprice = factualtaxprice;
	}
	public BigDecimal getFamount() {
		return famount;
	}
	public void setFamount(BigDecimal famount) {
		this.famount = famount;
	}
	public BigDecimal getFtax() {
		return ftax;
	}
	public void setFtax(BigDecimal ftax) {
		this.ftax = ftax;
	}
	public BigDecimal getFtaxamount() {
		return ftaxamount;
	}
	public void setFtaxamount(BigDecimal ftaxamount) {
		this.ftaxamount = ftaxamount;
	}
	public String getFdeliverydate() {
		return fdeliverydate;
	}
	public void setFdeliverydate(String fdeliverydate) {
		this.fdeliverydate = fdeliverydate;
	}
	public String getFsenddate() {
		return fsenddate;
	}
	public void setFsenddate(String fsenddate) {
		this.fsenddate = fsenddate;
	}
	public BigDecimal getFbaseqty() {
		return fbaseqty;
	}
	public void setFbaseqty(BigDecimal fbaseqty) {
		this.fbaseqty = fbaseqty;
	}
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	public String getFwarehouseid() {
		return fwarehouseid;
	}
	public void setFwarehouseid(String fwarehouseid) {
		this.fwarehouseid = fwarehouseid;
	}
	
}
