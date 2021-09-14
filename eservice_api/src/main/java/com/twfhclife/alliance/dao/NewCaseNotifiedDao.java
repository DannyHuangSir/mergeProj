package com.twfhclife.alliance.dao;

import org.apache.ibatis.annotations.Param;

public interface NewCaseNotifiedDao {
	
	/**
	 * 
	 * @param caseId
	 * @return int
	 * @throws Exception
	 */
	int addNotifyOfNewCase(@Param("caseId") String caseId,@Param("msg") String msg) throws Exception;
	
}
