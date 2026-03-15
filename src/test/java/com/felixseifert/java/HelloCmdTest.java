package com.felixseifert.java;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;

public class HelloCmdTest extends AbstractHelloTest {

  private String output;

  @BeforeAll
  static void beforeAll() throws IOException, InterruptedException {
    assumeFalse(isOsWindows()); // Do not run cmd tests on Windows
    ProcessBuilder pb = new ProcessBuilder("make", "build");
pb.directory(new java.io.File("src"));
Process build = pb.start();
    build.waitFor();
  }

  private static boolean isOsWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  @Override
  protected void callMainWith(final String[] args) throws IOException, InterruptedException {
    final List<String> cmd = prefaceArgsWithCmd(args);
    final Process cmdRun = new ProcessBuilder(cmd).start();
    cmdRun.waitFor();
    output = new String(cmdRun.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
  }

  private static List<String> prefaceArgsWithCmd(final String[] args) throws IOException {
    final String cmd = "./hello";
    return Stream.concat(Stream.of(cmd), Arrays.stream(args)).toList();
  }

  @Override
  protected String getOutput() {
    return output;
  }
}
