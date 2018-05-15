/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package driver;

import java.sql.SQLException;
import java.util.*;

import CustomExceptions.*;
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
	 * insert specified profile to the storage
	 * @throws SQLException 
	 */
	public void insertProfile(String name, int age, String status, String image,
			String gender, String state) throws SQLException {
		profileRepository.add(name, image, status, gender, age, state);
	}

//	/**
//	 * get string of specific person's data
//	 */
//	public String displayPerson(Profile profile) {
//		//write base data first
//		String info = "Name:" + profile.getName() + "; "
//				+ "Age:" + profile.getAge() + "; "
//				+ "Status:" + profile.getStatus() + "; "
//				+ "Image:" + profile.getImage() + "; ";
//		
//		if (profile instanceof AdultProfile) {
//			//add data specific for adults
//			AdultProfile adult = (AdultProfile)profile;
//			if (adult.getSpouse() == null) {
//				info += "Not Married; ";
//			} else {
//				info += "Spouse: " + adult.getSpouse().getName() + "; ";
//				info += "Children: " + getDependantName(adult) + "; ";
//			}
//		} else {
//			//add data specific for children
//			info += "Parents: " + getNamesOfTheParents((ChildProfile)profile) + "; ";
//		}
//		//show friends at the end
//		info += "Friends: ";
//		for (Profile friend : profile.getAllFriends()) {
//			info += friend.getName();
//		}
//		return info;
//	}

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
//		((AdultProfile)profile).deleteSpouse();
		
		// and then delete main profile
		//return true only when all profiles successfully deleted
		return profileRepository.delete(profile.getName())
				&& deleteChildrenSuccess;
	}
	
	/**
	 * help method to delete child
	 * returns true only when child successfully completely deleted
	 */
	private boolean deleteChild(ChildProfile child) {
		return profileRepository.delete(child.getName());
//				&& child.getParent1().deleteChild(child)
//				&& child.getParent2().deleteChild(child);		
	}

	/**
	 * indicates whether profiles has friendship relationship
	 */
	public boolean isDirectlyConnected(Profile profile1, Profile profile2) {
		return profileRepository.isDirectlyConnected(
				profile1.getName(), profile2.getName());
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
		return "Parent 1: " + profile.getParent1().getName()
				+ " | Parent 2: " +  profile.getParent2().getName();
	}
	
	/**
	 * just get all profiles from the storage
	 */
	public List<Profile> getAllProfiles(){
		return profileRepository.getAll();
	}


	public void setRelation(Profile profile1, Profile profile2,
			String relation)
			throws NotToBeColleaguesException, NotToBeClassmatesException,
			NotToBeFriendsException, TooYoungException,
			NotToBeCoupledException, NoAvailableException,
			SQLException {
		if (validateRelation(profile1, profile2, relation)) {
			profileRepository.setRelation(
					profile1.getName(),
					profile2.getName(),
					relation);
		}
	}
	
	public void setParentalRelation(String childName,
			String parent1, String parent2)
			throws SQLException {
			profileRepository.setRelation(childName, parent1, "parent");
			profileRepository.setRelation(childName, parent2, "parent");
	}

	private boolean validateRelation(Profile profile1, Profile profile2,
			String relation)
			throws NotToBeColleaguesException, NotToBeClassmatesException,
					NotToBeFriendsException, TooYoungException,
					NotToBeCoupledException, NoAvailableException,
					SQLException {
		int age1 = profile1.getAge();
		int age2 = profile2.getAge();
		switch(relation) {
			case "colleagues":
				if (age1 > 16 && age2 > 16) return true;
				throw new NotToBeColleaguesException();
			case "classmates":
				if (age1 <= 2 || age2 <= 2) return true;
				throw new NotToBeClassmatesException ();
			case "friends":
				if (age1 <= 16 && age2 > 16 || age2 <= 16 && age1 > 16) {
					throw new NotToBeFriendsException();
				}
				if (age1 <= 2 || age2 <= 2) {
					throw new TooYoungException();
				}
				if (age1 <= 16 && age2 <= 16
					&& (age1 - age2 > 3 || age2 - age1 > 3)) {
					throw new NotToBeFriendsException();
				}
				return true;
			case "couple":
				if (age1 <= 16 || age2 <= 16) {
					throw new NotToBeCoupledException();
				}
				if (!profileRepository.coupleAllowed(
						profile1.getName(),
						profile2.getName())) {
					throw new NoAvailableException();
				}
				return true;
			case "parent":
				// we don't need to check here because in this case
				// only valid data can 
				return true;
		}
		return false;
	}
	
	/**
	 * helper
	 * get only one person from each couple
	 */
	public String[] getSpouses(String separator) {
		return profileRepository.getSpouses(separator);
	}
}
