package se.nederlag.pyzzlix;

public class Pair<L,R> {
	L left;
	R right;
	
	public Pair() {
	}
	
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public String toString() {
		return "(" + left + ", " + right + ")";
	}
}
