
--SYSTEMS table
insert into ESERVICE_ADM.dbo.SYSTEMS
(SYS_ID,SYS_NAME,CREATE_USER,CREATE_TIMESTAMP)
values
('eservice_api','保單網路服務API','admin',getdate());

-----Insert PARAMETER_CATEGORY
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
   'SYS_PRO_SCH',
   '日程安排-系統屬性',
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
   'SYS_PRO_BASE_URL',
   '基本URL-系統屬性',
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
   'SYS_PRO_API_URL',
   '資源定位地址-系統屬性',
   NULL,
   '1.0000',
   GETDATE(),
   'admin',
   NULL,
   NULL
)

-----Insert PARAMETER
INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api.disable', N'cron.api.disable', N'Y', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.saveToEserviceTrans.expression', N'cron.saveToEserviceTrans.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api101.expression', N'cron.api101.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api102.expression', N'cron.api102.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api103.expression', N'cron.api103.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api104.expression', N'cron.api104.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api105.expression', N'cron.api105.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'cron.api106.expression', N'cron.api106.expression', N'0 */5 * * * ?', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api.accessToken', N'alliance.api.accessToken', N'ItRjYfGscv6eV1ZDq5Gq0XkimNKwqoX6', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_BASE_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api.baseRrl', N'alliance.api.baseRrl', N'http://210.61.11.88:80/lia-api', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_BASE_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api101.url', N'alliance.api101.url', N'http://210.61.11.88:80/lia-api/api/ext/lia101i', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api102.url', N'alliance.api102.url', N'http://210.61.11.88:80/lia-api/api/ext/lia102i', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api103.url', N'alliance.api103.url', N'http://210.61.11.88:80/lia-api/api/ext/lia103i', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api104.url', N'alliance.api104.url', N'http://210.61.11.88:80/lia-api/api/ext/lia104i', 
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api105.url', N'alliance.api105.url', N'http://210.61.11.88:80/lia-api/api/ext/lia105i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID, 
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, 
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, 
     ENCRYPT_TYPE, PARENT_PARAMETER_ID, 
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', 
     N'alliance.api106.url', N'alliance.api106.url', N'http://210.61.11.88:80/lia-api/api/ext/lia106i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1, 
     NULL, NULL, 
     GETDATE(), N'admin', NULL, NULL);






