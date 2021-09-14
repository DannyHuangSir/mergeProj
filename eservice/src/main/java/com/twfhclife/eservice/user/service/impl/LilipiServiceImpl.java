package com.twfhclife.eservice.user.service.impl;

import java.util.List;

import com.twfhclife.eservice.web.service.IParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.user.dao.LilipiDao;
import com.twfhclife.eservice.user.model.LilipiVo;
import com.twfhclife.eservice.user.service.ILilipiService;
import com.twfhclife.generic.service.impl.BaseServiceImpl;

/**
 * 被保人服務.
 * 
 * @author all
 */
@Service
public class LilipiServiceImpl extends BaseServiceImpl implements ILilipiService {

	@Autowired
	private LilipiDao lilipiDao;

	@Autowired
	private IParameterService parameterSerivce;

	/**
	 * 被保人-查詢.
	 * 
	 * @param lilipiVo LilipiVo
	 * @return 回傳查詢結果
	 */
	public List<LilipiVo> getLilipi(LilipiVo lilipiVo) {
		return lilipiDao.getLilipi(lilipiVo);
	}
	
	/**
	 * 被保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳被保人
	 */
	public LilipiVo findByPolicyNo(String policyNo) {
		LilipiVo lipiVo = lilipiDao.findByPolicyNo(policyNo);
		if (lipiVo != null) {
			lipiVo.setLipiNameBase64(unicodeService.convertString2Unicode(lipiVo.getLipiName()));
		}
		return lipiVo;
	}

	/**
	 * 获取文件上传最大值，单位kb
	 * @param elifeUpload012
	 * @return
	 */
	@Override
	public int findByUploadNumber(String elifeUpload012,String systemId) {
		String uploadMaxNumber = parameterSerivce.getParameterValueByCode(systemId, elifeUpload012);
		//使用模板
		// String  uploadMaxNumber=	lilipiDao.findByUploadNumber(elifeUpload012,systemId);
		int  i=0;
	if (uploadMaxNumber!=null && !"".equals(uploadMaxNumber)) {
		i=Integer.valueOf(uploadMaxNumber);
		}
		return  i;
	}
}