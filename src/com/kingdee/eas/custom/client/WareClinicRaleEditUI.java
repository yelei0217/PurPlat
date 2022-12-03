/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kingdee.bos.ctrl.extendcontrols.BizDataFormat;
import com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox;
import com.kingdee.bos.ctrl.kdf.table.KDTDefaultCellEditor;
import com.kingdee.bos.ctrl.kdf.table.KDTable;
import com.kingdee.bos.ctrl.kdf.util.render.ObjectValueRender;
import com.kingdee.bos.ctrl.swing.KDTextField;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.eas.basedata.scm.im.inv.WHStateEnum;

/**
 * output class name
 */
public class WareClinicRaleEditUI extends AbstractWareClinicRaleEditUI
{
    private static final Logger logger = CoreUIObject.getLogger(WareClinicRaleEditUI.class);
    
    /**
     * output class constructor
     */
    public WareClinicRaleEditUI() throws Exception
    {
        super();
        
        KDBizPromptBox kdtEntrys_warehouse_PromptBox = new KDBizPromptBox();
        kdtEntrys_warehouse_PromptBox.setQueryInfo("com.kingdee.eas.basedata.scm.im.inv.app.F7AllWarehouseQuery");
        kdtEntrys_warehouse_PromptBox.setVisible(true);
        kdtEntrys_warehouse_PromptBox.setEditable(true);
        kdtEntrys_warehouse_PromptBox.setDisplayFormat("$number$");
        kdtEntrys_warehouse_PromptBox.setEditFormat("$number$");
        kdtEntrys_warehouse_PromptBox.setCommitFormat("$number$");
        
        EntityViewInfo viewInfo = new EntityViewInfo();
        FilterInfo filter = new FilterInfo();
        filter.getFilterItems().add(new FilterItemInfo("storageOrg.id","jbYAAAMU2SvM567U",CompareType.EQUALS));//ø‚¥Ê◊È÷ØID
        filter.getFilterItems().add(new FilterItemInfo("whState",WHStateEnum.ACTIVE_VALUE,CompareType.EQUALS));//≤÷ø‚◊¥Ã¨
        viewInfo.setFilter(filter);
        kdtEntrys_warehouse_PromptBox.setEntityViewInfo(viewInfo);
        
        KDTDefaultCellEditor kdtEntrys_warehouse_CellEditor = new KDTDefaultCellEditor(kdtEntrys_warehouse_PromptBox);
        this.kdtEntrys.getColumn("warehouse").setEditor(kdtEntrys_warehouse_CellEditor);
        ObjectValueRender kdtEntrys_warehouse_OVR = new ObjectValueRender();
        kdtEntrys_warehouse_OVR.setFormat(new BizDataFormat("$number$"));
        this.kdtEntrys.getColumn("warehouse").setRenderer(kdtEntrys_warehouse_OVR);
        KDTextField kdtEntrys_warehouseName_TextField = new KDTextField();
        kdtEntrys_warehouseName_TextField.setName("kdtEntrys_warehouseName_TextField");
        kdtEntrys_warehouseName_TextField.setMaxLength(80);
        KDTDefaultCellEditor kdtEntrys_warehouseName_CellEditor = new KDTDefaultCellEditor(kdtEntrys_warehouseName_TextField);
        this.kdtEntrys.getColumn("warehouseName").setEditor(kdtEntrys_warehouseName_CellEditor);
   
    
    
        KDBizPromptBox kdtDEntrys_Clinic_PromptBox = new KDBizPromptBox();
        kdtDEntrys_Clinic_PromptBox.setQueryInfo("com.kingdee.eas.basedata.org.app.StorageItemQuery");
        kdtDEntrys_Clinic_PromptBox.setVisible(true);
        kdtDEntrys_Clinic_PromptBox.setEditable(true);
        kdtDEntrys_Clinic_PromptBox.setDisplayFormat("$number$");
        kdtDEntrys_Clinic_PromptBox.setEditFormat("$number$");
        kdtDEntrys_Clinic_PromptBox.setCommitFormat("$number$");

        viewInfo = new EntityViewInfo();
        filter = new FilterInfo();
        filter.getFilterItems().add(new FilterItemInfo("isCU",0,CompareType.EQUALS));// «∑Ò «CU
        filter.getFilterItems().add(new FilterItemInfo("IsOUSealUp",0,CompareType.EQUALS));// «∑Ò∑‚¥Ê
        filter.getFilterItems().add(new FilterItemInfo("number","MS%",CompareType.LIKE));// «∑Ò∆Ù”√
        viewInfo.setFilter(filter);
        kdtDEntrys_Clinic_PromptBox.setEntityViewInfo(viewInfo);
        
        KDTDefaultCellEditor kdtDEntrys_Clinic_CellEditor = new KDTDefaultCellEditor(kdtDEntrys_Clinic_PromptBox);
        this.kdtDEntrys.getColumn("Clinic").setEditor(kdtDEntrys_Clinic_CellEditor);
        ObjectValueRender kdtDEntrys_Clinic_OVR = new ObjectValueRender();
        kdtDEntrys_Clinic_OVR.setFormat(new BizDataFormat("$number$"));
        this.kdtDEntrys.getColumn("Clinic").setRenderer(kdtDEntrys_Clinic_OVR);
        KDTextField kdtDEntrys_ClinicName_TextField = new KDTextField();
        kdtDEntrys_ClinicName_TextField.setName("kdtDEntrys_ClinicName_TextField");
        kdtDEntrys_ClinicName_TextField.setMaxLength(80);
        KDTDefaultCellEditor kdtDEntrys_ClinicName_CellEditor = new KDTDefaultCellEditor(kdtDEntrys_ClinicName_TextField);
        this.kdtDEntrys.getColumn("ClinicName").setEditor(kdtDEntrys_ClinicName_CellEditor);
     
    }
    
