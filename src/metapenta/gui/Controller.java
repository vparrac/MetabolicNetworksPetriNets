package metapenta.gui;



import javafx.scene.control.CheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.processing.petrinet.MySketch;
import metapenta.processing.petrinet.Translator;
import processing.javafx.PSurfaceFX;
public class Controller implements Initializable  {

	public MetabolicNetwork metabolicNetwork;
	public static PSurfaceFX surface;
	public static MySketch p;
    protected static Stage stage;    
    private Translator translator;    
   @FXML
   CheckBox cbSinks;
   
   @FXML
   CheckBox cbSources;
    
   @FXML
   Text details_title;
   
   @FXML
   AnchorPane processing;
    
   @FXML
   HBox options_bar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Canvas canvas = (Canvas) surface.getNative();
        surface.fx.context = canvas.getGraphicsContext2D();        
        processing.getChildren().add(canvas);        
        canvas.widthProperty().bind(processing.widthProperty());
        canvas.heightProperty().bind(processing.heightProperty());	
        
	}	
	
	@FXML
	public void handleButtonAction(ActionEvent event){		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the metabolic network file");		
		File file = fileChooser.showOpenDialog(null);
		if(file!=null) {
			MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
			try {
				this.metabolicNetwork = loader.loadNetwork(file.getCanonicalPath());
				this.metabolicNetwork.makeNet();
				translator = new Translator(metabolicNetwork);
				p.positionTransitions = translator.positionTransitions;				
				p.positionsPlaces = translator.positionsPlaces;
				p.adjacencyMatrix = translator.adjacencyMatrix;
				p.adjacencyMatrixWeightsTP = translator.adjacencyMatrixWeightsTP;
				p.adjacencyMatrixWeightsPT = translator.adjacencyMatrixWeightsPT;
				p.translator = translator;
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}
	}
	
	@FXML
	public void paintSinks() {		
		if(cbSinks.isSelected()) {
			translator.calculateSinks();
		}		
		else {
			translator.restoreSinks();
		}
	}
	
	@FXML
	public void paintSources() {
		if(cbSources.isSelected()) {
			translator.calculateSources();
		}	
		else {
			translator.restoreSources();
		}
	}
}
