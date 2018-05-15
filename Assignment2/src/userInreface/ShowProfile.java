/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package userInreface;

import java.io.File;
import javafx.scene.image.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import users.Profile;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * this class represents stage to display full info about profile
 */
public class ShowProfile extends SecondaryStage {
	
	/**
	 * Profile to display
	 */
	Profile profile = null; // selected profile
	
	public ShowProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * fill main gridpane for the stage
	 */
	@Override
	protected void show(GridPane gridPaneContainer) {
		//init labels
		String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold;";
		Label lbName = new Label(profile.getName());
		lbName.setStyle(labelStyle); 
		
		Label lbStatus = new Label(profile.getStatus());
		lbStatus.setStyle(labelStyle);
		Label lbGender = new Label(profile.getGender());
		lbGender.setStyle(labelStyle); 
		Label lbAge = new Label(String.valueOf(profile.getAge()));
		lbAge.setStyle(labelStyle); 
		Label lbState = new Label(profile.getLivingState());
		lbState.setStyle(labelStyle);
		
		//add all buttons
		gridPaneContainer.add(new Label("Name:"), 0, 0);
		gridPaneContainer.add(lbName, 1, 0);
		gridPaneContainer.add(new Label("Image:"), 0, 1);
		
		gridPaneContainer.add(new Label("Status:"), 0, 2);
		gridPaneContainer.add(lbStatus, 1, 2);
		gridPaneContainer.add(new Label("Gender:"), 0, 3);
		gridPaneContainer.add(lbGender, 1, 3);
		gridPaneContainer.add(new Label("Age:"), 0, 4);
		gridPaneContainer.add(lbAge, 1, 4);
		gridPaneContainer.add(new Label("Living State:"), 0, 5);
		gridPaneContainer.add(lbState, 1, 5);
		
		//try add image if exists
		if (profile.getImage() != null && !profile.getImage().equals("")
				&& new File("images/" + profile.getImage()).exists()) {
			
			Image img = new Image("file:images/" + profile.getImage());
			ImageView view = new ImageView(img);
			view.setFitWidth(100);
			view.setFitHeight(100);
			gridPaneContainer.add(view, 1, 1);
			
		} else {
			Label lbImage = new Label("No Image");
			lbImage.setStyle(labelStyle + " -fx-text-fill: red;");
			gridPaneContainer.add(lbImage, 1, 1);
		}
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
		return 300;
	}
	
	/**
	 * close button indexes
	 */
	@Override
	protected int[] closeIdxs() {
		return new int[] {0, 7};
	}
}
