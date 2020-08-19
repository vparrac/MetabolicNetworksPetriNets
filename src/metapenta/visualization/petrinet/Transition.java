package metapenta.visualization.petrinet;

import processing.core.PApplet;

public class Transition extends Node{
		public Transition(float x, float y, float w, float h, String n, RGBTuple color_place, RGBTuple color_text, PApplet parent) {
			super(x, y, w, h, n, color_place, color_text,parent);
		}
		public void display() {
			parent.noStroke();
			parent.fill(color_place.r,color_place.g,color_place.b);
			parent.rect(x, y, w, h);
			parent.fill(color_text.r,color_text.g,color_text.b);
			parent.text(name, x-parent.textWidth(name)/2, y+2);
		}
	}