package com.employee.management.app.Exception;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.employee.management.app.Payload.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	
    //Global Exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleAllException(
			ResourceNotFoundException exception,
			WebRequest webRequest	
	  ){
		ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage(),
				webRequest.getDescription(false));
		
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);			
	}
	
	
	//specific exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
			Exception exception,
			WebRequest webRequest	
	  ){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
				webRequest.getDescription(false));
		
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);			
	}
}
