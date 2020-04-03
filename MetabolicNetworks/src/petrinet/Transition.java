package petrinet;
import java.util.ArrayList;
import java.util.List;

import model.GeneProduct;
/**
 * Class that represent a transition
 * @author Valerie Parra
 */
public class Transition {
	/**
	 * The id of the transition
	 */
	private int number;
	/**
	 * The list of edges in
	 */
	private List<Edge> in;
	/**
	 * The list of edges out
	 */
	private List<Edge> out;
	/**
	 * The enzymes of the reaction
	 */
	private List<GeneProduct> geneProduct;
	/**
	 * The name of the transition
	 */
	private String name;
	/**
	 * The id number of the transitions
	 */
	private String id;
	/**
	 * The constructor of class
	 * @param gp the list of the enzymes of the reaction
	 * @param number the id (number)
	 * @param name of the reaction
	 * @param id (the string)
	 */
	public Transition(List<GeneProduct> gp,int number, String name, String id) {
		this.in = new ArrayList<>();
		this.out = new ArrayList<>();
		this.geneProduct=gp;
		this.number=-1;
		this.number=number;
		this.name=name;
		this.id=id;
	}	
	/**
	 * Add a edges in the list of edges in
	 * @param edge to add
	 */
	public void addEdgeIn(Edge edge) {
		in.add(edge);
	}	
	/**
	 * Add a edges in the list of edges out
	 * @param edge to add
	 */
	public void addEdgeOut(Edge edge) {
		out.add(edge);
	}
	
	/**	
	 * @return The in edges of the transition
	 */
	public List<Edge> getIn() {
		return in;
	}
	/**	
	 * @return The out edges of the transition
	 */
	
	public List<Edge> getOut() {
		return out;
	}	
	/**	
	 * @return The list of enzymes that catalyst the transition
	 */
	
	public List<GeneProduct> getGeneProduct() {
		return geneProduct;
	}
	
	/**
	* Set the number (id) of the transition
	* @param number the new number (id) of transition
	**/
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	* @return the number (id) of this transition
	**/
	
	public int getNumber() {
		return number;
	}
	
	/**
	* @return the name of the transition (the reaction)
	**/
	
	public String getName() {
		return name;
	}
	
	/**
	* Set the id (the string id) of the transition
	* @param id the new id (the string id) of the transition
	**/
	public void setId(String id) {
		this.id = id;
	}
	/**
	*@return the id (string id) of the transition)
	**/
	public String getId() {
		return id;
	}
}
