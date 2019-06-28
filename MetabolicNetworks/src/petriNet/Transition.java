package petriNet;
import java.util.ArrayList;
import java.util.List;
import principal.GeneProduct;
public class Transition {
	private int number;
	private List<Edge> in;
	private List<Edge> out;
	private List<GeneProduct> geneProduct;	
	private String name;
	public Transition(List<GeneProduct> gp,int number, String name) {
		this.in = new ArrayList<>();
		this.out = new ArrayList<>();
		this.geneProduct=gp;
		this.number=-1;
		this.number=number;
		this.name=name;
	}	
	public void addEdgeIn(Edge edge) {
		in.add(edge);
	}	
	public void addEdgeOut(Edge edge) {
		out.add(edge);
	}	
	public List<Edge> getIn() {
		return in;
	}
	public List<Edge> getOut() {
		return out;
	}	
	public List<GeneProduct> getGeneProduct() {
		return geneProduct;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumber() {
		return number;
	}
	public String getName() {
		return name;
	}
}