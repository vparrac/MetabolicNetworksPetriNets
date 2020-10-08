package metapenta.petrinet;
import java.util.ArrayList;
import java.util.List;

import metapenta.model.Metabolite;
/**
 * This class representate a Place of Petri net
 * @author Valerie Parra
 */

public class Place <EnzymesClass, PlaceObjectClass,TransitionsObjectClass>{
	/**
	 * The object of the place
	 */
	private PlaceObjectClass object;
	
	/**
	 * The list of transitions where the metabolite is reactant
	 */
	private List<Edge<Transition<EnzymesClass, PlaceObjectClass, TransitionsObjectClass>>> inTransitions;
	private List<Edge<Transition<EnzymesClass, PlaceObjectClass, TransitionsObjectClass>>> outTransitions;
	
	/**
	 * Number of metabolite
	 */	
	private int metaboliteNumber;

	/**
	 * Constuctor of place clase
	 * @param metabolite of the place
	 * @param numberMetabolite the number of metabolite in Petri net
	 */
	public Place(PlaceObjectClass object , int numberMetabolite) {
		this.object= object;
		this.metaboliteNumber= numberMetabolite;		
		this.inTransitions = new ArrayList<Edge<Transition<EnzymesClass,PlaceObjectClass,TransitionsObjectClass>>>();
		this.outTransitions = new ArrayList<Edge<Transition<EnzymesClass,PlaceObjectClass,TransitionsObjectClass>>>();
	}
	
	
	/**
	 * Add a transition to place
	 * @param t the new transition
	 */

	public void addInTransition(Edge<Transition<EnzymesClass,PlaceObjectClass, TransitionsObjectClass>> t) {
		inTransitions.add(t);
	}
	
	/**
	 * Add a transition where the current metabolite is a reactant
	 * @param t
	 */
	public void addOutTransition(Edge<Transition<EnzymesClass,PlaceObjectClass, TransitionsObjectClass>> t) {
		outTransitions.add(t);
	}
	
	/**
	 * Returns the list of outler reactions or "transitions"
	 * @return the outlet transition
	 */

	public List<Edge<Transition<EnzymesClass,PlaceObjectClass,TransitionsObjectClass>>> getInTransitions() {
		return inTransitions;
	}

	
	/**
	 * Returns the list of outler reactions or "transitions"
	 * @return the outlet transition
	 */

	public List<Edge<Transition<EnzymesClass,PlaceObjectClass,TransitionsObjectClass>>> getOutTransitions() {
		return outTransitions;
	}

	
	/**
	 * Method that returns the number of the places in petri net
	 * @return numberMetabolite in petri net
	 */
	public int getMetaboliteNumber() {
		return metaboliteNumber;
	}

	/**
	 * Returns the of the place
	 * @return the metabolite
	 */		
	public PlaceObjectClass getObject() {
		return object;
	}
	
	/**
	 * Change Number of metabolite in the petri net
	 * @param numberMetabolite
	 */
	public void setMetaboliteNumber(Integer numberMetabolite) {
		this.metaboliteNumber = numberMetabolite;
	}
}