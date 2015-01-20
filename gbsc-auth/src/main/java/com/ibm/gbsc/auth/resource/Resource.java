/**
 *
 */
package com.ibm.gbsc.auth.resource;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author fanjingxuan
 */
@Entity
@Table(name = "RI_NV_AUTH_RESOURCE")
@NamedQueries({ @NamedQuery(name = "Resource.getAll", query = "From Resource r where r.parentRes is null order by r.resourceType", hints = {
        @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
public class Resource implements Serializable {
	/**
	 * 报表索引.
	 */
	public static final String RESOURCE_TYPE_RPT_INDEX = "1";

	/**
	 * indicator resource.
	 */
	public static final String RESOURCE_TYPE_INDICATORCATEGORY = "2";
	/**
	 *
	 */
	private static final long serialVersionUID = -5197939922820359198L;
	private String resourceId;
	private String resourceName;
	private String resourceType;

	private Resource parentRes;
	private List<Resource> childRes;
	private boolean checked;

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
	 * @return resourceId
	 */
	@Id
	@GeneratedValue
	@Column(name = "RESOURCE_ID")
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * @param resourceId
	 *            resourceId
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * @return resourceName
	 */
	@Column(name = "RESOURCE_NAME")
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * @param resourceName
	 *            resourceName
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return resourceType
	 */
	@Column(name = "RESOURCE_TYPE")
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType
	 *            resourceType
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return parentRes
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_RESOURCE", updatable = false)
	public Resource getParentRes() {
		return parentRes;
	}

	/**
	 * @param parentRes
	 *            parentRes
	 */
	public void setParentRes(Resource parentRes) {
		this.parentRes = parentRes;
	}

	/**
	 * @return childRes
	 */
	@OneToMany(mappedBy = "parentRes")
	public List<Resource> getChildRes() {
		return childRes;
	}

	/**
	 * @param childRes
	 *            childRes
	 */
	public void setChildRes(List<Resource> childRes) {
		this.childRes = childRes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
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
		Resource other = (Resource) obj;
		if (resourceId == null) {
			if (other.resourceId != null) {
				return false;
			}
		} else if (!resourceId.equals(other.resourceId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return boolean.
	 */
	@Transient
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            is to set value.
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
