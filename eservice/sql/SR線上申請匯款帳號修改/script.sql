--INSERT
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES ( max(PARAMETER_ID)+1, N'eservice', N'E0132', N'E0132-使用者輸入提示-二次輸入帳號不相符', N'二次輸入帳號不相符', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE = 'SYSTEM_MSG_PARAMETER'), NULL, N'使用者輸入提示-二次輸入帳號不相符', 1, NULL, NULL, getdate(), N'admin', NULL, NULL);

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER) 
VALUES ( (SELECT MAX(PARAMETER_ID)+1 FROM  ESERVICE_ADM.dbo.PARAMETER ), N'eservice', N'E0133', N'E0133-使用者輸入提示-二次輸入局號與帳號不相符', N'二次輸入局號與帳號不相符', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE = 'SYSTEM_MSG_PARAMETER'), NULL, N'使用者輸入提示-二次輸入局號與帳號不相符', 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
