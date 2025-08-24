# Backend Assignment - Store And Solve Algebraic Equations

A **Spring Boot REST API** to **store and evaluate algebraic equations**.  
Supports variables, parentheses, operator precedence, and exponentiation. 

Features : 
- Stores equations in memory
- Evaluate with dynamic variables
- Supports operators: `+`, `-`, `*`, `/`, `^` (power)
- Parentheses and operator precedence
- Rest endpoints with json input/output
- Error handling (invalid expressions, divide by zero, etc.)
- Unit tests with JUnit

Tech stack :
- Java 17+
- Spring Boot
- Maven
- JUnit 5

## Build and Run 
mvn spring-boot:run

The API will be available at 
http://localhost:8081


## API Endpoints

**Store an Equation**

Request
curl -X POST http://localhost:8080/api/equations/store \
  -H "Content-Type: application/json" \
  -d '{"expression":"a + b * c - (d / e) ^ f"}'

Response
{
  "id": 1,
  "expression": "a + b * c - (d / e) ^ f"
}

**Evaluate an Equation**

Request
curl -X POST http://localhost:8080/api/equations/{id}/evaluate \
  -H "Content-Type: application/json" \
  -d '{"a":5,"b":2,"c":3,"d":8,"e":4,"f":2}'

Response
{
  "result": 10.0
}

**List All Equations**

Request
curl http://localhost:8080/api/equations/getAll

Response
[
  {
    "id": 1,
    "expression": "a + b * c - (d / e) ^ f"
  },
  {
    "id": 2,
    "expression": "(x + y) * 2"
  }
]

**Get Equation By ID**

Request
curl http://localhost:8080/api/equations/{id}

Response
{
  "id": 1,
  "expression": "a + b * c - (d / e) ^ f"
}

## Error Handling

The API returns JSON errors for invalid cases:
- Unsupported operator : { "error": "Unsupported operator: @" }
- Divide by zero : { "error": "Division by zero" }
- Invalid Expression : { "error": "Invalid expression format" }

## Run Tests

Run unit Tests with : 
mvn test

## Developed By Pratibha











