create sequence NOTIFY_OF_NEW_CASE_MEDICAL_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

--醫療被通知有新案件  
create table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_MEDICAL
(
  SEQ_ID        FLOAT       not null,
  CASE_ID       VARCHAR(50) not null,
  TYPE          VARCHAR(1)  not null,
  CREATE_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  NC_STATUS     VARCHAR(5)  default '0' not null,
  STATUS_DATE   DATETIME    default CURRENT_TIMESTAMP not null,
  MSG           varchar(255) NULL
);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_MEDICAL
  add constraint NONCM_PK primary key (SEQ_ID,CASE_ID);
alter table ESERVICE.dbo.NOTIFY_OF_NEW_CASE_MEDICAL
  add constraint DF_NONCMS_ID default (NEXT VALUE for NOTIFY_OF_NEW_CASE_MEDICAL_SEQ) for SEQ_ID;

--create TRANS_YIQITONG_CLAIM Sequence
create sequence MEDICAL_TREATMENT_CLAIM_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;


CREATE TABLE [ESERVICE].[dbo].[MEDICAL_TREATMENT_CLAIM](
	[CLAIMS_SEQ_ID] [float] NOT NULL,
	[CASE_ID] [varchar](50) NULL,
	[TRANS_NUM] [varchar](15) NULL,
	[FILE_RECEIVED] [varchar](5) NULL,
	[APPLYITEM] [varchar](200) NULL,
	[POLICYNO] [varchar](1000) NULL,
	[CUSTOMERNAME] [varchar](200) NULL,
	[NAME] [varchar](200) NOT NULL,
	[RELATION] [varchar](20) NULL,
	[ID_NO] [varchar](100) NULL,
	[BIRDATE] [varchar](100) NULL,
	[TEL] [varchar](100) NULL,
	[PHONE] [varchar](100) NULL,
	[ZIP_CODE] [varchar](6) NULL,
	[ADDRESS] [varchar](100) NULL,
	[MAIL] [varchar](100) NULL,
	[DISEASENAME] [varchar](200) NULL,
	[DIAGNOSISDATE] [varchar](200) NULL,
	[HOSPITAL] [varchar](200) NULL,
	[OTHERCOMPANYINSURED] [varchar](200) NULL,
	[OTHERINSUCOMMPANY] [varchar](200) NULL,
	[OTHERINSUTYPE] [varchar](200) NULL,
	[OTHERINSUAMOUNT] [varchar](200) NULL,
	[OTHERINSUCLAIM] [varchar](200) NULL,
	[PAYMENT_METHOD] [varchar](1) NOT NULL,
	[BANK_CODE] [varchar](3) NOT NULL,
	[BRANCH_CODE] [varchar](4) NULL,
	[BANK_ACCOUNT] [varchar](100) NULL,
	[ACCOUNTNAME] [varchar](200) NULL,
	[APPLICATION_DATE] [varchar](8) NOT NULL default (convert(varchar, getdate(), 112)),
	[APPLICATION_TIME] [varchar](4)  NOT NULL default (REPLACE(CONVERT(varchar(5), GETDATE(), 108), ':', '')),
	[APPLICATION_ITEM] [varchar](1) NOT NULL,
	[JOB] [varchar](20) NULL,
	[JOB_DESCR] [varchar](200) NULL,
	[ACCIDENT_DATE] [varchar](8) NOT NULL,
	[ACCIDENT_TIME] [varchar](4) NULL,
	[ACCIDENT_CAUSE] [varchar](1) NOT NULL,
	[ACCIDENT_LOCATION] [varchar](200) NULL,
	[ACCIDENT_DESCR] [varchar](200) NULL,
	[POLICE_STATION] [varchar](200) NULL,
	[POLICE_NAME] [varchar](200) NULL,
	[POLICE_PHONE] [varchar](100) NULL,
	[POLICE_DATE] [varchar](8) NULL,
	[POLICE_TIME] [varchar](4) NULL,
	[CALLPOLICE] [varchar](10) NULL,
	[FROM_COMPANY_ID] [varchar](100) NULL,
	[TO_COMPANY_ID] [varchar](1000) NULL,
	[SEND_ALLIANCE] [varchar](1) DEFAULT ('N')   NULL,
	[CREATE_DATE] [datetime]  default CURRENT_TIMESTAMP not null,
	[AUTHORIZATION_FILE_TYPE] [varchar](8) NULL,
	[AUTHORIZATION_START_DATE] [varchar](8) NULL,
	[AUTHORIZATION_END_DATE] [varchar](8) NULL,
	[FROM_HOSPITAL_ID] [varchar](100) NULL,
	[TO_HOSPITAL_ID] [varchar](1000) NULL,
	[ALLIANCE_STATUS] [varchar](100) NULL,
	[HP_UID] [varchar](20) NULL,
	[CPOA] [varchar](max) NULL,
	[FILE_STATUS] [varchar](5) NULL,
	[STATUS] [varchar](20) NULL,
);
alter table ESERVICE.dbo.MEDICAL_TREATMENT_CLAIM
  add constraint MTC_PK primary key (CLAIMS_SEQ_ID);
alter table ESERVICE.dbo.MEDICAL_TREATMENT_CLAIM
  add constraint DF_MTC_ID default (NEXT VALUE for MEDICAL_TREATMENT_CLAIM_SEQ) for CLAIMS_SEQ_ID;



--查詢醫起通案件關連表-FILEDATES
create sequence MEDICAL_TREATMENT_CLAIM_FD_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;


create table ESERVICE.dbo.MEDICAL_TREATMENT_CLAIM_FILEDATAS
(
	[FD_ID] [float] NOT NULL,
	[CLAIMS_SEQ_ID] [float] NOT NULL,
	[TYPE] [varchar](50) NOT NULL,
	[FILE_NAME] [varchar](100) NOT NULL,
	[PATH] [varchar](200) NULL,
	[CREATE_DATE] [datetime]  default CURRENT_TIMESTAMP not null,
	[RFE_ID] [float] NULL,
	[FILE_BASE64] [varchar](max) NULL,
	[FILE_ID] [varchar](50) NULL,
	[FILE_STATUS][varchar](1000) NULL,
);
alter table ESERVICE.dbo.MEDICAL_TREATMENT_CLAIM_FILEDATAS
  add constraint MTCFD_PK primary key (FD_ID);
alter table ESERVICE.dbo.MEDICAL_TREATMENT_CLAIM_FILEDATAS
  add constraint DF_MTCFD_ID default (NEXT VALUE for MEDICAL_TREATMENT_CLAIM_FD_SEQ) for FD_ID;
GO