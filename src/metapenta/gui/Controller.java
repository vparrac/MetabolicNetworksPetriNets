package metapenta.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import metapenta.visualization.petrinet.MySketch;
import processing.javafx.PSurfaceFX;
public class Controller implements Initializable  {

	public static PSurfaceFX surface;
	public static MySketch p;
    protected static Stage stage;

    @FXML
    AnchorPane processing;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Canvas canvas = (Canvas) surface.getNative();
        surface.fx.context = canvas.getGraphicsContext2D();
        
        processing.getChildren().add(canvas);
        
		
	}

}
