/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package users;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represent profile for dependants whose age is below 16
 */
public class ChildProfile extends Profile {
	
	private AdultProfile parent2;
	private AdultProfile parent1;
	
	public ChildProfile(String name, int age, String status, 
			String gender, String livingState,
			AdultProfile parent1, AdultProfile parent2) {
		this(name, age, status, null, gender, livingState, parent1, parent2);
						
	}
	
	public ChildProfile(String name, int age, String status, String image,
			String gender, String livingState,
			AdultProfile parent1, AdultProfile parent2) {
		super(name, age, status, image, gender, livingState);
		
		parent1.addChildren(this);
		parent2.addChildren(this);
		
		this.parent1 = parent1;
		this.parent2 = parent2;
	}
	
	/**
	 * @return father
	 */
	public AdultProfile getParent1() {
		return parent1;
	}
	
	/**
	 * @return mother
	 */
	public AdultProfile getParent2() {
		return parent2;
	}
}
