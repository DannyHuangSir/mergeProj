package com.twfhclife.adm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.twfhclife.adm.domain.ResponseObj;
import com.twfhclife.adm.service.IMyTestService;
import com.twfhclife.generic.controller.BaseMvcController;
import com.twfhclife.generic.util.ApConstants;
import com.twfhclife.generic.util.MyStringUtil;
import com.twfhclife.keycloak.model.KeycloakUser;

@Controller
@EnableAutoConfiguration
public class MainController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(MainController.class);

	@Autowired
	IMyTestService myTestService;

	@Value("${eservice.es.log.filename}")
	protected String logEsFilename;
	@Value("${eservice.esadm.log.filename}")
	protected String logAdmFilename;

	/**
	 * default index page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}

	@GetMapping("/demopage")
	public String demopage() {
		logger.info("open demo/demopage.html");
		return "autho/demopage";
	}

	@GetMapping("/testpage")
	public String testpage(HttpServletRequest request) {
		logger.info("open testpage.html");
		Object KeycloakUserObj = request.getSession().getAttribute(ApConstants.KEYCLOAK_USER);
		if (KeycloakUserObj != null) {
			 KeycloakUser keycloakUser = (KeycloakUser) KeycloakUserObj;
			 
			 //modify by 203990_20220429
			 if(! ( "admin".equals(keycloakUser.getUsername()) || "202712".equals(keycloakUser.getUsername()) || "203990".equals(keycloakUser.getUsername()) ) ) {
				 return "index";
			 }
		} else {
			return "login";
		}
		return "autho/testpage";
	}

	@PostMapping("/test/unlockss")
	public ResponseEntity<ResponseObj> unlockss(@RequestBody String upw) {
		ResponseObj responseObj = new ResponseObj();
		try {
			if (upw != null && upw.equals("Adm123456")) {
				responseObj.setResult("SUCCESS");

			} else {
				responseObj.setResult("FAIL");
				responseObj.setResultData("unlock fail!");
			}
		} catch (Exception e) {
			responseObj.setResult("ERROR");
			responseObj.setResultData("unlock error!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@PostMapping("/test/getResult")
	public ResponseEntity<ResponseObj> testaction(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
			logger.debug("test sql : " + ss);
			res = myTestService.getQueryResult(ss);
			logger.debug("test sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(responseObj);

		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@PostMapping("/test/getAdmResult")
	public ResponseEntity<ResponseObj> testadm(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
			logger.debug("test adm sql : " + ss);
			res = myTestService.getAdmQueryResult(ss);
			logger.debug("test adm sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			//logger.error(e.getMessage(), e);
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(responseObj);

		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}


	@PostMapping("/test/getEserviceResult")
	public ResponseEntity<ResponseObj> getEserviceResult(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
		logger.debug("test getEserviceResult() sql: \n" + ss);
		res = myTestService.getEserviceResult(ss);
		logger.debug("test getEserviceResult() sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return  ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@PostMapping("/test/getEserviceJdResult")
	public ResponseEntity<ResponseObj> getEserviceJdResult(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
		logger.debug("test getEserviceJdResult() sql: \n" + ss);
		res = myTestService.getEserviceJdResult(ss);
		logger.debug("test getEserviceJdResult() sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return  ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@PostMapping("/test/getUnderwritingResult")
	public ResponseEntity<ResponseObj> getUnderwritingResult(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
		logger.debug("test getUnderwritingResult() sql: \n" + ss);
		res = myTestService.getUnderwritingResult(ss);
		logger.debug("test getUnderwritingResult() sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return  ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@PostMapping("/test/getShouxianResult")
	public ResponseEntity<ResponseObj> getShouxianResult(@RequestBody String ss) {
		ResponseObj responseObj = new ResponseObj();
		List<Map<String, Object>> res = null;
		try {
		logger.debug("test getShouxianResult() sql: \n" + ss);
		res = myTestService.getShouxianResult(ss);
		logger.debug("test getShouxianResult() sql result: " + (res == null ? "null" : res.size()));
		} catch (Exception e) {
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return  ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}
		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}

	@RequestMapping(path = "/test/logs", method = RequestMethod.GET)
	public ResponseEntity<Resource> getEsLog(@RequestParam String sys, @RequestParam String xx) {
		if (MyStringUtil.isNullOrEmpty(xx) || !xx.equals("oxHpuv5fmB6uWIQAlqhPOw==")) {
			return ResponseEntity.badRequest().build();
		}
		ByteArrayResource resource = null;
		HttpHeaders headers = new HttpHeaders();
		File file = null;
		try {
			if (sys.equals("es")) {
				file = new File(logEsFilename);
			} else if (sys.equals("adm")) {
				file = new File(logAdmFilename);
			}
			if (file != null) {
				java.nio.file.Path path = Paths.get(file.getAbsolutePath());
				resource = new ByteArrayResource(Files.readAllBytes(path));
			}
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=eservice_log.txt");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().headers(headers).contentLength(file == null ? 0 : file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}

	@PostMapping("/test/runshell")
	public ResponseEntity<ResponseObj> testshell(@RequestBody String sh) throws InterruptedException {
		ResponseObj responseObj = new ResponseObj();
		List<String> res = new ArrayList<>();

		String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(sh);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
				System.out.println("line: " + s);
				res.add("line: " + s);
            }   
            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            res.add("exit: " + p.exitValue());
            p.destroy();
        } catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw e;
        } catch (IOException e) {
			logger.error(e.getMessage(), e);
			responseObj.setResult("ERROR");
			responseObj.setResultData(e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}

		responseObj.setResult("SUCCESS");
		responseObj.setResultData(res);
		return ResponseEntity.status(HttpStatus.OK).body(responseObj);
	}


}
