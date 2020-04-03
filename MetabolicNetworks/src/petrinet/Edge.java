package petrinet;

import model.Metabolite;
/**
 * Represents a edge of the petri net
 * @author Valerie Parra Cortés
 *
 */
public class Edge {
	/**
	 * The stoichiometry of the metabolite
	 */
	private double stoichiometry;
	/**
	 * The metabolite
	 */
	private Metabolite meta;
	/**
	 * The transition
	 */
	private Transition transition;
	
	public Edge(double stoichiometry, Metabolite meta, Transition transition) {		
		this.stoichiometry = stoichiometry;			
		this.meta=meta;
		this.transition=transition;
	}
	/**
	 * @return the stoichiometry of the edge
	 */
	public double getStoichiometry() {
		return stoichiometry;
	}	
	/**
	 * This method changes the stoichiometry of the Edge  
	 * @param number
	 */
	public void setStoichiometry(int number) {
		this.stoichiometry = number;
	}			
	/**
	* @return the metabolite of edge
	 */
	public Metabolite getMetabolite() {
		return meta;
	}
	/**
	 * @return the transition of the edge
	 */
	public Transition getTransition() {
		return transition;
	}
}