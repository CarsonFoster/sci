package sci;

import java.math.BigDecimal;

public class CommandResult {
    private BigDecimal value;
    private StatList list;
    private boolean isValue;
    
    public CommandResult() {
        value = BigDecimal.ZERO;
        isValue = true;
    }
    
    public CommandResult(BigDecimal value) {
        this.value = value;
        isValue = true;
    }
    
    public CommandResult(StatList list) {
        this.list = list;
        isValue = false;
    }
    
    public boolean isValue() {
        return isValue;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public StatList getList() {
        return list;
    }
}
