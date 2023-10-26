package metapenta.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import processing.javafx.PSurfaceFX;
/**
 * Class to init the Stage of JavaFX processing scenes
 * @author vparrac
 *
 */
public class MetaPenta extends Application {
	/**
	 * Surface of Processing
	 */
    public static PSurfaceFX surface;
	@Override
	public void start(Stage primaryStage) throws Exception {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainGUI.fxml"));
        Parent root = loader.load();
        Controller.stage = primaryStage;
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("MetaPenta");
        primaryStage.setScene(scene);
        primaryStage.show();
        surface.stage = primaryStage;
        Controller.stage = primaryStage;
	}


}
