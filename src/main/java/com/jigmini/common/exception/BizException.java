package com.jigmini.common.exception;

/*
 * <PRE>
 * 업무 Exception
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
public class BizException extends Exception {
    public BizException() {
        super();
    }
    public BizException(String msg) {
        super(msg);
    }
}
