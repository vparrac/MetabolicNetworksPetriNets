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
		List<Reaction> reactionsBalanced = network.getReactionsBalanced();
		List<Reaction> reactionsUnbalanced = network.getReactionsUnbalanced();
		Map<Reaction, String> reactionsUnbalancedReason = network.reactionsUnbalancedReason(reactionsUnbalanced);
//		if(reactionsBalanced.size()> 0) {
//			
//		}
		//network.testBalanceo();
		try (PrintStream out = new PrintStream(args[1])) {
			out.print("{reactionsUnbalanced:[");
			for (int i = 0; i < reactionsUnbalanced.size(); i++) {				
				String s = (i>0)?",":"";
				out.print(s+reactionsUnbalanced.get(i).toString());
			}
			out.print("]}");
//			out.print("{reactionsBalanced:[");
//			for (int i = 0; i < reactionsBalanced.size(); i++) {				
//				String s = (i>0)?",":"";
//				out.print(s+reactionsBalanced.get(i).toString());
//			}
			out.print("]}");
			out.print("{reactionsUnbalanced:[");
			for (Entry<Reaction, String> entry : reactionsUnbalancedReason.entrySet()) {
	            //System.out.println(entry.getKey() + ": " + entry.getValue());
				out.print(entry.getKey().getName() +": "+ entry.getValue() +", ");
	        }
			

			
		}
	}
	
	

}
