/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.gbsc.common.vo.IRefBeanTree;
import com.ibm.gbsc.common.vo.RefBean;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class TreeUtil {
	public static interface TreeNodePropertySetter<T extends IRefBeanTree> {
		void setTreeNodeProperty(TreeNode node, T bean);
	}

	public static <T extends IRefBeanTree> List<TreeNode> toTreeNodes(Collection<? extends IRefBeanTree> collection) {
		return toTreeNodes(collection, null, null);
	}

	public static <T extends IRefBeanTree> List<TreeNode> toTreeNodes(Collection<? extends IRefBeanTree> collection,
	        Collection<? extends RefBean> checkedBeans) {
		return toTreeNodes(collection, checkedBeans, null);
	}

	public static <T extends IRefBeanTree> List<TreeNode> toTreeNodes(Collection<? extends IRefBeanTree> collection,
	        Collection<? extends RefBean> checkedBeans, TreeNodePropertySetter<T> treeNodePropertySetter) {
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>(collection.size());
		for (IRefBeanTree org : collection) {
			TreeNode node = new TreeNode();
			nodes.add(node);
			node.setId(org.getCode());
			node.setName(org.getName());
			// node.setCss(org.isVirtual() ? "virtualOrg" : "organization");
			node.setOpen(true);
			if (checkedBeans != null && checkedBeans.contains(org)) {
				node.setChecked(Boolean.TRUE);
			}
			if (org.getChildren() != null && !org.getChildren().isEmpty()) {
				node.setChildren(toTreeNodes(org.getChildren(), checkedBeans, treeNodePropertySetter));
			}
		}
		return nodes;

	}
}
