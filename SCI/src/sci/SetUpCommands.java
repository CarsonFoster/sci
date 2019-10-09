package sci;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    protected static String modules = "";
    public static void main() {
        // put all creation of commands and help messages here
        
        // HelpMessages for the modules themselves: help_msg name, null usage, null flags
        HelpMessage core = new HelpMessage("core", "help_msg", null, "core: The main module, from which you can access the other modules.", null);
        SCI.addHelp(core);
        HelpMessage ober = new HelpMessage("ober", "help_msg", null, "ober: Commands in this module can be accessed from anywhere.", null);
        SCI.addHelp(ober);
        HelpMessage data_help = new HelpMessage("data", "help_msg", null, "data: Enter data into the program.", null);
        SCI.addHelp(data_help);
        HelpMessage analysis_help = new HelpMessage("analysis", "help_msg", null, "analysis: Analyze the entered data.", null);
        SCI.addHelp(analysis_help);
        HelpMessage graph_help = new HelpMessage("graph", "help_msg", null, "graph: Graph the data in various ways.", null);
        
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
                ArrayList<String> keys = new ArrayList<>();
                keys.addAll(SCI.help.get(module).keySet());
                Collections.sort(keys);
                for (String key : keys) {
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
                SCI.res = null;
            }
        };
        
        // pwm command definition; it's an obercommand
        // Print Working Module
        Command pwm = new Command("ober", "pwm", "pwm", "Prints the current module.", null) {
            protected void run() {
                System.out.println(SCI.module);
                SCI.res = null;
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
                SCI.res = null;
            }
        };
        
        Command suppress = new Command("ober", "suppress", "suppress <command> [<args>]", "Suppresses the output of <command>, called with <args>.", 
            new String[][] {new String[] {"command", "The command to call."}, new String[] {"args", "The arguments to pass to <command>."}}) {
            
            protected void run() {
                if (SCI.args.size() < 1) {
                    SCI.error("`suppress` takes at minimum one argument.");
                }
                
                String[] args = null;
                System.out.println(SCI.args);
                String cmd = SCI.args.get(0);
                SCI.putArgs(args);
                SCI.console = false;
                if (SCI.commands.get("ober").containsKey(cmd)) { // if it's an ober command
                    SCI.commands.get("ober").get(cmd).run();
                } else if (SCI.commands.get(SCI.module).containsKey(cmd)) { // otherwise it must be in the current module
                    SCI.commands.get(SCI.module).get(cmd).run(); 
                } else
                    SCI.error("\"" + cmd + "\" is not an accepted command."); // otherwise, it's an error
                SCI.console = true;
            }
                
        };
        
        // set up the basic module commands
        // TODO: write the content for the modules
        Command data = new Command("core", "data", "data", "Enters statistical data into the program.", null) {
            protected void run() {
                if (!modules.contains("data")) {
                    modules += "_data_";
                    SetUpData.main();
                }
                SCI.module = "data";
                if (SCI.DEBUG) System.out.println(SCI.module);
                SCI.res = null;
            }
        };
        
        Command analyze = new Command("core", "analyze", "analyze", "Analyzes the statistical data input into the program.", "All commands round to the 5th decimal place after every discrete operation.", null) {
            protected void run() {
                if (!modules.contains("analysis")) {
                    modules += "_analysis_";
                    SetUpAnalysis.main();
                }
                SCI.module = "analysis";
                if (SCI.DEBUG) System.out.println(SCI.module);
                SCI.res = null;
            }
        };
        
        Command graph = new Command("core", "graph", "graph", "Graphs statistical data input into the program.", null) {
            protected void run() {
                SCI.module = "graphing";
                if (SCI.DEBUG) System.out.println(SCI.module);
                SCI.res = null;
            }
        };
    }
}

class SetUpData {
    public static void main() {
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
                       SCI.res = null;
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
                      line = line.replaceAll(",", " ");
                      StatList data = StatList.parseQuantitative(line.substring(0, line.length() - 1));
                      SCI.quantitative.put(Command.getArgs().get(1), data);
                      if (SCI.DEBUG) {
                           StatList got = SCI.quantitative.get(Command.getArgs().get(1));
                           System.out.println(got);
                       }
                      SCI.res = null;
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
                       SCI.res = null;
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
                      SCI.res = null;
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
                        SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                        return;
                    }
                    System.out.println(x);
                    SCI.res = null;
                }
            };
    }
}

