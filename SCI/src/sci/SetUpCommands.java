package sci;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    public static void main() {
        // put all creation of commands and help messages here
        HelpMessage core = new HelpMessage("core", "help_msg", null, "core: The main module, from which you can access the other modules.", null);
        SCI.addHelp(core);
        HelpMessage ober = new HelpMessage("ober", "help_msg", null, "ober: Commands in this module can be accessed from anywhere.", null);
        SCI.addHelp(ober);
        
        Command exit = new Command("ober", "exit", "exit", "Quits the program.", null) {
            protected void run() {
                System.exit(0);
            }
        };
        
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
                if (Command.getArgs().size() == 0) {
                    helpModule(SCI.module);
                    return;
                } 
                String command = Command.getArgs().get(0);
                if (SCI.help.keySet().contains(command)) {
                    helpModule(command);
                    return;
                }
                if (SCI.help.get("ober").containsKey(command)) {
                    System.out.println(SCI.help.get("ober").get(command));
                } else if (SCI.help.get(SCI.module).containsKey(command)) {
                    System.out.println(SCI.help.get(SCI.module).get(command));
                } else {
                    SCI.error("Help for " + command + " not found.");
                }
            }
        };
        
        Command pwm = new Command("ober", "pwm", "pwm", "Prints the current module.", null) {
            protected void run() {
                System.out.println(SCI.module);
            }
        };
        
        Command back = new Command("ober", "back", "back", "Returns to the core module.", null) {
            protected void run() {
                if (SCI.module.equals("core")) {
                    SCI.error("The current module is already core.");
                    return;
                }
                SCI.module = "core";
            }
        };
    }
}
