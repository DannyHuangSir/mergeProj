package com.twfhclife.eservice.onlineChange.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TravelPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TableGetVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;
import com.twfhclife.eservice.onlineChange.model.TravelPlanVo;
import com.twfhclife.eservice.onlineChange.model.TravelPolicyVo;
import com.twfhclife.eservice.onlineChange.service.ITravelPolicyService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.model.BeneficiaryVo;
import com.twfhclife.eservice.web.model.InsuredVo;
import com.twfhclife.eservice.web.model.ProposerVo;
import com.twfhclife.eservice.web.model.UserTermVo;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyJacksonUtil;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.generic.util.PdfUtil;
import com.twfhclife.generic.util.RptUtils;

@Service
@Transactional
public class TravelPolicyServiceImpl implements ITravelPolicyService {

	private static final Logger logger = LogManager.getLogger(TravelPolicyServiceImpl.class);
	
	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	private TravelPolicyDao travelPolicyDao;
	
	@Autowired
	private UsersDao userDao;
	
	@Override
	public TravelPlanVo getPremiumCount(TravelPlanVo userPlan, int age, int days){
		List<TravelPlanVo> plans = travelPolicyDao.getPremiumCount(userPlan, age, days);
		TravelPlanVo plan = plans.get(0);
		return plan;
	}
	
	@Override
	public synchronized String addTravelPolicyTrans(TransVo vo) {
		String message = "";
		Map<String, Object> params = new HashMap<String, Object>();
	    params.put("transNum", null);
	    try{
			onlineChangeDao.getTransNum(params);
		    vo.setTransNum(params.get("transNum").toString());
		    
		    //線上申請-完成狀態 
		    vo.setStatus(OnlineChangeUtil.TRANS_STATUS_COMPLETE);
		    
		    //未登入時使用需指定一個userId
		    if (MyStringUtil.isNullOrEmpty(vo.getUserId())) {
		    	vo.setUserId("travelPolicy");
		    }
		    
		    //新增至主表
		    onlineChangeDao.addTrans(vo);
		    
		    //新增至detail
		    if(TransTypeUtil.TRAVEL_POLICY_PARAMETER_CODE.equals(vo.getTransType())){	
		    	//旅行平安保險
		    	travelPolicyDao.addTransTravelPolicy(vo);
		    	travelPolicyDao.addTransTravelPolicyHolder(vo);
		    	travelPolicyDao.addTransTravelPolicyInsured(vo);
		    	travelPolicyDao.addTransTravelPolicyBene(vo);
		    	travelPolicyDao.addTransTravelPolicyExt(vo);
		    }else{
				logger.info("線上申請類別-旅平險-錯誤! transType 為 {}",vo.getTransType());
			}
			
			// TODO:save term
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			if (request.getSession().getAttribute(ApConstants.USER_TERM_TRAVEL_POLICY) != null) {
				Date termDate = (Date) request.getSession().getAttribute(ApConstants.USER_TERM_TRAVEL_POLICY);
				UserTermVo userTerm = new UserTermVo();
				userTerm.setUserId(vo.getTravelPolicy().getProposer().getIdentityId());
				userTerm.setAcceptDate(termDate);
				userTerm.setTermName(ApConstants.USER_TERM_TRAVEL_POLICY);
				userTerm.setMemo("transNum=" + vo.getTransNum());
				userDao.createUserTerm(userTerm);
			}
			
		    message = ApConstants.SUCCESS;
	    }catch(Exception e){
	    	message = e.getMessage();
	    	throw e;
	    }
		return message;
	}
	
