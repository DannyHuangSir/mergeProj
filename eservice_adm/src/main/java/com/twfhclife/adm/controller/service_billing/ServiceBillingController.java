package com.twfhclife.adm.controller.service_billing;

import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@EnableAutoConfiguration
public class ServiceBillingController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ServiceBillingController.class);

    @RequestLog
    @GetMapping("/serviceBillingDetail")
    public String serviceBillingDetail() {
        return "backstage/serviceBilling/serviceBilling-detail";
    }
}
