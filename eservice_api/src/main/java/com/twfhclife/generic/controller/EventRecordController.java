//package com.twfhclife.generic.controller;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.Gson;
//import com.twfhclife.eservice.api.adm.domain.EventRecordRequestVo;
//import com.twfhclife.generic.queue.EventQueueSender;
//
///**
// * 業務事件訊息接收.
// * 
// * @author all
// */
//@RestController
//@RequestMapping("/event")
//public class EventRecordController extends BaseMvcController {
//
//	private static final Logger logger = LogManager.getLogger(EventRecordController.class);
//	
//	@Autowired
//	public EventQueueSender queueSender;
//
//	@PostMapping(value = "/quene/send", produces = { "application/json" })
//	public void receive(@RequestBody EventRecordRequestVo request) {
//		try {
//			Gson gson = new Gson();
//			String json = gson.toJson(request);
//			logger.info("Receive json: {}", json);
//			queueSender.send(json);
//		} catch (Exception e) {
//			logger.error("Unable to send message to quene: {}", ExceptionUtils.getStackTrace(e));
//		}
//	}
//}
