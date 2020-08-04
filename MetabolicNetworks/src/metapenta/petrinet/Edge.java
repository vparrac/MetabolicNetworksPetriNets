package metapenta.petrinet;

import metapenta.model.Metabolite;
/**
 * Represents a edge of the petri net
 * @author Valerie Parra Cortés
 *
 */
public class Edge <O> {
	/**
	 * The stoichiometry of the metabolite
	 */
	private double tokens;
	/**
	 * The metabolite
	 */
	private O object;
	/**
	 * The transition
	 */
	private Transition transition;
	
	public Edge(double tokens, O object, Transition transition) {		
		this.tokens = tokens;			
		this.object=object;
		this.transition=transition;
	}
	/**
	 * @return the stoichiometry of the edge
	 */
	public double getStoichiometry() {
		return tokens;
	}	
	/**
	 * This method changes the stoichiometry of the Edge  
	 * @param number
	 */
	public void setStoichiometry(int number) {
		this.tokens = number;
	}			
	/**
	* @return the metabolite of edge
	 */
	public O getObject() {
		return object;
	}
	/**
	 * @return the transition of the edge
	 */
	public Transition getTransition() {
		return transition;
	}
}