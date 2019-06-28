package principal;

import java.util.ArrayList;
import java.util.List;
import petriNet.Edge;
import petriNet.Transition;

/**
 * Represents a metabolite that participates in chemical reactions
 * @author Jorge Duitama
 */
public class Metabolite {
	private int number;
	private String id;
	private String name;
	private String compartment;
	private String chemicalFormula;	
	private List<Edge> edgesIn;
	private List<Edge> edgesOut;
	private List<Transition> transition;
	/**
	 * Creates a new metabolite with the given information
	 * @param id of the new metabolite
	 * @param name of the new metabolite
	 * @param compartment in which the metabolite is present
	 */
	public Metabolite(String id, String name, String compartment) {
		super();
		this.id = id;
		this.name = name;
		this.compartment = compartment;
		edgesIn=new ArrayList<>();
		edgesOut=new ArrayList<>();
		this.number=-1;
		transition=new ArrayList<>();
		
	}
	/**
	 * @return String chemical formula of this metabolite 
	 */
	public String getChemicalFormula() {
		return chemicalFormula;
	}
	/**
	 * Changes the chemical formula
	 * @param chemicalFormula new formula
	 */
	public void setChemicalFormula(String chemicalFormula) {
		this.chemicalFormula = chemicalFormula;
	}
	/**
	 * @return id of this metabolite
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return name of this metabolite
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return compartment of this metabolite
	 */
	public String getCompartment() {
		return compartment;
	}
	
	public List<Edge> getEdgesIn() {
		return edgesIn;
	}
	public List<Edge> getEdgesOut() {
		return edgesOut;
	}
	
	public void addEdgeIn(Edge edge) {
		edgesIn.add(edge);
	}
	public void addEdgeOut(Edge edge) {
		edgesOut.add(edge);
	}	
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	public void addTransition(Transition t) {
		transition.add(t);
	}
	public List<Transition> getTransitions() {
		return transition;
	}
}
