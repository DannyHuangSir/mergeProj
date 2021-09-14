package com.twfhclife.eservice.onlineChange.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.dao.OnlineChangeDao;
import com.twfhclife.eservice.onlineChange.dao.TransDao;
import com.twfhclife.eservice.onlineChange.dao.TransDnsDao;
import com.twfhclife.eservice.onlineChange.dao.TransPolicyDao;
import com.twfhclife.eservice.onlineChange.model.TransDnsVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.service.ITransDnsService;
import com.twfhclife.eservice.onlineChange.service.ITransService;
import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.onlineChange.util.TransTypeUtil;
import com.twfhclife.eservice.web.model.TransPolicyVo;
import com.twfhclife.eservice.web.model.TransVo;

@Service
public class TransDnsServiceImpl implements ITransDnsService{
	
	@Autowired
	private OnlineChangeDao onlineChangeDao;
	
	@Autowired
	private ITransService transService;
	
	@Autowired
	private TransDao transDao;
	
	@Autowired
	private TransPolicyDao transPolicyDao;
	
	@Autowired
	private TransDnsDao transDnsDao;

	@Override
	public Map<String,String> insertTransDns(TransDnsVo transDnsVo, TransStatusHistoryVo hisVo) throws Exception {
		
		int addResult = -1;
		Map<String,String> mapRtn = new HashMap<String,String>();
		String transNum = null;
		if(transDnsVo!=null) {
			transNum = transDnsVo.getTransNum();
			if(transNum==null) {
				transNum = transService.getTransNum();// 設定交易序號
			}
			
			// 新增線上申請主檔TRANS
			java.util.Date toDay = new java.util.Date();
			TransVo transVo = new TransVo();
			transVo.setTransNum(transNum);
			transVo.setTransDate(toDay);
			transVo.setTransType(TransTypeUtil.DNS_PARAMETER_CODE);
			transVo.setStatus(transDnsVo.getStatus());
			transVo.setUserId("twfhclife");//TODO
			transVo.setCreateUser("twfhclife");//TODO
			transVo.setCreateDate(toDay);
			addResult = transDao.insertTrans(transVo);
			
			//新增TRANS_POLICY
			if(addResult>0) {
				// 新增保單號碼
				TransPolicyVo transPolicyVo = new TransPolicyVo();
				transPolicyVo.setTransNum(transNum);
				transPolicyVo.setPolicyNo("");//此時無保單號碼
				addResult = transPolicyDao.insertTransPolicy(transPolicyVo);
			}
			
			//新增TRANS_DNS
			if(addResult>0) {
				addResult = transDnsDao.insertTransDns(transDnsVo);
			}
			
			//insert success.
			mapRtn.put("TRANS_NUM", transNum);
			mapRtn.put("INSERT_RESULT", addResult+"");
		}
		
		//寫入狀態歷程
		if(addResult>0) {
			hisVo.setTransNum(transNum);
   			hisVo.setStatus(transDnsVo.getStatus());
			onlineChangeDao.addTransStatusHistory(hisVo);
		}
		
		return mapRtn;
	}

	@Override
	public TransDnsVo getTransServiceDnsDetail(String transNum) throws Exception {
		TransDnsVo transDaoDnsDetail=null;
		if(transNum!=null && !"".equals(transNum)){
			 transDaoDnsDetail = transDnsDao.getTransDaoDnsDetail(transNum);
		}
		return  transDaoDnsDetail;
	}

}
