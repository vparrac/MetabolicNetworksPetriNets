package metapenta.commands;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import metapenta.model.MetaPenta;
import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
/**
 * Class to test connected components method
 * @author Valerie Parra Cort�s
 */
public class ConnectedComponents {

	/**
	 * Class to test the conncected components of the metabolic networkd
	 * @param args [0] the path of the XML file and args[1] out file in csv
	 * @throws Exception if exists any error of I/O
	 */
	public static void main(String[] args) throws Exception {
		MetaPenta network = new MetaPenta(args[0]);
		network.connectedComponents();
	}
}
