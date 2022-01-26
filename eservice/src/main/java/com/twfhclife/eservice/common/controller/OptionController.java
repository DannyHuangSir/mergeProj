package com.twfhclife.eservice.common.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.twfhclife.eservice.generic.annotation.RequestLog;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.service.IOptionService;

/**
 * 選單資料控制器.
 * 
 * @author alan
 */
@Controller
@EnableAutoConfiguration
public class OptionController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(OptionController.class);

	@Autowired
	private IOptionService optionService;

	@RequestLog
	@PostMapping("/getCityList")
	public  ResponseEntity<ResponseObj> getCityList() {
		synchronized(this){
		try {
			processSuccess(optionService.getCityList());
		} catch (Exception e) {
			logger.error("Unable to getCityList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
		}
	}

	@RequestLog
	@PostMapping("/getRegionList")
	public ResponseEntity<ResponseObj> getRegionList(@RequestParam("cityId") String cityId) {
		synchronized(this) {
			if (cityId != null && !"".equals(cityId)) {
				try {
					logger.info("getRegionList===========: {}", cityId);
					List<Map<String, Object>> regionList = optionService.getRegionList(cityId);
					logger.info("========getRegionList================================: {}", regionList);
					processSuccess(regionList);
				} catch (Exception e) {
					logger.error("Unable to getRegionList: {}", ExceptionUtils.getStackTrace(e));
					processSystemError();
				}
			}
			return processResponseEntity();
		}
	}

	@RequestLog
	@PostMapping("/getRoadList")
	public ResponseEntity<ResponseObj> getRoadList(@RequestParam("regionId") String regionId) {
		synchronized(this) {
			if (regionId != null && !"".equals(regionId)) {
				try {
					logger.info("--------getRoadList-----@RequestParam(\"regionId\") String regionId{}", regionId);
					List<Map<String, Object>> roadList = optionService.getRoadList(regionId);
					logger.info("--------getRoadList-----{}", roadList);

					processSuccess(roadList);
				} catch (Exception e) {
					logger.error("Unable to getRoadList: {}", ExceptionUtils.getStackTrace(e));
					processSystemError();
				}
			}
			return processResponseEntity();
		}
	}
	
	@RequestLog
	@PostMapping("/getBankList")
	public ResponseEntity<ResponseObj> getBankList() {
		try {
			processSuccess(optionService.getBankList());
		} catch (Exception e) {
			logger.error("Unable to getBankList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getBranchesList")
	public ResponseEntity<ResponseObj> getBranchesList(@RequestParam("bankId") String bankId) {
		try {
			processSuccess(optionService.getBranchesList(bankId));
		} catch (Exception e) {
			logger.error("Unable to getBranchesList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getTransactionCodeList")
	public ResponseEntity<ResponseObj> getTransactionCodeList() {
		try {
			processSuccess(optionService.getTransactionCodeList());
		} catch (Exception e) {
			logger.error("Unable to getTransactionCodeList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@PostMapping("/getTransStatusList")
	public ResponseEntity<ResponseObj> getTransStatusList() {
		try {
			processSuccess(optionService.getTransStatusList());
		} catch (Exception e) {
			logger.error("Unable to getTransStatusList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
	@RequestLog
	@PostMapping("/getTransTypeList")
	public ResponseEntity<ResponseObj> getTransTypeList() {
		try {
			processSuccess(optionService.getTransTypeList());
		} catch (Exception e) {
			logger.error("Unable to getTransTypeList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getAreaList")
	public ResponseEntity<ResponseObj> getAreaList() {
		try {
			List<Map<String, Object>> map = optionService.getAreaList();
			processSuccess(map);
		} catch (Exception e) {
			logger.error("Unable to getAreaList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getCountryList")
	public ResponseEntity<ResponseObj> getCountryList(@RequestParam("areaId") String areaId) {
		try {
			processSuccess(optionService.getCountryList(areaId));
		} catch (Exception e) {
			logger.error("Unable to getCountryList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getJobList1")
	public ResponseEntity<ResponseObj> getJobList1() {
		try {
			processSuccess(optionService.getJobList1());
		} catch (Exception e) {
			logger.error("Unable to getJobList1: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getJobList2")
	public ResponseEntity<ResponseObj> getJobList2(@RequestParam("jobB") String jobB) {
		try {
			processSuccess(optionService.getJobList2(jobB));
		} catch (Exception e) {
			logger.error("Unable to getJobList2: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	@RequestLog
	@PostMapping("/getJobList3")
	public ResponseEntity<ResponseObj> getJobList3(@RequestParam("jobB") String jobB, @RequestParam("jobM") String jobM) {
		try {
			processSuccess(optionService.getJobList3(jobB, jobM));
		} catch (Exception e) {
			logger.error("Unable to getJobList3: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
	
}
