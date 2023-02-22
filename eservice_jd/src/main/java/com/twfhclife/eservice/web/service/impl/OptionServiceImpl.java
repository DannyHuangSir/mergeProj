package com.twfhclife.eservice.web.service.impl;

import com.twfhclife.eservice.web.dao.OptionDao;
import com.twfhclife.eservice.web.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements IOptionService {

	@Autowired
	private OptionDao optionDao;

	@Override
	public List<Map<String, Object>> getPolicyTyps() {
		return optionDao.getPolicyTyps();
	}

    @Override
    public List<Map<String, Object>> getLevelStates() {
        return optionDao.getLevelStates();
    }

    @Override
    public List<Map<String, Object>> getPayModeList() {
        return optionDao.getPayModeList();
    }
}