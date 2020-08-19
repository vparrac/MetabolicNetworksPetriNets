package metapenta.commands;

import java.io.IOException;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;

public class Tester {

	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork("./data/e_coli_core.xml");
		network.makeNet();
		
	}
}
