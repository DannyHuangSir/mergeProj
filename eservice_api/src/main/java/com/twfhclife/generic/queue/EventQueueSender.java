package com.twfhclife.generic.queue;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventQueueSender {

	private static final Logger logger = LogManager.getLogger(EventQueueSender.class);

	public final static String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";

	public final static String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";

	@Value("${event.queue.url}")
	public String providerUrl;

	@Value("${event.queue.destination}")
	public String queueName;

	@Value("${event.queue.username}")
	public String username;

	@Value("${event.queue.password}")
	public String password;

	public void send(String message) {
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
			jmsContext = connectionFactory.createContext(username, password);
			jmsContext.createProducer().send(destination, message);
			logger.info("Sent message: {}", message);
		} catch (Exception e) {
			logger.error("Unable to send message to queue: {}", ExceptionUtils.getStackTrace(e));
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}
