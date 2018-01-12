package com.sblack.autoconfigureproxy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.Proxy;
import org.json.*;
import java.io.File;
//import org.apache.commons.exec;

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

    public Path execPath(String desiredProgram) {
        ProcessBuilder pb = new ProcessBuilder(isWindows() ? "where" : "which", desiredProgram);
        Path foundProgram = null;
        try {
            Process proc = pb.start();
            int errCode = proc.waitFor();
            if (errCode == 0) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    foundProgram = Paths.get(reader.readLine());
                } catch (IOException ex) {
                    System.out.println("1no "+ desiredProgram);
                    ex.printStackTrace();
                }
            } else {
                System.out.println("2no "+ desiredProgram + "\n errcode " + errCode);
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("3no "+ desiredProgram);
            ex.printStackTrace();
        }
        return foundProgram;
    }

    /**
     * Reads commands from static resource file
     *
     * @return JSON Object containing commands
     */
    private JSONObject readCommands() {
        final String COMMANDS_FILE =  "commands.json";
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

            FileReader fileReader = new FileReader(currentPath + File.separator  + COMMANDS_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            while(line != null) {
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
     * @param command
     */
    private void runCommand(String command) {
        System.out.println(command);
    }

    /**
     * handles logic of what proxy to set
     * and how to set it
     * @param program's JSON object
     */
    private void setProxy(JSONObject program) {

        if(!inPath(program.getString("name"))) {
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
        for(int i=0; i<programs.length(); i++) {
            JSONObject program = programs.getJSONObject(i);
            this.setProxy(program);
        }
    }
}


