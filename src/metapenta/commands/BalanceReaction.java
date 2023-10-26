package metapenta.commands;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.model.Reaction;

public class BalanceReaction {
	
	
	public static void main(String[] args) throws Exception{
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		//List<Reaction> reactionsBalanced = network.getReactionsBalanced();
		List<Reaction> reactionsUnbalanced = network.getReactionsUnbalanced();
		Map<Reaction, Map<String, String>> reactionsUnbalancedReason = network.reactionsUnbalancedReason(reactionsUnbalanced);
		//network.testBalanceo();
		try (PrintStream out = new PrintStream(args[1])) {
			out.print("Reaction id \t Name \t Reason why it is unbalance \t Sum of reactants coefficients \t Sum of products coefficients \t Difference between reactants and products \t \n ");

			for (Entry<Reaction, Map<String, String>> entry : reactionsUnbalancedReason.entrySet()) {
	            //System.out.println(entry.getKey() + ": " + entry.getValue());
				Reaction r = entry.getKey();
				out.print(r.getId() + " \t "+ r.getName() + " \t ");
				Map<String, String> reason = entry.getValue();
				for (Entry<String, String> reasonEntry : reason.entrySet()) {
			        String reasonUnbalanced = reasonEntry.getKey();
			        String[] sum = reasonEntry.getValue().split("\\|");
			        System.out.println(reasonEntry.getValue());
			        System.out.println(sum[0]);
			        out.print(reasonUnbalanced + " \t " +  sum[0] + " \t " + sum[1] + " \t " +  sum[2] + " \t \n ");
			    }
				
	        }
			

			
		}
		try (PrintStream out2 = new PrintStream(args[2])) {
			for (Entry<Reaction, Map<String, String>> entry : reactionsUnbalancedReason.entrySet()) {
	            //System.out.println(entry.getKey() + ": " + entry.getValue());
				Reaction r = entry.getKey();
				if(r.balanceReaction()) {
					out2.print(r.getId() + " \t "+ r.getName() + " \t ");
				}
				
			
		}
		
		
	}
	}
	
	

	}
