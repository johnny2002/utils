/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class SessionCookieValueWriteFilter implements Filter {

	private String[] names;

	@Override
	public void destroy() {
		names = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(req, rep);
		HttpServletRequest request = (HttpServletRequest) req;

		HttpSession session = request.getSession(false);
		if (session != null) {
			HttpServletResponse response = (HttpServletResponse) rep;
			for (String name : names) {
				Object val = session.getAttribute(name);
				if (val != null) {
					Cookie cookie = new Cookie(name, (String) val);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
	}

	public void setCookieNames(String cookieNames) {
		names = cookieNames.split(",");

		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].trim();
		}

	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		setCookieNames(conf.getInitParameter("cookieNames"));
	}

}
