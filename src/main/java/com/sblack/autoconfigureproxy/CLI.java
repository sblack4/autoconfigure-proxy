package com.sblack.autoconfigureproxy;

import java.net.Proxy;
import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


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

        //
        ProxyRetriever proxyRetriever = new ProxyRetriever();

        for(String[] parsedArg : parsedArgs) {
            String option = parsedArg[0];
            String answer = parsedArg[1];

            switch (option){
                case("display"):
                    // display default proxy url
                    System.out.println(String.format("Proxy: %1$s", proxyRetriever.getProxyAddress()));
                    continue;

                case("output"):
                    // output proxy info to console
                    Proxy proxy = proxyRetriever.getProxy();
                    System.out.println(
                            "\n Default proxy: " +
                            "\n Proxy Type: " + proxy.type().toString() +
                                    "\n Proxy Address: " + proxy.address().toString()
                    );
                    continue;

                case("test"):
                    // take arg (url) and test against proxySelector
                    proxyRetriever.getProxy(answer);
                    continue;

                case("help"):
                    // print help info
                    cliParser.printHelp();
                    System.exit(0);
            }
        }

        // set proxies!
        ProxyService proxyService = new ProxyService(proxyRetriever.getProxy());
        proxyService.run();
    }
}
