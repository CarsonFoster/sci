package sci;

import java.math.BigDecimal;
import static java.math.BigDecimal.ROUND_HALF_UP;
import java.math.MathContext;

/**
 *
 * @author Carson Foster
 */
public class QuantitativeDatum extends Datum {
    private BigDecimal value;
    
    // shamelessly stolen from StackOverflow: https://stackoverflow.com/questions/13649703/square-root-of-bigdecimal-in-java
    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        final BigDecimal TWO = new BigDecimal("2");
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);

        }
        return x1;
    }
    
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
