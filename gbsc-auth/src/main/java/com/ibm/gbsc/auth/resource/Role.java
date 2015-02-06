package com.ibm.gbsc.auth.resource;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

import com.ibm.gbsc.common.vo.RefBean;

/**
 * Role class represents role.
 *
 * @author Johnny
 */
@Entity
@Table(name = "GBSC_AUTH_ROLE")
@NamedQueries({ @NamedQuery(name = "Role.getAll", query = "select rl from Role rl order by rl.name") })
@Cacheable
public class Role extends RefBean implements GrantedAuthority {
	/**
	 *
	 */
	private static final long serialVersionUID = 8201712327305350327L;

	/**
	 *
	 */
	public Role() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	@Transient
	public String getAuthority() {
		return getCode();
	}

}
