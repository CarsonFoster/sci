package sci.parsing;

import java.util.HashMap;
import java.util.function.BiFunction;

public class ParseTree {
    private HashMap<String, BiFunction<Double, Double, Double>> ops = new HashMap<>();
    
    public ParseTree() {
        ops.put("-", (x, y) -> x - y);
        ops.put("+", (x, y) -> x + y);
        ops.put("*", (x, y) -> x * y);
        ops.put("/", (x, y) -> x / y);
        ops.put("%", (x, y) -> x % y); // double quotient problems????
        ops.put("^", (x, y) -> Math.pow(x, y));
        ops.put("-U", (x, y) -> -y);
    }
    
    double parse(Node root) {
        if (root == null) return 0.0;
        TokenType tt = root.getToken().getType();
        String contents = root.getToken().getContents();
        
        if (tt == TokenType.DECIMAL || tt == TokenType.INTEGER) {
            return Double.parseDouble(contents);
        }
        
        if (tt == TokenType.COMMAND) {
            sci.SCI.console = false;
            sci.SCI.runCommand(contents.substring(1, contents.length() - 1)); // ignore <>
        }
        
        if (tt == TokenType.UNARYOP) contents += "U";
        
        return (ops.get(contents).apply(parse(root.getLeft()), parse(root.getRight())));
    }
}
