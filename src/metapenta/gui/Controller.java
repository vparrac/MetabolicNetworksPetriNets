package metapenta.gui;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
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
	TextArea initialMetabolites;

	@FXML
	TextArea targetMetabolite;

	@FXML
	Button findPathButton;

	@FXML
	Button downloadPathButton;

	@FXML
	TextArea entryMetabolite;

	@FXML
	Button findReactionMetaboliteButton;

	@FXML
	Button downloadReactionsMetaboliteButton;

	@FXML
	CheckBox sinksSourcesCheckBox;

	@FXML
	CheckBox connectedComponentsCheckbox;

	@FXML
	Button downloadFilesButton;

	
	List<String> lista;
	
	List<String> initialMetabolitesString;
	String targetStringMetabolite;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Canvas canvas = (Canvas) surface.getNative();
		surface.fx.context = canvas.getGraphicsContext2D();        
		processing.getChildren().add(canvas);        
		canvas.widthProperty().bind(processing.widthProperty());
		canvas.heightProperty().bind(processing.heightProperty());	


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
				enableComponets();
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
		boolean notNull = true;
		translator.resetSubnet();
		assignAttributesToMetabolicNetwork();
		downloadPathButton.setDisable(true);

		String[] initialMetabolitesString = initialMetabolites.getText().split(",");
		targetStringMetabolite = targetMetabolite.getText();

		if(initialMetabolitesString.length==0||targetStringMetabolite.equals("")) {
			notNull=false;

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error calculating the path");
			alert.setContentText("Please fill in all the fields in order to calculate your route");
			alert.showAndWait();

		}


		if(notNull) {
			boolean correctInput = true;
			List<String> im= new ArrayList<String>();		
			if(metabolicNetwork.getMetabolite(targetStringMetabolite)==null) {
				correctInput = false;
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error calculating the path");
				alert.setContentText("The final metabolite is not in the network. Please check the IDs and try again.");
				alert.showAndWait();
			}

			for (int i = 0; i < initialMetabolitesString.length && correctInput; i++) {
				im.add(initialMetabolitesString[i]);	
				if(metabolicNetwork.getMetabolite(initialMetabolitesString[i])==null) {
					correctInput = false;
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error calculating the path");
					alert.setContentText("One of the input metabolites is not defined. Please check the id.");
					alert.showAndWait();
					break;
				}
			}		
			
			lista = im;

			if(correctInput) {
				Set<String> reactions = translator.shortestPathByMetabolitesNumber(im, targetStringMetabolite);
				if(reactions.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error calculating the path");
					alert.setContentText("It is not possible with these initial metabolites to reach the final metabolite");
					alert.showAndWait();	
				}
				downloadPathButton.setDisable(false);
				assignAttributesToMetabolicNetwork();
			}

		}
	}


	public void assignAttributesToMetabolicNetwork() {
		p.positionTransitions = translator.positionTransitions;				
		p.positionsPlaces = translator.positionsPlaces;
		p.adjacencyMatrix = translator.adjacencyMatrix;
		p.adjacencyMatrixWeightsTP = translator.adjacencyMatrixWeightsTP;
		p.adjacencyMatrixWeightsPT = translator.adjacencyMatrixWeightsPT;
		p.translator = translator;
	}

	@FXML
	public void downloadButtonAction(ActionEvent event) {

	}


	private void enableComponets(){		
		initialMetabolites.setEditable(true);			
		targetMetabolite.setEditable(true);
		findPathButton.setDisable(false);
		entryMetabolite.setEditable(true);
		findReactionMetaboliteButton.setDisable(false);
		sinksSourcesCheckBox.setDisable(false);
		connectedComponentsCheckbox.setDisable(false);
		downloadFilesButton.setDisable(false);		
	}

	@FXML
	public void pathButtonAction(ActionEvent event) {
		TextInputDialog td = new TextInputDialog("Name of the files");
		td.setHeaderText("Enter the suffix of the files"); 
		Optional<String> result =td.showAndWait(); 
		result.ifPresent(name -> {
		    try {
		    	String file1 = name+"_metabolicNetwork.txt";
		    	String file2 = name+"_reactions.txt";
		    	String file3 = name+"_catalysts.txt";
				metabolicNetwork.printShortestPath(file1, file2, file3, lista, targetStringMetabolite);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText("Success creating files");
				alert.setContentText("The files were generated successfully");
				alert.showAndWait();
			} catch (FileNotFoundException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error calculating the path");
				alert.setContentText("An error occurred, please try again");
				alert.showAndWait();
				
			}
		});		
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
