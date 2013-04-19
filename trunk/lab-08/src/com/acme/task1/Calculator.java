package com.acme.task1;

/**
 * This class implements simple calculator 
 * with command line arguments.
 * @author Boris Puzikov
 *
 */
public class Calculator {
	
	private static final char OP_PLUS = '+';
	private static final char OP_MINUS = '-';
	private static final char OP_MULTIPLY = '*';
	private static final char OP_DIVIDE = '\\';
	
	private static final String ERR_ARGS_NUMBER = 
			"Wrong number of arguments. Must be at least 3.";
	private static final String ERR_NOT_NUMBER = 
			"Argument is not a number.";
	private static final String ERR_NOT_OPERATOR = 
			"Argument is not an operator. Valid operators: - + * \\";
	
	/**
	 * @param args Expression to evaluate.
	 */
	public static void main(String[] args) {
		try {
			double result = calculate(args);
			System.out.println(result);

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static double calculate(String[] args) {
		
        boolean isLessThree = (args.length < 3);
        if (isLessThree) {
        	throw new IllegalArgumentException(ERR_ARGS_NUMBER);
        }
        
        double operand1;
        double operand2;
        try{
            operand1 = Double.parseDouble(args[0]);
            operand2 = Double.parseDouble(args[2]);
        }
        catch(Exception e){
        	throw new IllegalArgumentException(ERR_NOT_NUMBER);
        };
        
        char operator = args[1].charAt(0);
        boolean isOperator = ((operator == OP_PLUS) || (operator == OP_MINUS) || 
                              (operator == OP_MULTIPLY) || (operator == OP_DIVIDE));
        if (!isOperator){
        	throw new IllegalArgumentException(ERR_NOT_OPERATOR);
        }
       
        double result = 0;
        switch (operator) {
		case OP_PLUS:
			result = operand1 + operand2;
			break;
		case OP_MINUS:
			result = operand1 - operand2;
			break;
		case OP_MULTIPLY:
			result = operand1 * operand2;
			break;
		case OP_DIVIDE:
			result = operand1 / operand2;
			break;
		}
        
        return result;
    }
}
