package com.sblack.autoconfigureproxy;

import org.apache.commons.cli.*;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

/**
 * Parses command line args
 * and returns options
 */
public class CommandLineParser {
    private List<CLIOption> clioptions;
    private Options options;

    private static class CLIOption {
        /**
         * The Opt.
         */
        String opt;
        /**
         * The Long opt.
         */
        String longOpt;
        /**
         * The Has arg.
         */
        Boolean hasArg;
        /**
         * The Description.
         */
        String description;
        /**
         * The Is required.
         */
        Boolean isRequired;

        /**
         * Instantiates a new Cli option.
         *
         * @param opt         the opt
         * @param longOpt     the long opt
         * @param hasArg      the has arg
         * @param description the description
         * @param isRequired  the is required
         */
        public CLIOption(String opt, String longOpt, Boolean hasArg, String description, Boolean isRequired) {
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
    public CommandLineParser() {
        options = new Options();

        this.clioptions = asList(
                new CLIOption("d", "display", false, "display proxy", false),
                new CLIOption("o", "output", true, "output pac file", false)
        );

        for(CLIOption opt : clioptions) {
            Option currentOpt = new Option(opt.opt, opt.longOpt, opt.hasArg, opt.description);
            currentOpt.setRequired(opt.isRequired);
            options.addOption(currentOpt);
        }
    }


    private void PrintHelp(){
        HelpFormatter formatter = new HelpFormatter();
        final String HEADER = "Get, set, and configure proxy settings";
        final String FOOTER = "";
        formatter.printHelp("configure-proxy", HEADER, options, FOOTER, true);
    }

    /**
     * Parse.
     *
     * @param args the args
     * @return the list
     */
    public List<String[]> Parse(String[] args){

        DefaultParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            PrintHelp();

            System.exit(1);
            return null;
        }

        List<String[]> answers = new ArrayList<String[]>();

        for(CLIOption opt : clioptions) {
            String[] answer = { opt.longOpt, cmd.getOptionValue(opt.opt)};
            answers.add(answer);
        }

        return answers;
    }
}
