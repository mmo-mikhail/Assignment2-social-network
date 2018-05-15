/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

public class NotToBeClassmatesException extends Exception {
	public NotToBeClassmatesException() {
		super("Young child cannot have classmate relation");	
	}
}
