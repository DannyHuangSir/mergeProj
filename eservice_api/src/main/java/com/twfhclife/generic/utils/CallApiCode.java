package com.twfhclife.generic.utils;

/**
 * @author hui.chen
 * @create 2021-06-30
 */
public interface CallApiCode {

    /**调用联盟API,调用失败,次数统计*/
    public static final String  CALL_API201_NO_NUMBER="CALL_API201_NO_NUMBER";
    public static final String  CALL_API202_NO_NUMBER="CALL_API202_NO_NUMBER";
    public static final String  CALL_API203_NO_NUMBER="CALL_API203_NO_NUMBER";
    public static final String  CALL_API204_NO_NUMBER="CALL_API204_NO_NUMBER";
    public static final String  CALL_API206_NO_NUMBER="CALL_API206_NO_NUMBER";
    public static final String  CALL_API_SAVE_CIO_TO_ESERVICE_TRANS="CALL_API_SAVE_CIO_TO_ESERVICE_TRANS";
    public static final String  CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS="CALL_API_SAVE_CIO_TO_ESERVICE_UNION_TRANS";

    /**
     * 醫療保單聯盟狀態
     */
    //首家保險公司已發送，待確認保戶於醫院授權情況
    public static final String MEDICAL_INTERFACE_STATUS_FTPS_PQHG = "MEDICAL_INTERFACE_STATUS_FTPS_PQHG";
    //已等待10日未取得保戶授權/授權區間不相符，案件取消
    public static final String MEDICAL_INTERFACE_STATUS_PQHF = "MEDICAL_INTERFACE_STATUS_PQHF";
    //平台已取得保戶於醫院之授權，準備至轉收家公司
    public static final String MEDICAL_INTERFACE_STATUS_PQHS_PTIG = "MEDICAL_INTERFACE_STATUS_PQHS_PTIG";
    //平台已成功CallBack給保險公司(首家/轉收家)，告知取得醫院授權
    public static final String MEDICAL_INTERFACE_STATUS_PQHS_PTIS = "MEDICAL_INTERFACE_STATUS_PQHS_PTIS";
    //保險公司(首家/轉收家)已確認將申請醫療資料查調，待發送至醫院端
    public static final String MEDICAL_INTERFACE_STATUS_ITPS_PTHG = "MEDICAL_INTERFACE_STATUS_ITPS_PTHG";
    //保險公司(首家/轉收家)已確認，不申請此次醫療資料查調
    public static final String MEDICAL_INTERFACE_STATUS_ITPS_END = "MEDICAL_INTERFACE_STATUS_ITPS_END";
    //醫院端已接收到此次查調資訊，待醫院端回覆資料
    public static final String MEDICAL_INTERFACE_STATUS_PTHS = "MEDICAL_INTERFACE_STATUS_PTHS";
    //醫院端已開始傳送資料，但尚未完成所有檔案的傳遞
    public static final String MEDICAL_INTERFACE_STATUS_HTPG = "MEDICAL_INTERFACE_STATUS_HTPG";
    //醫院端已完成所有檔案的傳送，平台準備 CallBack給保險公司(首家/轉收家)
    public static final String MEDICAL_INTERFACE_STATUS_HTPS_PTIG = "MEDICAL_INTERFACE_STATUS_HTPS_PTIG";
    //案件已取得醫療資料，平台CallBack給保險公司成功
    public static final String MEDICAL_INTERFACE_STATUS_HTPS_PTIS = "MEDICAL_INTERFACE_STATUS_HTPS_PTIS";
    //保險公司呼叫平台已成功取得醫療資料
    public static final String MEDICAL_INTERFACE_STATUS_ITPS = "MEDICAL_INTERFACE_STATUS_ITPS";
    //保險公司發送醫療資料重送
    public static final String MEDICAL_INTERFACE_STATUS_ITPR = "MEDICAL_INTERFACE_STATUS_ITPR";
    //流程結束
    public static final String MEDICAL_INTERFACE_STATUS_PQHF_END = "MEDICAL_INTERFACE_STATUS_PQHF_END";
    //已取得檔案
    public static final String    MEDICAL_INTERFACE_HAS_FILE="HAS_FILE";
    //檔案重新取得中
    public static final String    MEDICAL_INTERFACE_RE_FILE="RE_FILE";
    //表示聯盟案件,已經保存到台銀了
    final String SAVE_TRANS = "SAVE_TRANS";
    //死亡除戶通報-Header參數值組
    final  static  String SYS_DNS_HEADER = "SYS_DNS_HEADER";
    //死亡除戶通報-Header參數值
    final  static  String ALLIANCE_API_AUTHORIZATION = "alliance.api.dnsFS62.Authorization";


}
