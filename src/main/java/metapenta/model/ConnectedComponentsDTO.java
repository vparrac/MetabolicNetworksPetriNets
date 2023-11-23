package metapenta.model;

import java.util.List;
import java.util.Map;

public class ConnectedComponentsDTO {
    private Map<Integer, List<Metabolite>> connectedComponentsPlaces;
    private Map<Integer, List<Reaction>> connectedComponentsTransitions;

    public ConnectedComponentsDTO(Map<Integer, List<Metabolite>> connectedComponentsPlaces, Map<Integer, List<Reaction>> connectedComponentsTransitions ) {
        this.connectedComponentsPlaces = connectedComponentsPlaces;
        this.connectedComponentsTransitions = connectedComponentsTransitions;
    }
    public Map<Integer, List<Metabolite>> getConnectedMetabolites() {
        return connectedComponentsPlaces;
    }

    public Map<Integer, List<Reaction>> getConnectedComponentsTransitions() {
        return connectedComponentsTransitions;
    }
}
