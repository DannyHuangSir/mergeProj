--FOR ESERVICE DB
use ESERVICE;
go
 
--聯絡資料變更暨保全聯盟鏈
create sequence CONTACT_INFO_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create table ESERVICE.dbo.CONTACT_INFO(
SEQ_ID        FLOAT         NOT NULL,
CASE_ID       VARCHAR(50)   NULL,
BATCH_ID      FLOAT         NULL,
CODE          VARCHAR(10)   NULL,
MSG           VARCHAR(200)  NULL,
TRANS_NUM     varchar(20)   null,
CIDNO         VARCHAR(20)   NULL,
CBIRDATE      VARCHAR(20)    NULL,
CNAME         VARCHAR(200)  NULL,
CRNAME        VARCHAR(200)  NULL,
CENAME        VARCHAR(200)  NULL,
CPHONE        VARCHAR(15)   NULL,
CMAIL         VARCHAR(100)  NULL,
CADDRESS      VARCHAR(1000)  NULL,
NAME          VARCHAR(200)  NULL,
RNAME         VARCHAR(200)  NULL,
ENAME         VARCHAR(200)  NULL,
IDNO          VARCHAR(20)   NULL,
RZIPCODE      VARCHAR(20)    NULL,
RADDRESS      VARCHAR(1000)  NULL,
MZIPCODE      VARCHAR(20)    NULL,
MADDRESS      VARCHAR(1000)  NULL,
PHONE         VARCHAR(20)   NULL,
HOMETEL1      VARCHAR(20)    NULL,
HOMETELCODE1  VARCHAR(20)    NULL,
HOMETELEXT1   VARCHAR(20)   NULL,
HOMETEL2      VARCHAR(20)    NULL,
HOMETELCODE2  VARCHAR(20)    NULL,
HOMETELEXT2   VARCHAR(20)   NULL,
MAIL          VARCHAR(100)  NULL,
FROM_COMPANY  VARCHAR(100)  NULL,
TO_COMPANY    VARCHAR(1000) NULL,
AGREEMENT     VARCHAR(1024) NULL,
LOGDATETIME   VARCHAR(20)   NULL,
LOGDESC1      VARCHAR(50)   NULL,
SOURCE        VARCHAR(1)    NULL,
STATUS        VARCHAR(20)   NULL,
SEND_ALLIANCE VARCHAR(1)    NULL,
NOTIFY_SEQ_ID FLOAT         NULL,
CREATE_DATE   DATETIME     default CURRENT_TIMESTAMP not null
);            
alter table ESERVICE.dbo.CONTACT_INFO
  add constraint CIO_PK primary key (SEQ_ID);
alter table ESERVICE.dbo.CONTACT_INFO
  add constraint DF_CIO_ID default (NEXT VALUE for CONTACT_INFO_SEQ) for SEQ_ID;
GO

/*
-- ESERVICE.DBO.CONTACT_INFO    追加  TRANS_NUM字段
alter table ESERVICE.DBO.CONTACT_INFO  add  TRANS_NUM varchar(20) null;
*/

-- ESERVICE.DBO.CONTACT_INFO 加密字段长度的修改
alter table  ESERVICE.DBO.CONTACT_INFO alter column CIDNO varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column CBIRDATE varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column CPHONE varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column IDNO varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column PHONE varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column HOMETEL1 varchar(100)
alter table  ESERVICE.DBO.CONTACT_INFO alter column HOMETEL2 varchar(100)


create sequence CONTACT_INFO_FILEDATAS_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create table ESERVICE.dbo.CONTACT_INFO_FILEDATAS
(
  FD_ID           FLOAT        not null,
  CONTACT_SEQ_ID  FLOAT        not null,
  NOTIFY_SEQ_ID   FLOAT,
  MSG             VARCHAR(200),
  FILE_ID         VARCHAR(50) ,
  SIZE            VARCHAR(100) not null,
  TYPE            VARCHAR(50)   not null,
  FILE_NAME       VARCHAR(100) not null,
  FILE_STATUS     VARCHAR(20)  not null,
  PATH            VARCHAR(200),
  FILE_BASE64     VARCHAR(MAX),
  RFE_ID          FLOAT,
  UPDATE_MSG_DATE DATETIME,
  CREATE_DATE     DATETIME     default CURRENT_TIMESTAMP not null
);
alter table ESERVICE.dbo.CONTACT_INFO_FILEDATAS
  add constraint CI_FD_PK primary key (FD_ID);
alter table ESERVICE.dbo.CONTACT_INFO_FILEDATAS
  add constraint CI_FD_ID default (NEXT VALUE for CONTACT_INFO_FILEDATAS_SEQ) for FD_ID;
GO

/*
--被通知有新案件
--create NOTIFY_OF_NEWCASE Sequence
create sequence NOTIFY_OF_NEW_CASE_CHANGE_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_CHANGE
(
  SEQ_ID        FLOAT       not null,
  CASE_ID       VARCHAR(50) not null,
  TYPE          VARCHAR(1)  not null,
  CREATE_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  NC_STATUS     VARCHAR(5)  default '0' not null,
  STATUS_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  MSG           varchar(255)
);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_CHANGE
  add constraint NONCC_PK primary key (SEQ_ID,CASE_ID);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_CHANGE
  add constraint DF_NONCC_ID default (NEXT VALUE for NOTIFY_OF_NEW_CASE_CHANGE_SEQ) for SEQ_ID;
GO
*/

