INSERT ESERVICE_ADM.DBO.PARAMETER_CATEGORY(
	PARAMETER_CATEGORY_ID,
	SYSTEM_ID,
	CATEGORY_CODE,
	CATEGORY_NAME,
	REMARK,
	STATUS,
	CREATE_DATE,
	CREATE_USER,
	UPDATE_DATE,
	UPDATE_USER)
VALUES (
   (select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.DBO.PARAMETER_CATEGORY),
   'eservice_api',
   'SYS_MEDICAL_API_URL',
   N'醫療資源定位地址-系統屬性',
   NULL,
   '1.0000',
   GETDATE(),
   'admin',
   NULL,
   NULL
)
INSERT ESERVICE_ADM.DBO.PARAMETER_CATEGORY(
	PARAMETER_CATEGORY_ID,
	SYSTEM_ID,
	CATEGORY_CODE,
	CATEGORY_NAME,
	REMARK,
	STATUS,
	CREATE_DATE,
	CREATE_USER,
	UPDATE_DATE,
	UPDATE_USER)
VALUES (
   (select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.DBO.PARAMETER_CATEGORY),
   'eservice_api',
   'SYS_MEDICAL_SCH',
   N'醫療日程安排-系統屬性',
   NULL,
   '1.0000',
   GETDATE(),
   'admin',
   NULL,
   NULL
)
-----Insert PARAMETER for medicalAlliance  Schedule
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.api.medical.disable', N'cron.api.medical.disable', N'Y',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.saveToMedicalTrans.expression', N'cron.saveToMedicalTrans.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go


INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.saveTransToMedical.expression', N'cron.saveTransToMedical.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go







INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical401.expression', N'cron.medical401.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical402.expression', N'cron.medical402.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical403.expression', N'cron.medical403.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical404.expression', N'cron.medical404.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical405.expression', N'cron.medical405.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical406.expression', N'cron.medical406.expression',  N'0 */5 * * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical407.expression', N'cron.medical407.expression',  N'0 * */8 * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'cron.medical408.expression', N'cron.medical408.expression',  N'0 * */8 * * ?',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_SCH'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api401.url', N'medicalAlliance.api401.url', N'http://10.210.1.181/lia-api/api/ext/ihs401i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go


INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api402.url', N'medicalAlliance.api402.url', N'http://10.210.1.181/lia-api/api/ext/ihs402i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api403.url', N'medicalAlliance.api403.url', N'http://10.210.1.181/lia-api/api/ext/ihs403i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api404.url', N'medicalAlliance.api404.url', N'http://10.210.1.181/lia-api/api/ext/ihs404i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api405.url', N'medicalAlliance.api405.url', N'http://10.210.1.181/lia-api/api/ext/ihs405i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api406.url', N'medicalAlliance.api406.url', N'http://10.210.1.181/lia-api/api/ext/ihs406i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api407.url', N'medicalAlliance.api407.url', N'http://10.210.1.181/lia-api/api/ext/ihs407i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'medicalAlliance.api408.url', N'medicalAlliance.api408.url', N'http://10.210.1.181/lia-api/api/ext/ihs408i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_MEDICAL_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go


