/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 1 
 */
package repository;

import java.util.*;

import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 *	this class represents Data Access Layer. Here we can refer to other resources to get data such as DB or files
 *  in this case we just hardcode data
 */
public class ProfileRepository {

	static ArrayList<Profile> profiles; //here all profiles is stores
	
	private static ArrayList<Profile> getProfiles() {
		if (profiles == null) {
			//only the first time profiles list is called, it got initialized with hard coded data
			profiles = new ArrayList<Profile>();
			init();
		}
		return profiles;
	}
	
	/**
	 * data initialization method
	 */
	private static void init() {
		AdultProfile alice = new AdultProfile("Alice", 22, "RMIT student");
		AdultProfile bob = new AdultProfile("Bob", 30, "RMIT");
		bob.AddSpouse(alice);
		alice.AddSpouse(bob);
		ChildProfile mike = new ChildProfile("Mike", 12, "student at secondary school", bob, alice);
		
		AdultProfile nick = new AdultProfile("Nick", 25, "employer");
		nick.addFriend(bob);
		
		AdultProfile kate = new AdultProfile("Kate", 80, "teacher");
		AdultProfile sharoon = new AdultProfile("Sharoon", 80, "accounter");
		AdultProfile pete = new AdultProfile("Pete", 19, "officer");
		AdultProfile sandy = new AdultProfile("Sandy", 19, "student");
		AdultProfile misha = new AdultProfile("Misha", 19, "volunteir");
		pete.AddSpouse(sharoon);
		sharoon.AddSpouse(pete);
		ChildProfile sara = new ChildProfile("Sara", 10, "student at secondary school", sharoon, pete);
		ChildProfile brad = new ChildProfile("Brad", 2, "student at primary school", sharoon, pete);
		ChildProfile jack = new ChildProfile("Jack ", 4, "student at primary school", bob, alice);
		sara.addFriend(mike);
		mike.addFriend(sara);
		
		profiles.add(alice);
		profiles.add(bob);
		profiles.add(nick);
		profiles.add(mike);
		profiles.add(kate);
		profiles.add(sharoon);
		profiles.add(pete);
		profiles.add(sandy);
		profiles.add(misha);
		profiles.add(sara);
		profiles.add(brad);
		profiles.add(jack);
	}
	
	/**
	 * return all profiles from current repository
	 */
	public List<Profile> getAll() {
		return getProfiles();
	}
	
	/**
	 * add new profile
	 */
	public void add(Profile profile) {
		if (profile != null) {
			getProfiles().add(profile);
		}
	}
	
	/**
	 * Change profile
	 */
	public void update(UUID profileId, Profile profile) {
		//find old profile
		Profile prof = findByGuid(profileId);
		if (prof != null) {
			int idx = getProfiles().indexOf(prof); //get index in list of old profile
			getProfiles().set(idx, profile); //perform update
		}
	}
	
	/**
	 * delete the profile by its Id]
	 * return return true when profileId is found
	 */
	public Boolean delete(UUID profileId) {
		Profile prof = findByGuid(profileId);
		if (prof != null) {
			//when the profile by id is found, remove it from list
			getProfiles().remove(prof);
			return true;
		}
		//return false when profileId is not found
		return false;
	}
	
	/**
	 * get the profile from storage by its id
	 */
	private Profile findByGuid(UUID profileId) {
		return getProfiles().stream()
				.filter(profile -> profile.getProfileGuid() == profileId)
				.findFirst()
				.orElse(null);
	}
}
