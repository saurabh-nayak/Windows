package com.saurabh.CustomException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler 
{

	@ExceptionHandler(AddUserException.class)
public final ResponseEntity<Object> handleAddUserException(AddUserException ex, WebRequest request) throws Exception
{
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity(exceptionResponse,HttpStatus.NOT_FOUND);
}
	
	@ExceptionHandler(RuntimeException.class)
public final ResponseEntity<?> handleRuntimeException(AddUserException ex, WebRequest request) throws Exception
{
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
}
	
	@ExceptionHandler(InternalServerError.class)
public final ResponseEntity<?> handleInternalServerException(AddUserException ex, WebRequest request) throws Exception
{
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
}
	
	@ExceptionHandler(ValidationFailedException.class)
public final ResponseEntity<?> handleValidationFailedException(AddUserException ex, WebRequest request) throws Exception
{
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
}

}
