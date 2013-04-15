package com.acme.task6;

public class TextFormatter {

	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("Wrong args");
			return;
		}
		String str = args[0];
		StringBuffer result = new StringBuffer();
		
		boolean isInBlockComment = false;
		boolean isInLineComment = false;
		for (int i = 0; i < str.length(); i++) {
			char current = str.charAt(i);
			char previous = (i == 0) ? ' ' : str.charAt(i-1);
			
			if (isInBlockComment){
				if (previous == '*' && current == '/'){
					isInBlockComment = false;
				}
				if (current == '\n'){
					result.append(current);
				}
				continue;
			}
			
			else if (isInLineComment){
				if (current == '\n'){
					isInLineComment = false;
					result.append(current);
				}
				continue;
			}
			
			else if (previous == '/' && current == '*'){
				result.deleteCharAt(result.length()-1);
				isInBlockComment = true;
				continue;
			}
			
			else if (previous == '/' && current == '/'){
				result.deleteCharAt(result.length()-1);
				isInLineComment = true;
				continue;
			}
			
			else
				result.append(current);
		}
		System.out.println(result);
	}
}
