package com.twfhclife.eservice.attitudemail.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.AttitudeMailVo;

public interface AttitudeMailDao {

	/**
	 * 新增意見回復
	 * @param attitudeMailVo
	 */
	public int insertAttitudeMail(@Param("attitudeMailVo") AttitudeMailVo attitudeMailVo);
	
	/**
	 * 取得 ZIP_CODE 郵遞區號
	 * @param attitudeMailVo
	 * @return
	 */
	public String getZipCode(@Param("attitudeMailVo") AttitudeMailVo attitudeMailVo);
}
