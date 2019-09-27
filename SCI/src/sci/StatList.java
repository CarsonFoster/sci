package sci;

import java.util.ArrayList;

/**
 *
 * @author fostecar000
 */
public class StatList extends ArrayList<Datum> {
    public String toString() {
        String out = "";
        for (int i = 0; i < size(); i++) {
            out += get(i);
            if (i < size() - 1) out += " ";
        }
        return out;
    }
    
    protected static StatList parseCategorical(String line) {
        String[] strings = line.split("\\)");
        StatList cat = new StatList();
        for (int i = 0; i < strings.length; i++) {
            cat.add(new CategoricalUnit(strings[i].substring(i > 0 ? 2 : 1).split(" ")));
        }
        return cat;
    }
    
    protected static StatList parseQuantitative(String line) {
        String[] strNums = line.split(" ");
        StatList nums = new StatList();
        for (int i = 0; i < strNums.length; i++) {
            try {
                nums.add(new QuantitativeDatum(Double.parseDouble(strNums[i])));
            } catch (Exception e) {
                SCI.error("\"" + strNums[i] + "\" is not quantitative.");
                return null;
            }
        }
        return nums;
    }
    
    protected static boolean checkListName(String name) { // true if name not taken
        return !(SCI.categorical.containsKey(name) || SCI.quantitative.containsKey(name));
    }
} 
