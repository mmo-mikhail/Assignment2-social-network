package userInreface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * this class represents base stage for secondary stages
 */
public abstract class SecondaryStage {
	
	/**
	 * root stage
	 */
	protected Stage rootStage;
	
	/**
	 * current stage
	 */
	private Stage stage;
	
	public void show(Stage rootStage) {
		this.rootStage = rootStage;
		//init Stage
		Stage stage = new Stage();
        stage.setTitle("All People");
        this.stage = stage;
        
        //init Pane
  		GridPane everyonePane = new GridPane();
  		everyonePane.setAlignment(Pos.TOP_LEFT);
  		everyonePane.setPadding(new Insets(15, 15, 15, 15));
  		everyonePane.setHgap(5.5);
  		everyonePane.setVgap(9);
		
  		Button btnClose = new Button(getCloseButtonText());
  		btnClose.setOnAction(e -> {
  			closeStage();
		});
  		stage.setOnCloseRequest(we -> {
  			closeStage();
  		});
  		everyonePane.add(btnClose, closeIdxs()[0], closeIdxs()[1]);

  		//do children's implementation here
  		show(everyonePane);
  		
  		//set scene and show it
        stage.setScene(new Scene(everyonePane, getWidth(), getHeight()));
        rootStage.hide();
        stage.showAndWait();
	}
	
	/**
	 * @return close button text
	 */
	protected String getCloseButtonText() {
		return "OK";
	}
	
	/**
	 * close button indexes
	 */
	protected int[] closeIdxs() {
		return new int[] {1, 1};
	}

	/**
	 * @return height of the stage
	 */
	protected double getHeight() {
		return 200;
	}

	/**
	 * @return width of the stage
	 */
	protected double getWidth() {
		return 200;
	}
	

	/**
	 * closes the stage and display root/main stage
	 */
	protected void closeStage() {
		stage.close(); //hide current stage
		rootStage.show(); //display root one instead
	}
	
	/**
	 * fill main grid pane for the stage
	 */
	protected abstract void show(GridPane gridPaneContainer);
}
