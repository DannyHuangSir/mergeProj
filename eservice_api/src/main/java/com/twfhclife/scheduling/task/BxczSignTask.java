package com.twfhclife.scheduling.task;

import com.google.common.collect.Maps;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice.auth.dao.BxczDao;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import com.twfhclife.eservice.onlineChange.service.impl.BxczSignServiceImpl;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.utils.ApConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BxczSignTask {

    Log log = LogFactory.getLog(BxczSignTask.class);
    Logger logger = LoggerFactory.getLogger(BxczSignTask.class);

    @Value("${eservice.bxcz.417.url}")
    private String api417Url;
    @Value("${cron.api417.expression.enable: true}")
    public boolean api417Enable;

    @Value("${eservice.bxcz.login.client_id}")
    private String clientId;
    @Value("${eservice.bxcz.login.client_secret}")
    private String clientSecret;
    @Value("${eservice.bxcz.416.url}")
    private String api416Url;

    @Autowired
    private BxczDao bxczDao;

    @Autowired
    private BxczSignServiceImpl bxczSignService;

    public String API_DISABLE;

    @Autowired
    private IParameterService parameterService;


    @PostConstruct
    public void doAllianceServiceTask() {
        List<ParameterVo> resultSCHList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_PRO_SCH);
        List<ParameterVo> resultBASELList = parameterService.getParameterByCategoryCode(ApConstants.SYSTEM_ID, ApConstants.SYS_PRO_BASE_URL);
        if (resultBASELList != null) {
            resultBASELList.forEach(parameterItem -> {
                if ("alliance.api.accessToken".equals(parameterItem.getParameterName())) {
                    bxczSignService.setACCESS_TOKEN(parameterItem.getParameterValue());
                }
            });
        }

        if (resultSCHList != null) {
            resultSCHList.forEach(parameterItem -> {
                if ("bxcz.cron.api.disable".equals(parameterItem.getParameterName())) {
                    this.setAPI_DISABLE(parameterItem.getParameterValue());
                } else {
                    if (System.getProperty(parameterItem.getParameterName()) == null) {
                        logger.info("set parameter: {}, {}", parameterItem.getParameterName(), parameterItem.getParameterValue());
                        System.setProperty(parameterItem.getParameterName(), parameterItem.getParameterValue());
                    }
                }
            });
        }
    }

    @Scheduled(cron = "${bxcz.cron.api417.expression}")
    public void callApi417() {
        if (!api417Enable) {
            return;
        }

        log.info("Start call api417.");
        log.info("API_DISABLE=" + API_DISABLE);
        if ("N".equals(API_DISABLE)) {
            List<SignRecord> fileIds = bxczSignService.getNotDownloadSignFile();
            if (CollectionUtils.isNotEmpty(fileIds)) {
                fileIds.forEach(s -> {
                    Map<String, String> params = Maps.newHashMap();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Access-Token", clientSecret);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    try {
                        Date startTime = new Date();
                        String fileBase64 = bxczSignService.postForJson(api417Url, headers, params);
                        if (CollectionUtils.isNotEmpty(fileIds)) {
                            bxczSignService.updateSignDownloaded(s.getActionId());
                            bxczSignService.addSignFileData(s.getSignFileId(), clientId, fileBase64);
                        }
                        BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL", "下載檔案", "0", "", s.getActionId(), s.getTransNum(), startTime, new Date());
                        bxczDao.addSignApiLog(bxczSignApiLog);
                    } catch (Exception e) {
                        logger.error("call api417 error: {}, {}, {}", headers, params, e);
                    }
                });
            }
        }
        log.info("End call api417.");
    }

    public String getAPI_DISABLE() {
        return API_DISABLE;
    }

    public void setAPI_DISABLE(String API_DISABLE) {
        this.API_DISABLE = API_DISABLE;
    }
}
