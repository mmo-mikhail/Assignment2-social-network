/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 1 
 */
package users;
import java.util.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represents base profile entity
 */
public abstract class Profile {
	
	List<Profile> friends = new ArrayList<Profile>();
	String status;
	int age;
	String name;
	String image;
	
	UUID profileGuid; //unique id of each profile
	
	protected Profile(String name, int age, String status, String image) {
		this.status = status;
		this.age = age;
		this.name = name;
		this.image = image;
		
		profileGuid = UUID.randomUUID(); //generate unique id for current object
	}
	
	public boolean addFriend(Profile profile) {
		friends.add(profile);
		return true;
	}
	
	/**
	 * Gets all friends
	 */
	public List<Profile> getAllFriends(){
		return friends;
	}
	
	/**
	 * Remove friend
	 */
	public Boolean removeFriend(Profile profile) {
		return friends.remove(profile);
	}

	/**
	 * @return Age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return Status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return Profile Id
	 */
	public UUID getProfileGuid() {
		return profileGuid;
	}
}