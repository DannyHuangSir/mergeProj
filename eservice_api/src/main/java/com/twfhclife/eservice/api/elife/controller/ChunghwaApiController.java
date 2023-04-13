package com.twfhclife.eservice.api.elife.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.elife.domain.ChunghwaApiRequest;
import com.twfhclife.eservice.api.elife.domain.ChunghwaApiResponse;
import com.twfhclife.eservice.api.elife.domain.StreetNameDataVo;
import com.twfhclife.eservice.api.elife.service.IChunghwaApiService;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

@RestController
public class ChunghwaApiController extends BaseController{
	private static final Logger logger = LogManager.getLogger(ChunghwaApiController.class);
	
	@Autowired
	private IChunghwaApiService chunghwaApiService;

	@ApiRequest
	@PostMapping(value = "/getChunghwaApi", produces = { "application/json" })
	public List<String> getChunghwaApi(@Valid @RequestBody ChunghwaApiRequest req) {
		List<String> streetNameList =new ArrayList<>();
		ReturnHeader returnHeader = new ReturnHeader();
		ChunghwaApiResponse resp = new ChunghwaApiResponse();
		try {
			List<StreetNameDataVo> StreetNameDataList = chunghwaApiService.getStreetNameData(req.getCity(), req.getCityarea());
			for(StreetNameDataVo vo : StreetNameDataList) {
				streetNameList.add(vo.getStreetName());
			}
//			resp.setStreetNameList(StreetNameDataList);
//			returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
//			apiResponseObj.setReturnHeader(returnHeader);
//			apiResponseObj.setResult(resp);
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getChunghwaApi: {}", ExceptionUtils.getStackTrace(e));
			return null;
		}
		return streetNameList;
	}
	
}
