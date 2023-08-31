package metapenta.commands;

import metapenta.model.*;
import metapenta.petrinet2.Place;

import java.util.Map;

public class GapFIll {
    public static void main(String[] args) throws Exception {
        IMetaPenta network = new MetaPenta(args[0]);
        network.describeMetabolicNetwork(args[1]);
    }
}
