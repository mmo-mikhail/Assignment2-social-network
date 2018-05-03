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

	AdultProfile spouse;
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
	 * Add spouse to current person
	 */
	public void AddSpouse(AdultProfile profile) {
		spouse = profile;
		super.addFriend(profile);
	}
	
	/**
	 * Get spouse of current person
	 */
	public AdultProfile getSpouse() {
		return spouse;
	}
	
	/**
	 * Delete spouse of current person if exists
	 */
	public void deleteSpouse() {
		if (spouse == null) {
			return;
		}
		super.removeFriend(spouse);
		spouse = null;
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
	
	/**
	 * delete specific child
	 */
	public boolean deleteChild(ChildProfile child) {
		if (children.contains(child)) {
			return children.remove(child);
		}
		return false;
	}
}
