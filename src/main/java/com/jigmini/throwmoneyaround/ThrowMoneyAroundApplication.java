package com.jigmini.throwmoneyaround;

import com.jigmini.throwmoneyaround.web.ThrowMoneyAroundController;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * <PRE>
 * Application
 * </PRE>
 * @Date: 2021.02.14
 * @Author: 정선우
 */
@SpringBootApplication
public class ThrowMoneyAroundApplication {

	private static final Logger logger = LoggerFactory.getLogger(ThrowMoneyAroundController.class);

	public static void main(String[] args) {
		logger.debug("ThrowMoneyAroundApplication Start");
		SpringApplication.run(ThrowMoneyAroundApplication.class, args);
	}

}
