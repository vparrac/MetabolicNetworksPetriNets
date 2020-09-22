package metapenta.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	public void start(Stage arg0) throws Exception {

		Label lb1,lb2;
		lb1= new Label();
		lb1.setText("Hello");
		lb2=new Label();
		lb2.setText("World!");
		
		VBox pane = new VBox();
		
		pane.getChildren().add(lb1);
		pane.getChildren().add(lb2);
		Scene scene= new Scene(pane,250,150);
		arg0.setScene(scene);
		arg0.setTitle("Hello world");
		arg0.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
