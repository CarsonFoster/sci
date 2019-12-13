package sci;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import sci.parsing.*;

/**
 *
 * @author Carson Foster
 */
public class SCI {
    protected static final boolean DEBUG = true; // will be set to true when debugging the program 
    private static String intro = "Welcome to sci! sci is the Statistical Command Interface, and you can use it\n" // a simple intro message to print at beginning of execution
                                + "to enter data, analyze the data (in a limited fashion), and graph the data.\n"
                                + "Don't abuse it! :)";
    protected static String module = "core"; // tells what module we are currently in
    protected static Scanner cin;
    
    // module "ober" = commands that apply regardless of module, like exit
    protected static HashMap<String, HashMap<String, Command>> commands = new HashMap<>(); // maps module -> (name -> Command object)
    protected static HashMap<String, HashMap<String, HelpMessage>> help = new HashMap<>(); // maps module -> (name -> HelpMessage object)
    
    protected static ArrayList<String> args = new ArrayList<>(); // represents the arguments passed to the command
    protected static HashMap<String, String> env = new HashMap<>(); // represents environment variables
    
    protected static HashMap<String, StatList> quantitative = new HashMap<>();
    protected static HashMap<String, StatList> categorical = new HashMap<>();
    
    public static CommandResult res = null;
    public static boolean console = true;
    
    protected static boolean checkVariable(String var) {
        return var.matches("\\$[a-zA-Z0-9_]+");
    }
    
    protected static void addCommand(Command c) { // adds a command into the command hashmap
        if (!commands.containsKey(c.getModule())) { // module not in commands hashmap yet
            commands.put(c.getModule(), new HashMap<String, Command>()); // create empty hashmap for the module
        }
        commands.get(c.getModule()).put(c.getName(), c); // put name -> Command into the inner hashmap for the module
    }
    
    protected static void addHelp(HelpMessage h) { // adds a help message into the help hashmap
        if (!help.containsKey(h.getModule())) { // module not in help hasmap yet
            help.put(h.getModule(), new HashMap<String, HelpMessage>()); // create empty hashmap for the module
        }
        help.get(h.getModule()).put(h.getName(), h); // put name -> HelpMessage into the inner hashmap for the module
    }
    
    // puts the tokens into the args arraylist so that the commands can access them
    protected static void putArgs(String[] tokens) {
        args.clear(); // make sure it's empty first
        if (tokens == null) return;
        for (int i = 1; i < tokens.length; i++) { // skip the command name
            args.add(tokens[i]);
        }
    }
    
    // custom error message
    public static void error(String msg) {
        System.err.println("sci." + module + ": Error: " + msg); // Ex: sci.core: Error: message
        res = null;
    }
    
    public static void runCommand(String line) {
        String[] tokens = line.split(" "); // split by spaces, so into words/tokens
        putArgs(tokens); // makes the arguments available to the commands
        if (commands.get("ober").containsKey(tokens[0])) { // if it's an ober command
            commands.get("ober").get(tokens[0]).run();
        } else if (commands.get(module).containsKey(tokens[0])) { // otherwise it must be in the current module
            commands.get(module).get(tokens[0]).run();
        } else if (line.startsWith("$")) {
            if (!checkVariable(line)) {
                error("\"" + line + "\" is not a valid variable name.");
            }
            System.out.println(env.get(line));
        } else if (line.startsWith("# ")) {
            String expr;
            try {
                expr = line.substring(2);
            } catch (Exception e) {
                error("must be an expression to interpret.");
                return;
            }
            List<Token> ts = Lexer.lex(expr);
            Parser p = new Parser(ts);
            Node r = p.parse();
            ParseTree pt = new ParseTree();
            if (!pt.syntax(r)) {
                error("syntax error.");
                return;
            }
            BigDecimal res = pt.parse(r);
            if (res != null)
                System.out.println(res);
        } else
            error("\"" + tokens[0] + "\" is not an accepted command."); // otherwise, it's an error
    }
    
    public static void main(String[] args) {
        // set up the ober and core commands
        SetUpCommands.main();
        // if we're debugging, don't show the intro
        if (!DEBUG)
            System.out.println(intro);
        cin = new Scanner(System.in); // declare the console input scanner
        // main loop of get input, do command
        while (true) {
            System.out.print("sci." + module + " > "); // print the console prompt
            String line = cin.nextLine().trim(); // grab the line from the console and trim any trailing whitespace
            runCommand(line);
        }
    }
}
