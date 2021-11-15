
use ESERVICE_ADM
GO

-----------設置同意條款-------
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, STATUS, CREATE_DATE, CREATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), 'eservice', 'MEDICAL_TREATMENT', '保單醫起通條款', 1, getdate(), 'admin')
go

--- 醫療保險-条款项
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'MEDICAL_TREATMENT_CONTENT', N' 醫起通接受條款',
		N'	<h2 class="text-left">一、	授權個人申請醫療理賠相關資料傳送</h2>
		<p>本人（即立同意書人）為申請醫療保險理賠所需，爰同意並授權貴公司經由「理賠醫起通」服務傳輸以下事項：</p>
		<p>1.	本人同意授權貴公司得將本人之個人資料 (包括姓名、出生日、身分證字號)傳輸予經本人指定之醫療院所，以取得本人已同意授權傳輸之就醫相關資料。</p>
		<p>2.	本人知悉，指定之醫療院所得將本人指定授權區間就診之「全部科別」之就醫相關資料(包括但不限於姓名、出生年月日、身分證字號、醫療費用證明或收據副本及相關診斷證明書文件等)，傳輸予貴公司。</p>
		<h2 class="text-left">二、	個人理賠醫療資料傳送</h2>
		<p>1.	本人同意授權貴公司得向醫療院所調閱申請醫療保險理賠所需之醫療單據資料(包括但不限於診斷證明書、醫療費用證明等)。</p>
		<p>2.	依貴公司網站公告之醫療院所為授權對象。</p>
		<h2 class="text-left">三、	個人理賠申請通知</h2>
		<p>1.	本人同意授權貴公司得將本人提供申請醫療保險金之理賠案件申請內容，傳送至本人所同意之其他保險公司，以申請各該保險公司中以本人為被保險人之醫療保險理賠。</p>
		<p>2.	依貴公司網站公告之合作保險公司為授權對象。</p>
		<p>3.	若貴公司未能自醫療院所取得醫療相關資料，本人將自行向指定之合作保險公司提出理賠申請。</p>
		<h2 class="text-left">四、	本人已詳閱並充分瞭解上述各事項，除同意貴公司於符合相關法令之規範內，得為蒐集、處理及利用上開本人各項資料外；另聲明同意依照下列事項辦理：</h2>
		<p>1.	若貴公司於收到申請後十個工作日仍未能自本人所指定之醫療院所取得醫療相關資料，本人知悉所提出理賠申請文件未齊備，並盡速備齊相關文件。</p>
		<p>2.	理賠申請之文件備齊日，係以貴公司收齊本人所指定之醫療院所提供醫療單據資料之日為準。</p>
		<p>3.	本人知悉並同意若貴公司未能自本人所指定之醫療院所取得醫療相關資料，不論貴公司或其他本人所指定之合作保險公司均無法透過本項理賠服務進行理賠作業，本人將自行另向其他保險公司提出理賠申請。</p>
		<h2 class="text-left">五、	本人知悉依各家公司保單條款約定，若需提供正本文件（如收據、調查文件等），將依各家保險公司通知配合提供。</h2>
		<h2 class="text-left">六、	本人已審閱蒐集、處理及利用個人資料告知事項</h2>
		<p class="text-center">【蒐集、處理及利用個人資料告知事項】</p>
		<p>臺銀人壽保險股份有限公司(下稱本公司)依據個人資料保護法(以下稱個資法)第六條第二項、第八條第一項(間接蒐集者為個資法第九條第一項)及保險法第一百七十七條之一暨其授權辦法等相關規定，為辦理保險相關業務(含網路保險服務)之客戶服務、核保、理賠、契約保全、再保險、追償、申訴及爭議處理、辦理內部控制及稽核業務、提供除本公司外，於本公司網站公告之合作保險公司（利用對象）辦理理賠申請、理賠資料傳送爭議及更正、合於其營業項目或章程所訂業務需要等目的及為符合相關法令規範需要，而蒐集、處理、利用您的之病歷、醫療及健康檢查等個人相關資料。所蒐集之資料除再保險業務或委外業務執行之需要而於我國境外被處理及利用外，僅會於前開蒐集目的存續期間及依法令規定期間內，以合於法令規定之方式利用。您可以向本公司或同意傳送醫療單據資料之保險公司查詢、請求閱覽、製給複製本、補充或更正、請求停止蒐集、處理或利用及請求刪除個人資料，惟本公司及您同意傳送醫療單據資料之保險公司依法令規定或因執行業務所必須，得不依台端的請求處理。您若因未能提供相關個人資料，本公司及您同意傳送醫療單據資料之保險公司將可能延後或無法進行必要之審核及處理作業，將因此導致遲延或無法提供台端相關服務。</p>
		',
		(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL)
