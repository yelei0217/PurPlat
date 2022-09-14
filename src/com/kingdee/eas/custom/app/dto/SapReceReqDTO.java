package com.kingdee.eas.custom.app.dto;

import java.io.Serializable;

public class SapReceReqDTO  implements Serializable{
	
	 	/**
	 * 
	 */
	private static final long serialVersionUID = -1127536348515209745L;
	
		private String IF_TYPE; 
	    private String IF_TRANSACTION_ID; 
	    private String SEQUENCE; 
	    private String IF_FINISHED_FLAG; 
	    private String SYS_SRC; 
	    private String TIMESTAMP_SRC; 
	    private String SYS_TAR; 
	    private String TIMESTAMP_PO; 
	    private String IF_JSON_DATA;
	    
		public String getIF_TYPE() {
			return IF_TYPE;
		}
		public void setIF_TYPE(String if_type) {
			IF_TYPE = if_type;
		}
		public String getIF_TRANSACTION_ID() {
			return IF_TRANSACTION_ID;
		}
		public void setIF_TRANSACTION_ID(String if_transaction_id) {
			IF_TRANSACTION_ID = if_transaction_id;
		}
		public String getSEQUENCE() {
			return SEQUENCE;
		}
		public void setSEQUENCE(String sequence) {
			SEQUENCE = sequence;
		}
		public String getIF_FINISHED_FLAG() {
			return IF_FINISHED_FLAG;
		}
		public void setIF_FINISHED_FLAG(String if_finished_flag) {
			IF_FINISHED_FLAG = if_finished_flag;
		}
		public String getSYS_SRC() {
			return SYS_SRC;
		}
		public void setSYS_SRC(String sys_src) {
			SYS_SRC = sys_src;
		}
		public String getTIMESTAMP_SRC() {
			return TIMESTAMP_SRC;
		}
		public void setTIMESTAMP_SRC(String timestamp_src) {
			TIMESTAMP_SRC = timestamp_src;
		}
		public String getSYS_TAR() {
			return SYS_TAR;
		}
		public void setSYS_TAR(String sys_tar) {
			SYS_TAR = sys_tar;
		}
		public String getTIMESTAMP_PO() {
			return TIMESTAMP_PO;
		}
		public void setTIMESTAMP_PO(String timestamp_po) {
			TIMESTAMP_PO = timestamp_po;
		}
		public String getIF_JSON_DATA() {
			return IF_JSON_DATA;
		}
		public void setIF_JSON_DATA(String if_json_data) {
			IF_JSON_DATA = if_json_data;
		}
	    
	    
}

