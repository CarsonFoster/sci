package sci;

import java.math.BigDecimal;

/**
 *
 * @author Carson Foster
 */
public class CategoricalUnit extends Datum {
    public String[] values;
    
    public CategoricalUnit(String[] x) {
        values = x;
    }
    
    public String[] getValues() {
        return values;
    }
    
    public String getValue(int index) {
        return values[index];
    }
    
    public void setValue(int index, String value) {
        values[index] = value;
    }
    
    public BigDecimal getQuantValue(int index) {
        String value;
        try {
            value = values[index];
        } catch (Exception e) {
            SCI.error("Index \"" + index + "\" is invalid.");
            return null;
        }
        try {
            Double.parseDouble(value);
            return new BigDecimal(value);
        } catch (Exception e) {
            SCI.error("\"" + value + "\" is not quantitative.");
            return null;
        }
    }
    
    public String toString() {
        String out = "(";
        for (int i = 0; i < values.length; i++){
            out += values[i];
            if (i < values.length - 1) out += " ";
        }
        out += ")";
        return out;
    }
    
    public int compareTo(Object o) {
        return 0;
    }
}