	@Override
	public List<TravelPlanVo> createPlan(TravelPolicyVo vo) throws Exception {
		TravelPlanVo userPlan = vo.getTravelPlan();
		//計算保費
    	//處理天數
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date startDate = sdf.parse(vo.getBeginDate()+" "+vo.getBeginTime()+":00:00");
    	Date endDate = sdf.parse(vo.getEndDate()+" "+vo.getEndTime()+":00:00");
    	
    	//計算旅行天數
//    	long dateElse = (endDate.getTime() - startDate.getTime()) % (1000*3600*24);
    	long dateChang = (endDate.getTime() - startDate.getTime()) / (1000*3600*24);
//    	int dateList = (int)dateChang;
//		int days = dateElse != 0 ? dateList + 1 : dateList;
		int days = (int)dateChang + 1;
		vo.setTravelDay(days);
		
		//計算意外險與海外醫療險等 需要考慮年齡
	   	 BigDecimal percentMrid = null;
	   	 BigDecimal percentOrid = null;
	   	//要保人是否未滿15(滿15 = 40%(不可超過200) 未滿 = 70%)
	   	if(userPlan.getAge() >=15){
	   		percentMrid = new BigDecimal("0.40");
	   		percentOrid = new BigDecimal("0.40");
	   	}else{
	   		percentMrid = new BigDecimal("0.70");
	   		percentOrid = new BigDecimal("0.70");
	   	}
   	
		BigDecimal[] addid = new BigDecimal[3];
		BigDecimal[] mrid = new BigDecimal[3];
		BigDecimal[] orid = new BigDecimal[3];
		if (userPlan.getAge() < 15) {
			//依使用者選擇的保費給予預設方案  0-100 = 小資方案(0) / 100-200 = 放心(1) / 200 = 暢行(2)
			addid[0] = new BigDecimal(40);
			mrid [0] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 10);
			orid [0] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 10);
			
