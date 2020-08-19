package metapenta.visualization.petrinet;
import java.util.ArrayList;
import processing.core.PApplet;
public class MySketch extends PApplet {
	private static int WEIGHT_PLACES=60;
	private static int HEIGHT_PLACES=60;
	float xOffset=0;
	float yOffset=0;
	public ArrayList<Node> edges; 
	private Node current;
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
	public void draw() {
		background(255, 255, 255);	
		edges= new ArrayList<Node>();
		current=null;
		RGBTuple rgb_cirle = new RGBTuple(203, 108, 230);
		RGBTuple rgb_text = new RGBTuple(255, 255, 255);
		Node place = new Place(100,100,WEIGHT_PLACES,HEIGHT_PLACES,"Ejemplo", rgb_cirle, rgb_text,this);
		place.display(); 		
		Node transition = new Transition(200,200,WEIGHT_PLACES,HEIGHT_PLACES,"Ejemplo", rgb_cirle, rgb_text,this);
		transition.display(); 
		edges.add(transition);
		edges.add(place);	
		for (int i = 0; i < edges.size(); i++) {
			Node e = edges.get(i);
			if(e.getClass().getSimpleName().equals("Place")) {
				if (Math.sqrt(Math.pow((mouseX-e.x),2)+Math.pow((mouseY-e.y),2))<= e.h) {	
					e.overBox = true;  
					this.current=e;
					if(!e.locked) { 
						stroke(255); 
						fill(153);
					} 
				} else {
					stroke(153);
					fill(153);
					e.overBox = false;
				}
			}
			else {
				if (mouseX > e.x-e.w && mouseX < e.x+e.w && 
					      mouseY > e.y-e.h && mouseY < e.y+e.h) {
					current=e;
					e.overBox = true;  
					if(!e.locked) { 
						stroke(255); 
						fill(153);
					} 
				} else {
					stroke(153);
					fill(153);
					e.overBox = false;
				}
			}			
		}
	}	
	public void mousePressed() {
		  if(current!=null) { 
		    current.locked = true; 
		    fill(255, 255, 255);
		  } else {
		    current.locked = false;
		  }
		  xOffset = mouseX-current.x; 
		  yOffset = mouseY-current.x; 
	}
//	public void mouseDragged() {
//		  if(locked) {
//		    x = mouseX-xOffset; 
//		    y = mouseY-yOffset; 
//		    p.x = mouseX-xOffset; 
//		    p.y = mouseY-yOffset; 
//		  }
//		}
//
//	public void mouseReleased() {
//		  locked = false;
//		}
	public void setup(){}
		
}