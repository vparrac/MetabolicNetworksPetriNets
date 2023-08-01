package metapenta.commands;

import metapenta.model.MetabolicNetwork;
import metapenta.model.MetabolicNetworkXMLLoader;
public class GapFIll {
    public static void main(String[] args) throws Exception {
        MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
        MetabolicNetwork network = loader.loadNetwork(args[0]);
        network.makeNet();

        network.GapFill(args[1]);
    }
}
