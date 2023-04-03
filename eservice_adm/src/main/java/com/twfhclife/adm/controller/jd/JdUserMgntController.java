package com.twfhclife.adm.controller.jd;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.*;
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
public class JdUserMgntController extends BaseController {

    private static final Logger logger = LogManager.getLogger(JdUserMgntController.class);

    @Autowired
    private IAuthorizationService authorizationService;

    @Autowired
    private IJdUserMgntService userMgntService;

    @Autowired
    private IJdUserDepartmentService userDepartmentService;

    @Autowired
    private IJdUserRoleService userRoleService;

    @Autowired
    private IUserDeputyService userDeputyService;

    /**
     * 人員管理頁面程式進入點.
     *
     * @return
     */
    @RequestLog
    @LoginCheck(value=@FuncUsageParam(
            funcId = "699",
            systemId = ApConstants.SYSTEM_ID
    ))
    @GetMapping("/jdUserMgnt")
    public String jdUserMgnt() {
        return "backstage/jd/userMgnt";
    }

    /**
     * 取得人員管理查詢結果.
     *
     * @param vo UserEntityVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/getUserEntityPageList")
    public ResponseEntity<PageResponseObj> getUserEntityPageList(@RequestBody UserEntityVo userEntityVo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            // Note: UserEntityVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            userEntityVo.setId(getLoginUser().getId());
            List<Map<String, Object>> userAuthList = userMgntService.getUserEntityPageList(getUserId(), userEntityVo);

            // 設定使用者的系統跟角色權限清單
            authorizationService.setJdUserSystemRoleAuth(userAuthList);

            pageResp.setRows(userAuthList);
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(userMgntService.getUserEntityPageTotal(getUserId(), userEntityVo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(userEntityVo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + userEntityVo.getRows() - 1) / userEntityVo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getUserEntityPageList: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    @RequestLog
    @PostMapping("/jdUserMgnt/getNotifyUsers")
    public ResponseEntity<PageResponseObj> getNotifyUsers(@RequestBody NotifySearchVo userEntityVo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            List<Map<String, Object>> userList = userMgntService.getNotifyUsers(userEntityVo);
            pageResp.setRows(userList);
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getNotifyUsers: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    /**
     * 取得人員部門管理查詢結果.
     *
     * @param vo UserDepartmentVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/getUserDepartmentPageList")
    public ResponseEntity<PageResponseObj> getUserDepartmentPageList(@RequestBody UserDepartmentVo userDepartmentVo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            // Note: UserDepartmentVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            pageResp.setRows(userDepartmentService.getUserDepartmentPageList(userDepartmentVo));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(userDepartmentService.getUserDepartmentPageTotal(userDepartmentVo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(userDepartmentVo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + userDepartmentVo.getRows() - 1) / userDepartmentVo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getUserDepartmentPageList: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    /**
     * 人員部門管理-新增.
     *
     * @param userDepartmentVo UserDepartmentVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/insertUserDepartment")
    public ResponseEntity<ResponseObj> insertUserDepartment(@RequestBody UserDepartmentVo userDepartmentVo) {
        try {
            if (userDepartmentService.countUserDep(userDepartmentVo.getUserId()) >= 1){
                processSuccessMsg("每個人員只允許一個通路和分支機構");
            }else {
                int result = userDepartmentService.insertUserDepartment(userDepartmentVo);
                if (result > 0) {
                    processSuccessMsg("新增成功");
                } else {
                    processError("新增失敗");
                }
            }
        } catch (Exception e) {
            logger.error("Unable to insertUserDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 人員部門管理-刪除.
     *
     * @param userDepartmentVo UserDepartmentVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/deleteUserDepartment")
    public ResponseEntity<ResponseObj> deleteUserDepartment(@RequestBody UserDepartmentVo userDepartmentVo) {
        try {
            int result = userDepartmentService.deleteUserDepartment(userDepartmentVo);
            if (result > 0) {
                processSuccessMsg("刪除成功");
            } else {
                processError("刪除失敗");
            }
        } catch (Exception e) {
            logger.error("Unable to deleteUserDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 取得人員角色管理查詢結果.
     *
     * @param vo UserRoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/getUserRolePageList")
    public ResponseEntity<PageResponseObj> getUserRolePageList(@RequestBody UserRoleVo userRoleVo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            // Note: UserRoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            pageResp.setRows(userRoleService.getUserRolePageList(userRoleVo));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(userRoleService.getUserRolePageTotal(userRoleVo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(userRoleVo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + userRoleVo.getRows() - 1) / userRoleVo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getUserRolePageList: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    /**
     * 人員角色管理-新增.
     *
     * @param userRoleVo UserRoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/insertUserRole")
    public ResponseEntity<ResponseObj> insertUserRole(@RequestBody UserRoleVo userRoleVo) {
        try {
            if (userRoleService.countUserRole(userRoleVo.getUserId()) >= 1) {
                processError("每個人只允許一個角色");
            } else {
                int result = userRoleService.insertUserRole(userRoleVo);
                if (result > 0) {
                    processSuccessMsg("新增成功");
                } else {
                    processError("新增失敗");
                }
            }
        } catch (Exception e) {
            logger.error("Unable to insertUserRole: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 人員角色管理-刪除.
     *
     * @param userRoleVo UserRoleVo
     * @return
     */
    @RequestLog
    @PostMapping("/jdUserMgnt/deleteUserRole")
    public ResponseEntity<ResponseObj> deleteUserRole(@RequestBody UserRoleVo userRoleVo) {
        try {
            int result = userRoleService.deleteUserRole(userRoleVo);
            if (result > 0) {
                processSuccessMsg("刪除成功");
            } else {
                processError("刪除失敗");
            }
        } catch (Exception e) {
            logger.error("Unable to deleteUserRole: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


    @RequestLog
    @PostMapping("/jdUserMgnt/getJdUserQuery")
    public ResponseEntity<PageResponseObj> getJdUserQuery(@RequestBody JdUserVo vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            // Note: UserRoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            pageResp.setRows(userMgntService.getJdUserQuery(vo));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(userMgntService.countJdUser(vo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(vo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getJdUserQuery: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    @RequestLog
    @PostMapping("/jdUserMgnt/getJdUserIcQuery")
    public ResponseEntity<PageResponseObj> getJdUserIcQuery(@RequestBody JdUserVo vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            // Note: UserRoleVo 需要繼承Pagination，接收 jqGrid 的page 跟 rows 屬性
            // 設定jqGrid 資料查詢集合
            pageResp.setRows(userMgntService.getJdUserIcQuery(vo));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(userMgntService.countJdUserIc(vo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(vo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getUserRolePageList: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }
}
