package com.twfhclife.adm.service;

import java.util.List;

import com.twfhclife.adm.model.SystemsVo;

/**
 * 系統別服務.
 * 
 * @author alan
 */
public interface ISystemService {
	
	/**
	 * 查詢所有系統別.
	 * 
	 * @return 回傳所有系統別.
	 */
	public List<SystemsVo> getAll();
}