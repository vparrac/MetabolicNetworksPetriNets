package metapenta.model;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import metapenta.model.dto.GeneProductReactionsDTO;
import metapenta.model.errors.GeneProductDoesNotExitsException;
import metapenta.petrinet.Edge;
import metapenta.petrinet.Place;
import metapenta.petrinet.Transition;

/**
 * Represents a metabolic network of reactions on metabolites
 * @author Jorge Duitama
 */
public class MetabolicNetwork {
	private Map<String,GeneProduct> geneProducts = new TreeMap();
	private String name;

	/**
	 * Metabolites of  the metabolic Network
	 */
	private Map<String,Metabolite> metabolites = new TreeMap();
	private Set<String> compartments = new TreeSet<String>();
	private Map<String,Reaction> reactions = new TreeMap();

	//------------------------------------------------
	//.----------------Petri Net----------------------
	//------------------------------------------------

	/**
	 * A map of Integer to transition. The map represents the Transitions of Petri net 
	 */
	private  Map<Integer, Transition<Metabolite, Reaction>> transitions = new TreeMap<Integer, Transition<Metabolite,Reaction>>();

	/**
	 * A map of a String (that represents the id of the metabolite) to a Integer.
	 * This map represents the places of Petri net
	 */
	private  Map<String,Place<Metabolite, Reaction>> places = new TreeMap<String, Place<Metabolite,Reaction>>();

	private int[][] graph;

	private  Map<Integer,Place<Metabolite, Reaction>> placesbyNumber = new TreeMap<Integer, Place<Metabolite,Reaction>>();

	public void setName(String name) {
		this.name = name;
	}
	public static final int INFINITE=100000;

	public static final String COMMA=",";

	public void addGeneProduct(GeneProduct product) {
		geneProducts.put(product.getId(), product);
	}

	public void addMetabolite(Metabolite metabolite) {
		metabolites.put(metabolite.getId(), metabolite);
		compartments.add(metabolite.getCompartment());
	}

	public void addReaction(Reaction r) {
		reactions.put(r.getId(),r);
	}

	public Metabolite getMetabolite (String id) {
		return metabolites.get(id);
	}


	public Reaction getReaction(String id) {
		return reactions.get(id);
	}


	public  Map<String,Metabolite> getMetabolites(){
		return metabolites;
	}

	public  Map<String,Reaction> getReactions(){
		return reactions;
	}

	
	public MetabolicNetwork(Map<String,Metabolite> metabolites, Map<String,Reaction> reactions) {
		this.metabolites = metabolites;
		this.reactions = reactions;
	}

	public MetabolicNetwork() {

	}

	public List<Reaction> getReactionsUnbalanced() {
		List<Reaction> reactionsUnBalanced = new ArrayList<>();

		Set<String> keys=reactions.keySet();
		for (String key : keys) {
			Reaction reaction = reactions.get(key);
			boolean isBalance = reaction.getIsBalanced();

			if(!isBalance) {
				reactionsUnBalanced.add(reaction);
			}
		}
		return reactionsUnBalanced;
	}

	public Map<Reaction, Map<String, String>> reactionsUnbalancedReason(List<Reaction> reactionsUnbalanced){

		Map<Reaction, Map<String, String>> reactionsUnbalancedReason = new HashMap<>();

		for (Reaction reaction: reactionsUnbalanced) {

			Map<String, String> reason =reaction.casesNoBalanced();

			reactionsUnbalancedReason.put(reaction, reason);
		}

		return reactionsUnbalancedReason;


	}

	public void testBalanceo() {
		List<Reaction> reactions = getReactionsUnbalanced();
		for (Reaction r : reactions) {
			//Reaction r = reactions.get(1);
			r.getDifference();
			List<ReactionComponent> reactants= r.getReactants();
			List<ReactionComponent> products= r.getProducts();
			System.out.println("REACTANTES: ");
			for(ReactionComponent reactant: reactants) {
				//reactant.getDetailFormula();
				Map<String, Integer> formula = reactant.getMetabolite().getChemicalFormula().getElements();
				System.out.println("stoichimoetry: " + reactant.getStoichiometry());
				for (Map.Entry<String, Integer> entry : formula.entrySet()) {
					System.out.println(entry.getKey() + ": " + entry.getValue());
				}
			}
			System.out.println("PRODUCTOS: ");
			for(ReactionComponent product: products) {

				Map<String, Integer> formula = product.getMetabolite().getChemicalFormula().getElements();
				System.out.println("stoichimoetry: " + product.getStoichiometry());
				for (Map.Entry<String, Integer> entry : formula.entrySet()) {
					System.out.println(entry.getKey() + ": " + entry.getValue());
				}

			}

		}
	}


