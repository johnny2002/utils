/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.auth.web;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@RequestMapping("/auth/resources")
public class ResourceController {
	public String list() {
		return "/auth/role/roles.ftl";
	}
}
