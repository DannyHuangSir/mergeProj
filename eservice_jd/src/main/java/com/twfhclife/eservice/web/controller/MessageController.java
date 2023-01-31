package com.twfhclife.eservice.web.controller;

import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.service.IMessageService;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired
    private IMessageService messageService;

    @GetMapping("/message")
    public ResponseObj message() {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        responseObj.setResultData(messageService.getMessages(getUserId()));
        return responseObj;
    }
}