package com.kingdee.bos.service.formula.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.ContextUtils;
import com.kingdee.bos.framework.ejb.EJBFactory;
import com.kingdee.bos.kscript.KScriptException;
import com.kingdee.bos.service.formula.api.util.GQCContectUtil;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.util.db.SQLUtils;

public class MeiWeiLZBTPFunction implements IFormulaFunctions {

	private static Vector funcInfos = new Vector();
	
	static
	  {
	    funcInfos.add(new FuncInfo("getCenterWareHByClinId", "美维栗喆自定义函数", "获取中心仓ID：getCenterWareHByClinId(门诊组织ID)，返回栗喆中心仓ID"));
	  } 
	@Override
	public Object evalFunction(String func, List paramList) throws KScriptException {
		Object result = new Object();
		if ((paramList != null) && (func != null) && (!("".equals(func)))) {
			if ("getCenterWareHByClinId".equals(func))
				result = getCenterWareHByClinId(paramList);  
			System.out.println("func:" + func
					+ "#####################evalFunction reuslt is :" + result
					+ "##############");
		}
		return result;
	}

	@Override
	public boolean existFunction(String s) {
	    if (s == null) {
	        return false;
	      }
	      for (int i = 0; i < funcInfos.size(); i++) {
	        if (s.equals(((FuncInfo)funcInfos.get(i)).funcName)) {
	          return true;
	        }
	      }
	      return false;
	}

	@Override
	public String[] getAllFuncNames() {
		  String[] as = new String[funcInfos.size()];
		    for (int i = 0; i < funcInfos.size(); i++) {
		      as[i] = ((FuncInfo)funcInfos.get(i)).funcName;
		    }
		    return as;
	}

	@Override
	public String getFuncCategory(String s) {
	    if (s == null) {
	        return null;
	      }
	      for (int i = 0; i < funcInfos.size(); i++) {
	        if (s.equals(((FuncInfo)funcInfos.get(i)).funcName)) {
	          return ((FuncInfo)funcInfos.get(i)).funcCategory;
	        }
	      }
	      return null;
	}

	@Override
	public String getFuncDesc(String s) {
	    if (s == null) {
	        return null;
	      }
	      for (int i = 0; i < funcInfos.size(); i++) {
	        if (s.equals(((FuncInfo)funcInfos.get(i)).funcName)) {
	          return ((FuncInfo)funcInfos.get(i)).funcDesc;
	        }
	      }
	      return null;
	}
	
	 private String getCenterWareHByClinId(List paramList)
	  {
	    String result = "";
	    String clinicId = "";
	    if ((paramList.get(0) != null) && (!"".equals(paramList.get(0).toString()))) {
	    	clinicId = paramList.get(0).toString();
		    System.out.println("################clinicId:" + clinicId);

		    Context ctx = ContextUtils.getContextFromSessionNoException();
		    if (ctx == null) {
		      try
		      {
		        ctx = LZContectUtil.context("his");
		      }
		      catch (EASBizException e1)
		      {
		        e1.printStackTrace();
		      }
		      catch (BOSException e1)
		      {
		        e1.printStackTrace();
		      }
		    }
		    StringBuffer sql = new StringBuffer();
		    sql.append(" select a.CFWAREHOUSEID from CT_CUS_WareClinicRaleEntry a ").append("\r\n");
		    sql.append(" inner join CT_CUS_WareCREDE b on  a.fid = b.FPARENTID ").append("\r\n");
			sql.append(" where b.CFCLINICID = '").append(clinicId).append("' and a.CFDATASTATE =0 and b.CFDATASTATE = 0 ");  
 		    System.out.println("sql:" + sql.toString());
		    try
		    {
		      Connection con = EJBFactory.getConnection(ctx);
		      Statement batchStatement = con.createStatement();
		      ResultSet rs = batchStatement.executeQuery(sql.toString());
		      if ((rs != null) && 
		        (rs.next())) {
		        if ((rs.getString("CFWAREHOUSEID") != null) && (!"".equals(rs.getString("CFWAREHOUSEID").toString())))  
		          result = rs.getString("CFWAREHOUSEID").toString(); 
		      }
		      SQLUtils.cleanup(batchStatement, con);
		      SQLUtils.cleanup(con);
		    }
		    catch (SQLException e)
		    {
		      e.printStackTrace();
		    }
	    } 
	    return result;
	  }

}
