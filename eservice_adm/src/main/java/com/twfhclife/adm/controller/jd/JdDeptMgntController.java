package com.twfhclife.adm.controller.jd;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.service.IJdDeptMgntService;
import com.twfhclife.generic.annotation.*;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.EventCodeConstants;
import com.twfhclife.generic.util.MenuUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @auther lihao
 */
@Controller
@EnableAutoConfiguration
public class JdDeptMgntController extends BaseController {

    private static final Logger logger = LogManager.getLogger(JdDeptMgntController.class);

    @Autowired
    private IJdDeptMgntService deptMgntService;

    /**
     * 部門管理查詢頁面程式進入點.
     *
     * @return
     */
    @RequestLog
    @LoginCheck(value = @FuncUsageParam(
            funcId = "697",
            systemId = ApConstants.SYSTEM_ID
    ))
    @GetMapping("/jdDeptMgnt")
    public String funcMgnt() {
        return "backstage/jd/deptMgnt";
    }

    /**
     * 取得部門清單.
     *
     * @return
     */
    @RequestLog
    @PostMapping("/jdDeptMgnt/getDepts")
    public ResponseEntity<ResponseObj> getDepts() {
        try {
            List<DepartmentVo> dataList = deptMgntService.getDepts();
            if (!CollectionUtils.isEmpty(dataList)) {
                processSuccess(new MenuUtil().convertDeptAceTree(dataList));
            } else {
                logger.info("{}", "Unable to get all departments");
                processSuccessMsg("請先新增根目錄");
            }
        } catch (Exception e) {
            logger.error("Unable to geDepts: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/jdDeptMgnt/getDepartment")
    public ResponseEntity<PageResponseObj> getDepartment(@RequestBody NotifySearchVo vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            pageResp.setRows(deptMgntService.getNotifyDepts(vo.getPassageWay()));
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            logger.error("Unable to getDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    /**
     * 取得上一層部門清單.
     *
     * @return
     */
    @RequestLog
    @PostMapping("/jdDeptMgnt/getParentDepList")
    public ResponseEntity<ResponseObj> getParentDepList(@RequestBody DepartmentVo departmentVo) {
        try {
            processSuccess(deptMgntService.getParentDepList(departmentVo));
        } catch (Exception e) {
            logger.error("Unable to getParentDepList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 新增部門.
     *
     * @param departmentVo DepartmentVo
     * @return
     */
    @RequestLog
    @EventRecordLog(value = @EventRecordParam(
            eventCode = EventCodeConstants.DEPTMGT_002,
            sqlId = EventCodeConstants.DEPTMGT_002_SQL_ID,
            daoVoParamKey = "departmentVo",
            systemEventParams = {}
    ))
    @PostMapping("/jdDeptMgnt/insertDepartment")
    public ResponseEntity<ResponseObj> insertDepartment(@RequestBody DepartmentVo departmentVo) {
        try {
            departmentVo.setCreateUser(getUserId());
            departmentVo.setModifyUser(getUserId());
            if (deptMgntService.getDepLevel(departmentVo) > 0) {
                processError("不允許新增第三層分支機構！");
            } else {
                if (deptMgntService.isDeptIdExist(departmentVo)) {
                    processError("部門代碼不可重覆");
                } else {
                    if (deptMgntService.isDeptNameExist(departmentVo)) {
                        processError("部門名稱重覆");
                    } else {
                        if (deptMgntService.isBranchIdExist(departmentVo)) {
                            processError("原機構代碼不可重覆！");
                        } else {
                            int result = deptMgntService.addDepartment(departmentVo);
                            if (result > 0) {
                                processSuccessMsg("新增成功");
                            } else {
                                processError("新增失敗");
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            logger.error("Unable to insertDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 更新部門.
     *
     * @param departmentVo DepartmentVo
     * @return
     */
    @RequestLog
    @EventRecordLog(value = @EventRecordParam(
            eventCode = EventCodeConstants.DEPTMGT_003,
            sqlId = EventCodeConstants.DEPTMGT_003_SQL_ID,
            daoVoParamKey = "departmentVo",
            systemEventParams = {}
    ))
    @PostMapping("/jdDeptMgnt/updateDepartment")
    public ResponseEntity<ResponseObj> updateDepartment(@RequestBody DepartmentVo departmentVo) {
        try {
            departmentVo.setModifyUser(getUserId());
            DepartmentVo vo = deptMgntService.getDepId(departmentVo.getDepId());
            int updateDepartment = deptMgntService.updateDepartment(departmentVo);
            if (deptMgntService.countDeptName(departmentVo.getDepName()) >  1) {
                deptMgntService.updateDepartment(vo);
                processError("通路的部門名稱重複");
            } else {
                if (deptMgntService.countBranchName(departmentVo.getParentDep(), departmentVo.getDepName()) >  1) {
                    deptMgntService.updateDepartment(vo);
                    processError("同通路下的部門名稱重複");
                } else {
                    if (deptMgntService.countBranchId(departmentVo.getParentDep(),departmentVo.getBranchId()) > 1){
                        deptMgntService.updateDepartment(vo);
                        processError("原機構代碼重複");
                    }else {
                        processSuccessMsg("更新成功");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unable to updateDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    /**
     * 刪除部門.
     *
     * @param departmentVo DepartmentVo
     * @return
     */
    @RequestLog
    @EventRecordLog(value = @EventRecordParam(
            eventCode = EventCodeConstants.DEPTMGT_004,
            sqlId = EventCodeConstants.DEPTMGT_004_SQL_ID,
            sqlParams = {
                    @SqlParam(requestParamkey = "depId", sqlParamkey = "depId")
            },
            systemEventParams = {}
    ))
    @PostMapping("/jdDeptMgnt/deleteDepartment")
    public ResponseEntity<ResponseObj> deleteDepartment(@RequestBody DepartmentVo departmentVo) {
        try {
            int result = deptMgntService.deleteDepartment(departmentVo);
            if (result > 0) {
                processSuccessMsg("刪除成功");
            } else {
                processError("刪除失敗");
            }
        } catch (Exception e) {
            logger.error("Unable to deleteDepartment: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/jdDeptMgnt/getPassageWay")
    public ResponseEntity<ResponseObj> getPassageWay() {
        try {
            processSuccess(deptMgntService.getPassageWay());
        } catch (Exception e) {
            logger.error("Unable to getPassageWay: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }
}

