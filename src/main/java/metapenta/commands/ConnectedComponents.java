package metapenta.commands;

import metapenta.model.ConnectedComponentsDTO;
import metapenta.model.MetaPenta;
import metapenta.tools.io.ConnectedComponentsWriter;

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

		ConnectedComponentsDTO connectedComponents = network.connectedComponents();

		ConnectedComponentsWriter connectedComponentsWriter = new ConnectedComponentsWriter(connectedComponents, args[1]);
		connectedComponentsWriter.write();

	}
}
