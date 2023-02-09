package com.twfhclife.eservice.service.impl;

import com.twfhclife.eservice.service.ICoverageService;
import com.twfhclife.eservice.service.IUnicodeService;
import com.twfhclife.eservice.shouxian.dao.BenefitDao;
import com.twfhclife.eservice.shouxian.dao.CoverageDao;
import com.twfhclife.eservice.web.model.BenefitVo;
import com.twfhclife.eservice.web.model.CoverageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 保單保項服務.
 * 
 * @author all
 */
@Service
public class CoverageServiceImpl implements ICoverageService {


	@Autowired
	public IUnicodeService unicodeService;

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
	@Override
	public CoverageVo getInsuredCoverage(CoverageVo coverageVo) {
		return coverageDao.getInsuredCoverage(coverageVo);
	}
}