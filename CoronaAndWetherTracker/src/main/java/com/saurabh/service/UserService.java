package com.saurabh.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurabh.POJO.User;
import com.saurabh.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public User add(User user)
	{
		User savedUser=userRepository.save(user);
		return savedUser;
		
	}
	
    public Iterable<User> search()
    {
    	Iterable<User> allUsers=userRepository.findAll();
    	return allUsers;
    }
    
    public void delete(User user)
    {
    	userRepository.delete(user);
    }
    
    public List<Object> findUnApprovedUser()
    {
    return userRepository.findUnApprovedUser();	
    }
    
    public Set<Object> retrieveAllId()
    {
    	return userRepository.retrieveAllId();
    }
    
    public int changePassword(String password, String username)
    {
    	return userRepository.updatePassword(password, username);
    }
    
    public List<User> getAllUsers() {
    	return userRepository.getAllUsers();
    }

}
