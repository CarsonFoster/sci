package sci;

import java.util.ArrayList;

/**
 *
 * @author Carson Foster
 */
public class HelpMessage {
    private String module, name, usage, description;
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
    
    public String toString() {
        String out = "Usage: " + usage + "\n";
        out += description;
        if (flags.size() > 0)
            out += "\n\n";
        for (String[] flag : flags) {
            out += "    " + flag[0];
            out += "\t\t\t" + flag[1] + "\n";
        }
        return out;
    }
    
    public String getModule() {
        return module;
    }
    
    public String getName() {
        return name;
    }
}
