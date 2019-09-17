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
    
    protected static HashMap<String, HashMap<String, Command>> commands = new HashMap<>(); // maps module -> (name -> Command object)
    protected static HashMap<String, Command> obercommands = new HashMap<>(); // commands that apply regardless of module
    
    protected static ArrayList<String> args = new ArrayList<>(); // represents the arguments passed to the command
    protected static HashMap<String, String> env = new HashMap<>();
    
    protected static void addCommand(Command c) {
        if (!commands.containsKey(c.getModule())) { // module not in commands hashmap yet
            commands.put(c.getModule(), new HashMap<String, Command>()); // create empty hashmap for the module
        }
        commands.get(c.getModule()).put(c.getName(), c); // put name -> Command into the inner hashmap for the module
    }
    
    protected static void addOberCommand(Command c) {
        obercommands.put(c.getName(), c);
    }
    
    private static void putArgs(String[] tokens) {
        args.clear(); // make sure it's empty first
        for (int i = 1; i < tokens.length; i++) { // skip the command name
            args.add(tokens[i]);
        }
    }
    
    public static void main(String[] args) {
        // if we're debugging, don't show the intro
        SetUpCommands.main();
        if (!DEBUG)
            System.out.println(intro);
        Scanner cin = new Scanner(System.in); // declare the console input scanner
        // main loop of get input, do command
        while (true) {
            System.out.print("sci." + module + " > "); // print the console prompt
            String line = cin.nextLine().trim(); // grab the line from the console and trim any trailing whitespace
            String[] tokens = line.split(" "); // split by spaces, so into words/tokens
            putArgs(tokens);
            
        }
    }
}
