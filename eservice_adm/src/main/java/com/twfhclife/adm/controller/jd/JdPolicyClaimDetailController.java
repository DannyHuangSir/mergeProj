package com.twfhclife.adm.controller.jd;

import com.google.common.collect.Lists;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.model.*;
import com.twfhclife.adm.service.IJdPolicyClaimDetailService;
import com.twfhclife.generic.annotation.RequestLog;
import com.twfhclife.generic.controller.BaseController;
import com.twfhclife.generic.model.PolicyClaimDetailResponse;
import com.twfhclife.generic.model.UserDetailResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @auther lihao
 */
@Controller
public class JdPolicyClaimDetailController extends BaseController {


    @Autowired
    private IJdPolicyClaimDetailService jdPolicyClaimDetailService;

    private static final Logger logger = LoggerFactory.getLogger(JdPolicyClaimDetailController.class);

    @GetMapping("/policyClaimDetail")
    public String rptInsClaimDetail() {
        return  "backstage/jd/policyClaimDetail1";
    }

    @GetMapping("/userDetail")
    public String userDetail() { return "backstage/jd/userDetail"; }

    @RequestLog
    @PostMapping("/userDetail/filter")
    public String userDetailFilter(JdUserDetailVo vo) {
        addAttribute("vo", vo);
        return "backstage/jd/userDetail2";
    }

    @RequestLog
    @PostMapping("/policyClaimDetail/filter")
    public String policyClaimDetailFilter(JdPolicyClaimDetailVo vo) {
        addAttribute("vo", vo);
        return "backstage/jd/policyClaimDetail2";
    }

    @RequestLog
    @PostMapping("/userDetail/csv")
    public String userDetailCsv(JdUserDetailReqVo vo) {
        UserDetailResponse report1 = jdPolicyClaimDetailService.getUserDetail(vo);
        addAttribute("vo", vo);
        addAttribute("reportList", report1 != null ? report1.getUserDetailVos() : Lists.newArrayList());
        return "backstage/jd/userDetail3";
    }


    @RequestLog
    @PostMapping("/policyClaimDetail/csv")
    public String policyClaimDetailCSV1(JdPolicyClaimReqVo vo) {
        PolicyClaimDetailResponse report1 = jdPolicyClaimDetailService.getInsClaimStatisticsReport(vo);
        if (report1 != null && CollectionUtils.isNotEmpty(report1.getPolicyClaimDetailVo())) {
            int size = computeMaxNoteSize(report1.getPolicyClaimDetailVo());
            List<String> newColumn = Lists.newArrayList();
            List<String> newColumnName = Lists.newArrayList();
            List<String> lilipiColumn = Lists.newArrayList("noteDate", "dueDate", "itemContent", "contentMemo");
            List<String> lilipiColumnName =  Lists.newArrayList("照會日期", "照會回復截止日", "照會項目", "內容補充");

            report1.getPolicyClaimDetailVo().forEach(data -> {
                int tmpSize = (size <= 0 ? 1 : size);
                if (data.getNotes().size() < tmpSize) {
                    IntStream.range(0, tmpSize - data.getNotes().size()).forEach(idx -> data.getNotes().add(new JdClaimSubDetailVo()));
                }
            });

            if (size > 1) {
                processColumns(vo, size, newColumn, newColumnName, lilipiColumn, lilipiColumnName);
            }
        }

        addAttribute("vo", vo);
        addAttribute("reportList", report1 != null ? report1.getPolicyClaimDetailVo() : Lists.newArrayList());
        return   "backstage/jd/policyClaimDetail3";
    }

    private static void processColumns(JdPolicyClaimReqVo vo, int size, List<String> newColumn, List<String> newColumnName, List<String> lilipiColumn, List<String> lilipiColumnName) {
        vo.getColumn().forEach(c -> {
            if (lilipiColumn.contains(c)) {
                IntStream.range(0, size).forEach(idx -> newColumn.add(c));
            } else {
                newColumn.add(c);
            }
        });
        vo.getColumnName().forEach(c -> {
            if (lilipiColumnName.contains(c)) {
                IntStream.range(0, size).forEach(idx -> newColumnName.add(c));
            } else {
                newColumnName.add(c);
            }
        });
        vo.setColumn(newColumn);
        vo.setColumnName(newColumnName);
    }

    private int computeMaxNoteSize(List<JdPolicyClaimDetailVo> claimDetailVos) {
        int size = 0;
        for (JdPolicyClaimDetailVo claimDetailVo : claimDetailVos) {
            if (claimDetailVo.getNotes().size() > size) {
                size = claimDetailVo.getNotes().size();
            }
        }
        return size;
    }

    @PostMapping("/getBpmcurrenttak")
    public ResponseEntity<ResponseObj> getBpmcurrenttak(JdPolicyClaimDetailVo vo){
        try {
            processSuccess(jdPolicyClaimDetailService.getBpmcurrenttak());
        }catch (Exception e){
            logger.error("Unable to getBpmcurrenttak: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


    @PostMapping("/jd/getPolicyTypeNameList")
    public ResponseEntity<ResponseObj> getPolicyTypeNameList(JdPolicyClaimDetailVo vo){
        try {
            PolicyClaimDetailResponse  policyTypeName = jdPolicyClaimDetailService.getPolicyTypeNameList(vo);
            processSuccess(policyTypeName.getPolicyClaimDetailVo());
        }catch (Exception e){
            logger.error("Unable to getPolicyTypeNameList: {}", ExceptionUtils.getStackTrace(e));
            processSystemError();
        }
        return processResponseEntity();
    }


}

