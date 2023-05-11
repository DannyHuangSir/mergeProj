package com.twfhclife.eservice.api.elife.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.twfhclife.eservice.api.elife.domain.DashboardRequest;
import com.twfhclife.eservice.api.elife.domain.DashboardResponse;
import com.twfhclife.eservice.dashboard.model.EstatmentVo;
import com.twfhclife.eservice.dashboard.service.IApplyProgressService;
import com.twfhclife.eservice.dashboard.service.IDashboardService;
import com.twfhclife.eservice.dashboard.service.IEstatmentService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.policy.model.ExchangeRateVo;
import com.twfhclife.eservice.policy.model.PolicyExtraVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.policy.service.IExchangeRateService;
import com.twfhclife.eservice.policy.service.IPolicyExtraService;
import com.twfhclife.eservice.policy.service.IPolicyListService;
import com.twfhclife.eservice.policy.service.IPortfolioService;
import com.twfhclife.eservice.web.model.TransVo;
import com.twfhclife.generic.annotation.ApiRequest;
import com.twfhclife.generic.annotation.EventRecordLog;
import com.twfhclife.generic.annotation.EventRecordParam;
import com.twfhclife.generic.annotation.SqlParam;
import com.twfhclife.generic.annotation.SystemEventParam;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.domain.ApiResponseObj;
import com.twfhclife.generic.domain.ReturnHeader;

/**
 * 保障總覽相關資料控制器.
 * 
 * @author all
 */
@RestController
public class DashboardController extends BaseController {

	private static final Logger logger = LogManager.getLogger(DashboardController.class);

	@Autowired
	private IDashboardService dashboardService;
	
	@Autowired
	private IPolicyListService policyListService;
	
	@Autowired
	private IPortfolioService portfolioService;
	
	@Autowired
	private IPolicyExtraService policyExtraService;
	
	@Autowired
	private IEstatmentService estatmentService;
	
	@Autowired
	private IApplyProgressService applyProgressService;
	
	@Autowired
	private IExchangeRateService exchangeRateService;

