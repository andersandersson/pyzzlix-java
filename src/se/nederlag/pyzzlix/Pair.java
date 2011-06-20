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
	
	public boolean equals(Object other) {
		if(! (other instanceof Pair<?,?>) ) {
			return false;
		}
		
		Pair<L,R> other_p = (Pair<L,R>) other;		
		return this.left == other_p.left && this.right == other_p.right;
	}
}
