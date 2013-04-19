package com.acme.task2;

public class Foo {
	
	private static final String FINALIZE_MESSAGE = 
			"Finalize called: %d%n";
	public Foo(int iteration) {
		super();
		this.iteration = iteration;
	}
	private int iteration;
	@Override
	protected void finalize() throws Throwable {
		String message = String.format(FINALIZE_MESSAGE, this.iteration);
		System.out.println(message);
		super.finalize();
	}
}
