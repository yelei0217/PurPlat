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
	
	private String	fid ;
	private String	fnumber ;
	private Date	fbizdate ;
	private String	fstorageorgunitid ;
	private String	fstockerid ;
	private BigDecimal	ftotalqty ;
	private BigDecimal	ftotalamount ;
	private String	fsupplierid ;
	private String	fcreatorid ;
	private Date	fcreatetime ;
	
	private List<PurInDetailDTO> details ;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public Date getFbizdate() {
		return fbizdate;
	}

	public void setFbizdate(Date fbizdate) {
		this.fbizdate = fbizdate;
	}

	public String getFstorageorgunitid() {
		return fstorageorgunitid;
	}

	public void setFstorageorgunitid(String fstorageorgunitid) {
		this.fstorageorgunitid = fstorageorgunitid;
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

	public Date getFcreatetime() {
		return fcreatetime;
	}

	public void setFcreatetime(Date fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	public List<PurInDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<PurInDetailDTO> details) {
		this.details = details;
	}
	
	
}
