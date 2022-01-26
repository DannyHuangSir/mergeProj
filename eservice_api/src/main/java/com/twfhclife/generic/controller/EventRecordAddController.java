package com.twfhclife.generic.controller;

import com.twfhclife.eservice.api.adm.domain.EventRecordRequestVo;
import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.IBusinessEventService;
import com.twfhclife.eservice.api.adm.service.ISystemEventService;
import com.twfhclife.generic.queue.EventQueueSender;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 業務事件訊息接收.
 *
 * @author all
 */
@RestController
@RequestMapping("/eventAdd")
public class EventRecordAddController extends BaseMvcController {

	private static final Logger logger = LogManager.getLogger(EventRecordAddController.class);

	@Autowired
	public EventQueueSender queueSender;

	@Autowired
	public IBusinessEventService businessEventService;

	@Autowired
	public ISystemEventService systemEventService;

	@PostMapping(value = "/quene/send", produces = { "application/json" })
	public void receive(@RequestBody EventRecordRequestVo request) {
		try {
			saveEvent(request);
		} catch (Exception e) {
			logger.error("Unable to send message to quene: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	@Transactional
	private void saveEvent(EventRecordRequestVo eventReq) {
		try {
			int nextBusinessEventId = businessEventService.getNextId();
			BusinessEventVo businessEventVo = eventReq.getBusinessEvent();
			if (businessEventVo != null) {
				businessEventVo.setBusinessEventId(nextBusinessEventId);
				businessEventService.add(businessEventVo);
			}

			List<SystemEventVo> systemEventList = eventReq.getSystemEventList();
			if (!CollectionUtils.isEmpty(systemEventList)) {
				for (SystemEventVo systemEventVo : systemEventList) {
					systemEventVo.setBusinessEventId(nextBusinessEventId);
					systemEventService.add(systemEventVo);
				}
			}
			if (eventReq.getSystemEvent() != null) {
				SystemEventVo systemEventVo = eventReq.getSystemEvent();
				systemEventVo.setBusinessEventId(nextBusinessEventId);
				systemEventService.add(systemEventVo);
			}
		} catch (Exception e) {
			logger.warn("Unable to insert data from EventRecordRequestVo: {}", ExceptionUtils.getStackTrace(e));
		}
	}
}
