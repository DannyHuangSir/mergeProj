USE ESERVICE;

CREATE TABLE BXCZ_API_LOG(
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TYPE] [nvarchar](50) NOT NULL,
	[API_CODE] [nvarchar](20) NULL,
	[API_PARAM] [nvarchar](2000) NULL,
	[API_RESPONSE] [nvarchar](4000) NULL,
 CONSTRAINT [PK_BXCZ_API_LOG] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


CREATE TABLE BXCZ_SIGN_RECORD (
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[TRANS_NUM] [nvarchar](15) NULL,
	[VERIFY_CODE] [nvarchar](10) NULL,
	[VERIFY_MSG] [nvarchar](255) NULL,
	[ACTION_ID] [nvarchar](32) NULL,
	[ACTION_ID_START] [datetime] NULL,
	[ACTION_ID_END] [datetime] NULL,
	[ID_VERIFY_STATUS] [nvarchar](50) NULL,
	[ID_VERIFY_TIME] [datetime] NULL,
	[ID_VERIFY_TYPE] [nvarchar](50) NULL,
	[SIGN_FILEID] [nvarchar](50) NULL,
	[SIGN_STATUS] [nvarchar](50) NULL,
	[SIGN_TIME] [datetime] NULL,
	[SIGN_DOWNLOAD] [nvarchar](5) NULL,
 CONSTRAINT [PK_BXCZ_SIGN_RECORD] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE BXCZ_SIGN_FILEDATA (
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[FILE_ID] [nvarchar](50) NULL,
	[COMPANY_ID] [nvarchar](10) NULL,
	[CONTENT_BASE64] [varchar](max) NULL,
 CONSTRAINT [PK_BXCZ_SIGN_FILEDATA] PRIMARY KEY CLUSTERED
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO