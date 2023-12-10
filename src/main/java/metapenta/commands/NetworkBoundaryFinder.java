package metapenta.commands;

import metapenta.model.MetaPenta;
import metapenta.model.dto.NetworkBoundaryDTO;
import metapenta.tools.io.writers.NetworkBoundaryWriter;

public class NetworkBoundaryFinder {
    public static void main(String[] args) throws Exception {
        MetaPenta network = new MetaPenta(args[0]);
        NetworkBoundaryDTO networkBoundary = network.findNetworkBoundary();

        NetworkBoundaryWriter boundaryWriter = new NetworkBoundaryWriter(networkBoundary, args[1]);
        boundaryWriter.write();
    }
}
