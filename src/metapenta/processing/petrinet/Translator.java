package metapenta.processing.petrinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import metapenta.model.GeneProduct;
import metapenta.model.MetabolicNetwork;
import metapenta.model.Metabolite;
import metapenta.model.Reaction;
import metapenta.model.ReactionComponent;
import metapenta.petrinet.Edge;
import metapenta.petrinet.Place;
import metapenta.petrinet.Transition;
/**
 * This class is to "translate" the data of the model to the correct visualization
 * @author Valerie Parra Cortés
 */
public class Translator {

	/**
	 * Main meabolic Network model
	 */
	public MetabolicNetwork metabolicNetworkModel;
	public MetabolicNetwork subNetworkModel;
	/**
	 * The position of the transitions
	 */
	public ArrayList<TransitionProcessing> positionTransitions = new ArrayList<TransitionProcessing>();
	/**
	 * The location of places
	 */
	public ArrayList<PlaceProcessing> positionsPlaces = new ArrayList<PlaceProcessing>();
	/**
	 * Matrix of adjacency of Petri Net 
	 * 0 means no edge
	 * 1 means edge Transition->Place
	 * 2 means edge Place->Transition
	 * 3 means edge Place->Transition and Transition->Place
	 */
	public int[][] adjacencyMatrix;	
	/**
	 * Represents the weight between transitions to places 
	 */
	public double[][] adjacencyMatrixWeightsTP;
	/**
	 * Represents the weights between places to transition
	 */
	public double[][] adjacencyMatrixWeightsPT;

	private int x_places = 100;
	private int y_places = 50;


	private int x_transitions = 300;
	private int y_transitions = 50;
	

	private final static String IS_SUBSTRATE = "Substrates";
	private final static String IS_PRODUCT = "Products";


	public Translator(MetabolicNetwork metabolicNetworkModel) {
		this.metabolicNetworkModel = metabolicNetworkModel;		
//		translate();
	}	
	
	
	
	public void getReactionsOfMetabolite(String metaboliteKeyName) {		
		Map<String,List<Reaction>> reactions = metabolicNetworkModel.getReactionOfMetabolite(metaboliteKeyName);		
		Map<String,Reaction> reactions_map = new TreeMap<String,Reaction>();		
		
		List<Reaction> isSubstrate =reactions.get(IS_SUBSTRATE);
		for (int i = 0; i < isSubstrate.size(); i++) {
			reactions_map.put(isSubstrate.get(i).getId(), isSubstrate.get(i));
		}
		List<Reaction> isProduct = reactions.get(IS_PRODUCT);
		for (int i = 0; i < isProduct.size(); i++) {
			reactions_map.put(isProduct.get(i).getId(), isProduct.get(i));
		}		
		this.subNetworkModel = new MetabolicNetwork(reactions_map);
		translate();
	}	
	
	public Set<String> shortestPathByMetabolitesNumber(List<String> initialMetabolites, String last){
		Set<String> reactionsS = metabolicNetworkModel.shortestPathByMetabolitesNumber(initialMetabolites, last);
		Map<String,Reaction> reactions = new TreeMap<String, Reaction>();
		for (String keyReaction : reactionsS) {
			reactions.put(keyReaction, metabolicNetworkModel.getReaction(keyReaction));			
		}
		this.subNetworkModel = new MetabolicNetwork(reactions);
		translate();
		changeColorsMetabolites(initialMetabolites, last);	
		return reactionsS;
	}
	
	
	
