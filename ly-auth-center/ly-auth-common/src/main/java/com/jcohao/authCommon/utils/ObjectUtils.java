package com.jcohao.authCommon.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 对数据类型转换进行包装，里面提供判空的操作
 */
public class ObjectUtils {
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    public static Long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }

        if (obj instanceof Double || obj instanceof Float) {
            return Long.valueOf(StringUtils.substringBefore(obj.toString(), "."));
        }

        if (obj instanceof Number || obj instanceof String) {
            return Long.valueOf(obj.toString());
        } else {
            return 0L;
        }
    }

    public static Integer toInt(Object obj) {
        return toLong(obj).intValue();
    }


}
