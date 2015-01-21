package com.ibm.gbsc.auth.user;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.ibm.gbsc.common.vo.BaseVO;

/**
 * Role class represents role.
 *
 * @author Johnny
 */
@Entity
@Table(name = "GBSC_AUTH_ROLE")
@NamedQueries({ @NamedQuery(name = "Role.getAll", query = "select rl from Role rl order by rl.authority", hints = {
        @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
@Cacheable
public class Role implements Serializable, GrantedAuthority, BaseVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 7823630113731403230L;
	private String authority;
	private String name;

	/**
	 *
	 */
	public Role() {
	}

	/**
	 * @return the id
	 */
	@Override
	@Id
	@Column(name = "ROLE_ID")
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setAuthority(String id) {
		this.authority = id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "ROLE_NAME")
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return authority;
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
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Role other = (Role) obj;
		if (authority == null) {
			if (other.authority != null) {
				return false;
			}
		} else if (!authority.equals(other.authority)) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	@Transient
	public String getId() {
		return authority;
	}

}
