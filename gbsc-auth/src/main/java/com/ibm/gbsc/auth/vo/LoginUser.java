package com.ibm.gbsc.auth.vo;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.userdetails.UserDetails;

import com.ibm.gbsc.auth.model.Organization;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.model.User;
import com.ibm.gbsc.auth.model.UserState;

/**
 * 当前登录用户信息.
 *
 * @author Johnny
 */
public class LoginUser implements UserDetails {
	/**
	 *
	 */
	private static final long serialVersionUID = -1756852420675349892L;

	private final User user;

	private final Collection<Role> authorities;

	/**
	 * @param user
	 *            user
	 */
	public LoginUser(User user) {
		super();
		this.user = user;
		authorities = new HashSet<Role>();
		if (!user.getRoles().isEmpty()) {
			authorities.addAll(user.getRoles());
		}
		for (Organization org : user.getDepartments()) {
			addOrgRoles(org);
		}
	}

	/**
	 * @param org
	 *            org
	 */
	private void addOrgRoles(Organization org) {
		if (!org.getRoles().isEmpty()) {
			authorities.addAll(org.getRoles());
		}
		// 仅当相同的node code的上级机构才把角色传递到下级机构
		if (org.getParent() != null && org.getNodeCode().equals(org.getParent().getNodeCode())) {
			addOrgRoles(org.getParent());
		}
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Role> getAuthorities() {
		return authorities;
	}

	/** {@inheritDoc} */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/** {@inheritDoc} */
	@Override
	public String getUsername() {
		return user.getCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAccountNonExpired() {
		return user.getStatus() != UserState.CLOSED;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAccountNonLocked() {
		return user.getStatus() != UserState.LOCKED;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCredentialsNonExpired() {
		return user.getStatus() != UserState.LOCKED;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEnabled() {
		return user.getStatus() == UserState.NORMAL;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof LoginUser)) {
			return false;
		}
		LoginUser other = (LoginUser) obj;
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

}
