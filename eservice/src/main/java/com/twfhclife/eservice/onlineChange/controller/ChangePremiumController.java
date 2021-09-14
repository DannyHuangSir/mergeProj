package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseUserDataController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChangePremiumController extends BaseUserDataController  {

    @RequestLog
    @GetMapping("/changePremium1")
    public String changePremium1() {
        return "frontstage/onlineChange/changePremium/changePremium1";
    }

    @RequestLog
    @GetMapping("/changePremium2")
    public String changePremium2() {
        return "frontstage/onlineChange/changePremium/changePremium2";
    }

    @RequestLog
    @GetMapping("/changePremium3")
    public String changePremium3() {
        return "frontstage/onlineChange/changePremium/changePremium3";
    }

    @RequestLog
    @GetMapping("/changePremiumSuccess")
    public String changePremiumSuccess() {
        return "frontstage/onlineChange/changePremium/changePremium-success";
    }
}
