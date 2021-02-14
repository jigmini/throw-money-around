package com.jigmini.throwmoneyaround.service;

import java.util.HashMap;

/**
 * <PRE>
 * Service Layer
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
public interface ThrowMoneyAroundService {
    String saveThrowMoney(HashMap<String, String> paramMap) throws Exception;
    String saveGetMoney(HashMap<String, String> paramMap) throws Exception;
    HashMap retrieveThrowMoneyInfo(HashMap<String, String> paramMap) throws Exception;
}
