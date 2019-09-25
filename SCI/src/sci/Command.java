package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */

public class Command {
    private String module, name; // the module this command works in and its name
    private HelpMessage help; // the associated help message for this command
    
    protected static ArrayList<String> getArgs() { // returns the arguments passed to the command, not including the command name
        return SCI.args;
    }
    
    protected static String getEnv(String key) { // returns environment variable with the given name
        return SCI.env.get(key);
    }
    
    protected static void setEnv(String key, String value) { // set the value of the environment variable with the given name
        SCI.env.put(key, value);
    }
    
    protected Command(String module, String name) { // simple constructor with only module and name required
        this.module = module;
        this.name = name;
        SCI.addCommand(this);
    }
    
    protected Command(String module, String name, String usage, String description, String[][] flag_info) { // simple constructor with help message functionality
        this.module = module;
        this.name = name;
        help = new HelpMessage(module, name, usage, description, flag_info);
        SCI.addCommand(this);
        SCI.addHelp(help);
    }
    
    protected Command(String module, String name, String usage, String description, String ext, String[][] flag_info) { // supports help messages with extended descriptions
        this.module = module;
        this.name = name;
        help = new HelpMessage(module, name, usage, description, ext, flag_info);
        SCI.addCommand(this);
        SCI.addHelp(help);
    }
    
    protected String getModule() { // returns the module name
        return module;
    }
    
    protected String getName() { // returns the name of the command
        return name;
    }
    
    protected void run() {} // what the command actually does, will be overridden in object instantiation
}