	 public MetabolicNetwork(Map<String,Reaction> reactions) {
		int numberMetabolites=1,numberTransition=1;		
		placesbyNumber = new TreeMap<Integer, Place<Metabolite,Reaction>>();
		transitions= new TreeMap<Integer, Transition<Metabolite, Reaction>>();		
		places = new TreeMap<String,Place<Metabolite, Reaction>>();
		Set<String> keysReaction = reactions.keySet();					
		for (String key : keysReaction) {			
			Reaction rea = reactions.get(key);
			Transition< Metabolite, Reaction> transition = new Transition< Metabolite, Reaction>(numberTransition, rea);
			numberTransition++;			
			List<ReactionComponent> reactantsC=rea.getReactants();
			List<ReactionComponent> productsC=rea.getProducts();			
			for (ReactionComponent rc : reactantsC) {			
				Metabolite meta = rc.getMetabolite();							
				Place< Metabolite, Reaction> currentPlace = places.get(meta.getId());
				
				if(currentPlace==null) {					
					Place< Metabolite, Reaction> nm = new Place< Metabolite, Reaction>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					placesbyNumber.put(numberMetabolites, nm);
					numberMetabolites++;
					currentPlace = nm;
			}				
				
				
				Edge<Place< Metabolite, Reaction>> placeCurrentMetabolite= new Edge<Place< Metabolite, Reaction>>(rc.getStoichiometry(),currentPlace);
				Edge<Transition< Metabolite, Reaction>> edgeTransition= new Edge<Transition< Metabolite, Reaction>>(rc.getStoichiometry(), transition);								
				transition.addPlaceIn(placeCurrentMetabolite);
				currentPlace.addOutTransition(edgeTransition);
				
			}			
			for (ReactionComponent rc : productsC) {
				Metabolite meta = rc.getMetabolite();				
				Place< Metabolite, Reaction> currentPlace = places.get(meta.getId());
				if(currentPlace==null) {					
					Place< Metabolite, Reaction> nm = new Place< Metabolite, Reaction>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					placesbyNumber.put(numberMetabolites, nm);
					numberMetabolites++;	
					currentPlace = nm;
				}
				
				Edge<Place< Metabolite, Reaction>> placeCurrentMetabolite= new Edge<Place< Metabolite, Reaction>>(rc.getStoichiometry(),currentPlace);
				Edge<Transition< Metabolite, Reaction>> edgeTransition= new Edge<Transition< Metabolite, Reaction>>(rc.getStoichiometry(), transition);
				transition.addPlaceOut(placeCurrentMetabolite);
				currentPlace.addInTransition(edgeTransition);
			}			
			transitions.put(transition.getNumber(), transition);						
		}	
	}

	private List<Reaction> getReactionsCatalyzedBy(String geneProductName){
		List<Reaction> catalyzedReactions = new ArrayList();
		Set<String> keys = reactions.keySet();

		for (String key : keys) {
			Reaction reaction= reactions.get(key);
			List<GeneProduct> enzymes= reaction.getEnzymes();

			for (GeneProduct enzyme : enzymes) {				
				if(enzyme.getName().equals(geneProductName)) {
					catalyzedReactions.add(reaction);
					break;
				}
			}			
		}

		return catalyzedReactions;
	}


	public GeneProductReactionsDTO getGeneProductReactions(String geneProductId) throws GeneProductDoesNotExitsException {
		GeneProduct geneProduct = geneProducts.get(geneProductId);
		List<Reaction> reactions = getReactionsCatalyzedBy(geneProductId);

		return new GeneProductReactionsDTO(reactions, geneProduct);
	}

	public GeneProduct getGeneProduct(String geneProductId) throws GeneProductDoesNotExitsException {
		GeneProduct geneProduct = geneProducts.get(geneProductId);

		if (geneProduct == null){
			throw new GeneProductDoesNotExitsException();
		}

		return geneProduct;
	}

