package com.acme.task3;

public class Task3Arrays {
	public static void main(String[] args) {
		int[] array = new int[50];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = i + 1; 
		}
		
		for (int i = 0; i < array.length; i++) {
			if (array[i] % 3 == 0){
				System.out.println("Число " + array[i] + " кратно трем");
			}
			else{
				System.out.println(array[i]);
			}
		}

		System.out.println(avg(array));
		
	}
	
	public static double avg(int[] array) {
		double result = 0.0;
		
		for (int i = 0; i < array.length; i++) {
			result += (double) array[i] / array.length; 
		}
		
		return result;
	}
}
