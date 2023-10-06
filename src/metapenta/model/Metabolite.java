package metapenta.model;

/**
 * Represents a metabolite that participates in chemical reactions
 * @author Jorge Duitama
 */
public class Metabolite {
	
	private String id;
	private String name;
	private String compartment;
	private ChemicalFormula chemicalFormula;	
	
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
	}
	/**
	 * @return String chemical formula of this metabolite 
	 */
	public ChemicalFormula getChemicalFormula() {
		return chemicalFormula;
	}
	/**
	 * Changes the chemical formula
	 * @param chemicalFormula new formula
	 */
	public void setChemicalFormula(String chemicalFormula) {
		ChemicalFormula formula = new ChemicalFormula(chemicalFormula);
		this.chemicalFormula = formula;
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
	
	
	@Override
	public String toString() {		
		String out="{"+"\"id\": "+"\""+id+"\", \"name\":"+"\""+name+"\"}";
		return out;
	}
	
}
