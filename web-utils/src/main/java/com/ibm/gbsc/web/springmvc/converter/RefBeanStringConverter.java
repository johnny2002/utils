/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.springmvc.converter;

import org.springframework.core.convert.converter.Converter;

import com.ibm.gbsc.common.vo.RefBean;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class RefBeanStringConverter implements Converter<RefBean, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.core.convert.converter.Converter#convert(java.lang
	 * .Object)
	 */
	@Override
	public String convert(RefBean refBean) {
		return refBean.getName();
	}

}
