/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

/**
 * @author s3419069 (Mykhailo Muzyka)
 */
public class NoAvailableException extends Exception {
	public NoAvailableException() {
		super("One of adult is already connected with"
				+ " another adult as a couple");
	}
}
