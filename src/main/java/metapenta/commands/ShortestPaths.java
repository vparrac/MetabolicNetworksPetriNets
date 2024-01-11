package metapenta.commands;

import metapenta.model.MetaPenta;
import metapenta.model.dto.ShortestPathsDTO;
import metapenta.tools.io.writers.ShortestPathWriter;

/**
 * The main method of class
 * args[0] the path of the XML file
 * args[1] initial metabolite to calculate shortest paths
 * args[2] Output file
 */
public class ShortestPaths {
    public static void main(String[] args) throws Exception {

        MetaPenta network = new MetaPenta(args[0]);
        ShortestPathsDTO paths = network.getShortestPaths(args[1]);

        ShortestPathWriter shortestPathWriter = new ShortestPathWriter(paths, args[2]);
        shortestPathWriter.write();
    }
}
