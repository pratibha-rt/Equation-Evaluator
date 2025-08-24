package com.example.equationevaluator;

import com.example.equationevaluator.utils.ExpressionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EquationEvaluatorApplicationTests {

	@Test
	void testSimpleAddition() {
		assertEquals(8.0, ExpressionUtils.evaluate("3 + 5"));
	}

	@Test
	void testSubtractionAndMultiplication() {
		assertEquals(4.0, ExpressionUtils.evaluate("10 - 2 * 3"));
	}

	@Test
	void testParentheses() {
		assertEquals(20.0, ExpressionUtils.evaluate("(2 + 3) * 4"));
	}

	@Test
	void testDivision() {
		assertEquals(5.0, ExpressionUtils.evaluate("25 / 5"));
	}

	@Test
	void testInvalidExpression() {
		assertThrows(IllegalArgumentException.class, () -> {
			ExpressionUtils.evaluate("2 + * 3");
		});
	}

	@Test
	void testDivideByZero() {
		assertThrows(ArithmeticException.class, () -> {
			ExpressionUtils.evaluate("10 / 0");
		});
	}

	@Test
	void testMismatchedParentheses() {
		assertThrows(IllegalArgumentException.class, () -> {
			ExpressionUtils.evaluate("(2 + 3");
		});
	}

	@Test
	void testUnknownVariable() {
		assertThrows(IllegalArgumentException.class, () -> {
			ExpressionUtils.evaluate("x + 2");
		});
	}


}