--- API联盟查詢案件資訊郵件模板 进行发送邮件参数
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_api', N'MSG_PARAM_PROGRAM_STATUS', N'通信模板參數-案件狀態代碼'
, N'STATUS',
(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MessagingParameter'), NULL, NULL, 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_api', N'MSG_PARAM_PROGRAM_STATUS_MESSAGE', N'通信模板參數-案件狀態代碼描述'
, N'STATUS_MESSAGE',
(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MessagingParameter'), NULL, NULL, 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_api', N'MSG_PARAM_PROGRAM_HOSPITAL_CODE', N'通信模板參數-醫院代號'
, N'HOSPITAL_CODE',
(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MessagingParameter'), NULL, NULL, 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_api', N'MSG_PARAM_PROGRAM_INSURED_ID', N'通信模板參數-被保人身分證字號'
, N'INSURED_ID',
(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MessagingParameter'), NULL, NULL, 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go


-- API联盟API-403查詢案件資訊郵件模板
INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice_api',N'MEDICAL_MAIL_034',N'醫療保單查詢案件資訊取得案件通知MAIL模版'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'醫療保單查詢案件資訊取得案件通知'
,N'代碼:API-${CODE}</br>訊息:已從醫院端取得授權資料，請與保險公司的核心系統確認，是否是保戶</br>案件狀態代碼及中文:${STATUS}-${STATUS_MESSAGE}</br>與醫院對接序號:${HOSPITAL_CODE}</br>被保人身分證字號:${INSURED_ID}', getdate(),N'admin',getdate(),N'admin')
GO

---MAIL模版参数[MESSAGING_PARAMETER]
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
           ([MESSAGING_TEMPLATE_ID]
           ,[PARAMETER_ID])
     VALUES
           ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice_api' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_034')
           ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_api' and PARAMETER_CODE='MSG_PARAM_CODE')
		   )
GO

---MAIL模版参数[MESSAGING_PARAMETER]
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
           ([MESSAGING_TEMPLATE_ID]
           ,[PARAMETER_ID])
     VALUES
           ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice_api' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_034')
           ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_api' and PARAMETER_CODE='MSG_PARAM_PROGRAM_STATUS')
		   )
GO
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
           ([MESSAGING_TEMPLATE_ID]
           ,[PARAMETER_ID])
     VALUES
           ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice_api' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_034')
           ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_api' and PARAMETER_CODE='MSG_PARAM_PROGRAM_STATUS_MESSAGE')
		   )
GO
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
           ([MESSAGING_TEMPLATE_ID]
           ,[PARAMETER_ID])
     VALUES
           ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice_api' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_034')
           ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_api' and PARAMETER_CODE='MSG_PARAM_PROGRAM_HOSPITAL_CODE')
		   )
GO
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
           ([MESSAGING_TEMPLATE_ID]
           ,[PARAMETER_ID])
     VALUES
           ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice_api' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_034')
           ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_api' and PARAMETER_CODE='MSG_PARAM_PROGRAM_INSURED_ID')
		   )
GO
--PARAMETER_CATEGORY
insert into ESERVICE_ADM.dbo.PARAMETER_CATEGORY
	(PARAMETER_CATEGORY_ID,SYSTEM_ID,CATEGORY_CODE,CATEGORY_NAME,REMARK,STATUS,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER)
values
	(
		(select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY),
		'eservice_adm',
		'MEDICAL_ALLIANCE_MAIL',
		'醫療首家轉收件',
		'醫療授權資料需通知作業人員',
		'1',
		getdate(),
		'admin',
		getdate(),
		'admin'
	);
go

insert into ESERVICE_ADM.dbo.PARAMETER
	(PARAMETER_ID,SYSTEM_ID,PARAMETER_CODE,PARAMETER_NAME,PARAMETER_VALUE,PARAMETER_CATEGORY_ID,STATUS,ENCRYPT_TYPE,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER)
