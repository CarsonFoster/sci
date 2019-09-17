package sci;

/**
 *
 * @author Carson Foster
 */
public class SetUpCommands {
    public static void main() {
        // put all creation of commands and help messages here
        Command exit = new Command("core", "exit") {
            protected void run() {
                System.exit(0);
            }
        };
        SCI.addOberCommand(exit);
    }
}
