/**
 *
 */
package com.ibm.gbsc.auth.user;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ibm.gbsc.common.vo.BaseVO;

/**
 * @author Johnny
 *
 */
@Entity
@Table(name = "GBSC_AUTH_USER")
@NamedQueries({ @NamedQuery(name = "User.getByOrgCode", query = "select u from User u inner join u.departments d where d.code = :code order by u.code", hints = { @QueryHint(name = "org.hibernate.readOnly", value = "true") }) })
public class User implements BaseVO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8574148353464532503L;
	private String code;
	private String password;
	private String fullName;
	private String email;
	private Date birthDate;
	/**
	 * 分机号码
	 */
	private String extNumber;
	/**
	 * 手机号码
	 */
	private String mobileNumber;
	private UserState status;
	private List<Organization> departments;
	private Set<Role> roles;

	/**
	 * @return the loginId
	 */
	@Id
	@Column(name = "USER_CODE")
	@NotNull
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
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setFullName(String name) {
		this.fullName = name;
	}

	/**
	 * @return the email
	 */
	@Pattern(regexp = "^(([a-z0-9]*[-_\\.]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{1,3})?)?$", message = "auth.valid.email")
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
	@JoinTable(name = "GBSC_AUTH_USER_ORG", joinColumns = { @JoinColumn(name = "USER_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ORG_CODE") })
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
	@JoinTable(name = "GBSC_AUTH_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_CODE") })
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
					if (hOrg.getLevel() > org.getLevel()) {
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
	@Override
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
	 * @return the status
	 */
	@Enumerated(EnumType.STRING)
	@NotNull
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

	/**
	 * @return the birthDate
	 */
	@Column(columnDefinition = "DATE")
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the extNumber
	 */
	public String getExtNumber() {
		return extNumber;
	}

	/**
	 * @param extNumber
	 *            the extNumber to set
	 */
	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

}
