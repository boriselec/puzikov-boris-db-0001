package com.acme.task5;

public class TextModifier {
	
	public static void main(String[] args) {
		
		testModifier(args);
		
	}
	
	public static void testModifier(String[] args) {
		
		if (args.length == 0){
			return;
		}
		modify(args[0]);
		
	}
	
	public static void modify(String str) {
		
		for (int i = 0; i < str.length(); i++) {
			char charAtIndex = str.charAt(i);
			if (Character.isDigit(charAtIndex) == false){
				System.out.print(charAtIndex);
			}
			if (charAtIndex == '-' || charAtIndex == '+'){
				System.out.print(charAtIndex);
			}
		}
	}
}
