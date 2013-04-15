package com.acme.task1;

/**
 * This class implements simple calculator 
 * with command line arguments.
 * @author Boris Puzikov
 *
 */
public class Calculator {
	/**
	 * @param args Expression to evaluate.
	 */
	public static void main(String[] args) {
		
        boolean isLessThree = (args.length < 3);
        if (isLessThree) {
            System.out.println("Wrong number of arguments");
            return;
        }
        
        double operand1;
        double operand2;
        try{
            operand1 = Double.parseDouble(args[0]);
            operand2 = Double.parseDouble(args[2]);
        }
        catch(Exception e){
            System.out.println("Wrong numbers");
            return;
        };
        
        char operator = args[1].charAt(0);
        boolean isOperator = ((operator == '+') || (operator == '-') || 
                              (operator == '*') || (operator == '\\'));
        if (!isOperator){
            System.out.println("Wrong operator");
            return;
        }
       
        double result = 0;
        switch (operator) {
		case '+':
			result = operand1 + operand2;
			break;
		case '-':
			result = operand1 - operand2;
			break;
		case '*':
			result = operand1 * operand2;
			break;
		case '\\':
			result = operand1 / operand2;
			break;

		}
        System.out.println(result);



    }
}
