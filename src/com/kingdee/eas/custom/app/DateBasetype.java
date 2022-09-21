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
    public static final String GZ_LZ_PO_VALUE = "60";//alias=��ֵ-�����ɹ�����
    public static final String GZ_LZ_SO_VALUE = "61";//alias=��ֵ-�������۶���
    public static final String GZ_MZ_PO_VALUE = "62";//alias=��ֵ-����ɹ�����
    public static final String GZ_LZ_PI_VALUE = "63";//alias=��ֵ-�����ɹ���ⵥ
    public static final String GZ_LZ_SS_VALUE = "64";//alias=��ֵ-�������۳��ⵥ
    public static final String GZB_LZ_PI_VALUE = "65";//alias=����-�ɹ���ⵥ(�쵥) 
    public static final String GZB_LZ_PO_CR_VALUE = "66";//alias=����-�ɹ�����(�Զ��йر�) 
    public static final String GZB_LZ_SS_VALUE = "67";//alias=����-�ɹ����۳���(�쵥)  
    public static final String GZB_LZ_SO_CR_VALUE = "68";//alias=����-���۶���(�Զ��йر�) 
    public static final String GZB_MZ_PI_VALUE = "69";//alias=����ɹ����(�쵥) 
    public static final String GZB_MZ_PO_CR_VALUE = "70";//alias=����-�ɹ�����(�Զ��йر�) 
    public static final String DZ_MZ_PO_VALUE = "71";//alias=��ֵ-����ɹ����� 
    public static final String DZ_MZ_PI_VALUE = "72";//alias=��ֵ-����ɹ���ⵥ 
    public static final String DZB_MZ_PI_VALUE = "73";//alias=��ַ-����ɹ���ⵥ(�쵥)  
    public static final String DZB_MZ_PO_CR_VALUE = "74";//alias=��ַ-����ɹ�����(�Զ��йر�)
    public static final String VMI2C_LZ_PI_VALUE = "75";//alias=VMI-�����ɹ���ⵥ 
    public static final String VMI2CB_LZ_PI_VALUE = "76";//alias=VMI-�����ɹ���ⵥ���쵥�� 
    public static final String VMI_LZ_SS_VALUE = "77";//alias=����-���۳��ⵥVMI 
    public static final String VMI_MZ_PI_VALUE = "78";//alias=����-�ɹ���ⵥVMI 
    public static final String VMIB_MZ_PI_VALUE = "79";//alias=����-�ɹ���ⵥVMI(�쵥)
    public static final String VMIB_LZ_SS_VALUE = "80";//alias=����-���۳��ⵥVMI(�쵥)
    public static final String CGZ_U_MZ_SO_VALUE = "81";//alias=�����ֵ����-���۶���  
    public static final String CGZ_U_MZ_SS_VALUE = "82";//alias=�����ֵ����-���۳���  
    public static final String CDZ_U_MZ_SS_VALUE = "83";//alias=�����ֵ����-���۳���  
    public static final String VMI_U_MZ_SO_VALUE = "84";//alias=����-���۶��� 
    public static final String VMI_U_MZ_SS_VMI_VALUE = "85";//alias=����-���۳���VMI ��EAS�Զ���
    public static final String VMI_U_MZ_PI_VALUE = "86";//alias=����-�ɹ���� ��EAS�Զ���
    public static final String VMI_U_MZ_SS_VALUE = "87";//alias=����-���۳��� ��EAS�Զ���
    public static final String VMI_U_LZ_PI_VALUE = "88";//alias=����-�ɹ���� ��EAS�Զ���
    public static final String VMI_U_LZ_SS_VALUE = "89";//alias=����-���۳��� ��EAS�Զ���
    public static final String SK_MZ_OPI_VALUE = "90";//alias=����-��Ӫ�̿�-������ⵥ 
    public static final String SK_MZ_OSS_VALUE = "91";//alias=����-��Ӫ�̿�-�������ⵥ  
    public static final String GZ_CK_LZ_AP_VALUE = "92";//alias=��ֵ����-����-Ӧ���� 
    public static final String GZ_CK_LZ_CJ_VALUE = "93";//alias=��ֵ����-����-�ɱ�������
    public static final String GZ_CK_LZ_P_VALUE = "94";//alias=��ֵ����-����-���
    public static final String GZ_CK_LZ_AR_VALUE = "95";//alias=��ֵ����-����-Ӧ�յ�
    public static final String GZ_CK_LZ_R_VALUE = "96";//alias=��ֵ����-����-�տ
    public static final String GZ_CK_MZ_AP_VALUE = "97";//alias=��ֵ����-����-Ӧ����
    public static final String GZ_CK_MZ_P_VALUE = "98";//alias=��ֵ����-����-���
    public static final String VMI_CK_LZ_AP_VALUE = "99";//alias=VMI����-����-Ӧ���� 
    public static final String VMI_CK_LZ_CJ_VALUE = "100";//alias=VMI����-����-�ɱ�������
    public static final String VMI_CK_LZ_P_VALUE = "101";//alias=VMI����-����-���
    public static final String VMI_CK_LZ_AR_VALUE = "102";//alias=VMI����-����-Ӧ�յ�
    public static final String VMI_CK_LZ_R_VALUE = "103";//alias=VMI����-����-�տ
    public static final String VMI_CK_MZ_AP_VALUE = "104";//alias=VMI����-����-Ӧ����
    public static final String VMI_CK_MZ_CJ_VALUE = "105";//alias=VMI����-����-�ɱ�������
    public static final String VMI_CK_MZ_P_VALUE = "106";//alias=VMI����-����-���
    public static final String YC_PI_VALUE = "107";//alias=��ݼӹ�-�ɹ���ⵥ
    public static final String YC_SS_VALUE = "108";//alias=��ݼӹ�-���۳��ⵥ
    public static final String YX_MZ_PI_VALUE = "109";//alias=���ν�����-����ɹ���ⵥ
    public static final String YX_LZ_PI_VALUE = "110";//alias=���ν�����-�����ɹ���ⵥ
    public static final String YX_LZ_SS_VALUE = "111";//alias=���ν�����-�������۳��ⵥ
    public static final String YX_MZ_SS_VALUE = "112";//alias=���ν�����-�������۳��� 
    public static final String ZZ_YC_LZ_PO_VALUE = "114";//alias=��ֲԭ����̨-�چ�-�ɹ�����
    public static final String ZZ_YC_LZ_SO_VALUE = "115";//alias=��ֲԭ����̨-�چ�-���۶���
    public static final String ZZ_YC_LZ_PI_VALUE = "116";//alias=��ֲԭ����̨-�چ�-�ɹ���ⵥ
    public static final String ZZ_YC_LZ_SS_VALUE = "117";//alias=��ֲԭ����̨-�چ�-���۳��ⵥ
    public static final String ZZ_YC_MZ_PO_VALUE = "118";//alias=��ֲԭ����̨-����-�ɹ�����
    public static final String ZZ_YC_MZ_PI_VALUE = "120";//alias=��ֲԭ����̨-����-�ɹ���ⵥ
    public static final String ZZ_YC_MZ_SO_VALUE = "121";//alias=��ֲԭ����̨-����-���۶���
    public static final String ZZ_YC_MZ_SS_VALUE = "122";//alias=��ֲԭ����̨-����-���۳��ⵥ
    public static final String ZZ_YC_MZ_PI_C_VALUE = "123";//alias=��ֲԭ����̨-����-�ɹ���ⵥ�ɹ���
    public static final String ZZ_GX_LZ_PI_VALUE = "124";//alias=��ֲ���Ի���̨����-�ɹ���ⵥ
    public static final String SO_LZ_PO_VALUE = "125";//alias=�������۲ɹ�-����-�ɹ�����
    public static final String SO_LZ_SO_VALUE = "126";//alias=�������۲ɹ�-����-���۶���
    public static final String SO_LZ_PI_VALUE = "127";//alias=�������۲ɹ�-����-�ɹ���ⵥ
    public static final String SO_LZ_SS_VALUE = "128";//alias=�������۲ɹ�-����-���۳��ⵥ
    public static final String SOB_LZ_SO_VALUE = "129";//alias=���������˻�-����-���۶���
    public static final String SOB_LZ_SS_VALUE = "131";//alias=���������˻�-����-���۳��ⵥ
    public static final String SOB_LZ_PI_VALUE = "134";//alias=���������˻�-����-�ɹ���ⵥ
    public static final String DZ_CK_MZ_AP_VALUE = "135";//alias=��ֵ����-����-Ӧ����
    public static final String DZ_CK_MZ_CJ_VALUE = "136";//alias=��ֵ����-����-�ɱ�������
    public static final String DZ_CK_MZ_P_VALUE = "137";//alias=��ֵ����-����-���
    public static final String YC_CK_MZ_AP_VALUE = "138";//alias=��ͨ���ӹ�����-����-Ӧ����
    public static final String YC_CK_MZ_CJ_VALUE = "139";//alias=��ͨ���ӹ�����-����-�ɱ�������
    public static final String YC_CK_MZ_P_VALUE = "140";//alias=��ͨ���ӹ�����-����-���
    public static final String YX_CK_LZ_AP_VALUE = "141";//alias=���ν�������-����-Ӧ����
    public static final String YX_CK_LZ_CJ_VALUE = "142";//alias=���ν�������-����-�ɱ�������
    public static final String YX_CK_LZ_P_VALUE = "143";//alias=���ν�������-����-���
    public static final String YX_CK_LZ_R_VALUE = "144";//alias=���ν�������-����-�տ
    public static final String YX_CK_MZ_AP_VALUE = "145";//alias=���ν�������-����-Ӧ����
    public static final String YX_CK_MZ_CJ_VALUE = "146";//alias=���ν�������-����-�ɱ�������
    public static final String YX_CK_MZ_P_VALUE = "147";//alias=���ν�������-����-���
    public static final String WARECLINICRALE_S_VALUE = "39";//alias=�ֿ������Ӧ��ϵ��ѯ
    public static final String WAREHOUSE_VALUE = "150";//alias=�ֿ�
    public static final String GZ_MZ_PI_VALUE = "148";//alias=����ɹ���ⵥ

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
    public static final DateBasetype GZ_LZ_PO = new DateBasetype("GZ_LZ_PO", GZ_LZ_PO_VALUE);
    public static final DateBasetype GZ_LZ_SO = new DateBasetype("GZ_LZ_SO", GZ_LZ_SO_VALUE);
    public static final DateBasetype GZ_MZ_PO = new DateBasetype("GZ_MZ_PO", GZ_MZ_PO_VALUE);
    public static final DateBasetype GZ_LZ_PI = new DateBasetype("GZ_LZ_PI", GZ_LZ_PI_VALUE);
    public static final DateBasetype GZ_LZ_SS = new DateBasetype("GZ_LZ_SS", GZ_LZ_SS_VALUE);
    public static final DateBasetype GZB_LZ_PI = new DateBasetype("GZB_LZ_PI", GZB_LZ_PI_VALUE);
    public static final DateBasetype GZB_LZ_PO_CR = new DateBasetype("GZB_LZ_PO_CR", GZB_LZ_PO_CR_VALUE);
    public static final DateBasetype GZB_LZ_SS = new DateBasetype("GZB_LZ_SS", GZB_LZ_SS_VALUE);
    public static final DateBasetype GZB_LZ_SO_CR = new DateBasetype("GZB_LZ_SO_CR", GZB_LZ_SO_CR_VALUE);
    public static final DateBasetype GZB_MZ_PI = new DateBasetype("GZB_MZ_PI", GZB_MZ_PI_VALUE);
    public static final DateBasetype GZB_MZ_PO_CR = new DateBasetype("GZB_MZ_PO_CR", GZB_MZ_PO_CR_VALUE);
    public static final DateBasetype DZ_MZ_PO = new DateBasetype("DZ_MZ_PO", DZ_MZ_PO_VALUE);
    public static final DateBasetype DZ_MZ_PI = new DateBasetype("DZ_MZ_PI", DZ_MZ_PI_VALUE);
    public static final DateBasetype DZB_MZ_PI = new DateBasetype("DZB_MZ_PI", DZB_MZ_PI_VALUE);
    public static final DateBasetype DZB_MZ_PO_CR = new DateBasetype("DZB_MZ_PO_CR", DZB_MZ_PO_CR_VALUE);
    public static final DateBasetype VMI2C_LZ_PI = new DateBasetype("VMI2C_LZ_PI", VMI2C_LZ_PI_VALUE);
    public static final DateBasetype VMI2CB_LZ_PI = new DateBasetype("VMI2CB_LZ_PI", VMI2CB_LZ_PI_VALUE);
    public static final DateBasetype VMI_LZ_SS = new DateBasetype("VMI_LZ_SS", VMI_LZ_SS_VALUE);
    public static final DateBasetype VMI_MZ_PI = new DateBasetype("VMI_MZ_PI", VMI_MZ_PI_VALUE);
    public static final DateBasetype VMIB_MZ_PI = new DateBasetype("VMIB_MZ_PI", VMIB_MZ_PI_VALUE);
    public static final DateBasetype VMIB_LZ_SS = new DateBasetype("VMIB_LZ_SS", VMIB_LZ_SS_VALUE);
    public static final DateBasetype CGZ_U_MZ_SO = new DateBasetype("CGZ_U_MZ_SO", CGZ_U_MZ_SO_VALUE);
    public static final DateBasetype CGZ_U_MZ_SS = new DateBasetype("CGZ_U_MZ_SS", CGZ_U_MZ_SS_VALUE);
    public static final DateBasetype CDZ_U_MZ_SS = new DateBasetype("CDZ_U_MZ_SS", CDZ_U_MZ_SS_VALUE);
    public static final DateBasetype VMI_U_MZ_SO = new DateBasetype("VMI_U_MZ_SO", VMI_U_MZ_SO_VALUE);
    public static final DateBasetype VMI_U_MZ_SS_VMI = new DateBasetype("VMI_U_MZ_SS_VMI", VMI_U_MZ_SS_VMI_VALUE);
    public static final DateBasetype VMI_U_MZ_PI = new DateBasetype("VMI_U_MZ_PI", VMI_U_MZ_PI_VALUE);
    public static final DateBasetype VMI_U_MZ_SS = new DateBasetype("VMI_U_MZ_SS", VMI_U_MZ_SS_VALUE);
    public static final DateBasetype VMI_U_LZ_PI = new DateBasetype("VMI_U_LZ_PI", VMI_U_LZ_PI_VALUE);
    public static final DateBasetype VMI_U_LZ_SS = new DateBasetype("VMI_U_LZ_SS", VMI_U_LZ_SS_VALUE);
    public static final DateBasetype SK_MZ_OPI = new DateBasetype("SK_MZ_OPI", SK_MZ_OPI_VALUE);
    public static final DateBasetype SK_MZ_OSS = new DateBasetype("SK_MZ_OSS", SK_MZ_OSS_VALUE);
    public static final DateBasetype GZ_CK_LZ_AP = new DateBasetype("GZ_CK_LZ_AP", GZ_CK_LZ_AP_VALUE);
    public static final DateBasetype GZ_CK_LZ_CJ = new DateBasetype("GZ_CK_LZ_CJ", GZ_CK_LZ_CJ_VALUE);
    public static final DateBasetype GZ_CK_LZ_P = new DateBasetype("GZ_CK_LZ_P", GZ_CK_LZ_P_VALUE);
    public static final DateBasetype GZ_CK_LZ_AR = new DateBasetype("GZ_CK_LZ_AR", GZ_CK_LZ_AR_VALUE);
    public static final DateBasetype GZ_CK_LZ_R = new DateBasetype("GZ_CK_LZ_R", GZ_CK_LZ_R_VALUE);
    public static final DateBasetype GZ_CK_MZ_AP = new DateBasetype("GZ_CK_MZ_AP", GZ_CK_MZ_AP_VALUE);
    public static final DateBasetype GZ_CK_MZ_P = new DateBasetype("GZ_CK_MZ_P", GZ_CK_MZ_P_VALUE);
    public static final DateBasetype VMI_CK_LZ_AP = new DateBasetype("VMI_CK_LZ_AP", VMI_CK_LZ_AP_VALUE);
    public static final DateBasetype VMI_CK_LZ_CJ = new DateBasetype("VMI_CK_LZ_CJ", VMI_CK_LZ_CJ_VALUE);
    public static final DateBasetype VMI_CK_LZ_P = new DateBasetype("VMI_CK_LZ_P", VMI_CK_LZ_P_VALUE);
    public static final DateBasetype VMI_CK_LZ_AR = new DateBasetype("VMI_CK_LZ_AR", VMI_CK_LZ_AR_VALUE);
    public static final DateBasetype VMI_CK_LZ_R = new DateBasetype("VMI_CK_LZ_R", VMI_CK_LZ_R_VALUE);
    public static final DateBasetype VMI_CK_MZ_AP = new DateBasetype("VMI_CK_MZ_AP", VMI_CK_MZ_AP_VALUE);
    public static final DateBasetype VMI_CK_MZ_CJ = new DateBasetype("VMI_CK_MZ_CJ", VMI_CK_MZ_CJ_VALUE);
    public static final DateBasetype VMI_CK_MZ_P = new DateBasetype("VMI_CK_MZ_P", VMI_CK_MZ_P_VALUE);
    public static final DateBasetype YC_PI = new DateBasetype("YC_PI", YC_PI_VALUE);
    public static final DateBasetype YC_SS = new DateBasetype("YC_SS", YC_SS_VALUE);
    public static final DateBasetype YX_MZ_PI = new DateBasetype("YX_MZ_PI", YX_MZ_PI_VALUE);
    public static final DateBasetype YX_LZ_PI = new DateBasetype("YX_LZ_PI", YX_LZ_PI_VALUE);
    public static final DateBasetype YX_LZ_SS = new DateBasetype("YX_LZ_SS", YX_LZ_SS_VALUE);
    public static final DateBasetype YX_MZ_SS = new DateBasetype("YX_MZ_SS", YX_MZ_SS_VALUE);
    public static final DateBasetype ZZ_YC_LZ_PO = new DateBasetype("ZZ_YC_LZ_PO", ZZ_YC_LZ_PO_VALUE);
    public static final DateBasetype ZZ_YC_LZ_SO = new DateBasetype("ZZ_YC_LZ_SO", ZZ_YC_LZ_SO_VALUE);
    public static final DateBasetype ZZ_YC_LZ_PI = new DateBasetype("ZZ_YC_LZ_PI", ZZ_YC_LZ_PI_VALUE);
    public static final DateBasetype ZZ_YC_LZ_SS = new DateBasetype("ZZ_YC_LZ_SS", ZZ_YC_LZ_SS_VALUE);
    public static final DateBasetype ZZ_YC_MZ_PO = new DateBasetype("ZZ_YC_MZ_PO", ZZ_YC_MZ_PO_VALUE);
    public static final DateBasetype ZZ_YC_MZ_PI = new DateBasetype("ZZ_YC_MZ_PI", ZZ_YC_MZ_PI_VALUE);
    public static final DateBasetype ZZ_YC_MZ_SO = new DateBasetype("ZZ_YC_MZ_SO", ZZ_YC_MZ_SO_VALUE);
    public static final DateBasetype ZZ_YC_MZ_SS = new DateBasetype("ZZ_YC_MZ_SS", ZZ_YC_MZ_SS_VALUE);
    public static final DateBasetype ZZ_YC_MZ_PI_C = new DateBasetype("ZZ_YC_MZ_PI_C", ZZ_YC_MZ_PI_C_VALUE);
    public static final DateBasetype ZZ_GX_LZ_PI = new DateBasetype("ZZ_GX_LZ_PI", ZZ_GX_LZ_PI_VALUE);
    public static final DateBasetype SO_LZ_PO = new DateBasetype("SO_LZ_PO", SO_LZ_PO_VALUE);
    public static final DateBasetype SO_LZ_SO = new DateBasetype("SO_LZ_SO", SO_LZ_SO_VALUE);
    public static final DateBasetype SO_LZ_PI = new DateBasetype("SO_LZ_PI", SO_LZ_PI_VALUE);
    public static final DateBasetype SO_LZ_SS = new DateBasetype("SO_LZ_SS", SO_LZ_SS_VALUE);
    public static final DateBasetype SOB_LZ_SO = new DateBasetype("SOB_LZ_SO", SOB_LZ_SO_VALUE);
    public static final DateBasetype SOB_LZ_SS = new DateBasetype("SOB_LZ_SS", SOB_LZ_SS_VALUE);
    public static final DateBasetype SOB_LZ_PI = new DateBasetype("SOB_LZ_PI", SOB_LZ_PI_VALUE);
    public static final DateBasetype DZ_CK_MZ_AP = new DateBasetype("DZ_CK_MZ_AP", DZ_CK_MZ_AP_VALUE);
    public static final DateBasetype DZ_CK_MZ_CJ = new DateBasetype("DZ_CK_MZ_CJ", DZ_CK_MZ_CJ_VALUE);
    public static final DateBasetype DZ_CK_MZ_P = new DateBasetype("DZ_CK_MZ_P", DZ_CK_MZ_P_VALUE);
    public static final DateBasetype YC_CK_MZ_AP = new DateBasetype("YC_CK_MZ_AP", YC_CK_MZ_AP_VALUE);
    public static final DateBasetype YC_CK_MZ_CJ = new DateBasetype("YC_CK_MZ_CJ", YC_CK_MZ_CJ_VALUE);
    public static final DateBasetype YC_CK_MZ_P = new DateBasetype("YC_CK_MZ_P", YC_CK_MZ_P_VALUE);
    public static final DateBasetype YX_CK_LZ_AP = new DateBasetype("YX_CK_LZ_AP", YX_CK_LZ_AP_VALUE);
    public static final DateBasetype YX_CK_LZ_CJ = new DateBasetype("YX_CK_LZ_CJ", YX_CK_LZ_CJ_VALUE);
    public static final DateBasetype YX_CK_LZ_P = new DateBasetype("YX_CK_LZ_P", YX_CK_LZ_P_VALUE);
    public static final DateBasetype YX_CK_LZ_R = new DateBasetype("YX_CK_LZ_R", YX_CK_LZ_R_VALUE);
    public static final DateBasetype YX_CK_MZ_AP = new DateBasetype("YX_CK_MZ_AP", YX_CK_MZ_AP_VALUE);
    public static final DateBasetype YX_CK_MZ_CJ = new DateBasetype("YX_CK_MZ_CJ", YX_CK_MZ_CJ_VALUE);
    public static final DateBasetype YX_CK_MZ_P = new DateBasetype("YX_CK_MZ_P", YX_CK_MZ_P_VALUE);
    public static final DateBasetype WareClinicRale_S = new DateBasetype("WareClinicRale_S", WARECLINICRALE_S_VALUE);
    public static final DateBasetype WAREHOUSE = new DateBasetype("WAREHOUSE", WAREHOUSE_VALUE);
    public static final DateBasetype GZ_MZ_PI = new DateBasetype("GZ_MZ_PI", GZ_MZ_PI_VALUE);

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