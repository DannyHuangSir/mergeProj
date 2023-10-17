package com.twfhclife.adm.controller.service_billing;

import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.Spa402RequestVo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.api_client.ServiceBillingClient;
import com.twfhclife.generic.api_model.Spa401RequestVo;
import com.twfhclife.generic.api_model.Spa401ResponseVo;
import com.twfhclife.generic.controller.BaseController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@EnableAutoConfiguration
public class ServiceBillingController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ServiceBillingController.class);

    @RequestLog
    @GetMapping("/serviceBillingDetail")
    public String serviceBillingDetail() {
        return "backstage/serviceBilling/serviceBilling-detail";
    }

    @Value("${eservice.billing.company.id}")
    private String companyId;

    @Autowired
    private ServiceBillingClient serviceBillingClient;

    @RequestLog
    @PostMapping(value = "/spa401")
    @ResponseBody
    public ResponseEntity<ResponseObj> spa401(@RequestBody Spa401RequestVo vo) {
        try {
            vo.setOrgId(companyId);
            //调整page从0开始
            Integer page = vo.getPage();
            if (page != null && page > 0) {
                vo.setPage(page - 1);
            }
            Spa401ResponseVo resultData = serviceBillingClient.callSpa401(vo);
            resultData.setCurrentPage(page);
            processSuccess(resultData);
        } catch (Exception e) {
            logger.error("Unable to ServiceBillingController  -  spa401: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return processResponseEntity();
    }

    @RequestLog
    @PostMapping(value = "/spa402")
    @ResponseBody
    public ResponseEntity<ResponseObj> spa402(@RequestBody Spa402RequestVo vo) {
        try {
            processSuccessMsg(serviceBillingClient.callSpa402(vo));
        } catch (Exception e) {
            logger.error("Unable to ServiceBillingController  -  spa402: {}", ExceptionUtils.getStackTrace(e));
            addDefaultSystemError();
        }
        return processResponseEntity();
    }

}
