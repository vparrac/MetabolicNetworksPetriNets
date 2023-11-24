package metapenta.model.dto;

import metapenta.model.Metabolite;

import java.util.List;

public class NetworkBoundaryDTO {

    private List<Metabolite> sinks;

    private List<Metabolite> sources;
    public NetworkBoundaryDTO(List<Metabolite> sinks, List<Metabolite> sources){
        this.sinks = sinks;
        this.sources = sources;
    }

    public List<Metabolite> getSinks() {
        return sinks;
    }

    public void setSources(List<Metabolite> sources) {
        this.sources = sources;
    }
}