			addid[1] = new BigDecimal(100);
			mrid [1] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 20);
			orid [1] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 40);
			
			addid[2] = new BigDecimal(200);
			mrid [2] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 50);
			orid [2] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 80);
			
			if(userPlan.getAddid().intValue() < 100 ){
				userPlan.setPlanType("1");
				vo.setPlanType("1");
	    	}else if(userPlan.getAddid().intValue() >= 100 && userPlan.getAddid().intValue() < 200){
	    		userPlan.setPlanType("2");
				vo.setPlanType("2");
	    	}else{
	    		userPlan.setPlanType("3");
				vo.setPlanType("3");
	    	}
		} else {
			//依使用者選擇的保費給予預設方案 20-400 = 小資方案(0) / 500-900 = 放心(1) / 1000以上 = 暢行(2)
			int medium = userPlan.getAge() <= 70 ? 500 : 200;
			int maxmium = userPlan.getAge() <= 70 ? 1000 : 400;
			
			addid[0] = new BigDecimal(100);
			mrid [0] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 10);
			orid [0] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 10);
			
			addid[1] = new BigDecimal(medium);
			mrid [1] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 20);
			orid [1] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 50);
			
			addid[2] = new BigDecimal(maxmium);
			mrid [2] = new BigDecimal(userPlan.getMrid().intValue() == 0 ? 0 : 50);
			orid [2] = new BigDecimal(vo.getTravelDestType().equals("國內") ? 0 : 100);
			
			if(userPlan.getAddid().intValue() < medium ){
				userPlan.setPlanType("1");
				vo.setPlanType("1");
	    	}else if(userPlan.getAddid().intValue() >= medium && userPlan.getAddid().intValue() < maxmium){
	    		userPlan.setPlanType("2");
				vo.setPlanType("2");
	    	}else{
	    		userPlan.setPlanType("3");
				vo.setPlanType("3");
	    	}
		}
		
		List<TravelPlanVo> planList = new ArrayList<TravelPlanVo>();
		for (int i=0; i<addid.length; i++) {
			TravelPlanVo plan = new TravelPlanVo();
			plan.setPlanType(String.valueOf(i + 1));
	    	plan.setPlanTypeStr(i == 0 ? "小資方案" : i == 1 ? "放心方案" : "暢行方案");
		    plan.setAge(userPlan.getAge());
		    if (String.valueOf(i + 1).equals(userPlan.getPlanType())) {
		    	plan.setAddid(userPlan.getAddid());
		    	//還是要判斷一下 使用者輸入值不可大於 可用的保費值
    			if(userPlan.getAddid().multiply(percentMrid).intValue() < userPlan.getMrid().intValue()){
    				//如果大於給計算值
    				plan.setMrid(userPlan.getAddid().multiply(percentMrid));
    			}else{
    				plan.setMrid(userPlan.getMrid());
    			}
    			
    			if(userPlan.getAddid().multiply(percentOrid).intValue() < userPlan.getOrid().intValue()){
    				//如果大於給計算值
    				plan.setOrid(userPlan.getAddid().multiply(percentOrid));
    			}else{
    				plan.setOrid(userPlan.getOrid());
    			}
		    } else {
			    plan.setAddid(addid[i]);
			    plan.setMrid(mrid[i]);
			    plan.setOrid(orid[i]);
		    }
		    
		    TravelPlanVo premiumPlan = this.getPremiumCount(plan, userPlan.getAge(), days);
		    plan.setAddidPremium(new BigDecimal(premiumPlan.getAddid().intValue()));
	    	plan.setMridPremium(new BigDecimal(premiumPlan.getMrid().intValue()));
	    	plan.setOridPremium(new BigDecimal(premiumPlan.getOrid().intValue()));
		    plan.setPremium(premiumPlan.getAddid().add(premiumPlan.getMrid()).add(premiumPlan.getOrid()));
		    planList.add(plan);
		    
		}
		return planList;
		
	}
	
	/**
	 * 取得旅平險詳細畫面資料
	 * @param transType
	 * @return
	 */
	@Override
	public TableGetVo getTransDetail(String transType, String transNum) {
		TransVo trans = new TransVo();
		TableGetVo tableGet = new TableGetVo();
		//旅平險
		TravelPolicyVo vo = travelPolicyDao.getTravelPolicyDetial(transNum);
		trans.setTravelPolicy(vo);
		logger.debug("***policyTravalVo***:" + MyJacksonUtil.object2Json(vo));
		tableGet = getTravelPolicyHtml(trans);
		return tableGet;
	}
	
	/**
	 * 旅平險(僅顯示)
	 * @param trans
	 * @return
	 */
	public TableGetVo getTravelPolicyHtml(TransVo trans){
		TableGetVo tableGet = new TableGetVo();
		
		List<Map<String, Object>> doubleObject = new ArrayList<Map<String, Object>>();
		List<Map<String, String>> object = null;
		
		Map<String, Object>	doubleMap = null;
		Map<String, String>	map = null;
		
		TravelPolicyVo travelPolicy = trans.getTravelPolicy();
		
		object = new ArrayList<Map<String, String>>();
		doubleMap = new HashMap<String, Object>();
		
		/** 保單資訊 */
//		doubleMap.put("parTitle", "要保書基本內容");
		
//		map = new HashMap<String, String>();
//		map.put("keyColspan", "辦理投保事宜之地點為");
//		map.put("valueColspan", travelPolicy.getWnpiDeptStr());
//		object.add(map);
		
		map = new HashMap<String, String>();
		map.put("keyColspan", "保險金額");
		map.put("valueColspan", travelPolicy.getTravelPlan().getAddid().toString()+" 萬元");
		object.add(map);
		
		/**
		 * 計算
		 */
//		Date birthday = travelPolicy.getInsured().getBirthday();
//    	Calendar nowCal = Calendar.getInstance();
//    	Calendar birthdayCal = Calendar.getInstance();
//    	birthdayCal.setTime(birthday);
//    	int age = nowCal.get(Calendar.YEAR) - birthdayCal.get(Calendar.YEAR) ;
//    	//判斷今年的生日過了沒，沒過少算一歲。
//    	birthdayCal.set(Calendar.YEAR,nowCal.get(Calendar.YEAR));
//    	if(nowCal.getTime().getTime()<birthdayCal.getTime().getTime()){
//    	  age--;
//    	}
		
//    	String additional = "";
//    	String additionalTit = "";
//    	String additionalCon = "";
//    	String additionalCon2 = "";
//    	if(age >= 15 && age <=80){
//    		additionalCon = "15足歲~80歲";
//    		additionalCon2 = "40%";
//    	}else{
//    		additionalCon = "未滿15足歲";
//    		additionalCon2 = "70%";
//    	}
//    	if(!travelPolicy.getTravelPlan().getMrid().toString().equals("0")){
//    		additionalTit = "意外傷害醫療";
//    	}
//    	if(!travelPolicy.getTravelPlan().getOrid().toString().equals("0")){
//    		if(!additionalTit.equals("")){
//    			additionalTit = additionalTit + "＋";
//    		}
//    		additionalTit = additionalTit + "海外突發疾病醫療";
//    	}
//    	
//    	if(travelPolicy.getTravelPlan().getMrid() == new BigDecimal(0) && travelPolicy.getTravelPlan().getOrid() == new BigDecimal(0)){
//    		additional = "不附加";
//    	}else{
//    		additionalTit = additionalTit + "給付";
//	    	additional = additionalTit + " : "+additionalCon + "，最高為保險金額之"+additionalCon2+" (不超過200萬)";
//    	}
//    	map = new HashMap<String, String>();
//		map.put("keyColspan", "附加保險");
//		map.put("valueColspan", additional);
//		object.add(map);
		
		map = new HashMap<String, String>();
		map.put("keyColspan2", "意外傷害醫療給付保險金限額");
		map.put("valueColspan2", travelPolicy.getTravelPlan().getMrid().toString()+" 萬元");
		object.add(map);
		
		map = new HashMap<String, String>();
		map.put("keyColspan2", "海外突發疾病醫療給付保險金限額");
		map.put("valueColspan2", travelPolicy.getTravelPlan().getOrid().toString()+" 萬元");
		object.add(map);
		
		map = new HashMap<String, String>();
//		Date startDate = new Date();
//		Date endDate = new Date();
//		try {
//			//處理天數
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			startDate = sdf.parse(travelPolicy.getBeginDate()+" "+travelPolicy.getBeginTime()+":00");
//			endDate = sdf.parse(travelPolicy.getEndDate()+" "+travelPolicy.getEndTime()+":00");
//		} catch (ParseException e) {
//			logger.error(e.getMessage(), e);
//		}
		
    	//計算旅行天數
//    	long dateElse = (endDate.getTime() - startDate.getTime()) % (1000*3600*24);
//    	long dateChang = (endDate.getTime() - startDate.getTime()) / (1000*3600*24);
//    	int dateList = (int)dateChang;
//		int days = dateElse != 0 ? dateList + 1 : dateList;
		
		map.put("keyColspan", "保險期間");
		map.put("valueColspan", travelPolicy.getBeginDate()+" "+travelPolicy.getBeginTime()+":00  至 "+
				travelPolicy.getEndDate()+" "+travelPolicy.getEndTime()+":00");
		object.add(map);
		
		map = new HashMap<String, String>();
		map.put("key", "旅行目的地");
		map.put("value", travelPolicy.getTravelDest());
//		/** 還不會算 */
//		map.put("key2", "保險費合計");
//		map.put("value2", travelPolicy.getTravelPlan().getPremium() + "元");
//		object.add(map);
		
		doubleMap.put("trans", object);
		doubleObject.add(doubleMap);
		
//		/** 要保人資訊 */
//		object = new ArrayList<Map<String, String>>();
//		doubleMap = new HashMap<String, Object>();
//		
//		doubleMap.put("parTitle", "要保人資訊");
//		
//		map = new HashMap<String, String>();
//		map.put("key", "要保人姓名");
//		map.put("value", travelPolicy.getProposer().getProposer());
//		map.put("key2", "要保人身分證字號");
//		map.put("value2", travelPolicy.getProposer().getIdentityId());
//		map.put("key", "與被保險人關係");
//		map.put("value2", travelPolicy.getProposer().getRelationToInsured());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//		map.put("keyColspan", "生日");
//		map.put("valueColspan", sdf.format(travelPolicy.getProposer().getBirthday()));
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("keyColspan", "要保人地址");
//		map.put("valueColspan", travelPolicy.getProposer().getAddress());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("key", "聯絡電話(公)");
//		map.put("value", travelPolicy.getProposer().getOfficeTel());
//		map.put("key2", "聯絡電話(宅)");
//		map.put("value2", travelPolicy.getProposer().getHomeTel());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("key", "聯絡電話(行動)");
//		map.put("value", travelPolicy.getProposer().getCellphoneNum());
//		map.put("key2", "E-mail");
//		map.put("value2", travelPolicy.getProposer().getEmail());
//		object.add(map);
//		
//		doubleMap.put("trans", object);
//		doubleObject.add(doubleMap);
//		
//		/** 被保人資訊 */
//		object = new ArrayList<Map<String, String>>();
//		doubleMap = new HashMap<String, Object>();
//		
//		doubleMap.put("parTitle", "被保人資訊");
//
//		map = new HashMap<String, String>();
//		map.put("key", "被保險人姓名");
//		map.put("value", travelPolicy.getInsured().getInsuredName());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("key", "被保人身分證字號");
//		map.put("value", travelPolicy.getInsured().getIdentityId());
//		map.put("key2", "生日");
//		map.put("value2", sdf.format(travelPolicy.getInsured().getBirthday()));
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("keyColspan", "職業");
//		map.put("valueColspan", travelPolicy.getInsured().getJob());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("keyColspan", "被保人地址");
//		map.put("valueColspan", travelPolicy.getInsured().getAddress());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("key", "聯絡電話(公)");
//		map.put("value", travelPolicy.getInsured().getOfficeTel());
//		map.put("key2", "聯絡電話(宅)");
//		map.put("value2", travelPolicy.getInsured().getHomeTel());
//		object.add(map);
//		
//		map = new HashMap<String, String>();
//		map.put("keyColspan", "聯絡電話(行動)");
//		map.put("valueColspan", travelPolicy.getInsured().getCellphoneNum());
//		object.add(map);
//		
//		doubleMap.put("trans", object);
//		doubleObject.add(doubleMap);
//		
//		/** 受益人資訊 */
//		object = new ArrayList<Map<String, String>>();
//		doubleMap = new HashMap<String, Object>();
//		
//		doubleMap.put("parTitle", "受益人資訊");
//		
//		map = new HashMap<String, String>();
//		map.put("key", "受益人是否為法定繼承人");
//		map.put("value", "Y".equals(travelPolicy.getBeneficiary().getLegalInherit()) ? "是" : "否");
//		if ("N".equals(travelPolicy.getBeneficiary().getLegalInherit())) {
//			map.put("key", "受益人姓名");
//			map.put("value", travelPolicy.getBeneficiary().getBeneficiaryName());
//			map.put("key2", "受益人身分證字號");
//			map.put("value2", travelPolicy.getBeneficiary().getBeneficiaryRocid());
//			object.add(map);
//			
//			map = new HashMap<String, String>();
//			map.put("keyColspan", "與被保險人關係");
//			map.put("valueColspan", travelPolicy.getBeneficiary().getBeneficiaryRelationStr());
//			object.add(map);
//
//			map = new HashMap<String, String>();
//			map.put("keyColspan2", "身故受益人非被保險人之直系血親配偶或兄弟姊妹者，請註明原因");
//			map.put("valueColspan2", travelPolicy.getBeneficiary().getReason());
//			object.add(map);
//			
//			map = new HashMap<String, String>();
//			map.put("keyColspan", "受益人地址");
//			map.put("valueColspan", travelPolicy.getBeneficiary().getBeneficiaryAddressFull());
//			object.add(map);
//			
//			map = new HashMap<String, String>();
//			map.put("keyColspan", "受益人電話");
//			map.put("valueColspan", travelPolicy.getBeneficiary().getBeneficiaryTel());
//			object.add(map);
//		}
//		doubleMap.put("trans", object);
//		doubleObject.add(doubleMap);
//		
//		/** 其他資訊 */
//		object = new ArrayList<Map<String, String>>();
//		doubleMap = new HashMap<String, Object>();
//		
//		doubleMap.put("parTitle", "其他資訊");
//		
//		map = new HashMap<String, String>();
//		if(travelPolicy.getOtherTravel().equals("Y")){
//			map.put("keyColspan3", "就本次旅程，被保險人是否已投保其他旅行平安保險(保括本公司或其他公司之各種保險)");
//			map.put("valueColspan3", "是");
//			object.add(map);
//			
//			map = new HashMap<String, String>();
//			map.put("key", "保險公司名稱");
//			map.put("value", travelPolicy.getOtherTravelCompany());
//			map.put("key2", "保險金額(新台幣)");
//			map.put("value2", travelPolicy.getOtherAmtWrap().toString()+" 萬元");
//			object.add(map);
//		}else{
//			map.put("keyColspan3", "就本次旅程，被保險人是否已投保其他旅行平安保險(保括本公司或其他公司之各種保險)");
//			map.put("valueColspan3", "否");
//			object.add(map);
//		}
//		
//		doubleMap.put("trans", object);
//		doubleObject.add(doubleMap);

		tableGet.setTransDoubleObject(doubleObject);
		
		return tableGet;
	}

	@Override
	public byte[] getPDF(TravelPolicyVo travelPolicyVo) {
		byte[] pdfByte = null;
		try {
			RptUtils rptUtils = new RptUtils("travelPolicy.pdf");
			
			/* ======================================== 被保人 ========================================*/
			// 被保人
			InsuredVo insured = travelPolicyVo.getInsured();
			// 姓名
			rptUtils.txt(insured.getInsuredName(), 11, 1, 130, 638);
			// 身分證字號
			String insuredId = insured.getIdentityId();
			int a = 17;
			int lenInsuredId = insuredId.length();
			for (int i=0; i<lenInsuredId; i++) {
				rptUtils.txt(insuredId.substring(i, i + 1), 11, 1, 405 + (a * i), 638);
			}
			// 性別
			if (lenInsuredId == 10) {
				if ("1".equals(insuredId.substring(1, 2))) {
					rptUtils.txt("■", 11, 1, 299, 638);
				} else {
					rptUtils.txt("■", 11, 1, 324, 638);
				}
			}
			// 生日
			Date birthDate = insured.getBirthday();
			rptUtils.txt(String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(birthDate)) - 1911), 11, 1, 130, 616); 
			rptUtils.txt(new SimpleDateFormat("MM").format(birthDate), 11, 1, 175, 616); 
			rptUtils.txt(new SimpleDateFormat("dd").format(birthDate), 11, 1, 220, 616); 
			// 歲數
			String age = String.valueOf(travelPolicyVo.getTravelPlan().getAge());
			rptUtils.txt(age, 11, 1, 306, 615);
			// 電話
			rptUtils.txt(insured.getCellphoneNum(), 11, 1, 405, 615); 
			// 職業
