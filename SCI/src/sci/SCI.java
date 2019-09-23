package sci;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

/**
 *
 * @author Carson Foster
 */
public class SCI {
    private static final boolean DEBUG = false; // will be set to true when debugging the program 
    private static String intro = "Welcome to sci! sci is the Statistical Command Interface, and you can use it\n" // a simple intro message to print at beginning of execution
                                + "to enter data, analyze the data (in a limited fashion), and graph the data.\n"
                                + "Don't abuse it! :)";
    protected static String module = "core"; // tells what module we are currently in
    
    // module "ober" = commands that apply regardless of module, like exit
    protected static HashMap<String, HashMap<String, Command>> commands = new HashMap<>(); // maps module -> (name -> Command object)
    protected static HashMap<String, HashMap<String, HelpMessage>> help = new HashMap<>();
    
    protected static ArrayList<String> args = new ArrayList<>(); // represents the arguments passed to the command
    protected static HashMap<String, String> env = new HashMap<>();
    
    protected static void addCommand(Command c) {
        if (!commands.containsKey(c.getModule())) { // module not in commands hashmap yet
            commands.put(c.getModule(), new HashMap<String, Command>()); // create empty hashmap for the module
        }
        commands.get(c.getModule()).put(c.getName(), c); // put name -> Command into the inner hashmap for the module
    }
    
    protected static void addHelp(HelpMessage h) {
        if (!help.containsKey(h.getModule())) {
            help.put(h.getModule(), new HashMap<String, HelpMessage>());
        }
        help.get(h.getModule()).put(h.getName(), h);
    }
    
    private static void putArgs(String[] tokens) {
        args.clear(); // make sure it's empty first
        for (int i = 1; i < tokens.length; i++) { // skip the command name
            args.add(tokens[i]);
        }
    }
    
    // custom error message
    protected static void error(String msg) {
        System.err.println("sci." + module + ": Error: " + msg);
    }
    
    public static void main(String[] args) {
        SetUpCommands.main();
        // if we're debugging, don't show the intro
        if (!DEBUG)
            System.out.println(intro);
        Scanner cin = new Scanner(System.in); // declare the console input scanner
        // main loop of get input, do command
        while (true) {
            System.out.print("sci." + module + " > "); // print the console prompt
            String line = cin.nextLine().trim(); // grab the line from the console and trim any trailing whitespace
            String[] tokens = line.split(" "); // split by spaces, so into words/tokens
            putArgs(tokens);
            if (commands.get("ober").containsKey(tokens[0])) {
                commands.get("ober").get(tokens[0]).run();
            } else if (commands.get(module).containsKey(tokens[0])) {
                commands.get(module).get(tokens[0]).run();
            } else
                error(tokens[0] + " is not an accepted command.");
        }
    }
}
