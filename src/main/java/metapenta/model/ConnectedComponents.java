package metapenta.model;

import metapenta.petrinet2.Place;
import metapenta.petrinet2.Transition;

import java.util.HashMap;
import java.util.Map;

public class ConnectedComponents {
    private Map<Integer, Place> connectedComponentsPlaces;
    private Map<Integer, Transition> connectedComponentsTransitions;

    public ConnectedComponents(Map<Integer, Place> connectedComponentsPlaces, Map<Integer, Transition> connectedComponentsTransitions ) {
        this.connectedComponentsPlaces = connectedComponentsPlaces;
        this.connectedComponentsTransitions = connectedComponentsTransitions;
    }
    public Map<Integer, Place> getConnectedComponentsPlaces() {
        return connectedComponentsPlaces;
    }

    public Map<Integer, Transition> getConnectedComponentsTransitions() {
        return connectedComponentsTransitions;
    }
}
