package metapenta.petrinet2;

import java.util.ArrayList;
import java.util.List;

public class Transition<A extends Attributes> {
    private String ID;
    private String label;
    private Attributes attributes;
    List<Edge> edgesIn;
    List<Edge> edgesOut;

    public Transition(String id, String label, Attributes attributes) {
        ID = id;
        this.label = label;
        this.attributes = attributes;
        this.edgesIn = new ArrayList<>();
        this.edgesOut = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes){
        this.attributes = attributes;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public void AddEdgeIn(Edge<?> transition){
        this.edgesIn.add(transition);
    }
    public void AddEdgeOut(Edge<?> transition){
        this.edgesOut.add(transition);
    }

    public boolean stringAttributeFieldValueMatch(String field, String value){
        return attributes.fieldValueMatch(field, value);
    }
}
