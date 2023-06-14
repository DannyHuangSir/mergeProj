USE ESERVICE;

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'C', N'出院病摘', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr7', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'D', N'醫學影響', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr8', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'E', N'病理檢查', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr9', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'F', N'手術資料', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr10', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'G', N'門診', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr11', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'H', N'急診', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr12', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'I', N'住院', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr13', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'Z', N'其他', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='INSURANCE_CLAIM_UPLOADFILE' AND SYSTEM_ID = 'eservice'), NULL, N'file-fr14', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID , SYSTEM_ID , PARAMETER_CODE , PARAMETER_NAME , PARAMETER_VALUE , PARAMETER_CATEGORY_ID , SORT_NO , REMARK , STATUS , ENCRYPT_TYPE , PARENT_PARAMETER_ID , CREATE_DATE , CREATE_USER , UPDATE_DATE , UPDATE_USER ) VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER) , N'eservice', N'41', N'數位身份驗證及數位簽署等待處理', N'數位身份驗證及數位簽署等待處理', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='ONLINE_CHANGE_STATUS' AND SYSTEM_ID = 'eservice') , NULL , NULL , 1 , NULL , NULL , getdate() , N'system', NULL , NULL )
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID , SYSTEM_ID , PARAMETER_CODE , PARAMETER_NAME , PARAMETER_VALUE , PARAMETER_CATEGORY_ID , SORT_NO , REMARK , STATUS , ENCRYPT_TYPE , PARENT_PARAMETER_ID , CREATE_DATE , CREATE_USER , UPDATE_DATE , UPDATE_USER ) VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER) , N'eservice', N'42', N'數位身份驗證及數位簽署作業處理中', N'數位身份驗證及數位簽署作業處理中', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='ONLINE_CHANGE_STATUS' AND SYSTEM_ID = 'eservice') , NULL , NULL , 1 , NULL , NULL , getdate() , N'system', NULL , NULL )
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID , SYSTEM_ID , PARAMETER_CODE , PARAMETER_NAME , PARAMETER_VALUE , PARAMETER_CATEGORY_ID , SORT_NO , REMARK , STATUS , ENCRYPT_TYPE , PARENT_PARAMETER_ID , CREATE_DATE , CREATE_USER , UPDATE_DATE , UPDATE_USER ) VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER) , N'eservice', N'43', N'數位身份驗證失敗', N'數位身份驗證失敗', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='ONLINE_CHANGE_STATUS' AND SYSTEM_ID = 'eservice') , NULL , NULL , 1 , NULL , NULL , getdate() , N'system', NULL , NULL )
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID , SYSTEM_ID , PARAMETER_CODE , PARAMETER_NAME , PARAMETER_VALUE , PARAMETER_CATEGORY_ID , SORT_NO , REMARK , STATUS , ENCRYPT_TYPE , PARENT_PARAMETER_ID , CREATE_DATE , CREATE_USER , UPDATE_DATE , UPDATE_USER ) VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER) , N'eservice', N'44', N'數位簽署失敗', N'數位簽署失敗', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='ONLINE_CHANGE_STATUS' AND SYSTEM_ID = 'eservice') , NULL , NULL , 1 , NULL , NULL , getdate() , N'system', NULL , NULL )

INSERT ESERVICE_ADM.DBO.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) VALUES ((select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api', N'cron.api417.expression', N'cron.api417.expression', N'0 */5 * * * ?', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_SCH' AND SYSTEM_ID = 'eservice_api'), 0, NULL, 1, NULL, NULL, GETDATE(), N'admin', NULL, NULL);


