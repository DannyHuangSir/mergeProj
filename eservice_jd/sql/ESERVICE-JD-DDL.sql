CREATE DATABASE ESERVICE_JD
GO

ALTER DATABASE ESERVICE_JD
    COLLATE Chinese_Taiwan_Stroke_CI_AS
GO

USE [ESERVICE_JD]
GO

CREATE TABLE [ESERVICE_JD].[dbo].[USERS](
	[USER_ID] [nvarchar](50) NOT NULL,
	[USER_TYPE] [nvarchar](6) NULL,
	[ROC_ID] [nvarchar](10) NOT NULL,
	[MOBILE] [nvarchar](50) NULL,
	[EMAIL] [nvarchar](100) NULL,
	[LOGIN_FAIL_COUNT] [numeric](38, 4) NULL,
	[LAST_CHANG_PASSWORD_DATE] [datetime] NULL,
	[SMS_FLAG] [nvarchar](1) NULL,
	[MAIL_FLAG] [nvarchar](1) NULL,
	[FB_ID] [nvarchar](100) NULL,
	[MOICA_ID] [nvarchar](100) NULL,
	[CREATE_DATE] [datetime] NULL,
	[CREATE_USER] [nvarchar](50) NULL,
	[ONLINE_FLAG] [nvarchar](1) NULL,
	[STATUS] [nvarchar](10) NULL,
	[USER_NAME] [nvarchar](50) NULL,
	[LOGIN_TIME] [datetime] NULL,
	[CLAUSE_FLAG] [nchar](10) NULL,
	[SERIAL_NUM] [nvarchar](50) NOT NULL,
    [IC_ID] [nvarchar](20) NOT NULL,
    [LOGIN_SIZE] [nvarchar](20)  NULL,
    [EFFECTIVE_DATE] [datetime] NULL,
    [EXPIRATION_DATE] [datetime] NULL,
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE_JD].[dbo].[USERS] ADD  CONSTRAINT [DF_USERS_STATUS]  DEFAULT (N'enable') FOR [STATUS]
GO

CREATE FUNCTION [dbo].[FN_DEBASE64]
	(@String NVARCHAR(512))
RETURNS NVARCHAR(512)
AS
BEGIN
    RETURN CAST(CAST(N'' AS XML).value('xs:base64Binary(sql:variable("@String"))', 'VARBINARY(MAX)') AS NVARCHAR(MAX))
END
GO

CREATE FUNCTION [dbo].[FN_ENBASE64]
	(@String NVARCHAR(512))
RETURNS NVARCHAR(512)
AS
BEGIN
	DECLARE @BinString VARBINARY(MAX)
	SET @BinString = CAST(@String AS VARBINARY(MAX))
	RETURN CAST(N'' AS XML).value('xs:base64Binary(xs:hexBinary(sql:variable("@BinString")))','NVARCHAR(512)')
END
GO

CREATE TABLE [ESERVICE_JD].[dbo].[DEPARTMENT](
    [DEP_ID] [nvarchar](50) NOT NULL,
    [DEP_NAME] [nvarchar](100) NOT NULL,
    [DESCRIPTION] [nvarchar](200) NULL,
    [PARENT_DEP] [nvarchar](50) NULL,
    [CREATE_USER] [nvarchar](50) NULL,
    [CREATE_DATE] [date] NULL,
    [MODIFY_USER] [nvarchar](50) NULL,
    [MODIFY_DATE] [date] NULL,
    [BRANCH_ID] [nvarchar](50) NULL
    ) ON [PRIMARY]
GO

CREATE TABLE [ESERVICE_JD].[dbo].[MESSAGE](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TITLE] [nvarchar](50) NOT NULL,
	[MSG] [nvarchar](100) NOT NULL,
	[STATUS] [numeric](38, 4) NOT NULL,
	[CREATE_DATE] [datetime] NULL,
	[USER_ID] [varchar](50) NOT NULL,
	[NOTIFY_TIME] [datetime] NOT NULL,
 CONSTRAINT [SYS_C0010047] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [ESERVICE_JD].[dbo].[NOTIFY_SCHEDULE](
    [ID] [bigint] IDENTITY(1,1) NOT NULL,
    [TITLE] [nvarchar](50) NOT NULL,
    [MSG_CONTENT] [ntext] NOT NULL,
    [USERS] [ntext] NULL,
    [DEPS] [ntext] NULL,
    [ROLES] [ntext] NULL,
    [NOTIFY_TIME] [datetime] NOT NULL,
    [CREATE_DATE] [datetime] NOT NULL,
    [STATUS] [int] NOT NULL,
    [NOTIFY_TARGET] [ntext] NULL,
    [PASSAGE_WAY] [nvarchar](50) NULL,
 CONSTRAINT [PK_NOTIFY_SCHEDULE] PRIMARY KEY CLUSTERED
(
    [ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO


CREATE TABLE [ESERVICE_JD].[dbo].[USER_DEPARTMENT](
    [USER_ID] [varchar](50) NOT NULL,
    [DEP_ID] [varchar](50) NOT NULL,
    [TITLE_ID] [varchar](50) NULL,
    [BRANCH_ID] [varchar](50) NOT NULL
    ) ON [PRIMARY]
GO

CREATE TABLE [ESERVICE_JD].[dbo].[USER_IC](
	[USER_ID] [varchar](50) NOT NULL,
	[DEP_ID] [varchar](50) NOT NULL,
    [BRANCH_ID] [varchar](50) NOT NULL
) ON [PRIMARY]
GO

CREATE TABLE [ESERVICE_JD].[dbo].[ROLE_DEPARTMENT](
    [ROLE_ID] [varchar](50) NOT NULL,
    [DEP_ID] [varchar](50) NOT NULL
    ) ON [PRIMARY]
GO

alter table ESERVICE_ADM.dbo.ROLE add DIV_ROLE_ID  [nvarchar](50) NULL

alter table ESERVICE_JD.DBO.USER_DEPARTMENT alter column BRANCH_ID [nvarchar](50) NULL;

CREATE TABLE [ESERVICE_JD].[dbo].[BATCH_PLAN](
    [BATCH_ID] [varchar](50) NOT NULL,
    [BATCH_STATUS] [varchar](50) NOT NULL,
    [BATCH_JOIN_TIME] [datetime] NOT NULL,
    [BATCH_START_TIME] [datetime] NULL,
    [BATCH_END_TIME] [datetime] NULL,
    [FAIL_NUM] [int] NULL,
    [FAIL_LINK] [varbinary](max) NULL,
    [TYPE] [varchar](50) NOT NULL,
    [BATCH_FILE] [varbinary](max) NULL
    ) ON [PRIMARY]
    GO
