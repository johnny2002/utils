package com.ibm.gbsc.auth.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ibm.gbsc.common.vo.IRefBeanTree;
import com.ibm.gbsc.common.vo.RefBean;

/**
 * 功能.
 *
 * @author Johnny
 */
@Entity
@Table(name = "GBSC_AUTH_FUNC")
@NamedQueries({
        @NamedQuery(name = "Function.getAll", query = "select func from Function func where func.active = true"),
        @NamedQuery(name = "Function.get1stLevel", query = "select func from Function func where func.parent is null and func.active = true order by func.seq"),
        @NamedQuery(name = "Function.byRole", query = "SELECT func FROM Function func, IN(func.roles) rl WHERE rl = :role"),
        @NamedQuery(name = "Function.getTOP", query = "select func from Function func where func.top = true and func.active = true order by func.seq"),
        @NamedQuery(name = "Function.getMenu", query = "select func from Function func where func.menu = true and func.parent is null and func.active = true order by func.seq") })
@Cacheable
public class Function extends RefBean implements IRefBeanTree {
	/**
	 *
	 */
	private static final long serialVersionUID = 6307803926228004030L;
	/**
	 * 是否菜单
	 */
	private boolean menu;
	/**
	 * 是否顶部tab
	 */
	private Boolean top;
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
	@Override
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
	@ManyToMany
	@JoinTable(name = "GBSC_AUTH_FUNC_ROLE", joinColumns = { @JoinColumn(name = "FUNC_CODE") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_CODE") })
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
