package com.twfhclife.generic.configure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.twfhclife.generic.interceptor.AuthenticationInterceptor;
import com.twfhclife.generic.interceptor.CheckPolicyNoInterceptor;
import com.twfhclife.generic.interceptor.ErrorInterceptor;

@Configuration
public class ApplicationConfig extends WebMvcConfigurerAdapter {

	@Value("#{'${nonAuthPages}'.split(',')}") 
	private List<String> nonAuthPages;

	@Bean
	public AuthenticationInterceptor authenticationInterceptor() {
		return new AuthenticationInterceptor();
	}

	@Bean
	public CheckPolicyNoInterceptor checkPolicyNoInterceptor() {
		return new CheckPolicyNoInterceptor();
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
		
		registry.addInterceptor(checkPolicyNoInterceptor());
		registry.addInterceptor(errorInterceptor());
	}
}