package com.twfhclife.alliance.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.DnsContentVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseDnsVo;

public interface NotifyOfNewCaseDnsDao {

	/**
	 * 收到外部通知-告知轉收公司有新除戶案件要處理
	 * @param caseId
	 * @param type
	 * @param callId
	 * @return int
	 * @throws Exception
	 */
	int addNotifyOfNewCaseDns(@Param("vo") NotifyOfNewCaseDnsVo vo) throws Exception;
	
	/**
	 * 刪除
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	int deleteNotifyOfNewCaseDnsByCallId(@Param("callId") String callId) throws Exception;
	
	/**
	 * 刪除由DNS201查得的指定數據
	 * @param notifySeqId
	 * @return int
	 * @throws Exception
	 */
	int deleteDnsAllianceByNotifySeqId(@Param("notifySeqId") Float notifySeqId) throws Exception;
	
	/**
	 * 新增查詢案件資訊
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int addDnsAlliance(@Param("vo") DnsContentVo vo) throws Exception;
	
	/**
	 * 依ncStatus(ex:0,1...etc.)傳入值取得資料
	 * @param ncStatus
	 * @return ArrayList<NotifyOfNewCaseVo>
	 * @throws Exception
	 */
	ArrayList<NotifyOfNewCaseDnsVo> getNofifyOfNewCaseDnsByNcStatus(@Param("ncStatus") String ncStatus) throws Exception;
	
	/**
	 * 更新NC_STATUS(ex:已取完件update NC_STATUS=1)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateNcStatusBySeqId(@Param("vo") NotifyOfNewCaseDnsVo vo) throws Exception;
	
	List<DnsContentVo> getTransDnsByTransStatus(@Param("status") String status) throws Exception;

	List<DnsContentVo> getTransDnsByStatus(@Param("status") String status) throws Exception;
	/**
	 * update TRANS_DNS by seqId
	 * @param vo DnsContentVo
	 * @return int
	 * @throws Exception
	 */
	int updateTransDnsSendAllianceBySeqId(@Param("vo") DnsContentVo vo) throws Exception;
	
	/**
	 * 取得未轉存至eservice TRANS/TRANS_DNA的聯盟資料
	 * @param vo
	 * @returnList<DnsContentVo>
	 * @throws Exception
	 */
	List<DnsContentVo> getDnsAllianceByStatus(@Param("vo") DnsContentVo vo) throws Exception;
	
	/**
	 * 以seqId更新DNS_ALLIANCE.STATUS
	 * @param vo DnsContentVo
	 * @return int
	 * @throws Exception
	 */
	int updateDnsAllianceStatusBySeqId(@Param("vo") DnsContentVo vo) throws Exception;
	
	/**
	 * 以CASE_ID查詢筆數
	 * @param caseId
	 * @return int
	 * @throws Exception
	 */
	Integer getDnsAllianceByCaseId(@Param("caseId") String caseId) throws Exception;

	/**
	 * 修改狀態
	 * @param contentVo
	 * @return
	 */
	int updateTransDnsSDetailMessageByTransNum(@Param("vo") DnsContentVo contentVo)throws Exception;

	/**
	 * 查詢
	 * @param s
	 * @return
	 * @throws Exception
	 */
	List<DnsContentVo> getTransDnsByStatusAndFsz1PiSt(@Param("status") String s,@Param("status2") String status2,@Param("fsz1Id") String  fsz1Id,@Param("fsz1Id2") String  fsz1Id2)throws Exception;

	/**
	 * 修改回寫狀態
	 * @param contentVo
	 * @throws Exception
	 */
	int updateTransDnssfsz1PiStByPolicyNo(@Param("vo")  DnsContentVo contentVo)throws Exception;
}
