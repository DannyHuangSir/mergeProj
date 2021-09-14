package com.twfhclife.eservice.onlineChange.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.onlineChange.model.ContactInfoFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoFileDataVo;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransContactInfoVo;

/**
 * 變更保單聯絡資料主檔 Dao.
 * 
 * @author all
 */
public interface TransContactInfoDao {
	
	/**
	 * 變更保單聯絡資料主檔-查詢.
	 * 
	 * @param transContactInfoVo TransContactInfoVo
	 * @return 回傳查詢結果
	 */
	List<TransContactInfoVo> getTransContactInfo(@Param("transContactInfoVo") TransContactInfoVo transContactInfoVo);
	
	/**
	 * 取得下一筆序號.
	 * 
	 * @return 回傳序號
	 */
	BigDecimal getNextTransContactInfoId();
	
	/**
	 * 變更保單聯絡資料主檔-新增.
	 * 
	 * @param transContactInfoVo TransContactInfoVo
	 * @return 回傳影響筆數
	 */
	int insertTransContactInfo(@Param("transContactInfoVo") TransContactInfoVo transContactInfoVo);
	
	
    public Map<String,Object> getCIOUserDetailInfoOld(@Param("roc_id")String roc_id);
	
	public Map<String,Object> getCIOUserDetailInfoNew(@Param("roc_id")String roc_id);
	/**
	 * 获取某个保单序号对应的，批号所有的保单序号
	 * @param transNum  保单序号
	 * @return
	 */
	List<TransContactInfoVo> getTransContactInfoTransNum(@Param("transNum")String transNum);

	/**
	 * 存储FileDatas 内容数据
	 * @return
	 */
	int  addTransContactInfoFileDatas(@Param("vo") ContactInfoFileDataVo contactInfoFileDataVo);
	/**
	 * 查询文件的id值
	 * @return
	 */
	Float  selectTransContactInfoFileDatasId();

	/**
	 * 修改文件数据的fileBase64
	 * @param contactInfoFileDataVo
	 * @return
	 */
   int  	updateTransContactInfoFileDatasFileBase64(@Param("vo") ContactInfoFileDataVo contactInfoFileDataVo);
	/**
	 * 验证,取消申請是否可以进行取消此時STATUS=1
	 * @param transNum   单号
	 * @return   状态码
	 */
    int getTransContactInfoTransNumCheck(@Param("transNum")String transNum);
    
    /**
     * 依FILE_ID更新FILE_BASE64內容
     * @param vo
     * @return
     */
    int updateFileBase64ByFileId(@Param("vo") TransContactInfoFileDataVo vo);

	/**
	 * 根据transNum  进行修改  caseId
	 * @param caseId
	 * @param transNum
	 * @return
	 */
    int updateTransContactInfoCaseId(@Param("caseId") String  caseId,@Param("transNum") String  transNum);
	/**
	 * 通過Batch_ID獲取TransNum
	 * @param batchId
	 * @return
	 */
    List<String> getTransContactInfoTransNumByBatchId(@Param("batchId")Float batchId);
}