package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class VMISaleOrderDetailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4234533378367070851L;
	
	private String  id;
	private Integer fseq;
	private String  fmaterialid;
	private String  funitid;
	private BigDecimal      fassociateqty;
	private String  fbaseunitid;
	private Boolean fispresent;
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
	
	private BigDecimal flzsprice; 	//栗喆销售单价
	private BigDecimal flzstaxprice; 	//栗喆销售含税单价
	private BigDecimal flzsamount; 	//栗喆销售金额
	private BigDecimal flzstaxrate; 	//栗喆销售税率
	private BigDecimal flzstax; 	//栗喆销售税额
	private BigDecimal flzstaxamount; 	//栗喆销售价税合计
	private BigDecimal flzpprice; 	//栗喆采购单价
	private BigDecimal flzptaxprice; 	//栗喆采购含税单价
	private BigDecimal flzpamount; 	//栗喆采购金额
	private BigDecimal flzptaxrate; 	//栗喆采购税率
	private BigDecimal flzptax; 	//栗喆采购税额
	private BigDecimal flzptaxamount; 	//栗喆采购价税合计
	private String fwarehouseid;//仓库ID
	
	public String getFsnno() {
		return fsnno;
	}
	public void setFsnno(String fsnno) {
		this.fsnno = fsnno;
	}
	private String fsupplierid;//供应商ID
	private String fsnno ;//snNo
	
	public String getFsupplierid() {
		return fsupplierid;
	}
	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
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
	public Boolean getFispresent() {
		return fispresent;
	}
	public void setFispresent(Boolean fispresent) {
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
	public BigDecimal getFlzsprice() {
		return flzsprice;
	}
	public void setFlzsprice(BigDecimal flzsprice) {
		this.flzsprice = flzsprice;
	}
	public BigDecimal getFlzstaxprice() {
		return flzstaxprice;
	}
	public void setFlzstaxprice(BigDecimal flzstaxprice) {
		this.flzstaxprice = flzstaxprice;
	}
	public BigDecimal getFlzsamount() {
		return flzsamount;
	}
	public void setFlzsamount(BigDecimal flzsamount) {
		this.flzsamount = flzsamount;
	}
	public BigDecimal getFlzstaxrate() {
		return flzstaxrate;
	}
	public void setFlzstaxrate(BigDecimal flzstaxrate) {
		this.flzstaxrate = flzstaxrate;
	}
	public BigDecimal getFlzstax() {
		return flzstax;
	}
	public void setFlzstax(BigDecimal flzstax) {
		this.flzstax = flzstax;
	}
	public BigDecimal getFlzstaxamount() {
		return flzstaxamount;
	}
	public void setFlzstaxamount(BigDecimal flzstaxamount) {
		this.flzstaxamount = flzstaxamount;
	}
	public BigDecimal getFlzpprice() {
		return flzpprice;
	}
	public void setFlzpprice(BigDecimal flzpprice) {
		this.flzpprice = flzpprice;
	}
	public BigDecimal getFlzptaxprice() {
		return flzptaxprice;
	}
	public void setFlzptaxprice(BigDecimal flzptaxprice) {
		this.flzptaxprice = flzptaxprice;
	}
	public BigDecimal getFlzpamount() {
		return flzpamount;
	}
	public void setFlzpamount(BigDecimal flzpamount) {
		this.flzpamount = flzpamount;
	}
	public BigDecimal getFlzptaxrate() {
		return flzptaxrate;
	}
	public void setFlzptaxrate(BigDecimal flzptaxrate) {
		this.flzptaxrate = flzptaxrate;
	}
	public BigDecimal getFlzptax() {
		return flzptax;
	}
	public void setFlzptax(BigDecimal flzptax) {
		this.flzptax = flzptax;
	}
	public BigDecimal getFlzptaxamount() {
		return flzptaxamount;
	}
	public void setFlzptaxamount(BigDecimal flzptaxamount) {
		this.flzptaxamount = flzptaxamount;
	}
	public String getFwarehouseid() {
		return fwarehouseid;
	}
	public void setFwarehouseid(String fwarehouseid) {
		this.fwarehouseid = fwarehouseid;
	}
}
