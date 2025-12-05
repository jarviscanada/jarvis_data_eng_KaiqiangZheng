package ca.jrvs.apps.grep;
//Junit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaGrepImplTest {
  private static final Logger logger = LoggerFactory.getLogger(JavaGrepImplTest .class);
  private JavaGrep grep;

  @BeforeEach
  void setup() {
    grep = new JavaGrepImpl();
    grep.setRootPath("src/test/resources");
    grep.setOutFile("test_output.txt");
    grep.setRex(".*Romeo.*Juliet.*");
  }

  //@org.junit.jupiter.api.Test
  @Test
  void testListFiles() {
    List<File> files = grep.listFiles(grep.getRootPath());
    logger.info("These are the files under the dir:\n" + files);
    assertFalse(files.isEmpty(), "Files should not be empty");
  }

  @Test
  void testReadLines() {
    File file = new File("src/test/resources/shakespeare.txt");
    List<String> lines = grep.readLines(file);
    assertTrue(lines.size() > 0);
  }

  @Test
  void testContainsPattern() throws Exception {
    String line = "Romeo and Juliet";
    assertTrue(line.matches(".*Romeo.*"));
  }
}