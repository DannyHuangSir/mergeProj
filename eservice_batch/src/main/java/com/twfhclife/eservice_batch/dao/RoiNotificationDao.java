package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.RoiNotificationMapper;
import com.twfhclife.eservice_batch.model.InvestmentVo;
import com.twfhclife.eservice_batch.model.MyPortfolioVo;
import com.twfhclife.eservice_batch.model.RoiNotificationVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RoiNotificationDao extends BaseDao {
	
	private static final Logger logger = LogManager.getLogger(RoiNotificationDao.class);

	public List<RoiNotificationVo> findEnableAll() {
		List<RoiNotificationVo> listRoiNotifica = new ArrayList<RoiNotificationVo>();
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			listRoiNotifica = roiNotificationMapper.findEnableAll();
		} catch (Exception e) {
			logger.error("findEnableAll error: {}", e);
		} finally {
			this.release();
		}
		return listRoiNotifica;
	}
	
	public InvestmentVo findFundInfoByInsuNo(InvestmentVo investmentVo) {
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			investmentVo = roiNotificationMapper.findFundInfoByInsuNo(investmentVo);
		} catch(Exception e) {
			logger.error("findFundInfoByInsuNo error: {}", e);
		} finally {
			this.release();
		}
		return investmentVo;
	}
	
	public String findEmailByPolicyNo(String policyNo){
		String email = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			email = roiNotificationMapper.findEmailByPolicyNo(policyNo);
		} catch(Exception e) {
			logger.error("findEmailByPolicyNo error: {}", e);
		} finally {
			this.release();
		}
		return email;
	}
	
	public Map<String, Object> findNetValueLatest(String invtNo){
		Map<String, Object> map = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			map = roiNotificationMapper.findNetValueLatest(invtNo);
		} catch(Exception e) {
			logger.error("findNetValueLatest error: {}", e);
		} finally {
			this.release();
		}
		return map;
	}
	
	public Map<String, Object> findExchRateLatest(String exchCode){
		Map<String, Object> map = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			map = roiNotificationMapper.findExchRateLatest(exchCode);
		} catch(Exception e) {
			logger.error("findExchRateLatest error: {}", e);
		} finally {
			this.release();
		}
		return map;
	}
	
	public Map<String, Object> findAccumulation(String insuNo, String invtNo) {
		Map<String, Object> mapAccumulation = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			mapAccumulation = roiNotificationMapper.findAccumulation(insuNo, invtNo);
		} catch(Exception e) {
			logger.error("findAccumulation error: {}", e);
		} finally {
			this.release();
		}
		return mapAccumulation;
	}
	
	public Boolean insertRoiNotificationJob(RoiNotificationVo roiNotificationVo) {
		Boolean result = false;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			int count = roiNotificationMapper.insertRoiNotificationJob(roiNotificationVo);
			this.commit();
			result = count > 0;
		} catch(Exception e) {
			logger.error("insertRoiNotificationJob error: {}", e);
			this.rollback();
		} finally {
			this.release();
		}
		return result;
	}
	
	public Boolean updateAllDisable() {
		Boolean result = false;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			int count = roiNotificationMapper.updateAllDisable();
			this.commit();
			result = count >= 0;
		} catch(Exception e) {
			logger.error("updateAllDisable error: {}", e);
			this.rollback();
		} finally {
			this.release();
		}
		return result;
	}

	public InvestmentVo findFundByInvestNo(InvestmentVo investmentVo) {
		InvestmentVo vo = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			vo = roiNotificationMapper.findFundByInvestNo(investmentVo);
		} catch(Exception e) {
			logger.error("findFundByInvestNo error: {}", e);
		} finally {
			this.release();
		}
		return vo;
	}

	public MyPortfolioVo findPortfolioByInvestNo(InvestmentVo investmentVo) {
		MyPortfolioVo vo = null;
		try {
			RoiNotificationMapper roiNotificationMapper = this.getSqlSession().getMapper(RoiNotificationMapper.class);
			vo = roiNotificationMapper.findPortfolioByInvestNo(investmentVo);
		} catch(Exception e) {
			logger.error("findPortfolioByInvestNo error: {}", e);
		} finally {
			this.release();
		}
		return vo;
	}
}
