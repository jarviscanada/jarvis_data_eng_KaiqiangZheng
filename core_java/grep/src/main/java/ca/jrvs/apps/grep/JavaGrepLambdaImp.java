package ca.jrvs.apps.grep;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImpl {
  private final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

  public static void main(String[] args) {
    if (args.length != 3){
      throw new IllegalArgumentException("USAGE: JavaGrep <regex> <rootPath> <OutFile>");
    }

    JavaGrepImpl javaGrep = new JavaGrepLambdaImp();
    javaGrep.setRex(args[0]);
    javaGrep.setRootPath(args[1]);
    javaGrep.setOutFile(args[2]);

    try {
      javaGrep.process();
    } catch (Exception e) {
      javaGrep.logger.error("Error during processing", e);
    }

  }

  /**
   * Recursively list all files under rootDir using Stream API.
   */
  @Override
  public List<File> listFiles(String rootDir){
    File root = new File(rootDir);
    if (!root.exists()) {
      logger.error("Root path does not exist: " + rootDir);
//      return List.of(); java 9
      return Arrays.asList();
    }
    return listFilesRecursively(root);
  }

  /**
   * Helper method to recursively collect files using Streams.
   */
  private List<File> listFilesRecursively(File root) {
    File[] files = root.listFiles();

    if (files == null) {
//      return List.of(); java 9
      return Arrays.asList();
    }
    // Stream approach:
    // - if directory -> recursively list files
    // - if file -> return as stream
    return Stream.of(files)
        .flatMap(f -> f.isDirectory()
            ? listFilesRecursively(f).stream()
            : Stream.of(f))
        .collect(Collectors.toList());

  }

  /**
   * Read all lines from a file using Files.lines() (lazy stream)
   */
  @Override
  public List<String> readLines(File inputFile) {
    if(!inputFile.isFile()){
      throw new IllegalArgumentException("Not a file: " + inputFile.getAbsolutePath());
    }
    try (Stream<String> lineStream = Files.lines(inputFile.toPath())){
      return lineStream.collect(Collectors.toList());
    } catch (IOException e){
      logger.error("Unable to read file: " + inputFile.getAbsolutePath(), e);
//      return List.of(); java 9
      return Arrays.asList();
    }
  }


  /**
   * Improve match logic by using Stream API (optional override).
   * If not overridden, the parent class implementation will be used.
   */
  @Override
  public boolean containsPattern(String line) {
    return Pattern.compile(getRex()).matcher(line).find();
  }
}
