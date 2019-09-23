package sci;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    public static void main() {
        // put all creation of commands and help messages here
        Command exit = new Command("ober", "exit", "exit", "Quits the program.", null) {
            protected void run() {
                System.exit(0);
            }
        };
        
        Command help = new Command("ober", "help", "help <command>", "Provides helpful information on commands.", null) { // TODO : better define help
            protected void run() {
                String command = Command.getArgs().get(0);
                if (SCI.help.get("ober").containsKey(command)) {
                    System.out.println(SCI.help.get("ober").get(command));
                } else if (SCI.help.get(SCI.module).containsKey(command)) {
                    System.out.println(SCI.help.get(SCI.module).get(command));
                } else {
                    SCI.error("Help for " + command + " not found.");
                }
            }
        };
    }
}