	/**
	 * Create the Petri Net that represent the metabolic network
	 */
	public void  makeNet() {	
		int numberMetabolites=1,numberTransition=1;
		placesbyNumber = new TreeMap<Integer, Place<Metabolite,Reaction>>();
		transitions= new TreeMap<Integer, Transition<Metabolite, Reaction>>();		
		places = new TreeMap<String,Place<Metabolite, Reaction>>();


		Set<String> keysReaction = reactions.keySet();					
		for (String key : keysReaction) {			
			Reaction rea = reactions.get(key);
			Transition< Metabolite, Reaction> transition = new Transition< Metabolite, Reaction>(numberTransition, rea);
			numberTransition++;			
			List<ReactionComponent> reactantsC=rea.getReactants();
			List<ReactionComponent> productsC=rea.getProducts();			
			for (ReactionComponent rc : reactantsC) {			
				Metabolite meta = rc.getMetabolite();							
				Place< Metabolite, Reaction> currentPlace = places.get(meta.getId());

				if(currentPlace==null) {					
					Place< Metabolite, Reaction> nm = new Place< Metabolite, Reaction>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					placesbyNumber.put(numberMetabolites, nm);
					numberMetabolites++;
					currentPlace = nm;
				}				


				Edge<Place< Metabolite, Reaction>> placeCurrentMetabolite= new Edge<Place< Metabolite, Reaction>>(rc.getStoichiometry(),currentPlace);
				Edge<Transition< Metabolite, Reaction>> edgeTransition= new Edge<Transition< Metabolite, Reaction>>(rc.getStoichiometry(), transition);								
				transition.addPlaceIn(placeCurrentMetabolite);
				currentPlace.addOutTransition(edgeTransition);

			}			
			for (ReactionComponent rc : productsC) {
				Metabolite meta = rc.getMetabolite();				
				Place< Metabolite, Reaction> currentPlace = places.get(meta.getId());
				if(currentPlace==null) {					
					Place< Metabolite, Reaction> nm = new Place< Metabolite, Reaction>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					placesbyNumber.put(numberMetabolites, nm);
					numberMetabolites++;	
					currentPlace = nm;
				}

				Edge<Place< Metabolite, Reaction>> placeCurrentMetabolite= new Edge<Place< Metabolite, Reaction>>(rc.getStoichiometry(),currentPlace);
				Edge<Transition< Metabolite, Reaction>> edgeTransition= new Edge<Transition< Metabolite, Reaction>>(rc.getStoichiometry(), transition);
				transition.addPlaceOut(placeCurrentMetabolite);
				currentPlace.addInTransition(edgeTransition);
			}			
			transitions.put(transition.getNumber(), transition);						
		}				
	}

	/**
	 * Find the transition that can be triggered 
	 * @param transitions the transitions to evaluate
	 * @param metabolitesVisited a matrix where the rows are the metabolites, the first column indicate if exists the metabolite 
	 * the second column is the reaction that allows to obtain the metabolite and the third column the distance of a initial reaction 
	 * @return
	 */
	public  ArrayList<Transition< Metabolite, Reaction>> transitionsThaCanBeTriggered(List<Edge<Transition<Metabolite, Reaction>>> transitions, int[][] metabolitesVisited){
		ArrayList<Transition< Metabolite, Reaction>> transitionsThaCanBeTriggered= new ArrayList<>();
		for (int j = 0; j < transitions.size(); j++) {
			List<Edge<Place< Metabolite, Reaction>>> inEdges = transitions.get(j).getObject().getInPlaces();
			//The greatest distance that is not infinite.
			boolean canBeTriggered=true;
			for (int k = 0; k <inEdges.size(); k++) {
				int number=places.get(inEdges.get(k).getObject().getObject().getId()).getMetaboliteNumber();				
				if(metabolitesVisited[number][2]==INFINITE) {
					canBeTriggered=false;
				}
			}
			if(canBeTriggered) {
				transitionsThaCanBeTriggered.add(transitions.get(j).getObject());
			}
			canBeTriggered=false;
		}
		return transitionsThaCanBeTriggered;
	}
	/**
	 * Find the max distance of a metabolite in a specific transition
	 * @param transition the transition to be evaluated
	 * @param metabolitesVisited a matrix where the rows are the metabolites, the first column indicate if exists the metabolite 
	 * the second column is the reaction that allows to obtain the metabolite and the third column the distance of a initial reaction
	 * @return the max distance of a metabolite in the transition
	 */
	public int findMaximunDistanceOfTheInletMetabolitesOfATransition(Transition< Metabolite, Reaction> transition,int[][] metabolitesVisited) {
		List<Edge<Place< Metabolite, Reaction>>> edgesIn=transition.getInPlaces();
		int max=-1;
		for (int i = 0; i < edgesIn.size(); i++) {			
			if(metabolitesVisited[places.get(edgesIn.get(i).getObject().getObject().getId()).getMetaboliteNumber()][2]>max) {
				max=metabolitesVisited[places.get(edgesIn.get(i).getObject().getObject().getId()).getMetaboliteNumber()][2];
			}
		}		
		return max;		
	}