values
	(
		(select max(PARAMETER_ID)+1 from ESERVICE_ADM.dbo.PARAMETER),
		'eservice_adm',
		'MEDICAL_ALLIANCE_MAIL_TWFHCLIFE_ADM',
		'醫療作業通知人員',
		'203216@twfhclife.com.tw;190064@twfhclife.com.tw;203352@twfhclife.com.tw;203990@twfhclife.com.tw;202712@twfhclife.com.tw;202859@twfhclife.com.tw;831227@twfhclife.com.tw',
		(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ALLIANCE_MAIL'),
		'1',
		'N',
		getdate(),
		'admin',
		getdate(),
		'admin'
	);
go

-- 內部人員通知	首家件通知	理賠醫首家理賠申請通知
INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice',N'MEDICAL_MAIL_035',N'【首家理賠醫起通】MAIL模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'理賠醫起通首家理賠申請通知MAIL模板',N'<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%" class="m_-1402362586549430844background"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0" bgcolor="#F0F0F0"><br><table border="0" cellpadding="0" cellspacing="0" align="center" bgcolor="#FFFFFF" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844container"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:24px;font-weight:bold;line-height:130%;padding-top:0px;padding-bottom:15px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844header"><a href="https://www.twfhclife.com.tw/" target="_blank"><img src="https://elife.twfhclife.com.tw/eservice/img/my-logo.png" class="CToWUd"></a></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-bottom:3px;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:18px;font-weight:600;line-height:150%;padding-top:5px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844subheader">理賠醫起通首家理賠申請通知</td></tr><tr><td align="left" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:16px;font-weight:400;line-height:160%;padding-top:25px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844paragraph">收到首家案件 1 筆，申請時間：${LoginTime}，申請序號：${TransNum}，資料已寫入聯盟鏈醫起通線上申請。</td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;padding-top:5px" class="m_-1402362586549430844line"><hr color="#E0E0E0" align="center" width="100%" size="1" noshade="" style="margin:0;padding:0"></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%" class="m_-1402362586549430844list-item"><table align="left" border="0" cellspacing="0" cellpadding="0" style="width:inherit;margin:0;padding:0;border-collapse:collapse;border-spacing:0"><tbody><tr><td align="left" valign="top" style="font-size:16px;padding-top:10px;padding-bottom:10px;padding-left:0px;color:#000000;font-family:Microsoft JhengHei"></td></tr></tbody></table></td></tr></tbody></table><table border="0" cellpadding="0" cellspacing="0" align="center" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844wrapper"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%;font-size:18px;font-weight:400;line-height:150%;padding-top:20px;padding-bottom:20px;color:#999999;font-family:Microsoft JhengHei" class="m_-1402362586549430844footer">(本郵件是由系統自動寄送，請勿以此信件回覆。)</td></tr></tbody></table></td></tr></tbody></table>', getdate(),N'admin',getdate(),N'admin')
GO

INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_035')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_LoginTime')
   )
GO

INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_035')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransNum')
   )
GO


-- 客戶通知	首家件通知  臺銀人壽保單 理賠醫起通申請通知
INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice',N'MEDICAL_MAIL_036',N'【理賠醫申請通知】保戶首家件MAIL模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'通知保戶首家件通知',N'<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%" class="m_-1402362586549430844background"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0" bgcolor="#F0F0F0"><br><table border="0" cellpadding="0" cellspacing="0" align="center" bgcolor="#FFFFFF" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844container"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:24px;font-weight:bold;line-height:130%;padding-top:0px;padding-bottom:15px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844header"><a href="https://www.twfhclife.com.tw/" target="_blank"><img src="https://elife.twfhclife.com.tw/eservice/img/my-logo.png" class="CToWUd"></a></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-bottom:3px;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:18px;font-weight:600;line-height:150%;padding-top:5px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844subheader">臺銀人壽理賠醫起通申請通知</td></tr><tr><td align="left" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:16px;font-weight:400;line-height:160%;padding-top:25px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844paragraph">親愛的客戶您好：<br> 感謝您使用本公司保單網路服務，本公司已收到您的理賠醫起通申請案，申請時間：${LoginTime}，申請序號：${TransNum}，案件狀態：${TransStatus}，將儘速為您辦理。如需其他服務請撥打本公司客服專線或洽詢各地分公司辦理。</td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;padding-top:5px" class="m_-1402362586549430844line"><hr color="#E0E0E0" align="center" width="100%" size="1" noshade="" style="margin:0;padding:0"></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%" class="m_-1402362586549430844list-item"><table align="left" border="0" cellspacing="0" cellpadding="0" style="width:inherit;margin:0;padding:0;border-collapse:collapse;border-spacing:0"><tbody><tr><td align="left" valign="top" style="font-size:16px;padding-top:10px;padding-bottom:10px;padding-left:0px;color:#000000;font-family:Microsoft JhengHei">客服專線：0800-011-966</td></tr></tbody></table></td></tr></tbody></table><table border="0" cellpadding="0" cellspacing="0" align="center" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844wrapper"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%;font-size:18px;font-weight:400;line-height:150%;padding-top:20px;padding-bottom:20px;color:#999999;font-family:Microsoft JhengHei" class="m_-1402362586549430844footer">(本郵件是由系統自動寄送，請勿以此信件回覆。)</td></tr></tbody></table></td></tr></tbody></table>', getdate(),N'admin',getdate(),N'admin')
GO

