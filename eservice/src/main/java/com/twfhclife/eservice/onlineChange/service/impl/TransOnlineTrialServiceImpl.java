package com.twfhclife.eservice.onlineChange.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twfhclife.eservice.onlineChange.model.OnlineTrialVo;
import com.twfhclife.eservice.onlineChange.model.TransOnlineTrialVo;
import com.twfhclife.eservice.onlineChange.service.ITransOnlineTrialService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangMsgUtil;
import com.twfhclife.generic.api_client.TransCspApiUtilClient;
import com.twfhclife.generic.api_client.TransCtcLilipmClient;
import com.twfhclife.generic.api_client.TransCtcSelectUtilClient;
import com.twfhclife.generic.api_model.TransCsp002DataRequest;
import com.twfhclife.generic.api_model.TransCsp002DataResponse;
import com.twfhclife.generic.api_model.TransCtcLilipmRequest;
import com.twfhclife.generic.api_model.TransCtcLilipmResponse;
import com.twfhclife.generic.api_model.TransCtcLilipmVo;
import com.twfhclife.generic.api_model.TransCtcSelectDataResponse;
import com.twfhclife.generic.api_model.TransCtcSelectDataVo;
import com.twfhclife.generic.api_model.TransCtcSelectUtilRequest;
import com.twfhclife.generic.util.DateUtil;

@Service
public class TransOnlineTrialServiceImpl implements ITransOnlineTrialService {

	private final static String STRING_FORM_DATE = "yyyy/MM/dd hh:mm:ss";

	private final static String STRING_DATE = "yyyy-MM-dd hh:mm:ss";

	@Autowired
	private TransCtcSelectUtilClient transCtcSelectUtilClient;

	@Autowired
	private TransCtcLilipmClient transCtcLilipmClient;

	@Autowired
	private TransCspApiUtilClient transCspApiUtilClient;

	@Override
	public List<OnlineTrialVo> getPolicyDetail(String lipmId, String userId, String transType) {

		List<OnlineTrialVo> onlineTrialVoList = new ArrayList<OnlineTrialVo>();
		// 查詢CTC獲得保單資料
		TransCtcSelectUtilRequest transCtcSelectUtilRequest = new TransCtcSelectUtilRequest();
		transCtcSelectUtilRequest.setLipmId(lipmId);
		TransCtcSelectDataResponse resp = transCtcSelectUtilClient.getTransCtcSelectDataByLipmId(transCtcSelectUtilRequest);
		List<TransCtcSelectDataVo> selectUtilList = resp.getSelectDataList();
		if(selectUtilList.size() > 0 ) {
			Map<String , TransCtcSelectDataVo> map = selectUtilList.stream()
					.collect(Collectors.toMap(TransCtcSelectDataVo :: getLipmInsuNo, Function.identity(),(o1 , o2) -> o1 , ConcurrentHashMap :: new));
			List<TransCtcSelectDataVo> policyList = map.values().stream().collect(Collectors.toList());
			
			for (TransCtcSelectDataVo vo : policyList) {
				OnlineTrialVo onlineTrialVo = new OnlineTrialVo();
				// stop1 : 參數轉換
				// LIPM_INSU_NO保號
				onlineTrialVo.setLipmInsuNo(vo.getLipmInsuNo());
				// ASSN_ARRI_DATE 簽收日
				onlineTrialVo.setAssnArriDate(getStringDate(vo.getAssnArriDate()));
				// sett_ch_name 商品名稱
				onlineTrialVo.setSettChName(vo.getSettChName());
				// LIPM_NAME 要保人
				String lipmName = vo.getLipmName1();
				onlineTrialVo.setLipmName(lipmName.replace("　　", ""));
				// LIPI_NAME 主被保人姓名
				String lipiName = vo.getLipiName();
				onlineTrialVo.setLipiName(lipiName.replace("　　", ""));
				// LIPM_INSU_BEG_DATE 保單生效日
				onlineTrialVo.setLipmInsuBegDate(getStringDate(vo.getLipmInsuBegDate()));
				// LIPI_MAIN_AMT保額
				onlineTrialVo.setLipiMainAmt(vo.getLipiMainAmt());
				// LIPI_TABL_PREM 每期保費
				onlineTrialVo.setLipiTablPrem(vo.getLipiTablPrem());
				// PROD_CURRENCY幣別
				onlineTrialVo.setProdCurrency(vo.getProdCurrency());
				// LIPM_RCP_CODE繳別
				onlineTrialVo.setLipmRcpCode(vo.getLipmRcpCode());
				// LIPI_ST_DATE 繳清生效日
				onlineTrialVo.setLipiStDate(vo.getLipiStDate());
				// stop2 : 保單契約內有可撤銷條款之商品
				if (StringUtils.isNotEmpty(vo.getLipmSt())) {
					if (!"00".equals(vo.getLipmSt()) && !"15".equals(vo.getLipmSt()) && !"17".equals(vo.getLipmSt())) {
						onlineTrialVo.setApplyLockedFlag("Y");
						onlineTrialVo.setApplyLockedMsg("不符合可試算的契況條件。");
					}
				} else {
					onlineTrialVo.setApplyLockedFlag("Y");
					onlineTrialVo.setApplyLockedMsg("不符合可試算的契況條件。");
				}
				onlineTrialVoList.add(onlineTrialVo);
			}
		}
		
		return onlineTrialVoList;
	}

