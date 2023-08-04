package metapenta.petrinet2;

import java.util.List;
import java.util.Map;

public interface PetriNet {
   public List<Place<?>> getSources();
   public List<Place<?>> getSinks();
   public List<Transition<?>> getTransitionsByAttribute(String attribute, String value);
}
