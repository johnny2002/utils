/**
 *
 */
package com.ibm.gbsc.auth.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ibm.gbsc.common.vo.RefBean;

/**
 * 类作用：资源实体
 * 
 * @author Johnny@cn.ibm.com 使用说明：
 */
@Entity
@Table(name = "GBSC_AUTH_RESOURCE")
@NamedQueries({ @NamedQuery(name = "Resource.getAll", query = "From Resource r where r.parent is null order by r.type") })
public class Resource extends RefBean {
	/**
	 *
	 */
	private static final long serialVersionUID = -5197939922820359198L;
	/**
	 * 类型，如:报表，指标，客户等，各系统可以根据自己定义不同的类型
	 */
	private String type;

	private Resource parent;
	private List<Resource> children;
	private List<Role> roles;

	/**
	 * operation types.
	 *
	 * @author Johnny
	 *
	 */
	public final class OperationType {

		/**
		 * default contructor.
		 */
		private OperationType() {
		}

		/**
		 * read permission.
		 *
		 */
		public static final int READ = 1;
		/**
		 * write permission.
		 */
		public static final int EDIT = 2;
	}

	/**
	 * @return type
	 */
	@Column(name = "RESOURCE_TYPE")
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            type
	 */
	public void setType(String resourceType) {
		this.type = resourceType;
	}

	/**
	 * @return parent
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_RESOURCE", updatable = false)
	public Resource getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            parent
	 */
	public void setParent(Resource parentRes) {
		this.parent = parentRes;
	}

	/**
	 * @return children
	 */
	@OneToMany(mappedBy = "parent")
	public List<Resource> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            children
	 */
	public void setChildren(List<Resource> childRes) {
		this.children = childRes;
	}

	/**
	 * @return the roles
	 */
	@ManyToMany
	@JoinTable(name = "GBSC_AUTH_ROLE_RES", joinColumns = { @JoinColumn(name = "RES_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_CODE") })
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
