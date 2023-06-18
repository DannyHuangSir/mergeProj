package com.twfhclife.adm.service.impl;

import com.google.common.collect.Lists;
import com.twfhclife.adm.dao.JdBatchPlanDao;
import com.twfhclife.adm.dao.JdUserBatchDao;
import com.twfhclife.adm.dao.JdUserDao;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IJdDeptMgntService;
import com.twfhclife.adm.service.IJdRoleService;
import com.twfhclife.adm.service.IJdUserBatchService;
import com.twfhclife.adm.service.IJdUserMgntService;
import com.twfhclife.generic.util.ExcelUtils;
import com.twfhclife.keycloak.model.KeycloakUser;
import com.twfhclife.keycloak.service.KeycloakService;
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
public class JdUserBatchServiceImpl implements IJdUserBatchService {

    private static final Logger logger = LogManager.getLogger(JdUserBatchServiceImpl.class);
    @Autowired
    private JdUserBatchDao jdUserBatchDao;

    @Autowired
    private IJdDeptMgntService jdDeptMgntService;

    @Autowired
    private IJdRoleService jdRoleService;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private JdUserDao jdUserDao;

    @Autowired
    private JdBatchPlanDao jdBatchPlanDao;

    @Autowired
    private IJdUserMgntService jdUserMgntService;

    private String FILE_SAVE_PATH = "/Users/import/user";

    @Override
    public void upLoadFile(MultipartFile file) {
        JdBatchSchedulVO jdBatchSchedulVO = new JdBatchSchedulVO();
        Date date = new Date();
        // todo 加入排程時間
        jdBatchSchedulVO.setBatchJoinTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        jdBatchSchedulVO.setBatchStatus("waiting");
        jdBatchSchedulVO.setType("user");
        byte[] buffer = null;
        byte[] bytes = null;
        if (file != null) {
            String fileName = file.getOriginalFilename();
            //添加名称
            jdBatchSchedulVO.setBatchName(fileName);
            String filepath = FILE_SAVE_PATH;
            File localFile = new File(filepath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }

            FileInputStream fis = null;
            ByteArrayOutputStream bos = null;
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
            jdBatchSchedulVO.setBatchFile(buffer);
            jdBatchSchedulVO.setFailLink(buffer);
        }
        jdBatchPlanDao.addBatchPlan(jdBatchSchedulVO);
    }

    //每隔10分鐘
    @Scheduled(cron = "*/5 * * * * ?")
    //@Scheduled(cron = "0 */10 * * * ?")
    private void scheduledWork() throws IOException {
        workFile();
    }

