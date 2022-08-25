/**
 * output package name
 */
package com.kingdee.eas.custom.app;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import com.kingdee.util.enums.StringEnum;

/**
 * output class name
 */
public class DateBaseProcessType extends StringEnum
{
    public static final String UPDATE_VALUE = "1";//alias=更新
    public static final String ADDNEW_VALUE = "2";//alias=新增
    public static final String SEALUP_VALUE = "3";//alias=封存
    public static final String UNSEALUP_VALUE = "4";//alias=反封存
    public static final String DISABLE_VALUE = "5";//alias=禁用
    public static final String ENABLE_VALUE = "6";//alias=启用
    public static final String DELETE_VALUE = "7";//alias=删除
    public static final String ROLLBACK_VALUE = "8";//alias=回滚
    public static final String PUSH_VALUE = "9";//alias=下推
    public static final String GORDER_VALUE = "10";//alias=下推-采购订单
    public static final String GPAYMENT_VALUE = "11";//alias=下推-付款单
    public static final String GPURRE_VALUE = "12";//alias=下推-采购收货
    public static final String GLCM_VALUE = "13";//alias=下推-低值卡片
    public static final String CLCMBILL_VALUE = "14";//alias=创建-低值卡片领用
    public static final String CPURREC_VALUE = "15";//alias=生成-采购申请
    public static final String GPURIN_MZ_VALUE = "16";//alias=生成采购入库-门诊
    public static final String GSALEISS_MZ_VALUE = "17";//alias=生成销售出库-门诊
    public static final String GPURIN_LZ_VALUE = "18";//alias=生成采购入库-栗喆
    public static final String GSALEISS_LZ_VALUE = "19";//alias=生成销售出库-栗喆
    public static final String GSALEISS_VALUE = "20";//alias=下推-销售出库

    public static final DateBaseProcessType Update = new DateBaseProcessType("Update", UPDATE_VALUE);
    public static final DateBaseProcessType AddNew = new DateBaseProcessType("AddNew", ADDNEW_VALUE);
    public static final DateBaseProcessType SealUp = new DateBaseProcessType("SealUp", SEALUP_VALUE);
    public static final DateBaseProcessType UnSealUp = new DateBaseProcessType("UnSealUp", UNSEALUP_VALUE);
    public static final DateBaseProcessType DisAble = new DateBaseProcessType("DisAble", DISABLE_VALUE);
    public static final DateBaseProcessType ENABLE = new DateBaseProcessType("ENABLE", ENABLE_VALUE);
    public static final DateBaseProcessType DELETE = new DateBaseProcessType("DELETE", DELETE_VALUE);
    public static final DateBaseProcessType RollBack = new DateBaseProcessType("RollBack", ROLLBACK_VALUE);
    public static final DateBaseProcessType Push = new DateBaseProcessType("Push", PUSH_VALUE);
    public static final DateBaseProcessType GOrder = new DateBaseProcessType("GOrder", GORDER_VALUE);
    public static final DateBaseProcessType GPayment = new DateBaseProcessType("GPayment", GPAYMENT_VALUE);
    public static final DateBaseProcessType GPurRe = new DateBaseProcessType("GPurRe", GPURRE_VALUE);
    public static final DateBaseProcessType GLCM = new DateBaseProcessType("GLCM", GLCM_VALUE);
    public static final DateBaseProcessType CLCMBIll = new DateBaseProcessType("CLCMBIll", CLCMBILL_VALUE);
    public static final DateBaseProcessType CPurRec = new DateBaseProcessType("CPurRec", CPURREC_VALUE);
    public static final DateBaseProcessType GPurIn_MZ = new DateBaseProcessType("GPurIn_MZ", GPURIN_MZ_VALUE);
    public static final DateBaseProcessType GSaleIss_MZ = new DateBaseProcessType("GSaleIss_MZ", GSALEISS_MZ_VALUE);
    public static final DateBaseProcessType GPurIn_LZ = new DateBaseProcessType("GPurIn_LZ", GPURIN_LZ_VALUE);
    public static final DateBaseProcessType GSaleIss_LZ = new DateBaseProcessType("GSaleIss_LZ", GSALEISS_LZ_VALUE);
    public static final DateBaseProcessType GSaleIss = new DateBaseProcessType("GSaleIss", GSALEISS_VALUE);

    /**
     * construct function
     * @param String dateBaseProcessType
     */
    private DateBaseProcessType(String name, String dateBaseProcessType)
    {
        super(name, dateBaseProcessType);
    }
    
    /**
     * getEnum function
     * @param String arguments
     */
    public static DateBaseProcessType getEnum(String dateBaseProcessType)
    {
        return (DateBaseProcessType)getEnum(DateBaseProcessType.class, dateBaseProcessType);
    }

    /**
     * getEnumMap function
     */
    public static Map getEnumMap()
    {
        return getEnumMap(DateBaseProcessType.class);
    }

    /**
     * getEnumList function
     */
    public static List getEnumList()
    {
         return getEnumList(DateBaseProcessType.class);
    }
    
    /**
     * getIterator function
     */
    public static Iterator iterator()
    {
         return iterator(DateBaseProcessType.class);
    }
}