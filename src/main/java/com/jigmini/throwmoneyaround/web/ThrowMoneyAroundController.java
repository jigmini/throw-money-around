package com.jigmini.throwmoneyaround.web;

import com.jigmini.common.exception.BizException;
import com.jigmini.throwmoneyaround.service.ThrowMoneyAroundService;
import com.jigmini.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * <PRE>
 * Web Layer
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
@RestController
@RequestMapping("/throwmoneyaround")
public class ThrowMoneyAroundController {

    Logger logger = LoggerFactory.getLogger(ThrowMoneyAroundController.class);

    @Autowired
    ThrowMoneyAroundService throwMoneyAroundService;

    @GetMapping("/hello")
    public String hello() {
        logger.debug("hello");
        return "hello";
    }


    /**
     * <pre>
     * 뿌리기
     * </pre>
     *
     * URL
     * - http://localhost:8988/throwmoneyaround/throw
     *
     * @author 정선우
     * @param
     * @return throwToken, resultCode, resultMsg
     */
    @PostMapping("/throw")
    public HashMap<String, String> createThrowMoney(@RequestParam HashMap<String, String> requestParamMap,
                                   @RequestHeader(value="x-user-id") String userId,
                                   @RequestHeader(value="x-room-id") String roomId) {

        HashMap<String, String> resultMap = new HashMap<String, String>();

        logger.debug(requestParamMap.toString());
        logger.debug(userId + ", " + roomId);

        // validation-check
        if ("".equals(userId) || "".equals(roomId)) {
            logger.debug("userId 또는 roomId가 없습니다.");
            resultMap.put("resultCode", "-1");
            resultMap.put("resultMsg", "userId 또는 roomId가 없습니다.");
        }
        if (Util.isEmpty(requestParamMap,"throwAmt") || Util.isEmpty(requestParamMap,"throwNum")) {
            logger.debug("필수 값이 없습니다.");
            resultMap.put("resultCode", "-2");
            resultMap.put("resultMsg", "필수 값이 없습니다.");
        }
        if (Integer.parseInt(requestParamMap.get("throwAmt")) <= 0 || Integer.parseInt(requestParamMap.get("throwNum")) <= 0) {
            logger.debug("입력 값이 0 이하일 수 없습니다.");
            resultMap.put("resultCode", "-3");
            resultMap.put("resultMsg", "입력 값이 0 이하일 수 없습니다.");
        }

        requestParamMap.put("userId", userId);
        requestParamMap.put("roomId", roomId);
        try {
            String throwToken = throwMoneyAroundService.saveThrowMoney(requestParamMap);
            resultMap.put("throwToken", throwToken);
            resultMap.put("resultCode", "1");
            resultMap.put("resultMsg", "뿌리기 성공");
        } catch (Exception e) {
            logger.debug("알 수 없는 에러. 서버 확인 필요");
            e.printStackTrace();
            resultMap.put("resultCode", "-99");
            resultMap.put("resultMsg", "알 수 없는 에러. 서버 확인 필요");
        } finally {
            logger.debug(resultMap.toString());
            return resultMap;
        }

    }

