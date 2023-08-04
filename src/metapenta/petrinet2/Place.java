package metapenta.petrinet2;

import java.util.ArrayList;
import java.util.List;

public class Place<Attribute> {
    public static final String SINK = "SINK";
    public static final String SOURCE = "SOURCE";

    private String Id;
    private String label;
    private Attribute attributes;
    List<Edge<?>> edgesIn;
    List<Edge<?>> edgesOut;

    public Place(String id, String label, Attribute attributes) {
        Id = id;
        this.label = label;
        this.attributes = attributes;
        this.edgesIn = new ArrayList<>();
        this.edgesOut = new ArrayList<>();
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setId(String ID) {
        this.Id = ID;
    }
    public void setAttributes(Attribute attributes){
        this.attributes = attributes;
    }
    public String getLabel() {
        return label;
    }

    public List<Edge<?>> getEdgesIn() {
        return edgesIn;
    }

    public List<Edge<?>> getEdgesOut() {
        return edgesOut;
    }

    public String getID() {
        return Id;
    }
    public Attribute getAttributes() {
        return attributes;
    }
    public void AddEdgeIn(Edge transition){
        this.edgesIn.add(transition);
    }
    public void AddEdgeOut(Edge transition){
        this.edgesOut.add(transition);
    }

    public boolean isStatus(String status){
        return switch (status) {
            case SINK -> isSource();
            case SOURCE -> isSink();
            default -> false;
        };
    }

    public boolean isSource(){
        return edgesIn.isEmpty();
    }

    public boolean isSink(){
        return edgesOut.isEmpty();
    }

}
