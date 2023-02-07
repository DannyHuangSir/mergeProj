package com.twfhclife.eservice.jdzq.dao;

import com.twfhclife.eservice.web.model.CaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdzqDao {
    List<CaseVo> getCaseList(@Param("serialNums") List<String> serialNums);
}
