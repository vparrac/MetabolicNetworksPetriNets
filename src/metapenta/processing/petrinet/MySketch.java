package metapenta.processing.petrinet;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import metapenta.gui.Controller;
import metapenta.gui.JavaFXApplication;
import metapenta.model.Metabolite;
import metapenta.model.Reaction;
/**
 * Main class of the visualization panel this class extends of PApplet
 * the main class of processing library
 * @author Valerie Parra Cortés
 */
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PSurface;
import processing.javafx.PSurfaceFX;

public class MySketch extends PApplet {
	Text details_title;
	Text details_title_name;
	TextField details_id;	
	// Reaction
	TextField details_compartment;
	TextField details_chemical_formula;
	Text details_title_textArea_1;
	Text details_title_textArea_2;
	TextArea details_textArea_2;
	TextArea details_textArea_1;
	/**
	 * It represents id a user is over a note
	 */
	private boolean locked = false;;
	// Colors	
	private PFont myFont;
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
	public ArrayList<TransitionProcessing> positionTransitions = new ArrayList<TransitionProcessing>();
	/**
	 * The location of places
	 */
	public ArrayList<PlaceProcessing> positionsPlaces = new ArrayList<PlaceProcessing>();
	public Translator translator;
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
	 * Matrix of adjacency of Petri Net 
	 * 0 means no edge
	 * 1 means edge Transition->Place
	 * 2 means edge Place->Transition
	 * 3 means edge Place->Transition and Transition->Place
	 */

	public int[][] adjacencyMatrix;	
	/**
	 * Represents the weight between transitions to places 
	 */
	public int[][] adjacencyMatrixWeightsTP;
	/**
	 * Represents the weights between places to transition
	 */
	public int[][] adjacencyMatrixWeightsPT;