class SetUpAnalysis {
    private static BigDecimal median(StatList x, int start, int end) {
        StatList xCopy = new StatList(x);
        Collections.sort(xCopy);
        BigDecimal median;
        // end - start + 1 = len; start + len/2 and start + len/2 - 1
        // [0 1 2 3 4 5] len/2-1 and len/2
        int length = end - start + 1;
        if (length % 2 == 0)
            median = ((QuantitativeDatum)xCopy.get(start + length / 2 - 1)).getValue().add(((QuantitativeDatum)xCopy.get(start + length / 2)).getValue(), mc).divide(new BigDecimal(2), mc);
        else
            median = ((QuantitativeDatum)xCopy.get(start + length / 2)).getValue();
        return median;
    }
    
    private static BigDecimal q1(StatList x) {
        // change logic to just use indices
        // 0 through len/2 - 1
        return median(x, 0, x.size()/2 - 1);
    }
    
    private static BigDecimal q3(StatList x) {
        // len/2+1 through end
        return median(x, x.size()/2 + 1, x.size() - 1);
    }
    
    private static BigDecimal mean(StatList x) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Datum datum : x) {
            sum = sum.add(((QuantitativeDatum)datum).getValue(), mc);
        }
        sum = sum.divide(new BigDecimal(x.size()), mc);
        return sum;
    }
    
    protected static MathContext mc = new MathContext(5);
    public static void main() {
        Command xbar = new Command("analysis", "xbar", "xbar <list_name>", "Prints the sample mean of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the mean of."}}){
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`xbar` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal sum = mean(x);
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
        Command xbarCategorical = new Command("analysis", "\\xbar", "\\xbar <list_name> <index>", "Prints the sample mean of categorical list <list_name>'s <index>th elements", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the mean of."}, new String[] {"index", "The position of the quantitative data in the categorical unit list <list_name>."}}){
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\xbar` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal sum = mean(xCopy);
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
        Command xtilde = new Command("analysis", "xtilde", "xtilde <list_name>", "Prints the median of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the median of."}}) {
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`xtilde` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal med = median(x, 0, x.size() - 1);
                if (SCI.console)
                    System.out.println(med);
                SCI.res = new CommandResult(med);
            }
        };
        Command xtildeCategorical = new Command("analysis", "\\xtilde", "\\xtilde <list_name> <index>", "Prints the median of categorical list <list_name>'s <index>th elements.", "Indices start at 1.",
                new String[][] {new String[] {"list_name", "The name of the list to take the median of."}, new String[] {"index", "The position of the quantitative data in the categorical unit list <list_name>."}}) {
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\xtilde` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal med = median(xCopy, 0, xCopy.size() - 1);
                if (SCI.console)
                    System.out.println(med);
                SCI.res = new CommandResult(med);
            }
        };
        Command iqr = new Command("analysis", "iqr", "iqr <list_name>", "Prints the IQR of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the IQR of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`iqr` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal iqr = q3(x).subtract(q1(x), mc);
                if (SCI.console)
                    System.out.println(iqr);
                SCI.res = new CommandResult(iqr);
            }
        };
        Command iqrCategorical = new Command("analysis", "\\iqr", "\\iqr <list_name> <index>", "Prints the IQR of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the IQR of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\iqr` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal iqr = q3(xCopy).subtract(q1(xCopy), mc);
                if (SCI.console)
                    System.out.println(iqr);
                SCI.res = new CommandResult(iqr);
            }
        };
        Command q3 = new Command("analysis", "q3", "q3 <list_name>", "Prints the third quartile of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the third quartile of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`q3` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal q3 = q3(x);
                if (SCI.console)
                    System.out.println(q3);
                SCI.res = new CommandResult(q3);
            }
        };
        Command q3Categorical = new Command("analysis", "\\q3", "\\q3 <list_name> <index>", "Prints the third quartile of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the third quartile of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\q3` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal q3 = q3(xCopy);
                if (SCI.console)
                    System.out.println(q3);
                SCI.res = new CommandResult(q3);
            }
        };
        Command q1 = new Command("analysis", "q1", "q1 <list_name>", "Prints the first quartile of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the first quartile of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`q1` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                
                BigDecimal q1 = q1(x);
                if (SCI.console)
                    System.out.println(q1);
                SCI.res = new CommandResult(q1);
            }
        };
        Command q1Categorical = new Command("analysis", "\\q1", "\\q1 <list_name> <index>", "Prints the first quartile of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the first quartile of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\q1` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal q1 = q1(xCopy);
                if (SCI.console)
                    System.out.println(q1);
                SCI.res = new CommandResult(q1);
            }
        };
        Command min = new Command("analysis", "min", "min <list_name>", "Prints the minimum value in <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the minimum of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`min` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal smallest = ((QuantitativeDatum)x.get(0)).getValue();
                for (Datum el : x) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    if (val.compareTo(smallest) < 0) smallest = val;
                }
                if (SCI.console)
                    System.out.println(smallest);
                SCI.res = new CommandResult(smallest);
            }
        };
        Command minCategorical = new Command("analysis", "\\min", "\\min <list_name> <index>", "Prints the minimum of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the minimum of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\min` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal smallest = ((QuantitativeDatum)xCopy.get(0)).getValue();
                for (Datum el : xCopy) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    if (val.compareTo(smallest) < 0) smallest = val;
                }
                if (SCI.console)
                    System.out.println(smallest);
                SCI.res = new CommandResult(smallest);
            }
        };
        Command max = new Command("analysis", "max", "max <list_name>", "Prints the maximum value in <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the maximum of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`max` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal smallest = ((QuantitativeDatum)x.get(0)).getValue();
                for (Datum el : x) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    if (val.compareTo(smallest) < 0) smallest = val;
                }
                if (SCI.console)
                    System.out.println(smallest);
                SCI.res = new CommandResult(smallest);
            }
        };
        Command maxCategorical = new Command("analysis", "\\max", "\\max <list_name> <index>", "Prints the maximum of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the maximum of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\max` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                BigDecimal biggest = ((QuantitativeDatum)xCopy.get(0)).getValue();
                for (Datum el : xCopy) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    if (val.compareTo(biggest) > 0) biggest = val;
                }
                if (SCI.console)
                    System.out.println(biggest);
                SCI.res = new CommandResult(biggest);
            }
        };
        Command lsigma = new Command("analysis", "lsigma", "lsigma <list_name>", "Prints the population standard deviation of <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to take the population standard deviation of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`lsigma` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal mean = mean(x);
                BigDecimal sum = BigDecimal.ZERO;
                // sqrt(sum of (y-bar - y)^2 / n)
                for (Datum el : x) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    BigDecimal diff = mean.subtract(val, mc);
                    diff = diff.multiply(diff, mc);
                    sum = sum.add(diff, mc);
                }
                sum = sum.divide(new BigDecimal(x.size()), mc);
                sum = QuantitativeDatum.sqrt(sum, mc.getPrecision());
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
        Command lsigmaCategorical = new Command("analysis", "\\lsigma", "\\lsigma <list_name> <index>", "Prints the population standard deviation of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to take the population standard deviation of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\lsigma` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                
                BigDecimal mean = mean(xCopy);
                BigDecimal sum = BigDecimal.ZERO;
                // sqrt(sum of (y-bar - y)^2 / n)
                for (Datum el : xCopy) {
                    BigDecimal val = ((QuantitativeDatum)el).getValue();
                    BigDecimal diff = mean.subtract(val, mc);
                    diff = diff.multiply(diff, mc);
                    sum = sum.add(diff, mc);
                }
                sum = sum.divide(new BigDecimal(x.size()), mc);
                sum = QuantitativeDatum.sqrt(sum, mc.getPrecision());
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
        Command n = new Command("analysis", "n", "n <list_name>", "Prints the number of elements in <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to find the number of elements of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`n` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int s = x.size();
                BigDecimal size = new BigDecimal(s);
                if (SCI.console)
                    System.out.println(s);
                SCI.res = new CommandResult(size);
                
            }
        };
        Command nCategorical = new Command("analysis", "\\n", "\\n <list_name> <index>", "Prints the number of elements in categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to find the number of elements of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\n` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                int s = xCopy.size();
                if (SCI.console)
                    System.out.println(s);
                SCI.res = new CommandResult(new BigDecimal(s));
            }
        };
        Command mode = new Command("analysis", "mode", "mode <list_name>", "Prints the mode of elements in <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to find the mode of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`mode` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                HashMap<QuantitativeDatum, Integer> count = new HashMap<>();
                StatList xCopy = new StatList(x);
                for (Datum el: xCopy) {
                    QuantitativeDatum qd = (QuantitativeDatum)el;
                    if (!count.containsKey(qd)) {
                        count.put(qd, 1);
                    } else {
                        count.put(qd, count.get(qd) + 1);
                    }
                }
                int max = 0;
                for (QuantitativeDatum el : count.keySet()) {
                    if (count.get(el) > max)
                        max = count.get(el);
                }
                StatList modes = new StatList();
                for (QuantitativeDatum el : count.keySet()) {
                    if (count.get(el) == max) {
                        modes.add(el);
                    }
                }
                Collections.sort(modes);
                if (SCI.console)
                    System.out.println(modes);
                SCI.res = new CommandResult(modes);
            }
        };
        Command modeCategorical = new Command("analysis", "\\mode", "\\mode <list_name> <index>", "Prints the mode of categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to find the mode of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\mode` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                HashMap<QuantitativeDatum, Integer> count = new HashMap<>();
                for (Datum el: xCopy) {
                    QuantitativeDatum qd = (QuantitativeDatum)el;
                    if (!count.containsKey(qd)) {
                        count.put(qd, 1);
                    } else {
                        count.put(qd, count.get(qd) + 1);
                    }
                }
                int max = 0;
                for (QuantitativeDatum el : count.keySet()) {
                    if (count.get(el) > max)
                        max = count.get(el);
                }
                StatList modes = new StatList();
                for (QuantitativeDatum el : count.keySet()) {
                    if (count.get(el) == max) {
                        modes.add(el);
                    }
                }
                Collections.sort(modes);
                if (SCI.console)
                    System.out.println(modes);
                SCI.res = new CommandResult(modes);
            }
        };
        Command usigma = new Command("analysis", "usigma", "usigma <list_name>", "Prints the sum of elements in <list_name>.", new String[][] {new String[] {"list_name", "The name of the list to find the sum of elements of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 1) {
                    SCI.error("`usigma` takes exactly one argument.");
                    return;
                }
                StatList x = SCI.quantitative.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                BigDecimal sum = BigDecimal.ZERO;
                for (Datum el : x) {
                    BigDecimal value = ((QuantitativeDatum)el).getValue();
                    sum = sum.add(value, mc);
                }
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
        Command usigmaCategorical = new Command("analysis", "\\usigma", "\\usigma <list_name> <index>", "Prints the sum of elements in categorical list <list_name>'s <index>th elements.", 
                "Indices start at 1.", new String[][] {new String[] {"list_name", "The name of the list to find the sum of elements of."}}) { 
            protected void run() {
                if (Command.getArgs().size() != 2) {
                    SCI.error("`\\usigma` takes exactly two arguments.");
                    return;
                }
                StatList x = SCI.categorical.get(Command.getArgs().get(0));
                if (x == null) {
                    SCI.error("List \"" + Command.getArgs().get(0) + "\" does not exist.");
                    return;
                }
                int index;
                try {
                    index = Integer.parseInt(Command.getArgs().get(1)) - 1;
                } catch (Exception e) {
                    SCI.error("Index \"" + Command.getArgs().get(1) + "\" is invalid.");
                    return;
                }
                StatList xCopy = new StatList();
                for (Datum el : x) {
                    xCopy.add(new QuantitativeDatum(((CategoricalUnit)el).getQuantValue(index)));
                }
                
                BigDecimal sum = BigDecimal.ZERO;
                for (Datum el : xCopy) {
                    BigDecimal value = ((QuantitativeDatum)el).getValue();
                    sum = sum.add(value, mc);
                }
                if (SCI.console)
                    System.out.println(sum);
                SCI.res = new CommandResult(sum);
            }
        };
    }
}