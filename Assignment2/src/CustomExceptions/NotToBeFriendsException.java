/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package CustomExceptions;

public class NotToBeFriendsException extends Exception {
	public NotToBeFriendsException() {
		super("An adult and a child cannot be friends "
				+ "or age difference if childs is over 3 years");
	}
}