	/**
	 * This method initializes the array metabolitesVisited the initial distance infinite 
	 * @param metabolitesVisited the array to initialize
	 * @param first a list with initial metabolites
	 */

	public void initialValuesOfShortestPath(int[][] metabolitesVisited,List<String> first) {
		for (int j= 0; j < metabolitesVisited.length; j++) {
			metabolitesVisited[j][2]=INFINITE;
		}
		for (int j = 0; j <first.size(); j++) {		
			System.out.println(places.get(first.get(j)).getMetaboliteNumber());
			metabolitesVisited[places.get(first.get(j)).getMetaboliteNumber()][2]=0; //Distance
			metabolitesVisited[places.get(first.get(j)).getMetaboliteNumber()][0]=1; //is there that metabolite?
			metabolitesVisited[places.get(first.get(j)).getMetaboliteNumber()][1]=-1; //The last transition, -1 if it is not assigned								
		}
	}


	/**
	 * 
	 * @param first Lista metabolitos iniciales
	 * @param last Met�bolito a producir
	 * @param fileName1 Metabolic Network In CSV
	 * @param fileName2 Reactions Graph In CSV
	 * @param fileName3 Methabolic Path
	 * @throws FileNotFoundException 
	 */
	public int[][] shortestPathByMetabolitesNumber(List<String> first, String last, String fileName1,String fileName2, String fileName3) throws FileNotFoundException {
		int[][] graph = null;		
		int[] reactionsVisited; 
		//Row 1. Moles Row 2. The reaction 3. The <<Distance>>	
		int[][] metabolitesVisited=new int[places.size()+1][3]; //Places.size()+1 for enumeration
		int[] pqea;		
		int distanceBestPath=INFINITE+1;		
		// Find for each metabolite the transitions that can be triggered				
		reactionsVisited= new int[transitions.size()+1]; 
		metabolitesVisited=new int[places.size()+1][3];			
		pqea= new int[places.size()+1];			
		//Initial values		
		initialValuesOfShortestPath(metabolitesVisited, first);
		PriorityQueue<MetabolitesP> pq = new PriorityQueue<>();	
		for (String i : first) {
			pq.add(new MetabolitesP(metabolites.get(i),0));
		}						
		while (!pq.isEmpty()) { 
			Metabolite mp = pq.poll().getMetabolite();			
			int n = places.get(mp.getId()).getMetaboliteNumber();
			if(mp.getId().equals(last)) {
				break;
			}			
			if(pqea[n]==0) {
				pqea[n]++;	
				List<Transition< Metabolite, Reaction>> transitions= transitionsThaCanBeTriggered(places.get(mp.getId()).getOutTransitions(),metabolitesVisited);
				for (int j = 0; j < transitions.size(); j++) {
					reactionsVisited[transitions.get(j).getNumber()]=1;
					List<Edge<Place< Metabolite, Reaction>>> inEdgesOft = transitions.get(j).getOutPlaces();						
					//The max distance
					int maxValue= findMaximunDistanceOfTheInletMetabolitesOfATransition(transitions.get(j),metabolitesVisited)+1;
					//Update the distances
					for (int k = 0; k < inEdgesOft.size(); k++) {
						Metabolite meta = inEdgesOft.get(k).getObject().getObject();
						int number = places.get(meta.getId()).getMetaboliteNumber();
						if((maxValue)<metabolitesVisited[number][2]) {
							metabolitesVisited[number][2]=maxValue;
							metabolitesVisited[number][1]=transitions.get(j).getNumber();
						}							
						MetabolitesP mp1 = new MetabolitesP(meta,(int) metabolitesVisited[number][2]);
						pq.add(mp1);							
						metabolitesVisited[number][0]=1;								
					}
				}	
			}
		}
		//Makes the metabolic Pathway
		int numberLast =places.get(last).getMetaboliteNumber();
		if(metabolitesVisited[numberLast][2]<distanceBestPath) {
			graph=metabolicPathway(metabolitesVisited, last);	
			distanceBestPath=metabolitesVisited[numberLast][2];
		}			
		printMetabolicNetworkInCSV(graph, fileName1, first,last);
		printReactionsGraphInCSV(graph, fileName2);
		printCatalystOfMethabolicPath(graph, fileName3);
		return graph;
	}
	



