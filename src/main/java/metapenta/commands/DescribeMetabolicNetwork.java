package metapenta.commands;

import metapenta.model.*;
import metapenta.tools.io.DescribeNetworkWriter;

/**
 * args[0]: Metabolic network file
 * args[1]: Ouput file prefixes
 */
public class DescribeMetabolicNetwork {
    public static void main(String[] args) throws Exception {
        MetaPenta network = new MetaPenta(args[0]);

        DescribeNetworkWriter networkWriter = new DescribeNetworkWriter(network.getPetriNet(), args[1]);
        networkWriter.write();
    }
}
