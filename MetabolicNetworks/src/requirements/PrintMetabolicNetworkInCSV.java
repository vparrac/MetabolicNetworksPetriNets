package requirements;

import java.io.IOException;

import principal.MetabolicNetwork;
import principal.MetabolicNetworkXMLLoader;

public class PrintMetabolicNetworkInCSV {
	/**
	 * The main method of class
	 * @param args[0] the path of the XML file of metabolic network
	 * @param args[1] Name of file out
	 * @throws Exception if exists any error of I/O
	 */
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		network.makeNet();
		network.printAllMetabolicNetworkInCSV("./out/"+args[1]);
		
	}
}
