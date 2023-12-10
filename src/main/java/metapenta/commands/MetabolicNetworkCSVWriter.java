package metapenta.commands;
import metapenta.model.MetabolicNetwork;
import metapenta.tools.io.loaders.MetabolicNetworkXMLLoader;

/**
 * Class to test the metabolic network CSV Writer
 * @author Valerie Parra Cort�s
  */
public class MetabolicNetworkCSVWriter {
	/**
	 * The main method of class
	 * args[0] the path of the XML file of metabolic network
	 * args[1] Name of file out
	 * @throws Exception if exists any error of I/O
	 */
	public static void main(String[] args) throws Exception {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		network.makeNet();
		network.printAllMetabolicNetworkInCSV(args[1]);
		
	}
}
