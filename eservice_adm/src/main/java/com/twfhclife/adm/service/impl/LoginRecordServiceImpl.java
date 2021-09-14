package com.twfhclife.adm.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.LoginLogDao;
import com.twfhclife.adm.model.LoginLogVo;
import com.twfhclife.adm.service.ILoginRecordService;
import com.twfhclife.generic.annotation.RequestLog;

/**
 * 報表查詢-登入記錄查詢服務.
 * 
 * @author all
 */
@Service
public class LoginRecordServiceImpl implements ILoginRecordService{
	
	@Autowired
	private LoginLogDao loginLogDao;
	
	/**
	 * 取得登入記錄查詢結果
	 * 
	 * @param vo AuditLogVo
	 * @return 回傳登入記錄查詢
	 */	
	@RequestLog
	@Override
	public List<Map<String, Object>> getLoginRecordDetail(LoginLogVo vo){
		List<Map<String, Object>> list = loginLogDao.getLoginRecordDetail(vo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		for (Map<String, Object> map : list) {
			if (map.containsKey("LOGIN_DATE")) {
				map.put("LOGIN_DATE", sdf.format(new Date(((Timestamp) map.get("LOGIN_DATE")).getTime())));
			}
			if (map.containsKey("LOGOUT_DATE")) {
				map.put("LOGOUT_DATE", sdf.format(new Date(((Timestamp) map.get("LOGOUT_DATE")).getTime())));
			}
		}
		return list;
	}
	
	/**
	 * 取得登入記錄查詢結果總筆數
	 * 
	 * @param vo AuditLogVo
	 * @return 回傳總筆數
	 */
	@RequestLog
	@Override
	public int getLoginRecordDetailTotal(LoginLogVo vo) {
		return loginLogDao.getLoginRecordDetailTotal(vo);
	}
	
}
