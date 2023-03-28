USE ESERVICE_ADM
GO
insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'經代專區支援管理', 'FG', 'NULL',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());

insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'使用者匯入批次作業', 'F', 'userBatchImport',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區支援管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());

insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'IC人員負責分行匯入批次作業', 'F', 'ICBatchImport',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區支援管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());



insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'部門管理', 'F', 'jdDeptMgnt',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區支援管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());

insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'角色管理', 'F', 'jdRole',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區支援管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());

insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'人員管理', 'F', 'jdUserMgnt',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區支援管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());

insert
ESERVICE_ADM.dbo.FUNCTION_ITEM(
    FUNCTION_ID,
    FUNCTION_NAME,
    FUNCTION_TYPE,
    FUNCTION_URL,
    PARENT_FUNC_ID,
    SYS_ID,
    SORT,
    ACTIVE,
    CREATE_USER,
    CREATE_TIMESTAMP,
    UPDATE_USER,
    UPDATE_TIMESTAMP)
values
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'保單及照會明細報表', 'F', 'policyClaimDetail',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'報表查詢' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 0),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'報表查詢' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    GETDATE(),
    'admin',
    GETDATE());