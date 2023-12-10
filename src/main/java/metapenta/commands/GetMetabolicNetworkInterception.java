package metapenta.commands;

import metapenta.model.MetabolicNetwork;
import metapenta.tools.io.loaders.MetabolicNetworkXMLLoader;
import metapenta.tools.io.writers.MetabolicNetworkJSONWriter;
import metapenta.tools.io.writers.Writer;

public class GetMetabolicNetworkInterception {

    public static void main(String[] args) throws Exception {
        MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();

        MetabolicNetwork firstNetwork = loader.loadNetwork(args[0]);
        MetabolicNetwork secondNetwork = loader.loadNetwork(args[1]);

        MetabolicNetwork resultNetwork = firstNetwork.interception(secondNetwork);

        Writer metabolicNetworkWriter = new MetabolicNetworkJSONWriter(resultNetwork, args[3]);
        metabolicNetworkWriter.write();
    }
}
