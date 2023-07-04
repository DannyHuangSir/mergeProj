package com.twfhclife.adm.service;

import com.twfhclife.adm.model.JdPolicyClaimDetailVo;
import com.twfhclife.adm.model.JdPolicyClaimReqVo;
import com.twfhclife.adm.model.JdUserDetailReqVo;

import java.util.List;

/**
 * @auther lihao
 */
public interface IJdPolicyClaimDetailService {

    List<JdPolicyClaimDetailVo> getBpmcurrenttak();

    <T> T getPolicyTypeNameList(JdPolicyClaimDetailVo jdPolicyClaimDetailVo);

    <T> T getInsClaimStatisticsReport(JdPolicyClaimReqVo jdPolicyClaimDetailVo);

    <T> T getUserDetail(JdUserDetailReqVo vo);
}
