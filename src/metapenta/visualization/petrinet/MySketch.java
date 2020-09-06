package metapenta.visualization.petrinet;
import java.util.ArrayList;
/**
 * Main class of the visualization panel this class extends of PApplet
 * the main class of processing library
 * @author Valerie Parra Cortés
 */
import processing.core.PApplet;
public class MySketch extends PApplet {
	/**
	 * It represents id a user is over a note
	 */
	private boolean locked=false;;
	// Colors	
	private final static RGBTuple BLUE = new RGBTuple(3, 152, 158); 
	private static final RGBTuple BLUE_KING = new RGBTuple(0, 74, 173);
	private static final RGBTuple ORANGE = new RGBTuple(255, 145, 77);
	private static final RGBTuple WHITE = new RGBTuple(255, 255, 255);
		
	/**
	 * Number of pixels of the boxes
	 */
	private final static int BS = 50;
	/**
	 * is it the user over a node?
	 */
	private boolean bover = false;
	/**
	 * Whis is the postion of the current node?
	 */
	int whichImage;
	/**
	 * The position of the transitions
	 */
	private ArrayList<Transition> positionTransitions = new ArrayList<Transition>();
	/**
	 * The location of places
	 */
	private ArrayList<Place> positionsPlaces = new ArrayList<Place>();
	/**
	 * The offset of the mouse and the center of the node in x-axis
	 */
	private float xOffset = 0;
	/**
	 * The offset of the mouse and the center of the node in y-axis
	 */
	private float yOffset = 0;
	/**
	 * Is the current node a transition?
	 */
	private boolean isTransition=false;
	/**
	 * Radius of the places
	 */
	private int redius=50;	
	/**
	 * The main method of the Sketch class
	 * @param args 
	 */
	public static void main(String[] args) {
		PApplet.main(MySketch.class.getName());
	}
	public void settings() {
		size(640, 640); //The size of the windos
		smooth();
		if (frame != null) {
			frame.setResizable(true);
		}
	}	 
	
	/**
	 * This method print an arrow
	 * @param x1 the first coordinate of the arrow's start
	 * @param y1 the seconds coordinate of arrow's start
	 * @param x2 the first coordinate of  arrow's end
	 * @param y2 the seconds coordinate of arrow's end
	 */
	public void arrow(int x1, int y1, int x2, int y2) {
		line(x1, y1, x2, y2);
		pushMatrix();
		translate(x2, y2);
		float a = atan2(x1-x2, y2-y1);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		popMatrix();
	}

	public void setup() {
		positionTransitions.add(new Transition(100, 200, BS, BS, "1", BLUE_KING, WHITE));
		positionTransitions.add(new Transition(100, 200, BS, BS, "2", BLUE_KING, WHITE));
		positionsPlaces.add(new Place(200, 300, BS, BS, "1", ORANGE, WHITE));	
		positionsPlaces.add(new Place(200, 300, BS, BS, "2", ORANGE, WHITE));	
	}
	
	public void draw() {		
		background(255, 255, 255);
		fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);
		int x1=200;
		int y1=20;
		int x2=80;
		int y2=100;
		line(x1, y1, x2, y2);
		pushMatrix();
		translate(x2, y2);
		float a = atan2(x1-x2, y2-y1);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		popMatrix();
		stroke(126);	
		for (int j=0; j < positionTransitions.size(); j++) {
			fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);
			rect ( positionTransitions.get(j).getPx(), positionTransitions.get(j).getPy(), BS, BS) ;
			stroke(153);
		}

		for (int j=0; j < positionsPlaces.size(); j++) {
			fill(ORANGE.r,ORANGE.g,ORANGE.b);
			ellipse( positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(), redius, redius);			
		}
	}
	
	public void mousePressed() {
		
		checkOver();
		if (bover) { 
			locked = true;
			Node currentNodes = (isTransition)? positionTransitions.get(whichImage):positionsPlaces.get(whichImage);
			xOffset = mouseX-currentNodes.getPx(); 
			yOffset = mouseY-currentNodes.getPy();
		} 
		else {
			locked = false;			
		}
	}

	public void mouseReleased() {
		locked = false;
		bover = false;
	}

	public void mouseDragged() {
		if(locked) {
			Node currentNode=(isTransition)? positionTransitions.get(whichImage):positionsPlaces.get(whichImage);
			currentNode.setPx(mouseX-xOffset);
			currentNode.setPy(mouseY-yOffset);
		}
	}
	void checkOver() {
		boolean found=false;
		for (int i = 0; i <positionsPlaces.size() ; i++) {			
			if (Math.sqrt(Math.pow((mouseY-positionsPlaces.get(i).getPy()),2)+Math.pow((mouseX-positionsPlaces.get(i).getPx()),2))<= redius) {			
				whichImage=i;	
				bover = true;  
				isTransition=false;
				found=true;
				break; 
			} else {
				isTransition=false;
				bover = false;
			}
		}
		if(!found) {
			for (int i=0; i < positionTransitions.size(); i++) {					
				if (mouseX > positionTransitions.get(i).getPx()-BS && mouseX < positionTransitions.get(i).getPx()+BS && 
						mouseY > positionTransitions.get(i).getPy()-BS && mouseY < positionTransitions.get(i).getPy()+BS){
					whichImage=i;
					bover = true;  
					isTransition=true;				
					break; 
				} 
			}
		}		
	}
	
	
}