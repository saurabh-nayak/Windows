package com.saurabh.CustomException;

public class ValidationFailedException extends RuntimeException{
	
	ValidationFailedException(String msg)
	{
		super(msg);
	}
}
