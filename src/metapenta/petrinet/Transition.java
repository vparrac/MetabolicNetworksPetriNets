package metapenta.petrinet;
import java.util.ArrayList;
import java.util.List;

import metapenta.model.GeneProduct;
/**
 * Class that represent a transition
 * @author Valerie Parra
 */
public class Transition <EnzymeClass, PlaceObjectClass, TransitionsObjectClass>{
	/**
	 * The id of the transition
	 */
	private int transitionNumber;
	/**
	 * The list of edges in
	 */
	private List<Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>>> inPlaces;
	/**
	 * The list of edges out
	 */
	private List<Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>>> outPlaces;
	/**
	 * The enzymes of the reaction
	 */
	private List<EnzymeClass> objects;
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
	public Transition(List<EnzymeClass> gp,int number, String name, String id) {
		this.inPlaces = new ArrayList<>();
		this.outPlaces = new ArrayList<>();
		this.objects=gp;
		this.transitionNumber=-1;
		this.transitionNumber=number;
		this.name=name;
		this.id=id;
	}	
	/**
	 * Add a edges in the list of edges in
	 * @param edge to add
	 */
	public void addPlaceIn(Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>> edge) {
		inPlaces.add(edge);
	}	
	/**
	 * Add a edges in the list of edges out
	 * @param edge to add
	 */
	public void addPlaceOut(Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>> edge) {
		outPlaces.add(edge);
	}
	
	/**	
	 * @return The in edges of the transition
	 */
	public List<Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>>> getInPlaces() {
		return inPlaces;
	}
	/**	
	 * @return The out edges of the transition
	 */
	
	public List<Edge<Place<EnzymeClass, PlaceObjectClass, TransitionsObjectClass>>> getOutPlaces() {
		return outPlaces;
	}	
	/**	
	 * @return The list of enzymes that catalyst the transition
	 */
	
	public List<EnzymeClass> getGeneProducts() {
		return objects;
	}
	
	/**
	* Set the number (id) of the transition
	* @param number the new number (id) of transition
	**/
	public void setNumber(int number) {
		this.transitionNumber = number;
	}
	
	/**
	* @return the number (id) of this transition
	**/
	
	public int getNumber() {
		return transitionNumber;
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
	
	@Override
	public String toString() {
		return id;
	}
}
