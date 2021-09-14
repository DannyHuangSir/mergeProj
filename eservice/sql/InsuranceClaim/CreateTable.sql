--FOR ESERVICE DB
use ESERVICE;
go

ALTER TABLE ESERVICE.DBO.TRANS_POLICY ALTER COLUMN POLICY_NO VARCHAR(1000) NOT NULL;

ALTER TABLE ESERVICE.dbo.TRANS
ALTER column STATUS VARCHAR(20) not null;
go

--create TRANS_INSURANCE_CLAIM Sequence
create sequence TRANS_INSURANCE_CLAIM_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

--查詢/新建理賠案件
create table ESERVICE.dbo.TRANS_INSURANCE_CLAIM
(
  CLAIMS_SEQ_ID      FLOAT        not null,
  CASE_ID            VARCHAR(50),
  TRANS_NUM          VARCHAR(15),
  FILE_RECEIVED      VARCHAR(5),
  APPLYITEM          VARCHAR(200),
  POLICYNO           VARCHAR(1000),
  CUSTOMERNAME       VARCHAR(200),
  NAME               VARCHAR(200) not null,
  RELATION           VARCHAR(20),
  ID_NO              VARCHAR(20)  not null,
  BIRDATE            VARCHAR(8)   not null,
  TEL                VARCHAR(15),
  PHONE              VARCHAR(15)  not null,
  ZIP_CODE           VARCHAR(6),
  ADDRESS            VARCHAR(100),
  MAIL               VARCHAR(100),
  DISEASENAME        VARCHAR(200),
  DIAGNOSISDATE      VARCHAR(200),
  HOSPITAL           VARCHAR(200),
  OTHERCOMPANYINSURED        VARCHAR(200),
  OTHERINSUCOMMPANY  VARCHAR(200),
  OTHERINSUTYPE      VARCHAR(200),
  OTHERINSUAMOUNT    VARCHAR(200),
  OTHERINSUCLAIM     VARCHAR(200),
  PAYMENT_METHOD     VARCHAR(1) not null,
  BANK_CODE          VARCHAR(3) not null,
  BRANCH_CODE        VARCHAR(4),
  BANK_ACCOUNT       VARCHAR(14) not null,
  ACCOUNTNAME        VARCHAR(200),
  APPLICATION_DATE   VARCHAR(8)  not null default (convert(varchar, getdate(), 112)),
  APPLICATION_TIME   VARCHAR(4)  not null default (REPLACE(CONVERT(varchar(5), GETDATE(), 108), ':', '')),
  APPLICATION_ITEM   VARCHAR(1)  not null,
  JOB                VARCHAR(20),
  JOB_DESCR          VARCHAR(200),
  ACCIDENT_DATE      VARCHAR(8)  not null,
  ACCIDENT_TIME      VARCHAR(4),
  ACCIDENT_CAUSE     VARCHAR(1)  not null,
  ACCIDENT_LOCATION  VARCHAR(200),
  ACCIDENT_DESCR     VARCHAR(200),
  POLICE_STATION     VARCHAR(200),
  POLICE_NAME        VARCHAR(200),
  POLICE_PHONE       VARCHAR(20),
  POLICE_DATE        VARCHAR(8),
  POLICE_TIME        VARCHAR(4),
  CALLPOLICE         VARCHAR(10),
  FROM_COMPANY_ID    VARCHAR(100),
  TO_COMPANY_ID      VARCHAR(1000),
  SEND_ALLIANCE      VARCHAR(1) default 'N',
  CREATE_DATE        DATETIME default CURRENT_TIMESTAMP not null

);
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM
  add constraint TIC_PK primary key (CLAIMS_SEQ_ID);
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM
  add constraint DF_TIC_ID default (NEXT VALUE for TRANS_INSURANCE_CLAIM_SEQ) for CLAIMS_SEQ_ID;
GO


--查詢理賠案件關連表-FILEDATES
create sequence TRANS_INSURANCE_CLAIM_FD_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;


create table ESERVICE.dbo.TRANS_INSURANCE_CLAIM_FILEDATAS
(
  FD_ID           FLOAT        not null,
  CLAIMS_SEQ_ID   FLOAT        not null,
  TYPE            VARCHAR(50)   not null,
  FILE_NAME       VARCHAR(100) not null,
  PATH            VARCHAR(200),
  CREATE_DATE     DATETIME     default CURRENT_TIMESTAMP not null,
  RFE_ID          FLOAT,
  EZ_ACQUIRE_TASK_ID varchar(100)
);
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM_FILEDATAS
  add constraint TICFD_PK primary key (FD_ID);
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM_FILEDATAS
  add constraint DF_TICFD_ID default (NEXT VALUE for TRANS_INSURANCE_CLAIM_FD_SEQ) for FD_ID;
GO


--create TRANS_REF
create sequence TRANS_RFE_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create table ESERVICE.dbo.TRANS_RFE
(
  RFE_ID             FLOAT        not null,
  TRANS_NUM          VARCHAR(15),
  FD_ID              FLOAT,
  STATUS             VARCHAR(20),
  REQUEST_CONTENT    VARCHAR(1000),
  RESPONSE_CONTENT   VARCHAR(1000),
  REQUEST_DATE       DATETIME     default CURRENT_TIMESTAMP not null,
  RESPONSE_DATE      DATETIME     default CURRENT_TIMESTAMP not null
);
alter table ESERVICE.dbo.TRANS_RFE
  add constraint TRFE_PK primary key (RFE_ID);
alter table ESERVICE.dbo.TRANS_RFE
  add constraint DF_TRFE_ID default (NEXT VALUE for TRANS_RFE_SEQ) for RFE_ID;
GO

create sequence TRANS_STATUS_HISTORY_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create table ESERVICE.dbo.TRANS_STATUS_HISTORY
(
  TS_ID           FLOAT        not null,
  TRANS_NUM       VARCHAR(15),
  USER_IDENTITY   VARCHAR(200),
  CUSTOMERNAME    VARCHAR(200),
  REJECT_REASON   VARCHAR(200),
  STATUS          VARCHAR(20),
  CONTENT         VARCHAR(1000),
  REQUEST_DATE    DATETIME     default CURRENT_TIMESTAMP not null
)
alter table ESERVICE.dbo.TRANS_STATUS_HISTORY
  add constraint TSHTS_PK primary key (TS_ID);
alter table ESERVICE.dbo.TRANS_STATUS_HISTORY
  add constraint DF_TSHTS_ID default (NEXT VALUE for TRANS_STATUS_HISTORY_SEQ) for TS_ID;
GO



