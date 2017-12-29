package com.sblack.autoconfigureproxy;

//import org.apache.


/**
 * Entrance Point
 *
 */
public class CLI
{

    //private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * Program entry point.
     *
     * @param args command line args
     */
    public static void main( String[] args ) {

        CommandLineParser cliparser = new CommandLineParser();
        cliparser.Parse(args);

        ProxyRetriever proxyRetriever = new ProxyRetriever();
        proxyRetriever.DisplayHostnames();

    }
}
