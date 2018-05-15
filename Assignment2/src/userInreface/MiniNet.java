/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.sql.SQLException;

import CustomExceptions.*;
import driver.Driver;
import driver.IDriver;
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
import users.AdultProfile;
import users.ChildProfile;
import users.Profile;

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
		String result = Initializer.Init();
		if (result != null) {
			showError(result);
			return;
		}
		if (Initializer.getLogs().length() > 1) {
			showError(Alert.AlertType.WARNING, Initializer.getLogs());
		}
		GridPane mainPain = initPane(); // Create a pane1
		
		setupMainPane(mainPain, primaryStage);
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(mainPain, 600, 450);
		primaryStage.setTitle("Social Network"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        //Platform.exit();
		        System.exit(0);
		    }
		});
	}
	
	private GridPane initPane() {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.TOP_LEFT);
		pane.setPadding(new Insets(15, 15, 15, 15));
		pane.setHgap(5.5);
		pane.setVgap(20);
		return pane;
	}
	
	private void setupMainPane(GridPane pane, Stage rootStage) {
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
			SelectProfile sp = new SelectProfile(true);
			sp.show(rootStage);			
			if (sp.getSelectedProfile() == null) return;
			
			selectedPerson = sp.getSelectedProfile();
			lbSelectedPerson.setText("Selected Person: "
					+ selectedPerson.getName());
			btnDisplayProfile.setDisable(false);
			btnDeleteProfile.setDisable(false);
			btnFindSubNames.setDisable(false);
		});
		
		pane.add(btnShowAll, 0, 0);
		pane.add(btnAddNewPerson, 0, 1);
		pane.add(btnSelectPerson, 0, 2);
		pane.add(lbSelectedPerson, 1, 2);
		pane.add(btnDisplayProfile, 0, 3);
		pane.add(btnDeleteProfile, 0, 4);
		pane.add(btnIsDirectFriends, 0, 5);
		pane.add(btnDefineRelation, 0, 6);
		pane.add(btnFindSubNames, 0, 7);
		
		Button btnExit = new Button("Exit");
		btnExit.setOnAction(e -> { 
			rootStage.close();
		});
		pane.add(btnExit, 0, 8);
	}

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

	private void addNewPerson(Stage rootStage) {
		AddPerson addHelper = new AddPerson() {
			@Override
			public void displayError(String errorText) {
				showError(errorText);
			}
		};
		addHelper.show(rootStage);
	}
	
	private void defineRelation(Stage rootStage) {
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
		
		SelectRelation relationWindow = new SelectRelation();
		relationWindow.show(rootStage);
		if (relationWindow.getRelation() == null) return;
		try {
			driver.setRelation(
					sp1.getSelectedProfile(),
					sp2.getSelectedProfile(),
					relationWindow.getRelation());
		} catch (NotToBeColleaguesException | NotToBeClassmatesException
				| NotToBeFriendsException | TooYoungException
				| NotToBeCoupledException | NoAvailableException e) {
			showError(e.getMessage());
		} catch (SQLException e) {
			showError("DB error occured");
		} catch (Exception e) {
			showError("Unexpected error occured");
		}
	}

	private void areTwoConnected(Stage rootStage) {
		SelectProfile sp1 = new SelectProfile(true);
		sp1.show(rootStage);			
		if (sp1.getSelectedProfile() == null) return;
		
		SelectProfile sp2 = new SelectProfile(true);
		sp2.show(rootStage);			
		if (sp2.getSelectedProfile() == null) return;
		
		boolean isConnected = driver.isDirectlyConnected(
				sp1.getSelectedProfile(),
				sp2.getSelectedProfile());
		String text = sp1.getSelectedProfile().getName()
				+ " AND " + sp2.getSelectedProfile().getName()
				+ (isConnected
						? " is directly connected"
						: " is NOT directly connected"); 
		showError(AlertType.INFORMATION, text);
	}

	private boolean deleteProfile(Profile profile) {
		return driver.deletePerson(profile);
	}
	
	private static void showError(Alert.AlertType alertType, String text) {
		Alert alert = new Alert(alertType, text);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(500);
		alert.showAndWait();
	}
	
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