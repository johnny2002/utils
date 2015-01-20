/**
 *
 */
package com.ibm.gbsc.auth.resource;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.utils.vo.BaseVO;

/**
 * @author fanjingxuan
 */
@Entity
@Table(name = "RI_NT_AUTH_ROLE_RES")
@NamedQueries({
        @NamedQuery(name = "RoleResource.delByRoleId", query = "delete from RoleResource rr where rr.role.authority = :roleId and rr.operationType=:operationType"),
        @NamedQuery(name = "RoleResource.getByRoles", query = " from RoleResource rr where rr.role.authority in ( :roles ) and rr.resource.resourceType=:type "
                + "and rr.operationType>= :operationType", hints = { @QueryHint(name = "org.hibernate.readOnly", value = "true"),
                @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "RoleResource.delByResourceId", query = "delete from RoleResource rr where rr.resource.resourceId = :resourceId")

})
@Cacheable
public class RoleResource implements BaseVO, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2428421972761863843L;

	private Long id;
	private Role role;
	private Resource resource;
	private int operationType;

	/**
	 * @return operationType
	 */
	@Column(name = "OPERATION_TYPE")
	public int getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType
	 *            operationType
	 */
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	/**
	 * @param id
	 *            id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	@Id
	@GeneratedValue
	@Column(name = "ROLE_RESOURCE_ID")
	public Long getId() {
		return id;
	}

	/**
	 * @return role
	 */
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return resource resource
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_ID")
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            resource
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + operationType;
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
		RoleResource other = (RoleResource) obj;
		if (resource == null) {
			if (other.resource != null) {
				return false;
			}
		} else if (!resource.equals(other.resource)) {
			return false;
		}
		if (role == null) {
			if (other.role != null) {
				return false;
			}
		} else if (!role.equals(other.role)) {
			return false;
		} else if (operationType != other.operationType) {
			return false;
		}
		return true;
	}

}
