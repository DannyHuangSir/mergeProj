--After Alter All TABLE

--INSURANCE_CLAIM
select
	CLAIMS_SEQ_ID
	,CASE_ID
	,CODE
	,MSG
	,ESERVICE.DBO.FN_ENBASE64(NAME) NAME
	,ESERVICE.DBO.FN_ENBASE64(ID_NO) ID_NO
	,ESERVICE.DBO.FN_ENBASE64(BIRDATE) BIRDATE
	,ESERVICE.DBO.FN_ENBASE64(PHONE) PHONE
	,ZIP_CODE
	,ESERVICE.DBO.FN_ENBASE64(ADDRESS) ADDRESS
	,ESERVICE.DBO.FN_ENBASE64(MAIL) MAIL
	,PAYMENT_METHOD
	,BANK_CODE
	,BRANCH_CODE
	,ESERVICE.DBO.FN_ENBASE64(BANK_ACCOUNT) BANK_ACCOUNT
	,APPLICATION_DATE
	,APPLICATION_TIME
	,APPLICATION_ITEM
	,JOB
	,JOB_DESCR
	,ACCIDENT_DATE
	,ACCIDENT_TIME
	,ACCIDENT_CAUSE
	,ACCIDENT_LOCATION
	,ACCIDENT_DESCR
	,POLICE_STATION
	,ESERVICE.DBO.FN_ENBASE64(POLICE_NAME) POLICE_NAME
	,ESERVICE.DBO.FN_ENBASE64(POLICE_PHONE) POLICE_PHONE
	,POLICE_DATE
	,POLICE_TIME
	,FROM_COMPANY_ID
	,TO_COMPANY_ID
	,CREATE_DATE
	,FILE_RECEIVED
	,STATUS
	,SEND_ALLIANCE
	,NOTIFY_SEQ_ID
	,TRANS_NUM
into ESERVICE.DBO.INSURANCE_CLAIM_ENBASE
from ESERVICE.DBO.INSURANCE_CLAIM
GO

--rename table
USE ESERVICE;
GO
EXEC sp_rename 'INSURANCE_CLAIM', 'INSURANCE_CLAIM_BAK';
EXEC sp_rename 'INSURANCE_CLAIM_ENBASE', 'INSURANCE_CLAIM';

--after rename table
alter table ESERVICE.dbo.INSURANCE_CLAIM
  add constraint IC_PK primary key (CLAIMS_SEQ_ID);
alter table ESERVICE.dbo.INSURANCE_CLAIM
  add constraint DF_IC_ID default (NEXT VALUE for INSURANCE_CLAIM_SEQ) for CLAIMS_SEQ_ID;
GO

--TRANS_INSURANCE_CLAIM
select
	CLAIMS_SEQ_ID,
	CASE_ID,
	TRANS_NUM,
	FILE_RECEIVED,
	ESERVICE.DBO.FN_ENBASE64(NAME) NAME,
	ESERVICE.DBO.FN_ENBASE64(ID_NO) ID_NO,
	ESERVICE.DBO.FN_ENBASE64(BIRDATE) BIRDATE,
	ESERVICE.DBO.FN_ENBASE64(PHONE) PHONE,
	ZIP_CODE,
	ESERVICE.DBO.FN_ENBASE64(ADDRESS) ADDRESS,
	ESERVICE.DBO.FN_ENBASE64(MAIL) MAIL,
	PAYMENT_METHOD,
	BANK_CODE,
	BRANCH_CODE,
	ESERVICE.DBO.FN_ENBASE64(BANK_ACCOUNT) BANK_ACCOUNT,
	ACCOUNTNAME,
	APPLICATION_DATE,
	APPLICATION_TIME,
	APPLICATION_ITEM,
	JOB,
	JOB_DESCR,
	ACCIDENT_DATE,
	ACCIDENT_TIME,
	ACCIDENT_CAUSE,
	ACCIDENT_LOCATION,
	ACCIDENT_DESCR,
	POLICE_STATION,
	ESERVICE.DBO.FN_ENBASE64(POLICE_NAME) POLICE_NAME,
	ESERVICE.DBO.FN_ENBASE64(POLICE_PHONE) POLICE_PHONE,
	POLICE_DATE,
	POLICE_TIME,
	FROM_COMPANY_ID,
	TO_COMPANY_ID,
	SEND_ALLIANCE,
	APPLYITEM,
	POLICYNO,
	ESERVICE.DBO.FN_ENBASE64(CUSTOMERNAME) CUSTOMERNAME,
	RELATION,
	ESERVICE.DBO.FN_ENBASE64(TEL) TEL,
	DISEASENAME,
	DIAGNOSISDATE,
	HOSPITAL,
	OTHERCOMPANYINSURED,
	OTHERINSUCOMMPANY,
	OTHERINSUTYPE,
	OTHERINSUAMOUNT,
	OTHERINSUCLAIM,
	CALLPOLICE,
	CREATE_DATE
