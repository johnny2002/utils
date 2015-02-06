/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class TreeNode implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4252694171782669207L;
	private String id;
	private String name;
	private Collection<TreeNode> children;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the children
	 */
	public Collection<TreeNode> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(Collection<TreeNode> children) {
		this.children = children;
	}

}
