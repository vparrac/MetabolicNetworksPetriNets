package commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import model.MetabolicNetwork;
import model.MetabolicNetworkXMLLoader;
import model.Metabolite;

public class SinksFinder {
	/**
	 * The main method of class
	 * @param args[0] the path of the XML file of metabolic network
	 * @param args[1] the path of output file
	 */
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		MetabolicNetwork network = loader.loadNetwork(args[0]);
		network.makeNet();
		List<Metabolite> sinks = network.findSinks();
		try (PrintStream out = new PrintStream(args[1])) {
			out.print("{sinks:[");
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