package com.acme.task4;

public class Task4Arrays {
	private static final String COUNT_MESSAGE = 
			"Number of %d: %d";
	
	public static void main(String[] args) {
		
		int array[] = {7, 3, 9, 3, 3, 7, 9, 3, 7, 7};
		printCount(array);
		
	}
	public static void printCount(int[] array) {

	
		int numOf3 = 0;
		int numOf7 = 0;
		int numOf9 = 0;
		for (int i = 0; i < array.length; i++) {
			switch (array[i]) {
			case 3:
				numOf3++;
				break;
			case 7:
				numOf7++;
				break;
			case 9:
				numOf9++;
				break;

			default:
				break;
			}
		}
		
		System.out.println(String.format(COUNT_MESSAGE, 3, numOf3));
		System.out.println(String.format(COUNT_MESSAGE, 7, numOf7));
		System.out.println(String.format(COUNT_MESSAGE, 9, numOf9));
	}
}
