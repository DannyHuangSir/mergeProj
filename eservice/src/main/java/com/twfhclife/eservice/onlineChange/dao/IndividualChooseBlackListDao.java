package com.twfhclife.eservice.onlineChange.dao;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.IndividualChooseBlackListVo;

public interface IndividualChooseBlackListDao {

	public IndividualChooseBlackListVo getIndividualChooseBlackList(@Param("ip") String ip );
	
	public int insertIndividualChooseBlackList(@Param("individualChooseBlackListVo") IndividualChooseBlackListVo individualChooseBlackListVo);

	public int updateIndividualChooseBlackList(@Param("individualChooseBlackListVo") IndividualChooseBlackListVo individualChooseBlackListVo);
}
