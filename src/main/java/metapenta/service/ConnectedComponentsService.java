package metapenta.service;

import metapenta.model.ConnectedComponents;
import metapenta.model.Metabolite;
import metapenta.model.Reaction;
import metapenta.petrinet2.Edge;
import metapenta.petrinet2.PetriNet;
import metapenta.petrinet2.Place;
import metapenta.petrinet2.Transition;

import java.util.*;

public class ConnectedComponentsService {
    private static final String DOWN_CRITERIA = "DOWN";

    private static final String UP_CRITERIA = "UP";
    private PetriNet petriNet;
    private Map<String, Transition<Reaction>> transitions;
    private Map<String, Place<Metabolite>> places;

    private Map<Integer, Place> connectedComponentsPlaces = new HashMap<>();
    private Map<Integer, Transition> connectedComponentsTransitions = new HashMap<>();

    private int[] placesVisited;
    private int[] transitionsVisited;

    private int connectedComponentCurrentId = 0;

    private Transition currentTransition;

    public ConnectedComponentsService(PetriNet petriNet){
        this.petriNet = petriNet;
        this.transitions = petriNet.getTransitions();
        this.places = petriNet.getPlaces();

        this.placesVisited = new int[places.size()];
        this.transitionsVisited = new int[transitions.size()];
    }

    public ConnectedComponents getConnectedComponents(){
        calculateConnectedComponents();
        return new ConnectedComponents(connectedComponentsPlaces, connectedComponentsTransitions);
    }



    private void calculateConnectedComponents() {
        Set<String> transitionsKeys = transitions.keySet();
        for(String transitionId: transitionsKeys) {
            Transition<Reaction> transition = transitions.get(transitionId);
            if(placesVisited[transition.getObject().getNid()] == 0) {
                visitTransition(transition);
                connectedComponentCurrentId++;
            }
        }
    }

    private void visitTransition(Transition transition) {
        this.currentTransition = transition;
        markTransitionAsVisitedAndAssignGroupId(transition);
        visitTransitions(transition, DOWN_CRITERIA);
        visitTransitions(transition, UP_CRITERIA);
        assignGroupIdToTransitionPlaces(transition);
    }

    private void markTransitionAsVisitedAndAssignGroupId(Transition<Reaction> transition){
        transitionsVisited[transition.getObject().getNid()] = 1;
        connectedComponentsTransitions.put(connectedComponentCurrentId, transition);
    }

    private void visitTransitions(Transition<Reaction> transition, String criteria){
        List<Transition> downTransitions = getTransitions(transition, criteria);
        for(Transition downTransition: downTransitions) {
            visitTransition(downTransition);
        }
    }
    private void assignGroupIdToTransitionPlaces(Transition transition) {
        List<Edge<Place>> edgesIn = transition.getAllEdges();

        for (Edge<Place> edge: edgesIn) {
            Place place = edge.getTarget();
            assignGroupIdToPlace(place);
        }
    }

    private void assignGroupIdToPlace(Place place){
        connectedComponentsPlaces.put(connectedComponentCurrentId, place);
    }

    private List<Transition> getTransitions(Transition<Reaction> transition, String criteria) {
        List<Transition> transitions = new ArrayList<>();

        for (Edge edge: transition.getEdgesOut()) {
            Place place = (Place) edge.getTarget();
            List<Edge> placeEdges = getPlaceEdgeByCriteria(place, criteria);
            for (Edge placeEdge: placeEdges) {
                Transition t = (Transition) placeEdge.getTarget();
                transitions.add(t);
            }
        }

        return transitions;
    }


    private List<Edge> getPlaceEdgeByCriteria(Place place, String criteria) {
        switch (criteria){
            case DOWN_CRITERIA:
               return place.getEdgesOut();
            case UP_CRITERIA:
               return place.getEdgesIn();
        }

        return null;
    }
}
