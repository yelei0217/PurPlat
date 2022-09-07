package com.kingdee.eas.custom.app.dto;

import java.math.BigDecimal;
import java.util.List;

public class PurInDTO  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7305464449348809424L;
	
	
	private String  bid;	//蓝单ID
	private String  iswholebill;	//是否整单退库
	private List<PurInDetailDTO> details ;
	private String  id;
	private String  fnumber;
	private String    fbizdate;
	private String	fsupplierid ;
	private String  fcustomerid;
	private String  fstorageorgunitid;
	private String  fadminorgunitid;
	private BigDecimal  ftotalamount;
	private BigDecimal  ftotaltax;
	private BigDecimal  ftotalqty;
	private BigDecimal  ftotaltaxamount;
	private String  fsendaddress;
	private String  fdescription;
	private String  fcreatorid;
	private String	fpurchasepersonid ;
	private String	fstockerid;
	private String fdoctor;
	
	
	
	public String getFdoctor() {
		return fdoctor;
	}
	public void setFdoctor(String fdoctor) {
		this.fdoctor = fdoctor;
	}
	
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getIswholebill() {
		return iswholebill;
	}
	public void setIswholebill(String iswholebill) {
		this.iswholebill = iswholebill;
	}
	public List<PurInDetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<PurInDetailDTO> details) {
		this.details = details;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFbizdate() {
		return fbizdate;
	}
	public void setFbizdate(String fbizdate) {
		this.fbizdate = fbizdate;
	}
	public String getFsupplierid() {
		return fsupplierid;
	}
	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
	}
	public String getFcustomerid() {
		return fcustomerid;
	}
	public void setFcustomerid(String fcustomerid) {
		this.fcustomerid = fcustomerid;
	}
	public String getFstorageorgunitid() {
		return fstorageorgunitid;
	}
	public void setFstorageorgunitid(String fstorageorgunitid) {
		this.fstorageorgunitid = fstorageorgunitid;
	}
	public String getFadminorgunitid() {
		return fadminorgunitid;
	}
	public void setFadminorgunitid(String fadminorgunitid) {
		this.fadminorgunitid = fadminorgunitid;
	}
	public BigDecimal getFtotalamount() {
		return ftotalamount;
	}
	public void setFtotalamount(BigDecimal ftotalamount) {
		this.ftotalamount = ftotalamount;
	}
	public BigDecimal getFtotaltax() {
		return ftotaltax;
	}
	public void setFtotaltax(BigDecimal ftotaltax) {
		this.ftotaltax = ftotaltax;
	}
	public BigDecimal getFtotalqty() {
		return ftotalqty;
	}
	public void setFtotalqty(BigDecimal ftotalqty) {
		this.ftotalqty = ftotalqty;
	}
	public BigDecimal getFtotaltaxamount() {
		return ftotaltaxamount;
	}
	public void setFtotaltaxamount(BigDecimal ftotaltaxamount) {
		this.ftotaltaxamount = ftotaltaxamount;
	}
	public String getFsendaddress() {
		return fsendaddress;
	}
	public void setFsendaddress(String fsendaddress) {
		this.fsendaddress = fsendaddress;
	}
	public String getFdescription() {
		return fdescription;
	}
	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}
	public String getFcreatorid() {
		return fcreatorid;
	}
	public void setFcreatorid(String fcreatorid) {
		this.fcreatorid = fcreatorid;
	}
	public String getFpurchasepersonid() {
		return fpurchasepersonid;
	}
	public void setFpurchasepersonid(String fpurchasepersonid) {
		this.fpurchasepersonid = fpurchasepersonid;
	}
	public String getFstockerid() {
		return fstockerid;
	}
	public void setFstockerid(String fstockerid) {
		this.fstockerid = fstockerid;
	}
	 
	
}
