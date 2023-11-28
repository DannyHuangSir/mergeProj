package com.twfhclife.jd.web.service.impl;

import com.twfhclife.jd.api_client.BaseRestClient;
import com.twfhclife.jd.api_model.PolicyTypeListResponse;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.web.dao.OptionDao;
import com.twfhclife.jd.web.model.DepartmentVo;
import com.twfhclife.jd.web.service.IOptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements IOptionService {

	@Autowired
	private OptionDao optionDao;

    @Override
    public List<Map<String, Object>> getLevelStates() {
        return optionDao.getLevelStates();
    }

    @Override
    public List<Map<String, Object>> getPayModeList() {
        return optionDao.getPayModeList();
    }

    @Override
    public List<Map<String, Object>> getTransactionCodeList() {
        return optionDao.getTransactionCodeList(ApConstants.TRANSACTION_PARAMETER_CATEGORY_CODE);
    }

    @Override
    public List<Map<String, String>> getBankList() {
        return optionDao.getBankList();
    }

    @Autowired
    private BaseRestClient baseRestClient;

    @Value("${eservice_api.policy.type.list.url}")
    private String policyTypeListUrl;
    @Override
    public List<Map<String, String>> getPolicyTypeList() {
        PolicyTypeListResponse responseObj = baseRestClient.postApi("", policyTypeListUrl, PolicyTypeListResponse.class);
        return responseObj.getPolicyTypeList();
    }

    @Override
    public List<DepartmentVo> getBranchList(String userId, String username, DepartmentVo vo) {
        List<DepartmentVo> roles = null;
        if(vo.getRole() == 4 ) {
            roles = optionDao.getICUserBranchIDByRole(userId, vo);
        }

        boolean selectAllflag = false;
        if(roles != null &&  ( (roles.size() == 1 && StringUtils.isNotBlank(roles.get(0).getBranchId())) || roles.size() > 1 )) {
            selectAllflag = false;
        } else {
            selectAllflag = true;
        }


        return optionDao.getBranchList(userId,vo,selectAllflag,roles);
    }
}
