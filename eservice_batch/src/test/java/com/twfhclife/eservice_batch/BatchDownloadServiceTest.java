package com.twfhclife.eservice_batch;

import com.twfhclife.eservice_batch.service.BatchDownloadService;

/**
 * @author hui.chen
 * @create 2021-08-05
 */
public class BatchDownloadServiceTest {

    public static void main(String[] args) {
        BatchDownloadServiceTest batchDownloadServiceTest = new BatchDownloadServiceTest();
        batchDownloadServiceTest.updateTransStatusHistoryTest();
    }

    public  void  updateTransStatusHistoryTest(){
        BatchDownloadService batchDownloadService = new BatchDownloadService();
      //  batchDownloadService.addTransStatusHistory("202108050001","2");
        batchDownloadService.addTransStatusHistory("202108050001","6");
    }
}
