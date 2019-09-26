package sci;

/**
 *
 * @author Carson Foster
 */
public class QuantitativeDatum extends Datum {
    private double value;
    
    public QuantitativeDatum() {
        value = 0.0;
    }
    
    public QuantitativeDatum(double x) {
        value = x;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(double x) {
        value = x;
    }
    
    public String toString() {
        return Double.toString(value);
    }
}
