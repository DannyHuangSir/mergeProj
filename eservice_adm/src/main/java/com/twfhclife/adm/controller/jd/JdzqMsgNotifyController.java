package com.twfhclife.adm.controller.jd;

import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.model.NotifySearchVo;
import com.twfhclife.adm.service.IJdzqMsgService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class JdzqMsgNotifyController extends BaseController {


    private static final Logger logger = LogManager.getLogger(JdzqMsgNotifyController.class);

    @RequestLog
    @GetMapping("/jdzqNotifyManagement")
    public String jdzqNotifyManagement() {
        return "backstage/msg/jdzqNotify";
    }

    @RequestLog
    @GetMapping("/jdzqNotifyManagementDetail")
    public String jdzqNotifyManagementDetail() {
        return "backstage/msg/jdzqNotifyManagementDetail";
    }


    @Autowired
    private IJdzqMsgService jdzqMsgService;
    @RequestLog
    @PostMapping("/addJdzqMsgSchedule")
    @ResponseBody
    public ResponseEntity<ResponseObj> addJdzqMsgSchedule(@RequestBody JdzqNotifyMsg msg) {
        jdzqMsgService.addJdzqMsgSchedule(msg);
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping("/getJdzqMsgSchedule")
    public ResponseEntity<PageResponseObj> getJdzqMsgSchedule(@RequestBody NotifySearchVo vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            List<Map<String, Object>> msgScheduleList = jdzqMsgService.getMsgSchedule(vo);
            pageResp.setRows(msgScheduleList);
            pageResp.setRecords(jdzqMsgService.getMsgScheduleTotal(vo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(vo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getJdzqMsgSchedule: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    @RequestLog
    @PostMapping("/deleteNotifyMsg")
    public ResponseEntity<ResponseObj> deleteNotifyMsg(@RequestBody JdzqNotifyMsg vo) {
        try {
            jdzqMsgService.deleteNotifyMsg(vo.getId());
        } catch (Exception e) {
            logger.error("Unable to deleteNotifyMsg: {}", ExceptionUtils.getStackTrace(e));
        }
        processSuccessMsg("刪除成功！");
        return processResponseEntity();
    }
}