INSERT ESERVICE_ADM.DBO.PARAMETER (
     PARAMETER_ID, SYSTEM_ID,
     PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE,
     PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS,
     ENCRYPT_TYPE, PARENT_PARAMETER_ID,
     CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES (
     (select max(PARAMETER_ID)+1 from ESERVICE_ADM.DBO.PARAMETER), N'eservice_api',
     N'alliance.api108.url', N'alliance.api108.url', N'http://210.61.11.88:80/lia-api/api/ext/lia108i',
     (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.DBO.PARAMETER_CATEGORY where CATEGORY_CODE='SYS_PRO_API_URL'), 0, NULL, 1,
     NULL, NULL,
     GETDATE(), N'admin', NULL, NULL);

go

INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice_api',N'ELIFE_MAIL_071',N'【臺銀人壽理賠申請首家件失敗通知】MAIL模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'臺銀人壽理賠申請首家件失敗通知MAIL模板',N'<table width=\"100%\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%\" class=\"m_-1402362586549430844background\"> <tbody> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0\" bgcolor=\"#F0F0F0\"> <br> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" bgcolor=\"#FFFFFF\" width=\"560\" style=\"border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px\" class=\"m_-1402362586549430844container\"> <tbody> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:24px;font-weight:bold;line-height:130%;padding-top:0px;padding-bottom:15px;color:#000000;font-family:Microsoft JhengHei\" class=\"m_-1402362586549430844header\"> <a href=\"https://www.twfhclife.com.tw/\" target=\"_blank\"> <img src=\"https://elife.twfhclife.com.tw/eservice/img/my-logo.png\" class=\"CToWUd\"> </a> </td> </tr> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-bottom:3px;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:18px;font-weight:600;line-height:150%;padding-top:5px;color:#000000;font-family:Microsoft JhengHei\" class=\"m_-1402362586549430844subheader\">臺銀人壽保單理賠申請失敗通知</td> </tr> <tr> <td align=\"left\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;font-size:16px;font-weight:400;line-height:160%;padding-top:25px;color:#000000;font-family:Microsoft JhengHei\" class=\"m_-1402362586549430844paragraph\"> 親愛的客戶您好 : <br/> 感謝您使用本公司保單網路服務，您的保單理賠申請案，申請時間 : ${LoginTime}，申請序號 : ${TransNum}，案件狀態 : ${TransStatus}。如需其他服務請撥打本公司客服專線或洽各地分公司辦理。</td> </tr> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%;width:87.5%;padding-top:5px\" class=\"m_-1402362586549430844line\"> <hr color=\"#E0E0E0\" align=\"center\" width=\"100%\" size=\"1\" noshade=\"\" style=\"margin:0;padding:0\"> </td> </tr> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;padding-left:6.25%;padding-right:6.25%\" class=\"m_-1402362586549430844list-item\"> <table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:inherit;margin:0;padding:0;border-collapse:collapse;border-spacing:0\"> <tbody> <tr> <td align=\"left\" valign=\"top\" style=\"font-size:16px;padding-top:10px;padding-bottom:10px;padding-left:0px;color:#000000;font-family:Microsoft JhengHei\">客服專線：0800-011-966</td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"560\" style=\"border-collapse:collapse;border-spacing:0;padding:0;width:inherit;max-width:560px\" class=\"m_-1402362586549430844wrapper\"> <tbody> <tr> <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse;border-spacing:0;margin:0;padding:0;width:100%;font-size:18px;font-weight:400;line-height:150%;padding-top:20px;padding-bottom:20px;color:#999999;font-family:Microsoft JhengHei\" class=\"m_-1402362586549430844footer\">(本郵件是由系統自動寄送，請勿以此信件回覆。)</td> </tr> </tbody> </table> </td> </tr> </tbody> </table>', getdate(),N'admin',getdate(),N'admin')
GO


INSERT INTO ESERVICE_ADM.[dbo].[MESSAGING_TEMPLATE]([MESSAGING_TEMPLATE_ID],[SYSTEM_ID],[MESSAGING_TEMPLATE_CODE],[MESSAGING_TEMPLATE_NAME],[STATUS],[TRIGGER_TYPE],[EVENT_TYPE],[SEND_TYPE],[SEND_TIME],[CIRCLE_TYPE],[CIRCLE_VALUE],[RECEIVER_MODE],[MESSAGING_SUBJECT],[MESSAGING_CONTENT],[CREATE_DATE],[CREATE_USER],[UPDATE_DATE],[UPDATE_USER])
VALUES((select max([MESSAGING_TEMPLATE_ID])+1 from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE),N'eservice_api',N'ELIFE_SMS_071',N'【臺銀人壽理賠申請首家件失敗通知】SMS模板'
,3,N'api ',NULL,N'email',NULL,NULL,NULL,N'dynamic',N'臺銀人壽理賠申請首家件失敗通知SMS模板',N'親愛客戶您好 : 您的保單理賠申請案，申請時間 : ${LoginTime}，申請序號 :${TransNum}，案件狀態 : ${TransStatus}。如需其他服務請撥打本公司客服專線或洽各地分公司辦理，客服專線 : 0800-011-966。', getdate(),N'admin',getdate(),N'admin')
GO


INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_MAIL_071'),
		(select PARAMETER_ID from   ESERVICE_ADM.dbo.PARAMETER  where  PARAMETER_CODE='MSG_PARAM_LoginTime'));
GO

INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_MAIL_071'),
		(select PARAMETER_ID from   ESERVICE_ADM.dbo.PARAMETER where PARAMETER_CODE='MSG_PARAM_TransNum'));
GO

INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_MAIL_071'),
		(select PARAMETER_ID from  ESERVICE_ADM.dbo.PARAMETER where PARAMETER_CODE='MSG_PARAM_TransStatus'));
GO

INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_SMS_071'),
		(select PARAMETER_ID from   ESERVICE_ADM.dbo.PARAMETER  where  PARAMETER_CODE='MSG_PARAM_LoginTime'));
GO

INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_SMS_071'),
		(select PARAMETER_ID from   ESERVICE_ADM.dbo.PARAMETER where PARAMETER_CODE='MSG_PARAM_TransNum'));
GO

INSERT ESERVICE_ADM.dbo.MESSAGING_PARAMETER (MESSAGING_TEMPLATE_ID,PARAMETER_ID) VALUES ((select MESSAGING_TEMPLATE_ID from ESERVICE_ADM.dbo.MESSAGING_TEMPLATE where MESSAGING_TEMPLATE_CODE = 'ELIFE_SMS_071'),
		(select PARAMETER_ID from  ESERVICE_ADM.dbo.PARAMETER where PARAMETER_CODE='MSG_PARAM_TransStatus'));
GO