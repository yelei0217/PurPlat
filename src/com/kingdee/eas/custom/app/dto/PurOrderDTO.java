package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class PurOrderDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6134640840085828492L;
	
	private String	id ;
	private String	fnumber ;
	private String	fbizdate ;
	private String	fstorageorgunitid ;
	private String	fpurchasepersonid ;
	private String	fsupplierid ;
	private String	fadminorgunitid ;
	private BigDecimal	ftotalamount ;
	private BigDecimal	ftotaltax ;
	private BigDecimal	ftotaltaxamount ;
	private String	fcreatorid ;
 
	private List<PurOrderDetailDTO> details;

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

	public String getFstorageorgunitid() {
		return fstorageorgunitid;
	}

	public void setFstorageorgunitid(String fstorageorgunitid) {
		this.fstorageorgunitid = fstorageorgunitid;
	}

	public String getFpurchasepersonid() {
		return fpurchasepersonid;
	}

	public void setFpurchasepersonid(String fpurchasepersonid) {
		this.fpurchasepersonid = fpurchasepersonid;
	}

	public String getFsupplierid() {
		return fsupplierid;
	}

	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
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

	public BigDecimal getFtotaltaxamount() {
		return ftotaltaxamount;
	}

	public void setFtotaltaxamount(BigDecimal ftotaltaxamount) {
		this.ftotaltaxamount = ftotaltaxamount;
	}

	public String getFcreatorid() {
		return fcreatorid;
	}

	public void setFcreatorid(String fcreatorid) {
		this.fcreatorid = fcreatorid;
	}

//	public Date getFcreatetime() {
//		return fcreatetime;
//	}
//
//	public void setFcreatetime(Date fcreatetime) {
//		this.fcreatetime = fcreatetime;
//	}

	public List<PurOrderDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PurOrderDetailDTO> details) {
		this.details = details;
	}

	
}
