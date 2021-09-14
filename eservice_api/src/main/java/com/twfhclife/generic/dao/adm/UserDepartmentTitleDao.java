package com.twfhclife.generic.dao.adm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.adm.model.DepartmentVo;
import com.twfhclife.eservice.api.adm.model.JobTitleVo;
import com.twfhclife.generic.model.UserVo;

public interface UserDepartmentTitleDao {

	public List<DepartmentVo> getDepartmentsByUser(@Param("userId") String userId);

	public List<DepartmentVo> getDepartmentsAll();

	public List<JobTitleVo> getTitlesAll();

	public List<UserVo> getUserByDepTitle(@Param("realm") String realm, @Param("depId") String depId,
			@Param("titleId") String titleId, @Param("userId") String userId);

}
