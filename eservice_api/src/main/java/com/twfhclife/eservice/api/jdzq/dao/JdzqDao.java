package com.twfhclife.eservice.api.jdzq.dao;

import com.twfhclife.eservice.api.jdzq.domain.CaseQueryRequest;
import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdzqDao {

    List<CaseVo> getCaseList(@Param("vo") CaseQueryRequest caseQuery);
    CaseVo getCaseProcess(@Param("policyNo") String policyNo);
    CaseVo getPolicyInfo(@Param("policyNo") String policyNo);

//    List<PolicyClaimDetailVo> getPolicyClaimDetail(@Param("vo") PolicyClaimDetailVo vo,@Param("columnItem")String columnItem);
}
