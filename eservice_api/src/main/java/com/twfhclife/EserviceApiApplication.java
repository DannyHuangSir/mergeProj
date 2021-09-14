package com.twfhclife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.twfhclife.scheduling.task.AllianceServiceTask;
import com.twfhclife.scheduling.task.DnsAllianceServiceTask;
import com.twfhclife.scheduling.task.CIOAllianceServiceTask;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class EserviceApiApplication extends SpringBootServletInitializer {
	
//	@Bean
//	public TaskScheduler taskScheduler() {
//		return new ConcurrentTaskScheduler();
//	}
	
	/**
	 * 保險理賠
	 * @return
	 */
	@Bean
	public  AllianceServiceTask allianTask() {
		return new AllianceServiceTask();
	}

	/**
	 * 死亡除戶
	 * @return
	 */
	@Bean
	public DnsAllianceServiceTask dnsTask() {
		return new DnsAllianceServiceTask();
	}
	
	/**
	 * 資料變更
	 * @return
	 */
	@Bean
	public CIOAllianceServiceTask cioTask() {
		return new CIOAllianceServiceTask();
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EserviceApiApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(EserviceApiApplication.class, args);
	}
	
}
