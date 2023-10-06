package metapenta.commands;

import java.io.PrintStream;
import java.util.List;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.model.Reaction;

public class BalanceReaction {
	
	
	public static void main(String[] args) throws Exception{
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		List<Reaction> reactions = network.getReactionsBalance();
		try (PrintStream out = new PrintStream(args[1])) {
			out.print("{reactions:[");
			for (int i = 0; i < reactions.size(); i++) {				
				String s = (i>0)?",":"";
				out.print(s+reactions.get(i).toString());
			}
			out.print("]}");			
		}
	}
	
	

}
