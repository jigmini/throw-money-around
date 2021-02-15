# 돈 뿌리기 기능 구현

## 1. 개발환경

 - IntelliJ
 - Java 11
 - Maven
 - Spring Boot 2.4.0
 - Mybatis
 - Oracle 11g
 
## 2. 프로젝트 구성

throw-money-around</br>
.........└ src</br>
...........└ main</br>
..............└ java</br>
..................└ com</br>
.....................└ jigmini</br>
.........................└ common           - [공통]</br>
.............................└ exception    - [예외]</br>
.............................└ util         - [유틸]</br>
.........................└ throwmoneyaround</br>
.............................└ dao          - [DAO]</br>
.............................└ service      - [Service]</br>
.................................└ impl</br>
.............................└ web          - [Controller]</br>
..............└ resources</br>
..................└ mapper                 - [mapper(SQL)]</br>
.....................└ throwmoneyaround</br>

## 3. 테이블 설명(주요 컬럼 설명)
(oracle_object_create.sql)

 - 사용자(TB_USER) : 사용자
    * USER_ID       NUMBER(10)   사용자ID PK1
	 

 - 대화방(TB_CHATROOM) : 대화방
    + ROOM_ID       VARCHAR2(10) 방ID   PK1
	+ JOIN_CNT      NUMBER(10)   참여수
	

 - 대화방사용자(TB_CHATROOM_USER) : 대화방에 참여하고 있는 사용자
    + ROOM_ID       VARCHAR2(10) 방ID   PK1
	+ USER_ID       NUMBER(10)   사용자ID PK2
	

 - 사용자계좌(TB_USER_ACCOUNT) : 사용자의 계좌
    + USER_ID       NUMBER(10)   사용자ID PK1
	+ BANK_CODE     VARCHAR2(2)  은행코드
	+ ACCOUNT_NO    VARCHAR2(20) 계좌번호
	

 - 돈뿌리기(TB_THROW_MONEY) : 돈뿌리기 정보
    + THROW_ID      NUMBER(10)   뿌리기ID PK1
	+ THROW_USER_ID NUMBER(10)   뿌리기사용자ID
	+ ROOM_ID       VARCHAR2(10) 방ID
	+ THROW_AMT     NUMBER(20)   뿌리기금액
	+ REMAIN_AMT    NUMBER(20)   남은금액
	+ THROW_NUM     NUMBER(10)   뿌리기수(분배수)
	+ THROW_TOKEN   VARCHAR2(3)  뿌리기토큰
	+ CNCL_YN       VARCHAR2(1)  취소여부
	+ REGI_DT       DATE         등록일시(뿌리기일시)
	

 - 돈받기(TB_GET_MONEY) : 돈뿌리기에 대한 분배 및 받기 정보
    + THROW_ID      NUMBER(10)   뿌리기ID PK1
	+ GET_SEQ       NUMBER(10)   받기순번  PK2
	+ GET_USER_ID   NUMBER(10)   받기사용자ID
	+ GET_DT        DATE         받기일시
	+ GET_AMT       NUMBER(20)   받기금액
	
## 4. API 설명

com/jigmini/throwmoneyaround/web/ThrowMoneyAroundController.java


request 헤더 (공통 IN)
 - x-user-id: 사용자ID
 - x-room-id: 방ID


1) 뿌리기 ("/throw")
 - IN: throwAmt(뿌리기금액), throwNum(뿌리기수(분배수))
 - OUT: { throwToken(뿌리기토큰), resultCode(결과코드), resultMsg(결과메시지) }


 - org.apache.commons.lang3.RandomStringUtils 사용하여 토큰 발행
 - 뿌린 돈의 분배는 균등 분할
 - 토큰발행, 돈 뿌리기, 돈 받기, 사용자계좌 출금 4단계 진행


2) 받기 ("/get")
- IN: throwToken(뿌리기토큰)
- OUT: { getAmt(받기금액), resultCode(결과코드), resultMsg(결과메시지) }


 - 돈뿌리기 수정, 돈받기 수정, 사용자계좌 입금 3단계 진행


3) 조회 ("/retrieve")
- IN: throwToken(뿌리기토큰)
- OUT: { throwStatus(뿌리기상태), resultCode(결과코드), resultMsg(결과메시지) }</br>
...............└ { getMoneyList(돈받기리스트), throwDt(뿌리기일시), throwAmt(뿌리기금액), getFnshAmt(받기완료금액) }</br>
...........................└ [ { getAmt(받기금액), getUserId(받기사용자ID) } ] 


 - 돈뿌리기 정보와 해당 돈뿌리기에 대한 받은 정보 조회
 
## 5. 기타 소스 설명

com/jigmini/common/exception/BizException.java
 - 업무로직에 대한 Exception

com/jigmini/common/util/CamelHashMap.java
 - Under Score에서 Camel Case로 변환해주는 Map
    + Mybatis가 resultMap에 대해서 Camel Case mapping을 지원하지 않아 만듬

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
    + tomcat port, logging, DB connection config, mybatis config 설정함
