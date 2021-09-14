package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseUserDataController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttributeController extends BaseUserDataController  {

    @RequestLog
    @GetMapping("/attribute1")
    public String attribute1() {
        return "frontstage/onlineChange/attribute/attribute1";
    }

    @RequestLog
    @GetMapping("/attribute2")
    public String attribute2() {
        return "frontstage/onlineChange/attribute/attribute2";
    }

    @RequestLog
    @GetMapping("/attributeSuccess")
    public String attributeSuccess() {
        return "frontstage/onlineChange/attribute/attribute-success";
    }
}
