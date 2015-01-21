package com.ibm.gbsc.common.vo;

import java.io.Serializable;

/**
 * 分页查询参数基类.
 * 
 * @author Johnny
 * 
 */
public abstract class PagedQueryParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2198575204083560476L;
	private int pageNumber = 1;
	private int pageSize = 10;
	private String orderBy;
	private boolean needCount = true;

	/**
	 * 
	 */
	public void init() {
		pageNumber = 1;
		pageSize = 10;
		orderBy = null;
		needCount = true;
	}

	/**
	 * 要查询的页码，从1开始.
	 * 
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * 页面大小，缺省为每页10行.
	 * 
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 是否需要返回总记录数.
	 * 
	 * @return the needCount
	 */
	public boolean isNeedCount() {
		return needCount;
	}

	/**
	 * @param needCount
	 *            the needCount to set
	 */
	public void setNeedCount(boolean needCount) {
		this.needCount = needCount;
	}

	/**
	 * @return string.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            is to set value.
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
