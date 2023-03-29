package com.twfhclife.eservice.web.controller;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.controller.BaseController;
import com.twfhclife.eservice.keycloak.service.KeycloakService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.MessageVo;
import com.twfhclife.eservice.web.service.IMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
        result.put("data", messageService.getMessages(vo, getLoginUser().getId()));
        responseObj.setResultData(result);
        return responseObj;
    }

    @PostMapping("/getNotRead")
    public ResponseObj getNotRead() {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        responseObj.setResultData(messageService.getNotRead(getLoginUser().getId()));
        return responseObj;
    }
    @PostMapping("/readNotifyMsg")
    public ResponseObj readNotifyMsg(@RequestBody MessageVo vo) {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setResult(ResponseObj.SUCCESS);
        responseObj.setResultData(messageService.readMsg(vo.getId()));
        return responseObj;
    }
}