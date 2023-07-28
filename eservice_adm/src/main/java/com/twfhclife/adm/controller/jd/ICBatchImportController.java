package com.twfhclife.adm.controller.jd;

import java.net.URLEncoder;
import com.twfhclife.adm.dao.JdBatchPlanDao;
import com.twfhclife.adm.domain.PageResponseObj;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.JdBatchSchedulVO;
import com.twfhclife.adm.service.IJdICBatchService;
import com.twfhclife.generic.annotation.FuncUsageParam;
import com.twfhclife.generic.annotation.LoginCheck;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.util.ApConstants;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @auther lihao
 */
@Controller
public class ICBatchImportController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ICBatchImportController.class);

    @Autowired
    private IJdICBatchService jdICBatchService;
    @Autowired
    private JdBatchPlanDao jdBatchPlanDao;

    @RequestLog
    @LoginCheck(value=@FuncUsageParam(
            funcId = "695",
            systemId = ApConstants.SYSTEM_ID
    ))
    @GetMapping("/ICBatchImport")
    public String iCBatchImport() {
        return "backstage/jd/iCBatchImport";
    }

    @RequestLog
    @PostMapping("/jdBatch/getICs/batch")
    public ResponseEntity<PageResponseObj> getUsersBatch(@RequestBody JdBatchSchedulVO vo) {
        PageResponseObj pageResp = new PageResponseObj();
        try {
            pageResp.setRows(jdICBatchService.getICs(vo));
            // 設定jqGrid 資料查詢總筆數
            pageResp.setRecords(jdICBatchService.countICs(vo));
            // 設定jqGrid 目前頁數
            pageResp.setPage(vo.getPage());
            // 設定jqGrid 總頁數
            pageResp.setTotal((pageResp.getRecords() + vo.getRows() - 1) / vo.getRows());
            pageResp.setResult(PageResponseObj.SUCCESS);
        } catch (Exception e) {
            pageResp.setResult(PageResponseObj.ERROR);
            logger.error("Unable to getICs: {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.OK).body(pageResp);
    }

    @RequestLog
    @PostMapping("/jdBatch/uploadICFile")
    public ResponseEntity<ResponseObj> uploadICFile(@RequestParam("file") MultipartFile file) {
        try {
            jdICBatchService.upLoadFile(file);
        } catch (Exception e) {
            logger.error("Unable to addUsers: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }

    @RequestLog
    @GetMapping("/jdBatch/exportICFile")
    public void exportFile(@RequestParam("batchId") String batchId, @RequestParam("type")Boolean type, HttpServletResponse response) throws IOException {

        String filterBatchId = StringEscapeUtils.escapeHtml4(batchId);
        String DATE_FORMAT = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        String timeStr = sdf.format(c1.getTime());
        String csvFileName;
        JdBatchSchedulVO batch;
        byte[] batchFile = new byte[0];
        OutputStream outputStream = null;
        // todo 獲取數據
        if (type) {
            // 處理原始檔案數據
            csvFileName = "原始檔案" + timeStr + ".xlsx";
            batch = jdBatchPlanDao.getBatchLink(filterBatchId);
            if (batch != null) {
                batchFile = batch.getBatchFile();
            }
        } else {
            // 處理失敗檔案數據
            csvFileName = "失敗檔案" + timeStr + ".xlsx";
            batch = jdBatchPlanDao.getBatchFailLink(filterBatchId);
            if (batch != null) {
                batchFile = batch.getFailLink();
            }
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(csvFileName, "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers","Content-disposition");

        try {
            outputStream = response.getOutputStream();
            for (int i = 0; i < batchFile.length; i++) {
                outputStream.write(batchFile[i]);
            }
        } catch (Exception e) {
            logger.error("Unable to downloadBatchExcel: {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
