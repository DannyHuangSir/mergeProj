package com.twfhclife.eservice.attitudemail.service;

import com.twfhclife.eservice.web.model.AttitudeMailVo;

public interface IAttitudeMailService {

	/**
	 * 新增意見回復
	 * @param attitudeMailVo
	 */
	public boolean insertAttitudeMail(AttitudeMailVo attitudeMailVo);
}
