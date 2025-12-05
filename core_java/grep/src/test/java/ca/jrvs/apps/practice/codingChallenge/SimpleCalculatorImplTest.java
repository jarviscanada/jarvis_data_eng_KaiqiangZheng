//package ca.jrvs.apps.practice.codingChallenge;
////Junit4
//import static org.junit.Assert.*;
////import static org.junit.jupiter.api.Assertions.assertEquals; this is Junit 5
//
//public class SimpleCalculatorImplTest {
//  SimpleCalculator calculator;
//
//  @org.junit.Before
//  public void setUp() throws Exception {
//    calculator = new SimpleCalculatorImpl();
//  }
//
//  @org.junit.Test
//  public void add() {
//    int expected = 2;
//    int actual = calculator.add(1, 1);
//    assertEquals(expected, actual);
//  }
//
//  @org.junit.Test
//  public void subtract() {
//    int expected = 1;
//    int actual = calculator.subtract(2, 1);
//    assertEquals(expected, actual);
//  }
//
//  @org.junit.Test
//  public void multiply() {
//    int expected = 2;
//    int actual = calculator.multiply(2, 1);
//    assertEquals(expected, actual);
//  }
//
//  @org.junit.Test
//  public void divide() {
//    double expected = 10.0;
//    double actual = calculator.divide(20.0, 2);
//    assertEquals(expected, actual, 0.0001);
//  }
//}