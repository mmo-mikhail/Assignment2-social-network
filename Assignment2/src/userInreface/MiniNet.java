/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.util.Arrays;
import driver.Driver;
import driver.IDriver;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * @author s3419069 (Mykhailo Muzyka)
 *	Main UI class
 */
public class MiniNet extends Application {
	
	/**
	 * main class with logic
	 */
	static IDriver driver = new Driver();
	
	/**
	 * startup method
	 */
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		GridPane mainPain = initPane(); // Create a pane1
		
		setupMainPain(mainPain, primaryStage);
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(mainPain, 600, 400);
		primaryStage.setTitle("Social Network"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}
	
	private GridPane initPane() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setPadding(new Insets(15, 15, 15, 15));
		pane.setHgap(5.5);
		pane.setVgap(5.5);
		return pane;
	}
	
	private void setupMainPain(GridPane pane, Stage rootStage) {
		Button btnAddNewPerson = new Button("Add Person");
		btnAddNewPerson.setOnAction(e -> { runAddPerson(rootStage); });
		
		
		pane.add(btnAddNewPerson, 1,0);
	}
	
	private void runAddPerson(Stage rootStage) {
		//init Stage
		Stage stage = new Stage();
        stage.setTitle("Add New Person");
      
        //init Pane
		GridPane addPersonPane = new GridPane();
		addPersonPane.setAlignment(Pos.TOP_LEFT);
		addPersonPane.setPadding(new Insets(15, 15, 15, 15));
		addPersonPane.setHgap(5.5);
		addPersonPane.setVgap(5.5);
		
		TextField tfName = new TextField();
		TextField tfImage = new TextField();
		TextField tfStatus = new TextField();
		TextField tfGender = new TextField();
		TextField tfAge = new TextField();
		TextField tfLivingState = new TextField();
		
		Button okBtn = new Button("Ok");
		okBtn.setOnAction(e -> {
			if (addNewPerson(tfName.getText(),
					tfImage.getText(),
					tfStatus.getText(),
					tfGender.getText(),
					tfAge.getText(),
					tfLivingState.getText().toUpperCase())) {
				//if person was added successfully
				stage.hide(); //hide current stage
				rootStage.show(); //display root one instead
			}
		});
		Button cancelBtn = new Button ("Cancel");
		cancelBtn.setOnAction(e -> {
			stage.hide(); //hide current stage
			rootStage.show(); //display root one instead
		});
		//add all buttons
		addPersonPane.add(new Label("Name:"), 0, 0);
		addPersonPane.add(tfName, 1, 0);
		addPersonPane.add(new Label("Image:"), 0, 1);
		addPersonPane.add(tfImage, 1, 1);
		addPersonPane.add(new Label("Status:"), 0, 2);
		addPersonPane.add(tfStatus, 1, 2);
		addPersonPane.add(new Label("Gender:"), 0, 3);
		addPersonPane.add(tfGender, 1, 3);
		addPersonPane.add(new Label("Age:"), 0, 4);
		addPersonPane.add(tfAge, 1, 4);
		addPersonPane.add(new Label("Living State:"), 0, 5);
		addPersonPane.add(tfLivingState, 1, 5);
		
		addPersonPane.add(okBtn, 0, 7);
		addPersonPane.add(cancelBtn, 1, 7);
		
		//set scene and show it
        stage.setScene(new Scene(addPersonPane, 450, 450));
        stage.show();
        rootStage.hide();
	}
	
	private boolean addNewPerson(String name,
			String image,
			String status,
			String gender,
			String ageRaw,
			String livingState) {
		//validate
		if (isInputInvalid(name, image, status, gender, ageRaw, livingState))
			return false;
		//cool, go ahead
		int age = Integer.parseInt(ageRaw);

		
		
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
			isInvalid(gender.equals(""), "Gender cannot be empty") ||
			isInvalid(!isValidAge(age), "Please enter valid age") ||
			isInvalid(!isValidState(livingState), "Please enter valid state");
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
			showError(errorText);
		}
		return condition;
	}

	private static void showError(String text) {
		new Alert(Alert.AlertType.ERROR, text).showAndWait();
	}
	
	/**
	 * main launcher
	 */
	public static void main(String[] args) {
		launch(args);
	}
}