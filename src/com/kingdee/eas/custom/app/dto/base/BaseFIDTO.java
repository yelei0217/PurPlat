package com.kingdee.eas.custom.app.dto.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BaseFIDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -349806836985494559L;

	
	private String  id;
	private String  fnumber;
	private String    fbizdate;
	private String	fsupplierid ;
	private String  fcustomerid;
	private String  fcompanyorgunitid;
	//private String  fadminorgunitid;
	private BigDecimal  ftotalamount;
	private BigDecimal  ftotaltax;
	private BigDecimal  ftotalqty;
	private BigDecimal  ftotaltaxamount;
	private String  fsendaddress;
	private String  fdescription;
	private String  fcreatorid;
//	private String	fpersonid ;
//	private String	fstockerid;
	private String	finvoicenumber;
	private String fbank;
	private String fbankaccount;
	
	public String getFbank() {
		return fbank;
	}
	public void setFbank(String fbank) {
		this.fbank = fbank;
	}
	public String getFbankaccount() {
		return fbankaccount;
	}
	public void setFbankaccount(String fbankaccount) {
		this.fbankaccount = fbankaccount;
	}
	private List<BaseFIDetailDTO> details;
	
	
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
	 
//	public String getFstockerid() {
//		return fstockerid;
//	}
//	public void setFstockerid(String fstockerid) {
//		this.fstockerid = fstockerid;
//	}
	public String getFinvoicenumber() {
		return finvoicenumber;
	}
	public void setFinvoicenumber(String finvoicenumber) {
		this.finvoicenumber = finvoicenumber;
	}
	public List<BaseFIDetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<BaseFIDetailDTO> details) {
		this.details = details;
	}
	public String getFcompanyorgunitid() {
		return fcompanyorgunitid;
	}
	public void setFcompanyorgunitid(String fcompanyorgunitid) {
		this.fcompanyorgunitid = fcompanyorgunitid;
	}
//	public String getFpersonid() {
//		return fpersonid;
//	}
//	public void setFpersonid(String fpersonid) {
//		this.fpersonid = fpersonid;
//	}
	
	
}
