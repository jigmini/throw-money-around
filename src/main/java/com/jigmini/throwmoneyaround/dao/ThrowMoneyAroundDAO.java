package com.jigmini.throwmoneyaround.dao;

import com.jigmini.common.util.CamelHashMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * DB Access를 담당하는 Repository Layer
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */

@Mapper
@Repository
public interface ThrowMoneyAroundDAO {
    String selectThrowId();
    CamelHashMap selectThrowGetMoneyInfo(HashMap paramMap);
    CamelHashMap selectThrowMoneyInfo(HashMap paramMap);
    List selectGetMoneyInfo(HashMap paramMap);
    void insertThrowMoney(HashMap paramMap);
    void insertGetMoney(HashMap paramMap);
    void updateThrowMoney(HashMap paramMap);
    void updateGetMoney(HashMap paramMap);
    void updateUserAccount(HashMap paramMap);
}