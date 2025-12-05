# Introduction
This project contains a Java-based Grep application that mimics the Linux `grep` command.
The app allows users to search for matching strings across files in a directory recursively.
It demonstrates the use of Core Java features, Java 8 Lambda and Stream APIs for functional programming, and logging with SLF4J + Logback.
The project is developed using Maven and IntelliJ IDE, and it is dockerized for easier deployment.

# Quick Start
How to use your apps?
## Option 1:
1. Clone the repository:
```bash
git clone <repo-url>  # git clone https://github.com/jarviscanada/jarvis_data_eng_KaiqiangZheng
cd core_java/grep
```
2. Compile and Build Application
```bash
mvn clean package # = mvn clean compile package
```
3. Run locally
```bash
java -jar target/grep-1.0-SNAPSHOT.jar "<regex>" <rootDir> <outputFile>
```
Example:
```bash
java -jar target/grep-1.0-SNAPSHOT.jar ".*Romeo.*Juliet.*" ./data ./out/grep_output.txt
#or (regex with or without the quotation mark)
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImpl .*Romeo.*Juliet.* ./data ./out/grep.txt
#or (regex with or without the quotation mark)
java -classpath target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImpl .*Romeo.*Juliet.* ./data ./out/grep.txt
```

## Option 2:
Run using Docker:
``` bash
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
${docker_user}/grep ".*Romeo.*Juliet.*" /data /log/grep.out   #docker_user = kaiqiangzheng15
```

#Implemenation
## Pseudocode
write `process` method pseudocode.
```plaintext
process():
    files = listFiles(rootPath)  // recursively get all files
    matchedLines = []
    for each file in files:
        lines = readLines(file)
        for each line in lines:
            if line matches regex:
                matchedLines.add(line)
    writeToFile(matchedLines)
```

## Performance Issue
Reading all lines into memory at once may cause high memory usage for large files.
To optimize, we could return Stream<String> from readLines instead of List<String> and use lazy evaluation to process lines one at a time.

# Test
1. Prepare sample text files in the data folder.

2. Run the application with different regex patterns.

3. Verify the output file contains the correct matched lines.

4. Test edge cases such as empty files, nested directories, and invalid files.
JUnit 5 tests are also implemented for listFiles(), readLines(), and process() methods.

# Deployment
The app is dockerized for easier distribution:

Base image: eclipse-temurin:8-jdk-alpine

Jar copied into /usr/local/app/grep/lib/grep.jar

ENTRYPOINT configured to run the jar

Mount data and log folders to pass input and capture outpu

# Improvement
Use Stream<String> for readLines and listFiles to handle extremely large datasets efficiently.

Add command-line options for case-insensitive search and multi-pattern matching.

Implement parallel stream processing to improve performance on multi-core systems.