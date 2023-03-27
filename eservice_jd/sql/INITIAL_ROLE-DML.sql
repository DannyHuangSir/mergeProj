INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
VALUES
    (
        NEWID(),
        '通路主管',
        '通路主管',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'CHANNEL_LEADER'
    )
    insert INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
VALUES ((select ROLE_ID from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID = 'CHANNEL_LEADER'),
    'eservice_jd',
    'admin',
    getdate());

INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
VALUES
    (
        NEWID(),
        '分行主管',
        '分行主管',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'BRANCH_LEADER'
    )
    insert INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
VALUES ((select ROLE_ID from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID = 'BRANCH_LEADER'),
    'eservice_jd',
    'admin',
    getdate());



INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
VALUES
    (
        NEWID(),
        '一般人員',
        '一般人員',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'GENERAL_PERSONNEL'
    )
    insert INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
VALUES ((select ROLE_ID from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID = 'GENERAL_PERSONNEL'),
    'eservice_jd',
    'admin',
    getdate());



INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
VALUES
    (
        NEWID(),
        'IC人員',
        'IC人員',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'IC_PERSONNEL'
    )
    insert INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
VALUES ((select ROLE_ID from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID = 'IC_PERSONNEL'),
    'eservice_jd',
    'admin',
    getdate());


INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
VALUES
    (
        NEWID(),
        '平台管理人員',
        '平台管理人員',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'ADMINISTRATOR'
    )
    insert INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
VALUES ((select ROLE_ID from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID = 'ADMINISTRATOR'),
    'eservice_jd',
    'admin',
    getdate());


