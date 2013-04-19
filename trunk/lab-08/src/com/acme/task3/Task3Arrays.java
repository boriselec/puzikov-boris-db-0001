package com.acme.task3;

public class Task3Arrays {

	private static final String ARRAY_MESSAGE = 
			"%s is multiple of 3";
	
	public static void main(String[] args) {
		int[] array = new int[50];
		
		testArrays(array);
		
		System.out.println(avg(array));
	}
	
	public static void  testArrays(int[] array) {
		
		for (int i = 0; i < array.length; i++) {
			array[i] = i + 1; 
		}
		
		for (int i = 0; i < array.length; i++) {
			if (array[i] % 3 == 0){
				String message = String.format(ARRAY_MESSAGE, array[i]);
				System.out.println(message);
			}
			else{
				System.out.println(array[i]);
			}
		}

		
	}
	
	public static double avg(int[] array) {
		double result = 0.0;
		
		for (int i = 0; i < array.length; i++) {
			result += (double) array[i] / array.length; 
		}
		
		return result;
	}
}
