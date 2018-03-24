/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 1 
 */
package userInreface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import driver.*;
import users.*;


/**
 * @author s3419069 (Mykhailo Muzyka)
 *	Main UI class
 */
public class MiniNet {

	/**
	 * represent object to read lines from Console
	 */
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	/**
	 * main class with logic
	 */
	static IDriver driver = new Driver();
	
	/**
	 * startup method
	 */
	public static void main(String[] args) {
		Profile person = null; // selected person
		while (true) {
			printLn("Press press Enter to show menu");
			readLine();
			printLn(miniNetMenuText); //display main menu
			printLn((person != null)
					? "Selected Person - " + person.getName()
					: "No Person selected");
			String inpt = readLine();
			switch(inpt) {
				case "1": // Add new person
					addOrUpdatePerson(null);
					break;
				case "2": // Select person
					person = selectPerson();
					if (person == null) {
						continue;
					}
					break;
				case "3": // Update the selected person
					updatePerson(person);
					break;
				case "4": // Delete the selected person
					boolean success = deleteSelectedPerson(person);
					if (success) {
						person = null; // unselect person
					}
					break;
				case "5": // Marry adults
					Profile[] adults = selectTwoPeople(true);
					if (adults == null) {
						continue; //then cancel was requested
					}
					driver.marryPeople((AdultProfile)adults[0], (AdultProfile)adults[1]);
					break;
				case "6":  // Create friendship
					Profile[] people = selectTwoPeople(false);
					if (people == null) {
						continue;
					}
					printLn(driver.setupFriendShip(people[0], people[1])
							? "Friendship was created!"
							: "Error on creating friendship relationship");
					break;
				case "7": // Display names of parents/children
					displayNames(person);
					break;
				case "8": // List everyone
					for (Profile profile : driver.getAllProfiles()) {
						printLn(driver.displayPerson(profile));
					}
					break;
				case "9":  // Is direct friends
					Profile[] prsons = selectTwoPeople(false);
					if (prsons == null) {
						continue;
					}
					printLn("Is direct friends: "
							+ driver.isDirectFriends(prsons[0], prsons[1]));
					break;
				case "?": // to Exit
					return;
				default:
					printLn("Please enter option number from MiniNet Menu");
					break;
			}
		}
	}
	
	/**
	 * this method updates profile of a person
	 */
	private static void updatePerson(Profile person) {
		if (person == null) {
			printLn("Please select person to continue");
			return;
		}
		printLn("Current person info:");
		printLn(driver.displayPerson(person));
		printLn("Now enter new data:");
		addOrUpdatePerson(person);
	}

	/**
	 * Display name of parents when children selected and names of children for adults
	 */
	private static void displayNames(Profile person) {
		if (person == null) {
			printLn("Please select person to continue");
			return;
		}
		printLn("Current person info:");
		printLn(driver.displayPerson(person));
		if (person instanceof AdultProfile) {
			printLn("Children: "
					+ driver.getDependantName((AdultProfile)person));
		} else {
			printLn("Parents: "
					+ driver.getNamesOfTheParents((ChildProfile)person));
		}
	}

	/**
	 * @param onlyDivorsedAdults - to display only adults without spouse
	 * @return two people from displayed list 
	 * Select two people
	 */
	private static Profile[] selectTwoPeople(boolean onlyDivorsedAdults) {
		
		List<Profile> validProfiles = driver.getAllProfiles();
		if (onlyDivorsedAdults) {
			//filter profiles to include just adults without spouse
			validProfiles = validProfiles
					.stream()
					.filter(profile -> profile instanceof AdultProfile
							&& ((AdultProfile)profile).getSpouse() == null)
					.collect(Collectors.toList());
		}
		
		for (int i = 0; i < validProfiles.size(); i++) {
			printLn(i + ": " + driver.displayPerson(validProfiles.get(i)));
		}
		int idx = askForIndex("first person's", validProfiles.size());
		if (idx == -1) {
			printLn("Cancel was requested");
			return null;
		}
		int idx2 = askForIndex("second person's", validProfiles.size());
		if (idx2 == -1) {
			printLn("Cancel was requested");
			return null;
		}
		if (idx == idx2) {
			printLn("Error! Same people was selected");
			return null;
		}
		
		return new Profile[] {validProfiles.get(idx), validProfiles.get(idx2) };
	}

	/**
	 * @param person
	 * @return true if success and false when something goes wrong
	 * perform delete of selected person
	 */
	static private boolean deleteSelectedPerson(Profile person) {
		if (person == null) {
			printLn("Please select person to continue");
			return false;
		}
		printLn("Current person info:");
		printLn(driver.displayPerson(person));
		printLn("Press Enter to Delete or any other symbol to cancel");
		String input = readLine();
		if (input.equals("")) {
			Boolean result = driver.deletePerson(person);
			printLn(result ? "Person Deleted succussfuly" : "Error on deleting person");
			return result;
		}
		return false;
	}
	
