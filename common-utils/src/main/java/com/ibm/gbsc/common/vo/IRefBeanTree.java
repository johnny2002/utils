/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.vo;

import java.util.Collection;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public interface IRefBeanTree {
	String getCode();

	String getName();

	Collection<? extends IRefBeanTree> getChildren();
}
