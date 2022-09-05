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
public class PurPlatSyncEnum extends StringEnum
{
    public static final String SUCCESS_VALUE = "200";//alias=�����ɹ�
    public static final String JSON_ERROR_VALUE = "-1";//alias=JSON��ʽ�����쳣
    public static final String EXCEPTION_SERVER_VALUE = "-2";//alias=������쳣
    public static final String EXCEPTION_NET_VALUE = "-3";//alias=�����쳣
    public static final String EXISTS_BILL_VALUE = "-4";//alias=ҵ�񵥾��Ѵ���
    public static final String FIELD_NULL_VALUE = "-5";//alias=����Ϊ��
    public static final String NOTEXISTS_BILL_VALUE = "-6";//alias=ҵ�񵥾ݲ�����

    public static final PurPlatSyncEnum SUCCESS = new PurPlatSyncEnum("SUCCESS", SUCCESS_VALUE);
    public static final PurPlatSyncEnum JSON_ERROR = new PurPlatSyncEnum("JSON_ERROR", JSON_ERROR_VALUE);
    public static final PurPlatSyncEnum EXCEPTION_SERVER = new PurPlatSyncEnum("EXCEPTION_SERVER", EXCEPTION_SERVER_VALUE);
    public static final PurPlatSyncEnum EXCEPTION_NET = new PurPlatSyncEnum("EXCEPTION_NET", EXCEPTION_NET_VALUE);
    public static final PurPlatSyncEnum EXISTS_BILL = new PurPlatSyncEnum("EXISTS_BILL", EXISTS_BILL_VALUE);
    public static final PurPlatSyncEnum FIELD_NULL = new PurPlatSyncEnum("FIELD_NULL", FIELD_NULL_VALUE);
    public static final PurPlatSyncEnum NOTEXISTS_BILL = new PurPlatSyncEnum("NOTEXISTS_BILL", NOTEXISTS_BILL_VALUE);

    /**
     * construct function
     * @param String purPlatSyncEnum
     */
    private PurPlatSyncEnum(String name, String purPlatSyncEnum)
    {
        super(name, purPlatSyncEnum);
    }
    
    /**
     * getEnum function
     * @param String arguments
     */
    public static PurPlatSyncEnum getEnum(String purPlatSyncEnum)
    {
        return (PurPlatSyncEnum)getEnum(PurPlatSyncEnum.class, purPlatSyncEnum);
    }

    /**
     * getEnumMap function
     */
    public static Map getEnumMap()
    {
        return getEnumMap(PurPlatSyncEnum.class);
    }

    /**
     * getEnumList function
     */
    public static List getEnumList()
    {
         return getEnumList(PurPlatSyncEnum.class);
    }
    
    /**
     * getIterator function
     */
    public static Iterator iterator()
    {
         return iterator(PurPlatSyncEnum.class);
    }
}