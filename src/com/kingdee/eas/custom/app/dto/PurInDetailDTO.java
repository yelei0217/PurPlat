package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PurInDetailDTO  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1515961271241495249L;
	
	private String id ;
	private String fseq ;
	private String fsourcebillid ;
	private String fsourcebillnumber ;
	private String fsourcebillentryid ;
	private int fsourcebillentryseq ;
	private String fmaterialid ;
	private String funitid ;
	private String fbaseunitid ;
	private BigDecimal fassociateqty ;
	private String fwarehouseid ;
	private String fstockerid ;
	private BigDecimal fqty ;
	private BigDecimal fbaseqty ;
	private BigDecimal fprice ;
	private BigDecimal famount ;
	//private BigDecimal funitactualcost ;
	//private BigDecimal factualcost ;
	private boolean fispresent ;
	private String fparentid ;
	private BigDecimal ftaxrate ;
	private BigDecimal ftax ;
	private BigDecimal flocaltax ;
	private BigDecimal flocalprice ;
	private BigDecimal flocalamount ;
	private Date fmfg ;
	private Date fexp ;
	private String fremark ;
	private BigDecimal ftaxprice ;
	private BigDecimal ftaxamount ;
	private BigDecimal factualtaxprice ;
	
	public String getFparentid() {
		return fparentid;
	}
	public void setFparentid(String fparentid) {
		this.fparentid = fparentid;
	}
	public BigDecimal getFactualtaxprice() {
		return factualtaxprice;
	}
	public void setFactualtaxprice(BigDecimal factualtaxprice) {
		this.factualtaxprice = factualtaxprice;
	}
	public void setFsourcebillentryseq(int fsourcebillentryseq) {
		this.fsourcebillentryseq = fsourcebillentryseq;
	}
	public void setFispresent(boolean fispresent) {
		this.fispresent = fispresent;
	}
	public BigDecimal getFtaxamount() {
		return ftaxamount;
	}
	public void setFtaxamount(BigDecimal ftaxamount) {
		this.ftaxamount = ftaxamount;
	}
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
	public Integer getFsourcebillentryseq() {
		return fsourcebillentryseq;
	}
	public void setFsourcebillentryseq(Integer fsourcebillentryseq) {
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
	public String getFbaseunitid() {
		return fbaseunitid;
	}
	public void setFbaseunitid(String fbaseunitid) {
		this.fbaseunitid = fbaseunitid;
	}
	public BigDecimal getFassociateqty() {
		return fassociateqty;
	}
	public void setFassociateqty(BigDecimal fassociateqty) {
		this.fassociateqty = fassociateqty;
	}
 
	public String getFwarehouseid() {
		return fwarehouseid;
	}
	public void setFwarehouseid(String fwarehouseid) {
		this.fwarehouseid = fwarehouseid;
	}
	public String getFstockerid() {
		return fstockerid;
	}
	public void setFstockerid(String fstockerid) {
		this.fstockerid = fstockerid;
	}
	public BigDecimal getFqty() {
		return fqty;
	}
	public void setFqty(BigDecimal fqty) {
		this.fqty = fqty;
	}
	public BigDecimal getFbaseqty() {
		return fbaseqty;
	}
	public void setFbaseqty(BigDecimal fbaseqty) {
		this.fbaseqty = fbaseqty;
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
//	public BigDecimal getFunitactualcost() {
//		return funitactualcost;
//	}
//	public void setFunitactualcost(BigDecimal funitactualcost) {
//		this.funitactualcost = funitactualcost;
//	}
//	public BigDecimal getFactualcost() {
//		return factualcost;
//	}
//	public void setFactualcost(BigDecimal factualcost) {
//		this.factualcost = factualcost;
//	}
	public Boolean getFispresent() {
		return fispresent;
	}
	public void setFispresent(Boolean fispresent) {
		this.fispresent = fispresent;
	}
	public BigDecimal getFtaxrate() {
		return ftaxrate;
	}
	public void setFtaxrate(BigDecimal ftaxrate) {
		this.ftaxrate = ftaxrate;
	}
	public BigDecimal getFtax() {
		return ftax;
	}
	public void setFtax(BigDecimal ftax) {
		this.ftax = ftax;
	}
	public BigDecimal getFlocaltax() {
		return flocaltax;
	}
	public void setFlocaltax(BigDecimal flocaltax) {
		this.flocaltax = flocaltax;
	}
	public BigDecimal getFlocalprice() {
		return flocalprice;
	}
	public void setFlocalprice(BigDecimal flocalprice) {
		this.flocalprice = flocalprice;
	}
	public BigDecimal getFlocalamount() {
		return flocalamount;
	}
	public void setFlocalamount(BigDecimal flocalamount) {
		this.flocalamount = flocalamount;
	}
	public Date getFmfg() {
		return fmfg;
	}
	public void setFmfg(Date fmfg) {
		this.fmfg = fmfg;
	}
	public Date getFexp() {
		return fexp;
	}
	public void setFexp(Date fexp) {
		this.fexp = fexp;
	}
	public BigDecimal getFtaxprice() {
		return ftaxprice;
	}
	public void setFtaxprice(BigDecimal ftaxprice) {
		this.ftaxprice = ftaxprice;
	}
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
}
