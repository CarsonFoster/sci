package sci;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    public static void main() {
        // put all creation of commands and help messages here
        Command exit = new Command("ober", "exit") {
            protected void run() {
                System.exit(0);
            }
        };
        HelpMessage help_exit = new HelpMessage("ober", "exit", "exit", "Quits the program.", null);
        SCI.addCommand(exit);
    }
}
