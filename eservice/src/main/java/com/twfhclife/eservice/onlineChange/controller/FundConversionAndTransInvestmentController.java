package com.twfhclife.eservice.onlineChange.controller;

import com.twfhclife.eservice.onlineChange.service.ITransInvestmentService;
import com.twfhclife.eservice.policy.model.InvestmentPortfolioVo;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseUserDataController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hui.chen
 * @create 2021-08-29
 */
@Controller
public class FundConversionAndTransInvestmentController  extends BaseUserDataController {
    private static final Logger logger = LogManager.getLogger(FundConversionController.class);

    @Autowired
    private ITransInvestmentService transInvestmentService;

    /**
     *  未來保費投資標的與分配比例  與  已持有投資標的轉換  公共比例接口
     * @param vo
     * @return
     */
    @RequestLog
    @PostMapping("/investmentRatioSizeLimit")
    @ResponseBody
    public ResponseEntity<ResponseObj> getNewInvestments(@RequestBody InvestmentPortfolioVo vo) {
         try {
             InvestmentPortfolioVo investments = transInvestmentService.getInvestmentLimitSize(vo.getPolicyNo(),vo.getPolicyType());
            processSuccess(investments);
         }catch (Exception  ex){
             logger.error("--------Unable to init from FundConversionAndTransInvestmentController - investmentRatioSizeLimit--------: {}", ExceptionUtils.getStackTrace(ex));
             addDefaultSystemError();
         }
        return processResponseEntity();
    }
}
