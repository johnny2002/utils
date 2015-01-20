package com.ibm.gbsc.auth.function;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.utils.vo.BaseVO;

/**
 * 功能.
 *
 * @author Johnny
 */
@Entity
@Table(name = "RI_NT_AUTH_FUNC8")
@NamedQueries({
        @NamedQuery(name = "Function.getAll", query = "select menu from Function menu where menu.active = true", hints = {
                @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Function.get1stLevel", query = "select menu from Function menu where menu.parent is null and menu.active = true order by menu.seq", hints = {
                @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Function.getTOP", query = "select menu from Function menu where menu.top = true and menu.active = true order by menu.seq", hints = {
                @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Function.getMenu", query = "select menu from Function menu where menu.menu = true and menu.parent is null and menu.active = true order by menu.seq", hints = {
                @QueryHint(name = "org.hibernate.readOnly", value = "true"), @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
@Cacheable
public class Function implements BaseVO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6307803926228004030L;
	private String id;
	/**
	 * 是否菜单
	 */
	private boolean menu;
	/**
	 * 是否顶部tab
	 */
	private boolean top;
	/**
	 * 功能名称
	 */
	private String name;
	/**
	 * 功能URL，主要用于菜单
	 */
	private String url;
	/**
	 * 在何处打开目标 URL，如 _blank, _parent, _self, _top, framename, windowname
	 */
	private String targetWindow;
	/**
	 * 小窗口url
	 */
	private String portletUrl;
	/**
	 * 小窗口css
	 *
	 */
	private String portletCss;
	/**
	 * 菜单项的CSS
	 */
	private String menuCss;
	/**
	 * 顺序号
	 */
	private int seq;

	/**
	 * 是否激活
	 */
	private Boolean active;
	private Function parent;
	private List<Function> children;
	private Set<Role> roles;

	/**
	 * @return the id
	 */
	@Override
	@Id
	@Column(length = 20)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param menuName
	 *            menu name
	 */
	public void setName(String menuName) {
		this.name = menuName;
	}

	/**
	 * @return the url
	 */
	@Column(length = 256)
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the portletUrl
	 */
	@Column(name = "PORTLET_URL", length = 256)
	public String getPortletUrl() {
		return portletUrl;
	}

	/**
	 * @param portletUrl
	 *            the portletUrl to set
	 */
	public void setPortletUrl(String portletUrl) {
		this.portletUrl = portletUrl;
	}

	/**
	 * @return the portletCss
	 */
	@Column(name = "PORTLET_CSS", length = 100)
	public String getPortletCss() {
		return portletCss;
	}

	/**
	 * @param portletCss
	 *            the portletCss to set
	 */
	public void setPortletCss(String portletCss) {
		this.portletCss = portletCss;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent")
	@OrderBy("seq")
	public List<Function> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<Function> children) {
		this.children = children;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_FUNC_ID", updatable = false)
	public Function getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Function parent) {
		this.parent = parent;
	}

	/**
	 * @return the roles
	 */
	@ManyToMany(mappedBy = "functions")
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
	 * @return the order
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setSeq(int order) {
		this.seq = order;
	}

	/**
	 * @return top
	 */
	@Column(name = "IS_TOP")
	public Boolean getTop() {
		return top;
	}

	/**
	 * @param top
	 *            top or not
	 */
	public void setTop(Boolean top) {
		this.top = top;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return menu css
	 */
	public String getMenuCss() {
		return menuCss;
	}

	/**
	 * @param menuCss
	 *            menu css
	 */
	public void setMenuCss(String menuCss) {
		this.menuCss = menuCss;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Function other = (Function) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the menu
	 */
	public boolean isMenu() {
		return menu;
	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	/**
	 * @return the targetWindow
	 */
	public String getTargetWindow() {
		return targetWindow;
	}

	/**
	 * @param targetWindow
	 *            the targetWindow to set
	 */
	public void setTargetWindow(String targetWindow) {
		this.targetWindow = targetWindow;
	}

	/**
	 * @param top
	 *            the top to set
	 */
	public void setTop(boolean top) {
		this.top = top;
	}

}
