use ESERVICE;
go

CREATE TABLE [ESERVICE].[dbo].[TRANS_FUND_CONVERSION](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[TRANS_NUM] [nvarchar](15) NOT NULL,
	[POLICY_NO] [nvarchar](10) NOT NULL,
	[INVT_NO] [nvarchar](10) NOT NULL,
	[INVESTMENT_TYPE] [nvarchar](5) NOT NULL,
	[RATIO] [numeric](3, 0) NULL,
	[VALUE] [numeric](12, 4) NULL,
 CONSTRAINT [PK_TRANS_FUND_CONVERSION] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE].[dbo].[TRANS_FUND_CONVERSION] ADD  CONSTRAINT [DF_TRANS_FUND_CONVERSION_INVESTMENT_TYPE]  DEFAULT (N'IN') FOR [INVESTMENT_TYPE]
GO

CREATE TABLE [ESERVICE].[dbo].[TRANS_INVESTMENT](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[TRANS_NUM] [nvarchar](15) NOT NULL,
	[POLICY_NO] [nvarchar](10) NOT NULL,
	[INVT_NO] [nvarchar](10) NOT NULL,
	[DISTRIBUTION_RATIO] [numeric](3, 0) NULL,
 CONSTRAINT [PK_TRANS_INVESTMENT] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE].[dbo].[TRANS_INVESTMENT] ADD  [PRE_DISTRIBUTION_RATIO] [numeric](3, 0) NULL
GO

ALTER TABLE [ESERVICE].[dbo].[FUND] ADD [FUND_ISSUER] [nvarchar](40) NULL
GO

ALTER TABLE [ESERVICE].[dbo].[FUND] ADD [FUND_ISSUER_CODE] [nvarchar](40) NULL
GO

CREATE TABLE [ESERVICE].[dbo].[TRANS_DEPOSIT](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
    [TRANS_NUM] [nvarchar](50) NOT NULL,
    [POLICY_NO] [nvarchar](50) NOT NULL,
    [INVT_NO] [nvarchar](50) NULL,
    [AMOUNT] [numeric](18, 2) NULL,
    [RATIO] [numeric](10, 0) NULL,
    [DEPOSIT_METHOD] [nvarchar](1) NOT NULL,
    [PAYMENT_METHOD] [nvarchar](1) NOT NULL,
    [SWIFT_CODE] [nvarchar](50) NULL,
    [ENGLISH_NAME] [nvarchar](100) NULL,
    [BANK_CODE] [nvarchar](3) NULL,
    [BRANCH_CODE] [nvarchar](4) NULL,
    [ACCOUNT_NAME] [nvarchar](200) NULL,
    [BANK_ACCOUNT] [nvarchar](100) NULL,
    [ZIP_CODE] [nvarchar](6) NULL,
    [ADDRESS] [nvarchar](100) NULL,
    [BANK_NAME] [nvarchar](100) NULL,
    [BRANCH_NAME] [nvarchar](100) NULL,
    [CURRENCY] [nvarchar](10) NULL,
 CONSTRAINT [PK_TRANS_DEPOSIT] PRIMARY KEY CLUSTERED
(
    [ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE].[dbo].[TRANS_FUND_NOTIFICATION_DTL] ADD [UP_VALUE] [numeric](18, 2) NULL
GO

ALTER TABLE [ESERVICE].[dbo].[TRANS_FUND_NOTIFICATION_DTL] ADD [DOWN_VALUE] [numeric](18, 2) NULL
GO

ALTER TABLE [ESERVICE].[dbo].[ROI_NOTIFICATION_JOB] ADD [UP_VALUE] [numeric](18, 2) NULL
GO

ALTER TABLE [ESERVICE].[dbo].[ROI_NOTIFICATION_JOB] ADD [DOWN_VALUE] [numeric](18, 2) NULL
GO

--- 收益分配或撥回資產分配方式
CREATE TABLE [ESERVICE].[dbo].[TRANS_CASH_PAYMENT](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[TRANS_NUM] [nvarchar](15) NOT NULL,
	[POLICY_NO] [nvarchar](10) NOT NULL,
	[ALLOCATION] [nvarchar](20) NOT NULL,
	[PRE_ALLOCATION] [nvarchar](20) NULL,
 CONSTRAINT [PK_TRANS_CASH_PAYMENT] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [ESERVICE].[dbo].[QUESTION](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[QUESTION] [nvarchar](50) NOT NULL,
	[TITLE] [nvarchar](50) NULL,
	[IS_MULTI] [nvarchar](1) NOT NULL,
	[SEQUENCE] [numeric](3, 0) NULL,
	[CREATE_TIME] [date] NULL,
	[UPDATE_TIME] [date] NULL,
 CONSTRAINT [PK_QUESTIONNAIRE] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE].[dbo].[QUESTION] ADD  CONSTRAINT [DF_QUESTIONNAIRE_IS_MULTI]  DEFAULT (N'N') FOR [IS_MULTI]
GO

ALTER TABLE [ESERVICE].[dbo].[QUESTION] ADD  CONSTRAINT [DF_Table_1_ORDER]  DEFAULT ((0)) FOR [SEQUENCE]
GO

CREATE TABLE [ESERVICE].[dbo].[OPTIONS](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[QUESTION_ID] [numeric](18, 0) NULL,
	[ITEM] [nvarchar](50) NOT NULL,
	[SEQUENCE] [numeric](3, 0) NULL,
	[SCORE] [numeric](18, 0) NULL,
	[CREATE_TIME] [date] NULL,
	[UPDATE_TIME] [date] NULL,
 CONSTRAINT [PK_OPTIONS] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [ESERVICE].[dbo].[OPTIONS] ADD CONSTRAINT [DF_Table_2_ORDER]  DEFAULT ((0)) FOR [SEQUENCE]
GO

CREATE TABLE [ESERVICE].[dbo].[ANSWER](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[QUESTIONS] [text] NULL,
	[ANSWERS] [nvarchar](1000) NULL,
	[TRANS_NUM] [nvarchar](15) NULL,
	[CREATE_TIME] [date] NULL,
	[UPDATE_TIME] [date] NULL,
 CONSTRAINT [PK_ANSWER] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
