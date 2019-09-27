package sci;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    public static void main() {
        // put all creation of commands and help messages here
        
        // HelpMessages for the modules themselves: help_msg name, null usage, null flags
        HelpMessage core = new HelpMessage("core", "help_msg", null, "core: The main module, from which you can access the other modules.", null);
        SCI.addHelp(core);
        HelpMessage ober = new HelpMessage("ober", "help_msg", null, "ober: Commands in this module can be accessed from anywhere.", null);
        SCI.addHelp(ober);
        
        // exit command definition; it's an obercommand
        Command exit = new Command("ober", "exit", "exit", "Quits the program.", null) {
            protected void run() {
                System.exit(0);
            }
        };
        
        // help command definition; it's an obercommand
        Command help = new Command("ober", "help", "help [<command>]", "Provides helpful information on commands.", 
                "When no command is provided, the help message for the current module is printed.",
                new String[][] {new String[] {"command", "Command/module to get help on; must be in the current module or an obercommand"}}) {
            
            protected void helpModule(String module) {
                System.out.println("Note: `help help` and `help ober` are useful too!");
                System.out.println(SCI.help.get(module).get("help_msg"));
                for (String key : SCI.help.get(module).keySet()) {
                    if (key.equals("help_msg")) continue;
                    System.out.println(key + "\t\t\t" + SCI.help.get(module).get(key).getDescription());
                }
            }        
            
            protected void run() {
                // if there is more than 1 argument -> error
                if (Command.getArgs().size() > 1) {
                    SCI.error("Too many arguments: `help` takes maximum 1 argument");
                    return;
                }
                // if there aren't arguments -> help for the current module
                if (Command.getArgs().size() == 0) {
                    helpModule(SCI.module);
                    return;
                }
                String command = Command.getArgs().get(0);
                // if the argument is a module -> help for that module
                if (SCI.help.keySet().contains(command)) {
                    helpModule(command);
                    return;
                }
                
                // is it an obercommand? is it a command in the current module? otherwise, error
                if (SCI.help.get("ober").containsKey(command)) {
                    System.out.println(SCI.help.get("ober").get(command));
                } else if (SCI.help.get(SCI.module).containsKey(command)) {
                    System.out.println(SCI.help.get(SCI.module).get(command));
                } else {
                    SCI.error("Help for " + command + " not found.");
                }
            }
        };
        
        // pwm command definition; it's an obercommand
        // Print Working Module
        Command pwm = new Command("ober", "pwm", "pwm", "Prints the current module.", null) {
            protected void run() {
                System.out.println(SCI.module);
            }
        };
        
        // back command definition; it's an obercommand
        // changes from a module back to the core module
        Command back = new Command("ober", "back", "back", "Returns to the core module.", null) {
            protected void run() {
                if (SCI.module.equals("core")) { // if you're already in the core module -> error
                    SCI.error("The current module is already core.");
                    return;
                }
                SCI.module = "core";
            }
        };
        
        // set up the basic module commands
        // TODO: write the content for the modules
        Command data = new Command("core", "data", "data", "Enters statistical data into the program.", null) {
            protected void run() {
                SCI.module = "data";
                if (SCI.DEBUG) System.out.println(SCI.module);
            }
        };
        
        Command analyze = new Command("core", "analyze", "analyze", "Analyzes the statistical data input into the program.", null) {
            protected void run() {
                SCI.module = "analysis";
                if (SCI.DEBUG) System.out.println(SCI.module);
            }
        };
        
        Command graph = new Command("core", "graph", "graph", "Graphs statistical data input into the program.", null) {
            protected void run() {
                SCI.module = "graphing";
                if (SCI.DEBUG) System.out.println(SCI.module);
            }
        };
        
        Command addQuantitative = new Command("data", "add", "add <list_name>", "Prompts for quantitative data (each separated by a space) and enters it into <list_name>.",
            new String[][] {new String[] {"list_name", "List to enter the data into."}}) {
               protected void run() {
                   if (Command.getArgs().size() != 1) {
                       SCI.error("`add` takes exactly one argument.");
                       return;
                   }
                   if (!StatList.checkListName(Command.getArgs().get(0))) {
                       SCI.error("List name \"" + Command.getArgs().get(0) + "\" is already in use.");
                       return;
                   }
                   System.out.print("> ");
                   String line = SCI.cin.nextLine();
                   StatList nums = StatList.parseQuantitative(line);
                   if (nums == null) return;
                   SCI.quantitative.put(Command.getArgs().get(0), nums);
                   if (SCI.DEBUG) {
                       StatList got = SCI.quantitative.get(Command.getArgs().get(0));
                       System.out.println(got);
                   }
               }
            };
        Command importQuantitative = new Command("data", "import", "import <path> <list_name>", "Opens the file at <path> and puts the quantitative data there into <list_name>.",
            new String[][] {new String[] {"path", "Path to the file containing the quantitative data."}, new String[] {"list_name", "List to enter the data into."}}) {
              protected void run() {
                  if (Command.getArgs().size() != 2) {
                      SCI.error("`import` takes exactly two arguments.");
                      return;
                  }
                  
                  if (!StatList.checkListName(Command.getArgs().get(1))) {
                       SCI.error("List name \"" + Command.getArgs().get(1) + "\" is already in use.");
                       return;
                   }
                  
                  String path = Command.getArgs().get(0);
                  Scanner fin;
                  try {
                    fin = new Scanner(new File(path));
                  } catch(Exception e) {
                      SCI.error("Could not open file at \"" + path + "\"");
                      return;
                  }
                  String line = "";
                  while (fin.hasNextLine()) {
                      line += fin.nextLine().trim() + " ";
                  }
                  StatList data = StatList.parseQuantitative(line.substring(0, line.length() - 1));
                  SCI.quantitative.put(Command.getArgs().get(1), data);
                  if (SCI.DEBUG) {
                       StatList got = SCI.quantitative.get(Command.getArgs().get(1));
                       System.out.println(got);
                   }
              }  
            };
        
        Command addCategorical = new Command("data", "\\add", "\\add <list_name>", "Prompts for categorical data and enters it into <list_name>.", "Each categorical datum must be in the form (a b c .. n)",
            new String[][] {new String[] {"list_name", "List to enter the data into."}}) {
               protected void run() {
                   if (Command.getArgs().size() != 1) {
                       SCI.error("`\\add` takes exactly one argument.");
                       return;
                   }
                   
                   if (!StatList.checkListName(Command.getArgs().get(0))) {
                       SCI.error("List name \"" + Command.getArgs().get(0) + "\" is already in use.");
                       return;
                   }
                   
                   System.out.print("> ");
                   String line = SCI.cin.nextLine();
                   StatList cat = StatList.parseCategorical(line);
                   SCI.categorical.put(Command.getArgs().get(0), cat);
                   if (SCI.DEBUG) {
                       StatList got = SCI.categorical.get(Command.getArgs().get(0));
                       System.out.println(got);
                   }
               }
            };
        Command importCategorical = new Command("data", "\\import", "\\import <path> <list_name>", "Opens the file at <path> and puts the categorical data there into <list_name>.", "Each categorical datum must be in the form (a b c .. n)",
            new String[][] {new String[] {"path", "Path to the file containing the categorical data."}, new String[] {"list_name", "List to enter the data into."}}) {
              protected void run() {
                  if (Command.getArgs().size() != 2) {
                      SCI.error("`\\import` takes exactly two arguments.");
                      return;
                  }
                  
                  if (!StatList.checkListName(Command.getArgs().get(1))) {
                       SCI.error("List name \"" + Command.getArgs().get(1) + "\" is already in use.");
                       return;
                   }
                  
                  String path = Command.getArgs().get(0);
                  Scanner fin;
                  try {
                    fin = new Scanner(new File(path));
                  } catch(Exception e) {
                      SCI.error("Could not open file at \"" + path + "\"");
                      return;
                  }
                  String line = "";
                  while (fin.hasNextLine()) {
                      line += fin.nextLine().trim() + " ";
                  }
                  StatList data = StatList.parseCategorical(line.substring(0, line.length() - 1));
                  SCI.categorical.put(Command.getArgs().get(1), data);
                  if (SCI.DEBUG) {
                       StatList got = SCI.categorical.get(Command.getArgs().get(1));
                       System.out.println(got);
                   }
              }  
            };
        Command view = new Command("data", "view", "view <list_name>", "Prints the contents of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to print the contents of."}}) {
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`view` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    x = SCI.categorical.get(Command.getArgs().get(0));
                }
                if (x == null) {
                    System.out.println("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                System.out.println(x);
            }
        };
    }
}
