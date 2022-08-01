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
public class DateBasetype extends StringEnum
{
    public static final String ORGUNIT_VALUE = "1";//alias=��֯
    public static final String POSITION_VALUE = "2";//alias=ְλ
    public static final String SUPPLIER_VALUE = "3";//alias=��Ӧ��
    public static final String CUSTOMER_VALUE = "4";//alias=�ͻ�
    public static final String PERSON_VALUE = "5";//alias=��Ա
    public static final String MATERIAL_VALUE = "6";//alias=����
    public static final String PAYTYPE_VALUE = "7";//alias=���㷽ʽ
    public static final String FREEITEM_VALUE = "8";//alias=�շ���Ŀ
    public static final String MAINCLASS_VALUE = "9";//alias=ҵ�������ݷ���
    public static final String HIS_SALEORDER_VALUE = "10";//alias=his���۶���
    public static final String HIS_RECEIVABLES_VALUE = "11";//alias=hisӦ�յ�
    public static final String HIS_RECEIPT_VALUE = "12";//alias=his�տ
    public static final String HIS_PURCHIN_VALUE = "13";//alias=his�ɹ����
    public static final String HIS_SALEOUT_VALUE = "14";//alias=���۳���
    public static final String SUPPLIER2HIS_VALUE = "15";//alias=��Ӧ��2HIS
    public static final String PAYTYPETREE_VALUE = "16";//alias=���㷽ʽtree
    public static final String OA_OTHERBILL_VALUE = "17";//alias=����Ӧ����oa����
    public static final String OA_PURREQUEST_VALUE = "18";//alias=�ɹ����뵥oa����
    public static final String PAYMENTBILLTOMID_VALUE = "19";//alias=�������뵥����oa
    public static final String EXPENSETYPETOMID_VALUE = "20";//alias=��������ͬ�����м��
    public static final String SUPPLYINFOTOMID_VALUE = "21";//alias=������Ϣ
    public static final String OA_PAYMENTBILL_VALUE = "22";//alias=���
    public static final String HIS_PORCESS_VALUE = "23";//alias=his���ӹ�
    public static final String CROSSCLINICSALL_VALUE = "24";//alias=����֯����������
    public static final String BORROWLANDBILL_VALUE = "25";//alias=������������
    public static final String HR_RUZHI_VALUE = "26";//alias=Ա����ְ
    public static final String HR_LIZHI_VALUE = "27";//alias=Ա����ְ
    public static final String HR_YIDONG_VALUE = "28";//alias=Ա���춯
    public static final String VSPJD_VALUE = "29";//alias=�����۲�
    public static final String OA_SHR_HIRE_VALUE = "30";//alias=oa��shrת��ͬ��
    public static final String OA_SHR_RESIGN_VALUE = "31";//alias=oa��shr��ְͬ��
    public static final String OA_SHR_FLUCT_VALUE = "32";//alias=oa��shr����ͬ��(�ڲ�)
    public static final String OA_SHR_FLUCTOUT_VALUE = "33";//alias=oa��shr����ͬ��(�ⲿ)
    public static final String HIS_BACK_VALUE = "34";//alias=HIS�����ﻹǷ��
    public static final String HIS_REP_VALUE = "35";//alias=HIS������Ԥ��������
    public static final String HIS_REPORT_VALUE = "36";//alias=HIS�����ձ�
    public static final String HIS_BACK_EXT_VALUE = "37";//alias=HIS�����ﻹǷ��(��)
    public static final String HIS_REP_EXT_VALUE = "38";//alias=HIS������Ԥ��������(��)
    public static final String B2B_GZ_LZ_PO_VALUE = "60";//alias=��ֵ-�����ɹ�����
    public static final String B2B_GZ_LZ_SO_VALUE = "61";//alias=��ֵ-�������۶���
    public static final String B2B_GZ_MZ_PO_VALUE = "62";//alias=��ֵ-����ɹ�����
    public static final String B2B_GZ_LZ_PI_VALUE = "63";//alias=��ֵ-�����ɹ���ⵥ
    public static final String B2B_GZ_LZ_SS_VALUE = "64";//alias=��ֵ-�������۳��ⵥ