into ESERVICE.DBO.TRANS_INSURANCE_CLAIM_ENBASE
from ESERVICE.DBO.TRANS_INSURANCE_CLAIM
GO

--rename table
USE ESERVICE;
GO
EXEC sp_rename 'TRANS_INSURANCE_CLAIM', 'TRANS_INSURANCE_CLAIM_BAK';
EXEC sp_rename 'TRANS_INSURANCE_CLAIM_ENBASE', 'TRANS_INSURANCE_CLAIM';

--after rename table
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM
  add constraint TIC_PK primary key (CLAIMS_SEQ_ID);
alter table ESERVICE.dbo.TRANS_INSURANCE_CLAIM
  add constraint DF_TIC_ID default (NEXT VALUE for TRANS_INSURANCE_CLAIM_SEQ) for CLAIMS_SEQ_ID;
GO

--DNS_ALLIANCE
select
	SEQ_ID
	,NOTIFY_SEQ_ID
	,TYPE
	,JOB_ID
	,CASE_ID
	,ESERVICE.DBO.FN_ENBASE64(IDNO) IDNO
	,INSNOM
	,ESERVICE.DBO.FN_ENBASE64(NAME) NAME
	,ESERVICE.DBO.FN_ENBASE64(BIRDATE) BIRDATE
	,SEX
	,CON
	,CONDATE
	,ADDDATE
	,STATUS
	,CREATE_DATE
	,UPDATE_STATUS_DATE
into ESERVICE.DBO.DNS_ALLIANCE_ENBASE
from ESERVICE.DBO.DNS_ALLIANCE
GO

--rename table
USE ESERVICE;
GO
EXEC sp_rename 'DNS_ALLIANCE', 'DNS_ALLIANCE_BAK';
EXEC sp_rename 'DNS_ALLIANCE_ENBASE', 'DNS_ALLIANCE';

--after rename table
alter table ESERVICE.dbo.DNS_ALLIANCE
  add constraint DA_PK primary key (SEQ_ID);
alter table ESERVICE.dbo.DNS_ALLIANCE
  add constraint DF_DA_ID default (NEXT VALUE for DNS_ALLIANCE_SEQ) for SEQ_ID;
GO

--TRANS_DNS
select
	SEQ_ID
	,NOTIFY_SEQ_ID
	,TRANS_NUM
	,TYPE
	,JOB_ID
	,CASE_ID
	,ESERVICE.DBO.FN_ENBASE64(IDNO) IDNO
	,INSNOM
	,ESERVICE.DBO.FN_ENBASE64(NAME) NAME
	,ESERVICE.DBO.FN_ENBASE64(BIRDATE) BIRDATE
	,SEX
	,CON
	,CONDATE
	,ADDDATE
	,SEND_ALLIANCE
	,CREATE_DATE
	,CODE
	,MSG
	,UPDATENUMBER
	,UPDATE_CODE_DATE
into ESERVICE.DBO.TRANS_DNS_ENBASE
from ESERVICE.DBO.TRANS_DNS
GO

--rename table
USE ESERVICE;
GO
EXEC sp_rename 'TRANS_DNS', 'TRANS_DNS_BAK';
EXEC sp_rename 'TRANS_DNS_ENBASE', 'TRANS_DNS';

--after rename table
alter table ESERVICE.dbo.TRANS_DNS
  add constraint TD_PK primary key (SEQ_ID);
alter table ESERVICE.dbo.TRANS_DNS
  add constraint DF_TD_ID default (NEXT VALUE for TRANS_DNS_SEQ) for SEQ_ID;
GO

/*
--Just insert from select--
--INSURANCE_CLAIM_BAK
insert into ESERVICE.dbo.INSURANCE_CLAIM_BAK
select * from ESERVICE.dbo.INSURANCE_CLAIM where 1=1;

--TRANS_INSURANCE_CLAIM
insert into ESERVICE.dbo.TRANS_INSURANCE_CLAIM_BAK
select * from ESERVICE.dbo.TRANS_INSURANCE_CLAIM where 1=1;

--DNS_ALLIANCE
insert into ESERVICE.dbo.DNS_ALLIANCE_BAK
select * from ESERVICE.dbo.DNS_ALLIANCE where 1=1;

--TRANS_DNS
insert into ESERVICE.dbo.TRANS_DNS_BAK
select * from ESERVICE.dbo.TRANS_DNS where 1=1;
--Just insert from select--
*/
