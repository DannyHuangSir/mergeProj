package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.LilipiVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 被保人 Dao.
 * 
 * @author all
 */
public interface LilipiDao {
	
	/**
	 * 被保人-查詢.
	 * 
	 * @param lilipiVo LilipiVo
	 * @return 回傳查詢結果
	 */
	List<LilipiVo> getLilipi(@Param("lilipiVo") LilipiVo lilipiVo);
	
	/**
	 * 被保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳被保人
	 */
	LilipiVo findByPolicyNo(@Param("policyNo") String policyNo);

	/**
	 * 获取文件上传最大值，单位kb
	 * 取消，使用模板
	 * @param elifeUpload012
	 * @param systemId
	 * @return
	 */
   // String findByUploadNumber(@Param("elifeUpload012")String elifeUpload012,@Param("systemId")String systemId);
}