package com.sblack.autoconfigureproxy;

import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * Parses command line args
 * and returns options
 */
public class CommandLineParser {
    private List<CLIOption> cliOptions;
    private Options options;

    private static class CLIOption {
        String opt;
        String longOpt;
        Boolean hasArg;
        String description;
        Boolean isRequired;

        /**
         * Instantiates a new Cli option.
         * This way I can just define a list of them
         * and loop through the list to get / print them
         *
         * @param opt         the short option
         * @param longOpt     the long option
         * @param hasArg      boolean, has arg
         * @param description the description
         * @param isRequired  the is required
         */
        private CLIOption(String opt, String longOpt, Boolean hasArg, String description, Boolean isRequired) {
            this.opt = opt;
            this.longOpt = longOpt;
            this.hasArg = hasArg;
            this.description = description;
            this.isRequired = isRequired;
        }
    }

    /**
     * Instantiates a new Command line parser.
     */
    CommandLineParser() {
        options = new Options();

        // all the cli options here
        this.cliOptions = asList(
                new CLIOption("d", "display", false, "display proxy url", false),
                new CLIOption("o", "output", false, "output proxy information", false),
                new CLIOption("t", "test", true, "test url against proxy", false),
                new CLIOption("h", "help", false, "display this help message", false)
        );

        for(CLIOption opt : cliOptions) {
            Option currentOpt = new Option(opt.opt, opt.longOpt, opt.hasArg, opt.description);
            currentOpt.setRequired(opt.isRequired);
            options.addOption(currentOpt);
        }
    }


    public void printHelp(){
        HelpFormatter formatter = new HelpFormatter();
        final String HEADER = "Get, set, and configure proxy settings";
        final String FOOTER = "Please report bugs/say hi @ github.com/sblack4/autoconfigure-proxy";
        formatter.printHelp("configure-proxy", HEADER, options, FOOTER, true);
    }

    public void printHelp(PrintWriter printWriter){
        HelpFormatter formatter = new HelpFormatter();
        final String HEADER = "Get, set, and configure proxy settings";
        final String FOOTER = "Please report bugs/say hi @ github.com/sblack4/autoconfigure-proxy";

        formatter.printHelp(printWriter
                , HelpFormatter.DEFAULT_WIDTH
            , "configure-proxy"
            , HEADER
            , options
            , formatter.getLeftPadding()
            , formatter.getDescPadding()
            , FOOTER
            , true);
    }

    /**
     * Parse.
     *
     * @param args the args
     * @return the list
     */
    public List<String[]> parse(String[] args){
        DefaultParser parser = new DefaultParser();
        CommandLine cmd;
        List<String[]> answers = new ArrayList<String[]>();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
            System.exit(1);
            return answers;
        }

        for(Option opt : cmd.getOptions()) {
            String[] answer = { opt.getLongOpt(), opt.getValue() };
            answers.add(answer);
        }

        return answers;
    }
}
