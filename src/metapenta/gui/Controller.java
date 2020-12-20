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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
/**
 * JavaFX MVC model controller class
 * @author Valerie Parra Cortés
 */
public class Controller implements Initializable  {
	/**
	 * Main class of world
	 */
	public MetabolicNetwork metabolicNetwork;
	/**
	 * Surface that allows us to communicate with the main class of processing
	 */
	public static PSurfaceFX surface;
	/**
	 * Reference to processing main class
	 */
	public static MySketch metabolicNetworkProcessing;
	/**
	 *  Stage that will be used to display processing
	 */
	protected static Stage stage;  
	/**
	 * Class that translates the model used in the world so that it can be painted in the interface
	 */
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

	@FXML
	CheckBox isSubstrateCheckBox;

	@FXML
	CheckBox isProductCheckBox;

	@FXML
	CheckBox gapFindFilesCheckBox;

	List<String> lista;

	List<String> initialMetabolitesString;
	String targetStringMetabolite;
	String metaboliteName;

		@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			// These lines allow you to tell processing in which panel the entire JavaFX scene will be drawn
		Canvas canvas = (Canvas) surface.getNative();
		surface.fx.context = canvas.getGraphicsContext2D();        
		processing.getChildren().add(canvas);        
		canvas.widthProperty().bind(processing.widthProperty());
		canvas.heightProperty().bind(processing.heightProperty());
	}	

		
	/**
	 * Method that load a metabolic network enter by parameters
	 * @param event of button action (not used)
	 */
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


	/**
	 * Method that calculates and show the metabolic path
	 */
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


	
	/**
	 * Method to init the petri net of processing class
	 */
	public void assignAttributesToMetabolicNetwork() {
		metabolicNetworkProcessing.positionTransitions = translator.positionTransitions;				
		metabolicNetworkProcessing.positionsPlaces = translator.positionsPlaces;
		metabolicNetworkProcessing.adjacencyMatrix = translator.adjacencyMatrix;
		metabolicNetworkProcessing.adjacencyMatrixWeightsTP = translator.adjacencyMatrixWeightsTP;
		metabolicNetworkProcessing.adjacencyMatrixWeightsPT = translator.adjacencyMatrixWeightsPT;
		metabolicNetworkProcessing.translator = translator;
	}

	@FXML
	public void downloadButtonAction(ActionEvent event) {

	}


	/**
	 * Method to enable the components of UI where a network was upload
	 */
	private void enableComponets(){		
		initialMetabolites.setEditable(true);			
		targetMetabolite.setEditable(true);
		findPathButton.setDisable(false);
		entryMetabolite.setEditable(true);
		findReactionMetaboliteButton.setDisable(false);
		sinksSourcesCheckBox.setDisable(false);
		connectedComponentsCheckbox.setDisable(false);
		downloadFilesButton.setDisable(false);		
		isSubstrateCheckBox.setDisable(false);
		isProductCheckBox.setDisable(false);
		downloadFilesButton.setDisable(false);
		sinksSourcesCheckBox.setDisable(false);
		connectedComponentsCheckbox.setDisable(false);
		gapFindFilesCheckBox.setDisable(false);

	}
	
	/**
	 * Method that allows to the user download the information of path calculated
	 * @param event of button action (not used)
	 */

	@FXML
	public void pathButtonAction(ActionEvent event) {
		TextInputDialog td = new TextInputDialog("metabolicNetworkFileName");
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


	/**
	 * Method to download the reaction of the current metabolite
	 */
	@FXML
	public void downloadReactionsMetabolites() {
		TextInputDialog td = new TextInputDialog("metabolicNetworkFileName");
		td.setHeaderText("Enter the name of file"); 
		Optional<String> result =td.showAndWait(); 
		result.ifPresent(name -> {
			try {
				String file1 = name;				
				metabolicNetwork.printInAFileReactionsOfMetabolite(metaboliteName, file1);
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


	/**
	 * Method to download the files of sink, sources and connected components
	 */
	
	public void downloadFiles() {
		if(!sinksSourcesCheckBox.isSelected()&&!connectedComponentsCheckbox.isSelected()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error downloading the files");
			alert.setContentText("Please select a type of analysis");
			alert.showAndWait();
		}
		else {
			TextInputDialog td = new TextInputDialog("metabolicNetworkFileName");
			td.setHeaderText("Enter the suffix of the files"); 
			Optional<String> result =td.showAndWait(); 
			result.ifPresent(name -> {

				try {
					if(sinksSourcesCheckBox.isSelected()) {
						metabolicNetwork.printsSinksInAFile(name+"_sinks.txt");
						metabolicNetwork.printsSourcesInAFile(name+"_sources.txt");
					}
					if(connectedComponentsCheckbox.isSelected()) {
						metabolicNetwork.printConnectedComponents(name+"_cc.txt");
					}					
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
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error calculating the path");
					alert.setContentText("An error occurred, please try again");
					alert.showAndWait();
				}

			});		
		}
	}


	/**
	 * Method that resets the metabolic network network
	 * @param event
	 */
	
	@FXML	
	public void cleanButtonAction(ActionEvent event) {		
		translator.resetSubnet();
		assignAttributesToMetabolicNetwork();
	}
	/**
	 * Method to shows the reaction of currect selected metabolite
	 * @param event of button (not used)
	 */

	@FXML	
	public void reactionsButtonAction(ActionEvent event) {
		translator.resetSubnet();
		assignAttributesToMetabolicNetwork();
		boolean productsb = isProductCheckBox.isSelected();
		boolean substrateb = isSubstrateCheckBox.isSelected();
		String metabolite = entryMetabolite.getText();
		if(metabolite.equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error calculating the reactions");
			alert.setContentText("You must indicate the id of the metabolite to which you want to visualize the reactions.");
			alert.showAndWait();
		}
		else if(metabolicNetwork.getMetabolite(metabolite)==null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error calculating the reactions");
			alert.setContentText("The id entered is invalid");
			alert.showAndWait();
		}
		else {
			metaboliteName = metabolite;
			translator.getReactionsOfMetabolite(metabolite, productsb, substrateb);		
			assignAttributesToMetabolicNetwork();
			downloadReactionsMetaboliteButton.setDisable(false);
		}		
	}
}
