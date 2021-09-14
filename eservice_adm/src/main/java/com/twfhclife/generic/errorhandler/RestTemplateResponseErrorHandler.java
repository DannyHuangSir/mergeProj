package com.twfhclife.generic.errorhandler;

import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.twfhclife.generic.util.MyJacksonUtil;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	private static final Logger logger = LogManager.getLogger(RestTemplateResponseErrorHandler.class);

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		if (!"".equals(MyJacksonUtil.object2Json(httpResponse))) {
			logger.debug("##hasError:", MyJacksonUtil.object2Json(httpResponse));
		}
		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		if (!"".equals(MyJacksonUtil.object2Json(httpResponse))) {
			logger.debug("##handleError:", MyJacksonUtil.object2Json(httpResponse));
		}
		if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {// handle SERVER_ERROR

		} else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {// handle CLIENT_ERROR
			if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new NotFoundException();
			}
		}
	}
}