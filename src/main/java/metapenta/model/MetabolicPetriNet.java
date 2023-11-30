package metapenta.model;

import metapenta.model.petrinet2.PetriNet;

import java.util.*;

public class MetabolicPetriNet extends PetriNet {

    private Map<String, List<Metabolite>> metabolitesByCompartment = new HashMap<>();
    public List<String> getReversibleReactionsIds(){
        List<String> reactionIds = new ArrayList();
        Set<String> keys = transitions.keySet();

        for(String key: keys) {
            Reaction reaction = transitions.get(key).getObject();
            if(reaction.isReversible()){
                reactionIds.add(reaction.getId());
            }
        }

        return reactionIds;
    }

    public List<String> getIrreversibleReactionsIds(){
        List<String> reactionIds = new ArrayList();
        Set<String> keys = transitions.keySet();

        for(String key: keys) {
            Reaction reaction = transitions.get(key).getObject();
            if(!reaction.isReversible()){
                reactionIds.add(reaction.getId());
            }
        }

        return reactionIds;
    }


    public Map<String, List<Metabolite>> getReactionsByCompartments() {
        Set<String> placesKeys = places.keySet();

        for(String key: placesKeys) {
            Metabolite metabolite = places.get(key).getObject();
            List<Metabolite> compartmentMetabolites = metabolitesByCompartment.get(metabolite.getCompartment());
            if (compartmentMetabolites == null){
                compartmentMetabolites = new ArrayList<>();
                metabolitesByCompartment.put(metabolite.getCompartment(), compartmentMetabolites);
            }

            compartmentMetabolites.add(metabolite);
        }

        return metabolitesByCompartment;
    }

}
