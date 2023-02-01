package com.twfhclife.adm.controller.msg;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.JdzqNotifyMsg;
import com.twfhclife.adm.service.IJdzqMsgService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JdzqMsgNotifyController extends BaseController {

    @RequestLog
    @GetMapping("/jdzqNotifyManagement")
    public String jdzqNotifyManagement() {
        return "backstage/msg/jdzqNotify";
    }

    @Autowired
    private IJdzqMsgService jdzqMsgService;
    @RequestLog
    @PostMapping("/jdzqSendMsg")
    @ResponseBody
    public ResponseEntity<ResponseObj> jdzqSendMsg(@RequestBody JdzqNotifyMsg msg) {
        jdzqMsgService.addJdzqMsg(msg);
        return processResponseEntity();
    }
}
