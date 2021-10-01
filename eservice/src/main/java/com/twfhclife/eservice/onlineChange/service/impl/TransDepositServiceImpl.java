package com.twfhclife.eservice.onlineChange.service.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransDepositDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransDepositDetailVo;
import com.twfhclife.eservice.onlineChange.model.TransDepositVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.ITransDepositService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.policy.model.DepositPolicyListVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.eservice.web.model.UsersVo;
import com.twfhclife.generic.api_client.MessageTemplateClient;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.DateUtil;
import com.twfhclife.generic.util.StatuCode;
import javafx.scene.control.TextInputControl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class TransDepositServiceImpl implements ITransDepositService {

    private static final Logger log = LogManager.getLogger(TransDepositServiceImpl.class);

    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    private IPolicyListService policyListService;

    @Autowired
    private TransDao transDao;

    @Autowired
    private OnlineChangeDao onlineChangeDao;

    @Autowired
    private TransPolicyDao transPolicyDao;

    @Autowired
    private TransDepositDao transDepositDao;

    private static final String DEPOST_PARAMETER_CATEGORY_CODE = "DEPOST_PARAMETER_CATEGORY";

    public List<ParameterVo> getDepositConfigs(String policyNo) {
        return parameterDao.getParameterByCategoryCode(ApConstants.SYSTEM_ID, DEPOST_PARAMETER_CATEGORY_CODE);
    }

    @Override
    public PolicyListVo getDepositPolicy(String userRocId, String policyNo) {
        List<PolicyListVo> depositList = policyListService.getDepositList(userRocId, policyNo);
        if (!CollectionUtils.isEmpty(depositList)) {
            return depositList.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public void addNewDepositApply(TransDepositVo vo, UsersVo user) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("transNum", null);
        transDao.getTransNum(params);
        String transNum = params.get("transNum").toString();

        TransVo transVo = new TransVo();
        transVo.setTransNum(transNum);
        transVo.setTransDate(new Date());
        transVo.setTransType(TransTypeUtil.DEPOSIT_PARAMETER_CODE);
        transVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
        transVo.setUserId(user.getUserId());
        transVo.setCreateUser(user.getUserId());
        transVo.setCreateDate(new Date());
        transDao.insertTrans(transVo);

        TransStatusHistoryVo hisVo = new TransStatusHistoryVo();
        hisVo.setCustomerName("系統日程");
        log.info("UsersVo", user);
        hisVo.setUsersVo(user);
        //寫入狀態歷程
        hisVo.setTransNum(transNum);
        hisVo.setStatus(OnlineChangeUtil.TRANS_STATUS_AUDITED);
        onlineChangeDao.addTransStatusHistory(hisVo);

        TransPolicyVo transPolicyVo = new TransPolicyVo();
        transPolicyVo.setTransNum(transNum);
        transPolicyVo.setPolicyNo(vo.getPolicyNo());
        transPolicyDao.insertTransPolicy(transPolicyVo);

        List<TransDepositVo> newDeposits = new Gson().fromJson(vo.getInvtDeposits(), new TypeToken<List<TransDepositVo>>() {
        }.getType());
        if (!CollectionUtils.isEmpty(newDeposits)) {
            for (TransDepositVo e : newDeposits) {
                TransDepositVo transDepositVo = new TransDepositVo();
                BeanUtils.copyProperties(vo, transDepositVo);
                transDepositVo.setTransNum(transNum);
                transDepositVo.setInvtNo(e.getInvtNo());
                transDepositVo.setRatio(e.getRatio());
                transDepositVo.setAmount(e.getAmount());
                transDepositVo.setCurrency(e.getCurrency());
                transDepositDao.insert(transDepositVo);
            }
        } else {
            vo.setTransNum(transNum);
            transDepositDao.insert(vo);
        }
    }

    @Override
    public TransDepositDetailVo getAppliedTransDeposits(String transNum) {
        return transDepositDao.getAppliedTransDeposits(transNum);
    }
}
