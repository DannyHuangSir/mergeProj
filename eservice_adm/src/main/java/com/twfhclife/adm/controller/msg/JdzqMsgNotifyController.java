package com.twfhclife.adm.controller.msg;

import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JdzqMsgNotifyController extends BaseController {

    @RequestLog
    @GetMapping("/jdzqNotifyManagement")
    public String jdzqNotifyManagement() {
        return "backstage/msg/jdzqNotify";
    }
}
