package metapenta.visualization.petrinet;
import java.util.Map;
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
	
	/**
	 * Númber of pixeles of the boxes
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
	 * The location of the transitions
	 */
	private float[] locationTransitions = {200,20,250,20};
	/**
	 * The location of places
	 */
	private float[] positions_places = {100,20,250,30} ;
	/**
	 * The locations of transitions
	 */
	Map<Integer, int[]> edgesTransitions;
	/**
	 * The location of edges
	 */
	Map<Integer, int[]> edgesPlaces;
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
	 * @param args //TODO Invocation of this class will be from other class
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
		
	}
	
	public void draw() {		
		background(255, 255, 255);	
		stroke(126);	
		for (int j=0; j < locationTransitions.length/2; j++) {
			if (bover && whichImage==j) 
				stroke(255);  // white
			else
				noStroke(); 
			fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);
			rect ( locationTransitions[j*2], locationTransitions[j*2+1], BS, BS) ;
		}

		for (int j=0; j < positions_places.length/2; j++) {
			if (bover && whichImage==j) 
				stroke(255);  
			else
				noStroke(); 
			fill(ORANGE.r,ORANGE.g,ORANGE.b);
			ellipse ( positions_places[j*2], positions_places[j*2+1], redius, redius) ;
		}				
	}

	public void mousePressed() {
		checkOver();
		if (bover) { 
			locked = true;
			float[] nodes = (isTransition)? locationTransitions:positions_places;
			xOffset = mouseX-nodes[whichImage*2]; 
			yOffset = mouseY-nodes[whichImage*2+1];
			System.out.println("xOffset: " +xOffset);

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
			float[] nodes = (isTransition)? locationTransitions:positions_places;
			nodes [whichImage*2] = mouseX-xOffset;
			nodes [(whichImage*2)+1] = mouseY-yOffset;
		}
	}
	void checkOver() {
		boolean found=false;
		for (int i = 0; i <positions_places.length/2 ; i++) {
			System.out.println("Verificando places");
			if (Math.sqrt(Math.pow((mouseY-positions_places[i*2+1]),2)+Math.pow((mouseX-positions_places[i*2]),2))<= redius) {				
				System.out.println("Es place");
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
			for (int i=0; i < locationTransitions.length/2; i++) {		
				System.out.println("Verificando transitions");
				if (mouseX > locationTransitions[i*2]-BS && mouseX < locationTransitions[i*2]+BS && 
						mouseY > locationTransitions[i*2+1]-BS && mouseY < locationTransitions[i*2+1]+BS){
					whichImage=i;
					bover = true;  
					isTransition=true;				
					break; 
				} 
			}
		}		
	}
}