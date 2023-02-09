package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.DepositPolicyListVo;
import com.twfhclife.eservice.web.model.PolicyListVo;
import com.twfhclife.eservice.web.model.PolicyVo;
import com.twfhclife.eservice.web.model.ProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的保單清單 Dao.
 * 
 * @author all
 */
public interface PolicyListDao {
	
	/**
	 * 保單清單-查詢.
	 * 
	 * @param vo PolicyListVo
	 * @return 回傳查詢結果
	 */
	List<PolicyListVo> getPolicyList(@Param("vo") PolicyListVo vo);
	
	/**
	 * 使用者的保單清單-查詢.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	List<PolicyListVo> getUserPolicyList(@Param("rocId") String rocId);
	
	/**
	 * 使用者的保單清單(保單理賠)-查詢.
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	List<PolicyListVo> getUserPolicyListByRocId(@Param("rocId") String rocId);
	
	/**
	 * 取得使用者的所有保單清單(投資型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	List<PolicyListVo> getInvtPolicyList(@Param("rocId") String rocId);
	
	/**
	 * 取得使用者的所有保單清單(保障型).
	 * 
	 * @param rocId 用戶證號
	 * @return 回傳查詢結果
	 */
	List<PolicyListVo> getBenefitPolicyList(@Param("rocId") String rocId);
	
	/**
	 * 取得我的保單資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	PolicyVo findById(@Param("policyNo") String policyNo);

	/**
	 * 取得product資料.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	ProductVo findProductByPolicyNo(@Param("policyNo") String policyNo);

	/***
	 * 获取投资型保单
	 * @param rocId
	 * @return
	 */
    List<PolicyListVo> getInvestmentPolicyList(String rocId);


    List<DepositPolicyListVo> getDepositList(@Param("rocId") String userRocId, @Param("policyNo") String policyNo);
}