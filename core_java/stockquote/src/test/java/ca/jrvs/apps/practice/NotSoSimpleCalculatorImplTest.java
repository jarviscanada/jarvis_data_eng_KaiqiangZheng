package ca.jrvs.apps.practice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotSoSimpleCalculatorImplTest {

  NotSoSimpleCalculator calc; 

  @Mock
  SimpleCalculator mockSimpleCalc; 

  @BeforeEach
  void init() {
    
    calc = new NotSoSimpleCalculatorImpl(mockSimpleCalc);
  }

  @Test
  void test_power() {
    
    int expected = 8; 
    int actual = calc.power(2, 3);
    assertEquals(expected, actual);
  }

  @Test
  void test_abs() {
    

    // 1.
    int input = 10;
    int expected = 10;

    // 2. Stubbing 
    
    when(mockSimpleCalc.multiply(10, -1)).thenReturn(10);

    // 3. 
    int actual = calc.abs(input);

    // 4. 
    assertEquals(expected, actual);

    // 5. Verification 
    
    verify(mockSimpleCalc).multiply(10, -1);
  }

  @Test
  void test_sqrt() {
    // sqrt 
    double expected = 4.0;
    double actual = calc.sqrt(16);
    assertEquals(expected, actual);
  }
}