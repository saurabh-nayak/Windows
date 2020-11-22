package com.saurabh.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.saurabh.POJO.MyUserDetails;
import com.saurabh.POJO.User;
import com.saurabh.repository.UserRepository;

@Component
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
	UserRepository repository;
	@Override
	public MyUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> user=repository.findByUserName(userName);
	    user.orElseThrow(() -> new UsernameNotFoundException("not found: "+userName));
		return user.map(MyUserDetails :: new).get();
	}
}
