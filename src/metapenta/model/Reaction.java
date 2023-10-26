package metapenta.model;

import java.lang.reflect.Array;
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
	private boolean isBalanced = false;
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
		if (isBalanced()) {
			this.isBalanced = true;
		}
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
	

	public boolean getIsBalanced() {
		return isBalanced;
	}
	public void setIsBalanced(boolean isBalanced) {
		this.isBalanced = isBalanced;
	}
	
	public void addProduct(ReactionComponent product) {
		products.add(product);
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
	
	public boolean isBalanced() {
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
        System.out.println("suma reactantes");
        for (Map.Entry<String, Integer> entry : sumlistElemReactants.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("suma productos");
        for (Map.Entry<String, Integer> entry : sumlistElemProducts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("DIFERENCIA");
        for (Map.Entry<String, Integer> entry : difference.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return difference;
    } 
	
	public Map<String, String> casesNoBalanced() {
		
		String reason = "";
		String sumreactions = "";
		Map<String, String> reasonSum = new HashMap<>();
		List<Map<String, Integer>> listElemReactants = getListElements(reactants);
		Map<String, Integer> sumlistElemReactants = getSumElements(listElemReactants);
		
		List<Map<String, Integer>> listElemProducts = getListElements(products);
		Map<String, Integer> sumlistElemProducts = getSumElements(listElemProducts);
		
		//sumreactions = "Sum of stoichiometric coefficients(reactants): ";
        for (Map.Entry<String, Integer> entry : sumlistElemReactants.entrySet()) {
        	sumreactions = sumreactions + "{ "+entry.getKey() + ": " + entry.getValue()+ "}";
        }
        sumreactions = sumreactions + " | ";
        //reason = reason + "Sum of stoichiometric coefficients(products): ";
        for (Map.Entry<String, Integer> entry : sumlistElemProducts.entrySet()) {
        	sumreactions = sumreactions + "{ "+entry.getKey() + ": " + entry.getValue()+ "} ";
        }
        sumreactions = sumreactions + " | ";
        Map<String, Integer> difference = getDifference();
        //reason = reason + "Difference between reactants and products: ";
        for (Map.Entry<String, Integer> entry : difference.entrySet()) {
        	sumreactions = sumreactions + "{ "+entry.getKey() + ": " + entry.getValue()+ "}";
        }
        sumreactions = sumreactions + " | ";
        
		
		for(Map<String, Integer> elementReactants: listElemReactants) {
			if(elementReactants == null) {
				reason = "At least one reactant does not have chemical formula ";
			}
		}
		for(Map<String, Integer> elementProducts: listElemProducts) {
			if(elementProducts == null) {
				reason = "At least one product does not have chemical formula ";
			}
		}
		if(reason.equals("")) {
			if(sumlistElemReactants.size() != sumlistElemProducts.size()) {
				reason = "Reactants and product do not have the same elements ";
			}
			else {
				reason = "The sum of coefficients is differents in each side";
			}
			
		}
		
		
        
        reasonSum.put(reason, sumreactions);
			
		
		
		return reasonSum;
		
	}
	
	public boolean balanceReaction() {
		List<Map<String, Integer>> listElemReactants = getListElements(reactants);
		Map<String, Integer> sumlistElemReactants = getSumElements(listElemReactants);
		
		List<Map<String, Integer>> listElemProducts = getListElements(products);
		Map<String, Integer> sumlistElemProducts = getSumElements(listElemProducts);
		
		Map<String, Integer> diference = getDifference();
		
		List<Reaction> reactionsBalanced = new ArrayList<>();
		
		boolean changedToBalanced = false;
		
		int mcm = 0;
		
		for(Map<String, Integer> elementReactants: listElemReactants) {
			if(elementReactants == null) {
				setIsBalanced(false);
			}
		}
		for(Map<String, Integer> elementProducts: listElemProducts) {
			if(elementProducts == null) {
				setIsBalanced(false);
			}
		}
		
		if(sumlistElemReactants.size() != sumlistElemProducts.size()) {
			setIsBalanced(false);
		}
		else if (reactants.size() == 1 && products.size()==1) {
			if(reactants.get(0).getStoichiometry() == 1.0 && products.get(0).getStoichiometry() == 1.0) {
				ReactionComponent reactant = reactants.get(0);
				ReactionComponent product = products.get(0);
				
				Map<String, Integer> formulaReactant = reactant.getFormulaReactionComponent();
				Map<String, Integer> formulaProduct = product.getFormulaReactionComponent();
				
				
				
				if (formulaReactant.keySet().equals(formulaProduct.keySet())) {
					int contIgual = 0;
					String mayor = "";
					
					for (String element : formulaReactant.keySet()) {
						
						int numReactant = formulaReactant.get(element);
		                int numProduct = formulaProduct.get(element);
		                
		                int a = Math.max(numReactant, numProduct);
		                int b = Math.min(numReactant, numProduct);
		                
		                if (a == numReactant) {
		                	mayor = "reactant";
		                }else {
		                	mayor = "product";
		                }
		                
		                int mcmReactProduct = minimoComunMultiplo(a, b);
		                
		                if(mcm == 0) {
		                	mcm = mcmReactProduct;
		                	contIgual++;
		                }else {
		                	if(mcmReactProduct != mcm) {
		                		setIsBalanced(false);
		                		break;
		                	}else {
		                		contIgual++;
		                	}
		                }  
						
					}
					if(contIgual == formulaReactant.keySet().size()) {
						if(mayor.equals("reactant")) {
							reactant.setStoichiometry(mcm);
							if(isBalanced()) {
								setIsBalanced(true);
								reactionsBalanced.add(this);
								changedToBalanced = true;
							}
						}
						
					}
					
					
					
				}
			}
		}
		else if(diference.size() == 1) {
			for (Map.Entry<String, Integer> entry : diference.entrySet()) {
				String elem = entry.getKey();
				Integer differenceNum = entry.getValue();
				
				if (differenceNum > 0) {
					outerLoop1:
						for(ReactionComponent react : reactants) {
							Map<String, Integer> chemicalFormula = react.getFormulaReactionComponent();
							if(chemicalFormula.size() == 1) {
								for (Map.Entry<String, Integer> formula : chemicalFormula.entrySet()) {
									if(formula.getKey().equals(elem)) {
										react.setStoichiometry(differenceNum);
										reactionsBalanced.add(this);
										changedToBalanced =  true;
										break outerLoop1;
									}
								}
								
							}
							
						}
				}else {
				outerLoop2:
				for(ReactionComponent product: products) {
					Map<String, Integer> chemicalFormula = product.getFormulaReactionComponent();
					if(chemicalFormula.size() == 1) {
						for (Map.Entry<String, Integer> formula : chemicalFormula.entrySet()) {
							if(formula.getKey().equals(elem)) {
								product.setStoichiometry(differenceNum);
								reactionsBalanced.add(this);
								changedToBalanced =  true;
								break outerLoop2;
							}
						}
						
					}
					}
				}
				
			}
			
			
		}
		else if(diference.size() > 1) {
			boolean isEqual = false;
			for(ReactionComponent react : reactants) {
				Map<String, Integer> chemicalFormula = react.getFormulaReactionComponent();
				if (chemicalFormula.keySet().equals(diference.keySet())) {
					isEqual = true;
				
			}
			}
			for(ReactionComponent product: products) {
				Map<String, Integer> chemicalFormulaProduct = product.getFormulaReactionComponent();
				if (chemicalFormulaProduct.keySet().equals(diference.keySet())) {
					isEqual = true;
				
			}
				
			}
			
			if(!isEqual) {
				Metabolite meta = new Metabolite("new_id", "new_name", "c");
				String formulaString = "";
				for (Map.Entry<String, Integer> formula : diference.entrySet()) {
					String elem = formula.getKey();
					System.out.println(elem);
					Integer num = formula.getValue();
					System.out.println(num);
					if(num > 0) {
						formulaString = formulaString + elem + num;
					}
				}
				System.out.println("OUT");
				System.out.println(formulaString);
				meta.setChemicalFormula(formulaString);
				ReactionComponent reactionCom = new ReactionComponent(meta, 1);
				reactionCom.setFormulaReactionComponent(meta);
				addProduct(reactionCom);
			}
			
		}
			
		
		return changedToBalanced;
	
		
	}
	
	public static int maximoComunDivisor(int a, int b) {
        int temporal;
        while (b != 0) {
            temporal = b;
            b = a % b;
            a = temporal;
        }
        return a;
    }

    public static int minimoComunMultiplo(int a, int b) {
        // MCM(a, b) = (a * b) / MCD(a, b)
        return (a * b) / maximoComunDivisor(a, b);
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
