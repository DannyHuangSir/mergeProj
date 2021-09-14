package com.twfhclife.adm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.adm.domain.DownloadUserAuthCSVResponse;

/**
 * Authorization DAO.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public interface AuthorizationDao {

	/**
	 * Access data base to download user-authorization CSV by system.
	 * 
	 * @param sysId
	 * @return List<DownloadUserAuthCSVResponse>
	 * @throws Exception
	 */
	List<DownloadUserAuthCSVResponse> downloadUserAuthCSV(@Param("sysId") String sysId);
}