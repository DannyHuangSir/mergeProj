--FOR ESERVICE_ADM DB
use ESERVICE_ADM;
go

--create BLACK_LIST Sequence
create sequence BLACK_LIST_SEQ
minvalue 1
maxvalue 9223372036854775807
start with 1
increment by 1
no cache;

--黑名單
create table ESERVICE_ADM.dbo.BLACK_LIST
(
  BL_ID        FLOAT       not null,
  TRANS_NUM    VARCHAR(15),
  TRANS_TYPE   VARCHAR(20),
  ID_NO        VARCHAR(20),
  CREATE_DATE  DATETIME    default CURRENT_TIMESTAMP not null
);
alter table ESERVICE_ADM.dbo.BLACK_LIST
  add constraint BL_PK primary key (BL_ID);
alter table ESERVICE_ADM.dbo.BLACK_LIST
  add constraint DF_BL_ID default (NEXT VALUE for BLACK_LIST_SEQ) for BL_ID;
GO
