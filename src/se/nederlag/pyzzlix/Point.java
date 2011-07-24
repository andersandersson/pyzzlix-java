package se.nederlag.pyzzlix;

/**
 * Data class for 2D points. 
 * 
 * @author anders
 *
 */
public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Point add(Point othr) {
		return new Point(x+othr.x, y+othr.y);
	}
	
	public Point sub(Point othr) {
		return new Point(x-othr.x, y-othr.y);
	}
	
	public Point mul(Point othr) {
		return new Point(x*othr.x, y*othr.y);
	}
	
	public Point mul(double d) {
		return new Point(this.x*d, this.y*d);
	}
	
	public String toString() {
		return this.x + ", " + this.y;
	}
	
	public Point clone() {
		return new Point(x, y);
	}
}
