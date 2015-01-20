/**
 * 
 */
package com.ibm.gbsc.auth.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.ibm.banking.framework.dto.BaseVO;

/**
 * @author Johnny
 * 
 */
@Entity
@Table(name = "RI_NT_AUTH_USER")
@NamedQueries({ @NamedQuery(name = "User.getByOrgCode", query = "select u from User u inner join u.departments d where d.code = :code order by u.code", readOnly = true) })
public class User implements BaseVO, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8574148353464532503L;
	private String code;
	private String password;
	private String displayName;
	private String email;
	private String userDesc;
	private UserState status;
	private List<Organization> departments;
	private Set<Role> roles;

	/**
	 * @return the loginId
	 */
	@Id
	@Column(name = "USER_ID")
	public String getCode() {
		return code;
	}

	/**
	 * @param loginId
	 *            the loginId to set
	 */
	public void setCode(String loginId) {
		this.code = loginId;
	}

	/**
	 * @return the password
	 */
	@Column(name = "USER_PWD")
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the name
	 */
	@Column(name = "USER_NAME")
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	/**
	 * @return the email
	 */
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the org
	 */
	@ManyToMany
	@JoinTable(name = "RI_NT_AUTH_USER_ORG", joinColumns = { @JoinColumn(name = "USER_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ORG_CODE") })
	public List<Organization> getDepartments() {
		return departments;
	}

	/**
	 * @param deparments
	 *            departments
	 */
	public void setDepartments(List<Organization> deparments) {
		this.departments = deparments;
	}

	/**
	 * @return the roles
	 */
	@ManyToMany
	@JoinTable(name = "RI_NT_AUTH_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_CODE") })
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 
	 * @return The 1st non-virtual organization
	 */
	@Transient
	public Organization getOrg() {
		Organization hOrg = null;
		if (getDepartments() != null) {
			for (Organization org : getDepartments()) {
				if (hOrg != null && !org.isVirtual()) {
					if (hOrg.getLevel() > org.getLevel() || hOrg.getNodeCode().compareTo(org.getNodeCode()) > 0) {
						hOrg = org;
					}
				} else {
					hOrg = org;
				}
			}
		}
		return hOrg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.code;
	}

	/** {@inheritDoc} */
	@Transient
	public String getId() {
		return code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

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
		User other = (User) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	/**
	 * @return userDesc
	 */
	@Column(name = "USER_DESC")
	public String getUserDesc() {
		return userDesc;
	}

	/**
	 * @param userDesc
	 *            userDesc
	 */
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	/**
	 * @return the status
	 */
	@Enumerated(EnumType.STRING)
	public UserState getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(UserState status) {
		this.status = status;
	}

	@Transient
	public boolean isHeadUser() {
		boolean isHead = false;
		if (getDepartments() != null) {
			for (Organization org : getDepartments()) {
				if (org.getType().equals("HEAD")) {
					isHead = true;
					break;
				}
			}
		}
		return isHead;
	}

}