	/**
	 * Wtrite
	 * @param metabolitesVisited
	 * @param last
	 * @return
	 */
	public int[][] metabolicPathway(int[][] metabolitesVisited, String last) {
		int[][] graph = new int [transitions.size()+1][transitions.size()+1];
		int[] transitionsVisited=  new int[this.transitions.size()+1];
		boolean isTheBegining=true;

		if(metabolitesVisited[places.get(last).getMetaboliteNumber()][1]==-1) {
			System.err.println("En los metabolitos de entrada ya est� el metabolito final");
		}

		else if(metabolitesVisited[places.get(last).getMetaboliteNumber()][2]==INFINITE) {
			System.err.println("No es posible llegar al metabolito final");
		}

		else {
			ArrayDeque<Integer> transitionss = new ArrayDeque<>();
			Transition< Metabolite, Reaction> currentTransition=transitions.get(metabolitesVisited[places.get(last).getMetaboliteNumber()][1]);
			do {				
				if(transitionsVisited[currentTransition.getNumber()]!=0) {	
					if(!transitionss.isEmpty()) {
						currentTransition=transitions.get(transitionss.poll());}
					else {
						currentTransition=null;
					}
					continue;
				}

				transitionsVisited[currentTransition.getNumber()]++;
				List<Edge<Place< Metabolite, Reaction>>> edgesIn= currentTransition.getInPlaces();		
				int nInitialMetabolites=0;
				for (int i = 0; i < edgesIn.size(); i++) {
					int meta = places.get(edgesIn.get(i).getObject().getObject().getId()).getMetaboliteNumber();
					int transition =metabolitesVisited[meta][1];
					if(transition!=-1) {
						graph[transition][currentTransition.getNumber()]=1;
						transitionss.add(transition);

					}
					else {
						nInitialMetabolites++;
					}
				}
				if(nInitialMetabolites==edgesIn.size()&&isTheBegining) {
					System.err.println("S�lo es necesaria una transici�n para llegar al metabolito \n"+currentTransition.getObject().getName());
					break;
				}				
				isTheBegining=false;
				if(!transitionss.isEmpty()) {
					currentTransition=transitions.get(transitionss.poll());
				}				
			}  while(currentTransition!=null);
		}
		return graph;
	}