	/**
	 * @return selected profile of person by name
	 */
	static private Profile selectPerson() {
		printLn("Please enter name: ('E' - to cancel)");
		String name = readLine();
		if (name.equals("E")) {
			return null; //then cancel was requested
		}
		Profile person = driver.findByName(name); //here driver search for the name

		if (person == null) {
			//for cases when no person was found
			printLn("Person cannot be found. Please press Enter to continue");
			readLine();
			return null;
		}
		printLn(driver.displayPerson(person)); //display person's info
		printLn("This person is selected");
		return person;
	}
	
	/**
	 * @param person - null for new profiles
	 * perform add or udpate profile
	 */
	static private void addOrUpdatePerson(Profile person) {
		//input base data first
		printLn("Please enter name: ('E' - to cancel)");
		String name = readLine();
		if (name.equals("E")) {
			return;
		}
		int age = askForAge();
		printLn("Please enter status:");
		String status = readLine();
		
		printLn("Please enter image (or just press Enter to skip):");
		String image = readLine();
		if (image.equals("")) {
			image = null;
		}
		
		Profile newProfile = null;
		if (age <= 16) {
			//if it is child ask to set parents or cancel
			printLn("You are trying to add a child. Please select parents or cancel by entering 'E'");
			
			//then display all available couples. Get one person from each couple only
			List<AdultProfile> peopleFromCouple = driver.getUniqueSpouses();
			for (int i = 0; i < peopleFromCouple.size(); i++) {
				printLn(i + ": " + peopleFromCouple.get(i).getName()
						+ " AND "
						+ peopleFromCouple.get(i).getSpouse().getName());
			}
			int selectedCoupleIdx = askForIndex("parents/couple", peopleFromCouple.size());
			if (selectedCoupleIdx == -1) {
				return; //then cancel was requested
			}
			
			//when couple was selected, get one profile object
			AdultProfile parent = peopleFromCouple.get(selectedCoupleIdx);
			
			//create profile, but do not input it yet
			newProfile = driver.createProfile(
					name, age, status, image, parent, parent.getSpouse());
		}
		if (newProfile == null) {
			//when adult must be created
			newProfile = driver.createProfile(name, age, status, image);
		}
		if (person == null) {
			//perform Add
			driver.insertProfile(newProfile);
		} else {
			//perform Update
			driver.updateProfile(person.getProfileGuid(), newProfile);
		}
	}
	
	/**
	 * @return age of a person
	 * Ask for age until valid data input
	 */
	static private int askForAge() {		
		while (true) {
			printLn("Please enter age:");
			String ageRaw = readLine();
			
			try {
				int age = Integer.parseInt(ageRaw);
				if (age > 0 && age < 150) {
					return age; //here valid age is returned
				} else {
					printLn("Please enter valid human's age.");
				}
			} catch (NumberFormatException ex) {
				//not number was in input
				printLn("Age is not valid number.");
			}
		}
	}
	
	/**
	 * @param whatToAsk - id of what is asked
	 * @param maxNumber - top range number
	 * @return index
	 * ask for index number in specific range
	 */
	static private int askForIndex(String whatToAsk, int maxNumber) {		
		while (true) {
			printLn("Please enter " + whatToAsk + " id (to return - 'E'):");
			String idRaw = readLine();
			if (idRaw.equals("E")) {
				return -1; //when cancel was requested
			}
			try {
				int id = Integer.parseInt(idRaw);
				if (id > 0 && id < maxNumber) {
					return id; //here valid index was returned
				} else {
					printLn("Please enter valid range");
				}
			} catch (NumberFormatException ex) {
				//not number was in input
				printLn("Id is not valid number.");
			}
		}
	}
	

	
	/**
	 * @return input string
	 */
	static private String readLine() {
		try {
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param input - what to display
	 * just print line
	 */
	static private void printLn(String input) {
		System.out.println(input);
	}
	
	/**
	 * menu string
	 */
	static private final String miniNetMenuText =
			"===================================" + System.lineSeparator()
			+ "MiniNet Menu" + System.lineSeparator()
			+ "===================================" + System.lineSeparator()
			+ "1. Add new person to network" + System.lineSeparator()
			+ "2. Select a person (and display profile)" + System.lineSeparator()
			+ "3. Update profile" + System.lineSeparator()
			+ "4. Delete profile" + System.lineSeparator()
			+ "5. Marry adults" + System.lineSeparator()
			+ "6. Set friendship" + System.lineSeparator()
			+ "7.Display Names of person's children OR of parents if child selected" + System.lineSeparator()
			+ "8. List everyone" + System.lineSeparator()
			+ "9. Are these two direct friends?" + System.lineSeparator()
			
			+ "?. Exit" + System.lineSeparator() + System.lineSeparator()
			+ "Enter an option: ";
}
