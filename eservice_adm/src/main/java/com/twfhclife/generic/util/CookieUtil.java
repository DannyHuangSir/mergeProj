package com.twfhclife.generic.util;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

	// cookie的有效期預設為1天
    public final static int COOKIE_MAX_AGE = 60 * 60 * 24 * 1; 
    //cookie加密時的額外的salt
    public final static String salt = "www.twfhcLife.com";
    //自動登錄的Cookie名
    public final static String RememberMe = "remember-me";
    //自動登錄的Cookie名
    public final static String SSOToken = "sso-token";

    
	/**
	 * 添加一個新Cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param cookie
	 *            新cookie
	 * 
	 * @return null
	 */
	public static void addCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie != null)
			response.addCookie(cookie);
	}

	/**
	 * 添加一個新Cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param cookieName
	 *            cookie名稱
	 * @param cookieValue
	 *            cookie值
	 * @param domain
	 *            cookie所屬的子域
	 * @param httpOnly
	 *            是否將cookie設置成HttpOnly
	 * @param maxAge
	 *            設置cookie的最大生存期
	 * @param path
	 *            設置cookie路徑
	 * @param secure
	 *            是否只允許HTTPS訪問
	 * 
	 * @return null
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, String domain,
			boolean httpOnly, int maxAge, String path, boolean secure) {
		if (cookieName != null && !cookieName.equals("")) {
			if (cookieValue == null)
				cookieValue = "";

			Cookie newCookie = new Cookie(cookieName, cookieValue);
			if (domain != null)
				newCookie.setDomain(domain);

			newCookie.setHttpOnly(httpOnly);

			if (maxAge > 0)
				newCookie.setMaxAge(maxAge);

			if (path == null)
				newCookie.setPath("/");
			else
				newCookie.setPath(path);

			newCookie.setSecure(secure);

			addCookie(response, newCookie);
		}
	}

	/**
	 * 添加一個新Cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param cookieName
	 *            cookie名稱
	 * @param cookieValue
	 *            cookie值
	 * @param domain
	 *            cookie所屬的子域
	 * 
	 * @return null
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, String domain) {
		addCookie(response, cookieName, cookieValue, domain, true, COOKIE_MAX_AGE, "/", false);
	}

	/**
	 * 根據Cookie名獲取對應的Cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param cookieName
	 *            cookie名稱
	 * 
	 * @return 對應cookie，如果不存在則返回null
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null || cookieName == null || cookieName.equals(""))
			return null;

		for (Cookie c : cookies) {
			if (c.getName().equals(cookieName))
				return (Cookie) c;
		}
		return null;
	}

	/**
	 * 根據Cookie名獲取對應的Cookie值
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param cookieName
	 *            cookie名稱
	 * 
	 * @return 對應cookie值，如果不存在則返回null
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie == null)
			return null;
		else
			return cookie.getValue();
	}

	/**
	 * 刪除指定Cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param cookie
	 *            待刪除cookie
	 */
	public static void delCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie != null) {
			cookie.setPath("/");
			cookie.setMaxAge(0);
			cookie.setValue(null);

			response.addCookie(cookie);
		}
	}

	/**
	 * 根據cookie名刪除指定的cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param cookieName
	 *            待刪除cookie名
	 */
	public static void delCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie c = getCookie(request, cookieName);
		if (c != null && c.getName().equals(cookieName)) {
			delCookie(response, c);
		}
	}

	/**
	 * 根據cookie名修改指定的cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param cookieName
	 *            cookie名
	 * @param cookieValue
	 *            修改之後的cookie值
	 * @param domain
	 *            修改之後的domain值
	 */
	public static void editCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, String domain) {
		Cookie c = getCookie(request, cookieName);
		if (c != null && cookieName != null && !cookieName.equals("") && c.getName().equals(cookieName)) {
			addCookie(response, cookieName, cookieValue, domain);
		}
	}
}
