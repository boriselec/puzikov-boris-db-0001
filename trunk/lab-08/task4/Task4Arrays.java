package com.acme.task4;

public class Task4Arrays {
	public static void main(String[] args) {

		int array[] = {7, 3, 9, 3, 3, 7, 9, 3, 7, 7};
	
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
		System.out.println("Number of 3: " + numOf3);
		System.out.println("Number of 7: " + numOf7);
		System.out.println("Number of 9: " + numOf9);
	}
}
