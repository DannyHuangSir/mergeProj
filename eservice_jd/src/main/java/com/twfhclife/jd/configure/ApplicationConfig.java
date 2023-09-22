package com.twfhclife.jd.configure;

import com.twfhclife.jd.interceptor.AuthenticationInterceptor;
import com.twfhclife.jd.interceptor.ErrorInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class ApplicationConfig extends WebMvcConfigurerAdapter {

	@Value("#{('/demopage,' + '${nonAuthPages}').split(',')}")
	private List<String> nonAuthPages;

	@Bean
	public AuthenticationInterceptor authenticationInterceptor() {
		return new AuthenticationInterceptor();
	}

	@Bean
	public ErrorInterceptor errorInterceptor() {
		return new ErrorInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration authInterceptor = registry.addInterceptor(authenticationInterceptor());
		for (String nonAuthPageUrl : nonAuthPages) {
			authInterceptor.excludePathPatterns(nonAuthPageUrl);
		}
		registry.addInterceptor(errorInterceptor());
	}
}