//package ca.jrvs.apps.practice;
////Junit4
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class RegexExcImplTest {
//  RegexExc regex; // Just declare not instantiate
//
//  @Before
//  public void setUp() throws Exception {
//    System.out.println("Initiating RegexExcImpl...");
//    regex = new RegexExcImpl();
//  }
//
//  @After
//  public void tearDown() throws Exception {
//    System.out.println("Cleaning RegexExcImpl...");
//    regex = null;
//  }
//
//  @Test
//  public void matchJpeg() {
//    System.out.println("Running testMatchJpeg");
//    assertTrue(regex.matchJpeg("photo.jpg"));
//    assertTrue(regex.matchJpeg("PICTURE.JPEG"));
//    assertFalse(regex.matchJpeg("document.png"));
//  }
//
//  @Test
//  public void matchIp() {
//    System.out.println("Running testMatchIp");
//    assertTrue(regex.matchIp("192.168.0.1"));
//    assertFalse(regex.matchIp("9999.1.1.1")); // Too many
//    assertFalse(regex.matchIp("abc.def.ghi.jkl")); // Not decimal numbers
//  }
//
//  @Test
//  public void isEmptyLine() {
//    System.out.println("Running testIsEmptyLine");
//    assertTrue(regex.isEmptyLine(""));
//    assertTrue(regex.isEmptyLine("   "));
//    assertTrue(regex.isEmptyLine("\t"));
//    assertFalse(regex.isEmptyLine("hello"));
//  }
//}