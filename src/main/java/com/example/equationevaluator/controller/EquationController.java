package com.example.equationevaluator.controller;

import com.example.equationevaluator.models.Equation;
import com.example.equationevaluator.service.EquationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equations")
@RequiredArgsConstructor
public class EquationController {

    private final EquationService service;

    @PostMapping("/store")
    public ResponseEntity<Map<String, String>> store(@RequestBody Equation dto) {
        String id = service.save(dto.getExpression());
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Map<String, Object>>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String id) {
        Map<String, Object> eq = service.get(id);
        if (eq == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(eq);
    }

    @PostMapping("/{id}/evaluate")
    public ResponseEntity<Map<String, Object>> evaluate(@PathVariable String id,
                                                        @RequestBody Map<String, Double> vars) {
        double result = service.evaluate(id, vars);
        return ResponseEntity.ok(Map.of("id", id, "result", result));
    }
}
