package com.twfhclife.eservice.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.user.dao.LilipmDao;
import com.twfhclife.eservice.user.model.LilipmVo;
import com.twfhclife.eservice.user.service.ILilipmService;
import com.twfhclife.generic.service.impl.BaseServiceImpl;

/**
 * 要保人服務.
 * 
 * @author all
 */
@Service
public class LilipmServiceImpl extends BaseServiceImpl implements ILilipmService {

	@Autowired
	private LilipmDao lilipmDao;
	
	/**
	 * 要保人-查詢.
	 * 
	 * @param lilipmVo LilipmVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<LilipmVo> getLilipm(LilipmVo lilipmVo) {
		return lilipmDao.getLilipm(lilipmVo);
	}
	
	/**
	 * 要保人-根據證號查詢.
	 * 
	 * @param rocId 證號
	 * @return 回傳要保人
	 */
	@Override
	public LilipmVo findByRocId(String rocId) {
		return lilipmDao.findByRocId(rocId);
	}
	
	/**
	 * 要保人-根據保單號碼查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳要保人
	 */
	@Override
	public LilipmVo findByPolicyNo(String policyNo) {
		LilipmVo lipmVo = lilipmDao.findByPolicyNo(policyNo);
		if (lipmVo != null) {
			lipmVo.setLipmNameBase64(unicodeService.convertString2Unicode(lipmVo.getLipmName1()));
		}
		return lipmVo;
	}
	
	/**
	 * 查詢要保人聯絡資訊-根據保單號碼清單查詢最近的.
	 * 
	 * @param policyNos 保單號碼清單
	 * @return 回傳要保人
	 */
	@Override
	public LilipmVo findContactInfoByPolicyNoList(List<String> policyNos) {
		return lilipmDao.findContactInfoByPolicyNoList(policyNos);
	}
	
	/**
	 * 取得使用者的所有保單號碼清單.
	 * 
	 * @param rocId 使用者證號
	 * @return 回傳使用者的所有保單號碼
	 */
	@Override
	public List<String> getUserPolicyNos(String rocId) {
		if (StringUtils.isEmpty(rocId)) {
			return new ArrayList<>();
		}
		return lilipmDao.getUserPolicyNos(rocId);
	}

	@Override
	public List<LilipmVo> getAliveLilipm(LilipmVo lilipmVo) {
		return lilipmDao.getAliveLilipm(lilipmVo);
	}

	@Override
	public int getInsuredUsersByRocId(String rocId) {
		// TODO Auto-generated method stub
		return lilipmDao.getInsuredUsersByRocId(rocId) ;
	}
}