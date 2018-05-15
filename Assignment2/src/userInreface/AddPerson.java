/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.util.Arrays;

import driver.Driver;
import driver.IDriver;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddPerson extends SecondaryStage {
	
	/**
	 * main driver class with business logic
	 */
	static IDriver driver = new Driver();

	@Override
	public void show(GridPane gridPaneContainer) {
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

	@Override
	protected String getCloseButtonText() {
		return "Cancel";
	}

	@Override
	protected int[] closeIdxs() {
		return new int[] {1,7};
	}

	@Override
	protected double getHeight() {
		return 450;
	}

	@Override
	protected double getWidth() {
		return 450;
	}
	
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
				try {
					driver.insertProfile(name, age, status, image, gender, livingState);
				} catch (Exception e) {
					e.printStackTrace(System.out);
					displayError("Unexpected error occurred. Person was not added");
					return false;
				}
			} else {
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

	private boolean isValidState(String livingState) {
		String[] states = new String[]
				{"ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA"};
		return Arrays.asList(states).contains(livingState);
	}

	private boolean isValidAge(String ageRaw) {
		try {
			int age = Integer.parseInt(ageRaw);
			return age > 0 && age < 150;
		} catch (NumberFormatException ex) {
			//not number was in input
			return false;
		}
	}

	private boolean isInvalid(boolean condition, String errorText) {
		if (condition) {
			displayError(errorText);
		}
		return condition;
	}
	
	public void displayError(String errorText) {
		
	}
}
