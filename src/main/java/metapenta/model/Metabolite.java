package metapenta.model;

/**
 * Represents a metabolite that participates in chemical reactions
 * @author Jorge Duitama
 */
public class Metabolite {

	private int nid;
	private String id;
	private String name;
	private String compartment;
	private String chemicalFormula;	

	public Metabolite(String id, String name, String compartment, int nid) {
		super();
		this.id = id;
		this.name = name;
		this.compartment = compartment;
		this.nid = nid;
	}

	public String getChemicalFormula() {
		return chemicalFormula;
	}

	public void setChemicalFormula(String chemicalFormula) {
		this.chemicalFormula = chemicalFormula;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCompartment() {
		return compartment;
	}

	public int getNid() {
		return nid;
	}

	@Override
	public String toString() {		
		String out="{"+"\"id\": "+"\""+id+"\", \"name\":"+"\""+name+"\"}";
		return out;
	}
	
}