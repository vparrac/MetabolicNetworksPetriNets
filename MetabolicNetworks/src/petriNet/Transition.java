package petriNet;
import java.util.ArrayList;
import java.util.List;
import principal.GeneProduct;
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
	public List<Edge> getIn() {
		return in;
	}
	public List<Edge> getOut() {
		return out;
	}	
	public List<GeneProduct> getGeneProduct() {
		return geneProduct;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	public String getName() {
		return name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
}