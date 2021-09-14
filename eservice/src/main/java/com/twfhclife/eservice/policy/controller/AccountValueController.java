package com.twfhclife.eservice.policy.controller;

import org.springframework.stereotype.Controller;

import com.twfhclife.generic.controller.BaseController;

/**
 * 保單帳戶價值相關資料控制器.
 * 
 * @author alan
 */
@Controller
public class AccountValueController extends BaseController {

//	private static final Logger logger = LogManager.getLogger(AccountValueController.class);
//
//	@Autowired
//	private IPolicyListService policyListService;
//	
//	@Autowired
//	private IAccountValueService accountValueService;
//	
//	@Autowired
//	private ILilipmService lilipmService;
//	
//	@Autowired
//	private ILilipiService lilipiService;
//
//	/**
//	 * 帳戶價值通知書.
//	 * 
//	 * @return
//	 */
//	@RequestLog
//	@PostMapping("/listing8")
//	public String listing8(@RequestParam("policyNo") String policyNo) {
//		try {
//			PolicyVo policyVo = policyListService.findById(policyNo);
//			if (policyVo != null) {
//				String twEffectiveDate = DateUtil.getRocDate(policyVo.getEffectiveDate());
//				String twExpireDate = DateUtil.getRocDate(policyVo.getExpireDate());
//				
//				addAttribute("policyVo", policyVo);
//				addAttribute("policyStartEndDate", String.format("%s ~ %s", twEffectiveDate, twExpireDate));
//			}
//
//			// 要保人
//			addAttribute("lilipmVo", lilipmService.findByPolicyNo(policyNo));
//			// 被保人
//			addAttribute("lilipiVo", lilipiService.findByPolicyNo(policyNo));
//			// 帳戶價值通知書清單資料
//			addAttribute("accountValueList", accountValueService.getAccountValueList(policyNo));
//		} catch (Exception e) {
//			logger.error("Unable to get policy info from listing8: {}", ExceptionUtils.getStackTrace(e));
//			addDefaultSystemError();
//		}
//		return "frontstage/listing8";
//	}
//	
//	/**
//	 * 下載帳戶價值通知書.
//	 * 
//	 * @param sysName
//	 * @param response
//	 */
//	@RequestLog
//	@RequestMapping(value = "/downloadAccountValue")
//	public @ResponseBody HttpEntity<byte[]> downloadAccountValue(@RequestParam("policyNo") String policyNo,
//			@RequestParam("quaterCode") String quaterCode, HttpServletResponse response) {
//		byte[] document = null;
//		HttpHeaders header = new HttpHeaders();
//		try {
//			String fileName = String.format("inline; filename=帳戶價值通知書-%s.pdf", quaterCode);
//			
//			document = accountValueService.getAccountValueData(policyNo, quaterCode);
//			header.setContentType(new MediaType("application", "pdf"));
//			header.set("Content-Disposition", new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
//			if (document != null) {
//				header.setContentLength(document.length);
//			}
//		} catch (Exception e) {
//			logger.error("Unable to get data from downloadAccountValue: {}", ExceptionUtils.getStackTrace(e));
//		}
//		return new HttpEntity<byte[]>(document, header);
//	}
}