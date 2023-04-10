package com.twfhclife.generic.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.twfhclife.eservice.api.adm.domain.EventRecordRequestVo;
import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.IBusinessEventService;
import com.twfhclife.eservice.api.adm.service.ISystemEventService;

@Component
public class EventQueueScheduleReceiver {

	private static final Logger logger = LogManager.getLogger(EventQueueScheduleReceiver.class);

	public final static String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";

	public final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";

	@Autowired
	public IBusinessEventService businessEventService;

	@Autowired
	public ISystemEventService systemEventService;

	@Value("${event.queue.url}")
	public String providerUrl;

	@Value("${event.queue.destination}")
	public String queueName;

	@Value("${event.queue.username}")
	public String username;

	@Value("${event.queue.password}")
	public String password;

	@Value("${event.queue.receive.timeout}")
	public long timeout;

	@Value("${event.queue.receive.number}")
	public int maxReceiveNumber;

	@Value("${event.queue.schedule.receiver.enable: true}")
	public boolean enable;

	// 整點執行
	@Scheduled(cron = "0 50 16 * * ?")
	public void recevie() throws InterruptedException {
		if (!enable) {
			return;
		}
		JMSContext jmsContext = null;
		try {
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			env.put(Context.PROVIDER_URL, providerUrl);
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			
			Context context = new InitialContext(env);
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY);
			Destination destination = (Destination) context.lookup(queueName);
			// Create the JMS consumer
			logger.info("Begin receive queue message with maxReceiveNumber: {}", maxReceiveNumber);
			List<String> jsonList = new LinkedList<>();
			for (int i = 0; i < maxReceiveNumber; i++) {
				jmsContext = connectionFactory.createContext(username, password);
				JMSConsumer consumer = jmsContext.createConsumer(destination);
				String text = consumer.receiveBody(String.class, timeout);
				if (!StringUtils.isEmpty(text)) {
					logger.info("Received message with content: {}", text);
					jsonList.add(text);
					jmsContext.close();
				} else {
					logger.warn("Unable to Receive message: {}", "No message Received!");
					// 代表都收完了
					jmsContext.close();
					break;
				}
			}
			
			// 新增至DB
			for (String jsonData : jsonList) {
				try {
					EventRecordRequestVo eventReq = 
							new Gson().fromJson(jsonData, EventRecordRequestVo.class);
					BusinessEventVo businessEventVo = eventReq.getBusinessEvent();
					SystemEventVo systemEventVo = eventReq.getSystemEvent();
					
					int nextBusinessEventId = businessEventService.getNextId();
					businessEventVo.setBusinessEventId(nextBusinessEventId);
					systemEventVo.setBusinessEventId(nextBusinessEventId);

					businessEventService.add(businessEventVo);
					systemEventService.add(systemEventVo);
				} catch (Exception e) {
					logger.warn("Unable to insert data from EventRecordRequestVo: {}", ExceptionUtils.getStackTrace(e));
					continue;
				}
			}
		} catch (Exception e) {
			logger.error("Unable to receive message from quene: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}
