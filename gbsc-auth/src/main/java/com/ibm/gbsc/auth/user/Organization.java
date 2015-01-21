package com.ibm.gbsc.auth.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

/**
 * Organization object.
 *
 * @author Johnny
 */
@Entity
@Table(name = "GBSC_AUTH_ORG")
// @FilterDef(name="filterChildOrg")
@NamedQueries({
        @NamedQuery(name = "Organization.getByLevel", query = "select o from Organization o where o.level = :level order by o.code", hints = { @QueryHint(name = "org.hibernate.readOnly", value = "true") }),
        @NamedQuery(name = "Organization.getByLevelType", query = "select o from Organization o where o.level = :level and o.type = :type order by o.code", hints = { @QueryHint(name = "org.hibernate.readOnly", value = "true") }) })
public class Organization implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 593172425890684180L;
	private String code;
	private String name;
	private String type;
	private int level;
	private boolean virtual;
	private String nodeCode;
	private Organization parent;
	private List<Organization> childOrgs;
	private Set<Role> roles;
	private Set<User> users;

	// public static String HEAD_NODE_CODE = "001";

	/**
	 * @return the code
	 */
	@Id
	@Column(name = "ORG_CODE")
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
	@Column(name = "ORG_NAME")
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

	/**
	 * @return the level
	 */
	@Column(name = "ORG_LEVEL")
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_CODE")
	public Organization getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Organization parent) {
		this.parent = parent;
	}

	/**
	 * @return the nodeCode
	 */
	@Column(name = "NODE_CODE")
	public String getNodeCode() {
		return nodeCode;
	}

	/**
	 * @param nodeCode
	 *            the nodeCode to set
	 */
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	/**
	 * @return type
	 */
	@Column(name = "ORG_TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 是否虚拟组织: 如风险平台项目组是一个虚拟组织.
	 *
	 * @return virtual
	 */
	@Column(name = "IS_VIRTUAL")
	public boolean isVirtual() {
		return virtual;
	}

	/**
	 * @param virtual
	 *            virtual
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	/**
	 * @return the childOrgs
	 */

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parent", fetch = FetchType.LAZY)
	// @Filter(name="filterChildOrg",
	// condition=" (parent_org_id = '73' or  org_name like '%风险%' or org_name like '%客户%') ")
	public List<Organization> getChildOrgs() {
		return childOrgs;
	}

	/**
	 * @param childOrgs
	 *            the childOrgs to set
	 */

	public void setChildOrgs(List<Organization> childOrgs) {
		this.childOrgs = childOrgs;
	}

	/**
	 * @return roles
	 */
	@ManyToMany
	@JoinTable(name = "GBSC_AUTH_ORG_ROLE", joinColumns = { @JoinColumn(name = "ORG_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            roles
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return users
	 */
	@ManyToMany(mappedBy = "departments")
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            users
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
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
		Organization other = (Organization) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

}