    public void workFile() throws IOException {
        List<JdBatchSchedulVO> batchSchedulVOS = jdBatchPlanDao.getBatch();
        String filepath = FILE_SAVE_PATH;
        File localFile = new File(FILE_SAVE_PATH);
        if (batchSchedulVOS.size() > 0) {
            for (JdBatchSchedulVO batch : batchSchedulVOS) {
                //獲取數據
                byte[] batchFile = batch.getBatchFile();
                Date date = new Date();
                batch.setBatchStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                batch.setBatchStatus("processing");
                jdBatchPlanDao.updateBatchPlan(batch);

                String fileName = "workfile.xlsx";
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
                //todo 讀取csv
                ArrayList<JdUserVo> userList = new ArrayList<>();

                try {
                    List<List<String>> list = ExcelUtils.readExcel(readFilePath);
                    for (int i = 0; i < list.size(); i++) {
                        JdUserVo jdUserVo = new JdUserVo();
                        userList.add(jdUserVo);
                        if (list.get(i).size() < 15) {
                            jdUserVo.setFailResult("資料格式不符！");
                            continue;
                        }
                        jdUserVo.setActionType(list.get(i).get(0));
                        jdUserVo.setUserId(list.get(i).get(1));
                        jdUserVo.setStatus(list.get(i).get(2));
                        jdUserVo.setInitPassword(list.get(i).get(3).replaceAll("[.](.*)",""));
                        jdUserVo.setRoleId(list.get(i).get(4));
                        jdUserVo.setEffectiveDate(list.get(i).get(5));
                        jdUserVo.setExpirationDate(list.get(i).get(6));
                        jdUserVo.setDepId(list.get(i).get(7).replaceAll("[.](.*)",""));
                        jdUserVo.setBranchId(list.get(i).get(8).replaceAll("[.](.*)",""));
                        jdUserVo.setUserName(list.get(i).get(9));
                        jdUserVo.setIcId(list.get(i).get(10));
                        jdUserVo.setLoginSize(list.get(i).get(11));
                        jdUserVo.setRocId(list.get(i).get(12));
                        jdUserVo.setEmail(list.get(i).get(13));
                        jdUserVo.setMobile(list.get(i).get(14));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //todo 數據處理
                List<JdUserVo> failLinkList = new ArrayList<>();
                RoleVo roleId = new RoleVo();
                DepartmentVo depId = new DepartmentVo();
                DepartmentVo branchId = new DepartmentVo();
                for (JdUserVo vo : userList) {
                    if (StringUtils.isNotBlank(vo.getFailResult())) {
                        failLinkList.add(vo);
                        continue;
                    }
                    if (StringUtils.isBlank(vo.getActionType())){
                        vo.setFailResult("動作別欄位為必輸欄位，請檢查!");
                        failLinkList.add(vo);
                    }else {
                        if (!vo.getActionType().equals("MODIFY") && !vo.getActionType().equals("ADD")){
                            vo.setFailResult("動作別只允許ADD及MODIFY！");
                            failLinkList.add(vo);
                        }else {
                            if (vo.getActionType().equals("ADD")) {
                                try {
//                                    if (StringUtils.isEmpty(vo.getMobile())){
//                                        vo.setFailResult("行動電話欄位為必輸欄位，請檢查!");
//                                    }
                                    if (StringUtils.isEmpty(vo.getEmail())) {
                                        vo.setFailResult("EMAIL欄位為必輸欄位，請檢查!");
                                    }
                                    if (StringUtils.isEmpty(vo.getRocId())) {
                                        vo.setFailResult("身份證字號為必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isEmpty(vo.getIcId())){
                                        vo.setFailResult("業務員編號為必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isEmpty(vo.getUserName())){
                                        vo.setFailResult("姓名必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isEmpty(vo.getDepId())){
                                        vo.setFailResult("通路的原機構代碼必輸欄位，請檢查");
                                    }else {
                                        // 檢查通路代碼是否存在
                                        depId = jdDeptMgntService.getDivDep("0",vo.getDepId());
                                        if (depId == null) {
                                            vo.setFailResult("通路的原機構代碼不存在，請檢查!");
                                        }
                                    }
                                    if (StringUtils.isEmpty(vo.getExpirationDate())){
                                        vo.setFailResult("帳號失效日必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isEmpty(vo.getEffectiveDate())){
                                        vo.setFailResult("帳號生效日為必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isEmpty(vo.getRoleId())){
                                        vo.setFailResult("系統平臺角色欄位為必輸欄位，請檢查!");
                                    }else {
                                        // 系統平臺角色代碼是否存在
                                        roleId = jdRoleService.getRoleId(vo.getRoleId(),depId.getDepId());
                                        if (roleId == null) {
                                            vo.setFailResult("系統平臺角色代碼不存在，請檢查!");
                                        }
                                    }
                                    if (StringUtils.isEmpty(vo.getInitPassword())){
                                        vo.setFailResult("初始密碼為必輸欄位，請檢查");
                                    }
                                    if (StringUtils.isNotBlank(vo.getStatus())){
                                        vo.setFailResult("帳號狀態欄位為留空欄位，請檢查!");
                                    }
                                    if (StringUtils.isNotBlank(vo.getUserId())) {
                                        vo.setFailResult("系統帳號欄位為留空欄位，請檢查!");
                                    }
                                    if (StringUtils.isBlank(vo.getLoginSize())){
                                        vo.setFailResult("登錄字號欄位為必輸欄位，請檢查!");
                                    }
                                    if (StringUtils.isNotBlank(vo.getLoginSize())){
                                        if (jdUserDao.countLoginSize(vo.getLoginSize()) > 0){
                                            vo.setFailResult("登錄字號已存在，請檢查!");
                                        }
                                    }
                                    // 檢查分支機構代碼
                                    if (!StringUtils.isBlank(vo.getBranchId())){
                                        branchId = jdDeptMgntService.getBranchId(depId.getDepId(),
                                                vo.getBranchId());
                                        if (branchId == null) {
                                            vo.setFailResult("分支機構代碼不存在，請檢查!");
                                        }
                                    }
                                    // 檢查身份證號碼是否存在就已經檢查了系統帳號
                                    JdUserVo jdUserVo = jdUserDao.getUser(vo.getRocId());
                                    if (jdUserVo != null){
                                        if (StringUtils.isNotEmpty(jdUserVo.getRocId())) {
                                            vo.setFailResult("身分證字號已存在，請檢查!");
                                        }
                                        if (StringUtils.isNotEmpty(jdUserVo.getUserId())) {
                                            vo.setFailResult("系統帳號已存在，請檢查!");
                                        }
                                    }
                                    // 檢查所屬通路」+「業務員編號是否存在
                                    JdUserVo userIC = jdUserDao.getUserIC(depId.getDepId(),vo.getIcId());
                                    if (userIC != null) {
                                        vo.setFailResult("所屬通路+業務員編號已存在，請檢查!");
                                    }
                                    if (StringUtils.isEmpty(vo.getFailResult())) {
                                        // todo 添加到userEntity
                                        KeycloakUser keycloakUser = new KeycloakUser();
                                        BeanUtils.copyProperties(vo, keycloakUser);
                                        // keylock中username是系統帳號  firstname是用戶名稱
                                        keycloakUser.setUsername(vo.getRocId());
                                        keycloakUser.setFirstName(vo.getUserName());
                                        keycloakUser.setPassword(vo.getInitPassword());
                                        // todo 添加到users中
                                        keycloakService.createUser("elife_jdzq", keycloakUser);
                                        vo.setSerialNum("1");
                                        vo.setUserId(vo.getRocId());
                                        vo.setStatus("Unenabled");
                                        vo.setSmsFlag("1");
                                        vo.setMailFlag("1");
                                        jdUserBatchDao.addUsers(vo);
                                        UserEntityVo user = jdUserMgntService.getUser(vo.getRocId(), "elife_jdzq");
                                        //新增user_role表数据
                                        jdRoleService.insertUserRole(user.getId(),roleId.getRoleId());
                                        //新增user_dep表数据
                                        if (branchId == null){
                                            jdDeptMgntService.insertUserDep(user.getId(), depId.getDepId(),"","");
                                        }else {
                                            jdDeptMgntService.insertUserDep(user.getId(),depId.getDepId(),"",branchId.getBranchId());
                                        }
                                    } else {
                                        failLinkList.add(vo);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    failLinkList.add(vo);
                                }
                            }
                            if (vo.getActionType().equals("MODIFY")) {
                                DepartmentVo updateDepId = null;
                                RoleVo updateRoleId = null;
                                DepartmentVo updateBranchId = null;
                                try {
                                    // todo 處理更新邏輯
                                    if (StringUtils.isNotBlank(vo.getRocId())){
                                        vo.setFailResult("身份證字號為留空欄位，請檢查!");
                                        failLinkList.add(vo);
                                    }else {
                                        if (StringUtils.isNotBlank(vo.getInitPassword())) {
                                            vo.setFailResult("初始密碼為留空欄位，請檢查!");
                                            failLinkList.add(vo);
                                        }else {
                                            if (StringUtils.isEmpty(vo.getUserId())) {
                                                vo.setFailResult("系統帳號為必輸欄位，請檢查");
                                                failLinkList.add(vo);
                                            }else {
                                                JdUserVo updateUser = jdUserDao.getUser(vo.getUserId());
                                                if (updateUser == null){
                                                    vo.setFailResult("系統帳號不存在，請檢查!");
                                                    failLinkList.add(vo);
                                                }else {
                                                    if (StringUtils.isEmpty(vo.getStatus()) && StringUtils.isEmpty(vo.getRoleId())
                                                            && StringUtils.isEmpty(vo.getEffectiveDate()) && StringUtils.isEmpty(vo.getExpirationDate())
                                                            && StringUtils.isEmpty(vo.getDepId()) && StringUtils.isEmpty(vo.getBranchId())
                                                            && StringUtils.isEmpty(vo.getUserName()) && StringUtils.isEmpty(vo.getIcId())
                                                            && StringUtils.isEmpty(vo.getLoginSize()) && StringUtils.isEmpty(vo.getEmail())
                                                            && StringUtils.isEmpty(vo.getMobile())) {
                                                        vo.setFailResult("選填欄位需至少一個，請檢查!");
                                                        failLinkList.add(vo);
                                                    } else {
                                                        //登錄字號
                                                        if (StringUtils.isNotBlank(vo.getLoginSize())) {
                                                            if (vo.getLoginSize().equals("X") && StringUtils.isNotBlank(updateUser.getLoginSize())) {
                                                                vo.setFailResult("登錄字號無法清除!");
                                                            } else {
                                                                if (jdUserDao.countLoginSize(vo.getLoginSize()) > 0) {
                                                                    vo.setFailResult("登錄字號已存在，請檢查!");
                                                                }
                                                            }
                                                        }
                                                        if (StringUtils.isNotBlank(vo.getStatus())
                                                                && !vo.getStatus().equals("Unenabled")
                                                                && !vo.getStatus().equals("Enabled")
                                                                && !vo.getStatus().equals("Disabled")){
                                                            vo.setFailResult("帳號狀態代碼不存在，請檢查!");
                                                            failLinkList.add(vo);
                                                        }else {
                                                            if (StringUtils.isNotBlank(vo.getDepId())){
                                                                updateDepId = jdDeptMgntService.getDivDep("0",vo.getDepId());
                                                                if (updateDepId == null){
                                                                    vo.setFailResult("所屬通路代碼不存在，請檢查!");
                                                                }else {
                                                                    if (StringUtils.isNotBlank(vo.getIcId())){
                                                                        JdUserVo updateUserIC = jdUserDao.getUserIC(updateDepId.getDepId(),vo.getIcId());
                                                                        if (updateUserIC != null) {
                                                                            vo.setFailResult("所屬通路+業務員編號已存在，請檢查!");
                                                                        }
                                                                    }
                                                                    //分支機構
                                                                    if (StringUtils.isNotBlank(vo.getBranchId())){
                                                                        updateBranchId = jdDeptMgntService.getBranchId(updateDepId.getDepId(),
                                                                                vo.getBranchId());
                                                                        if (updateBranchId == null) {
                                                                            vo.setFailResult("分支機構代碼不存在，請檢查!");
                                                                        }
                                                                    }
                                                                    // 系統平臺角色代碼是否存在
                                                                    if (StringUtils.isNotBlank(vo.getRoleId())){
                                                                        updateRoleId = jdRoleService.getRoleId(vo.getRoleId(),updateDepId.getDepId());
                                                                        if (updateRoleId == null) {
                                                                            vo.setFailResult("系統平台角色代碼不存在，請檢查!");
                                                                        }
                                                                    }
                                                                }
                                                            }else {
                                                                updateDepId = jdDeptMgntService.getDivDep("0",updateUser.getBranchId());
                                                                if (updateDepId != null){
                                                                    if (StringUtils.isNotBlank(vo.getIcId())){
                                                                        JdUserVo updateUserIC = jdUserDao.getUserIC(updateDepId.getDepId(),vo.getIcId());
                                                                        if (updateUserIC != null) {
                                                                            vo.setFailResult("所屬通路+業務員編號已存在，請檢查!");
                                                                        }
                                                                    }
                                                                    //分支機構
                                                                    if (StringUtils.isNotBlank(vo.getBranchId())){
                                                                        updateBranchId = jdDeptMgntService.getBranchId(updateDepId.getDepId(),
                                                                                vo.getBranchId());
                                                                        if (updateBranchId == null) {
                                                                            vo.setFailResult("分支機構代碼不存在，請檢查!");
                                                                        }
                                                                    }
                                                                    // 系統平臺角色代碼是否存在
                                                                    if (StringUtils.isNotBlank(vo.getRoleId())){
                                                                        updateRoleId = jdRoleService.getRoleId(vo.getRoleId(),updateDepId.getDepId());
                                                                        if (updateRoleId == null) {
                                                                            vo.setFailResult("系統平台角色代碼不存在，請檢查!");
                                                                        }
                                                                    }
                                                                }
                                                            }
//
                                                            if (StringUtils.isEmpty(vo.getFailResult())) {
                                                                UserEntityVo user = jdUserMgntService.getUser(vo.getUserId(), "elife_jdzq");
                                                                jdUserBatchDao.updateUsers(vo);
                                                                //更新user_role表数据
                                                                if (updateRoleId != null && StringUtils.isNotBlank(updateRoleId.getRoleId())){
                                                                    jdRoleService.updateUserRole(user.getId(),updateRoleId.getRoleId());
                                                                }
                                                                //更新user_dep表数据
                                                                if (updateBranchId != null && StringUtils.isNotBlank(updateBranchId.getDepId())){
                                                                    int result = jdDeptMgntService.updateUserDep(user.getId(),updateDepId.getDepId(),updateBranchId.getBranchId());
                                                                    if (result == 0) {
                                                                        jdDeptMgntService.insertUserDep(user.getId(),updateDepId.getDepId(),"", updateBranchId.getBranchId());
                                                                    }
                                                                }
                                                            } else {
                                                                failLinkList.add(vo);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    vo.setFailResult("系統發生錯誤");
                                    failLinkList.add(vo);
                                }
                            }
                        }

                    }
                }
                //todo 失敗檔案寫入excel
                String readFailFilePath = null;
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFCell cell;
                XSSFSheet sheet = workbook.createSheet("Sheet1");
                // 创建表头
                XSSFRow row = sheet.createRow(0);
                List<String> headList = Lists.newArrayList("失敗原因","動作別"," 系統帳號"," 帳號狀態"," 初始密碼","系統平台角色","帳號生效日","帳號失效日","所屬通路","分支機構","姓名","業務員編號","登錄字號","身份證字號","EMAIL","行動電話");
                for (int i = 0; i < headList.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(headList.get(i));
                }
                // 寫入excel文件
                String DATE_FORMAT = "yyyyMMddHHmmss";
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Calendar c1 = Calendar.getInstance();
                String timeStr = sdf.format(c1.getTime());
                String fileName1 = "失敗檔案"+ timeStr + ".xlsx";
                //寫内容
                try {
                    readFailFilePath = filepath + File.separator + fileName1;
                    if (failLinkList.size() > 0){
                        File file = new File(readFailFilePath);
                        int k = 1;
                        for (JdUserVo jdUserVo : failLinkList) {
                            row = sheet.createRow(k);
                            row.createCell(0).setCellValue(jdUserVo.getFailResult());
                            row.createCell(1).setCellValue(jdUserVo.getActionType());
                            row.createCell(2).setCellValue(jdUserVo.getUserId());
                            row.createCell(3).setCellValue(jdUserVo.getStatus());
                            row.createCell(4).setCellValue(jdUserVo.getInitPassword());
                            row.createCell(5).setCellValue(jdUserVo.getRoleId());
                            row.createCell(6).setCellValue(jdUserVo.getEffectiveDate());
                            row.createCell(7).setCellValue(jdUserVo.getExpirationDate());
                            row.createCell(8).setCellValue(jdUserVo.getDepId());
                            row.createCell(9).setCellValue(jdUserVo.getBranchId());
                            row.createCell(10).setCellValue(jdUserVo.getUserName());
                            row.createCell(11).setCellValue(jdUserVo.getIcId());
                            row.createCell(12).setCellValue(jdUserVo.getLoginSize());
                            row.createCell(13).setCellValue(jdUserVo.getRocId());
                            row.createCell(14).setCellValue(jdUserVo.getEmail());
                            row.createCell(15).setCellValue(jdUserVo.getMobile());
                            k = k+1;
                            workbook.write(new FileOutputStream(file));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    workbook.close();
                }
                // todo 轉換成byte數組到數據庫
                byte[] buffer = null;
                FileInputStream fis = null;
                ByteArrayOutputStream bos = null;
                try {
                    File fail_file = new File(readFailFilePath);
                    fis = new FileInputStream(fail_file);
                    bos = new ByteArrayOutputStream();
                    byte[] b1 = new byte[1024];
                    int n;
                    while ((n = fis.read(b1)) != -1) {
                        bos.write(b1, 0, n);
                    }
                    fis.close();
                    bos.close();
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
                // todo 加入結束時間
                batch.setFailNum(failLinkList.size());
                batch.setBatchEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                batch.setBatchStatus("completed");
                jdBatchPlanDao.updateBatchPlan(batch);
            }
            //

        }
    }

    

    @Override
    public int addUsers(JdUserVo jdUserVo) {
        return jdUserBatchDao.addUsers(jdUserVo);
    }

    @Override
    public int updateUsers(JdUserVo jdUserVo) {
        return jdUserBatchDao.updateUsers(jdUserVo);
    }

    @Override
    public List<Map<String, Object>> getUsers(JdBatchSchedulVO vo) {
        return jdBatchPlanDao.getUsers(vo);
    }

    @Override
    public int countUsers(JdBatchSchedulVO vo) {
        return jdBatchPlanDao.countUsers(vo);
    }


}

