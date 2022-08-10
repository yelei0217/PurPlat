package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurInDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7305464449348809424L;
	
	private String	id ;
	private String	fnumber ;
	private String	fbizdate ;
	private String	fstorageorgunitid ;
	private String	fstockerid ;
	private BigDecimal	ftotalqty ;
	private BigDecimal	ftotalamount ;
 	private BigDecimal	ftotaltax ;
	private BigDecimal	ftotaltaxamount ;
	private String	fsupplierid ;
	private String	fcreatorid ;
	
	
	public String getFstorageorgunitid() {
		return fstorageorgunitid;
	}

	public void setFstorageorgunitid(String fstorageorgunitid) {
		this.fstorageorgunitid = fstorageorgunitid;
	}

	private List<PurInDetailDTO> details ;
	
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFbizdate() {
		return fbizdate;
	}

	public void setFbizdate(String fbizdate) {
		this.fbizdate = fbizdate;
	}


	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

 
	public String getFstockerid() {
		return fstockerid;
	}

	public void setFstockerid(String fstockerid) {
		this.fstockerid = fstockerid;
	}

	public BigDecimal getFtotalqty() {
		return ftotalqty;
	}

	public void setFtotalqty(BigDecimal ftotalqty) {
		this.ftotalqty = ftotalqty;
	}

	public BigDecimal getFtotalamount() {
		return ftotalamount;
	}

	public void setFtotalamount(BigDecimal ftotalamount) {
		this.ftotalamount = ftotalamount;
	}

	public String getFsupplierid() {
		return fsupplierid;
	}

	public void setFsupplierid(String fsupplierid) {
		this.fsupplierid = fsupplierid;
	}

	public String getFcreatorid() {
		return fcreatorid;
	}

	public void setFcreatorid(String fcreatorid) {
		this.fcreatorid = fcreatorid;
	}


	public List<PurInDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PurInDetailDTO> details) {
		this.details = details;
	}
	
	
}
