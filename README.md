# 돈 뿌리기 기능 구현

## 1. 개발환경

 - IntelliJ
 - Java 11
 - Maven
 - Spring Boot 2.4.0
 - Mybatis
 - Oracle 11g
 
## 2. 프로젝트 구성

throw-money-around
         └src
		   └main
			  └java
			     └com
			  	   └jigmini
				       └common           - 공통
						   └exception    - 예외
                           └util         - 유틸
                       └throwmoneyaround
                           └dao          - DAO
                           └service      - Service
                               └impl     
                           └web          - Controller
              └resources
                 └mapper                 - mapper(SQL)
                   └throwmoneyaround

## 3. 테이블 설명
 - 사용자(TB_USER) : 사용자
 - 대화방(TB_CHATROOM) : 대화방
 - 대화방사용자(TB_CHATROOM_USER) : 대화방에 참여하고 있는 사용자
 - 사용자계좌(TB_USER_ACCOUNT) : 사용자의 계좌
 - 돈뿌리기(TB_THROW_MONEY) : 돈뿌리기 정보
 - 돈받기(TB_GET_MONEY) : 돈뿌리기에 대한 분배 및 받기 정보

## 4. API 설명

com/jigmini/throwmoneyaround/web/ThrowMoneyAroundController.java

1) 뿌리기 ("/throw")
 - org.apache.commons.lang3.RandomStringUtils 사용하여 토큰 발행
 - 뿌린 돈의 분배는 균등 분할
 - 토큰발행, 돈 뿌리기, 돈 받기, 사용자계좌 출금 4단계 진행
 
2) 받기 ("/get")
 - 돈뿌리기 수정, 돈받기 수정, 사용자계좌 입금 3단계 진행
 
3) 조회 ("/retrieve")
 - 돈뿌리기 정보와 해당 돈뿌리기에 대한 받은 정보 조회
 
## 5. 기타 소스 설명

com/jigmini/common/exception/BizException.java
 - 업무로직에 대한 Exception

com/jigmini/common/util/CamelHashMap.java
 - Under Score에서 Camel Case로 변환해주는 Map
    > Mybatis가 resultMap에 대해서 Camel Case mapping을 지원하지 않아 만듬

com/jigmini/common/util/Util.java
 - 유틸
 
com/jigmini/throwmoneyaround/dao/ThrowMoneyAroundDAO.java
 - Repository Layer

com/jigmini/throwmoneyaround/service/ThrowMoneyAroundService.java
com/jigmini/throwmoneyaround/service/impl/ThrowMoneyAroundServiceImpl.java
 - Service Layer
 
com/jigmini/throwmoneyaround/web/ThrowMoneyAroundController.java
 - Web Layer

com/jigmini/throwmoneyaround/ThrowMoneyAroundApplication.java
 - Spring Boot Application
 
mapper/throwmoneyaround/ThrowMoneyAroundSQL.xml
 - SQL이 있는 mapper 파일

application.properties
 - Spring Boot Application 참조하는 프로퍼티
    > tomcat port, logging, DB connection config, mybatis config 설정함
