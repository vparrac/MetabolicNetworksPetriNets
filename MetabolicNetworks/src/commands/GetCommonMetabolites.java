package commands;

import java.io.IOException;
import java.util.List;

import model.MetabolicNetworkXMLLoader;
import model.Metabolite;

/**
 * Diferentes metabolitos
 * @author vparrac
 */
public class GetCommonMetabolites {
	public static void main(String[] args) throws IOException {
		MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
		List<Metabolite> network1 = loader.loadNetwork(args[0]).getMetabolitesAsList();
		List<Metabolite> network2 = loader.loadNetwork(args[1]).getMetabolitesAsList();
	
	}
}