	public String getStringDate(String date) {
		try {
			if (StringUtils.isNotEmpty(date)) {
				SimpleDateFormat formater = new SimpleDateFormat(STRING_FORM_DATE);
				return DateUtil.getRocDate(formater.parse(date));
			}
		} catch (ParseException e) {
			SimpleDateFormat formater = new SimpleDateFormat(STRING_DATE);
			try {
				return DateUtil.getRocDate(formater.parse(date));
			} catch (ParseException e1) {
				return null;
			}
		}
		return null;
	}

	@Override
	public TransOnlineTrialVo getOnlineTrialDetail(String lipmInsuSeqNo) {
		TransOnlineTrialVo transOnlineTrialVo = new TransOnlineTrialVo();
		TransCtcLilipmRequest request = new TransCtcLilipmRequest();
		request.setLipmInsuSeqNo(lipmInsuSeqNo);
		TransCtcLilipmResponse response = transCtcLilipmClient.getOnlineTrialDetail(request);
		TransCsp002DataRequest cspRequest = new TransCsp002DataRequest();
		cspRequest.setLipmInsuNo(lipmInsuSeqNo);
		String date = DateUtil.getRocDate(new Date());
		date = date.replace("/", "");
		cspRequest.setDate("0"+date);
		List<TransCtcLilipmVo> transCtcLilipmList = new ArrayList<>();
		transCtcLilipmList = response.getCtcLilipmListVo();
		if (transCtcLilipmList.size() > 0) {
			for (TransCtcLilipmVo vo : transCtcLilipmList) {
				// 解約金API
				// 1.險種的條件
				// LIPROD.PROD_TYPE =’1’ and LIPROD.PROD_KIND3 != '03'
				if (StringUtils.isNotEmpty(vo.getProdType())) {
					if (vo.getProdType().equals("1")
							&& (StringUtils.isEmpty(vo.getProdKind3()) || !vo.getProdKind3().equals("3"))) {
						// 2.借款及繳清的條件
						// 保單不能有借款: LIPM_LO_MK != ‘Y’
						if (StringUtils.isNotEmpty(vo.getLipmLoMk())) {
							if (!vo.getLipmLoMk().equals("Y")) {
								// 繳清生效日需已超過一年: LILIPI_ES.LIPI_ST_DATE+1年 < 系統日
								if (StringUtils.isNotEmpty(vo.getLipmStDate())) {
									cspRequest.setCancellationType(checkDate(vo));
								}
							}
						} else {
							if (StringUtils.isNotEmpty(vo.getLipmStDate())) {
								cspRequest.setCancellationType(checkDate(vo));
							}
						}
						// 設定是否要呼叫查詢可借款額度API
						cspRequest.setLoanType(true);
					}
				}
				// 設定是否要呼叫查詢滿期金API
				if (StringUtils.isEmpty(vo.getProdKind3()) || !vo.getProdKind3().equals("3")) {
					// call API
					cspRequest.setMaturityType(true);
				}
				// 設定是否要呼叫查詢已借款需還款額度API
				if (StringUtils.isNotEmpty(vo.getLipmLoMk())) {
					if (vo.getLipmLoMk().equals("Y")) {
						// call API
						cspRequest.setRepaymentType(true);
					}
				}
			}
			transOnlineTrialVo = getCsp002Detail(transOnlineTrialVo, cspRequest);
		}
		return transOnlineTrialVo;
	}

