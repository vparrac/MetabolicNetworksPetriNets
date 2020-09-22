package metapenta.visualization.petrinet;
/**
 * Class that represents a node that will be printed in the panel
 * @author Valerie Parra Cortés
 */
public class Node{
	/**
	 * x-coordinate
	 */
	private float px;
	/**
	 * y-coordinate
	 */
	private float py;
	/**
	 * Tuple with the color of place
	 */
	private RGBTuple color_place;
	/**
	 * Tuple with the color of the text 
	 */
	private RGBTuple color_text;
	/**
	 * Width
	 */
	private float w;
	/**
	 * Height
	 */
	private float h;	
	/**
	 * The name of the node
	 */
	private String name;
	
	/**
	 * Build a node with all its attributes
	 * @param x, the x-coordinate
	 * @param y, the y-coordinate
	 * @param w, the width
	 * @param h, the height
	 * @param n, The name of the node, the node will be display at the center of the node
	 * @param color_place, The color of the node
	 * @param color_text, The color of the text 
	 */
	
	public Node(float x, float y, float w, float h, String n, RGBTuple color_place, RGBTuple color_text) {
		this.px = x;
		this.py = y;			
		this.w = w;
		this.h = h;
		this.name = n;
		this.color_place=color_place;
		this.color_text=color_text;		
		
	}	
	/**
	 * Returns the x-coordinate
	 * @return x-coordinate
	 */
	public float getPx() {
		return px;
	}
	
	public void setPx(float px) {
		this.px = px;
	}
	public float getPy() {
		return py;
	}
	public void setPy(float py) {
		this.py = py;
	}
	public RGBTuple getColor_place() {
		return color_place;
	}
	public void setColor_place(RGBTuple color_place) {
		this.color_place = color_place;
	}
	public RGBTuple getColor_text() {
		return color_text;
	}
	public void setColor_text(RGBTuple color_text) {
		this.color_text = color_text;
	}
	public float getW() {
		return w;
	}
	public void setW(float w) {
		this.w = w;
	}
	public float getH() {
		return h;
	}
	public void setH(float h) {
		this.h = h;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}