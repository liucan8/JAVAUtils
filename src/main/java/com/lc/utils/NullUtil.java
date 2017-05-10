package com.lc.utils;

import java.util.Collection;

/**
 * 空值判断
 *
 * @author zhaoyongfei
 */
public class NullUtil {

    /**
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (null == obj || obj.equals(null)) {
            return true;
        }
        return false;
    }

    /**
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 集合
     *
     * @param coll
     * @return
     */
    public static <E> boolean isNull(Collection<E> coll) {
        if (isNull(coll) || coll.size() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 自负序列，字符串，字符数组
     *
     * @param cs
     * @return
     */
    public static boolean isNull(CharSequence cs) {
        if (isNull(cs) || cs.length() < 1) {
            return true;
        }
        return false;
    }
}
