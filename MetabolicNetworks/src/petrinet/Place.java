package petrinet;
import java.util.ArrayList;
import java.util.List;
import model.Metabolite;
/**
 * This class representate a Place of Petri net
 * @author Valerie Parra
 */
public class Place {
	/**
	 * The metabolite
	 */
	private Metabolite metabolite;
	/**
	 * The edges of reactions that comes to place
	 */
	private List<Edge> edgesIn;
	/**
	 * The edges 
	 */
	private List<Edge> edgesOut;

	/**
	 * The list of transitions where the metabolite 
	 */
	private List<Transition> transitions;
	
	/**
	 * Number of metabolite
	 */
	
	private int numberMetabolite;

	/**
	 * Constuctor of place clase
	 * @param metabolite of the place
	 * @param numberMetabolite the number of metabolite in Petri net
	 */
	public Place(Metabolite metabolite , int numberMetabolite) {
		this.metabolite= metabolite;
		this.numberMetabolite= numberMetabolite;
		this.edgesIn= new ArrayList<Edge>();
		this.edgesOut= new ArrayList<Edge>();
		this.transitions = new ArrayList<Transition>();
	}
	
	
	/**
	 * Returns the inlet edges of place
	 * @return Edges in of place
	 */
	public List<Edge> getEdgesIn() {
		return edgesIn;
	}

	/**
	 * Returns the outlet edges of place
	 * @return Edges out of place
	 */
	public List<Edge> getEdgesOut() {
		return edgesOut;
	}

	/**
	 * Add an inlet edge to place
	 * @param edge new inlter edge
	 */
	public void addEdgeIn(Edge edge) {
		edgesIn.add(edge);
	}
	/**
	 * Add an oulet edge to place
	 * @param edge new oulet edge
	 */
	public void addEdgeOut(Edge edge) {
		edgesOut.add(edge);
	}

	/**
	 * Add a transition to place
	 * @param t the new transition
	 */

	public void addTransition(Transition t) {
		transitions.add(t);
	}
	
	/**
	 * Returns the list of outler reactions or "transitions"
	 * @return the outlet transition
	 */

	public List<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * Method that returns the number of the places in petri net
	 * @return numberMetabolite in petri net
	 */
	public int getNumberMetabolite() {
		return numberMetabolite;
	}

	/**
	 * Change Number of metabolite in the petri net
	 * @param numberMetabolite
	 */
	public void setNumberMetabolite(Integer numberMetabolite) {
		this.numberMetabolite = numberMetabolite;
	}
	
	/**
	 * Returns the metabolite of the place
	 * @return the metabolite
	 */
	
	
	public Metabolite getMetabolite() {
		return metabolite;
	}
}