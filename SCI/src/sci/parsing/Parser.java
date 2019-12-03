package sci.parsing;

import java.util.ArrayList;
import java.util.List;

/* Grammar:
expression = expression BINARY_OP expression | UNARY_OP expression | LPAREN expression RPAREN | number | command
number = integer | decimal
integer = [0-9]+(?!\.)
decimal = [0-9]+\.[0-9]+
command = [STRING, number]+
*/


// Operators: (BI) +-*/%^ (U)-
// Precedence: Paren;-(u);^;*/%;+-

/* Grammar 2.0
unit = number | unary number | command
factor = unit | lparen expression rparen
exponent_group = factor | factor ^ exponent_group
term = exponent_group | term [/%*] exponent_group
expression = term | expression [+-] term
*/

public class Parser {
    private List<Token> tokens;
    private int index = 0;
    
    public Parser(List<Token> t) {
        tokens = t;
    }
    
    private boolean test(TokenType tt) {
        if (tokens.get(index).getType() == tt) {
            index++;
            return true;
        }
        return false;
    }
    
    public Node parseExpression() throws SyntaxException {
        Node number = parseNumber();
        if (number != null)
            return number;
        Node command = parseCommand();
        if (command != null)
            return command;
        Node lparen = parseType(TokenType.LPAREN);
        if (lparen != null) {
            Node expr = parseExpression();
            if (expr == null) throw new SyntaxException("Expression expected.");
            Node rparen = parseType(TokenType.RPAREN);
            if (rparen == null) throw new SyntaxException("Right parenthesis expected.");
            //TODO
        }
        return null;
    }
    
    private Node parseCommand() {
        Node lastParsed;
        ArrayList<Node> list = new ArrayList<>();
        do {
            lastParsed = parseStringOrNumber();
            if (lastParsed != null)
                list.add(lastParsed);
        } while (lastParsed != null);
        if (list.isEmpty())
            return null;
        Node base = list.get(0);
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).setLeft(list.get(i + 1));
        }
        return base;
    }
    
    private Node parseStringOrNumber() {
        Node number = parseNumber();
        if (number != null)
            return number;
        Node string = parseType(TokenType.STRING);
        if (string != null)
            return string;
        return null;
    }
    
    private Node parseNumber() {
        Node integer = parseType(TokenType.INTEGER);
        if (integer != null)
            return integer;
        Node decimal = parseType(TokenType.DECIMAL);
        if (decimal != null)
            return decimal;
        return null;
    }
    
    private Node parseType(TokenType tt) {
        if (test(tt))
            return new Node(tokens.get(index - 1));
        return null;
    }
}
