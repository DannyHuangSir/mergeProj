CREATE DATABASE ESERVICE_JD
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
	[STATUS] [nvarchar](10) NULL
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
