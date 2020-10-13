package metapenta.processing.petrinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import metapenta.model.GeneProduct;
import metapenta.model.MetabolicNetwork;
import metapenta.model.Metabolite;
import metapenta.model.Reaction;
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
	public int[][] adjacencyMatrixWeightsTP;
	/**
	 * Represents the weights between places to transition
	 */
	public int[][] adjacencyMatrixWeightsPT;

	private int x_places = 100;
	private int y_places = 50;


	private int x_transitions = 300;
	private int y_transitions = 50;




	public Translator(MetabolicNetwork metabolicNetworkModel) {
		this.metabolicNetworkModel = metabolicNetworkModel;		
		translate();
	}	
	
	private void translate() {
		Map<Integer, Transition<Metabolite, Reaction>> transitions = metabolicNetworkModel.getTransitions();
		Map<Integer, Place<Metabolite, Reaction>> places = metabolicNetworkModel.getPlacesbyNumber();
		Set<Integer> keysTransitions = metabolicNetworkModel.getTransitions().keySet();
		Set<Integer> keysPlaces = places.keySet();

		double nodesPerColum = Math.max(Math.ceil(Math.sqrt(keysTransitions.size())), Math.ceil(Math.sqrt(keysPlaces.size())));

		int counter = 0;
		for (Integer key : keysPlaces) {
			Place< Metabolite, Reaction> place = places.get(key);
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
			Transition<Metabolite, Reaction> transition = transitions.get(key);		
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
		this.adjacencyMatrixWeightsTP = new int[positionTransitions.size()][positionsPlaces.size()];
		this.adjacencyMatrixWeightsPT = new int[positionTransitions.size()][positionsPlaces.size()];
		
		for (Integer key : keysTransitions) {
			Transition< Metabolite, Reaction> transition = transitions.get(key);
			List<Edge<Place< Metabolite, Reaction>>> outPlaces = transition.getOutPlaces();
			
			for (int i = 0; i < outPlaces.size(); i++) {
				int number = adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1];
				if(number==0) {
					this.adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1]=1;
				}
				else if(number==2) {
					this.adjacencyMatrix[transition.getNumber()-1][outPlaces.get(i).getObject().getMetaboliteNumber()-1]=3;
				}	
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
			}			
		}
	}	
	public int[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	public int[][] getAdjacencyMatrixWeightsPT() {
		return adjacencyMatrixWeightsPT;
	}
	public int[][] getAdjacencyMatrixWeightsTP() {
		return adjacencyMatrixWeightsTP;
	}
	public ArrayList<PlaceProcessing> getPositionsPlaces() {
		return positionsPlaces;
	}
	public ArrayList<TransitionProcessing> getPositionTransitions() {
		return positionTransitions;
	}	
	public void calculateSinks() {
		List<Metabolite> sinks= metabolicNetworkModel.findSinks();		
		for (int i = 0; i < sinks.size(); i++) {			
			Map<String, Place<Metabolite, Reaction>> places = metabolicNetworkModel.getPlaces();
			Place<Metabolite, Reaction> place = places.get(sinks.get(i).getId());
			PlaceProcessing placeProcessing = positionsPlaces.get(place.getMetaboliteNumber()-1);
			placeProcessing.setColor_place(Constants.ORANGE);
		}	
	}
	
	public void calculateSources() {
		List<Metabolite> sinks= metabolicNetworkModel.findSources();		
		for (int i = 0; i < sinks.size(); i++) {			
			Map<String, Place<Metabolite, Reaction>> places = metabolicNetworkModel.getPlaces();
			Place<Metabolite, Reaction> place = places.get(sinks.get(i).getId());
			PlaceProcessing placeProcessing = positionsPlaces.get(place.getMetaboliteNumber()-1);
			placeProcessing.setColor_place(Constants.GREEN);
		}	
	}
	
	public void restoreSources() {
		List<Metabolite> sinks= metabolicNetworkModel.findSources();
		for (int i = 0; i < sinks.size(); i++) {			
			Map<String, Place<Metabolite, Reaction>> places = metabolicNetworkModel.getPlaces();
			Place<Metabolite, Reaction> place = places.get(sinks.get(i).getId());
			PlaceProcessing placeProcessing = positionsPlaces.get(place.getMetaboliteNumber()-1);
			placeProcessing.setColor_place(Constants.PURPLE);
		}
	}
	
	public void restoreSinks() {
		List<Metabolite> sinks= metabolicNetworkModel.findSinks();		
		for (int i = 0; i < sinks.size(); i++) {			
			Map<String, Place<Metabolite, Reaction>> places = metabolicNetworkModel.getPlaces();
			Place<Metabolite, Reaction> place = places.get(sinks.get(i).getId());
			PlaceProcessing placeProcessing = positionsPlaces.get(place.getMetaboliteNumber()-1);
			placeProcessing.setColor_place(Constants.PURPLE);
		}	
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