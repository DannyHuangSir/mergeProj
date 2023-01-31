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
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-13',
    'admin',
    '2023-01-13');

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
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'VIP客戶名單匯入批次作業', 'F', 'vipBatchImport',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-13',
    'admin',
    '2023-01-13');

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
    ( (select MAX(FUNCTION_ID)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM),N'經代專區', 'FG', 'NULL',
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'權限管理' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-16',
    'admin',
    '2023-01-16');

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
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-16',
    'admin',
    '2023-01-16');

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
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-16',
    'admin',
    '2023-01-16');

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
	(select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm'),
	'eservice_adm',
	(select MAX(SORT)+1 from ESERVICE_ADM.dbo.FUNCTION_ITEM where Try_convert(numeric(38, 4),PARENT_FUNC_ID) = (select a.FUNCTION_ID  from ESERVICE_ADM.dbo.FUNCTION_ITEM a  where a.FUNCTION_NAME = N'經代專區' and SYS_ID = 'eservice_adm')),
	'Y',
    'admin',
    '2023-01-16',
    'admin',
    '2023-01-16');

