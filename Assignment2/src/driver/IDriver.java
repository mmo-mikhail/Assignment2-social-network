/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package driver;

import java.sql.SQLException;
import java.util.List;

import CustomExceptions.*;
import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * General behavior of the driver
 */
public interface IDriver {
	
	void insertProfile(String name, int age, String status, String image,
			String gender, String livingState) throws SQLException;
	
	boolean deletePerson(Profile profile);
	
	boolean isDirectlyConnected(Profile profile1, Profile profile2);
	
	String getDependantName(AdultProfile adultProfile);
	
	String getNamesOfTheParents(ChildProfile profile);
	
	List<Profile> getAllProfiles(); 

	void setRelation(Profile selectedProfile, Profile selectedProfile2,
			String relation)
			throws NotToBeColleaguesException, NotToBeClassmatesException,
			NotToBeFriendsException, TooYoungException,
			NotToBeCoupledException, NoAvailableException, SQLException;
	
	void setParentalRelation(String childName,
			String parent1, String parent2) throws SQLException;
	
	//helpers
	String[] getSpouses(String separator);
}
