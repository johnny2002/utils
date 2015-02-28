/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.gson;

import java.lang.reflect.Field;

import com.google.gson.FieldNamingStrategy;

/**
 * 类作用：
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
		return "css".equals(f.getName()) ? "iconSkin" : f.getName();
	}

}
