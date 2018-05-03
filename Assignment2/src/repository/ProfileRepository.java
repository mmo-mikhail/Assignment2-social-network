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
