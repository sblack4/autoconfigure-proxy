package com.sblack.autoconfigureproxy;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.*;

public class CommandLineParserTest {
  private CommandLineParser commandLineParser = new CommandLineParser();
  private final static String helpMessage = "usage: configure-proxy [-d] [-h] [-o] [-t <arg>]" + System.getProperty("line.separator") +
      "Get, set, and configure proxy settings" + System.getProperty("line.separator") +
      " -d,--display      display proxy url" + System.getProperty("line.separator") +
      " -h,--help         display this help message" + System.getProperty("line.separator") +
      " -o,--output       output proxy information" + System.getProperty("line.separator") +
      " -t,--test <arg>   test url against proxy" + System.getProperty("line.separator") +
      "Please report bugs/say hi @ github.com/sblack4/autoconfigure-proxy";

  @Test
  public void printHelp() throws Exception {
    StringWriter stringWriter = new StringWriter();
    commandLineParser.printHelp(new PrintWriter(stringWriter));
    String output = stringWriter.toString().trim();
    assertEquals(output, helpMessage);
  }

  @Test
  public void parseDisplay() throws Exception {
    String[] args = new String[] { "-d" };
    List<String[]> answers = commandLineParser.parse(args);
    String[] expectOutput = new String[] { "display", null };
    assertArrayEquals(answers.get(0), expectOutput);
  }

  // TODO(@sblack4): test more permutations of parse arguments

}