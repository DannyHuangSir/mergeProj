package com.twfhclife.generic.exceptionhandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller 發生 uncatch exception 情況時，會統一在這個 class 被處理。
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(HttpServletRequest req, Exception e) {
		logger.error(e.getMessage(), e);
		String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
		Map<String, Object> error = Collections.singletonMap("error", errorMsg);
		return ResponseEntity.status(500).body(error);
	}

	/**
	 * Controller 裡標注 @RequestParam 的變數在 validate fail 時會丟出
	 * ConstraintViolationException。這個 method 專門處理此類 exception
	 *
	 * @param req
	 * @param e
	 *
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
		//logger.error(e.getMessage(), e);
		// "@NotBlank @RequestParam String myArg" 這樣的 validate 寫法在 validate fail 時無法得知
		// "哪個輸入參數名稱" 驗証失敗，這是 java reflection 本身的限制。
		// 用這類語法時要改寫成 "@NotBlank(myArg must not be blank) @RequestParam String
		// myArg"，程式裡的 validate annotation 要寫出 "完整出錯明細"，
		// 不然在處理 ConstraintViolationException 時只會知道驗証失敗的原因，卻不知道是哪個輸入參數名稱驗証失敗。
		List<String> errorMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.toList());
		Map<String, Object> error = Collections.singletonMap("error", errorMessages);
		return ResponseEntity.status(400).body(error);
	}

	/**
	 * Controller 裡標注 @RequestBody 的變數在 validate fail 時會丟出
	 * MethodArgumentNotValidException。這個 method 專門處理此類 exception
	 *
	 * @param req
	 * @param e
	 *
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleMethodArgumentNotValidException(HttpServletRequest req,
			MethodArgumentNotValidException e) {
		//logger.error(e.getMessage(), e);
		List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()) // 記錄 "fieldName + validateFailMessage"
				.collect(Collectors.toList());
		Map<String, Object> error = Collections.singletonMap("error", errorMessages);
		return ResponseEntity.status(400).body(error);
	}
}