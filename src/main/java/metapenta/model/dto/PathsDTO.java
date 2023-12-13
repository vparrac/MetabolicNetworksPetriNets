package metapenta.model.dto;

import metapenta.model.Metabolite;
import metapenta.model.Reaction;
import metapenta.model.petrinet2.Place;
import metapenta.model.petrinet2.Transition;

import java.util.ArrayList;
import java.util.List;

public class PathsDTO {
    List<List<Transition>> paths = new ArrayList<>();
    List<Place> initPlaces = new ArrayList<>();

    public void addPath(Place initPlace, List<Transition> reactions) {
        this.initPlaces.add(initPlace);
        this.paths.add(reactions);
    }

    public List<List<Transition>> getPaths() {
        return paths;
    }

    public List<Place> getInitPlaces() {
        return initPlaces;
    }
}
