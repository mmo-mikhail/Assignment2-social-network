/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

public class SelectParents extends SecondaryStage {

	private String[] couples;

	ComboBox<String> comboBox = new ComboBox<>();
	
	public String selectedParents() {
		return comboBox.getSelectionModel().getSelectedItem().toString();
	}

	public void setCouples(String[] spouses) {
		couples = spouses;
	}
	
	@Override
	protected void show(GridPane paneWrapper) {
		ObservableList<String> options = 
  			    FXCollections.observableArrayList(couples);
  		comboBox.setItems(options);
  		paneWrapper.add(comboBox, 1, 2);	
	}
}
