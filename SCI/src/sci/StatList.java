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
}
