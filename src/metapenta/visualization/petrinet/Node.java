package metapenta.visualization.petrinet;

import processing.core.PApplet;
public class Node {
	protected float x;
		protected float y;
		protected RGBTuple color_place;
		protected RGBTuple color_text;
		protected float w;
		protected float h;
		protected String name;
		protected boolean overBox;
		protected boolean locked;
		protected PApplet parent;		
		public Node(float x, float y, float w, float h, String n, RGBTuple color_place, RGBTuple color_text, PApplet parent) {
			this.x = x;
			this.y = y;			
			this.w = w;
			this.h = h;
			this.name = n;
			this.color_place=color_place;
			this.color_text=color_text;
			overBox=false;
			locked=false;
			this.parent=parent;
		}
		public void display() {
		}
		public float getX() {
			return x;
		}
		public void setX(float x) {
			this.x = x;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
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
		public boolean isOverBox() {
			return overBox;
		}
		public void setOverBox(boolean overBox) {
			this.overBox = overBox;
		}
		public boolean isLocked() {
			return locked;
		}
		public void setLocked(boolean locked) {
			this.locked = locked;
		}
		public PApplet getParent() {
			return parent;
		}
		public void setParent(PApplet parent) {
			this.parent = parent;
		}		
	}	