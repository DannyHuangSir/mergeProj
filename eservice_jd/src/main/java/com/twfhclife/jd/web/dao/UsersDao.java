package com.twfhclife.jd.web.dao;

import com.twfhclife.jd.web.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersDao {

	public UsersVo getUsers();
	
	public UsersVo getUserByAccount(@Param("account") String account);
	
	public void updatePassword(@Param("account") String account, @Param("enable") String enable);
	
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

	List<PermQueryVo> getCaseQueryByIc(@Param("userId") String id, @Param("parentDep") String parentDep, @Param("branchId") String branchId);

	List<PermQueryVo> getCaseQueryByAdministrator(@Param("parentDep") String parentDep, @Param("branchId") String branchId);
	/**
	 * 搜尋保單(角色:通路主管)
	 * branchId 為空則搜尋該通路所有分支機構
	 * @param id
	 * @param branchId 分支機構
	 * @return
	 */
	List<PermQueryVo> getCaseQueryByPassageWay(@Param("userId") String id, @Param("branchId") String branchId);

	UsersVo getUserBySaleId(@Param("saleId") String saleId, @Param("agentCode") String agentCode, @Param("agentBranchM") String agentBranchM);
	List<UsersVo> getBranchLeaders(@Param("agentCode") String agentCode, @Param("agentBranchM") String agentBranchM, @Param("branchCode") String branchCode);

	/**
	 * 獲取通路數組
	 * @param userId
	 * @param role
	 * @return
	 */
	List<DepartmentVo> getDeptParentList(@Param("userId") String userId, @Param("role") Integer role);
}
