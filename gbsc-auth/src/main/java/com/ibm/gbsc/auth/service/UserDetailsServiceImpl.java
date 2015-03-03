package com.ibm.gbsc.auth.service;

import javax.inject.Inject;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.auth.model.User;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.auth.vo.LoginUser;

/**
 * @author Johnny
 *
 */
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Inject
	UserService userService;

	/** {@inheritDoc} */
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userService.getUser(username);
		if (user.getOrg() == null) {
			throw new BadCredentialsException("User [" + username + "] has no departments found.");
		}
		return new LoginUser(user);
	}

}
