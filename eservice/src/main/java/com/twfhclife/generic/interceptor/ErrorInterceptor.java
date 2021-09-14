package com.twfhclife.generic.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ErrorInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		int status = response.getStatus();
		if (status == 500) {
			modelAndView.setViewName("/frontstage/500");
		} else if (status == 404) {
			modelAndView.setViewName("/frontstage/404");
		} else if (status == 405) {
			modelAndView.setViewName("/frontstage/405");
		}
	}

}