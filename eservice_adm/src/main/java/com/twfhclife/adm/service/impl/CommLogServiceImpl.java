package com.twfhclife.adm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.adm.dao.CommLogDao;
import com.twfhclife.adm.model.CommLogVo;
import com.twfhclife.adm.service.ICommLogService;
import com.twfhclife.generic.annotation.RequestLog;

@Service
public class CommLogServiceImpl implements ICommLogService{

	@Autowired
	private CommLogDao commLogDao;

	@RequestLog
	@Override
	public List<Map<String, Object>> getCommLogDetail(CommLogVo vo) {
		List<Map<String, Object>> list = commLogDao.getCommLogDetail(vo);

		return list;
	}

	@RequestLog
	@Override
	public List<String> getAllCommType() {
		List<String> list = commLogDao.getAllCommType();
//		for(int i=0;i<list.size();i++) {
//			System.out.println(list.get(i).toString());
//		}
		return list;
	}

	@RequestLog
	@Override
	public List<String> getCommCntById(String id) {
		CommLogVo vo = new CommLogVo();
		vo.setId(id);

		List<String> list = commLogDao.getCommCntById(vo);
		if (list != null && !list.isEmpty()) {
			System.out.println("list is null");
		} else {
			System.out.println("list is not null");
		}
		return list;
	}

	@RequestLog
	@Override
	public int getCommLogDetailPageTotal(CommLogVo vo) {
		int iResult = commLogDao.getCommLogDetailPageTotal(vo);
		return iResult;
	}

}