create sequence CONTACT_INFO_BATCH_ID_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

create sequence TRANS_CONTACT_INFO_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 20000
increment by 1
no cache;

---增加TRANS_CONTACT_FILEDATAS Table接收CONTACT_INFO_FILEDATAS資料，
---以便往後ＵＩ上有顯示聯盟變更保單資料縮圖
---聯盟檔案要傳核心時亦要使用
create sequence TRANS_CONTACT_INFO_FILEDATAS_SEQ
    minvalue 1
    maxvalue 9223372036854775807
    start with 1
    increment by 1
no cache;

create table ESERVICE.dbo.TRANS_CONTACT_INFO_FILEDATAS
(
  FD_ID           FLOAT        not null,
  CONTACT_SEQ_ID  FLOAT        not null,
  NOTIFY_SEQ_ID   FLOAT,
  MSG             VARCHAR(200),
  FILE_ID         VARCHAR(50) ,
  SIZE            VARCHAR(100) not null,
  TYPE            VARCHAR(50)   not null,
  FILE_NAME       VARCHAR(100) not null,
  FILE_STATUS     VARCHAR(20)  not null,
  PATH            VARCHAR(200),
  FILE_BASE64     VARCHAR(MAX),
  RFE_ID          FLOAT,
  UPDATE_MSG_DATE DATETIME,
  CREATE_DATE     DATETIME     default CURRENT_TIMESTAMP not null
);
alter table ESERVICE.dbo.TRANS_CONTACT_INFO_FILEDATAS
  add constraint TR_CI_FD_PK primary key (FD_ID);
alter table ESERVICE.dbo.TRANS_CONTACT_INFO_FILEDATAS
  add constraint TR_CI_FD_ID default (NEXT VALUE for TRANS_CONTACT_INFO_FILEDATAS_SEQ) for FD_ID;
GO

--聯盟件應捉取name欄位來顯示（依spec, name欄位是更新值）
ALTER TABLE ESERVICE.DBO.TRANS_CONTACT_INFO_DTL ADD NAME VARCHAR(200) NULL;
ALTER TABLE ESERVICE.DBO.TRANS_CONTACT_INFO_DTL ADD IDNO VARCHAR(20) NULL;

ALTER TABLE ESERVICE.DBO.TRANS_CONTACT_INFO_OLD ADD NAME VARCHAR(200) NULL;
ALTER TABLE ESERVICE.DBO.TRANS_CONTACT_INFO_OLD ADD IDNO VARCHAR(20) NULL;

--alter column TRANS_CONTACT_INFO_DTL/TRANS_CONTACT_INFO_OLD
alter table  ESERVICE.DBO.TRANS_CONTACT_INFO_DTL alter column IDNO varchar(100)
alter table  ESERVICE.DBO.TRANS_CONTACT_INFO_OLD alter column IDNO varchar(100)
-- 邮件添加长度
alter table  ESERVICE.DBO.TRANS_CONTACT_INFO_DTL alter column EMAIL varchar(100);
alter table  ESERVICE.DBO.TRANS_CONTACT_INFO_OLD alter column EMAIL varchar(100);

-- 加密ESERVICE.DBO.INSURANCE_CLAIM  字段，长度曾长
alter table  ESERVICE.DBO.INSURANCE_CLAIM  alter column ID_NO varchar(100)
alter table  ESERVICE.DBO.INSURANCE_CLAIM  alter column BIRDATE varchar(100)
alter table  ESERVICE.DBO.INSURANCE_CLAIM  alter column PHONE varchar(100)
alter table  ESERVICE.DBO.INSURANCE_CLAIM  alter column BANK_ACCOUNT varchar(100)
alter table  ESERVICE.DBO.INSURANCE_CLAIM  alter column POLICE_PHONE varchar(100)

-- 加密ESERVICE.DBO.TRANS_INSURANCE_CLAIM  字段，长度曾长
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column ID_NO varchar(100);
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column BIRDATE varchar(100);
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column TEL varchar(100);
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column PHONE varchar(100);
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column BANK_ACCOUNT varchar(100);
alter table  ESERVICE.DBO.TRANS_INSURANCE_CLAIM  alter column POLICE_PHONE varchar(100);

--DNS因應加密,增加長度
alter table  ESERVICE.DBO.DNS_ALLIANCE alter column IDNO varchar(100);
alter table  ESERVICE.DBO.DNS_ALLIANCE alter column NAME varchar(100);
alter table  ESERVICE.DBO.DNS_ALLIANCE alter column BIRDATE varchar(50);

alter table  ESERVICE.DBO.TRANS_DNS alter column IDNO varchar(100);
alter table  ESERVICE.DBO.TRANS_DNS alter column NAME varchar(100);
alter table  ESERVICE.DBO.TRANS_DNS alter column BIRDATE varchar(50);


-- TRANS_INSURANCE_CLAIM_FILEDATAS  追加  FILE_ID
alter table ESERVICE.DBO.TRANS_INSURANCE_CLAIM_FILEDATAS   add   FILE_ID    varchar(50)  default null


