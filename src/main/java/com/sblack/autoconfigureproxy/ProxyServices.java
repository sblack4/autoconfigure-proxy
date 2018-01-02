package com.sblack.autoconfigureproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.Proxy;
import org.apache.commons.exec;

/**
 * No static (& non-nested) classes in java apparently
 * Thank you stack overflow https://stackoverflow.com/questions/7486012/static-classes-in-java
 * This class contains all the services for which we will want to set, unset, and etc the proxy
 * TODO: find better pattern for this <3
 */
public final class ProxyService {
    private String OS;
    private String PATH;
    private String proxyAddress;
    private String proxyType;

    public ProxyService(Proxy proxy) {
        this.PATH = System.getenv("PATH");
        this.OS = System.getProperty("os.name").toLowerCase();

        try {
            this.proxyAddress = proxy.address().toString();
            this.proxyType = proxy.type().toString();
        } catch (Exception ex) {
            // probably no proxy
            System.out.println(ex);
            ex.printStackTrace();
            this.proxyAddress = "";
            this.proxyType = "DIRECT";
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

    private boolean inPath(String exec) {
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
                    System.out.println("no");
                }
            } else {
                System.out.println("no");
            }
        } catch (IOException ex) {
            System.out.println("no");
        } catch (InterruptedException ex) {
            System.out.println("no");
        }
        return foundProgram;
    }


    private void runCommand(String command) {

    }

    public void setProxy(){
        if (ProxyServices.this.proxyType.equals("HTTP")) {
            runCommand(setString);
        } else { // if null or ProxyServices.this.proxy.type().toString().equals("DIRECT")
            runCommand(unsetString);
        }
    }



    public boolean inPath() {
        return false;
    }


}
