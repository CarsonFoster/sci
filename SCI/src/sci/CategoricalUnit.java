package sci;

/**
 *
 * @author Carson Foster
 */
public class CategoricalUnit {
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
    
    public double getQuantValue(int index) {
        String value = values[index];
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            SCI.error("\"" + value + "\" is not quantitative.");
            return -1.0;
        }
    }
}
