/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package users;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represents base profile entity
 */
public abstract class Profile {
	
	String status;
	int age;
	String name;
	String image;
	String gender;
	String livingState;
	
	protected Profile(String name, int age, String status, String image,
			String gender, String livingState) {
		this.status = status;
		this.age = age;
		this.name = name;
		this.image = image;
		this.gender = gender;
		this.livingState = livingState;
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
	 * @return Gender
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * @return Living State
	 */
	public String getLivingState() {
		return livingState;
	}
}