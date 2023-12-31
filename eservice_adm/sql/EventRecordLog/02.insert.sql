--insert
--外部人員管理
insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,CREATE_DATE,CREATE_USER)
values
	((select max(PARAMETER_ID)+1 PARAMETER_ID from ESERVICE_ADM.dbo.PARAMETER),'eservice_adm','PARTNERUSER_ENTITYPAGELIST_001','外部人員管理事件記錄查詢','PARTNERUSER_ENTITYPAGELIST_001',
	 (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='EVENT_TYPE_JD' and SYSTEM_ID='eservice_adm'),'1',getdate(),'admin');
GO

insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,CREATE_DATE,CREATE_USER)
values
	((select max(PARAMETER_ID)+1 PARAMETER_ID from ESERVICE_ADM.dbo.PARAMETER),'eservice_adm','PARTNERUSER_INSERT_002','外部人員管理-新增','PARTNERUSER_INSERT_002',
	 (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='EVENT_TYPE_JD' and SYSTEM_ID='eservice_adm'),'1',getdate(),'admin');
GO

insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,CREATE_DATE,CREATE_USER)
values
	((select max(PARAMETER_ID)+1 PARAMETER_ID from ESERVICE_ADM.dbo.PARAMETER),'eservice_adm','PARTNERUSER_DELETE_003','外部人員管理-刪除','PARTNERUSER_DELETE_003',
	 (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='EVENT_TYPE_JD' and SYSTEM_ID='eservice_adm'),'1',getdate(),'admin');
GO

insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,CREATE_DATE,CREATE_USER)
values
	((select max(PARAMETER_ID)+1 PARAMETER_ID from ESERVICE_ADM.dbo.PARAMETER),'eservice_adm','PARTNERUSER_UNLOCK_004','外部人員管理-解鎖','PARTNERUSER_UNLOCK_004',
	 (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='EVENT_TYPE_JD' and SYSTEM_ID='eservice_adm'),'1',getdate(),'admin');
GO

insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,CREATE_DATE,CREATE_USER)
values
	((select max(PARAMETER_ID)+1 PARAMETER_ID from ESERVICE_ADM.dbo.PARAMETER),'eservice_adm','PARTNERUSER_UPDATEONLINECHANGE_005','外部人員管理-更新線上申請使用','PARTNERUSER_UPDATEONLINECHANGE_005',
	 (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='EVENT_TYPE_JD' and SYSTEM_ID='eservice_adm'),'1',getdate(),'admin');
GO
