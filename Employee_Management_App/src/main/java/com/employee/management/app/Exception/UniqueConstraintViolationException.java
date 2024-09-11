package com.employee.management.app.Exception;

public class UniqueConstraintViolationException extends RuntimeException {
	
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
