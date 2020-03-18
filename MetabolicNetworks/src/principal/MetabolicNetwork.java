package principal;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import petriNet.Edge;
import petriNet.Transition;
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
	private Map<Integer,String> keysG;
	private Map<String,Integer> metabolitesG;	
	private ArrayList<Set<Integer>> edges;
	//Petri net
	/**
	 * A map of Integer to transition. The map represents the Transitions of Petri net 
	 */
	private  Map<Integer, Transition> transitions;
	/**
	 *  A map of Transitions to Integer. The map represents the transitions of Petri net
	 */
	private  Map<String, Integer> transitions2;
	/**
	 * A map of a String (that represents the id of the metabolite) to a Integer.
	 * This map represents the places of Petri net
	 */
	private  Map<String,Integer> places;
	/**
	 * A map of a String (that represents the id of the metabolite) to a Integer.
	 * This map represents the places of Petri net
	 */
	private  Map<Integer,String> places2;
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
	/**
	 * The main method of class
	 * @param args[0] the path of the XML file 
	 * @param args[1] A list of initial metabolites separated with commas
	 * @throws Exception if exists any error of I/O
	 */
	public static void main(String[] args) throws Exception {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		//String [] initialMetabolitesA = args[1].split(",");
		//String [] finalMetabolitesA = args[2].split(",");
		//String outPrefix = args[3];
		network.makeNet();		
		//List<String> initialMetabolites= Arrays.asList(initialMetabolitesA);
		ArrayList<String> a = new ArrayList<String>();
		a.add("M_acon_C_c");
		a.add("M_h2o_c");
		a.add("M_nadp_c");
//		a.add("M_2pg_c");
//		a.add("M_fum_c");
//		a.add("M_q8h2_c");
		network.shortestPath2(a,"M_akg_c","fileName1.txt","fileName2.txt","fileName3.txt");
//		List<Metabolite> ml = network.findSinks();
//		System.out.println(ml.size());
//		for (Metabolite metabolite : ml) {
//			System.out.println(metabolite);
//		}
//		System.out.println("Número de sumideros " +ml.size());
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
		Metabolite metabolite=metabolites.get(metaboliteKeyName);
		List<Reaction> rsubstrates= new ArrayList<>();
		List<Reaction> rproducts=new ArrayList<>();
		Set<String> keys=reactions.keySet();		
		for (String key : keys) {
			Reaction rea= reactions.get(key);
			List<ReactionComponent> substrates= rea.getReactants();
			List<ReactionComponent> products= rea.getProducts();			
			for (int i = 0; i < substrates.size(); i++) {
				if(substrates.get(i).getMetabolite().getId().equals(metabolite.getId())) {
					rsubstrates.add(rea);					
				}
			}
			for (int i = 0; i < products.size(); i++) {
				if(products.get(i).getMetabolite().getId().equals(metabolite.getId())) {
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

	public Map<String,List<Metabolite>> catalysis(String enzymeName){
		Map<String,List<Metabolite>> cata= new TreeMap<>();
		Set<String> keys=reactions.keySet();		
		for (String key : keys) {
			Reaction rea= reactions.get(key);
			List<GeneProduct> enzymes=rea.getEnzymes();
			for (GeneProduct enzyme : enzymes) {
				List<Metabolite> metabolites;
				if(enzyme.getName().equals(enzymeName)) {
					metabolites= new ArrayList<>();
					for (int i = 0; i < rea.getProducts().size(); i++) {
						metabolites.add(rea.getProducts().get(i).getMetabolite());
					}
					cata.put(rea.getId(), metabolites);
					break;
				}
			}			
		}
		return cata;
	}

	/**
	 *  Create a graph with the metabolites as nodes 
	 *  Exists a edge between a two nodes if exists a reaction where one of the metabolites is a substrate and the other one is the product
	 */
	public void makeGraph() {
		//metabolites of Graph
		metabolitesG=new TreeMap<>();
		keysG=new TreeMap<>(); 
		Set<String> keysMetabolites=metabolites.keySet();
		int i=0;
		for (String key : keysMetabolites) {
			Metabolite meta = metabolites.get(key);
			if(!metabolitesG.containsValue(meta.getId())) {
				metabolitesG.put(meta.getId(), i);
				keysG.put(i, meta.getId());
			}
		}
		//Edges
		edges = new ArrayList<>();
		Set<String> keysReaction=reactions.keySet();
		for (String key : keysReaction) {
			Reaction rea = reactions.get(key);
			List<ReactionComponent> reactantsC=rea.getReactants();
			List<ReactionComponent> productsC=rea.getProducts();
			Set<Integer> products= new TreeSet<>();
			for(ReactionComponent rcp: productsC) {
				Metabolite m= rcp.getMetabolite();
				products.add(metabolitesG.get(m.getName()));
			}			
			for (ReactionComponent rc : reactantsC) {
				Metabolite meta = rc.getMetabolite();
				int keyM= metabolitesG.get(meta.getId());
				if(edges.get(keyM)==null) {
					Set<Integer> newSet= new TreeSet<>();
					newSet.addAll(products);
					edges.set(keyM, newSet);
				}				
			}
		}
	}

	
	//-------------------------------------------------------------------
	//-----------------------Metabolic Network --------------------------
	//--------------------------As Petri Net-----------------------------


	
	/**
	 * Create the petri net that represent the metabolic network
	 */
	public void  makeNet() {	
		int numberMetabolites=1;
		int numberTransition=1;
		transitions= new TreeMap<Integer, Transition>();
		transitions2= new TreeMap<String,Integer>();
		places = new TreeMap< String,Integer>();
		places2 = new TreeMap< Integer,String>();
		Set<String> keysReaction=reactions.keySet();	
		System.out.println("Reactions size : " +keysReaction.size());
		for (String key : keysReaction) {		
			
			Reaction rea = reactions.get(key);	

		
			Transition transition = new Transition(rea.getEnzymes(),numberTransition,rea.getName(),rea.getId());
			numberTransition++;
			List<ReactionComponent> reactantsC=rea.getReactants();
			List<ReactionComponent> productsC=rea.getProducts();			
			for (ReactionComponent rc : reactantsC) {
				Metabolite meta = rc.getMetabolite();
				if(meta.getNumber()==-1) {
					meta.setNumber(numberMetabolites);					
					places.put(meta.getId(),numberMetabolites);
					places2.put(numberMetabolites,meta.getId());
					numberMetabolites++;					
				}
				Edge edge= new Edge(rc.getStoichiometry(),meta,transition);				
				meta.addEdgeOut(edge);				
				transition.addEdgeIn(edge);
				meta.addTransition(transition);
			}			
			for (ReactionComponent rc : productsC) {
				Metabolite meta = rc.getMetabolite();
				if(meta.getNumber()==-1) {
					meta.setNumber(numberMetabolites);
					places.put(meta.getId(),numberMetabolites);
					places2.put(numberMetabolites,meta.getId());
					numberMetabolites++;					
				}
				Edge edge= new Edge(rc.getStoichiometry(),meta,transition);				
				meta.addEdgeIn(edge);
				transition.addEdgeOut(edge);
				meta.addTransition(transition);
			}			
			transitions.put(transition.getNumber(), transition);
			transitions2.put(transition.getName(),transition.getNumber());			
		}				
	}


	/**
	 * Find the transition that can be triggered 
	 * @param transitions the transitions to evaluate
	 * @param metabolitesVisited a matrix where the rows are the metabolites, the first column indicate if exists the metabolite 
	 * the second column is the reaction that allows to obtain the metabolite and the third column the distance of a initial reaction 
	 * @return
	 */
	public  ArrayList<Transition> transitionsThaCanBeTriggered(List<Transition> transitions, int[][] metabolitesVisited){
		ArrayList<Transition> transitionsThaCanBeTriggered= new ArrayList<>();
		for (int j = 0; j < transitions.size(); j++) {
			List<Edge> inEdges = transitions.get(j).getIn();
			//The greatest distance that is not infinite.
			boolean canBeTriggered=true;
			for (int k = 0; k <inEdges.size(); k++) {
				int number=inEdges.get(k).getMetabolite().getNumber();				
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
	public int findMaximunDistanceOfTheInletMetabolitesOfATransition(Transition transition,int[][] metabolitesVisited) {
		List<Edge> edgesIn=transition.getIn();
		int max=-1;
		for (int i = 0; i < edgesIn.size(); i++) {
			if(metabolitesVisited[edgesIn.get(i).getMetabolite().getNumber()][2]>max) {
				max=metabolitesVisited[edgesIn.get(i).getMetabolite().getNumber()][2];
			}
		}		
		return max;		
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
		System.out.println(places);
		for (int j= 0; j < metabolitesVisited.length; j++) {
			metabolitesVisited[j][2]=INFINITE;
		}
		for (int j = 0; j <first.size(); j++) {			
			System.out.println(places.get(first.get(j)));
			metabolitesVisited[places.get(first.get(j))][2]=0; //Distance
			metabolitesVisited[places.get(first.get(j))][0]=1; //is there that metabolite?
			metabolitesVisited[places.get(first.get(j))][1]=-1; //The last transition, -1 if it is not assigned								
		}
	}

	public void initialValuesOfShortestPath2(int[][] metabolitesVisited,List<String> first) {		
		for (int j = 0; j <metabolitesVisited.length; j++) {
			metabolitesVisited[j][0]=1;		
			metabolitesVisited[j][1]=-1;		
			metabolitesVisited[j][2]=0;										
		}

		metabolitesVisited[places.get("M_pyr_e")][2]=INFINITE;
		metabolitesVisited[places.get("M_pyr_e")][1]=0;
		metabolitesVisited[places.get("M_pyr_e")][0]=0;

	}
	/**
	 * Find the shortest path from a group of initial metabolites to a unique target metabolite
	 * @param first The list of initial metabolies
	 * @param last Target metabolite
	 * @param nameOfFile File where will be printed the graph 
	 * @return graph that represents  the visited metabolites
	 * @throws Exception  is  exists any  error  write  the file
	 */

	public int[][] shortestPath(List<String> first, String last, String nameOfFile) throws Exception{
		int[][] graph = null;
		//The reactions visited
		int[] reactionsVisited; 
		//Row 1. Moles Row 2. The reaction 3. The <<Distance>>	
		int[][] metabolitesVisited=new int[places.size()+1][3]; //Places.size()+1 for enumeration
		int[] pqea;		
		int distanceBestPath=INFINITE+1;		
		// Find for each metabolite the transitions that can be triggered
		for (int i = 0; i <first.size(); i++) {			
			reactionsVisited= new int[transitions.size()]; 
			metabolitesVisited=new int[places.size()+1][3];			
			pqea= new int[places.size()];			
			//Initial values
			initialValuesOfShortestPath2(metabolitesVisited, first);
			PriorityQueue<MetabolitesP> pq = new PriorityQueue<>();						
			pq.add(new MetabolitesP(metabolites.get(first.get(i)),0));
			while (!pq.isEmpty()) { 
				Metabolite mp = pq.poll().getMetabolite();			
				int n = mp.getNumber();
				if(mp.getId().equals(last)) {
					break;
				}
				if(pqea[n]==0) {
					pqea[n]++;			
					List<Transition> transitions= transitionsThaCanBeTriggered(mp.getTransitions(),metabolitesVisited);
					for (int j = 0; j < transitions.size(); j++) {
						reactionsVisited[transitions.get(j).getNumber()]=1;
						List <Edge> inEdgesOft = transitions.get(j).getOut();						
						//The max distance
						int maxValue= findMaximunDistanceOfTheInletMetabolitesOfATransition(transitions.get(j),metabolitesVisited)+1;
						//Update the distances
						for (int k = 0; k < inEdgesOft.size(); k++) {
							Metabolite meta = inEdgesOft.get(k).getMetabolite();
							int number = meta.getNumber();
							if((maxValue)<metabolitesVisited[number][2]) {
								metabolitesVisited[number][2]=maxValue;
								metabolitesVisited[number][1]=transitions.get(j).getNumber();
							}
							//Add to PQ
							MetabolitesP mp1 = new MetabolitesP(meta,(int) metabolitesVisited[number][2]);
							pq.add(mp1);
							//Exists
							metabolitesVisited[number][0]=1;								
						}
					}	
				}
			}
			//Makes the metabolic Pathway
			int numberLast =places.get(last);
			if(metabolitesVisited[numberLast][2]<distanceBestPath) {
				graph=metabolicPathway(metabolitesVisited, last);	
				distanceBestPath=metabolitesVisited[numberLast][2];
			}			
		}
		printReactionsGraphInCSV(graph,nameOfFile);
		return graph;
	}	

		/**
		 * 
		 * @param first Lista metabolitos iniciales
		 * @param last Metábolito a producir
		 * @param fileName1 Metabolic Network In CSV
		 * @param fileName2 Reactions Graph In CSV
		 * @param fileName3 Methabolic Path
		 * @return 
		 * @throws Exception
		 */

	public int[][] shortestPath2(List<String> first, String last, String fileName1,String fileName2, String fileName3) throws Exception{
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
		System.out.println(first);
		initialValuesOfShortestPath(metabolitesVisited, first);
		PriorityQueue<MetabolitesP> pq = new PriorityQueue<>();	
		for (String i : first) {
			pq.add(new MetabolitesP(metabolites.get(i),0));
		}						
		while (!pq.isEmpty()) { 
			Metabolite mp = pq.poll().getMetabolite();			
			int n = mp.getNumber();
			if(mp.getId().equals(last)) {
				break;
			}
			System.out.println(pqea.length);
			if(pqea[n]==0) {
				pqea[n]++;			
				List<Transition> transitions= transitionsThaCanBeTriggered(mp.getTransitions(),metabolitesVisited);
				for (int j = 0; j < transitions.size(); j++) {
					reactionsVisited[transitions.get(j).getNumber()]=1;
					List <Edge> inEdgesOft = transitions.get(j).getOut();						
					//The max distance
					int maxValue= findMaximunDistanceOfTheInletMetabolitesOfATransition(transitions.get(j),metabolitesVisited)+1;
					//Update the distances
					for (int k = 0; k < inEdgesOft.size(); k++) {
						Metabolite meta = inEdgesOft.get(k).getMetabolite();
						int number = meta.getNumber();
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
		System.out.println(last);
		System.out.println(places.get(last));
		int numberLast =places.get(last);
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

		if(metabolitesVisited[places.get(last)][1]==-1) {
			System.err.println("En los metabolitos de entrada ya está el metabolito final");
		}

		else if(metabolitesVisited[places.get(last)][2]==INFINITE) {
			System.err.println("No es posible llegar al metabolito final");
		}

		else {
			ArrayDeque<Integer> transitionss = new ArrayDeque<>();
			Transition currentTransition=transitions.get(metabolitesVisited[places.get(last)][1]);
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
				List<Edge> edgesIn= currentTransition.getIn();		
				int nInitialMetabolites=0;
				for (int i = 0; i < edgesIn.size(); i++) {
					int meta = edgesIn.get(i).getMetabolite().getNumber();
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
	public void printCatalystOfMethabolicPath(int[][] graph, String fileName) throws Exception{	
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
			
			Iterator<GeneProduct> i = allCatalyst.iterator();
			while(i.hasNext()) {
				System.out.println(i.next());
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
	public void printMetabolicNetworkInCSV(int[][] graph, String fileName, List<String> first, String last) throws Exception{
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
	public void printATransitionInCSV(Transition t, PrintStream out,int i, int j,int[] metabolitesVisited,int[] transitionsVisited, List<String> first, String last) {
		List<Edge> metaInTransition = t.getIn();
		if(transitionsVisited[t.getNumber()]==0) {
			transitionsVisited[t.getNumber()]++;
			for (Edge edge : metaInTransition) {
				String nameOfMeta = edge.getMetabolite().getId();
				if(first.contains(nameOfMeta)) {
					nameOfMeta="TT"+nameOfMeta;
				}
				else if(last.equalsIgnoreCase(nameOfMeta)) {
					nameOfMeta="OO"+nameOfMeta;
				}
				out.println(nameOfMeta+COMMA+t.getId()+",PP,TRUE,abc"+i+j+",1.234");
			}
			metaInTransition = t.getOut();		
			for (Edge edge : metaInTransition) {
				String nameOfMeta = edge.getMetabolite().getId();
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
	
	
	

	
	
	
	
	public List<Metabolite> findSinks() {	
		List<Metabolite> sinks= new ArrayList<Metabolite>();
		for (String key : metabolites.keySet()) {
			 Metabolite m = metabolites.get(key);
			 if(m.getEdgesOut().size()==0) {
				 sinks.add(m);
			 }
		}
		return sinks;
	}
	
	
	/**
	 * Find the transitions (reactions) where the metabolite is reagent
	 * @param idMetabolite to find the transitions (reactions)
	 */
	public void findTransitionsOfAMetabolite(String idMetabolite) {
		ArrayList<Transition> transitionsThaCanBeTriggered= new ArrayList<>();
		
	}

	//-------------------------------------------------
	//---------------Inner auxiliar classes------------
	//-------------------------------------------------

	/**
	 * Class thar represents a tuple of metabolite-Transition
	 * @author Valerie Parra Cortés
	 */

	class MetaboliteTransitionCouple {
		/**
		 * Id of transition
		 */
		private String idTransition;
		/***
		 * Id metabolite
		 */
		private String idMetabolite;

		/**
		 * Constructor of class MetaboliteTransitionCouple
		 * @param idTransition the identifier of transition
		 * @param idMetabolite the identifier of metabolite
		 */
		public MetaboliteTransitionCouple(String idTransition, String idMetabolite) {			
			this.idTransition = idTransition;
			this.idMetabolite = idMetabolite;
		}				
	}



	/**
	 * Class to create the priority queue. Represents a metabolite with a priority that is the distance to
	 * any of initial reactions 
	 * @author Valerie Parra
	 */
	class MetabolitesP implements Comparable<MetabolitesP>{
		/**
		 * The metabolite 
		 */		
		private Metabolite metabolite;
		/**
		 * The priority
		 */
		private int priority;	
		/**
		 * Constructor of the class, initializes the atributes in the value of the parameters
		 * @param metabolite The metabolite
		 * @param priority the priority
		 */
		public MetabolitesP(Metabolite metabolite, int priority) {
			super();
			this.metabolite = metabolite;
			this.priority = priority;
		}
		/**
		 * Method to get the metabolite
		 * @return themetabolite
		 */
		public Metabolite getMetabolite() {
			return metabolite;
		}

		@Override
		public int compareTo(MetabolitesP o) {		
			return o.priority-this.priority;
		}		
	}	
}