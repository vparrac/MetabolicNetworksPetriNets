package metapenta.visualization.petrinet;
import processing.core.PApplet;
public class MySketch extends PApplet {
	private boolean locked=false;;
	// Colors
	private final static RGBTuple BLUE = new RGBTuple(3, 152, 158); 
	private static final RGBTuple BLUE_KING = new RGBTuple(0, 74, 173);
	private static final RGBTuple ORANGE = new RGBTuple(255, 145, 77);
	
	private final static int BS = 50;	
	private boolean bover = false;	
	private float newx, newy;
	int whichImage;
	private float[] positions_transitions = {200,20,250,20} ;
	private float[] positions_places = {100,20,250,30} ;
	private float xOffset = 0; 
	private float yOffset = 0; 
	
	
	private boolean isTransition=false;
	private int radious=50;
	public static void main(String[] args) {
		PApplet.main(MySketch.class.getName());
	}
	public void settings() {
		size(640, 640);
		smooth();
		if (frame != null) {
			frame.setResizable(true);
		}
	}	 

	public void setup() {

	}

	public void draw() {		
		background(255, 255, 255);	
		for (int j=0; j < positions_transitions.length/2; j++) {
			if (bover && whichImage==j) 
				stroke(255);  // white
			else
				noStroke(); 
			fill(BLUE_KING.r,BLUE_KING.g,BLUE_KING.b);
			rect ( positions_transitions[j*2], positions_transitions[j*2+1], BS, BS) ;
		}

		for (int j=0; j < positions_places.length/2; j++) {
			if (bover && whichImage==j) 
				stroke(255);  
			else
				noStroke(); 
			fill(ORANGE.r,ORANGE.g,ORANGE.b);
			ellipse ( positions_places[j*2], positions_places[j*2+1], radious, radious) ;
		}
	}

	public void mousePressed() {
		checkOver();
		if (bover) { 
			locked = true;
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
		if (locked) {
//			float[] node = (isTransition)? positions_transitions:positions_places
			newx = mouseX; 
			newy = mouseY;
		}

		moveNodes();
	}

	void moveNodes() {
		if(isTransition) {
			positions_transitions [whichImage*2] = newx;
			positions_transitions [(whichImage*2)+1] = newy;
		}
		else if(!isTransition) {			
			positions_places [whichImage*2] = newx;
			positions_places [(whichImage*2)+1] = newy;
		}
	}

	void checkOver() {
		boolean found=false;
		
		for (int i = 0; i <positions_places.length/2 ; i++) {
			System.out.println("Verificando places");
			if (Math.sqrt(Math.pow((mouseY-positions_places[i*2+1]),2)+Math.pow((mouseX-positions_places[i*2]),2))<= radious) {				
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
			for (int i=0; i < positions_transitions.length/2; i++) {		
				System.out.println("Verificando transitions");
				if (mouseX > positions_transitions[i*2]-BS && mouseX < positions_transitions[i*2]+BS && 
						mouseY > positions_transitions[i*2+1]-BS && mouseY < positions_transitions[i*2+1]+BS){
					whichImage=i;
					bover = true;  
					isTransition=true;				
					break; 
				} 
			}
		}		
		System.out.println("Es transición "+isTransition);
		System.out.println("bover "+bover);
	}
}