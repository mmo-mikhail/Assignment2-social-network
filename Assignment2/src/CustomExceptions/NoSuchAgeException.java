/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

/**
 * @author s3419069 (Mykhailo Muzyka)
 */
public class NoSuchAgeException extends Exception {
	public NoSuchAgeException() {
		super("Age can be from 1 to 150 years only");	
	}
}
