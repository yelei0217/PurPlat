package com.kingdee.eas.custom.rest;

public class InterfaceResource {
	 /**B2B 接口基础URL **/
	public static final String sap_base_url ="https://admin-dev.wellekq.com/api/meiwei-resource/EAS/syncBaseData?access_token=MWJrIHED12GdwIyCLBt2nBIdWp7KEQxr&origin=EAS";

	 /**B2B SSL 证书路径  **/
	public static final String sap_Certificate_path = "/software/xxx.keystore";
	
	 /**B2B SSL 证书路径  **/
	public static final String sap_Certificate_pwd="123456";
	 
	 /**B2B  门诊中心仓对应关系同步接口 **/
	public static final String sap_warerale_url ="https://admin-dev.wellekq.com/api/meiwei-users/pur/ware/relation/baseSynDataByEas?access_token=MWJrIHED12GdwIyCLBt2nBIdWp7KEQxr&origin=EAS";

}
