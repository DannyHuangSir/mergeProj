package com.twfhclife.alliance.dao;

import com.twfhclife.alliance.model.NotifyOfNewCaseMedicalVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface NotifyOfNewCaseMedicalDao {

	/**
	 * 收到外部通知-告知轉收公司有新除戶案件要處理
	 */
	int addNotifyOfNewCaseMedical(@Param("vo") NotifyOfNewCaseMedicalVo vo) throws Exception;
//ID編號
	Float getNotifyOfNewCaseMedicalSeq() throws Exception;

	/**
	 * 進行查詢  未取得查詢理賠資料
	 * @param statusDefault
	 * @return
	 * @throws Exception
	 */
	List<NotifyOfNewCaseMedicalVo> getNotifyOfNewCaseMedicalByNcStatus(@Param("statusDefault")String statusDefault) throws Exception;

	/**
	 * 進行修改狀態信息與描述
	 * @param vo
	 * @return
	 * @throws Exception
	 */
    int updateNotifyOfNewCaseMedicalNcStatusBySeqId(@Param("vo")NotifyOfNewCaseMedicalVo vo)throws  Exception;
}
