/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

/**
 * @author s3419069 (Mykhailo Muzyka)
 */
public class NoParentException extends Exception {
	
	public NoParentException() {
		super("Invalid parent relationship");	
	}
	
	public NoParentException(String message) { 
		super(message);		
	}
}
