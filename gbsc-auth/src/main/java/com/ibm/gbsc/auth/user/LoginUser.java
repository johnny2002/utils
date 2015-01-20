package com.ibm.gbsc.auth.user;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.userdetails.UserDetails;

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

	private User user;

	private Collection<Role> authorities;

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
	 * @param org org
	 */
	private void addOrgRoles(Organization org) {
		if (!org.getRoles().isEmpty()) {
			authorities.addAll(org.getRoles());
		}
		if (org.getParent() != null) {
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
	public Collection<Role> getAuthorities() {
		return authorities;
	}

	/** {@inheritDoc} */
	public String getPassword() {
		return user.getPassword();
	}

	/** {@inheritDoc} */
	public String getUsername() {
		return user.getCode();
	}

	/** {@inheritDoc} */
	public boolean isAccountNonExpired() {
		return user.getStatus() != UserState.CANCELED;
	}

	/** {@inheritDoc} */
	public boolean isAccountNonLocked() {
		return user.getStatus() != UserState.LOCKED;
	}

	/** {@inheritDoc} */
	public boolean isCredentialsNonExpired() {
		return user.getStatus() != UserState.CANCELED;
	}

	/** {@inheritDoc} */
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
