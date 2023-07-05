package com.twfhclife.adm.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.adm.dao.JdBatchPlanDao;
import com.twfhclife.adm.dao.JdUserBatchDao;
import com.twfhclife.adm.dao.JdUserDao;
import com.twfhclife.adm.model.DepartmentVo;
import com.twfhclife.adm.model.JdBatchSchedulVO;
import com.twfhclife.adm.model.JdUserVo;
import com.twfhclife.adm.service.IJdDeptMgntService;
import com.twfhclife.adm.service.IJdICBatchService;
import com.twfhclife.generic.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @auther lihao
 */
@Service
public class IJdICBatchServiceImpl implements IJdICBatchService {
    private static final Logger logger = LogManager.getLogger(JdUserBatchServiceImpl.class);

    @Autowired
    private JdUserBatchDao jdUserBatchDao;

    @Autowired
    private IJdDeptMgntService jdDeptMgntService;

    @Autowired
    private JdUserDao jdUserDao;

    @Autowired
    private JdBatchPlanDao jdBatchPlanDao;

    private String FILE_SAVE_PATH = "/Users/import/ic";

    @Override
    public int addUsers(JdUserVo jdUserVo) {
        return jdUserBatchDao.addUsers(jdUserVo);
    }

    @Override
    public int updateUsers(JdUserVo jdUserVo) {
        return jdUserBatchDao.updateUsers(jdUserVo);
    }


    @Override
    public List<Map<String, Object>> getICs(JdBatchSchedulVO vo) {
        return jdBatchPlanDao.getICs(vo);
    }

    @Override
    public int countICs(JdBatchSchedulVO vo) {
        return jdBatchPlanDao.countICs(vo);
    }

    @Override
    public int deleteIC(JdUserVo jdUserVo) {
        int deleteICs = jdUserBatchDao.deleteIC(jdUserVo);
        if (deleteICs > 0) {
            return jdUserBatchDao.deleteUserIC(jdUserVo);
        }
        return deleteICs;
    }

