package ca.jrvs.apps.grep;

import java.util.Arrays;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaGrepLambdaImpTest {

  private JavaGrepImpl grep;
  private Path tempDir;

  @BeforeEach
  void setUp() throws IOException {
    grep = new JavaGrepLambdaImp();

    // Create a temp directory for testing
    tempDir = Files.createTempDirectory("grepTest");
  }

  @AfterEach
  void tearDown() throws IOException {
    // Clean up temp dir recursively
    Files.walk(tempDir)
        .map(Path::toFile)
        .forEach(File::delete);
  }

  @Test
  void testListFiles() throws IOException {
    // Create nested directories + files
    Path dir1 = Files.createDirectory(tempDir.resolve("sub"));
    Path file1 = Files.createFile(tempDir.resolve("a.txt"));
    Path file2 = Files.createFile(dir1.resolve("b.txt"));

    List<File> files = grep.listFiles(tempDir.toString());

    assertEquals(2, files.size()); // only two files expected
    assertTrue(files.stream().anyMatch(f -> f.getName().equals("a.txt")));
    assertTrue(files.stream().anyMatch(f -> f.getName().equals("b.txt")));
  }

  @Test
  void testReadLines() throws IOException {
    Path file = Files.createFile(tempDir.resolve("test.txt"));
    Files.write(file, Arrays.asList("hello", "world"));

    List<String> lines = grep.readLines(file.toFile());

    assertEquals(2, lines.size());
    assertEquals("hello", lines.get(0));
    assertEquals("world", lines.get(1));
  }

  @Test
  void testContainsPattern() {
    grep.setRex("foo.*bar");

    assertTrue(grep.containsPattern("foo---bar"));
    assertFalse(grep.containsPattern("foo something else"));
  }

  @Test
  void testProcessEndToEnd() throws IOException {
    // 1. Create input file
    Path file = Files.createFile(tempDir.resolve("input.txt"));
    Files.write(file, Arrays.asList(
        "Romeo loves Juliet",
        "Hello World",
        "Juliet loves Romeo"
    ));

    // 2. Setup grep settings
    Path output = tempDir.resolve("out.txt");

    grep.setRex(".*Romeo.*");
    grep.setRootPath(tempDir.toString());
    grep.setOutFile(output.toString());

    // 3. Run the process
    grep.process();

    // 4. Validate output file
    List<String> result = Files.readAllLines(output);

    assertEquals(2, result.size());
    assertTrue(result.get(0).contains("Romeo"));
    assertTrue(result.get(1).contains("Romeo"));
  }
}
