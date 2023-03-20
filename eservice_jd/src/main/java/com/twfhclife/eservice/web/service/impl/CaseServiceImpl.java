package com.twfhclife.eservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twfhclife.eservice.api_client.BaseRestClient;
import com.twfhclife.eservice.api_model.CaseProcessDataResponse;
import com.twfhclife.eservice.api_model.NoteContentDataResponse;
import com.twfhclife.eservice.api_model.NotePdfDataResponse;
import com.twfhclife.eservice.api_model.PersonalCaseDataResponse;
import com.twfhclife.eservice.keycloak.model.KeycloakUser;
import com.twfhclife.eservice.util.RptUtils2;
import com.twfhclife.eservice.web.dao.UsersDao;
import com.twfhclife.eservice.web.domain.NotePdfVo;
import com.twfhclife.eservice.web.model.PermQueryVo;
import com.twfhclife.eservice.web.domain.CaseQueryVo;
import com.twfhclife.eservice.web.model.CaseVo;
import com.twfhclife.eservice.web.model.PolicyBaseVo;
import com.twfhclife.eservice.web.service.ICaseService;
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

    @Value("${eservice_api.personal.case.url}")
    private String personalCaseUrl;

    @Override
    public List<CaseVo> getCaseList(KeycloakUser user, CaseQueryVo vo) {
        // role == 1 一般人員 2 分行主管 3 通路主管 4 IC人員
        int role = usersDao.checkUserRole(user.getId());
        List<CaseVo> result = Lists.newArrayList();
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
            default:
                caseQuery.addAll(usersDao.getCaseQueryByUser(user.getId()));
                break;
        }
        if (!CollectionUtils.isEmpty(caseQuery)) {
            if (vo == null) {
                vo = new CaseQueryVo();
            }
            vo.setCaseQuery(caseQuery);
            PersonalCaseDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(vo), personalCaseUrl, PersonalCaseDataResponse.class);
            result.addAll(responseObj.getCaseList());
        }
        return result;
    }

    @Value("${eservice_api.case.process.url}")
    private String caseProcessUrl;

    @Override
    public CaseVo getCaseProcess(String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), caseProcessUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }

    @Value("${eservice_api.case.policy.info.url}")
    private String casePolicyInfoUrl;
    @Override
    public CaseVo getCasePolicyInfo(String policyNo) {
        CaseProcessDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), casePolicyInfoUrl, CaseProcessDataResponse.class);
        return responseObj.getCaseVo();
    }
    @Value("${eservice_api.case.note.content.url}")
    private String noteContentUrl;
    @Override
    public List<CaseVo> getNoteContent(String policyNo) {
        NoteContentDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), noteContentUrl, NoteContentDataResponse.class);
        return responseObj.getCases();
    }

    @Value("${eservice_api.note.pdf.url}")
    private String notePdfUrl;
    @Override
    public byte[] getNotePdf(String policyNo) throws Exception {
        NotePdfDataResponse responseObj = baseRestClient.postApi(new Gson().toJson(new PolicyBaseVo(policyNo)), notePdfUrl, NotePdfDataResponse.class);
        return generatePDF(responseObj.getNotePdf());
    }

    private byte[] generatePDF(NotePdfVo pdfVo) throws Exception {
        Resource kaiuResource = new ClassPathResource("fonts/kaiu.ttf");
        String kaiuFontPath = kaiuResource.getFile().getPath();
        BaseFont bfChinese = BaseFont.createFont(kaiuFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Document document = new Document(PageSize.A4);
        Font zh = new Font(bfChinese, 10, Font.NORMAL);
        Font tableFont = new Font(bfChinese, 12, Font.NORMAL);
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

        table.addCell(RptUtils2.createCell("地址：106 台北市大安區敦化南路二段 69 號 3 樓：", zh, Element.ALIGN_LEFT, 4, false));
        table.addCell(RptUtils2.createCell("傳真：(02)2702-5420", zh, Element.ALIGN_LEFT, 1, false));
        table.addCell(RptUtils2.createCell("(02)2706-0417", zh, Element.ALIGN_LEFT, 1, false));
        table.addCell(RptUtils2.createCell("電話：(02)27849151", zh, Element.ALIGN_LEFT, 1, false));
        table.addCell(RptUtils2.createCell("分機：2354", zh, Element.ALIGN_LEFT, 1, false));

        table.addCell(RptUtils2.createCell("保單號碼：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getPolicyNo(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("主約險種名稱：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getPolicyTypeName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("要保人：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAppName(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("被保險人：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getInsName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("經攬單位代碼：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAgentCode(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("經攬單位名稱：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAgentName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("分行-分支名稱：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getBranchDesc(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("照會確認日：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getManageDate(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("經辦姓名：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getProcessorName(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("經辦電話及分機：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getExtNumber(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("受理編號：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAccDocNo(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("經攬人姓名：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAgentSalesName(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("經攬人ID：", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getAgentSalesID(), tableFont, Element.ALIGN_LEFT, 1, true));

        table.addCell(RptUtils2.createCell("請儘速補齊以下事項，並於" + pdfVo.getDueDate() + "日前送回本公司，逾期將先行" +
                "辦退，待資料補齊後再行受理", tableFont, Element.ALIGN_LEFT, 4, false));
        table.addCell(RptUtils2.createCell("照會事項：", tableFont, Element.ALIGN_LEFT, 4, true));
        table.addCell(RptUtils2.createCell("照會項目", tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell("照會訊息", tableFont, Element.ALIGN_LEFT, 3, true));

        table.addCell(RptUtils2.createCell(pdfVo.getNote_Key(), tableFont, Element.ALIGN_LEFT, 1, true));
        table.addCell(RptUtils2.createCell(pdfVo.getContentMemo(), tableFont, Element.ALIGN_LEFT, 3, true));

        table.addCell(RptUtils2.createCell("備注：", tableFont, Element.ALIGN_LEFT, 4, false));

        table.addCell(RptUtils2.createCell(pdfVo.getNoteVerifyMemo(), tableFont, Element.ALIGN_LEFT, 4, false));

        table.addCell(RptUtils2.createCell("請以正式收到核保照會單為回復依據。", tableFont, Element.ALIGN_LEFT, 4, false));

        document.add(paragraph);
        document.add(table);
        document.close();
        return baos.toByteArray();
    }
}
