USE ESERVICE
GO

--TRUNCATE TABLE ESERVICE.dbo.QUESTION
--TRUNCATE TABLE ESERVICE.dbo.OPTIONS
--go

--- Questions and Options
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME], [EXTRA_VALUE]) VALUES (CAST(1 AS Numeric(18, 0)), N'A. 71歲以上', 1, 0, NULL, NULL, '>71')
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME], [EXTRA_VALUE]) VALUES (CAST(1 AS Numeric(18, 0)), N'B. 51歲以上~70歲', 2, 1, NULL, NULL, '51-70')
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME], [EXTRA_VALUE]) VALUES (CAST(1 AS Numeric(18, 0)), N'C. 40歲以上~50歲', 3, 2, NULL, NULL, '40-50')
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME], [EXTRA_VALUE]) VALUES (CAST(1 AS Numeric(18, 0)), N'D. 40歲以下', 4, 3, NULL, NULL, '<40')
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(2 AS Numeric(18, 0)), N'無投資經驗、僅辦理過定存或傳統型保險商品 (請續填問項3)', 5, 0, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(2 AS Numeric(18, 0)), N'有投資經驗，請填寫2-1、2-2及2-3問項。', 6, 0, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(3 AS Numeric(18, 0)), N'投資型保單、股票、債券、基金', 7, 1, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(3 AS Numeric(18, 0)), N'結構型債券、期貨、選擇權或其他衍生性金融商品', 8, 2, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(4 AS Numeric(18, 0)), N'未滿3個月', 9, 0, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(4 AS Numeric(18, 0)), N'3個月以上~未滿3年', 10, 1, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(4 AS Numeric(18, 0)), N'3年以上', 11, 2, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(5 AS Numeric(18, 0)), N'未滿25%', 12, 1, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(5 AS Numeric(18, 0)), N'25%以上〜未滿50% ', 13, 2, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(5 AS Numeric(18, 0)), N'50%以上', 14, 3, NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(6 AS Numeric(18, 0)), N'A. 只可承受獲利，不能接受損失 【若選擇本選項，不論總分為何，均屬於「不適合購買投資型保單」】', CAST(15 AS Numeric(3, 0)), CAST(0 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(6 AS Numeric(18, 0)), N'B. 可能獲利5%，可能損失5%', CAST(16 AS Numeric(3, 0)), CAST(1 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(6 AS Numeric(18, 0)), N'C. 可能獲利10%，可能損失10%', CAST(17 AS Numeric(3, 0)), CAST(2 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(6 AS Numeric(18, 0)), N'D. 可能獲利15%，可能損失15%', CAST(18 AS Numeric(3, 0)), CAST(3 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(7 AS Numeric(18, 0)), N'A. 無', CAST(19 AS Numeric(3, 0)), CAST(0 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(7 AS Numeric(18, 0)), N'B. 未滿3小時', CAST(20 AS Numeric(3, 0)), CAST(1 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(7 AS Numeric(18, 0)), N'C. 3小時以上~未滿9小時', CAST(21 AS Numeric(3, 0)), CAST(2 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(7 AS Numeric(18, 0)), N'D. 9小時以上', CAST(22 AS Numeric(3, 0)), CAST(3 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(8 AS Numeric(18, 0)), N'A. 虧損未達6個月時就考慮賣掉', CAST(23 AS Numeric(3, 0)), CAST(0 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(8 AS Numeric(18, 0)), N'B. 虧損已經6個月以上才考慮進行調整', CAST(24 AS Numeric(3, 0)), CAST(1 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(8 AS Numeric(18, 0)), N'C. 持有1年以上，因長期投資才是致勝關鍵', CAST(25 AS Numeric(3, 0)), CAST(2 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(9 AS Numeric(18, 0)), N'A. 避免資產的損失', CAST(26 AS Numeric(3, 0)), CAST(1 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(9 AS Numeric(18, 0)), N'B. 資產穩健的成長', CAST(27 AS Numeric(3, 0)), CAST(2 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(9 AS Numeric(18, 0)), N'C. 資產迅速的成長', CAST(28 AS Numeric(3, 0)), CAST(3 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(10 AS Numeric(18, 0)), N'A. 影響程度大', CAST(29 AS Numeric(3, 0)), CAST(0 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(10 AS Numeric(18, 0)), N'B. 影響程度中', CAST(30 AS Numeric(3, 0)), CAST(1 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(10 AS Numeric(18, 0)), N'C. 影響程度小', CAST(31 AS Numeric(3, 0)), CAST(2 AS Numeric(18, 0)), NULL, NULL)
INSERT [ESERVICE].[dbo].[OPTIONS] ([QUESTION_ID], [ITEM], [SEQ], [SCORE], [CREATE_TIME], [UPDATE_TIME]) VALUES (CAST(10 AS Numeric(18, 0)), N'D. 沒有影響', CAST(32 AS Numeric(3, 0)), CAST(3 AS Numeric(18, 0)), NULL, NULL)

INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'1. 您目前的年齡為', NULL, N'N', 1, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'2. 金融商品投資經驗：', NULL, N'N', 2, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'2-1.請問金融商品投資種類【可複選，但以分數較高者計分】', NULL, N'Y', 3, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'2-2.請問2-1問項勾選之金融商品投資時間為', NULL, N'N', 4, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'2-3.請問2-1問項勾選之金融商品投資佔總資產的百分比為', NULL, N'N', 5, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'3.您期望年投資報酬率及所能承受年投資風險為(包含價格與匯率波動)', NULL, N'N', 6, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'4.您每月花多少時間吸收財經資訊', NULL, N'N', 7, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'5.投資因市場波動使得有所虧損時，可能採取哪一項處理方式', NULL, N'N', 8, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'6.您購買投資型保險商品想達成的財務目標', NULL, N'N', 9, getdate(), '2099-12-30')
INSERT [ESERVICE].[dbo].[QUESTION] ([ID], [QUESTION], [TITLE], [IS_MULTI], [SEQ], [EFFECTIVE_TIME], [STOP_TIME]) VALUES (next value for ESERVICE.dbo.QUESTION_SEQUENCE, N'7.如您持有之整體投資資產下跌超過15%，請問對您的生活影響程度為何', NULL, N'N', 10, getdate(), '2099-12-30')
go