--create

----醫起通案件醫療檔案需求表-MEDICALINFO
use ESERVICE
go

create sequence TRANS_MEDICAL_TREATMENT_CLAIM_MI_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;
go

create table ESERVICE.dbo.TRANS_MEDICAL_TREATMENT_CLAIM_MEDICALINFO
(
	MI_ID         float NOT NULL,
	CLAIMS_SEQ_ID float NOT NULL,
	SENO          varchar(2) not null,
	HS_TIME       varchar(8) not null,
	HE_TIME       varchar(8) not null,
	OTYPE         varchar (50) not null,
	OTYPE_NAME    varchar(100) default '',
	DEPID         varchar (50) not null,
	DEP_NAME      varchar(100) default '',
	SUB_DEPID     varchar (50) not null,
	SUB_DEP_NAME  varchar(100) default '',
	DTYPE         varchar (200) not null,
	DTYPE_NAME    varchar(100) default '',
	CREATE_DATE   datetime default CURRENT_TIMESTAMP not null,
	RFE_ID        float  NULL,
	EZ_ACQUIRE_TASK_ID varchar (100) null,
	FILE_ID            varchar (50) null,
	FILE_NAME     varchar (100) not null,
	FILE_BASE64   varchar (max) not null default '',
	FILE_STATUS   varchar (1000) null,
	PATH            VARCHAR(200)
);
alter table ESERVICE.dbo.TRANS_MEDICAL_TREATMENT_CLAIM_MEDICALINFO
  add constraint TMTCM_PK primary key (MI_ID);
alter table ESERVICE.dbo.TRANS_MEDICAL_TREATMENT_CLAIM_MEDICALINFO
  add constraint DF_TMTCMMI_ID default (NEXT VALUE for TRANS_MEDICAL_TREATMENT_CLAIM_MI_SEQ) for MI_ID;
GO
