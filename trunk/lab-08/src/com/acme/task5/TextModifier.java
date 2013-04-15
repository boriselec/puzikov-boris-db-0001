package com.acme.task5;

public class TextModifier {
	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("Wrong args");
			return;
		}
		String str = args[0];
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
