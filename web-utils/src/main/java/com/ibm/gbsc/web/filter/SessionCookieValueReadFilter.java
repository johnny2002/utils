/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class SessionCookieValueReadFilter implements Filter {
	private Set<String> names;

	@Override
	public void destroy() {
		names.clear();
		names = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		HttpSession session = request.getSession();

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("/".equals(c.getPath()) && names.contains(c.getName())) {
					session.setAttribute(c.getName(), c.getValue());
				}
			}
		}

		chain.doFilter(req, rep);
	}

	public void setCookieNames(String cookieNames) {
		String[] cnames = cookieNames.split(",");
		names = new HashSet<String>(cnames.length * 2);
		for (String cookieName : cnames) {
			names.add(cookieName.trim());
		}
	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		setCookieNames(conf.getInitParameter("cookieNames"));

	}

}
