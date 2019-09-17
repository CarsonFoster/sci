package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */

public class Command {
    private String module, name; // the module this command works in and its name
    
    protected static ArrayList<String> getArgs() {
        return SCI.args;
    }
    
    protected static String getEnv(String key) {
        return SCI.env.get(key);
    }
    
    protected static void setEnv(String key, String value) {
        SCI.env.put(key, value);
    }
    
    protected Command(String module, String name) { // TODO: add HelpMessage support
        this.module = module;
        this.name = name;
        SCI.addCommand(this);
    }
    
    protected String getModule() {
        return module;
    }
    
    protected String getName() {
        return name;
    }
    
    protected void run() {} // what the command actually does, will be overridden
}
