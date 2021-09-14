package com.twfhclife.generic.util;

/**
 * 業務事件代碼.
 * 
 * @author alan
 */
public interface EventCodeConstants {
	
	/** 登入記錄查詢 */
	public static final String LOGIN_RECORD = "RPTQRY_001";
	public static final String LOGIN_RECORD_SQL_ID = "com.twfhclife.adm.dao.LoginLogDao.getLoginRecordDetail";
	
	/** 通知記錄查詢 */
	public static final String COMMLOG_001 = "COMMLOG_001";
	public static final String COMMLOG_001_SQL_ID = "com.twfhclife.adm.dao.CommLogDao.getCommLogDetail";
	
	/** 線上申請查詢 */
	public static final String ONLINECHANGE_001 = "ONLINECHANGE_001";
	public static final String ONLINECHANGE_001_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getOnlineChangeDetail";
	/** 繳別-明細查詢 */
	public static final String ONLINECHANGE_002 = "ONLINECHANGE_002";
	public static final String ONLINECHANGE_002_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransPaymode";
	/** 年金給付方式-明細查詢 */
	public static final String ONLINECHANGE_003 = "ONLINECHANGE_003";
	public static final String ONLINECHANGE_003_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransAnnuityMethod";
	/** 紅利選擇權-明細查詢 */
	public static final String ONLINECHANGE_004 = "ONLINECHANGE_004";
	public static final String ONLINECHANGE_004_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransBonus";
	/** 增值回饋分享金領取方式-明細查詢 */
	public static final String ONLINECHANGE_005 = "ONLINECHANGE_005";
	public static final String ONLINECHANGE_005_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransReward";
	/** 自動墊繳選擇權-明細查詢 */
	public static final String ONLINECHANGE_006 = "ONLINECHANGE_006";
	public static final String ONLINECHANGE_006_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransCushion";
	/** 變更受益人-明細查詢 */
	public static final String ONLINECHANGE_007 = "ONLINECHANGE_007";
	public static final String ONLINECHANGE_007_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransBeneficiary";
	/** 展期-明細查詢 */
	public static final String ONLINECHANGE_008 = "ONLINECHANGE_008";
	public static final String ONLINECHANGE_008_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransRenew";
	/** 減額-明細查詢 */
	public static final String ONLINECHANGE_009 = "ONLINECHANGE_009";
	public static final String ONLINECHANGE_009_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransReduce";
	/** 申請減少保險金額-明細查詢 */
	public static final String ONLINECHANGE_010 = "ONLINECHANGE_010";
	public static final String ONLINECHANGE_010_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransReducePolicy";
	/** 保單聯絡資料-明細查詢 */
	public static final String ONLINECHANGE_011 = "ONLINECHANGE_011";
	public static final String ONLINECHANGE_011_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransContactInfo";
	/** 設定停損停利通知-明細查詢 */
	public static final String ONLINECHANGE_012 = "ONLINECHANGE_012";
	public static final String ONLINECHANGE_012_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransFundNotification";
	/** 收費管道-明細查詢 */
	public static final String ONLINECHANGE_013 = "ONLINECHANGE_013";
	public static final String ONLINECHANGE_013_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransPayMethod";
	/** 信用卡效期-明細查詢 */
	public static final String ONLINECHANGE_014 = "ONLINECHANGE_014";
	public static final String ONLINECHANGE_014_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransCreditCardInfo";
	/** 解約-明細查詢 */
	public static final String ONLINECHANGE_015 = "ONLINECHANGE_015";
	public static final String ONLINECHANGE_015_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransCancelContract";
	/** 紅利提領列印-明細查詢 */
	public static final String ONLINECHANGE_016 = "ONLINECHANGE_016";
	public static final String ONLINECHANGE_016_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransSurrender";
	/** 申請保單貸款-明細查詢 */
	public static final String ONLINECHANGE_017 = "ONLINECHANGE_017";
	public static final String ONLINECHANGE_017_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransLoan";
	/** 基本資料變更-明細查詢 */
	public static final String ONLINECHANGE_018 = "ONLINECHANGE_018";
	public static final String ONLINECHANGE_018_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransLoanCustInfo";
	/** 審核 */
	public static final String ONLINECHANGE_019 = "ONLINECHANGE_019";
	public static final String ONLINECHANGE_019_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";
	/** 補件 */
	public static final String ONLINECHANGE_020 = "ONLINECHANGE_020";
	public static final String ONLINECHANGE_020_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";
	/** 滿期-明細查詢 */
	public static final String ONLINECHANGE_021 = "ONLINECHANGE_021";
	public static final String ONLINECHANGE_021_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransMaturity";
	/** 完成 */
	public static final String ONLINECHANGE_022 = "ONLINECHANGE_022";
	public static final String ONLINECHANGE_022_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";
	/** 下載所有上傳檔案 */
	public static final String ONLINECHANGE_023 = "ONLINECHANGE_023";
	public static final String ONLINECHANGE_023_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getAllUploadFile";
	/** 補發表單-明細查詢 */
	public static final String ONLINECHANGE_024 = "ONLINECHNAGE_024";
	public static final String ONLINECHANGE_024_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransResendPolicy";
	/** 終止授權-明細查詢 */
	public static final String ONLINECHANGE_025 = "ONLINECHNAGE_025";
	public static final String ONLINECHANGE_025_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransCancelAuth";
	/** 保單價值列印-明細查詢 */
	public static final String ONLINECHANGE_026 = "ONLINECHANGE_026";
	public static final String ONLINECHANGE_026_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransValuePrint";
	/** 旅平險-明細查詢 */
	public static final String ONLINECHANGE_027 = "ONLINECHANGE_027";
	public static final String ONLINECHANGE_027_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransTravelPolicy";
	/** 變更匯款帳號 */
	public static final String ONLINECHANGE_028 = "ONLINECHANGE_028";
	public static final String ONLINECHANGE_028_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransChangePayAccount";
	
