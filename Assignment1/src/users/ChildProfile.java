/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 1 
 */
package users;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represent profile for dependants whose age is below 16
 */
public class ChildProfile extends Profile {
	
	private AdultProfile mother;
	private AdultProfile father;
	
	public ChildProfile(String name, int age, String status, 
			AdultProfile mother, AdultProfile father) {
		this(name, age, status, null, mother, father);
						
	}
	
	public ChildProfile(String name, int age, String status, String image, 
			AdultProfile mother, AdultProfile father) {
		super(name, age, status, image);
		
		mother.addChildren(this);
		father.addChildren(this);
		
		this.father = father;
		this.mother = mother;
	}
	
	/**
	 * @return father
	 */
	public AdultProfile getFather() {
		return father;
	}
	
	/**
	 * @return mother
	 */
	public AdultProfile getMother() {
		return mother;
	}
	
	/**
	 * @return true when friend was added
	 * perform add friend
	 */
	public boolean addFriend(Profile profile) {
		if (!(profile instanceof ChildProfile)) {
			return false; //child can have only child as a friend
		}
		if (getAge() <= 2 || profile.getAge() <= 2) {
			return false; //children whose age is below or equal to 2 cannot have friends
		}
		
		if (getAge() - profile.getAge() <= 3 
				&& profile.getAge() - getAge() <= 3) {
			//difference in age cannot be over 3 years
			return super.addFriend(profile);
		}
		return false;
	}
}
