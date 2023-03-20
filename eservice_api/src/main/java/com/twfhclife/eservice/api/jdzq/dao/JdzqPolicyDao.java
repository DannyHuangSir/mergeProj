package com.twfhclife.eservice.api.jdzq.dao;

import com.twfhclife.eservice.api.jdzq.model.PolicyClaimDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther lihao
 */
public interface JdzqPolicyDao {

    List<PolicyClaimDetailVo> getPolicyClaimDetail(@Param("vo") PolicyClaimDetailVo vo, @Param("columnItem")String columnItem);

    List<PolicyClaimDetailVo> getPolicyTypeNameList(@Param("vo") PolicyClaimDetailVo vo);
}
