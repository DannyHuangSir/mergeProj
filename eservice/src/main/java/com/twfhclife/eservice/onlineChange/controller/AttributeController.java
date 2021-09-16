package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.service.IAttributeService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseUserDataController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AttributeController extends BaseUserDataController  {

    @Autowired
    private IAttributeService attributeService;

    @RequestLog
    @GetMapping("/attribute1")
    public String attribute1() {
        addAttribute("questions", attributeService.getQuestions());
        return "frontstage/onlineChange/attribute/attribute1";
    }

    @RequestLog
    @PostMapping("/attribute2")
    public String attribute2() {
        return "frontstage/onlineChange/attribute/attribute2";
    }

    @RequestLog
    @GetMapping("/attributeSuccess")
    public String attributeSuccess() {
        return "frontstage/onlineChange/attribute/attribute-success";
    }
}