go

-----------設置保單醫起通上傳申請應備文件-------
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, STATUS, CREATE_DATE, CREATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), 'eservice', 'MEDICAL_TREATMENT_UPLOADFILE', '保單醫起通上傳申請應備文件', 1, getdate(), 'admin')
go

		--- 理賠申請醫療單據調閱授權同意書
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'MEDICAL_DOCUMENT_CONTENT', N'理賠申請醫療單據調閱授權同意書(如為首次申請需要附上)', N'N', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_UPLOADFILE'), NULL, N'file-fr1', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go


		--- 理賠申請醫療單書面描述信息
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'MEDICAL_TREATMENT_PAGE', N' 理賠醫書面條款信息',
		N'數據庫中的數據--即日起，使用本公司線上理賠申請服務時，可一併授權本公司將您的理賠申請資料，透過「中華民國人壽保險商業同業公會」建制之「理賠醫起通」服務傳送予您有投保之保險公司（該保險公司須有參與理賠醫起通），免除以往須向不同保險公司分別提出理賠申請之不便。',
		(select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='PAGE_WORDING'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL)
go



	--保單理賠醫起通  對於的保單類型
INSERT ESERVICE_ADM.dbo.PARAMETER_CATEGORY (PARAMETER_CATEGORY_ID, SYSTEM_ID, CATEGORY_CODE, CATEGORY_NAME, REMARK, STATUS, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(PARAMETER_CATEGORY_ID)+1 from ESERVICE_ADM.dbo.PARAMETER_CATEGORY), N'eservice', N'MEDICAL_TREATMENT_ITEMS', N'保單理賠醫起通', N'', 1, getdate(), N'admin', getdate(), N'admin')
GO

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'UC', N'保單理賠醫起通', N'UC', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'保單理賠醫起通', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'US', N'保單理賠醫起通', N'US', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'保單理賠醫起通', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'01', N'疾病住院給付附約（附一）', N'01', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'疾病住院', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'02', N'意外傷害醫療給付附約（附二）', N'02', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'意外傷害醫療', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'04', N'家庭傷害保障附約（附四）', N'04', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'家庭傷害保障', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')

go
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'05', N'住院醫療給付附約（附五）', N'05', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'住院醫療', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'07', N'癌症醫療健康保險附約（附七）', N'07', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'癌症醫療健康保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'08', N'意外傷害住院醫療日額給付附約', N'08', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'意外傷害醫療日額', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'09', N'住院醫療日額給付保險附約（附九）', N'09', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'住院醫療日額', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1C', N'綜合住院醫療日額給付附約（附十二）', N'1C', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'綜合住院醫療日額', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1E', N'六年期住院醫療日額給付附約', N'1E', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'六年住院醫療日額', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1F', N'新住院醫療給付附約', N'1F', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新住院醫療給付', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1G', N'新傷害醫療給付附約', N'1G', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新傷害醫療給付', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1P', N'健康人生綜合住院醫療給付保險附約', N'1P', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'健康人生綜合住院', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1Q', N'金安心住院醫療健康保險附約', N'1Q', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'金安心住院醫療', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1S', N'金安康傷害醫療保險附約', N'1S', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'金安康傷害醫療保險附約', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'1U', N'新安康傷害保險附約', N'1U', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新安康傷害險附約', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'28', N'終身防癌健康保險', N'28', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'終身防癌健康保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'39', N'新終身防癌健康保險', N'39', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新終身防癌健康保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'BM', N'關懷一生終身防癌健康保險', N'BM', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'關懷一生終身防癌健康保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'CM', N'新關懷一生終身防癌健康保險', N'CM', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新關懷一生終身防癌健康險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'DN', N'新關懷一生終身防癌健康保險（101）', N'DN', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新關懷一生終身防癌健康保險（101）', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'FM', N'新關懷一生終身防癌健康保險（109）', N'FM', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新關懷一生終身防癌健康保險（109）', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'GM', N'新關懷一生終身防癌健康保險(109A)', N'GM', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新關懷一生防癌險(109A)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'49', N'新終身醫療健康保險', N'49', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新終身醫療健康保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'CE', N'新終身醫療健康保險(98)', N'CE', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新終身醫療健康保險(98)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'BR', N'醫定好健康還本保險', N'BR', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'醫定好健康還本保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'CB', N'新醫定好健康還本保險', N'CB', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'新醫定好健康還本保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'CR', N'倍得康終身醫療健康保險', N'CR', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'倍得康終身醫療', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'DT', N'倍得康終身醫療健康保險（101）', N'DT', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'倍得康終身醫療（101）', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'EG', N'松柏長青終身醫療健康保險', N'EG', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'松柏長青終身醫療', 1, N'N', NULL, getdate(), N'admin', CAST(N'2020-12-15' AS Date), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'ET', N'松柏長青終身醫療健康保險(106)', N'ET', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'松柏長青終身醫療(106)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'FT', N'松柏長青終身醫療健康保險(109)', N'FT', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'松柏長青終身醫療(109)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'GH', N'松柏長青終身醫療健康保險(109A)', N'GH', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'松柏長青終身醫療(109A)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'EJ', N'守護久久手術醫療終身健康保險', N'EJ', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'守護久久手術醫療終身保險', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'EU', N'守護久久手術醫療終身健康保險(106)', N'EU', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'守護久久手術醫療(106）', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'FU', N'守護久久手術醫療終身健康保險(109)', N'FU', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'守護久久手術醫療(109）', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'GJ', N'守護久久手術醫療終身健康保險(109A)', N'GJ', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'守護久久手術醫療(109A)', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'73', N'保單理賠醫起通73保單開始的', N'73', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'保單理賠醫起通73保單開始的', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go

INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'60', N'保單理賠醫起通60保單開始的', N'60', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_TREATMENT_ITEMS'), NULL, N'保單理賠醫起通60保單開始的', 1, N'N', NULL, getdate(), N'admin', getdate(), N'admin')
go
----保單理賠醫起通
INSERT ESERVICE_ADM.dbo.PARAMETER
    (
	PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER
	)
VALUES
   (
   (select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER)
	, N'eservice'
	, N'MEDICAL_TREATMENT'
	, N'保單理賠醫起通'
	, N'MEDICAL_TREATMENT'
	, (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='ONLINE_CHANGE')
	, NULL
	, NULL
	, 1
	, NULL
	, NULL
	, getdate()
	, N'system'
	, NULL
	, NULL
	)
go


INSERT ESERVICE_ADM.dbo.PARAMETER
    (
	PARAMETER_ID
	, SYSTEM_ID
	, PARAMETER_CODE
	, PARAMETER_NAME
	, PARAMETER_VALUE
	, PARAMETER_CATEGORY_ID
	, SORT_NO
	, REMARK
	, STATUS
	, ENCRYPT_TYPE
	, PARENT_PARAMETER_ID
	, CREATE_DATE
	, CREATE_USER
	, UPDATE_DATE
	, UPDATE_USER
	)
VALUES
   (
   (select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER)
	, N'eservice'
	, N'ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL'
	, N'ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL'
	, N'http://10.7.168.104:9080/DecisionService/rest/eserviceChecker/InsuranceClaimPolicyClaim2'
	, (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='SYSTEM_CONSTANTS')
	, NULL
	, NULL
	, 1
	, NULL
	, NULL
	, getdate()
	, N'system'
	, NULL
	, NULL
	)
go


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'MEDICAL_BLACKLIST_ALERT01', N'MEDICAL_BLACKLIST_ALERT01', N'${NAME} 保戶會員您好：</br>因您於${TRANS_CREATEDATE}申請的理賠醫起通聯盟鏈申請(案件${TRANS_NUM})理賠，因${MEDICAL_ABNORMAL_MESSAGE}，依理賠醫起通聯盟鏈規範將限制您未來再次使用本服務。</br>', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='SYSTEM_MSG_PARAMETER'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL)
go


INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'MEDICAL_BIRDATE_WINDOW_MSG', N'生日提示信息', N'輸入的生日與系統默認的生日不一致', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='MEDICAL_WINDOW_MSG'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
GO

--MEDICAL_TREATMENT
INSERT ESERVICE_ADM.dbo.PARAMETER (PARAMETER_ID, SYSTEM_ID, PARAMETER_CODE, PARAMETER_NAME, PARAMETER_VALUE, PARAMETER_CATEGORY_ID, SORT_NO, REMARK, STATUS, ENCRYPT_TYPE, PARENT_PARAMETER_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER)
VALUES ((select max(parameter_id)+1 from ESERVICE_ADM.dbo.PARAMETER), N'eservice', N'WORDING_MEDICAL_TREATMENT_02603', N'保險理賠醫起通-申請成功', N'<p>系統將於每日下午4:00處理您送出的申請。<br />「保險理賠醫起通」約需1個工作天進行作業，您可隨時至「我的申請紀錄」掌握最新處理進度。</p>', (select PARAMETER_CATEGORY_ID from ESERVICE_ADM.dbo.PARAMETER_CATEGORY where CATEGORY_CODE='PAGE_WORDING'), NULL, NULL, 1, NULL, NULL, getdate(), N'admin', NULL, NULL);
GO

