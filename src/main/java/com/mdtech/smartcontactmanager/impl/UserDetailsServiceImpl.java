package com.mdtech.smartcontactmanager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mdtech.smartcontactmanager.entity.UserEntity;
import com.mdtech.smartcontactmanager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity user = userRepository.getUserByemail(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Connot find user with the given email");
		}
		
		UserDetailImpl userDetailImpl = new UserDetailImpl(user);
		return userDetailImpl;
	}

}
