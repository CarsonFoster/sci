package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */
public class HelpMessage {
    private String module, name, usage, description; // the strings representing the help message's associated module, command name, command usage, and short description
    private String extended = null; // extended description that not all commands use; explicitly defaults to null
    private ArrayList<String[]> flags = new ArrayList<>(); // the arguments or flags that the associated command takes
    
    // more simple constructor; flag_info is an array of 2 element arrays, representing the flag/argument and the explanation
    public HelpMessage(String module, String name, String usage, String description, String[][] flag_info) {
        this.module = module;
        this.name = name;
        this.usage = usage;
        this.description = description;
        if (flag_info != null) { // sometimes there are no flags
            for (String[] flag : flag_info) {
                flags.add(flag);
            }
        }
    }
    
    // allows for an extended description
    public HelpMessage(String module, String name, String usage, String description, String extended_description, String[][] flag_info) {
        this(module, name, usage, description, flag_info);
        extended = extended_description;
    }
    
    // of the form:
    // Usage: <usage string here>
    // <description here>
    // <extended description here if applicable | or first flag, 3 tabs, explanation if applicable>
    // <rest of flags, if applicable>
    public String toString() {
        String out = "";
        if (usage != null)
            out += "Usage: " + usage + "\n";
        out += description;
        if (extended != null) {
            out += "\n" + extended;
        }
        if (flags.size() > 0)
            out += "\n";
        for (String[] flag : flags) {
            out += "\n" + flag[0];
            out += "\t\t\t" + flag[1];
        }
        return out;
    }
    
    public String getModule() { // returns the associated module
        return module;
    }
    
    public String getName() { // returns the name of the associated command
        return name;
    }
    
    public String getDescription() { // returns the description of the command
        return description;
    }
}