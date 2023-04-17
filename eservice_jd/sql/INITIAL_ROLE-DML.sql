INSERT INTO ESERVICE_ADM.DBO.ROLE
(ROLE_ID,ROLE_NAME,DESCRIPTION,PARENT_ROLE_ID,DEPARTMENT_ID,CREATE_USER,CREATE_DATE,MODIFY_USER,MODIFY_DATE,DIV_ROLE_ID)
SELECT  NEWID(),
        '通路主管',
        '通路主管',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'CHANNEL_LEADER' FROM ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
union
SELECT
    NEWID(),
            '分行主管',
            '分行主管',
            '',
            '',
            'admin',
            getdate(),
            'admin',
            getdate(),
            'BRANCH_LEADER' FROM ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0

union
SELECT
 NEWID(),
        '一般人員',
        '一般人員',
        '',
        '',
        'admin',
        getdate(),
        'admin',
        getdate(),
        'GENERAL_PERSONNEL' FROM ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0

union
SELECT
  NEWID(),
         'IC人員',
         'IC人員',
         '',
         '',
         'admin',
         getdate(),
         'admin',
         getdate(),
         'IC_PERSONNEL' FROM ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0

union
SELECT
  NEWID(),
         '平台管理人員',
         '平台管理人員',
         '',
         '',
         'admin',
         getdate(),
         'admin',
         getdate(),
         'ADMINISTRATOR' FROM ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0;


INSERT INTO ESERVICE_ADM.DBO.ROLE_SYS_AUTH
(ROLE_ID,
 SYS_ID,
 CREATE_USER,
 CREATE_DATE)
SELECT ROLE_ID,'eservice_jd', 'admin', getdate()  from ESERVICE_ADM.dbo.ROLE WHERE DIV_ROLE_ID IN ('CHANNEL_LEADER', 'BRANCH_LEADER', 'GENERAL_PERSONNEL', 'IC_PERSONNEL', 'ADMINISTRATOR');

insert ESERVICE_JD.dbo.ROLE_DEPARTMENT(ROLE_ID,a.DEP_ID)
SELECT B.ROLE_ID, A.DEP_ID FROM (
	select row_number() over(order by DEP_ID) as rownum, * from ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
) A LEFT JOIN (SELECT row_number() over(order by ROLE_ID) as rownum, * from ESERVICE_ADM.DBO.ROLE WHERE  DIV_ROLE_ID = 'CHANNEL_LEADER') B
ON A.rownum = B.rownum;

insert ESERVICE_JD.dbo.ROLE_DEPARTMENT(ROLE_ID,a.DEP_ID)
SELECT B.ROLE_ID, A.DEP_ID FROM (
	select row_number() over(order by DEP_ID) as rownum, * from ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
) A LEFT JOIN (SELECT row_number() over(order by ROLE_ID) as rownum, * from ESERVICE_ADM.DBO.ROLE WHERE  DIV_ROLE_ID = 'BRANCH_LEADER') B
ON A.rownum = B.rownum;

insert ESERVICE_JD.dbo.ROLE_DEPARTMENT(ROLE_ID,a.DEP_ID)
SELECT B.ROLE_ID, A.DEP_ID FROM (
	select row_number() over(order by DEP_ID) as rownum, * from ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
) A LEFT JOIN (SELECT row_number() over(order by ROLE_ID) as rownum, * from ESERVICE_ADM.DBO.ROLE WHERE  DIV_ROLE_ID = 'GENERAL_PERSONNEL') B
ON A.rownum = B.rownum;

insert ESERVICE_JD.dbo.ROLE_DEPARTMENT(ROLE_ID,a.DEP_ID)
SELECT B.ROLE_ID, A.DEP_ID FROM (
	select row_number() over(order by DEP_ID) as rownum, * from ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
) A LEFT JOIN (SELECT row_number() over(order by ROLE_ID) as rownum, * from ESERVICE_ADM.DBO.ROLE WHERE  DIV_ROLE_ID = 'IC_PERSONNEL') B
ON A.rownum = B.rownum;

insert ESERVICE_JD.dbo.ROLE_DEPARTMENT(ROLE_ID,a.DEP_ID)
SELECT B.ROLE_ID, A.DEP_ID FROM (
	select row_number() over(order by DEP_ID) as rownum, * from ESERVICE_JD.DBO.DEPARTMENT WHERE PARENT_DEP = 0
) A LEFT JOIN (SELECT row_number() over(order by ROLE_ID) as rownum, * from ESERVICE_ADM.DBO.ROLE WHERE  DIV_ROLE_ID = 'ADMINISTRATOR') B
ON A.rownum = B.rownum;



