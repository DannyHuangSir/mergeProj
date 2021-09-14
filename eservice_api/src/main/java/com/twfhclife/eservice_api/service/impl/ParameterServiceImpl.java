package com.twfhclife.eservice_api.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.twfhclife.common.util.EncryptionUtil;
import com.twfhclife.eservice.api.adm.domain.ParamAddRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamDelRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamRequestVo;
import com.twfhclife.eservice.api.adm.domain.ParamUpdateRequestVo;
import com.twfhclife.eservice.api.adm.model.ParameterVo;
import com.twfhclife.eservice_api.service.IParameterService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.dao.adm.ParameterDao;

/**
 * 參數維護服務.
 * 
 * @author all
 */
@Service("apiParameterService")
public class ParameterServiceImpl implements IParameterService {

	private static final Logger logger = LogManager.getLogger(ParameterServiceImpl.class);

	@Autowired
	@Qualifier("apiParameterDao")
	private ParameterDao parameterDao;

	/**
	 * 參數維護-分頁查詢.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳參數維護-分頁查詢結果
	 */
	@Override
	public List<Map<String, String>> getParameterPageList(ParamRequestVo parameterVo) {
		return parameterDao.getParameterPageList(parameterVo);
	}

	/**
	 * 參數維護-查詢結果總筆數.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳總筆數
	 */
	@Override
	public int getParameterPageTotal(ParamRequestVo parameterVo) {
		return parameterDao.getParameterPageTotal(parameterVo);
	}

	/**
	 * 參數維護-查詢.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳查詢結果
	 */
	@Override
	public List<ParameterVo> getParameter(ParamRequestVo parameterVo) {
		return parameterDao.getParameter(parameterVo);
	}

	/**
	 * 用參數代碼取出參數.
	 * 
	 * @param systemId
	 *            系統別
	 * @param parameterCode
	 *            參數代碼
	 * @return 回傳查詢結果
	 */
	@Override
	public String getParameterValueByCode(String systemId, String parameterCode) {
		return parameterDao.getParameterValueByCode(systemId, parameterCode);
	}

	/**
	 * 用參數類別代碼取出所有以下的參數.
	 * 
	 * @param systemId
	 *            系統別
	 * @param categoryCode
	 *            參數類別代碼
	 * @return 回傳查詢結果
	 */
	@Override
	public List<ParameterVo> getParameterByCategoryCode(String systemId, String categoryCode) {
		return parameterDao.getParameterByCategoryCode(systemId, categoryCode);
	}

	/**
	 * 參數維護-新增.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳影響筆數
	 * @throws Exception
	 */
	@Override
	public int insertParameter(ParamAddRequestVo parameterVo) throws Exception {
		if ("Y".equals(parameterVo.getEncryptType())) {
			parameterVo.setParameterValue(EncryptionUtil.Encrypt(parameterVo.getParameterValue()));
		}
		return parameterDao.insertParameter(parameterVo);
	}

	/**
	 * 參數維護-更新.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳影響筆數
	 * @throws Exception 
	 */
	@Override
	public int updateParameter(ParamUpdateRequestVo parameterVo) throws Exception {
		if ("Y".equals(parameterVo.getEncryptType())) {
			parameterVo.setParameterValue(EncryptionUtil.Encrypt(parameterVo.getParameterValue()));
		}
		
		return parameterDao.updateParameter(parameterVo);
	}

	/**
	 * 參數維護-刪除.
	 * 
	 * @param parameterVo
	 *            ParameterVo
	 * @return 回傳影響筆數
	 */
	@Override
	public int deleteParameter(ParamDelRequestVo parameterVo) {
		return parameterDao.deleteParameter(parameterVo);
	}

	/**
	 * 取得參數的下拉選單資料.
	 * 
	 * @param systemId
	 *            系統代號
	 * @param categoryCode
	 *            參數種類類型代碼
	 * @return 回傳下拉選單資料.
	 */
	@Override
	public List<ParameterVo> getOptionList(String systemId, String categoryCode) {
		return parameterDao.getOptionList(systemId, categoryCode);
	}

	@Override
	public List<String> getRegionOption() {
		// TODO Auto-generated method stub
		return parameterDao.getRegionOption();
	}
	
	
}
