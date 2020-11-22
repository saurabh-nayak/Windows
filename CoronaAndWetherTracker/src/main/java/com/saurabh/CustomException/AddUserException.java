package com.saurabh.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AddUserException extends RuntimeException
{
    public AddUserException(String msg) {
		// TODO Auto-generated constructor stub
    	super(msg);
	}
}