    public static final DateBasetype orgUnit = new DateBasetype("orgUnit", ORGUNIT_VALUE);
    public static final DateBasetype position = new DateBasetype("position", POSITION_VALUE);
    public static final DateBasetype Supplier = new DateBasetype("Supplier", SUPPLIER_VALUE);
    public static final DateBasetype Customer = new DateBasetype("Customer", CUSTOMER_VALUE);
    public static final DateBasetype Person = new DateBasetype("Person", PERSON_VALUE);
    public static final DateBasetype Material = new DateBasetype("Material", MATERIAL_VALUE);
    public static final DateBasetype PayType = new DateBasetype("PayType", PAYTYPE_VALUE);
    public static final DateBasetype FreeItem = new DateBasetype("FreeItem", FREEITEM_VALUE);
    public static final DateBasetype MainClass = new DateBasetype("MainClass", MAINCLASS_VALUE);
    public static final DateBasetype HIS_SaleOrder = new DateBasetype("HIS_SaleOrder", HIS_SALEORDER_VALUE);
    public static final DateBasetype HIS_Receivables = new DateBasetype("HIS_Receivables", HIS_RECEIVABLES_VALUE);
    public static final DateBasetype HIS_Receipt = new DateBasetype("HIS_Receipt", HIS_RECEIPT_VALUE);
    public static final DateBasetype HIS_PurchIn = new DateBasetype("HIS_PurchIn", HIS_PURCHIN_VALUE);
    public static final DateBasetype HIS_SaleOut = new DateBasetype("HIS_SaleOut", HIS_SALEOUT_VALUE);
    public static final DateBasetype Supplier2HIS = new DateBasetype("Supplier2HIS", SUPPLIER2HIS_VALUE);
    public static final DateBasetype PayTypeTree = new DateBasetype("PayTypeTree", PAYTYPETREE_VALUE);
    public static final DateBasetype OA_OtherBill = new DateBasetype("OA_OtherBill", OA_OTHERBILL_VALUE);
    public static final DateBasetype OA_PurRequest = new DateBasetype("OA_PurRequest", OA_PURREQUEST_VALUE);
    public static final DateBasetype PaymentBillToMid = new DateBasetype("PaymentBillToMid", PAYMENTBILLTOMID_VALUE);
    public static final DateBasetype ExpenseTypeToMid = new DateBasetype("ExpenseTypeToMid", EXPENSETYPETOMID_VALUE);
    public static final DateBasetype SupplyinfoToMid = new DateBasetype("SupplyinfoToMid", SUPPLYINFOTOMID_VALUE);
    public static final DateBasetype OA_PaymentBill = new DateBasetype("OA_PaymentBill", OA_PAYMENTBILL_VALUE);
    public static final DateBasetype HIS_Porcess = new DateBasetype("HIS_Porcess", HIS_PORCESS_VALUE);
    public static final DateBasetype CrossClinicsAll = new DateBasetype("CrossClinicsAll", CROSSCLINICSALL_VALUE);
    public static final DateBasetype BorrowLandBill = new DateBasetype("BorrowLandBill", BORROWLANDBILL_VALUE);
    public static final DateBasetype HR_RUZHI = new DateBasetype("HR_RUZHI", HR_RUZHI_VALUE);
    public static final DateBasetype HR_LIZHI = new DateBasetype("HR_LIZHI", HR_LIZHI_VALUE);
    public static final DateBasetype HR_YIDONG = new DateBasetype("HR_YIDONG", HR_YIDONG_VALUE);
    public static final DateBasetype VSPJD = new DateBasetype("VSPJD", VSPJD_VALUE);
    public static final DateBasetype OA_SHR_Hire = new DateBasetype("OA_SHR_Hire", OA_SHR_HIRE_VALUE);
    public static final DateBasetype OA_SHR_Resign = new DateBasetype("OA_SHR_Resign", OA_SHR_RESIGN_VALUE);
    public static final DateBasetype OA_SHR_Fluct = new DateBasetype("OA_SHR_Fluct", OA_SHR_FLUCT_VALUE);
    public static final DateBasetype OA_SHR_FluctOut = new DateBasetype("OA_SHR_FluctOut", OA_SHR_FLUCTOUT_VALUE);
    public static final DateBasetype HIS_Back = new DateBasetype("HIS_Back", HIS_BACK_VALUE);
    public static final DateBasetype HIS_Rep = new DateBasetype("HIS_Rep", HIS_REP_VALUE);
    public static final DateBasetype HIS_Report = new DateBasetype("HIS_Report", HIS_REPORT_VALUE);
    public static final DateBasetype HIS_Back_Ext = new DateBasetype("HIS_Back_Ext", HIS_BACK_EXT_VALUE);
    public static final DateBasetype HIS_Rep_Ext = new DateBasetype("HIS_Rep_Ext", HIS_REP_EXT_VALUE);
    public static final DateBasetype B2B_GZ_LZ_PO = new DateBasetype("B2B_GZ_LZ_PO", B2B_GZ_LZ_PO_VALUE);
    public static final DateBasetype B2B_GZ_LZ_SO = new DateBasetype("B2B_GZ_LZ_SO", B2B_GZ_LZ_SO_VALUE);
    public static final DateBasetype B2B_GZ_MZ_PO = new DateBasetype("B2B_GZ_MZ_PO", B2B_GZ_MZ_PO_VALUE);
    public static final DateBasetype B2B_GZ_LZ_PI = new DateBasetype("B2B_GZ_LZ_PI", B2B_GZ_LZ_PI_VALUE);
    public static final DateBasetype B2B_GZ_LZ_SS = new DateBasetype("B2B_GZ_LZ_SS", B2B_GZ_LZ_SS_VALUE);

    /**
     * construct function
     * @param String dateBasetype
     */
    private DateBasetype(String name, String dateBasetype)
    {
        super(name, dateBasetype);
    }
    
    /**
     * getEnum function
     * @param String arguments
     */
    public static DateBasetype getEnum(String dateBasetype)
    {
        return (DateBasetype)getEnum(DateBasetype.class, dateBasetype);
    }

    /**
     * getEnumMap function
     */
    public static Map getEnumMap()
    {
        return getEnumMap(DateBasetype.class);
    }

    /**
     * getEnumList function
     */
    public static List getEnumList()
    {
         return getEnumList(DateBasetype.class);
    }
    
    /**
     * getIterator function
     */
    public static Iterator iterator()
    {
         return iterator(DateBasetype.class);
    }
}