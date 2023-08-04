package metapenta.petrinet2;

public class Edge<Target>{
    private Target target;
    private int weight;
    public Edge(Target target, int weight) {
        this.target = target;
        this.weight = weight;
    }
    public Target getTarget() {
        return target;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setTarget(Target target){
         this.target = target;
    }
    public void setWeight(int weight){
        this.weight = weight;
    }

}
