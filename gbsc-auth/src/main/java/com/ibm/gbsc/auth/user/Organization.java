package com.ibm.gbsc.auth.user;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.common.vo.RefBean;

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
public class Organization extends RefBean {
	/**
	 *
	 */
	private static final long serialVersionUID = 593172425890684180L;
	private String type;
	private int level;
	private boolean virtual;
	private String nodeCode;
	private Organization parent;
	private List<Organization> children;
	private Set<Role> roles;
	private Set<User> users;

	// public static String HEAD_NODE_CODE = "001";

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
	 * @return the children
	 */

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parent", fetch = FetchType.LAZY)
	public List<Organization> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */

	public void setChildren(List<Organization> childOrgs) {
		this.children = childOrgs;
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

}
