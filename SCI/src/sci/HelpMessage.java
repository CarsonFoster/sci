package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */
public class HelpMessage {
    private String module, name, usage, description;
    private String extended = null;
    private ArrayList<String[]> flags = new ArrayList<>();
    
    public HelpMessage(String module, String name, String usage, String description, String[][] flag_info) {
        this.module = module;
        this.name = name;
        this.usage = usage;
        this.description = description;
        if (flag_info != null) {
            for (String[] flag : flag_info) {
                flags.add(flag);
            }
        }
    }
    
    public HelpMessage(String module, String name, String usage, String description, String extended_description, String[][] flag_info) {
        this(module, name, usage, description, flag_info);
        extended = extended_description;
    }
    
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
    
    public String getModule() {
        return module;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
