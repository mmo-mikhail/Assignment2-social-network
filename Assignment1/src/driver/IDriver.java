/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 1 
 */
package driver;

import java.util.List;
import java.util.UUID;

import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * General behavior of the driver
 */
public interface IDriver {

	Profile createProfile(String name, int age, String status, String image);
	
	Profile createProfile(String name, int age, String status, String image, AdultProfile mother, AdultProfile father);
	
	void insertProfile(Profile newProfile);
	
	Profile findByName(String name);
	
	String displayPerson(Profile profile);
	
	void updateProfile(UUID profileId, Profile newProfile);
	
	boolean deletePerson(Profile profile);
	
	boolean setupFriendShip(Profile profile1, Profile profile2);
	
	boolean marryPeople(AdultProfile profile1, AdultProfile profile2);
	
	boolean isDirectFriends(Profile profile1, Profile profile2);
	
	String getDependantName(AdultProfile adultProfile);
	
	String getNamesOfTheParents(ChildProfile profile);
	
	List<Profile> getAllProfiles(); 
	
	//helpers
	List<AdultProfile> getUniqueSpouses();
	
}
