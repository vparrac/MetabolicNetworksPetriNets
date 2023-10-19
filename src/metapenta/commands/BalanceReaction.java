package metapenta.commands;

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
//		if(reactionsBalanced.size()> 0) {
//			
//		}
		//network.testBalanceo();
		try (PrintStream out = new PrintStream(args[1])) {
			out.print("Reaction id | Name | Reason why it is unbalance | Sum of reactants coefficients | Sum of products coefficients | Difference between reactants and products | \n ");
//			for (int i = 0; i < reactionsUnbalanced.size(); i++) {				
//				String s = (i>0)?",":"";
//				out.print(s+reactionsUnbalanced.get(i).toString());
//			}
//			out.print("]}");
//			out.print("{reactionsBalanced:[");
//			for (int i = 0; i < reactionsBalanced.size(); i++) {				
//				String s = (i>0)?",":"";
//				out.print(s+reactionsBalanced.get(i).toString());
//			}
			//out.print("]}");
			for (Entry<Reaction, Map<String, String>> entry : reactionsUnbalancedReason.entrySet()) {
	            //System.out.println(entry.getKey() + ": " + entry.getValue());
				Reaction r = entry.getKey();
				out.print(r.getId() + " | "+ r.getName() + " | ");
				Map<String, String> reason = entry.getValue();
				for (Entry<String, String> reasonEntry : reason.entrySet()) {
			        String reasonUnbalanced = reasonEntry.getKey();
			        String[] sum = reasonEntry.getValue().split("\\|");
			        System.out.println(reasonEntry.getValue());
			        System.out.println(sum[0]);
			        out.print(reasonUnbalanced + " | " +  sum[0] + " | " + sum[1] + " | " +  sum[2] + " | \n ");
			    }
				
	        }
			

			
		}
	}
	
	

}