----客戶通知	首家件通知  臺銀人壽保 理賠醫起通 申請通知
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_036')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_LoginTime')
   )
GO

INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_036')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransNum')
   )
GO

INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_MAIL_036')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransStatus')
   )
GO



-- 客戶通知	首家件通知  臺銀人壽保單 理賠醫起通申請SMS通知
INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice',N'MEDICAL_SMS-037',N'【理賠醫起通】首家件SMS模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'線上理賠醫起通',N'親愛的客戶您好：您的臺銀人壽保單網路服務線上理賠申請序號：${TransNum}，申請結果為 ${TransStatus}，備註說明：${TransRemark}。', getdate(),N'admin',getdate(),N'admin')
GO


INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_SMS-037')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransNum')
   )
GO
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_SMS-037')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransStatus')
   )
GO
INSERT INTO  ESERVICE_ADM.[dbo].[MESSAGING_PARAMETER]
   ([MESSAGING_TEMPLATE_ID]
   ,[PARAMETER_ID])
VALUES
   ((select MESSAGING_TEMPLATE_ID  FROM   ESERVICE_ADM.dbo.[MESSAGING_TEMPLATE]  WHERE SYSTEM_ID='eservice' and [MESSAGING_TEMPLATE_CODE]='MEDICAL_SMS-037')
   ,(select PARAMETER_ID  FROM   ESERVICE_ADM.dbo.PARAMETER  WHERE SYSTEM_ID='eservice_adm' and PARAMETER_CODE='MSG_PARAM_TransRemark')
   )
GO

--- 內部人員通知	轉收件通知	醫起通轉收申請通知
INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice',N'ELIFE_MAIL_036',N'【醫起通轉收申請】MAIL模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'醫起通轉收申請通知MAIL模板',N'<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%" class="m_-1402362586549430844background"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0" bgcolor="#F0F0F0"><br><table border="0" cellpadding="0" cellspacing="0" align="center" bgcolor="#FFFFFF" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844container"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:24px;font-weight:bold;line-height:130%;padding-top:0px;padding-bottom:15px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844header"><a href="https://www.twfhclife.com.tw/" target="_blank"><img src="https://elife.twfhclife.com.tw/eservice/img/my-logo.png" class="CToWUd"></a></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-bottom:3px;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:18px;font-weight:600;line-height:150%;padding-top:5px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844subheader">理賠醫起通轉收申請通知</td></tr><tr><td align="left" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:16px;font-weight:400;line-height:160%;padding-top:25px;color:#000000;font-family:Microsoft JhengHei" class="m_-1402362586549430844paragraph">收到轉收案件 1 筆，申請時間：${LoginTime}，申請序號：${TransNum}，資料已寫入理賠醫起通線上申請。</td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;padding-top:5px" class="m_-1402362586549430844line"><hr color="#E0E0E0" align="center" width="100%" size="1" noshade="" style="margin:0;padding:0"></td></tr><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%" class="m_-1402362586549430844list-item"><table align="left" border="0" cellspacing="0" cellpadding="0" style="width:inherit;margin:0;padding:0;border-collapse:collapse;border-spacing:0"><tbody><tr><td align="left" valign="top" style="font-size:16px;padding-top:10px;padding-bottom:10px;padding-left:0px;color:#000000;font-family:Microsoft JhengHei"></td></tr></tbody></table></td></tr></tbody></table><table border="0" cellpadding="0" cellspacing="0" align="center" width="560" style="border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px" class="m_-1402362586549430844wrapper"><tbody><tr><td align="center" valign="top" style="border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%;font-size:18px;font-weight:400;line-height:150%;padding-top:20px;padding-bottom:20px;color:#999999;font-family:Microsoft JhengHei" class="m_-1402362586549430844footer">(本郵件是由系統自動寄送，請勿以此信件回覆。)</td></tr></tbody></table></td></tr></tbody></table>', getdate(),N'admin',getdate(),N'admin')
GO