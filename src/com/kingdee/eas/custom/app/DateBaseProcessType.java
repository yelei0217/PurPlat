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
    public static final String UPDATE_VALUE = "1";//alias=����
    public static final String ADDNEW_VALUE = "2";//alias=����
    public static final String SEALUP_VALUE = "3";//alias=���
    public static final String UNSEALUP_VALUE = "4";//alias=�����
    public static final String DISABLE_VALUE = "5";//alias=����
    public static final String ENABLE_VALUE = "6";//alias=����
    public static final String DELETE_VALUE = "7";//alias=ɾ��
    public static final String ROLLBACK_VALUE = "8";//alias=�ع�
    public static final String PUSH_VALUE = "9";//alias=����
    public static final String GORDER_VALUE = "10";//alias=����-�ɹ�����
    public static final String GPAYMENT_VALUE = "11";//alias=����-���
    public static final String GPURRE_VALUE = "12";//alias=����-�ɹ��ջ�
    public static final String GLCM_VALUE = "13";//alias=����-��ֵ��Ƭ
    public static final String CLCMBILL_VALUE = "14";//alias=����-��ֵ��Ƭ����
    public static final String CPURREC_VALUE = "15";//alias=����-�ɹ�����
    public static final String GPURIN_MZ_VALUE = "16";//alias=���ɲɹ����-����
    public static final String GSALEISS_MZ_VALUE = "17";//alias=�������۳���-����
    public static final String GPURIN_LZ_VALUE = "18";//alias=���ɲɹ����-����
    public static final String GSALEISS_LZ_VALUE = "19";//alias=�������۳���-����
    public static final String GSALEISS_VALUE = "20";//alias=����-���۳���

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