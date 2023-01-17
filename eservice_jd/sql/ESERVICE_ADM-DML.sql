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
    (694.0000, '使用者匯入批次作業', 'F', 'userBatchImport', '185', 'eservice_adm', 6, 'Y', 'admin','2023-01-13','admin','2023-01-13'),
    (695.0000, 'VIP客戶名單匯入批次作業', 'F', 'vipBatchImport', '185', 'eservice_adm', 6, 'Y','admin','2023-01-13','admin','2023-01-13'),
    (696.0000, '經代專區', 'FG', 'NULL', '185', 'eservice_adm', 6, 'Y','admin','2023-01-16','admin','2023-01-16'),
    (697.0000, '部門管理', 'F', 'jdDeptMgnt', '696', 'eservice_adm', 1, 'Y', 'admin', '2023-01-16', 'admin', '2023-01-16'),
    (698.0000, '角色管理', 'F', 'jdRole', '696', 'eservice_adm', 2, 'Y', 'admin', '2023-01-16', 'admin', '2023-01-16'),
    (699.0000, '人員管理', 'F', 'jdUserMgnt', '696', 'eservice_adm', 3, 'Y', 'admin', '2023-01-16', 'admin', '2023-01-16');

insert ESERVICE_JD.dbo.DEPARTMENT(
    DEP_ID,
    DEP_NAME,
    DESCRIPTION,
    PARENT_DEP,
    CREATE_USER,
    CREATE_DATE,
    MODIFY_USER,
    MODIFY_DATE
)values ('0', 'Root', 'NULL', 'NULL', 'admin', '2023-01-16', 'admin', '2023-01-16');



