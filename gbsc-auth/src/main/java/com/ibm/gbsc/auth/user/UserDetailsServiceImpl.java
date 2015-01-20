package com.ibm.gbsc.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Johnny
 *
 */
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
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
