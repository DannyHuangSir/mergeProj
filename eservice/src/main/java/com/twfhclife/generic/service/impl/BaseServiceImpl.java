package com.twfhclife.generic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.generic.service.IBaseService;
import com.twfhclife.generic.service.IUnicodeService;

@Service
public class BaseServiceImpl implements IBaseService {

	@Autowired
	public IUnicodeService unicodeService;
}
