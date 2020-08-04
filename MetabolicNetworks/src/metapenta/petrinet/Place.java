package metapenta.petrinet;
import java.util.ArrayList;
import java.util.List;

import metapenta.model.Metabolite;
/**
 * This class representate a Place of Petri net
 * @author Valerie Parra
 */
public class Place <O,OT>{
	/**
	 * The metabolite
	 */
	private O object;
	/**
	 * The edges of reactions that comes to place
	 */
	private List<Edge<O>> edgesIn;
	/**
	 * The edges 
	 */
	private List<Edge<O>> edgesOut;

	/**
	 * The list of transitions where the metabolite 
	 */
	private List<Transition<OT,O>> transitions;
	
	/**
	 * Number of metabolite
	 */
	
	private int numberMetabolite;

	/**
	 * Constuctor of place clase
	 * @param metabolite of the place
	 * @param numberMetabolite the number of metabolite in Petri net
	 */
	public Place(O object , int numberMetabolite) {
		this.object= object;
		this.numberMetabolite= numberMetabolite;
		this.edgesIn= new ArrayList<Edge<O>>();
		this.edgesOut= new ArrayList<Edge<O>>();
		this.transitions = new ArrayList<Transition<OT,O>>();
	}
	
	
	/**
	 * Returns the inlet edges of place
	 * @return Edges in of place
	 */
	public List<Edge<O>> getEdgesIn() {
		return edgesIn;
	}

	/**
	 * Returns the outlet edges of place
	 * @return Edges out of place
	 */
	public List<Edge<O>> getEdgesOut() {
		return edgesOut;
	}

	/**
	 * Add an inlet edge to place
	 * @param edge new inlter edge
	 */
	public void addEdgeIn(Edge<O> edge) {
		edgesIn.add(edge);
	}
	/**
	 * Add an oulet edge to place
	 * @param edge new oulet edge
	 */
	public void addEdgeOut(Edge<O> edge) {
		edgesOut.add(edge);
	}

	/**
	 * Add a transition to place
	 * @param t the new transition
	 */

	public void addTransition(Transition<OT,O> t) {
		transitions.add(t);
	}
	
	/**
	 * Returns the list of outler reactions or "transitions"
	 * @return the outlet transition
	 */

	public List<Transition<OT,O>> getTransitions() {
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
	 * Returns the  of the place
	 * @return the metabolite
	 */
	
	
	public O getObject() {
		return object;
	}
}