package com.twfhclife.adm.controller.jd;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.model.RoleVo;
import com.twfhclife.adm.service.IJdRoleService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Controller
public class JdRoleController extends BaseController {

    private static final Logger logger = LogManager.getLogger(JdRoleController.class);

    @Autowired
    private IJdRoleService roleService;

    /**
     * 使用者角色管理頁面程式進入點.
     *
     * @return
     */
    @RequestLog
    @LoginCheck(value = @FuncUsageParam(
            funcId = "698",
            systemId = ApConstants.SYSTEM_ID
    ))
    @GetMapping("/jdRole")
    public String jdRole() {
        return "backstage/jd/role";
    }

    /**
     * 取得使用者角色管理查詢結果.
     *
     * @return
     */
    @RequestLog
    @PostMapping("/jdRole/getRoleDetail")
    public ResponseEntity<PageResponseObj> getRoleDetail(@RequestBody RoleVo vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            String userName = getUserId();
            String keyCloakUserId = getLoginUser().getId();

            // Note: RoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            pageResp.setRows(roleService.getRolePageList(vo, userName, keyCloakUserId));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(roleService.getRolePageTotal(vo, userName, keyCloakUserId));
            // 設定jqGrid 目前頁數
            pageResp.setPage(vo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getRoleDetail: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    /**
     * 使用者角色管理-新增.
     *
     * @param roleVo RoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdRole/insertRole")
    public ResponseEntity<ResponseObj> insertRole(@RequestBody RoleVo roleVo) {
        try {
            roleVo.setCreateUser(getUserId());
            roleVo.setModifyUser(getUserId());
            //如果角色代碼存在,不允許新增
            if (roleService.countRoleDep(roleVo.getDivRoleId(),roleVo.getDepartmentId())>=1){
                processSuccessMsg("通路下角色代碼已存在");
            }else {
                int result = roleService.insertRole(roleVo);
                if (result > 0) {
                    processSuccessMsg("新增成功");
                } else {
                    processError("新增失敗");
                }
            }
        } catch (Exception e) {
            logger.error("Unable to insertRole: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 使用者角色管理-更新.
     *
     * @param roleVo RoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdRole/updateRole")
    public ResponseEntity<ResponseObj> updateRole(@RequestBody RoleVo roleVo) {
        try {
            roleVo.setModifyUser(getUserId());
            int result = roleService.updateRole(roleVo);
            if (result > 0) {
                processSuccessMsg("更新成功");
            } else {
                processError("更新失敗");
            }
        } catch (Exception e) {
            logger.error("Unable to updateRole: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 使用者角色管理-刪除.
     *
     * @param roleVo RoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdRole/deleteRole")
    public ResponseEntity<ResponseObj> deleteRole(@RequestBody RoleVo roleVo) {
        try {
            int result = roleService.deleteRole(roleVo);
            if (result > 0) {
                processSuccessMsg("刪除成功");
            } else {
                processError("刪除失敗");
            }
        } catch (Exception e) {
            logger.error("Unable to deleteRole: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/jdRole/getNotifyRoles")
    public ResponseEntity<PageResponseObj> getNotifyUsers(@RequestBody NotifySearchVo notifySearchVo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            List<Map<String, Object>> userList = roleService.getNotifyRoles(notifySearchVo);
            pageResp.setRows(userList);
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getNotifyRoles: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    @RequestLog
    @PostMapping("/jdRole/roleByUserList")
    public ResponseEntity<ResponseObj> jdRoleByUserList(@RequestBody RoleVo roleVo){
        try {
            processSuccess(roleService.roleByUserList(roleVo));
        } catch (Exception e) {
            logger.error("Unable to roleByUserList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }
}
