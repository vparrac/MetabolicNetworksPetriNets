package metapenta.model;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import metapenta.petrinet.Edge;
import metapenta.petrinet.Place;
import metapenta.petrinet.Transition;
/**
 * Represents a metabolic network of reactions on metabolites
 * @author Jorge Duitama
 */
public class MetabolicNetwork {
	private Map<String,GeneProduct> geneProducts = new TreeMap<String,GeneProduct>();
	/**
	 * Metabolites of  the metabolic Network
	 */
	private static Map<String,Metabolite> metabolites = new TreeMap<String,Metabolite>();
	private Set<String> compartments = new TreeSet<String>();
	private static Map<String,Reaction> reactions = new TreeMap<String,Reaction>();

	//Petri net
	/**
	 * A map of Integer to transition. The map represents the Transitions of Petri net 
	 */
	private  Map<Integer, Transition<GeneProduct, Metabolite>> transitions;

	/**
	 * A map of a String (that represents the id of the metabolite) to a Integer.
	 * This map represents the places of Petri net
	 */
	private  Map<String,Place<Metabolite,GeneProduct>> places;

	/**
	 * Represents the infinite distance between 2 metabolites
	 */
	public static final int INFINITE=100000;
	/**
	 * String with the value of comma (,) to generate the csv
	 */
	public static final String COMMA=",";
	/**F
	 * Adds a new gene product that can catalyze reactions
	 * @param product New gene product
	 */
	public void addGeneProduct(GeneProduct product) {
		geneProducts.put(product.getId(), product);
	}
	/**
	 * Adds a new metabolite. If a metabolite with the given name is already added, it 
	 * @param metabolite New metabolite
	 */
	public void addMetabolite(Metabolite metabolite) {
		metabolites.put(metabolite.getId(), metabolite);
		compartments.add(metabolite.getCompartment());
	}
	/**
	 * Adds a new reaction
	 * @param r New reaction between metabolites
	 */
	public void addReaction(Reaction r) {
		reactions.put(r.getId(),r);
	}
	/**
	 * Returns the gene product with the given id
	 * @param id of the product to search
	 * @return GeneProduct with the given id
	 */
	public GeneProduct getGeneProduct (String id) {
		return geneProducts.get(id);
	}
	/**
	 * Returns the metabolite with the given id
	 * @param id of the metabolite to search
	 * @return Metabolite with the given id
	 */
	public Metabolite getMetabolite (String id) {
		return metabolites.get(id);
	}
	/**
	 * @return List of metabolites in the network
	 */
	public List<Metabolite> getMetabolitesAsList() {
		return new ArrayList<Metabolite>(metabolites.values());
	}
	/**
	 * @return List of reactions in the network
	 */
	public List<Reaction> getReactionsAsList () {
		return new ArrayList<Reaction>(reactions.values());
	}

	//-------------------------------------------------------------------
	//-----------------------Metabolic Network --------------------------
	//---------------------------As graph--------------------------------

	/**
	 * Method that find the reactions where a metabolite  
	 * @param metaboliteKeyName the id of the metabolite
	 * @return A map of the where reaction where metabolite is a substrate and where the metabolite is a product 
	 */
	public Map<String,List<Reaction>> getReactionOfMetabolite(String metaboliteKeyName) {
		Map<String,List<Reaction>> reaction= new TreeMap<>();	
		List<Reaction> rsubstrates= new ArrayList<>();
		List<Reaction> rproducts=new ArrayList<>();
		Set<String> keys=reactions.keySet();		
		for (String key : keys) {
			Reaction rea= reactions.get(key);
			List<ReactionComponent> substrates= rea.getReactants();
			List<ReactionComponent> products= rea.getProducts();			
			for (int i = 0; i < substrates.size(); i++) {
				if(substrates.get(i).getMetabolite().getId().equals(metaboliteKeyName)) {
					rsubstrates.add(rea);					
				}
			}
			for (int i = 0; i < products.size(); i++) {
				if(products.get(i).getMetabolite().getId().equals(metaboliteKeyName)) {
					rproducts.add(rea);					
				}
			}
		}		
		reaction.put("Substrates", rsubstrates);
		reaction.put("Products", rproducts);
		return reaction;
	}
	/**
	 * Find the reactions where the enzyme is the catalyst
	 * @param enzymeName the id of Enzume
	 * @return a mapa whith the reactions
	 */

