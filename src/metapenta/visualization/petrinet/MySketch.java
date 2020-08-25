package metapenta.visualization.petrinet;
import processing.core.PApplet;
public class MySketch extends PApplet {

	private boolean locked=false;;
	private final static RGBTuple BLUE = new RGBTuple(3, 152, 158); 
	private static final RGBTuple BLUE_KING = new RGBTuple(0, 74, 173);
	private static final RGBTuple ORANGE = new RGBTuple(255, 145, 77);
	private float bx;
	private float by;
	private int bs = 40;
	private int bz = 30;
	private boolean bover = false;
	private float bdifx = 0; 
	private float bdify = 0; 
	private float newx, newy;
	int whichImage;
	private float[] positions_transitions= new float [3*2] ;
	private float[] positions_places = new float[0];
	private boolean in_transition=false;
	private int radious=20;


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
		bx = width/2;
		by = height/2;
		for (int j=0; j < 3*2; j+=2) {
			positions_transitions[j]= random(width-50);
			positions_transitions[j+1]= random(height-50);
//			positions_places[j]= random(width-50);
//			positions_places[j+1]= random(height-50);
			
		}	  
		fill(153);
	}

	public void draw() 
	{ 
		background(255, 255, 255);	
		for (int j=0; j < 3; j++) {
			if (bover && whichImage==j) 
				stroke(255);  // white
			else
				noStroke(); 
			fill(255,145,77);
			rect ( positions_transitions[j*2], positions_transitions[j*2+1], 50, 50) ;
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
			newx = mouseX; 
			newy = mouseY;
		}

		positions_transitions [whichImage*2] = newx;
		positions_transitions [(whichImage*2)+1] = newy;
	}

	void checkOver() {
		for (int i=0; i < 3; i++) {
			if (mouseX > positions_transitions[i*2]-bs && mouseX < positions_transitions[i*2]+bs && 
					mouseY > positions_transitions[i*2+1]-bs && mouseY < positions_transitions[i*2+1]+bs){
				println ("mouseover image: "+i);
				whichImage=i;
				bover = true;  
				in_transition=true;
				break; 
			} 
			else{
				in_transition=false;
				bover = false;
			}
		}
		for (int i = 0; i < positions_places.length/2; i++) {
			if (Math.sqrt(Math.pow((mouseX-positions_places[i*2]),2)+Math.pow((mouseY-positions_places[i*2+1]),2))<= radious) {	
				bover = true;  
				whichImage=i;
				in_transition=false;
			} else {
				bover = false;
			}


		}
	}
}