	/**
	 * 查詢客戶的保障總覽.
	 * 
	 * @param req DashboardRequest
	 * @return
	 */
	@ApiRequest
	@EventRecordLog(value = @EventRecordParam(
			eventCode = "ES-001", 
			systemEventParams = {
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getInvtPolicyList",
					execMethod = "查詢投資型保單",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyListDao.getBenefitPolicyList",
					execMethod = "查詢保障型保單",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.policy.dao.PolicyExtraDao.getUserAllLoanData",
					execMethod = "查詢資產與負債資料",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				),
				@SystemEventParam(
					sqlId = "com.twfhclife.eservice.dashboard.dao.EstatmentDao.getEstatment",
					execMethod = "查詢通知資料",
					sqlVoType = "com.twfhclife.eservice.dashboard.model.EstatmentVo",
					sqlVoKey = "estatmentVo",
					sqlParams = { 
						@SqlParam(requestParamkey = "policyHolderId", sqlParamkey = "rocId")
					}
				)
			}))
	@PostMapping(value = "/getPolicyDashBoard", produces = { "application/json" })
	public ResponseEntity<?> getPolicyDashBoard(@Valid @RequestBody DashboardRequest req) {
		ApiResponseObj<DashboardResponse> apiResponseObj = new ApiResponseObj<>();
		ReturnHeader returnHeader = new ReturnHeader();
		DashboardResponse resp = new DashboardResponse();
		
		try {
			String rocId = req.getPolicyHolderId();
			if (!StringUtils.isEmpty(rocId)) {
				// 保障型保單
				List<PolicyListVo> benefitPolicyList = policyListService.getBenefitPolicyList(rocId);
				if (benefitPolicyList != null) {
					Map<String, List<String>> insuredProductData = dashboardService.getInsuredProductData(benefitPolicyList);
					
					// 取得保障型的被保人資料
					resp.setInsuredProductData(insuredProductData);
					// 保障型商品數量
					resp.setBenefitPolicyListSize(benefitPolicyList.size());
				}
				
				// 投資型保單
				BigDecimal policyAcctValueTotal = BigDecimal.ZERO;
				List<PolicyListVo> invtPolicyList = policyListService.getInvtPolicyList(rocId);
				if (invtPolicyList != null) {
					// 設定平均報酬率及帳戶價值資料
					portfolioService.setPortfolioData(invtPolicyList);
					
					policyAcctValueTotal = this.dashboardService.getPolicyAcctValueTotal(invtPolicyList);
					
					// 設定參考合計帳戶價值合計
					resp.setPolicyAcctValueTotal(policyAcctValueTotal);
					resp.setInvtPolicyList(invtPolicyList);
				}
				
				// 取得我的資產與負債資料
				PolicyExtraVo policyExtraVo = policyExtraService.getUserAllLoanData(rocId);
				resp.setPolicyExtraVo(policyExtraVo);
				
				// 設定事件通知資料
				setNoticeBoardList(resp, rocId);
				
				// 設定一個月內申請進度清單
				setOnlineChangeView(resp, rocId);
				
				returnHeader.setReturnHeader(ReturnHeader.SUCCESS_CODE, "", "", "");
				apiResponseObj.setReturnHeader(returnHeader);
				apiResponseObj.setResult(resp);
			}
		} catch (Exception e) {
			returnHeader.setReturnHeader(ReturnHeader.ERROR_CODE, e.getMessage(), "", "");
			logger.error("Unable to getPolicyDashBoard: {}", ExceptionUtils.getStackTrace(e));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseObj);
		} finally {
			apiResponseObj.setReturnHeader(returnHeader);
		}
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseObj);
	}
	
	/**
	 * 設定事件通知資料.
	 * 
	 * @param resp DashboardResponse
	 * @param rocId
	 */
	private void setNoticeBoardList(DashboardResponse resp, String rocId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			List<EstatmentVo> noticeBoardList = new ArrayList<>();
			
			if (!StringUtils.isEmpty(rocId)) {
				EstatmentVo estatmentVo = new EstatmentVo();
				estatmentVo.setRocId(rocId);
				noticeBoardList = estatmentService.getNoticeBoardList(estatmentVo);
			}
			
			BigDecimal notificationNotRead = BigDecimal.ZERO;  // 未讀數量
			List<String> noticeMonthList = new LinkedList<>(); // 通知月份
			if (!CollectionUtils.isEmpty(noticeBoardList)) {
				for (EstatmentVo vo : noticeBoardList) {
					String noticeMonth = sdf.format(vo.getCreateDate());
					if (!noticeMonthList.contains(noticeMonth)) {
						noticeMonthList.add(noticeMonth);
					}
					
					if (vo.getStatus().equals(BigDecimal.ZERO)) {
						notificationNotRead = notificationNotRead.add(BigDecimal.ONE);
					}
				}
			}
			
			resp.setNoticeBoardList(noticeBoardList);
			resp.setNotificationNotRead(notificationNotRead);
			resp.setNoticeMonthList(noticeMonthList);
		} catch (Exception e) {
			logger.error("Unable to setNoticeBoardList: {}", ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * 設定一個月內申請進度清單.
	 * 
	 * @param resp DashboardResponse
	 * @param policyNos
	 */
	private void setOnlineChangeView(DashboardResponse resp, String rocId) {
		try {
			List<TransVo> applyProcessingList = applyProgressService.getChangeHistoryList(rocId,
					OnlineChangeUtil.TRANS_STATUS_PROCESSING);
			List<TransVo> applyCompleteList = applyProgressService.getChangeHistoryList(rocId,
					OnlineChangeUtil.TRANS_STATUS_COMPLETE);
			
			resp.setApplyCompleteList(applyCompleteList);
			resp.setApplyProcessingList(applyProcessingList);
		} catch (Exception e) {
			logger.error("Unable to setOnlineChangeView: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
