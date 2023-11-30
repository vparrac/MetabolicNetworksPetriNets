package metapenta.model.dto;

import metapenta.model.MetabolicNetwork;
import metapenta.model.Metabolite;
import metapenta.model.Reaction;

import java.util.List;

public class NetworksOverlappingDTO {

    private List<Metabolite> metabolites;
    private List<Reaction> reactions;

    private MetabolicNetwork metabolicNetwork1;

    private MetabolicNetwork getMetabolicNetwork2;

    public NetworksOverlappingDTO(MetabolicNetwork metabolicNetwork1, MetabolicNetwork metabolicNetwork2) {
        this.getMetabolicNetwork2 = metabolicNetwork2;
        this.metabolicNetwork1 = metabolicNetwork1;
    }

}
