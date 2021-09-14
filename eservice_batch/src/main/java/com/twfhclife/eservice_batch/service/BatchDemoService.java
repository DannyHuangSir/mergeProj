package com.twfhclife.eservice_batch.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice_batch.dao.UserDao;
import com.twfhclife.eservice_batch.model.UserVo;

public class BatchDemoService {

	private static final Logger logger = LogManager.getLogger(BatchDemoService.class);

	public void DemoBatch() {
		UserDao userDao = new UserDao();
		UserVo user = userDao.getUserByUsername("hpe_david");
		logger.info("### Batch demo result: " + user.getUsername() + ", email=" + user.getEmail());
	}
}
