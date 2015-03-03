/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.gson;

import java.lang.reflect.Field;

import com.google.gson.FieldNamingStrategy;

/**
 * 类作用：用于映射TreeNode的字段到ZTree字段
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class ZtreeNodeFieldNamingStrategy implements FieldNamingStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gson.FieldNamingStrategy#translateName(java.lang.reflect.Field
	 * )
	 */
	@Override
	public String translateName(Field f) {
		if ("css".equals(f.getName())) {
			return "iconSkin";
		}
		return f.getName();
	}

}
