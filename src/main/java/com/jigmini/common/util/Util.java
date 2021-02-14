package com.jigmini.common.util;

import java.util.HashMap;

/*
 * <PRE>
 * Util
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
public class Util {

    public static boolean isEmpty(HashMap<String, String> map, String key) {
        if (!map.containsKey("key") || !"".equals((String) map.get("key"))) {
            return false;
        } else {
            return true;
        }
    }
}
