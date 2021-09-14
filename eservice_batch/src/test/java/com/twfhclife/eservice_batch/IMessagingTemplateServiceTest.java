package com.twfhclife.eservice_batch;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.twfhclife.eservice_batch.dao.IMessagingTemplateServiceImpl;
import com.twfhclife.eservice_batch.dao.TransContactInfoDtlDao;
import com.twfhclife.eservice_batch.model.MessageTriggerRequestVo;
import com.twfhclife.eservice_batch.service.BatchDownloadService;
import com.twfhclife.eservice_batch.util.CallApiMailCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hui.chen
 * @create 2021-07-23
 */
public class  IMessagingTemplateServiceTest {

    public static void main02(String[] args) {
        BatchDownloadService batchDownloadService = new BatchDownloadService();
        TransContactInfoDtlDao transContactInfoDtlDao = new TransContactInfoDtlDao();
        List<Map<String, Object>> transContactInfoDetailList = transContactInfoDtlDao.getTransContactInfoDetailList("202108020002");
        if (transContactInfoDetailList!=null  && transContactInfoDetailList.size()>0) {
            Map<String, Object> stringObjectMap = transContactInfoDetailList.get(0);
            String from_company_id = (String) stringObjectMap.get("FROM_COMPANY_ID");
            String send_alliance = (String) stringObjectMap.get("SEND_ALLIANCE");
            String email = (String) stringObjectMap.get("EMAIL");
            
            String[] emails = {email};
            
            if (!"L01".equals(from_company_id)) {
                /*
                 *  @ policyType    [首家不同意轉送聯盟鏈/聯盟轉收件]
                 * @ policyState   線上/保全聯盟鏈轉送
                 * @ messagingTemplateCode  郵件模板
                 * @ insuredMail  保戶郵件
                 * */
                batchDownloadService.resultInsuredSendMail("","保全聯盟鏈轉送", CallApiMailCode.TRANSFER_MAIL_021,emails);
            }else {
                if ("N".equals(send_alliance)){
                    batchDownloadService.resultInsuredSendMail("",null,CallApiMailCode.TRANSFER_MAIL_022,emails);
                }else{
                    batchDownloadService.resultInsuredSendMail("","線上",CallApiMailCode.TRANSFER_MAIL_021,emails);
                }

            }
        }
    }
    public static void main(String[] args) {
        TransContactInfoDtlDao transContactInfoDtlDao = new TransContactInfoDtlDao();
        List<Map<String, Object>> transContactInfoDetailList = transContactInfoDtlDao.getTransContactInfoDetailList("202108020002");
        if (transContactInfoDetailList!=null  && transContactInfoDetailList.size()>0) {
            Map<String, Object> stringObjectMap = transContactInfoDetailList.get(0);
            String from_company_id = (String) stringObjectMap.get("FROM_COMPANY_ID");
            String send_alliance = (String) stringObjectMap.get("SEND_ALLIANCE");
            String email = (String) stringObjectMap.get("EMAIL");
            System.out.println(from_company_id);
            System.out.println(send_alliance);
            System.out.println(email);
        }

    }
    public static void main01(String[] args) {
        IMessagingTemplateServiceImpl transContactInfoService = new IMessagingTemplateServiceImpl();
        Map<String, Object> mailInfo = transContactInfoService.getSendMailInfo();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("TransStatus", (String) mailInfo.get("statusName"));
        paramMap.put("TransRemark", (String) mailInfo.get("transRemark"));
        paramMap.put("POLICY_TYPE", "保全聯盟鏈");
        paramMap.put("POLICY_NUMBER", "123123,123123,345345,567567,89789");
        paramMap.put("POLICY_STATUS", "失败");
        //發送系統管理員
        List<String> receivers = new ArrayList<String>();
        receivers = (List)mailInfo.get("receivers");

        MessageTriggerRequestVo vo = new MessageTriggerRequestVo();
        vo.setMessagingTemplateCode("TRANSFER_MAIL_020");
        vo.setSendType("email");
        vo.setMessagingReceivers(receivers);
        vo.setParameters(paramMap);
        vo.setSystemId("eservice_batch");
        //进行发送通信
        IMessagingTemplateServiceImpl iMessagingTemplateService = new IMessagingTemplateServiceImpl();
        String resultMsg = iMessagingTemplateService.triggerMessageTemplate(vo);
        System.out.println(resultMsg);


    }
}
