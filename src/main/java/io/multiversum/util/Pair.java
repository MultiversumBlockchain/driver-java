package io.multiversum.util;

public class Pair<A, B> {

	private A first;
	private B second;
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	
	public Pair() {
		first = null;
		second = null;
	}
	
	public A getFirst() {
		return first;
	}
	
	public B getSecond() {
		return second;
	}
	
	public void setFirst(A value) {
		first = value;
	}
	
	public void setSecond(B value) {
		second = value;
	}
	
	@Override
	public String toString() {
		return "Pair[" + first + "," + second + "]";
	}
	
}
