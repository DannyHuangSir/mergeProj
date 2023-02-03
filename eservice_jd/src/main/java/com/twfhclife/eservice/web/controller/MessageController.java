package com.twfhclife.eservice.web.controller;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.service.IMessageService;
import com.twfhclife.eservice.web.service.IPolicyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MessageController extends BaseController {

    private static final Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired
    private IMessageService messageService;
    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("/message")
    public ResponseObj message(@RequestBody MessageVo vo) {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        Map<String, Object> result = Maps.newHashMap();
        result.put("data", messageService.getMessages(vo, keycloakService.getUserByUsername(getUserId()).getId()));
        result.put("count", messageService.getTotalCount(vo, keycloakService.getUserByUsername(getUserId()).getId()));
        responseObj.setResultData(result);
        return responseObj;
    }

    @PostMapping("/getNotRead")
    public ResponseObj getNotRead() {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        Map<String, Object> result = Maps.newHashMap();
        responseObj.setResultData(messageService.getNotRead(keycloakService.getUserByUsername(getUserId()).getId()));
        return responseObj;
    }
}