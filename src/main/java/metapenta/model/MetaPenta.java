package metapenta.model;

import metapenta.petrinet2.Edge;
import metapenta.petrinet2.Place;
import metapenta.petrinet2.Transition;
import metapenta.petrinet2.PetriNet;
import metapenta.service.ConnectedComponentsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            Reaction reaction = network.getReaction(key);
            Transition transition = this.createAndLoadTransitionToPetriNet(reaction);

            List<ReactionComponent> reactants = reaction.getReactants();
            List<Edge> edgesIn = this.loadMetabolitesAndCreateEdgeList(reactants);
            transition.AddEdgesIn(edgesIn);


            List<ReactionComponent> products = reaction.getProducts();
            List<Edge> edgesOut = this.loadMetabolitesAndCreateEdgeList(products);
            transition.AddEdgesOut(edgesOut);
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


    public ConnectedComponents connectedComponents() {
        ConnectedComponentsService ccService = new ConnectedComponentsService(this.petriNet);

        return ccService.getConnectedComponents();
    }

}
