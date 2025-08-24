package com.example.equationevaluator.utils;

import com.example.equationevaluator.models.Node;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class ExpressionUtils {

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "^");

    private static int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> -1;
        };
    }

    private static boolean rightAssociative(String op) {
        return "^".equals(op);
    }

    public static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int n = expr.length();
        for (int i = 0; i < n; ) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }


            if (Character.isDigit(c) || (c == '.')) { // number (supports decimals)
                int j = i + 1;
                while (j < n) {
                    char cj = expr.charAt(j);
                    if (Character.isDigit(cj) || cj == '.') j++; else break;
                }
                tokens.add(expr.substring(i, j));
                i = j;
                continue;
            }


            if (Character.isLetter(c) || c == '_') { // identifier
                int j = i + 1;
                while (j < n) {
                    char cj = expr.charAt(j);
                    if (Character.isLetterOrDigit(cj) || cj == '_') j++; else break;
                }
                tokens.add(expr.substring(i, j));
                i = j;
                continue;
            }

            if ("()+-*/^".indexOf(c) >= 0) {
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }


            throw new IllegalArgumentException("Unexpected character in expression: '" + c + "'");
        }
        return tokens;
    }

    public static List<String> infixToPostfix(String expr) {
        List<String> tokens = tokenize(expr);
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();


        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            if (OPERATORS.contains(t)) {
                while (!stack.isEmpty() && OPERATORS.contains(stack.peek())) {
                    String top = stack.peek();
                    int p1 = precedence(t);
                    int p2 = precedence(top);
                    if ((rightAssociative(t) && p1 < p2) || (!rightAssociative(t) && p1 <= p2)) {
                        output.add(stack.pop());
                    } else break;
                }
                stack.push(t);
            } else if ("(".equals(t)) {
                stack.push(t);
            } else if (")".equals(t)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    output.add(stack.pop());
                }
                if (stack.isEmpty() || !"(".equals(stack.pop()))
                    throw new IllegalArgumentException("Mismatched parentheses");
            } else {

                output.add(t);
            }
        }


        while (!stack.isEmpty()) {
            String top = stack.pop();
            if ("(".equals(top) || ")".equals(top)) throw new IllegalArgumentException("Mismatched parentheses");
            output.add(top);
        }
        return output;
    }

    public static Node buildTree(List<String> postfix) {
        Deque<Node> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (OPERATORS.contains(token)) {
                Node r = stack.pop();
                Node l = stack.pop();
                Node node = new Node(token);
                node.left = l; node.right = r;
                stack.push(node);
            } else {
                stack.push(new Node(token));
            }
        }
        if (stack.size() != 1) throw new IllegalStateException("Invalid expression");
        return stack.pop();
    }

    public static double evaluate(Node root, Map<String, Double> vars) {
        if (root.left == null && root.right == null) {
            String v = root.value;
            if (vars != null && vars.containsKey(v)) return vars.get(v);
            try { return Double.parseDouble(v); } catch (NumberFormatException e) { throw new IllegalArgumentException("Missing variable value for '" + v + "'"); }
        }
        double a = evaluate(root.left, vars);
        double b = evaluate(root.right, vars);
        return switch (root.value) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> b == 0 ? Double.NaN : a / b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalStateException("Unknown operator: " + root.value);
        };
    }

    public static String toInfix(Node node) {
        if (node == null) return "";
        if (node.left == null && node.right == null) return node.value;
        return "(" + toInfix(node.left) + " " + node.value + " " + toInfix(node.right) + ")";
    }
}
