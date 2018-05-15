/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class SelectRelation extends SecondaryStage {
	
	ComboBox<String> comboBox = new ComboBox<>();
	
	public String getRelation() {
		try {
			return comboBox.getSelectionModel().getSelectedItem().toString();
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void show(GridPane paneWrapper) {
		ObservableList<String> options = 
  			    FXCollections.observableArrayList(
  			        "couple",
  			        "friends",
  			        "classmates",
  			        "colleagues"
  			    );
  		comboBox.setItems(options);
  		paneWrapper.add(comboBox, 1, 2);	
	}
	
}