	@Override
	protected PSurface initSurface() {
		g = createPrimaryGraphics();
		PSurface genericSurface = g.createSurface();
		PSurfaceFX fxSurface = (PSurfaceFX) genericSurface;
		fxSurface.sketch = this;
		JavaFXApplication.surface = fxSurface;
		Controller.surface = fxSurface;
		new Thread(() -> Application.launch(JavaFXApplication.class)).start();
		while (fxSurface.stage == null) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}
		this.surface = fxSurface;
		initJavaFXSceneElements();
		return surface;
	}
	private void initJavaFXSceneElements() {
		Canvas canvas = (Canvas) surface.getNative();
		details_title = (Text) canvas.getScene().lookup("#details_title");
		details_title_name = (Text) canvas.getScene().lookup("#details_title_name");
		details_id = (TextField) canvas.getScene().lookup("#details_id");
		details_compartment = (TextField) canvas.getScene().lookup("#details_compartment");
		details_chemical_formula = (TextField) canvas.getScene().lookup("#details_chemical_formula");
	}
	public void settings() {
		size(100, 100, FX2D);
	}	 
	/**
	 * This method print an arrow
	 * @param x1 the first coordinate of the arrow's start
	 * @param y1 the seconds coordinate of arrow's start
	 * @param x2 the first coordinate of  arrow's end
	 * @param y2 the seconds coordinate of arrow's end
	 */
	public void arrow(float x1, float y1, float x2, float y2, int weight) {
		line(x1, y1, x2, y2);
		pushMatrix();
		translate(x2, y2);
		float a = atan2(x1-x2, y2-y1);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		fill(Constants.BLACK.r,Constants.BLACK.g,Constants.BLACK.b);
		text(weight+"", 8, -20);
		popMatrix();
	}


	public void bidirectionalArrow(float x1, float y1, float x2, float y2, int weight1, int weight2) {		
		line(x1, y1, x2, y2);
		pushMatrix();
		translate(x2, y2);
		float a = atan2(x1-x2, y2-y1);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		fill(Constants.BLACK.r,Constants.BLACK.g,Constants.BLACK.b);
		text(weight1+"", 8, -20);
		popMatrix();
		pushMatrix();
		translate(x1, y1);
		a = atan2(x2-x1, y1-y2);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		fill(Constants.BLACK.r,Constants.BLACK.g,Constants.BLACK.b);
		text(weight2+"", -16, -20);
		popMatrix();


	}	

	public void setup() {
		Controller.p = this;
		myFont = createFont("Yu Gothic Light", 12);
		textFont(myFont);
		adjacencyMatrix = new int[positionTransitions.size()][positionsPlaces.size()];
		adjacencyMatrixWeightsTP = new int[positionTransitions.size()][positionsPlaces.size()];
		adjacencyMatrixWeightsPT = new int[positionTransitions.size()][positionsPlaces.size()];
		//		noLoop();
	}

	public void draw() {
		background(255, 255, 255);
		fill(Constants.BLUE_KING.r, Constants.BLUE_KING.g, Constants.BLUE_KING.b);	
		stroke(100);	
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[0].length; j++) {
				float x1, x2, y1, y2;
				x1 = positionTransitions.get(i).getPx()+Constants.BS/2;
				y1 = positionTransitions.get(i).getPy()+Constants.BS/2;
				x2 = positionsPlaces.get(j).getPx();
				y2 = positionsPlaces.get(j).getPy();	
				float[] ipTransitionsToPlaces = intersectionPointCircleLine(x1, y1, x2, y2);
				float[] ipPlacesToTransitions = intersectionPointRectLine(x2, y2, x1, y1);				
				if(adjacencyMatrix[i][j]==1) {								
					arrow(ipPlacesToTransitions[0], ipPlacesToTransitions[1], ipTransitionsToPlaces[0],ipTransitionsToPlaces[1], adjacencyMatrixWeightsPT[i][j]);					
				}
				else if(adjacencyMatrix[i][j]==2) {
					arrow(ipTransitionsToPlaces[0], ipTransitionsToPlaces[1], ipPlacesToTransitions[0],ipPlacesToTransitions[1], adjacencyMatrixWeightsTP[i][j]);					
				}
				else if(adjacencyMatrix[i][j]==3) {								
					bidirectionalArrow(ipTransitionsToPlaces[0], ipTransitionsToPlaces[1], ipPlacesToTransitions[0], ipPlacesToTransitions[1], adjacencyMatrixWeightsTP[i][j],adjacencyMatrixWeightsPT[i][j]);		
				}
			}
		}
		for (int j=0; j < positionTransitions.size(); j++) {			
			float px = positionTransitions.get(j).getPx(), py = positionTransitions.get(j).getPy();
			String name = positionTransitions.get(j).getName();
			float r =positionTransitions.get(j).getColor_text().r, g=positionTransitions.get(j).getColor_text().g, b = positionTransitions.get(j).getColor_text().b;
			fill(positionTransitions.get(j).getColor_place().r, positionTransitions.get(j).getColor_place().g, positionTransitions.get(j).getColor_place().b);
			rect ( px, py, Constants.BS, Constants.BS) ;
			fill( r, g, b);
			text( name, px+Constants.BS/2-textWidth(name)/2, py+Constants.BS/2);			
		}

		for (int j=0; j < positionsPlaces.size(); j++) {
			fill(positionsPlaces.get(j).getColor_place().r, positionsPlaces.get(j).getColor_place().g, positionsPlaces.get(j).getColor_place().b);
			ellipse( positionsPlaces.get(j).getPx(), positionsPlaces.get(j).getPy(), Constants.RADIUS, Constants.RADIUS);		
			fill(positionsPlaces.get(j).getColor_text().r, positionsPlaces.get(j).getColor_text().g, positionsPlaces.get(j).getColor_text().b);
			String name = positionsPlaces.get(j).getName();
			float px = positionsPlaces.get(j).getPx(), py = positionsPlaces.get(j).getPy();			
			text( positionsPlaces.get(j).getName(), px-textWidth(name)/2, py);
		}
	}

	public void mousePressed() {	
		//		loop();
		checkOver();
		if (bover) { 
			locked = true;
			NodeProcessing currentNodes = (isTransition)? positionTransitions.get(whichImage):positionsPlaces.get(whichImage);
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
		//		noLoop();
	}

	public void mouseDragged() {
		//		loop();
		if(locked) {
			NodeProcessing currentNode=(isTransition)? positionTransitions.get(whichImage):positionsPlaces.get(whichImage);
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
			if (Math.sqrt(Math.pow((mouseY-positionsPlaces.get(i).getPy()),2)+Math.pow((mouseX-positionsPlaces.get(i).getPx()),2))<= Constants.RADIUS) {			
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
				if (mouseX > positionTransitions.get(i).getPx()-Constants.BS && mouseX < positionTransitions.get(i).getPx()+Constants.BS && 
						mouseY > positionTransitions.get(i).getPy()-Constants.BS && mouseY < positionTransitions.get(i).getPy()+Constants.BS){
					whichImage=i;
					bover = true;  
					isTransition=true;				
					break; 
				} 
			}
		}		
		if(bover) {			
			if(!isTransition) {
				setDetailsMetabolite();
			}			
			else {
				setDetailsReaction();
			}			
		}		
	}
	private void setDetailsMetabolite() {
		NodeProcessing currentNode = positionsPlaces.get(whichImage);
		details_title.setText(Constants.METABOLITE);
		Metabolite meta = translator.getMetabolite(currentNode.getName());
		details_title_name.setText(meta.getName());
		details_id.setText(Constants.ID + currentNode.getName());
		details_chemical_formula.setVisible(true);
		details_compartment.setText(Constants.COMPARTMENT+ meta.getCompartment());
		details_chemical_formula.setText(Constants.CHEMICAL_FORMULA+meta.getChemicalFormula());

	}
	private void setDetailsReaction() {
		NodeProcessing currentNode = positionTransitions.get(whichImage);
		Reaction reaction = translator.getReaction(currentNode.getName());
		details_title_name.setText(reaction.getName());
		details_id.setText(Constants.ID + currentNode.getName());
		details_compartment.setText(Constants.REVERSIBLE + reaction.isReversible());
		details_chemical_formula.setVisible(false);
	}
	private float[] intersectionPointCircleLine(double x1, double y1, double cx2, double cy2) {
		double dx = cx2-x1, dy = cy2-y1;
		double norm = norm(dx,dy);
		double factor = (norm-Constants.RADIUS/2)/norm;
		double ndx = dx*factor+x1, ndy = dy*factor+y1;
		float[] coordinates = new float[2];
		coordinates[0] = (float) ndx;
		coordinates[1] = (float) ndy;
		return coordinates;		
	}
	private float[] intersectionPointRectLine(double x1, double y1, double cx2, double cy2) {
		double dx = cx2-x1, dy = cy2-y1;
		float[] points = new float[2];
		if( (dx >= 0 && Math.abs(dy) <= Math.abs(dx)) || (dx <= 0 && Math.abs(dy) <= Math.abs(dx)) ) { // Left and Right
			double xl = (dx <= 0) ? cx2 + Constants.BS/2: cx2 - Constants.BS/2;
			double[] lineEquation = lineEquation(x1, y1, cx2, cy2);
			double yl = lineEquation[0]*xl + lineEquation[1];
			points[0] = (float) xl;
			points[1] = (float) yl;
		}		
		else if( (dy >= 0 && Math.abs(dy) >= Math.abs(dx)) || (dy <= 0 && Math.abs(dy) >= Math.abs(dx))) {	//Up and down		
			double yl = (float) ((dy>=0)? cy2 - Constants.BS/2: cy2 + Constants.BS/2);
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
		return Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
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