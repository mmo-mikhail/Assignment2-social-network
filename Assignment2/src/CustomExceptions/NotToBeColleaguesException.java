/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

/**
 * @author s3419069 (Mykhailo Muzyka)
 */
public class NotToBeColleaguesException extends Exception {
	public NotToBeColleaguesException() {
		super("A child cannot have colleague relation");	
	}
}
