package com.twfhclife.eservice.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.policy.service.ICoverageService;
import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.policy.dao.BenefitDao;
import com.twfhclife.eservice.policy.dao.CoverageDao;
import com.twfhclife.eservice.policy.model.BenefitVo;
import com.twfhclife.eservice.policy.model.CoverageVo;
import com.twfhclife.generic.service.impl.BaseServiceImpl;

/**
 * 保單保項服務.
 * 
 * @author all
 */
@Service
public class CoverageServiceImpl extends BaseServiceImpl implements ICoverageService {

	@Autowired
	private CoverageDao coverageDao;

	@Autowired
	private BenefitDao benefitDao;
	
	/**
	 * 保單保項-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public List<CoverageVo> getCoverageList(CoverageVo coverageVo) {
		List<CoverageVo> coverageList = coverageDao.getCoverageList(coverageVo);
		if (coverageList != null) {
			for (CoverageVo vo : coverageList) {
				if (vo.getProductVo() != null) {
					BenefitVo benefitVo = new BenefitVo();
					benefitVo.setProductCode(vo.getProductVo().getProductCode());
					vo.setBenefitList(benefitDao.getBenefitList(benefitVo, vo.getPolicyNo()));
					vo.setInsuredNameBase64(unicodeService.convertString2Unicode(vo.getInsuredName()));
				}
			}
		}
		
		return coverageList;
	}

	/**
	 * 主約被保人保項-查詢.
	 * 
	 * @param coverageVo CoverageVo
	 * @return 回傳查詢結果
	 */
	@RequestLog
	@Override
	public CoverageVo getInsuredCoverage(CoverageVo coverageVo) {
		return coverageDao.getInsuredCoverage(coverageVo);
	}
}