    /**
     * <pre>
     * 받기
     * </pre>
     *
     * URL
     * - http://localhost:8988/throwmoneyaround/get
     *
     * @author 정선우
     * @param
     * @return getAmt, resultCode, resultMsg
     */
    @PutMapping("/get")
    public HashMap<String, String> getThrowMoney(@RequestParam HashMap<String, String> requestParamMap,
                                                 @RequestHeader(value="x-user-id") String userId,
                                                 @RequestHeader(value="x-room-id") String roomId) {

        HashMap<String, String> resultMap = new HashMap<String, String>();

        logger.debug(requestParamMap.toString());
        logger.debug(userId + ", " + roomId);

        // validation-check
        if ("".equals(userId) || "".equals(roomId)) {
            logger.debug("userId 또는 roomId가 없습니다.");
            resultMap.put("resultCode", "-1");
            resultMap.put("resultMsg", "userId 또는 roomId가 없습니다.");
        }
        if (Util.isEmpty(requestParamMap,"throwToken")) {
            logger.debug("필수 값이 없습니다.");
            resultMap.put("resultCode", "-2");
            resultMap.put("resultMsg", "필수 값이 없습니다.");
        }
        if (requestParamMap.get("throwToken").length() != 3) {
            logger.debug("토큰 값의 길이가 정의되지 않은 길이입니다.");
            resultMap.put("resultCode", "-4");
            resultMap.put("resultMsg", "토큰 값의 길이가 정의되지 않은 길이입니다.");
        }

        requestParamMap.put("userId", userId);
        requestParamMap.put("roomId", roomId);

        try {
            String getAmt = throwMoneyAroundService.saveGetMoney(requestParamMap);
            resultMap.put("getAmt", getAmt);
            resultMap.put("resultCode", "2");
            resultMap.put("resultMsg", "받기 성공");
        } catch (BizException e) {

            logger.debug(e.getMessage());
            e.printStackTrace();

            String[] resultArr = e.getMessage().split(",");
            resultMap.put("resultCode", resultArr[0]);
            resultMap.put("resultMsg", resultArr[1]);
        } catch (Exception e) {
            logger.debug("알 수 없는 에러. 서버 확인 필요");
            e.printStackTrace();
            resultMap.put("resultCode", "-99");
            resultMap.put("resultMsg", "알 수 없는 에러. 서버 확인 필요");
        } finally {
            logger.debug(resultMap.toString());
            return resultMap;
        }

    }

    /**
     * <pre>
     * 조회
     * </pre>
     *
     * URL
     * - http://localhost:8988/throwmoneyaround/retrieve
     *
     * @author 정선우
     * @param
     * @return throwStatus, resultCode, resultMsg
     */
    @GetMapping("/retrieve")
    public HashMap retrieveThrowMoneyInfo(@RequestParam HashMap<String, String> requestParamMap,
                                                 @RequestHeader(value="x-user-id") String userId,
                                                 @RequestHeader(value="x-room-id") String roomId) {

        HashMap resultMap = new HashMap();

        logger.debug(requestParamMap.toString());
        logger.debug(userId + ", " + roomId);

        // validation-check
        if ("".equals(userId) || "".equals(roomId)) {
            logger.debug("userId 또는 roomId가 없습니다.");
            resultMap.put("resultCode", "-1");
            resultMap.put("resultMsg", "userId 또는 roomId가 없습니다.");
        }
        if (Util.isEmpty(requestParamMap,"throwToken")) {
            logger.debug("필수 값이 없습니다.");
            resultMap.put("resultCode", "-2");
            resultMap.put("resultMsg", "필수 값이 없습니다.");
        }
        if (requestParamMap.get("throwToken").length() != 3) {
            logger.debug("토큰 값의 길이가 정의되지 않은 길이입니다.");
            resultMap.put("resultCode", "-4");
            resultMap.put("resultMsg", "토큰 값의 길이가 정의되지 않은 길이입니다.");
        }

        requestParamMap.put("userId", userId);
        requestParamMap.put("roomId", roomId);

        try {
            HashMap throwStatus = throwMoneyAroundService.retrieveThrowMoneyInfo(requestParamMap);
            resultMap.put("throwStatus", throwStatus);
            resultMap.put("resultCode", "3");
            resultMap.put("resultMsg", "조회 성공");
        } catch (BizException e) {

            logger.debug(e.getMessage());
            e.printStackTrace();

            String[] resultArr = e.getMessage().split(",");
            resultMap.put("resultCode", resultArr[0]);
            resultMap.put("resultMsg", resultArr[1]);
        } catch (Exception e) {
            logger.debug("알 수 없는 에러. 서버 확인 필요");
            e.printStackTrace();
            resultMap.put("resultCode", "-99");
            resultMap.put("resultMsg", "알 수 없는 에러. 서버 확인 필요");
        } finally {
            logger.debug(resultMap.toString());
            return resultMap;
        }
    }

}
