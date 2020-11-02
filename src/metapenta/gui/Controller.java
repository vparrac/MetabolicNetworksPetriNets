package metapenta.gui;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
   Text details_title;
   
   @FXML
   AnchorPane processing;
    
   @FXML
   HBox options_bar;
   
   @FXML
   ChoiceBox<String> metabolite_list;   
	
   @FXML
   TextArea id_metabolite_text;
   
   @FXML
   ComboBox<String> optionsPath;
   
   @FXML
   TextArea initialMetabolites;
   
   @FXML
   TextArea targetMetabolite;
   
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Canvas canvas = (Canvas) surface.getNative();
        surface.fx.context = canvas.getGraphicsContext2D();        
        processing.getChildren().add(canvas);        
        canvas.widthProperty().bind(processing.widthProperty());
        canvas.heightProperty().bind(processing.heightProperty());	
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("Reactions number", "Enzymes number");
        optionsPath.setItems(items);
        
	}	
	
	@FXML
	public void loadButtonAction(ActionEvent event){		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the metabolic network file");		
		File file = fileChooser.showOpenDialog(null);
		if(file!=null) {
			MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
			try {
				this.metabolicNetwork = loader.loadNetwork(file.getCanonicalPath());
				this.metabolicNetwork.makeNet();				
				translator = new Translator(metabolicNetwork);		
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Sucess");
		        alert.setHeaderText("Successfully uploaded");
		        alert.setContentText("The information of the network was updated successfully");
		        alert.showAndWait();				
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}		
	}
	
	
	@FXML
	public void findMetabolicPath() {
		String[] initialMetabolitesString = initialMetabolites.getText().split(",");
		String targetStringMetabolite = targetMetabolite.getText();
		List<String> im= new ArrayList<String>();
		for (int i = 0; i < initialMetabolitesString.length; i++) {
			im.add(initialMetabolitesString[i]);			
		}		
		try {
			Set<String> graph = metabolicNetwork.shortestPathByMetabolitesNumber(im, targetStringMetabolite);
			System.out.println(graph.toString());			
		} catch (Exception e) {		
			e.printStackTrace();
		}		
	}
	
	
	@FXML
	public void downloadButtonAction(ActionEvent event) {
		
	}
	
	@FXML	
	public void findReactionButtonAction() {
//		String metabolite = id_metabolite_text.getText();
//		translator.getReactionsOfMetabolite(metabolite);		
//		p.positionTransitions = translator.positionTransitions;				
//		p.positionsPlaces = translator.positionsPlaces;
//		p.adjacencyMatrix = translator.adjacencyMatrix;
//		p.adjacencyMatrixWeightsTP = translator.adjacencyMatrixWeightsTP;
//		p.adjacencyMatrixWeightsPT = translator.adjacencyMatrixWeightsPT;
//		p.translator = translator;
//		
	}
}
