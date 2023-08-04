package metapenta.petrinet2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PetriNetImp implements PetriNet {
    private Map<String, Place<?>> places;
    private Map<String, Transition<?>> transitions;

    public PetriNetImp(){
        this.places = new TreeMap<>();
        this.transitions = new TreeMap<>();
    }

    public Map<String, Transition<?>> getTransitions() {
        return transitions;
    }

    public Map<String, Place<?>> getPlaces() {
        return places;
    }

    public void AddPlace(String id, Place<?> place){
        this.places.put(id, place);
    }

    public void AddTransition(String id, Transition<?> transition){
        this.transitions.put(id, transition);
    }

    public void setTransitions(Map<String, Transition<?>> transitions) {
        this.transitions = transitions;
    }

    @Override
    public List<Place<?>> getSources() {
        return getPlacesByStatus(Place.SOURCE);
    }

    @Override
    public List<Place<?>> getSinks() {
        return getPlacesByStatus(Place.SINK);
    }

    @Override
    public List<Transition<?>> getTransitionsByAttribute(String attribute, String value) {
        List<Transition<?>> sources = new ArrayList<>();
        for (String key : transitions.keySet()) {
            if (transitions.get(key).stringAttributeFieldValueMatch(attribute, value)){
                sources.add(transitions.get(key));
            }
        }
        return sources;
    }

    private List<Place<?>> getPlacesByStatus(String status){
        List<Place<?>> sources = new ArrayList<>();
        for (String key : places.keySet()) {
            if (places.get(key).isStatus(status)){
                sources.add(places.get(key));
            }
        }
        return sources;
    }


}
