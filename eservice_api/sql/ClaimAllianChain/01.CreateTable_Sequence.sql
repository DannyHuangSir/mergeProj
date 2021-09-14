--FOR ESERVICE DB
use ESERVICE;
go

--create NOTIFY_OF_NEWCASE Sequence
create sequence NOTIFY_OF_NEW_CASE_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

--被通知有新案件
create table ESERVICE.dbo.NOTIFY_OF_NEW_CASE
(
  SEQ_ID        FLOAT       not null,
  CASE_ID       VARCHAR(50) not null,
  TYPE          VARCHAR(1)  not null,
  CREATE_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  NC_STATUS     VARCHAR(5)  default '0' not null,
  STATUS_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  MSG           varchar(255)
);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE
  add constraint NONC_PK primary key (SEQ_ID,CASE_ID);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE
  add constraint DF_NONC_ID default (NEXT VALUE for NOTIFY_OF_NEW_CASE_SEQ) for SEQ_ID;
GO


--create INSURANCE_CLAIM Sequence
create sequence INSURANCE_CLAIM_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

--查詢/新建理賠案件
create table ESERVICE.dbo.INSURANCE_CLAIM
(
  CLAIMS_SEQ_ID      FLOAT        not null,
  NOTIFY_SEQ_ID      FLOAT,
  CASE_ID            VARCHAR(50),
  CODE               VARCHAR(10),
  MSG                VARCHAR(200),
  TRANS_NUM          VARCHAR(15),
  FILE_RECEIVED      VARCHAR(5),
  NAME               VARCHAR(200) not null,
  ID_NO              VARCHAR(20)  not null,
  BIRDATE            VARCHAR(8)   not null,
  PHONE              VARCHAR(15)  not null,
  ZIP_CODE           VARCHAR(6),
  ADDRESS            VARCHAR(500),
  MAIL               VARCHAR(100),
  PAYMENT_METHOD     VARCHAR(1) not null,
  BANK_CODE          VARCHAR(3) not null,
  BRANCH_CODE        VARCHAR(4),
  BANK_ACCOUNT       VARCHAR(14) not null,
  APPLICATION_DATE   VARCHAR(8)  not null default (convert(varchar, getdate(), 112)),
  APPLICATION_TIME   VARCHAR(4)  not null default (REPLACE(CONVERT(varchar(5), GETDATE(), 108), ':', '')),
  APPLICATION_ITEM   VARCHAR(1)  not null,
  JOB                VARCHAR(100),
  JOB_DESCR          VARCHAR(500),
  ACCIDENT_DATE      VARCHAR(8)  not null,
  ACCIDENT_TIME      VARCHAR(4),
  ACCIDENT_CAUSE     VARCHAR(1)  not null,
  ACCIDENT_LOCATION  VARCHAR(200),
  ACCIDENT_DESCR     VARCHAR(500),
  POLICE_STATION     VARCHAR(500),
  POLICE_NAME        VARCHAR(500),
  POLICE_PHONE       VARCHAR(20),
  POLICE_DATE        VARCHAR(8),
  POLICE_TIME        VARCHAR(4),
  FROM_COMPANY_ID    VARCHAR(100),
  TO_COMPANY_ID      VARCHAR(1000),
  STATUS             VARCHAR(10),
  SEND_ALLIANCE      VARCHAR(1) default 'N',
  CREATE_DATE        DATETIME default CURRENT_TIMESTAMP not null

);
alter table ESERVICE.dbo.INSURANCE_CLAIM
  add constraint IC_PK primary key (CLAIMS_SEQ_ID);
alter table ESERVICE.dbo.INSURANCE_CLAIM
  add constraint DF_IC_ID default (NEXT VALUE for INSURANCE_CLAIM_SEQ) for CLAIMS_SEQ_ID;
GO


--查詢理賠案件關連表-FILEDATES
create sequence INSURANCE_CLAIM_FD_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;


create table ESERVICE.dbo.INSURANCE_CLAIM_FILEDATAS
(
  FD_ID           FLOAT        not null,
  CLAIMS_SEQ_ID   FLOAT        not null,
  NOTIFY_SEQ_ID   FLOAT,
  MSG             VARCHAR(200),
  FILE_ID         VARCHAR(50) ,
  SIZE            VARCHAR(100) not null,
  TYPE            VARCHAR(50)   not null,
  FILE_NAME       VARCHAR(100) not null,
  FILE_STATUS     VARCHAR(20)  not null,
  PATH            VARCHAR(200),
  RFE_ID          FLOAT,
  UPDATE_MSG_DATE DATETIME,
  CREATE_DATE     DATETIME     default CURRENT_TIMESTAMP not null
);
alter table ESERVICE.dbo.INSURANCE_CLAIM_FILEDATAS
  add constraint ICFD_PK primary key (FD_ID);
alter table ESERVICE.dbo.INSURANCE_CLAIM_FILEDATAS
  add constraint DF_IDFD_ID default (NEXT VALUE for INSURANCE_CLAIM_FD_SEQ) for FD_ID;
GO

--create UNION_COURSE Sequence
create sequence UNION_COURSE_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;


--聯盟鏈歷程
create table ESERVICE.dbo.UNION_COURSE
(
  SEQ_ID          FLOAT       not null,
  CASE_ID         VARCHAR(50),
  TRANS_NUM       VARCHAR(15),
  TYPE            VARCHAR(10)  not null,
  NAME            VARCHAR(50)  not null,
  CREATE_DATE     DATETIME    default CURRENT_TIMESTAMP not null,
  COMPLETE_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  NC_STATUS       VARCHAR(5)  default '0' not null,
  MSG             VARCHAR(255)
);
alter table ESERVICE.dbo.UNION_COURSE
  add constraint UN_PK primary key (SEQ_ID);
alter table ESERVICE.dbo.UNION_COURSE
  add constraint DF_UN_ID default (NEXT VALUE for UNION_COURSE_SEQ) for SEQ_ID;
GO

--

  
