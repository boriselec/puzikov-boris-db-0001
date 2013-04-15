package com.acme.task2;

public class GCTest {

	public static void main(String[] args) {
		int numberOfIterations = 1000000;
		
		Foo[] array = new Foo[numberOfIterations];
		
		for (int i = 0; i < numberOfIterations; i++) {
			Foo foo = new Foo(i);
			array[i] = foo; 
		}
	}
}
