/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.util.List;

import driver.Driver;
import driver.IDriver;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import users.Profile;

public class SelectProfile extends SecondaryStage {
	
	/**
	 * main driver class with business logic
	 */
	static IDriver driver = new Driver();
	
	/**
	 * selected person
	 */
	Profile selectedPerson = null; // selected person
	
	private boolean isProfileSelectable;
	
	public SelectProfile(boolean isProfileSelectable) {
		this.isProfileSelectable = isProfileSelectable;
	}

	private ObservableList<String[]> generateData(List<Profile> profiles) {
		ObservableList<String[]> list = FXCollections.observableArrayList();
		for (Profile profile : profiles) {
			list.add(new String [] {
  					profile.getName(),
  					profile.getStatus(),
  					Integer.toString(profile.getAge()),
  					profile.getGender(),
  					profile.getLivingState()
  					});
		}
		return list;
	}
	
	public Profile getSelectedProfile() {
		return selectedPerson;
	}

	@Override
	protected void show(GridPane gridPaneContainer) {
		TableView<String[]> tb = new TableView<>();
		gridPaneContainer.add(tb, 1, 3);  		
  		tb.setEditable(false);
  		
  		TableColumn<String[], String> tc1 = new TableColumn<>("Name");
  		tc1.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[0]));
  		TableColumn<String[], String> tc2 = new TableColumn<>("Status");
  		tc2.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[1]));
  		TableColumn<String[], String> tc3 = new TableColumn<>("Age");
  		tc3.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[2]));
  		TableColumn<String[], String> tc4 = new TableColumn<>("Gender");
  		tc4.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[3]));
  		TableColumn<String[], String> tc5 = new TableColumn<>("State");
  		tc5.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[4]));

  		tb.getColumns().add(tc1);
  		tb.getColumns().add(tc2);
  		tb.getColumns().add(tc3);
  		tb.getColumns().add(tc4);
  		tb.getColumns().add(tc5);
  		
		List<Profile> profiles = driver.getAllProfiles();
  		tb.setItems(generateData(profiles));
		
  		if(isProfileSelectable) {
  			Label lbSelectedPerson =
  					new Label("Please Select Person");
  			gridPaneContainer.add(lbSelectedPerson, 1, 2);
  			tb.setRowFactory(tv -> {
  				TableRow<String[]> row = new TableRow<>();
  				row.setOnMouseClicked(event -> {
  					if (row.isEmpty()
  							|| event.getButton() != MouseButton.PRIMARY) {
  						return;
  					}
  					selectedPerson = profiles.get(row.getIndex());
  					lbSelectedPerson.setText("Selected Person: "
  							+ selectedPerson.getName());
  				});
  				return row;
  			});
  		}
	}
	
	@Override
	protected double getHeight() {
		return 450;
	}

	@Override
	protected double getWidth() {
		return 450;
	}
}
