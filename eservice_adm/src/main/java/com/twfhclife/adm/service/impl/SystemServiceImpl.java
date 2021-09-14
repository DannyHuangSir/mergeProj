package com.twfhclife.adm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.SystemsDao;
import com.twfhclife.adm.model.SystemsVo;
import com.twfhclife.adm.service.ISystemService;

/**
 * 系統別服務.
 * 
 * @author alan
 */
@Service
public class SystemServiceImpl implements ISystemService {
	
	@Autowired
	private SystemsDao systemsDao;
	
	/**
	 * 查詢所有系統別.
	 * 
	 * @return 回傳所有系統別.
	 */
	@Override
	public List<SystemsVo> getAll() {
		return systemsDao.findAll();
	}
}
