package principal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	private static Map<String,Metabolite> metabolites = new TreeMap<String,Metabolite>();
	private Set<String> compartments = new TreeSet<String>();
	private static Map<String,Reaction> reactions = new TreeMap<String,Reaction>();
	private Map<Integer,String> keysG;
	private Map<String,Integer> metabolitesG;	
	private ArrayList<Set<Integer>> edges;
	//Petri net
	private  Map<Integer, Transition> transitions;
	private  Map<String, Integer> transitions2;
	private  Map<String,Integer> places;
	private  Map<Integer,String> places2;
	public static final int INFINITE=100000;
	/**
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

	public static void main(String[] args) throws Exception {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		//		/*System.out.println("Gene products");
		//		for(GeneProduct enzyme:network.geneProducts.values()) {
		//			System.out.println(enzyme.getId()+" "+enzyme.getName());
		//		}*/
		//		List<Reaction> reactions = network.getReactionsAsList();
		//		System.out.println("Loaded "+reactions.size()+" reactions");
		//		for(Reaction r:reactions) {
		//			System.out.println(r.getId()+" "+r.getName()+" "+r.getReactants().size()+" "+r.getProducts().size()+" "+r.getEnzymes().size()+" "+r.getLowerBoundFlux()+" "+r.getUpperBoundFlux());
		//		}
		//
		System.out.println("List of reactions of Metabolite: "+""
				+ "M_acald_c");
		Map<String,List<Reaction>> reactionsOFMetabolite =network.getReactionOfMetabolite("M_acald_c");
		System.out.println("It is a substrate");
		List<Reaction> substrate=reactionsOFMetabolite.get("Substrates");
		for (Reaction reaction : substrate) {
			System.out.println(reaction.getName());
		}
		System.out.println("It is a product");
		List<Reaction> products=reactionsOFMetabolite.get("Products");
		for (Reaction reaction : products) {
			System.out.println(reaction.getName());
		}
		System.out.println("Reactions where "+"G_b1241"+" is a catalysts");
		Map<String,List<Metabolite>> cata=network.catalysis("G_b1241");
		Set<String> keys=cata.keySet();
		for (String reaction : keys) {
			System.out.println("Reaction: "+reaction);
			List<Metabolite> metabolites = cata.get(reaction);
			for (Metabolite meta : metabolites) {
				System.out.println("- "+meta.getName());
			}
		}
		network.makeNet();
		System.out.println("Petri net created");
		System.out.println("Write the entry");
		BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
		int list = Integer.parseInt(bf.readLine());
		List<String> initialMetabolites= new ArrayList<>();
		for (int i = 0; i < list; i++) {
			initialMetabolites.add(bf.readLine());
		}
		network.shortestPath(initialMetabolites, bf.readLine());		
	}

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

	public void makeGraph(boolean cicle) {
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

	public void  makeNet() {	
		int numberMetabolites=1;
		int numberTransition=1;
		transitions= new TreeMap<Integer, Transition>();
		transitions2= new TreeMap<String,Integer>();
		places = new TreeMap< String,Integer>();
		places2 = new TreeMap< Integer,String>();
		Set<String> keysReaction=reactions.keySet();		
		for (String key : keysReaction) {					
			Reaction rea = reactions.get(key);			
			Transition transition = new Transition(rea.getEnzymes(),numberTransition,rea.getName());
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

	public  ArrayList<Transition> transitionsThaCanBeTriggered(List<Transition> transitions, int[][] metabolitesVisited){
		ArrayList<Transition> transitionsThaCanBeTriggered= new ArrayList<>();
		for (int j = 0; j < transitions.size(); j++) {
			List<Edge> inEdges = transitions.get(j).getIn();
			//The greatest distance that is not infinite.
			boolean canBeTriggered=true;
			for (int k = 0; k <inEdges.size(); k++) {
				int number=places.get(inEdges.get(k).getMeta().getId());
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
	public  int findMaximunDistanceOfTheInletMetabolitesOfATransition(Transition transition,int[][] metabolitesVisited) {
		List<Edge> edgesIn=transition.getIn();
		int max=-1;
		for (int i = 0; i < edgesIn.size(); i++) {
			if(metabolitesVisited[edgesIn.get(i).getMeta().getNumber()][2]>max) {
				max=metabolitesVisited[edgesIn.get(i).getMeta().getNumber()][2];
			}
		}		
		return max;
	}

	public void initialValuesOfShortestPath(int[][] metabolitesVisited,List<String> first) {
		for (int j= 0; j < metabolitesVisited.length; j++) {
			metabolitesVisited[j][2]=INFINITE;
		}
		for (int j = 0; j <first.size(); j++) {				
			metabolitesVisited[places.get(first.get(j))][2]=0;
			metabolitesVisited[places.get(first.get(j))][0]=1;
			metabolitesVisited[places.get(first.get(j))][1]=-1;								
		}
	}

	public void shortestPath(List<String> first, String last) throws Exception{		
		//The reactions visited
		int[] reactionsVisited; 
		//Row 1. Moles Row 2. The reaction 3. The <<Distance>>		
		int[][] metabolitesVisited=new int[places.size()][3];
		int[] pqea; 
		ArrayList<String> s= new ArrayList<>(); 
		int distanceBestPath=INFINITE+1;		
		// Find for each metabolite the transitions that can be triggered
		for (int i = 0; i <first.size(); i++) {			
			reactionsVisited= new int[transitions.size()]; 
			metabolitesVisited=new int[places.size()][3];			
			pqea= new int[places.size()];			
			//Initial values
			initialValuesOfShortestPath(metabolitesVisited, first);
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
							Metabolite meta = inEdgesOft.get(k).getMeta();
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
				metabolicPathway(metabolitesVisited, last);	
				distanceBestPath=metabolitesVisited[numberLast][2];
			}			
		}

	}	

	public  void metabolicPathway(int[][] metabolitesVisited, String last) throws Exception{		
		try (PrintStream out = new PrintStream("./out/out.txt")) {
			out.println("source,target,interaction,directed,symbol,value");		
			int[] state= new int[places.size()];	
			if(metabolitesVisited[places.get(last)][1]==-1) {
				out.println("En los metabolitos de entrada ya esta el metabolito final");
				
			}
			else if(metabolitesVisited[places.get(last)][2]==this.INFINITE) {
				out.println("No es posible llegar al metabolito final");
			}
			else {
				Transition t=transitions.get(metabolitesVisited[places.get(last)][1]);		
				List<Edge> edgesIn= t.getIn();		
				for (int i = 0; i < edgesIn.size(); i++) {
					visitAMetabolite(metabolitesVisited, edgesIn.get(i).getMeta().getNumber(), state, t.getName(), out);
				}
			}
		}
				
	}

	public void visitAMetabolite(int[][] metabolitesVisited, int numberOfMetabolite,int[] state, String last, PrintStream out) throws Exception{
		state[numberOfMetabolite]=1;
		if(metabolitesVisited[numberOfMetabolite][1]==-1) {
			System.out.println(last+","+last+","+"TRUE,aab,1213");
			out.print(last+","+last+","+"TRUE,aab,1213");
		}
		else {
			Transition t= transitions.get(metabolitesVisited[numberOfMetabolite][1]);
			List<Edge> l=t.getIn();
			for (int i = 0; i < l.size(); i++) {
				Edge e=l.get(i);
				if(metabolitesVisited[e.getMeta().getNumber()][1]!=-1&&state[e.getMeta().getNumber()]==0) {
					out.print(last+","+t.getName()+","+"TRUE,aab,1213");
					System.out.println(last+","+t.getName()+","+"TRUE,aab,1213");
					visitAMetabolite(metabolitesVisited, numberOfMetabolite, state, t.getName(),out);
				}
			}
		}
	}
	static class MetabolitesP implements Comparable<MetabolitesP>{
		private Metabolite metabolite;
		private int priority;		
		public MetabolitesP(Metabolite metabolite, int priority) {
			super();
			this.metabolite = metabolite;
			this.priority = priority;
		}
		public Metabolite getMetabolite() {
			return metabolite;
		}
		@Override
		public int compareTo(MetabolitesP o) {		
			return o.priority-this.priority;
		}		
	}	
}