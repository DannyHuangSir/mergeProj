package com.twfhclife.alliance.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.NotifyOfNewCaseChangeVo;

public interface NotifyOfNewCaseChangeDao {
	
	/**
	 * 收到外部通知-告知轉收公司有新案件要處理
	 * @param caseId
	 * @param type
	 * @return int
	 * @throws Exception
	 */
	int addNotifyOfNewCaseChange(@Param("caseId") String caseId,@Param("type") String type) throws Exception;
	
	/**
	 * 依ncStatus(ex:0,1...etc.)傳入值取得資料
	 * @param ncStatus
	 * @return ArrayList<NotifyOfNewCaseVo>
	 * @throws Exception
	 */
	ArrayList<NotifyOfNewCaseChangeVo> getNofifyOfNewCaseChangeByNcStatus(@Param("ncStatus") String ncStatus) throws Exception;
	
	/**
	 * 更新NC_STATUS(ex:已取完件update NC_STATUS=1)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateNccStatusBySeqId(@Param("vo") NotifyOfNewCaseChangeVo vo) throws Exception;

}
