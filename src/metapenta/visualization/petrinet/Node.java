package metapenta.visualization.petrinet;

import java.io.Console;

import processing.core.PApplet;
public class Node{
    public float px;
	public float py;
	public RGBTuple color_place;
	public RGBTuple color_text;
	public float w;
	public float h;
	
	public String name;
	public PApplet parent;
	public boolean place;
	public Node(float x, float y, float w, float h, String n, RGBTuple color_place, RGBTuple color_text, PApplet parent, boolean place) {
		this.px = x;
		this.py = y;			
		this.w = w;
		this.h = h;
		this.name = n;
		this.color_place=color_place;
		this.color_text=color_text;
		this.parent=parent;
		this.place=place;
		System.out.println("in?");
	}
	
	public void display() {
		if(place) {
			this.px=this.px+1;
			
			displayPlace();
		}
		else {
			displayTransition();
		}
	}
	
	public void displayPlace() {
			parent.noStroke();
			parent.fill(203);			
			parent.ellipse(this.px,this.py,w,h);
			parent.fill(color_text.r,color_text.g,color_text.b);
			parent.text(name, px-parent.textWidth(name)/2, py+2);
		}
	
	public void displayTransition() {
		parent.noStroke();
		parent.fill(color_place.r,color_place.g,color_place.b);
		parent.rect(px, py, w, h);
		parent.fill(color_text.r,color_text.g,color_text.b);
		parent.text(name, px-parent.textWidth(name)/2, py+2);
	}
	}