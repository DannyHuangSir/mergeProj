package com.twfhclife.jd.web.dao;

import com.twfhclife.jd.web.model.DepartmentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OptionDao {


	List<Map<String, Object>> getPolicyTyps();

	List<Map<String, Object>> getLevelStates();

	List<Map<String, Object>> getPayModeList();

    List<Map<String, Object>> getTransactionCodeList(@Param("categoryCode") String categoryCode);

	List<Map<String, String>> getBankList();

	List<DepartmentVo> getBranchList(@Param("userId") String userId, @Param("vo") DepartmentVo vo, @Param("selectAllflag") boolean selectAllflag, @Param("roles") List<DepartmentVo> roles);

	List<DepartmentVo> getICUserBranchIDByRole(@Param("userId") String userId,@Param("vo") DepartmentVo vo);
}
