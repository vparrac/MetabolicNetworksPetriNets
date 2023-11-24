package metapenta.service;

import metapenta.model.Metabolite;
import metapenta.model.Reaction;
import metapenta.model.dto.MetaboliteReactionsDTO;
import metapenta.model.petrinet2.Edge;
import metapenta.model.petrinet2.PetriNet;
import metapenta.model.petrinet2.Place;
import metapenta.model.petrinet2.Transition;

import java.util.ArrayList;
import java.util.List;

public class MetaboliteReactionsService {

    private static final String IS_PRODUCT = "is_product";

    private static final String IS_SUBSTRATE = "is_substrate";

    private PetriNet petriNet;

    private Metabolite metabolite;

    public MetaboliteReactionsService(PetriNet petriNet, Metabolite metabolite) {
        this.petriNet = petriNet;
        this.metabolite = metabolite;
    }

    public MetaboliteReactionsDTO getMetaboliteReactions() {
        List<Reaction> isSubstrate = getReactionsByCriteria(IS_SUBSTRATE);
        List<Reaction> isProduct = getReactionsByCriteria(IS_PRODUCT);
        return new MetaboliteReactionsDTO(isSubstrate, isProduct);
    }

    private List<Reaction> getReactionsByCriteria(String criteria){
        List<Reaction> reactions = new ArrayList<>();
        Place placeOfMetabolite = petriNet.getPlaces().get(metabolite.getId());

        List<Edge<Transition<Reaction>>> edges = getEgeByCriteria(placeOfMetabolite, criteria);
        for(Edge<Transition<Reaction>> edge: edges) {
            reactions.add(edge.getTarget().getObject());
        }

        return reactions;
    }

    private List<Edge<Transition<Reaction>>> getEgeByCriteria(Place place , String criteria) {
        switch (criteria) {
            case (IS_PRODUCT):
                return place.getEdgesIn();
            case (IS_SUBSTRATE):
                return place.getEdgesOut();
        }

        return new ArrayList<>();
    }
}
