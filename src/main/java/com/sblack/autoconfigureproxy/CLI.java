package com.sblack.autoconfigureproxy;

import com.github.markusbernhardt.proxy.*;
import java.net.ProxySelector;
import java.net.URI;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import static java.lang.System.exit;


/**
 * Entrance Point
 *
 */
public class CLI
{
//    private static final Logger logger = LoggerFactory.getLogger(CLI.class);

    /**
     * Program entry point.
     *
     * @param args command line args
     */
    public static void main( String[] args ) {

        // get & handle command-line arguments
        CommandLineParser cliParser = new CommandLineParser();
    List<String[]> parsedArgs = cliParser.parse(args);

        ProxyRetriever proxyRetriever = new ProxyRetriever();

        for(String[] parsedArg : parsedArgs) {
            String option = parsedArg[0];
            String answer = parsedArg[1];

            switch (option){
                case("display"):
                    // display default proxy url
                    String proxyString = proxyRetriever.getProxyString();
                    System.out.println(String.format("Proxy: %1$s", proxyString));
                    continue;

                case("output"):
                    // output proxy info to console
                    continue;

                case("test"):
                    // take arg (url) and test against proxySelector
                    continue;

                case("help"):
                    // print help info
                    cliParser.printHelp();
            }
        }
    }
}
