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

/**
 * @author s3419069 (Mykhailo Muzyka)
 * this class represents Select Parents stage
 */
public class SelectParents extends SecondaryStage {

	/**
	 * couples list
	 */
	private String[] couples;

	/**
	 * just simple combo box
	 */
	private ComboBox<String> comboBox = new ComboBox<>();
	
	/**
	 * @return selected parents
	 */
	public String selectedParents() {
		return comboBox.getSelectionModel().getSelectedItem().toString();
	}

	/**
	 * set couplse
	 */
	public void setCouples(String[] spouses) {
		couples = spouses;
	}
	
	/**
	 * fill main gridpane for the stage
	 */
	@Override
	protected void show(GridPane paneWrapper) {
		ObservableList<String> options = 
  			    FXCollections.observableArrayList(couples);
  		comboBox.setItems(options);
  		paneWrapper.add(comboBox, 1, 2);	
	}
}
