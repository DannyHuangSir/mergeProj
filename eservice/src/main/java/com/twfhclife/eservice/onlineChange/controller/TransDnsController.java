package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.model.TransDnsVo;
import com.twfhclife.eservice.onlineChange.service.ITransDnsService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseUserDataController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hui.chen
 * @create 2021-07-16
 */
@Controller
public class TransDnsController  extends BaseUserDataController {

    private static final Logger logger = LogManager.getLogger(TransCertPrintController.class);



    @Autowired
    private ITransDnsService transDnsService;

    /**
     * 取得申請死亡除户明細資料.
     *
     * @param transNum 申請序號
     * @return
     */
    @RequestLog
    @PostMapping("/getTransDnsDetail")
    public String getTransDnsDetail(@RequestParam("transNum") String transNum) {
        try {
            TransDnsVo   transDnsDetail=transDnsService.getTransServiceDnsDetail(transNum);
            logger.info("getTransDnsDetail is: {}", transDnsDetail.toString());

            addAttribute("transDnsDetail", transDnsDetail);
        }catch (Exception e){
            logger.error("Unable to getTransDnsDetail: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }

        return "frontstage/onlineChange/changeInfo/change-info-dns-detail";
    }


}
