package com.twfhclife.eservice.service;

import com.twfhclife.eservice.web.model.LilipiVo;

import java.util.List;

/**
 * 被保人服務.
 * 
 * @author all
 */
public interface ILilipiService {
	
	/**
	 * 被保人-查詢.
	 * 
	 * @param lilipiVo LilipiVo
	 * @return 回傳查詢結果
	 */
	public List<LilipiVo> getLilipi(LilipiVo lilipiVo);
	
	/**
	 * 被保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳被保人
	 */
	public LilipiVo findByPolicyNo(String policyNo);

	/**
	 * 获取文件上传最大值，单位kb
	 * @param elifeUpload012
	 * @return
	 */
    int findByUploadNumber(String elifeUpload012,String systemId);
}