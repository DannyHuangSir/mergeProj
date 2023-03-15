package com.twfhclife.eservice.web.dao;

import com.twfhclife.eservice.web.model.PermQueryVo;
import com.twfhclife.eservice.web.model.UserTermVo;
import com.twfhclife.eservice.web.model.UsersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDao {

	public UsersVo getUsers();
	
	public UsersVo getUserByAccount(@Param("account") String account);
	
	public void updatePassword(@Param("account") String account);
	
	public UsersVo getUserByFbId(@Param("realm") String realm, @Param("fbId") String fbId);
	
	public UsersVo getUserByCardSn(@Param("realm") String realm, @Param("cardSn") String cardSn);
	
	public void createUserTerm(@Param("vo") UserTermVo userTerm);
	
	/**
	 * 變更基本資料.
	 * 
	 * @param usersVo UsersVo
	 * @return 回傳影響筆數
	 */
	int updateCustInfo(@Param("usersVo") UsersVo usersVo);

	public void updateOnlineFlagY(@Param("userId") String userId);

    int incLoginFailCount(@Param("userId") String userId);

	int updateLoginSuccess(@Param("userId") String userId);

	int changePassword(String account);

	int checkUserRole(@Param("userId") String id);

	List<PermQueryVo> getCaseQueryBySupervisor(@Param("userId") String id);

	List<PermQueryVo> getCaseQueryByUser(@Param("userId") String id);

	List<PermQueryVo> getCaseQueryByIc(@Param("userId") String id);

	List<PermQueryVo> getCaseQueryByPassageWay(@Param("userId") String id);
}