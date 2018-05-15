package userInreface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class SecondaryStage {
	
	protected Stage rootStage;
	
	protected Stage stage;
	
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
	
	protected String getCloseButtonText() {
		return "OK";
	}

	protected int[] closeIdxs() {
		return new int[] {1, 1};
	}

	protected double getHeight() {
		return 200;
	}

	protected double getWidth() {
		return 200;
	}
	
	protected void closeStage() {
		stage.close(); //hide current stage
		rootStage.show(); //display root one instead
	}
	
	protected abstract void show(GridPane gridPaneContainer);
}
