package com.twfhclife.alliance.controller;

import java.util.List;
import java.util.Map;

import com.twfhclife.alliance.model.MedicalRequestVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.alliance.service.IClaimChainService;
import com.twfhclife.generic.annotation.ApiRequest;

@RestController
public class ClaimChainController{

	private static final Logger logger = LogManager.getLogger(ClaimChainController.class);
	
	@Autowired
	IClaimChainService claimChainService;
	
	/**
	 * API-107 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/notifyOfNewCase")
	public ClaimResponseVo notifyOfNewCase(@RequestBody ClaimRequestVo reqVo) {
		logger.info("Start ClaimChainController.notifyOfNewCase().");
		
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		try {
			rtnVo = claimChainService.notifyOfNewCase(reqVo);
		}catch(Exception e) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}

		//rtnVo.setMsg("this is a test call");
		
		logger.info("End ClaimChainController.notifyOfNewCase().");
		return rtnVo;
	}
	
	/**
	 * for API-105查詢理賠案件後呼叫，及可用於本地新增
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/addInsuranceClaim")
	public ClaimResponseVo addInsuranceClaim(InsuranceClaimMapperVo icvo) {
		logger.info("Start ClaimChainController.addInsuranceClaim().");
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		
		try {
			//TODO
			if(icvo!=null) {
				int rtnValue = claimChainService.addInsuranceCliam(icvo);
				if(rtnValue>=1) {
					rtnVo.setCode(ClaimResponseVo.CODE_SUCCESS);
				}else {
					rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
					rtnVo.setMsg("claimChainService.addInsuranceCliam error.");
				}
			}else {
				
				rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
				
				String msg = "input params is null.";
				rtnVo.setMsg(msg);
				logger.error(msg);
			}

		}catch(Exception e) {

			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}
		
		logger.info("End ClaimChainController.addInsuranceClaim().");
		return rtnVo;
	}
	
	/**
	 * API-205 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/notifyOfNewCaseChange")
	public ClaimResponseVo newCaseNotified(@RequestBody ClaimRequestVo reqVo) {
		logger.info("Start ClaimChainController.newCaseNotified().");
		
		
		ClaimResponseVo rtnVo = new ClaimResponseVo();
		try {
			rtnVo = claimChainService.newCaseNotified(reqVo);
		}catch(Exception e) {
			rtnVo.setCode(ClaimResponseVo.CODE_ERROR);
			rtnVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}

		//rtnVo.setMsg("this is a test call");
		
		logger.info("End ClaimChainController.newCaseNotified().");
		return rtnVo;
	}
	
	/**
	 * DNS-491 通知有新案件
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/dns491")
	public DnsResponseVo notifyOfNewCaseDns(@RequestBody DnsRequestVo reqVo){
		logger.info("Start ClaimChainController.notifyOfNewCaseDns().");
		
		DnsResponseVo rtnVo = null;
		
		if(reqVo!=null && reqVo.getType()!=null && reqVo.getJobIds()!=null) {
			String strType = reqVo.getType();
			List<String> listStr = reqVo.getJobIds();
			
			try {
				//save to eservice NOTIFY_OF_NEW_CASE_DNS table
				rtnVo = this.claimChainService.notifyOfNewCaseDns(reqVo);
			}catch(Exception e) {
				logger.error(e);
			}
			
			if(rtnVo==null) {
				rtnVo = new DnsResponseVo();
				rtnVo.setCode(DnsResponseVo.CODE_ERROR);
				rtnVo.setMsg("eservice_api error.");
			}

		}else {
			if(rtnVo==null) {
				rtnVo = new DnsResponseVo();
			}
			
			rtnVo.setCode(DnsResponseVo.CODE_ERROR);
			
			StringBuffer sb = new StringBuffer("");
			sb.append("Input parameter error:");
			if(reqVo==null) {
				sb.append("DnsRequestVo is null.");
			}
			if(reqVo!=null && reqVo.getType()==null) {
				sb.append("type is null.");
			}
			if(reqVo!=null && reqVo.getJobIds()==null) {
				sb.append("jobId is null.");
			}
			rtnVo.setMsg(sb.toString());
			
			logger.info("input DnsRequestVo error:"+reqVo.toString());
		}
		
		logger.info("End ClaimChainController.notifyOfNewCaseDns().");
		return rtnVo;
		
	}


	/**
	 * API-491 案件通知(CallBack 到保險公司)
	 * @return ClaimResponseVo
	 */
	@ApiRequest
	@RequestMapping("/api491")
	public MedicalRequestVo addNotifyOfNewCaseMedical(@RequestBody MedicalRequestVo vo){
		logger.info("Start ClaimChainController.addNotifyOfNewCaseMedical().");
		MedicalRequestVo mdVo = new MedicalRequestVo();
		try {
			if(vo!=null) {
				int rtnValue = claimChainService.addNotifyOfNewCaseMedical(vo);
				if(rtnValue>=1) {
					mdVo.setCode(ClaimResponseVo.CODE_SUCCESS);
					mdVo.setMsg(ClaimResponseVo.MSG_SUCCESS);
				}else {
					mdVo.setCode(ClaimResponseVo.CODE_ERROR);
					mdVo.setMsg("claimChainService.addNotifyOfNewCaseMedical error.");
				}
			}else {

				mdVo.setCode(ClaimResponseVo.CODE_ERROR);
				String msg = "input params is null.";
				mdVo.setMsg(msg);
				logger.error(msg);
			}
		}catch(Exception e) {
			mdVo.setCode(ClaimResponseVo.CODE_ERROR);
			mdVo.setMsg(ClaimResponseVo.MSG_ERROR_S001);
			logger.error(e);
		}
		logger.info("End ClaimChainController.addNotifyOfNewCaseMedical().");
		return mdVo;
	}
}
