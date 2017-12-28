package com.sblack.autoconfigureproxy;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses command line args
 * and returns options
 */
public class CommandLineParser {

    private static class CLIOption {
        String opt;
        String longOpt;
        Boolean hasArg;
        String description;
        Boolean isRequired;

        public CLIOption(String opt, String longOpt, Boolean hasArg, String description, Boolean isRequired) {
            this.opt = opt;
            this.longOpt = longOpt;
            this.hasArg = hasArg;
            this.description = description;
            this.isRequired = isRequired;
        }
    }

    /**
     * Parse.
     *
     * @param args the args
     * @return the list
     */
    public List<String[]> Parse(String[] args){
        Options options = new Options();

        List<CLIOption> clioptions = new ArrayList<CLIOption>();
        clioptions.add(new CLIOption("d", "display", false, "display proxy", false));
        clioptions.add(new CLIOption("o", "output", true, "output pac file", true));

        for(CLIOption opt : clioptions) {
            Option currentOpt = new Option(opt.opt, opt.longOpt, opt.hasArg, opt.description);
            currentOpt.setRequired(opt.isRequired);
            options.addOption(currentOpt);
        }


        DefaultParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("configure-proxy", options);

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
