package metapenta.visualization.petrinet;
import java.util.ArrayList;
import java.util.Arrays;

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
	private int radius=50;	
	/**
	 * Matrix of adjacency of Petri Net 
	 * 0 means no edge
	 * 1 means edge Transition->Place
	 * 2 means edge Place->Transition
	 * 3 means edge Place->Transition and Transition->Place
	 */

	private byte[][] adjacencyMatrix;
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
	public void arrow(float x1, float y1, float x2, float y2) {
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
		adjacencyMatrix = new byte[positionTransitions.size()][positionsPlaces.size()];
		adjacencyMatrix[0][0]=1;
		adjacencyMatrix[0][1]=2;
		adjacencyMatrix[1][0]=1;
		adjacencyMatrix[1][1]=1;
	}

	public void draw() {		
		background(255, 255, 255);
		fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);	
		stroke(126);	
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[0].length; j++) {
				if(adjacencyMatrix[i][j]==1) {
					float x1 = positionTransitions.get(i).getPx()+BS/2,y1 = positionTransitions.get(i).getPy()+BS/2;
					float x2 = positionsPlaces.get(j).getPx(), y2 = positionsPlaces.get(j).getPy();					
					float[] coordinates = intersectionPointCircleLine(x1, y1, x2, y2);
					arrow(x1, y1, coordinates[0],coordinates[1]);					
				}
				else if(adjacencyMatrix[i][j]==2) {
					float[] intersectionPoints = intersectionPointRectLine(positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy() ,positionTransitions.get(i).getPx()+BS/2, positionTransitions.get(i).getPy()+BS/2);
					System.out.println(Arrays.toString(intersectionPoints));
					arrow(positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(), intersectionPoints[0],intersectionPoints[1]);
					line(positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(),positionTransitions.get(i).getPx()+BS/2, positionTransitions.get(i).getPy()+BS/2);
				}
				else if(adjacencyMatrix[i][j]==3) {
					line(positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(),positionTransitions.get(i).getPx(), positionTransitions.get(i).getPy());
					line(positionTransitions.get(i).getPx(), positionTransitions.get(i).getPy(), positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy());
				}
			}
		}
		for (int j=0; j < positionTransitions.size(); j++) {
			fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);
			rect ( positionTransitions.get(j).getPx(), positionTransitions.get(j).getPy(), BS, BS) ;
			stroke(153);
		}

		for (int j=0; j < positionsPlaces.size(); j++) {
			fill(ORANGE.r,ORANGE.g,ORANGE.b);
			ellipse( positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(), radius, radius);			
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
	/**
	 * Check if the user press over a node
	 */
	public void checkOver() {
		boolean found=false;
		for (int i = 0; i <positionsPlaces.size() ; i++) {			
			if (Math.sqrt(Math.pow((mouseY-positionsPlaces.get(i).getPy()),2)+Math.pow((mouseX-positionsPlaces.get(i).getPx()),2))<= radius) {			
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



	private float[] intersectionPointCircleLine(double x1, double y1, double cx2, double cy2) {
		double dx = cx2-x1, dy = cy2-y1;
		double norm = norm(dx, dy);
		double factor = (norm-radius/2)/norm;
		double ndx = dx*factor+x1, ndy = dy*factor+y1;
		float[] coordinates = new float[2];
		coordinates[0] = (float) ndx;
		coordinates[1] = (float) ndy;
		return coordinates;		
	}


	private float[] intersectionPointRectLine(double x1, double y1, double cx2, double cy2) {
		double dx = cx2-x1, dy = cy2-y1;
		float[] points = new float[2];
		if( (dx >= 0 && Math.abs(dy) <= Math.abs(dx)) || (dx <= 0 && Math.abs(dy) <= Math.abs(dx)) ) { // Left
			double xl = (dx <= 0) ? cx2 + BS/2: cx2 - BS/2;
			double[] lineEquation = lineEquation(x1, y1, cx2, cy2);
			double yl = lineEquation[0]*xl + lineEquation[1];
			points[0] = (float) xl;
			points[1] = (float) yl;
		}		
		else if( (dy >= 0 && Math.abs(dy) >= Math.abs(dx)) || (dy <= 0 && Math.abs(dy) >= Math.abs(dx))) {			
			double yl = (float) ((dy>=0)? cy2 - BS/2: cy2 + BS/2);
			if(cx2==x1) {
				points[0] = (float) cx2;
				points[1] = (float) yl;
			}
			else {
				double[] lineEquation = lineEquation(x1, y1, cx2, cy2);
				points[0] = (float) ((yl-lineEquation[1])/lineEquation[0]);
				points[1] = (float) yl;
			}
		}
		return points;
	}



	private double norm(double dx, double dy) {
		return Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));		
	}
	
	private double[] lineEquation (double x1, double y1, double x2, double y2) {
		double m = (y2-y1)/(x2-x1);
		double b = y1-((y2-y1)/(x2-x1))*x1;
		double[] lineEquation = new double[2];
		lineEquation[0] = m;
		lineEquation[1] = b;		
		return lineEquation;
	}



}