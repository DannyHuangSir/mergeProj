package com.twfhclife.adm.controller.auth;

import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther lihao
 */
@RestController
@RequestMapping("/vip/batch/import")
public class ICBatchImportController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ICBatchImportController.class);


    @RequestLog
    @LoginCheck(value=@FuncUsageParam(
            funcId = "695",
            systemId = ApConstants.SYSTEM_ID
    ))
    @GetMapping("/iCBatchImport")
    public String vUserBatchImport() {
        return "backstage/auth/iCBatchImport";
    }
}
