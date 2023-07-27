package com.twfhclife.jd.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.jd.api_client.BaseRestClient;
import com.twfhclife.jd.api_model.*;
import com.twfhclife.jd.keycloak.model.KeycloakUser;
import com.twfhclife.jd.util.ApConstants;
import com.twfhclife.jd.util.RptUtils2;
import com.twfhclife.jd.web.dao.UsersDao;
import com.twfhclife.jd.web.domain.CaseQueryVo;
import com.twfhclife.jd.web.domain.NotePdfVo;
import com.twfhclife.jd.web.domain.PdfVo;
import com.twfhclife.jd.web.domain.PersonSortVo;
import com.twfhclife.jd.web.model.CaseVo;
import com.twfhclife.jd.web.model.PermQueryVo;
import com.twfhclife.jd.web.model.PolicyBaseVo;
import com.twfhclife.jd.web.service.ICaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private BaseRestClient baseRestClient;

    @Value("${eservice_api.policy.case.list.url}")
    private String caseListUrl;


    @Value("${eservice_api.case.process.url}")
    private String caseProcessUrl;

    @Override
    public CaseVo getCaseProcess(String userId, String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), caseProcessUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }

    @Value("${eservice_api.case.policy.info.url}")
    private String casePolicyInfoUrl;
    @Override
    public CaseVo getCasePolicyInfo(String userId, String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), casePolicyInfoUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }
    @Value("${eservice_api.case.note.content.url}")
    private String noteContentUrl;
    @Override
    public List<CaseVo> getNoteContent(String userId, String policyNo) {
        NoteContentDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo, ApConstants.SYSTEM_ID, userId)), noteContentUrl, NoteContentDataResponse.class);
        return responseObj.getCases();
    }

    @Value("${eservice_api.note.pdf.url}")
    private String notePdfUrl;
    @Override
    public byte[] getNotePdf(String userId, String policyNo, String noteKey) throws Exception {
        NotePdfDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PdfVo(policyNo, noteKey, ApConstants.SYSTEM_ID, userId)), notePdfUrl, NotePdfDataResponse.class);
        return generatePDF(responseObj.getNotePdf());
    }

    @Value("${eservice_api.personal.case.url}")
    private String personalCaseUrl;

    @Override
    public List<CaseVo> getPersonalCaseList(KeycloakUser user, PersonSortVo sort) {
        List<CaseVo> result = Lists.newArrayList();
        List<PermQueryVo> caseQuery = Lists.newArrayList();
        int role = usersDao.checkUserRole(user.getId());
        switch (role) {
            case 2:
                caseQuery.addAll(usersDao.getCaseQueryBySupervisor(user.getId()));
                break;
            case 3:
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId()));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId()));
                break;
            case 5:
                break;
            default:
                caseQuery.addAll(usersDao.getCaseQueryByUser(user.getId()));
                break;
        }

        if (!CollectionUtils.isEmpty(caseQuery)) {
            CaseQueryVo vo = new CaseQueryVo();
            vo.setCaseQuery(caseQuery);
            vo.setUserId(user.getUsername());
            vo.setSysId(ApConstants.SYSTEM_ID);
            vo.setSort(sort);
            PersonalCaseDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(vo), personalCaseUrl, PersonalCaseDataResponse.class);
            result.addAll(responseObj.getCaseList());
        }
        return result;
    }

    @Override
    public CaseListDataResponse getCaseList(KeycloakUser user, CaseQueryVo vo) {
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員
        int role = usersDao.checkUserRole(user.getId());
        List<PermQueryVo> caseQuery = Lists.newArrayList();
        switch (role) {
            case 2:
                caseQuery.addAll(usersDao.getCaseQueryBySupervisor(user.getId()));
                break;
            case 3:
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId()));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId()));
                break;
            case 5:
                break;
            default:
                caseQuery.addAll(usersDao.getCaseQueryByUser(user.getId()));
                break;
        }

        if (!CollectionUtils.isEmpty(caseQuery) || role == 5) {
            if (vo == null) {
                vo = new CaseQueryVo();
            }
            vo.setCaseQuery(caseQuery);
            vo.setUserId(user.getUsername());
            vo.setSysId(ApConstants.SYSTEM_ID);
            return baseRestClient.postApi(new Gson().toJson(vo), caseListUrl, CaseListDataResponse.class);
        }
        return new CaseListDataResponse();
    }

    private byte[] generatePDF(NotePdfVo pdfVo) throws Exception {
        Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
        String kaiuFontPath = kaiuResource.getFile().getPath();
        BaseFont bfChinese = BaseFont.createFont(kaiuFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Document document = new Document(PageSize.A4);
        Font zh = new Font(bfChinese, 12, Font.BOLD);
        Font tableFont = new Font(bfChinese, 12, Font.BOLD);
        // 段落
        Paragraph paragraph = new Paragraph("臺銀人壽核保照會單", new Font(bfChinese, 20, Font.BOLD));
        paragraph.setAlignment(1); // 設置文字居中 0靠左   1，居中     2，靠右
        paragraph.setIndentationLeft(12); // 設置左縮進
        paragraph.setIndentationRight(12); // 設置右縮進
        paragraph.setFirstLineIndent(24); // 設置首行縮進
        paragraph.setLeading(20f); // 行间距
        paragraph.setSpacingBefore(5f); // 設置段落上空白
        paragraph.setSpacingAfter(10f); // 設置段落下空白

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        PdfPTable table = new PdfPTable(new float[]{100f, 100f, 100f, 100f});

        table.addCell(RptUtils2.createCell("核保人員: " + pdfVo.getProcessorName(), zh, Element.ALIGN_LEFT, 2, false));
        table.addCell(RptUtils2.createCell("分機：2354", zh, Element.ALIGN_LEFT, 2, false));

        table.addCell(RptUtils2.createBlankCell(4, false));

        table.addCell(RptUtils2.createCell("保單號碼：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getPolicyNo(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("收件編號：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAccDocNo(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("要保人：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAppName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("經攬單位：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAgentCode() + "-" + pdfVo.getAgentName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("被保險人：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getInsName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("分支單位及分行：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getBranchDesc(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("主約險種：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getPolicyTypeName(), tableFont, Element.ALIGN_LEFT, 3, true));

        table.addCell(RptUtils2.createCell("照會日期：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getManageDate(), tableFont, Element.ALIGN_LEFT, 3, true));

        table.addCell(RptUtils2.createCell("經攬人姓名：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getpSalesName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("經攬人登錄字號：", tableFont, Element.ALIGN_RIGHT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getpSalesID(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createBlankCell(4, false));

        table.addCell(RptUtils2.createCell("請儘速補齊下列事項，並於" + pdfVo.getDueDate() + "日前送回本公司，逾期將先行" + "辦退，待資料補齊後再行受理。", tableFont, Element.ALIGN_LEFT, 4, false));

        table.addCell(RptUtils2.createBlankCell(4, false));

        table.addCell(RptUtils2.createCell("照會事項：", tableFont, Element.ALIGN_LEFT, 4, true));
        table.addCell(RptUtils2.createCell("照會項目", tableFont, Element.ALIGN_CENTER, 1, true));
        table.addCell(RptUtils2.createCell("照會訊息", tableFont, Element.ALIGN_CENTER, 3, true));
        if (!CollectionUtils.isEmpty(pdfVo.getNoteItems())) {
            pdfVo.getNoteItems().forEach(item -> {
                table.addCell(RptUtils2.createCell(item.getItemCode(), tableFont, Element.ALIGN_CENTER, 1, true));
                table.addCell(RptUtils2.createCell(item.getItemContent(), tableFont, Element.ALIGN_CENTER, 3, true));
            });
        }

        table.addCell(RptUtils2.createBlankCell(4, false));

        table.addCell(RptUtils2.createCell("備註：", tableFont, Element.ALIGN_LEFT, 4, false));
        table.addCell(RptUtils2.createCell(pdfVo.getNoteVerifyMemo(), tableFont, Element.ALIGN_LEFT, 4, false));

        table.addCell(RptUtils2.createBlankCell(4, false));
        table.addCell(RptUtils2.createCell("請以正式收到核保照會單為回復依據。", tableFont, Element.ALIGN_LEFT, 4, false));

        document.add(paragraph);
        document.add(table);
        document.close();
        return baos.toByteArray();
    }
}
