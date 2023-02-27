package com.twfhclife.adm.dao;

import com.twfhclife.adm.model.JdBatchSchedulVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
@Mapper
public interface JdBatchPlanDao {

    int addBatchPlan(@Param("jdBatchSchedulVO") JdBatchSchedulVO jdBatchSchedulVO);

    int updateBatchPlan(@Param("jdBatchSchedulVO") JdBatchSchedulVO vo);

    List<Map<String, Object>> getUsers(@Param("vo") JdBatchSchedulVO vo);

    List<Map<String, Object>> getICs(@Param("vo") JdBatchSchedulVO vo);

    int countUsers(@Param("vo") JdBatchSchedulVO vo);

    int countICs(@Param("vo") JdBatchSchedulVO vo);

    List<JdBatchSchedulVO> getBatch();

    JdBatchSchedulVO getBatchLink(@Param("batchId")String batchId);

    JdBatchSchedulVO getBatchFailLink(@Param("batchId")String batchId);

    List<JdBatchSchedulVO> getICBatch();
}
