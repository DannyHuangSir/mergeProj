package com.twfhclife.eservice.auth.dao;

import com.twfhclife.eservice.onlineChange.model.Bxcz415CallBackDataVo;
import com.twfhclife.eservice.onlineChange.model.BxczSignApiLog;
import com.twfhclife.eservice.onlineChange.model.SignRecord;
import com.twfhclife.eservice.web.model.SignTrans;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BxczDao {
    int insertBxczApiLog(@Param("actionId") String actionId, @Param("type") String type, @Param("apiCode") String apiCode, @Param("apiParam") String apiParam, @Param("apiResponse") String apiResponse);

    int insertBxczSignRecord(@Param("vo") SignRecord signRecord, @Param("code") String code, @Param("msg") String msg, @Param("idVerifyTime") Date idVerifyTime,  @Param("signTime") Date signTime);

    int updateBxczSignRecordByActionId(@Param("vo") Bxcz415CallBackDataVo vo, @Param("code") String code, @Param("msg") String msg, @Param("idVerifyTime") Date idVerifyTime,  @Param("signTime") Date signTime);

    SignRecord getSignRecordByActionId(@Param("actionId") String actionId);
    List<SignRecord> getNotDownloadSignFile();
    int updateSignDownloaded(@Param("actionId") String actionId);
    int insertSignFileData(@Param("fileId") String fileId, @Param("companyId") String companyId, @Param("fileBase64") String fileBase64);

    SignRecord getNewSignStatus(@Param("transNum") String transNum);

    String getSignFileByFileId(String signFileId);
    int addSignApiLog(@Param("vo") BxczSignApiLog vo);

    int updateSignStatus418(@Param("actionId") String actionId, @Param("idVerifyStatus") String idVerifyStatus, @Param("signStatus") String signStatus, @Param("time") Date date);

    int countSignRecord(@Param("actionId") String actionId, @Param("idVerifyStatus") String idVerifyStatus, @Param("signStatus") String signStatus);

    SignTrans getSignTrans(@Param("actionId") String actionId);
}
