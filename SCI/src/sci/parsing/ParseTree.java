package sci.parsing;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.function.BiFunction;

public class ParseTree {
    private HashMap<String, BiFunction<BigDecimal, BigDecimal, BigDecimal>> ops = new HashMap<>();
    private static MathContext mc = new MathContext(5);
    private boolean quit = false;
    
    public ParseTree() {
        ops.put("-", (x, y) -> x.subtract(y, mc));
        ops.put("+", (x, y) -> x.add(y, mc));
        ops.put("*", (x, y) -> x.multiply(y, mc));
        ops.put("/", (x, y) -> x.divide(y, mc));
        ops.put("%", (x, y) -> x.remainder(y, mc)); // double quotient problems????
        ops.put("^", (x, y) -> x.pow(y.intValue(), mc));
        ops.put("-U", (x, y) -> y.negate());
    }
    
    public boolean syntax(Node root) {
        TokenType tt = root.getToken().getType();
        
        if (tt == TokenType.INTEGER || tt == TokenType.DECIMAL || tt == TokenType.COMMAND)
            return (root.getLeft() == null && root.getRight() == null);
       
        if (tt == TokenType.UNARYOP)
            return (root.getLeft() == null && root.getRight() != null && syntax(root.getRight()));
        
        // BINARYOP
        return (root.getLeft() != null && root.getRight() != null && syntax(root.getLeft()) && syntax(root.getRight()));
            
    }
    
    public BigDecimal parse(Node root) {
        if (root.getLevel() == 0) quit = false;
        if (quit) return null;
        if (root == null) return BigDecimal.ZERO;
        TokenType tt = root.getToken().getType();
        String contents = root.getToken().getContents();
        
        if (tt == TokenType.DECIMAL || tt == TokenType.INTEGER) {
            return new BigDecimal(contents);
        }
        
        if (tt == TokenType.COMMAND) {
            sci.SCI.console = false;
            sci.SCI.runCommand(contents.substring(1, contents.length() - 1)); // ignore <>
            sci.CommandResult x = sci.SCI.res;
            if (x == null) {
                quit = true;
                return null;
            } else
                return x.getValue();
        }
        
        if (tt == TokenType.UNARYOP) contents += "U";
        
        BigDecimal left = parse(root.getLeft());
        if (quit) return null;
        BigDecimal right = parse(root.getRight());
        if (quit) return null;
        BigDecimal res;
        try {
            res = ops.get(contents).apply(left, right);
        } catch (Exception e) {
            String msg = e.getMessage();
            sci.SCI.error(msg + (msg.endsWith(".") ? "" : "."));
            quit = true;
            return null;
        }
        return res;//(ops.get(contents).apply(parse(root.getLeft()), parse(root.getRight())));
    }
}
