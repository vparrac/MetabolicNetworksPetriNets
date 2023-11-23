package metapenta.model;

import metapenta.model.dto.ConnectedComponentsDTO;
import metapenta.model.dto.NetworkBoundaryDTO;
import metapenta.petrinet2.Edge;
import metapenta.petrinet2.Place;
import metapenta.petrinet2.Transition;
import metapenta.petrinet2.PetriNet;
import metapenta.service.ConnectedComponentsService;
import metapenta.service.NetworkBoundaryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetaPenta{
    private MetabolicNetworkXMLLoader loader = new MetabolicNetworkXMLLoader();
    private PetriNet petriNet = new PetriNet();
    public MetaPenta(String networkFile){
        try {
            MetabolicNetwork network = loader.loadNetwork(networkFile);
            loadPetriNet(network);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void describeMetabolicNetwork(String outFilePrefix) throws Exception {
        petriNet.describeMetabolicNetwork(outFilePrefix);
    }

    private void loadPetriNet(MetabolicNetwork network){
        List<String> keysReaction = network.getReactionIds();
        for (String key : keysReaction) {
            loadReactionToPetriNet(network.getReaction(key));
        }
    }

    private void loadReactionToPetriNet(Reaction reaction) {
        Transition transition = this.createAndLoadTransitionToPetriNet(reaction);

        List<Edge> edgesIn = this.loadMetabolitesAndCreateEdgeList(reaction.getReactants());
        transition.AddEdgesIn(edgesIn);


        List<Edge> edgesOut = this.loadMetabolitesAndCreateEdgeList(reaction.getProducts());
        transition.AddEdgesOut(edgesOut);

        loadOutEdgesInPlacesOfTransition(transition);
        loadInEdgesInPlacesOfTransition(transition);
    }


    private void loadInEdgesInPlacesOfTransition(Transition transition) {
        List<Edge<Place>> edges = transition.getEdgesOut();
        for (Edge<Place> edge: edges) {
            Place place = edge.getTarget();

            Edge placeEdge = new Edge<>(transition, edge.getWeight());
            place.addEdgeIn(placeEdge);
        }
    }
    private void loadOutEdgesInPlacesOfTransition(Transition transition) {
        List<Edge<Place>> edges = transition.getEdgesIn();
        for (Edge<Place> edge: edges) {
            Place place = edge.getTarget();

            Edge placeEdge = new Edge<>(transition, edge.getWeight());
            place.addEdgeOut(placeEdge);
        }
    }

    private Transition createAndLoadTransitionToPetriNet(Reaction reaction){
        Transition transition = petriNet.getTransition(reaction.getId());

        if ( transition == null ){
            transition = new Transition(reaction.getId(), reaction.getName(), reaction);
            petriNet.AddTransition(reaction.getId(), transition);
        }

        return transition;
    }

    private List<Edge> loadMetabolitesAndCreateEdgeList(List<ReactionComponent> reactionComponents){
        List<Edge> edges = new ArrayList<>();
        for (ReactionComponent reactionComponent : reactionComponents) {
            Metabolite metabolite = reactionComponent.getMetabolite();

            Place<Metabolite> place = petriNet.getPlace(metabolite.getId());
            if (place == null){
                place = createAndAddPlaceToNet(metabolite);
            }

            Edge<Place> edge = new Edge(place, reactionComponent.getStoichiometry());
            edges.add(edge);
        }

        return edges;
    }


    private Place createAndAddPlaceToNet(Metabolite metabolite){
        Place<Metabolite> place = new Place<>(metabolite.getId(), metabolite.getName(), metabolite);
        petriNet.addPlace(metabolite.getId(), place);

        return place;
    }

    public ConnectedComponentsDTO connectedComponents() {
        ConnectedComponentsService connectedComponentsService = new ConnectedComponentsService(this.petriNet);

        return connectedComponentsService.getConnectedComponents();
    }

    public NetworkBoundaryDTO findNetworkBoundary() {
        NetworkBoundaryService networkBoundaryService = new NetworkBoundaryService(petriNet.getSinks(), petriNet.getSources());

        return networkBoundaryService.getNetworkBoundary();
    }

}