    @Override
    public void upLoadFile(MultipartFile file) {
        JdBatchSchedulVO jdBatchSchedulVO = new JdBatchSchedulVO();
        Date date = new Date();
        // todo 加入排程時間
        jdBatchSchedulVO.setBatchJoinTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        // 等待處理中
        jdBatchSchedulVO.setBatchStatus("waiting");
        jdBatchSchedulVO.setType("IC");
        byte[] buffer = null;
        if (file != null) {
            String fileName = file.getOriginalFilename();
            jdBatchSchedulVO.setBatchName(fileName);
            String filepath = FILE_SAVE_PATH;
            File localFile = new File(filepath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            ByteArrayOutputStream bos = null;
            FileInputStream fis = null;
            try {
                File server_file = new File(filepath + File.separator + fileName);
                if (server_file.exists()) {
                    SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmss");
                    fileName = fileName.split("\\.")[0] + fmdate.format(new Date()) + "." + fileName.split("\\.")[1];
                    server_file = new File(filepath + File.separator + fileName);
                }
                file.transferTo(server_file);
                fis = new FileInputStream(server_file);
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }

                buffer = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        logger.error("upLoadFile: " + e);
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        logger.error("upLoadFile: " + e);
                    }
                }
            }
            jdBatchSchedulVO.setBatchFile(buffer);
            jdBatchSchedulVO.setFailLink(buffer);
        }
        jdBatchPlanDao.addBatchPlan(jdBatchSchedulVO);
    }

    @Scheduled(cron = "*/5 * * * * *")
    private void scheduledICWork() throws IOException {
        workICFile();
    }

    @Override
    public void workICFile() throws IOException {
        List<JdBatchSchedulVO> batchSchedulVOS = jdBatchPlanDao.getICBatch();
        String filepath = FILE_SAVE_PATH;
        File localFile = new File(FILE_SAVE_PATH);
        if (batchSchedulVOS.size() > 0) {
            for (JdBatchSchedulVO batch : batchSchedulVOS) {
                //獲取數據
                byte[] batchFile = batch.getBatchFile();
                Date date = new Date();
                batch.setBatchStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                // 處理中
                batch.setBatchStatus("processing");
                jdBatchPlanDao.updateBatchPlan(batch);

                String fileName = "workICfile.xlsx";
                BufferedOutputStream bufferedOutputStream = null;
                // 讀取文件零時地址
                String readFilePath = null;
                if (!localFile.exists()) {
                    localFile.mkdirs();
                }
                //todo 轉成csv
                try {
                    File server_file = new File(filepath + File.separator + fileName);
                    if (server_file.exists()) {
                        SimpleDateFormat fmdate = new SimpleDateFormat("yyyyMMddHHmmss");
                        fileName = fileName.split("\\.")[0] + fmdate.format(new Date()) + "." + fileName.split("\\.")[1];
                        server_file = new File(filepath + File.separator + fileName);
                    }
                    readFilePath = filepath + File.separator + fileName;
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(server_file));
                    for (int i = 0; i < batchFile.length; i++) {
                        bufferedOutputStream.write(batchFile[i]);
                    }
                    bufferedOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                }
                ArrayList<JdUserVo> userList = new ArrayList<>();
                try {
                    List<List<String>> list = ExcelUtils.readExcel(readFilePath);
                    for (int i = 0; i < list.size(); i++) {
                        List<String> stringList = new ArrayList<>();
                        JdUserVo jdUserVo = new JdUserVo();
                        List<String> arrayList = list.get(i);
                        jdUserVo.setActionType(arrayList.get(0));
                        jdUserVo.setDepId(arrayList.get(1).replaceAll("[.](.*)", ""));
                        jdUserVo.setIcId(arrayList.get(2));
                        // todo 通路+所屬人員Id
                        for (int k = 3; k < arrayList.size(); k++) {
                            if (!StringUtils.isBlank(arrayList.get(k))) {
                                stringList.add(arrayList.get(k));
                            }
                        }
                        jdUserVo.setDepBranchList(stringList);
                        userList.add(jdUserVo);
                    }
                } catch (Exception e) {
                    logger.error("讀取數據失敗");
                }
                // 數據處理
                List<JdUserVo> failLinkList = new ArrayList<>();
                for (JdUserVo jdUserVo : userList) {
                    if (StringUtils.isBlank(jdUserVo.getActionType())) {
                        jdUserVo.setFailResult("動作別欄位為必輸欄位，請檢查!");
                        failLinkList.add(jdUserVo);
                    } else {
                        if (!jdUserVo.getActionType().equals("MODIFY") && !jdUserVo.getActionType().equals("DELETE")) {
                            jdUserVo.setFailResult("動作別只允許MODIFY及DELETE!");
                            failLinkList.add(jdUserVo);
                        } else {
                            if (StringUtils.isEmpty(jdUserVo.getDepId())) {
                                jdUserVo.setFailResult("所屬IC人員通路為必輸欄位，請檢查!");
                                failLinkList.add(jdUserVo);
                            } else {
                                if (StringUtils.isEmpty(jdUserVo.getIcId())) {
                                    jdUserVo.setFailResult("IC人員編號為必輸欄位，請檢查!");
                                    failLinkList.add(jdUserVo);
                                } else {
                                    if (jdUserVo.getActionType().equals("DELETE")) {
                                        if (jdUserVo.getDepBranchList().size() > 0) {
                                            jdUserVo.setFailResult("通路@分支機構為留空字段，請檢查!");
                                            failLinkList.add(jdUserVo);
                                        } else {
                                            DepartmentVo deleteDepId = jdDeptMgntService.getDivDep("0", jdUserVo.getDepId());
                                            if (deleteDepId == null) {
                                                jdUserVo.setFailResult("所屬IC人員通路代碼不存在，請檢查!");
                                                failLinkList.add(jdUserVo);
                                            } else {
                                                JdUserVo deleteIcId = jdUserDao.getIcId(jdUserVo.getIcId(), deleteDepId.getDepId());
                                                if (deleteIcId == null) {
                                                    jdUserVo.setFailResult("IC人員不存在，請檢查!");
                                                }
                                                if (StringUtils.isEmpty(jdUserVo.getFailResult())) {
                                                    jdUserDao.deleteUserIC(deleteIcId);
                                                } else {
                                                    failLinkList.add(jdUserVo);
                                                }
                                            }
                                        }
                                    } else {
                                        if (jdUserVo.getDepBranchList().size() == 0 && jdUserVo.getDepBranchList().isEmpty()) {
                                            jdUserVo.setFailResult("通路@分支機構欄位需至少一組，請檢查!");
                                            failLinkList.add(jdUserVo);
                                        } else {
                                            DepartmentVo depId = jdDeptMgntService.getDivDep("0", jdUserVo.getDepId());
                                            if (depId == null) {
                                                jdUserVo.setFailResult("所屬IC人員通路代碼不存在，請檢查!");
                                                failLinkList.add(jdUserVo);
                                            } else {
                                                //
                                                JdUserVo icId = jdUserDao.getIcId(jdUserVo.getIcId(), depId.getDepId());
                                                if (icId == null) {
                                                    jdUserVo.setFailResult("IC人員不存在，請檢查!");
                                                    failLinkList.add(jdUserVo);
                                                } else {
                                                    boolean deleteType = true;
                                                    JdUserVo jdUserVo1 = new JdUserVo();
                                                    for (int i = 0; i < jdUserVo.getDepBranchList().size(); i++) {
                                                        if (!StringUtils.isEmpty(jdUserVo.getDepBranchList().get(i))) {
                                                            String[] strings = jdUserVo.getDepBranchList().get(i).split("@");
                                                            String depId1 = null;
                                                            String branchId1 = null;
                                                            if (strings.length >= 2) {
                                                                depId1 = strings[0];
                                                                branchId1 = strings[1];
                                                                DepartmentVo divDep = jdDeptMgntService.getDivDep("0", depId1);
                                                                if (divDep == null) {
                                                                    jdUserVo.setFailResult("通路代碼不存在，請檢查!");
                                                                    break;
                                                                } else {
                                                                    DepartmentVo branchId = jdDeptMgntService.getBranchId(divDep.getDepId(), branchId1);
                                                                    if (branchId == null) {
                                                                        logger.info("分支機構不存在，通路代碼：{}， 分支機構代碼：{}", divDep.getDepId(), branchId1);
                                                                        jdUserVo.setFailResult("分支機構代碼不存在，請檢查!");
                                                                        break;
                                                                    }
                                                                    if (StringUtils.isBlank(jdUserVo.getFailResult())) {
                                                                        jdUserVo.setUserId(icId.getUserId());
                                                                        BeanUtils.copyProperties(jdUserVo, jdUserVo1);
                                                                        jdUserVo1.setDepId(depId1);
                                                                        jdUserVo1.setBranchId(branchId1);
                                                                        int countUserIc = jdUserDao.countUserIc(jdUserVo1.getUserId());
                                                                        if (countUserIc > 0 && deleteType) {
                                                                            if (jdUserDao.deleteUserIC(jdUserVo1) > 0) {
                                                                                jdUserDao.insertUserIC(jdUserVo1);
                                                                                deleteType = false;
                                                                            }
                                                                        } else {
                                                                            jdUserDao.insertUserIC(jdUserVo1);
                                                                            deleteType = false;
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (strings.length == 1) {
                                                                    depId1 = strings[0];
                                                                    DepartmentVo divDep = jdDeptMgntService.getDivDep("0", depId1);
                                                                    if (divDep == null) {
                                                                        jdUserVo.setFailResult("通路代碼不存在，請檢查!");
                                                                        break;
                                                                    } else {
                                                                        if (StringUtils.isBlank(jdUserVo.getFailResult())) {
                                                                            jdUserVo.setUserId(icId.getUserId());
                                                                            BeanUtils.copyProperties(jdUserVo, jdUserVo1);
                                                                            jdUserVo1.setDepId(depId1);
                                                                            int countUserIc = jdUserDao.countUserIc(jdUserVo1.getUserId());
                                                                            if (countUserIc > 0 && deleteType) {
                                                                                if (jdUserDao.deleteUserIC(jdUserVo1) > 0) {
                                                                                    jdUserDao.insertUserIC(jdUserVo1);
                                                                                    deleteType = false;
                                                                                }
                                                                            } else {
                                                                                jdUserDao.insertUserIC(jdUserVo1);
                                                                                deleteType = false;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (!StringUtils.isEmpty(jdUserVo.getFailResult())) {
                                                        failLinkList.add(jdUserVo);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                String readFailFilePath = null;
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFCell cell;
                XSSFSheet sheet = workbook.createSheet("Sheet1");
                // 创建表头
                XSSFRow row = sheet.createRow(0);
                List<String> headList = Lists.newArrayList("失敗原因", "動作別", "所屬IC人員通路", "IC人員編號", "通路@分支結構");
                for (int i = 0; i < headList.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(headList.get(i));
                }
                // 寫入excel文件
                String DATE_FORMAT = "yyyyMMddHHmmss";
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Calendar c1 = Calendar.getInstance();
                String timeStr = sdf.format(c1.getTime());
                String fileName1 = "失敗檔案IC" + timeStr + ".xlsx";
                //寫内容
                try {
                    readFailFilePath = filepath + File.separator + fileName1;
                    if (failLinkList.size() > 0) {
                        File file = new File(readFailFilePath);
                        // 換行
                        int k = 1;
                        for (JdUserVo jdUserVo : failLinkList) {
                            row = sheet.createRow(k);
                            k = k + 1;
                            row.createCell(0).setCellValue(jdUserVo.getFailResult());
                            row.createCell(1).setCellValue(jdUserVo.getActionType());
                            row.createCell(2).setCellValue(jdUserVo.getDepId());
                            row.createCell(3).setCellValue(jdUserVo.getIcId());
                            if (jdUserVo.getDepBranchList().size() > 0) {
                                // 換列
                                int m = 4;
                                for (int i = 0; i < jdUserVo.getDepBranchList().size(); i++) {
                                    row.createCell(m).setCellValue(jdUserVo.getDepBranchList().get(i));
                                    m = m + 1;
                                }
                            }
                            workbook.write(new FileOutputStream(file));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    workbook.close();
                }
                FileInputStream fis = null;
                ByteArrayOutputStream bos = null;
                // todo 轉換成byte數組到數據庫
                byte[] buffer = null;
                try {
                    File fail_file = new File(readFailFilePath);
                    fis = new FileInputStream(fail_file);
                    bos = new ByteArrayOutputStream();
                    byte[] b1 = new byte[1024];
                    int n;
                    while ((n = fis.read(b1)) != -1) {
                        bos.write(b1, 0, n);
                    }
                    buffer = bos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            logger.error("workICFile: " + e);
                        }
                    }
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                            logger.error("workICFile: " + e);
                        }
                    }
                }
                batch.setFailLink(buffer);
                Date endDate = new Date();
                // todo 排程結束時間
                batch.setFailNum(failLinkList.size());
                batch.setBatchEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                // 已完成
                batch.setBatchStatus("completed");
                jdBatchPlanDao.updateBatchPlan(batch);
            }
        }
    }


}