	private TransOnlineTrialVo getCsp002Detail(TransOnlineTrialVo vo, TransCsp002DataRequest cspRequest) {
		TransCsp002DataResponse csp002Response = transCspApiUtilClient.getTransCsp002Api(cspRequest);
		TransOnlineTrialVo selectVo = csp002Response.getTransOnlineTrialVo();
		if (selectVo != null) {
			if (cspRequest.isMaturityType()) {
				if (StringUtils.isNotEmpty(selectVo.getMaturityAmt())) {
					vo.setMaturityAmt(selectVo.getMaturityAmt());
				} else {
					if("無滿期金不可試算".equals(selectVo.getMaturityMsg())) {
						vo.setMaturityMsg(selectVo.getMaturityMsg());
					}else {
						vo.setMaturityMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);	
					}
				}
			}else {
				vo.setMaturityMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);				
			}
			if (cspRequest.isCancellationType()) {
				if (StringUtils.isNotEmpty(selectVo.getCancellationAmt())) {
					vo.setCancellationAmt(selectVo.getCancellationAmt());
				} else {
					vo.setCancellationMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
				}
			}else {
				vo.setCancellationMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
			}
			
			if (cspRequest.isLoanType()) {
				if (StringUtils.isNotEmpty(selectVo.getLoanAmt())) {
					if(selectVo.getLoanAmt().equals("0")) {
						vo.setLoanMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
					}else {
						vo.setLoanAmt(selectVo.getLoanAmt());
					}
				} else {
					vo.setLoanMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
				}
			}else {
				vo.setLoanMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
			}
			
			if (cspRequest.isRepaymentType()) {
				if (StringUtils.isNotEmpty(selectVo.getRepaymentAmt())) {
					vo.setRepaymentAmt(selectVo.getRepaymentAmt());
					if (StringUtils.isNotEmpty(selectVo.getRefundAccount())) {
						vo.setRefundAccount(selectVo.getRefundAccount());
					} else {
						vo.setRefundMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
					}
				} else {
					vo.setRepaymentMsg(OnlineChangMsgUtil.TRANS_ONLINE_TRIAL_ERROR);
				}
			}else {
				vo.setRepaymentMsg("保單無借款。");
			}
		}
		return vo;
	}

	private boolean checkDate(TransCtcLilipmVo vo) {
		if(vo.getLipmSt().equals("15")) {
			LocalDateTime systemDate = LocalDateTime.now();
			String stDate = vo.getLipmStDate();
			stDate = stDate.substring(0, 4);
			if ((Integer.valueOf(stDate) + 1) < systemDate.getYear()) {
				return true;
			}
		}else if(vo.getLipmSt().equals("00")){
			String arriDate = StringUtils.isNotEmpty(vo.getAssnArriDate()) ? vo.getAssnArriDate()
					: vo.getLipmInsuBegDate();
			arriDate = arriDate.replace("/", "-");
			int days = DateUtil.getDateInterval(arriDate.substring(0,10)) ;
			if (days - 10 > 0) {
				// 設定是否要呼叫查詢解約金額的API
				return true;	
			}
		}else if(vo.getLipmSt().equals("17")) {
			return true;			
		}
		return false;
	}	
	
}
