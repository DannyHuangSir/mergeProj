package com.twfhclife.eservice_batch.dao;

import com.twfhclife.eservice_batch.mapper.TransApplyItemMapper;
import com.twfhclife.eservice_batch.mapper.TransMapper;
import com.twfhclife.eservice_batch.model.TransApplyItemVo;
import com.twfhclife.eservice_batch.model.TransStatusHistoryVo;
import com.twfhclife.eservice_batch.model.TransVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class TransApplyItemDao extends BaseDao {

	private static final Logger logger = LogManager.getLogger(TransApplyItemDao.class);
	
	public TransApplyItemVo selectByTransNum(TransApplyItemVo vo) {
		TransApplyItemVo trans = null;
		try {
			TransApplyItemMapper transMapper = this.getSqlSession().getMapper(TransApplyItemMapper.class);
			trans = transMapper.selectByTransNum(vo.getTransNum());
		} catch (Exception e) {
			logger.error("selectByTransNum error:", e);
		} finally {
			this.release();
		}
		return trans;
		
	}

	public int updateTransApplyItem(TransApplyItemVo vo) {
		int result = 0;
		TransApplyItemVo transApplyItemVo = selectByTransNum(vo);
		try {
			Date sysTime = new Date();
			TransApplyItemMapper transMapper = this.getSqlSession().getMapper(TransApplyItemMapper.class);
			if (transApplyItemVo == null) {
				vo.setUpdateTime(sysTime);
				vo.setInsertTime(sysTime);
				result = transMapper.insert(vo);
			} else {
				vo.setUpdateTime(sysTime);
				result = transMapper.update(vo);
			}
			this.getSqlSession().commit();
		} catch (Exception e) {
			logger.error("updateTransApplyItem error:", e);
		} finally {
			this.release();
		}
		return result;
	}
}
