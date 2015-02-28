/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.common.vo;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@MappedSuperclass
public class RefBean implements BaseVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -2993123553842359025L;
	private String code;
	private String name;

	/**
	 * @return the code
	 */
	@Id
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		// result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefBean other = (RefBean) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		// if (name == null) {
		// if (other.name != null)
		// return false;
		// } else if (!name.equals(other.name))
		// return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.gbsc.common.vo.BaseVO#getId()
	 */
	@Override
	@Transient
	public Serializable getId() {
		return code;
	}

	/*
	 * 本方法还用于与code比较，以用于判断是否同一个code项，所以不要改写
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	final public String toString() {
		return code;
	}

}
