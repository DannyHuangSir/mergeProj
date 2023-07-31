USE ESERVICE_ADM
alter table ESERVICE_ADM.dbo.ROLE add DIV_ROLE_ID  [nvarchar](50) NULL

create sequence BUSINESS_EVENT_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 88803
increment by 1
no cache;

create sequence SYSTEM_EVENT_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 88803
increment by 1
no cache;
