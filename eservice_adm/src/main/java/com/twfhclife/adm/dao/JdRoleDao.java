package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.model.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Mapper
public interface JdRoleDao {

    /**
     * 取得使用者角色管理查詢結果.
     *
     * @param roleVo RoleVo
     * @param keyCloakUserId keyCloak user UUID
     * @param adminUserFlag 最高管理員權限註記
     * @return 回傳使用者角色管理查詢結果
     */
    List<Map<String, Object>> getRolePageList(@Param("roleVo") RoleVo roleVo,
                                              @Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);

    /**
     * 取得使用者角色管理查詢結果總筆數.
     *
     * @param roleVo RoleVo
     * @param keyCloakUserId keyCloak user UUID
     * @param adminUserFlag 最高管理員權限註記
     * @return 回傳總筆數
     */
    int getRolePageTotal(@Param("roleVo") RoleVo roleVo, @Param("keyCloakUserId") String keyCloakUserId,
                         @Param("adminUserFlag") String adminUserFlag);

    /**
     * 使用者角色管理-查詢.
     *
     * @param roleVo RoleVo
     * @param keyCloakUserId keyCloak user UUID
     * @param adminUserFlag 最高管理員權限註記
     * @return 回傳查詢結果
     */
    List<RoleVo> getRole(@Param("roleVo") RoleVo roleVo,
                         @Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);


    /**
     * 使用者角色管理-依照使用者權限查詢.
     *
     * @param keyCloakUserId keyCloak user UUID
     * @param adminUserFlag 最高管理員權限註記
     * @return 回傳查詢結果
     */
    List<RoleVo> getRoleByAuth(@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag);

    /**
     * 使用者角色管理-新增.
     *
     * @param roleVo RoleVo
     * @return 回傳影響筆數
     */
    int insertRole(@Param("roleVo") RoleVo roleVo);

    /**
     * 使用者角色管理-更新.
     *
     * @param roleVo RoleVo
     * @return 回傳影響筆數
     */
    int updateRole(@Param("roleVo") RoleVo roleVo);

    /**
     * 使用者角色管理-刪除.
     *
     * @param roleVo RoleVo
     * @return 回傳影響筆數
     */
    int deleteRole(@Param("roleVo") RoleVo roleVo);

    int updateRoleDep(@Param("roleVo") RoleVo roleVo);

    int deleteRoleDep(@Param("roleVo") RoleVo roleVo);

    List<Map<String, Object>> getNotifyRoles(@Param("vo") NotifySearchVo notifySearchVo);

//    int deleteRoleSysAuth(@Param("roleVo") RoleVo roleVo);

    RoleVo getRoleId(@Param("roleId") String roleId,@Param("depId")String depId);

    List<RoleVo> getDepRole(@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag,@Param("depId")String depId);

    List<RoleVo> getOptionDepRole(@Param("keyCloakUserId") String keyCloakUserId, @Param("adminUserFlag") String adminUserFlag,@Param("depId")String depId);

    int insertUserRole(@Param("userId")String userId,@Param("roleId")String roleId);

    int updateUserRole(@Param("userId")String userId,@Param("roleId")String roleId);

    int countRoleDep(@Param("roleId") String roleId,@Param("depId") String depId);

    int countUserRole(@Param("roleId") String roleId,@Param("userId") String userId);

    List<RoleVo> roleByUserList(@Param("roleVo")RoleVo roleVo);
}