	private void changeColorsMetabolites(List<String> initialMetabolites, String last) {	
		for (int i = 0; i < positionsPlaces.size(); i++) {
			if(initialMetabolites.contains(positionsPlaces.get(i).getName())) {
				positionsPlaces.get(i).setColor_place(Constants.ORANGE);
			}			
			if(last.equalsIgnoreCase(positionsPlaces.get(i).getName())) {
				positionsPlaces.get(i).setColor_place(Constants.GREEN);
			}
		}
		
		
	}
	
	
	private void translate() {
		Set<Integer> keysTransitions = subNetworkModel.getTransitions().keySet();
		Set<Integer> keysPlaces = subNetworkModel.getPlacesbyNumber().keySet();
		double nodesPerColum = Math.max(Math.ceil(Math.sqrt(keysTransitions.size())), Math.ceil(Math.sqrt(keysPlaces.size())));
		int counter = 0;
		for (Integer key : keysPlaces) {
			Place< Metabolite, Reaction> place = subNetworkModel.getPlacesbyNumber().get(key);
			PlaceProcessing placeProcesing = new PlaceProcessing(x_places, y_places, Constants.BS, Constants.BS, place.getObject().getId(), Constants.PURPLE, Constants.BLACK);
			this.positionsPlaces.add(placeProcesing);
			counter++;
			if(counter%nodesPerColum==0) {
				this.y_places = Constants.Y_INIT;
				this.x_places = x_places + Constants.X_OFFSET;
			}
			else{
				this.y_places = y_places + Constants.Y_OFFSET;
			}
		}
		counter=0;
		for (Integer key : keysTransitions) {
			Transition<Metabolite, Reaction> transition = subNetworkModel.getTransitions().get(key);		
			TransitionProcessing transitionProcessing = new PlaceProcessing(x_transitions, y_transitions, Constants.BS, Constants.BS, transition.getObject().getId(),Constants.BLUE_KING, Constants.WHITE);
			this.positionTransitions.add(transitionProcessing);
			counter++;
			if(counter%nodesPerColum == 0) {
				this.y_transitions = Constants.Y_INIT;
				this.x_transitions = x_transitions + Constants.X_OFFSET;
			}
			else{
				this.y_transitions = y_transitions + Constants.Y_OFFSET;
			}			
		}

		this.adjacencyMatrix = new int[positionTransitions.size()][positionsPlaces.size()];
		this.adjacencyMatrixWeightsTP = new double[positionTransitions.size()][positionsPlaces.size()];
		this.adjacencyMatrixWeightsPT = new double[positionTransitions.size()][positionsPlaces.size()];
		
		for (Integer key : keysTransitions) {
			Transition< Metabolite, Reaction> transition = subNetworkModel.getTransitions().get(key);
			List<Edge<Place< Metabolite, Reaction>>> outPlaces = transition.getOutPlaces();
			
			for (int i = 0; i < outPlaces.size(); i++) {
				int number = adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1];
				if(number==0) {
					this.adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1]=1;
				}
				else if(number==2) {
					this.adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1]=3;
				}
				
				adjacencyMatrixWeightsTP[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1]= outPlaces.get(i).getStoichiometry();
				System.out.println(outPlaces.get(i).getStoichiometry());
			}			
			List<Edge<Place< Metabolite, Reaction>>> inPlaces = transition.getInPlaces();
			for (int i = 0; i < inPlaces.size(); i++) {				
				int number = adjacencyMatrix[transition.getNumber()-1][inPlaces.get(i).getObject().getMetaboliteNumber()-1];			
				if(number==0) {
					this.adjacencyMatrix[transition.getNumber()-1][inPlaces.get(i).getObject().getMetaboliteNumber()-1]=2;
				}
				else if (number==1) {
					this.adjacencyMatrix[transition.getNumber()-1][inPlaces.get(i).getObject().getMetaboliteNumber()-1]=3;
				}		
				adjacencyMatrixWeightsPT[transition.getNumber()-1][inPlaces.get(i).getObject().getMetaboliteNumber()-1]= inPlaces.get(i).getStoichiometry();
				System.out.println(inPlaces.get(i).getStoichiometry());
			}			
		}
	}	
	
	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	public double[][] getAdjacencyMatrixWeightsPT() {
		return adjacencyMatrixWeightsPT;
	}
	public double[][] getAdjacencyMatrixWeightsTP() {
		return adjacencyMatrixWeightsTP;
	}
	public ArrayList<PlaceProcessing> getPositionsPlaces() {
		return positionsPlaces;
	}
	public ArrayList<TransitionProcessing> getPositionTransitions() {
		return positionTransitions;
	}	
		public Metabolite getMetabolite(String id) {
		return this.metabolicNetworkModel.getMetabolite(id);
	}
	
	public Reaction getReaction(String id) {
		return this.metabolicNetworkModel.getReaction(id);
	}
	
	public Map<String, List<Reaction>> getReactionMetaboliteIsProduct(String metaboliteKeyName){
		return this.metabolicNetworkModel.getReactionOfMetabolite(metaboliteKeyName);
	}
	
}