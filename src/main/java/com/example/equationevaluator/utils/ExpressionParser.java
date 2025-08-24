package com.example.equationevaluator.utils;
import com.example.equationevaluator.models.Node;

import java.util.*;

public class ExpressionParser {

    // Precedence of operators
    private static int precedence(String op) {
        switch (op) {
            case "+": case "-": return 1;
            case "*": case "/": return 2;
            default: return 0;
        }
    }

    // Check if token is operator
    private static boolean isOperator(String token) {
        return "+-*/".contains(token);
    }

    // Convert infix expression to postfix (Shunting Yard Algorithm)
    private static List<String> infixToPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        // Tokenize by spaces OR individual characters
        StringTokenizer tokenizer = new StringTokenizer(expr, "+-*/() ", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            if (token.matches("[0-9]+") || token.matches("[a-zA-Z]+")) {
                // numbers or variables
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek())
                        && precedence(stack.peek()) >= precedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // remove "("
                }
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    // Build expression tree from postfix expression
    private static Node buildTree(List<String> postfix) {
        Stack<Node> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperator(token)) {
                Node right = stack.pop();
                Node left = stack.pop();
                Node node = new Node(token);
                node.left = left;
                node.right = right;
                stack.push(node);
            } else {
                stack.push(new Node(token));
            }
        }

        return stack.pop();
    }

    //parse infix expression to Node
    public static Node parse(String expression) {
        try {
            List<String> postfix = infixToPostfix(expression);
            if (postfix.isEmpty()) {
                throw new IllegalArgumentException("Expression is empty.");
            }
            return buildTree(postfix);
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Invalid expression: mismatched operators or parentheses.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing expression: " + expression, e);
        }
    }
}

