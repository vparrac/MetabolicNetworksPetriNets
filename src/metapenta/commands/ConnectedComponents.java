package metapenta.commands;
import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;

public class ConnectedComponents {

	/**
	 * The main method of class
	 * @param args[0] the path of the XML file 
	 * @param args[1] The initial metabolites separated by comma
	 * @param args[2] Target metabolite
	 * @param args[3] fileName1 Metabolic Network In CSV	
	 * @throws Exception if exists any error of I/O
	 */
	public static void main(String[] args) throws Exception {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		network.makeNet();
		network.connectedComponents();		
	}
}
