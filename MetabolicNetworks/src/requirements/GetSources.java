package requirements;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import principal.MetabolicNetwork;
import principal.MetabolicNetworkXMLLoader;
import principal.Metabolite;

public class GetSources {
	/**
	 * The main method of class
	 * @param args[0] the path of the XML file of metabolic network
	 * @param args[1] the path of output file
	 */
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		network.makeNet();
		List<Metabolite> sinks = network.findSources();
		try (PrintStream out = new PrintStream("./out/"+args[1])) {
			out.print("{sources:[");
			for (int i = 0; i < sinks.size(); i++) {
				if(i==sinks.size()-1) {
					out.print(sinks.get(i).toString());
				}
				else {
					out.print(sinks.get(i).toString()+",");
				}				
			}
			out.print("]}");			
		}		
	}
}