	/**
	 * 保單理賠
	 */
	public static final String ONLINECHANGE_029 = "ONLINECHANGE_029";
	public static final String ONLINECHANGE_029_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransInsuranceClaim";
	
	/** 保單理賠件-已收到所有紙本 */
	public static final String ONLINECHANGE_030 = "ONLINECHANGE_030";
	public static final String ONLINECHANGE_030_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateInsuranceClaimFileReceived";
	
	/** 保單理賠件-是否傳送聯盟鏈 */
	public static final String ONLINECHANGE_031 = "ONLINECHANGE_031";
	public static final String ONLINECHANGE_031_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateInsuranceClaimSendAlliance";

	/** 保單理賠件-是否開始處理 */
	public static final String ONLINECHANGE_032 = "ONLINECHANGE_032";
	public static final String ONLINECHANGE_032_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";
	
	/** 保單理賠件-通知補件 */
	public static final String ONLINECHANGE_033 = "ONLINECHANGE_033";
	public static final String ONLINECHANGE_033_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";

	/** 保單理賠件-狀態歷程 */
	public static final String ONLINECHANGE_034 = "ONLINECHANGE_034";
	public static final String ONLINECHANGE_034_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";
	
	/** 保單理賠件-補件單歷程 */
	public static final String ONLINECHANGE_035 = "ONLINECHANGE_035";
	public static final String ONLINECHANGE_035_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.updateTransStatus";

	/** 醫療保單理賠件-查詢詳情 */
	public static final String ONLINECHANGE_036 = "ONLINECHANGE_036";
	public static final String ONLINECHANGE_036_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getConversionDetail";

	public static final String ONLINECHANGE_037 = "ONLINECHANGE_037";
	public static final String ONLINECHANGE_037_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.selectCompareInvestments";

	public static final String ONLINECHANGE_038 = "ONLINECHANGE_038";
	public static final String ONLINECHANGE_038_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getTransPaymentByTransNum";

	public static final String ONLINECHANGE_039 = "ONLINECHANGE_039";
	public static final String ONLINECHANGE_039_SQL_ID = "com.twfhclife.adm.dao.OnlineChangeDao.getAppliedTransDeposits";

	/** 功能維護-查詢 */
	public static final String FUNCMGT_001 = "FUNCMGT_001";
	public static final String FUNCMGT_001_SQL_ID = "com.twfhclife.adm.dao.FunctionItemDao.getAllFuncBySysId";
	/** 功能維護-新增 */
	public static final String FUNCMGT_002 = "FUNCMGT_002";
	public static final String FUNCMGT_002_SQL_ID = "com.twfhclife.adm.dao.FunctionItemDao.addFunctionItem";
	/** 功能維護-更新 */
	public static final String FUNCMGT_003 = "FUNCMGT_003";
	public static final String FUNCMGT_003_SQL_ID = "com.twfhclife.adm.dao.FunctionItemDao.updateFunctionItem";
	/** 功能維護-刪除 */
	public static final String FUNCMGT_004 = "FUNCMGT_004";
	public static final String FUNCMGT_004_SQL_ID = "com.twfhclife.adm.dao.FunctionItemDao.deleteFunctionItem";
	/** 功能維護-匯出 */
	public static final String FUNCMGT_005 = "FUNCMGT_005";
	public static final String FUNCMGT_005_SQL_ID = "com.twfhclife.adm.dao.FunctionItemDao.getAllFuncBySysId";
	
	/** 角色權限維護-查詢 */
	public static final String ROLEMGNT_001 = "ROLEMGT_001";
	public static final String ROLEMGNT_001_SQL_ID = "com.twfhclife.adm.dao.RoleMgntDao.getRoleMgntPageList";
	
	/** 系統帳號查詢 */
	public static final String PERMISSIONS_SEARCH = "ROLEMGT_003";
	
	/** 報表匯出 */
	public static final String DOWNLOAD_USER_AUTH_CSV = "ROLEMGT_004";
	
