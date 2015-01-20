package com.ibm.gbsc.auth.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.security.core.GrantedAuthority;

import com.ibm.banking.framework.dto.BaseVO;
import com.ibm.gbsc.auth.function.Function;
import com.ibm.gbsc.auth.resource.RoleResource;

/**
 * Role class represents role.
 * 
 * @author Johnny
 */
@Entity
@Table(name = "RI_NT_AUTH_ROLE")
@NamedQueries({
		@NamedQuery(name = "Role.getAll", query = "select rl from Role rl order by rl.authority", readOnly = true, cacheable = true, cacheRegion = "RefBean"),
		@NamedQuery(name = "Role.getByApp", query = "select rl from Role rl where rl.appId = :appId order by rl.name", readOnly = true, cacheable = true, cacheRegion = "RefBean") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "HRefBean")
public class Role implements Serializable, GrantedAuthority, BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7823630113731403230L;
	private String authority;
	private String name;
	private String appId;
	private List<Function> functions;
	private List<RoleResource> roleResList;

	/**
	 * 
	 */
	public Role() {
	}

	/**
	 * @param authority
	 *            role name
	 * @param appId
	 *            appid
	 */
	public Role(String authority, String appId) {
		this.authority = authority;
		this.appId = appId;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return authority;
	}

	/**
	 * @return the appId
	 */
	@Column(name = "APPID")
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		// result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
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
		if (appId == null) {
			if (other.appId != null) {
				return false;
			}
		} else if (!appId.equals(other.appId)) {
			return false;
		}
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
	@Transient
	public String getId() {
		return authority;
	}

	/**
	 * @return roleResList roleRes
	 */
	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL }, mappedBy = "role", fetch = FetchType.LAZY)
	public List<RoleResource> getRoleResList() {
		return roleResList;
	}

	/**
	 * @param roleResList
	 *            roleResList
	 */
	public void setRoleResList(List<RoleResource> roleResList) {
		this.roleResList = roleResList;
	}

	/**
	 * @return functions
	 */
	@ManyToMany
	@JoinTable(name = "RI_NT_AUTH_FUNC_ROLE", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "FUNC_ID") })
	public List<Function> getFunctions() {
		return functions;
	}

	/**
	 * @param functions
	 *            functions
	 */
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

}
