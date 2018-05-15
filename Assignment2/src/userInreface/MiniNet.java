/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.sql.SQLException;

import CustomExceptions.*;
import driver.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import repository.Initializer;
import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 *	Main UI class
 */
public class MiniNet extends Application {
	
	/**
	 * main driver class with business logic
	 */
	static IDriver driver = new Driver();
	
	/**
	 * selected person
	 */
	Profile selectedPerson = null; // selected person
	
	/**
	 * startup method
	 */
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		try {
			//try init db
			String result = Initializer.Init();
			if (result != null) {
				showError(result);
				return;
			}
			if (Initializer.getLogs().length() > 1) {
				//if any warnings available, display it
				showError(Alert.AlertType.WARNING, Initializer.getLogs());
			}
			GridPane mainPain = initPane(); // Create a pane1
			
			//add all buttons here
			setupMainPane(mainPain, primaryStage);
			
			// Create a scene and place it in the stage
			Scene scene = new Scene(mainPain, 600, 450);
			primaryStage.setTitle("Social Network"); // Set the stage title
			primaryStage.setScene(scene); // Place the scene in the stage
			primaryStage.show(); // Display the stage
			
			//close stage on pressing Exit button
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        System.exit(0);
			    }
			});
		} catch (Exception e) {
			showError("Fatal Error on inializing program");
		}
	}
	
	/**
	 * @return main pane from root stage
	 */
	private GridPane initPane() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setPadding(new Insets(15, 15, 15, 15));
		pane.setHgap(5.5);
		pane.setVgap(20);
		return pane;
	}
	
	/**
	 * setup main pane
	 */
	private void setupMainPane(GridPane pane, Stage rootStage) {
		//create buttons
		Button btnShowAll = new Button("Show Everyone");
		btnShowAll.setOnAction(e -> {
			new SelectProfile(false).show(rootStage);
		});	
		Button btnAddNewPerson = new Button("Add Person");
		btnAddNewPerson.setOnAction(e -> { addNewPerson(rootStage); });
		
		Button btnDisplayProfile = new Button("Display selected profile");
		btnDisplayProfile.setDisable(true);
		btnDisplayProfile.setOnAction(e -> {
			ShowProfile sp = new ShowProfile(selectedPerson);
			sp.show(rootStage);
		});
		
		Button btnFindSubNames = new Button("Display Names of person's"
				+ "children OR of parents if child selected");
		btnFindSubNames.setDisable(true);
		btnFindSubNames.setOnAction(e -> { findSubNames();});
		
		Label lbSelectedPerson = new Label("No person selected");
		
		Button btnDeleteProfile = new Button("Delete selected profile");
		btnDeleteProfile.setDisable(true);
		btnDeleteProfile.setOnAction(e ->
		{
			//disable buttons when profile deleted
			if (deleteProfile(selectedPerson)) {
				btnDisplayProfile.setDisable(true);
				btnDeleteProfile.setDisable(true);
				btnFindSubNames.setDisable(true);
				showError("Person Deleted successfully");
				lbSelectedPerson.setText("No profile selected");
			} else {
				showError("Error on deleting person");
			}
		});
		
		Button btnIsDirectFriends =
				new Button("Are two prople directly connected?");
		btnIsDirectFriends.setOnAction(e -> { areTwoConnected(rootStage); });
		
		Button btnDefineRelation = new Button("Define relation");
		btnDefineRelation.setOnAction(e -> { defineRelation(rootStage); });
		
		Button btnSelectPerson = new Button("Select Person");
		btnSelectPerson.setOnAction(e ->
		{
			//show another stage to select person
			SelectProfile sp = new SelectProfile(true);
			sp.show(rootStage);			
			if (sp.getSelectedProfile() == null) return;
			
			selectedPerson = sp.getSelectedProfile();
			lbSelectedPerson.setText("Selected Person: "
					+ selectedPerson.getName());
			//enable disabled features
			btnDisplayProfile.setDisable(false);
			btnDeleteProfile.setDisable(false);
			btnFindSubNames.setDisable(false);
		});
		
		//add buttons to pane
		pane.add(btnShowAll, 0, 0);
		pane.add(btnAddNewPerson, 0, 1);
		pane.add(btnSelectPerson, 0, 2);
		pane.add(lbSelectedPerson, 1, 2);
		pane.add(btnDisplayProfile, 0, 3);
		pane.add(btnDeleteProfile, 0, 4);
		pane.add(btnIsDirectFriends, 0, 5);
		pane.add(btnDefineRelation, 0, 6);
		pane.add(btnFindSubNames, 0, 7);
		
		//create and add Exit button
		Button btnExit = new Button("Exit");
		btnExit.setOnAction(e -> { 
			rootStage.close();
		});
		pane.add(btnExit, 0, 8);
	}

	/**
	 * display sub names
	 */
	private void findSubNames() {
		String text;
		if (selectedPerson instanceof AdultProfile) {
			text = "Children: "
				+ driver.getDependantName((AdultProfile)selectedPerson);
		} else {
			text = "Parents: "
				+ driver.getNamesOfTheParents(
						(ChildProfile)selectedPerson);
		}
		
		showError(AlertType.INFORMATION, text);
	}

	/**
	 * show Add Person stage
	 */
	private void addNewPerson(Stage rootStage) {
		AddPerson addHelper = new AddPerson() {
			@Override
			public void displayError(String errorText) {
				showError(errorText);
			}
		};
		addHelper.show(rootStage);
	}
	
	/**
	 * show stages to define relations
	 */
	private void defineRelation(Stage rootStage) {
		//diplsay stages to select profiles
		SelectProfile sp1 = new SelectProfile(true);
		sp1.show(rootStage);			
		if (sp1.getSelectedProfile() == null) return;
		
		SelectProfile sp2 = new SelectProfile(true);
		sp2.show(rootStage);
		if (sp2.getSelectedProfile() == null) return;
		
		if (sp1.getSelectedProfile().getName()
			== sp2.getSelectedProfile().getName()) {
			showError("Please choose different people");
			return;
		}
		
		//after 2 profiles selected, select relation
		SelectRelation relationWindow = new SelectRelation();
		relationWindow.show(rootStage);
		if (relationWindow.getRelation() == null) {
			showError("Please select relation");
			return;
		};
		try {
			driver.setRelation(
					sp1.getSelectedProfile(),
					sp2.getSelectedProfile(),
					relationWindow.getRelation());
		} catch (NotToBeColleaguesException | NotToBeClassmatesException
				| NotToBeFriendsException | TooYoungException
				| NotToBeCoupledException | NoAvailableException e) {
			//show specific error message
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("DB error occured");
		} catch (Exception e) {
			showError("Unexpected error occured");
		}
	}

	/**
	 * display whether 2 people directly connected
	 */
	private void areTwoConnected(Stage rootStage) {
		//diplsay stages to select profiles
		SelectProfile sp1 = new SelectProfile(true);
		sp1.show(rootStage);			
		if (sp1.getSelectedProfile() == null) return;
		
		SelectProfile sp2 = new SelectProfile(true);
		sp2.show(rootStage);			
		if (sp2.getSelectedProfile() == null) return;
		
		//after 2 profiles selected, get connection info
		boolean isConnected = driver.isDirectlyConnected(
				sp1.getSelectedProfile(),
				sp2.getSelectedProfile());
		String text = sp1.getSelectedProfile().getName()
				+ " AND " + sp2.getSelectedProfile().getName()
				+ (isConnected
						? " are directly connected"
						: " are NOT directly connected"); 
		showError(AlertType.INFORMATION, text);
	}

	/**
	 * delete profile
	 * @return true if deleted successfully
	 */
	private boolean deleteProfile(Profile profile) {
		return driver.deletePerson(profile);
	}
	
	/**
	 * shows alert dialog with given alert type
	 */
	private static void showError(Alert.AlertType alertType, String text) {
		Alert alert = new Alert(alertType, text);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(500);
		alert.showAndWait();
	}
	
	/**
	 * shows error dialog
	 */
	private static void showError(String text) {
		showError(Alert.AlertType.ERROR, text);
	}
	
	/**
	 * main launcher
	 */
	public static void main(String[] args) {
		launch(args);
	}
}