	/** 事件記錄查詢 */
	public static final String EVENT_RECORD = "EVENTQRY_001";
	
	/** 參數維護-查詢 */
	public static final String PARAMMGT_001 = "PARAMMGT_001";
	public static final String PARAMMGT_001_SQL_ID = "com.twfhclife.adm.dao.ParameterDao.getParameterPageList";
	/** 參數維護-新增 */
	public static final String PARAMMGT_002 = "PARAMMGT_002";
	public static final String PARAMMGT_002_SQL_ID = "com.twfhclife.adm.dao.ParameterDao.insertParameter";
	/** 參數維護-更新 */
	public static final String PARAMMGT_003 = "PARAMMGT_003";
	public static final String PARAMMGT_003_SQL_ID = "com.twfhclife.adm.dao.ParameterDao.updateParameter";
	/** 參數維護-刪除 */
	public static final String PARAMMGT_004 = "PARAMMGT_004";
	public static final String PARAMMGT_004_SQL_ID = "com.twfhclife.adm.dao.ParameterDao.deleteParameter";
	/** 參數維護-匯出 */
	public static final String PARAMMGT_005 = "PARAMMGT_005";
	public static final String PARAMMGT_005_SQL_ID = "com.twfhclife.adm.dao.ParameterDao.getParameter";
	/** 參數類型維護-查詢 */
	public static final String PARAMMGT_006 = "PARAMMGT_006";
	public static final String PARAMMGT_006_SQL_ID = "com.twfhclife.adm.dao.ParameterCategoryDao.getParameterCategoryPageList";
	/** 參數類型維護-新增 */
	public static final String PARAMMGT_007 = "PARAMMGT_007";
	public static final String PARAMMGT_007_SQL_ID = "com.twfhclife.adm.dao.ParameterCategoryDao.insertParameterCategory";
	/** 參數類型維護-更新 */
	public static final String PARAMMGT_008 = "PARAMMGT_008";
	public static final String PARAMMGT_008_SQL_ID = "com.twfhclife.adm.dao.ParameterCategoryDao.updateParameterCategory";
	/** 參數類型維護-刪除 */
	public static final String PARAMMGT_009 = "PARAMMGT_009";
	public static final String PARAMMGT_009_SQL_ID = "com.twfhclife.adm.dao.ParameterCategoryDao.deleteParameterCategory";
	/** 參數類型維護-匯出 */
	public static final String PARAMMGT_010 = "PARAMMGT_010";
	public static final String PARAMMGT_010_SQL_ID = "com.twfhclife.adm.dao.ParameterCategoryDao.getParameterCategor";
	
	/** 查詢通信模板 */
	public static final String COMMUNICATION_TEMPLATE_SEARCH = "MSGMGT_001";
	
	/** 新增通信模板 */
	public static final String COMMUNICATION_TEMPLATE_ADD = "MSGMGT_002";
	
	/** 部門管理-查詢 */
	public static final String DEPTMGT_001 = "DEPTMGT_001";
	public static final String DEPTMGT_001_SQL_ID = "com.twfhclife.adm.dao.DepartmentDao.getDepts";
	/** 部門管理-新增 */
	public static final String DEPTMGT_002 = "DEPTMGT_002";
	public static final String DEPTMGT_002_SQL_ID = "com.twfhclife.adm.dao.DepartmentDao.addDepartment";
	/** 部門管理-更新 */
	public static final String DEPTMGT_003 = "DEPTMGT_003";
	public static final String DEPTMGT_003_SQL_ID = "com.twfhclife.adm.dao.DepartmentDao.updateDepartment";
	/** 部門管理-刪除 */
	public static final String DEPTMGT_004 = "DEPTMGT_004";
	public static final String DEPTMGT_004_SQL_ID = "com.twfhclife.adm.dao.DepartmentDao.deleteDepartment";

	/** 職位管理-查詢 */
	public static final String JOBTITLEMGT_001 = "JOBTITLEMGT_001";
	public static final String JOBTITLEMGT_001_SQL_ID = "com.twfhclife.adm.dao.JobTitleDao.getJobTitlePageList";
	/** 職位管理-新增 */
	public static final String JOBTITLEMGT_002 = "JOBTITLEMGT_002";
	public static final String JOBTITLEMGT_002_SQL_ID = "com.twfhclife.adm.dao.JobTitleDao.insertJobTitle";
	/** 職位管理-更新 */
	public static final String JOBTITLEMGT_003 = "JOBTITLEMGT_003";
	public static final String JOBTITLEMGT_003_SQL_ID = "com.twfhclife.adm.dao.JobTitleDao.updateJobTitle";
	/** 職位管理-刪除 */
	public static final String JOBTITLEMGT_004 = "JOBTITLEMGT_004";
	public static final String JOBTITLEMGT_004_SQL_ID = "com.twfhclife.adm.dao.JobTitleDao.deleteJobTitle";

}
