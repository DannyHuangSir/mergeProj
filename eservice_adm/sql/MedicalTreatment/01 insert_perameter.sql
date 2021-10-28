-- 保单理赔报表
insert ESERVICE_ADM.[dbo].[FUNCTION_ITEM] select
(select max(FUNCTION_ID)+1 from ESERVICE_ADM.[dbo].[FUNCTION_ITEM]),
N'理賠醫起通申請統計報表',
FUNCTION_TYPE,
'medicalTreatmentStatistics',
PARENT_FUNC_ID,
SYS_ID,
(select max(SORT)+1 from ESERVICE_ADM.[dbo].[FUNCTION_ITEM] where PARENT_FUNC_ID = '184'),
ACTIVE,
CREATE_USER,
getDate(),
UPDATE_USER,
getDate()
from ESERVICE_ADM.[dbo].[FUNCTION_ITEM] where FUNCTION_ID  = 570.0000




insert ESERVICE_ADM.[dbo].[FUNCTION_ITEM] select
(select max(FUNCTION_ID)+1 from ESERVICE_ADM.[dbo].[FUNCTION_ITEM]),
N'理賠醫起通申請明細報表',
FUNCTION_TYPE,
'medicalTreatmentDetail',
PARENT_FUNC_ID,
SYS_ID,
(select max(SORT)+1 from ESERVICE_ADM.[dbo].[FUNCTION_ITEM] where PARENT_FUNC_ID = '184'),
ACTIVE,
CREATE_USER,
getDate(),
UPDATE_USER,
getDate()
from ESERVICE_ADM.[dbo].[FUNCTION_ITEM] where FUNCTION_ID  = 570.0000

---設置醫療聯盟介接案件狀態 組
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
   'MEDICAL_INTERFACE_STATUS',
   N'醫療聯盟介接案件狀態',
   NULL,
   '1.0000',
   GETDATE(),
   'admin',
   NULL,
   NULL
)

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'MEDICAL_INTERFACE_STATUS_FTPS_PQHG', N'首家保險公司已發送，待確認保戶於醫院授權情況', N'FTPS_PQHG',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_PQHF', N'已等待10日未取得保戶授權/授權區間不相符，案件取消', N'PQHF',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_PQHS_PTIG', N'平台已取得保戶於醫院之授權，準備至轉收家公司', N'PQHS_PTIG',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_PQHS_PTIS', N'平台已成功CallBack給保險公司(首家/轉收家)，告知取得醫院授權', N'PQHS_PTIS',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_ITPS_PTHG', N'保險公司(首家/轉收家)已確認將申請醫療資料查調，待發送至醫院端', N'ITPS_PTHG',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_ITPS_END', N'保險公司(首家/轉收家)已確認，不申請此次醫療資料查調', N'ITPS_END',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_PTHS', N'醫院端已接收到此次查調資訊，待醫院端回覆資料', N'PTHS',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_HTPG', N'醫院端已開始傳送資料，但尚未完成所有檔案的傳遞', N'HTPG',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_HTPS_PTIG', N'醫院端已完成所有檔案的傳送，平台準備 CallBack給保險公司(首家/轉收家)', N'HTPS_PTIG',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_HTPS_PTIS', N'案件已取得醫療資料，平台CallBack給保險公司成功', N'HTPS_PTIS',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_ITPS', N'保險公司呼叫平台已成功取得醫療資料', N'ITPS',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_ITPR', N'保險公司發送醫療資料重送', N'ITPR',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
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
     N'MEDICAL_INTERFACE_STATUS_PQHF_END', N'流程結束', N'PQHF_END',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_INTERFACE_STATUS'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go

-----------保單醫起通聯盟文件類型-------
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, STATUS, CREATE_DATE, CREATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), 'eservice_api', 'MEDICAL_TREATMENT_FEDERATION_FILE_TYPE', N'保單醫起通聯盟文件類型', 1, getdate(), 'admin')
go

---醫療聯盟文件類型

INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'MEDICAL_FEDERATION_FILE_TYPE_CertificateDiagnosis', N'診斷證明書', N'CertificateDiagnosis',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_FEDERATION_FILE_TYPE'), 0, NULL, 1,
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
     N'MEDICAL_FEDERATION_FILE_TYPE_Receipt', N'收據', N'Receipt',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_FEDERATION_FILE_TYPE'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL)
go


		--醫療異常件異常件通知訊息--
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, STATUS, CREATE_DATE, CREATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), 'eservice_adm', 'MEDICAL_ABNORMAL_REASON_MSG', '醫療異常件通知訊息', 1, getdate(), 'admin')
go


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_RETURN_PAPER_IN_10DAYS', N'十天內未寄回紙本文件', N'01', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ABNORMAL_REASON_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_UPLOAD_PAPER_NOTCLEAR', N'上傳文件影像不清楚', N'02', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ABNORMAL_REASON_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_RESEND_CONSENT_TO_FORWARD', N'轉送同意書未回傳', N'03', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ABNORMAL_REASON_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_UNION_CLAIMS', N'不屬可申請聯盟理賠的商品', N'04', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ABNORMAL_REASON_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_AUTHORIZATION', N'無授權/授權不通過', N'05', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_ABNORMAL_REASON_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
go

		--醫療提示彈窗信息--
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, STATUS, CREATE_DATE, CREATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), 'eservice_adm', 'MEDICAL_WINDOW_MSG', N'醫療提示彈窗信息', 1, getdate(), 'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_APIALLIANCE_WINDOW_MSG', N'聯盟更新失敗提示信息', N'「目前不可執行已完成：聯盟更新失敗，請稍後再試」', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_ITPR_WINDOW_MSG', N'醫療病例重新上傳提示信息', N'醫療病例重新上傳,成功待醫院端回覆資料', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);



INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_UPDATE_WINDOW_MSG', N'更新案件提示信息', N'更新案件,成功待平台端回覆資料', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_NOT_FINISHED_WINDOW_MSG', N'聯盟狀態流程未結束提示信息', N'聯盟狀態流程未結束,請稍後重試', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice_adm', N'MEDICAL_UPDATE_STATUS_WINDOW_MSG', N'更新案件提示信息', N'當前案件首次正在推送至聯盟中,請稍後操作此操作', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
