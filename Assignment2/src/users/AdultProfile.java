/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package users;

import java.util.ArrayList;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represent adults whose age over 16
 */
public class AdultProfile extends Profile {

	/**
	 * children list of the current adult
	 */
	ArrayList<ChildProfile> children = new ArrayList<ChildProfile>();
	
	public AdultProfile(String name, int age, String status,
			String gender, String livingState) {
		super(name, age, status, null, gender, livingState);
			
	}
	
	public AdultProfile(String name, int age, String status, String image,
			String gender, String livingState) {
		super(name, age, status, image, gender, livingState);
			
	}
	
	/**
	 * Get all children
	 */
	public ArrayList<ChildProfile> getChildren() {	
		return children;
	}
	
	/**
	 * Add specific child
	 */
	public void addChildren(ChildProfile child) {
		if (!children.contains(child)) {
			children.add(child);
		}
	}
}
