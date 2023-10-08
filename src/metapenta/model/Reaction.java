package metapenta.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a reaction between metabolites
 * @author Jorge Duitama
 */
public class Reaction {
	private String id;
	private String name;
	private List<ReactionComponent> reactants;
	private List<ReactionComponent> products;
	private boolean reversible = false;
	private double lowerBoundFlux = -1000;
	private double upperBoundFlux = 1000;
	private List<GeneProduct> enzymes;
	private Map<String, Integer> stoichiometryDifference;
	/**
	 * Creates a new reaction with the given information
	 * @param id of the reaction
	 * @param name of the reaction
	 * @param reactants Metabolites that serve as input of the reaction
	 * @param products Metabolites that serve as output of the reaction
	 */
	public Reaction(String id, String name, List<ReactionComponent> reactants, List<ReactionComponent> products) {
		super();
		this.id = id;
		this.name = name;
		this.reactants = reactants;
		this.products = products;
	}
	/**
	 * @return true if the reaction is reversible, false otherwise
	 */
	public boolean isReversible() {
		return reversible;
	}
	/**
	 * Changes the reversible status
	 * @param reversible new reversible status
	 */
	public void setReversible(boolean reversible) {
		this.reversible = reversible;
	}
	/**
	 * @return id of the reaction
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return name of the reaction
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return List of input metabolites
	 */
	public List<ReactionComponent> getReactants() {
		return reactants;
	}
	/**
	 * @return List of output metabolites
	 */
	public List<ReactionComponent> getProducts() {
		return products;
	}
	/**
	 * @return List<GeneProduct> Enzymes that catalyze the reaction
	 */
	public List<GeneProduct> getEnzymes() {
		return enzymes;
	}
	/**
	 * Changes the enzymes that catalyze the reaction
	 * @param enzymes New enzymes
	 */
	public void setEnzymes(List<GeneProduct> enzymes) {
		this.enzymes = enzymes;
	}
	/**
	 * @return Lower bound of the flux in this reaction
	 */
	public double getLowerBoundFlux() {
		return lowerBoundFlux;
	}
	/**
	 * Changes the lower bound of the flux in this reaction
	 * @param lowerBound new lower bound
	 */
	public void setLowerBoundFlux(double lowerBound) {
		this.lowerBoundFlux = lowerBound;
	}
	/**
	 * @return Upper bound of the flux in this reaction
	 */
	public double getUpperBoundFlux() {
		return upperBoundFlux;
	}
	/**
	 * Changes the upper bound of the flux in this reaction
	 * @param upperBound new upper bound
	 */
	public void setUpperBoundFlux(double upperBound) {
		this.upperBoundFlux = upperBound;
	}

	/**
	 * Method that makes a String with the information about the reactants
	 * of the Reaction
	 * @return reactantSring
	 */
	private String printReactants() {
		String reactantSring="";

		for (int i = 0; i < reactants.size(); i++) {
			if(i==reactants.size()-1) {
				reactantSring+=reactants.get(i).toString();
			}
			else {
				reactantSring+=reactants.get(i).toString()+",";
			}			
		}
		return reactantSring;
	}
	
	private List<Map<String, Integer>> getListElements(List<ReactionComponent> reactionsComponent) {
		List<Map<String, Integer>> listElements = new ArrayList<>();
		for(ReactionComponent reaction: reactionsComponent) {
			Map<String, Integer> elements = reaction.getFormulaReactionComponent();
			listElements.add(elements);
		}
			return listElements;
	}
	
	private Map<String, Integer> getSumElements(List<Map<String, Integer>> listElements) {
		Map<String, Integer> sum = listElements.stream().flatMap(elements -> elements.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,Integer::sum));
		return sum;
	}
	
	public boolean isBalance() {
		List<Map<String, Integer>> listElemReactants = getListElements(reactants);
		Map<String, Integer> sumlistElemReactants = getSumElements(listElemReactants);
		
		List<Map<String, Integer>> listElemProducts = getListElements(products);
		Map<String, Integer> sumlistElemProducts = getSumElements(listElemProducts);
		
		return sumlistElemReactants.equals(sumlistElemProducts);
		
	}
	
	public Map<String, Integer> getDifference() {
        List<Map<String, Integer>> listElemReactants = getListElements(reactants);
        Map<String, Integer> sumlistElemReactants = getSumElements(listElemReactants);

        List<Map<String, Integer>> listElemProducts = getListElements(products);
        Map<String, Integer> sumlistElemProducts = getSumElements(listElemProducts);

        Map<String, Integer> difference = new HashMap<>();

        for (String key : sumlistElemReactants.keySet()) {
        	int reactantValue = sumlistElemReactants.getOrDefault(key, 0);
            int productValue = sumlistElemProducts.getOrDefault(key, 0);

            if (reactantValue - productValue != 0) {
                difference.put(key, reactantValue - productValue);
            }
        }
        System.out.println("DIFERENCIA");
        for (Map.Entry<String, Integer> entry : difference.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return difference;
    }

	/**
	 * Method that makes a String with the information about the products
	 * of the reaction
	 * of the Reaction
	 * @return A string with the information of the reaction
	 */
	private String printProducts() {
		String productString="";

		for (int i = 0; i < products.size(); i++) {
			if(i==products.size()-1) {
				productString+=products.get(i).toString();
			}
			else {
				productString+=products.get(i).toString()+",";
			}			
		}
		return productString;
	}
//	private String printStoichiometryDifference() {
//		String StoichiometryDifference="";
//		
//		for (Map.Entry<String, Integer> entry : stoichiometryDifference.entrySet()) {
//			String
//			
//		}
//
//		for (int i = 0; i < products.size(); i++) {
//			if(i==products.size()-1) {
//				productString+=products.get(i).toString();
//			}
//			else {
//				productString+=products.get(i).toString()+",";
//			}			
//		}
//		return productString;
//	}

	@Override
	public String toString() {
		String jsonReaction="{";
		jsonReaction+=" \"id\":"+"\""+id+"\",";
		jsonReaction+=" \"name\":"+"\""+name+"\",";
		jsonReaction+=" \"reversible\":"+reversible+",";
		jsonReaction+=" \"reactants\":[";
		jsonReaction+= printReactants()+" ],";
		jsonReaction+=" \"products\":[";
		jsonReaction+= printProducts()+" ]";
		jsonReaction+="}";
		return jsonReaction;
	}
	
	public Reaction clone() {
		return new Reaction(id, name, reactants, products);
	}


}
