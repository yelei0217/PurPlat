package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CostAdjusDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2616357151970762371L;

	private String  id;
	private String  fnumber;
	private String    fbizdate;
	private String  fcustomerid;
	private String  fstorageorgunitid;
	private String  fadminorgunitid;
	private BigDecimal  ftotalamount;
	private BigDecimal  ftotaltax;
	private BigDecimal  ftotaltaxamount;
	private BigDecimal  ftotalqty;
	private String  fsendaddress;
	private String  fdescription;
	private String  fcreatorid;
	private String	fsupplierid ;
	
	public BigDecimal getFtotalqty() {
		return ftotalqty;
	}
	public void setFtotalqty(BigDecimal ftotalqty) {
		this.ftotalqty = ftotalqty;
	}
	private List<CostAdjusDetailDTO> details;
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
	public String getFsupplierid() {
		return fsupplierid;
	}
	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
	}
	public List<CostAdjusDetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<CostAdjusDetailDTO> details) {
		this.details = details;
	}
	
}