    @Override
    public void kdtEntrys_Changed(int rowIndex, int colIndex) throws Exception {
    	// TODO Auto-generated method stub
    	super.kdtEntrys_Changed(rowIndex, colIndex);
    }
    
    @Override
    public void actionSave_actionPerformed(ActionEvent e) throws Exception {
     	super.actionSave_actionPerformed(e);
    }
     
    /**
     * output loadFields method
     */
    public void loadFields()
    {
        super.loadFields();
    }

    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }

	@Override
	protected void beforeStoreFields(ActionEvent arg0) throws Exception {
 		super.beforeStoreFields(arg0); 
    	Set set = new HashSet();
 		for (int i=0,n=kdtEntrys.getRowCount();i<n;i++) {
			if (com.kingdee.bos.ui.face.UIRuleUtil.isNotNull(kdtEntrys.getCell(i,"warehouse").getValue()))  
				set.add(kdtEntrys.getCell(i,"warehouse").getValue()); 
		}
 		if(set.size() < kdtEntrys.getRowCount())
 			throw new com.kingdee.eas.common.EASBizException(com.kingdee.eas.common.EASBizException.CHECKNUMDUP,new Object[] {"≤÷ø‚±‡¬Î"});
 		set.clear();
 		for (int i=0,n=kdtDEntrys.getRowCount();i<n;i++) {
			if (com.kingdee.bos.ui.face.UIRuleUtil.isNotNull(kdtDEntrys.getCell(i,"Clinic").getValue()))  
				set.add(kdtDEntrys.getCell(i,"Clinic").getValue());
		}
 		if(set.size() < kdtDEntrys.getRowCount())
 			throw new com.kingdee.eas.common.EASBizException(com.kingdee.eas.common.EASBizException.CHECKNUMDUP,new Object[] {"√≈’Ô±‡¬Î"});
 		set.clear();
	}
    
    /**
     * output getBizInterface method
     */
    protected com.kingdee.eas.framework.ICoreBase getBizInterface() throws Exception
    {
        return com.kingdee.eas.custom.WareClinicRaleFactory.getRemoteInstance();
    }

    /**
     * output createNewDetailData method
     */
    protected IObjectValue createNewDetailData(KDTable table)
    {
		
        return null;
    }

    /**
     * output createNewData method
     */
    protected com.kingdee.bos.dao.IObjectValue createNewData()
    {
        com.kingdee.eas.custom.WareClinicRaleInfo objectValue = new com.kingdee.eas.custom.WareClinicRaleInfo();
        objectValue.setCreator((com.kingdee.eas.base.permission.UserInfo)(com.kingdee.eas.common.client.SysContext.getSysContext().getCurrentUser()));
		
        return objectValue;
    }

}