	public List<Reaction> getReactionsCatalyzedBy(String enzymeName){
		List<Reaction> cata= new ArrayList<Reaction>();
		Set<String> keys=reactions.keySet();		
		for (String key : keys) {
			Reaction rea= reactions.get(key);
			List<GeneProduct> enzymes=rea.getEnzymes();
			for (GeneProduct enzyme : enzymes) {				
				if(enzyme.getName().equals(enzymeName)) {
					cata.add(rea);
					break;
				}
			}			
		}
		return cata;
	}

	//-------------------------------------------------------------------
	//-----------------------Metabolic Network --------------------------
	//--------------------------As Petri Net-----------------------------


	/**
	 * Create the Petri Net that represent the metabolic network
	 */
	public void  makeNet() {	
		int numberMetabolites=1,numberTransition=1;
		transitions= new TreeMap<Integer, Transition<GeneProduct, Metabolite>>();		
		places = new TreeMap<String,Place<Metabolite,GeneProduct>>();
		Set<String> keysReaction=reactions.keySet();			
		for (String key : keysReaction) {	
			Reaction rea = reactions.get(key);
			Transition<GeneProduct, Metabolite> transition = new Transition<GeneProduct, Metabolite>(rea.getEnzymes(),numberTransition,rea.getName(),rea.getId());
			numberTransition++;
			List<ReactionComponent> reactantsC=rea.getReactants();
			List<ReactionComponent> productsC=rea.getProducts();			
			for (ReactionComponent rc : reactantsC) {			
				Metabolite meta = rc.getMetabolite();							
				Place<Metabolite, GeneProduct> currentPlace = places.get(meta.getId());
				if(currentPlace==null) {					
					Place<Metabolite, GeneProduct> nm = new Place<Metabolite, GeneProduct>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					numberMetabolites++;
					currentPlace = nm;
				}								
				Edge<Metabolite> edge= new Edge<Metabolite>(rc.getStoichiometry(),meta);		
				currentPlace.addEdgeOut(edge);						
				transition.addEdgeIn(edge);
				currentPlace.addTransition(transition);
			}			
			for (ReactionComponent rc : productsC) {
				Metabolite meta = rc.getMetabolite();				
				Place<Metabolite, GeneProduct> currentPlace = places.get(meta.getId());
				if(currentPlace==null) {					
					Place<Metabolite, GeneProduct> nm = new Place<Metabolite, GeneProduct>(meta, numberMetabolites);
					places.put(meta.getId(),nm);
					numberMetabolites++;	
					currentPlace = nm;
				}				
				Edge<Metabolite> edge= new Edge<Metabolite>(rc.getStoichiometry(),meta);				
				currentPlace.addEdgeIn(edge);
				transition.addEdgeOut(edge);
				currentPlace.addTransition(transition);
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
	public  ArrayList<Transition<GeneProduct, Metabolite>> transitionsThaCanBeTriggered(List<Transition<GeneProduct, Metabolite>> transitions, int[][] metabolitesVisited){
		ArrayList<Transition<GeneProduct, Metabolite>> transitionsThaCanBeTriggered= new ArrayList<>();
		for (int j = 0; j < transitions.size(); j++) {
			List<Edge<Metabolite>> inEdges = transitions.get(j).getIn();
			//The greatest distance that is not infinite.
			boolean canBeTriggered=true;
			for (int k = 0; k <inEdges.size(); k++) {
				int number=places.get(inEdges.get(k).getObject().getId()).getNumberMetabolite();				
				if(!((metabolitesVisited[number][0]==1)&&(metabolitesVisited[number][2]!=INFINITE))) {
					canBeTriggered=false;
				}
			}
			if(canBeTriggered) {
				transitionsThaCanBeTriggered.add(transitions.get(j));
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
	public int findMaximunDistanceOfTheInletMetabolitesOfATransition(Transition<GeneProduct, Metabolite> transition,int[][] metabolitesVisited) {
		List<Edge<Metabolite>> edgesIn=transition.getIn();
		int max=-1;
		for (int i = 0; i < edgesIn.size(); i++) {			
			if(metabolitesVisited[places.get(edgesIn.get(i).getObject().getId()).getNumberMetabolite()][2]>max) {
				max=metabolitesVisited[places.get(edgesIn.get(i).getObject().getId()).getNumberMetabolite()][2];
			}
		}		
		return max;		
	}



	/**
	 * Find all paths between a List of initial metabolties to a target metabolite
	 * @param first List o initial metabolites
	 * @param last target metabolite
	 * @param file file Where the paths will be printed
	 * @throws FileNotFoundException 
	 */
	public void getAllPaths(List<String> first, String last, String file) throws FileNotFoundException {
		int[] metabolitesVisited=new int[places.size()+1];
		for (int j = 0; j <first.size(); j++) {					
			metabolitesVisited[places.get(first.get(j)).getNumberMetabolite()]=1;														
		}
		ArrayList<Metabolite> first_metabolites= new ArrayList<Metabolite>();
		String pathsString = "";		
		for (String m : first) {
			ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> path = new ArrayList<Integer>();
			Metabolite meta = places.get(m).getObject();	
			first_metabolites.add(meta);
			ArrayList<Metabolite> ancestors = new ArrayList<Metabolite>();
			searchPaths(meta, metabolitesVisited, path, paths, last,ancestors);
			ArrayList<ArrayList<Integer>> p_paths = new ArrayList<ArrayList<Integer>>();
			for (ArrayList<Integer> p : paths) {
				List<Edge<Metabolite>> i_reaction= transitions.get(p.get(0)).getIn();
				boolean allAtBegining=true;
				for (Edge<Metabolite> i : i_reaction) {
					Metabolite mi = i.getObject();
					if(!first_metabolites.contains(mi)) {
						allAtBegining=false;
						break;
					}
				}
				if(allAtBegining) {
					if(!p_paths.contains(p)) {
						p_paths.add(p);
						String pv="Path: ";				
						for (int i = 0; i < p.size(); i++) {
							String r_s= transitions.get(p.get(0)).getName();
							pv+=(i==p.size()-1)?r_s+"\n":r_s+",";					
						}						
						pathsString+=pv;
					}
				}
			}
		}
		try (PrintStream out = new PrintStream(file)) {
			out.println(pathsString);
		}
	}

	@SuppressWarnings("unchecked")
	private void searchPaths(Metabolite m,int[] metabolitesVisited, ArrayList<Integer> path, ArrayList<ArrayList<Integer>> paths, String target, ArrayList<Metabolite> ancestors) {	
		if(ancestors.contains(m)) {
			return;
		}
		ancestors.add(m);
		int[] previousState = Arrays.copyOf(metabolitesVisited, metabolitesVisited.length);
		List<Transition<GeneProduct, Metabolite>> transitions= transitionsThaCanBeTriggeredAllPaths(places.get(m.getId()).getTransitions(),metabolitesVisited);
		if(transitions.size()==0||m.getId().equals(target)) {			
			ArrayList<Integer> path_f = (ArrayList<Integer>) path.clone();
			paths.add(path_f);
			return;
		}
		for (Transition<GeneProduct, Metabolite> transition : transitions) {			
			List<Edge<Metabolite>> out=transition.getOut();
			for (Edge<Metabolite> edge : out) {
				Metabolite mp = edge.getObject();
				int number = places.get(mp.getId()).getNumberMetabolite();					
				metabolitesVisited[number]=1;
			}			
		}
		for (Transition<GeneProduct, Metabolite> transition : transitions) {	
			path.add(transition.getNumber());
			List<Edge<Metabolite>> out=transition.getOut();
			for (Edge<Metabolite> edge : out) {
				Metabolite mp = edge.getObject();
				searchPaths(mp, metabolitesVisited, path, paths, target, ancestors);
			}
			path.remove(path.size()-1);
		}		

		ancestors.remove(m);
		metabolitesVisited= previousState;		
	}


	public List<Transition<GeneProduct, Metabolite>> transitionsThaCanBeTriggeredAllPaths(List<Transition<GeneProduct,Metabolite>> transitions, int[] metabolitesVisited){
		ArrayList<Transition<GeneProduct, Metabolite>> transitionsThaCanBeTriggered= new ArrayList<>();
		for (int j = 0; j < transitions.size(); j++) {
			List<Edge<Metabolite>> inEdges = transitions.get(j).getIn();		
			boolean canBeTriggered=true;
			for (int k = 0; k <inEdges.size(); k++) {
				int number=places.get(inEdges.get(k).getObject().getId()).getNumberMetabolite();				
				if(metabolitesVisited[number]!=1) {
					canBeTriggered=false;
					break;
				}
			}
			if(canBeTriggered) {
				transitionsThaCanBeTriggered.add(transitions.get(j));
			}
			canBeTriggered=false;
		}
		return transitionsThaCanBeTriggered;		
	}


	/**
	 * This method initializes the array metabolitesVisited the initial distance infinite 
	 * and 0 for the initial metabolites, the function returns a matrix of number of metabolites x
	 * 3, where the first column represents the distance from the source to the target metabolite, 
	 * the second column represents the absence/presence of metabolite (1 if the metabolite exists and
	 * 0 if no) and the 3rd column represents the transitions that creates the matabolite, if the metabolite
	 * is part of initial metabolites, the default value is -1  
	 * @param metabolitesVisited the array to initialize
	 * @param first a list with initial metabolites
	 */

	public void initialValuesOfShortestPath(int[][] metabolitesVisited,List<String> first) {
		for (int j= 0; j < metabolitesVisited.length; j++) {
			metabolitesVisited[j][2]=INFINITE;
		}
		for (int j = 0; j <first.size(); j++) {					
			metabolitesVisited[places.get(first.get(j)).getNumberMetabolite()][0]=0; //Distance
			metabolitesVisited[places.get(first.get(j)).getNumberMetabolite()][0]=1; //is there that metabolite?
			metabolitesVisited[places.get(first.get(j)).getNumberMetabolite()][1]=-1; //The last transition, -1 if it is not assigned								
		}
	}


	public int[][] shortestPathByCatalystsNumber(List<String> first, String last, String fileName1,String fileName2, String fileName3) throws Exception{
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
			int n = places.get(mp.getId()).getNumberMetabolite();
			if(mp.getId().equals(last)) {
				break;
			}			
			if(pqea[n]==0) {
				pqea[n]++;	

				List<Transition<GeneProduct, Metabolite>> transitions= transitionsThaCanBeTriggered(places.get(mp.getId()).getTransitions(),metabolitesVisited);
				for (int j = 0; j < transitions.size(); j++) {
					reactionsVisited[transitions.get(j).getNumber()]=1;
					List <Edge<Metabolite>> inEdgesOft = transitions.get(j).getOut();						
					//The max previous distance
					int maxValue= findMaximunDistanceOfTheInletMetabolitesOfATransition(transitions.get(j),metabolitesVisited)+transitions.get(j).getGeneProduct().size();										
					//Update the distances
					for (int k = 0; k < inEdgesOft.size(); k++) {
						Metabolite meta = inEdgesOft.get(k).getObject();
						int number = places.get(meta.getId()).getNumberMetabolite();
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
		int numberLast =places.get(last).getNumberMetabolite();
		if(metabolitesVisited[numberLast][2]<distanceBestPath) {
			graph=metabolicPathway(metabolitesVisited, last);	
			distanceBestPath=metabolitesVisited[numberLast][2];
		}			
		printMetabolicNetworkInCSV(graph,fileName1,first,last);
		printReactionsGraphInCSV(graph,fileName2);
		printCatalystOfMethabolicPath(graph, fileName3);
		return graph;
	}	

	/**
	 * 
	 * @param first Lista metabolitos iniciales
	 * @param last Metábolito a producir
	 * @param fileName1 Metabolic Network In CSV
	 * @param fileName2 Reactions Graph In CSV
	 * @param fileName3 Methabolic Path
	 * @throws Exception
	 */
	public int[][] shortestPathByMetabolitesNumber(List<String> first, String last, String fileName1,String fileName2, String fileName3) throws Exception{
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
			int n = places.get(mp.getId()).getNumberMetabolite();
			if(mp.getId().equals(last)) {
				break;
			}			
			if(pqea[n]==0) {
				pqea[n]++;		
				List<Transition<GeneProduct, Metabolite>> transitions= transitionsThaCanBeTriggered(places.get(mp.getId()).getTransitions(),metabolitesVisited);
				for (int j = 0; j < transitions.size(); j++) {
					reactionsVisited[transitions.get(j).getNumber()]=1;
					List <Edge<Metabolite>> inEdgesOft = transitions.get(j).getOut();						
					//The max distance
					int maxValue= findMaximunDistanceOfTheInletMetabolitesOfATransition(transitions.get(j),metabolitesVisited)+1;
					//Update the distances
					for (int k = 0; k < inEdgesOft.size(); k++) {
						Metabolite meta = inEdgesOft.get(k).getObject();
						int number = places.get(meta.getId()).getNumberMetabolite();
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
		int numberLast =places.get(last).getNumberMetabolite();
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
	 * Print the adjacency matrix of a graph 
	 * @param nameOfFile the path of the file
	 * @param graph the graph 
	 * @throws Exception in errors of I/O
	 */

	public void writeGraph(String nameOfFile,int[][] graph) throws Exception {
		try (PrintStream out = new PrintStream(nameOfFile)) {
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					out.print(graph[i][j]);
				}
				out.print("\n");
			}
		}
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

		if(metabolitesVisited[places.get(last).getNumberMetabolite()][1]==-1) {
			System.err.println("En los metabolitos de entrada ya está el metabolito final");
		}

		else if(metabolitesVisited[places.get(last).getNumberMetabolite()][2]==INFINITE) {
			System.err.println("No es posible llegar al metabolito final");
		}

		else {
			ArrayDeque<Integer> transitionss = new ArrayDeque<>();
			Transition<GeneProduct, Metabolite> currentTransition=transitions.get(metabolitesVisited[places.get(last).getNumberMetabolite()][1]);
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
				List<Edge<Metabolite>> edgesIn= currentTransition.getIn();		
				int nInitialMetabolites=0;
				for (int i = 0; i < edgesIn.size(); i++) {
					int meta = places.get(edgesIn.get(i).getObject().getId()).getNumberMetabolite();
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
					System.err.println("Sólo es necesaria una transición para llegar al metabolito \n"+currentTransition.getName());
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

	public void printReactionsGraphInCSV(int[][] graph, String fileName) throws Exception{		
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("source,target,interaction,directed,symbol,value");
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					if(graph[i][j]!=0) {
						String nameTransition1= transitions.get(i).getId();
						String nameTransition2= transitions.get(j).getId();						
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
	private void printCatalystOfMethabolicPath(int[][] graph, String fileName) throws Exception{	
		Set<GeneProduct> allCatalyst = new TreeSet<GeneProduct>();
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("Catalys of metabolic pathway");
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
					if(graph[i][j]!=0) {
						List<GeneProduct> catalysReaction1= transitions.get(i).getGeneProduct();
						List<GeneProduct> catalysReaction2= transitions.get(j).getGeneProduct();
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
	 * @throws Exception in errors of I/O
	 */
	private void printMetabolicNetworkInCSV(int[][] graph, String fileName, List<String> first, String last) throws Exception{
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
	 * Print the graph entered by parameter in a CSV
	 * @param graph The graph to be printed, the graph represents the adyacent matrix of reactions
	 * @param fileName the path of CSV
	 * @param first The ids of initial metabolites
	 * @param last the final metabolite
	 * @throws Exception in errors of I/O
	 */
	public void printAllMetabolicNetworkInCSV(String fileName) throws IOException{
		try (PrintStream out = new PrintStream(fileName)) {
			out.println("source,target,interaction,directed,symbol,value");
			Set<Integer> keys= transitions.keySet();
			for (Integer integer : keys) {				
				printATransitionInCSVToAllNetwork(transitions.get(integer),out);
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
	private void printATransitionInCSVToAllNetwork(Transition<GeneProduct, Metabolite> t, PrintStream out) {
		List<Edge<Metabolite>> metaInTransition = t.getIn();		
		for (Edge<Metabolite> edge : metaInTransition) {
			String nameOfMeta = edge.getObject().getId();		
			out.println(nameOfMeta+COMMA+t.getId()+",PP,TRUE,abc"+",1.234");
		}
		metaInTransition = t.getOut();		
		for (Edge<Metabolite> edge : metaInTransition) {
			String nameOfMeta = edge.getObject().getId();		
			out.println(t.getId()+COMMA+nameOfMeta+",PP,TRUE,abc"+",1.234");
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
	private void printATransitionInCSV(Transition<GeneProduct, Metabolite> t, PrintStream out,int i, int j,int[] metabolitesVisited,int[] transitionsVisited, List<String> first, String last) {
		List<Edge<Metabolite>> metaInTransition = t.getIn();
		if(transitionsVisited[t.getNumber()]==0) {
			transitionsVisited[t.getNumber()]++;
			for (Edge<Metabolite> edge : metaInTransition) {
				String nameOfMeta = edge.getObject().getId();
				if(first.contains(nameOfMeta)) {
					nameOfMeta="TT"+nameOfMeta;
				}
				else if(last.equalsIgnoreCase(nameOfMeta)) {
					nameOfMeta="OO"+nameOfMeta;
				}
				out.println(nameOfMeta+COMMA+t.getId()+",PP,TRUE,abc"+i+j+",1.234");
			}
			metaInTransition = t.getOut();		
			for (Edge<Metabolite> edge : metaInTransition) {
				String nameOfMeta = edge.getObject().getId();
				if(first.contains(nameOfMeta)) {
					nameOfMeta="TT"+nameOfMeta;
				}
				else if(last.equalsIgnoreCase(nameOfMeta)) {
					nameOfMeta="OO"+nameOfMeta;
				}
				out.println(t.getId()+COMMA+nameOfMeta+",PP,TRUE,abc"+i+j+",1.234");
			}	
		}
	}	

	/**
	 * Find the sinks of the net
	 * @return List with sinks metabolites
	 */
	public List<Metabolite> findSinks() {	
		List<Metabolite> sinks= new ArrayList<Metabolite>();
		for (String key : metabolites.keySet()) {
			Metabolite m = metabolites.get(key);
			if(places.get(m.getId()).getEdgesOut().size()==0) {
				sinks.add(m);
			}
		}
		return sinks;
	}

	/**
	 * Find the sinks of the net
	 * @return List with sinks metabolites
	 */
	public List<Metabolite> findSources() {	
		List<Metabolite> sinks= new ArrayList<Metabolite>();
		for (String key : metabolites.keySet()) {
			Metabolite m = metabolites.get(key);
			if(places.get(m.getId()).getEdgesIn().size()==0) {
				sinks.add(m);
			}
		}
		return sinks;
	}


	//-------------------------------------------------
	//---------------Inner auxiliar classes------------
	//-------------------------------------------------


	/**
	 * Construct the correspondent sub-net of the comparment 
	 * @param comparment the comparment
	 */
	public void connectedComponents() {				
		int[] metabolitesVisited = new int[places.size()+1];
		int[] transitionsVisited = new int[transitions.size()+1];
		int conectedComponentId =1;
		int tiempo=0;
	
		Set<String> keys = places.keySet();
		
		for(String node: keys) {
			if(metabolitesVisited[places.get(node).getNumberMetabolite()]==0) {
				visitNode(metabolitesVisited, transitionsVisited, node, tiempo, conectedComponentId);
				conectedComponentId++;
			}			
		}
		System.out.println(Arrays.toString(transitionsVisited));
		
	}

	private void visitNode(int[] metabolitesVisited, int[] transitionsVisited, String node,  int tiempo, int conectedComponentId) {
		metabolitesVisited[places.get(node).getNumberMetabolite()] = 1;		
		tiempo = tiempo+1;					
		List<Transition<GeneProduct, Metabolite>> neighbours = places.get(node).getTransitions();
		for (int i = 0; i < neighbours.size(); i++) {
			Transition<GeneProduct,Metabolite> reaction = neighbours.get(i);
//			if(transitionsVisited[reaction.getNumber()]!=0) {
//				continue;
//			}
			transitionsVisited[reaction.getNumber()]=conectedComponentId;
			List<Edge<Metabolite>> in= reaction.getIn();
			List<Edge<Metabolite>> out=reaction.getOut();
			for (int j = 0; j < in.size(); j++) {
				String node_1=in.get(j).getObject().getId();
				if(metabolitesVisited[places.get(node_1).getNumberMetabolite()]==0) {
					visitNode(metabolitesVisited, transitionsVisited, node_1, tiempo, conectedComponentId);
				}				
			}
			for (int j = 0; j < out.size(); j++) {
				String node_2=out.get(j).getObject().getId();
				if(metabolitesVisited[places.get(node_2).getNumberMetabolite()]==0) {
					visitNode(metabolitesVisited, transitionsVisited, node_2, tiempo, conectedComponentId);
				}
			}
			
		}		
	}

		
}