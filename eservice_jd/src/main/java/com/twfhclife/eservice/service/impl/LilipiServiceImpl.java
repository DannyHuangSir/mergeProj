package com.twfhclife.eservice.service.impl;

import com.twfhclife.eservice.service.ILilipiService;
import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.shouxian.dao.LilipiDao;
import com.twfhclife.eservice.web.model.LilipiVo;
import com.twfhclife.eservice.web.service.IParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 被保人服務.
 * 
 * @author all
 */
@Service
public class LilipiServiceImpl implements ILilipiService {


	@Autowired
	public IUnicodeService unicodeService;

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