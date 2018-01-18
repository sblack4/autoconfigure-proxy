package com.sblack.autoconfigureproxy;

import org.apache.commons.exec.environment.EnvironmentUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.commons.exec.*;

import java.io.*;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: find better pattern for this <3
 */
public final class ProxyService {
  private String OS;
  private String PATH;
  private String proxyAddress;
  private String proxyType;
  private JSONObject commandsObj;

  public ProxyService(Proxy proxy) {
    this.PATH = System.getenv("PATH");
    this.OS = System.getProperty("os.name").toLowerCase();

    try {
      this.commandsObj = this.readCommands();
      // these are often null and so throw nullpointer exceptions
      this.proxyAddress = proxy.address().toString();
      this.proxyType = proxy.type().toString();
    } catch (NullPointerException ex) {
      this.proxyAddress = "";
      this.proxyType = "DIRECT";
    } catch (Exception ex) {
      // probably no proxy
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public class CollectingLogOutputStream extends LogOutputStream {
    private final List<String> lines = new LinkedList<String>();

    @Override
    protected void processLine(String line, int level) {
      lines.add(line);
    }

    public List<String> getLines() {
      return lines;
    }
  }

  private boolean isMac() {
    return OS.contains("mac");
  }

  private boolean isWindows() {
    return OS.contains("win");
  }

  private boolean isUnix() {
    return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
  }

  public boolean inPath(String exec) {
    return execPath(exec) != null;
  }

  /**
   * parses protocol type of proxy address
   * returns empty string if not applicable
   *
   * @return protocol as string for use in URL
   */
  public String getProtocol() {
    if (proxyType.toLowerCase().equals("http")) {
      return "http";
    }
    return "";
  }

  /**
   * returns port of proxy address
   * defaults to port 80
   *
   * @return port as int
   */
  public String getPort() {
    int port = 80;
    try {
      port = Integer.parseInt(proxyAddress.substring(proxyAddress.lastIndexOf(':') + 1));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Integer.toString(port);
  }

  public String getAddress() {
    String address = proxyAddress;
    try {
      int beginAddress = Math.max(0, proxyAddress.indexOf('/') + 1);
      int endAddress = Math.min(proxyAddress.length(), proxyAddress.lastIndexOf(':'));
      address = proxyAddress.substring(beginAddress, endAddress);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return address;
  }

  /**
   * gets path of executable
   * returns null if not there
   *
   * @param program which you desire the path for
   * @return the path to said executable or null
   */
  public Path execPath(String desiredProgram) {
    // windows computers have the where utility for finding programs in the path
    // while *nix have an analogous which
    String whereOrWhich = isWindows() ? "where" : "which";

    try {
      String find_command = whereOrWhich + " " + desiredProgram;
      CommandLine commandLine = CommandLine.parse(find_command);
      DefaultExecutor defaultExecutor = new DefaultExecutor();
      ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(30 * 1000);

      CollectingLogOutputStream collectingLogOutputStream = new CollectingLogOutputStream();
      PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(collectingLogOutputStream);

      defaultExecutor.setStreamHandler(pumpStreamHandler);
      defaultExecutor.setWatchdog(executeWatchdog);
      defaultExecutor.setExitValues(new int[]{0, 1, 2});
      defaultExecutor.execute(commandLine);

      String output = collectingLogOutputStream.getLines().get(0);
      String parseableOutput = output.substring(output.indexOf(':') + 1, output.length());
      Path foundProgram = Paths.get(parseableOutput);

      if (Files.exists(foundProgram)) {
        return foundProgram;
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * Reads commands from static resource file
   *
   * @return JSON Object containing commands
   */
  private JSONObject readCommands() {
    final String COMMANDS_FILE = "commands.json";
    StringBuilder stringBuilder = new StringBuilder();

    try {
      // the file will be in the same directory as the jar
      // so get the path to this file
      File currentFile = new File(ProxyService.class
          .getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .toURI()
          .getPath());

      String currentPath = currentFile.getAbsolutePath()
          .substring(0, currentFile.getAbsolutePath().lastIndexOf(File.separator));

      FileReader fileReader = new FileReader(currentPath + File.separator + COMMANDS_FILE);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();

      while (line != null) {
        stringBuilder.append(line);
        stringBuilder.append(System.lineSeparator());
        line = bufferedReader.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new JSONObject(stringBuilder.toString());
  }

  /**
   * Run individual command
   */
  private void runCommand(String command) {
    String commandWithProtocol = command.replace("<PROTOCOL>", getProtocol());
    String commandWithAddress = commandWithProtocol.replace("<PROXY_ADDRESS>", getAddress());
    String commandWithPort = commandWithAddress.replace("<PORT>", getPort());
    System.out.println(commandWithPort);

    CommandLine commandLine;

    if(isWindows()) {
     commandLine = CommandLine.parse("cmd.exe /c");
    } else {
     commandLine = CommandLine.parse("bash -c");
    }

    try {
      commandLine.addArgument(commandWithPort);
      DefaultExecutor defaultExecutor = new DefaultExecutor();
      ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(30 * 1000);

      defaultExecutor.setWatchdog(executeWatchdog);
      defaultExecutor.setExitValues(new int[]{0, 1, 2});
      int exitValue = defaultExecutor.execute(commandLine, EnvironmentUtils.getProcEnvironment());

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * handles logic of what proxy to set
   * and how to set it
   *
   * @param program's JSON object
   */
  private void setProxy(JSONObject program) {

    // if it's not in the path there's no use
    // so return
    if (!inPath(program.getString("name"))) {
      return;
    }

    // right now only HTTP proxy...
    // TODO: add support for other types of proxies
    if (this.proxyType.equals("HTTP")) {
      runCommand(program.getString("setCommand"));
    } else { // if null or ProxyServices.this.proxy.type().toString().equals("DIRECT")
      runCommand(program.getString("unsetCommand"));
    }
  }

  /**
   * cycles through programs from commands.json
   * and sets / unsets the proxy
   */
  public void run() {
    JSONArray programs = this.commandsObj.getJSONArray("programs");
    for (int i = 0; i < programs.length(); i++) {
      JSONObject program = programs.getJSONObject(i);
      this.setProxy(program);
    }
  }
}