//			rptUtils.txt(insured.getJob(), 11, 1, 310, 560); 
			// 住址
			rptUtils.txt(insured.getAddress(), 11, 1, 140, 593);
			// 目前是否受有監護宣告
			rptUtils.txt("■", 11, 1, "0".equals(insured.getWnpiAnnounce()) ? 81 : 51, 572);
			/* ======================================== 被保人 ========================================*/
			

			/* ======================================== 要保人 ========================================*/
			
			// 要保人
			ProposerVo policyHolder = travelPolicyVo.getProposer();
			// 與被保險人關係
			if ("本人".equals(policyHolder.getRelationToInsured())) {
				rptUtils.txt("■", 11, 1, 105, 556);// 本人
			} else if ("父母".equals(policyHolder.getRelationToInsured())) {
				rptUtils.txt("■", 11, 1, 301, 556);// 父母
			} else if ("配偶".equals(policyHolder.getRelationToInsured())) {
				rptUtils.txt("■", 11, 1, 336, 556);// 配偶
			} else if ("子女".equals(policyHolder.getRelationToInsured())) {
				rptUtils.txt("■", 11, 1, 371, 556);// 子女
			} else {
				rptUtils.txt("■", 11, 1, 406, 556);// 其他
				rptUtils.txt(policyHolder.getRelationToInsured(), 11, 1, 450, 557);// 其他
			}
			// 姓名
			rptUtils.txt(policyHolder.getProposer(), 11, 1, 130, 536);
			// 地址
			if ("1".equals(policyHolder.getSameAddr())) {
				rptUtils.txt("■", 9, 1, 105, 491);// 同被保人住所
			} else {
				rptUtils.txt(policyHolder.getAddress(), 11, 1, 230, 491);
			}
			
			
			// 生日
			Date bDate = policyHolder.getBirthday();
			rptUtils.txt(String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(bDate)) - 1911), 11, 1, 130, 513); 
			rptUtils.txt(new SimpleDateFormat("MM").format(bDate), 11, 1, 175, 513); 
			rptUtils.txt(new SimpleDateFormat("dd").format(bDate), 11, 1, 220, 513); 
			// 電話
			rptUtils.txt(policyHolder.getCellphoneNum(), 11, 1, 405, 513); 
			// 身分證字號
			String rocId = policyHolder.getIdentityId();
			int x = 17;
			int len = rocId.length();
			for (int i=0; i<len; i++) {
				rptUtils.txt(rocId.substring(i, i + 1), 11, 1, 405 + (x * i), 536);
			}
			// 性別
			if (len == 10) {
				if ("1".equals(rocId.substring(1, 2))) {
					rptUtils.txt("■", 11, 1, 299, 536);
				} else {
					rptUtils.txt("■", 11, 1, 324, 536);
				}
			}
			//E-mail
			rptUtils.txt(StringUtils.trimToEmpty(policyHolder.getEmail()), 11, 1, 130, 468);
			// 國籍
			if ("中華民國".equals(policyHolder.getCountry())) {
				rptUtils.txt("■", 11, 1, 400, 469);
			} else {
				rptUtils.txt("■", 11, 1, 470, 469);
				rptUtils.txt(policyHolder.getCountry(), 10, 1, 505, 470);
			}
			/* ======================================== 要保人 ========================================*/
			
			/* ======================================== 投保內容 ========================================*/
			// 保險始期
			String[] startDate = travelPolicyVo.getBeginDate().split("/");
			rptUtils.txt(Integer.parseInt(startDate[0]) - 1911 + "", 11, 1, 158, 430); 
			rptUtils.txt(startDate[1], 11, 1, 200, 430); 
			rptUtils.txt(startDate[2], 11, 1, 245, 430); 
			String startTime = travelPolicyVo.getBeginTime();
			rptUtils.txt(startTime, 11, 1, 305, 430); 
			// 保險終止
			String[] endDate = travelPolicyVo.getEndDate().split("/");
			rptUtils.txt(Integer.parseInt(endDate[0]) - 1911 + "", 11, 1, 158, 414); 
			rptUtils.txt(endDate[1], 11, 1, 200, 414); 
			rptUtils.txt(endDate[2], 11, 1, 245, 414); 
			String endTime = travelPolicyVo.getEndTime();
			rptUtils.txt(endTime, 11, 1, 305, 414); 
			// 旅行天數
			rptUtils.txt(String.valueOf(travelPolicyVo.getTravelDay()), 11, 1, 420, 422); 
			// 其他保險公司名稱
			rptUtils.txt(StringUtils.trimToEmpty(travelPolicyVo.getOtherTravelCompany()), 11, 1, 365, 338); 
			// 其他保險金額
			rptUtils.txt(String.valueOf(travelPolicyVo.getOtherAmtWrap() == null ? "" : travelPolicyVo.getOtherAmtWrap()), 11, 1, 500, 338); 
			// 已審閱
			rptUtils.txt("■", 10, 1, 38, 147);
			
			// 旅平險保單方案內容
			TravelPlanVo travelPlan = travelPolicyVo.getTravelPlan();
			// 保險金額
			rptUtils.txt(String.valueOf(travelPlan.getAddid()), 11, 1, 118, 395);
			// 保險費
			rptUtils.txt(String.valueOf(travelPlan.getAddidPremium()), 11, 1, 118, 367);
			// 意外傷害醫療給付
			if (travelPlan.getMrid() != null && travelPlan.getMrid().intValue() > 0) {
				rptUtils.txt("■", 10, 1, 192, 395);
				rptUtils.txt(String.valueOf(travelPlan.getMrid()), 10, 1, 390, 400);
				rptUtils.txt(String.valueOf(travelPlan.getMridPremium()), 10, 1, 470, 400);
			}
			// 海外突發醫療給付
			if (travelPlan.getOrid() != null && travelPlan.getOrid().intValue() > 0) {
				rptUtils.txt("■", 10, 1, 192, 367);
				rptUtils.txt(String.valueOf(travelPlan.getOrid()), 10, 1, 390, 370);
				rptUtils.txt(String.valueOf(travelPlan.getOridPremium()), 10, 1, 470, 370);
			}
			// 保險費合計
			rptUtils.txt(String.valueOf(travelPlan.getPremium()), 11, 1, 523, 362);
			// 旅行地點
			rptUtils.txt("■", 11, 1, "國內".equals(travelPolicyVo.getTravelDestType()) ? 105 : 304, 448);// 國內/外
			rptUtils.txt(StringUtils.trimToEmpty(travelPolicyVo.getTravelDest()), 11, 1, "國內".equals(travelPolicyVo.getTravelDestType()) ? 190 : 390, 450);
			/* ======================================== 投保內容 ========================================*/
			
			/* ======================================== 受益人 ========================================*/
			if ("N".equals(travelPolicyVo.getBeneficiary().getLegalInherit())) {
				// 受益人
				BeneficiaryVo beneficiary = travelPolicyVo.getBeneficiary();
				// 姓名
				rptUtils.txt(beneficiary.getBeneficiaryName(), 11, 1, 140, 280); 
				// 生日String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(beneficiary.getBeniBirthday())) - 1911)
				rptUtils.txt(
						String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(beneficiary.getBeniBirthday())) - 1911)
								+ new SimpleDateFormat("-MM-dd").format(beneficiary.getBeniBirthday()), 11, 1, 220, 280);
				// 身分證字號
				rptUtils.txt(beneficiary.getBeneficiaryRocid(), 11, 1, 305, 280); 
				// 國籍
				if (beneficiary.getCountry().indexOf(" ") != -1) {
					int length = beneficiary.getCountry().split(" ").length;
					for (int i=0; i<length; i++) {
						rptUtils.txt(beneficiary.getCountry().split(" ")[i], 10, 1, 382, 280-(i*9));
					}
				} else {
					rptUtils.txt(beneficiary.getCountry(), 10, 1, 383, 280);
				}
				// 與被保人關係
				rptUtils.txt(beneficiary.getBeneficiaryRelationStr(), 11, 1, 443, 280); 
				// 姓名/地址/電話
				StringBuilder str = new StringBuilder();
				str.append(beneficiary.getBeneficiaryName()).append(" / ")
						.append(beneficiary.getBeneficiaryAddressFull()).append(" / ")
						.append(beneficiary.getBeneficiaryTel());
				rptUtils.txt(str.toString(), 11, 1, 180, 225); 
			} else {
				// 法定繼承人
				rptUtils.txt("法定繼承人", 11, 1, 140, 280); 
			}

			/* ======================================== 受益人 ========================================*/
			
			pdfByte= rptUtils.toPdf();
			//使用 PdfUtil 加密, key=要保人身份證字號
			pdfByte = new PdfUtil().doEncryption(pdfByte, travelPolicyVo.getProposer().getIdentityId());
		} catch (Exception e) {
			logger.error("Unable to getPDF: {}", ExceptionUtils.getStackTrace(e));
		}
		return pdfByte;
	}
	
}
