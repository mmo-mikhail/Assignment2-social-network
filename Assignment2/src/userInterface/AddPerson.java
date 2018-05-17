/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInterface;

import java.util.Arrays;

import driver.Driver;
import driver.IDriver;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * this class represents Add Person stage
 */
public class AddPerson extends SecondaryStage {
	
	/**
	 * main driver class with business logic
	 */
	static IDriver driver = new Driver();

	/**
	 * fill main gridpane for the stage
	 */
	@Override
	public void show(GridPane gridPaneContainer) {
		//create text fields
		TextField tfName = new TextField();
		TextField tfImage = new TextField();
		TextField tfStatus = new TextField();
		TextField tfGender = new TextField();
		TextField tfAge = new TextField();
		TextField tfLivingState = new TextField();
		
		Button btnOk = new Button("Ok");
		btnOk.setOnAction(e -> {
			if (addNewPerson(tfName.getText(),
					tfImage.getText(),
					tfStatus.getText(),
					tfGender.getText(),
					tfAge.getText(),
					tfLivingState.getText().toUpperCase(),
					rootStage)) {
				//if person was added successfully
				closeStage();
			}
		});
		
		//add all buttons
		gridPaneContainer.add(new Label("Name:"), 0, 0);
		gridPaneContainer.add(tfName, 1, 0);
		gridPaneContainer.add(new Label("Image:"), 0, 1);
		gridPaneContainer.add(tfImage, 1, 1);
		gridPaneContainer.add(new Label("(Please note that images must be stored in 'images' folder)"), 2, 1);
		gridPaneContainer.add(new Label("Status:"), 0, 2);
		gridPaneContainer.add(tfStatus, 1, 2);
		gridPaneContainer.add(new Label("Gender:"), 0, 3);
		gridPaneContainer.add(tfGender, 1, 3);
		gridPaneContainer.add(new Label("Age:"), 0, 4);
		gridPaneContainer.add(tfAge, 1, 4);
		gridPaneContainer.add(new Label("Living State:"), 0, 5);
		gridPaneContainer.add(tfLivingState, 1, 5);
		
		gridPaneContainer.add(btnOk, 0, 7);		
	}
	
	/**
	 * @return Stage title
	 */
	@Override
	protected String stageTitle() {
		return "Add Person";
	}
	
	/**
	 * @return close button text
	 */
	@Override
	protected String getCloseButtonText() {
		return "Cancel";
	}

	/**
	 * close button indexes
	 */
	@Override
	protected int[] closeIdxs() {
		return new int[] {1,7};
	}

	/**
	 * @return height of the stage
	 */
	@Override
	protected double getHeight() {
		return 300;
	}

	/**
	 * @return width of the stage
	 */
	@Override
	protected double getWidth() {
		return 600;
	}
	
	/**
	 * try add new person and return false if one was not added
	 */
	private boolean addNewPerson(String name,
			String image,
			String status,
			String gender,
			String ageRaw,
			String livingState,
			Stage rootStage) {
		try {
			//validate
			if (isInputInvalid(name, image, status, gender, ageRaw, livingState))
				return false;
			//cool, go ahead
			int age = Integer.parseInt(ageRaw);
			
			if (age > 16) {
				//add adult
				try {
					driver.insertProfile(name, age, status, image, gender, livingState);
				} catch (Exception e) {
					e.printStackTrace(System.out);
					displayError("Unexpected error occurred. Person was not added");
					return false;
				}
			} else {
				//add child, but select parents first
				String separator = ", ";
				SelectParents sp = new SelectParents();
				sp.setCouples(driver.getSpouses(separator));
				sp.show(rootStage);
				String parents = sp.selectedParents();
				if (parents != null) {
					String[] parentsArray = parents.split(separator);
					if (parentsArray.length != 2) {
						displayError("Error on selecting parents");
					} else {
						//now we can add child and set parents
						driver.insertProfile(name, age, status, image, gender, livingState);
						driver.setParentalRelation(name, parentsArray[0], parentsArray[1]);
					}
				} else {
					displayError("Please select parents!");
				}
			}
		} catch(Exception e) {
			e.printStackTrace(System.out);
			displayError("Error on adding person");
		}
		return true;
	}

	/**
	 * return true if input data is valid
	 */
	private boolean isInputInvalid(String name,
			String image,
			String status,
			String gender,
			String age,
			String livingState) {
		//show error if at least one of following condition is true
		return isInvalid(name.equals(""), "Name cannot be empty") ||
			isInvalid(name.length() > 50, "Name is too long") ||
			isInvalid(status.equals(""), "Status cannot be empty") ||
			isInvalid(status.length() > 50, "Status is too long") ||
			isInvalid(gender.equals("") ||
					!gender.equals("M") && !gender.equals("F"),
					"Gender can only be 'F' or 'M'") ||
			isInvalid(!isValidAge(age), "Please enter valid age") ||
			isInvalid(!isValidState(livingState),
					"Please enter valid state:"
					+ "\"ACT\", \"NSW\", \"NT\", \"QLD\", "
					+ "\"SA\", \"TAS\", \"VIC\", \"WA\"");
	}

	/**
	 * check if given state is valid
	 */
	private boolean isValidState(String livingState) {
		String[] states = new String[]
				{"ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA"};
		return Arrays.asList(states).contains(livingState);
	}

	/**
	 * check if age is in permitted range
	 */
	private boolean isValidAge(String ageRaw) {
		try {
			int age = Integer.parseInt(ageRaw);
			return age > 0 && age < 150;
		} catch (NumberFormatException ex) {
			//not number was in input
			return false;
		}
	}

	/**
	 * just display error if condition is true
	 * @return condition
	 */
	private boolean isInvalid(boolean condition, String errorText) {
		if (condition) {
			displayError(errorText);
		}
		return condition;
	}
	
	/**
	 * display error method. should be overriden
	 */
	public void displayError(String errorText) { }
}
