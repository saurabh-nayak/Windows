package com.saurabh.ContextEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.saurabh.POJO.User;
import com.saurabh.service.UserService;

public class MyContextEventListener implements ServletContextListener 
{
	Map<String,User> mapUser=new HashMap<String,User>();
@Autowired
UserService service;
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		// TODO Auto-generated method stub
		List<User> allUsers=service.getAllUsers();
		for(User user:allUsers)
		{
			mapUser.put(user.getUserName(),user);
		}
		contextEvent.getServletContext().setAttribute("listUsers",mapUser);
	}

}
