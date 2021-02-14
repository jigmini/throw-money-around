package com.jigmini.common.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.io.Serializable;
import java.util.HashMap;

/*
 * <PRE>
 * Mybatis resultMap에 대해서 underscore case를 camal case로 변환하는 HashMap
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
public class CamelHashMap<K, V> extends HashMap<K, V> implements Serializable {

    public static final long serialVersionUID = 6723434363565878261L;
    
    @Override
    public V put(K key, V value) {
        return super.put((K) JdbcUtils.convertUnderscoreNameToPropertyName((String) key), value);
    }
}
