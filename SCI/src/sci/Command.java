package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */

public class Command {
    private String module, name; // the module this command works in and its name
    private HelpMessage help;
    
    protected static ArrayList<String> getArgs() {
        return SCI.args;
    }
    
    protected static String getEnv(String key) {
        return SCI.env.get(key);
    }
    
    protected static void setEnv(String key, String value) {
        SCI.env.put(key, value);
    }
    
    protected Command(String module, String name) {
        this.module = module;
        this.name = name;
        SCI.addCommand(this);
    }
    
    protected Command(String module, String name, String usage, String description, String[][] flag_info) {
        this.module = module;
        this.name = name;
        help = new HelpMessage(module, name, usage, description, flag_info);
        SCI.addCommand(this);
        SCI.addHelp(help);
    }
    
    protected Command(String module, String name, String usage, String description, String ext, String[][] flag_info) {
        this.module = module;
        this.name = name;
        help = new HelpMessage(module, name, usage, description, ext, flag_info);
        SCI.addCommand(this);
        SCI.addHelp(help);
    }
    
    protected String getModule() {
        return module;
    }
    
    protected String getName() {
        return name;
    }
    
    protected void run() {} // what the command actually does, will be overridden
}
