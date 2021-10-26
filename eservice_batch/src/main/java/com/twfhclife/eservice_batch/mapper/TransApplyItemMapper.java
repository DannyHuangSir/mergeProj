package com.twfhclife.eservice_batch.mapper;

import com.twfhclife.eservice_batch.model.TransApplyItemVo;

public interface TransApplyItemMapper {

    int insert(TransApplyItemVo record);

    TransApplyItemVo selectByTransNum(String transNum);

    int update(TransApplyItemVo record);
}