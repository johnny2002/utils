/**
 *
 */
package com.ibm.gbsc.utils.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Johnny
 *
 */
@MappedSuperclass
public abstract class AuditVO implements Serializable, BaseVO {
	/**
	 *
	 */
	private static final long serialVersionUID = -3470794757789839181L;
	private String createUser;
	private String lastUpdateUser;
	private Date createTime;
	private Date lastUpdateTime;

	/**
	 * @return string.
	 */
	@Column(name = "CREATE_USER", length = 45)
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *            is to set value.
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return string.
	 */
	@Column(name = "UPDATE_USER", length = 45)
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	/**
	 * @param updateUser
	 *            is to set value.
	 */
	public void setLastUpdateUser(String updateUser) {
		this.lastUpdateUser = updateUser;
	}

	/**
	 * @return date.
	 */
	@Basic
	@Column(name = "CREATE_DATE", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            is to set value.
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return date.
	 */
	@Basic
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param updateTime
	 *            is to set value.
	 */
	public void setLastUpdateTime(Date updateTime) {
		this.lastUpdateTime = updateTime;
	}
}
