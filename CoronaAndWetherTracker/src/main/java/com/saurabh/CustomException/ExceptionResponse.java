package com.saurabh.CustomException;

import org.springframework.http.HttpStatus;

public class ExceptionResponse 
{
        String message;
        HttpStatus status;
		public ExceptionResponse(String message, HttpStatus status) {
			super();
			this.message = message;
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public HttpStatus getStatus() {
			return status;
		}
		public void setStatus(HttpStatus status) {
			this.status = status;
		}
        
}
