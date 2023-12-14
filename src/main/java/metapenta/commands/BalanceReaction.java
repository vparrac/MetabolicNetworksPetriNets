package metapenta.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import metapenta.model.MetabolicNetworkXMLOutput;
import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.model.Reaction;

public class BalanceReaction {
	private String networkFile;
	private String outPrefix;
	
	
	
	public String getNetworkFile() {
		return networkFile;
	}

	public void setNetworkFile(String networkFile) {
		this.networkFile = networkFile;
	}

	public String getOutPrefix() {
		return outPrefix;
	}

	public void setOutPrefix(String outPrefix) {
		this.outPrefix = outPrefix;
	}

	public static void main(String[] args) throws Exception{
		BalanceReaction instance = new BalanceReaction();
		instance.networkFile = args[0];
		instance.outPrefix = args[1];
		instance.run();
		
		

	}

	public void run() throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(networkFile);
		//List<Reaction> reactionsBalanced = network.getReactionsBalanced();
		List<Reaction> reactionsUnbalanced = network.getReactionsUnbalanced();
		Map<Reaction, Map<String, String>> reactionsUnbalancedReason = network.reactionsUnbalancedReason(reactionsUnbalanced);
		String reportFile = outPrefix+"_report.tsv";
		//network.testBalanceo();
		try (PrintStream out = new PrintStream(reportFile)) {
			out.print("Reaction id \t Name \t Reason why it is unbalance \t Sum of reactants coefficients \t Sum of products coefficients \t Difference between reactants and products \t It was balanced \t What was modified \t Sum of reactants coefficients \t Sum of products coefficients \t Difference between reactants and products \n ");

			for (Entry<Reaction, Map<String, String>> entry : reactionsUnbalancedReason.entrySet()) {
	            //System.out.println(entry.getKey() + ": " + entry.getValue());
				Reaction r = entry.getKey();
				out.print(r.getId() + "\t "+ r.getName() + "\t");
				Map<String, String> reason = entry.getValue();
				for (Entry<String, String> reasonEntry : reason.entrySet()) {
			        String reasonUnbalanced = reasonEntry.getKey();
			        String[] sum = reasonEntry.getValue().split("\\|");
			        System.out.println(reasonEntry.getValue());
			        System.out.println(sum[0]);
			        out.print(reasonUnbalanced + " \t " +  sum[0] + " \t " + sum[1] + " \t " +  sum[2] + " \t ");
			    }
				
				for (Entry<Boolean, String> balanceEntry : r.balanceReaction().entrySet()) {
					if(balanceEntry.getKey()) {
						out.print("YES \t ");
						out.print(balanceEntry.getValue()+ "\t ");
						List<Map<String, Integer>> reacts = r.getListElements(r.getReactants());
						Map<String, Integer> sumreacts = r.getSumReactants();
						for (Entry<String, Integer> entry1 : sumreacts.entrySet()) {
							out.print("{ "+ entry1.getKey() + ": " + entry1.getValue() + "}");
						}
						out.print(" \t ");
						
						List<Map<String, Integer>> products = r.getListElements(r.getProducts());
						Map<String, Integer> sumproducts = r.getSumProducts();
						for (Entry<String, Integer> entry2 : sumproducts.entrySet()) {
							out.print("{ "+entry2.getKey() + ": " + entry2.getValue()+ "}");
							System.out.println("PRODUCTS");
							System.out.println("{ "+entry2.getKey() + ": " + entry2.getValue()+ "}");
						}
						out.print(" \t ");
						
						Map<String, Integer> difference = r.getDifference();
						for (Entry<String, Integer> entry3 : difference.entrySet()) {
							out.print("{ "+entry3.getKey() + ": " + entry3.getValue()+ "}");
						}
						
						out.print(" \t \n");
						
						
					}
					else {
						out.print("NO \t ");
						out.print(balanceEntry.getValue() + " \t ");
						out.print(" - \t - \t - \t - \t \n");
						break;
					}
				}
				
			
			}

			
		}
		
		MetabolicNetworkXMLOutput output = new MetabolicNetworkXMLOutput();
		output.saveNetwork(network, outPrefix+"_network.xml");
			
			
		
	}
	
	

}
