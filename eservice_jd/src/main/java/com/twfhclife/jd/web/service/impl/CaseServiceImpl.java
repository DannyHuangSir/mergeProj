package com.twfhclife.jd.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
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
import com.twfhclife.jd.web.model.PolicyVo;
import com.twfhclife.jd.web.service.ICaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {
    private static final Logger logger = LogManager.getLogger(CaseServiceImpl.class);

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
//        return generatePDF2(responseObj.getNotePdf());
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
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId(),""));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId(),"",""));
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
                caseQuery.addAll(usersDao.getCaseQueryByPassageWay(user.getId(), vo.getBranchId()));
                break;
            case 4:
                caseQuery.addAll(usersDao.getCaseQueryByIc(user.getId(),vo.getParentDep(),vo.getBranchId()));
                break;
            case 5:
                if(StringUtils.isNotBlank(vo.getParentDep()) || StringUtils.isNotBlank(vo.getBranchId())) {
                    caseQuery.addAll(usersDao.getCaseQueryByAdministrator(vo.getParentDep(), vo.getBranchId()));
                }
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

    private byte[] generatePDF2(NotePdfVo pdfVo) throws Exception {
        Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
        String kaiuFontPath = String.valueOf(kaiuResource.getFile().toURI());

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        html.append("<style>");
        html.append("@font-face {");
        html.append("font-family: kaiufont;");
        html.append("src: url(").append(kaiuFontPath).append(");");
        html.append("}");
        html.append("body {");
        html.append("font-family: kaiufont");
        html.append("}");
        html.append("table , th , td {");
        html.append("border-collapse: collapse;");
        html.append("}");
        html.append("th , td {");
        html.append("border: 1px solid black;");
        html.append("width: 25%;");
        html.append("}");
        html.append("table {");
        html.append("margin: 0 auto;");
        html.append("border: none;");
        html.append("}");
        html.append(".text-align-right {");
        html.append("text-align: right;");
        html.append("}");
        html.append(".text-align-left {");
        html.append("text-align: left;");
        html.append("}");
        html.append(".text-align-center {");
        html.append("text-align: center;");
        html.append("}");
        html.append(".container {");
        html.append("margin:15px;");
        html.append("padding: 40px;");
        html.append("}");
        html.append(".width-auto {");
        html.append("width: 100%;");
        html.append("}");
        html.append(".col-1 {");
        html.append("width:25%;");
        html.append("}");
        html.append(".col-2 {");
        html.append("width:50%;");
        html.append("}");
        html.append(".col-3 {");
        html.append("width:75%;");
        html.append("}");
        html.append(".col-4 {");
        html.append("width:100%;");
        html.append("}");
        html.append(".no-border {");
        html.append("border: none;");
        html.append("}");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<page class=\"A4\">");
        html.append("<div class=\"container\">");
        html.append("<div style=\"text-align: center;font-weight: bold;font-size: 30px;\">臺銀人壽核保照會單 </div>");
//        html.append("<div style=\"text-align: left;font-weight: bold;margin-bottom: 20px; margin-top: 20px;\">核保人員: ").append(pdfVo.getProcessorName()).append("分機：2354").append("</div>");
        html.append("<table class=\"width-auto\">");
        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-left no-border\" colspan=\"2\" >核保人員: ").append(pdfVo.getProcessorName()).append("</td>");
//        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getProcessorName()).append("</td>");
        html.append("<td class=\"col-1 text-align-left no-border\" colspan=\"2\" >分機：").append(pdfVo.getExtNumber()).append("</td>");
//        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getExtNumber()).append("</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">保單號碼：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getPolicyNo()).append("</td>");
        html.append("<td class=\"col-1 text-align-right\">收件編號：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getAccDocNo()).append("</td>");
        html.append("</tr>");

        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">要保人：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getAppName()).append("</td>");
        html.append("<td class=\"col-1 text-align-right\">經攬單位：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getAgentCode() + "-" + pdfVo.getAgentName()).append("</td>");
        html.append("</tr>");

        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">被保險人：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getInsName()).append("</td>");
        html.append("<td class=\"col-1 text-align-right\">分支單位及分行：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getBranchDesc()).append("</td>");
        html.append("</tr>");

        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">主約險種：</td>");
        html.append("<td class=\"col-3 text-align-left\" colspan=\"3\">").append(pdfVo.getPolicyTypeName()).append("</td>");
        html.append("</tr>");

        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">照會日期：</td>");
        html.append("<td class=\"col-3 text-align-left\" colspan=\"3\">").append(pdfVo.getManageDate()).append("</td>");
        html.append("</tr>");

        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-right\">經攬人姓名：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getpSalesName()).append("</td>");
        html.append("<td class=\"col-1 text-align-right\">經攬人登錄字號：</td>");
        html.append("<td class=\"col-1 text-align-left\">").append(pdfVo.getpSalesID()).append("</td>");
        html.append("</tr>");
        html.append("</table>");

        html.append("<div style=\"text-align: left;font-weight: bold;margin-bottom: 20px; margin-top: 20px;\">請儘速補齊下列事項，並於").append(pdfVo.getDueDate()).append("日前送回本公司，逾期將先行辦退，待資料補齊後再行受理。</div>");

        html.append("<table class=\"width-auto\">");
        html.append("<tr>");
        html.append("<td class=\"col-4 text-align-left\" colspan=\"4\">照會事項：</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td class=\"col-1 text-align-center\">照會項目</td>");
        html.append("<td class=\"col-3 text-align-center\" colspan=\"3\">照會訊息</td>");
        html.append("</tr>");

        if (!CollectionUtils.isEmpty(pdfVo.getNoteItems())) {
            pdfVo.getNoteItems().forEach(item -> {
                html.append("<tr>");
                html.append("<td class=\"col-1 text-align-center\" >").append(item.getItemCode()).append("</td>");
                html.append("<td class=\"col-3 text-align-left\" colspan=\"3\">").append(item.getItemContent()).append("</td>");
                html.append("</tr>");
            });
        }

        html.append("</table>");
        html.append("<div class=\"text-align-left\" style=\"font-weight: bold;margin-bottom: 20px; margin-top: 20px;\">備註：</div>");
        html.append("<div>").append(pdfVo.getNoteVerifyMemo()).append("</div>");
        html.append("<div class=\"text-align-left\" style=\"font-weight: bold;\">請以正式收到核保照會單為回復依據。");
        html.append("</div>");
        html.append("</div>");
        html.append("</page>");
        html.append("</body>");
        html.append("</html>");
        File file = new File("report2.html");
        if (file.exists()) {
            logger.info("report2.html is exist, path: {}", file.getAbsolutePath());
        } else {
            logger.info("report2.html is not exist");
            file.createNewFile();
        }

        byte[] pdfBytes;
        try (PrintStream printStream = new PrintStream(new FileOutputStream(file), true, "utf-8");
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            printStream.println(html);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(() -> {
                try {
                    return kaiuResource.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, "kaiu", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.withFile(file);
            builder.toStream(os);
            builder.run();
            pdfBytes = os.toByteArray();

        }
        return pdfBytes;
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
        table.addCell(RptUtils2.createCell("分機：" + pdfVo.getExtNumber(), zh, Element.ALIGN_LEFT, 2, false));

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
                table.addCell(RptUtils2.createCell(item.getItemContent(), tableFont, Element.ALIGN_LEFT, 3, true));
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
