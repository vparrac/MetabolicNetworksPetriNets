package metapenta.commands;

import metapenta.model.dto.ConnectedComponentsDTO;
import metapenta.model.MetaPenta;
import metapenta.tools.io.ConnectedComponentsWriter;

public class ConnectedComponents {
	public static void main(String[] args) throws Exception {
		MetaPenta network = new MetaPenta(args[0]);

		ConnectedComponentsDTO connectedComponents = network.connectedComponents();

		ConnectedComponentsWriter connectedComponentsWriter = new ConnectedComponentsWriter(connectedComponents, args[1]);
		connectedComponentsWriter.write();
	}
}
