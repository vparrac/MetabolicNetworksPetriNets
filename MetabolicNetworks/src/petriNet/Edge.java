package petriNet;

import principal.Metabolite;

public class Edge {		
	private double stoichiometry;
	private Metabolite meta;
	private Transition transition;	
	public Edge(double stoichiometry, Metabolite meta, Transition transition) {		
		this.stoichiometry = stoichiometry;			
		this.meta=meta;
		this.transition=transition;
	}
	public double getStoichiometry() {
		return stoichiometry;
	}	
	public void setStoichiometry(int number) {
		this.stoichiometry = number;
	}			
	public Metabolite getMeta() {
		return meta;
	}
	public Transition getTransition() {
		return transition;
	}
}