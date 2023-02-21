package com.twfhclife.eservice.api.jdzq.dao;

import com.twfhclife.eservice.api.jdzq.model.CaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdzqDao {

    List<CaseVo> getCaseListBySerialNum(@Param("serialNums") List<String> serialNums);

}
