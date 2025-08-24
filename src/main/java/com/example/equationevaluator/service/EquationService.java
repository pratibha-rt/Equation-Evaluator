package com.example.equationevaluator.service;

import com.example.equationevaluator.utils.ExpressionUtils;
import com.example.equationevaluator.models.Node;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquationService {
    private final Map<String, Node> trees = new LinkedHashMap<>();
    private final Map<String, String> originals = new LinkedHashMap<>();

    public String save(String infixExpression) {
        List<String> postfix = ExpressionUtils.infixToPostfix(infixExpression);
        Node root = ExpressionUtils.buildTree(postfix);
        String id = UUID.randomUUID().toString();
        trees.put(id, root);
        originals.put(id, infixExpression);
        return id;
    }

    public List<Map<String, Object>> listAll() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (String id : originals.keySet()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", id);
            row.put("infix", originals.get(id));
            row.put("reconstructed", ExpressionUtils.toInfix(trees.get(id)));
            list.add(row);
        }
        return list;
    }

    public Map<String, Object> get(String id) {
        Node node = trees.get(id);
        if (node == null) return null;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("infix", originals.get(id));
        map.put("reconstructed", ExpressionUtils.toInfix(node));
        map.put("postfix", ExpressionUtils.infixToPostfix(originals.get(id)));
        return map;
    }

    public double evaluate(String id, Map<String, Double> vars) {
        Node node = trees.get(id);
        if (node == null) throw new NoSuchElementException("Equation not found: " + id);
        return ExpressionUtils.evaluate(node, vars);
    }
}
