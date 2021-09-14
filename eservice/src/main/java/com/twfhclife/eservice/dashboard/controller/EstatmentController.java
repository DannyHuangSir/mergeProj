package com.twfhclife.eservice.dashboard.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.twfhclife.eservice.dashboard.model.EstatmentAttrVo;
import com.twfhclife.eservice.dashboard.model.EstatmentVo;
import com.twfhclife.eservice.dashboard.service.IEstatmentService;
import com.twfhclife.eservice.web.domain.ResponseObj;
import com.twfhclife.eservice.web.model.UserDataInfo;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;

/**
 * 我的通知.
 * 
 * @author alan
 */
@Controller
public class EstatmentController extends BaseController {

	private static final Logger logger = LogManager.getLogger(EstatmentController.class);

	@Autowired
	private IEstatmentService estatmentService;
	
	/**
	 * 取得我的通知資料.
	 * 
	 * @param estatmentVo EstatmentVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/getNoticeBoardList")
	public ResponseEntity<ResponseObj> getNoticeBoardList(@RequestBody EstatmentVo estatmentVo) {
		try {
			processSuccess(estatmentService.getNoticeBoardList(estatmentVo));
		} catch (Exception e) {
			logger.error("Unable to getNoticeBoardList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}

	/**
	 * 取得我的通知屬性資料.
	 * 
	 * @param estatmentAttrVo EstatmentAttrVo
	 * @return
	 */
	@RequestLog
	@PostMapping("/getEstatmentAttrList")
	public ResponseEntity<ResponseObj> getEstatmentAttrList(@RequestBody EstatmentAttrVo estatmentAttrVo) {
		try {
			processSuccess(estatmentService.getEstatmentAttrList(estatmentAttrVo));
			estatmentService.updateEstatment(estatmentAttrVo.getEstatmentId());
			
			// 重設null, 頁面切換時會重新查詢，因資料已經有更新成已讀狀態
			addSession(UserDataInfo.USER_NOTICE_BOARD_LIST, null);
		} catch (Exception e) {
			logger.error("Unable to getEstatmentAttrList: {}", ExceptionUtils.getStackTrace(e));
			processSystemError();
		}
		return processResponseEntity();
	}
}
