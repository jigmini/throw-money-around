package com.jigmini.throwmoneyaround.service.impl;

import com.jigmini.common.exception.BizException;
import com.jigmini.common.util.CamelHashMap;
import com.jigmini.throwmoneyaround.dao.ThrowMoneyAroundDAO;
import com.jigmini.throwmoneyaround.service.ThrowMoneyAroundService;
import com.jigmini.throwmoneyaround.web.ThrowMoneyAroundController;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Service Layer
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */

@Service
public class ThrowMoneyAroundServiceImpl implements ThrowMoneyAroundService {

    Logger logger = LoggerFactory.getLogger(ThrowMoneyAroundController.class);

    @Autowired
    ThrowMoneyAroundDAO throwMoneyAroundDAO;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveThrowMoney(HashMap<String, String> paramMap) throws Exception {

        logger.debug("ThrowMoneyAroundServiceImpl.saveThrowMoney : " + paramMap.toString());

        // Token 발행
        String throwToken = RandomStringUtils.randomAlphanumeric(3);
        paramMap.put("throwToken", throwToken);

        // 돈 뿌리기 저장
        String throwId = throwMoneyAroundDAO.selectThrowId();
        paramMap.put("throwId", throwId);
        throwMoneyAroundDAO.insertThrowMoney(paramMap);

        // 돈 받기 저장
        int throwNum = Integer.parseInt(paramMap.get("throwNum"));
        int throwAmt = Integer.parseInt(paramMap.get("throwAmt"));
        String userId = paramMap.get("userId");
        for (int i = 0; i < throwNum; i++) {
            HashMap<String, String> getMoneyMap = new HashMap<String, String>();

            int getAmt = throwAmt / (throwNum - i);
            throwAmt -= getAmt;

            getMoneyMap.put("throwId", throwId);
            getMoneyMap.put("userId", userId);
            getMoneyMap.put("getAmt", String.valueOf(getAmt));

            throwMoneyAroundDAO.insertGetMoney(getMoneyMap);
        }

        // 사용자계좌 출금
        paramMap.put("depWitDivCode", "02"); // 입출금구분코드 01: 입금, 02: 출금
        paramMap.put("witAmt", paramMap.get("throwAmt")); // 출금금액
        throwMoneyAroundDAO.updateUserAccount(paramMap);

        return throwToken;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveGetMoney(HashMap<String, String> paramMap) throws Exception {

        logger.debug("ThrowMoneyAroundServiceImpl.saveGetMoney : " + paramMap.toString());

        // 받기 가능한 돈뿌리기가 있는지 정보 조회
        CamelHashMap<String, String> getMoneyMap = throwMoneyAroundDAO.selectThrowGetMoneyInfo(paramMap);

        if (getMoneyMap == null || getMoneyMap.size() < 3) {
            throw new BizException("-98,다음의 에러 중 하나: 뿌린 사람은 받을 수 없음 / 이미 받음 / 대화방에 없음 / 뿌린지 10분 경과 / 뿌리기 취소 / 뿌린 금액 모두 소진");
        } else {
            logger.debug(getMoneyMap.toString());
        }

        // 돈 뿌리기 수정
        throwMoneyAroundDAO.updateThrowMoney(getMoneyMap);

        // 돈 받기 수정
        throwMoneyAroundDAO.updateGetMoney(getMoneyMap);

        // 사용자계좌 입금
        HashMap<String, String> depMap = new HashMap<String, String>();
        String getAmt = String.valueOf(getMoneyMap.get("getAmt"));

        depMap.put("depWitDivCode", "01"); // 입출금구분코드 01: 입금, 02: 출금
        depMap.put("depAmt", getAmt); // 입금금액
        depMap.put("userId", paramMap.get("userId"));

        logger.debug("받기 입금 처리 직전 " + depMap.toString());

        throwMoneyAroundDAO.updateUserAccount(depMap);

        return getAmt;
    }

    @Override
    public HashMap retrieveThrowMoneyInfo(HashMap<String, String> paramMap) throws Exception {
        logger.debug("ThrowMoneyAroundServiceImpl.retrieveThrowMoneyInfo : " + paramMap.toString());

        HashMap resultMap = new HashMap();

        // 돈뿌리기 정보 조회
        CamelHashMap<String, String> throwMoneyInfo = throwMoneyAroundDAO.selectThrowMoneyInfo(paramMap);

        if (throwMoneyInfo == null || throwMoneyInfo.size() < 1) {
            throw new BizException("-97,다음의 에러 중 하나: 대화방 또는 뿌린사람이 없음 / 뿌린 기간이 7일 경과 / 토큰값 불일치");
        } else {
            logger.debug(throwMoneyInfo.toString());
        }

        // 뿌리기TOKEN에 대한 받은 정보 조회
        List<CamelHashMap<String, String>> getMoneyList = throwMoneyAroundDAO.selectGetMoneyInfo(throwMoneyInfo);

        resultMap.put("throwDt", throwMoneyInfo.get("regiDt"));
        resultMap.put("throwAmt", throwMoneyInfo.get("throwAmt"));
        resultMap.put("getFnshAmt", throwMoneyInfo.get("getFnshAmt"));
        resultMap.put("getMoneyList", getMoneyList);

        return resultMap;
    }
}