	public void printReactionsGraphInCSV(int[][] graph, String fileName) throws FileNotFoundException{		
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("source,target,interaction,directed,symbol,value");
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					if(graph[i][j]!=0) {
						String nameTransition1= transitions.get(i).getObject().getId();
						String nameTransition2= transitions.get(j).getObject().getId();						
						out.println(nameTransition1+COMMA+nameTransition2+",PP,TRUE,abc"+i+j+",1.234");						
					}						
				}

			}
		}		
	}




	/**
	 * Buscar varias rutas
	 * jduitama
	 * @param graph
	 * @param fileName
	 * @throws Exception
	 */
	private void printCatalystOfMethabolicPath(int[][] graph, String fileName) throws FileNotFoundException{	
		Set<GeneProduct> allCatalyst = new TreeSet<GeneProduct>();
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("Catalys of metabolic pathway");
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					if(graph[i][j]!=0) {
						List<GeneProduct> catalysReaction1= transitions.get(i).getObject().getEnzymes();
						List<GeneProduct> catalysReaction2= transitions.get(j).getObject().getEnzymes();
						allCatalyst.addAll(catalysReaction1);
						allCatalyst.addAll(catalysReaction2);
					}						
				}
			}		
		}		
	}


	/**
	 * Print the graph entered by parameter in a CSV
	 * @param graph The graph to be printed, the graph represents the adyacent matrix of reactions
	 * @param fileName the path of CSV
	 * @param first The ids of initial metabolites
	 * @param last the final metabolite
	 * @throws FileNotFoundException 
	 * @throws Exception in errors of I/O
	 */
	private void printMetabolicNetworkInCSV(int[][] graph, String fileName, List<String> first, String last) throws FileNotFoundException{
		int[] metabolitesVisited= new int[places.size()];
		int[] transitionsVisited= new int[transitions.size()];
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("source,target,interaction,directed,symbol,value");
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					if(graph[i][j]!=0) {
						printATransitionInCSV(transitions.get(i),out,i,j,metabolitesVisited,transitionsVisited,first,last);
						printATransitionInCSV(transitions.get(j),out,i,j,metabolitesVisited,transitionsVisited,first,last);			
					}			
				}
			}
		}
	}


	/**
	 * Method that print the transition in the CSV entered by parameter 
	 * @param t the transiton to be printed
	 * @param out the Stream of the CSV
	 * @param i A identifier
	 * @param j A identifier
	 * @param metabolitesVisited a matrix where the rows are the metabolites, the first column indicate if exists the metabolite 
	 * the second column is the reaction that allows to obtain the metabolite and the third column the distance of a initial reaction
	 * @param transitionsVisited the transition that were printed 
	 * @param first The initial ids of metabolites
	 * @param last The final metabolite
	 */
	private void printATransitionInCSV(Transition< Metabolite, Reaction> t, PrintStream out,int i, int j,int[] metabolitesVisited,int[] transitionsVisited, List<String> first, String last) {
		List<Edge<Place< Metabolite, Reaction>>> metaInTransition = t.getInPlaces();
		if(transitionsVisited[t.getNumber()]==0) {
			transitionsVisited[t.getNumber()]++;
			for (Edge<Place< Metabolite, Reaction>> edge : metaInTransition) {
				String nameOfMeta = edge.getObject().getObject().getId();
				if(first.contains(nameOfMeta)) {
					nameOfMeta="TT"+nameOfMeta;
				}
				else if(last.equalsIgnoreCase(nameOfMeta)) {
					nameOfMeta="OO"+nameOfMeta;
				}
				out.println(nameOfMeta+COMMA+t.getObject().getId()+",PP,TRUE,abc"+i+j+",1.234");
			}
			metaInTransition = t.getOutPlaces();		
			for (Edge<Place< Metabolite, Reaction>> edge : metaInTransition) {
				String nameOfMeta = edge.getObject().getObject().getId();
				if(first.contains(nameOfMeta)) {
					nameOfMeta="TT"+nameOfMeta;
				}
				else if(last.equalsIgnoreCase(nameOfMeta)) {
					nameOfMeta="OO"+nameOfMeta;
				}
				out.println(t.getObject().getId()+COMMA+nameOfMeta+",PP,TRUE,abc"+i+j+",1.234");
			}	
		}
	}	


	//-------------------------------------------------
	//---------------Inner auxiliar classes------------
	//-------------------------------------------------


	public List<String> getReactionIds(){
		List<String> reactionIds = new ArrayList();
		Set<String> keys = reactions.keySet();
		reactionIds.addAll(keys);

		return reactionIds;
	}

	public MetabolicNetwork interception(MetabolicNetwork metabolicNetwork){
		Map<String, Metabolite> metabolites = interceptionMetabolites(metabolicNetwork);
		Map<String, Reaction> reactions = interceptionReactions(metabolicNetwork);

		return new MetabolicNetwork(metabolites, reactions);
	}

	private Map<String, Metabolite> interceptionMetabolites(MetabolicNetwork metabolicNetwork){
		Map<String, Metabolite> metabolites = new HashMap<>();
		Set<String> metabolitesKeys = metabolicNetwork.getMetabolites().keySet();
		for (String metaboliteID : metabolitesKeys) {
			Metabolite metabolite = this.metabolites.get(metaboliteID);
			if( metabolite != null ) {
				metabolites.put(metaboliteID, metabolite);
			}
		}		
		return metabolites;
	}

	private Map<String, Reaction> interceptionReactions(MetabolicNetwork metabolicNetwork){
		Map<String, Reaction> reactions = new HashMap<>();
		Set<String> reactionIds = metabolicNetwork.getReactions().keySet();
		for (String reactionId : reactionIds) {
			Reaction reaction = reactions.get(reactionId);
			if( reaction!=null ) {
				reactions.put(reactionId, reaction);
			}
		}		
		return reactions;
	}


	public GeneProduct[] getGeneProductsAsList() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Metabolite> getMetabolitesAsList() {
		return new ArrayList<Metabolite>(metabolites.values());
	}


	public List<Reaction> getReactionsAsList () {
		return new ArrayList<Reaction>(reactions.values());
	}

}