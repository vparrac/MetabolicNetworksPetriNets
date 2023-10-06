package metapenta.model;

import metapenta.petrinet2.Edge;
import metapenta.petrinet2.Place;
import metapenta.petrinet2.Transition;
import metapenta.petrinet2.PetriNet;

import java.io.IOException;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.List;

public class MetaPenta implements IMetaPenta{
    private MetabolicNetworkXMLLoader loader;
    private PetriNet petriNet;
    private MetabolicNetwork network;
    public MetaPenta(String networkFile){
        try {
            loader = new MetabolicNetworkXMLLoader();
            petriNet = new PetriNet();
            network = loader.loadNetwork(networkFile);
            loadPetriNet(network);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void describeMetabolicNetwork(String outFilePrefix) throws Exception {
        petriNet.describeMetabolicNetwork(outFilePrefix);
    }

    @Override
    public void sourcesFinder(String outFilePrefix) throws Exception {
        //petriNet.getSources(outFilePrefix);
        
    }
    @Override
    public void getSinks(String outFilePrefix) throws Exception {
        petriNet.getSinks();
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

            Edge<Place> edge = new Edge<>(place, reactionComponent.getStoichiometry());
            edges.add(edge);
        }

        return edges;
    }

    private Place createAndAddPlaceToNet(Metabolite metabolite){
        Place<Metabolite> place = new Place<>(metabolite.getId(), metabolite.getName(), metabolite);
        petriNet.addPlace(metabolite.getId(), place);

        return place;
    }


}
