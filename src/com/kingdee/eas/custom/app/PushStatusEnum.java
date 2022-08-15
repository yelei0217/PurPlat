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
public class PushStatusEnum extends StringEnum
{
    public static final String UNDO_VALUE = "0";//alias=未下推
    public static final String DOSUCCESS_VALUE = "1";//alias=下推成功
    public static final String DOFAIL_VALUE = "-1";//alias=下推失败

    public static final PushStatusEnum unDo = new PushStatusEnum("unDo", UNDO_VALUE);
    public static final PushStatusEnum doSuccess = new PushStatusEnum("doSuccess", DOSUCCESS_VALUE);
    public static final PushStatusEnum doFail = new PushStatusEnum("doFail", DOFAIL_VALUE);

    /**
     * construct function
     * @param String pushStatusEnum
     */
    private PushStatusEnum(String name, String pushStatusEnum)
    {
        super(name, pushStatusEnum);
    }
    
    /**
     * getEnum function
     * @param String arguments
     */
    public static PushStatusEnum getEnum(String pushStatusEnum)
    {
        return (PushStatusEnum)getEnum(PushStatusEnum.class, pushStatusEnum);
    }

    /**
     * getEnumMap function
     */
    public static Map getEnumMap()
    {
        return getEnumMap(PushStatusEnum.class);
    }

    /**
     * getEnumList function
     */
    public static List getEnumList()
    {
         return getEnumList(PushStatusEnum.class);
    }
    
    /**
     * getIterator function
     */
    public static Iterator iterator()
    {
         return iterator(PushStatusEnum.class);
    }
}