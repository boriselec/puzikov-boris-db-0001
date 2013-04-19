package com.acme.task2;

public class GCTest {

	private final static int ARRAY_SIZE = 1000000;
	
	public static void main(String[] args) {
		testGC();
	}
	
	public static void testGC() {
		int numberOfIterations = ARRAY_SIZE;
		Foo[] array = new Foo[numberOfIterations];
		
		for (int i = 0; i < numberOfIterations; i++) {
			Foo foo = new Foo(i);
			array[i] = foo; 
		}
	}
}
