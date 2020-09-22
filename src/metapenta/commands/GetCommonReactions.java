package metapenta.commands;

import java.io.IOException;
import java.util.List;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.model.Reaction;

public class GetCommonReactions {
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network1 = loader.loadNetwork(args[0]);
		MetabolicNetwork network2 = loader.loadNetwork(args[1]);
		List<Reaction> commonReations = network1.commonReactions(network2);
		for (Reaction reaction : commonReations) {
			System.out.println(reaction);
		}
	}

}
