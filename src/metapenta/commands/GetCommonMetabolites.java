package metapenta.commands;

import java.io.IOException;
import java.util.List;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
import metapenta.model.Metabolite;

/**
 * Diferentes metabolitos
 * @author vparrac
 */
public class GetCommonMetabolites {
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network1 = loader.loadNetwork(args[0]);
		MetabolicNetwork network2 = loader.loadNetwork(args[1]);
		
		List<Metabolite> commonMetabolites = network1.commonMetabolites(network2);
		for (Metabolite metabolite : commonMetabolites) {
			System.out.println(metabolite);
		}
	}
}
