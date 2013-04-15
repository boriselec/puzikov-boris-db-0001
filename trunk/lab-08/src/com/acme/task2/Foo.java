package com.acme.task2;

public class Foo {
	
	public Foo(int iteration) {
		super();
		this.iteration = iteration;
	}
	private int iteration;
	@Override
	protected void finalize() throws Throwable {
		System.out.println("Finalize called: " + this.iteration);
		super.finalize();
	}
}
