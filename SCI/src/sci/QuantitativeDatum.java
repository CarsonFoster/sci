package sci;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * @author Carson Foster
 */
public class QuantitativeDatum extends Datum {
    private BigDecimal value;
    
    public QuantitativeDatum() {
        value = new BigDecimal("0.0");
    }
    
    public QuantitativeDatum(BigDecimal x) {
        value = x;
    }
    
    public QuantitativeDatum(String x) {
        value = new BigDecimal(x);
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void setValue(String x) {
        value = new BigDecimal(x);
    }
    
    public String toString() {
        return value.toString();
    }
    
    public int compareTo(Object o) {
        QuantitativeDatum x = (QuantitativeDatum)o;
        return value.subtract(x.getValue()).signum();
    }
}
