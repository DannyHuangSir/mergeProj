package com.twfhclife.alliance.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.twfhclife.alliance.model.*;
import com.twfhclife.alliance.service.IExternalService;
import com.twfhclife.eservice.api.adm.domain.MessageTriggerRequestVo;
import com.twfhclife.eservice.onlineChange.model.*;
import com.twfhclife.eservice.onlineChange.service.IBxczSignService;
import com.twfhclife.eservice.onlineChange.service.IInsuranceClaimService;
import com.twfhclife.eservice.onlineChange.service.IMedicalTreatmentService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.web.model.BxczState;
import com.twfhclife.eservice_api.service.IMessagingTemplateService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.utils.AesUtil;
import com.twfhclife.generic.utils.ApConstants;
import com.twfhclife.generic.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BxczSignController {

    private static final Logger logger = LogManager.getLogger(BxczSignController.class);

    @Autowired
    private IBxczSignService bxczSignService;

    @Autowired
    private IExternalService externalService;

    @Autowired
    private IInsuranceClaimService insuranceClaimService;
    @Autowired
    private IMedicalTreatmentService medicalTreatmentService;

    @ApiRequest
    @RequestMapping("/api414")
    public Bxcz414ReturnVo callApi414(
            @RequestBody Bxcz414CallBackVo vo) {

        Date startTime = new Date();

        logger.info("Start BxczSignController.callApi414().");
        Bxcz414ReturnVo ret = new Bxcz414ReturnVo();
        try {
            if (StringUtils.isBlank(vo.getState())) {
                ret.setCode("1");
                ret.setMsg("此活動代碼不存在。");
                return ret;
            }
            SignRecord signRecord = bxczSignService.getSignRecord(vo.getActionId());

            if (signRecord == null) {
                ret.setCode("1");
                ret.setMsg("此活動代碼不存在。");
                return ret;
            }

            BxczState state = new Gson().fromJson(new String(Base64.getDecoder().decode(vo.getState())), BxczState.class);
            BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL_BACK", "活動編碼確認並索取簽屬文件相關資料", "0", "", state.getActionId(), state.getTransNum(), startTime, new Date());
            bxczSignService.addSignBxczApiRecord(bxczSignApiLog);
            if (StringUtils.equals(state.getType(), ApConstants.INSURANCE_CLAIM)) {
                TransInsuranceClaimVo claimVo = insuranceClaimService.getTransInsuranceClaimDetail(state.getTransNum());
                if (claimVo == null) {
                    ret.setCode("2");
                    ret.setMsg("非本公司保戶！");
                    return ret;
                }
                logger.info("api414 TransInsuranceClaimVo: ", new Gson().toJson(claimVo));
                ret.setCode("0");
                ret.setMsg("success");
                Map<String, Object> data = Maps.newHashMap();
                data.put("idNo", claimVo.getIdNo());
                data.put("name", claimVo.getName());
                data.put("birdate", claimVo.getBirdate());
                if (signRecord.getSignStart() != null && signRecord.getSignEnd() != null) {
                    data.put("acExpiredSec", String.valueOf((signRecord.getSignEnd().getTime() - System.currentTimeMillis()) / 1000));
                } else {
                    data.put("acExpiredSec", "0");
                }
                data.put("to", claimVo.getTo());
                data.put("redirectUri", callBack414 + "?actionId=" + vo.getActionId());
                data.put("id_token", StringUtils.isBlank(state.getId()) ? "" : AesUtil.decrypt(state.getId(), state.getActionId()));
                data.put("cpoaContent", Lists.newArrayList());
                ret.setData(data);
                return ret;
            } else {
                TransMedicalTreatmentClaimVo medicalVo = medicalTreatmentService.getTransInsuranceClaimDetail(state.getTransNum());
                if (medicalVo == null) {
                    ret.setCode("2");
                    ret.setMsg("非本公司保戶！");
                    return ret;
                }
                logger.info("api414 TransMedicalTreatmentClaimVo: ", new Gson().toJson(medicalVo));
                ret.setCode("0");
                ret.setMsg("success");
                Map<String, Object> data = Maps.newHashMap();
                data.put("idNo", medicalVo.getIdNo());
                data.put("name", medicalVo.getName());
                data.put("birdate", StringUtils.isNotBlank(medicalVo.getBirdate()) ? medicalVo.getBirdate().replaceAll("/", "") : "");
                if (signRecord.getSignStart() != null && signRecord.getSignEnd() != null) {
                    data.put("acExpiredSec", String.valueOf((signRecord.getSignEnd().getTime() - System.currentTimeMillis()) / 1000));
                } else {
                    data.put("acExpiredSec", "0");
                }
                data.put("to", medicalVo.getTo());
                data.put("redirectUri", callBack414 + "?actionId=" + vo.getActionId());
                data.put("id_token", StringUtils.isBlank(state.getId()) ? "" : AesUtil.decrypt(state.getId(), state.getActionId()));
                CoapContentVo coapContent = new CoapContentVo();
                coapContent.setHpId(medicalVo.getToHospitalId());
                coapContent.setSubHpId(medicalVo.getToSubHospitalId());
                List<TransMedicalTreatmentClaimMedicalInfoVo> medicalInfoVos = medicalTreatmentService.getMedicalInfo(medicalVo.getClaimSeqId());
                int i = 1;
                if (CollectionUtils.isNotEmpty(medicalInfoVos)) {
                    for (TransMedicalTreatmentClaimMedicalInfoVo e : medicalInfoVos) {
                        MedicalInfoVo infoVo = new MedicalInfoVo();
                        BeanUtils.copyProperties(e, infoVo);
                        infoVo.setSeNo(i ++ );
                        coapContent.getMedicalInfo().add(infoVo);
                    }
                }
                data.put("cpoaContent", Lists.newArrayList(coapContent));
                ret.setData(data);
                return ret;
            }
        } catch (Exception e) {
            logger.error("BxczSignController.callApi414 error: " + e);
            ret.setCode("500");
            ret.setMsg("error");
        }
        logger.info("End BxczSignController.callAPI414().");
        return ret;
    }

    @Autowired
    private ITransService transService;

    @Value("${eservice.bxcz.414.callback.url}")
    private String callBack414;

    @ApiRequest
    @RequestMapping("/api415")
    public Bxcz415ReturnVo callApi415(
            @RequestBody Bxcz415CallBackVo vo) {
        Date startTime = new Date();
        logger.info("Start BxczSignController.callApi415(). param: {}", new Gson().toJson(vo));
        Bxcz415ReturnVo ret = new Bxcz415ReturnVo();
        try {
            if (vo.getData() != null && StringUtils.isNotBlank(vo.getData().getState())) {
                Bxcz415CallBackDataVo data = vo.getData();
                BxczState state = new Gson().fromJson(new String(Base64.getDecoder().decode(data.getState())), BxczState.class);
                BxczSignApiLog bxczSignApiLog = new BxczSignApiLog("CALL_BACK", "數位身分驗證結果/數位簽署狀態通知", vo.getCode(), vo.getMsg(), state.getActionId(), state.getTransNum(), startTime, new Date());
                bxczSignService.addSignBxczApiRecord(bxczSignApiLog);
                int result = bxczSignService.updateSignRecordStatus(vo.getCode(), vo.getMsg(), data);
                if (result > 0) {
                    ret.setCode("0");
                    ret.setMsg("success");
                    return ret;
                }
            }
            ret.setCode("-1");
            ret.setMsg("系統錯誤！");
        } catch (Exception e) {
            logger.error("BxczSignController.callApi415 error: " + e);
            ret.setCode("500");
            ret.setMsg("error");
        }

        logger.info("End BxczSignController.callAPI415().");
        return ret;
    }
}
