/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package driver;

import java.util.*;

import repository.ProfileRepository;
import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represents the main business logic of the project
 */
public class Driver implements IDriver {

	/**
	 * storage of profiles
	 */
	private ProfileRepository profileRepository = new ProfileRepository();
	
	/**
	 * just create profile of Adult without adding it to storage
	 */
	public Profile createProfile(String name, int age, String status, String image,
			String gender, String livingState) {
		AdultProfile newProfile =
				new AdultProfile(name, age, status, image, gender, livingState);
		return newProfile;
	}
	
	/**
	 * just create profile of Child without adding it to storage
	 */
	public Profile createProfile(String name, int age, String status, String image,
			String gender, String livingState,
			AdultProfile mother, AdultProfile father) {
		if (mother.getSpouse() != father) {
			return null; // couple must be married to have a child
		}
		Profile newProfile =
				new ChildProfile(name, age, status, image, gender, livingState, mother, father);
		return newProfile;
	}
	
	/**
	 * insert specified profile to the storage
	 */
	public void insertProfile(Profile newProfile) {
		profileRepository.add(newProfile);
	}

	/**
	 * get single profile by profile's name match or null when no profile is found
	 */
	public Profile findByName(String name) {
		try {
			return profileRepository.getAll().stream()
					.filter(person -> person.getName().equals(name))
					.findFirst().get();
		} catch(NoSuchElementException ex) {
			//.get() method raise this exception when no element found
			return null;
		}
	}

	/**
	 * get string of specific person's data
	 */
	public String displayPerson(Profile profile) {
		//write base data first
		String info = "Name:" + profile.getName() + "; "
				+ "Age:" + profile.getAge() + "; "
				+ "Status:" + profile.getStatus() + "; "
				+ "Image:" + profile.getImage() + "; ";
		
		if (profile instanceof AdultProfile) {
			//add data specific for adults
			AdultProfile adult = (AdultProfile)profile;
			if (adult.getSpouse() == null) {
				info += "Not Married; ";
			} else {
				info += "Spouse: " + adult.getSpouse().getName() + "; ";
				info += "Children: " + getDependantName(adult) + "; ";
			}
		} else {
			//add data specific for children
			info += "Parents: " + getNamesOfTheParents((ChildProfile)profile) + "; ";
		}
		//show friends at the end
		info += "Friends: ";
		for (Profile friend : profile.getAllFriends()) {
			info += friend.getName();
		}
		return info;
	}

	/**
	 * update profile
	 */
	public void updateProfile(UUID profileId, Profile newProfile) {
		profileRepository.update(profileId, newProfile);
	}

	/**
	 * delete profile from storage
	 * delete children if it adult and spouse if needed
	 */
	public boolean deletePerson(Profile profile) {
		if (profile instanceof ChildProfile) {
			return deleteChild((ChildProfile)profile);
		}
		//otherwise it's Adult Profile
		//so we delete all children first
		ArrayList<ChildProfile> children = ((AdultProfile)profile).getChildren();
		boolean deleteChildrenSuccess = true;
		int size = children.size();
		for (int i = 0; i <size ; i++) {
			if (!deleteChild(children.get(0))) {
				deleteChildrenSuccess = false;
			}
		}
		//delete spouse
		((AdultProfile)profile).deleteSpouse();
		
		// and then delete main profile
		//return true only when all profiles successfully deleted
		return profileRepository.delete(profile.getProfileGuid())
				&& deleteChildrenSuccess;
	}
	
	/**
	 * help method to delete child
	 * returns true only when child successfully completely deleted
	 */
	private boolean deleteChild(ChildProfile child) {
		return profileRepository.delete(child.getProfileGuid())
				&& child.getMother().deleteChild(child)
				&& child.getFather().deleteChild(child);		
	}

	/**
	 * help method to delete child
	 * returns true only when friendship was created completely
	 */
	public boolean setupFriendShip(Profile profile1, Profile profile2) {
		if (profile1.getAge() <= 16 && profile2.getAge() >= 16
				|| profile2.getAge() <= 16 && profile1.getAge() >= 16) {
			return false; // there cannot be friendship between child and adult
		}
		return profile1.addFriend(profile2)
				&& profile2.addFriend(profile1);
	}
	
	/**
	 * set marriage relationship for adults
	 * return true when relationship created
	 */
	public boolean marryPeople(AdultProfile profile1, AdultProfile profile2) {
		if (profile1.getSpouse() != null || profile2.getSpouse() != null
				|| profile1 == profile2) {
			return false; // each adult must not have spouse and they must be different people
		}
		profile1.AddSpouse(profile2);
		profile2.AddSpouse(profile1);
		return true;
	}
	

	/**
	 * indicates whether profiles has friendship relationship
	 */
	public boolean isDirectFriends(Profile profile1, Profile profile2) {
		return profile1.getAllFriends().contains(profile2);
	}

	/**
	 * get names of all children of specific adult
	 */
	public String getDependantName(AdultProfile adultProfile) {
		String totalNames = "";
		ArrayList<ChildProfile> children = adultProfile.getChildren();
		for (int i = 0; i < children.size(); i++) {
			//go though each child
			//do not type | symbol at the end
			totalNames += children.get(i).getName()
					+ ((i + 1 == children.size()) ? "" : "| "); 
		}
		return totalNames;
	}

	/**
	 * get names of both parents of specific child
	 */
	public String getNamesOfTheParents(ChildProfile profile) {
		return "Father: " + profile.getFather().getName()
				+ " | Mother: " +  profile.getMother().getName();
	}
	
	/**
	 * helper
	 * get only one person from each couple
	 */
	public List<AdultProfile> getUniqueSpouses() {
		ArrayList<AdultProfile> personsFromCouple = new ArrayList<AdultProfile>();
		for (Profile profile : profileRepository.getAll()) {
			if (!(profile instanceof AdultProfile)) continue; // skip children
			AdultProfile adult = (AdultProfile)profile;
			AdultProfile spouse = adult.getSpouse();
			if (spouse == null) continue; // skip non married people
			
			if (!personsFromCouple.contains(spouse)
					&& !personsFromCouple.contains(adult)) {
				personsFromCouple.add(adult);
			}
		}
		return personsFromCouple;
	}
	
	/**
	 * just get all profiles from the storage
	 */
	public List<Profile> getAllProfiles(){
		return profileRepository.getAll();
	}
}
