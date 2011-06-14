package se.nederlag.pyzzlix;

/**
 * Data class for 2D points. 
 * 
 * Made as an immutable as a minor test.
 * 
 * @author anders
 *
 */
public final class Point {
	public final double x;
	public final double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return this.x + ", " + this.y;
	}
}
