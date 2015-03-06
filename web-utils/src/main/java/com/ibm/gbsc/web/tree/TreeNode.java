/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.tree;

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
	/**
	 * 非叶子节点缺省是否打开
	 */
	private Boolean open;
	/**
	 * 当树有复选框时，这个标记是否选中
	 */
	private Boolean checked;
	/**
	 * 节点的css class name
	 */
	private String css;
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

	/**
	 * @return the css
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css
	 *            the css to set
	 */
	public void setCss(String css) {
		this.css = css;
	}

	/**
	 * @return the open
	 */
	public Boolean getOpen() {
		return open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(Boolean open) {
		this.open = open;
	}

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
