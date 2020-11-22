package com.saurabh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurabh.POJO.UserDetail;
import com.saurabh.repository.UserDetailRepository;

@Service	
public class UserDetailService {
	@Autowired
	UserDetailRepository userDetailRepository;
	
	public UserDetail add(UserDetail userDetail)
	{
		UserDetail savedUserDetail= userDetailRepository.save(userDetail);
		return savedUserDetail;
	}

	
    public List<UserDetail> search()
    {
    	List<UserDetail> allUserDetails=userDetailRepository.findAllElements();
    	return allUserDetails;
    }
    
    public void delete(UserDetail userDetail)
    {
    	userDetailRepository.delete(userDetail);
    }
    
    public List<UserDetail> searchByName(String name)
    {
    	return (userDetailRepository.readuserDetailByName(name));
    }
    
    public Optional<UserDetail> searchById(int id)
    {
    	return (userDetailRepository.findById(id));
    }
    
    public void setLastLogin(String lastLogin,int id)
    {
    	userDetailRepository.lastLogin(lastLogin,id);
    }
    
    public void setLastModified(String lastModified,int id)
    {
    	userDetailRepository.lastModified(lastModified, id);
    }

}
