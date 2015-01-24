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
	/**
	 * 记录总数，null表示未知，需要查询时求出总数，非null值表示已经知道总数，无需再次求值
	 */
	private Integer recordCount;

	/**
	 *
	 */
	public void init() {
		pageNumber = 1;
		pageSize = 10;
		orderBy = null;
		recordCount = null;
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

	/**
	 * @return the recordCount
	 */
	public Integer getRecordCount() {
		return recordCount;
	}

	/**
	 * @param recordCount
	 *            the recordCount to set
	 */
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
}
